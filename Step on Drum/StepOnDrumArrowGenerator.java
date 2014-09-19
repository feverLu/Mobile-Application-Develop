package edu.neu.madcourse.binbinlu.finalproject;



import java.util.TimerTask;


import android.os.Handler;
import android.os.Message;


public class StepOnDrumArrowGenerator extends TimerTask{
	private Handler mHandler;
	/*private long initialTime =System.currentTimeMillis();*/
	private int counter ;
	
	public StepOnDrumArrowGenerator(Handler handler) {
		mHandler = handler;
		counter = -1;
	}
	
	@Override
	public void run() {
		/*long timeGap = System.currentTimeMillis() - initialTime;
*/		
		counter ++;
		sendMessage(Messages.REDRAW_ARROW);
		if(counter  == 0)
			sendMessage(Messages.PLAY_SONG_ONE);
		if(counter == 600)
			sendMessage(Messages.PLAY_SONG_TWO);
		if(counter==1800)
			sendMessage(Messages.PLAY_SONG_THREE);
		
		
		if(counter <=600){
			if((counter%60)==0)
				sendMessage(Messages.ADD_NEW_ARROW);
		}else{
			if(600<counter){
				if((counter%40)==0)
					sendMessage(Messages.ADD_NEW_ARROW);
			}/*else{
				if(1800<counter){
					if((counter%10)==0)
						sendMessage(Messages.ADD_NEW_ARROW);
				}
			}*/
		}
		
	}
	
	private void sendMessage(int what) {
		if (mHandler != null) {
			Message message = Message.obtain(mHandler, what);
			mHandler.sendMessage(message);
		}
	}

	
	
	
	
}
