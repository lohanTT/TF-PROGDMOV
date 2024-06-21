package com.example.albumap.entities.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.albumap.entities.Foto;
import com.example.albumap.entities.Tag;

import java.util.List;

public class TagWithFotos{
    @Embedded
    public Tag tag;

    @Relation(
            parentColumn = "tagId",
            entityColumn = "fotoId",
            associateBy = @Junction(FotoTagCrossRef.class)
    )
    public List<Foto> fotos;
}
