package drv.lombard.standart.api;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;
import org.apache.http.Header;

/**
 * Created with IntelliJ IDEA.
 * User: andy
 * Date: 12/1/13
 * Time: 12:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class DVRApiFacade {

  private String connectionURL;
  private String sessionID;
  private AsyncHttpClient httpClient;

  private static final AsyncHttpResponseHandler NO_OP_RESPONSE_HANDLER = new TextHttpResponseHandler(){
    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseBody) {
      super.onSuccess(statusCode, headers, responseBody);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {
      super.onFailure(statusCode, headers, responseBody, error);    //To change body of overridden methods use File | Settings | File Templates.
    }
  };

  public DVRApiFacade(String connectionURL, String sessionID) {
    this.connectionURL = connectionURL;
    this.sessionID = sessionID;
    httpClient = HttpClientHolder.get();
  }


  public void changeResolution(Resolution resolution){
    httpClient.get(connectionURL+"/Setresolution.cgi?Cookie="+sessionID+"&RES="+resolution.ordinal(), NO_OP_RESPONSE_HANDLER);
  }

  public void changeQuality(Quality quality){
    httpClient.get(connectionURL+"/Setquality.cgi?Cookie="+sessionID+"&QUA="+quality.ordinal(), NO_OP_RESPONSE_HANDLER);
  }

}
