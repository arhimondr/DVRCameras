package drv.lombard.standart;


import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.*;

/**
 * Created with IntelliJ IDEA.
 * User: andy
 * Date: 11/17/13
 * Time: 8:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class DebugLogConfig {

  static DalvikLogHandler activeHandler;

  protected static class DalvikLogHandler extends Handler {

    private static final String LOG_TAG = "HttpClient";

    @Override
    public void close() {
      // do nothing
    }

    @Override
    public void flush() {
      // do nothing
    }

    @Override
    public void publish(LogRecord record) {
      if (record.getLoggerName().startsWith("org.apache")) {
        Log.d(LOG_TAG, record.getMessage());
      }
    }
  }

  public static void enable() {
    try {
      String config = "org.apache.http.impl.conn.level = FINEST\n"
              + "org.apache.http.impl.client.level = FINEST\n"
              + "org.apache.http.client.level = FINEST\n" + "org.apache.http.level = FINEST";
      InputStream in = new ByteArrayInputStream(config.getBytes());
      LogManager.getLogManager().readConfiguration(in);
    } catch (IOException e) {
      Log
              .w(DebugLogConfig.class.getSimpleName(),
                      "Can't read configuration file for logging");
    }
    Logger rootLogger = LogManager.getLogManager().getLogger("");
    activeHandler = new DalvikLogHandler();
    activeHandler.setLevel(Level.ALL);
    rootLogger.addHandler(activeHandler);
  }

}
