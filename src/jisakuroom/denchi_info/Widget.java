package jisakuroom.denchi_info;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

public class Widget extends AppWidgetProvider{
	private ConfChangedReceiver receiver;
	@Override
	public void onEnabled(Context context){
		try {
			receiver = new ConfChangedReceiver();
			IntentFilter filter = new IntentFilter(Intent.ACTION_CONFIGURATION_CHANGED);
			context.getApplicationContext().registerReceiver(receiver, filter);
		} catch (Exception e) {
			// TODO: handle exception
		}
		super.onEnabled(context);
	}
	@Override
    public void onUpdate(Context c, AppWidgetManager awm, int[] awi) {
		/*for (int id : awi) {
			
		}*/
		Log.v("電池Info AppWidgetProvider", "OnUpdate()");
		//クリック反応
		/*ComponentName cn = new ComponentName(c, Widget.class);
		RemoteViews rv = new RemoteViews(c.getPackageName(), R.layout.widgetview);
		Intent intent = new Intent(c, jisakuroom.denchi_info.Main.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(c, 0, intent, 0);
		rv.setOnClickPendingIntent(R.id.TextView01, pendingIntent);
		rv.setOnClickPendingIntent(R.id.textView02, pendingIntent);
		rv.setOnClickPendingIntent(R.id.RelativeLayout01_widget, pendingIntent);
		awm.updateAppWidget(cn, rv);*/
		//ここまで
		
		Intent in = new Intent(c, batteryservice.class);
		//in.putExtra("appWidgetId", id);
		c.startService(in);//サービス始動
		
		//ここまで
        super.onUpdate(c, awm, awi);
    }
	
	@Override
    public void onDisabled(Context context) {
		try {
			context.unregisterReceiver(receiver);
		} catch (Exception e) {
			// TODO: handle exception
		}
		Intent in = new Intent(context, batteryservice.class);
        try {
			context.stopService(in);
		} catch (Exception e) {
			// TODO: handle exception
		}
        Log.v("電池Info AppWidgetProvider", "onDisabled()");
        super.onDisabled(context);
    }
	
	public void SetClick(Context context){
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		ComponentName cn = new ComponentName(context, Widget.class);
		RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widgetview);
		Intent intent = new Intent(context, jisakuroom.denchi_info.Main.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		rv.setOnClickPendingIntent(R.id.TextView01, pendingIntent);
		rv.setOnClickPendingIntent(R.id.textView02, pendingIntent);
		rv.setOnClickPendingIntent(R.id.RelativeLayout01_widget, pendingIntent);
		manager.updateAppWidget(cn, rv);
		Log.v("電池Info","SetClick()");
	}
	
	

}
