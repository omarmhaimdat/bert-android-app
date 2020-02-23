package com.example.projet_de_synthese;

import java.util.List;

public class Sentiment {
    private int number_of_tweets;

    private int neutral;

    private int positive;

    private int negative;

    private List<tweet> tweets;

    public int getNumber_of_tweets() {
        return number_of_tweets;
    }

    public int getNeutral() {
        return neutral;
    }

    public int getPositive() {
        return positive;
    }

    public int getNegative() {
        return negative;
    }

    public List<tweet> getTweets() {
        return tweets;
    }
}
