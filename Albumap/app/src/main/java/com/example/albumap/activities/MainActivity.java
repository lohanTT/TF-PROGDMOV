package com.example.albumap.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.albumap.DAO.UsuarioDAO;
import com.example.albumap.MyApplication;
import com.example.albumap.database.AppDatabase;
import com.example.albumap.databinding.ActivityMainBinding;
import com.example.albumap.entities.Usuario;
import com.example.albumap.utils.Toaster;

public class MainActivity extends AppCompatActivity {
    MyApplication app;
    ActivityMainBinding binding;

    //DATA
    AppDatabase db;
    UsuarioDAO usuarioDAO;

    //COMPONENTS
    Toaster toaster;
    EditText edtEmail, edtSenha;
    TextView txtCadastrese;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (MyApplication) getApplication();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //DATA
        db = AppDatabase.getDatabase(getApplicationContext());
        usuarioDAO = db.usuarioDAO();

        //COMPONENTES
        toaster = new Toaster(MainActivity.this);
        edtEmail = binding.edtEmail;
        edtSenha = binding.edtSenha;
        txtCadastrese = binding.txtCadastrese;

        binding.btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String email = edtEmail.getText().toString();
//                String senha = edtSenha.getText().toString();
//
//                Usuario usuario = usuarioDAO.login(email, senha);
//
//                if (usuario != null) {
//                    edtEmail.setText("");
//                    edtSenha.setText("");
//
//                    app.setUsuarioEmSessao(usuario);
//                    Intent intent = new Intent(MainActivity.this, MapActivity.class);
//                    startActivity(intent);
//                } else {
//                    toaster.Short("Credenciais inválidas");
//                    edtSenha.setText("");
//                }

                app.setUsuarioEmSessao(usuarioDAO.getUsuario(1));
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);

            }
        });

        txtCadastrese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CadastroActivity.class));
            }
        });
    }
}