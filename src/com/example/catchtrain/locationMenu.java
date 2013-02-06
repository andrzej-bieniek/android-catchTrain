package com.example.catchtrain;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class locationMenu extends ListActivity {

	String locations[] = {	"London Euston",
							"Harrow & Wealdstone",
							"Bushey",
							"Watford Junction",
							"Kings Langley",
							"Apsley",
							"Hemel Hempstead",
							"Berkhamsted",
							"Tring"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setListAdapter(new ArrayAdapter<String>(locationMenu.this, android.R.layout.simple_list_item_1, locations));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Bundle basket = new Bundle();
		Intent i = new Intent();
		basket.putString("location", locations[position]);
		i.putExtras(basket);
		setResult(RESULT_OK, i);
		finish();
	}


}
