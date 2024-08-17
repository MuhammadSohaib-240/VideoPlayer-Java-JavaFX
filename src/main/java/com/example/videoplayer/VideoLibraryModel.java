package com.example.videoplayer;

@SuppressWarnings("All")
public class VideoLibraryModel {
    String videoName;
    String videoLocation;
    String videoDuration;
    int id;

    public VideoLibraryModel(int id, String videoName, String videoLocation, String videoDuration) {
        this.id = id;
        this.videoName = videoName;
        this.videoLocation = videoLocation;
        this.videoDuration = videoDuration;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoLocation() {
        return videoLocation;
    }

    public void setVideoLocation(String videoLocation) {
        this.videoLocation = videoLocation;
    }

    public String getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(String videoDuration) {
        this.videoDuration = videoDuration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
