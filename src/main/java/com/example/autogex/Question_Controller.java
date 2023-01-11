package com.example.autogex;

import com.example.autogex.infos.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Question_Controller {
    int step=1;
    @FXML
    TextField maxSize,minSize,fixedSize,prefix,suffix,number,caracterMulti;
    @FXML
    VBox add2,add3,add4;
    @FXML
    HBox hBox2,hBox3,hBox4;
    @FXML
    Label stepLabel;
    Question question = new Question();
    @FXML
    public void initialize(){
        add3.setOnMouseClicked(a->{
            String pref = prefix.getText();
            if(!pref.isEmpty()){
                question.prefix.add(pref);
            }
            prefix.setText("");
            hBox3.getChildren().add(new Label(pref));
        });
        add4.setOnMouseClicked(a->{
            String suff = suffix.getText();
            if(!suff.isEmpty()){
                question.suffix.add(suff);
            }
            suffix.setText("");
            hBox4.getChildren().add(new Label(suff));
        });
        add2.setOnMouseClicked(a->{
            if(!caracterMulti.getText().isEmpty()) {
                Repetition repetition;
                try {
                    repetition = new Repetition(caracterMulti.getText(), Integer.parseInt(number.getText()));
                } catch (NumberFormatException e) {
                    repetition = new Repetition(caracterMulti.getText(), 0);
                }
                question.repetitions.add(repetition);
            }
            hBox2.getChildren().add(new Label(caracterMulti.getText()+" : "+number.getText()+" fois"));
            caracterMulti.setText("");
            number.setText("");
        });
        alertBox();
    }
    @FXML
    protected void onNext(ActionEvent event) throws IOException {
        try {
            stepLabel.setText(String.valueOf(step));
            question.maxSize=Integer.parseInt(maxSize.getText());
            question.minSize=Integer.parseInt(minSize.getText());
            question.fixedSize=Integer.parseInt(fixedSize.getText());
            String suff = suffix.getText();
            if(!suff.isEmpty()){
                question.suffix.add(suff);
            }
        }catch (NumberFormatException ignored){}
        String pref = prefix.getText();
        if(!pref.isEmpty()){
            question.prefix.add(pref);
        }
        if(!caracterMulti.getText().isEmpty()) {
            Repetition repetition;
            try {
                repetition = new Repetition(caracterMulti.getText(), Integer.parseInt(number.getText()));
            } catch (NumberFormatException e) {
                repetition = new Repetition(caracterMulti.getText(), 0);
            }
            question.repetitions.add(repetition);
        }
        AppConst.questionInfo.questions.add(question.copy());
        step++;
        if(step<= AppConst.questionInfo.numberOfPart) {
            fixedSize.setText("");
            minSize.setText("");
            maxSize.setText("");
            hBox2.getChildren().clear();
            hBox3.getChildren().clear();
            hBox4.getChildren().clear();
            fixedSize.setText("");
            prefix.setText("");
            suffix.setText("");
            number.setText("");
            caracterMulti.setText("");
            question=new Question();
            alertBox();
        }else{
            AppConst.type= Type.Question;
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(WelcomeController.class.getResource("verification_view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
            stage.setScene(scene);
        }
    }
    private void  alertBox(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Repondez aux question sur la partie "+step+" de votre mot");
        alert.showAndWait();
    }


}
