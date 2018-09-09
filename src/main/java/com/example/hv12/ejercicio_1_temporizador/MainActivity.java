package com.example.hv12.ejercicio_1_temporizador;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    TextView lblContador;
    Button btniIniciar;
    ProgressBar Progress;
    ContadorAsincrono contador;
    EditText edtTiempo;
    int tiempo=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lblContador = findViewById(R.id.lblContador);
        btniIniciar = findViewById(R.id.btnIniciar);
        edtTiempo=findViewById(R.id.editTiempo);
        Progress=findViewById(R.id.Progress);
        btniIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarContador(tiempo);
                edtTiempo.setEnabled(false);
            }
        });
        edtTiempo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    lblContador.setText(edtTiempo.getText()+" seg");
                    tiempo=Integer.parseInt(edtTiempo.getText().toString());
                }
            }
        });
        edtTiempo.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    lblContador.setText(edtTiempo.getText()+" seg");
                    tiempo=Integer.parseInt(edtTiempo.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    private void iniciarContador(int i){
        /**si es primera vez -> se inicia **/
        if(contador==null){
            contador = new ContadorAsincrono(this,lblContador, btniIniciar, Progress, tiempo, edtTiempo);
            contador.execute(i);
            /**si ha terminado de ejecutar el hilo -> se crea otro hilo **/
        }else if(contador.getStatus()==AsyncTask.Status.FINISHED){
            contador = new ContadorAsincrono(this,lblContador,btniIniciar, Progress, tiempo, edtTiempo);
            contador.execute(i);
            /** si esta ejecutado y no esta pausado -> entonces se pausa**/
        }else if(contador.getStatus()== AsyncTask.Status.RUNNING && !contador.esPausa()  ){
            contador.pausarContador();
            /** si no entro en las condiciones anteriores por defecto esta pausado -> se reanuda*/
        }else{
            contador.reanudarContador();
        }
        boolean estado=contador.esPausa();
        if(estado){
            btniIniciar.setText("Reanudar");
        }else{
            btniIniciar.setText("Pausa");
        }
    }
}
