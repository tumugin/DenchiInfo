package jisakuroom.denchi_info;

import java.math.BigDecimal;
import java.math.RoundingMode;

import jisakuroom.denchi_info.R.string;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings.System;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

public class Main extends Activity {
	public TextView textView1;
	public TextView textView2;
	public TextView textView3;
	public TextView textView4;
	public TextView textView5;
	public TextView textView6;
	public TextView textView7;
	public String widgetPct;
	public String widgetTemp = "";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        textView1 = (TextView) findViewById(R.id.TextView01_main);
        textView2 = (TextView) findViewById(R.id.TextView02_main);
        textView3 = (TextView) findViewById(R.id.TextView03_main);
        textView4 = (TextView) findViewById(R.id.TextView04_main);
        textView5 = (TextView) findViewById(R.id.TextView05_main);
        textView6 = (TextView) findViewById(R.id.TextView06_main);
        textView7 = (TextView) findViewById(R.id.TextView07_main);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, getString(R.string.temp)).setIcon(android.R.drawable.ic_menu_preferences);
        
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case 1:
    		Intent intent = new Intent(this, jisakuroom.denchi_info.setting.class);
    		startActivity(intent);
    		return true;
    	}
    	return false;
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //String tani = prefs.getString(getString(R.string.tani_setting), "null");
        //Log.v("Temp",tani);
        //�o�b�e���[���̎�M�J�n
        IntentFilter filter=new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver,filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(widgetTemp.length() != 0){
        	AppWidgetManager awm = AppWidgetManager.getInstance(this);
			ComponentName cn = new ComponentName(this, Widget.class);
			RemoteViews rv = new RemoteViews(getPackageName(), R.layout.widgetview);
			rv.setTextViewText(R.id.textView02, widgetTemp);
			awm.updateAppWidget(cn, rv);
        }
        //�o�b�e���[���̎�M��~
        unregisterReceiver(batteryReceiver);
    }
    
    
    //�o�b�e��������M����u���[�h�L���X�g���V�[�o�[
    private BroadcastReceiver batteryReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
                //�[�d��Ԃ̎擾
                String statusStr="";
                int status=intent.getIntExtra("status",0);
                if (status==BatteryManager.BATTERY_STATUS_CHARGING) {
                    statusStr = getString(R.string.charging);//�[�d��
                } else if (status==BatteryManager.BATTERY_STATUS_DISCHARGING) {
                    statusStr = getString(R.string.discharging);//���d��
                } else if (status==BatteryManager.BATTERY_STATUS_FULL) {
                    statusStr = getString(R.string.full);//�[�d����
                } else if (status==BatteryManager.BATTERY_STATUS_NOT_CHARGING) {
                    statusStr = getString(R.string.discharging);//���d��
                } else if (status==BatteryManager.BATTERY_STATUS_UNKNOWN) {
                    statusStr = getString(R.string.unknown);//�s��
                } 
                textView6.setText(getString(R.string.status) + statusStr);
                
                //�v���O��ʂ̎擾
                String pluggedStr = getString(R.string.noplug);//�v���O����
                int plugged=intent.getIntExtra("plugged",0);
                if (plugged==BatteryManager.BATTERY_PLUGGED_AC) {
                    pluggedStr = getString(R.string.plugged_ac);//AC�A�_�v�^�[
                } else if (plugged==BatteryManager.BATTERY_PLUGGED_USB) {
                    pluggedStr = getString(R.string.usb);//USB
                }
                textView7.setText(getString(R.string.plugshubetu) + pluggedStr);
                

                //�o�b�e���[�ʂ̎擾
                int level=intent.getIntExtra("level",0);
                int scale=intent.getIntExtra("scale",0);
                int batt_pct = (level*100/scale);
                textView1.setText(getString(R.string.zanriyou) + batt_pct + "%");
                
                //�w���X��
                int health = intent.getIntExtra("health",0);
                String healthString = getString(R.string.health_unknown);//�s��
                switch (health) {
                case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                    healthString = getString(R.string.health_unknown);//�s��
                    break;
                case BatteryManager.BATTERY_HEALTH_GOOD:
                    healthString = getString(R.string.health_good);//�ǍD
                    break;
                case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                    healthString = getString(R.string.health_overheat);//�I�[�o�[�q�[�g
                    break;
                case BatteryManager.BATTERY_HEALTH_DEAD:
                    healthString = getString(R.string.health_dead);//�s��
                    break;
                case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                    healthString = getString(R.string.health_over_voltage);//�I�[�o�[�{���e�[�W
                    break;
                case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                    healthString = getString(R.string.health_unspecified_failure);//�擾���s
                    break;
                }
                textView2.setText(getString(R.string.helth) + healthString);

                //���x�̎擾
                float temperature = intent.getIntExtra("temperature",0) / 10;
                
                float fahrenheit = 0;
                if(intent.hasExtra("temperature")){
                	fahrenheit = 1.8f * temperature + 32;
                }
                
                BigDecimal bDecimal = new BigDecimal(String.valueOf(temperature));
                bDecimal.setScale(2, RoundingMode.CEILING);
                
                BigDecimal bDecimal2 = new BigDecimal(String.valueOf(fahrenheit));
                bDecimal2.setScale(2, RoundingMode.CEILING);
                
                //�E�B�W�F�b�g�X�V�p�f�[�^�[
                widgetPct = String.valueOf(batt_pct);
                
                //���x�̒P�ʂ��擾
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(jisakuroom.denchi_info.Main.this);
                String tani = prefs.getString(getString(R.string.tani_setting), "Celsius");
                if (tani.matches("Celsius") && intent.hasExtra("temperature")) {
					textView5.setText(getString(R.string.naibuondo) + bDecimal + "��");
					widgetTemp = String.valueOf(Math.round(temperature)) + "��";
				}else if(tani.matches("Fahrenheit") && intent.hasExtra("temperature")){
					textView5.setText(getString(R.string.naibuondo) + bDecimal2 + "F");
					widgetTemp = String.valueOf(Math.round(fahrenheit)) + "F";
				}else {
					textView5.setText(getString(R.string.health_unknown));
					widgetTemp = "--";
				}
                
                
                //�d�r���
                String technology = intent.getStringExtra("technology");
                textView4.setText(getString(R.string.shurui) + technology);
                //�d��
                int voltage = intent.getIntExtra("voltage", 0);
                textView3.setText(getString(R.string.denatu) + voltage + "mV");
                
            }
        }
    };
}