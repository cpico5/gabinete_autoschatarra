package mx.gob.cdmx.gabineteautoschatarra;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInstaller.Session;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

//import android.service.textservice.SpellCheckerService.Session;


public class CalendarViewFotos extends Activity {


	public final String TAG = "Calendar_audios";
	Calendar c = Calendar.getInstance();

	SimpleDateFormat df3 = new SimpleDateFormat("yyyMMdd");
	String formattedDate3 = df3.format(c.getTime());
	SimpleDateFormat df4 = new SimpleDateFormat("yyy-MM-dd");
	String formattedDateFecha = df4.format(c.getTime());
	SimpleDateFormat df5 = new SimpleDateFormat("HH:mm a");
	String formattedDate5 = df5.format(c.getTime());

	Session session = null;
	ProgressDialog pdialog = null;
	private ProgressDialog dialog;
	Context context = null;
	EditText reciep, sub, msg;
	String recibe, recibe_copia, passrecibe, manda, passmanda, subject, textMessage;

	public GregorianCalendar month, itemmonth;// calendar instances.

	public CalendarAdapter adapter;// adapter instance
	public Handler handler;// for grabbing some event values for showing the dot
	// marker.
	public ArrayList<String> items; // container to store calendar items which
	// needs showing the event marker
	int serverResponseCode = 0;
	String upLoadServerUri = null;
	String upLoadServerUriAudio = null;
	String upLoadServerUriBase = null;
	String upLoadServerUriFotos = null;
	final String path = "/mnt/sdcard/Mis_archivos/";
	final String pathZip = "/mnt/sdcard/";

	public String fecha;
	public String fecha_envio;
	public String tablet;
	Nombre nom = new Nombre();
	String nombreEncuesta = nom.nombreEncuesta();

	public EditText Usuario;
	public String encuestaQuien;
	public String pasoUsuario;
	public String encuestador;
	UsuariosSQLiteHelper usdbh;
	private SQLiteDatabase db;

//	public String sacaChip(){
//		String deviceId = Secure.getString(this.getContentResolver(),Secure.ANDROID_ID);
//		tablet=deviceId;	
//		return tablet;
//	}

	public String cachaChip() {
		Bundle datos = this.getIntent().getExtras();
		String Chip = datos.getString("Chip");
		return Chip;
	}

	public String sacaImei() {
		String szImei;
		TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);//Telefono
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
			szImei = TelephonyMgr.getDeviceId(); // Requires READ_PHONE_STATE
			if(szImei==null){
				szImei = Secure.getString(this.getContentResolver(),Secure.ANDROID_ID);// Tableta
			}
			return szImei;
		}
		szImei = TelephonyMgr.getDeviceId(); // Requires READ_PHONE_STATE
		if(szImei==null){
			szImei = Secure.getString(this.getContentResolver(),Secure.ANDROID_ID);// Tableta
		}
		return szImei;
	}
	
	
		public static String getHostName(String defValue) {
		try {
			Method getString = Build.class.getDeclaredMethod("getString", String.class);
			getString.setAccessible(true);
			return getString.invoke(null, "net.hostname").toString();
		} catch (Exception ex) {
			return defValue;
		}
	}
	static String ID = getHostName(null);
	static String prefix = ID;
//	String prefix = cachaChip();
	
	

		
		public void Salir(View v){
			
			Intent intent = new Intent(CalendarViewFotos.this, Menu_Principal.class);
			Bundle bundle = new Bundle();
			bundle.putString("usuario", cachaUsuario());
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
			
		}
	
// ENVIA FOTOGRAFÍAS

	public int uploadFotos(String sourceFileUri, String fech) {

		File sdCard;
		sdCard = Environment.getExternalStorageDirectory();
		//final String pathFotos = sdCard.getAbsolutePath() + "/"+ nombreEncuesta+"-Audio"+fech;
		final String pathFotos = sdCard.getAbsolutePath() + "/Mis_archivos/Fotos" + formattedDate3 + "N";

		String fileName = sourceFileUri;

		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		File sourceFile = new File(sourceFileUri);

		if (!sourceFile.isFile()) {

//		     dialog.dismiss();
			Log.i(TAG, " =====> archivo:  El Archivo no existe... :" + pathFotos + "" + "/" + "20161124_002_359083065132816_1.jpg");
			runOnUiThread(new Runnable() {
				public void run() {

				}
			});

			return 0;

		} else {
			try {
				// open a URL connection to the Servlet
				FileInputStream fileInputStream = new FileInputStream(sourceFile);
				URL url = new URL(upLoadServerUriFotos);
				// Open a HTTP  connection to  the URL
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true); // Allow Inputs
				conn.setDoOutput(true); // Allow Outputs
				conn.setUseCaches(false); // Don't use a Cached Copy
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
				conn.setRequestProperty("uploaded_file", fileName);

				dos = new DataOutputStream(conn.getOutputStream());

				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + fileName + "\""
						+ lineEnd);

				dos.writeBytes(lineEnd);

				// create a buffer of  maximum size
				bytesAvailable = fileInputStream.available();

				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];
				// read file and write it into form...
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				while (bytesRead > 0) {
					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				}
				// send multipart form data necesssary after file data...
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
				// Responses from the server (code and message)
				serverResponseCode = conn.getResponseCode();
				String serverResponseMessage = conn.getResponseMessage();

				Log.i("Foto", "HTTP Response fotos is : " + serverResponseMessage + ": " + serverResponseCode);

				if (serverResponseCode == 200) {

					runOnUiThread(new Runnable() {
						public void run() {

							String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
									+ " http://www.androidexample.com/media/uploads/"
									+ "20161124_002_359083065132816_1.jpg";

//		                      Toast.makeText(Entrada.this, "File Upload Complete."+msg,Toast.LENGTH_SHORT).show();
						}
					});
				}

				//close the streams //
				fileInputStream.close();
				dos.flush();
				dos.close();

			} catch (MalformedURLException ex) {

//		        dialog.dismiss();
				ex.printStackTrace();

				runOnUiThread(new Runnable() {
					public void run() {
//		                messageText.setText("MalformedURLException Exception : check script url.");
//		                Toast.makeText(CalendarViewFotos.this, "MalformedURLException",
//		                                                    Toast.LENGTH_SHORT).show();
					}
				});

				Log.i(TAG, " =====> archivo:  El Archivo no existe... :" + "Upload file to server " + "error: " + ex.getMessage());

//		        Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
			} catch (Exception e) {

//		        dialog.dismiss();
				e.printStackTrace();

				runOnUiThread(new Runnable() {
					public void run() {
//		                messageText.setText("Error de Internet");
//		                Toast.makeText(CalendarViewFotos.this, "Error de Internet",
//		                        Toast.LENGTH_SHORT).show();
					}
				});
				Log.i(TAG, " =====> archivo:  El Archivo no existe... :" + "Upload file to server Exception " + "Exception : " + e.getMessage());

//		        Log.e("Upload file to server Exception", "Exception : "
//		                                         + e.getMessage(), e);
			}
			return serverResponseCode;

		} // End else block
	}


	class UpdateFotos extends AsyncTask<String, Float, String> {

		protected void onPreExecute() {
			super.onPreExecute();
			Log.i("Foto", "CarlosQ:  Entra ====>");
			dialog = new ProgressDialog(CalendarViewFotos.this);
			dialog.setMessage("Enviando Fotografías...");
			dialog.setTitle("Progreso");
			dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			dialog.setCancelable(false);
			dialog.setProgress(0);
			dialog.setMax(100);
			dialog.show(); //Mostramos el diálogo antes de comenzar
		}


		@Override
		protected String doInBackground(String... params) {


			final String date2 = params[0];
			File sdCard;
			sdCard = Environment.getExternalStorageDirectory();
			final String pathFotos = sdCard.getAbsolutePath() + "/Fotos/FotosGS_" + date2 + "N";
			final String pathFotosN = sdCard.getAbsolutePath() + "/Fotos/FotosGS_" + date2 + "N/";
			final String pathFotosF = sdCard.getAbsolutePath() + "/Fotos/FotosGS_" + date2 + "/";
			;

			String sDirectorio = pathFotos;
			final File f = new File(sDirectorio);
			Log.i(null, "lista" + pathFotos);
			final String customURL = "https://opinion.cdmx.gob.mx/fotografias/gabinete_autoschatarra/";

			Log.i("Foto", "CarlosQ =======> lista de fotos: " + pathFotos);
			Log.i("Foto", "CarlosQ =======> pathFotosN: " + pathFotosN);
			Log.i("Foto", "CarlosQ =======> pathFotosF: " + pathFotosF);
			Log.i("Foto", "CarlosQ =======> URL: " + customURL);

			File F = new File(pathFotos);

			try {

				if (F.exists()) {

					File[] ficheros = F.listFiles();

					for (int i = 0; i < ficheros.length; i++) {
						//Simulamos cierto retraso
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
						}

						publishProgress(i / (float) (ficheros.length)); //Actualizamos los valores
					}


					String[] s = new String[ficheros.length];
					String[] t = new String[ficheros.length];
					for (int x = 0; x < ficheros.length; x++) {
						Log.i("Foto", "CarlosQ ===========> lista: " + ficheros[x].getName());
						s[x] = pathFotos + "/" + ficheros[x].getName();
//							t[x] = ficheros[x].getName();
						// solo la foto
						t[x] = ficheros[x].getName();

						//	uploadFotos(s[x],date2);
						URL u = new URL(customURL + t[x]);
						HttpURLConnection huc = (HttpURLConnection) u.openConnection();
						huc.setRequestMethod("GET");  //OR  huc.setRequestMethod ("HEAD");
						huc.connect();
						huc.getResponseCode();
						Log.i("Foto", "CarlosQ: =====================> Respuesta del servidor " + huc.getResponseCode());
						if (huc.getResponseCode() == 200) {
							moveFile(pathFotosN, t[x], pathFotosF);
							Log.i("Foto", "CarlosQ: ================>  Foto en el servidor y Movida a otra carpeta ====>" + t[x]);
						} else if (huc.getResponseCode() == 404) {
							uploadFotos(s[x], date2);
							Log.i("Foto", "CarlosQ: =================> Foto Enviada y a_n sin Moverse ====>" + t[x]);
						}
					}
					// first parameter is d files second parameter is zip file name

				} else {
					Log.i(null, "lista 2: " + "No existe el directorio");
				}
				// first parameter is d files second parameter is zip file name

			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.i(null, "error zip: " + "_" + e.getMessage());
			}


			return date2;
		}


		protected void onProgressUpdate(Float... valores) {
			int p = Math.round(100 * valores[0]);

		}


		protected void onPostExecute(String date2) {
			super.onPostExecute(date2);

			dialog.dismiss();

			Toast.makeText(getApplicationContext(), "Archivo Enviado", Toast.LENGTH_LONG).show();

			dialogo(fecha_envio);


		}

	}

	private void moveFile(String inputPath, String inputFile, String outputPath) {

		InputStream in = null;
		OutputStream out = null;
		try {

			//create output directory if it doesn't exist
			File dir = new File(outputPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}


			in = new FileInputStream(inputPath + inputFile);
			out = new FileOutputStream(outputPath + inputFile);

			byte[] buffer = new byte[1024];
			int read;
			while ((read = in.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}
			in.close();
			in = null;

			// write the output file
			out.flush();
			out.close();
			out = null;

			// delete the original file
			new File(inputPath + inputFile).delete();


		} catch (FileNotFoundException fnfe1) {
			Log.i(null, "Archivos  tag" + fnfe1.getMessage());
		} catch (Exception e) {
			Log.i(null, "Archivos  tag" + e.getMessage());
		}

	}


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar);
		
		//Crea Log cuando falla la app
		Thread.setDefaultUncaughtExceptionHandler(new Crash(this,this));
		
		 Locale.setDefault( Locale.US );
		month = (GregorianCalendar) GregorianCalendar.getInstance();
		itemmonth = (GregorianCalendar) month.clone();

		items = new ArrayList<String>();
		adapter = new CalendarAdapter(this, month);

		GridView gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(adapter);

		handler = new Handler();
		handler.post(calendarUpdater);

		dialog = new ProgressDialog(this);
        dialog.setMessage("Comprimiendo...");
        dialog.setTitle("Progreso");
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(false);

		upLoadServerUriFotos = "https://opinion.cdmx.gob.mx/cgi-bin/php/recibeFotos" + nombreEncuesta + ".php?encuesta=" + nombreEncuesta + "";
		upLoadServerUriBase  = "https://opinion.cdmx.gob.mx/cgi-bin/php/recibeBases" + nombreEncuesta + ".php?encuesta=" + nombreEncuesta + "";

		Button salir=(Button)findViewById(R.id.btnSalir);

		TextView title = (TextView) findViewById(R.id.title);
		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));

		RelativeLayout previous = (RelativeLayout) findViewById(R.id.previous);

		previous.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setPreviousMonth();
				refreshCalendar();
			}
		});

		RelativeLayout next = (RelativeLayout) findViewById(R.id.next);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setNextMonth();
				refreshCalendar();

			}
		});

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				((CalendarAdapter) parent.getAdapter()).setSelected(v);
				String selectedGridDate = CalendarAdapter.dayString.get(position);
				String[] separatedTime = selectedGridDate.split("-");
				String gridvalueString = separatedTime[2].replaceFirst("^0*","");// taking last part of date. ie; 2 from 2012-12-02.
				int gridvalue = Integer.parseInt(gridvalueString);
				// navigate to next or previous month on clicking offdays.
				if ((gridvalue > 10) && (position < 8)) {
					setPreviousMonth();
					refreshCalendar();
				} else if ((gridvalue < 7) && (position > 28)) {
					setNextMonth();
					refreshCalendar();
				}
				((CalendarAdapter) parent.getAdapter()).setSelected(v);

				fecha=selectedGridDate.replace("-", "");
				fecha_envio=selectedGridDate;
				
				Log.i(null,"Zip: "+ fecha );

				new UpdateFotos().execute(fecha);

			}
		});
	}

	protected void setNextMonth() {
		if (month.get(GregorianCalendar.MONTH) == month
				.getActualMaximum(GregorianCalendar.MONTH)) {
			month.set((month.get(GregorianCalendar.YEAR) + 1),
					month.getActualMinimum(GregorianCalendar.MONTH), 1);
		} else {
			month.set(GregorianCalendar.MONTH,
					month.get(GregorianCalendar.MONTH) + 1);
		}

	}

	protected void setPreviousMonth() {
		if (month.get(GregorianCalendar.MONTH) == month
				.getActualMinimum(GregorianCalendar.MONTH)) {
			month.set((month.get(GregorianCalendar.YEAR) - 1),
					month.getActualMaximum(GregorianCalendar.MONTH), 1);
		} else {
			month.set(GregorianCalendar.MONTH,
					month.get(GregorianCalendar.MONTH) - 1);
		}

	}

	protected void showToast(String string) {
		Toast.makeText(this, string, Toast.LENGTH_SHORT).show();

	}

	public void refreshCalendar() {
		TextView title = (TextView) findViewById(R.id.title);

		adapter.refreshDays();
		adapter.notifyDataSetChanged();
		handler.post(calendarUpdater); // generate some calendar items

		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
	}

	public Runnable calendarUpdater = new Runnable() {

		@Override
		public void run() {
			items.clear();

			// Print dates of the current week
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
			String itemvalue;
			for (int i = 0; i < 7; i++) {
				itemvalue = df.format(itemmonth.getTime());
				itemmonth.add(GregorianCalendar.DATE, 1);
				items.add("2012-09-12");
				items.add("2012-10-07");
				items.add("2012-10-15");
				items.add("2012-10-20");
				items.add("2012-11-30");
				items.add("2012-11-28");
			}

			adapter.setItems(items);
			adapter.notifyDataSetChanged();
		}
	};

	//PARA SUBIR ARCHIVOS Y FOTOS
	
	public void dialogo(final String fecha) {
		
		String date = fecha;
		
		String date2=date.replace("-", "");
		
		File sdCard;
		sdCard = Environment.getExternalStorageDirectory();
//		tablet = prefix;
		tablet = cachaChip();

		final String AudiosZip = nombreEncuesta+"-Audio"+date2+".zip";
		Log.i(null,"zip: "+ AudiosZip);
		
				
		File file = new File(sdCard.getAbsolutePath()+ "/"+AudiosZip);
		Log.i(null,"zip: "+ "File: "+file);
		
			sdCard = Environment.getExternalStorageDirectory();
		final String pathFotos = sdCard.getAbsolutePath() + "/Mis_archivos/Fotos" + date2 + "N";
		
		Log.i(null, "Fecha: "+date2);
		Log.i(null, "pathAudios: "+pathFotos);
		
		

		File F = new File(pathFotos);
		File[] ficheros = F.listFiles();
		int cuantos = 0;
		
		try {
			cuantos=ficheros.length;
			
			if(cuantos>0){
				
				cuantos=ficheros.length;
				
			}else{
				cuantos=0;
			}
		} catch (Exception e) {
			Log.i(null, "tamaño: "+e.getMessage());
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setMessage(cuentaArchivos())
		builder.setMessage(
//				"Encuestas Normales:\t\t\t" + dameNormal(fecha)+"\n\n"+
//				"Abandonos:\t\t" + dameAbandono(fecha)+"\n\n"+
//				"Rechazos:\t\t\t" + dameRechazo(fecha)+"\n\n" +
				"Fotos:\t\t\t\t"+cuantos
				)
		
		

				.setTitle("Fotografías Enviadas: ").setCancelable(false)
//				.setNegativeButton("SALIR", new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int id) {
//
//						//método a implementar
//					
//					}
//				})
				.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						Log.i(null, "Enviados: "+"fecha: "+fecha);
//						Log.i(null, "Enviados: "+"abandono: "+dameAbandono(fecha));
//						Log.i(null, "Enviados: "+"rechazo: "+dameRechazo(fecha));

//						pasaDatos();


					}
				});
		AlertDialog alert = builder.create();
		alert.show();

	}
	


	

	
	public String cachaUsuario(){
		Bundle datos=this.getIntent().getExtras();
	    String usuario=datos.getString("usuario");
	    return usuario;
	}


	
	public String cachaNombre(){
		Bundle datos=this.getIntent().getExtras();
	    String Nombre=datos.getString("Nombre");
	    return Nombre;
	}
	

	

}

