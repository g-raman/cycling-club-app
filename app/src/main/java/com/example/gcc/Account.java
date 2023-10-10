package com.example.gcc;

import java.io.Serializable;

public class Account implements Serializable {
    public String password;
    public String role;
    public String username;

    public String getPassword() {
        return this.password;
    }

    public String getRole() { return this.role; }

    public void setPassword(String password) {
       this.password = password;
    }
}
