package it.lusio.android.dailyselfie;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;


public class ImageViewer extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image_viewer);

        final ImageView imageView = (ImageView) findViewById(R.id.imageView);

        Uri uri = getIntent().getExtras().getParcelable("URI");
        Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());
        imageView.setImageBitmap(bitmap);
    }
}
