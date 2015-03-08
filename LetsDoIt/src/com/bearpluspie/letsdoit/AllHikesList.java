package com.bearpluspie.letsdoit;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AllHikesList extends ListActivity{
	private List<String> hikeList;
	private DataHelper dh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dh = new DataHelper(this);
		hikeList = dh.selectHikesDummy();
		
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, hikeList));
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		return true;
	}

	
	
}
