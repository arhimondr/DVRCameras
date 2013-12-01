package drv.lombard.standart.api;

import com.loopj.android.http.AsyncHttpClient;

/**
 * Created with IntelliJ IDEA.
 * User: andy
 * Date: 12/1/13
 * Time: 12:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class HttpClientHolder {

  private static AsyncHttpClient asyncHttpClient;

  public static AsyncHttpClient get(){
    if(asyncHttpClient==null){
      synchronized (HttpClientHolder.class){
        if(asyncHttpClient==null){
          asyncHttpClient = new AsyncHttpClient();
        }
      }
    }

    return asyncHttpClient;
  }
}
