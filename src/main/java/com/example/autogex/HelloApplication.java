package com.example.autogex;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        FXMLLoader fxmlLoader2 = new FXMLLoader(HelloApplication.class.getResource("welcome.fxml"));
        Scene scene2 = new Scene(fxmlLoader2.load(), 1000, 600);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.setOnShown(ev->{
            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(5));
            pauseTransition.setOnFinished(e->{
                stage.setScene(scene2);
            });
            pauseTransition.play();
        });
//        stage.initStyle(StageStyle.TRANSPARENT);

       stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}