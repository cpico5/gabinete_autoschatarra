package mx.gob.cdmx.gabineteautoschatarra.service;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import org.json.JSONException;
import org.json.JSONObject;

public class Imei {

	Context ctx;
	public Imei(Context context) {
		this.ctx = context;
	}

	public String getImai(){
		String Imei = "";

		//Getting the Object of TelephonyManager
		TelephonyManager tManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
		//Getting IMEI Number of Devide
		if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			//return TODO;
		} else {

			Imei = tManager.getDeviceId();

		}
		return Imei;
	}

	public String getDeviceInof(){
		JSONObject deviceInfor = new JSONObject();
		String result = "";
		try {
			deviceInfor.put("SERIAL: ", Build.SERIAL);
			deviceInfor.put("MODEL: ", Build.MODEL);
			deviceInfor.put("ID: " , Build.ID);
			deviceInfor.put("Manufacture: " , Build.MANUFACTURER );
			deviceInfor.put("Brand: " , Build.BRAND);
			deviceInfor.put("Type: " , Build.TYPE );
			deviceInfor.put("User: " , Build.USER );
			deviceInfor.put("BASE: " , Build.VERSION_CODES.BASE);
			deviceInfor.put("INCREMENTAL: " , Build.VERSION.INCREMENTAL);
			deviceInfor.put("SDK:  " , Build.VERSION.SDK);
			deviceInfor.put("BOARD: " , Build.BOARD );
			deviceInfor.put("BRAND: " , Build.BRAND );
			deviceInfor.put("HOST: " , Build.HOST);
			deviceInfor.put("FINGERPRINT: " , Build.FINGERPRINT);
			deviceInfor.put("Version Code: " , Build.VERSION.RELEASE);
			result = deviceInfor.toString();
		} catch(JSONException e){
			result = e.getMessage();
		}
		return result;
	}
	
}	 