package com.easyarch.Chat.demo.HandlerV2.entity;

public enum DataType {
    TEXT(null),
    //bmp,png,tif,gif,JPEG,jpg
    BMP(".bmp"),
    PNG(".png"),
    TIF(".tif"),
    GIF(".gif"),
    JPEG(".jpeg"),
    JPG(".jpg"),
    //AVI、mov,mp4
    AVI(".avi"),
    MOV(".mov"),
    MP4(".mp4"),
    //mp3,mpeg
    MP3(".mp3"),
    MPEG(".mpeg")
    ;

    private String value;
    private DataType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
