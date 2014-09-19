package edu.neu.madcourse.binbinlu.finalproject;

import edu.neu.madcourse.binbinlu.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;


public class StepOnDrumAck extends Activity{
	private static final String TAG = "StepOnDrumAck";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.step_on_drum_ack);
	    AckPageAdapter adapter = new AckPageAdapter();
	    ViewPager myPager = (ViewPager) findViewById(R.id.ackpager);
	    myPager.setAdapter(adapter);
	    myPager.setCurrentItem(0);

		// Set up click listeners for all the buttons
		/*View startButton = findViewById(R.id.step_on_drum_start_button_tuto);
		startButton.setOnClickListener(this);*/
		
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

	
	}
