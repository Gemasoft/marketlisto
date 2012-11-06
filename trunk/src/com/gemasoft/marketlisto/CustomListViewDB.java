package com.gemasoft.marketlisto;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class CustomListViewDB extends ListActivity {

	private ListView mainListView = null;
	CustomSqlCursorAdapter adapter = null;
	private SqlHelper dbHelper = null;
	private Cursor currentCursor = null;
	private ListView listView = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (this.dbHelper == null) {
			this.dbHelper = new SqlHelper(this);

		}

		listView = getListView();
		listView.setItemsCanFocus(false);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		Button btnClear = (Button) findViewById(R.id.btnClear);
		btnClear.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Toast.makeText(getApplicationContext(),	" You clicked Clear button", Toast.LENGTH_SHORT).show();
				ClearDBSelections();
			}
		});

		new SelectDataTask().execute();
		this.mainListView = getListView();
		mainListView.setCacheColorHint(0);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		new SelectDataTask().execute();
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.dbHelper.close();
	}

	protected void ClearDBSelections() {
		this.adapter.ClearSelections();
	}

	private class SelectDataTask extends AsyncTask<String, Void, String> {

		protected void onPreExecute() {
			 //this.dialog.setMessage("Getting Names...");
			 //this.dialog.show();
		}

		protected String doInBackground(final String... args) {
			try {
				CustomListViewDB.this.dbHelper.createDatabase();
				CustomListViewDB.this.dbHelper.openDataBase();
				CustomListViewDB.this.currentCursor = CustomListViewDB.this.dbHelper.getCursor();
			} catch (SQLException sqle) {
				throw sqle;
			}
			return null;
		}

		// can use UI thread here
		@SuppressWarnings("deprecation")
		protected void onPostExecute(final String result) {
			startManagingCursor(CustomListViewDB.this.currentCursor);
			int[] listFields = new int[] { R.id.cbxChecked };
			String[] dbColumns = new String[] { SqlHelper.COLUMN_TITLE };

			CustomListViewDB.this.adapter = new CustomSqlCursorAdapter(
					CustomListViewDB.this, R.layout.listitem_custom_row,
					CustomListViewDB.this.currentCursor, dbColumns, listFields,
					CustomListViewDB.this.dbHelper);
			setListAdapter(CustomListViewDB.this.adapter);

		}
	}

}