package com.bluelab.job;


import java.util.Date;

import com.bluelab.client.Client;
import com.bluelab.database.DBClient;
import com.bluelab.database.DBJob;
import com.bluelab.database.DBJobPrice;
import com.bluelab.database.DBProductColor;
import com.bluelab.jobprice.JobPrice;
import com.bluelab.productcolor.ProductColor;
import com.bluelab.util.AlertDialog;
import com.bluelab.util.ComboBoxSearch;
import com.bluelab.util.DateUtil;
import com.bluelab.util.FxmlInterface;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;


public class JobCtrl implements FxmlInterface {

    @FXML
    private GridPane grid;

    @FXML
    private Spinner<Integer> jobQtd;

    @FXML
    private Spinner<Double> shipping;

    @FXML
    private Spinner<Double> repetitionValue;

    @FXML
    private JFXDatePicker date;

    @FXML
    private ComboBoxSearch<Client> client;

    @FXML
    private JFXComboBox<JobPrice> jobPrice;

    @FXML
    private JFXComboBox<ProductColor> productColor;

    @FXML
    private TableView<Job> viewJob;

    @FXML
    private TableColumn<Job, Client> colClient;

    @FXML
    private TableColumn<Job, Job> colJobType;

    @FXML
    private TableColumn<Job, ProductColor> colProductColor;

    @FXML
    private TableColumn<Job, Date> colDate;

    @FXML
    private JFXCheckBox repetition;

    @FXML
    private JFXCheckBox nocost;

    @FXML
    private Label lblTotal;
    
    @FXML
    private JFXTextArea obs;

    private Job currentJob;

    public void initialize() {
        client = new ComboBoxSearch<Client>();
        grid.getChildren().add(client);

        client.setMaxWidth(Double.MAX_VALUE);

        GridPane.setColumnIndex(client, 1);
        GridPane.setRowIndex(client, 1);

        client.setItems(DBClient.getList());
        productColor.setItems(DBProductColor.getList());
        productColor.getSelectionModel().select(19);
        viewJob.setItems(DBJob.getList());

        date.setValue(DateUtil.localDateNow());

        createColumns();

        viewJob.setOnMouseClicked(event -> {
            currentJob = viewJob.getSelectionModel().getSelectedItem();
            if (currentJob != null)
                loadJob(currentJob);
        });

        client.setOnAction(event -> {
            Client c = client.getSelectionModel().getSelectedItem();

            if (c == null)
                return;

            jobPrice.setItems(FXCollections.observableArrayList(DBJob.list(c.getPriceTable().getId())));

        });

        jobQtd.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100));
        jobQtd.getValueFactory().setValue(1);
        jobQtd.valueProperty().addListener((obs, oldValue, newValue) -> calcTotal());

        shipping.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1000));
        shipping.getValueFactory().setValue(0.0);
        shipping.valueProperty().addListener((obs, oldValue, newValue) -> calcTotal());

        repetitionValue.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 10000));
        repetitionValue.getValueFactory().setValue(0.0);
        repetitionValue.valueProperty().addListener((obs, oldValue, newValue) -> calcTotal());

        repetitionValue.setOnKeyPressed(event -> {
            calcTotal();
        });

        repetition.setOnAction(event -> {
            repetitionValue.getValueFactory().setValue(0.0);
            repetitionValue.setDisable(!repetition.isSelected());

            calcTotal();
        });

        nocost.setOnAction(event -> {
            calcTotal();
        });

    }

    private void createColumns() {
        colClient.setCellValueFactory(new PropertyValueFactory<>("client"));
        colJobType.setCellValueFactory(new PropertyValueFactory<>("jobPrice"));
        colProductColor.setCellValueFactory(new PropertyValueFactory<>("productColor"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    private void clearFields() {
        client.getSelectionModel().clearSelection();
        jobPrice.getSelectionModel().clearSelection();
        productColor.getSelectionModel().select(19);
        jobQtd.getValueFactory().setValue(1);
        shipping.getValueFactory().setValue(0.0);
        date.setValue(DateUtil.localDateNow());
        repetition.setSelected(false);
        nocost.setSelected(false);
        repetitionValue.setDisable(true);
        obs.clear();
    }

    @Override
    public void disableFields(boolean d) {
        client.setDisable(d);
        jobPrice.setDisable(d);
        productColor.setDisable(d);
        jobQtd.setDisable(d);
        shipping.setDisable(d);
        date.setDisable(d);
        repetition.setDisable(d);
        nocost.setDisable(d);
        obs.setDisable(d);
    }

    private void loadJob(Job j) {
        client.getSelectionModel().select(j.getClient());
        jobPrice.getSelectionModel().select(j.getJobPrice());
        productColor.getSelectionModel().select(j.getProductColor());
        jobQtd.getValueFactory().setValue(j.getQtd());
        shipping.getValueFactory().setValue(j.getShipping());
        date.setValue(DateUtil.toLocalDate(j.getDate()));
        repetition.setSelected(j.isRepetition());
        nocost.setSelected(j.isNocost());
        repetitionValue.getValueFactory().setValue(j.getRepValue());
        obs.setText(j.getObs());

        disableFields(true);
    }

    @FXML
    void verifyPrice() {
        JobPrice j = jobPrice.getValue();

        if (j == null)
            return;

        if (j.getPrice() == 0) {
            double newValue = AlertDialog.insertPriceAlert(j);
            if (newValue != 0) {
                j.setPrice(newValue);
                DBJobPrice.update(j);
            }
        }
        calcTotal();
    }

    void calcTotal() {
        JobPrice jp = jobPrice.getSelectionModel().getSelectedItem();

        if (jp == null)
            return;

        if (jobQtd.getValue() == null)
            jobQtd.getValueFactory().setValue(0);

        if (shipping.getValue() == null)
            shipping.getValueFactory().setValue(0.0);

        int nc = nocost.isSelected() || (repetition.isSelected() && repetitionValue.getValue() == 0) ? 0 : 1;

        double price = repetitionValue.getValue() == null || repetitionValue.getValue() == 0 ? jp.getPrice()
                : repetitionValue.getValue();

        double total = ((price * jobQtd.getValue()) * nc) + shipping.getValue();
        lblTotal.setText(String.valueOf(total));
    }

    @FXML
    @Override
    public void create() {
        currentJob = null;
        clearFields();
        disableFields(false);
    }

    @FXML
    @Override
    public void edit() {
        disableFields(false);
    }

    @FXML
    @Override
    public void save() {
        Job j = new Job();

        j.setClient(client.getValue());
        j.setJobPrice(jobPrice.getValue());
        j.setProductColor(productColor.getValue());
        j.setQtd(jobQtd.getValue());
        j.setShipping(shipping.getValue());
        j.setDate(DateUtil.toDate(date.getValue()));
        j.setRepetition(repetition.isSelected());
        j.setNocost(nocost.isSelected());
        j.setTotal(Double.parseDouble(lblTotal.getText()));
        j.setRepValue(repetitionValue.getValue());
        j.setObs(obs.getText());

        if (currentJob != null) {
            if (AlertDialog.updateAlert(j)) {
                j.setId(currentJob.getId());
                DBJob.update(j);
            }
        }
        else {
            if (AlertDialog.saveNewAlert(j)) {
                DBJob.insert(j);
            }
        }

        clearFields();
        disableFields(false);
    }

    @Override
    public void delete() {
        if (currentJob != null) {
            if (AlertDialog.deleteAlert(currentJob)) {
                DBJob.delete(currentJob.getId());
                clearFields();
            }
        }
        disableFields(false);
    }
}
