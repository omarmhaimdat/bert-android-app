package com.example.projet_de_synthese;

public class tweet {

    private String text;
    private String created_at;
    private String sentiment;
    private Float confidence;
    private String lang;

    public String getText() {
        return text;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getSentiment() {
        return sentiment;
    }

    public Float getConfidence() {
        return confidence;
    }

    public String getLang() {
        return lang;
    }
}
