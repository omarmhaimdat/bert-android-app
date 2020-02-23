package com.example.projet_de_synthese;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    private TextView textPositive;
    private TextView textNeutral;
    private TextView textNegative;

    private BarChart chart;


    private EditText inputField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textPositive = findViewById(R.id.text_positive);
        textNeutral = findViewById(R.id.text_neutral);
        textNegative = findViewById(R.id.text_negative);

        chart = findViewById(R.id.barchart);

        inputField = findViewById(R.id.textField);

        ArrayList<String> labels = new ArrayList<>();
        labels.add("Positive");
        labels.add("Neutral");
        labels.add("Negative");
        String[] xAxisLables = new String[]{"", "Positive","Neutral", "Negative"};
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLables));
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setTextColor(getResources().getColor(R.color.white));
        chart.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
        chart.getLegend().setTextColor(getResources().getColor(R.color.white));


        BarEntry entry1 = new BarEntry((float)1.0, 0);
        BarEntry entry2 = new BarEntry((float)2.0, 0);
        BarEntry entry3 = new BarEntry((float)3.0, 0);
        ArrayList<BarEntry> entry = new ArrayList<>();
        entry.add(entry1);
        entry.add(entry2);
        entry.add(entry3);
        BarDataSet bardataset = new BarDataSet(entry, "Sentiment Distribution");
        int[] colors = new int[]{getResources().getColor(R.color.myGreen),
                getResources().getColor(R.color.myYellow),
                getResources().getColor(R.color.myRed)};

        bardataset.setValueTextColor(getResources().getColor(R.color.white));

        bardataset.setColors(colors);

        chart.animateY(1000);

        BarData data = new BarData(bardataset);

        chart.setData(data);
        chart.notifyDataSetChanged();
        chart.invalidate();

        inputField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //do what you want on the press of 'done'
                    Log.e("inputField", inputField.getText().toString());

                    String inputText = inputField.getText().toString();
                    OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                            .connectTimeout(100, TimeUnit.SECONDS)
                            .readTimeout(100, TimeUnit.SECONDS)
                            .writeTimeout(100, TimeUnit.SECONDS)
                            .build();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://ab0ba3d8.ngrok.io/")
                            .client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

                    Call<Sentiment> call = jsonPlaceHolderApi.getSentiment(inputText);

                    call.enqueue(new Callback<Sentiment>() {
                        @Override
                        public void onResponse(Call<Sentiment> call, Response<Sentiment> response) {

                            if(!response.isSuccessful()){
                                textPositive.setText("Code: " + response.code());
                                return;
                            }

                            Sentiment sentiment = response.body();

                            int total = sentiment.getNumber_of_tweets();
                            int pos = sentiment.getPositive();
                            int neu = sentiment.getNeutral();
                            int neg = sentiment.getNegative();

                            float posPer = (((float) pos/total)*100);
                            float neuPer = (((float) neu/total)*100);
                            float negPer = (((float) neg/total)*100);


                            textNegative.setText(String.format("%.01f", negPer) + "%");
                            textNeutral.setText(String.format("%.01f", neuPer) + "%");
                            textPositive.setText(String.format("%.01f", posPer) + "%");

                            BarEntry entry1 = new BarEntry((float)1.0, pos);
                            BarEntry entry2 = new BarEntry((float)2.0, neu);
                            BarEntry entry3 = new BarEntry((float)3.0, neg);
                            ArrayList<BarEntry> entry = new ArrayList<>();
                            entry.add(entry1);
                            entry.add(entry2);
                            entry.add(entry3);
                            BarDataSet bardataset = new BarDataSet(entry, "Sentiment Distribution");
                            int[] colors = new int[]{getResources().getColor(R.color.myGreen),
                                    getResources().getColor(R.color.myYellow),
                                    getResources().getColor(R.color.myRed)};
                            bardataset.setValueTextColor(getResources().getColor(R.color.white));
                            bardataset.setColors(colors);
                            chart.animateY(3000);

                            BarData data = new BarData(bardataset);

                            chart.setData(data);
                            chart.notifyDataSetChanged();
                            chart.invalidate();


                        }

                        @Override
                        public void onFailure(Call<Sentiment> call, Throwable t) {
                            textPositive.setText(t.getMessage());
                        }
                    });
                    return true;
                }
                return false;
            }
        });



    }
}
