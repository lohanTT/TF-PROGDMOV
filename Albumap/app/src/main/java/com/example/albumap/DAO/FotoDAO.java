package com.example.albumap.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.albumap.entities.Foto;
import com.example.albumap.entities.relations.FotoWithTags;

import java.util.List;

@Dao
public interface FotoDAO {
    @Insert
    long inserirFoto(Foto foto);
    @Update
    void updateFoto(Foto foto);
    @Transaction
    @Query("select * from foto where fotoId = :fotoId")
    FotoWithTags getFotoWithTags(long fotoId);

    @Transaction
    @Query("select * from foto where fotoId")
    List<FotoWithTags> GetAllFotoWithTags();

    @Query("select * from foto")
    List<Foto> getAllFoto();

    @Query("select * from foto where fotoId = :fotoId")
    Foto getFotoById(long fotoId);


}
