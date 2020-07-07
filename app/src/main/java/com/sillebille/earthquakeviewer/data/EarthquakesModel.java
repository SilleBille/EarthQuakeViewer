package com.sillebille.earthquakeviewer.data;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

public final class EarthquakesModel extends ViewModel implements Serializable {
    @SerializedName("earthquakes")
    public List<EarthQuake> earthQuakes;

    @NonNull
    public String toString() {
        String returnValue = "";
        if(earthQuakes != null) {
            for (EarthQuake e : earthQuakes) {
                Log.d("EARTHQUAKESMODEL: ", e.equivalentId);
                returnValue += e.equivalentId + "   ";
            }
            return returnValue;
        }
        return "Null value";
    }

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

        public EarthQuake(String dateTime, long depth, double longitude, String source, String equivalentId, double magnitude, double latitude){
            this.dateTime = dateTime;
            this.depth = depth;
            this.longitude = longitude;
            this.source = source;
            this.equivalentId = equivalentId;
            this.magnitude = magnitude;
            this.latitude = latitude;
        }

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
            Bundle b = new Bundle();
            b.putParcelable("data", this);
            parcel.writeBundle(b);
        }
    }
}
