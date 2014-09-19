package edu.neu.madcourse.binbinlu.finalproject;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

@SuppressLint("UseSparseArrays")
public class StepOnDrumBeat {
	private SoundPool soundPool;
	Map<Integer, Integer> soundMap = new HashMap<Integer, Integer>();
	
	public StepOnDrumBeat(Context context, int resource) {
	soundPool = new SoundPool(3, AudioManager.STREAM_SYSTEM, 5);
	soundMap.put(1, soundPool.load(context, resource, 1));
	//soundMap.put(2, soundPool.load(context, R.raw.beat, 1));
	}
	
	public void play(){
		soundPool.play(soundMap.get(1), 1, 1, 1, 0, 1);
	}
}
