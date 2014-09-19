package edu.neu.madcourse.binbinlu.finalproject;

import edu.neu.madcourse.binbinlu.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class StepOnDrumDes extends Activity implements OnClickListener{
	   @Override
	   protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.step_on_drum_des);
	      View stButton = findViewById(R.id.step_on_drum_start_on_des_button);
	      stButton.setOnClickListener(this);
	   }
	   
	   public void onClick(View v) {
		      switch (v.getId()) {
		      case R.id.step_on_drum_start_on_des_button:
		    	  Intent i = new Intent(this, StepOnDrum.class);
		          startActivity(i);
		          break;
		       
		      
		      }
	   }
}
