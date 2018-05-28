package info.dailytools.dlyoutube;

import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.os.Bundle;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.os.Environment;
import android.os.AsyncTask;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            init();
        }
        catch (Exception e) {
            int duration = Toast.LENGTH_LONG;
            CharSequence text = getString(R.string.msg_error_occurred) + ": " + e.getMessage();

            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
            toast.show();
        }

        /*if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState);
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    /*@Override
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
    }

    private void restoreInstanceState(Bundle savedInstanceState) {
    }*/

    protected void onDownloadClick(View v) {
        try {
            EditText txtUrl = findViewById(R.id.txt_url);
            Spinner spnFileType = findViewById(R.id.spn_file_type);
            Spinner spnQuality = findViewById(R.id.spn_quality);

            String url = txtUrl.getText().toString();
            String format = spnFileType.getSelectedItem().toString();
            String quality = spnQuality.getSelectedItem().toString();

            if (isDownloadLocationWritable()) {
                if (!url.equals("")) {

                    format = format.substring(0, 1).toLowerCase();

                    quality = quality.substring(0, 1).toLowerCase();
                    if (quality.equals("h")) {
                        quality = "b";
                    }
                    else {
                        quality = "w";
                    }

                    startDownload(url, format, quality);
                }
            }
            else {
                int duration = Toast.LENGTH_LONG;
                CharSequence text = getString(R.string.msg_error_location);

                Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                toast.show();
            }
        }
        catch (Exception e) {
            int duration = Toast.LENGTH_LONG;
            CharSequence text = getString(R.string.msg_error_occurred) + ": " + e.getMessage();

            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
            toast.show();
        }
    }

    private void init() {
        boolean enableDownload = isStoragePermissionGranted();

        setToolbar();
        setDownload(enableDownload);
        setUrl();
        setOption();

        getWindow().setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.tb_toolbar);
        setSupportActionBar(toolbar);
    }

    private void setDownload(boolean enableDownload) {
        isStoragePermissionGranted();

        Button btnDownload = findViewById(R.id.btn_download);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onDownloadClick(v);
            }
        });
        btnDownload.setEnabled(enableDownload);
    }

    private void setUrl() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        EditText txtUrl = findViewById(R.id.txt_url);
        txtUrl.setText("");

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (sharedText != null && !sharedText.equals("")) {
                    txtUrl.setText(sharedText);
                }
            }
        }
    }

    private void setOption() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        //file types
        String[] fileTypes = getResources().getStringArray(R.array.file_types);
        Spinner fileType = findViewById(R.id.spn_file_type);
        ArrayAdapter fileTypeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, fileTypes);
        fileTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fileType.setAdapter(fileTypeAdapter);

        String defaultFileType = preferences.getString("file_types", getString(R.string.default_file_type));
        int defaultFileTypePos = fileTypeAdapter.getPosition(defaultFileType);

        if (defaultFileTypePos != -1) {
            fileType.setSelection(defaultFileTypePos);
        }

        //quality
        String[] qualities = getResources().getStringArray(R.array.qualities);
        Spinner quality = findViewById(R.id.spn_quality);
        ArrayAdapter qualityAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, qualities);
        qualityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quality.setAdapter(qualityAdapter);

        String defaultQuality = preferences.getString("qualities", getString(R.string.default_quality));
        int defaultQualityPos = qualityAdapter.getPosition(defaultQuality);

        if (defaultQualityPos != -1) {
            quality.setSelection(defaultQualityPos);
        }
    }

    private void startDownload(String url, String format, String quality) {
        String convertUrlPattern = getString(R.string.convert_url_pattern);
        String requestUrl = String.format(convertUrlPattern, url, format, quality);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notify_download)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setProgress(100, 0, false);

        AsyncContentDownloader downloader = new AsyncContentDownloader();
        downloader.notificationBuilder = notificationBuilder;
        downloader.execute(requestUrl);

        ProgressBar pbLoad = findViewById(R.id.pb_loader);
        pbLoad.setVisibility(View.VISIBLE);

        Button btnDownload = findViewById(R.id.btn_download);
        btnDownload.setEnabled(false);
        btnDownload.setBackgroundColor(0xFFAAAAAA);
        btnDownload.setTextColor(0xFFCCCCCC);
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

        init();
    }

    private boolean isDownloadLocationWritable() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultDownloadLocation = preferences.getString("download_location", "");

        return isExternalStorageWritable(new File(defaultDownloadLocation));
    }

    class AsyncContentDownloader extends AsyncTask<String, Integer, String> {
        private final int CONNECT_TIMEOUT = 5000;
        private final int READ_TIMEOUT = 15000;
        private final int MAX_RETRY = 10;
        private final int WAIT = 500;
        private NotificationCompat.Builder notificationBuilder;

        @Override
        protected String doInBackground(String... path) {
            StringBuilder response = new StringBuilder();
            BufferedReader br = null;
            int i = 0;

            while (i < MAX_RETRY) {
                try {
                    URL url = new URL(path[0]);
                    URLConnection con = url.openConnection();
                    con.setConnectTimeout(CONNECT_TIMEOUT);
                    con.setReadTimeout(READ_TIMEOUT);

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
                    int duration = Toast.LENGTH_LONG;
                    CharSequence text = "";

                    AsyncFileDownloader downloader = new AsyncFileDownloader();
                    downloader.notificationBuilder = notificationBuilder;
                    downloader.title = title;
                    downloader.execute(url, ext);

                    text = getString(R.string.msg_task_added);

                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                    toast.show();
                }
            }
        }
    }

    class AsyncFileDownloader extends AsyncTask<String, Integer, Integer> {
        private final int CONNECT_TIMEOUT = 5000;
        private final int READ_TIMEOUT = 900000;
        private final int MAX_RETRY = 10;
        private final int WAIT = 500;
        private final int DELAY = 1000;
        private NotificationCompat.Builder notificationBuilder;
        Integer lengthOfFile;
        String title;
        int notifyId = (int)(new Date().getTime());

        @Override
        protected Integer doInBackground(String... param) {
            Integer fileLength = 0;
            InputStream input = null;
            OutputStream output = null;
            int i = 0;

            String url = param[0];
            String ext = param[1];

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String defaultDownloadLocation = preferences.getString("download_location", getString(R.string.default_download_location));

            String path = defaultDownloadLocation + "/" + title + "." + ext;

            while (i < MAX_RETRY) {
                try {
                    File file = new File(path);

                    if (isExternalStorageWritable(file)) {
                        Boolean isAppend = false;
                        int total = 0;

                        URL u = new URL(url);
                        URLConnection con = u.openConnection();
                        con.setConnectTimeout(CONNECT_TIMEOUT);
                        con.setReadTimeout(READ_TIMEOUT);
                        con.connect();

                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        else {
                            total = (int)file.length();
                            con.setRequestProperty("Range", "bytes=" + total + "-");
                            isAppend = true;
                        }

                        // getting file length
                        lengthOfFile = con.getContentLength();

                        if (lengthOfFile > 0) {
                            // Output stream to write file
                            output = new FileOutputStream(file, isAppend);

                            // input stream to read file - with 8k buffer
                            input = con.getInputStream();

                            byte[] data = new byte[4096];
                            int count = 0;

                            while ((count = input.read(data)) != -1) {
                                total += count;

                                // publishing the progress....
                                // After this onProgressUpdate will be called
                                publishProgress((int) (total * 100 / lengthOfFile));

                                // writing data to file
                                output.write(data, 0, count);
                                output.flush();
                                fileLength = total;
                            }

                            output.close();
                            input.close();
                        }
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

            if (notificationBuilder != null) {
                notificationBuilder.setProgress(100, 0, false);
                notificationBuilder.setContentText(title);
                notificationBuilder.setContentTitle(getString(R.string.msg_download_starting));

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
                notificationManager.notify(notifyId, notificationBuilder.build());
            }

            ProgressBar pbLoad = findViewById(R.id.pb_loader);
            pbLoad.setVisibility(View.INVISIBLE);

            Button btnDownload = findViewById(R.id.btn_download);
            btnDownload.setEnabled(true);
            btnDownload.setBackgroundColor(0xFFCC0000);
            btnDownload.setTextColor(0xFFFFFFFF);
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);

            if (notificationBuilder != null) {
                notificationBuilder.setProgress(100, progress[0], false);
                notificationBuilder.setContentTitle(getString(R.string.msg_download_downloading) + " " + progress[0] + "%");

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
                notificationManager.notify(notifyId, notificationBuilder.build());
            }
        }

        @Override
        protected void onPostExecute(Integer fileLength) {
            super.onPostExecute(fileLength);

            if (notificationBuilder != null) {
                if (fileLength.equals(lengthOfFile)) {
                    notificationBuilder.setProgress(0, 0, false);
                    notificationBuilder.setContentTitle(getString(R.string.msg_download_completed));
                }
                else {
                    notificationBuilder.setContentTitle(getString(R.string.msg_error_occurred));
                }

                notificationBuilder.setOngoing(false);

                try {
                    Thread.sleep(DELAY);
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
                    notificationManager.notify(notifyId, notificationBuilder.build());
                }
                catch (Exception e) {
                }
            }
        }
    }
}