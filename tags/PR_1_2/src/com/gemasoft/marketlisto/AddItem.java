package com.gemasoft.marketlisto;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

public class AddItem extends Activity {
	MySQLHelper helper = new MySQLHelper(this);
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        
    	Button btnCancel = (Button) findViewById(R.id.btnCancelAdd);
    	btnCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AddItem.this.finish();
			}
		});
    	
    	Button btnAdd = (Button) findViewById(R.id.btnAddNewItem);
    	btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
			AutoCompleteTextView newItem = (AutoCompleteTextView)  findViewById(R.id.txtNewItemTitle);
				String title = newItem.getText().toString().trim();
				
				if(title.length() != 0){
					helper.insertItem(title, 0);
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.action_add_performed), Toast.LENGTH_LONG).show();
					AddItem.this.finish();
				}else{
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.action_add_error), Toast.LENGTH_LONG).show();
				}
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_item, menu);
        return true;
    }
}
