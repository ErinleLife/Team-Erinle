package com.arnav.pocdoc.activateLocator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.arnav.pocdoc.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class ActivateLocatorActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    BottomSheetDialog bottomSheetDialog;

    LatLng sydney = new LatLng(-34, 151);
    LatLng TamWorth = new LatLng(-31.083332, 150.916672);
    LatLng NewCastle = new LatLng(-32.916668, 151.750000);
    LatLng Brisbane = new LatLng(-27.470125, 153.021072);

    ArrayList<LatLng> locationArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_locator);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frg_map);
        mapFragment.getMapAsync(this);


        // in below line we are initializing our array list.

        // on below line we are adding our
        // locations in our array list.
        locationArrayList.add(sydney);
        locationArrayList.add(TamWorth);
        locationArrayList.add(NewCastle);
        locationArrayList.add(Brisbane);


    }

    public void back(View view) {
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//
//        mMap.addMarker(new MarkerOptions().position(new LatLng(79, 20)).title("Marker"));
//        mMap.addMarker(new MarkerOptions().position(new LatLng(50, 60)).title("Marker"));
//        mMap.addMarker(new MarkerOptions().position(new LatLng(90, 20)).title("Marker"));
//        mMap.getFocusedBuilding();
//        LatLng latLng = new LatLng(79, 20);
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
//        mMap.animateCamera(cameraUpdate);

        for (int i = 0; i < locationArrayList.size(); i++) {

            // below line is use to add marker to each location of our array list.
            mMap.addMarker(new MarkerOptions().position(locationArrayList.get(i)).title("Marker").icon(BitmapFromVector(this,R.drawable.google_markers_icon)));

            // below lin is use to zoom our camera on map.
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f));

            mMap.getFocusedBuilding();
//            LatLng latLng = new LatLng(locationArrayList.get(i).longitude, locationArrayList.get(i).longitude);
//            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(locationArrayList.get(i), 17);
//            mMap.animateCamera(cameraUpdate);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(locationArrayList.get(i)));
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String markerName = marker.getTitle();
                showBottomSheet();
                return false;
            }
        });

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



    private void showBottomSheet() {

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_activate_locator);

        bottomSheetDialog.getBehavior().setPeekHeight(getResources().getDimensionPixelSize(R.dimen._500sdp));



        bottomSheetDialog.show();
    }


    public void close(View view) {
        bottomSheetDialog.dismiss();
    }



}