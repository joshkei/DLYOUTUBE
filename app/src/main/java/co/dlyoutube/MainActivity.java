package co.dlyoutube;

import android.accounts.NetworkErrorException;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.EditText;
import android.view.View;
import android.os.Environment;
import android.content.Context;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.regex.Pattern;
import java.util.ArrayList;

//testing
import android.widget.TextView;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private final String SHORT_DOMAIN = "youtu.be";
    private final String LONG_DOMAIN = "m.youtube.com";
    private final String VIDEO_ORG_STRING = "watch?v=";
    private final String VIDEO_INFO_STRING = "get_video_info?&video_id=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            }
        }
    }

    //testing
    public void testOnClick(View v) {
        FileOutputStream outputStream = null;

        try {
            byte[] file = downloadFile("https://developer.android.com/reference/android/view/View.OnClickListener.html");
            String fileName = "test.html";

            outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(file);

            TextView txtTest = (TextView) findViewById(R.id.test);
            txtTest.setText(new String(file, StandardCharsets.UTF_8));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                outputStream.close();
            }
            catch (Exception ex) {
            }
        }
    }

    private void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null && !sharedText.equals("")) {

            String videoUrl = "";
            byte[] videoInfo = null;
            String videoInfoText = "";

            //change URL
            try {
                videoUrl = changeURL(sharedText);
            } catch (IndexOutOfBoundsException ex) {
                //URL format problem
            }

            EditText txtUrl = (EditText) findViewById(R.id.url);
            txtUrl.setText(videoUrl);

            //download video info
            if (!videoUrl.equals("")) {
                try {
                    videoInfo = downloadFile(videoUrl);
                } catch (IOException e) {
                    //video info cannot be downloaded
                }
            }

            //decode video info
            if (videoInfo != null) {
                try {
                    videoInfoText = decodeInfo(videoInfo.toString());
                    videoInfoText = decodeInfo(videoInfoText);
                } catch (UnsupportedEncodingException e) {
                    //video info cannot be decoded
                }
            }

            if (!videoInfoText.equals("")) {

            }
        }
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

    private byte[] downloadFile(String urlStr) throws IOException {
        byte[] data = null;

        ByteArrayOutputStream baos = null;
        InputStream is = null;

        try {
            baos = new ByteArrayOutputStream();

            URL url = new URL(urlStr);
            is = url.openStream();
            byte[] byteChunk = new byte[4096];
            int n;

            while ((n = is.read(byteChunk)) > 0) {
                baos.write(byteChunk, 0, n);
            }
            data = baos.toByteArray();
        }
        catch (IOException e) {
            throw e;
        }
        finally {
            try {
                baos.close();
                is.close();
            }
            catch (Exception ex) {
            }
        }

        return data;
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

    private void saveFile(byte[] file, String path) {

    }
}
