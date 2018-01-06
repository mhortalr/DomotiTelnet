package com.example.mariano.domotitelnet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        //   connect=(Button)findViewById(R.id.connect);
        //   titulo_persianas_dormitorio=(Button)findViewById(R.id.titulo_persianas_dormitorio);
        //   ip=(EditText)findViewById(R.id.ip);
        //   port=(EditText)findViewById(R.id.port);
/*        titulo_persianas_dormitorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PersianasDormitorioActivity.class);
                intent.putExtra("ip","mhortalr.ddns.net");
                intent.putExtra("port","723");
                startActivity(intent);
                finish();
            }
        });*/
    }

    public void startPersianas(View v) {
        Intent intent = new Intent(MainActivity.this, PersianasDormitorioActivity.class);
        intent.putExtra("ip", "mhortalr.ddns.net");
        intent.putExtra("port", "723");
        startActivity(intent);
        MainActivity.this.finish();
    }
    public void startToldo(View v) {
        Intent intent = new Intent(MainActivity.this, ToldoSalonActivity.class);
        intent.putExtra("ip", "mhortalr.ddns.net");
        intent.putExtra("port", "823");
//        intent.putExtra("ip", "192.168.1.38");
//        intent.putExtra("port", "23");
        startActivity(intent);
        MainActivity.this.finish();
    }

    public void acabar(View v) {
        MainActivity.this.finish();
    }

}