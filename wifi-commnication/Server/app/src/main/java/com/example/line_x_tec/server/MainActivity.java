package com.example.line_x_tec.server;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private final int SERVER_PORT = 8080; //Define the server port
    TextView tVCMsg;
    Button button_send;
    EditText editText;
    ServerSocket socServer;
    Socket socClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tVCMsg = (TextView) findViewById(R.id.textView);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Create a server socket object and bind it to a port
                    socServer = new ServerSocket(SERVER_PORT);
                    //Create server side client socket reference
                    socClient = null;
                    //Infinite loop will listen for client requests to connect
                    while (true) {
                        //Accept the client connection and hand over communication to server side client socket
                        socClient = socServer.accept();
                        //For each client new instance of AsyncTask will be created
                        ServerAsyncTask serverAsyncTask = new ServerAsyncTask();
                        //Start the AsyncTask execution
                        //Accepted client socket object will pass as the parameter
                        serverAsyncTask.execute(new Socket[]{socClient});
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        OnClickButtonClickListener();
    }

    class ServerAsyncTask extends AsyncTask<Socket, Void, String> {
        //Background task which serve for the client
        @Override
        protected String doInBackground(Socket... params) {
            String result = null;
            //Get the accepted socket object
            Socket mySocket = params[0];
            try {
                //Get the data input stream comming from the client
                InputStream is = mySocket.getInputStream();
                //Get the output stream to the client
                //PrintWriter out = new PrintWriter(
                //        mySocket.getOutputStream(), true);
                //Write data to the data output stream
               // out.println("Hello from server");
                //Buffer the data input stream
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(is));
                //Read the contents of the data buffer
                result = br.readLine();
                //Close the client connection
             //   mySocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            //After finishing the execution of background task data will be write the text view
            tVCMsg.setText(s);
        }
    }


    public void OnClickButtonClickListener() {
        button_send = (Button) findViewById(R.id.button1);
        editText = (EditText) findViewById(R.id.editText);
        button_send.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            PrintWriter out = new PrintWriter(socClient.getOutputStream(), true);
                            out.println(editText.getText().toString());
                        }
                        catch(IOException io){
                            io.printStackTrace();
                        }


                    }
                }
        );
    }
}