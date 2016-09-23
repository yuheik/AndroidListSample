package com.sample.myapplication.Utils;

import android.os.AsyncTask;
import android.support.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtil {

    public interface RequestListener {
        void onResult(String result);
    }

    /**
     * Execute network request
     * @param urlString
     * @return result String. Null when failed.
     */
    @Nullable
    public static void request(final String urlString, final RequestListener requestListener) {
        new AsyncTask<Object, Object, String>() {
            @Override
            protected String doInBackground(Object... voids) {
                HttpURLConnection urlConnection;
                BufferedInputStream bufferedInputStream = null;
                try {
                    urlConnection = (HttpURLConnection) new URL(urlString).openConnection();
                    urlConnection.connect();
                    bufferedInputStream = new BufferedInputStream(urlConnection.getInputStream());
                } catch (IOException e) {
                    LogUtil.error("network error", e);
                    return null;
                }

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buff = new byte[1024];
                int length;
                try {
                    while ((length = bufferedInputStream.read(buff)) != -1) {
                        if (length > 0) {
                            byteArrayOutputStream.write(buff, 0, length);
                        }
                    }
                } catch (IOException e) {
                    LogUtil.error("stream read error", e);
                    return null;
                }

                return new String(byteArrayOutputStream.toByteArray());
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                requestListener.onResult(result);
            }
        }.execute();
    }
}
