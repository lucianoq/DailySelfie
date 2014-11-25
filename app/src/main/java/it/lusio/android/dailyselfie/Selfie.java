package it.lusio.android.dailyselfie;

import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lusio on 24/11/14.
 */
public class Selfie {
    private final long mId;
    private final String mName;
    private final long mTime;
    private final Uri mImageUri;

    public Selfie(long time, Uri imageUri) {
        this.mId = time;
        this.mTime = time;
        this.mName = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date(time));
        this.mImageUri = imageUri;
    }

    public String getName() {
        return mName;
    }

    public long getTime() {
        return mTime;
    }

    public Uri getImageUri() {
        return mImageUri;
    }

    public long getId() {
        return mId;
    }
}
