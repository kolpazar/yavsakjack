package org.eyyam.yavsakjack.mods;

import java.lang.reflect.Method;

import org.eyyam.yavsakjack.Constants;
import org.eyyam.yavsakjack.Registry;
import org.eyyam.yavsakjack.YavsakJackModule;

import android.text.Layout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class AdjustLineSpacing {

	public static void init(LoadPackageParam lpparam) {
		
		// Set tweet list view paddings.
		XposedBridge.hookMethod(XposedHelpers.findMethodBestMatch(Registry.class_TweetView, "onMeasure", int.class, int.class), new XC_MethodHook() {
    		@Override
    		protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
    			if (YavsakJackModule.prefs.getBoolean(Constants.SETTING_ADJUSTTEXT, false)) {
	    			int paddingLeft = (Integer) XposedHelpers.callMethod(param.thisObject, "getPaddingLeft");
	    			//int paddingTop = (Integer) XposedHelpers.callMethod(param.thisObject, "getPaddingTop");
	    			int paddingRight = (Integer) XposedHelpers.callMethod(param.thisObject, "getPaddingRight");
	    			int paddingBottom = (Integer) XposedHelpers.callMethod(param.thisObject, "getPaddingBottom");
	    			Method setPaddingMethod = XposedHelpers.findMethodBestMatch(Registry.class_View, "setPadding", int.class, int.class, int.class, int.class);
	    			setPaddingMethod.invoke(param.thisObject, paddingLeft, 15, paddingRight, paddingBottom);
    			}
    		}
    		@Override
    		protected void afterHookedMethod(MethodHookParam param) throws Throwable {
    			if (YavsakJackModule.prefs.getBoolean(Constants.SETTING_ADJUSTTEXT, false)) {
					int width = (Integer) XposedHelpers.callMethod(param.thisObject, "getMeasuredWidth");
					int height = (Integer) XposedHelpers.callMethod(param.thisObject, "getMeasuredHeight");
					Method setMeasuredDimensionMethod = XposedHelpers.findMethodBestMatch(Registry.class_View, "setMeasuredDimension", int.class, int.class);
					setMeasuredDimensionMethod.invoke(param.thisObject, width, height - 8);
    			}
    		}	
		});
		
		// Set tweet list view line spacing.
		XposedBridge.hookMethod(XposedHelpers.findConstructorBestMatch(Registry.class_StaticLayout, CharSequence.class, TextPaint.class, int.class, Layout.Alignment.class, float.class, float.class, boolean.class), new XC_MethodHook() {
    		@Override
    		protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
    			if (YavsakJackModule.prefs.getBoolean(Constants.SETTING_ADJUSTTEXT, false)) {
    				param.args[4] = 1.2f;
    			}
    		}
    		@Override
    		protected void afterHookedMethod(MethodHookParam param) throws Throwable {
    			
    		}	
		});
		
		// Set tweet list view line spacing.
		XposedBridge.hookMethod(XposedHelpers.findConstructorBestMatch(Registry.class_StaticLayout, 
				CharSequence.class, int.class, int.class, TextPaint.class, int.class, Layout.Alignment.class, 
				float.class, float.class, boolean.class, TextUtils.TruncateAt.class, int.class), new XC_MethodHook() {
    		@Override
    		protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
    			if (YavsakJackModule.prefs.getBoolean(Constants.SETTING_ADJUSTTEXT, false)) {
    				param.args[6] = 1.2f;
    			}
    		}
    		@Override
    		protected void afterHookedMethod(MethodHookParam param) throws Throwable {
    		}	
		});
		
		// Set tweet detail view line spacing.
		XposedBridge.hookMethod(XposedHelpers.findMethodBestMatch(Registry.class_TweetDetailView, "onFinishInflate"), new XC_MethodHook() {
    		@Override
    		protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
    		}
    		@Override
    		protected void afterHookedMethod(MethodHookParam param) throws Throwable {
    			if (YavsakJackModule.prefs.getBoolean(Constants.SETTING_ADJUSTTEXT, false)) {
    				ViewGroup view = (ViewGroup) param.thisObject;
    				TextView textView = (TextView) view.findViewById(Registry.getId("content"));
    				textView.setLineSpacing(0, 1.2f);
    			}
    		}	
		});

	}
}
