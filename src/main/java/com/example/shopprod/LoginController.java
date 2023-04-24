package com.example.shopprod;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class LoginController {

    @FXML
    private Button Backtolog;

    @FXML
    private Button Proccedb;

    @FXML
    private Label alert;

    @FXML
    private Label alert1;

    @FXML
    private Button already_account;

    @FXML
    private Button create_account;

    @FXML
    private AnchorPane forgotpass_form;

    @FXML
    private TextField getusername;

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

    @FXML
    void back(ActionEvent event) {
        login_form.setVisible(true);
        forgotpass_form.setVisible(false);
    }

    @FXML
    void to_forgetpass(ActionEvent event) {
        login_form.setVisible(false);
        forgotpass_form.setVisible(true);
    }

    @FXML
    void SendMail(ActionEvent event) {

    }

    public void SwitchForm(javafx.event.ActionEvent event) throws MalformedURLException {
        TranslateTransition slider = new TranslateTransition();
        if (event.getSource() == create_account) {
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
        } else if (event.getSource() == already_account) {
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
    }

    /*-----------------------------------------------------------------------------------------------------------------------*/
    public static String iduser;

    public void Login(ActionEvent event) throws Exception {
        String id = login(log_username.getText(), log_password.getText());
        if (id.equals("0")) {
            System.out.println("jebi se nimas");
            alert.setText("Incorrect password or username");

            PauseTransition delay = new PauseTransition(Duration.millis(2000));
            delay.setOnFinished(ev -> {
                alert.setText("");
            });
            delay.play();

        } else if(id.equals("1")) {
            iduser = id;
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setContentText("Login Successful");
            alert1.showAndWait();

            Parent root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);

            stage.setTitle("Mainpage");
            stage.setMinHeight(800);
            stage.setMinWidth(1500);

            stage.setScene(scene);
            stage.show();

            login_button.getScene().getWindow().hide();
        }
        else{
            iduser = id;
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setContentText("Login Successful");
            alert1.showAndWait();

            Parent root= FXMLLoader.load(getClass().getResource("Users_panel.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);

            stage.setTitle("Mainpage");
            stage.setMinHeight(800);
            stage.setMinWidth(1400);

            stage.setScene(scene);
            stage.show();

            login_button.getScene().getWindow().hide();
        }
    }

    public static String hashpass(String pass) {
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        m.update(pass.getBytes());
        byte[] bytes = m.digest();
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        pass = s.toString();
        return (pass);
    }

    public void register() throws IOException {
        String username = reg_username.getText();
        String email = reg_email.getText();
        String pass = reg_password.getText();
        String pass2 = reg_re_passowrd.getText();
        if (!(username.isEmpty()) || !(email.isEmpty()) || !(username.isEmpty())) {
            if (pass.equals(pass2)) {
                pass = hashpass(pass);
                URL obj = new URL("http://localhost:8080/user");
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setDoOutput(true);
                JSONObject file = new JSONObject();
                file.put("username", username);
                file.put("email", email);
                file.put("password", pass);

                String jsonInputString = file.toString();

                try (OutputStream os = con.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Something is not working");
                    alert.setContentText("Username already in use try again :)");
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                            System.out.println("Pressed OK.");
                        }
                    });
                }
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;

                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println(response.toString());
                    reg_username.setText("");
                    reg_password.setText("");
                    reg_email.setText("");
                    reg_re_passowrd.setText("");
                    TranslateTransition slider = new TranslateTransition();
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
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Something is not working");
                    alert.setContentText("Username already in use try again :)");
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                            System.out.println("Pressed OK.");
                        }
                    });
                }
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Something is not working");
            alert.setContentText("Please fill out all the fields");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        }
    }

    /*------------------------------------------------------------------______________________________________________________________*/
    public static class user {
        private String users_id;
        private String username;
        private String password;
        private String email;
        private String created;

        public user() {
        }

        public user(String id, String username, String password, String email, String created) {
            this.users_id = id;
            this.username = username;
            this.password = password;
            this.email = email;
            this.created = created;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getId() {
            return users_id;
        }

        public void setId(String id) {
            this.users_id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public String login(String username, String password) throws Exception {
        URL obj = new URL("http://localhost:8080/user/log/" + username + "/" + hashpass(password));
        System.out.println(obj);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        int responsecode = con.getResponseCode();
        System.out.println(responsecode);
        if (responsecode == 200) {
            Scanner sc = new Scanner(obj.openStream());
            String inline = "";
            while (sc.hasNext()) {
                inline += sc.nextLine();
            }
            if (!(inline.equals("[]"))) {
                inline.replace("[", "");
                inline.replace("]", "");
                sc.close();
                ObjectMapper mapper = new ObjectMapper();
                JsonNode arrayNode = mapper.readTree(inline);
                JsonNode objectNode = arrayNode.get(0);
                String id = objectNode.get("users_id").asText();
                return id;
            } else {
                return "0";
            }
        } else {
            return "0";
        }

    }

}