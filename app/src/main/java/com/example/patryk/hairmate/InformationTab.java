package com.example.patryk.hairmate;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class InformationTab extends Fragment{
    private static final String TAG = "InformationTab";
    public static String id;
    ImageView logo;
    TextView name, address, phone_number,email, describtion;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1_fragment,container,false);

        Bundle bundle = getActivity().getIntent().getExtras();
        Salon salon = bundle.getParcelable("data");

        id = salon.getId();


        logo = (ImageView) view.findViewById(R.id.photo_tab);
        Picasso.get()
                .load(salon.getPhoto())
                .into(logo);

        name = view.findViewById(R.id.tab_salon_name);
        name.setText(salon.getName());

        address = view.findViewById(R.id.tab_salon_address);
        address.setText(salon.getAddress() + ", " + salon.getCity());

        phone_number = view.findViewById(R.id.tab_salon_tel);
        phone_number.setText(salon.getPhone_number());

        email = view.findViewById(R.id.tab_salon_email);
        email.setText(salon.getEmail());

        describtion = view.findViewById(R.id.tab_salon_opis);
        describtion.setText(salon.getDescription());


        return view;
    }

}
