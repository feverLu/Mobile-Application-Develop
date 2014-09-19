package edu.neu.madcourse.binbinlu.bogglenet;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import edu.neu.madcourse.binbinlu.R;
import edu.neu.madcourse.binbinlu.playersboggle.PersistentGame;
import edu.neu.mobileclass.apis.KeyValueAPI;


public class MySimpleAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<Map<String, Object>> list;
	private int layoutID;
	private String flag[];
	private int ItemIDs[];
	private buttonViewHolder holder=new buttonViewHolder();
	private Context mContext;
	public String player1;
	public String  player2;
	private Timer timer;
	private TimerTask timerTask;
	private Handler handler;
	private int period = 4;
	private Gson gson = new Gson();
	private String json;
	private int acceptOrDecline;
	private Online onlineObject = new Online();
	private ProgressDialog pro;
	

	public MySimpleAdapter(Context mcontext, List<Map<String, Object>> mlist,
			int mlayoutID, String mflag[], int mItemIDs[]) {
		mInflater = LayoutInflater.from(mcontext);
		list = mlist;
		layoutID = mlayoutID;
		flag = mflag;
		ItemIDs = mItemIDs;
		mContext = mcontext;
	}

	public MySimpleAdapter() {
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	private class buttonViewHolder {
		TextView logName;
		Button buttonInvite;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = mInflater.inflate(R.layout.persistent_online_item, null);
		holder.logName=(TextView) convertView.findViewById(ItemIDs[0]);
		holder.buttonInvite=(Button) convertView.findViewById(ItemIDs[1]);
		holder.logName.setText((String)list.get(position).get(flag[0]));
		holder.buttonInvite.setText((String)list.get(position).get(flag[1]));
		holder.buttonInvite.setOnClickListener(new buttonListener(position));
		return convertView;
	}

	class buttonListener implements OnClickListener {
		private int position;
		buttonListener(int pos) {
			position=pos;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			player1=list.get(position).get("playerList").toString();
			player2=LogIn.enteredUserName;
			
			if (player1.equals(player2)) {
				Toast.makeText(Online.context, 
						position+" You can not play with yourself~~",Toast.LENGTH_SHORT).show();	
			} 
			else {
				Online.isInvited = true;
				setMyselfState(player1, player2);
//				KeyValueAPI.put("coldest1030", "university", player2+player1, "inviting");
				
				if (KeyValueAPI.isServerAvailable()) {
					String tempInvite = gson.fromJson( KeyValueAPI.get("coldest1030", "university", "invite"), String.class);
					if (tempInvite == null) {
						KeyValueAPI.put("coldest1030", "university", "invite", "inviting");
						waitAccept();
					}
				}
			}
		}
		
	}
	
	
	private void openNewDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(Online.context);
		builder.setTitle(R.string.boggle_new_game_title);
		builder.setPositiveButton(R.array.boggle_board_size, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				acceptOrDecline = which;
				dialog.cancel();
				waitAccept();
			}
		});
		builder.show();
	}

	// start a new game with the given boggle board size
	private void startGame() {
		 //start game here...
		Intent intent = new Intent(mContext, PersistentGame.class);
//		intent.putExtra(PersistentGame.KEY_DIFFICULTY, acceptOrDecline);
		intent.putExtra(PersistentGame.KEY_DIFFICULTY, 0);
		mContext.startActivity(intent);
		}
	
	protected void disProDialog() {
		pro.cancel();
	}
	
	protected void waitAccept() {
		period = 4;
		pro = new ProgressDialog(Online.context);
		pro.setTitle("Please Wait...");
		pro.setCancelable(false);
		pro.setCanceledOnTouchOutside(false);
		pro.show();
		timer = new Timer();
		timerTask = new TimerTask(){
			public void run(){
				Message msg = new Message();
				if (period>0) {
					msg.what = period--;
				} 
				handler.sendMessage(msg);	
			}
		};
		
		timer.schedule(timerTask, 1000, 4000);
		
		handler = new Handler(){
			public void handleMessage(Message msg){
				super.handleMessage(msg);
				if(msg.what>0){
				//do something 
						
				if (onlineObject.checkService()) {
					String check = onlineObject.getInviteInf();			
					if ((check!=null)&&(check.equals("yes"))) {
						disProDialog();
						KeyValueAPI.clearKey("coldest1030", "university", "invite");
						timer.cancel();
						//let the inviter to change the opponent's information 
						setMyselfState(player2, player1);
						PersistentGame.setCompitition(player1, player2);
						startGame();
					} else if ((check!=null)&&(check.equals("no"))) {
						timer.cancel();
						disProDialog();
						Online.isInvited = false;
						//if someone is declined, we need to clear the "player2 + player1" and the onlineUsers tables
						KeyValueAPI.clearKey("coldest1030", "university", "invite");
						unsetMyselfState(player1, player2);
//					AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
//					.setTitle(R.string.you_are_declined)
//					.setItems(R.array.confirm,
//					new DialogInterface.OnClickListener() {
//					public void onClick(
//					DialogInterface dialoginterface, int which) {
//					}
//					}).show();
							
					AlertDialog.Builder builder = new AlertDialog.Builder(Online.context);
					builder.setTitle("You are declined.");
					builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								
						@Override
					public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
						dialog.cancel();
					}
				});
				builder.show();	
			}
		}
	}else{
		// do something
		
		Online.isInvited = false;
		timer.cancel();		
		disProDialog();
		unsetMyselfState(player1, player2);
		KeyValueAPI.clearKey("coldest1030", "university", "invite");
		AlertDialog.Builder builder = new AlertDialog.Builder(Online.context);
		builder.setTitle("      Time Out     ");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							
				@Override
			public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				dialog.cancel();
				}
	    });
		builder.show();
	}	
  }
 };	
}
	
	public void setMyselfState (String player1, String player2) {
		if (onlineObject.checkService()) {
			onlineObject.getOnlineUsers();
			LinkedList<State> onlineUsers = onlineObject.onlineUsers;
			
			for (int i=0; i<onlineUsers.size(); i++) {
				if (onlineUsers.get(i).logName.toString().equals(player2)) {
					onlineUsers.get(i).ifPlaying = true;
					onlineUsers.get(i).opponent = player1;

					if (onlineObject.checkService()) {
						onlineObject.putOnlineUsers();
					}
				}
			}
		}
}

	public void unsetMyselfState (String player1, String player2) {
		onlineObject.getOnlineUsers();
		LinkedList<State> onlineUsers = onlineObject.onlineUsers;
		for (int i=0; i<onlineUsers.size(); i++) {
			if (onlineUsers.get(i).logName.toString().equals(player2)) {
				onlineUsers.get(i).ifPlaying = false;
				onlineUsers.get(i).opponent = null;
				onlineObject.putOnlineUsers();
			}
		}
	}
}
	

	


