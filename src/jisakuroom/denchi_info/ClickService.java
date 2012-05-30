package jisakuroom.denchi_info;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ClickService extends Service{
	@Override
    public void onStart(Intent intent, int startId) {
		for(int appwidgetid : intent.getIntArrayExtra("widgets")){
			
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
