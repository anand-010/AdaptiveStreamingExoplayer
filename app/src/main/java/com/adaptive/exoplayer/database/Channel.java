package com.adaptive.exoplayer.database;

public class Channel {
    private String image;
    private String contry;
    private String language;
    private String category;
    private String name;
    private String url;

    public String getImage() {
        return image;
    }

    public String getContry() {
        return contry;
    }

    public String getLanguage() {
        return language;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public Channel(String image, String contry, String language, String category, String name, String url) {
        this.image = image;
        this.contry = contry;
        this.language = language;
        this.category = category;
        this.name = name;
        this.url = url;
    }
}



