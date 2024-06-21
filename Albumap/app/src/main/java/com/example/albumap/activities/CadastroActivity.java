package com.example.albumap.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.albumap.DAO.UsuarioDAO;
import com.example.albumap.MyApplication;
import com.example.albumap.R;
import com.example.albumap.database.AppDatabase;
import com.example.albumap.databinding.ActivityCadastroBinding;
import com.example.albumap.entities.Usuario;
import com.example.albumap.utils.Toaster;
import com.example.albumap.utils.Validators;

public class CadastroActivity extends AppCompatActivity {
    MyApplication app;
    ActivityCadastroBinding binding;

    //DATA
    AppDatabase db;
    UsuarioDAO usuarioDAO;

    //COMPONENTS
    Toaster toaster;
    Button btnCadastrar;
    EditText edtNome, edtEmail, edtSenha, edtConfirmarSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (MyApplication) getApplication();
        binding = ActivityCadastroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //DATA
        db = AppDatabase.getDatabase(getApplicationContext());
        usuarioDAO = db.usuarioDAO();

        //COMPONENTS
        toaster = new Toaster(CadastroActivity.this);
        btnCadastrar = binding.btnCadastrar;
        edtNome = binding.edtCadastrarNome;
        edtEmail = binding.edtCadastrarEmail;
        edtSenha = binding.edtCadastrarSenha;
        edtConfirmarSenha = binding.edtConfirmarCadastrarSenha;

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = edtNome.getText().toString();
                String email = edtEmail.getText().toString();
                String senha = edtSenha.getText().toString();
                String confirmacao = edtConfirmarSenha.getText().toString();

                if (!Validators.isNome(nome)){
                    toaster.Long("Preencha o campo 'nome'");
                    return;
                }

                if (!Validators.isEmail(email)){
                    toaster.Long("Email inválido");
                    return;
                }

                if (usuarioDAO.existsUsuario(email)){
                    toaster.Long("Email em uso");
                    return;
                }

                if (!Validators.isSenha(senha)){
                    toaster.Long("A senha deve conter 8 caracteres, sendo letras maiúsculas e minúsculas, números e caracteres especiais");
                    return;
                }

                if (!Validators.isConfirmacaoSenha(senha, confirmacao)){
                    toaster.Long("A senha não foi confirmada corretamente");
                    return;
                }

                Usuario usuario = new Usuario((long) 0 , nome, email, senha);

                app.setUsuarioEmSessao(usuarioDAO.getUsuario(usuarioDAO.insertUsuario(usuario)));

                edtNome.setText("");
                edtEmail.setText("");
                edtSenha.setText("");
                edtConfirmarSenha.setText("");

                startActivity(new Intent(CadastroActivity.this, MapActivity.class));
            }
        });



    }
}