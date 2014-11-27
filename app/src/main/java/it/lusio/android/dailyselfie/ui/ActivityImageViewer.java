package it.lusio.android.dailyselfie.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import it.lusio.android.dailyselfie.R;


public class ActivityImageViewer extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image_viewer);

        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Uri uri = getIntent().getExtras().getParcelable("URI");

        Picasso
                .with(this)
                .load(uri)
                .fit()
                .centerInside()
                .into(imageView);
    }
}
