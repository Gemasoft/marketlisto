package com.gemasoft.marketlisto;

public class ListItem {
	public int _ID;
	public boolean checked;
	public String title;
	public ListItem(){
		super();
	}
	public ListItem(int _ID,boolean checked, String title){
		super();
		this.checked = checked;
		this.title = title;
		this._ID = _ID;
	}
}
