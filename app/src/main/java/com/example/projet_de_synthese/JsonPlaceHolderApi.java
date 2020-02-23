package com.example.projet_de_synthese;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {

    @GET("{brand}")
    Call<Sentiment> getSentiment(@Path("brand") String brand);
}
