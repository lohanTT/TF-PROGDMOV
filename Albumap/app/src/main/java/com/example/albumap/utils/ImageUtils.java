package com.example.albumap.utils;

import android.content.Context;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {

    private static final String TAG = "ImageUtils";

    // Método para obter a data e hora de uma imagem a partir da sua URI como string
    public static LocalDateTime getDateTimeFromUriString(Context context, String imageUriString) {
        Uri imageUri = Uri.parse(imageUriString);
        String imagePath = getImagePathFromUri(context, imageUri);
        if (imagePath != null) {
            try {
                ExifInterface exif = new ExifInterface(imagePath);
                String dateTime = exif.getAttribute(ExifInterface.TAG_DATETIME);
                if (dateTime != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");
                    return LocalDateTime.parse(dateTime, formatter);
                }
            } catch (IOException e) {
                Log.e(TAG, "Erro ao ler metadados EXIF: " + e.getMessage());
            }
        }
        return null;
    }

    // Método para obter a localização (latitude e longitude) de uma imagem a partir da sua URI como string
    public static LocationInfo getLocationFromUriString(Context context, String imageUriString) {
        Uri imageUri = Uri.parse(imageUriString);
        String imagePath = getImagePathFromUri(context, imageUri);

        System.out.println(imagePath+"|||||||||"+imageUriString);
        if (imagePath != null) {
            try {
                ExifInterface exif = new ExifInterface(imagePath);
                float[] latLong = new float[2];
                if (exif.getLatLong(latLong)) {
                    return new LocationInfo(latLong[0], latLong[1]);
                }
            } catch (IOException e) {
                Log.e(TAG, "Erro ao ler metadados EXIF: " + e.getMessage());
            }
        }
        return null;
    }

    // Método utilitário para obter o caminho absoluto do arquivo a partir da URI
    private static String getImagePathFromUri(Context context, Uri imageUri) {
        String imagePath = null;
        if (imageUri == null) {
            return null;
        }
        if (imageUri.getScheme() != null && imageUri.getScheme().equals("content")) {
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
                if (inputStream != null) {
                    inputStream.close();
                    imagePath = imageUri.getPath();
                }
            } catch (IOException e) {
                Log.e(TAG, "Erro ao obter caminho absoluto da imagem: " + e.getMessage());
            }
        } else if (imageUri.getScheme() != null && imageUri.getScheme().equals("file")) {
            imagePath = imageUri.getPath();
        }
        return imagePath;
    }

    // Classe interna para encapsular informações de localização (latitude e longitude)
    public static class LocationInfo {
        private double latitude;
        private double longitude;

        public LocationInfo(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }
}
