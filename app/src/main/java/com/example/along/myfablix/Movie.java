package com.example.along.myfablix;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Long on 3/2/2017.
 */

public class Movie {
    private int id;
    private URL bannerUrl;
    private int year;
    private URL trailerUrl;
    private String director;
    private String title;

    public Movie(int id, URL bannerUrl, int year, URL trailerUrl, String director, String title) {
        this.id = id;
        this.bannerUrl = bannerUrl;
        this.year = year;
        this.trailerUrl = trailerUrl;
        this.director = director;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public URL getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {

        try {
            this.bannerUrl = new URL(bannerUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public URL getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        try {
            this.trailerUrl = new URL(trailerUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}

