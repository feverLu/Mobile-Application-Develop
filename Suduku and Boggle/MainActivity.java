package edu.neu.madcourse.binbinlu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import edu.neu.madcourse.binbinlu.R;
import edu.neu.madcourse.binbinlu.boggle.Main;
import edu.neu.madcourse.binbinlu.bogglenet.LogIn;
import edu.neu.madcourse.binbinlu.sudoku.Sudoku;
import edu.neu.mobileclass.apis.KeyValueAPI;
import android.view.View;
import android.content.Intent;
import android.view.View.OnClickListener;


public class MainActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		View continueButton = findViewById(R.id.teammembers);
		continueButton.setOnClickListener(this);
    	
		View newButton = findViewById(R.id.sudoku);
		newButton.setOnClickListener(this);
		
    	View boggleButton = findViewById(R.id.boggle);
    	boggleButton.setOnClickListener(this);  
    	
    	View persistentboggleButton = findViewById(R.id.persistentboggle);
    	persistentboggleButton.setOnClickListener(this);  
    	
     	View errorButton = findViewById(R.id.createerror);
     	errorButton.setOnClickListener(this);
     	
     	View exitButton = findViewById(R.id.exit);
     	exitButton.setOnClickListener(this);
        
     	//set the text of the title bar
    	this.setTitle("Binbin Lu");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sudoku_activity_main, menu);
		return true;
	}
	  public void onClick(View v) {
	    	switch (v.getId()) {
	    	case R.id.teammembers:
	    		Intent i = new Intent(this, TeamMember.class);
	    		startActivity(i);
	    	break;
	    	case R.id.sudoku:
	        	Intent j = new Intent(this, Sudoku.class);
	        	startActivity(j);
	        break;
	    	case R.id.boggle:
	    		Intent l = new Intent(this, Main.class);
	    		startActivity(l);
	    	break;
	    	case R.id.persistentboggle:
//	    		KeyValueAPI.clear("coldest1030", "university");
//	    		KeyValueAPI.clearKey("coldest1030", "university", "online");
	    		KeyValueAPI.clearKey("coldest1030", "university", "invite");
	    		KeyValueAPI.clearKey("coldest1030", "university", "quit");
	    		Intent m = new Intent(this, LogIn.class);
	    		startActivity(m);
	    	break;
	    	case R.id.createerror:
	        	Intent k = new Intent(this, CreateError.class);
	        	startActivity(k);
	        break;
	    	case R.id.exit:
	    		finish();
	    	break;
	       
	    	// More buttons go here (if any) ...
	    	}
	    	}

	    }
