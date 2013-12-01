package drv.lombard.standart;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Debug;
import android.os.PowerManager;
import drv.lombard.standart.api.DVRApiFacade;
import drv.lombard.standart.api.Quality;
import drv.lombard.standart.api.Resolution;
import drv.lombard.standart.connection.DisplayVideoApplication;
import drv.lombard.standart.connection.Picture;

import static drv.lombard.standart.Constants.*;

/**
 * Created with IntelliJ IDEA.
 * User: andy
 * Date: 11/17/13
 * Time: 8:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class CameraDisplayActivity extends Activity {


  private DrawView drawView;
  private PowerManager.WakeLock wakeLock;

  private DisplayVideoApplication displayVideoApplication;

  private DVRApiFacade dvrApiFacade;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    final String sessionId = getIntent().getStringExtra(SESSION_ID);
    final String connectionUrl = getIntent().getStringExtra(CONNECTION_URL);

    dvrApiFacade = new DVRApiFacade(connectionUrl, sessionId);
    dvrApiFacade.changeQuality(Quality.HIGH);
    dvrApiFacade.changeResolution(Resolution.HIGH);

    displayVideoApplication = new DisplayVideoApplication(connectionUrl, sessionId);
    drawView = new DrawView(this, displayVideoApplication.getPicture());
    drawView.setBackgroundColor(Color.WHITE);
    setContentView(drawView);

    final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
    wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
    wakeLock.acquire();

    displayVideoApplication.start();
    drawView.invalidate();
  }

  @Override
  protected void onPause() {
    super.onPause();
    displayVideoApplication.pause();
  }

  @Override
  protected void onResume() {
    super.onResume();
    displayVideoApplication.resume();
  }

  @Override
  protected void onDestroy() {
    displayVideoApplication.stop();
    wakeLock.release();
    super.onDestroy();
  }

}