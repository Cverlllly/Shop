package com.example.shopprod;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.ResourceBundle;

import static com.example.shopprod.LoginController.iduser;


public class UsersPanelController implements Initializable {

    @FXML
    private AnchorPane bckToMain;

    @FXML
    private Button cart;

    @FXML
    private AnchorPane cart_pane;

    @FXML
    private ScrollPane cart_scroll;

    @FXML
    private GridPane display_cart;

    @FXML
    private GridPane displayitems;

    @FXML
    private Button finish_button;

    @FXML
    private ContextMenu menu;

    @FXML
    Label number_items;

    @FXML
    private ScrollPane scroll_pane;

    @FXML
    private TextField searchBar;

    @FXML
    private Button search_btn;

    @FXML
    private AnchorPane shop_pane;

    @FXML
    private StackPane sp;

    @FXML
    private Label total;

    @FXML
    private Button user;


    private ObservableList<Product> CardData = FXCollections.observableArrayList();

    public ObservableList<Product> getData() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/product")).GET().build();
        ObservableList<Product> listdata = FXCollections.observableArrayList();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
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
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.show();
            } else {
                byte[] imageBytes = Base64.getDecoder().decode(imageString);
                Product product = new Product(product_id, name, price, serial_number, description, qty, parts[0], imageBytes);
                listdata.add(product);
            }
        }
        return listdata;

    }

    public Double totalamount=0.0;

    public void DisplayItems() throws IOException, InterruptedException {
        CardData.clear();
        CardData.addAll(getData());

        int row = 0;
        int column = 0;
        displayitems.getRowConstraints().clear();
        displayitems.getColumnConstraints().clear();
        for (int i = 0; i < CardData.size(); i++) {
            FXMLLoader load = new FXMLLoader();
            load.setLocation(getClass().getResource("item.fxml"));
            AnchorPane pane = load.load();
            ItemController item = load.getController();
            item.setData(CardData.get(i));
            if (column == 3) {
                column = 0;
                row += 1;
            }
            displayitems.add(pane, column++, row);
            x += 1;
        }
    }

    static int x = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (x > 0) {
        } else {
            try {
                DisplayItems();
                getAllOrders();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public List<Product> getAllOrders() throws IOException, InterruptedException {
        totalamount=0.0;
        List<Product> prod_list = new ArrayList<>();
        prod_list.clear();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/order_item"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONArray jsonArray = (JSONArray) JSONValue.parse(response.body());
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            Long id = (Long) jsonObject.get("order_item_id");
            JSONObject order = (JSONObject) jsonObject.get("order");
            JSONObject prod = (JSONObject) jsonObject.get("product");
            Long quantity = (Long) jsonObject.get("qty");
            Long order_id = (Long) order.get("order_id");
            Long prod_id = (Long) prod.get("product_id");
            JSONObject user = (JSONObject) order.get("user");
            Long user_id = (Long) user.get("users_id");
            if (order_id == checkOrder()) {
                System.out.println("User: " + user_id + " Prduct: " + prod_id);
                String prod_name = (String) prod.get("name");
                Double prod_price = (Double) prod.get("price");
                totalamount=totalamount+prod_price;
                String imageString = (String) prod.get("image");
                if (imageString == null) {
                    System.out.println(imageString);
                } else {
                    byte[] imageBytes = Base64.getDecoder().decode(imageString);
                    String qty = quantity.toString();
                    Product testProd = new Product(prod_name, prod_price, imageBytes, qty);
                    prod_list.add(testProd);
                }
            }
        }
        total.setText("Total amount: "+String.valueOf(totalamount)+"€");
        number_items.setText(String.valueOf(prod_list.size()));
        return prod_list;
    }

    public Long checkOrder() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/order"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONArray jsonArray = (JSONArray) JSONValue.parse(response.body());
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            Long order_id = (Long) jsonObject.get("order_id");

            String date = (String) jsonObject.get("date_created");

            Long confirmation_number = (Long) jsonObject.get("confirmation_number");

            Double amount = (Double) jsonObject.get("amount");

            Boolean done = (Boolean) jsonObject.get("done");


            String dt = date.toString().replace("T", " ");
            String[] parts = dt.split(" ");
            JSONObject user = (JSONObject) jsonObject.get("user");
            Long userId = (Long) user.get("users_id");
            Long id = Long.valueOf(iduser);
            if (userId == id && !done) {
                return order_id;
            }
        }
        return 0L;
    }

    private ObservableList<Product> cart_item = FXCollections.observableArrayList();

    public void Cart_items() throws IOException, InterruptedException {
        cart_item.clear();
        cart_item.addAll(getAllOrders());

        int row = 0;
        int column = 0;
        display_cart.getRowConstraints().clear();
        display_cart.getColumnConstraints().clear();
        for (int i = 0; i < cart_item.size(); i++) {
            FXMLLoader load = new FXMLLoader();
            load.setLocation(getClass().getResource("cart_item.fxml"));
            AnchorPane cart_pane = load.load();
            Cart_itemController item = load.getController();
            item.setData(cart_item.get(i));
            if (column == 1) {
                column = 0;
                row += 1;
            }
            display_cart.add(cart_pane, column++, row);
        }
    }

    public void onClickCart() throws IOException, InterruptedException {
        shop_pane.setVisible(false);
        cart_pane.setVisible(true);
        Cart_items();
    }
    public void backToShop(){
        shop_pane.setVisible(true);
        cart_pane.setVisible(false);
    }
}
