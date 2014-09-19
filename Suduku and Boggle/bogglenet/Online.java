package edu.neu.madcourse.binbinlu.bogglenet;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import edu.neu.mobileclass.apis.KeyValueAPI;
import edu.neu.madcourse.binbinlu.R;
import edu.neu.madcourse.binbinlu.playersboggle.PersistentGame;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.Toast;

public class Online extends Activity {
	private Gson gson = new Gson();
	private String json;
	private JsonParser parser=new JsonParser();
	private JsonArray array;
	public LinkedList<State> onlineUsers;
	private LinkedList<State> checkUsers = new LinkedList<State>();
	private ListView playerList;
	private List<Map<String, Object>> listItem;
	
	private String backClearOnline;
	private Handler onlineNewUser;
	private Timer timer;
	private MySimpleAdapter listItemAdapter;
	public static Context context;
	private TimerTask onlineTimerTask;

	private String myName;
	private String peerName;
	private boolean alreadyOpen = false;
	public static boolean isInvited = false;
	private boolean isBack = false;
	
 	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.persistent_online);

		context = this;
		//add the player's online information and upload it to the server
		  if (checkService()) {
				getOnlineUsers();
			} else {
				serverUnavailableToast();
			}
			
			//set every button in the list view
			listItem=new ArrayList<Map<String, Object>>();
			int onlineSize=onlineUsers.size();
			for(int i=0; i<onlineSize; i++)   
	        {   
	            Map<String, Object> map = new HashMap<String, Object>();   
	            String onlineName=onlineUsers.get(i).logName;
	            map.put("playerList", onlineName); 
	            map.put("listState", "invite");    
				listItem.add(map);   
	        }   
			
			playerList=(ListView) findViewById(R.id.listView);
			
			//set the adapter for the listview and button
			listItemAdapter= new MySimpleAdapter(this, listItem, 
					R.layout.persistent_online_item, new String[] {"playerList", "listState"},
					new int[] {R.id.persistentList, R.id.persistentButton });
			playerList.setAdapter(listItemAdapter);
			//this.setContentView(playerList);
 	}
 
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
        	isBack = true;
        	if (checkService()) {
        		getOnlineUsers();
        	} else {
        		serverUnavailableToast();
        	}
        	for (int i=onlineUsers.size()-1; i>=0; i--) {
        		if (onlineUsers.get(i).logName.toString().equals(LogIn.enteredUserName)) {
        			onlineUsers.remove(i);
        		}
        	}
        	backClearOnline=gson.toJson(onlineUsers);
        	if (checkService()) {
        		KeyValueAPI.put("coldest1030", "university", "online", backClearOnline);
        		Toast.makeText(getApplicationContext(), "Back Pressed Successfully!",Toast.LENGTH_SHORT).show();
        	} else {
        		serverUnavailableToast();
        	}
        	this.finish();
        	return true;
        } else {
        	return super.onKeyDown(keyCode, event);
        }
    }

	public void updateUserName(LinkedList<State> checkUsers) {
		int checkSize=checkUsers.size();
		onlineUsers.clear();
		listItem.clear();
		for(int i=0; i<checkSize; i++)   
        {   
            Map<String, Object> map = new HashMap<String, Object>();   
            String onlineName=checkUsers.get(i).logName;
            map.put("playerList", onlineName); 
            map.put("listState", "invite");    
            listItem.add(map);   
        }   
		onlineUsers=checkUsers;
		listItemAdapter.notifyDataSetChanged();
		Toast.makeText(context, "Working!",Toast.LENGTH_SHORT).show();
	}
	
	
	
	public boolean checkService() {
		if (KeyValueAPI.isServerAvailable()) 
			return true;
		return false;
	}
	
	public void getOnlineUsers() {
		try {
		if (KeyValueAPI.get("coldest1030", "university", "online")!=null) {
			json=KeyValueAPI.get("coldest1030", "university", "online");
			array = parser.parse(json).getAsJsonArray();
			Type listType = new TypeToken<LinkedList<State>>(){}.getType();
			onlineUsers = gson.fromJson(array, listType);
		}
		} catch (Exception e) {
			serverUnavailableToast();
			return;
		}
	}
	
	public String getInviteInf() {
			if (!KeyValueAPI.get("coldest1030", "university", "invite").isEmpty()) {
				json=KeyValueAPI.get("coldest1030", "university", "invite");
				String check = gson.fromJson(json, String.class);
				return check;
			}
		return null;
	}
	
	public void putOnlineUsers() {
		String addOnlineUsers = gson.toJson(onlineUsers);
		if (checkService()) {
			KeyValueAPI.put("coldest1030", "university", "online", addOnlineUsers);
		}
	}
	
	protected void serverUnavailableToast() {
		Toast.makeText(getApplicationContext(), "Server Unavailable!",Toast.LENGTH_SHORT).show();
	}
	
//	protected void checkAccept() {
//		Intent intent = new Intent(this, CheckAccept.class);
//	    startActivity(intent);
//	}
	
	protected void onPause() {
		 super.onPause();
		 timer.cancel();
		 
		 if (!isBack) {
			 //start the service in background if someone is inviting 
			 OnlineService.myName = LogIn.enteredUserName;
			 startService(new Intent(this, OnlineService.class));
		 } else {
			 isBack = false;
		 }
	}

	
	protected void onResume() {
	    super.onResume();
	    
	    //stop the service to check if someone is inviting 
	    stopService (new Intent(this, OnlineService.class));
	    
	    timer=new Timer();
	    //check if there is anyone inviting me
	    onlineTimerTask = new TimerTask() {
	    	public void run() {
	    		Message msg = new Message();
	    		msg.what = 1;
	    		onlineNewUser.sendMessage(msg);
	    	}
	    };
		timer.schedule(onlineTimerTask, 0, 5000);	
		
//		timer = new Timer();
		onlineNewUser = new Handler(){
			public void handleMessage(Message msg){
				super.handleMessage(msg);
				try {
					if(msg.what>0){
						if (KeyValueAPI.get("coldest1030", "university", "online")!=null) {
							json=KeyValueAPI.get("coldest1030", "university", "online");
							Type listType = new TypeToken<LinkedList<State>>(){}.getType();
							checkUsers = gson.fromJson(json, listType);
						}	
						int onlineSize=onlineUsers.size();
						int checkSize=checkUsers.size();
			    			
						if (onlineSize!=checkSize) {
							updateUserName(checkUsers);
							onlineUsers = checkUsers;
						} else {
							for (int i=0; i<onlineSize; i++) {
			    					
								//check if we need to update a username
								if (!checkUsers.get(i).logName.equals(onlineUsers.get(i).logName)) {
									updateUserName(checkUsers);
									onlineUsers = checkUsers;
									break;
								}
							}
			    		checkInvite();
						}
					}
				} catch (Exception e) {
					return;
				}
			}
		};
	}
	
	protected void checkInvite () {
		int onlineSize=onlineUsers.size();
		//check if someone has invited someone
		for (int i=0; i<onlineSize; i++) {
			if ((checkUsers.get(i).ifPlaying == true)&&(!isInvited)) {
				if (KeyValueAPI.isServerAvailable()) {
					String tempInvite = gson.fromJson( KeyValueAPI.get("coldest1030", "university", "invite"), String.class);
					if (tempInvite != null) {
						if (tempInvite.equals("inviting")) {
							String iteratorName = checkUsers.get(i).opponent;
							if (iteratorName.equals(LogIn.enteredUserName)) {
								peerName = checkUsers.get(i).logName;
								myName = LogIn.enteredUserName;
								if (!alreadyOpen)
									openAcceptOption();
							}
						}
					}
				}
			}
		}
	}
	
	
	
	
	protected void openAcceptOption() {
		alreadyOpen = true;
		new AlertDialog.Builder(this)
		.setTitle(R.string.persistent_boggle)
		.setCancelable(false)
		.setItems(R.array.accept_or_decline,
		new DialogInterface.OnClickListener() {
			public void onClick(
				DialogInterface dialoginterface, int which) {
					isAccepted(which);
				}
		}).show();
	}
	
	protected void isAccepted(int option) {
		//accept
		if (option == 0) {
			if (KeyValueAPI.isServerAvailable()) {
				String inviteStill = getInviteInf();
				if (inviteStill == null) {
					alreadyOpen = false;
					KeyValueAPI.clearKey("coldest1030", "university", "invite");
					Toast.makeText(getApplicationContext(), "Time Out!",Toast.LENGTH_SHORT).show();
				} else if (inviteStill.equals("inviting")) {
					accept();
					PersistentGame.setCompitition(peerName, myName);
					Intent game = new Intent(this, PersistentGame.class);
					game.putExtra(PersistentGame.KEY_DIFFICULTY, 0);
					alreadyOpen = false;
					startActivity(game);
				}
			}
		}
		//decline
		if (option == 1) {
			decline();
			alreadyOpen = false;
		}
	}
	
	
	protected void decline() {
		if (checkService()) {
			KeyValueAPI.put("coldest1030", "university", "invite", "no");
		}
	}
	
	
	protected void accept() {
		if (checkService()) {
			KeyValueAPI.put("coldest1030", "university", "invite", "yes");
			//setMyselfState(player1, player2);
		}
	}
	
	public void setMyselfState (String player1, String player2) {
			if (checkService()) {
				getOnlineUsers();
				for (int i=0; i<onlineUsers.size(); i++) {
					if (onlineUsers.get(i).logName.toString().equals(player2)) {
						onlineUsers.get(i).ifPlaying = true;
						onlineUsers.get(i).opponent = player1;
						if (checkService()) {
							putOnlineUsers();
						}
					}
				}
			}
	}
	
	
	public void unsetMyselfState (String player1, String player2) {
			if (checkService()) {
				getOnlineUsers();
				for (int i=0; i<onlineUsers.size(); i++) {
//					if (onlineUsers.get(i).logName.toString().equals(player1)) {
//						onlineUsers.get(i).ifPlaying = false;
//						onlineUsers.get(i).opponent = null;
//						if (Online.persistentOnline.checkService()) {
//							Online.persistentOnline.putOnlineUsers();
//						}
//					}
					if (onlineUsers.get(i).logName.toString().equals(player2)) {
						onlineUsers.get(i).ifPlaying = false;
						onlineUsers.get(i).opponent = null;
						if (checkService()) {
							putOnlineUsers();
						}
					}
				}
			}
	}
	
	
	protected void onDestroy() {
		super.onDestroy();
	}

}

