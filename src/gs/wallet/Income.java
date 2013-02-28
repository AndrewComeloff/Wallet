package gs.wallet;

import java.util.Calendar;
import java.util.Date;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class Income extends FragmentActivity implements DatePickerDialogFragment.NoticeDialogListener {

	Spinner spinCategory, spinCurrancy, spinAcount, spinHowOften;
	EditText etTitle, etAmountIncome, etDate;

	String category, title, amountIncome, acount, currancy;

	int currConst, howOften;
	int year, month, day, h;
	int[] arrCurrNum;
	String[] arrCurrWord, arrAccount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fix_income);

		spinCategory = (Spinner) findViewById(R.id.spinCategoryIncome);
		spinCurrancy = (Spinner) findViewById(R.id.spinCurrency);
		spinAcount = (Spinner) findViewById(R.id.spinAccount);
		spinHowOften = (Spinner) findViewById(R.id.spinHowOften);
		
		etTitle = (EditText) findViewById(R.id.etTitleAccount);
		etAmountIncome = (EditText) findViewById(R.id.etAmountIncome);
		etDate = (EditText) findViewById(R.id.etDate);
		
		// get accounts from DB
		DBOpenHelper dbHelper = new DBOpenHelper(this);
		arrAccount = dbHelper.selectColumn(this);	
		
		if (arrAccount.length == 0) {
			arrAccount = new String[1];
			arrAccount[0] = "המבאגüעו סקוע"; 
		}
		
		// get the current date
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		// -- spin Category --
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.category_income_arrays,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinCategory.setAdapter(adapter);
		spinCategory.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				category = spinCategory.getSelectedItem().toString();
				if (position == 6) {
					Toast.makeText(getBaseContext(),
							"Position = " + position + " " + category,
							Toast.LENGTH_SHORT).show();
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(getBaseContext(), "Nothing", Toast.LENGTH_SHORT)
						.show();
			}

		});
		
		// -- spin Currancy --
		adapter = ArrayAdapter.createFromResource(this,
				R.array.code_currancy_arrays,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinCurrancy.setAdapter(adapter);
		spinCurrancy.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				currancy = spinCurrancy.getSelectedItem().toString();
				// Toast.makeText(getBaseContext(),
				// "Position = " + position + " " + str, Toast.LENGTH_SHORT)
				// .show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> paramAdapterView) {
				// TODO Auto-generated method stub

			}

		});
		
//		 -- spin Account --
		 ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrAccount);
		 adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 spinAcount.setAdapter(adapter2);
		 spinAcount.setOnItemSelectedListener(new OnItemSelectedListener() {
		
		 @Override
		 public void onItemSelected(AdapterView<?> arg0, View view,
		 int position, long id) {
		 // TODO Auto-generated method stub
		 acount = spinCurrancy.getSelectedItem().toString();
		 // Toast.makeText(getBaseContext(),
		 // "Position = " + position + " " + str, Toast.LENGTH_SHORT)
		 // .show();
		 }
		
		 @Override
		 public void onNothingSelected(AdapterView<?> paramAdapterView) {
		 // TODO Auto-generated method stub
		
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
				// TODO Auto-generated method stub
				howOften = position;
//				Toast.makeText(getBaseContext(), "Position = " + position,
//						Toast.LENGTH_SHORT).show();
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
			arrCurrNum[i] = i + 1;
		}
	}

	public void clickAdd(View v) {
		title = etTitle.getText().toString();
		amountIncome = etAmountIncome.getText().toString();

		currConst = currFindConst(currancy);

		// open DB
		// DBOpenHelper dbHelper = new DBOpenHelper(this);

		// dbHelper.setAccount(category, title, amountIncome, currConst);
		// dbHelper.rawQuery(this);

		Toast.makeText(getBaseContext(),
				"Currancy = " + currancy + currConst + "\n" + amountIncome,
				Toast.LENGTH_SHORT).show();
	}
	
	public void clickDate(View v) {				
		DatePickerDialogFragment dlg = new DatePickerDialogFragment();
		dlg.setNoticeDialogListener(Income.this);
		dlg.setDate(year, month, day);
		dlg.show(getSupportFragmentManager(),
				"DatePickerDialogFragment");
		
		
	}

	public void clickSkip(View v) {
		DBOpenHelper dbHelper = new DBOpenHelper(this);

		dbHelper.rawQuery(this);
	}

	public int currFindConst(String curr) {
		int num = 0;
		for (int i = 0; i < arrCurrWord.length; i++) {
			if (arrCurrWord[i].equals(curr)) {
				num = i + 1;
			}
		}
		return num;
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
		// TODO Auto-generated method stub
		this.year = year;
		this.month = month;
		this.day = day;
		String dayOfWeek = DateFormat.format("EE", new Date(year, month, day)).toString();
		etDate.setText(dayOfWeek + ", " + pad(day) + "." + pad(month + 1) + "." + year);
	}

}
