package gs.wallet;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Account extends Activity {
	
	Spinner spinCategory, spinCurrancy;
	EditText etTitle;
	EditText etAlreadyOnAcc;
	
	String category, title, currancy, alreadyOnAcc;
	
	int currConst;
	int[] arrCurrNum;
	String[] arrCurrWord;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account);

		spinCategory = (Spinner) findViewById(R.id.spinCategoryAccount);
		spinCurrancy = (Spinner) findViewById(R.id.spinCurrencyAccount);
		etTitle = (EditText)findViewById(R.id.etTitleAccount);
		etAlreadyOnAcc = (EditText)findViewById(R.id.etAlreadyOnAccount);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.category_account_arrays,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinCategory.setAdapter(adapter);
		spinCategory.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				category = spinCategory.getSelectedItem().toString();
//				Toast.makeText(getBaseContext(),
//						"Position = " + position + " " + str, Toast.LENGTH_SHORT)
//						.show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(getBaseContext(), "Nothing", Toast.LENGTH_SHORT)
						.show();
			}
			
			
		});
		
		adapter = ArrayAdapter.createFromResource(
				this, R.array.currancy_arrays,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinCurrancy.setAdapter(adapter);
		spinCurrancy.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				currancy = spinCurrancy.getSelectedItem().toString();
//				Toast.makeText(getBaseContext(),
//						"Position = " + position + " " + str, Toast.LENGTH_SHORT)
//						.show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> paramAdapterView) {
				// TODO Auto-generated method stub
				
			}

		});
		
		// fill the arrCurrNum of constants for DB 
		Resources res = getResources();
		arrCurrWord = res.getStringArray(R.array.currancy_arrays);
		arrCurrNum = new int[arrCurrWord.length];
		for (int i = 0; i < arrCurrWord.length; i++) {
			arrCurrNum[i] = i+1;
		}
	}

	public void clickAdd(View v) {
		title = etTitle.getText().toString();
		alreadyOnAcc = etAlreadyOnAcc.getText().toString();
		
		currConst = currFindConst(currancy);
		
		// open DB
        DBOpenHelper dbHelper = new DBOpenHelper(this);
        dbHelper.setAccount(category, title, alreadyOnAcc, currConst);
        dbHelper.rawQuery(this);
		
		Toast.makeText(getBaseContext(),
				"Currancy = " +currancy+currConst + "\n" + alreadyOnAcc, Toast.LENGTH_SHORT).show();
		
		Intent intent = new Intent(this, Income.class);
	      startActivity(intent);
	}
	
	public void clickSkip(View v) {
//		DBOpenHelper dbHelper = new DBOpenHelper(this);
//		dbHelper.rawQuery(this);
		
        Intent intent = new Intent(this, Income.class);
	      startActivity(intent);
	}
	
	public int currFindConst(String curr){
		int num = 0;
		for (int i = 0; i < arrCurrWord.length; i++) {
			if (arrCurrWord[i].equals(curr)) {
				num = i+1;
			}
		}		
		return num;		
	}
	
}
