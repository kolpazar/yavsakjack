package org.eyyam.yavsakjack.ui;

import java.util.ArrayList;
import org.eyyam.yavsakjack.Constants;
import org.eyyam.yavsakjack.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class Settings {

	private SharedPreferences prefs;
	private ArrayList<Setting> settings;
	
	@SuppressLint("WorldReadableFiles")
	public Settings(Context context) {
		prefs = context.getSharedPreferences(Constants.SETTINGS_FILE, Context.MODE_WORLD_READABLE);
		settings = new ArrayList<Setting>();
		settings.add(new Setting(Constants.SETTING_HIDEINLINE, context.getString(R.string.setting_hide_inline)));
		settings.add(new Setting(Constants.SETTING_TWEETACTIONS, context.getString(R.string.setting_tweet_actions)));
		settings.add(new Setting(Constants.SETTING_HIDESPONSORED, context.getString(R.string.setting_hide_sponsored)));
		settings.add(new Setting(Constants.SETTING_ADJUSTTEXT, context.getString(R.string.setting_adjust_text)));
	}
	
	public ArrayList<Setting> getSettings() {
		return settings;
	}

	public Setting get(int position) {
		return settings.get(position);
	}
	
	public boolean getBoolean(String name) {
		return prefs.getBoolean(name, false);
	}
	
	public void setBoolean(String name, boolean value) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(name, value);
		editor.apply();
	}
}
