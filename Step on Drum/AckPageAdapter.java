package edu.neu.madcourse.binbinlu.finalproject;


import edu.neu.madcourse.binbinlu.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class AckPageAdapter extends PagerAdapter {
	
   
	public int getCount() {
        return 2;
    }
    public Object instantiateItem(final View collection, int position) {
        LayoutInflater inflater = (LayoutInflater) collection.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        int resId = 0;
        View view = null;
        switch (position) {
        case 0:
            resId = R.layout.step_on_drum_ack_first;
            view = inflater.inflate(resId, null);
            break;
        case 1:
            resId = R.layout.step_on_drum_ack_second;
            view = inflater.inflate(resId, null);
            break;
       }
        
        ((ViewPager) collection).addView(view, 0);
        return view;
    }
    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);
    }
    @Override
    public Parcelable saveState() {
        return null;
    }
    
}

