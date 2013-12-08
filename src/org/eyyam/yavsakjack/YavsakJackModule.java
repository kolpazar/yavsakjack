package org.eyyam.yavsakjack;

import org.eyyam.yavsakjack.mods.AdjustLineSpacing;
import org.eyyam.yavsakjack.mods.HideInlineActions;
import org.eyyam.yavsakjack.mods.HideSponsoredTweets;
import org.eyyam.yavsakjack.mods.TweetActions;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class YavsakJackModule implements IXposedHookZygoteInit, IXposedHookLoadPackage {

	public static XSharedPreferences prefs;
	
	@Override
	public void initZygote(StartupParam startupParam) throws Throwable {
		prefs = new XSharedPreferences(Constants.PACKAGE_MODULE, Constants.SETTINGS_FILE);
		prefs.makeWorldReadable();
	}
	
	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		if (!lpparam.packageName.equals(Constants.PACKAGE_TWITTER)) {
	        return;
		}
		Registry.init(lpparam);
		prefs = new XSharedPreferences(Constants.PACKAGE_MODULE, Constants.SETTINGS_FILE);
		HideInlineActions.init(lpparam);
		AdjustLineSpacing.init(lpparam);
		HideSponsoredTweets.init(lpparam);
		TweetActions.init(lpparam);
	}
	
}
