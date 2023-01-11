package com.example.autogex;

import com.example.autogex.infos.AppConst;
import com.example.autogex.infos.TransitionTable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class EtatsFinauxController {
    @FXML
    HBox hBox;
    @FXML
    public void initialize(){
        for (int i = 1; i <= AppConst.transitionTable.numberEtat; i++) {
            Button btn = new Button(String.valueOf(i));
            int finalI = i;
            btn.setOnMouseClicked(a->{
                if(!AppConst.transitionTable.etatsFinaux.contains(finalI)){
                    AppConst.transitionTable.etatsFinaux.add(finalI);
                    btn.setStyle("-fx-background-color: #41E906;");
                }else{
                    AppConst.transitionTable.etatsFinaux.remove(Integer.valueOf(finalI));
                    btn.setStyle("-fx-background-color: #2E5DD6;");
                }
            });
            hBox.getChildren().add(btn);
        }
    }
    @FXML
    protected void onNext(ActionEvent event) throws IOException {
        if(AppConst.transitionTable.etatsFinaux.isEmpty()){
            VerificationController.alertBox("Vous devez avoir au moins 1 etat final ");
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(WelcomeController.class.getResource("add_transition_view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        stage.setScene(scene);
    }
}
