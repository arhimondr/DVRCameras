package drv.lombard.standart.connection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Decode
        implements Runnable {

  private MessengeData F_MessengeData;

  public Decode(MessengeData messengedata) {
    F_MessengeData = messengedata;
  }

  public void run() {

    try{
      while (!Thread.currentThread().isInterrupted()) {
        byte[] imageBytes = F_MessengeData.delReciveBuffer();

        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        F_MessengeData.addDecodeBuffer(bitmap);
      }
    } catch (InterruptedException e){
      //Just end a thread
    }

  }
}
