package org.eyyam.yavsakjack.mods;

import java.lang.reflect.Method;

import org.eyyam.yavsakjack.Constants;
import org.eyyam.yavsakjack.Registry;
import org.eyyam.yavsakjack.YavsakJackModule;

import android.view.View;
import android.view.ViewGroup;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class HideSponsoredTweets {

	public static void init(LoadPackageParam lpparam) {
		
		XposedBridge.hookMethod(XposedHelpers.findMethodBestMatch(Registry.class_GroupedRowView, "onMeasure", int.class, int.class), new XC_MethodHook() {
    		
			@Override
    		protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
    			
    		}
    		
    		@Override
    		protected void afterHookedMethod(MethodHookParam param) throws Throwable {
    			if (YavsakJackModule.prefs.getBoolean(Constants.SETTING_HIDESPONSORED, false)) {
	    			ViewGroup view = (ViewGroup) param.thisObject;
	    			View tweetView = view.findViewById(Registry.getId("row"));
	    			Object tweet = Registry.field_TweetView_Tweet.get(tweetView);
	    			if (tweet != null) {
	    				Object promotedContent = Registry.field_Tweet_PromotedContent.get(tweet);
	    				if (promotedContent != null) {
	            			int width = (Integer) XposedHelpers.callMethod(param.thisObject, "getMeasuredWidth");
	            			Method setMeasuredDimensionMethod = XposedHelpers.findMethodBestMatch(Registry.class_View, "setMeasuredDimension", int.class, int.class);
	            			setMeasuredDimensionMethod.invoke(param.thisObject, width, 0);
	    				}
	    			}
    			}
    		}
    		
		});
	}
}
