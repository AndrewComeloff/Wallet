package gs.wallet;

import gs.wallet.DialogCategory.DialogListener;
import gs.wallet.DialogCategoryEdit.DialogEditListener;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Account extends FragmentActivity implements DialogListener, DialogEditListener {
	
	Spinner spinCategory, spinCurrency;
	EditText etTitle, etAlreadyOnAcc;
	
	String category, title;
	
	int currency; 
	int posSpinCat = 0;
	int[] arrCurrNum;
	String[] arrCategory;
	Resources res = getResources();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account);

		spinCategory = (Spinner) findViewById(R.id.spinCategoryAccount);
		spinCurrency = (Spinner) findViewById(R.id.spinCurrencyAccount);
		etTitle = (EditText)findViewById(R.id.etTitleAccount);
		etAlreadyOnAcc = (EditText)findViewById(R.id.etAlreadyOnAccount);
		
		
		
//		// get category of accounts from DB
//		final DBOpenHelper dbHelper = new DBOpenHelper(this);
//		arrCategory = dbHelper.selectColumn(this, DBOpenHelper.TABLE_CAT_ACC, DBOpenHelper.CAT_ACC_NAME);		
		
		spinCategory(posSpinCat);
		

//		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
//				this, R.array.category_account_arrays,
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
//				dlgCategory(position);
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> arg0) {
//				Toast.makeText(getBaseContext(), "Nothing", Toast.LENGTH_SHORT)
//						.show();
//			}
//			
//			
//		});
		
		// -- spin Currency --
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.currency_arrays,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinCurrency.setAdapter(adapter);
		spinCurrency.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long id) {
				currency = position+1;
			}

			@Override
			public void onNothingSelected(AdapterView<?> paramAdapterView) {
			}
		});
	}
	
	private void spinCategory(int position) {
		// get category of accounts from DB
		final DBOpenHelper dbHelper = new DBOpenHelper(this);
		arrCategory = dbHelper.getColumn(this, DBOpenHelper.TABLE_CAT_ACC, DBOpenHelper.CATEGORY);
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
	        dbHelper.setCategory(DBOpenHelper.TABLE_CAT_ACC, category);
			
			Toast.makeText(getBaseContext(), "press ADD - " + category, Toast.LENGTH_SHORT).show();
			dlgCat.dismiss();
			posSpinCat = arrCategory.length-1;
			spinCategory(posSpinCat);
		} else {
			Toast.makeText(getBaseContext(), res.getString(R.string.add_name), Toast.LENGTH_SHORT).show();
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
		String[] arrCategoryID = dbHelper.getColumn(this, DBOpenHelper.TABLE_CAT_ACC, DBOpenHelper.ID);
		dbHelper.deleteRow(DBOpenHelper.TABLE_CAT_ACC, Integer.parseInt(arrCategoryID[posSpinCat]));
		Toast.makeText(getBaseContext(), "deleted category " + category, Toast.LENGTH_SHORT).show();
		dlgCatEdit.dismiss();
		spinCategory(posSpinCat-1);
	}
	public void clickDlgChange(View v) {
		category = dlgCatEdit.getCategory();
		if (!category.equals("")) {
			DBOpenHelper dbHelper = new DBOpenHelper(this);
			String[] arrCategoryID = dbHelper.getColumn(this, DBOpenHelper.TABLE_CAT_ACC, DBOpenHelper.ID);
			dbHelper.editCell(DBOpenHelper.TABLE_CAT_ACC, DBOpenHelper.CATEGORY, 
					Integer.parseInt(arrCategoryID[posSpinCat]), category);
			dlgCatEdit.dismiss();
			spinCategory(posSpinCat);
		}else {
			Toast.makeText(getBaseContext(), res.getString(R.string.add_name), Toast.LENGTH_SHORT).show();
		}		
	}
	
	public void clickSkip(View v) {
        Intent intent = new Intent(this, Income.class);
	      startActivity(intent);
	}
	public void clickAdd(View v) {
		title = etTitle.getText().toString();
		String strAlreadyOnAcc = etAlreadyOnAcc.getText().toString();
		if (strAlreadyOnAcc.equals("")) {
			strAlreadyOnAcc = "0";
		}
		float alreadyOnAcc = Float.parseFloat(strAlreadyOnAcc);
		
		// open DB
        DBOpenHelper dbHelper = new DBOpenHelper(this);
        String[] arrCategoryID = dbHelper.getColumn(this, DBOpenHelper.TABLE_CAT_ACC, DBOpenHelper.ID);
        dbHelper.setAccount(Integer.parseInt(arrCategoryID[posSpinCat]), title, alreadyOnAcc, currency);
        dbHelper.rawQuery(this);
		
		Toast.makeText(getBaseContext(),
				"Currency = " +currency + "\n" + alreadyOnAcc, Toast.LENGTH_SHORT).show();
		
		Intent intent = new Intent(this, Income.class);
	      startActivity(intent);
	}		
}
