package com.example.albumap.activities;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.example.albumap.databinding.ActivityMapBinding;

import com.example.albumap.R;

import com.example.albumap.utils.Toaster;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapBinding binding;

    //COMPONENTS
    Toaster toaster;
    FloatingActionButton btnAdd, btnFromCamera, btnFromGallery;

    //CONSTS
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private static final int REQUEST_IMAGE_CAPTURE = 201;

    //VARS
    private String currentImagePath;

    //FLAGS
    boolean areFABsVisible = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //COMPONENTS
        toaster = new Toaster(this);
        btnAdd = binding.fabAdd;
        btnFromCamera = binding.fabCamera;
        btnFromGallery = binding.fabGallery;

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (areFABsVisible) {
                    btnFromCamera.setVisibility(View.GONE);
                    btnFromGallery.setVisibility(View.GONE);
                    areFABsVisible = false;
                } else {
                    btnFromCamera.setVisibility(View.VISIBLE);
                    btnFromGallery.setVisibility(View.VISIBLE);
                    areFABsVisible = true;
                }
            }
        });

        btnFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkCameraPermission()){
                    dispatchTakePictureIntent();
                } else {
                    requestCameraPermission();
                }
            }
        });

        btnFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapActivity.this, PublicarActivity.class));
            }
        });
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

    public boolean checkCameraPermission() {
        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int writePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return cameraPermission == PackageManager.PERMISSION_GRANTED && writePermission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_CAMERA_PERMISSION);
    }

    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null){
            //Criar o arquivo de salvamento da foto
            File file = null;
            try {
                file = createImageFile();
            } catch (IOException ex){
                toaster.Short("Erro ao criar o arquivo da imagem");
                return;
            }

            if (file != null){
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.albumap.fileprovider",
                        file);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Criar um nome de arquivo de imagem
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File dir = getExternalFilesDir(null);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                dir
        );

        currentImagePath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                toaster.Short("Permissão de câmera negada");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @NonNull Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            toaster.Short("Foto capturada com sucesso: " + currentImagePath);
        }
    }
}
