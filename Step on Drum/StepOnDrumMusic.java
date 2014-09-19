package edu.neu.madcourse.binbinlu.finalproject;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;

public class StepOnDrumMusic extends Activity {
	private MediaPlayer song;
//	private Timer timer;
//	private TimerTask timerTask;
//	StepOnDrumBeat object = new StepOnDrumBeat(this, R.raw.beat);
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		song.setOnCompletionListener(
				new MediaPlayer.OnCompletionListener() {
					
					public void onCompletion(MediaPlayer mp) {
						// TODO Auto-generated method stub
						try {
							song.release();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
		
		song.setOnErrorListener(new MediaPlayer.OnErrorListener() {
			
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// TODO Auto-generated method stub
				try {
					song.release();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return false;
			}
		});
	}
	
	public StepOnDrumMusic(Context context, int resource) {
		song = MediaPlayer.create(context, resource);
	}
	
	public  void play() {
		try {
			if (song != null) {
				song.stop();
			}
			song.prepare();
			song.setLooping(false);
			song.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public  void stop () {
		try {
			if (song != null) {
				song.stop();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
