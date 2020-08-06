package mx.gob.cdmx.gabineteautoschatarra;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
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

import mx.gob.cdmx.gabineteautoschatarra.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInstaller.Session;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings.Secure;
//import android.service.textservice.SpellCheckerService.Session;
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



public class CalendarViewAudios extends Activity {
	
Calendar c = Calendar.getInstance();
	
	SimpleDateFormat df3 = new SimpleDateFormat("yyyMMdd");
	String formattedDate3 = df3.format(c.getTime());
	SimpleDateFormat df4 = new SimpleDateFormat("yyy-MM-dd");
	String formattedDateFecha = df4.format(c.getTime());
	SimpleDateFormat df5 = new SimpleDateFormat("HH:mm a");
	String formattedDate5 = df5.format(c.getTime());
	
	Session session=null;
	ProgressDialog pdialog=null;
	 private ProgressDialog dialog;
	Context context=null;
	EditText reciep,sub,msg;
	String recibe,recibe_copia,passrecibe, manda,passmanda,subject, textMessage;

	public GregorianCalendar month, itemmonth;// calendar instances.

	public CalendarAdapter adapter;// adapter instance
	public Handler handler;// for grabbing some event values for showing the dot
							// marker.
	public ArrayList<String> items; // container to store calendar items which
									// needs showing the event marker
	int serverResponseCode = 0;
	String upLoadServerUri = null;
	String upLoadServerUriAudio = null;
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
	
	public String cachaChip(){
		Bundle datos=this.getIntent().getExtras();
	    String Chip=datos.getString("Chip");
	    return Chip;
	}
	
	public String sacaImei(){
		String szImei;
		TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);//Telefono
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
			
			Intent intent = new Intent(CalendarViewAudios.this, Menu_Principal.class);
			Bundle bundle = new Bundle();
			bundle.putString("usuario", cachaUsuario());
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
			
		}
	
		
		//Enviar Audio
				public int uploadAudio(String sourceFileUri, String fech) {
			        
			        File sdCard;
					sdCard = Environment.getExternalStorageDirectory();
					final String pathAudios = sdCard.getAbsolutePath() + "/"+ nombreEncuesta+"-Audio"+fech;
			        
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
			             
			             dialog.dismiss(); 
			             Log.i(null, "archivo:  El Archivo no existe... :" + pathAudios + "" + "/" + "20161124_002_359083065132816_1.jpg");
			             runOnUiThread(new Runnable() {
			                 public void run() {

			                 }
			             }); 
			              
			             return 0;
			          
			        }
			        else
			        {
			             try { 
			                   // open a URL connection to the Servlet
			                 FileInputStream fileInputStream = new FileInputStream(sourceFile);
			                 URL url = new URL(upLoadServerUriAudio);
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

			                 Log.i("uploadFile", "HTTP Response is : "+ serverResponseMessage + ": " + serverResponseCode);
			                  
			                 if(serverResponseCode == 200){
			                      
			                     runOnUiThread(new Runnable() {
			                          public void run() {
			                               
			                              String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
			                                            +" http://www.androidexample.com/media/uploads/"
			                                            +"20161124_002_359083065132816_1.jpg";

//			                              Toast.makeText(Entrada.this, "File Upload Complete."+msg,Toast.LENGTH_SHORT).show();
			                          }
			                      });                
			                 }    
			                  
			                 //close the streams //
			                 fileInputStream.close();
			                 dos.flush();
			                 dos.close();
			                   
			            } catch (MalformedURLException ex) {
			                 
			                dialog.dismiss();  
			                ex.printStackTrace();
			                 
			                runOnUiThread(new Runnable() {
			                    public void run() {
//			                        messageText.setText("MalformedURLException Exception : check script url.");
			                        Toast.makeText(CalendarViewAudios.this, "MalformedURLException", 
			                                                            Toast.LENGTH_SHORT).show();
			                    }
			                });
			                 
			                Log.i(null, "archivo:  El Archivo no existe... :" + "Upload file to server "+ "error: " + ex.getMessage());

//			                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
			            } catch (Exception e) {
			                 
			                dialog.dismiss();  
			                e.printStackTrace();
			                 
			                runOnUiThread(new Runnable() {
			                    public void run() {
//			                        messageText.setText("Error de Internet");
			                        Toast.makeText(CalendarViewAudios.this, "Error de Internet", 
			                                Toast.LENGTH_SHORT).show();
			                    }
			                });
			                Log.i(null, "archivo:  El Archivo no existe... :" + "Upload file to server Exception "+ "Exception : "+ e.getMessage());

//			                Log.e("Upload file to server Exception", "Exception : "
//			                                                 + e.getMessage(), e);  
			            }
			            return serverResponseCode; 
			             
			         } // End else block 
			       } 

		
		
		class UpdateAudios extends AsyncTask<String, Float, String> {

			protected void onPreExecute() {
				super.onPreExecute();
			
				dialog = new ProgressDialog(CalendarViewAudios.this);
	            dialog.setMessage("Enviando Audios...");
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
					final String pathAudios = sdCard.getAbsolutePath() + "/" + nombreEncuesta+"-Audio"+date2;
		        
			        String sDirectorio = pathAudios;
					final File f = new File(sDirectorio);
					Log.i(null,"lista"+pathAudios);
					
//					final String customURL = "https://encuestas.sies2018.org/coordinacion/audios/";
					final String customURL = "https://censo19s.mx/cgi-bin/audios/censo_vulnerabilidad/";

				Log.i(null, "lista 1: " + pathAudios);

				File F = new File(pathAudios);

				try {
					
					if (F.exists()) {
						
					File[] ficheros = F.listFiles();
					
					for (int i = 0; i <ficheros.length; i++) {
	                    //Simulamos cierto retraso
	                    try {Thread.sleep(500); }
	                    catch (InterruptedException e) {}
	 
	                    publishProgress(i/(float)(ficheros.length)); //Actualizamos los valores
	                }
	 

					
						String[] s = new String[ficheros.length];
						String[] t = new String[ficheros.length];
						for (int x = 0; x < ficheros.length; x++) {
							Log.i(null, "lista: " + ficheros[x].getName());
							s[x] = pathAudios + "/" + ficheros[x].getName();
							t[x] = ficheros[x].getName();
							 uploadAudio(s[x],date2);
//							 URL u = new URL (customURL+t[x]);
//     						HttpURLConnection huc =  ( HttpURLConnection )  u.openConnection (); 
//     						huc.setRequestMethod ("GET");  //OR  huc.setRequestMethod ("HEAD"); 
//     						huc.connect () ; 
//     						huc.getResponseCode();
//     						Log.i(null, "Archivo:  lista ==>" + huc.getResponseCode());
//     						if(huc.getResponseCode()==200 || huc.getResponseCode()==500){
////     							moveFile(pathFotosN, t[x], pathFotosF);
//     							Log.i(null, "Archivo:  Movido ==>" + t[x] );
//     						}else{
//     							Log.i(null, "Archivo:  Sin Moverse ==>" + t[x] );
//     						}
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
			
			
			protected void onProgressUpdate (Float... valores) {
	              int p = Math.round(100*valores[0]);
	              dialog.setProgress(p);
	          }


//tomo
			protected void onPostExecute(String date2) {
				super.onPostExecute(date2);
				dialog.dismiss();
				
				Toast.makeText(getApplicationContext(), "Audios Enviados", Toast.LENGTH_LONG).show();
				dialogo(fecha_envio);
//				correo(date2, prefix);
//				correo(date2, sacaChip());
				
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
        
		upLoadServerUriAudio = "https://opinion.cdmx.gob.mx/cgi-bin/php/recibeAudios"+nombreEncuesta+".php?encuesta='"+nombreEncuesta+"'";
//		upLoadServerUriFoto = "https://censo19s.mx/cgi-bin/php/recibeFotos.php";
//		upLoadServerUriFirma = "https://censo19s.mx/cgi-bin/php/recibeFirmas.php";
		
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
				
				new UpdateAudios().execute(fecha);
//				new UpdateAudiosServidor().execute(fecha);
				

						//correo(fecha,prefix);

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
		final String pathAudios = sdCard.getAbsolutePath() + "/" + nombreEncuesta+"-Audio"+date2;
		
		Log.i(null, "Fecha: "+date2);
		Log.i(null, "pathAudios: "+pathAudios);
		
		

		File F = new File(pathAudios);
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
				"Audios:\t\t\t\t"+cuantos
				)
		
		

				.setTitle("Registros Enviados: ").setCancelable(false)
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
						Log.i(null, "Enviados: "+"normal: "+dameNormal(fecha));
//						Log.i(null, "Enviados: "+"abandono: "+dameAbandono(fecha));
//						Log.i(null, "Enviados: "+"rechazo: "+dameRechazo(fecha));

//						pasaDatos();


					}
				});
		AlertDialog alert = builder.create();
		alert.show();

	}
	
	public String dameNormal(String fecha) {
		String DATABASE_NAME = Environment.getExternalStorageDirectory() +"/Mis_archivos/" +nombreEncuesta+"_"+sacaImei()+"";

		// Abrimos la base de datos 'DBUsuarios' en modo escritura
		usdbh = new UsuariosSQLiteHelper(this, "F", null, 1,DATABASE_NAME);
		String cuantosNormal;
		db = usdbh.getWritableDatabase();
		String selectQuery1 = "select count(*) as total from encuestas where fecha='" + fecha + "'";
		Cursor c = db.rawQuery(selectQuery1, null);
		if (c.getCount() > 0) {
			// Recorremos el cursor hasta que no haya más registros
			c.moveToFirst();
			cuantosNormal = c.getString(0);
		} else {
			cuantosNormal = "0";
		}
		c.close();
		db.close();
		return cuantosNormal;
	}
	
	public int dameAbandono(String fecha) {
		String DATABASE_NAME = Environment.getExternalStorageDirectory() +"/Mis_archivos/" +nombreEncuesta+"_"+sacaImei()+"";

		// Abrimos la base de datos 'DBUsuarios' en modo escritura
		usdbh = new UsuariosSQLiteHelper(this, "F", null, 1,DATABASE_NAME);
		int cuantosAbandonos;
		db = usdbh.getWritableDatabase();
		String selectQuery1 = "select count(*) as total from encuestas where fecha='" + fecha + "' and tipocaptura='ABANDONO'";
		Cursor c = db.rawQuery(selectQuery1, null);
		if (c.getCount() > 0) {
			// Recorremos el cursor hasta que no haya más registros
			c.moveToFirst();
			cuantosAbandonos = c.getInt(0);
		} else {
			cuantosAbandonos = 0;
		}
		c.close();
		db.close();
		return cuantosAbandonos;
	}
	
	public String dameRechazo(String fecha) {
		String DATABASE_NAME = Environment.getExternalStorageDirectory() +"/Mis_archivos/" +nombreEncuesta+"_"+sacaImei()+"";

		// Abrimos la base de datos 'DBUsuarios' en modo escritura
		usdbh = new UsuariosSQLiteHelper(this, "F", null, 1,DATABASE_NAME);
		String cuantosRechazos;
		db = usdbh.getWritableDatabase();
		String selectQuery1 = "select count(*) as total from encuestas where fecha='" + fecha + "' and tipocaptura='RECHAZO'";
		Cursor c = db.rawQuery(selectQuery1, null);
		if (c.getCount() > 0) {
			// Recorremos el cursor hasta que no haya más registros
			c.moveToFirst();
			cuantosRechazos = c.getString(0);
		} else {
			cuantosRechazos = "0";
		}
		c.close();
		db.close();
		return cuantosRechazos;
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
	


	public String nombreArchivo(String selectedGridDate){
		TelephonyManager tManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		//Getting IMEI Number of Devide
		String Imei=tManager.getDeviceId();
		String date=selectedGridDate.toString();
		String var2=".txt";
		String var3=date+var2;
		final String nombre = date+"_"+Imei+ var2;
		return nombre;
	}
	
	public String nombreBaseDatos(){
//		String Base=nombreEncuesta+"_"+prefix;
		String Base=nombreEncuesta+"_"+cachaChip();
		return Base;
	}
	
	public void dialogoTamaño(){
//		timer.cancel();
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        CalendarViewAudios.this.runOnUiThread(new Runnable() {
            public void run() {
                builder.setMessage("El Archivo excede los 20 MB")
                        .setTitle("IMPORTANTE...!!!")
                        .setCancelable(false)
                        .setPositiveButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {


                                    System.exit(0);
                                    	
                                    	
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

    }
	

}

