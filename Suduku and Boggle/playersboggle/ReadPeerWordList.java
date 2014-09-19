package edu.neu.madcourse.binbinlu.playersboggle;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import edu.neu.madcourse.binbinlu.bogglenet.Online;
import edu.neu.mobileclass.apis.KeyValueAPI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class ReadPeerWordList extends TimerTask {
	private Handler mHandler;
	private String mPeerName;
	private Gson gson = new Gson();
	private String json;
	private List<String> peerWords = new ArrayList<String>();
	private int longiness = 15;
	
	public ReadPeerWordList(Handler handler, String peerName) {
		mHandler = handler;
		mPeerName = peerName;
	} 	
	
	@SuppressLint("NewApi")
	@Override
	public void run() {
		// TODO Auto-generated method stub
		longiness = longiness - 1;
		if (!KeyValueAPI.get("coldest1030", "university", "quit").isEmpty()) {
			try {
				String str = KeyValueAPI.get("coldest1030", "university", "quit");
				String quitPerson = gson.fromJson(str, String.class);
				if (quitPerson.equals(mPeerName)&&(longiness>0)) {
					PersistentGame.isOppnentQuit = true;
					PersistentGame.totalScore = PersistentGame.totalScore + PersistentGame.oppnentScore;
					KeyValueAPI.clearKey("coldest1030", "university", "quit");
				}
			} catch (Exception e) {
				return;
			}
		}
		
		if (!KeyValueAPI.get("coldest1030", "University", mPeerName).isEmpty()) {
			try {
				json = KeyValueAPI.get("coldest1030", "University", mPeerName);
				Type listType = new TypeToken<ArrayList<String>>(){}.getType();
				peerWords = gson.fromJson(json, listType);
				if (peerWords != null) {
					sendMessage(longiness, peerWords);
				}
			} catch (Exception e) {
				return;
			}
		} else {
			sendMessage(longiness, null);
		}
		
		
	}
	
	private void sendMessage (int what, List<String> data) {
		if (mHandler != null) {
			Message message = Message.obtain(mHandler, what, data);
			mHandler.sendMessage(message);
		}
	}

}
