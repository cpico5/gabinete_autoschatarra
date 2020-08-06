package mx.gob.cdmx.gabineteautoschatarra.service;

import android.app.Activity;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class Crash implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler defaultUEH;
    private Activity app = null;

    public Crash(Activity app) {
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        this.app = app;
    }

    public void uncaughtException(Thread t, Throwable e) {
        StackTraceElement[] arr = e.getStackTrace();
        String report = e.toString()+"\n\n";
        report += "--------- Seguimiento de Pila ---------\n\n";
        for (int i=0; i<arr.length; i++) {
            report += "    "+arr[i].toString()+"\n";
        }
        report += "-------------------------------\n\n";

        // If the exception was thrown in a background thread inside
        // AsyncTask, then the actual exception can be found with getCause

        report += "--------- Causa ---------\n\n";
        Throwable cause = e.getCause();
        if(cause != null) {
            report += cause.toString() + "\n\n";
            arr = cause.getStackTrace();
            for (int i=0; i<arr.length; i++) {
                report += "    "+arr[i].toString()+"\n";
            }
        }
        report += "-------------------------------\n\n";

        
        final String logString = new String(report.getBytes());
		 
		 //create text file in SDCard
		 File sdCard = Environment.getExternalStorageDirectory();
		 File dir = new File (sdCard.getAbsolutePath() + "/Mis_archivos/Mis_Logs");
		 dir.mkdirs();
		 String nombreE = "ubicacion";
		 File file = new File(dir, nombreE+".txt");
        
        try {  
  		  //to write logcat in text file
  		  FileOutputStream fOut = new FileOutputStream(file);
  		  OutputStreamWriter osw = new OutputStreamWriter(fOut); 

  		        // Write the string to the file
  		        osw.write(logString);            
  		        osw.flush();
  		        osw.close();
  		 } catch (FileNotFoundException e1) {
  		  e1.printStackTrace();
  		 } catch (IOException e1) {
  		  e1.printStackTrace();
  		 }

        defaultUEH.uncaughtException(t, e);
    }
}
	

	       
	
