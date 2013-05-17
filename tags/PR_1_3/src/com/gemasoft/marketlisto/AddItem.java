package com.gemasoft.marketlisto;

import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AddItem extends Activity {
	MySQLHelper helper = new MySQLHelper(this);
	int isAdded = 0;
	
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
    	
    	final Button btnAdd = (Button) findViewById(R.id.btnAddNewItem);
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
    	
    	TextView txtNewItemTitle = (TextView) findViewById(R.id.txtNewItemTitle);
    	txtNewItemTitle.setOnKeyListener(new OnKeyListener(){

			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == 66 && isAdded == 0){					
					btnAdd.performClick();
					isAdded = 1;
				}		
				return false;
			}	
    		
    	});
    	
    }
}
