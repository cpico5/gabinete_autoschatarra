package mx.gob.cdmx.gabineteautoschatarra.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class ImageTool {
    final static int escala = 6;

    public static void setPic(ImageView imagen, String foto) {
        // Get the dimensions of the View
        int targetW = imagen.getWidth() / escala;
        int targetH = imagen.getHeight() / escala;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(foto, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(foto, bmOptions);
        imagen.setImageBitmap(bitmap);
    }
}
