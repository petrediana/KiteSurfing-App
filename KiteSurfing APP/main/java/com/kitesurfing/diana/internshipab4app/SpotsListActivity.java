package com.kitesurfing.diana.internshipab4app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kitesurfing.diana.internshipab4app.templates.SpotsList.Spot;
import com.kitesurfing.diana.internshipab4app.templates.SpotsList.SpotsListBody;
import com.kitesurfing.diana.internshipab4app.templates.SpotsList.SpotsListResponse;
import com.kitesurfing.diana.internshipab4app.utils.Constants;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.kitesurfing.diana.internshipab4app.utils.Constants.TAG;

public class SpotsListActivity extends AppCompatActivity {

    private RecyclerView spotsRecyclerView;
    private RecyclerView.Adapter spotsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    static String token;
    private List<Spot> spotList;
    static String countryFilter = "";
    static String windProbFilter = "0";
    static boolean filtersChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spots_list);

        Intent intent = getIntent();
        String emailText = intent.getStringExtra(Constants.INTENT_EMAIL);
        token = intent.getStringExtra(Constants.TOKEN);
        setTitle(getTitle().toString() + " - " + emailText);

        spotsRecyclerView = (RecyclerView) findViewById(R.id.spots_recycler_view);
        spotsRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        spotsRecyclerView.setLayoutManager(layoutManager);

        getSpotsList(token);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (filtersChanged) {
            filtersChanged = false;
            getSpotsList(token);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_spots_list_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_filters) {
            Intent intent = new Intent(this, FiltersActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void getSpotsList(String token) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://internship-2019.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SpotsService spotsService = retrofit.create(SpotsService.class);

        SpotsListBody spotsListBody = new SpotsListBody();
        spotsListBody.setCountry(countryFilter);
        spotsListBody.setWindProbability(Integer.parseInt(windProbFilter));
        Log.i(TAG, "spots list body: " + new Gson().toJson(spotsListBody));

        Call<SpotsListResponse> spotsListResponseCall = spotsService.getSpotsList(token, spotsListBody);
        spotsListResponseCall.enqueue(new Callback<SpotsListResponse>() {
            @Override
            public void onResponse(Call<SpotsListResponse> call, Response<SpotsListResponse> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.i(TAG, "onResponse: spots list retrieved");
                        spotList = response.body().getResult();

                        createRecyclerView();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.w(TAG, "onResponse: spots list failed, code: " + response.code());
                    Toast.makeText(SpotsListActivity.this, "Spots list retrieval failed, code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SpotsListResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(SpotsListActivity.this, "No Internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createRecyclerView() {
        spotsAdapter = new SpotsAdapter(spotList, spotsRecyclerView);
        Log.i(TAG, "onCreate: spots adapter string is " + spotList);
        spotsRecyclerView.setAdapter(spotsAdapter);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(spotsRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        spotsRecyclerView.addItemDecoration(mDividerItemDecoration);
    }
}
