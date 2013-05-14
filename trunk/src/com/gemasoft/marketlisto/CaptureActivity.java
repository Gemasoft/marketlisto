package com.gemasoft.marketlisto;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.TextView;
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

		final TextView txtPrice = (TextView) findViewById(R.id.txtRowDetails);

		txtPrice.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				final TextView btnCapture = (TextView) findViewById(R.id.btnCapture);
				if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
						(keyCode == KeyEvent.KEYCODE_ENTER))
				{
					// Perform capture on Enter key press
					btnCapture.performClick();
					return true;
				}
				return false;
			}
		});


	}	

	public void cancelCapture(View view){
		CaptureActivity.this.finish();	
	}

	public void performCapture(View view){

		EditText qty = (EditText) findViewById(R.id.txtQuantity);		
		EditText pri = (EditText) findViewById(R.id.txtRowDetails);

		String strQty = qty.getText().toString().trim();
		String strPri = pri.getText().toString().trim();

		if(strQty.length() != 0){	
			if(strPri.length() != 0){	
				helper = new MySQLHelper(this);
				helper.checkItem(_ID, 1, qty.getText(), pri.getText());

				Toast.makeText(this,"Total: $" + String.valueOf(helper.getTotalPrice()), Toast.LENGTH_LONG).show();

				CaptureActivity.this.finish();	
			}else{
				Toast.makeText(this,getResources().getString(R.string.empty_price_alert), Toast.LENGTH_LONG).show();
			}
		}else{
			Toast.makeText(this,getResources().getString(R.string.empty_quantity_alert), Toast.LENGTH_LONG).show();
		}

	}

}
