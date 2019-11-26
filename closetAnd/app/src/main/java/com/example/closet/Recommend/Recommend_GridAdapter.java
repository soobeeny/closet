package com.example.closet.Recommend;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.example.closet.Clothes.UrlToBitmap;
import com.example.closet.R;

import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

class Recommend_GridAdapter extends BaseAdapter {

    Context context;
    private LayoutInflater inflater;
    private int layout;

    ArrayList<URL> photoUrls;
    ArrayList<Bitmap> photoBitmap = new ArrayList<>();
    UrlToBitmap urlToBitmap;
    ArrayList<String> arrayTextList;

    public Recommend_GridAdapter(Context context, ArrayList<URL> photoUrls, ArrayList<String> arrayTextList) {

        this.context = context;
        inflater = LayoutInflater.from(context);

        this.photoUrls = photoUrls;
        this.arrayTextList = arrayTextList;

        urlToBitmap = new UrlToBitmap(photoUrls);
        urlToBitmap.execute();
        try {
            photoBitmap = urlToBitmap.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getCount() {
        return photoUrls.size();
    }

    public Object getItem(int i) {
        return photoUrls.get(i);
    }

    public long getItemId(int i) {
        return i;
    }

    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (view == null) {
            viewHolder = new ViewHolder();

            view = inflater.inflate(R.layout.recommend_griditem, viewGroup, false);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.recommend_iv);
            viewHolder.button = (Button) view.findViewById(R.id.like_button);

            view.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) view.getTag();

        viewHolder.imageView.setImageBitmap(photoBitmap.get(i));

        /* viewHolder.button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                viewHolder.button.setActivated(true);
            }
        });
         */

        return view;
    }

    private class ViewHolder {
        private ImageView imageView;
        private Button button;
    }

}