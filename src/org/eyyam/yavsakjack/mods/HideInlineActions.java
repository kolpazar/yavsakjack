package org.eyyam.yavsakjack.mods;

import java.lang.reflect.Array;

import org.eyyam.yavsakjack.Constants;
import org.eyyam.yavsakjack.Registry;
import org.eyyam.yavsakjack.YavsakJackModule;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class HideInlineActions {

	public static void init(LoadPackageParam lpparam) {
		
		// Hide inline actions from timeline.
		XposedBridge.hookMethod(XposedHelpers.findMethodBestMatch(Registry.class_TweetView, "setHideInlineActions", Boolean.class), new XC_MethodHook() {
    		@Override
    		protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
    			if (YavsakJackModule.prefs.getBoolean(Constants.SETTING_HIDEINLINE, false)) {
    				param.args[0] = Boolean.valueOf(true);
    			}
    		}
    		@Override
    		protected void afterHookedMethod(MethodHookParam param) throws Throwable {
    		}	
		});
		
		// Hide inline actions from interactions and activity feed.
		XposedBridge.hookMethod(XposedHelpers.findMethodBestMatch(Registry.class_TweetView, "getInlineActionTypes"), new XC_MethodHook() {
    		@Override
    		protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
    			if (YavsakJackModule.prefs.getBoolean(Constants.SETTING_HIDEINLINE, false)) {
    				param.setResult(Array.newInstance(Registry.class_TweetActionType, 0));
    			}
    		}
    		@Override
    		protected void afterHookedMethod(MethodHookParam param) throws Throwable {
    		}	
		});
		
		
	}
}
