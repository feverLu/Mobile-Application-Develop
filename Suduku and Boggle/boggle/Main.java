package edu.neu.madcourse.binbinlu.boggle;

import edu.neu.madcourse.binbinlu.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.content.Intent;
import android.view.View.OnClickListener;

import android.app.AlertDialog;
import android.content.DialogInterface;

    public class Main extends Activity implements OnClickListener {
	
    	static int RESUME = 0;
    	static boolean CONTINUE = false;   
    	static int period1;
    	static int period2;
    	static int period3;
    	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.boggle_main);

		View newButton = findViewById(R.id.bogglenewgame);
		newButton.setOnClickListener(this);

		View resumeButton = findViewById(R.id.boggleresume);
		resumeButton.setOnClickListener(this);
		
		View rulesButton = findViewById(R.id.bogglerules);
		rulesButton.setOnClickListener(this);
		
		View acknowledgeButton = findViewById(R.id.boggleackowledge);
		acknowledgeButton.setOnClickListener(this);

		View exitButton = findViewById(R.id.boggleexit);
		exitButton.setOnClickListener(this);

//		int period1 = getPreferences(MODE_PRIVATE).getInt(BoggleSmall.PERIOD, 0);
//		int period2 = getPreferences(MODE_PRIVATE).getInt(BoggleMedium.PERIOD, 0);
//		int period3 = getPreferences(MODE_PRIVATE).getInt(BoggleLarge.PERIOD, 0);
		
		if ((period1<=0)&&(period2<=0)&&(period3<=0)){
			resumeButton.setEnabled(false);
		}
	}

	
	protected void onResume() {
	    super.onResume();
	    
	    View resumeButton = findViewById(R.id.boggleresume);
		if ((period1>0)||(period2>0)||(period3>0)){
			resumeButton.setEnabled(true);
			period1=0;
			period2=0;
			period3=0;
		}
	 }
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bogglenewgame:
			CONTINUE = false;
			openNewGameDialog();
			break;
			
		case R.id.boggleresume:
			CONTINUE = true;
			startGame(RESUME);
			break;
			
		case R.id.bogglerules:	
			Intent i = new Intent(Main.this, Rules.class);
			startActivity(i);
			break;
			
		case R.id.boggleackowledge:	
			Intent j = new Intent(Main.this, Acknowledgement.class);
			startActivity(j);
			break;
			
		case R.id.boggleexit:
			finish();
			break;
		default:
			break;
		}
	}

	// ask the user to choose the board size
	private void openNewGameDialog() {
		new AlertDialog.Builder(this)
				.setTitle(R.string.boggle_new_game_title)
				.setItems(R.array.boggle_board_size,
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {
								startGame(i);
							}
						}).show();
	}

	// start a new game with the given boggle board size
	private void startGame(int i) {
		 //start game here...
		switch (i) {
	      case 0:
	    	  Intent l = new Intent(Main.this, Small.class);
			  startActivity(l);
	    	  break;
	    	  
	      case 1:
	    	  Intent j = new Intent(Main.this, Medium.class);
			  startActivity(j);
	    	  break;
		    
	      case 2:
	    	  Intent k = new Intent(Main.this, Large.class);
			  startActivity(k);
	    	  break;
		    
		    default:
		    	break;
		}
	}
}
