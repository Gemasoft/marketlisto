package com.gemasoft.marketlisto;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class ListItemAdapter extends ArrayAdapter<ListItem> {

	Context context;
	int layoutResourceId;
	ListItem data[] = null;
	
	public ListItemAdapter(Context context, int layoutResourceId, ListItem[] data){
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View row = convertView;
		ListItemHolder holder = null;
		
		if(row == null){
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent,false);
			holder = new ListItemHolder();
			holder.cbxChecked= (CheckBox)row.findViewById(R.id.cbxChecked);
			holder.txtTitle= (TextView)row.findViewById(R.id.txtTitle);
			row.setTag(holder);
		}else{
			holder = (ListItemHolder)row.getTag();
		}
		ListItem listItem = data[position];
		holder.cbxChecked.setChecked(listItem.checked);
		holder.txtTitle.setText(listItem.title);
		
		return row;
	}
	static class ListItemHolder
    {
        CheckBox cbxChecked;
        TextView txtTitle;
    }
}
