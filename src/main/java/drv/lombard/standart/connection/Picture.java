package drv.lombard.standart.connection;

import android.graphics.Bitmap;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Picture
        implements Runnable {

  private MessengeData messengeData;

  private volatile boolean paused;

  private List<BitmapAvailableListener> listeners = new CopyOnWriteArrayList<BitmapAvailableListener>();

  public Picture(MessengeData messengedata) {
    this.messengeData = messengedata;
  }

  public void addListener(BitmapAvailableListener listener){
    listeners.add(listener);
  }

  public void pause(){
    paused = true;
  }

  public synchronized void resume(){
    paused = false;
    notify();
  }

  public void run() {
    try{
      while (!Thread.currentThread().isInterrupted()) {

        synchronized (this) {
          while (paused) wait();
        }

        Bitmap realBitmap = messengeData.delDecodeBuffer();

        //currentBitmap = Bitmap.createScaledBitmap(realBitmap, realBitmap.getWidth()*2, realBitmap.getHeight()*2, true);

        for(BitmapAvailableListener listener : listeners){
          listener.onNewBitmapAvailable(realBitmap);
        }
      }
    } catch (InterruptedException e){
      //just end a thread
    }
  }

}
