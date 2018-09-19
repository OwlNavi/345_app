package com.example.cwagt.taskapp345.helper;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import com.example.cwagt.taskapp345.view.AddUser;
import com.example.cwagt.taskapp345.view.MainActivity;

//see https://code.tutsplus.com/tutorials/android-fundamentals-scheduling-recurring-tasks--mobile-5788

/**
 * This receives the alarm broadcast and responds
 */
public class AlarmReceiver extends BroadcastReceiver {

	private static final String DEBUG_TAG = "AlarmReceiver";

	@Override
	public void onReceive(final Context context, Intent intent) {
		Log.d(DEBUG_TAG, "Recurring alarm called");
		// start the alarm.


		Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		Ringtone r = RingtoneManager.getRingtone(context, notification);
		r.play();
	}

}