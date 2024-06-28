package com.example.albumap.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.albumap.entities.Foto;
import com.example.albumap.entities.relations.FotoTagCrossRef;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@Dao
public interface FotoTagCrossRefDAO {
    @Insert
    void insertFotoTagCrossRef(FotoTagCrossRef crossRef);

    @Transaction
    @Query(
            "select F.* from fototagcrossref FT " +
            "inner join foto F on F.fotoId = FT.fotoId " +
            "where FT.tagId = :tagId ")
    List<Foto> getFotoByTags(long tagId);

    @Query("select * from FotoTagCrossRef")
    List<FotoTagCrossRef> getAll();
    @Query("delete from FotoTagCrossRef where tagId = :tagId")
    void deleteTFotoTagCrossRefsByTag(long tagId);

    @Query("delete from FotoTagCrossRef where fotoId = :fotoId")
    void  deleteFotoTagCrossRefsByFoto(long fotoId);

}
