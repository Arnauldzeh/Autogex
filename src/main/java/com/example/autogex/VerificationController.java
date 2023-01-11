package com.example.autogex;

import com.example.autogex.automate.AutomateQuestion;
import com.example.autogex.automate.AutomateRegex;
import com.example.autogex.automate.AutomateTransition;
import com.example.autogex.infos.AppConst;
import com.example.autogex.infos.Type;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class VerificationController {
    @FXML
    TextField textField;
    @FXML
    HBox hBox;

    @FXML
    Label regex;

    @FXML    protected void onNext(ActionEvent event){
        try {
            if(AppConst.type.equals(Type.Transition)){
                AutomateTransition automate = new AutomateTransition(AppConst.transitionTable);
                boolean result = automate.verifieWord(textField.getText());
                if(result){
                    alertBox("Ce mot appartient a Ce langage");
                }else {
                    alertBox("Ce mot n'appartient pas a Ce langage");
                }
            }else if(AppConst.type.equals(Type.ReGex)){
                AutomateRegex automate = new AutomateRegex();
                boolean result = automate.verifieWord(textField.getText());
                if(result){
                    alertBox("Ce mot appartient a Ce langage");
                }else {
                    alertBox("Ce mot n'appartient pas a Ce langage");
                }
            }else if(AppConst.type.equals(Type.Question)){
                AutomateQuestion automate = new AutomateQuestion();
                boolean result = automate.verifieWord(textField.getText());
                if(result){
                    alertBox("Ce mot appartient a Ce langage");
                }else {
                    alertBox("Ce mot n'appartient pas a Ce langage");
                }
            }
        }catch (Exception e){
            alertBox(e.getMessage());
        }
    }
    public static void  alertBox(String str){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(str);
        alert.showAndWait();
    }
    @FXML
    protected void menu(ActionEvent event) throws IOException{
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(WelcomeController.class.getResource("SelectTypeView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        stage.setScene(scene);
    }

    @FXML
    public void initialize(){
        if(AppConst.type.equals(Type.ReGex)){
            hBox.setVisible(true);
            regex.setText(AppConst.regex);
        }
        if(AppConst.type.equals(Type.Question)){
            hBox.setVisible(true);
            AutomateQuestion automate = new AutomateQuestion();
            regex.setText(automate.getRegex());
        }
    }


}
