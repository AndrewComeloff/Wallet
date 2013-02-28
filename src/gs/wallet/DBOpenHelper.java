package gs.wallet;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DBOpenHelper extends SQLiteOpenHelper {
	
	private static final String DB_NAME = "walletDB";
	private static final int DB_VERSION = 1;
	
	public static final String TABLE_CURR = "currancy";
	public static final String CURR_ID = "_id";
	public static final String CURR_NAME = "name_curr";
	public static final String CURR_RATE = "rate_curr";
	
	public static final String TABLE_ACC = "account";
	public static final String ACC_ID = "_id";
	public static final String ACC_CATEGORY = "category";
	public static final String ACC_TITLE = "title";
	public static final String ACC_ALREADY = "amount";
	public static final String ACC_CURRENCY = "currency_id";
	
	public static final String TABLE_INC = "income";
	public static final String INC_ID = "_id";
	public static final String INC_CATEGORY = "category";
	public static final String INC_TITLE = "title";
	public static final String INC_AMOUNT = "amount";
	public static final String INC_CURRENCY = "currency_id";
	public static final String INC_ACCOUNT = "account_id";
	public static final String INC_HOW_OFTEN = "how_often";
	public static final String INC_DATE = "date";
	
	final String LOG_TAG = "myLogs";
	
	private SQLiteDatabase database;

	String[] strArrCode;
	
	public DBOpenHelper(Context context) {		
		super(context, DB_NAME, null, DB_VERSION);
//		this.context = context;
		Resources res = context.getResources();
		strArrCode = res.getStringArray(R.array.code_currancy_arrays);
	}
	


	@Override
	public void onCreate(SQLiteDatabase db) {		
		// DB Currancy
		db.execSQL("CREATE TABLE " + TABLE_CURR + " (" +
		        CURR_ID + " INTEGER PRIMARY KEY , " +
		        CURR_NAME + " TEXT, " +
		        CURR_RATE + " FLOAT)");
		// DB Account
		db.execSQL("CREATE TABLE "+ TABLE_ACC + " (" +
				ACC_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
				ACC_CATEGORY + " TEXT, " + 
				ACC_TITLE + " TEXT, " + 
				ACC_ALREADY + " TEXT, " + 
				ACC_CURRENCY + " INTEGER NOT NULL ,FOREIGN KEY (" + 
				ACC_CURRENCY + ") REFERENCES " +
				TABLE_CURR + " (" + CURR_ID + "));");
		// DB Income
		db.execSQL("CREATE TABLE "+ TABLE_INC + " (" +
				INC_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
				INC_CATEGORY + " TEXT, " + 
				INC_TITLE + " TEXT, " + 
				INC_AMOUNT + " TEXT, " + 
				INC_HOW_OFTEN + " TEXT, " +
				INC_DATE + " TEXT, " +
				INC_CURRENCY + " INTEGER, " +
				INC_ACCOUNT + " INTEGER)");
		
		ContentValues values = new ContentValues();
		for (int i = 0; i < strArrCode.length; i++) {
			values.put(CURR_NAME, strArrCode[i]);
			db.insert(TABLE_CURR, null, values);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURR);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACC);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_INC);
		
		onCreate(db);
	}

	
	/**
	 * Add account with parameters to the database
	 * @param category
	 * @param title
	 * @param alreadyOnAccount
	 * @param currConst
	 */
	public void setAccount(String category, String title, String alreadyOnAccount,	int currConst) {
		openDB();
		ContentValues values = new ContentValues();
		values.put(ACC_CATEGORY, category);
		values.put(ACC_TITLE, title);
		values.put(ACC_ALREADY, alreadyOnAccount);
		values.put(ACC_CURRENCY, currConst);
		database.insert(TABLE_ACC, null, values);
		database.close();
	}
	
	public void rawQuery(Context ctx) {
        openDB();
		final String MY_QUERY = "SELECT " + ACC_CATEGORY + ", " + ACC_TITLE
				+ ", " + ACC_ALREADY + ", " + CURR_NAME + " FROM "
				+ TABLE_ACC + " INNER JOIN " + TABLE_CURR + " WHERE "
				+ TABLE_ACC + "." + ACC_CURRENCY + " = " + TABLE_CURR + "."
				+ CURR_ID;
		Cursor accCursor = database.rawQuery(MY_QUERY, null);
//        Cursor accCursor = database.query(TABLE_ACC, null, null, null, null, null, CATEGORY_ACC);
		if (accCursor.moveToFirst()) {

			// определ€ем номера столбцов по имени в выборке
//			int idColIndex = accCursor.getColumnIndex(ACC_ID);
			int categoryColIndex = accCursor.getColumnIndex(ACC_CATEGORY);
			int titleColIndex = accCursor.getColumnIndex(ACC_TITLE);
			int alreadyColIndex = accCursor.getColumnIndex(ACC_ALREADY);
			int currColIndex = accCursor.getColumnIndex(CURR_NAME);

			do {
				// получаем значени€ по номерам столбцов и пишем все в лог
				Toast.makeText(ctx,
//						"ID = " + accCursor.getInt(idColIndex) +
						"category = " + accCursor.getString(categoryColIndex) +
						", \ntitle = " + accCursor.getString(titleColIndex) +
						", \nalready on acc = " + accCursor.getString(alreadyColIndex) +
						", \ncurancy = " + accCursor.getString(currColIndex), Toast.LENGTH_SHORT).show();
				Log.d(LOG_TAG,
//						"ID = " + accCursor.getInt(idColIndex) +
						"category = " + accCursor.getString(categoryColIndex) +
						", \ntitle = " + accCursor.getString(titleColIndex) +
						", \nalready on acc = " + accCursor.getString(alreadyColIndex) +
						", \ncurancy = " + accCursor.getString(currColIndex));
				// переход на следующую строку
				// а если следующей нет (текуща€ - последн€€), то false -
				// выходим из цикла
			} while (accCursor.moveToNext());
		} else
			Log.d(LOG_TAG, "0 rows");
		accCursor.close();
		database.close();

//		return null;
	}
	
	public String[] selectColumn(Context ctx) {
        openDB();
		final String MY_QUERY = "SELECT " + ACC_TITLE + " FROM " + TABLE_ACC ;
		Cursor accCursor = database.rawQuery(MY_QUERY, null);
		String[] arrAccount = new String[accCursor.getCount()];
		if (accCursor.moveToFirst()) {

			// определ€ем номера столбцов по имени в выборке
			int titleColIndex = accCursor.getColumnIndex(ACC_TITLE);
			
			int i = 0;
			do {
				arrAccount[i++] =  accCursor.getString(titleColIndex);
				// получаем значени€ по номерам столбцов и пишем все в лог
//				Toast.makeText(ctx, "titleCount = " + titleCount + " titleColumnCount = " 
//				+ titleColumnCount + " titleColIndex = " + titleColIndex
//						, Toast.LENGTH_SHORT).show();
				// переход на следующую строку
				// а если следующей нет (текуща€ - последн€€), то false -
				// выходим из цикла
			} while (accCursor.moveToNext());
		} else
			Log.d(LOG_TAG, "0 rows");
		accCursor.close();
		database.close();

		return arrAccount;
	}
	
	private void openDB() {
		try {
        	database = getWritableDatabase();
		} catch (SQLException e) {
			Log.e(this.toString(), "Error while getting database");
			throw new Error("The end");
		}
	}
	
	public int deleteDB(){
		openDB();
		database.delete(TABLE_INC, null, null);
		return database.delete(TABLE_ACC, null, null);
	}
}
