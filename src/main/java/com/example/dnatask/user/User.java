package com.example.dnatask.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
class User {

    @Id
    @GeneratedValue
    private long id;

    // TODO name duplication validation?
    private String name;

    User(String name) {
        this.name = name;
    }

    protected User() {
        // JPA
    }

    long getId() {
        return id;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }
}
