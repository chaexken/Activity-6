package com.example.act6a;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.telecom.Call;
import android.widget.TextView;
import android.widget.Toast;

import com.example.act6a.api.ApiUtilities;
import com.example.act6a.api.CountryData;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView text_total_confirm, text_total_active, text_total_recovered, text_total_death, text_total_tests;
    TextView text_today_confirm, text_today_recovered, text_today_death;
    TextView text_date;
    PieChart pieChart;

    private List<CountryData> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<>();

        initial();

        ApiUtilities.getApiInterface().getCountryData()
                .enqueue(new Callback<List<CountryData>>() {
                    @Override
                    public void onResponse(retrofit2.Call<List<CountryData>> call, Response<List<CountryData>> response) {
                        list.addAll(response.body());

                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getCountry().equals("Philippines")) {
                                int confirm = Integer.parseInt(list.get(i).getCases());
                                int active = Integer.parseInt(list.get(i).getActive());
                                int recovered = Integer.parseInt(list.get(i).getRecovered());
                                int death = Integer.parseInt(list.get(i).getDeaths());
                                int tests = Integer.parseInt(list.get(i).getTests());


                                text_total_confirm.setText(NumberFormat.getInstance().format(confirm));
                                text_total_active.setText(NumberFormat.getInstance().format(active));
                                text_total_recovered.setText(NumberFormat.getInstance().format(recovered));
                                text_total_death.setText(NumberFormat.getInstance().format(death));
                                text_total_tests.setText(NumberFormat.getInstance().format(tests));

                                text_today_death.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayDeaths())));
                                text_today_confirm.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayCases())));
                                text_today_recovered.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayRecovered())));
                                text_total_tests.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTests())));

                                setText(list.get(i).getUpdated());

                                pieChart.addPieSlice(new PieModel("Confirm", confirm, Color.parseColor("#0D731E")));
                                pieChart.addPieSlice(new PieModel("Active", active, Color.parseColor("#AE0CIA")));
                                pieChart.addPieSlice(new PieModel("Recovered", recovered, Color.parseColor("#FF5722")));
                                pieChart.addPieSlice(new PieModel("Death", death, Color.parseColor("#673AB7")));
                            }
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<List<CountryData>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setText(String updated) {

        DateFormat format = new SimpleDateFormat("MMM dd. yyyy");

        long milliseconds = Long.parseLong(updated);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);

        text_date.setText("Updated at"+format.format(calendar.getTime()));
    }

    private void  initial(){
        text_total_confirm = findViewById(R.id.txtview_totalconfirmed);
        text_total_recovered = findViewById(R.id.txtview_totalrecovered);
        text_total_active = findViewById(R.id.txtview_totalactive);
        text_total_death = findViewById(R.id.txtview_totaldeath);
        text_total_tests = findViewById(R.id.txtview_totaltest);
        text_today_confirm = findViewById(R.id.txtview_todayconfirmed);
        text_today_recovered = findViewById(R.id.txtview_todayrecovered);
        text_today_death = findViewById(R.id.txtview_todaydeath);
        pieChart = findViewById(R.id.piechart);
        text_date = findViewById(R.id.txtview_date);
    }
}

