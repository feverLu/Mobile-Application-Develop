package edu.neu.madcourse.binbinlu.finalproject;

import edu.neu.madcourse.binbinlu.R;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;


public class TutoPagerAdapter extends PagerAdapter {
	private StepOnDrumTutorial stepOnDrumTutorial;
    public TutoPagerAdapter(StepOnDrumTutorial stepOnDrumTutorial) {
		// TODO Auto-generated constructor stub
    	this.stepOnDrumTutorial=stepOnDrumTutorial;
	}
	public int getCount() {
        return 3;
    }
    public Object instantiateItem(final View collection, int position) {
        LayoutInflater inflater = (LayoutInflater) collection.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        int resId = 0;
        View view = null;
        switch (position) {
        case 0:
            resId = R.layout.step_on_drum_tuto_first;
            view = inflater.inflate(resId, null);
            break;
        case 1:
            resId = R.layout.step_on_drum_tuto_second;
            view = inflater.inflate(resId, null);
            break;
        case 2:
        	
            resId = R.layout.step_on_drum_tuto_third;
            view = inflater.inflate(resId, null);
            ImageButton button=(ImageButton)view.findViewById(R.id.step_on_drum_start_button_tuto);
        	

            button.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                	collection.getContext().startActivity(new Intent(collection.getContext(), StepOnDrumGame.class));
                }
            });
           
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

