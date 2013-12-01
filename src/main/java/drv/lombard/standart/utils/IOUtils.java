package drv.lombard.standart.utils;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: andy
 * Date: 11/30/13
 * Time: 3:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class IOUtils {

  public static void closeQuietly(Closeable closeable){
    if(closeable==null) return;
    try {
      closeable.close();
    } catch (IOException e) {
      //just ignore
    }
  }

  public static void closeQuietly(Socket socket){
    if(socket==null) return;
    try {
      socket.close();
    } catch (IOException e) {
      //just ignore
    }
  }


}
