package com.kitesurfing.diana.internshipab4app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kitesurfing.diana.internshipab4app.templates.FavoriteSpot.FavoriteSpotBody;
import com.kitesurfing.diana.internshipab4app.templates.FavoriteSpot.FavoriteSpotResponse;
import com.kitesurfing.diana.internshipab4app.templates.SpotDetails.SpotDetailsBody;
import com.kitesurfing.diana.internshipab4app.templates.SpotDetails.SpotDetailsResponse;
import com.kitesurfing.diana.internshipab4app.templates.SpotsList.Spot;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.kitesurfing.diana.internshipab4app.utils.Constants.SPOT_DETAILS;
import static com.kitesurfing.diana.internshipab4app.utils.Constants.TAG;

public class SpotsAdapter extends RecyclerView.Adapter<SpotsAdapter.MyViewHolder> {
    private List<Spot> spotList;
    private RecyclerView recyclerView;
    private LinearLayout linearLayout;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout linearLayout;
        public TextView spotNameTextView;
        public TextView spotCountryTextView;
        public LinearLayout spotLinearLayout;
        public ImageView favoriteImageView;

        public MyViewHolder(LinearLayout v) {
            super(v);
            linearLayout = v;
            spotNameTextView = linearLayout.findViewById(R.id.spot_name_text_view);
            spotCountryTextView = linearLayout.findViewById(R.id.spot_country_text_view);
            spotLinearLayout = linearLayout.findViewById(R.id.spot_layout);
            favoriteImageView = linearLayout.findViewById(R.id.favorite_image_view);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SpotsAdapter(List<Spot> spotList, RecyclerView recyclerView) {
        this.spotList = spotList;
        this.recyclerView = recyclerView;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SpotsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        linearLayout = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.spots_list_element_view, parent, false);


        MyViewHolder vh = new MyViewHolder(linearLayout);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Spot currentSpot = spotList.get(position);
        holder.spotNameTextView.setText(currentSpot.getName());
        holder.spotCountryTextView.setText(currentSpot.getCountry());

        ImageView favoriteImageView = holder.favoriteImageView;
        if (currentSpot.getIsFavorite()) {
            favoriteImageView.setImageResource(R.drawable.staron);
        } else {
            favoriteImageView.setImageResource(R.drawable.staroff);
        }

        LinearLayout spotLinearLayout = holder.spotLinearLayout;
        spotLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: item: " + currentSpot.getName());

                getSpotDetails(currentSpot, v.getContext());
            }
        });
        favoriteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.favorite_image_view) {
                    Log.i(TAG, "imageView onClick: currentSpot favorite status: " + currentSpot.getIsFavorite());
                    toggleSpotFavoriteStatus(currentSpot, (ImageView) v);
                }
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return spotList.size();
    }

    public void toggleSpotFavoriteStatus(final Spot spot, final ImageView favoriteImageView) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://internship-2019.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SpotsService spotsService = retrofit.create(SpotsService.class);

        final FavoriteSpotBody favoriteSpotBody = new FavoriteSpotBody();
        favoriteSpotBody.setSpotId(spot.getId());

        Call<FavoriteSpotResponse> favoriteSpotResponseCall;
        if (spot.getIsFavorite()) {
            favoriteSpotResponseCall = spotsService.removeSpotFromFavorites(SpotsListActivity.token, favoriteSpotBody);
        } else {
            favoriteSpotResponseCall = spotsService.addSpotToFavorites(SpotsListActivity.token, favoriteSpotBody);
        }
        favoriteSpotResponseCall.enqueue(new Callback<FavoriteSpotResponse>() {
            @Override
            public void onResponse(Call<FavoriteSpotResponse> call, Response<FavoriteSpotResponse> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "onResponse: response spotid: " + response.body().getResult() +
                            ", ours: " + spot.getId());
                    if (response.body().getResult().equals(favoriteSpotBody.getSpotId())) {
                        spot.setIsFavorite(!spot.getIsFavorite());
                        favoriteImageView.setImageResource(spot.getIsFavorite() ? R.drawable.staron : R.drawable.staroff);
                        Toast.makeText(favoriteImageView.getContext(), "Toggled spot favorite status.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.w(TAG, "onResponse: response code: " + response.code());
                    Toast.makeText(favoriteImageView.getContext(), "Could not toggle spot favorite status, response code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FavoriteSpotResponse> call, Throwable t) {
                t.printStackTrace();
                Log.w(TAG, "No internet connection");
            }
        });
    }

    public void getSpotDetails(Spot spot, final Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://internship-2019.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SpotsService spotsService = retrofit.create(SpotsService.class);

        SpotDetailsBody spotDetailsBody = new SpotDetailsBody();
        spotDetailsBody.setSpotId(spot.getId());

        Call<SpotDetailsResponse> spotDetailsResponseCall = spotsService.getSpotDetails(SpotsListActivity.token, spotDetailsBody);
        spotDetailsResponseCall.enqueue(new Callback<SpotDetailsResponse>() {
            @Override
            public void onResponse(Call<SpotDetailsResponse> call, Response<SpotDetailsResponse> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "Spot details fetched successfully");
                    launchDetailsActivity(response.body(), context);
                } else {
                    Log.w(TAG, "Spot details fetch failed, response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SpotDetailsResponse> call, Throwable t) {
                t.printStackTrace();
                Log.w(TAG, "Spot details fetch failed, no internet connection");
            }
        });
    }

    public void launchDetailsActivity(SpotDetailsResponse spotDetailsResponse, Context context) {
        Gson gson = new Gson();
        String jsonSpotDetailsResponse = gson.toJson(spotDetailsResponse);
        Log.i(TAG, "json spotDetailsResponse: " + jsonSpotDetailsResponse);

        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(SPOT_DETAILS, jsonSpotDetailsResponse);
        context.startActivity(intent);
    }
}