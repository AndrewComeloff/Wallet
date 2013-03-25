package gs.wallet;

import java.util.Calendar;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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


public class Expense extends FragmentActivity implements DatePickerDialogFragment.NoticeDialogListener {

	Spinner spinCategory, spinAcount, spinHowOften;
	EditText etTitle, etAmountIncome, etCurrency, etDate;

	String category, title, currancy, imageName;

	int currConst, acountID, currancyID, howOften;
	int year, month, day, h;
	int[] arrCurrNum;
	String[] arrHowOften, arrAccount;
	
	ImageView ivIcons;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fix_expense);

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
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.category_expense_arrays,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinCategory.setAdapter(adapter);
		spinCategory.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long id) {
				category = spinCategory.getSelectedItem().toString();
				if (position == 6) {
					Toast.makeText(getBaseContext(),
							"Position = " + position + " " + category,
							Toast.LENGTH_SHORT).show();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {				
				Toast.makeText(getBaseContext(), "Nothing", Toast.LENGTH_SHORT)
						.show();
			}
		});
		
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
				String[] arrAccountID = dbHelper.getColumn(Expense.this,
						DBOpenHelper.TABLE_ACC, DBOpenHelper.ID);				
				
				if (arrAccountID.length > 0) {
					// Get account ID from selected position
					acountID = Integer.parseInt(arrAccountID[position]);
					// Get currancy ID from selected account
					currancyID = Integer.parseInt(dbHelper.getCell(
							Expense.this, DBOpenHelper.TABLE_ACC,
							DBOpenHelper.ACC_CURRENCY, DBOpenHelper.ID,
							acountID));
					// Set currancy
					etCurrency.setText(dbHelper.getCell(Expense.this,
							DBOpenHelper.TABLE_CURR, DBOpenHelper.CURR_NAME,
							DBOpenHelper.ID, currancyID));
				} 
				
				Toast.makeText(getBaseContext(), "Acount = " + acountID,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> paramAdapterView) {

			}
		});
		
		// -- spin How Often --
		adapter = ArrayAdapter.createFromResource(this, R.array.how_often,
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

		// fill the arrCurrency of constants for DB
//		Resources res = getResources();
//		String[] arrCurrency = res.getStringArray(R.array.currancy_arrays);
//		arrCurrNum = new int[arrCurrency.length];
//		for (int i = 0; i < arrCurrency.length; i++) {
//			arrCurrNum[i] = i + 1;
//		}
	}
	
	GridIconsDialogFragment dlg = new GridIconsDialogFragment();
	
	public void clickIcons(View v) {
		dlg.show(getSupportFragmentManager(), "GridIconsDialogFragment");
	}
	
	public void clickIconsDialog(View v) {
		setIcon(v.getId());
		imageName = (String) v.getTag();
//		String imageName = (String) ivIcons.getTag();
//		int intImageName = ivIcons.getResources();
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
		dlg.setNoticeDialogListener(Expense.this);
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
		DBOpenHelper dbHelper = new DBOpenHelper(this);
		dbHelper.rawQuery(this);
		
		// втавляем картинку по имени файла
		String st = "icon_01";
		Resources res = getResources();
		int resID = res.getIdentifier(st , "drawable", getPackageName());
		ivIcons.setImageResource(resID);
	}
	
	public void clickAdd(View v) {
		title = etTitle.getText().toString();
		float amount = Float.parseFloat(etAmountIncome.getText().toString());

		DBOpenHelper dbHelper = new DBOpenHelper(this);
//		dbHelper.setExpense(category, imageName, title, amount, acountID, howOften, (pad(day) + "." + pad(month + 1) + "." + year));
		
//		Intent intent = new Intent(this, Expense.class);
//	    startActivity(intent);

	}
}
