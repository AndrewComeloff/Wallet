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
	
	String category, title;
	
	int currancy;
	int[] arrCurrNum;
//	String[] arrCurrWord;

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
				category = spinCategory.getSelectedItem().toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
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
				currancy = position+1;
			}

			@Override
			public void onNothingSelected(AdapterView<?> paramAdapterView) {
			}

		});
		

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
