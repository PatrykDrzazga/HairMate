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

public class ServiceAdapter extends ArrayAdapter<Service> {

    private List<Service> data;
    private Context context;

    public ServiceAdapter(Context context, List<Service> data) {
        super(context, R.layout.price_row);
        this.data = data;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.price_row, null);

            holder = new ViewHolder();
            holder.name = (TextView) row.findViewById(R.id.service_name);
            holder.price = (TextView) row.findViewById(R.id.service_price);


            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }


        holder.name.setText(data.get(position).getName());
        holder.price.setText(data.get(position).getPrice() + " zł");


        return row;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    static class ViewHolder {
        TextView name;
        TextView price;
    }
}

