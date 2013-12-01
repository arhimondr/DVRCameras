package drv.lombard.standart.connection;

import android.graphics.Bitmap;

public class MessengeData {

  private int n_Recive_i;
  private int n_Recive_j;
  private int n_Recive_j1;
  private int n_Decode_i;
  public int n_Decode_j;
  private int n_Decode_j1;
  private int ReciveSize;
  private int DecodeSize;
  public byte b_RecieveBuffer[][];
  private Bitmap Img[];

  public MessengeData() {
    n_Recive_i = 0;
    n_Recive_j = 0;
    n_Recive_j1 = 3;
    n_Decode_i = 0;
    n_Decode_j = 0;
    n_Decode_j1 = 3;
    ReciveSize = 10;
    DecodeSize = 10;
    b_RecieveBuffer = new byte[ReciveSize][];
    Img = new Bitmap[DecodeSize];
  }

  public void MessengeData() {
  }

  public synchronized void addReciveBuffer(byte abyte0[]) throws InterruptedException {
    while (b_RecieveBuffer[n_Recive_i] != null)
        wait();
    b_RecieveBuffer[n_Recive_i] = abyte0;
    abyte0 = null;
    n_Recive_i = (n_Recive_i + 1) % ReciveSize;
    notify();
  }

  public synchronized byte[] delReciveBuffer() throws InterruptedException {
    while (b_RecieveBuffer[n_Recive_j] == null)
        wait();
    b_RecieveBuffer[n_Recive_j1] = null;
    n_Recive_j1 = n_Recive_j;
    n_Recive_j = (n_Recive_j + 1) % ReciveSize;
    notify();
    return b_RecieveBuffer[n_Recive_j1];
  }

  public synchronized void addDecodeBuffer(Bitmap image) throws InterruptedException {
    while (Img[n_Decode_i] != null)
        wait();
    Img[n_Decode_i] = image;
    image = null;
    n_Decode_i = (n_Decode_i + 1) % DecodeSize;
    notify();
  }

  public synchronized Bitmap delDecodeBuffer() throws InterruptedException {
    while (Img[n_Decode_j] == null)
        wait();
    Img[n_Decode_j1] = null;
    n_Decode_j1 = n_Decode_j;
    n_Decode_j = (n_Decode_j + 1) % DecodeSize;
    notify();
    return Img[n_Decode_j1];
  }


}
