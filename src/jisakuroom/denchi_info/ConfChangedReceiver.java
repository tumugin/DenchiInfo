package jisakuroom.denchi_info;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ConfChangedReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			Intent in = new Intent(context, batteryservice.class);
			//in.putExtra("appWidgetId", id);
			context.startService(in);//サービス始動
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}
