package edu.neu.madcourse.binbinlu.playersboggle;

import java.util.TimerTask;

import android.os.Handler;
import android.os.Message;

public class GameTimer extends TimerTask {
	private Handler mHandler;
	private int longiness = 60;
	
	public GameTimer (Handler handler) {
		mHandler = handler;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (mHandler != null) {
			Message message = new Message();
			message.what = --longiness;
			mHandler.sendMessage(message);
		}
	}

}
