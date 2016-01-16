package com.moiseenko.gitapp.api;

/**
 * Created by Виктор on 07.01.2016.
 */

public class AuthObject {
    public String message;
    public String documentation_url;

    public AuthObject(String message, String documentation_url) {
        this.message = message;
        this.documentation_url = documentation_url;
    }

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
