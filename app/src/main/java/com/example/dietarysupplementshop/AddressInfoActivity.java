package com.example.dietarysupplementshop;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.dietarysupplementshop.adapter.CartItemAdapter;
import com.example.dietarysupplementshop.constant.Validation;
import com.example.dietarysupplementshop.interfaces.AddressAPI;
import com.example.dietarysupplementshop.interfaces.GeocodingApi;
import com.example.dietarysupplementshop.model.Address;
import com.example.dietarysupplementshop.model.CartItem;
import com.example.dietarysupplementshop.model.District;
import com.example.dietarysupplementshop.model.Province;
import com.example.dietarysupplementshop.model.Ward;
import com.example.dietarysupplementshop.requests.AddToCartRequest;
import com.example.dietarysupplementshop.requests.UpdateAddressRequest;
import com.example.dietarysupplementshop.responses.AccountInformation;
import com.example.dietarysupplementshop.viewModel.AccountViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.List;
import java.util.stream.Collectors;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddressInfoActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Spinner citySpinner, districtSpinner, wardSpinner;

    private EditText fullNameEditText, phoneEditText, detailAddressEditText;
    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private boolean mLocationPermissionGranted = false;

    private SwitchMaterial defaultAddressSwitch;

    private List<Province> provinces;
    private AccountViewModel accountViewModel;


    private Button saveChangesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_info);

        accountViewModel = MyApplication.getInstance().getAccountViewModel();

        initializeViews();
        validateInputs();

        TextView titleActivity = findViewById(R.id.titleTextView);
        Intent intent = getIntent();
        if (intent != null) {
            long addressId = intent.getLongExtra("addressId", -1);
            if (addressId != -1) {
                titleActivity.setText("Edit Address");
                accountViewModel.getInfoAddress(addressId).observe(this, address -> {
                    if (address != null) {
                        switch (address.getStatus()) {
                            case SUCCESS:
                                if (address.getData() != null) {
                                    setupRetrofit(() -> loadDataToView(address.getData()));
                                }
                                break;
                            case ERROR:
                                Toast.makeText(getApplicationContext(), address.getMessage(), Toast.LENGTH_LONG).show();
                                break;
                            case LOADING:
                                break;
                        }
                    }
                });
                saveChangesButton.setOnClickListener(view -> {
                    if (isValidInput()) {
                        String detailAddress = detailAddressEditText.getText().toString().trim();
                        String selectedWard = wardSpinner.getSelectedItem().toString();
                        String selectedDistrict = districtSpinner.getSelectedItem().toString();
                        String selectedCity = citySpinner.getSelectedItem().toString();
                        String fullAddress = detailAddress + ", " + selectedWard + ", " + selectedDistrict + ", " + selectedCity;

                        // Tạo một request cập nhật mới
                        UpdateAddressRequest updateRequest = new UpdateAddressRequest();
                        updateRequest.setAddress_id(addressId);
                        updateRequest.setFullname(fullNameEditText.getText().toString().trim());
                        updateRequest.setPhone(phoneEditText.getText().toString());
                        updateRequest.setAddress_detail(fullAddress);
                        if(defaultAddressSwitch.isChecked()){
                            accountViewModel.setDefaultAddress(addressId);
                        }
                        accountViewModel.updateAddress(updateRequest);
                        Toast.makeText(AddressInfoActivity.this, "Update address success!", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(this, MyAddressActivity.class);
                        startActivity(intent1);
                        finish();

                    }
                });

            } else {
                titleActivity.setText("Add New Address");
                defaultAddressSwitch.setVisibility(View.GONE);
                setupRetrofit(() -> setupCitySpinner());
                saveChangesButton.setOnClickListener(view -> {
                    if (isValidInput()) {
                        String detailAddress = detailAddressEditText.getText().toString().trim();
                        String selectedWard = wardSpinner.getSelectedItem().toString();
                        String selectedDistrict = districtSpinner.getSelectedItem().toString();
                        String selectedCity = citySpinner.getSelectedItem().toString();
                        String fullAddress = detailAddress + ", " + selectedWard + ", " + selectedDistrict + ", " + selectedCity;

                        accountViewModel.addAddress(fullNameEditText.getText().toString().trim(), phoneEditText.getText().toString(), fullAddress);
                        Toast.makeText(AddressInfoActivity.this, "Add new address success!", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(this, MyAddressActivity.class);
                        startActivity(intent1);
                        finish();

                    }
                });
            }
        }

        getLocationPermission();
    }

    private void loadDataToView(Address address) {
        fullNameEditText.setText(address.getFullname());
        phoneEditText.setText(address.getPhone());
        defaultAddressSwitch.setChecked(address.getIs_default());
        defaultAddressSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (defaultAddressSwitch.isChecked() && !isChecked) {
                defaultAddressSwitch.setChecked(true);
            }
        });
        updateSpinnerWithAddress(address.getAddress_detail());
    }

    private void validateInputs() {
        fullNameEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (fullNameEditText.getText().toString().trim().isEmpty()) {
                    fullNameEditText.setError("Name cannot be empty");
                } else {
                    fullNameEditText.setError(null);
                }
            }
        });

        phoneEditText.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                String phone = phoneEditText.getText().toString();
                if (!Validation.isValidPhoneNumber(phone)) {
                    phoneEditText.setError("Invalid phone number");
                } else {
                    phoneEditText.setError(null);
                }
            }
        });
    }

    private boolean isValidInput() {
        // Kiểm tra họ và tên
        String fullName = fullNameEditText.getText().toString().trim();
        if (fullName.isEmpty()) {
            fullNameEditText.setError("Name cannot be empty");
            return false;
        }

        // Kiểm tra số điện thoại
        String phone = phoneEditText.getText().toString();
        if (!Validation.isValidPhoneNumber(phone)) {
            phoneEditText.setError("Invalid phone number");
            return false;
        }

        // Kiểm tra detail address
        String detailAddress = detailAddressEditText.getText().toString().trim();
        if (detailAddress.isEmpty()) {
            detailAddressEditText.setError("Detail address cannot be empty");
            return false;
        }

        // Kiểm tra spinner city, district, ward
        if (citySpinner.getSelectedItem() == null || citySpinner.getSelectedItem().toString().trim().isEmpty()) {
            Toast.makeText(AddressInfoActivity.this, "Please select a city", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (districtSpinner.getSelectedItem() == null || districtSpinner.getSelectedItem().toString().trim().isEmpty()) {
            Toast.makeText(AddressInfoActivity.this, "Please select a district", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (wardSpinner.getSelectedItem() == null || wardSpinner.getSelectedItem().toString().trim().isEmpty()) {
            Toast.makeText(AddressInfoActivity.this, "Please select a ward", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void initializeViews() {
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
        saveChangesButton = findViewById(R.id.saveChangesButton);

        fullNameEditText = findViewById(R.id.fullNameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        detailAddressEditText = findViewById(R.id.detailAddressEditText);

        citySpinner = findViewById(R.id.citySpinner);
        districtSpinner = findViewById(R.id.districtSpinner);
        wardSpinner = findViewById(R.id.wardSpinner);

        defaultAddressSwitch = findViewById(R.id.defaultAddressSwitch);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void setupRetrofit(ProvincesLoadCallback callback) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://provinces.open-api.vn").addConverterFactory(GsonConverterFactory.create()).build();

        AddressAPI addressAPI = retrofit.create(AddressAPI.class);
        addressAPI.getProvinces(3).enqueue(new Callback<List<Province>>() {
            @Override
            public void onResponse(Call<List<Province>> call, Response<List<Province>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    provinces = response.body();
                    if (callback != null) {
                        callback.onProvincesLoaded();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Province>> call, Throwable t) {
                provinces = null;
            }
        });
    }


    private void setupCitySpinner() {
        if (provinces != null) {
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
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            initMap();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
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
        String apiKey = "AIzaSyD8NPSxs_GNW1JBU8d5RJl5_974LL8xnV8";

        // Khởi tạo Logging Interceptor
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Thêm Interceptor vào OkHttpClient
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
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

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        GeocodingApi geocodingApi = retrofit.create(GeocodingApi.class);
        String latLngString = latLng.latitude + "," + latLng.longitude;
        Call<GeocodingApi.GeocodingResponse> call = geocodingApi.getAddressFromCoordinates(latLngString, apiKey);
        call.enqueue(new Callback<GeocodingApi.GeocodingResponse>() {
            @Override
            public void onResponse(Call<GeocodingApi.GeocodingResponse> call, Response<GeocodingApi.GeocodingResponse> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().results.isEmpty()) {
                    String address = response.body().results.get(0).toString();
                    updateSpinnerWithAddress(address);
                }
            }

            @Override
            public void onFailure(Call<GeocodingApi.GeocodingResponse> call, Throwable t) {
                Toast.makeText(AddressInfoActivity.this, "Error fetching address", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateSpinnerWithAddress(String fullAddress) {
        String[] addressParts = fullAddress.split(",");

        // Đảm bảo địa chỉ có tất cả các phần cần thiết
        if (addressParts.length >= 4) {
            detailAddressEditText.setText(addressParts[0].trim());

            String wardName = addressParts[1].trim();
            String districtName = addressParts[2].trim();
            String cityName = addressParts[3].trim();

            if (provinces != null) {
                Province matchingProvince = findMatchingProvince(cityName);

                if (matchingProvince != null) {
                    setupCitySpinnerWithProvinces(matchingProvince.getName());

                    District matchingDistrict = findMatchingDistrict(matchingProvince, districtName);

                    if (matchingDistrict != null) {
                        setupDistrictSpinnerWithDistricts(matchingProvince.getDistricts(), matchingDistrict.getName());

                        Ward matchingWard = findMatchingWard(matchingDistrict, wardName);

                        if (matchingWard != null) {
                            setupWardSpinnerWithWards(matchingDistrict.getWards(), matchingWard.getName());
                            // Cập nhật vị trí của địa chỉ này lên bản đồ
                            getCoordinatesFromAddress(fullAddress);
                        }
                    }
                }
            }
        }
    }

    private Province findMatchingProvince(String cityName) {
        return provinces.stream()
                .filter(province -> province.getName().toLowerCase().replace(" ", "").trim().equalsIgnoreCase(cityName.toLowerCase().replace(" ", "").trim()))
                .findFirst()
                .orElse(null);
    }

    private District findMatchingDistrict(Province matchingProvince, String districtName) {
        return matchingProvince.getDistricts().stream()
                .filter(district -> district.getName().toLowerCase().replace(" ", "").trim().equalsIgnoreCase(districtName.toLowerCase().replace(" ", "").trim()))
                .findFirst()
                .orElse(null);
    }

    private Ward findMatchingWard(District matchingDistrict, String wardName) {
        return matchingDistrict.getWards().stream()
                .filter(ward -> ward.getName().toLowerCase().replace(" ", "").trim().equalsIgnoreCase(wardName.toLowerCase().replace(" ", "").trim()))
                .findFirst()
                .orElse(null);
    }

    private void setupCitySpinnerWithProvinces(String selectedProvinceName) {
        List<String> provinceNames = provinces.stream().map(Province::getName).collect(Collectors.toList());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, provinceNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapter);
        setSpinnerSelection(citySpinner, selectedProvinceName);
    }

    private void setupDistrictSpinnerWithDistricts(List<District> districts, String selectedDistrictName) {
        List<String> districtNames = districts.stream().map(District::getName).collect(Collectors.toList());
        ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, districtNames);
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtSpinner.setAdapter(districtAdapter);
        setSpinnerSelection(districtSpinner, selectedDistrictName);
    }

    private void setupWardSpinnerWithWards(List<Ward> wards, String selectedWardName) {
        List<String> wardNames = wards.stream().map(Ward::getName).collect(Collectors.toList());
        ArrayAdapter<String> wardAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, wardNames);
        wardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wardSpinner.setAdapter(wardAdapter);
        setSpinnerSelection(wardSpinner, selectedWardName);
    }


    private void setSpinnerSelection(Spinner spinner, String value) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
        if (adapter != null) {
            int position = adapter.getPosition(value);
            if (position != -1) {
                spinner.setSelection(position);
            }
        }
    }

    interface ProvincesLoadCallback {
        void onProvincesLoaded();
    }


}
