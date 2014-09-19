package edu.neu.madcourse.binbinlu;

import edu.neu.madcourse.binbinlu.R;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.widget.TextView;
import android.app.Activity;
import android.os.Bundle;

public class TeamMemberDeviceID extends Activity {
	
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.team_member_deviceid);
	
	TelephonyManager tm=(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
	TextView txt_device_ID=(TextView)findViewById(R.id.txt_device_id);
	String deviceID="phoneID:"+tm.getDeviceId().toString();
	
	txt_device_ID.setText(deviceID);

}
	
}
