package com.gemasoft.marketlisto;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;


public class ListItemAdapter extends ArrayAdapter<ListItem> {
	
	private Context context;
	int layoutResourceId;
	private ArrayList<ListItem> dataItems = null;

	private Cursor currentCursor;
	private MySQLHelper dbHelper;

    Typeface  ThinPencilHandwriting;

	static class ListItemHolder {
		CheckBox cbxChecked;
		TextView txtRowDetails;
	}

	public ListItemAdapter(Context context, int layoutResourceId, ArrayList<ListItem> dataItems) {
		super(context, layoutResourceId, dataItems);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.dataItems = dataItems;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		
		View row = convertView;
		ListItemHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new ListItemHolder();
			holder.cbxChecked = (CheckBox) row.findViewById(R.id.cbxChecked);
			holder.txtRowDetails = (TextView) row.findViewById(R.id.txtRowDetails);
			
			row.setTag(holder);
		} else {
			holder = (ListItemHolder) row.getTag();
		}

		ListItem listItem = dataItems.get(pos);
		
		holder.cbxChecked.setTag(listItem._ID);
		holder.cbxChecked.setText(listItem.title);
		holder.cbxChecked.setChecked(listItem.checked);
		
		holder.txtRowDetails.setText(listItem.quantity + " * $" + listItem.price + "  ");
		
		if (listItem.checked) {
			holder.cbxChecked.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		}
		
		//ThinPencilHandwriting = Typeface.createFromAsset(context.getAssets(), "fonts/ThinPencilHandwriting.ttf");
		holder.cbxChecked.setTypeface(ThinPencilHandwriting, Typeface.BOLD);
		
		
		return row;
	}

	@SuppressWarnings("deprecation")
	public void ClearSelections() {
		this.dbHelper.clearSelections();
		this.currentCursor.requery();
	}

}
