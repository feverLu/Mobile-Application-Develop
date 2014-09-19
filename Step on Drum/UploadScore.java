package edu.neu.madcourse.binbinlu.finalproject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.neu.mobileclass.apis.KeyValueAPI;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class UploadScore extends TimerTask{
	private static final String TAG = "StepOnDrumUpLoadScore";
	private Handler mHandler;
	/*private long initialTime =System.currentTimeMillis();*/
	private List<Map<String, Object>> highScore = new ArrayList<Map<String, Object>>();
	private HashMap<String, Object> map = new HashMap<String, Object>();
	private Gson gson = new Gson();
	private StringBuffer scoreContent;
	private static final String TEAM_NAME ="youye";
	private static final String PASSWORD = "123";
    private static final String KEY ="stepondrumgamescore";
    private String deviceID;
    private int score;
    private boolean ifFound =false;
	
	public UploadScore(Handler handler,String str, int score) {
		mHandler = handler;
		deviceID = str;
		this.score = score;
		
	
	}
	
	@SuppressLint("NewApi")
	@Override
	public void run() {
		/*long timeGap = System.currentTimeMillis() - initialTime;*/
		
		try{
			Log.i(TAG,"entered run!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" );
				
				try {
					Gson gson = new Gson();
					List<Map<String, String>> highScore = new ArrayList<Map<String, String>>();
					
					
					if (!KeyValueAPI.get(TEAM_NAME, PASSWORD, KEY).isEmpty()) {
						String str = KeyValueAPI.get(TEAM_NAME,PASSWORD, KEY);
						Type listType = new TypeToken<ArrayList<Map<String, String>>>(){}.getType();
						highScore = gson.fromJson(str, listType);
					}
				//TelephonyManager tm=(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
				
				
				int size = highScore.size();
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("Score", String.valueOf(score));
				Calendar ca = Calendar.getInstance();
				String tim = (ca.getTime().getYear()+1900)+"-"+((ca.getTime().getMonth()+1)/10)+((ca.getTime().getMonth()+1)%10)
						+"-"+(ca.getTime().getDate()/10)+(ca.getTime().getDate()%10)+" "+
									(ca.getTime().getHours()/10)+(ca.getTime().getHours()%10)+
											":"+ca.getTime().getMinutes()/10+ca.getTime().getMinutes()%10+":"+
													ca.getTime().getSeconds()/10+ca.getTime().getSeconds()%10;
				map.put("Time", tim);
				map.put("DeviceID", deviceID);
				for (int i=0; i<size; i++) {
					
					if (highScore.get(i).get("DeviceID").equals(deviceID)) {
						ifFound = true;
						int c =Integer.valueOf(highScore.get(i).get("Score"));
						if (c<score){
							highScore.remove(i);
							highScore.add(map);
						}
					} 
				}
				if(!ifFound)
					highScore.add(map);
				
				size = highScore.size();
				for (int i=0; i<size; i++) {
					Map<String, String> m = highScore.get(i);
					int a = Integer.valueOf(m.get("Score"));
					for(int j = 1; j <size;j++){
						int b=Integer.valueOf(highScore.get(j).get("Score"));
						if(b>a){
							highScore.set(i, highScore.get(j));
							highScore.set(j,m);
						}
					}
				}
				String jsonString = gson.toJson(highScore);
				Log.d(TAG,"string list about to be uploaded: "+jsonString+" ............");
				KeyValueAPI.put(TEAM_NAME, PASSWORD, KEY, jsonString);
				
				sendMessage(Messages.UPLOAD_SCORE_FINISH);
			} catch (Exception e) {
				sendMessage(Messages.UPLOAD_SCORE_FINISH);
			}
	}catch(Exception e){
		sendMessage(Messages.UPLOAD_SCORE_FINISH);
	}
}
	
	private void sendMessage(int what) {
		if (mHandler != null) {
			Message message = Message.obtain(mHandler, what);
			mHandler.sendMessage(message);
		}
	}

	
	
	
	
}