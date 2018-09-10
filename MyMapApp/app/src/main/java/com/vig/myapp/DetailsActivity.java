package com.vig.myapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.appolica.flubber.Flubber;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vig.myapp.custom.TextSansRegular;
import com.vig.myapp.models.UserData;
import com.vig.myapp.util.Utils;

import java.lang.reflect.Type;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.vig.myapp.util.Utils.roundTo6decimals;
import static com.vig.myapp.util.Utils.validateCoOrdinates;

public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    UserData currentUserData = null;
    public static String TAG = DetailsActivity.class.getSimpleName();
    SupportMapFragment mapFragment;
    Context context;

    @Bind(R.id.layout_details)
    LinearLayout layoutDetails;
    @Bind(R.id.txt_userid)
    TextSansRegular txtUserid;
    @Bind(R.id.txt_username)
    TextSansRegular txtUsername;
    @Bind(R.id.txt_useremail)
    TextSansRegular txtUseremail;
    @Bind(R.id.txt_lat)
    TextSansRegular txtLat;
    @Bind(R.id.txt_long)
    TextSansRegular txtLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        context = DetailsActivity.this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("User Details");
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_view);
        mapFragment.getMapAsync(this);
        String userData = getIntent().getStringExtra("user_data");
        if (userData != null) {
            Type type = new TypeToken<UserData>() {
            }.getType();
            Utils.largeLog(TAG, userData);
            currentUserData = new Gson().fromJson(userData, type);
            txtUsername.setText(currentUserData.getName());
            txtUseremail.setText(currentUserData.getEmail());
            if (currentUserData.get_id().toString().length() > 8) {
                txtUserid.setText("#" + currentUserData.get_id().substring(0, 8).toUpperCase());
            } else {
                txtUserid.setText("#" + currentUserData.get_id().toUpperCase());
            }
            txtLat.setText(currentUserData.getLocation().getLatitude());
            txtLong.setText(currentUserData.getLocation().getLongitude());
            layoutDetails.setVisibility(View.VISIBLE);
            Flubber.with()
                    .animation(Flubber.AnimationPreset.SLIDE_DOWN)
                    .repeatCount(0)
                    .duration(2000)
                    .createFor(layoutDetails)
                    .start();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                /*Intent intent = new Intent(DetailsActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();*/
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (currentUserData != null) {
            double lat = Double.parseDouble(currentUserData.getLocation().getLatitude());
            double lon = Double.parseDouble(currentUserData.getLocation().getLongitude());

            lat = roundTo6decimals(lat);
            lon = roundTo6decimals(lon);
            if (validateCoOrdinates(lat, lon)) {
                LatLng userLocation = new LatLng(lat, lon);
                googleMap.addMarker(new MarkerOptions().position(userLocation)
                        .title(currentUserData.getName()));
//                CameraUpdate zoom = CameraUpdateFactory.zoomTo(18);
//                googleMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16));
            } else {
                Utils.showErrorToast(context, "Invalid Location Co-Ordinates!");
            }
        }
    }

}
