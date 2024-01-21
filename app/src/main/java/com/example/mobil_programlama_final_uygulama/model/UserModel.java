package com.example.mobil_programlama_final_uygulama.model;

public class UserModel {
    String email;
    String lastName;
    String name;

    public UserModel() {
    }

    public UserModel(String email, String lastName, String name) {
        this.email = email;
        this.lastName = lastName;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
