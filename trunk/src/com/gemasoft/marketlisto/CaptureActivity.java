package com.gemasoft.marketlisto;

import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import android.app.Activity;

public class CaptureActivity extends Activity {
	
	MySQLHelper helper = new MySQLHelper(this);
	
	int _ID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    _ID = extras.getInt("_ID");
		}
		
		
	}	

	public void cancelCapture(View view){
		CaptureActivity.this.finish();	
	}
	
	public void performCapture(View view){
		
		AutoCompleteTextView qty = (AutoCompleteTextView) findViewById(R.id.txtQuantity);		
		AutoCompleteTextView pri = (AutoCompleteTextView) findViewById(R.id.txtRowDetails);
		
		helper = new MySQLHelper(this);
		helper.checkItem(_ID, 1, qty.getText(), pri.getText());
		
		Toast.makeText(this,"Total: $" + String.valueOf(helper.getTotalPrice()), Toast.LENGTH_LONG).show();
		
		CaptureActivity.this.finish();	
		
		
	}
	
}
