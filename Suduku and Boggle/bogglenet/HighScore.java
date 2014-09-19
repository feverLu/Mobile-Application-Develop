package edu.neu.madcourse.binbinlu.bogglenet;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import edu.neu.madcourse.binbinlu.R;
import edu.neu.mobileclass.apis.KeyValueAPI;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class HighScore extends Activity {
	private List<Map<String, Object>> highScore = new ArrayList<Map<String, Object>>();
//	private List<String> name;
//	private List<String> score;
//	private List<String> time;
	private Gson gson = new Gson();
//	private ArrayAdapter<String> adapterName;
//	private ArrayAdapter<String> adapterScore;
//	private ArrayAdapter<String> adapterTime;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.persistent_highscore);
		
		if (KeyValueAPI.isServerAvailable()) {
			if (!KeyValueAPI.get("coldest1030", "university", "highestScore").isEmpty()) {
				String str = KeyValueAPI.get("coldest1030", "university", "highestScore");
				Type listType = new TypeToken<ArrayList<Map<String, Object>>>(){}.getType();
				highScore = gson.fromJson(str, listType);
			}
		}
		if (!highScore.isEmpty()) {
			ListView scoreItem = (ListView) findViewById (R.id.high_score);
			SimpleAdapter adapter = new SimpleAdapter(this, highScore, R.layout.persistent_time_show, 
				new String[] {"Name", "Score", "Time"}, new int[] {R.id.score_name_item, R.id.score_score_item, R.id.score_time_item});
			scoreItem.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
		
		
//		name = new ArrayList<String>();
//		score = new ArrayList<String>();
//		time = new ArrayList<String>();
//		
//		ListView scoreListName = (ListView) findViewById (R.id.high_score_name);
//		adapterName = new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1, name);
//		scoreListName.setAdapter(adapterName);
//		
//		ListView scoreListScore = (ListView) findViewById (R.id.high_score_score);
//		adapterScore = new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1, score);
//		scoreListScore.setAdapter(adapterScore);
//		
//		ListView scoreListTime = (ListView) findViewById (R.id.high_score_time);
//		adapterTime = new ArrayAdapter<String> (this, R.layout.persistent_time_show, time);
//		scoreListTime.setAdapter(adapterTime);
	
	}
	
	protected void onResume() {
		super.onResume();
		
		
//		int highScoreSize = highScore.size();
//		
//		for (int i = 0; i < highScoreSize; i++) {
//			name.add(highScore.get(i).get("Name").toString());
//			score.add(highScore.get(i).get("Score").toString());
//			time.add(highScore.get(i).get("Time").toString());
//		}
		
		
		
//		adapterName.notifyDataSetChanged();
//		adapterScore.notifyDataSetChanged();
//		adapterTime.notifyDataSetChanged();
	}
}
	
