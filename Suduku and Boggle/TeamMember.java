package edu.neu.madcourse.binbinlu;

import android.app.Activity;
import android.os.Bundle;
import edu.neu.madcourse.binbinlu.R;
import edu.neu.madcourse.binbinlu.sudoku.Music;
import android.view.View;
import android.content.Intent;
import android.view.View.OnClickListener;



public class TeamMember extends Activity implements OnClickListener {
	
	
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.team_members);


	View deviceidButton = findViewById(R.id.device);
	deviceidButton.setOnClickListener(this);
}

protected void onResume() {
    super.onResume();
    Music.play(this, R.raw.cannon);
 }

@Override
protected void onPause() {
   super.onPause();
   Music.stop(this);
   // Save the current puzzle
}

public void onClick(View v) {
	switch (v.getId()) {
	case R.id.device:
		Intent i = new Intent(this, TeamMemberDeviceID.class);
		startActivity(i);

}
}
}

