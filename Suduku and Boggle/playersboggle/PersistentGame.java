package edu.neu.madcourse.binbinlu.playersboggle;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.neu.madcourse.binbinlu.R;
import edu.neu.madcourse.binbinlu.boggle.BoggleMusic;
import edu.neu.madcourse.binbinlu.bogglenet.MySimpleAdapter;
import edu.neu.madcourse.binbinlu.bogglenet.Online;
import edu.neu.madcourse.binbinlu.bogglenet.OnlineService;
import edu.neu.madcourse.binbinlu.bogglenet.State;
import edu.neu.mobileclass.apis.KeyValueAPI;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PersistentGame<diff> extends Activity implements OnClickListener {
	public static final int DIFFICULTY_EASY=0;
	public static final int DIFFICULTY_MEDIUM=1;
	public static final int DIFFICULTY_HARD=2;
	public static final int PERIOD = 4000;
	public static final int DELAY = 1000;
	public static final String KEY_DIFFICULTY="edu.neu.madcourse.binbinlu.difficulty";
	private int diff;
	private char[] gridChar;
	private char[] charSequences;
	//all the word has been found in this round
	private List<String> totalWords = new ArrayList<String>(); 
	private List<String> peerWords = new ArrayList<String>();
	//the total score in this round, set it 0 at first
	public static int totalScore;
	public static int oppnentScore;
	private static RevisedBloomFilter<String> filter = new RevisedBloomFilter<String>(
			432334 * 16, 432334);
	
	private TextView timerView;
	private TextView yourScore;
	private TextView peerScore;
	private ListView yourWordList;
	private ListView peerWordList;
	private ArrayAdapter yourAdapter; 
	private ArrayAdapter peerAdapter;
	private Timer timer;
	private Handler handler;
	private Timer peerInfTimer;
	private Handler peerInfHandler;
	private List<Map<String, Object>> highScore;
	private Online onlineObject = new Online();
	private MySimpleAdapter object = new MySimpleAdapter();
	
	private Gson gson = new Gson();
	private boolean isBack = false;
	
	public static String peerName;
	public static String myName;
	public static boolean isOppnentQuit = false;
	
	GenerateChar genSequences; 
	PersistentView persistentView;
	
	public static void setCompitition (String player1, String player2) {
		peerName = player1;
		myName = player2;
	}
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.persistent_compete);
		
		stopService(new Intent(Online.context, OnlineService.class));
		Online.isInvited = false;
		
		KeyValueAPI.get("coldest1030", "University", myName);
		
		diff=getIntent().getIntExtra(KEY_DIFFICULTY, DIFFICULTY_HARD);
		getGrid(diff);
		genSequences = new GenerateChar(0);
		charSequences = genSequences.getGrid();
		
		totalScore = 0;
		oppnentScore = 0;
	
		iniView();
	    initDict();
	    iniTime();
	    peerWord();
	}
	
	private void peerWord() {
		peerInfTimer = new Timer();
		peerInfHandler = new Handler() {
		public void handleMessage (Message msg) {
			super.handleMessage(msg);
			if (msg.what>0) {
				peerWords = (ArrayList<String>) msg.obj;
				if (peerWords != null) {
					updatePeerScore(peerWords);
					peerAdapter = new ArrayAdapter<String> (PersistentGame.this, R.layout.peristent_wordlist_peer, peerWords);
					peerWordList.setAdapter(peerAdapter);
					peerAdapter.notifyDataSetChanged();
				}
				if (isOppnentQuit) {
					Toast.makeText(PersistentGame.this, "Your oppnent quited",Toast.LENGTH_SHORT).show();
				}
			} else {
				stopService(new Intent(PersistentGame.this, PersistentGameService.class));
				peerInfTimer.cancel();
			}
		}
	};
	ReadPeerWordList readTask = new ReadPeerWordList(peerInfHandler, peerName);
	peerInfTimer.schedule(readTask, DELAY, PERIOD);	
	}
	
	private void updatePeerScore(List<String> peerPeerWords) {
		if (peerPeerWords!=null) {
			int peerWordSize = peerPeerWords.size();
			oppnentScore = 0;
			for (int i = 0; i<peerWordSize; i++) {
				int len = peerPeerWords.get(i).length();
				if ((len>2) && (len<5))
					oppnentScore = oppnentScore +1;
				else if (len>4)
					oppnentScore = oppnentScore + (len-3);
			}
			peerScore.setText("Score: " + oppnentScore);
		}
	}
	
	//initiate the timer
	private void iniTime() {
		
		timer = new Timer();
		
		handler = new Handler(){
			public void handleMessage(Message msg){
				super.handleMessage(msg);
				if(msg.what>0){
					timerView.setText("" + msg.what + "   seconds");
				}
				else{
					timer.cancel();
					showScore();
				}
			}
		};
		GameTimer timerTask = new GameTimer(handler);
		timer.schedule(timerTask, DELAY, 1000);	
	}
	
	private void showScore() {
		object.unsetMyselfState(peerName, myName);
		KeyValueAPI.clearKey("coldest1030", "university", myName);
		KeyValueAPI.clearKey("coldest1030", "university", "invite");
		KeyValueAPI.clearKey("coldest1030", "university", "quit");
		
		Log.i("showScore", "ok");
		Log.i("showScore", "ok");
		Log.i("showScore", "ok");
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Your total Socore is: " + totalScore +"");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
		public void onClick(DialogInterface dialog, int which) {
				Log.i("onclick", "ok");
				Log.i("onclick", "ok");
				Log.i("onclick", "ok");
		// TODO Auto-generated method stub
//				highScore = new ArrayList<Map<String, Object>>();
//				int temp = 100;
//				try {
//					if (!KeyValueAPI.get("coldest1030", "university", "highestScore").isEmpty()) {
//						String str = KeyValueAPI.get("coldest1030", "university", "highestScore");
//						Type listType = new TypeToken<ArrayList<Map<String, Object>>>(){}.getType();
//						highScore = gson.fromJson(str, listType);
//						try {
//							temp = Integer.parseInt((highScore.get(0).get("Score").toString()));
//						} catch (Exception e) {
//							temp = 100;
//						}
//					}
//					if (temp < totalScore) {
////						if (totalScore > )
//						
//						highScore.clear();
//						highScore.add(map);
//						String str = gson.toJson(highScore);
//						KeyValueAPI.put("coldest1030", "university", "highestScore", str);
//					} else if (temp == totalScore) {
//						Map<String, Object> map = new HashMap<String, Object>();
//						map.put("Score", totalScore);
//						Calendar ca = Calendar.getInstance();
//						map.put("Time", ca.getTime());
//						map.put("Name", myName);
//						highScore.add(map);
//						String str = gson.toJson(highScore);
//						KeyValueAPI.put("coldest1030", "university", "highestScore", str);
//					}
				isBack = true;
				highScore = new ArrayList<Map<String, Object>>();
				if (!KeyValueAPI.get("coldest1030", "university", "highestScore").isEmpty()) {
					String str = KeyValueAPI.get("coldest1030", "university", "highestScore");
					Type listType = new TypeToken<ArrayList<Map<String, Object>>>(){}.getType();
					highScore = gson.fromJson(str, listType);
				}
				
				Log.i("highScore", highScore.toString());
				Log.i("highScore", highScore.toString());
				Log.i("highScore", highScore.toString());
				List<Map<String, Object>> tempScoreList = new ArrayList<Map<String, Object>>();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("Score", totalScore);
				Calendar ca = Calendar.getInstance();
				map.put("Time", ca.getTime());
				map.put("Name", myName);
				Log.i("mapmap", map.toString());
				Log.i("mapmap", map.toString());
				Log.i("mapmap", map.toString());
				
				if (highScore.isEmpty()) {
					tempScoreList.add(map);
					Log.i("tempScoreList", tempScoreList.toString());
					Log.i("tempScoreList", tempScoreList.toString());
					Log.i("tempScoreList", tempScoreList.toString());
				} else if (highScore.size() < 10) {
					int highScoreSize = highScore.size();
					for (int i = 0; i <highScoreSize; i++) {
						int length = highScore.get(i).get("Score").toString().length();
						int highScoreScore = Integer.parseInt(highScore.get(i).get("Score").toString().substring(0, length-2));
						//int highScoreScore = Integer.valueOf(highScore.get(i).get("Score").toString()).intValue();
						if (totalScore < highScoreScore) {
							tempScoreList.add(highScore.get(i)); 
						} else {
							tempScoreList.add(map);
							tempScoreList.addAll(highScore.subList(i, highScoreSize));
							Log.i("tempScoreList", tempScoreList.toString()); 
							Log.i("tempScoreList", tempScoreList.toString());
							Log.i("tempScoreList", tempScoreList.toString());
							break;
						}
					}
				} else {
					for (int i = 0; i < 10; i++) {
						int len = highScore.get(i).get("Score").toString().length();
						int highScoreScore = Integer.parseInt(highScore.get(i).get("Score").toString().substring(0, len-2));
						if (totalScore < highScoreScore) {
							tempScoreList.add(highScore.get(i)); 
						} else {
							tempScoreList.add(map);
							tempScoreList.addAll(highScore.subList(i, 9));
							Log.i("tempScoreList", tempScoreList.toString());
							Log.i("tempScoreList", tempScoreList.toString());
							Log.i("tempScoreList", tempScoreList.toString());
							break;
						}
					}
				}
				String str = gson.toJson(tempScoreList);
				Log.i("strstrstr", str);
				KeyValueAPI.put("coldest1030", "university", "highestScore", str);
				finish();
			}
		});
		if(!((Activity) PersistentGame.this).isFinishing())
		{
			builder.show();
		}
	}
	
	//initiate the view of the layout and set adapter
	private void iniView() {
	     timerView = (TextView)  findViewById(R.id.persistent_timer);
	     timerView.setText("Time  1 : 00");
	     yourScore = (TextView) findViewById(R.id.persistent_your_score);
	     yourScore.setText("Score: "+totalScore);
	     peerScore = (TextView) findViewById(R.id.persistent_peer_score);
	     peerScore.setText("Score: "+totalScore);
	     yourWordList = (ListView) findViewById (R.id.persistent_your_wordlist);
	     peerWordList = (ListView) findViewById (R.id.persistent_peer_wordlist);
	     yourAdapter = new ArrayAdapter<String> (this, R.layout.persistent_wordlist, totalWords); 
	     //peerAdapter = new ArrayAdapter<String> (this, R.layout.peristent_wordlist_peer, peerWords);
	     yourWordList.setAdapter(yourAdapter);
	  // peerWordList.setAdapter(peerAdapter);
	}
	
	
	//initiate the dictionary
 	private void initDict() {
		if (filter.isEmpty()) {
			ReadDict dict = new ReadDict(this, filter);
			dict.execute();
		}
 	}
	
	//check if a word exists in the whole word list
	public boolean checkWord(String slideWord) {
		if(slideWord.length()<3)
			return false;
		else return (filter.contains(slideWord));
	}
	
	//return all the words that have been found
	public List<String> getFound() {
		return totalWords;		
	}
	
	//add the score of this word to the total score
	public void addScore(int tmpScore) {
		totalScore = totalScore + tmpScore;
		yourScore.setText("Score: "+totalScore);
	}
	
	public void addWord (String slideWord) {
		totalWords.add(slideWord);
		
		String sTotalWords = gson.toJson(totalWords);
		if (KeyValueAPI.isServerAvailable()) {
			KeyValueAPI.put("coldest1030", "university", myName, sTotalWords);
		}
		yourAdapter.notifyDataSetChanged();
	}
	
	//set the characters in each grid
	public void getGrid(int diff) {
		GenerateChar gen=new GenerateChar(diff);	
		gridChar=gen.getGrid();
	}
	
	//Return a string for the tile at the given coordinates 
	protected char getTileString(int x, int y) {
		return charSequences[4 * x + y ];
	}
		
	public String getTileLetter(int x, int y) {
		char letter = charSequences[(x * 4) + y];
		return String.valueOf(letter);
	}

	public void playMusic (boolean rightOrWrong) {
		if (rightOrWrong) {
			BoggleMusic.play(this, R.raw.right);	
		}
	}

	
	public void vibrate(int duration) {
		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		vibrator.vibrate(duration); 
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
        	isBack = true;
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Are you sure to quit?");
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

				@Override
			public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
					dialog.cancel();
					clearInfAboutGame();
					PersistentGame.this.finish();
				}
			});
			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			});
			builder.show();
    		 return true;
        } else if (keyCode==KeyEvent.KEYCODE_HOME) {
        	return true;
        } else {
        	return super.onKeyDown(keyCode, event);
        }
    }
	
	public void clearInfAboutGame() {
		if (KeyValueAPI.isServerAvailable()) {
			
			//the opponent can continue to play even his/her opponent quits. In this situation, the "online" table on server should also be changed 
			onlineObject.getOnlineUsers();
			LinkedList<State> onlineUsers = onlineObject.onlineUsers;
			
			for (int i=0; i<onlineUsers.size(); i++) {
				if (onlineUsers.get(i).logName.toString().equals(peerName)) {
					onlineUsers.get(i).opponent = null;
					onlineObject.putOnlineUsers();
				}
			}
			//clear my wordlist
			KeyValueAPI.clearKey("coldest1030", "university", myName);
//			KeyValueAPI.clearKey("coldest1030", "university", peerName);
//			MySimpleAdapter.unsetMyselfState(myName, peerName);
			object.unsetMyselfState(peerName, myName);
		}
	}
	
	protected void onPause() {
		if (!isBack) {
			Intent intent = new Intent(this, PersistentGameService.class);
			intent.putExtra("myName", myName);
			intent.putExtra("peerName", peerName);
			startService(intent);
		} else {
			isBack = false;
		}
		super.onPause();
	}
	
	protected void onResume() {
		 super.onResume();
		 stopService(new Intent(this, PersistentGameService.class));
	}

	protected void onDestroy() {
		super.onDestroy();
	}

	public void peerQuit() {
		totalScore = totalScore + oppnentScore;
	}
}


