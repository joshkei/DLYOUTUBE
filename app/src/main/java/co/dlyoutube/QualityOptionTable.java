package co.dlyoutube;

import java.util.Hashtable;

public class QualityOptionTable {
    private Hashtable<String, QualityOption> optionTable;

    public Hashtable<String, QualityOption> getOptionTable() {
        return this.optionTable;
    }

    public QualityOptionTable() {
        optionTable = new Hashtable<String, QualityOption>();

        optionTable.put("5", new QualityOption("5", "FLV", 240, 64));
        optionTable.put("6", new QualityOption("6","FLV", 270, 64));
        optionTable.put("13", new QualityOption("13", "3GP", 0, 0));
        optionTable.put("17", new QualityOption("17", "3GP", 144, 24));
        optionTable.put("18", new QualityOption("18", "MP4", 360, 96));
        optionTable.put("22", new QualityOption("22", "MP4", 720, 192));
        optionTable.put("34", new QualityOption("34", "FLV", 480, 128));
        optionTable.put("35", new QualityOption("35", "FLV", 360, 128));
        optionTable.put("36", new QualityOption("36", "3GP", 240, 36));
        optionTable.put("37", new QualityOption("37", "MP4", 1080, 192));
        optionTable.put("38", new QualityOption("38", "MP4", 3072, 192));
        optionTable.put("43", new QualityOption("43", "WEBM", 360, 128));
        optionTable.put("44", new QualityOption("44", "WEBM", 480, 128));
        optionTable.put("45", new QualityOption("45", "WEBM", 720, 192));
        optionTable.put("46", new QualityOption("46", "WEBM", 1080, 192));
        optionTable.put("82", new QualityOption("82", "MP4", 360, 96));
        optionTable.put("83", new QualityOption("83", "MP4", 240, 96));
        optionTable.put("84", new QualityOption("84", "MP4", 720, 192));
        optionTable.put("85", new QualityOption("85", "MP4", 1080, 192));
        optionTable.put("100", new QualityOption("100", "WEBM", 360, 128));
        optionTable.put("101", new QualityOption("101", "WEBM", 360, 192));
        optionTable.put("102", new QualityOption("102", "WEBM", 720, 192));
        optionTable.put("133", new QualityOption("133", "MP4", 240, 0));
        optionTable.put("134", new QualityOption("134", "MP4", 360, 0));
        optionTable.put("135", new QualityOption("135", "MP4", 480, 0));
        optionTable.put("136", new QualityOption("136", "MP4", 720, 0));
        optionTable.put("137", new QualityOption("137", "MP4", 1080, 0));
        optionTable.put("138", new QualityOption("138", "MP4", 2160, 0));
        optionTable.put("160", new QualityOption("160", "MP4", 144, 0));
        optionTable.put("242", new QualityOption("242", "WEBM", 240, 0));
        optionTable.put("243", new QualityOption("243", "WEBM", 360, 0));
        optionTable.put("244", new QualityOption("244", "WEBM", 480, 0));
        optionTable.put("247", new QualityOption("247", "WEBM", 720, 0));
        optionTable.put("248", new QualityOption("248", "WEBM", 1080, 9));
        optionTable.put("264", new QualityOption("264", "MP4", 1440, 0));
        optionTable.put("266", new QualityOption("266", "MP4", 2160, 0));
        optionTable.put("271", new QualityOption("271", "WEBM", 1440, 0));
        optionTable.put("272", new QualityOption("272", "WEBM", 2160, 0));
        optionTable.put("278", new QualityOption("278", "WEBM", 144, 0));
        optionTable.put("298", new QualityOption("298", "MP4", 720, 0));
        optionTable.put("299", new QualityOption("299", "MP4", 1080, 0));
        optionTable.put("302", new QualityOption("302", "WEBM", 720, 0));
        optionTable.put("303", new QualityOption("303", "WEBM", 1080, 0));
        optionTable.put("139", new QualityOption("139", "MP4", 0, 48));
        optionTable.put("140", new QualityOption("140", "MP4", 0, 128));
        optionTable.put("141", new QualityOption("141", "MP4", 0, 256));
        optionTable.put("171", new QualityOption("171", "WEBM", 0, 128));
        optionTable.put("172", new QualityOption("172", "WEBM", 0, 192));
        optionTable.put("249", new QualityOption("249", "WEBM", 0, 5));
        optionTable.put("250", new QualityOption("250", "WEBM", 0, 7));
        optionTable.put("251", new QualityOption("251", "WEBM", 0, 16));
        optionTable.put("92", new QualityOption("92", "TS", 240, 48));
        optionTable.put("93", new QualityOption("93", "TS", 480, 128));
        optionTable.put("94", new QualityOption("94", "TS", 720, 128));
        optionTable.put("95", new QualityOption("95", "TS", 1080, 256));
        optionTable.put("96", new QualityOption("96", "TS", 720, 256));
        optionTable.put("120", new QualityOption("120", "FLV", 720, 128));
        optionTable.put("127", new QualityOption("127","TS", 0, 96));
        optionTable.put("128", new QualityOption("128","TS", 0, 96));
        optionTable.put("132", new QualityOption("132", "TS", 240, 48));
        optionTable.put("151", new QualityOption("151", "TS", 720, 24));
    }
}
