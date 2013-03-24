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
	
	private static final String STAT = "stat";
	
	public static final String ID = "_id";
	
	public static final String TABLE_CURR = "currancy";	
	public static final String CURR_NAME = "name";
	public static final String CURR_RATE = "rate";
	
	public static final String TABLE_CAT_ACC = "category_account";	
	public static final String CAT_ACC_NAME = "name";
	
	public static final String TABLE_CAT_INC = "category_income";	
	public static final String CAT_INC_NAME = "name";
	
	public static final String TABLE_CAT_EXP = "category_expense";
	public static final String CAT_EXP_NAME = "name";
	
	public static final String TABLE_ACC = "account";
	public static final String ACC_CATEGORY = "category";
	public static final String ACC_TITLE = "title";
	public static final String ACC_ALREADY = "amount";
	public static final String ACC_CURRENCY = "currency_id";
	
	public static final String TABLE_INC = "income";
	public static final String INC_CATEGORY = "category";
	public static final String INC_ICON = "icon";
	public static final String INC_TITLE = "title";
	public static final String INC_AMOUNT = "amount";
	public static final String INC_ACCOUNT = "account_id";
	public static final String INC_HOW_OFTEN = "how_often";
	public static final String INC_DATE = "date";
	
	public static final String TABLE_EXP = "expense";
	public static final String EXP_CATEGORY = "category";
	public static final String EXP_ICON = "icon";
	public static final String EXP_TITLE = "title";
	public static final String EXP_AMOUNT = "amount";
	public static final String EXP_ACCOUNT = "account_id";
	public static final String EXP_HOW_OFTEN = "how_often";
	public static final String EXP_DATE = "date";
	
	final String LOG_TAG = "myLogs";
	
	private SQLiteDatabase database;

	String[] strArrCode, strArrCatAcc, strArrCatInc, strArrCatExp;
	
	public DBOpenHelper(Context context) {		
		super(context, DB_NAME, null, DB_VERSION);
//		this.context = context;
		Resources res = context.getResources();
		strArrCode = res.getStringArray(R.array.code_currancy_arrays);
		strArrCatAcc = res.getStringArray(R.array.category_account_arrays);
		strArrCatInc = res.getStringArray(R.array.category_income_arrays);
		strArrCatExp = res.getStringArray(R.array.category_expense_arrays);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {		
		// table Currancy
		db.execSQL("CREATE TABLE " + TABLE_CURR + " (" +
		        ID + " INTEGER PRIMARY KEY , " +
		        CURR_NAME + " TEXT, " +
		        CURR_RATE + " FLOAT)");
		// table Category Account
		db.execSQL("CREATE TABLE " + TABLE_CAT_ACC + " (" +
		        ID + " INTEGER PRIMARY KEY , " +
		        CAT_ACC_NAME + " TEXT, " +
		        STAT + " INTEGER)");
		// table Category Income
		db.execSQL("CREATE TABLE " + TABLE_CAT_INC + " (" +
		        ID + " INTEGER PRIMARY KEY , " +
		        CAT_INC_NAME + " TEXT, " +
		        STAT + " INTEGER)");
		// table Category Expense
		db.execSQL("CREATE TABLE " + TABLE_CAT_EXP + " (" +
		        ID + " INTEGER PRIMARY KEY , " +
		        CAT_EXP_NAME + " TEXT, " +
		        STAT + " INTEGER)");
		// table Account
		db.execSQL("CREATE TABLE "+ TABLE_ACC + " (" +
				ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
				ACC_CATEGORY + " TEXT, " + 
				ACC_TITLE + " TEXT, " + 
				ACC_ALREADY + " TEXT, " +
				STAT + " INTEGER, " +
				ACC_CURRENCY + " INTEGER)");
//				ACC_CURRENCY + " INTEGER NOT NULL ,FOREIGN KEY (" + 
//				ACC_CURRENCY + ") REFERENCES " +
//				TABLE_CURR + " (" + CURR_ID + "));");
		// table Income
		db.execSQL("CREATE TABLE "+ TABLE_INC + " (" +
				ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
				INC_CATEGORY + " TEXT, " +
				INC_ICON + " TEXT, " + 
				INC_TITLE + " TEXT, " + 
				INC_AMOUNT + " TEXT, " + 
				INC_HOW_OFTEN + " TEXT, " +
				INC_DATE + " TEXT, " +	
				STAT + " INTEGER, " +
				INC_ACCOUNT + " INTEGER)");
		// table Expense
		db.execSQL("CREATE TABLE "+ TABLE_EXP + " (" +
				ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
				EXP_CATEGORY + " TEXT, " +
				EXP_ICON + " TEXT, " + 
				EXP_TITLE + " TEXT, " + 
				EXP_AMOUNT + " TEXT, " + 
				EXP_HOW_OFTEN + " TEXT, " +
				EXP_DATE + " TEXT, " +	
				STAT + " INTEGER, " +
				EXP_ACCOUNT + " INTEGER)");
		
		// Fills a currency from resources
		fillTable(db, strArrCode, TABLE_CURR, CURR_NAME);
		// Fills a category of account from resources
		fillTable(db, strArrCatAcc, TABLE_CAT_ACC, CAT_ACC_NAME);
		// Fills a category of income from resources
		fillTable(db, strArrCatInc, TABLE_CAT_INC, CAT_INC_NAME);
		// Fills a category of expense from resources
		fillTable(db, strArrCatExp, TABLE_CAT_EXP, CAT_EXP_NAME);
	}
	
	// Fill categories from resources
	protected void fillTable(SQLiteDatabase db, String[] arr, String TABLE, String COLUMN) {
		ContentValues values = new ContentValues();
		for (int i = 0; i < arr.length; i++) {
			values.put(COLUMN, arr[i]);
			db.insert(TABLE, null, values);
			}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURR);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAT_ACC);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAT_INC);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAT_EXP);
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
	public void setAccount(String category, String title, float alreadyOnAccount,	int currConst) {
		openDB();
		ContentValues values = new ContentValues();
		values.put(ACC_CATEGORY, category);
		values.put(ACC_TITLE, title);
		values.put(ACC_ALREADY, alreadyOnAccount);
		values.put(ACC_CURRENCY, currConst);
		values.put(STAT, 1);
		database.insert(TABLE_ACC, null, values);
		database.close();
	}
	
	/**
	 * Add income with parameters to the database
	 * @param category
	 * @param title
	 * @param amount 
	 * @param account Number of account
	 * @param howOften Index from array
	 * @param date 
	 */
	public void setIncome(String category, String icon, String title, 
			Float amount, int account_id, int howOften, String date) {
		openDB();
		ContentValues values = new ContentValues();
		values.put(INC_CATEGORY, category);
		values.put(INC_ICON, icon);
		values.put(INC_TITLE, title);
		values.put(INC_AMOUNT, amount);
		values.put(INC_ACCOUNT, account_id);
		values.put(INC_HOW_OFTEN, howOften);
		values.put(INC_DATE, date);
		values.put(STAT, 1);
		database.insert(TABLE_INC, null, values);
		database.close();
	}
	
	/**
	 * Add expense with parameters to the database
	 * @param category
	 * @param title
	 * @param amount 
	 * @param account Number of account
	 * @param howOften Index from array
	 * @param date 
	 */
	public void setExpense(String category, String icon, String title, 
			Float amount, int account_id, int howOften, String date) {
		openDB();
		ContentValues values = new ContentValues();
		values.put(EXP_CATEGORY, category);
		values.put(EXP_ICON, icon);
		values.put(EXP_TITLE, title);
		values.put(EXP_AMOUNT, amount);
		values.put(EXP_ACCOUNT, account_id);
		values.put(EXP_HOW_OFTEN, howOften);
		values.put(EXP_DATE, date);
		values.put(STAT, 1);
		database.insert(TABLE_EXP, null, values);
		database.close();
	}
	
	public void rawQuery(Context ctx) {
        openDB();
		final String MY_QUERY = "SELECT " + ACC_CATEGORY + ", " + ACC_TITLE
				+ ", " + ACC_ALREADY + ", " + CURR_NAME + " FROM "
				+ TABLE_ACC + " INNER JOIN " + TABLE_CURR + " WHERE "
				+ TABLE_ACC + "." + ACC_CURRENCY + " = " + TABLE_CURR + "."
				+ ID;
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
	
	/**
	 * Get column with all rows from table
	 * @param context
	 * @param table 
	 * @param column
	 * @return String array
	 */
	public String[] selectColumn(Context context, String table, String column) {
        openDB();
		final String MY_QUERY = "SELECT " + column + " FROM " + table + " WHERE " + STAT + " = 1" ;
		Cursor accCursor = database.rawQuery(MY_QUERY, null);
		String[] array = new String[accCursor.getCount()];
		if (accCursor.moveToFirst()) {

			int titleColIndex = accCursor.getColumnIndex(column);
			
			int i = 0;
			do {
				array[i++] =  accCursor.getString(titleColIndex);
			} while (accCursor.moveToNext());
		} else
			Log.d(LOG_TAG, "0 rows");
		accCursor.close();
		database.close();

		return array;
	}
	
	/**
	 * Get cell from table
	 * @param context
	 * @param table Name of table (constant)
	 * @param column column from get information (constant)
	 * @param _id column id(constant)
	 * @param position from column _id
	 * @return String
	 */
	public String selectCell(Context context, String table, String column, 
			String _id, int position) {		
        openDB();
        
		final String MY_QUERY = "SELECT " + column + " FROM " + table 
				+ " WHERE " + _id + " = " + position ;
		Cursor accCursor = database.rawQuery(MY_QUERY, null);
		String str = null;
		if (accCursor.moveToFirst()) {
			int titleColIndex = accCursor.getColumnIndex(column);
			str =  accCursor.getString(titleColIndex);			
		} else
			Log.d(LOG_TAG, "0 rows");
		accCursor.close();
		database.close();

		return str;
	}
	
	private void openDB() {
		try {
        	database = getWritableDatabase();
		} catch (SQLException e) {
			Log.e(this.toString(), "Error while getting database");
			throw new Error("The end");
		}
	}
	
//	public boolean isTableExist(String tableName){ 
//        Cursor cur = null; 
//        try{ 
//            cur = database.rawQuery("SELECT * FROM " + tableName + " WHERE 1=0", null ); 
//            return true; 
//        }catch(Exception ex){ 
//            return false; 
//        }finally{ 
//            if (cur != null) cur.close(); 
//        } 
//    } 
	
	public int deleteDB(){
		openDB();
		database.delete(TABLE_INC, null, null);
		return database.delete(TABLE_ACC, null, null);
	}
}
