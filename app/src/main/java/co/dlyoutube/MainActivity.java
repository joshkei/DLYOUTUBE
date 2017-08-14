package co.dlyoutube;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.view.View;
import android.os.Environment;
import android.os.AsyncTask;

import java.io.File;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

//testing

import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private final String VIDEO_INFO_FILE_NAME = "video.info";
    private final String VIDEO_FILE_NAME = "temp_video";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            Intent intent = getIntent();
            String action = intent.getAction();
            String type = intent.getType();

            if (Intent.ACTION_SEND.equals(action) && type != null) {
                if ("text/plain".equals(type)) {
                    String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                    if (sharedText != null && !sharedText.equals("")) {
                        String videoInfoUrl = changeURL(sharedText);

                        EditText txtUrl = (EditText) findViewById(R.id.url);
                        txtUrl.setText(videoInfoUrl);

                        if (videoInfoUrl != null && !videoInfoUrl.equals("")) {
                            String videoInfoPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + VIDEO_INFO_FILE_NAME;
                            downloadFile(videoInfoUrl, videoInfoPath, true);
                        }
                    }
                }
            }
        } catch (Exception ex) {

        }
    }

    public void testOnClick(View v) {
        //String test = changeURL("http://youtu.be/dhfo342khkdsf");

        String test = "cbrver=54.0&tag_for_child_directed=False&root_ve_type=27240&csi_page_type=embed&pltype=content&innertube_api_version=v1&oid=LVw_RQmDKUq5Mpm7YtazGg&relative_loudness=-2.79899978638&itct=CAEQu2kiEwiawffr8rLVAhVGQRwKHVHSAcco6NQB&excluded_ads=2=2_2;46=14_14;59=14_14;61=1_3,2_3;65=15_2,15_2_1,15_2_4,17_2,17_2_1;84=1_1,1_2,1_2_1,1_3,2_2,2_2_1,2_2_4,2_3,15_2,15_2_1,15_2_4,17_2,17_2_1;89=2_2,15_2;94=15_2,15_2_1,15_2_4,17_2,17_2_1&hl=en_US&cafe_experiment_id=56704024&remarketing_url=https://www.youtube.com/pagead/viewthroughconversion/962985656/?backend=innertube&cname=1&cver=1_20170727&data=backend=innertube;cname=1;cver=1_20170727;ptype=view;type=view;utuid=PDQZW9bUmsXSTmUq4rYeVw;utvid=DYJFtq8kVy8&foc_id=PDQZW9bUmsXSTmUq4rYeVw&label=followon_view&ptype=view&cver=1.20170727&account_playback_token=QUFFLUhqbHA1RVA0SHJ3YUJxd3dOanVNWDFXUlNUNGsyQXxBQ3Jtc0tuZDZlU1NvTWpvMzE4RUoyQXgwLWlFdm13U2QzcENlWWpFc1BvYTBpeEg4em0yU09HOGRaSWlRUFh4TU5pYzd1VVNieVdEd1dCbjdaZi1Tcnd3M3ctRDg3UkNxZVRfX1BqR3cwSmZLcVFjcGN4dlNnTQ==&view_count=744619&of=8cMiwdtJTM-WiRNka_nb7w&player_response={\\\\\"playbackTracking\\\\\":{},\\\\\"captions\\\\\":{\\\\\"playerCaptionsTracklistRenderer\\\\\":{\\\\\"contribute\\\\\":{\\\\\"captionsMetadataRenderer\\\\\":{\\\\\"addSubtitlesText\\\\\":{\\\\\"runs\\\\\":[{\\\\\"text\\\\\":\\\\\"Add subtitles/CC\\\\\",\\\\\"navigationEndpoint\\\\\":{\\\\\"clickTrackingParams\\\\\":\\\\\"CAEQu2kiEwiawffr8rLVAhVGQRwKHVHSAcco6NQB\\\\\",\\\\\"urlEndpoint\\\\\":{\\\\\"url\\\\\":\\\\\"/timedtext_video?ref=watch\\u0026v=DYJFtq8kVy8\\\\\"}}}]},\\\\\"noSubtitlesText\\\\\":{\\\\\"simpleText\\\\\":\\\\\"This video doesn't have subtitles or closed captions yet.\\\\\"},\\\\\"promoSubtitlesText\\\\\":{\\\\\"simpleText\\\\\":\\\\\"Help this video reach more people by contributing subtitles or closed captions in a language you speak.\\\\\"}}}}},\\\\\"videoDetails\\\\\":{\\\\\"thumbnail\\\\\":{\\\\\"thumbnails\\\\\":[{\\\\\"url\\\\\":\\\\\"https://i.ytimg.com/vi/DYJFtq8kVy8/hqdefault.jpg?sqp=-oaymwEWCKgBEF5IWvKriqkDCQgBFQAAiEIYAQ==\\u0026rs=AOn4CLD6yDxaCOs7XZHGRCmwwEzAOp2COQ\\\\\",\\\\\"width\\\\\":168,\\\\\"height\\\\\":94},{\\\\\"url\\\\\":\\\\\"https://i.ytimg.com/vi/DYJFtq8kVy8/hqdefault.jpg?sqp=-oaymwEWCMQBEG5IWvKriqkDCQgBFQAAiEIYAQ==\\u0026rs=AOn4CLDFXntxjxgJtuHg1R8xUKdwpCvsjw\\\\\",\\\\\"width\\\\\":196,\\\\\"height\\\\\":110},{\\\\\"url\\\\\":\\\\\"https://i.ytimg.com/vi/DYJFtq8kVy8/hqdefault.jpg?sqp=-oaymwEXCPYBEIoBSFryq4qpAwkIARUAAIhCGAE=\\u0026rs=AOn4CLA5hhvalR_sIwX4J56k0FBDo7-qiQ\\\\\",\\\\\"width\\\\\":246,\\\\\"height\\\\\":138},{\\\\\"url\\\\\":\\\\\"https://i.ytimg.com/vi/DYJFtq8kVy8/hqdefault.jpg?sqp=-oaymwEXCNACELwBSFryq4qpAwkIARUAAIhCGAE=\\u0026rs=AOn4CLAXroGUKJ9HysDdR6SzQImSQk4nnA\\\\\",\\\\\"width\\\\\":336,\\\\\"height\\\\\":188}]}},\\\\\"endscreen\\\\\":{\\\\\"endscreenUrlRenderer\\\\\":{\\\\\"url\\\\\":\\\\\"//www.youtube.com/get_endscreen?v=DYJFtq8kVy8\\u0026ei=s85-WdqbDMaCcdGkh7gM\\u0026client=1\\\\\"}},\\\\\"adSafetyReason\\\\\":{\\\\\"isRemarketingEnabled\\\\\":true,\\\\\"isFocEnabled\\\\\":true}}&vid=DYJFtq8kVy8&apiary_host_firstparty=&encoded_ad_safety_reason=CgBYAXAB&eventid=s85-WdqbDMaCcdGkh7gM&token=1&is_listed=1&ldpj=-28&cid=14744272&innertube_context_client_version=1.20170727&fade_out_start_milliseconds=0&iv_allow_in_place_switch=1&enablecsi=1&fexp=9422596,9431012,9434289,9445372,9446364,9449243,9453072,9455191,9457023,9457141,9460060,9463594,9465961,9466793,9466795,9466797,9467217,9467507,9468797,9468799,9468805,9474594,9475645,9476026,9478523,9480475,9481684,9481954,9482647,9484080,9484209,9484221,9484236,9484530,9484707,9484791,9485999,9486229&player_error_log_fraction=1.0&gut_tag=/4061/ytunknown/main&host_language=en&allow_html5_ads=1&no_get_video_log=1&atc=a=3&b=uIlJY38GlP5kYwQnu_UjrqrtC0g&c=1501482675&d=1&e=DYJFtq8kVy8&c3a=11&c1a=1&c6a=1&hh=XywZlh-VIHtd4T6nUc1ZZaw3Yo6b84tmhBsgEak0gb0&core_dbp=ChY4Y01pd2R0SlRNLVdpUk5rYV9uYjd3EAEgASgA&ppv_remarketing_url=https://www.googleadservices.com/pagead/conversion/971134070/?backend=innertube&cname=1&cver=1_20170727&data=backend=innertube;cname=1;cver=1_20170727;dactive=0;dynx_itemid=DYJFtq8kVy8;ptype=ppv&ptype=ppv&remarketing_only=1&as_launched_in_country=1&author=JeuxActu&status=ok&ad_slots=0&fade_in_start_milliseconds=-3000&avg_rating=4.74752569199&fmt_list=22/1280x720/9/0/115,43/640x360/99/0/0,18/640x360/9/0/115,36/320x180/99/1/0,17/176x144/99/1/0&length_seconds=163&focEnabled=1&apply_fade_on_midrolls=True&t=1&url_encoded_fmt_stream_map=type=video/mp4; codecs=\\\\\"avc1.64001F, mp4a.40.2\\\\\"&quality=hd720&itag=22&url=https://r2---sn-5uh5o-f5f6.googlevideo.com/videoplayback?sparams=dur%2Cei%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpcm2cms%2Cpl%2Cratebypass%2Crequiressl%2Csource%2Cexpire&requiressl=yes&ei=s85-WdqbDMaCcdGkh7gM&dur=162.655&mime=video%2Fmp4&initcwndbps=2382500&itag=22&pcm2cms=yes&expire=1501504275&id=0d8245b6af24572f&lmt=1491022548273586&ratebypass=yes&source=youtube&ip=164.132.51.91&key=yt6&mn=sn-5uh5o-f5f6&mm=31&mv=m&pl=24&mt=1501482574&ms=au&ipbits=0&signature=242D4FC08760CD99B53E818AFFC25CA13C669E05.A04747FF4ADACDE0C675E11E453FAA86FD10C819,type=video/webm; codecs=\\\\\"vp8.0, vorbis\\\\\"&quality=medium&itag=43&url=https://r2---sn-5uh5o-f5f6.googlevideo.com/videoplayback?sparams=clen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpcm2cms%2Cpl%2Cratebypass%2Crequiressl%2Csource%2Cexpire&requiressl=yes&ei=s85-WdqbDMaCcdGkh7gM&dur=0.000&mv=m&mime=video%2Fwebm&initcwndbps=2382500&itag=43&pcm2cms=yes&expire=1501504275&id=0d8245b6af24572f&lmt=1490797810581359&ratebypass=yes&source=youtube&clen=14458849&ip=164.132.51.91&key=yt6&mn=sn-5uh5o-f5f6&mm=31&gir=yes&pl=24&mt=1501482574&ms=au&ipbits=0&signature=78335B52A2D7EA02FAE1D7469A67BFCE5AEB09CD.C477F93CDA11C42F6A012C4E16241E4DA75C025D,type=video/mp4; codecs=\\\\\"avc1.42001E, mp4a.40.2\\\\\"&quality=medium&itag=18&url=https://r2---sn-5uh5o-f5f6.googlevideo.com/videoplayback?sparams=clen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpcm2cms%2Cpl%2Cratebypass%2Crequiressl%2Csource%2Cexpire&requiressl=yes&ei=s85-WdqbDMaCcdGkh7gM&dur=162.655&mv=m&mime=video%2Fmp4&initcwndbps=2382500&itag=18&pcm2cms=yes&expire=1501504275&id=0d8245b6af24572f&lmt=1491022384719852&ratebypass=yes&source=youtube&clen=10630751&ip=164.132.51.91&key=yt6&mn=sn-5uh5o-f5f6&mm=31&gir=yes&pl=24&mt=1501482574&ms=au&ipbits=0&signature=9EEE54CA9C872D0F40A82A9E3F1976EDA7735BD6.89BF77A9E322CE1DEE71A06A75F99462AA76C2FA,type=video/3gpp; codecs=\\\\\"mp4v.20.3, mp4a.40.2\\\\\"&quality=small&itag=36&url=https://r2---sn-5uh5o-f5f6.googlevideo.com/videoplayback?sparams=clen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpcm2cms%2Cpl%2Crequiressl%2Csource%2Cexpire&requiressl=yes&ei=s85-WdqbDMaCcdGkh7gM&dur=162.679&mv=m&mime=video%2F3gpp&initcwndbps=2382500&itag=36&pcm2cms=yes&expire=1501504275&id=0d8245b6af24572f&lmt=1490796279771083&source=youtube&clen=4062500&ip=164.132.51.91&key=yt6&mn=sn-5uh5o-f5f6&mm=31&gir=yes&pl=24&mt=1501482574&ms=au&ipbits=0&signature=788DEC3167098A0B5A1B3FA56EA8CB9013824B08.8AA27EB779DA9D2306F1C0882489428003FA9A35,type=video/3gpp; codecs=\\\\\"mp4v.20.3, mp4a.40.2\\\\\"&quality=small&itag=17&url=https://r2---sn-5uh5o-f5f6.googlevideo.com/videoplayback?sparams=clen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpcm2cms%2Cpl%2Crequiressl%2Csource%2Cexpire&requiressl=yes&ei=s85-WdqbDMaCcdGkh7gM&dur=162.679&mv=m&mime=video%2F3gpp&initcwndbps=2382500&itag=17&pcm2cms=yes&expire=1501504275&id=0d8245b6af24572f&lmt=1490796280877594&source=youtube&clen=1495780&ip=164.132.51.91&key=yt6&mn=sn-5uh5o-f5f6&mm=31&gir=yes&pl=24&mt=1501482574&ms=au&ipbits=0&signature=AE9045239E1421D8DB98555160EDD5DC2EE72E58.DBCD2C387AE51050904A70A1CEC29A80EB7FFBF6&adaptive_fmts=itag=137&type=video/mp4; codecs=\\\\\"avc1.640028\\\\\"&lmt=1491022455876263&url=https://r2---sn-5uh5o-f5f6.googlevideo.com/videoplayback?sparams=clen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Ckeepalive%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpcm2cms%2Cpl%2Crequiressl%2Csource%2Cexpire&requiressl=yes&ei=s85-WdqbDMaCcdGkh7gM&dur=162.560&mv=m&mime=video%2Fmp4&initcwndbps=2382500&itag=137&pcm2cms=yes&expire=1501504275&id=0d8245b6af24572f&lmt=1491022455876263&source=youtube&clen=36236296&ip=164.132.51.91&key=yt6&keepalive=yes&mn=sn-5uh5o-f5f6&mm=31&gir=yes&pl=24&mt=1501482574&ms=au&ipbits=0&signature=CB62FD232A538E83CC940993C7503B08B34F1E9D.4FBB49BDD0A23C022BF6E800F294F66AF2E6799C&quality_label=1080p&clen=36236296&bitrate=2691561&projection_type=1&xtags=&size=1920x1080&fps=25&index=716-1131&init=0-715,itag=248&type=video/webm; codecs=\\\\\"vp9\\\\\"&lmt=1490798055565532&url=https://r2---sn-5uh5o-f5f6.googlevideo.com/videoplayback?sparams=clen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Ckeepalive%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpcm2cms%2Cpl%2Crequiressl%2Csource%2Cexpire&requiressl=yes&ei=s85-WdqbDMaCcdGkh7gM&dur=162.520&mv=m&mime=video%2Fwebm&initcwndbps=2382500&itag=248&pcm2cms=yes&expire=1501504275&id=0d8245b6af24572f&lmt=1490798055565532&source=youtube&clen=43455493&ip=164.132.51.91&key=yt6&keepalive=yes&mn=sn-5uh5o-f5f6&mm=31&gir=yes&pl=24&mt=1501482574&ms=au&ipbits=0&signature=0F3FCC88296A1440DD8DC8E023CD45A3EEC84288.2BEF9BF79C56F92D68B66082D9BCE15150E2B792&quality_label=1080p&clen=43455493&bitrate=3134409&projection_type=1&xtags=&size=1920x1080&fps=25&index=243-798&init=0-242,itag=136&type=video/mp4; codecs=\\\\\"avc1.4d401f\\\\\"&lmt=1491022401794172&url=https://r2---sn-5uh5o-f5f6.googlevideo.com/videoplayback?sparams=clen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Ckeepalive%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpcm2cms%2Cpl%2Crequiressl%2Csource%2Cexpire&requiressl=yes&ei=s85-WdqbDMaCcdGkh7gM&dur=162.560&mv=m&mime=video%2Fmp4&initcwndbps=2382500&itag=136&pcm2cms=yes&expire=1501504275&id=0d8245b6af24572f&lmt=1491022401794172&source=youtube&clen=21308605&ip=164.132.51.91&key=yt6&keepalive=yes&mn=sn-5uh5o-f5f6&mm=31&gir=yes&pl=24&mt=1501482574&ms=au&ipbits=0&signature=05BED4318B5A4E39ACFCC8531E56712075EBA642.BC9CFCF9A4990D36BC12E9DA7BFA843321153DC0&quality_label=720p&clen=21308605&bitrate=1725612&projection_type=1&xtags=&size=1280x720&fps=25&index=714-1129&init=0-713,itag=247&type=video/webm; codecs=\\\\\"vp9\\\\\"&lmt=1490799223923705&url=https://r2---sn-5uh5o-f5f6.googlevideo.com/videoplayback?sparams=clen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Ckeepalive%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpcm2cms%2Cpl%2Crequiressl%2Csource%2Cexpire&requiressl=yes&ei=s85-WdqbDMaCcdGkh7gM&dur=162.520&mv=m&mime=video%2Fwebm&initcwndbps=2382500&itag=247&pcm2cms=yes&expire=1501504275&id=0d8245b6af24572f&lmt=1490799223923705&source=youtube&clen=24029869&ip=164.132.51.91&key=yt6&keepalive=yes&mn=sn-5uh5o-f5f6&mm=31&gir=yes&pl=24&mt=1501482574&ms=au&ipbits=0&signature=E11DB5CB08241E57CA24A08F4FEB1EEA64056DAF.32FC326D1C6B3945F66AEE932C5C1271C7C6B003&quality_label=720p&clen=24029869&bitrate=1701325&projection_type=1&xtags=&size=1280x720&fps=25&index=243-790&init=0-242,itag=135&type=video/mp4; codecs=\\\\\"avc1.4d401e\\\\\"&lmt=1491022455827820&url=https://r2---sn-5uh5o-f5f6.googlevideo.com/videoplayback?sparams=clen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Ckeepalive%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpcm2cms%2Cpl%2Crequiressl%2Csource%2Cexpire&requiressl=yes&ei=s85-WdqbDMaCcdGkh7gM&dur=162.560&mv=m&mime=video%2Fmp4&initcwndbps=2382500&itag=135&pcm2cms=yes&expire=1501504275&id=0d8245b6af24572f&lmt=1491022455827820&source=youtube&clen=11972860&ip=164.132.51.91&key=yt6&keepalive=yes&mn=sn-5uh5o-f5f6&mm=31&gir=yes&pl=24&mt=1501482574&ms=au&ipbits=0&signature=08E0FC76307875B823DCFDFEE504ABA82C67D960.862571A06A235493ABF96948DDCDB3D9B0045CAA&quality_label=480p&clen=11972860&bitrate=987293&projection_type=1&xtags=&size=854x480&fps=25&index=715-1130&init=0-714,itag=244&type=video/webm; codecs=\\\\\"vp9\\\\\"&lmt=1490799223982252&url=https://r2---sn-5uh5o-f5f6.googlevideo.com/videoplayback?sparams=clen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Ckeepalive%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpcm2cms%2Cpl%2Crequiressl%2Csource%2Cexpire&requiressl=yes&ei=s85-WdqbDMaCcdGkh7gM&dur=162.520&mv=m&mime=video%2Fwebm&initcwndbps=2382500&itag=244&pcm2cms=yes&expire=1501504275&id=0d8245b6af24572f&lmt=1490799223982252&source=youtube&clen=12020543&ip=164.132.51.91&key=yt6&keepalive=yes&mn=sn-5uh5o-f5f6&mm=31&gir=yes&pl=24&mt=1501482574&ms=au&ipbits=0&signature=249169C2DB5247484403DC7492E557636854F0DE.D8BC6D5B45A199D539DDD14A6A3F3B16E398FD3C&quality_label=480p&clen=12020543&bitrate=855931&projection_type=1&xtags=&size=854x480&fps=25&index=243-777&init=0-242,itag=134&type=video/mp4; codecs=\\\\\"avc1.4d401e\\\\\"&lmt=1491022401746918&url=https://r2---sn-5uh5o-f5f6.googlevideo.com/videoplayback?sparams=clen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Ckeepalive%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpcm2cms%2Cpl%2Crequiressl%2Csource%2Cexpire&requiressl=yes&ei=s85-WdqbDMaCcdGkh7gM&dur=162.560&mv=m&mime=video%2Fmp4&initcwndbps=2382500&itag=134&pcm2cms=yes&expire=1501504275&id=0d8245b6af24572f&lmt=1491022401746918&source=youtube&clen=6128021&ip=164.132.51.91&key=yt6&keepalive=yes&mn=sn-5uh5o-f5f6&mm=31&gir=yes&pl=24&mt=1501482574&ms=au&ipbits=0&signature=315C9EC9D370683B7991AB34750B29184217C715.6048223CC86CEFCB610E53C05DB2973A39813244&quality_label=360p&clen=6128021&bitrate=505189&projection_type=1&xtags=&size=640x360&fps=25&index=715-1130&init=0-714,itag=243&type=video/webm; codecs=\\\\\"vp9\\\\\"&lmt=1490799222650281&url=https://r2---sn-5uh5o-f5f6.googlevideo.com/videoplayback?sparams=clen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Ckeepalive%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpcm2cms%2Cpl%2Crequiressl%2Csource%2Cexpire&requiressl=yes&ei=s85-WdqbDMaCcdGkh7gM&dur=162.520&mv=m&mime=video%2Fwebm&initcwndbps=2382500&itag=243&pcm2cms=yes&expire=1501504275&id=0d8245b6af24572f&lmt=1490799222650281&source=youtube&clen=6867616&ip=164.132.51.91&key=yt6&keepalive=yes&mn=sn-5uh5o-f5f6&mm=31&gir=yes&pl=24&mt=1501482574&ms=au&ipbits=0&signature=6B8C28222E6F5AFC2809B309B3D22A1B6ECD7874.82779A0D656C63137383DA569CD06E56DAC4EAB0&quality_label=360p&clen=6867616&bitrate=475356&projection_type=1&xtags=&size=640x360&fps=25&index=243-777&init=0-242,itag=133&type=video/mp4; codecs=\\\\\"avc1.4d4015\\\\\"&lmt=1491022402286664&url=https://r2---sn-5uh5o-f5f6.googlevideo.com/videoplayback?sparams=clen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Ckeepalive%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpcm2cms%2Cpl%2Crequiressl%2Csource%2Cexpire&requiressl=yes&ei=s85-WdqbDMaCcdGkh7gM&dur=162.560&mv=m&mime=video%2Fmp4&initcwndbps=2382500&itag=133&pcm2cms=yes&expire=1501504275&id=0d8245b6af24572f&lmt=1491022402286664&source=youtube&clen=4484875&ip=164.132.51.91&key=yt6&keepalive=yes&mn=sn-5uh5o-f5f6&mm=31&gir=yes&pl=24&mt=1501482574&ms=au&ipbits=0&signature=A8EE75333E7C6C96897BDDAA48EA43726994F469.683A6ABA881B28F094CDFE7372E9245B3E38EE0B&quality_label=240p&clen=4484875&bitrate=272043&projection_type=1&xtags=&size=426x240&fps=25&index=714-1129&init=0-713,itag=242&type=video/webm; codecs=\\\\\"vp9\\\\\"&lmt=1490799222507296&url=https://r2---sn-5uh5o-f5f6.googlevideo.com/videoplayback?sparams=clen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Ckeepalive%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpcm2cms%2Cpl%2Crequiressl%2Csource%2Cexpire&requiressl=yes&ei=s85-WdqbDMaCcdGkh7gM&dur=162.520&mv=m&mime=video%2Fwebm&initcwndbps=2382500&itag=242&pcm2cms=yes&expire=1501504275&id=0d8245b6af24572f&lmt=1490799222507296&source=youtube&clen=3701553&ip=164.132.51.91&key=yt6&keepalive=yes&mn=sn-5uh5o-f5f6&mm=31&gir=yes&pl=24&mt=1501482574&ms=au&ipbits=0&signature=7736D0F14A93DC4788246BA7B263480271745C5A.11AFCE1AAB951A8D31FEC712276E546B94E263E4&quality_label=240p&clen=3701553&bitrate=262309&projection_type=1&xtags=&size=426x240&fps=25&index=242-775&init=0-241,itag=160&type=video/mp4; codecs=\\\\\"avc1.4d400c\\\\\"&lmt=1491022455795418&url=https://r2---sn-5uh5o-f5f6.googlevideo.com/videoplayback?sparams=clen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Ckeepalive%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpcm2cms%2Cpl%2Crequiressl%2Csource%2Cexpire&requiressl=yes&ei=s85-WdqbDMaCcdGkh7gM&dur=162.560&mv=m&mime=video%2Fmp4&initcwndbps=2382500&itag=160&pcm2cms=yes&expire=1501504275&id=0d8245b6af24572f&lmt=1491022455795418&source=youtube&clen=1356283&ip=164.132.51.91&key=yt6&keepalive=yes&mn=sn-5uh5o-f5f6&mm=31&gir=yes&pl=24&mt=1501482574&ms=au&ipbits=0&signature=1ACF6F3B56BD9B58768E7B1E65BD4EA2B1A99AE6.705FAB11FBBF1BFA665CE3D2D1634C24014D9254&quality_label=144p&clen=1356283&bitrate=111541&projection_type=1&xtags=&size=256x144&fps=25&index=713-1128&init=0-712,itag=278&type=video/webm; codecs=\\\\\"vp9\\\\\"&lmt=1490799223241819&url=https://r2---sn-5uh5o-f5f6.googlevideo.com/videoplayback?sparams=clen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Ckeepalive%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpcm2cms%2Cpl%2Crequiressl%2Csource%2Cexpire&requiressl=yes&ei=s85-WdqbDMaCcdGkh7gM&dur=162.520&mv=m&mime=video%2Fwebm&initcwndbps=2382500&itag=278&pcm2cms=yes&expire=1501504275&id=0d8245b6af24572f&lmt=1490799223241819&source=youtube&clen=1829236&ip=164.132.51.91&key=yt6&keepalive=yes&mn=sn-5uh5o-f5f6&mm=31&gir=yes&pl=24&mt=1501482574&ms=au&ipbits=0&signature=D57668867A0356B1572A9887FFC566A5F3B0741B.BEBAD3EF36FE2E0F6689DAA5186772520AF9DA90&quality_label=144p&clen=1829236&bitrate=135937&projection_type=1&xtags=&size=256x144&fps=25&index=242-775&init=0-241,bitrate=127903&type=audio/mp4; codecs=\\\\\"mp4a.40.2\\\\\"&xtags=&url=https://r2---sn-5uh5o-f5f6.googlevideo.com/videoplayback?sparams=clen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Ckeepalive%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpcm2cms%2Cpl%2Crequiressl%2Csource%2Cexpire&requiressl=yes&ei=s85-WdqbDMaCcdGkh7gM&dur=162.655&mv=m&mime=audio%2Fmp4&initcwndbps=2382500&itag=140&pcm2cms=yes&expire=1501504275&id=0d8245b6af24572f&lmt=1491022366051762&source=youtube&clen=2584073&ip=164.132.51.91&key=yt6&keepalive=yes&mn=sn-5uh5o-f5f6&mm=31&gir=yes&pl=24&mt=1501482574&ms=au&ipbits=0&signature=CF533F4DFFD085C7A5DDDBFDF8E3D2B1005545F7.4ED721993638918F70789418C4D2D6286631FE7B&lmt=1491022366051762&projection_type=1&clen=2584073&itag=140&index=592-827&init=0-591,bitrate=125781&type=audio/webm; codecs=\\\\\"vorbis\\\\\"&xtags=&url=https://r2---sn-5uh5o-f5f6.googlevideo.com/videoplayback?sparams=clen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Ckeepalive%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpcm2cms%2Cpl%2Crequiressl%2Csource%2Cexpire&requiressl=yes&ei=s85-WdqbDMaCcdGkh7gM&dur=162.599&mv=m&mime=audio%2Fwebm&initcwndbps=2382500&itag=171&pcm2cms=yes&expire=1501504275&id=0d8245b6af24572f&lmt=1490797754564225&source=youtube&clen=2055647&ip=164.132.51.91&key=yt6&keepalive=yes&mn=sn-5uh5o-f5f6&mm=31&gir=yes&pl=24&mt=1501482574&ms=au&ipbits=0&signature=782E61AED8BB6726BA8DE39C6023335D73C5A421.B0DDF0A6666D62C67C8010ABE8AB995097EEB945&lmt=1490797754564225&projection_type=1&clen=2055647&itag=171&index=4452-4737&init=0-4451,bitrate=50742&type=audio/webm; codecs=\\\\\"opus\\\\\"&xtags=&url=https://r2---sn-5uh5o-f5f6.googlevideo.com/videoplayback?sparams=clen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Ckeepalive%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpcm2cms%2Cpl%2Crequiressl%2Csource%2Cexpire&requiressl=yes&ei=s85-WdqbDMaCcdGkh7gM&dur=162.601&mv=m&mime=audio%2Fwebm&initcwndbps=2382500&itag=249&pcm2cms=yes&expire=1501504275&id=0d8245b6af24572f&lmt=1490797757845540&source=youtube&clen=864942&ip=164.132.51.91&key=yt6&keepalive=yes&mn=sn-5uh5o-f5f6&mm=31&gir=yes&pl=24&mt=1501482574&ms=au&ipbits=0&signature=BD4217373A2E6702221C23368E1DED519837D142.59414EAD6F8C05E0174C589802EB8B2B09040747&lmt=1490797757845540&projection_type=1&clen=864942&itag=249&index=272-556&init=0-271,bitrate=67639&type=audio/webm; codecs=\\\\\"opus\\\\\"&xtags=&url=https://r2---sn-5uh5o-f5f6.googlevideo.com/videoplayback?sparams=clen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Ckeepalive%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpcm2cms%2Cpl%2Crequiressl%2Csource%2Cexpire&requiressl=yes&ei=s85-WdqbDMaCcdGkh7gM&dur=162.601&mv=m&mime=audio%2Fwebm&initcwndbps=2382500&itag=250&pcm2cms=yes&expire=1501504275&id=0d8245b6af24572f&lmt=1490797755238053&source=youtube&clen=1142047&ip=164.132.51.91&key=yt6&keepalive=yes&mn=sn-5uh5o-f5f6&mm=31&gir=yes&pl=24&mt=1501482574&ms=au&ipbits=0&signature=BCCB1829A5A90743F89FB9A37063A75F44154383.D0387407EDCD7A8FA10FF95A78B67BEF955B54E3&lmt=1490797755238053&projection_type=1&clen=1142047&itag=250&index=272-557&init=0-271,bitrate=131140&type=audio/webm; codecs=\\\\\"opus\\\\\"&xtags=&url=https://r2---sn-5uh5o-f5f6.googlevideo.com/videoplayback?sparams=clen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Ckeepalive%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpcm2cms%2Cpl%2Crequiressl%2Csource%2Cexpire&requiressl=yes&ei=s85-WdqbDMaCcdGkh7gM&dur=162.601&mv=m&mime=audio%2Fwebm&initcwndbps=2382500&itag=251&pcm2cms=yes&expire=1501504275&id=0d8245b6af24572f&lmt=1490797754318831&source=youtube&clen=2250507&ip=164.132.51.91&key=yt6&keepalive=yes&mn=sn-5uh5o-f5f6&mm=31&gir=yes&pl=24&mt=1501482574&ms=au&ipbits=0&signature=5C69642CABC26DD58BCA044E51171D949D39555D.75E0E667CB0ED52E9017EA41FCBAEB460B3F2B47&lmt=1490797754318831&projection_type=1&clen=2250507&itag=251&index=272-557&init=0-271&mpvid=NF86MvVbMz0BTUtG&cos=Windows&instream_long=False&videostats_playback_base_url=https://s.youtube.com/api/stats/playback?ns=yt&vm=CAEQARgE&of=8cMiwdtJTM-WiRNka_nb7w&plid=AAVVly1_hvLLv3Er&fexp=9422596,9431012,9434289,9445372,9446364,9449243,9453072,9455191,9457023,9457141,9460060,9463594,9465961,9466793,9466795,9466797,9467217,9467507,9468797,9468799,9468805,9474594,9475645,9476026,9478523,9480475,9481684,9481954,9482647,9484080,9484209,9484221,9484236,9484530,9484707,9484791,9485999,9486229&el=embedded&docid=DYJFtq8kVy8&ei=s85-WdqbDMaCcdGkh7gM&cl=163306509&len=163&cr=FR&allow_ratings=1&fflags=html5_variability_discount=0.5&html5_serverside_call_server_on_biscotti_timeout=true&html5_reseek_on_infinite_buffer=true&ios_notifications_disabled_subs_tab_promoted_item_promo=true&mweb_autonav_paddles=true&html5_no_clear_on_quota_exceeded=true&enable_red_carpet_p13n_shelves=true&ad_video_end_renderer_duration_milliseconds=7000&html5_idle_preload_secs=1&signed_out_notification_settings=true&html5_min_startup_smooth_target=10.0&html5_capstone_avoidance_tolerance=0.4&html5_connect_timeout_secs=7.0&html5_deferred_source_buffer_creation=true&disable_reporting_html5_no_vast_ads_as_error=true&large_play_button_hover_src=&html5_variability_full_discount_thresh=3.0&html5_always_reload_on_403=true&html5_throttle_rate=0.0&html5_serverside_biscotti_id_wait_ms=1000&live_fresca_v2=true&html5_always_enable_timeouts=true&html5_pause_manifest_ended=true&channel_about_page_gadgets=true&disable_client_side_midroll_freq_capping=true&flex_theater_mode=true&html5_live_disable_dg_pacing=true&android_enable_thumbnail_overlay_side_panel=false&html5_live_4k_more_buffer=true&html5_chrome_ambisonics_opus=true&kids_asset_theme=server_side_assets&html5_request_sizing_multiplier=0.8&html5_new_preloading=true&html5_stale_dash_manifest_retry_factor=1.0&mysubs_highlight_shelf_counterfactual=false&html5_throttle_burst_secs=15.0&mweb_disable_iphone_interstitial_for_ios_10=true&disable_indisplay_adunit_on_embedded=true&html5_min_readbehind_secs=0&signed_out_notifications_inbox=true&legacy_autoplay_flag=true&yto_enable_watch_offer_module=true&show_thumbnail_on_standard=true&report_promotes_terrorism_ios=true&yto_enable_unlimited_landing_page_yto_features=true&html5_max_av_sync_drift=50&stop_using_ima_sdk_gpt_request_activity=true&kids_eliminate_playlists_in_channels=true&dynamic_ad_break_pause_threshold_sec=0&king_crimson_player=false&html5_disable_move_pssh_to_moov=true&interaction_log_delayed_event_batch_size=200&html5_repredict_misses=5&html5_min_secs_between_format_selections=8.0&html5_get_video_info_timeout_ms=0&midroll_notify_time_seconds=5&enable_plus_page_pts=true&html5_min_upgrade_health=0&html5_vp9_live_whitelist=false&html5_seeks_are_not_rebuffers=true&yto_enable_ytr_promo_refresh_assets=true&html5_spherical_bicubic_mode=0&lugash_header_warmup=true&html5_msi_error_fallback=true&sdk_wrapper_levels_allowed=0&html5_deadzone_multiplier=1.1&mobile_disable_ad_mob_on_home=true&html5_max_readahead_bandwidth_cap=0&html5_aspect_from_adaptive_format=true&html5_disable_audio_slicing=true&html5_tight_max_buffer_allowed_impaired_time=0.0&send_html5_api_stats_ads_abandon=true&report_promotes_terrorism_polymer=true&vss_dni_delayping=0&kids_enable_terms_servlet=true&player_destroy_old_version=true&ad_duration_threshold_for_showing_endcap_seconds=15&desktop_cleanup_companion_on_instream_begin=true&html5_new_fallback=true&mweb_blacklist_progressive_chrome_mobile=true&mweb_muted_autoplay_animation=none&ios_disable_notification_preprompt=true&html5_preload_size_excludes_metadata=true&dynamic_ad_break_seek_threshold_sec=0&html5_allowable_liveness_drift_chunks=2&html5_enable_mesh_projection=true&uniplayer_dbp=true&chrome_promo_enabled=true&variable_load_timeout_ms=0&use_new_style=true&autoplay_time=8000&enable_local_channel=true&html5_min_vss_watchtime_to_cut_secs=0.0&persist_text_on_preview_button=true&html5_trust_platform_bitrate_limits=true&web_player_api_logging_fraction=0.01&request_mpu_on_unfilled_ad_break=true&enable_ccs_buy_flow_for_chirp=true&postroll_notify_time_seconds=5&html5_subsegment_readahead_tail_margin_secs=0.2&html5_background_cap_idle_secs=60&html5_source_buffer_retry_interval_ms=100.0&playready_on_borg=true&mpu_visible_threshold_count=2&html5_source_buffer_required_for_play=0.0&android_native_live_enablement=true&android_buy_bucket_buy_flows=true&html5_elbow_tracking_tweaks=true&forced_brand_precap_duration_ms=2000&mweb_enable_skippables_on_iphone=true&max_resolution_for_white_noise=360&segment_volume_reporting=true&use_new_skip_icon=true&html5_repredict_interval_secs=0.0&use_fast_fade_in_0s=true&live_chunk_readahead=3&html5_strip_emsg=true&new_promo_page=true&dash_manifest_version=5&html5_tight_max_buffer_allowed_bandwidth_stddevs=0.0&report_promotes_terrorism_mobile_web=true&html5_use_mediastream_timestamp=true&sdk_ad_prefetch_time_seconds=-1&html5_min_buffer_to_resume=6&html5_clearance_fix=true&watermark_src=&html5_retry_media_element_errors_delay=0&html5_fallback_probe_ms=5000&html5_reduce_startup_rebuffers=true&html5_request_size_max_secs=0&enable_live_state_auth=true&disable_search_mpu=true&html5_default_quality_cap=0&pla_shelf_hovercard=true&html5_subsegment_readahead_target_buffer_health_secs=0.5&html5_suspended_state=true&html5_minimum_readahead_seconds=0.0&html5_bandwidth_window_size=0&html5_max_buffer_duration=0&html5_suspend_manifest_on_pause=true&yto_feature_hub_channel=false&html5_use_adaptive_live_readahead=true&kids_enable_server_side_assets=false&report_promotes_terrorism_android=true&sidebar_renderers=true&live_readahead_seconds_multiplier=0.8&ios_enable_mixin_accessibility_custom_actions=true&kids_mqb_use_confirmed_safe_videos_only=false&fixed_padding_skip_button=true&web_player_flash_fallback_killswitch=true&kids_enable_post_onboarding_red_flow=true&mweb_disable_iphone_interstitial_for_ios_10_ads_backend=true&html5_check_for_reseek=true&html5_playing_event_buffer_underrun=true&html5_timeupdate_readystate_check=true&website_actions_use_companion_image_as_banner=true&html5_post_interrupt_readahead=20&html5_max_buffer_health_for_downgrade=15&html5_live_pin_to_tail=true&enable_prefetch_for_postrolls=true&html5_disable_non_contiguous=true&html5_enable_embedded_player_visibility_signals=true&fix_gpt_pos_params=true&html5_background_quality_cap=360&html5_hopeless_mode_request_size_secs=15&disable_trusted_ad_domains_player_check=true&html5_ad_no_buffer_abort_after_skippable=true&youtubei_for_web=true&html5_local_max_byterate_lookahead=15&enable_pla_desktop_shelf=true&html5_nnr_downgrade_count=4&lugash_header_by_service=true&enable_playlist_multi_season=true&html5_subsegment_readahead_progress_timeout_fraction=0.8&html5_min_readbehind_cap_secs=0&kids_enable_privacy_notice=true&web_player_disable_flash_watch=true&html5_request_size_min_secs=0.0&html5_subsegment_readahead_timeout_secs=2.0&show_countdown_on_bumper=true&website_actions_throttle_percentage=1.0&kids_enable_block_servlet=true&html5_serverside_call_server_on_biscotti_error=true&html5_platform_minimum_readahead_seconds=0.0&html5_max_vss_watchtime_ratio=0.0&report_promotes_terrorism_desktop=true&king_crimson_player_redux=true&html5_variability_no_discount_thresh=1.0&use_push_for_desktop_live_chat=true&html5_min_vss_watchtime_to_cut_secs_redux=0.0&html5_disable_vp9_hdr_enc=true&large_play_button_src=&logo_gaming_src=&mweb_playsinline=true&html5_desktop_serverside_get_biscotti_id_from_client=true&html5_min_request_size_kb=64&yt_unlimited_pts_skip_ads_promo_desktop_always=true&doubleclick_gpt_retagging=true&polymer_report_missing_web_navigation_endpoint=false&logo_src=&html5_adjust_effective_request_size=true&html5_report_conn=true&enable_offer_restricts_for_watch_page_offers=true&lock_fullscreen=false&disable_new_pause_state3=true&html5_disable_urgent_upgrade_for_quality=true&html5_get_video_info_promiseajax=true&mweb_pu_android_chrome_54_above=true&html5_progressive_signature_reload=true&enable_finished_broadcast_reactr_sharing=false&disable_max_adsense_channel_limit=true&enable_bulleit_lidar_integration=true&html5_maximum_readahead_seconds=0.0&show_thumbnail_behind_ypc_offer_module=true&vmap=<?xml version=\\\\\"1.0\\\\\" encoding=\\\\\"UTF-8\\\\\"?><vmap:VMAP xmlns:vmap=\\\\\"http://www.iab.net/videosuite/vmap\\\\\" xmlns:yt=\\\\\"http://youtube.com\\\\\" version=\\\\\"1.0\\\\\"><vmap:AdBreak breakType=\\\\\"linear\\\\\" timeOffset=\\\\\"start\\\\\"><vmap:AdSource allowMultipleAds = \\\\\"false\\\\\"><vmap:VASTData><VAST version=\\\\\"3.0\\\\\"><Ad><Wrapper><AdSystem version=\\\\\"1\\\\\">YT:AdSense</AdSystem><VASTAdTagURI><![CDATA[https://googleads.g.doubleclick.net/pagead/ads?ad_type=text_image&channel=Vertical_1106+Vertical_1107+Vertical_211+Vertical_3+Vertical_34+Vertical_36+Vertical_613+afv_overlay+afv_user_id_PDQZW9bUmsXSTmUq4rYeVw+afv_user_jeuxactutv+invideo_overlay_480x70_cat20+yt_cid_14744272+yt_mpvid_NF86MvVbMz0BTUtG+yt_no_360+yt_no_ap+yt_no_wa+ytdevice_1+ytdevicever_1.20170727+ytel_embedded+ytps_default&client=ca-pub-6219811747049371&dbp=ChY4Y01pd2R0SlRNLVdpUk5rYV9uYjd3EAEgASgA&description_url=http://www.youtube.com/video/DYJFtq8kVy8&eid=56704024&hl=en&host=ca-host-pub-3575961816217993&ht_id=5859103&loeid=9422596,9431012,9434289,9445372,9451823,9453072,9455191,9457023,9460060,9460349,9465961,9474594,9475645,9478523,9481684,9481954,9482851,9484080,9484221,9484236,9484530,9484791,9486229&osd=2&pucrd=CgYIABAAGAA&sdki=18803DAD&url=http://www.youtube.com/video/DYJFtq8kVy8&videoad_start_delay=0&v_p=JYOMREQ=&ytdevice=1&ytdevicever=1.20170727&yt_pt=APb3F29k0PcPFXxAhmxqMDJy7O65pmCGl6PV2vVChRRJv-PA_bXHkHYl7-1FIRefzmN3mym4-PmzNvkViPhVkt_7_Waf0L-k0qZxdeuxGhvb1MWBEcYDWRa9h8duTiBI0gpGrh1mDa5yyXRHMeSBxqjSxspXCYR3FE6jIYTzXD6GVytlJYQqXOBr-ERDvE31Pw2cwPB6sFBhiA3c54HZ6vtcn5X4NCEpPyFjdskAEyFaTwTrE6JHAxl6HnyeBHTI9-SxDDBf9aN_VkicuqAr&yt_vis=0]]></VASTAdTagURI><Impression><![CDATA[https://www.youtube.com/api/stats/ads?ver=2&ns=1&event=2&device=1&content_v=DYJFtq8kVy8&el=embedded&ei=s85-WdqbDMaCcdGkh7gM&devicever=1.20170727&asr=CgBYAXAB&format=[FORMAT_NAMESPACE]_[FORMAT_TYPE]_[FORMAT_SUBTYPE]&break_type=[BREAK_TYPE]&conn=[CONN]&cpn=[CPN]&lact=[LACT]&m_pos=[MIDROLL_POS]&mt=[MT]&p_h=[P_H]&p_w=[P_W]&rwt=[RWT]&sdkv=[SDKV]&slot_pos=[SLOT_POS]&vis=[VIS]&vol=[VOL]&wt=[WT]&ad_cpn=[AD_CPN]&ad_id=[AD_ID]&ad_len=[AD_LEN]&ad_mt=[AD_MT]&ad_sys=[AD_SYS]&ad_v=[AD_V]&aqi=]]></Impression><Error><![CDATA[https://www.youtube.com/api/stats/ads?ver=2&ns=1&event=5&device=1&content_v=DYJFtq8kVy8&el=embedded&ei=s85-WdqbDMaCcdGkh7gM&devicever=1.20170727&format=[FORMAT_NAMESPACE]_[FORMAT_TYPE]_[FORMAT_SUBTYPE]&break_type=[BREAK_TYPE]&conn=[CONN]&cpn=[CPN]&lact=[LACT]&m_pos=[MIDROLL_POS]&mt=[MT]&p_h=[P_H]&p_w=[P_W]&rwt=[RWT]&sdkv=[SDKV]&slot_pos=[SLOT_POS]&vis=[VIS]&vol=[VOL]&wt=[WT]&ad_cpn=[AD_CPN]&ad_id=[AD_ID]&ad_len=[AD_LEN]&ad_mt=[AD_MT]&ad_sys=[AD_SYS]&ad_v=[AD_V]&blocking_error=[BLOCKING_ERROR]&error_msg=[ERROR_MSG]&ima_error=[IMA_ERROR]&internal_id=[INTERNAL_ID]&error_code=[YT_ERROR_CODE]&aqi=]]></Error><Creatives><Creative><NonLinearAds><TrackingEvents><Tracking event=\\\\\"close\\\\\"><![CDATA[https://www.youtube.com/api/stats/ads?ver=2&ns=1&event=4&device=1&content_v=DYJFtq8kVy8&el=embedded&ei=s85-WdqbDMaCcdGkh7gM&devicever=1.20170727&format=[FORMAT_NAMESPACE]_[FORMAT_TYPE]_[FORMAT_SUBTYPE]&break_type=[BREAK_TYPE]&conn=[CONN]&cpn=[CPN]&lact=[LACT]&m_pos=[MIDROLL_POS]&mt=[MT]&p_h=[P_H]&p_w=[P_W]&rwt=[RWT]&sdkv=[SDKV]&slot_pos=[SLOT_POS]&vis=[VIS]&vol=[VOL]&wt=[WT]&ad_cpn=[AD_CPN]&ad_id=[AD_ID]&ad_mt=[AD_MT]&ad_sys=[AD_SYS]&ad_v=[AD_V]&i_x=[I_X]&i_y=[I_Y]&aqi=]]></Tracking></TrackingEvents><NonLinear><NonLinearClickTracking><![CDATA[https://www.youtube.com/api/stats/ads?ver=2&ns=1&event=6&device=1&content_v=DYJFtq8kVy8&el=embedded&ei=s85-WdqbDMaCcdGkh7gM&devicever=1.20170727&format=[FORMAT_NAMESPACE]_[FORMAT_TYPE]_[FORMAT_SUBTYPE]&break_type=[BREAK_TYPE]&conn=[CONN]&cpn=[CPN]&lact=[LACT]&m_pos=[MIDROLL_POS]&mt=[MT]&p_h=[P_H]&p_w=[P_W]&rwt=[RWT]&sdkv=[SDKV]&slot_pos=[SLOT_POS]&vis=[VIS]&vol=[VOL]&wt=[WT]&ad_cpn=[AD_CPN]&ad_id=[AD_ID]&ad_len=[AD_LEN]&ad_mt=[AD_MT]&ad_sys=[AD_SYS]&ad_v=[AD_V]&i_x=[I_X]&i_y=[I_Y]&aqi=[AQI]]]></NonLinearClickTracking></NonLinear></NonLinearAds></Creative></Creatives><Extensions><Extension type=\\\\\"waterfall\\\\\" fallback_index=\\\\\"1\\\\\"/><Extension type=\\\\\"activeview\\\\\"><CustomTracking><Tracking event=\\\\\"viewable_impression\\\\\"><![CDATA[https://www.youtube.com/api/stats/ads?ver=2&ns=1&event=11&device=1&content_v=DYJFtq8kVy8&el=embedded&ei=s85-WdqbDMaCcdGkh7gM&devicever=1.20170727&format=[FORMAT_NAMESPACE]_[FORMAT_TYPE]_[FORMAT_SUBTYPE]&break_type=[BREAK_TYPE]&conn=[CONN]&cpn=[CPN]&lact=[LACT]&m_pos=[MIDROLL_POS]&mt=[MT]&p_h=[P_H]&p_w=[P_W]&rwt=[RWT]&sdkv=[SDKV]&slot_pos=[SLOT_POS]&vis=[VIS]&vol=[VOL]&wt=[WT]&ad_cpn=[AD_CPN]&ad_id=[AD_ID]&ad_len=[AD_LEN]&ad_mt=[AD_MT]&ad_sys=[AD_SYS]&ad_v=[AD_V]&aqi=]]></Tracking></CustomTracking></Extension></Extensions></Wrapper></Ad></VAST></vmap:VASTData></vmap:AdSource><vmap:TrackingEvents><vmap:Tracking event=\\\\\"error\\\\\"><![CDATA[https://www.youtube.com/api/stats/ads?ver=2&ns=1&event=1&device=1&content_v=DYJFtq8kVy8&el=embedded&ei=s85-WdqbDMaCcdGkh7gM&devicever=1.20170727&break_type=[BREAK_TYPE]&conn=[CONN]&cpn=[CPN]&lact=[LACT]&m_pos=[MIDROLL_POS]&mt=[MT]&p_h=[P_H]&p_w=[P_W]&rwt=[RWT]&sdkv=[SDKV]&slot_pos=[SLOT_POS]&vis=[VIS]&vol=[VOL]&wt=[WT]]]></vmap:Tracking></vmap:TrackingEvents><vmap:Extensions><vmap:Extension type=\\\\\"YouTube\\\\\"><yt:BreakProperty break_type=\\\\\"BREAK_TYPE_PRE_ROLL\\\\\"/></vmap:Extension></vmap:Extensions></vmap:AdBreak><vmap:Extensions><vmap:Extension type=\\\\\"YouTube\\\\\"><TrackingDecoration match=\\\\\"^https?://((([a-z][a-z0-9.-]*\\.)?(youtube|corp\\.google).com/api/stats/ads)|((www\\.)?youtube\\.com/pagead/psul))\\\\\" headers=\\\\\"device,user\\\\\"/><TrackingMacro match=\\\\\"^https?://(secure\\-uat\\-dpr\\.imrworldwide|secure\\-gg\\.imrworldwide|g\\.scorecardresearch)\\.com/\\\\\" macros=\\\\\"device_id\\\\\"/><RegexUriMacroValidator><MacroToRegexUris macro=\\\\\"NIELSEN_DEVICE_ID\\\\\"><RegexUri value=\\\\\"^https?://(secure\\-uat\\-dpr\\.imrworldwide|secure\\-gg\\.imrworldwide|g\\.scorecardresearch)\\.com/\\\\\"/></MacroToRegexUris><MacroToRegexUris macro=\\\\\"COMSCORE_DEVICE_ID\\\\\"><RegexUri value=\\\\\"^https?://(secure\\-uat\\-dpr\\.imrworldwide|secure\\-gg\\.imrworldwide|g\\.scorecardresearch)\\.com/\\\\\"/></MacroToRegexUris><MacroToRegexUris macro=\\\\\"MOAT_INIT\\\\\"><RegexUri value=\\\\\"^https?://yts\\.moatads\\.com\\\\\"/><RegexUri value=\\\\\"^https?://yts-testing\\.moatads\\.com\\\\\"/><RegexUri value=\\\\\"^https?://pagead2\\.googlesyndication\\.com/pagead/gen_204\\\\\"/></MacroToRegexUris><MacroToRegexUris macro=\\\\\"MOAT_VIEWABILITY\\\\\"><RegexUri value=\\\\\"^https?://[^.]*\\.moatads\\.com\\\\\"/><RegexUri value=\\\\\"^https?://pagead2\\.googlesyndication\\.com/pagead/gen_204\\\\\"/><RegexUri value=\\\\\"^https?://pubads\\.g\\.doubleclick\\.net\\\\\"/></MacroToRegexUris><MacroToRegexUris macro=\\\\\"IAS_VIEWABILITY\\\\\"><RegexUri value=\\\\\"^https?://pm\\.adsafeprotected\\.com/youtube\\\\\"/><RegexUri value=\\\\\"^https?://pm\\.test-adsafeprotected\\.com/youtube\\\\\"/><RegexUri value=\\\\\"^https?://pagead2\\.googlesyndication\\.com/pagead/gen_204\\\\\"/><RegexUri value=\\\\\"^https?://pubads\\.g\\.doubleclick\\.net\\\\\"/></MacroToRegexUris><MacroToRegexUris macro=\\\\\"DV_VIEWABILITY\\\\\"><RegexUri value=\\\\\"^https?://e[0-9] \\.yt\\.srs\\.doubleverify\\.com\\\\\"/><RegexUri value=\\\\\"^https?://pagead2\\.googlesyndication\\.com/pagead/gen_204\\\\\"/><RegexUri value=\\\\\"^https?://pubads\\.g\\.doubleclick\\.net\\\\\"/></MacroToRegexUris><MacroToRegexUris macro=\\\\\"GOOGLE_VIEWABILITY\\\\\"><RegexUri value=\\\\\"^https?://pagead2\\.googlesyndication\\.com\\\\\"/><RegexUri value=\\\\\"^https?://pubads\\.g\\.doubleclick\\.net\\\\\"/><RegexUri value=\\\\\"^https?://googleads\\.g\\.doubleclick\\.net\\\\\"/><RegexUri value=\\\\\"^https?://([a-z0-9] \\.)*youtube\\.com\\\\\"/><RegexUri value=\\\\\"^https?://ad[\\.-]([a-z0-9] \\.){0,1}doubleclick\\.net\\\\\"/><RegexUri value=\\\\\"^https?://ade\\.googlesyndication\\.com\\\\\"/></MacroToRegexUris></RegexUriMacroValidator></vmap:Extension></vmap:Extensions></vmap:VMAP>&ad3_module=1&allow_below_the_player_companion=True&title=VALERIAN - NOUVELLE Bande Annonce VF (Luc Besson - Film 2017)&ssl=1&rmktEnabled=1&enabled_engage_types=1,3,4,5,6,17&watermark=,https://s.ytimg.com/yts/img/watermark/youtube_watermark-vflHX6b6E.png,https://s.ytimg.com/yts/img/watermark/youtube_hd_watermark-vflAzLcD6.png&iv3_module=1&sffb=True&tmi=1&fade_out_duration_milliseconds=1000&midroll_freqcap=420.0&allow_embed=1&keywords=Valerian,Bande Annonce,VF,Film,2017,Luc Besson,Dane DeHaan,Cara Delevingne,Rihanna&ad_device=1&afv_ad_tag=https://googleads.g.doubleclick.net/pagead/ads?ad_type=text_image&channel=Vertical_1106+Vertical_1107+Vertical_211+Vertical_3+Vertical_34+Vertical_36+Vertical_613+afv_overlay+afv_user_id_PDQZW9bUmsXSTmUq4rYeVw+afv_user_jeuxactutv+invideo_overlay_480x70_cat20+yt_cid_14744272+yt_mpvid_NF86MvVbMz0BTUtG+yt_no_360+yt_no_ap+yt_no_wa+ytdevice_1+ytdevicever_1.20170727+ytel_embedded+ytps_default&client=ca-pub-6219811747049371&dbp=ChY4Y01pd2R0SlRNLVdpUk5rYV9uYjd3EAEgASgA&description_url=http://www.youtube.com/video/DYJFtq8kVy8&eid=56704024&hl=en&host=ca-host-pub-3575961816217993&ht_id=5859103&loeid=9422596,9431012,9434289,9445372,9451823,9453072,9455191,9457023,9460060,9460349,9465961,9474594,9475645,9478523,9481684,9481954,9482851,9484080,9484221,9484236,9484530,9484791,9486229&osd=2&pucrd=CgYIABAAGAA&sdki=18803DAD&url=http://www.youtube.com/video/DYJFtq8kVy8&v_p=JYOMREQ=&ytdevice=1&ytdevicever=1.20170727&yt_pt=APb3F29k0PcPFXxAhmxqMDJy7O65pmCGl6PV2vVChRRJv-PA_bXHkHYl7-1FIRefzmN3mym4-PmzNvkViPhVkt_7_Waf0L-k0qZxdeuxGhvb1MWBEcYDWRa9h8duTiBI0gpGrh1mDa5yyXRHMeSBxqjSxspXCYR3FE6jIYTzXD6GVytlJYQqXOBr-ERDvE31Pw2cwPB6sFBhiA3c54HZ6vtcn5X4NCEpPyFjdskAEyFaTwTrE6JHAxl6HnyeBHTI9-SxDDBf9aN_VkicuqAr&yt_vis=0&iv_invideo_url=https://www.youtube.com/annotations_invideo?cap_hist=1&video_id=DYJFtq8kVy8&client=1&ei=s85-WdqbDMaCcdGkh7gM&cosver=6.1&c=WEB&storyboard_spec=https://i9.ytimg.com/sb/DYJFtq8kVy8/storyboard3_L$L/$N.jpg|48#27#100#10#10#0#default#rs$AOn4CLAWCl3Xegv0lUwzSKJq_oWMjYpPHA|80#45#83#10#10#2000#M$M#rs$AOn4CLC8oE4BAq1i07MuH2J5x2WRIQpZmw|160#90#83#5#5#2000#M$M#rs$AOn4CLBVeNzQ6K37mGDYot7fVFAZ1CKEbQ&ptchn=PDQZW9bUmsXSTmUq4rYeVw&plid=AAVVly1_hvLLv3Er&ptk=Mixicom&fade_in_duration_milliseconds=1000&gapi_hint_params=m;/_/scs/abc-static/_/js/k=gapi.gapi.en.pwuFxAM9sSs.O/m=__features__/rt=j/d=1/rs=AHpOoo_kFxiSkGFruvghs_M-2UjERAt_Iw&dbp=Ch4KFjhjTWl3ZHRKVE0tV2lSTmthX25iN3cQASABKAAQARgC&ad_flags=0&swf_player_response=1&vm=CAEQARgE&loudness=-23.7989997864&uid=PDQZW9bUmsXSTmUq4rYeVw&csn=s85-WdqbDMaCcdGkh7gM&cl=163306509&idpj=-1&afv=True&apiary_host=&iv_load_policy=1&loeid=9422596,9431012,9434289,9445372,9451823,9453072,9455191,9457023,9460060,9460349,9465961,9474594,9475645,9478523,9481684,9481954,9482851,9484080,9484221,9484236,9484530,9484791,9486229&ismb=19060000&shortform=True&cbr=Firefox&thumbnail_url=https://i.ytimg.com/vi/DYJFtq8kVy8/default.jpg&baseUrl=https://www.youtube.com/pagead/viewthroughconversion/962985656/&midroll_prefetch_size=1&innertube_api_key=AIzaSyAO_FJ2SlqU8Q4STEHLGCilw_Y9_11qcW8&xhr_apiary_host=youtubei.youtube.com&show_content_thumbnail=True&ad_logging_flag=1&video_id=DYJFtq8kVy8&ucid=UCPDQZW9bUmsXSTmUq4rYeVw&timestamp=1501482675";

        ArrayList<DownloadOption> options = extractOption(test);
    }

    private void setDownloadOption(ArrayList<DownloadOption> options) {
        //file types
        ArrayList<String> fileTypes = new ArrayList<String>();
        for (int i = 0; i < options.size(); i++) {
            String fileType = options.get(i).getQualityOption().getFileType();
            if (!fileTypes.contains(fileType)) {
                fileTypes.add(fileType);
            }
        }

        Spinner fileType = (Spinner)findViewById(R.id.file_type);
        ArrayAdapter fileTypeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, fileTypes);
        fileType.setAdapter(fileTypeAdapter);

        //video quality
        ArrayList<Integer> tempVideoQualities = new ArrayList<Integer>();
        for (int i = 0; i < options.size(); i++) {
            Integer videoQuality = options.get(i).getQualityOption().getVideoQuality();
            if (!tempVideoQualities.contains(videoQuality)) {
                tempVideoQualities.add(videoQuality);
            }
        }

        Collections.sort(tempVideoQualities, Collections.reverseOrder());

        ArrayList<String> videoQualities = new ArrayList<String>();
        for (int i = 0; i < tempVideoQualities.size(); i++) {
            if (tempVideoQualities.get(i) != 0) {
                videoQualities.add(tempVideoQualities.get(i) + "p");
            }
            else {
                videoQualities.add("N/A");
            }
        }

        Spinner videoQuality = (Spinner)findViewById(R.id.video_quality);
        ArrayAdapter videoQualityAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, videoQualities);
        videoQuality.setAdapter(videoQualityAdapter);

        //audio quality
        ArrayList<Integer> tempAudioQualities = new ArrayList<Integer>();
        for (int i = 0; i < options.size(); i++) {
            Integer audioQuality = options.get(i).getQualityOption().getAudioQuality();
            if (!tempAudioQualities.contains(audioQuality)) {
                tempAudioQualities.add(audioQuality);
            }
        }

        Collections.sort(tempAudioQualities, Collections.reverseOrder());

        ArrayList<String> audioQualities = new ArrayList<String>();
        for (int i = 0; i < tempAudioQualities.size(); i++) {
            if (tempAudioQualities.get(i) != 0) {
                audioQualities.add(tempAudioQualities.get(i) + " kbps");
            }
            else {
                audioQualities.add("N/A");
            }
        }

        Spinner audioQuality = (Spinner)findViewById(R.id.audio_quality);
        ArrayAdapter audioQualityAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, audioQualities);
        audioQuality.setAdapter(audioQualityAdapter);
    }

    private String changeURL(String url) throws IndexOutOfBoundsException {

        String newUrl = "";

        final String VIDEO_INFO_URL = "http://www.youtube.com/get_video_info?&video_id=";
        final String pattern = "(?:https?:\\/\\/)?(?:www\\.)?youtu(?:.be\\/|be\\.com\\/watch\\?v=|be\\.com\\/v\\/)(.{8,})";

        Pattern compiledPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = compiledPattern.matcher(url);

        if (matcher.find()) {
            newUrl = VIDEO_INFO_URL + matcher.group(1);
        }

        return newUrl;
    }

    private void downloadFile(String url, String path, boolean isOverWritten) throws IOException {
        if (isOverWritten) {
            File file = new File(path);

            if (file.exists()) {
                file.delete();
            }
        }

        AsyncFileDownloader downloader = new AsyncFileDownloader();
        downloader.execute(url, path);
    }

    public void processVideoInfo() {
        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + VIDEO_INFO_FILE_NAME;
            File file = new File(path);

            if (file.exists()) {
                byte[] videoInfoFile = readFile(path);

                if (videoInfoFile != null & videoInfoFile.length > 0) {
                    String videoInfo = new String(videoInfoFile, StandardCharsets.UTF_8);

                    videoInfo = decodeInfo(videoInfo);
                    videoInfo = decodeInfo(videoInfo);

                    if (!videoInfo.equals("")) {
                        ArrayList<DownloadOption> options = extractOption(videoInfo);

                        if (options != null && options.size() > 0) {
                            setDownloadOption(options);
                        }

                    }
                }
            }
        } catch (IOException ex) {

        }
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

    private String decodeInfo(String text) throws UnsupportedEncodingException {
        return URLDecoder.decode(text, "UTF-8");
    }

    private ArrayList<DownloadOption> extractOption(String videoInfo) {
        ArrayList<DownloadOption> downloadOptions = new ArrayList<DownloadOption>();

        //ArrayList<String> types = new ArrayList<String>();
        ArrayList<String> tags = new ArrayList<String>();
        ArrayList<String> urls = new ArrayList<String>();

        final String OPTION_PATTERN = "url_encoded_fmt_stream_map=.*?_url";
        //final String TYPE_PATTERN = "type=(video|audio)/\\w+\\;";
        final String URL_PATTERN = "url\\=\\S+(\\,|&)";
        final String TAG_PATTERN = "itag=([0-9]+)";

        QualityOptionTable qualityOptionTable = new QualityOptionTable();
        Hashtable<String, QualityOption> optionTable = qualityOptionTable.getOptionTable();


        try {
            Pattern optionPattern = Pattern.compile(OPTION_PATTERN, Pattern.CASE_INSENSITIVE);
            Matcher optionMatcher = optionPattern.matcher(videoInfo);

            if (optionMatcher.find()) {
                String info = optionMatcher.group();

                Pattern tagPattern = Pattern.compile(TAG_PATTERN, Pattern.CASE_INSENSITIVE);
                Matcher tagMatcher = tagPattern.matcher(info);

                String tempTag = "";

                while (tagMatcher.find()) {
                    String tag = tagMatcher.group();

                    tag = tag.substring(5);

                    if (!tag.equals(tempTag)) {
                        tags.add(tag);
                    }

                    tempTag = tag;
                }

                Pattern urlPattern = Pattern.compile(URL_PATTERN, Pattern.CASE_INSENSITIVE);
                Matcher urlMatcher = urlPattern.matcher(info);

                while (urlMatcher.find()) {
                    String url = urlMatcher.group();
                    url = url.substring(4, url.length() - 1);

                    urls.add(url);
                }

                if (tags.size() == urls.size()) {
                    for (int i = 0; i < tags.size(); i++) {
                        if (optionTable.containsKey(tags.get(i))) {
                            QualityOption option = optionTable.get(tags.get(i));
                            String url = urls.get(i);

                            downloadOptions.add(new DownloadOption(option, url));
                        }
                        else {
                            System.out.println(tags.get(i));
                        }
                    }
                }
            }
        }
        catch (Exception ex) {

        }

        return downloadOptions;
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

    class AsyncFileDownloader extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... path) {
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

            } finally {
                try {
                    output.close();
                    input.close();
                } catch (Exception ex) {
                }
            }

            return path[1];
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
        protected void onPostExecute(String file) {
            super.onPostExecute(file);

            if (file.endsWith(VIDEO_INFO_FILE_NAME)) {
                MainActivity activity = new MainActivity();
                activity.processVideoInfo();
            }
        }
    }
}