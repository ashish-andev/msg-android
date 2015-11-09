package io.msgapp.android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("username")
    @Expose
    public long id;

    @SerializedName("username")
    @Expose
    public String username;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("password")
    @Expose
    public String password;

    @SerializedName("email")
    @Expose
    public String email;

    public User(String name, String username, String email, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }
}