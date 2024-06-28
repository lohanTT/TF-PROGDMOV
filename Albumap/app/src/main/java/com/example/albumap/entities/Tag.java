package com.example.albumap.entities;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
@Entity(tableName = "tag", indices = {@Index(value = {"descricao"}, unique = true)})
public class Tag implements Serializable {
    @PrimaryKey(autoGenerate = true)
    long tagId;
    String descricao;

    int color;

    public Tag(long tagId, String descricao, @Nullable int color) {
        this.tagId = tagId;
        this.descricao = descricao;
        this.color = color;
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

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Tag tag = (Tag) obj;
        return this.tagId == tag.getTagId();
    }
}
