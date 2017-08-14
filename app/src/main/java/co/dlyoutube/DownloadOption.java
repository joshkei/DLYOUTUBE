package co.dlyoutube;

public class DownloadOption {
    private QualityOption qualityOption;
    private String url;

    public QualityOption getQualityOption() {
        return this.qualityOption;
    }

    public void setQualityOption(QualityOption qualityOption) {
        this.qualityOption = qualityOption;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String qualityOption) {
        this.url = url;
    }

    public DownloadOption() {

    }

    public DownloadOption(QualityOption qualityOption, String url) {
        this.qualityOption = qualityOption;
        this.url = url;
    }
}
