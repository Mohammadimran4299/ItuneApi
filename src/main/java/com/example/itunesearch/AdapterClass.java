package com.example.itunesearch;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdapterClass extends ArrayAdapter<DataArtist> {
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<DataArtist> mGridData = new ArrayList<DataArtist>();

    public AdapterClass(MainActivity mContext, int layoutResourceId, ArrayList<DataArtist> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }


    public void setGridData(ArrayList<DataArtist> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.titleTextView = (TextView) row.findViewById(R.id.text_songname);
            holder.textViewname = (TextView) row.findViewById(R.id.text_artistn);
            holder.textViewalbum = (TextView) row.findViewById(R.id.text_albumn);
            holder.imageView = (ImageView) row.findViewById(R.id.artist_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        DataArtist item = mGridData.get(position);
        holder.titleTextView.setText(Html.fromHtml(item.getSongName()));
        holder.textViewname.setText(item.getArtName());
        holder.textViewalbum.setText(item.getArtalbum());
        Glide.with(mContext).load(item.getArtImage())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.imageView);


        return row;
    }

    static class ViewHolder {
        TextView titleTextView,textViewname,textViewalbum;
        ImageView imageView;
    }
}

