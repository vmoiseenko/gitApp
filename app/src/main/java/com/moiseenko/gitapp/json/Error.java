package com.moiseenko.gitapp.json;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Viktar_Maiseyenka on 16.01.2016.
 */
public class Error {
    @SerializedName("message")
    public String message;
    @SerializedName("documentation_url")
    public String documentation_url;


}
