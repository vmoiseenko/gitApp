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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDocumentation_url() {
        return documentation_url;
    }

    public void setDocumentation_url(String documentation_url) {
        this.documentation_url = documentation_url;
    }
}
