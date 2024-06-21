package com.example.albumap.activities;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.example.albumap.databinding.ActivityMapBinding;

import com.example.albumap.R;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Adicionar um marcador em um local de exemplo e mover a câmera
        LatLng exampleLocation = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(exampleLocation).title("Marker in Example Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(exampleLocation, 10));

        // Adicionar mais marcadores com base nos dados das fotos
        // Exemplo:
        // LatLng photoLocation = new LatLng(photo.getLatitude(), photo.getLongitude());
        // mMap.addMarker(new MarkerOptions().position(photoLocation).title(photo.getTag()));
    }
}
