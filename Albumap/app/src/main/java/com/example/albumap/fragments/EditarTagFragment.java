package com.example.albumap.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.albumap.DAO.FotoTagCrossRefDAO;
import com.example.albumap.DAO.TagDAO;
import com.example.albumap.database.AppDatabase;
import com.example.albumap.databinding.FragmentEditarTagBinding;
import com.example.albumap.entities.Tag;
import com.nvt.color.ColorPickerDialog;

import java.util.Random;

public class EditarTagFragment extends DialogFragment {
    FragmentEditarTagBinding binding;
    AppDatabase db;
    TagDAO tagDAO;
    FotoTagCrossRefDAO fotoTagCrossRefDAO;
    Tag tag;
    private OnTagCreatedListener onTagCreatedListener;

    int color;

    EditText edtEditTag;
    LinearLayout colorEditTag;
    Button btnConfirmar, btnApagar;
    public EditarTagFragment(Tag tag){
        super();
        this.tag = tag;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEditarTagBinding.inflate(inflater);
        db = AppDatabase.getDatabase(getContext());
        tagDAO = db.tagDAO();
        fotoTagCrossRefDAO = db.fotoTagCrossRefDAO();
        edtEditTag = binding.edtEditTag;
        colorEditTag = binding.colorEditTag;
        Random random = new Random();
        color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        colorEditTag.getBackground().setTint(color);
        colorEditTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialog colorPickerDialog = new ColorPickerDialog(getContext(), color, false, new ColorPickerDialog.OnColorPickerListener() {
                    @Override
                    public void onCancel(ColorPickerDialog dialog) {

                    }

                    @Override
                    public void onOk(ColorPickerDialog dialog, int color) {
                        setColor(color);
                    }
                });
                colorPickerDialog.show();
            }
        });

        btnConfirmar = binding.btnEditTag;
        btnConfirmar.setBackgroundColor(color);
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tag.setColor(color);
                tag.setDescricao(edtEditTag.getText().toString().trim());

                if (tag.getTagId() == 0){
                    tagDAO.insertTag(tag);
                } else {
                    tagDAO.updateTag(tag);
                }

                Bundle result = new Bundle();
                result.putBoolean("finished", true);
                getParentFragmentManager().setFragmentResult("requestKey", result);

                dismiss();
            }
        });

        btnApagar = binding.btnExcluirTag;
        btnApagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Você realmente deseja excluir esta tag? Se continuar, todas as fotos com essa tag perderão a associação.").setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            fotoTagCrossRefDAO.deleteTFotoTagCrossRefsByTag(tag.getTagId());
                            tagDAO.deleteTag(tag);

                            Bundle result = new Bundle();
                            result.putBoolean("finished", true);
                            getParentFragmentManager().setFragmentResult("requestKey", result);

                            dismiss();
                        } catch (SQLiteConstraintException e) {
                            new AlertDialog.Builder(getContext()).setMessage("Erro ao excluir").show();
                        }
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
        });

        if (tag.getTagId() != 0){
            renderEditar();
        } else {
            renderAdicionar();
        }


        return binding.getRoot();
    }
    private void renderEditar(){
        edtEditTag.setText(tag.getDescricao());
        setColor(tag.getColor());
        btnApagar.setVisibility(View.VISIBLE);
    }

    private void renderAdicionar(){
        edtEditTag.setText("");
        btnApagar.setVisibility(View.INVISIBLE);
    }

    private void setColor(int color){
        this.color = color;
        colorEditTag.getBackground().setTint(color);
        btnConfirmar.setBackgroundColor(color);
    }
}
