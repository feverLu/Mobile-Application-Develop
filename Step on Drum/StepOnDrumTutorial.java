package edu.neu.madcourse.binbinlu.finalproject;


import edu.neu.madcourse.binbinlu.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

public class StepOnDrumTutorial extends Activity implements OnClickListener{
	private static final String TAG = "StepOnDrumTuto";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.step_on_drum_tutorial);
	    TutoPagerAdapter adapter = new TutoPagerAdapter(this);
	    ViewPager myPager = (ViewPager) findViewById(R.id.mythreepanelpager);
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

	public void onClick(View v) {
		/*switch (v.getId()) {
		case R.id.step_on_drum_start_button_tuto:
			Intent a = new Intent(this, StepOnDrumGame.class);
			startActivity(a);
			break;
		
		}*/
	}




}