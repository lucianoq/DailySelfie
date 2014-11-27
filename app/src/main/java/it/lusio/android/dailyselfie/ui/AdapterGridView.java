package it.lusio.android.dailyselfie.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import it.lusio.android.dailyselfie.Constants;
import it.lusio.android.dailyselfie.R;

public class AdapterGridView extends BaseAdapter {
    private final Context mContext;
    private List<Selfie> mItems = new ArrayList<Selfie>();
    private LayoutInflater mInflater;

    public AdapterGridView(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);

        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Constants.ALBUM_NAME);
        storageDir.mkdirs();
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

    public void add(Selfie selfie) {
        Log.i(Constants.TAG, "Adding new Selfie.");
        mItems.add(selfie);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        SquareImageView picture;
        TextView name;

        if (v == null) {
            v = mInflater.inflate(R.layout.grid_item, viewGroup, false);
            v.setTag(R.id.picture, v.findViewById(R.id.picture));
            v.setTag(R.id.text, v.findViewById(R.id.text));
        }

        picture = (SquareImageView) v.getTag(R.id.picture);
        name = (TextView) v.getTag(R.id.text);

        final Selfie item = mItems.get(i);

        Picasso
                .with(mContext)
                .load(item.getImageUri())
                .fit()
                .centerInside()
//                .centerCrop()
                .into(picture);

        name.setText(item.getName());

        v.setOnLongClickListener(new View.OnLongClickListener() {
            private Selfie selfie = item;

            @Override
            public boolean onLongClick(View v) {
                Log.i(Constants.TAG, "Deleting Selfie.");
                new AlertDialog.Builder(mContext)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(mContext.getString(R.string.dialog_delete_single_title))
                        .setMessage(mContext.getString(R.string.dialog_delete_single_message))
                        .setPositiveButton(mContext.getString(R.string.positive_answer), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new File(selfie.getImageUri().getPath()).delete();
                                mItems.remove(selfie);
                                AdapterGridView.this.notifyDataSetChanged();
                            }

                        })
                        .setNegativeButton(mContext.getString(R.string.negative_answer), null)
                        .show();
                return true;
            }
        });

        v.setOnClickListener(new View.OnClickListener() {
            private Uri uri = item.getImageUri();

            @Override
            public void onClick(View v) {
                Log.i(Constants.TAG, "Opening ImageViewer.");
                ((ActivityMain) mContext).showViewer(uri);
            }
        });

        return v;
    }

    public void deleteAll() {
        for (Selfie s : mItems) {
            new File(s.getImageUri().getPath()).delete();
        }
        mItems.clear();
        this.notifyDataSetChanged();
    }
}