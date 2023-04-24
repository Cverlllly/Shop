package com.example.shopprod;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Cart_itemController {

    @FXML
    private AnchorPane cart_pane;

    @FXML
    private Button delete_cart;

    @FXML
    private ImageView image;

    @FXML
    private Label name;

    @FXML
    private Label price;

    @FXML
    private ChoiceBox<?> qty;
    private Product product;

    public void setData(Product prod) throws IOException {
        this.product = prod;

        name.setText(product.getName());
        price.setText(String.valueOf(product.getPrice()));
        byte[] img = product.getImg();
        InputStream in = new ByteArrayInputStream(img);
        BufferedImage bufferedImage = ImageIO.read(in);
        Image imgs = SwingFXUtils.toFXImage(bufferedImage, null);
        image.setImage(imgs);
    }


}
