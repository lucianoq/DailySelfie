package it.lusio.android.dailyselfie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class GridViewAdapter extends BaseAdapter {
    private static final int THUMBNAIL_SIZE = 540;
    private final Context mContext;
    private String mAlbumName;
    private List<Selfie> mItems = new ArrayList<Selfie>();
    private LayoutInflater mInflater;

    public GridViewAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mAlbumName = mContext.getResources().getString(R.string.album);

        loadExistingSelfies();
    }

    private void loadExistingSelfies() {
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                mAlbumName);
        File[] files = storageDir.listFiles();

        for (File f : files) {
            mItems.add(new Selfie(f.lastModified(), Uri.fromFile(f)));
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mItems.get(i).getId();
    }

    public List<Selfie> getList() {
        return mItems;
    }

    public void add(Selfie selfie) {
        mItems.add(selfie);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        ImageView picture;
        TextView name;

        if (v == null) {
            v = mInflater.inflate(R.layout.grid_item, viewGroup, false);
            v.setTag(R.id.picture, v.findViewById(R.id.picture));
            v.setTag(R.id.text, v.findViewById(R.id.text));
        }

        picture = (ImageView) v.getTag(R.id.picture);
        name = (TextView) v.getTag(R.id.text);

        final Selfie item = (Selfie) getItem(i);

        try {
            picture.setImageDrawable(getThumbnail(item.getImageUri()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        name.setText(item.getName());

        v.setOnLongClickListener(new View.OnLongClickListener() {

            private Selfie selfie = item;

            @Override
            public boolean onLongClick(View v) {
                new File(this.selfie.getImageUri().getPath()).delete();
                mItems.remove(selfie);
                GridViewAdapter.this.notifyDataSetChanged();
                return true;
            }
        });

        return v;
    }

//    private Drawable thumbnail(Uri imageUri) {
//        Bitmap bitmap = Bitmap try {
//            bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), imageUri);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        int imageHeight = bd.getBitmap().getHeight();
//        int imageWidth = bd.getBitmap().getWidth();
//
//        Matrix matrix = new Matrix();
//        matrix.postScale(0.1f, 0.1f);
//
//        Bitmap resizedBitmap = Bitmap.createBitmap(bd.getBitmap(), 0, 0, imageWidth, imageHeight, matrix, false);
//
//        return new BitmapDrawable(mContext.getResources(), resizedBitmap);
//    }

    private Drawable getThumbnail(Uri uri) throws IOException {
        InputStream input = mContext.getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
            return null;

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        double ratio = (originalSize > THUMBNAIL_SIZE) ? (originalSize / THUMBNAIL_SIZE) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = mContext.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return new BitmapDrawable(mContext.getResources(), bitmap);
    }

    private int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0) return 1;
        else return k;
    }
}