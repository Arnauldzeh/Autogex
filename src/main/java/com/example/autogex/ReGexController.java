package com.example.autogex;

import com.example.autogex.infos.AppConst;
import com.example.autogex.infos.Type;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ReGexController {
    @FXML
    TextField textField;

    @FXML
    protected void onNext(ActionEvent event)throws IOException {
        AppConst.type = Type.ReGex;
        AppConst.regex =textField.getText();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(WelcomeController.class.getResource("verification_view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        stage.setScene(scene);
    }
}
