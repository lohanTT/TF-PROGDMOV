package com.example.albumap.entities;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.threeten.bp.LocalDateTime;

import java.io.Serializable;

@Entity(tableName = "foto")
public class Foto implements Serializable {
    @PrimaryKey(autoGenerate = true)
    long fotoId;
    String uri;
    @Nullable
    String descricao;
    @Nullable
    LocalDateTime dataHora;
    public  double latitude;
    public double longitude;

    public Foto(long fotoId, String uri, @Nullable String descricao, @Nullable LocalDateTime dataHora, double latitude, double longitude) {
        this.fotoId = fotoId;
        this.uri = uri;
        this.descricao = descricao;
        this.dataHora = dataHora;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getFotoId() {
        return fotoId;
    }

    public void setFotoId(long fotoId) {
        this.fotoId = fotoId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Nullable
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(@Nullable String descricao) {
        this.descricao = descricao;
    }

    @Nullable
    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(@Nullable LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getlongitude() {
        return longitude;
    }

    public void setlongitude(double longitude) {
        this.longitude = longitude;
    }
}
