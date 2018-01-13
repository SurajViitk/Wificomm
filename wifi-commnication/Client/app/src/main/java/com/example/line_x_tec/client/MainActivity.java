package com.example.line_x_tec.client;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {
    TextView tVSMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tVSMsg=(TextView)findViewById(R.id.textView);
        tVSMsg.setMovementMethod(new ScrollingMovementMethod());
        ClientAsyncTask clientAST = new ClientAsyncTask();
        clientAST.execute(new String[]{"192.168.43.1", "8080", "Hello from client"});
    }
    class ClientAsyncTask extends AsyncTask<String, Void, String> {
        String result = null;
        protected String doInBackground(String... params) {

            while(true){


            try {
                Socket socket = new Socket(params[0],Integer.parseInt(params[1]));
                InputStream is = socket.getInputStream();
                PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
                out.println(params[2]);
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                result = br.readLine();
                publishProgress(null);
               // tVSMsg.setText(result);
                //socket.close();
            }
            catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                }
            //return result;
        }

        }
        @Override
        protected void onProgressUpdate(Void... values) {


            //Write server message to the text view
            tVSMsg.append(result+"\n");
        }

    }

}
