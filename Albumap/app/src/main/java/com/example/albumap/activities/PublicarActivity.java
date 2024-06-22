package com.example.albumap.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.albumap.R;
import com.example.albumap.databinding.ActivityPublicarBinding;
import com.example.albumap.entities.Foto;
import com.example.albumap.utils.Toaster;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PublicarActivity extends AppCompatActivity {
    ActivityPublicarBinding binding;

    // DATA
    Foto foto;

    // COMPONENTS
    Toaster toaster;
    EditText edtDescricao, edtData, edtHora, edtLocalizacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPublicarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // DATA
        foto = (Foto) getIntent().getSerializableExtra("foto");
        if (foto == null){
            foto = new Foto(0, "", "", LocalDateTime.now(), 0, 0);
        }

        // COMPONENTS
        toaster = new Toaster(this);
        edtDescricao = binding.edtDescricao;
        edtData = binding.edtData;


        edtHora = binding.edtHora;
        edtLocalizacao = binding.edtLocalizacao;

        edtData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        edtHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });



    }

    private void showDatePickerDialog(){
        LocalDateTime date = foto.getDataHora();
        int year = date.getYear();
        int month = date.getMonthValue() -1;
        int day = date.getDayOfMonth();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                PublicarActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        LocalDateTime newDate = date.withYear(year).withMonth(month + 1).withDayOfMonth(dayOfMonth);
                        updateDateTime(newDate);
                    }
                },
                year,
                month,
                day
        );

        datePickerDialog.show();

    }

    private void showTimePickerDialog(){
        LocalDateTime time = foto.getDataHora();
        int hour = time.getHour();
        int minute = time.getMinute();

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                PublicarActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                        LocalDateTime newTime = foto.getDataHora().withHour(hourOfDay).withMinute(minute);
                        updateDateTime(newTime);
                    }
                },
                hour,
                minute,
                true
        );

        timePickerDialog.show();
    }

    private void updateDateTime(LocalDateTime newTime){
        DateTimeFormatter dateformatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        edtData.setText(newTime.format(dateformatter));
        edtHora.setText(newTime.format(timeFormatter));
    }
}