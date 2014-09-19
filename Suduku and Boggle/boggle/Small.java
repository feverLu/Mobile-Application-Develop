package edu.neu.madcourse.binbinlu.boggle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import edu.neu.madcourse.binbinlu.R;
import edu.neu.madcourse.binbinlu.sudoku.Music;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Small extends Activity implements OnClickListener {
	private int totalScore = 0;
	private char randomChar;
	private char proportion[] = new char[100];
	private String buttonValue[] = new String[4 * 4];
	private Button charButton[] = new Button[4 * 4];
	private Integer arrayList[] = new Integer[8 * 4];
	private ArrayList<String> ifAppeared = new ArrayList<String>();
	private ArrayList<Integer> pressedButton = new ArrayList<Integer>();
	private String textContent;
	private HashSet<String> set = new HashSet<String>();
	private HashSet<String> bonnus = new HashSet<String>();
	private HashMap<String, HashSet<String>> hashMap = new HashMap<String, HashSet<String>>();
	static Timer timer;
	static TimerTask timerTask;
	static Handler handler;
	static int period;
	boolean stop=false;
	boolean alreadyback = false;
	
	private static final String TOTALSCORE = "score";
	private static final String BUTTONVALUE = "buttonvalue";
	private static final String IFAPPEARED = "ifappeared";
    static final String PERIOD = "period";
    private ListView listView;
    
    
  	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.boggle_game_small);

		Main.RESUME = 0;
		View pauseButton = findViewById(R.id.boggle_pause);
		pauseButton.setOnClickListener(this);
		
		if(Main.CONTINUE){
			preferenceBack();
			alreadyback = true;
			timeStarter();
		}	
		else {
			SetBoardSmall();
			timeStarter();
		}
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++) {
				charButton[4 * i + j].setOnClickListener(this);
			}
//		listView = (ListView) findViewById(R.id.boggle_notes);	
//		String values[] = new String[ifAppeared.size()];
//		for (int i=0; i<ifAppeared.size(); i++){
//			values[i] = ifAppeared.get(i).toString();
//		}
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.two_line_list_item, android.R.id.text1, values);
//		listView.setAdapter(adapter);
		
		AssetManager aassets = this.getAssets();
		InputStream iiStream = null;
		InputStreamReader iiReader = null;
		BufferedReader bbReader = null;
		try{
			iiStream = aassets.open("target.txt");
			iiReader = new InputStreamReader(iiStream, "UTF-8");
			bbReader = new BufferedReader (iiReader);
			String tempString = null;
			while((tempString=bbReader.readLine())!=null){
				bonnus.add(tempString);
			}
			
			bbReader.close();
		}catch (IOException e){
			e.printStackTrace();
		}finally{
			if(bbReader!=null){
				try{
					bbReader.close();
				}catch(IOException err){
					err.printStackTrace();
				}
			}
		}
}


	protected void onResume() {
	    super.onResume();
	    Music.play(this, R.raw.guzheng);
	    if(Main.CONTINUE){
	    	 if(alreadyback){
	 	    	alreadyback = false;
	 	    }
	 	    else {
	 	    	preferenceBack(); 
	 	    	timeStarter();
	 	    }
	    }
	 }

	protected void preferenceBack(){
		totalScore = getPreferences(MODE_PRIVATE).getInt(TOTALSCORE, 0);

		TextView boggleScore = (TextView) findViewById(R.id.boggle_score);
	    boggleScore.setText("Score:  " + totalScore + ""); 
		buttonValue = getPreferences(MODE_PRIVATE).getString(BUTTONVALUE, "").substring(1, getPreferences(MODE_PRIVATE).getString(BUTTONVALUE, "").length()-1).split(", ");
		charButton[4 * 0 + 0] = (Button) findViewById(R.id.boggle_1);
		charButton[4 * 0 + 0].setText(buttonValue[4*0 + 0 ] + "");
		charButton[4 * 0 + 1] = (Button) findViewById(R.id.boggle_2);
		charButton[4 * 0 + 1].setText(buttonValue[4*0 + 1 ] + "");
		charButton[4 * 0 + 2] = (Button) findViewById(R.id.boggle_3);
		charButton[4 * 0 + 2].setText(buttonValue[4*0 + 2 ] + "");
		charButton[4 * 0 + 3] = (Button) findViewById(R.id.boggle_4);
		charButton[4 * 0 + 3].setText(buttonValue[4*0 + 3 ] + "");
		charButton[4 * 1 + 0] = (Button) findViewById(R.id.boggle_5);
		charButton[4 * 1 + 0].setText(buttonValue[4*1 + 0 ] + "");
		charButton[4 * 1 + 1] = (Button) findViewById(R.id.boggle_6);
		charButton[4 * 1 + 1].setText(buttonValue[4*1 + 1 ] + "");
		charButton[4 * 1 + 2] = (Button) findViewById(R.id.boggle_7);
		charButton[4 * 1 + 2].setText(buttonValue[4*1 + 2 ] + "");
		charButton[4 * 1 + 3] = (Button) findViewById(R.id.boggle_8);
		charButton[4 * 1 + 3].setText(buttonValue[4*1 + 3 ] + "");
		charButton[4 * 2 + 0] = (Button) findViewById(R.id.boggle_9);
		charButton[4 * 2 + 0].setText(buttonValue[4*2 + 0 ] + "");
		charButton[4 * 2 + 1] = (Button) findViewById(R.id.boggle_10);
		charButton[4 * 2 + 1].setText(buttonValue[4*2 + 1 ] + "");
		charButton[4 * 2 + 2] = (Button) findViewById(R.id.boggle_11);
		charButton[4 * 2 + 2].setText(buttonValue[4*2 + 2 ] + "");
		charButton[4 * 2 + 3] = (Button) findViewById(R.id.boggle_12);
		charButton[4 * 2 + 3].setText(buttonValue[4*2 + 3 ] + "");
		charButton[4 * 3 + 0] = (Button) findViewById(R.id.boggle_13);
		charButton[4 * 3 + 0].setText(buttonValue[4*3 + 0 ] + "");
		charButton[4 * 3 + 1] = (Button) findViewById(R.id.boggle_14);
		charButton[4 * 3 + 1].setText(buttonValue[4*3 + 1 ] + "");
		charButton[4 * 3 + 2] = (Button) findViewById(R.id.boggle_15);
		charButton[4 * 3 + 2].setText(buttonValue[4*3 + 2 ] + "");
		charButton[4 * 3 + 3] = (Button) findViewById(R.id.boggle_16);
		charButton[4 * 3 + 3].setText(buttonValue[4*3 + 3 ] + "");
		
		String mm = getPreferences(MODE_PRIVATE).getString(IFAPPEARED, "");
		int lengthAppeared = mm.length();	
		String nn = null;
		TextView boggleNotes = (TextView) findViewById(R.id.boggle_notes);
		boggleNotes.setText("Notes");
		ifAppeared.clear();
		int sigh = 4;
		for (int i=0; i<lengthAppeared; i++){
			if (mm.charAt(i)>='a' && mm.charAt(i)<='z'){
				nn = nn+mm.charAt(i);
			}
			else if (mm.charAt(i) == ','){
				ifAppeared.add(nn.substring(sigh, nn.length()));
				boggleNotes.append("\n");
				boggleNotes.append(nn.substring(sigh, nn.length()));
				sigh = nn.length();
			}			
		}	
		if (nn!=null) {
			ifAppeared.add(nn.substring(sigh, nn.length()));
			boggleNotes.append("\n");
			boggleNotes.append(nn.substring(sigh, nn.length()));
		}
		period = getPreferences(MODE_PRIVATE).getInt(PERIOD, 0);
		stop = !stop;		
	}
	
	@Override
	protected void onPause() {
	   super.onPause();
	   Music.stop(this);
	   BoggleMusicMusic.stop(this);
	   // Save the current state
	   this.getPreferences(MODE_PRIVATE).edit().putInt(TOTALSCORE, totalScore).commit();
	   this.getPreferences(MODE_PRIVATE).edit().putString(BUTTONVALUE, Arrays.toString(buttonValue)).commit();
	   this.getPreferences(MODE_PRIVATE).edit().putString(IFAPPEARED, ifAppeared.toString()).commit();
	   this.getPreferences(MODE_PRIVATE).edit().putInt(PERIOD, period).commit();
	   Main.period1=period;
	   timer.cancel();
	}
	
	void countDown(){
		BoggleMusicMusic.play(this, R.raw.count);
	}
	void stopBackGroundMusic() {
		Music.stop(this);
	}
	
	void timeStarter(){

		if (!stop){
			period=60;
		}
		else{
			stop=!stop;
		}
		timer = new Timer();
	    final TextView timeTextView = (TextView) findViewById(R.id.boggle_time);
	    
		timerTask = new TimerTask(){
			public void run(){
					Message msg = new Message();
					if (period>0) {
						msg.what = period--;
					}
					handler.sendMessage(msg);	
			}
		};

		handler = new Handler(){
			public void handleMessage(Message msg){
				super.handleMessage(msg);
				if(msg.what>0){
					timeTextView.setText("" + msg.what + "   seconds");
					if (msg.what==11){
						stopBackGroundMusic();
					}
					if (msg.what<11){
						countDown();
					}
				}
				else{
					timeTextView.setText("" + 0 + "   seconds");
					timer.cancel();
					finish();
				}
			}
		};	
		timer.schedule(timerTask, 1000, 1000);	
		}
	
    private void duplicateDialog() {
    	Toast toast;
		toast = Toast.makeText(getApplicationContext(), "Duplicate Input",
				Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
    }
	
	private void falseDialog() {
		Toast toast;
		toast = Toast.makeText(getApplicationContext(), "Invalid Input",
				Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	
	private void rightDialog(){
		BoggleMusic.play(this, R.raw.right);
//		Toast toast;
//		toast = Toast.makeText(getApplicationContext(), "Bingo!",
//				Toast.LENGTH_LONG);
//		toast.setGravity(Gravity.CENTER, 0, 0);
//		toast.show();
	}

 	public void readFile(String str) {
		String dictName = str.charAt(0)+".txt";
		AssetManager assets = this.getAssets();
		InputStream iStream = null;
		InputStreamReader iReader = null;
		BufferedReader bReader = null;
		try{
			iStream = assets.open(dictName);
			iReader = new InputStreamReader(iStream, "UTF-8");
			bReader = new BufferedReader (iReader);
			String tempString = null;
			while((tempString=bReader.readLine())!=null){
				set.add(tempString);
			}
			bReader.close();
		}catch (IOException e){
			e.printStackTrace();
		}finally{
			if(bReader!=null){
				try{
					bReader.close();
				}catch(IOException err){
					err.printStackTrace();
				}
			}
		}
	}

	protected void sCheckString(){
		int arraySize = pressedButton.size();
		TextView boggleNotes= new TextView(this);
		boggleNotes = (TextView) findViewById(R.id.boggle_notes);
        
		switch (arraySize) {
	      case 1:
	    	  break;
	    	  
	      case 2:
	    	  textContent = textContent.subSequence(textContent.length()-1, textContent.length()) + "";
	    	  boggleNotes.append("\n" +textContent);
	    	  if(hashMap.containsKey(textContent)){
	    	  }
	    	  else{
			     readFile(textContent);
			     hashMap.put(textContent, set);
			  }
    	      break;
    	      
	      default:
		      pressedButton.toArray(arrayList);
              int y2 = arrayList[arraySize-1];
              int x2 = arrayList[arraySize-2];
              int y1 = arrayList[arraySize-3];
	          int x1 = arrayList[arraySize-4];	

	          if ((Math.abs(x1-x2)<2)&&(Math.abs(y1-y2)<2)){
	        	  if ((Math.abs(x1-x2)==0)&&(Math.abs(y1-y2)==0)){
	        		  //press the button twice which means it is the end of the word, then detect if the word is in wordlist  	 
	        		  textContent = textContent.substring(0, textContent.length()-1);
	        		  boolean i = set.contains(textContent);
	        		  TextView boggleScore = new TextView(this);
	        		  if (i){
	        			  rightDialog();
	        			//Bonnus word
	        			  if (bonnus.contains(textContent)){
	        					Toast toast;
	        					toast = Toast.makeText(getApplicationContext(), "Bonnus!",
	        							Toast.LENGTH_SHORT);
	        					toast.setGravity(Gravity.CENTER, 0, 0);
	        					toast.show();
	        				    totalScore = totalScore+2;
	        			  }
	        			
	        			  //if letters of the word is less than 5, the score add 1
	        			  if(textContent.length()<5){
	        				  boolean j = ifAppeared.contains(textContent);
	        				  if (j){
	        					  pressedButton.clear();
	           			          textContent=""; 
	        				  }
	        				  else {
	        					  ifAppeared.add(textContent);
	        					  totalScore = totalScore + 1;
		        			      pressedButton.clear();
	           			          textContent="";
		        			      boggleScore = (TextView) findViewById(R.id.boggle_score);
		        			      boggleScore.setText("Score:  " + totalScore + ""); 
	        				  }
	        			  }
                          //if the number of the letters of the word is no less than 5	        			  
	        			  else {
	        				  boolean k = ifAppeared.contains(textContent);
	        				  if (k){ 
		        			      pressedButton.clear();
	           			          textContent="";
	        				  }
	        				  else{
	        					  ifAppeared.add(textContent);
	        					  totalScore = totalScore + textContent.length() - 3;
	        			          boggleScore = (TextView) findViewById(R.id.boggle_score);
		        			      boggleScore.setText("Score:  " + totalScore + ""); 
		        			      pressedButton.clear();
	           			          textContent="";
	        				  }
	        			   }
	        		  }
	        		  else {
	        			  //not found the word, vibrate
	        			  ifAppeared.add(textContent);
	        			  pressedButton.clear();
       			          textContent="";
	        			  Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
	        			  vibrator.vibrate(300);  
	        		  } 
	        	  }
	            else {        
        		//display the inputed word in notes   
	            	 boolean ifSame = false;
	            	  if (pressedButton.size()>=6) {
	            		  for (int same=0; same<(pressedButton.size()-5); ){
		            		  if ((pressedButton.get(same)==x2)&&(pressedButton.get(same+1)==y2)) {
		            			  duplicateDialog();
		            			  pressedButton.remove(pressedButton.size()-1);
		          	        	  pressedButton.remove(pressedButton.size()-1);
		            	          ifSame = true;
		            		  }
		            		  same = same + 2;
		              	  }
	            	  }
         		  if (!ifSame) {
        			      boggleNotes.append("" + (textContent.charAt(textContent.length()-1)));
         		  }
	                }

	          }
	          
	        //player typed an invalid button that is not next to the previous button
	        else{
	        	falseDialog();
	        	pressedButton.remove(pressedButton.size()-1);
	        	pressedButton.remove(pressedButton.size()-1);
	        	}
	          break;
	          }
		}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.boggle_pause:
			timer.cancel();	
			Main.CONTINUE=true;
			Intent i = new Intent(Small.this, Pause.class);
			stop=!stop;
			startActivity(i);
			timeStarter();	
			break;
		case R.id.boggle_1:
			pressedButton.add(0);
			pressedButton.add(0);
			textContent = textContent + buttonValue[4 * 0 + 0];
			sCheckString();
			break;
		case R.id.boggle_2:
			pressedButton.add(0);
			pressedButton.add(1);
			textContent = textContent + buttonValue[4 * 0 + 1];
			sCheckString();
			break;
		case R.id.boggle_3:
			pressedButton.add(0);
			pressedButton.add(2);
			textContent = textContent + buttonValue[4 * 0 + 2];
			sCheckString();
			break;
		case R.id.boggle_4:
			pressedButton.add(0);
			pressedButton.add(3);
			textContent = textContent + buttonValue[4 * 0 + 3];
			sCheckString();
			break;
		case R.id.boggle_5:
			pressedButton.add(1);
			pressedButton.add(0);
			textContent = textContent + buttonValue[4 * 1 + 0];
			sCheckString();
			break;
		case R.id.boggle_6:
			pressedButton.add(1);
			pressedButton.add(1);
			textContent = textContent + buttonValue[4 * 1 + 1];
			sCheckString();
			break;
		case R.id.boggle_7:
			pressedButton.add(1);
			pressedButton.add(2);
			textContent = textContent + buttonValue[4 * 1 + 2];
			sCheckString();
			break;
		case R.id.boggle_8:
			pressedButton.add(1);
			pressedButton.add(3);
			textContent = textContent + buttonValue[4 * 1 + 3];
			sCheckString();
			break;
		case R.id.boggle_9:
			pressedButton.add(2);
			pressedButton.add(0);
			textContent = textContent + buttonValue[4 * 2 + 0];
			sCheckString();
			break;
		case R.id.boggle_10:
			pressedButton.add(2);
			pressedButton.add(1);
			textContent = textContent + buttonValue[4 * 2 + 1];
			sCheckString();
			break;
		case R.id.boggle_11:
			pressedButton.add(2);
			pressedButton.add(2);
			textContent = textContent + buttonValue[4 * 2 + 2];
			sCheckString();
			break;
		case R.id.boggle_12:
			pressedButton.add(2);
			pressedButton.add(3);
			textContent = textContent + buttonValue[4 * 2 + 3];
			sCheckString();
			break;
		case R.id.boggle_13:
			pressedButton.add(3);
			pressedButton.add(0);
			textContent = textContent + buttonValue[4 * 3 + 0];
			sCheckString();
			break;
		case R.id.boggle_14:
			pressedButton.add(3);
			pressedButton.add(1);
			textContent = textContent + buttonValue[4 * 3 + 1];
			sCheckString();
			break;
		case R.id.boggle_15:
			pressedButton.add(3);
			pressedButton.add(2);
			textContent = textContent + buttonValue[4 * 3 + 2];
			sCheckString();
			break;
		case R.id.boggle_16:
			pressedButton.add(3);
			pressedButton.add(3);
			textContent = textContent + buttonValue[4 * 3 + 3];
			sCheckString();
			break;
		}
	}

	public char randomChar() {
		// produce a random string
		for (int k = 0; k < 8; k++)
			proportion[k] = 'a';
		for (int k = 8; k < 10; k++)
			proportion[k] = 'b';
		for (int k = 10; k < 14; k++)
			proportion[k] = 'c';
		for (int k = 14; k < 17; k++)
			proportion[k] = 'd';
		for (int k = 17; k < 28; k++)
			proportion[k] = 'e';
		for (int k = 28; k < 29; k++)
			proportion[k] = 'f';
		for (int k = 29; k < 31; k++)
			proportion[k] = 'g';
		for (int k = 31; k < 34; k++)
			proportion[k] = 'h';
		for (int k = 34; k < 43; k++)
			proportion[k] = 'i';
		for (int k = 43; k < 44; k++)
			proportion[k] = 'j';
		for (int k = 44; k < 45; k++)
			proportion[k] = 'k';
		for (int k = 45; k < 50; k++)
			proportion[k] = 'l';
		for (int k = 50; k < 53; k++)
			proportion[k] = 'm';
		for (int k = 53; k < 60; k++)
			proportion[k] = 'n';
		for (int k = 60; k < 67; k++)
			proportion[k] = 'o';
		for (int k = 67; k < 70; k++)
			proportion[k] = 'p';
		for (int k = 70; k < 71; k++)
			proportion[k] = 'q';
		for (int k = 71; k < 78; k++)
			proportion[k] = 'r';
		for (int k = 78; k < 86; k++)
			proportion[k] = 's';
		for (int k = 86; k < 92; k++)
			proportion[k] = 't';
		for (int k = 92; k < 95; k++)
			proportion[k] = 'u';
		for (int k = 95; k < 96; k++)
			proportion[k] = 'v';
		for (int k = 96; k < 97; k++)
			proportion[k] = 'w';
		for (int k = 97; k < 98; k++)
			proportion[k] = 'x';
		for (int k = 98; k < 99; k++)
			proportion[k] = 'y';
		for (int k = 99; k < 100; k++)
			proportion[k] = 'z';

		Random rand = new Random(System.nanoTime());
		randomChar = proportion[rand.nextInt(100)];
		return randomChar;
	}

	// set the chars on the board
	public void SetBoardSmall() {

		charButton[4 * 0 + 0] = (Button) findViewById(R.id.boggle_1);
		charButton[4 * 0 + 0].setText(randomChar() + "");
		buttonValue[4 * 0 + 0] = (String) charButton[4 * 0 + 0].getText();

		charButton[4 * 0 + 1] = (Button) findViewById(R.id.boggle_2);
		charButton[4 * 0 + 1].setText(randomChar() + "");
		buttonValue[4 * 0 + 1] = (String) charButton[4 * 0 + 1].getText();

		charButton[4 * 0 + 2] = (Button) findViewById(R.id.boggle_3);
		charButton[4 * 0 + 2].setText(randomChar() + "");
		buttonValue[4 * 0 + 2] = (String) charButton[4 * 0 + 2].getText();

		charButton[4 * 0 + 3] = (Button) findViewById(R.id.boggle_4);
		charButton[4 * 0 + 3].setText(randomChar() + "");
		buttonValue[4 * 0 + 3] = (String) charButton[4 * 0 + 3].getText();

		charButton[4 * 1 + 0] = (Button) findViewById(R.id.boggle_5);
		charButton[4 * 1 + 0].setText(randomChar() + "");
		buttonValue[4 * 1 + 0] = (String) charButton[4 * 1 + 0].getText();

		charButton[4 * 1 + 1] = (Button) findViewById(R.id.boggle_6);
		charButton[4 * 1 + 1].setText(randomChar() + "");
		buttonValue[4 * 1 + 1] = (String) charButton[4 * 1 + 1].getText();

		charButton[4 * 1 + 2] = (Button) findViewById(R.id.boggle_7);
		charButton[4 * 1 + 2].setText(randomChar() + "");
		buttonValue[4 * 1 + 2] = (String) charButton[4 * 1 + 2].getText();

		charButton[4 * 1 + 3] = (Button) findViewById(R.id.boggle_8);
		charButton[4 * 1 + 3].setText(randomChar() + "");
		buttonValue[4 * 1 + 3] = (String) charButton[4 * 1 + 3].getText();

		charButton[4 * 2 + 0] = (Button) findViewById(R.id.boggle_9);
		charButton[4 * 2 + 0].setText(randomChar() + "");
		buttonValue[4 * 2 + 0] = (String) charButton[4 * 2 + 0].getText();

		charButton[4 * 2 + 1] = (Button) findViewById(R.id.boggle_10);
		charButton[4 * 2 + 1].setText(randomChar() + "");
		buttonValue[4 * 2 + 1] = (String) charButton[4 * 2 + 1].getText();

		charButton[4 * 2 + 2] = (Button) findViewById(R.id.boggle_11);
		charButton[4 * 2 + 2].setText(randomChar() + "");
		buttonValue[4 * 2 + 2] = (String) charButton[4 * 2 + 2].getText();

		charButton[4 * 2 + 3] = (Button) findViewById(R.id.boggle_12);
		charButton[4 * 2 + 3].setText(randomChar() + "");
		buttonValue[4 * 2 + 3] = (String) charButton[4 * 2 + 3].getText();

		charButton[4 * 3 + 0] = (Button) findViewById(R.id.boggle_13);
		charButton[4 * 3 + 0].setText(randomChar() + "");
		buttonValue[4 * 3 + 0] = (String) charButton[4 * 3 + 0].getText();

		charButton[4 * 3 + 1] = (Button) findViewById(R.id.boggle_14);
		charButton[4 * 3 + 1].setText(randomChar() + "");
		buttonValue[4 * 3 + 1] = (String) charButton[4 * 3 + 1].getText();

		charButton[4 * 3 + 2] = (Button) findViewById(R.id.boggle_15);
		charButton[4 * 3 + 2].setText(randomChar() + "");
		buttonValue[4 * 3 + 2] = (String) charButton[4 * 3 + 2].getText();

		charButton[4 * 3 + 3] = (Button) findViewById(R.id.boggle_16);
		charButton[4 * 3 + 3].setText(randomChar() + "");
		buttonValue[4 * 3 + 3] = (String) charButton[4 * 3 + 3].getText();
		
	}
}

