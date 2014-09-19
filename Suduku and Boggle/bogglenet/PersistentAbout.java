package edu.neu.madcourse.binbinlu.bogglenet;

import edu.neu.madcourse.binbinlu.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class PersistentAbout extends Activity implements OnClickListener {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.persistent_about);
		
		View ruleButton = findViewById(R.id.persistent_rules);
		ruleButton.setOnClickListener(this);	
		
		View acknowledgeButton = findViewById(R.id.persistent_acknowledge);
		acknowledgeButton.setOnClickListener(this);
		

		View highScoreButton = findViewById(R.id.persistent_high_score);
		highScoreButton.setOnClickListener(this);
	}
	
	 public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
    	case R.id.persistent_rules:
    		Intent i = new Intent(this, PersistentRules.class);
    		startActivity(i);
    		break;
    	case R.id.persistent_acknowledge:
    		Intent j =  new Intent(this, PersistentAcknowledge.class);
    		startActivity(j);
    		break;
    	case R.id.persistent_high_score:
    		Intent k = new Intent(this, HighScore.class);
    		startActivity(k);
    		break;
    	default:
    		break;
		}
	 }
}
