package com.example.nguyenhuutu.convenientmenu.helper;

import android.app.DownloadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class RequestServer {
    private String url;
    private String method;
    private JSONObject params;

    public RequestServer() {
        this.url = "";
        this.method = "GET";
        this.params = new JSONObject();
    }
    public RequestServer(String _url, String _method) {
        this.url = _url;
        this.method = _method;
        this.params = new JSONObject();
    }

    public void setUrl(String _url) {
        this.url = _url;
    }

    public void setMethod(String _method) {
        this.method = _method;
    }

    public void setParams(Map<String, Object> _params) throws JSONException{
        for (Map.Entry<String, Object> entry:
             _params.entrySet()) {
            this.params.accumulate(entry.getKey(), entry.getValue());
        }
    }

    public void setParam(String key, Object value) throws JSONException {
        this.params.accumulate(key.toString(), value.toString());
    }

    private String getParamsString() {
        return this.params.toString();
    }

    public String executeRequest() {
        StringBuilder result = new StringBuilder();

        try {
            URL page = new URL(this.url);

            HttpURLConnection conn = (HttpURLConnection) page.openConnection();
            conn.setRequestMethod(this.method);

            switch (this.method) {
                case "GET":
                    conn.setDoInput(true);
                    break;
                case "POST":
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                    OutputStream outputStream = conn.getOutputStream();
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                    BufferedWriter buffWriter = new BufferedWriter(outputStreamWriter);
                    buffWriter.write(this.getParamsString());
                    buffWriter.flush();
                    buffWriter.close();
                    outputStream.close();
                    break;
            }

            conn.connect();

            InputStreamReader in = new InputStreamReader((InputStream) conn.getContent());
            BufferedReader buff = new BufferedReader(in);
            String line;
            while ((line = buff.readLine()) != null) {
                result.append(line + "\n");
            }

            buff.close();
            in.close();
            if (conn != null) {
                conn.disconnect();
            }
        }
        catch(Exception ex) {
            result.append("Error: " + ex.toString());
        }

        return result.toString();
    }
}
