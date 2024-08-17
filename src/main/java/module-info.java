module com.example.videoplayer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.sql;
    requires opencv;


    opens com.example.videoplayer to javafx.fxml;
    exports com.example.videoplayer;
}