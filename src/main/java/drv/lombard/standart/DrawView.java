package drv.lombard.standart;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import drv.lombard.standart.connection.BitmapAvailableListener;
import drv.lombard.standart.connection.Picture;

public class DrawView extends View {

  private volatile Bitmap currentBitmap = Bitmap.createBitmap(Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888));
  private Paint paint = new Paint();


  public DrawView(Context context, Picture picture) {
    super(context);

    picture.addListener(new BitmapAvailableListener() {
      @Override
      public void onNewBitmapAvailable(Bitmap bitmap) {
        currentBitmap = bitmap;
        postInvalidate();
      }
    });

  }

  @Override
  public void onDraw(Canvas canvas) {

    int canvasHeight = canvas.getHeight();
    int canvasWidth = canvas.getWidth();

    int scaledHeight, scaledWidth;


    if(canvasHeight>=canvasWidth){
      scaledWidth = canvasWidth;
      scaledHeight = (int)(currentBitmap.getHeight() * ((float)scaledWidth/currentBitmap.getWidth()));
    } else {
      scaledHeight = canvasHeight;
      scaledWidth = (int)(currentBitmap.getWidth() * ((float)scaledHeight/currentBitmap.getHeight()));
    }

    if(scaledHeight>canvasHeight){
      scaledWidth = (int)(scaledWidth * ((float)canvasHeight/scaledHeight));
      scaledHeight = canvasHeight;
    }

    if(scaledWidth>canvasWidth){
      scaledHeight = (int)(scaledHeight * ((float)canvasWidth/scaledWidth));
      scaledWidth = canvasWidth;
    }


    Bitmap scaledBitmap = Bitmap.createScaledBitmap(currentBitmap, scaledWidth, scaledHeight, false);

    int centreX = Math.max((canvasWidth  - scaledBitmap.getWidth()) / 2, 0);
    int centreY = Math.max((canvasHeight - scaledBitmap.getHeight()) / 2, 0);
    canvas.drawBitmap(scaledBitmap, centreX, centreY, paint);
  }

}
