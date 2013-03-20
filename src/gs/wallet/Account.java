package gs.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Account extends FragmentActivity implements CategoryDialogFragment.NoticeDialogListener{
	
	Spinner spinCategory, spinCurrancy;
	EditText etCategory, etTitle, etAlreadyOnAcc;
	
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
//		etCategory = (EditText)findViewById(R.id.etCategory);
		etTitle = (EditText)findViewById(R.id.etTitleAccount);
		etAlreadyOnAcc = (EditText)findViewById(R.id.etAlreadyOnAccount);
		
		if (arrCategory != null) {
			spinCategory(arrCategory);
			Toast.makeText(getBaseContext(), "array not null" , Toast.LENGTH_SHORT).show();
		} else {
			arrCategory = new String[1];
			arrCategory[0]="Add Category";
			spinCategory(arrCategory);
			Toast.makeText(getBaseContext(), "array null" , Toast.LENGTH_SHORT).show();
		}
		

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

	public void dlgCategory(int position){
		if (arrCategory.length == position+1) {
			CategoryDialogFragment dlg = new CategoryDialogFragment();
		    dlg.show(getSupportFragmentManager(), "CategoryDialogFragment");
		}		
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

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
//		LayoutInflater inflater = getLayoutInflater();
//		View v = inflater.inflate(R.layout.dlg_category, null);
// 	   	etCategory = (EditText)v.findViewById(R.id.etCategory);
//		category = etCategory.getText().toString();
		
		String[] array = new String[arrCategory.length+1]; 
		array = arrCategory.clone();
		array[array.length]=category;
		Toast.makeText(getBaseContext(), "press OK", Toast.LENGTH_SHORT).show();
	}

		
}
