package com.example.videoplayer;

import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ExtraFeatures implements Initializable {
    String fileName = "";
    int framePerSecond;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void saveFileName() {
        try {
            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("Output File Name");
            dialog.setHeaderText("Enter file name:");
            dialog.setContentText("File Name:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(name -> {
                fileName = name;
            });
        } catch (Exception e) {
            // PASS
        }
    }

    public void setFramePerSecond() {
        try {
            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("Set Frame Per N Seconds");
            dialog.setHeaderText("Enter frame per n seconds:");
            dialog.setContentText("Frame Per N Second:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(frame -> {
                framePerSecond = Integer.parseInt(frame);
            });
        } catch (Exception e) {
            // PASS
        }
    }

    public void informationAlert(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void renameVideoFile(String videoName, String updatedFileName, String videoLocation) {
        File file = new File(videoLocation + "//" + videoName + ".mp4");
        File rename = new File(videoLocation + "//" + updatedFileName + ".mp4");
        boolean flag = file.renameTo(rename);
        if (flag == true) {
            informationAlert("Information Alert", null, "Video File Updated Successfully!");
        }
        else {
            // PASS
        }
    }

    public void deleteFile(String videoName, String videoLocation) {
        File file = new File(videoLocation + "//" + videoName + ".mp4");
        if (file.delete()) {
            informationAlert("Information Dialog", null,
                    "Video Deleted From Library Successfully!");
        }
        else {
            // PASS
        }
    }
}
