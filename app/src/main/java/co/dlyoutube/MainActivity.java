package co.dlyoutube;

import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.EditText;
import android.view.View;
import android.os.Environment;
import android.os.AsyncTask;

import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import java.util.ArrayList;

//testing
import android.widget.TextView;
import android.widget.Toast;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private final String SHORT_DOMAIN = "youtu.be";
    private final String LONG_DOMAIN = "m.youtube.com";
    private final String VIDEO_ORG_STRING = "watch?v=";
    private final String VIDEO_INFO_STRING = "get_video_info?&video_id=";
    private final String VIDEO_INFO_PATH = "video.info";
    private final String VIDEO_FILE_PATH = "temp_video";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (sharedText != null && !sharedText.equals("")) {
                    String videoInfoUrl = changeURL(sharedText);

                    if (videoInfoUrl != null && !videoInfoUrl.equals("")) {
                        String videoInfoPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + VIDEO_INFO_PATH;

                        try {
                            downloadFile(videoInfoUrl, videoInfoPath, true);
                        }
                        catch (IOException ex) {

                        }

                        try {
                            byte[] videoInfoFile = readFile(videoInfoPath);
                        }
                        catch (IOException ex) {

                        }

                        //String videoInfoText = decodeInfo(videoInfo.toString());
                        //videoInfoText = decodeInfo(videoInfoText);
                    }
                }
            }
        }
    }

    //testing
    public void testOnClick(View v) {
        try {
            AsyncFileDownloader downloader = new AsyncFileDownloader();
            downloader.execute("https://developer.android.com/reference/android/os/AsyncTask.html", Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + VIDEO_INFO_PATH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downloadFile(String url, String path, boolean isOverWritten) throws IOException {
        if (isOverWritten) {
            Files.deleteIfExists(Paths.get(path));
        }

        AsyncFileDownloader downloader = new AsyncFileDownloader();
        downloader.execute(url, path);
    }

    private byte[] readFile(String path) throws IOException {
        byte[] file = null;

        Path filePath = Paths.get(path);

        if (Files.exists(filePath)) {
            Files.readAllBytes(filePath);
        }

        return file;
    }

    private String changeURL(String url) {

        String newUrl = url;

        if (newUrl.length() > 0) {
            //youtu.be
            if (newUrl.toLowerCase().indexOf(SHORT_DOMAIN) != -1) {
                newUrl = newUrl.replaceAll("(?i)" + Pattern.quote(SHORT_DOMAIN), LONG_DOMAIN);

                int slashPos = newUrl.lastIndexOf("/");

                if (slashPos > 0 && slashPos < newUrl.length() - 1) {
                    newUrl = newUrl.substring(0, slashPos + 1) + VIDEO_INFO_STRING + newUrl.substring(slashPos + 1);
                }
            }
            //youtube.com
            if (newUrl.toLowerCase().indexOf(VIDEO_ORG_STRING) != -1) {
                newUrl = newUrl.replaceAll("(?i)" + Pattern.quote(VIDEO_ORG_STRING), VIDEO_INFO_STRING);
            }
        }

        return newUrl;
    }

    private String decodeInfo(String text) throws UnsupportedEncodingException {
        return URLDecoder.decode(text, "UTF-8");
    }

    private ArrayList<Pair<String, String>> extractInfo(String videoInfo) {
        ArrayList<Pair<String, String>> info = new ArrayList<Pair<String, String>>();


        return info;
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    class AsyncFileDownloader extends AsyncTask<String, Integer, Long> {
        @Override
        protected Long doInBackground(String... path) {
            int count;
            InputStream input = null;
            OutputStream output = null;
            long total = 0;

            try {
                URL u = new URL(path[0]);
                URLConnection con = u.openConnection();
                con.connect();
                // getting file length
                int lenghtOfFile = con.getContentLength();

                // input stream to read file - with 8k buffer
                input = new BufferedInputStream(u.openStream());

                // Output stream to write file
                output = new FileOutputStream(path[1]);

                byte data[] = new byte[1024];

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    //publishProgress(""+(int)((total*100)/lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                output.flush();
            } catch (IOException e) {
                String a = e.toString();
            } finally {
                try {
                    output.close();
                    input.close();
                } catch (Exception ex) {
                }
            }

            return total;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
        }

        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);

        }
    }
}