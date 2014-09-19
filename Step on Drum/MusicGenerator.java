package edu.neu.madcourse.binbinlu.finalproject;

import android.os.Handler;
import android.os.Message;
import java.util.TimerTask;


public class MusicGenerator extends TimerTask{
	private Handler mHandler;
	/*private long initialTime =System.currentTimeMillis();*/
	private boolean songOne;
	private boolean songTwo;
	private boolean songThree;
	private int counter;
	public MusicGenerator(Handler handler) {
		mHandler = handler;
		songOne =false;;
		songTwo = false;
		songThree = false;
		counter = -1;
	}
	
	@Override
	public void run() {
		/*long timeGap = System.currentTimeMillis() - initialTime;*/
		counter ++;
		if(counter ==0){
			
			sendMessage(Messages.PLAY_SONG_ONE);
			
			
		}else{
			if(counter ==600){
				
				sendMessage(Messages.PLAY_SONG_TWO);
				
			}else{
				if(counter==1800){
					
					sendMessage(Messages.PLAY_SONG_THREE);
					
				}
				
			}
		}
}
	
	private void sendMessage(int what) {
		if (mHandler != null) {
			Message message = Message.obtain(mHandler, what);
			mHandler.sendMessage(message);
		}
	}

	
	
	
	
}
