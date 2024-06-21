package com.example.albumap.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
@Entity(tableName = "tag")
public class Tag implements Serializable {
    @PrimaryKey(autoGenerate = true)
    long tagId;
    String descricao;

    public Tag(long tagId, String descricao) {
        this.tagId = tagId;
        this.descricao = descricao;
    }

    public long getTagId() {
        return tagId;
    }

    public void setTagId(long tagId) {
        this.tagId = tagId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
