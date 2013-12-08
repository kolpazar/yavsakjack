package org.eyyam.yavsakjack.mods;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eyyam.yavsakjack.Constants;
import org.eyyam.yavsakjack.Registry;
import org.eyyam.yavsakjack.YavsakJackModule;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class TweetActions {

	private static Field field_Tweet_Content;
	private static Field field_Tweet_Id;
	private static Field field_Tweet_Username;
	private static int idMenuCopyTweet = 0x7F0AF000;
	private static int idMenuCopyTweetUrl = 0x7F0AF001;
	private static int idMenuCopyTweetMediaUrl = 0x7F0AF002;
	private static Pattern urlPattern = Pattern.compile("https?:\\/\\/t.co\\/\\w+");
	
	private static int idReportTweet;
	private static boolean tweetMenuInProgress;
	
	private static Field field_ToolbarItem_Id;
	private static Field field_ToolbarItem_Text;
	private static Class<?> class_ToolbarItem;
	
	private static void hookToolbarItemSelected() {
		Method[] methods = Registry.class_TweetActivity.getDeclaredMethods();
		for (Method method: methods) {
			Class<?>[] classes = method.getParameterTypes();
			if ((classes.length == 1) && (classes[0].equals(class_ToolbarItem))) {
				XposedBridge.hookMethod(method, new XC_MethodHook() {
		    		@Override
		    		protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
		    			int itemId = field_ToolbarItem_Id.getInt(param.args[0]);
		    			if (toolbarItemSelected(param.thisObject, itemId)) {
		    				param.setResult(true);
		    			}
		    		}
		    		
		    		@Override
		    		protected void afterHookedMethod(MethodHookParam param) throws Throwable {
		    			
		    		}	
				});
			}
		}
	}

	private static boolean toolbarItemSelected(Object tweetActivity, int itemId) throws Throwable {
		Object tweet = Registry.field_TweetActivity_Tweet.get(tweetActivity);
		if (itemId == idMenuCopyTweet) {
			copyToClipboard(tweetActivity, "Tweet", (String) field_Tweet_Content.get(tweet));
			return true;
		} else if (itemId == idMenuCopyTweetUrl) {
			String url = "http://twitter.com/" + (String) field_Tweet_Username.get(tweet) + "/status/" + Long.toString(field_Tweet_Id.getLong(tweet));
			copyToClipboard(tweetActivity, "Tweet URL", url);
			return true;
		} else if (itemId == idMenuCopyTweetMediaUrl) {
			String content = ((String) field_Tweet_Content.get(tweet));
			Matcher m = urlPattern.matcher(content);
			final List<String> urls = new LinkedList<String>();
			while (m.find()) {
				urls.add(m.group());
			}
			if (urls.size() > 0) {
				
				final Object thisObject = tweetActivity;
		        new AsyncTask<String, Void, String>() {
		            protected String doInBackground(String... params) {
						String url = params[0];
						try {
							HttpURLConnection con;
							con = (HttpURLConnection)(new URL(url).openConnection());
							con.setInstanceFollowRedirects(false);
							con.connect();
							String redirectUrl = con.getHeaderField("Location");
							if (redirectUrl != null) {
								url = redirectUrl;
							}
						} catch (MalformedURLException e) {
						} catch (IOException e) {
						}
						return url;
		            }
		            protected void onPostExecute(String result) {
						copyToClipboard(thisObject, "Media URL", result);
		            };
		        }.execute(urls.get(urls.size() - 1));
			}
			return true;
		} else {
			return false; 
		}
	}
	
	private static void copyToClipboard(Object context, String label, String text) {
		ClipboardManager clipboard = (ClipboardManager) ((Activity) context).getSystemService(Context.CLIPBOARD_SERVICE);
		clipboard.setPrimaryClip(ClipData.newPlainText(label, text));
		Toast.makeText((Context) context, label + " copied to clipboard", Toast.LENGTH_SHORT).show();
	}
	
	public static void init(LoadPackageParam lpparam) {
		
		tweetMenuInProgress = false;
		
		int fieldNumLong = 0, fieldNumString = 0;
		for (Field field: Registry.class_Tweet.getDeclaredFields()) {
			if (field.getType().equals(String.class)) {
				fieldNumString++;
				if (fieldNumString == 1) {
					field_Tweet_Username = field;
				} else if (fieldNumString == 8) {
					field_Tweet_Content = field;
				}
			} else if (field.getType().equals(long.class)) {
				fieldNumLong++;
				if (fieldNumLong == 1) {
					field_Tweet_Id = field;
				}
			}
		}
		
		idReportTweet = Registry.getId("menu_report_tweet");
		
		Method[] methods = Registry.class_TweetActivity.getDeclaredMethods();
		for (Method method: methods) {
			Class<?>[] classes = method.getParameterTypes();
			if ((classes.length == 2) && (classes[1].equals(Registry.class_Toolbar))) {
				XposedBridge.hookMethod(method, new XC_MethodHook() {
		    		@Override
		    		protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
		    			tweetMenuInProgress = true;
		    		}
		    		
		    		@Override
		    		protected void afterHookedMethod(MethodHookParam param) throws Throwable {
		    			tweetMenuInProgress = false;
		    		}	
				});
			}
		}
		
		methods = Registry.class_Toolbar.getDeclaredMethods();
		for (Method method: methods) {
			Class<?>[] classes = method.getParameterTypes();
			if ((classes.length == 1) && (classes[0].equals(Collection.class))) {
				XposedBridge.hookMethod(method, new XC_MethodHook() {
					
					private void addToolbarItem(Object toolbar, ArrayList list, Object oldItem, int id, CharSequence text) throws Throwable {
    					Object newItem = class_ToolbarItem.getDeclaredConstructor(Registry.class_Toolbar, boolean.class).newInstance(toolbar, true);
    					field_ToolbarItem_Id.setInt(newItem, id);
    					field_ToolbarItem_Text.set(newItem, text);
    					list.add(newItem);
					}
					
		    		@Override
		    		protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
		    			if (tweetMenuInProgress) {
							ArrayList list = (ArrayList) param.args[0];
		    				if (list.size() > 0) {
		    					Object oldItem = list.get(0);
		    					if (field_ToolbarItem_Id == null) {
		    						// Find id and text fields in the ToolbarItem class. 
		    						Class<?> itemClass = oldItem.getClass();
			    					Field[] itemFields = itemClass.getDeclaredFields();
			    					for (Field field: itemFields) {
			    						if (field.getType().equals(int.class) && (field.getInt(oldItem) == idReportTweet)) {
			    							field_ToolbarItem_Id = field;
			    							class_ToolbarItem = itemClass;
			    							hookToolbarItemSelected();
			    						} else if (field.getType().equals(CharSequence.class) && (field.get(oldItem) != null)) {
			    							field_ToolbarItem_Text = field;
			    						}
			    					}
		    					}
		    					if (field_ToolbarItem_Id != null) {
	    							// This is the tweet detail menu. Add new items.
		    		    			if (YavsakJackModule.prefs.getBoolean(Constants.SETTING_TWEETACTIONS, false)) {
		    		    				addToolbarItem(param.thisObject, list, oldItem, idMenuCopyTweet, "Copy tweet");
		    		    				addToolbarItem(param.thisObject, list, oldItem, idMenuCopyTweetUrl, "Copy tweet URL");
		    		    				addToolbarItem(param.thisObject, list, oldItem, idMenuCopyTweetMediaUrl, "Copy media URL");
		    		    				param.args[0] = list;
		    		    			}
	    							tweetMenuInProgress = false;
		    					}
		    				}
		    			}
		    		}
		    		
		    		@Override
		    		protected void afterHookedMethod(MethodHookParam param) throws Throwable {
		    		}	
					
				});
			}
		}
		
	}
	
}
