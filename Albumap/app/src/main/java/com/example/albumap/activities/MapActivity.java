package com.example.albumap.activities;
import android.Manifest;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.example.albumap.DAO.FotoDAO;
import com.example.albumap.DAO.FotoTagCrossRefDAO;
import com.example.albumap.DAO.TagDAO;
import com.example.albumap.database.AppDatabase;
import com.example.albumap.databinding.ActivityMapBinding;

import com.example.albumap.R;

import com.example.albumap.entities.Foto;
import com.example.albumap.entities.Tag;
import com.example.albumap.entities.relations.FotoTagCrossRef;
import com.example.albumap.entities.relations.FotoWithTags;
import com.example.albumap.fragments.TagSelectDialogFragment;
import com.example.albumap.utils.CameraUtils;
import com.example.albumap.utils.Toaster;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.threeten.bp.LocalDateTime;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, TagSelectDialogFragment.MyDialogListener {
    AppDatabase db;
    FotoDAO fotoDAO;
    FotoTagCrossRefDAO fotoTagCrossRefDAO;
    List<Foto> fotos;
    TagDAO tagDAO;
    List<Tag> tags;
    private GoogleMap mMap;
    private ActivityMapBinding binding;


    //COMPONENTS
    TagSelectDialogFragment tagSelectDialogFragment;
    LocationManager locationManager;
    SupportMapFragment mapFragment;
    HashMap<String, Marker> markers;
    Toaster toaster;
    FloatingActionButton btnAdd, btnFromCamera, btnFromGallery, btnFilter;

    //CONSTS
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private static final int REQUEST_IMAGE_CAPTURE = 201;
    private static final int REQUEST_LOCATION_PERMISSION = 202;
    private static final int REQUEST_GALLERY_PERMISSION = 203;
    private static final int PICK_IMAGE_REQUEST = 204;

    //VARS
    private String currentImagePath = "";
    private Location currentLocation = null;
    //FLAGS
    boolean areFABsVisible = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = AppDatabase.getDatabase(this);
        fotoDAO = db.fotoDAO();
        fotoTagCrossRefDAO = db.fotoTagCrossRefDAO();
        tagDAO = db.tagDAO();
        tags = tagDAO.getAllTag();

        //COMPONENTS
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        currentLocation = getLocation();



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnAdd = binding.fabAdd;
        btnFromCamera = binding.fabCamera;
        btnFromGallery = binding.fabGallery;
        btnFilter = binding.fabFilter;

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
                if (checkGalleryPermission()){
                    openGallery();
                } else {
                    requestGalleryPermission();
                }
                //startActivity(new Intent(MapActivity.this, PublicarActivity.class));
            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTagSelectDialog(tags);
            }
        });
    }

    private void showTagSelectDialog(List<Tag> selecionadas) {
        tagSelectDialogFragment = new TagSelectDialogFragment();
        System.out.println("||||||||||||||||||a"+selecionadas);
        tagSelectDialogFragment.setSelectedTags(selecionadas);
        System.out.println("||||||||||||||||||b"+selecionadas);
        tagSelectDialogFragment.show(getSupportFragmentManager(), "DialogTags");
    }

    @Override
    public void onDialogResult(String result) {
        tags = tagSelectDialogFragment.getSelectedTags();
        refreshFotos();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (!checkLocationPermission()){
            requestLocationPermission();
        } else {
            System.out.println("!!!!!!!!!!!!!!!!!");
            mMap = googleMap;
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    long fotoId = Long.parseLong(marker.getTitle());
                    Foto alterarFoto = fotoDAO.getFotoById(fotoId);
                    Intent intent = new Intent(MapActivity.this, PublicarActivity.class);
                    intent.putExtra("Foto", alterarFoto);
                    startActivity(intent);
                    return false;
                }
            });

            System.out.println(currentLocation);
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
//             Adicionar um marcador em um local de exemplo e mover a câmera
            markers = new HashMap<>();
            refreshFotos();
            // Adicionar mais marcadores com base nos dados das fotos
            // Exemplo:
            // LatLng photoLocation = new LatLng(photo.getLatitude(), photo.getLongitude());
            // mMap.addMarker(new MarkerOptions().position(photoLocation).title(photo.getTag()));
        }
    }

    public void removeAllMarkers(){
        for (Marker marker: markers.values()){
            marker.remove();
        }
        markers.clear();
    }
    public void addMarkers(){
        if (mMap != null){
            for(Foto foto: fotos){
                Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(foto.latitude, foto.getlongitude())));
                marker.setTitle(String.valueOf(foto.getFotoId()));
                markers.put(String.valueOf(foto.getFotoId()), marker);
            }
        }
    }

    public boolean checkGalleryPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestGalleryPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_GALLERY_PERMISSION);
    }

    public boolean checkLocationPermission(){
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarsePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        return locationPermission == PackageManager.PERMISSION_GRANTED && coarsePermission == PackageManager.PERMISSION_GRANTED;
    }

    private  void requestLocationPermission(){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_LOCATION_PERMISSION );
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

    private void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
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
                if (currentLocation != null){
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                }

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

        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLocation();
                LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
            }
        }
    }

    public static String getFilePathFromUri(ContentResolver contentResolver, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = contentResolver.query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String filePath = cursor.getString(column_index);
            cursor.close();
            return filePath;
        }
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @NonNull Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null){
            Uri selectedImageUri = data.getData();
            currentImagePath = getFilePathFromUri(getContentResolver(), selectedImageUri);

            Foto novaFoto = new Foto(
                    0,
                    currentImagePath,
                    "",
                    LocalDateTime.now(),
                    0, 0
            );

            Intent intent = new Intent(MapActivity.this, PublicarActivity.class);
            intent.putExtra("Foto", novaFoto);
            intent.putExtra("curLat", currentLocation.getLatitude());
            intent.putExtra("curLon", currentLocation.getLongitude());
            startActivity(intent);
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            getLocation();

            try {
                System.out.println("PHOTO PATH "+currentImagePath);
                CameraUtils.addExifMetadata(currentImagePath, currentLocation);
            } catch (IOException e){
                e.printStackTrace();
            }


            Foto novaFoto = new Foto(
                0,
                    currentImagePath,
                    "",
                    LocalDateTime.now(),
                    0, 0
            );

            Intent intent = new Intent(MapActivity.this, PublicarActivity.class);
            intent.putExtra("Foto", novaFoto);
            intent.putExtra("curLat", currentLocation.getLatitude());
            intent.putExtra("curLon", currentLocation.getLongitude());
            startActivity(intent);
        }
    }

    public Location getLocation(){
        if (checkLocationPermission()) {
            currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return currentLocation;
        } else {
            return  null;
        }

    }

    public void refreshFotos(){
        StringBuilder stringBuilder = new StringBuilder();
        long[] tagIDs = new long[tags.size()];

        for (int i = 0; i < tags.size(); i++){
            tagIDs[i] = tags.get(i).getTagId();
        }

        fotos = fotoDAO.getAllFoto();

        removeAllMarkers();
        addMarkers();
    }
}
