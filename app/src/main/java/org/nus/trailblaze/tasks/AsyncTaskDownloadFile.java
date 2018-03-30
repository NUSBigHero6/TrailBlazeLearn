package org.nus.trailblaze.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by wengweichen on 25/3/18.
 */

public class AsyncTaskDownloadFile extends AsyncTask<String, String, InputStream> {

    private ProgressDialog dialog;
    private PDFView pdfView;

    public AsyncTaskDownloadFile(PDFView pdfView, ProgressDialog dialog) {
        this.pdfView = pdfView;
        this.pdfView.setVisibility(View.VISIBLE);
        this.dialog = dialog;
    }

    /**
     * Downloading file in background thread
     * */
    @Override
    protected InputStream doInBackground(String... f_url) {
        int count;
        try {
            Log.d("File url", f_url[0]);
            URL url = new URL(f_url[0]);
            URLConnection conection = url.openConnection();
            conection.connect();

            // this will be useful so that you can show a tipical 0-100%
            // progress bar
            int lenghtOfFile = conection.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(url.openStream(),
                    8192);


            input.close();

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

        return null;
    }

    /**
     * Updating progress bar
     * */
    protected void onProgressUpdate(String... progress) {
        // setting progress percentage
        dialog.setProgress(Integer.parseInt(progress[0]));
    }

    /**
     * After completing background task Dismiss the progress dialog
     * **/
    @Override
    protected void onPostExecute(InputStream inputStream) {
        // dismiss the dialog after the file was downloaded
        dialog.dismiss();
        pdfView.fromStream(inputStream).load();
        pdfView.setVisibility(View.VISIBLE);
    }
}