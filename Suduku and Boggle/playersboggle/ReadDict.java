package edu.neu.madcourse.binbinlu.playersboggle;

import java.io.IOException;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;

public class ReadDict extends AsyncTask<Void, Void, Void>{

	private static RevisedBloomFilter<String> mFilter;
	private Context mContext;
	
	public ReadDict (Context context, RevisedBloomFilter<String> filter) {
		mContext = context;
		mFilter = filter;
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		try {
			mFilter.readBit(mContext.getResources().getAssets().open(
					"boggle_bloom_filter", AssetManager.ACCESS_RANDOM));
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return null;
	}
}
