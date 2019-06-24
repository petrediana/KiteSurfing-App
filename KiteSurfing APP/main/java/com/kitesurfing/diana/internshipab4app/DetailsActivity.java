package com.kitesurfing.diana.internshipab4app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kitesurfing.diana.internshipab4app.templates.SpotDetails.SpotDetailsResponse;
import com.kitesurfing.diana.internshipab4app.templates.SpotsList.Spot;
import com.kitesurfing.diana.internshipab4app.utils.Constants;

public class DetailsActivity extends AppCompatActivity {

    private TextView countryTextView;
    private TextView latitudeTextView;
    private TextView longitudeTextView;
    private TextView windProbTextView;
    private TextView whenToGoTextView;
    private Menu menu;
    private boolean favoriteStatus;
    private SpotDetailsResponse spotDetailsResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        countryTextView = (TextView) findViewById(R.id.country_text_view);
        latitudeTextView = (TextView) findViewById(R.id.latitude_text_view);
        longitudeTextView = (TextView) findViewById(R.id.longitude_text_view);
        windProbTextView = (TextView) findViewById(R.id.wind_prob_text_view);
        whenToGoTextView = (TextView) findViewById(R.id.when_to_go_text_view);

        Intent parentIntent = getIntent();
        String jsonSpotDetails = parentIntent.getStringExtra(Constants.SPOT_DETAILS);
        Gson gson = new Gson();
        spotDetailsResponse = gson.fromJson(jsonSpotDetails, SpotDetailsResponse.class);

        countryTextView.setText(spotDetailsResponse.getResult().getCountry());
        latitudeTextView.setText(spotDetailsResponse.getResult().getLatitude().toString());
        longitudeTextView.setText(spotDetailsResponse.getResult().getLatitude().toString());
        windProbTextView.setText(spotDetailsResponse.getResult().getWindProbability().toString() + "%");
        whenToGoTextView.setText(spotDetailsResponse.getResult().getWhenToGo());
        favoriteStatus = spotDetailsResponse.getResult().getIsFavorite();
        setTitle(getTitle() + " - " + spotDetailsResponse.getResult().getName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_details_menu, menu);

        menu.getItem(0).setIcon(favoriteStatus ? R.drawable.staron : R.drawable.staroff);

        return super.onCreateOptionsMenu(menu);
    }
}
