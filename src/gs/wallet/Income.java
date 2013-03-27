package gs.wallet;

import java.util.Calendar;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class Income extends FragmentActivity implements DatePickerDialogFragment.NoticeDialogListener {

	Spinner spinCategory, spinAcount, spinHowOften;
	EditText etTitle, etAmountIncome, etCurrency, etDate;

	String category, title, currency, imageName;

	int currConst, acountID, currencyID, howOften;
	int year, month, day, h;
	int posSpinCat = 0;
	int[] arrCurrNum;
	String[] arrCategory, arrHowOften, arrAccount;
	
	ImageView ivIcons;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fix_income);

		spinCategory = (Spinner) findViewById(R.id.spinCategoryIncome);
		spinAcount = (Spinner) findViewById(R.id.spinAccount);
		spinHowOften = (Spinner) findViewById(R.id.spinHowOften);
		
		etTitle = (EditText) findViewById(R.id.etTitleIncome);
		etAmountIncome = (EditText) findViewById(R.id.etAmountIncome);
		etCurrency = (EditText) findViewById(R.id.etCurrency);
		etDate = (EditText) findViewById(R.id.etDate);
		
		ivIcons = (ImageView)findViewById(R.id.ivIcons);
		
		Resources res = getResources();

		// get title of accounts from DB
		final DBOpenHelper dbHelper = new DBOpenHelper(this);
		arrAccount = dbHelper.getColumn(this, DBOpenHelper.TABLE_ACC, DBOpenHelper.ACC_TITLE );	
		
		if (arrAccount.length == 0) {
			arrAccount = new String[1];
			arrAccount[0] = res.getString(R.string.add_account); 
		}
		
		// get the current date
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		// -- spin Category --
		spinCategory(posSpinCat);
//		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
//				this, R.array.category_income_arrays,
//				android.R.layout.simple_spinner_item);
//		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//		spinCategory.setAdapter(adapter);
//		spinCategory.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> arg0, View view,
//					int position, long id) {
//				category = spinCategory.getSelectedItem().toString();
//				if (position == 6) {
//					Toast.makeText(getBaseContext(),
//							"Position = " + position + " " + category,
//							Toast.LENGTH_SHORT).show();
//				}
//			}
//			@Override
//			public void onNothingSelected(AdapterView<?> arg0) {				
//				Toast.makeText(getBaseContext(), "Nothing", Toast.LENGTH_SHORT)
//						.show();
//			}
//		});
		
		// -- spin Account --
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arrAccount);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinAcount.setAdapter(adapter2);
		spinAcount.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long id) {
				// Get array with acc_id
				String[] arrAccountID = dbHelper.getColumn(Income.this,
						DBOpenHelper.TABLE_ACC, DBOpenHelper.ID);				
				
				if (arrAccountID.length > 0) {
					// Get account ID from selected position
					acountID = Integer.parseInt(arrAccountID[position]);
					// Get currency ID from selected account
					currencyID = Integer.parseInt(dbHelper.getCell(
							Income.this, DBOpenHelper.TABLE_ACC,
							DBOpenHelper.ACC_CURRENCY, acountID));
					// Set currency
					etCurrency.setText(dbHelper.getCell(Income.this,
							DBOpenHelper.TABLE_CURR, DBOpenHelper.CURR_NAME, currencyID));
				} 
				
				Toast.makeText(getBaseContext(), "Acount = " + acountID,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> paramAdapterView) {

			}
		});
		
		// -- spin How Often --
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.how_often,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinHowOften.setAdapter(adapter);
		spinHowOften.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long id) {
				howOften = position + 1;
			}

			@Override
			public void onNothingSelected(AdapterView<?> paramAdapterView) {

			}
		});
	}
	
	private void spinCategory(int position) {
		// get category of accounts from DB
		final DBOpenHelper dbHelper = new DBOpenHelper(this);
		arrCategory = dbHelper.getColumn(this, DBOpenHelper.TABLE_CAT_INC, DBOpenHelper.CATEGORY);
		// add to array new field
		String[] arrCatClone = new String[arrCategory.length+1];
		for (int i = 0; i < arrCategory.length; i++) {
			arrCatClone[i]=arrCategory[i];
		}
		arrCatClone[arrCatClone.length-1]="Add Category";
		arrCategory = arrCatClone.clone();
		// create spin
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arrCategory);
		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);		

		spinCategory.setAdapter(adapter);
		spinCategory.setSelection(position);
		spinCategory.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long id) {
				category = spinCategory.getSelectedItem().toString();
				posSpinCat = position;
				
				dlgCategory(position);				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				Toast.makeText(getBaseContext(), "Nothing", Toast.LENGTH_SHORT)
						.show();
			}
		});
	}
	
	DialogCategory dlgCat = new DialogCategory();
	public void dlgCategory(int position){
		if (arrCategory.length-1 == position) {			
		    dlgCat.show(getSupportFragmentManager(), "DialogCategory");
		}		
	}		
	
	public void clickDlgAdd(View v) {
		category = dlgCat.getCategory();
		if (!category.equals("")) {
			// open DB and set new category
	        DBOpenHelper dbHelper = new DBOpenHelper(this);
	        dbHelper.setCategory(DBOpenHelper.TABLE_CAT_INC, category);
			
			Toast.makeText(getBaseContext(), "press ADD - " + category, Toast.LENGTH_SHORT).show();
			dlgCat.dismiss();
			posSpinCat = arrCategory.length-1;
			spinCategory(posSpinCat);
		} else {
			Toast.makeText(getBaseContext(), "please input name a new category", Toast.LENGTH_SHORT).show();
		}
		
	}
	public void clickDlgCancel(View v) {
		Toast.makeText(getBaseContext(), "press Cancel - " + category, Toast.LENGTH_SHORT).show();
		dlgCat.dismiss();
		spinCategory(0);
	}
	
	DialogCategoryEdit dlgCatEdit = new DialogCategoryEdit();
	public void clickEditCategory(View v) {	
		dlgCatEdit.setCategory(category);
		dlgCatEdit.show(getSupportFragmentManager(), "DialogCategoryEdit");
		Toast.makeText(getBaseContext(), "press edit category", Toast.LENGTH_SHORT).show();
	}
	
	public void clickDlgDel(View v) {
		DBOpenHelper dbHelper = new DBOpenHelper(this);
		String[] arrCategoryID = dbHelper.getColumn(this, DBOpenHelper.TABLE_CAT_INC, DBOpenHelper.ID);
		dbHelper.deleteRow(DBOpenHelper.TABLE_CAT_INC, Integer.parseInt(arrCategoryID[posSpinCat]));
		Toast.makeText(getBaseContext(), "deleted category " + category, Toast.LENGTH_SHORT).show();
		dlgCatEdit.dismiss();
		spinCategory(posSpinCat-1);
	}
	public void clickDlgChange(View v) {
		category = dlgCatEdit.getCategory();
		DBOpenHelper dbHelper = new DBOpenHelper(this);
		String[] arrCategoryID = dbHelper.getColumn(this, DBOpenHelper.TABLE_CAT_INC, DBOpenHelper.ID);
		dbHelper.editCell(DBOpenHelper.TABLE_CAT_INC, DBOpenHelper.CATEGORY, Integer.parseInt(arrCategoryID[posSpinCat]), category);
		Toast.makeText(getBaseContext(), "changed category " + category, Toast.LENGTH_SHORT).show();
		dlgCatEdit.dismiss();
		spinCategory(posSpinCat);
	}
	
	DialogGridIcons dlg = new DialogGridIcons();
	
	public void clickIcons(View v) {
		dlg.show(getSupportFragmentManager(), "GridIconsDialogFragment");
	}
	
	public void clickIconsDialog(View v) {
		setIcon(v.getId());
		imageName = (String) v.getTag();
		Toast.makeText(this, "image name = " + imageName, Toast.LENGTH_SHORT).show();
	}
	
	private void setIcon(int getId){
		final int ID_IMAGE = R.drawable.icon_01;
		final int ID_VIEW = R.id.ivIcon_01;
		int idImage = getId - ID_VIEW;
		ivIcons.setImageResource(ID_IMAGE + idImage);		
		dlg.dismiss();
	}
	
	public void clickDate(View v) {				
		DatePickerDialogFragment dlg = new DatePickerDialogFragment();
		dlg.setNoticeDialogListener(Income.this);
		dlg.setDate(year, month, day);
		dlg.show(getSupportFragmentManager(),
				"DatePickerDialogFragment");		
	}
	
	// add 0 before ten-digit number 
	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	@Override
	public void onDate(int year, int month, int day) {
		this.year = year;
		this.month = month;
		this.day = day;

		// get day of week
		Calendar cal = Calendar.getInstance();		
		cal.set(year, month, day);		
		String dayOfWeek = DateFormat.format("EEEE", cal).toString();
		String strMonth = DateFormat.format("MMMM", cal).toString();

		etDate.setText(dayOfWeek + ", " + pad(day) + " " + strMonth + " " + year);
	}
	
	public void clickSkip(View v) {
//		DBOpenHelper dbHelper = new DBOpenHelper(this);
//		dbHelper.rawQuery(this);
		
		// втавляем картинку по имени файла
//		String st = "icon_01";
//		Resources res = getResources();
//		int resID = res.getIdentifier(st , "drawable", getPackageName());
//		ivIcons.setImageResource(resID);
		
		Intent intent = new Intent(this, Expense.class);
	    startActivity(intent);
	}
	
	public void clickAdd(View v) {
		title = etTitle.getText().toString();
		String strAmount = etAmountIncome.getText().toString();
		if (strAmount.equals("")) {
			strAmount = "0";
		}
		float amount = Float.parseFloat(strAmount);

		DBOpenHelper dbHelper = new DBOpenHelper(this);
		String[] arrCategoryID = dbHelper.getColumn(this, DBOpenHelper.TABLE_CAT_ACC, DBOpenHelper.ID);
		dbHelper.setIncome(Integer.parseInt(arrCategoryID[posSpinCat]), imageName, title, amount, acountID, howOften, (pad(day) + "." + pad(month + 1) + "." + year));
		
		Intent intent = new Intent(this, Expense.class);
	    startActivity(intent);
	}
}
