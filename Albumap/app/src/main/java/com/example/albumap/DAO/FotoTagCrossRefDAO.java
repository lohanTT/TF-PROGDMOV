package com.example.albumap.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.albumap.entities.Foto;
import com.example.albumap.entities.relations.FotoTagCrossRef;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@Dao
public interface FotoTagCrossRefDAO {
    @Insert
    void insertFotoTagCrossRef(FotoTagCrossRef crossRef);
    @Query(
            "select F.* from foto F " +
            "inner join FotoTagCrossRef FT on F.fotoId = FT.fotoId " +
            "where FT.tagId IN (:tagIds) " +
            "GROUP BY F.fotoId " +
            "HAVING COUNT(DISTINCT FT.tagId) = :tagCount")
    List<Foto> getFotoByTags(long[] tagIds, int tagCount);
}
