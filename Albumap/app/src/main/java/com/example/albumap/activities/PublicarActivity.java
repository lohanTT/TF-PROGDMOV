package com.example.albumap.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.albumap.DAO.FotoDAO;
import com.example.albumap.DAO.FotoTagCrossRefDAO;
import com.example.albumap.GeocodingResponse;
import com.example.albumap.GeocodingService;
import com.example.albumap.R;
import com.example.albumap.database.AppDatabase;
import com.example.albumap.databinding.ActivityPublicarBinding;
import com.example.albumap.entities.Foto;
import com.example.albumap.entities.Tag;
import com.example.albumap.entities.relations.FotoTagCrossRef;
import com.example.albumap.fragments.TagselectFragment;
import com.example.albumap.utils.ImageUtils;
import com.example.albumap.utils.Toaster;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeParseException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PublicarActivity extends AppCompatActivity {
    Intent intent;
    ActivityPublicarBinding binding;
    AppDatabase db;
    FotoDAO fotoDAO;
    FotoTagCrossRefDAO fotoTagCrossRefDAO;

    // PLACE PICKER
    int PLACE_PICKER_REQUEST = 1;

    // DATA
    Foto foto;

    // COMPONENTS
    private static final String TAG = "DisplayPlaceActivity";
    private PlacesClient placesClient;
    private static final String BASE_URL = "https://maps.googleapis.com/";
    AutocompleteSupportFragment autocompleteFragment;
    TagselectFragment tagselectFragment;
    ImageView imageView;
    Toaster toaster;
    EditText edtDescricao, edtData, edtHora, edtLocalizacao;

    FloatingActionButton btnConfirmarPublicacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPublicarBinding.inflate(getLayoutInflater());
        db = AppDatabase.getDatabase(PublicarActivity.this);
        fotoDAO = db.fotoDAO();
        fotoTagCrossRefDAO = db.fotoTagCrossRefDAO();
        setContentView(binding.getRoot());

        intent = getIntent();
        // DATA
        foto = (Foto) intent.getSerializableExtra("Foto");

        // COMPONENTS
        imageView = binding.imgViewPublicar;

        Places.initialize(getApplicationContext(), getString(R.string.google_maps_API_key));
        placesClient = Places.createClient(this);
        autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        tagselectFragment = new TagselectFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.tagselect_fragment, tagselectFragment)
                .commit();

        toaster = new Toaster(this);

        edtDescricao = binding.edtDescricao;

        edtData = binding.edtData;

        edtHora = binding.edtHora;

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {

                foto.setLatitude(place.getLatLng().latitude);
                foto.setlongitude(place.getLatLng().longitude);
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
            }
        });

        edtData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        edtHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        btnConfirmarPublicacao = binding.btnConfirmarPublicacao;
        btnConfirmarPublicacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PublicacaoValida()){
                    toaster.Short("Insira ao menos a localização");
                } else {
                    foto.setDescricao(edtDescricao.getText().toString().trim());
                    if(foto.getFotoId() == 0){
                        fotoDAO.inserirFoto(foto);
                        for (Tag tag: tagselectFragment.getSelectedTags()){
                            FotoTagCrossRef fotoTagCrossRef = new FotoTagCrossRef();
                            fotoTagCrossRef.fotoId = foto.getFotoId();
                            fotoTagCrossRef.tagId = tag.getTagId();
                            fotoTagCrossRefDAO.insertFotoTagCrossRef(fotoTagCrossRef);
                        }
                    } else {
                        fotoDAO.updateFoto(foto);
                        fotoTagCrossRefDAO.deleteFotoTagCrossRefsByFoto(foto.getFotoId());
                        for (Tag tag: tagselectFragment.getSelectedTags()){
                            FotoTagCrossRef fotoTagCrossRef = new FotoTagCrossRef();
                            fotoTagCrossRef.fotoId = foto.getFotoId();
                            fotoTagCrossRef.tagId = tag.getTagId();
                            fotoTagCrossRefDAO.insertFotoTagCrossRef(fotoTagCrossRef);
                        }
                    }

                    finish();

                }

            }
        });
        renderImage();
        renderTagPicker();
        renderDescricaoPicker();
        renderLocationPicker();
        renderDateAndTimePicker();

    }

    private boolean PublicacaoValida() {
        return !(foto.getLatitude() == 0 || foto.getlongitude() == 0);
    }

    private void showDatePickerDialog(){
        LocalDateTime date = foto.getDataHora();
        int year = date.getYear();
        int month = date.getMonthValue() -1;
        int day = date.getDayOfMonth();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                PublicarActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        LocalDateTime newDate = date.withYear(year).withMonth(month + 1).withDayOfMonth(dayOfMonth);
                        updateDate(newDate);
                    }
                },
                year,
                month,
                day
        );

        datePickerDialog.show();

    }

    private void showTimePickerDialog(){
        LocalDateTime time = foto.getDataHora();
        int hour = time.getHour();
        int minute = time.getMinute();

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                PublicarActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                        LocalDateTime newTime = foto.getDataHora().withHour(hourOfDay).withMinute(minute);
                        updateTime(newTime);
                    }
                },
                hour,
                minute,
                true
        );

        timePickerDialog.show();
    }

    private void updateDate(LocalDateTime newTime){
        foto.setDataHora(newTime);
        DateTimeFormatter dateformatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");

        edtData.setText(newTime.format(dateformatter));
    }

    private void updateTime(LocalDateTime newTime){
        foto.setDataHora(newTime);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        edtHora.setText(newTime.format(timeFormatter));
    }

    private void getPlaceDetails(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> adresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (adresses != null && !adresses.isEmpty()){
                Address adress = adresses.get(0);
                String placeName = adress.getFeatureName();
                String placeAdress = adress.getAddressLine(0);

                this.autocompleteFragment.setText(placeAdress + ", " + placeName);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getPlaceFromCoordinates(double latitude, double longitude) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GeocodingService service = retrofit.create(GeocodingService.class);

        String latlng = latitude + "," + longitude;
        Call<GeocodingResponse> call = service.getGeocodingResult(latlng, "YOUR_API_KEY");

        call.enqueue(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<GeocodingResponse.Result> results = response.body().results;
                    if (!results.isEmpty()) {
                        String placeId = results.get(0).place_id;
                        fetchPlaceDetails(placeId);
                    } else {
                        Log.e(TAG, "No results found");
                    }
                } else {
                    Log.e(TAG, "Geocoding request failed");
                }
            }

            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable t) {
                Log.e(TAG, "Geocoding request failed: " + t.getMessage());
            }
        });
    }

    private void fetchPlaceDetails(String placeId) {
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);

        FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);

        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();

        }).addOnFailureListener((exception) -> {
            Log.e(TAG, "Place not found: " + exception.getMessage());
        });
    }

    private Bitmap loadImageWithCorrectOrientation(String imagePath) throws IOException {
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

        ExifInterface exif = new ExifInterface(imagePath);
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(bitmap, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(bitmap, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(bitmap, 270);
            case ExifInterface.ORIENTATION_NORMAL:
            default:
                return bitmap;
        }
    }

    private Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private void renderImage(){
        try {
            Bitmap bitmap =  loadImageWithCorrectOrientation(foto.getUri());
            imageView.setImageBitmap(bitmap);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void renderDescricaoPicker(){
        edtDescricao.setText(foto.getDescricao());
    }
    private void renderTagPicker(){
        if (foto.getFotoId() != 0){
            List<Tag> tags = fotoDAO.getFotoWithTags(foto.getFotoId()).tags;
            tagselectFragment.setSelectedTags(tags);
        }
    }
    private void renderLocationPicker(){
        if (foto.getFotoId() == 0){
            try {
                ExifInterface exifInterface = new ExifInterface(foto.getUri());
                float[] latlong = new float[2];
                if (exifInterface.getLatLong(latlong)){
                    double latitude = latlong[0];
                    double longitude = latlong[1];
                    getPlaceDetails(latitude, longitude);

                }
                else {
                    getPlaceDetails(0, 0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            getPlaceDetails(foto.getLatitude(), foto.getlongitude());
        }
    }
    private void renderDateAndTimePicker(){
        if (foto.getFotoId() == 0){
            try {
                ExifInterface exifInterface = new ExifInterface(foto.getUri());
                String dateTime = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");

                try {
                    LocalDateTime localDateTime = LocalDateTime.parse(dateTime, formatter);
                    updateDate(localDateTime);
                    updateTime(localDateTime);
                } catch (DateTimeParseException e) {
                    e.printStackTrace();
                }
                System.out.println(dateTime);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            updateTime(foto.getDataHora());
            updateDate(foto.getDataHora());
        }
    }

};



