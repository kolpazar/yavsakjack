package org.eyyam.yavsakjack.ui;

import org.eyyam.yavsakjack.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingsAdapter extends ArrayAdapter<Setting> {

	private Settings settings;

	public SettingsAdapter(Context context, int textViewResourceId, Settings settings) {
		super(context, textViewResourceId, settings.getSettings());
		this.settings = settings;
	}

	private class ViewHolder {
		TextView text;
		CheckBox check;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return true; //!(settingList.get(position) instanceof SettingHeader);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Setting setting = settings.get(position);
		if ((setting instanceof Setting) && !(convertView instanceof RelativeLayout)) {
			convertView = null;
		}
		ViewHolder holder = null;
		
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.row_setting, null);
			holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(R.id.settingText);
			holder.check = (CheckBox) convertView.findViewById(R.id.settingCheck);
			convertView.setTag(holder);
			holder.check.setClickable(false);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.text.setText(setting.getText());
		holder.check.setChecked(settings.getBoolean(setting.getName()));
		return convertView;
	}

}
