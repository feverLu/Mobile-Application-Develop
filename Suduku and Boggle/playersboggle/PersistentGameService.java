package edu.neu.madcourse.binbinlu.playersboggle;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import edu.neu.madcourse.binbinlu.MainActivity;
import edu.neu.madcourse.binbinlu.R;
import edu.neu.madcourse.binbinlu.bogglenet.LogIn;
import edu.neu.madcourse.binbinlu.bogglenet.Online;
import edu.neu.mobileclass.apis.KeyValueAPI;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.ConditionVariable;
import android.os.IBinder;
import android.os.Parcel;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

public class PersistentGameService extends Service {
	private Timer timer;
	private TimerTask timerTask;
	private Gson gson = new Gson();
	private String json;
	
	private NotificationManager notificationManager;
	private PowerManager powerManager;
	private PowerManager.WakeLock wakeLock;
	private ConditionVariable conditionVariable;
	private Vibrator vibrator;
	
	private String myName;
	private String peerName;
	private List<String> oldPeerWords;
	private List<String> newPeerWords;
	
	public void onCreate() {
		super.onCreate();
		Log.i("gameService¿ªÆô", "OK");
		Log.i("gameService¿ªÆô", "OK");
		Log.i("gameService¿ªÆô", "OK");
		
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		powerManager = (PowerManager) getSystemService(POWER_SERVICE);
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		conditionVariable = new ConditionVariable(false);
		
		oldPeerWords = new ArrayList<String>();
		newPeerWords = new ArrayList<String>();
		oldPeerWords = getPeerWords();
		
		timer = new Timer();
		timerTask = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (conditionVariable.block(1000))
					PersistentGameService.this.stopSelf();
				newPeerWords = getPeerWords();
				isChanged();
			}
		};
		timer.schedule(timerTask, 1000, 5000);
		
	}

	public int onStartCommand (Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		if (intent != null) {
			this.myName = intent.getStringExtra("myName");
			this.peerName = intent.getStringExtra("peerName");
		}
		return START_NOT_STICKY;
	}
	
	private void isChanged() {
//		try {
		int newSize, oldSize;
		Log.i("gameischanged", "OK");
			if(newPeerWords!=null) {
				newSize = newPeerWords.size();
				if (oldPeerWords!=null) {
					oldSize = oldPeerWords.size();
				} else oldSize = 0;
				Log.i(""+oldSize, newSize+"");
				Log.i(""+oldSize, newSize+"");
				Log.i(""+oldSize, newSize+"");
				Log.i(""+oldSize, newSize+"");
				if (newSize > oldSize)
					startNotification("Your opponent got more words");
			}
//		} catch (Exception e) {
//			return;
//		}
	}
	
	private List<String> getPeerWords() {
		Log.i("gamepeerWorlds", "OK");
		List<String> tmpWords = new ArrayList<String>();
		try{
			json = KeyValueAPI.get("coldest1030", "university", peerName);
			Type listType = new TypeToken<ArrayList<String>>(){}.getType();
			tmpWords = gson.fromJson(json, listType);
			return tmpWords;
		} catch(Exception e) {
			return null;
		}
	}
	
	private void startNotification(String text) {
		Log.i("startNotification", "OK");
		Intent notificationIntent = new Intent(this, PersistentGame.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		long[] vib = {50, 300, 0, 0};
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
			.setAutoCancel(true).setTicker(text)
			.setSmallIcon(R.drawable.unique_icon)
			.setContentTitle("A new Message")
			.setVibrate(vib)
			.setOnlyAlertOnce(true)
			.setContentIntent(contentIntent);
		
		Intent lastForthIntent = new Intent(this, MainActivity.class);
		Intent lastThirdIntent = new Intent(this, LogIn.class);
		Intent lastSecondIntent = new Intent(this, Online.class);
		Intent lastFirstIntent = new Intent(this, PersistentGame.class);
		
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(PersistentGame.class);
		stackBuilder.addNextIntent(lastForthIntent);
		stackBuilder.addNextIntent(lastThirdIntent);
		stackBuilder.addNextIntent(lastSecondIntent);
		stackBuilder.addNextIntent(lastFirstIntent);
		
		notificationManager.notify(2, builder.build());
		wakeLock = powerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "hello world");
		wakeLock.acquire(3000);
		//vibrator.vibrate(300);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return binder;
	}

	private final IBinder binder = new Binder() {
		@Override
		protected boolean onTransact(int code, Parcel data, Parcel reply,
				int flags) throws RemoteException {
			return super.onTransact(code, data, reply, flags);
		}
	};
	
	public boolean onUnbind(Intent i) {
		return super.onUnbind(i);
	}
	
	public void onDestroy() {
		timer.cancel();
		timerTask.cancel();
		super.onDestroy();
	}
	
	
}
