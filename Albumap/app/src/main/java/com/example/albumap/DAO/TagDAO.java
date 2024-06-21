package com.example.albumap.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.albumap.entities.Tag;
import com.example.albumap.entities.relations.TagWithFotos;

import java.util.List;

@Dao
public interface TagDAO {
    @Insert
    public long insertTag(Tag tag);
    @Update
    public void updateTag(Tag tag);
    @Delete
    public void deleteTag(Tag tag);
    @Query("select * from tag")
    public List<Tag> getAllTag();
    @Query("select * from tag where tagId = :tagId")
    public Tag getTag(long tagId);
    @Transaction
    @Query("select * from tag where tagId = :tagId")
    public TagWithFotos getTagWithFotos(long tagId);
}
