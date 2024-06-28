package com.example.albumap.fragments;

import com.example.albumap.DAO.TagDAO;
import com.example.albumap.R;
import com.example.albumap.database.AppDatabase;
import com.example.albumap.databinding.ActivityPublicarBinding;
import com.example.albumap.databinding.FragmentTagselectBinding;
import com.example.albumap.entities.Tag;
import com.example.albumap.utils.Toaster;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import java.util.ArrayList;
import java.util.List;

public class TagselectFragment extends Fragment {
    FragmentTagselectBinding binding;
    AppDatabase db;
    TagDAO tagDAO;
    List<Tag> tags;
    List<Tag> selectedTags;
    ChipGroup chipGroup;
    LinearLayout noTagsTagSelect, withTagsTagSelect;

    Toaster toaster;

    TextView txtTagSelect;
    TextView txtCreateTag;
    Button btnCreateTag;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTagselectBinding.inflate(inflater);
        db = AppDatabase.getDatabase(getActivity());
        tagDAO = db.tagDAO();
        if (selectedTags == null){
            selectedTags = new ArrayList<>();
        }
        View rootView = binding.getRoot();
        noTagsTagSelect = binding.NoTagsTagSelect;
        withTagsTagSelect = binding.WithTagsTagSelect;
        chipGroup = binding.chipGroup;

        toaster = new Toaster(getContext());

        txtTagSelect = binding.txtTagSelect;
        txtTagSelect.setText(getResources().getString(R.string.filter_tag));
        txtCreateTag = binding.txtCreateTag;
        txtCreateTag.setOnClickListener(adicionarTag());
        btnCreateTag = binding.btnCreateTag;
        btnCreateTag.setOnClickListener(adicionarTag());

        getChildFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                System.out.println("Aqui: "+ result.toString() + requestKey);
                // Receber o resultado enviado pelo fragmento filho
                boolean isTagCreated = result.getBoolean("finished");
                if (isTagCreated) {
                    renderFragment();
                }
            }
        });


        renderFragment();
        return rootView;
    }

    public View.OnClickListener adicionarTag(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditarTagFragment editarTagFragment = new EditarTagFragment(new Tag(0, "", 0));
                editarTagFragment.show(getChildFragmentManager(), null);
            }
        };
    }

    public View.OnLongClickListener editarTag(Tag tag){
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                EditarTagFragment editarTagFragment = new EditarTagFragment(tag);
                editarTagFragment.show(getChildFragmentManager(), null);
                return true;
            }
        };
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }


    public void getChips(){
        tags = tagDAO.getAllTag();
    }

    public void renderFragment(){
        getChips();
        if (this.tags == null || this.tags.size() == 0){
            noTagsTagSelect.setVisibility(View.VISIBLE);
            withTagsTagSelect.setVisibility(View.GONE);
        } else {
            renderChips();
            noTagsTagSelect.setVisibility(View.GONE);
            withTagsTagSelect.setVisibility(View.VISIBLE);
        }
    }

    public void renderChips(){
        chipGroup.removeAllViews();
        for(Tag tag: tags) {
            Chip chip = new Chip(getActivity());
            chip.setText(tag.getDescricao());
            chip.setCheckable(true);
            chip.setCheckedIconVisible(true);

            if (selectedTags.contains(tag)){
                chip.setChecked(true);
            }

            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (chip.isChecked()){
                        selectedTags.add(tag);
                    } else {
                        selectedTags.remove(tag);
                    }
                }
            });

            chip.setRippleColorResource(R.color.primary);

            int[][] states = new int[][]{
                    new int[]{android.R.attr.state_enabled},
                    new int[]{android.R.attr.state_selected},
                    new int[]{android.R.attr.state_checked},
            };

            int azul = Color.blue(tag.getColor());
            int verde  = Color.green(tag.getColor());
            int vermelho = Color.red(tag.getColor());
            int alfa = Color.alpha(tag.getColor());
            int cor = Color.argb(alfa, vermelho, verde, azul);
            int [] colors = new int[]{
                    cor,
                    cor,
                    cor,
                    cor
            };
            ColorStateList chipColors = new ColorStateList(states, colors);
            chip.setChipBackgroundColor(chipColors);

            chipGroup.addView(chip);
            chip.setOnLongClickListener(editarTag(tag));
        }

    }

    public void setSelectedTags(List<Tag> selectedTags){
        this.selectedTags = selectedTags;
    }

    public List<Tag> getSelectedTags(){
        List<Tag> result = new ArrayList<>();

        for (Tag tag: tags){
            if (selectedTags.contains(tag)){
                result.add(tag);
            }
        }
        return result;
    }
}
