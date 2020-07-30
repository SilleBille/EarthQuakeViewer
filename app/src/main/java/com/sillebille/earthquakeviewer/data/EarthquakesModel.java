package com.sillebille.earthquakeviewer.data;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import androidx.lifecycle.ViewModel;


/**
 * POJO class to map values from JSON request using GSON library. This class also extend ViewModel
 * to preserve values from destroying when orientation changes
 */
public final class EarthquakesModel extends ViewModel implements Serializable {
    @SerializedName("earthquakes")
    public List<EarthQuake> earthQuakes;

    public static final class EarthQuake implements Serializable, Parcelable {
        @SerializedName("datetime")
        public final String dateTime;
        @SerializedName("depth")
        public final double depth;
        @SerializedName("lng")
        public final double longitude;
        @SerializedName("src")
        public final String source;
        @SerializedName("eqid")
        public final String equivalentId;
        @SerializedName("magnitude")
        public final double magnitude;
        @SerializedName("lat")
        public final double latitude;

        protected EarthQuake(Parcel in) {
            dateTime = in.readString();
            depth = in.readDouble();
            longitude = in.readDouble();
            source = in.readString();
            equivalentId = in.readString();
            magnitude = in.readDouble();
            latitude = in.readDouble();
        }

        public static final Creator<EarthQuake> CREATOR = new Creator<EarthQuake>() {
            @Override
            public EarthQuake createFromParcel(Parcel in) {
                return new EarthQuake(in);
            }

            @Override
            public EarthQuake[] newArray(int size) {
                return new EarthQuake[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            // Define how the data needs to be parceled
            parcel.writeString(dateTime);
            parcel.writeDouble(depth);
            parcel.writeDouble(longitude);
            parcel.writeString(source);
            parcel.writeString(equivalentId);
            parcel.writeDouble(magnitude);
            parcel.writeDouble(latitude);
        }
    }
}
