package gs.wallet;

import gs.wallet.CategoryDialogFragment.DialogListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Account extends FragmentActivity implements DialogListener {
	
	Spinner spinCategory, spinCurrancy;
	EditText etTitle, etAlreadyOnAcc;
	
	String category, title;
	
	int currancy;
	int[] arrCurrNum;
	String[] arrCategory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account);

		spinCategory = (Spinner) findViewById(R.id.spinCategoryAccount);
		spinCurrancy = (Spinner) findViewById(R.id.spinCurrencyAccount);
		etTitle = (EditText)findViewById(R.id.etTitleAccount);
		etAlreadyOnAcc = (EditText)findViewById(R.id.etAlreadyOnAccount);
		
		// get category of accounts from DB
		final DBOpenHelper dbHelper = new DBOpenHelper(this);
		arrCategory = dbHelper.selectColumn(this, DBOpenHelper.TABLE_CAT_ACC, DBOpenHelper.CAT_ACC_NAME);	
		
		spinCategory(arrCategory);
		

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
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.currancy_arrays,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinCurrancy.setAdapter(adapter);
		spinCurrancy.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long id) {
				currancy = position+1;
			}

			@Override
			public void onNothingSelected(AdapterView<?> paramAdapterView) {
			}
		});
	}
	
	private void spinCategory(String[] array) {
		if (array == null) {
			array = new String[1];
			array[0]="Add Category";
			arrCategory = array.clone();
		} 
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, array);
		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinCategory.setAdapter(adapter);
		spinCategory.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long id) {
				category = spinCategory.getSelectedItem().toString();
				dlgCategory(position);				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				Toast.makeText(getBaseContext(), "Nothing", Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

	CategoryDialogFragment dlg = new CategoryDialogFragment();
	public void dlgCategory(int position){
		if (arrCategory.length == position+1) {			
		    dlg.show(getSupportFragmentManager(), "CategoryDialogFragment");
		}		
	}
	
	public void clickDlgAdd(View v) {
		category = dlg.getCategory();
//		String[] array = new String[arrCategory.length+1]; 
//		array = arrCategory.clone();
//		array[array.length]=category;
		
		Toast.makeText(getBaseContext(), "press ADD - " + category, Toast.LENGTH_SHORT).show();
		dlg.dismiss();
	}
	public void clickDlgCancel(View v) {		
		Toast.makeText(getBaseContext(), "press Cancel - " + category, Toast.LENGTH_SHORT).show();
		dlg.dismiss();
	}	
	
	public void clickSkip(View v) {		
        Intent intent = new Intent(this, Income.class);
	      startActivity(intent);
	}
	public void clickAdd(View v) {
		title = etTitle.getText().toString();
		float alreadyOnAcc = Float.parseFloat(etAlreadyOnAcc.getText().toString());
		
		// open DB
        DBOpenHelper dbHelper = new DBOpenHelper(this);
        dbHelper.setAccount(category, title, alreadyOnAcc, currancy);
        dbHelper.rawQuery(this);
		
		Toast.makeText(getBaseContext(),
				"Currancy = " +currancy + "\n" + alreadyOnAcc, Toast.LENGTH_SHORT).show();
		
		Intent intent = new Intent(this, Income.class);
	      startActivity(intent);
	}		
}
