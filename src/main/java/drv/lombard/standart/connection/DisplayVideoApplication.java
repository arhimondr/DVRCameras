package drv.lombard.standart.connection;

import java.io.*;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: andy
 * Date: 11/30/13
 * Time: 12:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class DisplayVideoApplication {

  private MessengeData messengeData;
  private Download download;
  private Decode decode;
  private Picture picture;

  private Thread downloadThread;
  private Thread decodeThread;
  private Thread pictureThread;

  public DisplayVideoApplication(String connectionURL, String sessionID){

    String strUrl = connectionURL+"/Getvideo.cgi?Cookie=" + sessionID;

    messengeData = new MessengeData();
    download = new Download(strUrl, messengeData);
    decode = new Decode(messengeData);
    picture = new Picture(messengeData);
  }

  public void start(){
    downloadThread = new Thread(download, "##Application Download Thread");
    decodeThread = new Thread(decode, "##Application Decode Thread");
    pictureThread = new Thread(picture, "##Application Picture Thread");

    downloadThread.start();
    decodeThread.start();
    pictureThread.start();
  }

  public void pause(){
    picture.pause();
  }

  public void resume(){
    picture.resume();
  }

  public void stop(){
    pictureThread.interrupt();
    decodeThread.interrupt();
    downloadThread.interrupt();
  }

  public Picture getPicture() {
    return picture;
  }


}
