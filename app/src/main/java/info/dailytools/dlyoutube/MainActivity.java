package info.dailytools.dlyoutube;

import android.support.v7.app.AppCompatActivity;
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
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.app.DownloadManager;

import java.io.File;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
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
             Toast.makeText(getApplicationContext(), getString(R.string.msg_error_occurred) + ": " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        /*if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState);
        }*/
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        try {
            setUrl();
        }
        catch (Exception e) {
             Toast.makeText(getApplicationContext(), getString(R.string.msg_error_occurred) + ": " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // getIntent() should always return the most recent
        setIntent(intent);
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

/*    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onComplete);
    }*/

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

/*
    BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            */
/*long  refId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            DownloadManager downloadManager = (DownloadManager)getSystemService(getApplicationContext().DOWNLOAD_SERVICE);
            downloadManager.remove(refId);*//*


            Toast.makeText(getApplicationContext(), getText(R.string.msg_download_completed), Toast.LENGTH_LONG).show();
        }
    };
*/

    protected void onDownloadClick(View v) {
        try {
            EditText txtUrl = findViewById(R.id.txt_url);
            Spinner spnFileType = findViewById(R.id.spn_file_type);
            Spinner spnQuality = findViewById(R.id.spn_quality);

            String url = txtUrl.getText().toString();
            String format = spnFileType.getSelectedItem().toString();
            String quality = spnQuality.getSelectedItem().toString();

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String downloadLocation = preferences.getString("download_location", "");
            Boolean wifiOnly = preferences.getBoolean("wifi_only", true);

            if (isLocationWritable(downloadLocation)) {
                if (isNetworkAvailable(wifiOnly)) {
                    if (!url.equals("")) {

                        format = format.substring(0, 1).toLowerCase();

                        quality = quality.substring(0, 1).toLowerCase();
                        if (quality.equals("h")) {
                            quality = "b";
                        } else {
                            quality = "w";
                        }

                        startDownload(url, format, quality);
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), getString(R.string.msg_no_wifi), Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(getApplicationContext(), getString(R.string.msg_error_location), Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), getString(R.string.msg_error_occurred) + ": " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    protected void onViewFilesClick(View v) {
        try {
            Intent intent = new Intent();
            intent.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getString(R.string.msg_error_occurred) + ": " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void init() {
        boolean enableDownload = isStoragePermissionGranted();

        setToolbar();
        setButton(enableDownload);
        setOption();

        //registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.tb_toolbar);
        setSupportActionBar(toolbar);
    }

    private void setButton(boolean enableDownload) {
        isStoragePermissionGranted();

        Button btnDownload = findViewById(R.id.btn_download);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onDownloadClick(v);
            }
        });
        btnDownload.setEnabled(enableDownload);

        Button btnViewFiles = findViewById(R.id.btn_view_files);
        btnViewFiles.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onViewFilesClick(v);
            }
        });
    }

    private void setUrl() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        EditText txtUrl = findViewById(R.id.txt_url);

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

        AsyncContentDownloader downloader = new AsyncContentDownloader();
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

    private boolean isLocationWritable(String location) {
        return isExternalStorageWritable(new File(location));
    }

    private boolean isNetworkAvailable(Boolean wifiOnly) {
        return (wifiOnly && isWifiConnected()) || !wifiOnly;
    }

    private boolean isWifiConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return wifi.isConnected();
    }

    class AsyncContentDownloader extends AsyncTask<String, Integer, String> {
        private final int CONNECT_TIMEOUT = 3000;
        private final int READ_TIMEOUT = 10000;
        private final int MAX_RETRY = 3;
        private final int WAIT = 500;

        @Override
        protected String doInBackground(String... path) {
            StringBuilder response = new StringBuilder();
            InputStream is = null;
            BufferedReader br = null;
            InputStreamReader isr = null;
            int i = 0;

            while (i < MAX_RETRY) {
                try {
                    URL url = new URL(path[0]);
                    URLConnection con = url.openConnection();
                    con.setConnectTimeout(CONNECT_TIMEOUT);
                    con.setReadTimeout(READ_TIMEOUT);
                    con.connect();

                    // open the stream and put it into BufferedReader
                    is = con.getInputStream();
                    isr = new InputStreamReader(is);
                    br = new BufferedReader(isr);
                    String inputLine;
                    while ((inputLine = br.readLine()) != null) {
                        response.append(inputLine);
                    }

                    break;
                } catch (Exception e) {
                    try {
                        Thread.sleep(WAIT);
                    }
                    catch (Exception ex) {}
                    i++;
                } finally {
                    try {br.close();} catch(Exception e) {}
                    try {isr.close();} catch(Exception e) {}
                    try {is.close();} catch(Exception e) {}
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
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    String downloadLocation = preferences.getString("download_location", getString(R.string.default_download_location));
                    Boolean wifiOnly = preferences.getBoolean("wifi_only", true);

                    downloadFile(url, title, ext, downloadLocation, wifiOnly);
                }
                else {
                    Toast.makeText(getApplicationContext(), getString(R.string.msg_error_video_info), Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(getApplicationContext(), getString(R.string.msg_error_video_info), Toast.LENGTH_LONG).show();
            }

            ProgressBar pbLoad = findViewById(R.id.pb_loader);
            pbLoad.setVisibility(View.INVISIBLE);

            Button btnDownload = findViewById(R.id.btn_download);
            btnDownload.setEnabled(true);
            btnDownload.setBackgroundColor(0xFFCC0000);
            btnDownload.setTextColor(0xFFFFFFFF);
        }

        private void downloadFile(String url, String title, String ext, String downloadLocation, Boolean wifiOnly) {
            String filePath = downloadLocation + "/" + title + "." + ext;
            File file = new File(filePath);

            if (isExternalStorageWritable(file)) {
                if (file.exists()) {
                    file.delete();
                }

                Uri uri = Uri.parse(url);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
                request.setAllowedOverRoaming(false);
                request.setTitle(title);
                request.setDescription("Downloading " + title);
                request.setVisibleInDownloadsUi(true);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                if (!wifiOnly) {
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                }

                filePath = filePath.replace(Environment.getExternalStorageDirectory().getAbsolutePath(), "");
                request.setDestinationInExternalPublicDir("", filePath);

                DownloadManager downloadManager = (DownloadManager) getSystemService(getApplicationContext().DOWNLOAD_SERVICE);
                downloadManager.enqueue(request);

                Toast.makeText(getApplicationContext(), getString(R.string.msg_task_added), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.msg_error_location), Toast.LENGTH_LONG).show();
            }
        }
    }
}