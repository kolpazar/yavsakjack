package org.eyyam.yavsakjack;

import org.eyyam.yavsakjack.ui.Setting;
import org.eyyam.yavsakjack.ui.Settings;
import org.eyyam.yavsakjack.ui.SettingsAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

public class YavsakJackActivity extends Activity {

	private Settings settings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yavsak_jack);
		settings = new Settings(this); 
		
		ListView listSettings = (ListView) findViewById(R.id.listSettings);
		SettingsAdapter listAdapter = new SettingsAdapter(this, R.layout.row_setting, settings);
		listSettings.setAdapter(listAdapter);
		listSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Setting setting = (Setting) parent.getItemAtPosition(position);
				settings.setBoolean(setting.getName(), !settings.getBoolean(setting.getName()));
				CheckBox check = (CheckBox) view.findViewById(R.id.settingCheck);
				check.toggle();
			}
		});
		
	}

	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.yavsak_jack, menu);
		return true;
	}
	*/
}
