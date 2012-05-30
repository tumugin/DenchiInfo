package jisakuroom.denchi_info;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

import android.R.integer;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

public class batteryservice extends Service{
	//public static int WidgetId = 0;
	@Override
    public void onStart(Intent in, int si) {
		//WidgetId = in.getIntExtra("appWidgetId", 0);
		//Log.v("“d’rInfoWidgetId",String.valueOf(WidgetId));
		//SetClick(this.getApplicationContext());
		if (in == null){
			Log.e("“d’rInfo","Intent is null.Can`t start service.");
			return;
		}
		IntentFilter filter = new IntentFilter();
		
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, filter);
        Log.v("“d’rInfo","onStart()");
        
        
        super.onCreate();
    }
	@Override
	public void onDestroy(){
		try {
			unregisterReceiver(batteryReceiver);
		} catch (Exception e) {
			Log.v("“d’rInfo","Can`t destroy service.Maybe the reciver is not registerd.");
			Log.v("“d’rInfo","onDestroy()");
		}
        super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
		
	private static BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
		int scale = 100;
        int level = 0;
        float temp = 0;
        float fahrenheit = 0;
        boolean hasEx = false;
        
        
        @Override
        public void onReceive(Context c, Intent in) {
        	try {
        		if (c == null){
        			Log.e("“d’rInfo","Context is null.Can`t update widget.");
        			return;
        		}
				String ac = in.getAction();
				if (ac.equals(Intent.ACTION_BATTERY_CHANGED)) {
					level = in.getIntExtra("level", 0);
					scale = in.getIntExtra("scale", 0);
					temp = in.getIntExtra("temperature", 0) / 10;
					
					if(in.hasExtra("temperature")){
	                	fahrenheit = 1.8f * temp + 32;
	                	hasEx = true;
	                }else {
						hasEx = false;
					}
	              
				}
				int battery_pct = (int)(level*100/scale);
				AppWidgetManager awm = AppWidgetManager.getInstance(c);
				ComponentName cn = new ComponentName(c, Widget.class);
				RemoteViews rv = new RemoteViews(c.getPackageName(), R.layout.widgetview);
				Intent intent = new Intent(c, jisakuroom.denchi_info.Main.class);
				PendingIntent pendingIntent = PendingIntent.getActivity(c, 0, intent, 0);
				rv.setOnClickPendingIntent(R.id.RelativeLayout01_widget, pendingIntent);
				rv.setTextViewText(R.id.TextView01, "" +(int)(level*100/scale) + "%");
				//rv.setTextViewText(R.id.textView1, "100%");
				//İ’è
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
                String tani = prefs.getString(c.getString(R.string.tani_setting), "Celsius");
                
                if (tani.matches("Celsius")) {
                	if(hasEx = true){
                		rv.setTextViewText(R.id.textView02, Math.round(temp) + "");
                	}else {
                		rv.setTextViewText(R.id.textView02, "--" + "");
					}
					
				}else {
					if(hasEx = true){
						rv.setTextViewText(R.id.textView02, Math.round(fahrenheit) + "F");
					}else{
						rv.setTextViewText(R.id.textView02, "--" + "F");
					}
					
				}
                
				if(battery_pct >= 0 && battery_pct <30){
					rv.setImageViewResource(R.id.imageView1, R.drawable.red2);
				}else if (battery_pct >= 30 && battery_pct <60) {
					rv.setImageViewResource(R.id.imageView1, R.drawable.orange);
				}else if (battery_pct >= 60 && battery_pct <=100) {
					rv.setImageViewResource(R.id.imageView1, R.drawable.blue);
				}
				
				awm.updateAppWidget(cn, rv);
				//awm.updateAppWidget(WidgetId, rv);
				Log.v("“d’rInfoXVƒCƒxƒ“ƒg","XVOK");
				
			} catch (Exception e) {
				// TODO: handle exception
			}
        	
        }
    };
    public void SetClick(Context context){
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		ComponentName cn = new ComponentName(context, Widget.class);
		RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widgetview);
		Intent intent = new Intent(context, jisakuroom.denchi_info.Main.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		//rv.setOnClickPendingIntent(R.id.TextView01, pendingIntent);
		//rv.setOnClickPendingIntent(R.id.textView02, pendingIntent);
		rv.setOnClickPendingIntent(R.id.RelativeLayout01_widget, pendingIntent);
		manager.updateAppWidget(cn, rv);
		Log.v("“d’rInfo","SetClick()");
	}
	

}
