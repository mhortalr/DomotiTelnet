package com.example.mariano.domotitelnet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mariano on 4/01/18.
 */

public class PersianasDormitorioActivity extends AppCompatActivity {
    private Socket client;
    private Writer writer;
    private BufferedReader reader;
    private static final int GETDATA=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persianas_dormitorio);
        Intent intent=getIntent();
        String ip=intent.getStringExtra("ip");
        String port=intent.getStringExtra("port");
        init(ip,port);
        Toast.makeText(PersianasDormitorioActivity.this,"connetto", Toast.LENGTH_SHORT).show();

        Button bajar_derecha=(Button)findViewById(R.id.bajar_derecha);
        Button subir_derecha=(Button)findViewById(R.id.subir_derecha);
        Button bajar_izquierda=(Button)findViewById(R.id.bajar_izquierda);
        Button subir_izquierda=(Button)findViewById(R.id.subir_izquierda);
//        Button salir=(Button)findViewById(R.id.salir);

        if (bajar_derecha != null) {
            bajar_derecha.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str_content="d";
                    Log.d("telnet",str_content);
                    try {
                        sendToServer(str_content.trim());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        if (subir_derecha != null) {
            subir_derecha.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str_content="c";
                    Log.d("telnet",str_content);
                    try {
                        sendToServer(str_content.trim());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        if (bajar_izquierda != null) {
            bajar_izquierda.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str_content="b";
                    Log.d("telnet",str_content);
                    try {
                        sendToServer(str_content.trim());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        if (subir_izquierda != null) {
            subir_izquierda.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str_content="a";
                    Log.d("telnet",str_content);
                    try {
                        sendToServer(str_content.trim());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
      /*  if (salir != null) {
            salir.setOnClickListener(new View.OnClickListener() {
             @Override
                public void onClick(View v) {
                 try {
                     client.close();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
                 Intent intent = new Intent(PersianasDormitorioActivity.this, MainActivity.class);
                 startActivity(intent);
             }
            });
        }*/
    }
    public void paginaPrincipal(View v) throws IOException {
        client.close();
        Intent intent = new Intent(PersianasDormitorioActivity.this, com.example.mariano.domotitelnet.MainActivity.class);
        startActivity(intent);
        PersianasDormitorioActivity.this.finish();
    }

    private String Myfilter(String data){
        int xx=27;
        char tt=(char )xx;
        String seg=tt+"[\\s\\S].*?m";
        Pattern pattern= Pattern.compile(seg);
        Matcher matcher=pattern.matcher(data);
        return  matcher.replaceAll("");

    }
    private void init(final String ip, final String port) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    client = new Socket(ip, Integer.parseInt(port));
                    client.setKeepAlive(true);
                    client.setSoTimeout(1000);
                    Log.d("telnet", "client");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), "gbk"));
                    reader = new BufferedReader(new InputStreamReader(client.getInputStream(), "gbk"));
                    new Thread(new Read(reader)).start();
                    Log.d("telnet", "tt");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    class Read implements  Runnable{
        private BufferedReader reader;
        StringBuilder builder;
        public Read(BufferedReader reader){
            Log.d("telnet","read");
            this.reader=reader;
            builder=new StringBuilder();
        }
        @Override
        public void run() {
            while(true){
                builder.delete(0,builder.length());
                String con;
                try {
                    if(reader.ready())
                        while((con=reader.readLine())!=null){
                            builder.append(con);
                            builder.append("\r\n");
                        }
                    Log.d("telnet","msg");

                } catch (IOException e) {
                    // e.printStackTrace();
                    Log.d("telnet",builder.toString());
                    if(!builder.equals("")){
                        Message message=handler.obtainMessage();
                        message.what=GETDATA;
                        String data=Myfilter(builder.toString());
                        message.obj=data;
                        handler.sendMessage(message);
                    }
                }
            }
        }
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GETDATA:
                    String str=(String )msg.obj;
                    break;
            }
        }
    };

    private  void sendToServer(final String content) throws IOException {
        Log.d("write",content);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    writer.write(content);
                    writer.write("\r\n");
                    writer.write("\r\n");
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();// writer.write(content);
        //writer.flush();
    }
}