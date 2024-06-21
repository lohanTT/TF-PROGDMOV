package com.example.albumap.entities;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "foto")
public class Foto implements Serializable {
    @PrimaryKey(autoGenerate = true)
    long fotoId;

    String uri;

    @Nullable
    String descricao;

    @Nullable
    Date dataHora;

    double coordX;

    double coordY;

    public Foto(long fotoId, String uri, @Nullable String descricao, @Nullable Date dataHora, double coordX, double coordY) {
        this.fotoId = fotoId;
        this.uri = uri;
        this.descricao = descricao;
        this.dataHora = dataHora;
        this.coordX = coordX;
        this.coordY = coordY;
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
    public Date getDataHora() {
        return dataHora;
    }

    public void setDataHora(@Nullable Date dataHora) {
        this.dataHora = dataHora;
    }

    public double getCoordX() {
        return coordX;
    }

    public void setCoordX(double coordX) {
        this.coordX = coordX;
    }

    public double getCoordY() {
        return coordY;
    }

    public void setCoordY(double coordY) {
        this.coordY = coordY;
    }
}
