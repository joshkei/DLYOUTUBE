package co.dlyoutube;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.os.Environment;
import android.os.AsyncTask;
import android.view.View;

import java.io.File;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private final String CONVERT_URL = "http://www.dailytools.info/tools/convertyoutube";
    private final String CONVERT_URL_PATTERN = "%s/%s?url=%s&ext=%s&quality=%s";
    private final String CONVERT_URL_PARAM = "getvideoinfo";
    public final int FIRST_DOWNLOAD_MARGIN = 900;
    public final int DOWNLOAD_MARGIN = 200;
    public final int DOWNLOAD_BAR_MARGIN = 100;
    public final int DOWNLOAD_STATUS_MARGIN = 150;
    public ArrayList<String> downloadList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init(isStoragePermissionGranted());

/*        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState);
        }*/
    }

/*    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        saveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        restoreInstanceState(savedInstanceState);
    }

    private void saveInstanceState(Bundle savedInstanceState) {
        EditText txtUrl = (EditText)findViewById(R.id.txt_url);
        Spinner spnFileType = (Spinner)findViewById(R.id.spn_file_type);
        Spinner spnQuality = (Spinner)findViewById(R.id.spn_quality);
        TextView txtResult = (TextView) findViewById(R.id.txt_result);

        String url = txtUrl.getText().toString();
        String format = spnFileType.getSelectedItem().toString();
        String quality = spnQuality.getSelectedItem().toString();
        String result = txtResult.getText().toString();

        savedInstanceState.putString("URL", url);
        savedInstanceState.putString("FORMAT", format);
        savedInstanceState.putString("QUALITY", quality);
        savedInstanceState.putString("RESULT", result);
    }

    private void restoreInstanceState(Bundle savedInstanceState) {
        String url = savedInstanceState.getString("URL");
        String format = savedInstanceState.getString("FORMAT");
        String quality = savedInstanceState.getString("QUALITY");
        String result = savedInstanceState.getString("RESULT");

        EditText txtUrl = (EditText)findViewById(R.id.txt_url);
        Spinner spnFileType = (Spinner)findViewById(R.id.spn_file_type);
        Spinner spnQuality = (Spinner)findViewById(R.id.spn_quality);
        TextView txtResult = (TextView) findViewById(R.id.txt_result);

        txtUrl.setText(url);
        spnFileType.setSelection(((ArrayAdapter)spnFileType.getAdapter()).getPosition(format));
        spnQuality.setSelection(((ArrayAdapter)spnQuality.getAdapter()).getPosition(quality));
        txtResult.setText(result);
    }*/

    protected void onDownloadClick(View v) {
        try {
            EditText txtUrl = (EditText)findViewById(R.id.txt_url);
            Spinner spnFileType = (Spinner)findViewById(R.id.spn_file_type);
            Spinner spnQuality = (Spinner)findViewById(R.id.spn_quality);

            String url = txtUrl.getText().toString();
            String format = spnFileType.getSelectedItem().toString();
            String quality = spnQuality.getSelectedItem().toString();

            if (!url.equals("")) {
                format = format.substring(0, 1).toLowerCase();
                quality = quality.substring(0, 1).toLowerCase();

                startDownload(url, format, quality);
            }
        }
        catch (Exception e) {
        }
    }

    private void init(boolean enableDownload) {

        try {
            Button btnDownload = (Button)findViewById(R.id.btn_download);
            btnDownload.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onDownloadClick(v);
                }
            });
            btnDownload.setEnabled(enableDownload);

            setUrl();
            setOption();
        }
        catch (Exception e) {
        }
    }

    private void setUrl() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (sharedText != null && !sharedText.equals("")) {
                    EditText txtUrl = (EditText)findViewById(R.id.txt_url);
                    txtUrl.setText(sharedText);
                }
            }
        }
    }

    private void setOption() {
        //file types
        ArrayList<String> fileTypes = new ArrayList<String>();
        fileTypes.add("Video");
        fileTypes.add("Audio");

        Spinner fileType = (Spinner)findViewById(R.id.spn_file_type);
        ArrayAdapter fileTypeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, fileTypes);
        fileType.setAdapter(fileTypeAdapter);

        //quality
        ArrayList<String> qualities = new ArrayList<String>();
        qualities.add("Best");
        qualities.add("Worst");

        Spinner quality = (Spinner)findViewById(R.id.spn_quality);
        ArrayAdapter qualityAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, qualities);
        quality.setAdapter(qualityAdapter);
    }

    private void startDownload(String url, String format, String quality) {
        String requestUrl = String.format(CONVERT_URL_PATTERN, CONVERT_URL, CONVERT_URL_PARAM, url, format, quality);

        AsyncContentDownloader downloader = new AsyncContentDownloader();
        downloader.execute(requestUrl);

        Button btnDownload = (Button)findViewById(R.id.btn_download);
        btnDownload.setEnabled(false);
    }

    private RelativeLayout addDownload(String title) {
        ConstraintLayout clHome = (ConstraintLayout)findViewById(R.id.cl_home);
        int count = downloadList.size();

        TextView tvTitle = new TextView(this);
        tvTitle.setText(title);
        tvTitle.setTextSize(18);
        tvTitle.setTag("title");
        RelativeLayout.LayoutParams lpTitle = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lpTitle.setMargins(0, count * DOWNLOAD_MARGIN, 0, 0);

        ProgressBar pbDownload = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        pbDownload.setTag("bar");
        RelativeLayout.LayoutParams lpBar = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        pbDownload.setMinimumHeight(80);
        lpBar.setMargins(0, (count * DOWNLOAD_MARGIN) + DOWNLOAD_BAR_MARGIN, 0, 0);

        TextView tvStatus = new TextView(this);
        tvStatus.setTextSize(14);
        tvStatus.setTag("status");
        RelativeLayout.LayoutParams lpStatus = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lpStatus.setMargins(0, (count * DOWNLOAD_MARGIN) + DOWNLOAD_STATUS_MARGIN, 0, 0);

        RelativeLayout rlDownload = new RelativeLayout(this);
        rlDownload.setPadding(80, FIRST_DOWNLOAD_MARGIN, 80, 0);
        RelativeLayout.LayoutParams lpDownload = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        rlDownload.addView(tvTitle, lpTitle);
        rlDownload.addView(pbDownload, lpBar);
        rlDownload.addView(tvStatus, lpStatus);

        clHome.addView(rlDownload, lpDownload);

        return rlDownload;
    }

    private void removeDownload(RelativeLayout rlDownload) {
        ConstraintLayout clHome = (ConstraintLayout)findViewById(R.id.cl_home);
        clHome.removeView(rlDownload);
    }

    private boolean isExternalStorageWritable(File file) {
        String state = Environment.getExternalStorageState();

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            state = Environment.getExternalStorageState(file);
        }

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private boolean isExternalStorageReadable(File file) {
        String state = Environment.getExternalStorageState();

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            state = Environment.getExternalStorageState(file);
        }

        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    private boolean isStoragePermissionGranted() {
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                android.support.v4.app.ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean result = (grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED);
        init(result);
    }

    private byte[] readFile(String path) throws IOException {
        byte[] bytes = null;
        File file = new File(path);

        if (file.exists()) {
            int size = (int) file.length();
            bytes = new byte[size];
            BufferedInputStream buf = null;

            try {
                buf = new BufferedInputStream(new FileInputStream(file));
                buf.read(bytes, 0, bytes.length);
            } catch (IOException ex) {
                throw ex;
            } finally {
            }
            if (buf != null) {
                buf.close();
            }
        }

        return bytes;
    }

    class AsyncContentDownloader extends AsyncTask<String, Integer, String> {
        private final int TIMEOUT = 5000;
        private final int MAX_RETRY = 10;
        private final int WAIT = 500;

        @Override
        protected String doInBackground(String... path) {
            StringBuilder response = new StringBuilder();
            BufferedReader br = null;
            int i = 0;

            while (i < MAX_RETRY) {
                try {
                    URL url = new URL(path[0]);
                    URLConnection con = url.openConnection();
                    con.setConnectTimeout(TIMEOUT);
                    con.setReadTimeout(TIMEOUT);
                    con.connect();

                    // open the stream and put it into BufferedReader
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String inputLine;
                    while ((inputLine = br.readLine()) != null) {
                        response.append(inputLine);
                    }

                    br.close();
                    break;
                } catch (Exception e) {
                    try {
                        Thread.sleep(WAIT);
                    }
                    catch (Exception ex) {}
                    i++;
                } finally {
                    try {
                        br.close();
                    }
                    catch (Exception e) {
                    }
                }
            }

            return response.toString();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String content) {
            super.onPostExecute(content);

            String url = "";
            String title = "";
            String ext = "";

            if (!content.equals("")) {
                try {
                    JSONObject json = new JSONObject(content);
                    url = json.getString("url");
                    title = json.getString("title");
                    ext = json.getString("ext");
                }
                catch (JSONException ex) {
                }

                if (!url.equals("") && !title.equals("") && !ext.equals("")) {
                    //if (!downloadList.contains(url)) {
                        AsyncFileDownloader downloader = new AsyncFileDownloader();
                        downloader.url = url;
                        downloader.title = title;
                        downloader.ext = ext;
                        downloader.execute(url, title, ext);

                        Button btnDownload = (Button)findViewById(R.id.btn_download);
                        btnDownload.setEnabled(true);
                    //}
                    //else {
                        //already downloading
                    //}
                }
            }

            if (content.equals("") || url.equals("") || title.equals("") || ext.equals("")) {

            }
        }
    }

    class AsyncFileDownloader extends AsyncTask<String, Integer, Long> {
        private final int TIMEOUT = 5000;
        private final int MAX_RETRY = 10;
        private final int WAIT = 500;
        private final int COMPLETED_DELAY = 2000;
        private final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOWNLOADS;
        private String url;
        private String title;
        private String ext;
        private RelativeLayout rlDownload;
        int lenghtOfFile;

        @Override
        protected Long doInBackground(String... param) {
            Long fileLength = 0L;
            int count;
            InputStream input = null;
            OutputStream output = null;
            long total = 0;
            byte data[] = null;
            int i = 0;

            url = param[0];
            title = param[1];
            ext = param[2];

            String path = DOWNLOAD_PATH + "/" + title + "." + ext;

            while (i < MAX_RETRY) {
                try {
                    URL u = new URL(url);
                    URLConnection con = u.openConnection();
                    con.setConnectTimeout(TIMEOUT);
                    con.setReadTimeout(TIMEOUT);
                    con.connect();
                    // getting file length
                    lenghtOfFile = con.getContentLength();

                    // input stream to read file - with 8k buffer
                    input = new BufferedInputStream(u.openStream());
                    File file = new File(path);

                    if (isExternalStorageWritable(file)) {
                        file.createNewFile();
                        // Output stream to write file
                        output = new FileOutputStream(file, false);

                        data = new byte[1024];

                        while ((count = input.read(data)) != -1) {
                            total += count;
                            // publishing the progress....
                            // After this onProgressUpdate will be called
                            publishProgress((int)(total * 100 / lenghtOfFile));

                            // writing data to file
                            output.write(data, 0, count);
                        }

                        output.flush();
                        fileLength = file.length();
                    }

                    break;
                } catch (Exception e) {
                    try {
                        Thread.sleep(WAIT);
                    }
                    catch (Exception ex) {
                    }
                    i++;
                } finally {
                    try {
                        output.close();
                        input.close();
                    }
                    catch (Exception e) {
                    }
                }
            }

            return fileLength;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            rlDownload = addDownload(title);
//            int count = rlDownload.getChildCount();
//
//            for (int i = 0; i < count; i++) {
//                View v = rlDownload.getChildAt(i);
//                if (v instanceof ProgressBar) {
//                    ((ProgressBar)v).setProgress(0);
//                }
//                else if (v instanceof TextView) {
//                    ((TextView)v).setText("Starting");
//                }
//            }
            ProgressBar bar = (ProgressBar)rlDownload.findViewWithTag("bar");
            bar.setProgress(0);

            TextView status = (TextView)rlDownload.findViewWithTag("status");
            status.setText("Starting");

            downloadList.add(url);
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);

            if (rlDownload != null) {
//                int count = rlDownload.getChildCount();
//
//                for (int i = 0; i < count; i++) {
//                    View v = rlDownload.getChildAt(i);
//                    if (v instanceof ProgressBar) {
//                        ((ProgressBar)v).setProgress(progress[0]);
//                    }
//                    else if (v instanceof TextView) {
//                        ((TextView)v).setText(String.format("%s d%%%", "Downloading", progress[0]));
//                    }
//                }
                ProgressBar bar = (ProgressBar)rlDownload.findViewWithTag("bar");
                bar.setProgress(progress[0]);

                TextView status = (TextView)rlDownload.findViewWithTag("status");
                status.setText(String.format("%s %d%%", "Downloading", progress[0]));
            }
        }

        @Override
        protected void onPostExecute(Long fileLength) {
            super.onPostExecute(fileLength);

            if (rlDownload != null) {
                if (fileLength > 0 && fileLength == lenghtOfFile) {
//                    int count = rlDownload.getChildCount();
//
//                    for (int i = 0; i < count; i++) {
//                        View v = rlDownload.getChildAt(i);
//                        if (v instanceof TextView) {
//                            ((TextView)v).setText("Completed");
//                        }
//                    }
                    TextView status = (TextView)rlDownload.findViewWithTag("status");
                    status.setText("Completed");

                    try {
                        Thread.sleep(COMPLETED_DELAY);
                    }
                    catch (Exception ex) {
                    }

                    removeDownload(rlDownload);
                    downloadList.remove(url);
                } else {

                }
            }
        }
    }
}