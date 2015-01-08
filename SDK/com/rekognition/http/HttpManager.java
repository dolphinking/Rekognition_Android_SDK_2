package com.rekognition.http;

import android.graphics.Bitmap;
import android.util.Log;

import com.rekognition.http.model.HttpParameter;
import com.rekognition.http.model.RekognitionAPIException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

public class HttpManager {

    private static HttpManager instance = null;      

    private static final Logger logger = Logger.getLogger(HttpManager.class.getName());

    private static final String DEFAULT_CHARSET = "UTF-8";

    private static final int OK_RESPONSE_STATUS_CODE = 200;

    public static synchronized HttpManager getInstance() {
        if (instance == null) {
            instance = new HttpManager();
        }
        return instance;
    }

    private HttpManager() {
    }
    
    private HttpURLConnection getURLConnectionForURL(URL urlObj) throws IOException{
        URLConnection urlConnection;        
        urlConnection = urlObj.openConnection();
        
        if ( urlConnection instanceof HttpURLConnection) {
            return (HttpURLConnection) urlConnection;
        }
        else {
            Log.e("HttpManager", "URL is not http or https");
            return null;
        }
        
    }

    public String get(String url, Collection<HttpParameter> params) throws RekognitionAPIException {
        if (params != null && !params.isEmpty()) {
            String encodedParams = encodeParameters(params);
            url = url + "?" + encodedParams;
        }
        String sResponse = null;
        try {
            URL urlObj = new URL(url);
            HttpURLConnection urlConnection = getURLConnectionForURL(urlObj);                         
            if (urlConnection == null) {
                throw new RekognitionAPIException("URL is not http or https");
            }
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                int responseCode = urlConnection.getResponseCode();
                if (responseCode != OK_RESPONSE_STATUS_CODE) {
                    throw new RekognitionAPIException("Un-acceptable response code from server", responseCode);
                }
                sResponse = readStream(in);
            }
            finally {
              urlConnection.disconnect();
            }
        }
        catch(MalformedURLException ex){
            logger.log(Level.WARNING, "Malformed URL", ex);
            throw new RekognitionAPIException(ex);          
        }
        catch(IOException ex){
            logger.log(Level.WARNING, "Error when access remote host", ex);
            throw new RekognitionAPIException(ex);          
        } 
        return sResponse;
    }

    
    public String get(String url) throws RekognitionAPIException {
        return get(url, null);
    }

    public String post(String postUrl, Collection<HttpParameter> params) throws RekognitionAPIException {
        if (logger.isLoggable(Level.ALL)) {
            logger.log(Level.WARNING, "Got a POST request to " + postUrl);
        }
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        DataInputStream inStream = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";        
        String boundary =  "****";
        String response_data = null;                             
        try{
            //------------------ CLIENT REQUEST          
            // open a URL connection to the Servlet
            URL url = new URL(postUrl);
            conn = getURLConnectionForURL(url);            
            if (conn == null) {
                throw new RekognitionAPIException("URL is not http or https");
            }
            // Allow Inputs
            conn.setDoInput(true);
            // Allow Outputs
            conn.setDoOutput(true);
            // Don't use a cached copy.
            conn.setUseCaches(false);
            // Use a post method.
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary="+boundary);
            dos = new DataOutputStream( conn.getOutputStream() );
            for (HttpParameter param:params) {
                String value = param.getValue();
                dos.writeBytes(twoHyphens + boundary  + lineEnd);                
                if (value != null) {
                    dos.writeBytes("Content-Disposition: form-data; name=\"" + param.getName() + "\"");
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(value);                    
                }
                else if (param.getBitmap() != null){                    
                    dos.writeBytes("Content-Disposition: form-data; name=\"" + param.getName() + "\"; filename=\"file1.jpg\"" + lineEnd);                    
                    dos.writeBytes("Content-Type: image/jpeg" + lineEnd);                    
                    dos.writeBytes(lineEnd);
                    Bitmap bmp = param.getBitmap();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, dos); 
                }
                else {
                    Log.d(getClass().getSimpleName(), "Http parameter " + param.getName() + " field has no value and no bitmap");
                }
                dos.writeBytes(lineEnd);
            }
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            dos.flush();
            dos.close();
        }
        catch (MalformedURLException ex){
            Log.d(getClass().getSimpleName(), "error: " + ex.getMessage(), ex);
            throw new RekognitionAPIException(ex);
        }
        catch (IOException ioe){
            Log.d(getClass().getSimpleName(), "error: " + ioe.getMessage(), ioe);
            throw new RekognitionAPIException(ioe);
        }
        //------------------ read the SERVER RESPONSE
        try {            
            inStream = new DataInputStream ( conn.getInputStream() );
            int responseCode = conn.getResponseCode();
            if (responseCode != OK_RESPONSE_STATUS_CODE) {
                throw new RekognitionAPIException("Un-acceptable response code from server", responseCode);
            }
            response_data = readStream(inStream);
        }
        catch (IOException ioex){
            Log.e("Debug", "error: " + ioex.getMessage(), ioex);
            throw new RekognitionAPIException(ioex);
        }
        finally {
            conn.disconnect();
        }
       return response_data;
    }

    private String encodeParameters(Collection<HttpParameter> params) throws RekognitionAPIException {
        if (params == null) {
            return "";
        }
        StringBuilder buf = new StringBuilder();
        int j = 0;
        for (HttpParameter nvp : params) {
            if (j != 0) {
                buf.append("&");
            }
            j++;
            try {
                buf.append(URLEncoder.encode(nvp.getName(), DEFAULT_CHARSET)).append("=").append(URLEncoder.encode(nvp.getValue(), DEFAULT_CHARSET));
            } catch (UnsupportedEncodingException ex) {
                throw new RekognitionAPIException(ex);
            }
        }
        return buf.toString();
    }
    private String readStream(InputStream inputStream) throws RekognitionAPIException{
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        String result=null;
        try {
            StringBuilder total = new StringBuilder(inputStream.available());
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
            result = total.toString();
        }
        catch(IOException e){
            Log.e("Error", "Read stream error: " + e.toString());
            throw new RekognitionAPIException(e);
        }
        return result;
    }
}
