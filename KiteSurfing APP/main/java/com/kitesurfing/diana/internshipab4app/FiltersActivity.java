package com.kitesurfing.diana.internshipab4app;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kitesurfing.diana.internshipab4app.templates.SpotsCountries.SpotsCountriesResponse;
import com.kitesurfing.diana.internshipab4app.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.kitesurfing.diana.internshipab4app.SpotsListActivity.countryFilter;
import static com.kitesurfing.diana.internshipab4app.SpotsListActivity.filtersChanged;
import static com.kitesurfing.diana.internshipab4app.SpotsListActivity.windProbFilter;

public class FiltersActivity extends AppCompatActivity {

    private EditText countryEditText;
    private EditText windProbEditText;
    private Button filtersApplyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        countryEditText = (EditText) findViewById(R.id.country_edit_text);
        windProbEditText = (EditText) findViewById(R.id.wind_prob_edit_text);
        filtersApplyButton = (Button) findViewById(R.id.filters_apply_button);

        countryEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(Constants.TAG, "clicked on countryEditText");
                getSpotsCountries(v.getContext());
            }
        });

        filtersApplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countryFilter = countryEditText.getText().toString();
                windProbFilter = windProbEditText.getText().toString();
                if (windProbFilter.equals("")) {
                    windProbFilter = "0";
                }
                filtersChanged = true;
                finish();
            }
        });

        setTitle(getTitle() + " - Filters");
        countryEditText.setText(SpotsListActivity.countryFilter);
        windProbEditText.setText(SpotsListActivity.windProbFilter);
    }

    private void getSpotsCountries(final Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://internship-2019.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SpotsService spotsService = retrofit.create(SpotsService.class);

        Call<SpotsCountriesResponse> spotsCountriesResponseCall = spotsService.getSpotsCountries(SpotsListActivity.token);
        spotsCountriesResponseCall.enqueue(new Callback<SpotsCountriesResponse>() {
            @Override
            public void onResponse(Call<SpotsCountriesResponse> call, Response<SpotsCountriesResponse> response) {
                if (response.isSuccessful()) {
                    List<String> countries = response.body().getResult();
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("Pick a country");
                    final String[] countriesArray = new String[countries.size()];
                    countries.toArray(countriesArray);
                    alertDialogBuilder.setItems(countriesArray, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            countryEditText.setText(countriesArray[which]);
                        }
                    });
                    alertDialogBuilder.setNegativeButton("Clear", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            countryEditText.setText("");
                        }
                    });
                    alertDialogBuilder.create().show();
                }
            }

            @Override
            public void onFailure(Call<SpotsCountriesResponse> call, Throwable t) {

            }
        });
    }
}
