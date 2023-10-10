package com.example.gcc;

import java.io.Serializable;

public class Account implements Serializable {
   public String password;
    public String username;

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
       this.password = password;
    }
}
