package com.gemasoft.marketlisto;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class CustomRow extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listitem_custom_row);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.listitem_custom_row, menu);
        return true;
    }
}
