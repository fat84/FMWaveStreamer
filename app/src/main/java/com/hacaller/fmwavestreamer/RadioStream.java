package com.hacaller.fmwavestreamer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Herbert Caller on 21/01/2018.
 */

public class RadioStream {

    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("url_stream")
    @Expose
    private String urlStream;
    @SerializedName("radio")
    @Expose
    private String radio;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUrlStream() {
        return urlStream;
    }

    public void setUrlStream(String urlStream) {
        this.urlStream = urlStream;
    }

    public String getRadio() {
        return radio;
    }

    public void setRadio(String radio) {
        this.radio = radio;
    }
}
