package com.bluelab.main;


import java.io.IOException;

import com.bluelab.client.ClientCtrl;
import com.bluelab.job.JobCtrl;
import com.bluelab.jobprice.JobPriceCtrl;
import com.bluelab.payment.PaymentCtrl;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;


public class MainPanelCtrl {

    @FXML
    private JFXHamburger burguer;
    
    @FXML
    private VBox vboxMenu;

    @FXML
    private HBox mainPanel;

    @FXML
    private ToggleButton btnClient;

    @FXML
    private ToggleGroup menu;

    @FXML
    private ToggleButton btnTables;

    @FXML
    private ToggleButton btnJobs;

    @FXML
    private ToggleButton btnInvoice;

    @FXML
    private ToggleButton btnStatistic;

    private AnchorPane clientPanel;

    private AnchorPane jobPricePanel;

    private AnchorPane jobPanel;

    private AnchorPane paymentPanel;

    public void initialize() {
        try {
            FXMLLoader loader = new FXMLLoader(ClientCtrl.class.getResource("Client.fxml"));
            clientPanel = loader.load();

            loader = new FXMLLoader(PaymentCtrl.class.getResource("Payment.fxml"));
            paymentPanel = loader.load();

            loader = new FXMLLoader(JobCtrl.class.getResource("Job.fxml"));
            jobPanel = loader.load();

            loader = new FXMLLoader(JobPriceCtrl.class.getResource("JobPrice.fxml"));
            jobPricePanel = loader.load();

            HBox.setHgrow(clientPanel, Priority.ALWAYS);
            HBox.setHgrow(jobPricePanel, Priority.ALWAYS);
            HBox.setHgrow(jobPanel, Priority.ALWAYS);
            HBox.setHgrow(paymentPanel, Priority.ALWAYS);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(burguer);

        transition.setRate(-1);
        burguer.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
            closeMenu(transition.getRate() * -1 == -1 ? true : false);
            transition.setRate(transition.getRate() * -1);
            transition.play();
        });

        closeMenu(true);

    }

    private void closeMenu(boolean b) {
        if (b) {
            btnClient.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            btnJobs.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            btnTables.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            btnInvoice.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            btnStatistic.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            vboxMenu.setPrefWidth(90.0);
        }
        else {
            btnClient.setContentDisplay(ContentDisplay.LEFT);
            btnJobs.setContentDisplay(ContentDisplay.LEFT);
            btnTables.setContentDisplay(ContentDisplay.LEFT);
            btnInvoice.setContentDisplay(ContentDisplay.LEFT);
            btnStatistic.setContentDisplay(ContentDisplay.LEFT);
            vboxMenu.setPrefWidth(220.0);
        }
    }

    @FXML
    void loadClient() {
        if (mainPanel.getChildren().size() > 1)
            mainPanel.getChildren().remove(1);

        mainPanel.getChildren().add(clientPanel);

        if (!btnClient.isSelected())
            btnClient.setSelected(true);

    }

    @FXML
    void loadJob() {
        if (mainPanel.getChildren().size() > 1)
            mainPanel.getChildren().remove(1);

        mainPanel.getChildren().add(jobPanel);

        if (!btnJobs.isSelected())
            btnJobs.setSelected(true);
    }

    @FXML
    void loadTables() {
        if (mainPanel.getChildren().size() > 1)
            mainPanel.getChildren().remove(1);

        mainPanel.getChildren().add(jobPricePanel);

        if (!btnTables.isSelected())
            btnTables.setSelected(true);
    }

    @FXML
    void loadInvoice() {
        if (mainPanel.getChildren().size() > 1)
            mainPanel.getChildren().remove(1);

        mainPanel.getChildren().add(paymentPanel);

        if (!btnInvoice.isSelected())
            btnInvoice.setSelected(true);
    }
}
