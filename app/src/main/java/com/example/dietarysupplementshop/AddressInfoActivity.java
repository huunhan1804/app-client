package com.example.dietarysupplementshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dietarysupplementshop.interfaces.AddressAPI;
import com.example.dietarysupplementshop.interfaces.GeocodingApi;
import com.example.dietarysupplementshop.model.District;
import com.example.dietarysupplementshop.model.Province;
import com.example.dietarysupplementshop.model.Ward;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;
import java.util.stream.Collectors;

public class AddressInfoActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Spinner citySpinner, districtSpinner, wardSpinner;
    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private boolean mLocationPermissionGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_info);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initializeViews();
        setupRetrofit();
        getLocationPermission();
    }

    private void initializeViews() {
        citySpinner = findViewById(R.id.citySpinner);
        districtSpinner = findViewById(R.id.districtSpinner);
        wardSpinner = findViewById(R.id.wardSpinner);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void setupRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://provinces.open-api.vn")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AddressAPI addressAPI = retrofit.create(AddressAPI.class);
        addressAPI.getProvinces(3).enqueue(new Callback<List<Province>>() {
            @Override
            public void onResponse(Call<List<Province>> call, Response<List<Province>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setupCitySpinner(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Province>> call, Throwable t) {
                // Handle error
            }
        });
    }

    private void setupCitySpinner(List<Province> provinces) {
        List<String> provinceNames = provinces.stream().map(Province::getName).collect(Collectors.toList());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, provinceNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapter);
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setupDistrictSpinner(provinces.get(position).getDistricts());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void setupDistrictSpinner(List<District> districts) {
        List<String> districtNames = districts.stream().map(District::getName).collect(Collectors.toList());
        ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, districtNames);
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtSpinner.setAdapter(districtAdapter);
        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setupWardSpinner(districts.get(position).getWards());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void setupWardSpinner(List<Ward> wards) {
        List<String> wardNames = wards.stream().map(Ward::getName).collect(Collectors.toList());
        ArrayAdapter<String> wardAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, wardNames);
        wardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wardSpinner.setAdapter(wardAdapter);
        wardSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedWard = wards.get(position).getName();
                String selectedDistrict = districtSpinner.getSelectedItem().toString();
                String selectedCity = citySpinner.getSelectedItem().toString();
                String fullAddress = selectedWard + ", " + selectedDistrict + ", " + selectedCity;
                getCoordinatesFromAddress(fullAddress);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            initMap();
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionGranted = false;
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.length > 0) {
            mLocationPermissionGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = false;
                    return;
                }
            }
            initMap();
        }
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mLocationPermissionGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
    }

    private void getDeviceLocation() {
        FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionGranted) {
                Task<Location> location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Location currentLocation = task.getResult();
                        if (currentLocation != null) {
                            LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(currentLatLng).title("My Location"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f));
                            reverseGeocodeLocation(currentLatLng);
                        } else {
                            Toast.makeText(AddressInfoActivity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(AddressInfoActivity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void getCoordinatesFromAddress(String address) {
        String BASE_URL = "https://maps.googleapis.com/";
        String apiKey = "AIzaSyBMBro7LzRzc3zcBq83Gp0fU3FfrRS2IcA";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GeocodingApi geocodingApi = retrofit.create(GeocodingApi.class);
        Call<GeocodingApi.GeocodingResponse> call = geocodingApi.getCoordinatesFromAddress(address, apiKey);
        call.enqueue(new Callback<GeocodingApi.GeocodingResponse>() {
            @Override
            public void onResponse(Call<GeocodingApi.GeocodingResponse> call, Response<GeocodingApi.GeocodingResponse> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().results.isEmpty()) {
                    double lat = response.body().results.get(0).geometry.location.lat;
                    double lng = response.body().results.get(0).geometry.location.lng;
                    LatLng latLng = new LatLng(lat, lng);
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Selected Address"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
                } else {
                    Toast.makeText(AddressInfoActivity.this, "Unable to find coordinates for the address", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GeocodingApi.GeocodingResponse> call, Throwable t) {
                // Xử lý lỗi
            }
        });
    }

    private void reverseGeocodeLocation(LatLng latLng) {
        String BASE_URL = "https://maps.googleapis.com/";
        String apiKey = "AIzaSyBMBro7LzRzc3zcBq83Gp0fU3FfrRS2IcA";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GeocodingApi geocodingApi = retrofit.create(GeocodingApi.class);
        String latLngString = latLng.latitude + "," + latLng.longitude;
        Call<GeocodingApi.GeocodingResponse> call = geocodingApi.getAddressFromCoordinates(latLngString, apiKey);
        call.enqueue(new Callback<GeocodingApi.GeocodingResponse>() {
            @Override
            public void onResponse(Call<GeocodingApi.GeocodingResponse> call, Response<GeocodingApi.GeocodingResponse> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().results.isEmpty()) {
                    String address = response.body().results.get(0).toString();
                    updateSpinnerWithAddress(address);
                } else {
                    Toast.makeText(AddressInfoActivity.this, "Unable to find address for the coordinates", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GeocodingApi.GeocodingResponse> call, Throwable t) {
                Toast.makeText(AddressInfoActivity.this, "Error fetching address", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateSpinnerWithAddress(String address) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) citySpinner.getAdapter();
        if (adapter != null) {
            adapter.insert(address, 0);
            citySpinner.setSelection(0);
        } else {
            // Xử lý trường hợp adapter chưa được thiết lập
            Toast.makeText(this, "Adapter not set yet!", Toast.LENGTH_SHORT).show();
        }
    }


}
