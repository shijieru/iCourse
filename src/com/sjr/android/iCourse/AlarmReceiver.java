package com.sjr.android.iCourse;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(
			Context context,
			Intent intent) {
			Toast.makeText(context, "�����õ�����ʱ�䵽��", Toast.LENGTH_LONG).show();
		
	}
}
