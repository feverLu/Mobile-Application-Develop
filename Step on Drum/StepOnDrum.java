package edu.neu.madcourse.binbinlu.finalproject;


import edu.neu.madcourse.binbinlu.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class StepOnDrum extends Activity implements OnClickListener {
	private static final String TAG = "StepOnDrum";
   
   
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.step_on_drum_main);

		// Set up click listeners for all the buttons
		View startButton = findViewById(R.id.step_on_drum_start_button);
		startButton.setOnClickListener(this);
		View ackButton = findViewById(R.id.step_on_drum_ack_button);
		ackButton.setOnClickListener(this);
		View scoreButton = findViewById(R.id.step_on_drum_score_button);
		scoreButton.setOnClickListener(this);
		View tutoButton = findViewById(R.id.step_on_drum_tutorial_button);
		tutoButton.setOnClickListener(this);
		
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.step_on_drum_start_button:
			Intent a = new Intent(this, StepOnDrumGame.class);
			startActivity(a);
			break;
		case R.id.step_on_drum_ack_button:
			Intent b = new Intent(this, StepOnDrumAck.class);
			startActivity(b);
			break;
		case R.id.step_on_drum_score_button:
			Intent c = new Intent(this, StepOnDrumScore.class);
			startActivity(c);
			break;
		case R.id.step_on_drum_tutorial_button:
			Intent d = new Intent(this, StepOnDrumTutorial.class);
			startActivity(d);
			break;
			
		}
	}
}
