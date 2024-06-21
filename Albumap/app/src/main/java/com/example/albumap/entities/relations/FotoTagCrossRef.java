package com.example.albumap.entities.relations;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(primaryKeys = {"fotoId", "tagId"})
public class FotoTagCrossRef {
    @NonNull
    public long fotoId;
    @NonNull
    public long tagId;
}
