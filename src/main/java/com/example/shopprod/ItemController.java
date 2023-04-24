package com.example.shopprod;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.example.shopprod.LoginController.iduser;

public class ItemController implements Initializable {

    @FXML
    private Button add_item;

    @FXML
    private Button more_item;

    @FXML
    private AnchorPane prod_cardform;

    @FXML
    private ImageView prod_img;

    @FXML
    private Label prod_name;

    @FXML
    private Label prod_price;
    private Image img;

    private Product product;

    public void setData(Product prod) throws IOException {
        this.product = prod;

        prod_name.setText(product.getName());
        prod_price.setText(String.valueOf(product.getPrice()));
        byte[] img = product.getImg();
        InputStream in = new ByteArrayInputStream(img);
        BufferedImage bufferedImage = ImageIO.read(in);
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
        prod_img.setImage(image);
        more_item.setOnAction(event -> {
        });
        add_item.setOnAction(actionEvent -> {
            try {
                if (checkOrder().equals(0L)) {
                    LocalDate currentDate = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String formattedDate = currentDate.format(formatter);
                    System.out.println(formattedDate);
                    CreateNewOrder(formattedDate, generate_conNumber(), prod.getPrice(), false);
                } else {
                    CreateNewOrder_item(prod, 2);
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }

        });
    }
    public void CreateNewOrder(String dataCreted, Integer ConNumber, Double amount, Boolean done) throws IOException {
        URL obj = new URL("http://localhost:8080/order");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        JSONObject jsonOrder = new JSONObject();
        jsonOrder.put("date_created", dataCreted);
        jsonOrder.put("confirmation_number", ConNumber);
        jsonOrder.put("amount", amount);
        jsonOrder.put("done", done);
        JSONObject jsonUser = new JSONObject();
        jsonUser.put("users_id", iduser);
        jsonOrder.put("user", jsonUser);
        String jsonInputString = jsonOrder.toString();
        System.out.println(jsonInputString);
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
        }
    }

    public Integer generate_conNumber() {
        Random rand = new Random();
        int randomNum = rand.nextInt(900000) + 100000;
        return randomNum;
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

    public void CreateNewOrder_item(Product prod, Integer qty) throws IOException, InterruptedException {
        URL obj = new URL("http://localhost:8080/order_item");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        JSONObject jsonOrderItem = new JSONObject();
        JSONObject jsonOrder = new JSONObject();

        JSONObject jsonProduct = new JSONObject();
        jsonProduct.put("product_id", prod.getProduct_id());
        jsonOrder.put("order_id", checkOrder());

        jsonOrderItem.put("order", jsonOrder);
        jsonOrderItem.put("product", jsonProduct);
        jsonOrderItem.put("qty", qty);


        String jsonInputString = jsonOrderItem.toString();

        System.out.println(jsonInputString);
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
        }
        getordercount();
    }

    public List<Product> getordercount() throws IOException, InterruptedException {
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
            JSONObject order=(JSONObject) jsonObject.get("order");
            JSONObject prod = (JSONObject) jsonObject.get("product");
            Long quantity = (Long) jsonObject.get("qty");
            Long order_id=(Long) order.get("order_id");
            Long prod_id=(Long) prod.get("product_id");
            JSONObject user=(JSONObject) order.get("user");
            Long user_id=(Long) user.get("users_id");
            if(order_id==checkOrder()){
                System.out.println("User: "+user_id+" Prduct: "+prod_id);
                String prod_name=(String) prod.get("name");
                Double prod_price=(Double) prod.get("price");
                String imageString = (String) prod.get("image");
                if (imageString == null) {
                    System.out.println(imageString);
                } else {
                    byte[] imageBytes = Base64.getDecoder().decode(imageString);
                    String qty= quantity.toString();
                    Product testProd=new Product(prod_name,prod_price,imageBytes,qty);
                    prod_list.add(testProd);
                }
            }
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Users_panel.fxml"));
        StackPane sp = loader.load();
        UsersPanelController controller = loader.getController();
        System.out.println(prod_list.size());
        controller.number_items.setText("");
        controller.number_items.setText(String.valueOf(prod_list.size()));
        return prod_list;
    }


    public Integer as=0;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
