package com.arnav.pocdoc.activateLocator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.arnav.pocdoc.BaseActivity;
import com.arnav.pocdoc.R;
import com.arnav.pocdoc.SimplyRelief.models.HospitalLocatorResponse;
import com.arnav.pocdoc.databinding.ActivityActivateLocatorBinding;
import com.arnav.pocdoc.hospitalLocator.HospitalLocatorActivity;
import com.arnav.pocdoc.retrofit.ApiClient;
import com.arnav.pocdoc.retrofit.ApiInterface;
import com.arnav.pocdoc.retrofit.NetworkRequest;
import com.arnav.pocdoc.utils.Constants;
import com.arnav.pocdoc.utils.Utils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class ActivateLocatorActivity extends BaseActivity implements OnMapReadyCallback {

    protected ApiInterface apiService;
    protected CompositeSubscription compositeSubscription;
    private BottomSheetDialog bottomSheetDialog;
    private ArrayList<String> arr = new ArrayList<>();
    private List<Marker> markers = new ArrayList<>();
    private ActivityActivateLocatorBinding binding;
    private SupportMapFragment mapFragment;
    private ArrayList<LatLng> locationArrayList = new ArrayList<>();
    private GoogleMap mMap;
    private HospitalLocatorResponse result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_activate_locator);
        binding.setActivity(this);
        setUpHeaderView();

        apiService = ApiClient.getClient(this).create(ApiInterface.class);
        compositeSubscription = new CompositeSubscription();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frg_map);
        getSymptomsData();
    }

    private void setUpHeaderView() {
        binding.header.tvTitle.setText(getResources().getString(R.string.activate_locator));
        binding.header.tvBack.setOnClickListener(view -> onBackPressed());
    }

    private void getSymptomsData() {
        showProgress();
        Subscription subscription = NetworkRequest.performAsyncRequest(apiService.getPharmacy()
                , response -> {
                    hideProgress();
                    if (response.isSuccessful()) {

                        result = response.body();
                        for (int i = 0; i < result.getData().size(); i++) {
                            arr.add(result.getData().get(i).getName() + ", " + result.getData().get(i).getAddress() + ", " + result.getData().get(i).getZipcode());
                            locationArrayList.add(new LatLng(Double.parseDouble(result.getData().get(i).getLat()), Double.parseDouble(result.getData().get(i).getLong1())));
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.row_hospital, arr);
                        binding.autoCompleteTextView1.setThreshold(1);
                        binding.autoCompleteTextView1.setAdapter(adapter);

                        mapFragment.getMapAsync(this);

                        binding.autoCompleteTextView1.setOnItemClickListener((adapterView, view, ii, l) -> {
                            for (int i = 0; i < arr.size(); i++) {
                                if (arr.get(i).equals(binding.autoCompleteTextView1.getText().toString())) {
                                    mMap.getFocusedBuilding();
                                    CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                                            locationArrayList.get(i), 15);
                                    mMap.animateCamera(location);
                                    Utils.hideKeyBoard(ActivateLocatorActivity.this);
                                    break;
                                }
                            }
                        });

                    }
                }
                , throwable -> {
                    hideProgress();
                    throwable.printStackTrace();
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        for (int i = 0; i < locationArrayList.size(); i++) {
            // below line is use to add marker to each location of our array list.
            Marker marker = mMap.addMarker(new MarkerOptions().position(locationArrayList.get(i)).title(result.getData().get(i).getName()).icon(BitmapFromVector(this, R.drawable.google_markers_icon)));
            // below lin is use to zoom our camera on map.
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
            mMap.getFocusedBuilding();
            mMap.moveCamera(CameraUpdateFactory.newLatLng(locationArrayList.get(i)));
            markers.add(marker);
        }

        mMap.setOnMarkerClickListener(marker -> {
            String markerName = marker.getTitle();
            for (int i = 0; i < arr.size(); i++) {
                if (result.getData().get(i).getName().equals(markerName)) {
                    showBottomSheet(i);
                }
            }
            return false;
        });

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));
    }

    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);
        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);
        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void showBottomSheet(int position) {
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_activate_locator);
        bottomSheetDialog.getBehavior().setPeekHeight(getResources().getDimensionPixelSize(R.dimen._500sdp));
        TextView tvMedical = bottomSheetDialog.findViewById(R.id.tvMedical);
        TextView tvEmail = bottomSheetDialog.findViewById(R.id.tvEmail);
        TextView tvAddress = bottomSheetDialog.findViewById(R.id.tvAddress);
        TextView tvDesc = bottomSheetDialog.findViewById(R.id.tvDesc);
        TextView tvScanSend = bottomSheetDialog.findViewById(R.id.tvScanSend);

        tvMedical.setText(result.getData().get(position).getName());
        tvEmail.setText(result.getData().get(position).getEmail());
        tvAddress.setText(result.getData().get(position).getAddress());
        tvDesc.setText(result.getData().get(position).getDescription());
        tvScanSend.setOnClickListener(view -> {
            if (bottomSheetDialog != null)
                bottomSheetDialog.hide();
            Intent intent = new Intent(getApplicationContext(), HospitalLocatorActivity.class);
            intent.putExtra(Constants.pharmacyPosition, result.getData().get(position).getId());
            startActivity(intent);
        });

        bottomSheetDialog.show();
    }

    public void close(View view) {
        if (bottomSheetDialog != null)
            bottomSheetDialog.dismiss();
    }

}