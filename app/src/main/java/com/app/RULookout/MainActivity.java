package com.app.RULookout;

import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Address;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    GoogleMap mGoogleMap;
    GoogleApiClient mGoogleApiClient;
    ArrayList<String> timeBank = new ArrayList<String>();
    int timeBankCounter = 0;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("lifecycle", "onCreate Called");
        super.onCreate(savedInstanceState);
        if (googlePlayServicesAvailabale()) {
            //Toast.makeText(this, "Google Play Services Enabled", Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_main);
            initMap();
        }


    }
    /*@Override
    protected void onStart(){
        super.onStart();
        Log.i("lifecycle", "onStart Called");

    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i("lifecycle", "onResume Called");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.i("lifecycle", "onPause Called");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i("lifecycle", "onStop Called");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("lifecycle", "onDestroy Called");
    }*/


    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    public boolean googlePlayServicesAvailabale() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Can't connect to play servies", Toast.LENGTH_LONG).show();
        }
        return false;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        if (mGoogleMap != null) {
            mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View v = getLayoutInflater().inflate(R.layout.info_window, null);

                    TextView tvDescription = (TextView) v.findViewById(R.id.tv_description);
                    TextView tvDate = (TextView) v.findViewById(R.id.tv_date);
                    TextView tvTime = (TextView) v.findViewById(R.id.tv_time);
                    TextView tvSnippet = (TextView) v.findViewById(R.id.tv_snippet);

                    tvDescription.setText(marker.getTitle());
                    tvDate.setText(parseForMarkerDate(marker.getSnippet()));
                    tvTime.setText(parseForMarkerTime(marker.getSnippet()));
                    timeBank.add(timeBankCounter, parseForMarkerTime(marker.getSnippet()));
                    timeBankCounter++;
                    tvSnippet.setText(parseForMarkerDescription(marker.getSnippet()));

                    return v;
                }
            });
        }

        goToLocationZoom(40.499495, -74.448151, 15);
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        mGoogleMap.setMyLocationEnabled(true);
      /*  mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
        */
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mGoogleMap.setMyLocationEnabled(true);
        }

        GetCrimes client = new Retrofit.Builder()
                .baseUrl("http://198.199.65.123:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GetCrimes.class);


        Call<List<CrimeData>> call = client.fetchCrimes("http://xxx.xxx.xx.xxx:3000/api/v1/crimes");

        call.enqueue(new Callback<List<CrimeData>>() {
            @Override
            public void onResponse(Call<List<CrimeData>> call, Response<List<CrimeData>> response) {
                //Toast.makeText(MainActivity.this,"Crimes Loaded!", Toast.LENGTH_SHORT).show();

                for (CrimeData crimeData : response.body()) {
                    setMarker(
                            crimeData.getTitle(),
                            crimeData.getLatitude(),
                            crimeData.getLongitude(),
                            crimeData.getDescription()
                    );
                }
            }
            @Override
            public void onFailure(Call<List<CrimeData>> call, Throwable t) {
                Log.d("TEST", "Retrofit somehow failed.", t);
                Toast.makeText(MainActivity.this,"Error Loading Crimes", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void goToLocation(double lat, double lng) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLng(ll);
        mGoogleMap.moveCamera(update);
    }

    private void goToLocationZoom(double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mGoogleMap.moveCamera(update);
    }

    public void geoLocate(View view) throws IOException {
        EditText et = (EditText) findViewById(R.id.editText);
        String location = et.getText().toString();
        if (location.equals("")
                || location.equals(null)
                || location.equals("!")
                || location.equals(34)
                || location.equals("#")
                || location.equals(".")
                || location.equals("$")
                || location.equals("%")
                || location.equals("^")
                || location.equals("&")
                || location.equals("*")
                || location.equals("(")
                || location.equals(")")
                || location.equals(",")
                || location.equals(";")
                || location.equals("/")
                || location.equals("{")
                || location.equals("}")
                || location.equals("[")
                || location.equals("]")
                || location.equals("=")
                || location.equals("-")
                || location.equals("+")
                || location.equals("`")
                || location.equals("~")
                || location.equals(" ")
                ){
            goToLocationZoom(40.499495, -74.448151, 15);
        }
        else {

            Geocoder gc = new Geocoder(this);
            List<Address> list = gc.getFromLocationName(location, 1);
            Address address = list.get(0);
            String locality = address.getLocality();

            Toast.makeText(this, locality, Toast.LENGTH_LONG).show();

            double lat = address.getLatitude();
            double lng = address.getLongitude();
            goToLocationZoom(lat, lng, 16);
        }


    }

    private void setMarker(String locality, double lat, double lng, String description) {
        switch (parseForMarkerDescription(description)) {
            case "Burglary":
                MarkerOptions optionsB = new MarkerOptions()
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                        .title(locality)
                        .position(new LatLng(lat, lng))
                        .snippet(description);
                mGoogleMap.addMarker(optionsB);
                break;

            case "Aggravated Assault":
                MarkerOptions optionsAA = new MarkerOptions()
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                        .title(locality)
                        .position(new LatLng(lat, lng))
                        .snippet(description);
                mGoogleMap.addMarker(optionsAA);
                break;

            case "Robbery":
                MarkerOptions optionsR = new MarkerOptions()
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        .title(locality)
                        .position(new LatLng(lat, lng))
                        .snippet(description);
                mGoogleMap.addMarker(optionsR);
                break;
            case "Shooting":
                MarkerOptions optionsS = new MarkerOptions()
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                        .title(locality)
                        .position(new LatLng(lat, lng))
                        .snippet(description);
                mGoogleMap.addMarker(optionsS);
                break;
            case "Robbery/Homicide":
                MarkerOptions optionsRH = new MarkerOptions()
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                        .title(locality)
                        .position(new LatLng(lat, lng))
                        .snippet(description);
                mGoogleMap.addMarker(optionsRH);

        }
    }
    private String parseForMarkerDescription(String description) {
        char d = ' ';
        int a = 0;
        for (a = 0; d != '%'; a++) {
            d = description.charAt(a);
        }
        return description.substring(0, a - 1);
    }
    private String parseForMarkerTime(String snippet){
        char l = ' ';
        char m = ' ';
        int i = 0;
        int j = 0;
        for (i = 0 ; l != '%' ; i++){
            l = snippet.charAt(i);
        }
        for (j = i ; m != '%' ; j++){
            m = snippet.charAt(j);
        }
        return snippet.substring(j);
    }

    private String parseForMarkerDate(String snippet){
        char l = ' ';
        char m = ' ';
        int i = 0;
        int j = 0;
        for (i = 0 ; l != '%' ; i++){
            l = snippet.charAt(i);
        }
        for (j = i ; m != '%' ; j++){
            m = snippet.charAt(j);
        }
        return snippet.substring(i, j-1);
    }


    LocationRequest mLocationRequest;

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location == null) {
            Toast.makeText(this, "Can't get current location", Toast.LENGTH_LONG).show();
        } else {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 15);
            mGoogleMap.animateCamera(update);
        }

    }
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    */
}

