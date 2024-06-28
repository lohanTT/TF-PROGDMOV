package com.example.albumap.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.albumap.DAO.FotoDAO;
import com.example.albumap.DAO.FotoTagCrossRefDAO;
import com.example.albumap.DAO.TagDAO;
import com.example.albumap.DAO.UsuarioDAO;
import com.example.albumap.entities.Foto;
import com.example.albumap.entities.Tag;
import com.example.albumap.entities.Usuario;
import com.example.albumap.entities.relations.FotoTagCrossRef;

@Database(entities = {Usuario.class, Foto.class, Tag.class, FotoTagCrossRef.class}, version = 9, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "Albumap")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public abstract UsuarioDAO usuarioDAO();
    public abstract FotoDAO fotoDAO();
    public abstract TagDAO tagDAO();
    public abstract FotoTagCrossRefDAO fotoTagCrossRefDAO();
}
