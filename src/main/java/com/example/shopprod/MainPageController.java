package com.example.shopprod;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


public class MainPageController {


    @FXML
    private Button add_btn;

    @FXML
    private Button add_user;

    @FXML
    private TableColumn<?, ?> created_col;

    @FXML
    private TextField creation_date_field;

    @FXML
    private Button customer_button;

    @FXML
    private Button dashboard_button;

    @FXML
    private TableColumn<?, ?> date_column;

    @FXML
    private Button delete_btn;

    @FXML
    private Button delete_user;

    @FXML
    private TextArea descript;

    @FXML
    private TableColumn<?, ?> description_column;

    @FXML
    private ImageView display;

    @FXML
    private TableColumn<?, ?> email_col;

    @FXML
    private TextField email_field;

    @FXML
    private TableColumn<?, ?> id_prod_col;

    @FXML
    private AnchorPane img_display;

    @FXML
    private Button img_import;

    @FXML
    private Button inventory_button;

    @FXML
    private AnchorPane inventory_panel;

    @FXML
    private Button logout_button;

    @FXML
    private TableColumn<?, ?> name_column;

    @FXML
    private Button order_button;

    @FXML
    private TableColumn<?, ?> password_col;

    @FXML
    private TextField password_field;

    @FXML
    private TableColumn<?, ?> price_column;

    @FXML
    private TextField price_field;

    @FXML
    private TextField product_field;

    @FXML
    private TableColumn<?, ?> qty_column;

    @FXML
    private TextField qty_field;

    @FXML
    private TextField serial_field;

    @FXML
    private TableColumn<?, ?> serial_num_Column;

    @FXML
    private TableView<Product> tabel_display;

    @FXML
    private Button update_btn;

    @FXML
    private Button update_user;

    @FXML
    private AnchorPane user_panel;

    @FXML
    private TableView<User> user_tabel_display;

    @FXML
    private TableColumn<?, ?> username_col;

    @FXML
    private TextField username_field;

    @FXML
    private TableColumn<?, ?> users_id;


    public class Product {
        private Long product_id;
        private String name;
        private Double price;
        private String serial_number;
        private String Description;
        private String qty;
        private String lastupdate;
        private byte[] img;

        public Product() {
        }

        public Product(Long product_id, String name, Double price, String serial_number, String description, String qty, String lastupdate, byte[] img) {
            this.product_id = product_id;
            this.name = name;
            this.price = price;
            this.serial_number = serial_number;
            this.Description = description;
            this.qty = qty;
            this.lastupdate = lastupdate;
            this.img = img;
        }

        public Long getProduct_id() {
            return product_id;
        }

        public void setProduct_id(Long product_id) {
            this.product_id = product_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public String getSerial_number() {
            return serial_number;
        }

        public void setSerial_number(String serial_number) {
            this.serial_number = serial_number;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String description) {
            Description = description;
        }

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }

        public String getLastupdate() {
            return lastupdate;
        }

        public void setLastupdate(String lastupdate) {
            this.lastupdate = lastupdate;
        }

        public byte[] getImg() {
            return img;
        }

        public void setImg(byte[] img) {
            this.img = img;
        }
    }

    public void onClickInventory() throws IOException, InterruptedException {
        openInv();
        FillTableProd();
    }

    List<Product> products = new ArrayList<>();

    public void FillTableProd() throws IOException, InterruptedException {
        tabel_display.getItems().clear();
        products.clear();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/product"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response);
        JSONArray jsonArray = (JSONArray) JSONValue.parse(response.body());
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            Long product_id = (Long) jsonObject.get("product_id");
            String serial_number = (String) jsonObject.get("serial_number");
            String name = (String) jsonObject.get("name");
            String description = (String) jsonObject.get("description");
            String qty = (String) jsonObject.get("qty");
            Object date = jsonObject.get("lastupdate");
            String dt = date.toString().replace("T", " ");
            String[] parts = dt.split(" ");
            Double price = (Double) jsonObject.get("price");
            String imageString = (String) jsonObject.get("image");
            if (imageString.equals(null)) {
                System.out.println(imageString);
            } else {
                byte[] imageBytes = Base64.getDecoder().decode(imageString);
                System.out.println(imageBytes);
                Product product = new Product(product_id, name, price, serial_number, description, qty, parts[0], imageBytes);
                products.add(product);
            }

        }
        id_prod_col.setCellValueFactory(new PropertyValueFactory<>("product_id"));
        name_column.setCellValueFactory(new PropertyValueFactory<>("name"));
        serial_num_Column.setCellValueFactory(new PropertyValueFactory<>("serial_number"));
        price_column.setCellValueFactory(new PropertyValueFactory<>("price"));
        description_column.setCellValueFactory(new PropertyValueFactory<>("description"));
        qty_column.setCellValueFactory(new PropertyValueFactory<>("qty"));
        date_column.setCellValueFactory(new PropertyValueFactory<>("lastupdate"));

        ObservableList<Product> observableList = FXCollections.observableArrayList(products);
        tabel_display.setItems(observableList);
    }

    public void onClickAdd() throws IOException, InterruptedException {
        String name = product_field.getText();
        String price = price_field.getText();
        String s_n = serial_field.getText();
        String qty = qty_field.getText();
        String desription = descript.getText();
        Double price1 = Double.parseDouble(price);


        if (!(name.isEmpty()) || !(price.isEmpty()) || !(s_n.isEmpty()) || !(qty.isEmpty()) || !(desription.isEmpty())) {
            URL obj = new URL("http://localhost:8080/product");
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            org.json.simple.JSONObject file = new org.json.simple.JSONObject();
            file.put("name", name);
            file.put("price", price1);
            file.put("serial_number", s_n);
            file.put("qty", qty);
            file.put("description", desription);
            file.put("image", test);

            String jsonInputString = file.toString();

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Something is not working");
                alert.setContentText(" something went wrong:)");
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
                product_field.setText("");
                price_field.setText("");
                serial_field.setText("");
                descript.setText("");
                qty_field.setText("");
                display.setImage(null);
                FillTableProd();
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

    Integer index = -1;
    Object id = 0;

    public void fillFields() {
        index = tabel_display.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        System.out.println(id_prod_col.getCellData(index).toString());
        id = id_prod_col.getCellData(index);
        product_field.setText(name_column.getCellData(index).toString());
        serial_field.setText(serial_num_Column.getCellData(index).toString());
        price_field.setText(price_column.getCellData(index).toString());
        qty_field.setText(qty_column.getCellData(index).toString());
        descript.setText(description_column.getCellData(index).toString());
        List<Product> productList = products;
        for (Product product : productList) {
            Long productName = product.getProduct_id();
            if (id == productName) {
                System.out.println("id: " + productName);
                try {
                    byte[] img = product.getImg();
                    InputStream in = new ByteArrayInputStream(img);
                    BufferedImage bufferedImage = ImageIO.read(in);
                    Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                    display.setImage(image);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("file might be the wrong type");
                }

            }

        }
    }

    public void updateProduct() throws IOException, InterruptedException {
        String name = product_field.getText();
        String price = price_field.getText();
        String s_n = serial_field.getText();
        String qty = qty_field.getText();
        String desription = descript.getText();
        Double price1 = Double.parseDouble(price);


        if (!(name.isEmpty()) || !(price.isEmpty()) || !(s_n.isEmpty()) || !(qty.isEmpty()) || !(desription.isEmpty())) {
            URL obj = new URL("http://localhost:8080/product");
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            org.json.simple.JSONObject file = new org.json.simple.JSONObject();
            Long prd_id = (Long) id;
            file.put("product_id", prd_id);
            file.put("name", name);
            file.put("price", price1);
            file.put("serial_number", s_n);
            file.put("qty", qty);
            file.put("description", desription);
            file.put("image", test);

            String jsonInputString = file.toString();

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Something is not working");
                alert.setContentText(" something went wrong:)");
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
                product_field.setText("");
                price_field.setText("");
                serial_field.setText("");
                descript.setText("");
                qty_field.setText("");
                FillTableProd();
            }
        }
    }

    public void Delete() throws IOException, InterruptedException {
        if (id != null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Are u sure");
            alert.setContentText("Are you sure u want to delete this product " + product_field.getText() + " with serial number " + serial_field.getText());
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                    Long idd = (Long) id;
                    URL obj = null;
                    try {
                        obj = new URL("http://localhost:8080/product/" + idd);
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                    HttpURLConnection connection = null;
                    try {
                        connection = (HttpURLConnection) obj.openConnection();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        connection.setRequestMethod("DELETE");
                    } catch (ProtocolException e) {
                        throw new RuntimeException(e);
                    }
                    int responseCode = 0;
                    try {
                        responseCode = connection.getResponseCode();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(responseCode);
                    try {
                        FillTableProd();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

        }
    }

    //____________________________________________________________________________________________________________________________________________________________________________//
    public class User {
        private Long users_id;
        private String username;
        private String password;
        private String email;
        private String created;

        public User() {
        }

        public User(Long users_id, String username, String password, String email, String created) {
            this.users_id = users_id;
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

        public Long getUsers_id() {
            return users_id;
        }

        public void setUsers_id(Long users_id) {
            this.users_id = users_id;
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

    Object user_id = 0;

    public void fillUsers() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/user"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<User> products = new ArrayList<>();
        JSONArray jsonArray = (JSONArray) JSONValue.parse(response.body());
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);

            Long users_id = (Long) jsonObject.get("users_id");
            String username = (String) jsonObject.get("username");
            String password = (String) jsonObject.get("password");
            String email = (String) jsonObject.get("email");
            Object date = jsonObject.get("created");
            String dt = date.toString().replace("T", " ");
            String[] parts = dt.split(" ");
            User us = new User(users_id, username, password, email, parts[0]);
            products.add(us);
        }
        users_id.setCellValueFactory(new PropertyValueFactory<>("users_id"));
        username_col.setCellValueFactory(new PropertyValueFactory<>("username"));
        password_col.setCellValueFactory(new PropertyValueFactory<>("password"));
        email_col.setCellValueFactory(new PropertyValueFactory<>("email"));
        created_col.setCellValueFactory(new PropertyValueFactory<>("created"));

        ObservableList<User> observableList = FXCollections.observableArrayList(products);
        user_tabel_display.setItems(observableList);
    }

    public void Customers() throws IOException, InterruptedException {
        fillUsers();
        openCust();
    }

    public void addUser() throws IOException, InterruptedException {
        String username = username_field.getText();
        String password = password_field.getText();
        String email = email_field.getText();
        String pass = LoginController.hashpass(password);
        if (!(username.isEmpty()) || !(password.isEmpty()) || !(email.isEmpty())) {
            URL obj = new URL("http://localhost:8080/user");
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            org.json.simple.JSONObject file = new org.json.simple.JSONObject();
            file.put("username", username);
            file.put("password", pass);
            file.put("email", email);

            String jsonInputString = file.toString();

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Something is not working");
                alert.setContentText(" something went wrong:)");
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
                username_field.setText("");
                password_field.setText("");
                email_field.setText("");
                fillUsers();
            }
            catch (Exception e){
                Alert al=new Alert(Alert.AlertType.ERROR);
                al.setContentText("Username must be unique!");
                al.show();
            }
        }
    }

    String pass;

    public void filluserstable() {

        index = user_tabel_display.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        pass = password_col.getCellData(index).toString();
        user_id = users_id.getCellData(index);
        username_field.setText(username_col.getCellData(index).toString());
        password_field.setText(password_col.getCellData(index).toString());
        email_field.setText(email_col.getCellData(index).toString());
        creation_date_field.setText(created_col.getCellData(index).toString());
        System.out.println(user_id);
        chckpass();
    }

    public void chckpass() {
        if (pass.equals(password_field.getText())) {
            update_user.setDisable(false);
        } else {
            update_user.setDisable(true);
        }
    }

    public void openInv() {
        inventory_panel.setVisible(true);
        user_panel.setVisible(false);
    }

    public void openCust() {
        inventory_panel.setVisible(false);
        user_panel.setVisible(true);
    }

    public void Logout() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);

        stage.setTitle("Login");
        stage.setMinHeight(520);
        stage.setMinWidth(800);

        stage.setScene(scene);
        stage.show();
        logout_button.getScene().getWindow().hide();
    }

    String test;

    public void import_img() throws IOException {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            byte[] fileBytes = FileUtils.readFileToByteArray(selectedFile);
            Image image = new Image(new FileInputStream(selectedFile));
            test = Base64.getEncoder().encodeToString(fileBytes);
            System.out.println(test);
            display.setImage(image);
        }

    }

    public void delete_user() {
        if (user_id != null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Are u sure");
            alert.setContentText("Are you sure u want to delete this user " + username_field.getText() + " with id " + user_id.toString());
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                    Long idd = (Long) user_id;
                    URL obj = null;
                    try {
                        obj = new URL("http://localhost:8080/user/" + idd);
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                    HttpURLConnection connection = null;
                    try {
                        connection = (HttpURLConnection) obj.openConnection();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        connection.setRequestMethod("DELETE");
                    } catch (ProtocolException e) {
                        throw new RuntimeException(e);
                    }
                    int responseCode = 0;
                    try {
                        responseCode = connection.getResponseCode();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(responseCode);
                    try {
                        fillUsers();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

        }
    }

}