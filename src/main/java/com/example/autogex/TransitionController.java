package com.example.autogex;

import com.example.autogex.infos.AppConst;
import com.example.autogex.infos.Transition;
import com.example.autogex.infos.TransitionTable;
import com.example.autogex.infos.Type;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TransitionController {
    @FXML
    Label etat,error;

    @FXML
    HBox hBox;

    @FXML
    TextField symbole,nextEtat;

    int step = 1;
    @FXML
    public void initialize(){
        etat.setText(String.valueOf(step));
        alertBox();

    }
    private void  alertBox(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Entrer les transitions quittant de l'etat "+step);
        alert.showAndWait();
    }
    @FXML
    protected void onNext(ActionEvent event)throws IOException {
        addTransition(null);
        hBox.getChildren().clear();
        error.setText("");
        symbole.setText("");
        nextEtat.setText("");
        if(step< AppConst.transitionTable.numberEtat){
            step++;
            alertBox();
            etat.setText(String.valueOf(step));
        }else {
            AppConst.type = Type.Transition;
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(WelcomeController.class.getResource("verification_view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
            stage.setScene(scene);
        }
    }

    @FXML
    protected void addTransition(MouseEvent event){
        final String sym = symbole.getText();
        int next =0;
        try{
            next = Integer.parseInt(nextEtat.getText());
        }catch (NumberFormatException e){
            error.setText("L'etat de destination doit etre un nombre");
            return;
        }
        if(next>AppConst.transitionTable.numberEtat || next<=0){
            if(event!=null){
                error.setText("Entrer un etats valide");
            }
            return;
        }
        if(sym.isEmpty()){
            error.setText("Entrer un symbole valide");
            return;
        }
        error.setText("");
        hBox.getChildren().add(new Button(step+"--"+sym+"-->"+next));
        Transition transition = new Transition(step,next,new ArrayList<>(List.of(sym.split(" "))));
        AppConst.transitionTable.transitions.add(transition);
        symbole.setText("");
        nextEtat.setText("");
    }
}
