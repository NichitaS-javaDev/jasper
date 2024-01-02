package org.example.entity;

import java.sql.Date;

public class Holiday {
    private final String country;
    private final Date date;
    private final String name;

    public Holiday(String country, Date date, String name) {
        this.country = country;
        this.date = date;
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public Date getDate() {
        return date;
    }

    public String getName() {
        return name;
    }
}
