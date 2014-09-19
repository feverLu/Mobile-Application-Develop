package edu.neu.madcourse.binbinlu.finalproject;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.telephony.TelephonyManager;
import android.util.Log;

import android.view.KeyEvent;
import android.view.WindowManager;

import edu.neu.madcourse.binbinlu.R;
import edu.neu.mobileclass.apis.KeyValueAPI;


public class StepOnDrumGame extends Activity implements SensorEventListener {
	private static final String TAG = "StepOnDrumGame";
	
/*	private float mLastX, mLastY, mLastZ;*/
/*	private boolean mInitialized;*/

	private SensorManager mSensorManager;
    private Sensor mAccelerometer;
	private final static float NOISE = 0.005f;
	private final static float mNOISE =-0.005f;
	/*private final static int THRESHOLD = 60;
	private final static int mTHRESHOLD = -60;*/
	public StepOnDrumBeat boo;
	public StepOnDrumMusic musicOne;
	public StepOnDrumMusic musicTwo;
	public StepOnDrumMusic musicThree;
	private int lastNext = -1;
	
	public float[] values = new float[3];
	public float[] init = new float[] { 0.0f, 0.0f, 0.0f };
	public float[] old = new float[]{0.0f,0.0f,0.0f};
	public float[] cur =new float[]{0.0f,0.0f,0.0f}; 
	public float filter = 0.2f;
	
//	public int x, y, z;
	public long lastUpdateTime = 0;
	public boolean openSensorData= false;
	
	private StepOnDrumGameView gameView;
	public ArrayList <StepOnDrumArrow> arrowList;
	private static Handler handler;
	private StepOnDrumArrowGenerator arrowGenerater;
	private UploadScore uploadScore;
	private Timer scoreTimer;
	/*private MusicGenerator musicGenerator;*/
	private Timer timer;
/*	private Timer muTimer;*/
	/** Called when the activity is first created. */
	
	private Random random = new Random(System.nanoTime());
	private Random muRandom = new Random(System.nanoTime());
	
	public int score;
	public int miss;
	
	private TelephonyManager tm;
	private String deviceID;
	private static final String TEAM_NAME ="coldest1030";
	private static final String PASSWORD = "university";
    private static final String KEY ="stepondrumgamescore";

	@Override
	public void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		initializeHandler();
		arrowList= new ArrayList <StepOnDrumArrow>();
		arrowGenerater = new StepOnDrumArrowGenerator(handler);
		/*musicGenerator = new MusicGenerator(handler);*/
		boo = new StepOnDrumBeat(this,R.raw.beat);
		musicOne = new StepOnDrumMusic(this,R.raw.begin);
		musicTwo = getMusicTwo(this);
		musicThree = getMusicThree(this);
		
		
		/*mInitialized = false;*/
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		
		
		score =0;
		miss=0;
		gameView = new StepOnDrumGameView(this);
		setContentView(gameView);
		
		tm=(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		deviceID=tm.getDeviceId().toString();
		

	
	
	
	}
	private StepOnDrumMusic getMusicTwo(Context context){
		int i =muRandom.nextInt(2);
		StepOnDrumMusic mu = null;
		if(i == 0){
			mu = new StepOnDrumMusic(context,R.raw.one_one );
		}else{
			mu = new StepOnDrumMusic(context,R.raw.one_two );
		}
		return mu;
	}
	
	private StepOnDrumMusic getMusicThree(Context context){
		StepOnDrumMusic mu =null;
		int i =muRandom.nextInt(4);
		if(i == 0)
			mu = new StepOnDrumMusic(context,R.raw.two_one );
		if(i==1)
			mu = new StepOnDrumMusic(context,R.raw.two_two );
		if(i==2)
			mu= new StepOnDrumMusic(context,R.raw.two_three );
		if(i ==3)
			mu = new StepOnDrumMusic(context,R.raw.two_four );
		
		return mu;
		
	}
	
	@SuppressLint("HandlerLeak")
	private void initializeHandler() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case Messages.REDRAW_ARROW:
					redrawArrow();
					break;

				case Messages.ADD_NEW_ARROW:
					addNewArrow();
					break;
				case Messages.PLAY_SONG_ONE:
					playSongOne();
					break;
				case Messages.PLAY_SONG_TWO:
					playSongTwo();
					break;
				case Messages.PLAY_SONG_THREE:
					playSongThree();
					break;
				case Messages.UPLOAD_SCORE_FINISH:
					endGame();
					break;
				default:
					break;
				}
			}
		};
	}
	
	private void playSongOne(){
		
		musicOne.play();
	}
	
	private void playSongTwo(){
		musicOne.stop();
		Log.d(TAG, "music is "+musicOne);
		musicTwo.play();
		
	}
	private void playSongThree(){
		musicTwo.stop();
		
		musicThree.play();
		
	}
	
	private void redrawArrow(){
		gameView.invalidate();
		/*gameView.isOk = true;
		gameView.t.start();*/
	}
	
	private void addNewArrow(){
		int next = getNext();
		StepOnDrumArrow arrow = new StepOnDrumArrow((gameView.getHeight()*0.005f), next, gameView);
		arrowList.add(arrow);
	}
	private int getNext(){
		int i = random.nextInt(4);
		while(i==lastNext){
			i = random.nextInt(4);
		}
		lastNext = i;
		return i;
	}

	
	@Override
	protected void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
		startTimer();
		//gameView.resume();
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
		
		}
	private void startTimer() {
		if (timer == null)
			timer = new Timer();
		/*if(muTimer == null)
			muTimer =new Timer();*/
		//timer.schedule(arrowGenerater,  0, 50);
		timer.schedule(arrowGenerater,  0, 50);
		/*muTimer.schedule(musicGenerator, 0,50);*/
		
	}
	
	
	protected void onPause() {
		super.onPause();
		//endGame();
		stopTimer();
		mSensorManager.unregisterListener(this);
		finish();
		
	}
	
	private void stopTimer() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		if(scoreTimer!=null){
			scoreTimer.cancel();
			scoreTimer = null;
		}
		
		/*if (muTimer != null) {
			muTimer.cancel();
			muTimer = null;
			}*/
			if(musicOne!=null)
				musicOne.stop();
			if(musicTwo!=null)
				musicTwo.stop();
			if(musicThree!=null)
				musicThree.stop();
		
	}
	
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// can be safely ignored for this demo
		}
	
	
	public void onSensorChanged(SensorEvent event) {
		if(openSensorData){
	        long currentUpdateTime = System.currentTimeMillis(); 
	        long timeInterval = currentUpdateTime - lastUpdateTime; 
	        if ((int)timeInterval < 20) 
	            lastUpdateTime = currentUpdateTime; 
	        else {
	        	if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
		            if (old[0] == 0 && old[1] == 0 &&old[2] == 0) {
		                old[0] = filter*event.values[0];
		                old[1] = filter*event.values[1];
		                old[2] = filter*event.values[2];
		            }
		            values = event.values;
		            cur[0] = (1-filter)*old[0]+filter*values[0];		           
		            cur[1] = (1-filter)*old[1]+filter*values[1];		           
		            cur[2] = (1-filter)*old[2]+filter*values[2];
		            /*
		            old[0] = cur[0];
	                old[1] = cur[1];
	                old[2] = cur[2];
		            
	                x +=(int) cur[0];
	                y += (int)cur[1];
	                z += (int)cur[2];*/
	        	}
	        }
		}
	}
	
	
	
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
        	uploadScore();
        	//endGame();
        	return true;
        	
        	
        	
        	
        	
			
				    		 
        } else if (keyCode==KeyEvent.KEYCODE_HOME) {
        	uploadScore();
        	//endGame();
        	return true;
        } else {
        	return super.onKeyDown(keyCode, event);
        }
    }

	private void uploadScore(){
		uploadScore = new UploadScore(handler,deviceID, score);
		if (scoreTimer == null)
			scoreTimer = new Timer();
		/*if(muTimer == null)
			muTimer =new Timer();*/
		//timer.schedule(arrowGenerater,  0, 50);
		scoreTimer.schedule(uploadScore,  0, 100000);
	}
	
	/*public Bitmap getBg(int c){
		if(c%24== 0||c%24== 1)
			return BitmapFactory.decodeResource(getResources(), R.drawable.background1);
		if(c%24== 2||c%24== 3)
			return BitmapFactory.decodeResource(getResources(), R.drawable.background2);
		if(c%24== 4||c%24== 5)
			return BitmapFactory.decodeResource(getResources(), R.drawable.background3);
		if(c%24== 6||c%24== 7)
			return BitmapFactory.decodeResource(getResources(), R.drawable.background4);
		if(c%24== 8||c%24== 9)
			return BitmapFactory.decodeResource(getResources(), R.drawable.background5);
		if(c%24== 10||c%24== 11)
			return BitmapFactory.decodeResource(getResources(), R.drawable.background6);
		if(c%24== 12||c%24== 13)
			return BitmapFactory.decodeResource(getResources(), R.drawable.background7);
		if(c%24== 14||c%24== 15)
			return BitmapFactory.decodeResource(getResources(), R.drawable.background8);
		if(c%24== 16||c%24== 17)
			return BitmapFactory.decodeResource(getResources(), R.drawable.background9);
		if(c%24== 18||c%24== 19)
			return BitmapFactory.decodeResource(getResources(), R.drawable.background10);
		if(c%24== 20||c%24== 21)
			return BitmapFactory.decodeResource(getResources(), R.drawable.background11);
		if(c%24== 22||c%24== 23)
			return BitmapFactory.decodeResource(getResources(), R.drawable.background12);
		return BitmapFactory.decodeResource(getResources(), R.drawable.hellomad);
	}*/
	
	/*public Bitmap getBitmap(StepOnDrumArrow arrow){
		if(arrow.direction == 0){
			if(!arrow.detected)
				return BitmapFactory.decodeResource(getResources(), R.drawable.arrow_left);
			else
				return BitmapFactory.decodeResource(getResources(), R.drawable.arrow_left_detected);
		}
		if(arrow.direction == 1){
			if(!arrow.detected)
				return BitmapFactory.decodeResource(getResources(), R.drawable.arrow_right);
			else
				return BitmapFactory.decodeResource(getResources(), R.drawable.arrow_right_detected);
		}
		if(arrow.direction == 2){
			if(!arrow.detected)
				return BitmapFactory.decodeResource(getResources(), R.drawable.arrow_up);
			else
				return BitmapFactory.decodeResource(getResources(), R.drawable.arrow_up_detected);
		}
		if(arrow.direction == 3){
			if(!arrow.detected)
				return BitmapFactory.decodeResource(getResources(), R.drawable.arrow_down);
			else
				return BitmapFactory.decodeResource(getResources(), R.drawable.arrow_down_detected);
		}
		return BitmapFactory.decodeResource(getResources(), R.drawable.hellomad);
	}*/
	
	
	
	public boolean ifDetected (StepOnDrumArrow arrow){
		if(arrow.direction ==0){
			if (cur[0]>NOISE){
				clearXYZ();
				drumBoo();
				score+=10;
				return true;}
		}else
		if(arrow.direction ==1){
			if (cur[0]<mNOISE){
				clearXYZ();
				drumBoo();
				score+=10;
				return true;}
		}else
		if(arrow.direction ==2){
			if(cur[1]<=0&&cur[2]>=0&&(Math.sqrt(cur[1]*cur[1]+cur[2]*cur[2])>NOISE ) ){
				clearXYZ();
				drumBoo();
				score+=10;
				return true;}				 
		}else
		if(arrow.direction ==3){
			if(cur[1]>=0&&cur[2]<=0&&(Math.sqrt(cur[1]*cur[1]+cur[2]*cur[2])>NOISE ) ){
				clearXYZ();
				drumBoo();
				score+=10;
				return true;}	
		}
		clearXYZ();
		miss+=1;
		if(miss == 10)
			uploadScore();
		return false;
	}
	private void endGame(){
		stopTimer();
		mSensorManager.unregisterListener(this);
		new AlertDialog.Builder(this)
	    .setTitle("Game Over! "+"You got: " +String.valueOf(score)+" points.")
	    .setCancelable(false)
	    .setItems(R.array.bogglegameover,
	     new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialoginterface,
	              int i) {
	        	finish();
	        }
	     })
	    .show();
		
	}
	
	
	/*public int getColor(){
		int maxYZ = Math.max(Math.abs(y), Math.abs(z));
		int maxXYZ = Math.max(Math.abs(x), maxYZ);
		if(Math.abs(maxXYZ)<=NOISE)
			return 0;
		if(maxXYZ == Math.abs(x)){
			if(x>0){
				drumBoo();
				clearXYZ();
				return 1;
			}
			else{
				drumBoo();
				clearXYZ();
				return 2;
			}
		}else{
			if(Math.abs(y)>Math.abs(z)){
				if(y<0){
					drumBoo();
					clearXYZ();
					return 3;
				}	
				else {
					drumBoo();
					clearXYZ();
					return 4;
				
				}
			}else{
				if(z<0){
					drumBoo();
					clearXYZ();
					return 4;
				}
				else {
					drumBoo();
					clearXYZ();
					return 3;
				}
			}
		}
}*/

	private void drumBoo(){
		
		boo.play();
	}
	
	private void clearXYZ(){
		cur[0]=0.0f;
		cur[1]=0.0f;
		cur[2]=0.0f;
		
	}
	
	public float getFrontEnd() {
		float frontEnd = (float) Math.sqrt(cur[1]*cur[1]+cur[2]*cur[2]);
		return frontEnd;
	}
	
}
