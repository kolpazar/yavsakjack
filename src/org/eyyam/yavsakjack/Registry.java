package org.eyyam.yavsakjack;

import java.lang.reflect.Field;
import java.util.HashMap;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Registry {

	private static Boolean inited = false;
	
	public static Class<?> class_TweetView;
	public static Class<?> class_TweetDetailView;
	public static Class<?> class_Tweet;
	public static Class<?> class_PromotedContent;
	public static Class<?> class_GroupedRowView;
	public static Class<?> class_View;
	public static Class<?> class_ViewGroup;
	public static Class<?> class_Context;
	public static Class<?> class_MultiTouchImageView;
	public static Class<?> class_StaticLayout;
	public static Class<?> class_TweetActivity;
	public static Class<?> class_TweetActionType;
	public static Class<?> class_Toolbar;
	
	public static HashMap<String, Class<?>> class_resourceId;
	public static Class<?> class_Id;
	
	public static Field field_Tweet_PromotedContent;
	public static Field field_TweetActivity_Tweet;
	public static Field field_TweetDetailView_Tweet;
	public static Field field_TweetView_Tweet;

	public static void init(LoadPackageParam lpparam) {
		if (inited) {
			return;
		}
		inited = true;
		class_TweetView = XposedHelpers.findClass("com.twitter.library.widget.TweetView", lpparam.classLoader);
		class_TweetDetailView = XposedHelpers.findClass("com.twitter.applib.widget.TweetDetailView", lpparam.classLoader);
		class_Tweet = XposedHelpers.findClass("com.twitter.library.provider.Tweet", lpparam.classLoader);
		class_PromotedContent = XposedHelpers.findClass("com.twitter.library.api.PromotedContent", lpparam.classLoader);
		class_GroupedRowView = XposedHelpers.findClass("com.twitter.library.widget.GroupedRowView", lpparam.classLoader);
		class_TweetActivity = XposedHelpers.findClass("com.twitter.applib.TweetActivity", lpparam.classLoader);
		class_TweetActionType = XposedHelpers.findClass("com.twitter.library.TweetActionType", lpparam.classLoader);
		class_Toolbar = XposedHelpers.findClass("com.twitter.internal.android.widget.ToolBar", lpparam.classLoader);
		
		class_View = XposedHelpers.findClass("android.view.View", lpparam.classLoader);
		class_ViewGroup = XposedHelpers.findClass("android.view.ViewGroup", lpparam.classLoader);
		class_Context = XposedHelpers.findClass("android.content.Context", lpparam.classLoader);
		class_MultiTouchImageView = XposedHelpers.findClass("com.twitter.library.widget.MultiTouchImageView", lpparam.classLoader);
		class_StaticLayout = XposedHelpers.findClass("android.text.StaticLayout", lpparam.classLoader);
		
		field_Tweet_PromotedContent = getFieldByClass(class_Tweet, class_PromotedContent);
		field_TweetActivity_Tweet = getFieldByClass(class_TweetActivity, class_Tweet);
		field_TweetDetailView_Tweet = getFieldByClass(class_TweetDetailView, class_Tweet);
		field_TweetView_Tweet = getFieldByClass(class_TweetView, class_Tweet);
	
		findIdClasses("com.twitter.applib", lpparam);
	}
	
	private static void findIdClasses(String packageName, LoadPackageParam lpparam) {
		class_resourceId = new HashMap<String, Class<?>>();
		class_resourceId.put("menu", null);
		class_resourceId.put("layout", null);
		int i = 0, fl = 0, sl = 0;
		Class<?> class_t;
		String c;
		while (true) {
			fl = i / 26;
			sl = i % 26;
			c = Character.toString((char) (sl + 97)); 
			if (fl > 0) {
				c = Character.toString((char) (fl + 97)) + c;
			}
			try {
				class_t = XposedHelpers.findClass(packageName + "." + c, lpparam.classLoader);
			} catch (ClassNotFoundError e) {
				return;
			}
			for (Field field: class_t.getDeclaredFields()) {
				if (field.getType().equals(int.class) && (field.getName().equals("settings_row_view"))) {
					class_resourceId.put("layout", class_t); // lw
				} else if (field.getType().equals(int.class) && (field.getName().equals("menu_report_tweet"))) {
					class_Id = class_t; // lu
				} else if (field.getType().equals(int.class) && (field.getName().equals("home_toolbar"))) {
					class_resourceId.put("menu", class_t); // lx
				}
			}
			i++;
		}
	}
	
	private static int getInt(Class<?> c, String name) {
		try {
			return c.getField(name).getInt(null);
		} catch (Exception e) {
			XposedBridge.log("id " + name + " not found!");
			return 0;
		}
	}
	
	public static int getId(String name) {
		return getInt(class_Id, name);
	}
	
	public static int getResourceId(String resourceType, String name) {
		return getInt(class_resourceId.get(resourceType), name);
	}
	
	private static Field getFieldByClass(Class<?> mainClass, Class<?> fieldClass) {
		for (Field field: mainClass.getDeclaredFields()) {
			if (field.getType().equals(fieldClass)) {
				return field;
			}
		}
		return null;
	}

}
