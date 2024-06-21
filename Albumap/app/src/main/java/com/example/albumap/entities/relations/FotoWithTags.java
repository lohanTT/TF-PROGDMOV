package com.example.albumap.entities.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.albumap.entities.Foto;
import com.example.albumap.entities.Tag;

import java.util.List;

public class FotoWithTags {
    @Embedded
    public Foto foto;

    @Relation(
            parentColumn = "fotoId",
            entityColumn = "tagId",
            associateBy = @Junction(FotoTagCrossRef.class)
    )
    public List<Tag> tags;
}
