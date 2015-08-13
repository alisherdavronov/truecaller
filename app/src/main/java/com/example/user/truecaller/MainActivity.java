package com.example.user.truecaller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String URL = " http://www.truecaller.com/support";

    private Button btn;
    private TextView out1;
    private TextView out2;
    private TextView out3;

    private boolean t1;
    private boolean t2;
    private boolean t3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.button);
        out1 = (TextView) findViewById(R.id.textView1);
        out2 = (TextView) findViewById(R.id.textView2);
        out3 = (TextView) findViewById(R.id.textView3);

        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            t1 = t2 = t3 = true;
            btn.setEnabled(false);

            new Truecaller10thCharacterRequest().execute(URL);
            new TruecallerEvery10thCharacterRequest().execute(URL);
            new TruecallerWordCounterRequest().execute(URL);
        } else {
            out1.setText("Truecaller10thCharacter error:\nNo network connection available.");
            out2.setText("TruecallerEvery10thCharacter error:\nNo network connection available.");
            out3.setText("TruecallerWordCounter error:\nNo network connection available.");
        }
    }


    private class Truecaller10thCharacterRequest extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            out1.setText("running ...");
        }

        @Override
        protected String doInBackground(String... urls) {

            try {
                InputStream is = null;

                try {
                    URL url = new URL(urls[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();
                    int response = conn.getResponseCode();
                    if (response != 200) return "error:\nHTTP Status Code is " + response;

                    is = conn.getInputStream();

                    Reader reader = new InputStreamReader(is, "UTF-8");

                    int ch = 0;
                    for (int i=0; i < 10; i++) {
                        ch = reader.read();
                        if (ch == -1) return "error:\nNot enough symbols";
                    }

                    return "10th character in the web page text is '" + (char)ch + "' with code " + ch;

                } finally {
                    if (is != null) {
                        is.close();
                    }
                }
            } catch (IOException e) {
                return "error:\nUnable to retrieve web page. URL may be invalid.";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            out1.setText(result);
            t1 = false;
            if (!t1 && !t2 && !t3 ) btn.setEnabled(true);
        }
    }



    private class TruecallerEvery10thCharacterRequest extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            out2.setText("running ...");
        }

        @Override
        protected String doInBackground(String... urls) {

            try {
                InputStream is = null;

                try {
                    URL url = new URL(urls[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();
                    int response = conn.getResponseCode();
                    if (response != 200) return "error:\nHTTP Status Code is " + response;

                    is = conn.getInputStream();

                    Reader reader = new InputStreamReader(is, "UTF-8");

                    StringBuilder sb = new StringBuilder();
                    sb.append("Every 10th character in the web page text is '");

                    int i = 0;
                    int j = 0;
                    int ch;
                    while ((ch = reader.read()) != -1 ) {
                        if (i > 0 && i % 10 == 0) {
                            sb.append((char) ch);
                            if (j > 0 && j % 10 == 0) sb.append(' ');
                            j++;
                        }
                        i++;
                    }
                    sb.append("'");
                    return sb.toString();

                } finally {
                    if (is != null) {
                        is.close();
                    }
                }
            } catch (IOException e) {
                return "error:\nUnable to retrieve web page. URL may be invalid.";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            out2.setText(result);
            t2 = false;
            if (!t1 && !t2 && !t3 ) btn.setEnabled(true);
        }
    }

    private class TruecallerWordCounterRequest extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            out3.setText("running ...");
        }

        @Override
        protected String doInBackground(String... urls) {

            try {
                InputStream is = null;

                try {
                    URL url = new URL(urls[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();
                    int response = conn.getResponseCode();
                    if (response != 200) return "error:\nHTTP Status Code is " + response;
                    is = conn.getInputStream();
                    Reader reader = new InputStreamReader(is, "UTF-8");
                    BufferedReader br = new BufferedReader(reader);

                    Map<String, Integer> wc = new TreeMap<>();
                    String line = null;
                    while( (line = br.readLine())!= null ){
                        String [] tokens = line.split("[\\s\\d~`!@#\\$%\\^&\\*\\(\\)\\-\\+\\[\\]\\{\\}\'\"\\\\|/\\?,\\.;:]+");
                        for (String token : tokens) {
                            if (token.equals("")) continue;
                            token = token.toLowerCase();
                            int n = wc.containsKey(token)? wc.get(token) : 0;
                            n++;
                            wc.put(token, n);
                        }
                    }

                    StringBuilder sb = new StringBuilder();
                    sb.append("result:\n");
                    for (Map.Entry<String, Integer> e : wc.entrySet()) {
                        sb.append(e.getKey())
                                .append(" : ")
                                .append(e.getValue())
                                .append("\n");
                    }

                    return sb.toString();
                } finally {
                    if (is != null) {
                        is.close();
                    }
                }
            } catch (IOException e) {
                return "error:\nUnable to retrieve web page. URL may be invalid.";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            out3.setText(result);
            t3 = false;
            if (!t1 && !t2 && !t3 ) btn.setEnabled(true);
        }
    }

}
