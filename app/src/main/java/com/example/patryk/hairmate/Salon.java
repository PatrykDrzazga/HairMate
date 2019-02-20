package com.example.patryk.hairmate;

import android.os.Parcel;
import android.os.Parcelable;

public class Salon implements Parcelable {
    private String id;
    private String photo;
    private String name;
    private String city;
    private String address;
    private String email;
    private String phone_number;
    private String description;

    public Salon(String id, String photo, String name, String city, String address, String email, String phone_number, String describtion){
        this.photo = photo;
        this.name = name;
        this.city = city;
        this.address = address;
        this.email = email;
        this.phone_number = phone_number;
        this.description = describtion;
        this.id = id;

    }

    public Salon(Parcel in) {
        id = in.readString();
        photo = in.readString();
        name = in.readString();
        address = in.readString();
        city = in.readString();
        phone_number = in.readString();
        description = in.readString();
        email = in.readString();
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getDescription() {
        return description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoto() {

        return photo;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(photo);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(city);
        dest.writeString(phone_number);
        dest.writeString(description);
        dest.writeString(email);

    }

    public static final Parcelable.Creator<Salon> CREATOR = new Parcelable.Creator<Salon>()
    {
        public Salon createFromParcel(Parcel in)
        {
            return new Salon(in);
        }
        public Salon[] newArray(int size)
        {
            return new Salon[size];
        }
    };
}