package com.example.albumap.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.albumap.entities.Usuario;

import java.util.List;

@Dao
public interface UsuarioDAO {
    @Insert
    public long insertUsuario(Usuario usuario);
    @Update
    public void updateUsuario(Usuario usuario);
    @Delete
    public  void deleteUsuario(Usuario usuario);
    @Query("select exists (select * from usuario where email = :email)")
    public boolean existsUsuario(String email);
    @Query("select * from usuario where email = :email and senha = :senha")
    public Usuario login(String email, String senha);
    @Query("select * from usuario where usuarioId = :usuarioId")
    public Usuario getUsuario(long usuarioId);
}
