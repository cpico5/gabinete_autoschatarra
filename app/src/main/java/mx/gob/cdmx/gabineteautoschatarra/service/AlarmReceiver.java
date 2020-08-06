package mx.gob.cdmx.gabineteautoschatarra.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;

public class AlarmReceiver extends BroadcastReceiver {

	public AlarmReceiver() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onReceive(Context context, Intent arg1) {
		Intent serviceIntent = new Intent(context, AndroidLocationServices.class);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			ContextCompat.startForegroundService(context, serviceIntent );
		} else {
			context.startService(serviceIntent);
		}


	}

}
