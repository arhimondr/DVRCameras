package drv.lombard.standart;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import drv.lombard.standart.api.HttpClientHolder;
import org.apache.http.HttpStatus;
import static drv.lombard.standart.Constants.*;

public class LoginActivity extends Activity {

  private EditText loginField;
  private EditText passwordField;
  private EditText connectionURLField;

  /**
   * Called when the activity is first created.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    loginField = (EditText) findViewById(R.id.Login);
    passwordField = (EditText) findViewById(R.id.Password);
    connectionURLField = (EditText) findViewById(R.id.connectionUrl);

    DebugLogConfig.enable();
  }

  public void onConnectPressed(View button) {

    if(!validate()) return;

    String login = loginField.getText().toString();
    String password = passwordField.getText().toString();

    String url = connectionURLField.getText().toString();

    if(url.endsWith("/") && url.length()>1){
      url = url.substring(0, url.length()-1);
    }

    final String connectionURL = url;

    final ProgressDialog progress = new ProgressDialog(LoginActivity.this);
    progress.setTitle("Loading");
    progress.setMessage("Wait while loading...");
    progress.setCancelable(false);
    progress.show();

    HttpClientHolder.get().get(connectionURL + "/Login.cgi?Username=" + login + "&Password=" + password, new TextHttpResponseHandler() {

      @Override
      public void onSuccess(int statusCode, org.apache.http.Header[] headers, java.lang.String responseBody) {

        if (statusCode == HttpStatus.SC_OK) {
          Intent intent = new Intent(LoginActivity.this, CameraDisplayActivity.class);
          intent.putExtra(SESSION_ID, parseSessionId(responseBody));
          intent.putExtra(CONNECTION_URL, connectionURL);
          startActivity(intent);
        } else {
          showAlertDialog("Invalid auth");
        }

        progress.dismiss();
      }

      @Override
      public void onFailure(int statusCode, org.apache.http.Header[] headers, byte[] responseBody, java.lang.Throwable error) {
        showAlertDialog("Could not connect to remote server. Network is unavailable, or connection URL is invalid");
        progress.dismiss();
      }
    });

  }

  private String parseSessionId(String response){
    return  new String(response.substring(response.indexOf("Session-ID=") + 11, response.indexOf("Session-ID=") + 41));
  }

  private void showAlertDialog(String message) {
    new AlertDialog.Builder(this).setMessage(message)
            .setCancelable(true).setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                      }
                    }).create().show();
  }

  private boolean validate(){

    loginField.setError(null);
    passwordField.setError(null);
    connectionURLField.setError(null);

    String login = loginField.getText().toString();
    String password = passwordField.getText().toString();
    String connectionURL = connectionURLField.getText().toString();

    if(login==null || login.length()==0){
      loginField.setError("You must enter login");
      return false;
    }

    if(password==null || password.length()==0){
      passwordField.setError("You must enter password");
      return false;
    }

    if(connectionURL==null || connectionURL.length()==0){
      connectionURLField.setError("You must enter a connection URL");
      return false;
    }

    if(!URLUtil.isValidUrl(connectionURL)){
      connectionURLField.setError("You must enter valid URL");
      return false;
    }

    return true;
  }

  public void onDefaultPressed(View button){
    loginField.setText("admin");
    passwordField.setText("admin");
    connectionURLField.setText("http://dvr.dyndns.org:88");
    validate();
  }
}
