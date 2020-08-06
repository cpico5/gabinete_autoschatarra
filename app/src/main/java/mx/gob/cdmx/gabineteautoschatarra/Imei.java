package mx.gob.cdmx.gabineteautoschatarra;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import org.json.JSONException;
import org.json.JSONObject;

public class Imei  {

	Context ctx;
	public Imei(Context context) {
		this.ctx = context;
	}

	public String getImei(){
		String Imei = "";

		//Getting the Object of TelephonyManager
		TelephonyManager tManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
		//Getting IMEI Number of Devide


			//Imei = tManager.getImei().toString();

			Imei = tManager.getDeviceId();

			if(Imei==null){

				Imei = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);

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