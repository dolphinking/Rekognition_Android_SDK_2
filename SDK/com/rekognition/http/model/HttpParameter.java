package com.rekognition.http.model;

import android.graphics.Bitmap;
import android.util.Base64;


public class HttpParameter {

    private final String name;
    private final String value;
    private final BitmapField bitmapField;

    public HttpParameter(String name, String value) {
        this.name = name;
        this.value = value;
        this.bitmapField = null;
    }

    public HttpParameter(String name, double value) {
        this.name = name;
        this.value = String.valueOf(value);
        this.bitmapField = null;
    }

    public HttpParameter(String name, int value) {
        this.name = name;
        this.value = String.valueOf(value);
        this.bitmapField = null;
    }

    public HttpParameter(String name, long value) {
        this.name = name;
        this.value = String.valueOf(value);
        this.bitmapField = null;
    }

    public HttpParameter(String name, Base64Field base64Field) {
        this.name = name;
        this.value = base64Field.getBase64EncodedValue();
        this.bitmapField = null;
    }
    
    public HttpParameter(String name, BitmapField bmpField) {
        this.name = name;
        this.value = null;
        this.bitmapField = bmpField;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }
    
    public Bitmap getBitmap() {
        return this.bitmapField.getBitmap();
    }

    public static class Base64Field {

        private final String plainText;
        private final String base64EncodedValue;

        public Base64Field(byte[] bytes) {
            this.base64EncodedValue = Base64.encodeToString(bytes, Base64.DEFAULT);
            this.plainText = new String(bytes);
        }

        public Base64Field(String plainText) {
            this.plainText = plainText;
            byte[] bytes = plainText.getBytes();
            this.base64EncodedValue = Base64.encodeToString(bytes, Base64.DEFAULT);
        }

        /**
         * @return the plainText
         */
        public String getPlainText() {
            return plainText;
        }

        /**
         * @return the base64EncodedValue
         */
        public String getBase64EncodedValue() {
            return base64EncodedValue;
        }
    }    

    public static class BitmapField {
            
        private final Bitmap bitmap;
        
        public BitmapField(Bitmap bmp) {            
            bitmap = bmp;
        }
        
        public Bitmap getBitmap() {
            return bitmap;
        }       
    }
}
