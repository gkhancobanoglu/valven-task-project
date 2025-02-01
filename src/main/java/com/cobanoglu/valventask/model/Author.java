package com.cobanoglu.valventask.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Author {

    private String name;
    private String email;

    // Getter ve Setter metotları
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
