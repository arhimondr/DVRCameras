package drv.lombard.standart.connection;

import drv.lombard.standart.utils.IOUtils;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: andy
 * Date: 11/30/13
 * Time: 3:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class Download implements Runnable {

  private static int HEADER_SIZE_ADD = 1;
  private static int HEADER_HANDLER_USER_ADD = 2;
  private static int HEADER_ALARM_ADD = 4;
  private static int HEADER_RES_ADD = 8;
  private static int HEADER_QUA_ADD = 16;
  private static int HEADER_TIMESTAMP_ADD = 32;
  private static int HEADER_ONLINE_USER_ADD = 64;
  private static int HEADER_ALARM_TRIGGER_ADD = 128;
  private static int HEADER_MOTION_DETECT_ADD = 1;
  private static int HEADER_VIDEO_LOSS_ADD = 2;
  private static int HEADER_RETR_DOWNLOAD_FILE = 4;
  private static int HEADER_CH_ADD = 8;
  private static int HEADER_NO_SIGNAL_ADD = 16;
  private static int HEADER_SERVER_TIME_ADD = 32;


  private MessengeData messengeData;

  private String strUrl;

  public Download(String strUrl, MessengeData messengeData) {
    this.messengeData = messengeData;
    this.strUrl = strUrl;
  }

  @Override
  public void run() {
    try {
      makeConnection();
    } catch (InterruptedException e) {
      //just complete thread
    }
  }

  private void retryConnection() throws InterruptedException {
    Thread.sleep(1000);
    makeConnection();
  }

  private void makeConnection() throws InterruptedException {
    Socket sock = null;
    InputStream inputStream = null;
    OutputStream outputStream = null;
    try {
      URL url = new URL(strUrl);

      InetAddress inetaddress = InetAddress.getByName(url.getHost());
      sock = new Socket(inetaddress, url.getPort());
      inputStream = new DataInputStream(sock.getInputStream());
      outputStream = new DataOutputStream(sock.getOutputStream());

      String str_LoginMessenge =
              "HTTP/1.1\r\nAccept: */* \r\nAccept-Language: zh-tw\r\n" + "Accept-Encoding: gzip, deflate\r\nUser-Agent: Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.4322)\r\n"
                      + "Host: " + url.getHost() + "\r\n" + "Connection: Keep-Alive\r\n\r\n";

      byte[] data = ("GET " + url.getPath() + "?" + url.getQuery() + " " + str_LoginMessenge).getBytes();

      outputStream.write(data, 0, data.length);
      outputStream.flush();

      receiveData(inputStream);
    } catch (IOException ioexception) {
      retryConnection();
    } finally {
      IOUtils.closeQuietly(outputStream);
      IOUtils.closeQuietly(inputStream);
      IOUtils.closeQuietly(sock);
    }
  }

  private void receiveData(InputStream inputStream) throws IOException, InterruptedException {

    boolean b_downloadMessenge;
    boolean b_Header_No_Signal;
    byte abyte0[] = new byte[50];

    while (!Thread.currentThread().isInterrupted()) {
      int j1 = 4;
      int i = 0;
      int i1 = 0;
      do {
        i = inputStream.read(abyte0, i1, j1 - i1);
        if (i == -1)
          throw new IOException("Can't read the Login data !!");
        i1 += i;
      } while (i1 < j1);
      j1 = 0;
      if ((abyte0[0] & HEADER_SIZE_ADD) != 0)
        j1 += 4;
      if ((abyte0[0] & HEADER_HANDLER_USER_ADD) != 0) {
        j1 += 16;
      }
      if ((abyte0[0] & HEADER_ALARM_ADD) != 0)
        j1 += 2;
      if ((abyte0[0] & HEADER_RES_ADD) != 0)
        j1++;
      if ((abyte0[0] & HEADER_QUA_ADD) != 0)
        j1++;
      if ((abyte0[0] & HEADER_TIMESTAMP_ADD) != 0) {
        j1 += 4;
      }
      if ((abyte0[0] & HEADER_ONLINE_USER_ADD) != 0) {
        j1++;
      }
      if ((abyte0[0] & HEADER_ALARM_TRIGGER_ADD) != 0) {
        System.out.println("Triger_add");
        j1 += 2;
      }

      if ((abyte0[1] & HEADER_MOTION_DETECT_ADD) != 0) {
        System.out.println("motion");
        j1 += 2;
      }

      if ((abyte0[1] & HEADER_VIDEO_LOSS_ADD) != 0)
        j1 += 2;

      if ((abyte0[1] & HEADER_RETR_DOWNLOAD_FILE) != 0)
        b_downloadMessenge = true;
      else
        b_downloadMessenge = false;

      if ((abyte0[1] & HEADER_CH_ADD) != 0)
        j1++;

      if ((abyte0[1] & HEADER_NO_SIGNAL_ADD) != 0) {
        b_Header_No_Signal = true;
      } else {
        b_Header_No_Signal = false;
      }

      if ((abyte0[1] & HEADER_SERVER_TIME_ADD) != 0) {
        j1 += 4;
      }

      for (i1 = 0; i1 != j1; ) {
        int j = inputStream.read(abyte0, i1, j1 - i1);
        if (j == -1)
          throw new IOException("Can't read a image data  b");
        i1 += j;
      }

      j1 = 0;
      j1 = abyte0[3] & 0xff;
      j1 <<= 8;
      j1 += abyte0[2] & 0xff;
      j1 <<= 8;
      j1 += abyte0[1] & 0xff;
      j1 <<= 8;
      j1 += abyte0[0] & 0xff;

      byte abyte1[] = new byte[j1];
      i1 = 0;
      do {
        int l = inputStream.read(abyte1, i1, j1 - i1);
        if (l == -1)
          throw new IOException("Can't read a image data  c");
        i1 += l;
      } while (i1 < j1);

      if (abyte1[0] == -1 && abyte1[1] == -40) {
        if (!b_downloadMessenge && !b_Header_No_Signal)
          messengeData.addReciveBuffer(abyte1);
      } else if (!b_downloadMessenge) {
        System.out.println("not FF D8");
        boolean flag5 = true;
        do {
          if (!flag5)
            break;
          do
            inputStream.read(abyte0, 0, 1);
          while (abyte0[0] == -1);
          inputStream.read(abyte0, 0, 1);
          if (abyte0[0] == -39)
            flag5 = false;
        } while (true);
      }
    }
  }
}
