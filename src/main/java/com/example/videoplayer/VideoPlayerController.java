package com.example.videoplayer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@SuppressWarnings("All")
public class VideoPlayerController implements Initializable {
    @FXML
    Button playPauseButton;

    @FXML
    Button previousMediaButton;

    @FXML
    Button nextMediaButton;

    @FXML
    Button volumeButton;

    @FXML
    Button changeRatioButton;

    @FXML
    Slider volumeSlider;

    @FXML
    Button extractImagesButton;

    @FXML
    Button screenshotButton;

    @FXML
    Button libraryButton;

    @FXML
    ProgressBar progressBar;

    @FXML
    HBox mediaViewLayout;

    @FXML
    MediaView mediaView;

    @FXML
    private Stage stage;

    @FXML
    private Scene scene;

    @FXML
    private Parent root;

    @FXML
    MenuBar menuBar;

    Media media;
    MediaPlayer mediaPlayer;
    LinkedList<File> videos;
    File directory;
    File[] files;
    private Timer timer;
    TimerTask task;
    int seconds;
    int minutes;
    int hours;
    int videoNumber = 0;
    String time;
    private boolean running;
    String inputFilePath;
    String outputFilePath;
    String fileName;
    String imagesNumber = "%04d.png";
    int framesPerNSeconds;
    ExtraFeatures extraFeatures = new ExtraFeatures();
    FFMPEGCommands ffmpegCommands = new FFMPEGCommands();
    SQLite3Operations sqLite3Operations = new SQLite3Operations();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sqLite3Operations.connect();

        Menu edit = new Menu("Edit");
        Menu playbackSpeed = new Menu("Playback Speed");
        menuBar.getMenus().add(edit);
        menuBar.getMenus().add(playbackSpeed);

        MenuItem rgbToMonochrome = new MenuItem("RGB To Monochrome");
        MenuItem trimVideo = new MenuItem("Trim Video");
        MenuItem extractImages = new MenuItem("Extract Images");
        MenuItem item = new MenuItem();

        //Creating Playback Speed Slider
        Slider playbackSpeedSlider = new Slider(1, 8, 1);
        playbackSpeedSlider.setStyle("-fx-background-color: black;");
        playbackSpeedSlider.setShowTickLabels(true);
        playbackSpeedSlider.setShowTickMarks(true);
        playbackSpeedSlider.setMajorTickUnit(1);
        playbackSpeedSlider.setBlockIncrement(1);
        item.setGraphic(playbackSpeedSlider);

        edit.getItems().add(rgbToMonochrome);
        edit.getItems().add(trimVideo);
        edit.getItems().add(extractImages);
        playbackSpeed.getItems().addAll(item);

        videos = new LinkedList<File>();
        directory = new File("Videos");
        files = directory.listFiles();

        if (files != null) {
            for (File file: files) {
                videos.add(file);
            }
        }

        media = new Media(videos.get(videoNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaView.setPreserveRatio(true);
        mediaView.fitWidthProperty().bind(mediaViewLayout.widthProperty());
        mediaView.fitHeightProperty().bind(mediaViewLayout.heightProperty());

        playPauseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                playPauseMedia();
            }
        });

        nextMediaButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                nextMedia();
            }
        });

        previousMediaButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                previousMedia();
            }
        });

        extractImagesButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    // Open dialogue to get save location path
                    DirectoryChooser dc = new DirectoryChooser();
                    File selectSaveLocation = dc.showDialog(null);
                    Path path = Paths.get(selectSaveLocation.getAbsolutePath());

                    // Formatting save location path string
                    extraFeatures.saveFileName();
                    fileName = extraFeatures.fileName;
                    outputFilePath = String.format("%s\\\\", path.toString().replaceAll(
                            "\\\\", "\\\\\\\\")) + fileName;

                    // Formatting video location path string
                    inputFilePath = videos.get(videoNumber).getAbsolutePath().replaceAll(
                            "\\\\", "\\\\\\\\");

                    extraFeatures.setFramePerSecond();
                    framesPerNSeconds = extraFeatures.framePerSecond;

                    // Taking Screenshot
                    extractImages();
                } catch (Exception e) {
                    // PASS
                }
            }
        });

        libraryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                openLibrary();
            }
        });

        screenshotButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    playPauseMedia();
                    DirectoryChooser dc = new DirectoryChooser();
                    File selectSaveLocation = dc.showDialog(null);
                    Path path = Paths.get(selectSaveLocation.getAbsolutePath());

                    extraFeatures.saveFileName();
                    fileName = extraFeatures.fileName;
                    outputFilePath = String.format("%s\\\\", path.toString().replaceAll(
                            "\\\\", "\\\\\\\\")) + fileName + ".jpeg";

                    inputFilePath = videos.get(videoNumber).getAbsolutePath().replaceAll(
                            "\\\\", "\\\\\\\\");

                    takeScreenshot();

                    extraFeatures.informationAlert("Information Dialog", null,
                            "Screenshot Taken Successfully");
                } catch (Exception e) {
                    playPauseMedia();
                }
            }
        });

        changeRatioButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                changeVideoRatio();
            }
        });

        volumeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                muteAudio();
            }
        });

        volumeSlider.setMin(0);
        volumeSlider.setMax(100);
        volumeSlider.setValue(50);

        volumeSlider.valueProperty().addListener(new ChangeListener<>() {
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                mediaPlayer.setVolume((volumeSlider.getValue() / 100));
            }
        });

        rgbToMonochrome.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser dc = new DirectoryChooser();
                File selectSaveLocation = dc.showDialog(null);
                Path path = Paths.get(selectSaveLocation.getAbsolutePath());

                extraFeatures.saveFileName();
                fileName = extraFeatures.fileName;
                outputFilePath = String.format("%s\\\\", path.toString().replaceAll(
                        "\\\\", "\\\\\\\\")) + fileName + ".mp4";

                // Formatting video location path string
                inputFilePath = videos.get(videoNumber).getAbsolutePath().replaceAll(
                        "\\\\", "\\\\\\\\");

                rgbToMonochrome();
            }
        });

        playbackSpeedSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                mediaPlayer.setRate(playbackSpeedSlider.getValue());
            }
        });

        extractImages.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    DirectoryChooser dc = new DirectoryChooser();
                    File selectSaveLocation = dc.showDialog(null);
                    Path path = Paths.get(selectSaveLocation.getAbsolutePath());

                    extraFeatures.saveFileName();
                    fileName = extraFeatures.fileName;
                    outputFilePath = String.format("%s\\\\", path.toString().replaceAll(
                            "\\\\", "\\\\\\\\")) + fileName;

                    inputFilePath = videos.get(videoNumber).getAbsolutePath().replaceAll(
                            "\\\\", "\\\\\\\\");

                    extraFeatures.setFramePerSecond();
                    framesPerNSeconds = extraFeatures.framePerSecond;

                    extractImages();
                } catch (Exception e) {
                    // PASS
                }
            }
        });

        trimVideo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                clipVideo();
            }
        });
    }

    public void clipVideo() {

    }

    public void playPauseMedia() {
        if (playPauseButton.getText().equals("O")) {
            try {
                beginTimer();
                playPauseButton.setText("| |");
                mediaPlayer.play();
            } catch (Exception e) {
                // PASS
            }
        } else if (playPauseButton.getText().equals("| |")) {
            try {
                cancelTimer();
                playPauseButton.setText("O");
                mediaPlayer.pause();
            } catch (Exception e) {
                // PASS
            }
        }
    }

    public void nextMedia() {
        try {
            beginTimer();
            if (videoNumber < videos.size() - 1) {
                videoNumber++;
            } else {
                videoNumber = 0;
            }
            mediaPlayer.stop();

            if (running) {
                cancelTimer();
            }
            media = new Media(videos.get(videoNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            mediaPlayer.play();
        } catch (Exception e) {
            // PASS
        }
    }

    public void previousMedia() {
        try {
            beginTimer();
            if (videoNumber > 0) {
                videoNumber--;
            } else {
                videoNumber = videos.size() - 1;
            }
            mediaPlayer.stop();
            if (running) {
                cancelTimer();
            }
            media = new Media(videos.get(videoNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            mediaPlayer.play();
        } catch (Exception e) {
            // PASS
        }
    }

    public void takeScreenshot() {
        ffmpegCommands.screenshotCommand(String.format("ffmpeg -i \"%s\" -ss %s -frames:v 1 \"%s\"",
                inputFilePath, time, outputFilePath));
    }

    public void extractImages() {
        ffmpegCommands.extractingImages(String.format("ffmpeg -i \"%s\" -vf fps=1/%s \"%s-%s\"",
                inputFilePath, framesPerNSeconds, outputFilePath, imagesNumber));
    }

    public void openLibrary() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("VideoLibrary.fxml"));
            Scene scene = new Scene(root);
            Stage primaryStage = new Stage();
            primaryStage.setTitle("Video Library");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.initOwner(libraryButton.getScene().getWindow());
            primaryStage.show();
        } catch (Exception e) {
            // PASS
        }
    }

    public void changeVideoRatio() {
        if (changeRatioButton.getText().equals("F")) {
            try {
                mediaView.setPreserveRatio(false);
                mediaView.fitWidthProperty().bind(mediaViewLayout.widthProperty());
                mediaView.fitHeightProperty().bind(mediaViewLayout.heightProperty());
                changeRatioButton.setText("N");
            } catch (Exception e) {
                // PASS
            }

        } else if (changeRatioButton.getText().equals("N")) {
            try {
                mediaView.setPreserveRatio(true);
                mediaView.fitWidthProperty().bind(mediaViewLayout.widthProperty());
                mediaView.fitHeightProperty().bind(mediaViewLayout.heightProperty());
                changeRatioButton.setText("F");
            } catch (Exception e) {
                // PASS
            }
        }
    }

    public void muteAudio() {
        if (volumeButton.getText().equals("Mute")) {
            volumeSlider.setValue(0);
            volumeButton.setText("UnMute");
        } else if (volumeButton.getText().equals("UnMute")) {
            volumeSlider.setValue(50);
            volumeButton.setText("Mute");
        }
    }

    public void rgbToMonochrome() {
        ffmpegCommands.convertVideoToMonochrome(String.format("ffmpeg -i \"%s\" -vf monochrome \"%s\"",
                inputFilePath, outputFilePath));
    }

    public void beginTimer() {
        timer = new Timer();
        task = new TimerTask() {
            public void run() {
                try {
                    running = true;
                    while (running) {
                        double current = mediaPlayer.getCurrentTime().toSeconds();
                        double end = media.getDuration().toSeconds();
                        progressBar.setProgress(current/end);
                        hours = (int) mediaPlayer.getCurrentTime().toSeconds() / 3600;
                        minutes = ((int) mediaPlayer.getCurrentTime().toSeconds() % 3600) / 60;
                        seconds = (int) mediaPlayer.getCurrentTime().toSeconds() % 60;
                        time = (String.format("%02d:%02d:%02d", hours, minutes, seconds));

                        if ((current / end * 100) == 100) {
                            cancelTimer();
                            nextMedia();
                        }
                    }
                } catch (Exception e) {
                    System.exit(0);
                    // PASS
                }
            }
        };
        timer.scheduleAtFixedRate(task, 1000, 1000);
    }

    public void cancelTimer() {
        running = false;
        timer.cancel();
    }
}