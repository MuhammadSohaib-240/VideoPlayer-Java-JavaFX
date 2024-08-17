package com.example.videoplayer;

import org.opencv.core.Core;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

public class OpenCV {

    int seconds, minutes, hours;
    String time;

    public String getVideoDuration(String file) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        VideoCapture capture = new VideoCapture();
        capture.open(file);

        int totalFrames = (int) capture.get(Videoio.CAP_PROP_FRAME_COUNT);
        int totalSeconds = (int) (totalFrames/30);

        hours = (int) totalSeconds / 3600;
        minutes = ((int) totalSeconds % 3600) / 60;
        seconds = (int) totalSeconds % 60;
        time = (String.format("%02d:%02d:%02d", hours, minutes, seconds));

        return time;
    }
}
