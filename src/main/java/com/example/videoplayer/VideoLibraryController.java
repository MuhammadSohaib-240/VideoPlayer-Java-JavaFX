package com.example.videoplayer;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

@SuppressWarnings("All")
public class VideoLibraryController extends SQLite3Operations implements Initializable {
    @FXML
    TableView<VideoLibraryModel> tableView;

    @FXML
    TableColumn<VideoLibraryModel, Integer> idColumn;

    @FXML
    TableColumn<VideoLibraryModel, String> nameColumn;

    @FXML
    TableColumn<VideoLibraryModel, String> locationColumn;

    @FXML
    TableColumn<VideoLibraryModel, String> durationColumn;

    @FXML
    Button addButton;

    @FXML
    Button updateButton;

    @FXML
    Button deleteButton;

    @FXML
    TextField searchBar;

    int seconds, minutes, hours;
    String time;

    ////////////////////////////////////////////////////////////
    // OBJECTS TO INTERACT WITH METHODS OF DIFFERENT CLASSES //
    //////////////////////////////////////////////////////////
    ExtraFeatures extraFeatures = new ExtraFeatures();
    FFMPEGCommands ffmpegCommands = new FFMPEGCommands();
    OpenCV openCV = new OpenCV();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //////////////////////////////////////////////////
        // INITIALIZING AND DISPLAYING DATA IN LIBRARY //
        ////////////////////////////////////////////////
        viewData();

        ////////////////////////////////////////
        // BUTTON & SLIDERS ACTION LISTENERS //
        //////////////////////////////////////

        // Add/Insert Video File Button Listener
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                insertVideo();
            }
        });

        // Update Video File Name Button Listener
        updateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updateVideoName();
            }
        });

        // Delete Video File Button Listener
        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                deleteVideo();
            }
        });

        searchBar.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                // Refreshing and displaying searched keyword data
                tableView.getItems().clear();
                idList.clear();
                nameList.clear();
                locationList.clear();
                durationList.clear();
                search();
            }
        });
    }

    public void insertVideo() {
        // Getting & Formatting Input Video Location Path
        FileChooser fileChooser = new FileChooser();
        File inputVideoFile = fileChooser.showOpenDialog(null);
        String inputVideoFileLocation = inputVideoFile.toString().replaceAll(
                "\\\\", "\\\\\\\\");

        // Getting & Formatting Save Location Path
        extraFeatures.saveFileName();
        File outputVideoFile = new File("Videos" + "\\" + extraFeatures.fileName +
                ".mp4");
        String outputVideoFileLocation = outputVideoFile.toString().replaceAll
                ("\\\\", "\\\\\\\\");

        // Copying & Saving File To Built In Library
        ffmpegCommands.copyVideo(String.format("ffmpeg -i \"%s\" -codec copy \"%s\"",
                inputVideoFileLocation, outputVideoFileLocation));

        // Inserting Data Into Database
        insert(extraFeatures.fileName, "D:\\Homework\\DSA\\Project\\Video Player\\Videos\\",
                openCV.getVideoDuration(inputVideoFileLocation));

        // Displaying Data In JavaFX GUI Table
        idColumn.setCellValueFactory(new PropertyValueFactory<VideoLibraryModel, Integer>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<VideoLibraryModel, String>("videoName"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<VideoLibraryModel, String>("videoLocation"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<VideoLibraryModel, String>("videoDuration"));
        VideoLibraryModel videoLibrary = new VideoLibraryModel(idList.get(idList.size() - 1),
                nameList.get(nameList.size() - 1), locationList.get(locationList.size() - 1),
                durationList.get(durationList.size() - 1));
        ObservableList<VideoLibraryModel> videoLibraries = tableView.getItems();
        videoLibraries.add(videoLibrary);
        tableView.setItems(videoLibraries);
    }

    public void updateVideoName() {
        // Fetching Data From The JavaFX GUI Table
        int videoId = tableView.getSelectionModel().getSelectedItem().getId();
        String videoName = tableView.getSelectionModel().getSelectedItem().getVideoName();
        String videoLocation = tableView.getSelectionModel().getSelectedItem().getVideoLocation();
        String videoDuration = tableView.getSelectionModel().getSelectedItem().getVideoDuration();

        // Giving Input Of Updated Video FIle
        extraFeatures.saveFileName();
        String updatedVideoName = extraFeatures.fileName;

        // Update Video File Name In Database
        update(videoId, updatedVideoName, videoLocation, videoDuration);

        // Updates & Renames Video File In The Directory
        extraFeatures.renameVideoFile(videoName, updatedVideoName, videoLocation);

        // Refresh Data
        tableView.getItems().clear();
        idList.clear();
        nameList.clear();
        locationList.clear();
        durationList.clear();
        viewData();
    }

    public void deleteVideo() {
        // Fetching Data From JavaFX GUI Table
        int videoId = tableView.getSelectionModel().getSelectedItem().getId();
        String videoName = tableView.getSelectionModel().getSelectedItem().getVideoName();
        String videoLocation = tableView.getSelectionModel().getSelectedItem().getVideoLocation();

        // Deleting Video File Data From Database
        delete(videoId);

        // Clearing All Data From JavaFX GUI Table
        tableView.getSelectionModel().getTableView().getItems().clear();

        // Delete Video File From The Directory
        extraFeatures.deleteFile(videoName, videoLocation);

        // Refreshing data
        tableView.getItems().clear();
        idList.clear();
        nameList.clear();
        locationList.clear();
        durationList.clear();
        viewData();
    }

    public void viewData() {
        // Fetching Data From Database
        read();

        // Displaying Data In JavaFX GUI Table
        int i = 0;
        while (i < idList.size()){
            try {
                idColumn.setCellValueFactory(new PropertyValueFactory<VideoLibraryModel, Integer>("id"));
                nameColumn.setCellValueFactory(new PropertyValueFactory<VideoLibraryModel, String>("videoName"));
                locationColumn.setCellValueFactory(new PropertyValueFactory<VideoLibraryModel, String>("videoLocation"));
                durationColumn.setCellValueFactory(new PropertyValueFactory<VideoLibraryModel, String>("videoDuration"));
                VideoLibraryModel videoLibrary = new VideoLibraryModel(idList.get(i), nameList.get(i),
                        locationList.get(i), durationList.get(i));
                ObservableList<VideoLibraryModel> videoLibraries = tableView.getItems();
                videoLibraries.add(videoLibrary);
                tableView.setItems(videoLibraries);
                i++;
            } catch (Exception e) {
                System.out.println("Unknown error occurred while reading data!");
            }
        }
    }

    public void search() {
        String word = searchBar.getText().toString();
        searching(word);
        int i = 0;
        while (i < idList.size()){
            try {
                idColumn.setCellValueFactory(new PropertyValueFactory<VideoLibraryModel, Integer>("id"));
                nameColumn.setCellValueFactory(new PropertyValueFactory<VideoLibraryModel, String>("videoName"));
                locationColumn.setCellValueFactory(new PropertyValueFactory<VideoLibraryModel, String>("videoLocation"));
                durationColumn.setCellValueFactory(new PropertyValueFactory<VideoLibraryModel, String>("videoDuration"));
                VideoLibraryModel videoLibrary = new VideoLibraryModel(idList.get(i), nameList.get(i), locationList.get(i),
                        durationList.get(i));
                ObservableList<VideoLibraryModel> videoLibraries = tableView.getItems();
                videoLibraries.add(videoLibrary);
                tableView.setItems(videoLibraries);
                i++;
            } catch (Exception e) {
                System.out.println("Unknown error occured while reading data!");
            }
        }
    }
}
