package com.project.e_room;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PinjamRuangActivity extends AppCompatActivity {
    public static final String URL = "https://eroom-pnm.000webhostapp.com/eroom_client/";
    private ProgressDialog progress;
    private Calendar calendar = Calendar.getInstance();

    @BindView(R.id.layoutPinjamRuangActivity) LinearLayout linearLayoutPinjamRuangActivity;
    @BindView(R.id.editTextRuang) EditText editTextRuang;
    @BindView(R.id.editTextTanggal) EditText editTextTanggal;
    @BindView(R.id.editTextJamAwal) EditText editTextJamAwal;
    @BindView(R.id.editTextJamAkhir) EditText editTextJamAkhir;
    @BindView(R.id.editTextNamaPeminjam) EditText editTextNamaPeminjam;
    @BindView(R.id.editTextKeperluan) EditText editTextKeperluan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinjam_ruang);
        ButterKnife.bind(this);

        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_NO:
                setTheme(R.style.AppTheme);
                break;

            case Configuration.UI_MODE_NIGHT_YES:
                setTheme(R.style.AppThemeDark);
                linearLayoutPinjamRuangActivity.setBackgroundResource(R.drawable.back_dark);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(Color.parseColor("#000000"));
                }
                break;
        }
    }

    @OnClick(R.id.editTextTanggal)
    public void tampilTanggal() {
        new DatePickerDialog(PinjamRuangActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String formatTanggal = "dd-MM-yyyy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatTanggal);
                editTextTanggal.setText(simpleDateFormat.format(calendar.getTime()));
            }
        },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @OnClick(R.id.editTextJamAwal)
    public void tampilJamAwal() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(PinjamRuangActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                editTextJamAwal.setText(String.format("%02d:%02d", hourOfDay, minute));

            }
        },
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(PinjamRuangActivity.this));
        timePickerDialog.show();
    }

    @OnClick(R.id.editTextJamAkhir)
    public void tampilJamAkhir() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(PinjamRuangActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                editTextJamAkhir.setText(String.format("%02d:%02d", hourOfDay, minute));

            }
        },
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(PinjamRuangActivity.this));
        timePickerDialog.show();
    }

    @OnClick(R.id.buttonSubmit)
    public void submit() {
        String namaPeminjam = editTextNamaPeminjam.getText().toString();
        String ruang = editTextRuang.getText().toString();
        String tanggal = editTextTanggal.getText().toString();
        String jamAwal = editTextJamAwal.getText().toString();
        String jamAkhir = editTextJamAkhir.getText().toString();
        String keperluan = editTextKeperluan.getText().toString();

        if (ruang.isEmpty()) {
            editTextRuang.setError("Ruang harus diisi.");
        } else if (tanggal.isEmpty()) {
            editTextTanggal.setError("Tanggal harus diisi.");
        } else if (jamAwal.isEmpty()) {
            editTextJamAwal.setError("Jam awal harus diisi.");
        } else if (jamAkhir.isEmpty()) {
            editTextJamAkhir.setError("Jam akhir harus diisi.");
        } else if (namaPeminjam.isEmpty()) {
            editTextNamaPeminjam.setError("Nama peminjam harus diisi.");
        } else if (keperluan.isEmpty()) {
            editTextKeperluan.setError("Keperluan harus diisi.");
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Konfirmasi");
            builder.setMessage("Anda yakin ingin meminjam ruang?");

            builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    progress = new ProgressDialog(PinjamRuangActivity.this);
                    progress.setCancelable(false);
                    progress.setMessage("Tunggu sebentar...");
                    progress.show();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    RegisterAPI api = retrofit.create(RegisterAPI.class);
                    Call<Value> call = api.daftar(namaPeminjam, ruang, tanggal, jamAwal, jamAkhir, keperluan);
                    call.enqueue(new Callback<Value>() {
                        @Override
                        public void onResponse(Call<Value> call, Response<Value> response) {
                            String value = response.body().getValue();
                            String message = response.body().getMessage();
                            progress.dismiss();
                            if (value.equals("1")) {
                                Toast.makeText(PinjamRuangActivity.this, message, Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(PinjamRuangActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Value> call, Throwable t) {
                            progress.dismiss();
                            Toast.makeText(PinjamRuangActivity.this, "Kesalahan jaringan.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}
