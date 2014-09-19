package edu.neu.madcourse.binbinlu.finalproject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.neu.mobileclass.apis.KeyValueAPI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.WindowManager;
import android.widget.TextView;

public class StepOnDrumScore extends Activity{
	
	public static String scoreContent;
	private ScoreView scoreView;
	private static Handler handler;
	private GetScore getScore;
	private Timer timer;
	private TelephonyManager tm;
	
	
	protected void onCreate(Bundle savedInstanceState) {
				
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		initializeHandler();
		
		scoreContent = ">>>>>>>>>>>Connecting>>>>>>>>>>>";
		
		tm=(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		
		String deviceID=tm.getDeviceId().toString();
		getScore = new GetScore(handler,deviceID);
		/*musicGenerator = new MusicGenerator(handler);*/
		
		scoreView = new ScoreView(this);
		setContentView(scoreView);
		}
	
	@SuppressLint("HandlerLeak")
	private void initializeHandler() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case Messages.REDRAW_SCORE:
					redrawScore();
					break;
				default:
					break;
				}
			}
		};
	}
	
	private void redrawScore(){
		scoreView.invalidate();
		stopTimer();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		startTimer();
		
		
	}

	private void startTimer() {
		if (timer == null)
			timer = new Timer();
		
		timer.schedule(getScore,  0, 100000);
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		stopTimer();
	}
	private void stopTimer() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}
		
}
