package com.example.shopprod;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.awt.event.ActionEvent;

public class LoginController {

    @FXML
    private Button already_account;

    @FXML
    private Button create_account;

    @FXML
    private PasswordField log_password;

    @FXML
    private TextField log_username;

    @FXML
    private Button login_button;

    @FXML
    private AnchorPane login_form;

    @FXML
    private Hyperlink no_password;

    @FXML
    private TextField reg_email;

    @FXML
    private PasswordField reg_password;

    @FXML
    private PasswordField reg_re_passowrd;

    @FXML
    private Button reg_signup;

    @FXML
    private TextField reg_username;

    @FXML
    private AnchorPane register_form;

    @FXML
    private AnchorPane side_pane;


    public void SwitchForm(javafx.event.ActionEvent event) {
        TranslateTransition slider=new TranslateTransition();
        if(event.getSource()==create_account){
            slider.setNode(side_pane);
            slider.setToX(400);
            slider.setToZ(20);
            slider.setDuration(Duration.seconds(.5));
            slider.setOnFinished((actionEvent) -> {
                register_form.setVisible(true);
                login_form.setVisible(false);
                create_account.setVisible(false);
                already_account.setVisible(true);
            });
            slider.play();
        }
        else if(event.getSource()==already_account){
            slider.setNode(side_pane);
            slider.setToX(0);
            slider.setToZ(20);
            slider.setDuration(Duration.seconds(.5));
            slider.setOnFinished((actionEvent) -> {
                register_form.setVisible(false);
                login_form.setVisible(true);
                create_account.setVisible(true);
                already_account.setVisible(false);

            });
            slider.play();
        }
        /*-----------------------------------------------------------------------------------------------------------------------*/

    }
}
