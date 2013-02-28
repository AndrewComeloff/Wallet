package gs.wallet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SqlAdapter extends BaseAdapter {

	private static final String DB_NAME = "walletDB";
	private static final int DB_VERSION = 1;
	private static final int ID_COLUMN = 0;
//	private static final String KEY_NAME = "name"; 
	private static final int NAME_COLUMN = 1;
	
	public static final String TABLE_CURR = "currancy";
	public static final String CURR_ID = "_id";
	public static final String USD_CURR = "usd";
	public static final String EUR_CURR = "eur";
	public static final String BYR_CURR = "byr";
	
	public static final String TABLE_ACC = "account";
	public static final String ACC_ID = "_id";
	public static final String CATEGORY_ACC = "category_acc";
	public static final String TITLE_ACC = "title_acc ";
	public static final String ALREADY_ON_ACC = "already_on_acc";
	public static final String CURRENCY_ACC = "currency";

	private Cursor cursor;
	private SQLiteDatabase database;
	private DbOpenHelper dbOpenHelper;
	private Context context;

	//ƒалее следуют об€зательные к перегрузке методы адаптера
	
	public SqlAdapter(Context context) {
		super();
		this.context = context;
//		init();
	}

	@Override
	public long getItemId(int position) {
		Name nameOnPosition = getItem(position);
		return nameOnPosition.getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parrent) {
		TextView textView;
		if (null == convertView) {
			textView = (TextView) View.inflate(context, R.layout.list_item,
					null);
		} else {
			textView = (TextView) convertView;
		}
		textView.setText(getItem(position).getName());
		return textView;
	}

	@Override
	public int getCount() {
		return cursor.getCount();
	}

	@Override
	public Name getItem(int position) {
		if (cursor.moveToPosition(position)) {
			long id = cursor.getLong(ID_COLUMN);
			String name = cursor.getString(NAME_COLUMN);
			Name nameOnPositon = new Name(id, name);
			return nameOnPositon;
		} else {
			throw new CursorIndexOutOfBoundsException(
					"Cant move cursor on postion");
		}
	}
	
	//ћетоды дл€ работы с базой данных

	public Cursor getAllEntriesAcc() {
		//—писок колонок базы, которые следует включить в результат
		String[] columnsToTake = { ACC_ID, CATEGORY_ACC, TITLE_ACC, ALREADY_ON_ACC, CURRENCY_ACC };
		// составл€ем запрос к базе
		return database.query(TABLE_ACC, columnsToTake,
				null, null, null, null, ACC_ID);
	}

	public long addCategoryAcc(Name name) {
		ContentValues values = new ContentValues();
		values.put(ACC_ID, name.getName());
		long id = database.insert(TABLE_ACC, null, values);
		refresh();
		return id;
	}

//	public boolean removeItem(Name nameToRemove) {
//		boolean isDeleted = (database.delete(TABLE_ACC, KEY_NAME + "=?",
//				new String[] { nameToRemove.getName() })) > 0;
//		refresh();
//		return isDeleted;
//	}

//	public boolean updateItem(long id, String newName) {
//		ContentValues values = new ContentValues();
//		values.put(KEY_NAME, newName);
//		boolean isUpdated = (database.update(TABLE_NAME, values, KEY_ID + "=?",
//				new String[] {id+""})) > 0;
//		return isUpdated;
//	}
	
	//ѕрочие служебные методы

	public void onDestroy() {
		dbOpenHelper.close();
	}

	//¬ызывает обновление вида
	private void refresh() {
		cursor = getAllEntriesAcc();
		notifyDataSetChanged();
	}

	// »нициализаци€ адаптера: открываем базу и создаем курсор
//	private void init() {
//		dbOpenHelper = new DbOpenHelper(context, DB_NAME, null, DB_VESION);
//		try {
//			database = dbOpenHelper.getWritableDatabase();
//		} catch (SQLException e) {
//			// /≈сли база не открылась, то дальше нам дороги нет
//			// но это особый случай
//			Log.e(this.toString(), "Error while getting database");
//			throw new Error("The end");
//		}
//		cursor = getAllEntriesAcc();
//	}

	//  ласс-помошник отвечающий за создание/открытие
	// базы и осуществл€ющий контроль ее версий
	private static class DbOpenHelper extends SQLiteOpenHelper {

		public DbOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		// ¬ызываетс€ при создании базы на устройстве
		public void onCreate(SQLiteDatabase db) {
			// ѕостроим стандартный sql-запрос дл€ создани€ таблицы
			db.execSQL("CREATE TABLE " + TABLE_CURR + " (" +
			        CURR_ID + " INTEGER PRIMARY KEY , " +
			        USD_CURR + " FLOAT, " +
			        EUR_CURR + " FLOAT, " +
			        BYR_CURR + " FLOAT)");
			
			db.execSQL("CREATE TABLE "+ TABLE_ACC + " (" +
					ACC_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
					CATEGORY_ACC + " TEXT, " + 
					TITLE_ACC + " TEXT, " + 
					ALREADY_ON_ACC + " DOUBLE, " + 
					CURRENCY_ACC + " INTEGER NOT NULL ,FOREIGN KEY (" + 
					CURRENCY_ACC + ") REFERENCES " +
					TABLE_CURR + " (" + CURR_ID + "));");
		}

		@Override
		// ћетод будет вызван, если изменитс€ верси€ базы
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// “ут можно организовать миграцию данных из старой базы в новую
			// или просто "выбросить" таблицу и создать заново
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURR);
			db.execSQL("DROP TABLE IF EXISTS "+ TABLE_ACC);
			onCreate(db);
		}
	}

}
