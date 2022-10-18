package com.curvello.fabricio.tomaagua;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView tvRelogio;
    EditText etMensagem;
    Button btCriarAlarme;
    TimePickerDialog timePickerDialog;
    Calendar calendar;

    int hora;
    int minuto;
    String mensagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniciarComponentes();
    }

    private void iniciarComponentes() {
        tvRelogio = findViewById(R.id.tvRelogio);
        etMensagem = findViewById(R.id.etMensagem);
        btCriarAlarme = findViewById(R.id.btCriarAlarme);
    }

    //Aciona a View timePickerDialog para que o usuário defina a hora do alarme
    public void definirHoraAlarme(View view) {
        calendar = Calendar.getInstance();
        hora = calendar.get(Calendar.HOUR_OF_DAY);
        minuto = calendar.get(Calendar.MINUTE);

        timePickerDialog = new TimePickerDialog(MainActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int horaDoDia, int minutos) {
                String h = String.format("%02d", horaDoDia);
                String m = String.format("%02d", minutos);
                hora = Integer.parseInt(h);
                minuto = Integer.parseInt(m);
                tvRelogio.setText(h + ":" + m);
            }
        }, hora, minuto, true);
        timePickerDialog.show();
    }

    /*
      ATENÇÃO !!!
      NÃO ESQUECER DE INCLUIR A PERMISSÃO NO ANDROID MANIFEST:
      ANTES DE <application !!!

      <uses-permission android:name="com.android.alarm.permission.SET_ALARM" />

      VALIDAR SE A PERMMISÃO É EXATAMENTE ESSA. É COMUM AO DIGITAR O AS SUGERIR OUTRA PARECIDA.
    */


    public void criarAlarme( View view ) {
        minimizaTeclado(btCriarAlarme);

        if(!etMensagem.getText().toString().isEmpty()) {
            mensagem = etMensagem.getText().toString();
        } else {
            mensagem = "!!! TOMA ÁGUA !!!";
        }

        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        intent.putExtra(AlarmClock.EXTRA_HOUR, hora);
        intent.putExtra(AlarmClock.EXTRA_MINUTES, minuto);
        intent.putExtra(AlarmClock.EXTRA_MESSAGE, mensagem);

        //Validação se existe app de alarme no celular
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, "Não existe app de alarme neste aparelho de celular", Toast.LENGTH_SHORT).show();
        }

    }

    public void minimizaTeclado(Button button){
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(button.getWindowToken(), 0);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}