package co.dlyoutube;

public class QualityOption {

    private String tag;
    private String fileType;
    private int videoQuality;
    private int audioQuality;

    public String getTag() {
        return this.tag;
    }

    public String getFileType() {
        return this.fileType;
    }

    public int getVideoQuality() {
        return this.videoQuality;
    }

    public int getAudioQuality() {
        return this.audioQuality;
    }

    public QualityOption() {
    }

    public QualityOption(String tag, String fileType, int videoQuality, int audioQuality) {
        this.tag = tag;
        this.fileType = fileType;
        this.videoQuality = videoQuality;
        this.audioQuality = audioQuality;
    }
}
