package edu.neu.madcourse.binbinlu.bogglenet;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.LinkedList;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;
import edu.neu.madcourse.binbinlu.R;
import edu.neu.mobileclass.apis.KeyValueAPI;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class LogIn extends Activity implements OnClickListener{
	private EditText userName;
	private EditText registerName;
	private LinkedList<String> log=new LinkedList<String>();
	private LinkedList<String> register=new LinkedList<String>();
	private LinkedList<State> onlineIfPlaying=new LinkedList<State>();
	private State eachOnline=new State();
	private Gson gson = new Gson();
	private String logInName;
	private String registeredName;
	private String addOnline;
	private String checkRegister;
	private JsonParser parser=new JsonParser();
	private JsonArray userNameArray;
	private JsonArray registerNameArray;
	
	public static String enteredUserName;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.persistent_log);
		
		View logButton = findViewById(R.id.persistent_log);
		logButton.setOnClickListener(this);	
		
		View registerButton = findViewById(R.id.persistent_register);
		registerButton.setOnClickListener(this);
		
		View aboutButton = findViewById(R.id.persistent_about);
		aboutButton.setOnClickListener(this);
		
		userName = (EditText)findViewById(R.id.persistent_username);
		registerName = (EditText)findViewById(R.id.persistent_register_username);
		}
	
	protected void onPause() {
		super.onPause();
	}
	
	protected void onResume() {
	    super.onResume();
	 }
	
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
    	case R.id.persistent_register:
    		// register an user name in the "register" table
    		String registerUserName=registerName.getText().toString();
    		if (!registerUserName.isEmpty()) {
    			if (KeyValueAPI.isServerAvailable()) {
    				if (!KeyValueAPI.get("coldest1030", "university", "register").isEmpty()){
    					checkRegister=KeyValueAPI.get("coldest1030", "university", "register");
    				}
    			} else {
    				Toast.makeText(getApplicationContext(), "Server Unavailable!",Toast.LENGTH_SHORT).show();
    			}
    			if (checkRegister!=null) {
    				registerNameArray = parser.parse(checkRegister).getAsJsonArray();
    				Type listType = new TypeToken<LinkedList<String>>(){}.getType();
    				register=gson.fromJson(registerNameArray, listType);
    				if(!register.contains(registerName)) {
    					register.add(registerUserName);
    				}  else {
    					Toast.makeText(getApplicationContext(), "User Name Already Exist!",Toast.LENGTH_SHORT).show();
    				}
    			} else {
    				register.add(registerUserName);
    			}
    			registeredName=gson.toJson(register);
    			if (KeyValueAPI.isServerAvailable()) {
    				KeyValueAPI.put("coldest1030", "university", "register", registeredName);
    				Toast.makeText(getApplicationContext(), "User Name Registered Successfully!",Toast.LENGTH_SHORT).show();
    			} else {
        			Toast.makeText(getApplicationContext(), "Server Unavailable!",Toast.LENGTH_SHORT).show();
    			}
    		} else {
    			Toast.makeText(getApplicationContext(), "At least one character for the username!",Toast.LENGTH_SHORT).show();
    		}
    		break;
    		
    	case R.id.persistent_log:
    		enteredUserName = userName.getText().toString();
    		// check if the user name has been registered before
    		if (KeyValueAPI.isServerAvailable()) {
    			if (!KeyValueAPI.get("coldest1030", "university", "register").isEmpty()) {
    				logInName=KeyValueAPI.get("coldest1030", "university", "register");
    				//registerNameArray = parser.parse(logInName).getAsJsonArray();
    				Type listType = new TypeToken<LinkedList<String>>(){}.getType();
//    				log = gson.fromJson(registerNameArray, listType);
    				log = gson.fromJson(logInName, listType);
    				if (!log.contains(enteredUserName)) {
    					Toast.makeText(getApplicationContext(), "User Name Not Exist!",Toast.LENGTH_SHORT).show();
    				} else {
    					// the register table contains the entered the user name, able to start a game
    					if (KeyValueAPI.isServerAvailable()) {
    						if(!KeyValueAPI.get("coldest1030", "university", "online").isEmpty()){
    							addOnline=KeyValueAPI.get("coldest1030", "university", "online");
    						}
    					}
    					// add the user name to the online table, which means an existed registered user log in
    					if (addOnline!=null) {
    							userNameArray = parser.parse(addOnline).getAsJsonArray();
    							Type listTypeTwo = new TypeToken<LinkedList<State>>(){}.getType();
    							onlineIfPlaying = gson.fromJson(userNameArray, listTypeTwo);
    					}
    					if (!isLoged()) {
    						eachOnline.logName=enteredUserName;
    						onlineIfPlaying.add(eachOnline);
    						addOnline=gson.toJson(onlineIfPlaying);
    						if (KeyValueAPI.isServerAvailable()) {
    							KeyValueAPI.put("coldest1030", "university", "online", addOnline);
    						}
    						Intent intent = new Intent(this, Online.class);
    						startActivity(intent);
    					} else {
    						Toast.makeText(getApplicationContext(), "Username already loged in!",Toast.LENGTH_SHORT).show();	
    					}
    				}
    			}else {
    					//the register table is empty
    					Toast.makeText(getApplicationContext(), "User Name Not Exist!",Toast.LENGTH_SHORT).show();	
    			}
    		} else {
    			Toast.makeText(getApplicationContext(), "Server Unavailable!",Toast.LENGTH_SHORT).show();
    		}
    	    break;
    	    
    	case R.id.persistent_about:
    		Intent j = new Intent(this, PersistentAbout.class);
    		startActivity(j);
		default:
			break;
		}
	}
	
	private boolean isLoged() {
		for (int i = 0; i<onlineIfPlaying.size(); i++) {
			if (onlineIfPlaying.get(i).logName.equals(enteredUserName)) {
				return true;
			} 
		}
		return false;
	}

	//delete duplicate content
//	public void removeDuplicate() {					
//		HashSet<String> set=new HashSet<String>();
//		int index=0;
//		int size=onlineIfPlaying.size();
//		while(index<size) { 
//			if (!set.contains(onlineIfPlaying.get(index).logName)) {
//				set.add(onlineIfPlaying.get(index).logName);
//				index++;
//			} else {
//				onlineIfPlaying.remove(index);
//				size--;
//			}
//		}
//	}
//	  public void removeDuplicate()   
//	  {   
//	   HashSet<PersistentState> set = new HashSet<PersistentState>(onlineIfPlaying);   
//	   onlineIfPlaying.clear();   
//	   onlineIfPlaying.addAll(set);   
//	  }  
}





