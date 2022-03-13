package com.example.dnatask.joboffer.job.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Employer {

    @Id
    private long id;

    private String name;

    public Employer(long id, String name) {
        this.id = id;
        this.name = name;
    }

    protected Employer() {

    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Employer employer = (Employer) o;
        return id == employer.id && name.equals(employer.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
