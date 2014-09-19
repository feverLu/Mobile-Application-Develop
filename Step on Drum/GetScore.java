package edu.neu.madcourse.binbinlu.finalproject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import edu.neu.mobileclass.apis.KeyValueAPI;

public class GetScore extends TimerTask{
	private static final String TAG ="GerScore";
	private Handler mHandler;
	/*private long initialTime =System.currentTimeMillis();*/
	private List<Map<String, Object>> highScore = new ArrayList<Map<String, Object>>();
	private HashMap<String, Object> map = new HashMap<String, Object>();
	private Gson gson = new Gson();
	
	private static final String TEAM_NAME ="youye";
	private static final String PASSWORD = "123";
    private static final String KEY ="stepondrumgamescore";
    private String deviceID;
    private boolean ifFound = false;
    
	
	public GetScore(Handler handler, String str) {
		mHandler = handler;
		deviceID = str;
		
	}
	
	@SuppressLint("NewApi")
	@Override
	public void run() {
		/*long timeGap = System.currentTimeMillis() - initialTime;*/
		
		try{
			Log.i(TAG,KeyValueAPI.get(TEAM_NAME, PASSWORD, KEY));
			
			
			if (KeyValueAPI.isServerAvailable()) {
				if (!KeyValueAPI.get(TEAM_NAME, PASSWORD, KEY).isEmpty()) {
					String str = KeyValueAPI.get(TEAM_NAME, PASSWORD, KEY);
					Type listType = new TypeToken<ArrayList<Map<String, Object>>>(){}.getType();
					highScore = gson.fromJson(str, listType);
				}
			}
			if (!highScore.isEmpty()) {
				int size = highScore.size();
				for (int i=0; i<size; i++) {
					if (highScore.get(i).get("DeviceID").equals(deviceID)) {
						StepOnDrumScore.scoreContent="Your Highest Score: "+highScore.get(i).get("Score")+
								". Rank: " +String.valueOf(i+1) +". Time: "+highScore.get(i).get("Time");
						sendMessage(Messages.REDRAW_SCORE);
						ifFound = true;
						break;
					}
				}
			}
			if(!ifFound){
				StepOnDrumScore.scoreContent="NO match record found.....";
				sendMessage(Messages.REDRAW_SCORE);
			}
			
		}catch(Exception e){
			
	}
		
}
	
	private void sendMessage(int what) {
		if (mHandler != null) {
			Message message = Message.obtain(mHandler, what);
			mHandler.sendMessage(message);
		}
	}

	
	
	
	
}