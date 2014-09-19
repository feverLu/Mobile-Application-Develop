package edu.neu.madcourse.binbinlu.bogglenet;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.neu.madcourse.binbinlu.MainActivity;
import edu.neu.madcourse.binbinlu.R;
import edu.neu.mobileclass.apis.KeyValueAPI;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
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

public class OnlineService extends Service {
	private Timer timer;
	private TimerTask timerTask;
	private Gson gson = new Gson();
	private String json;
	private LinkedList<State> onlineUsers;
	private LinkedList<State> checkUsers;
	private int onlineUserSize;
	private int checkUserSize;
	public static String myName;
	private NotificationManager notificationManager;
	private PowerManager powerManager;
	private PowerManager.WakeLock wakeLock;
	private ConditionVariable conditionVariable;
	private Vibrator vibrator;

	public void onCreate() {
		super.onCreate();
		Log.i("service¿ªÆô" ,"OK");
		Log.i("service¿ªÆô" ,"OK");
		Log.i("service¿ªÆô" ,"OK");
		
		
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		powerManager = (PowerManager) getSystemService(POWER_SERVICE);
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		conditionVariable = new ConditionVariable(false);

		onlineUsers = new LinkedList<State>();
		checkUsers = new LinkedList<State>();

		onlineUsers = getOnlineUsers();
		timer = new Timer();
		timerTask = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (conditionVariable.block(1000))
					OnlineService.this.stopSelf();
				onlineUsers = checkUsers;
				checkUsers = getOnlineUsers();
				isChanged();

			}
		};
		timer.schedule(timerTask, 1000, 5000);
	}

	public LinkedList<State> getOnlineUsers() {
		LinkedList<State> users = new LinkedList<State>();
		if (KeyValueAPI.isServerAvailable()) {
			if (KeyValueAPI.get("coldest1030", "university", "online") != null) {
				json = KeyValueAPI.get("coldest1030", "university", "online");
				Type listType = new TypeToken<LinkedList<State>>() {
				}.getType();
				users = gson.fromJson(json, listType);
				return users;
			}
		}
		return null;
	}

	public void isChanged() {
		Log.i("ischanged", "ok");
		Log.i("ischanged", "ok");
		Log.i("ischanged", "ok");
		// TODO:
		try {
			onlineUserSize = onlineUsers.size();
			checkUserSize = checkUsers.size();
			Log.i("try block", "ok");
			Log.i("try block", "ok");
			Log.i("try block", "ok");
			for (int i = 0; i < onlineUserSize; i++) {
				if (KeyValueAPI.isServerAvailable()) {
					String tmpInvite = gson.fromJson(KeyValueAPI.get(
						"coldest1030", "university", "invite"), String.class);
					if (tmpInvite != null) {
						if (tmpInvite.equals("inviting")) {
							Log.i("inviting", "ok");
							Log.i("inviting", "ok");
							Log.i("inviting", "ok");
							String opponentName = checkUsers.get(i).opponent;
							if ((opponentName != null)
									&& opponentName.equals(myName)) {
								startNotification("Someone is inviting you");
							}
						}
					}
				}
			}
		} catch (Exception e) {
			// another notification shows that bad service
			onlineUsers = checkUsers;
			startNotification("Bad Internet");
			return;
		}
	}

	public void startNotification( String text) {
		Log.i("startNotification", "ok");
		Log.i("startNotification", "ok");
		Log.i("startNotification", "ok");
		Intent notificationIntent = new Intent(this, Online.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		long[] vib = {50, 300, 300, 300};
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
			.setAutoCancel(true).setTicker(text)
			.setSmallIcon(R.drawable.unique_icon)
			.setContentTitle("A new message")
			.setVibrate(vib)
			.setOnlyAlertOnce(true)
			.setContentIntent(contentIntent);
		
		Intent lastThirdIntent = new Intent(this, MainActivity.class);
		Intent lastSecondIntent = new Intent(this, LogIn.class);
		Intent lastFirstIntent = new Intent(this, Online.class);
		
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(Online.class);
		stackBuilder.addNextIntent(lastThirdIntent);
		stackBuilder.addNextIntent(lastSecondIntent);
		stackBuilder.addNextIntent(lastFirstIntent);
		Log.i("stackBuilder", "ok");
		Log.i("stackBuilder", "ok");
		Log.i("stackBuilder", "ok");
		notificationManager.notify(1, builder.build());
//		notificationManager.cancel(1);
		wakeLock = powerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "hello world!");
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
        Log.i("onUnbind", "Service.onUnbind");   
       return super.onUnbind(i);
    }   
	
	public void onDestroy() {
		conditionVariable.open();
		timer.cancel();
		timerTask.cancel();
		super.onDestroy();
	}

}
