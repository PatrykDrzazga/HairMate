package com.example.patryk.hairmate;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class OpinionAdapter extends ArrayAdapter<Opinion> {

    private List<Opinion> data;
    private Context context;

    public OpinionAdapter(Context context, List<Opinion> data) {
        super(context, R.layout.opinion_row);
        this.data=data;
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if(row == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.opinion_row, null);

            holder = new ViewHolder();
            holder.photo = (ImageView) row.findViewById(R.id.opinion_image);
            holder.title = (TextView) row.findViewById(R.id.opinion_item_title);
            holder.content = (TextView) row.findViewById(R.id.opinion_item_content);
            holder.author = (TextView) row.findViewById(R.id.opinion_item_author);
            holder.date = (TextView) row.findViewById(R.id.opinion_item_date);

            row.setTag(holder);
        } else{
            holder = (ViewHolder) row.getTag();
        }


        holder.title.setText(data.get(position).getTitle());
        holder.content.setText(data.get(position).getContent());
        holder.author.setText("Autor: " + data.get(position).getAuthor());
        holder.date.setText("Data: " + data.get(position).getDate());


        return row;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    static class ViewHolder{
        ImageView photo;
        TextView title;
        TextView content;
        TextView author;
        TextView date;
    }
}
