package it.lusio.android.dailyselfie;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends Activity {

    private static final int TAKE_SELFIE_REQUEST_CODE = 1;
    private static final String LAST_PHOTO_PATH = "mLastPhotoPath";
    private static final String LAST_PHOTO_DATE = "mLastPhotoDate";
    private GridViewAdapter mGridViewAdapter;
    private String mLastPhotoPath = "";
    private long mLastSelfieTime = new Date().getTime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, getString(R.string.no_external_storage), Toast.LENGTH_SHORT).show();
            finish();
        }

        if (savedInstanceState != null) {
            mLastPhotoPath = savedInstanceState.getString(LAST_PHOTO_PATH);
            mLastSelfieTime = savedInstanceState.getLong(LAST_PHOTO_DATE);
        }

        new AlarmService(getApplicationContext()).startAlarm();

        setContentView(R.layout.activity_main);

        GridView gridView = (GridView) findViewById(R.id.gridview);
        if (gridView != null) {
            mGridViewAdapter = new GridViewAdapter(this);
            gridView.setAdapter(mGridViewAdapter);
        }

        mGridViewAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case (R.id.action_camera):
                Log.i(Constants.TAG, "Action Camera Clicked");
                takeNewSelfie();
                return true;
            case (R.id.action_delete_all):
                Log.i(Constants.TAG, "Delete all Clicked");
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(getString(R.string.dialog_delete_all_title))
                        .setMessage(getString(R.string.dialog_delete_all_text))
                        .setPositiveButton(getString(R.string.positive_answer), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mGridViewAdapter.deleteAll();
                            }

                        })
                        .setNegativeButton(getString(R.string.negative_answer), null)
                        .show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void takeNewSelfie() {
        mLastSelfieTime = new Date().getTime();

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

        String imageFileName = "s_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(mLastSelfieTime);
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Constants.ALBUM_NAME);
        storageDir.mkdirs();
        File photoFile = new File(storageDir.getAbsolutePath() + File.separator + imageFileName + ".jpg");

        mLastPhotoPath = photoFile.getAbsolutePath();
        // Continue only if the File was successfully created
        if (photoFile != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            startActivityForResult(intent, TAKE_SELFIE_REQUEST_CODE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == TAKE_SELFIE_REQUEST_CODE) {
            Selfie selfie = new Selfie(mLastSelfieTime, Uri.fromFile(new File(mLastPhotoPath)));
            mGridViewAdapter.add(selfie);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putString(LAST_PHOTO_PATH, mLastPhotoPath);
        savedInstanceState.putLong(LAST_PHOTO_DATE, mLastSelfieTime);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public void showViewer(Uri uri) {
        Intent intent = new Intent(this, ImageViewer.class);
        Bundle b = new Bundle();
        b.putParcelable("URI", uri);
        intent.putExtras(b);
        startActivity(intent);
    }
}
