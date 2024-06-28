package com.example.albumap.utils;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraUtils {

    private static final String TAG = "CameraUtils";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static String currentPhotoPath;
    private static Location currentLocation;

    private CameraUtils() {
        // Construtor privado para evitar instância da classe
    }

    public static void dispatchTakePictureIntent(AppCompatActivity activity, Location location) {
        currentLocation = location;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Criar o arquivo de salvamento da foto
            File file = null;
            try {
                file = createImageFile(activity);
            } catch (IOException ex) {
                ex.printStackTrace();
                return;
            }

            if (file != null) {
                Uri photoURI = FileProvider.getUriForFile(activity,
                        "com.example.albumap.fileprovider",
                        file);

                // Adicionar informações extras ao Intent da câmera
                if (currentLocation != null) {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                }

                activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private static File createImageFile(Context context) throws IOException {
        // Criar um nome de arquivo único baseado na data/hora atual
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(null);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        currentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }

    public static void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK) {
            try {
                // Adicionar metadados EXIF à imagem capturada
                addExifMetadata(currentPhotoPath, currentLocation);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addExifMetadata(String photoPath, Location location) throws IOException {
        ExifInterface exifInterface = new ExifInterface(photoPath);
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE, convert(location.getLatitude()));
            exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, convert(location.getLongitude()));
            exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, latitude < 0 ? "S" : "N");
            exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, longitude < 0 ? "W" : "E");
        }
        // Adicione a data/hora atual
        exifInterface.setAttribute(ExifInterface.TAG_DATETIME, getCurrentDateTime());
        exifInterface.saveAttributes();
    }

    private static String getCurrentDateTime() {
        return new SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    private static String convert(double latitude) {
        latitude = Math.abs(latitude);
        int degrees = (int) latitude;
        latitude -= degrees;
        latitude *= 60.0;
        int minutes = (int) latitude;
        latitude -= minutes;
        latitude *= 60.0;
        int seconds = (int) (latitude * 1000.0);

        return degrees + "/1," + minutes + "/1," + seconds + "/1000,";
    }
}

