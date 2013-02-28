package gs.wallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;



/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
	public class MainActivity extends Activity {
		
		final String LOG_TAG = "myLogs";

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.main);			
		}

//		@Override
//		public boolean onCreateOptionsMenu(Menu menu) {
//			// Inflate the menu; this adds items to the action bar if it is present.
//			getMenuInflater().inflate(R.menu.main, menu);
//			return true;
//		}
		
		public void click(View v) {
			Intent intent = new Intent(this, Account.class);
		      startActivity(intent);
//			Toast.makeText(MainActivity.this, "working", Toast.LENGTH_SHORT).show();
		}
		
		public void clickDelDB(View v) {
//			Toast.makeText(MainActivity.this, "working", Toast.LENGTH_SHORT).show();
			DBOpenHelper dbHelper = new DBOpenHelper(this);
			Toast.makeText(MainActivity.this, "deleted rows count = " + dbHelper.deleteDB(), Toast.LENGTH_SHORT).show();
			
		}		

}
