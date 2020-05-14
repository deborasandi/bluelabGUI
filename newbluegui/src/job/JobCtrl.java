package job;


import java.time.LocalDate;
import java.util.Date;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;

import alert.AlertDialog;
import application.Main;
import client.Client;
import database.DBClient;
import database.DBJob;
import database.DBJobPrice;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import jobprice.JobPrice;


public class JobCtrl {

    @FXML
    private Spinner<Integer> jobQtd;

    @FXML
    private Spinner<Double> shipping;

    @FXML
    private JFXDatePicker date;

    @FXML
    private JFXComboBox<Client> client;

    @FXML
    private JFXComboBox<JobPrice> jobPrice;

    @FXML
    private TableView<Job> viewJob;

    @FXML
    private TableColumn<Job, Client> colClient;

    @FXML
    private TableColumn<Job, Job> colJobType;

    @FXML
    private TableColumn<Job, Date> colDate;

    @FXML
    private JFXCheckBox repetition;

    @FXML
    private JFXCheckBox nocost;

    @FXML
    private Label lblTotal;

    private ObservableList<Client> listClient;

    private ObservableList<JobPrice> listJobPrice;

    private ObservableList<Job> listJobs;

    private Job currentJob;

    public void initialize() {
        listClient = FXCollections.observableArrayList(DBClient.getList());
        client.getItems().addAll(listClient);

        listJobPrice = FXCollections.observableArrayList(DBJobPrice.getList());

        listJobs = FXCollections.observableArrayList(DBJob.getList());
        viewJob.getItems().addAll(listJobs);

        date.setValue(LocalDate.now());

        createColumns();

        viewJob.setOnMouseClicked(new EventHandler<Event>() {

            @Override
            public void handle(Event event) {
                currentJob = viewJob.getSelectionModel().getSelectedItem();
                if (currentJob != null)
                    loadJob(currentJob);
            }
        });

        jobQtd.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10));
        jobQtd.getValueFactory().setValue(1);
        jobQtd.valueProperty().addListener((obs, oldValue, newValue) -> calcTotal());

        shipping.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1000));
        shipping.getValueFactory().setValue(0.0);
        shipping.valueProperty().addListener((obs, oldValue, newValue) -> calcTotal());
    }

    private void createColumns() {
        colClient.setCellValueFactory(new PropertyValueFactory<>("client"));
        colJobType.setCellValueFactory(new PropertyValueFactory<>("jobPrice"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    @FXML
    void deleteJob(ActionEvent event) {
        if (currentJob != null) {
            if (AlertDialog.showDelete(currentJob)) {
                DBJob.delete(currentJob.getId());
                clearFields();
                Main.refreshJobs();
                refreshViewJob();
            }
        }
    }

    @FXML
    void newJob(ActionEvent event) {
        currentJob = null;
        clearFields();
        disableFields(false);
    }

    @FXML
    void refresh(ActionEvent event) {
        refreshViewJob();
    }

    @FXML
    void saveJob(ActionEvent event) {
        Job j = new Job();

        j.setClient(client.getValue());
        j.setJobPrice(jobPrice.getValue());
        j.setQtd(jobQtd.getValue());
        j.setShipping(shipping.getValue());
        j.setDate(java.sql.Date.valueOf(date.getValue()));
        j.setRepetition(repetition.isSelected());
        j.setNocost(nocost.isSelected());
        j.setTotal(Double.parseDouble(lblTotal.getText()));

        if (currentJob != null) {
            if (AlertDialog.showSaveUpdate(j)) {
                j.setId(currentJob.getId());
                DBJob.update(j);
            }
        }
        else {
            if (AlertDialog.showSaveNew(j))
                DBJob.insert(j);
        }

        clearFields();
        Main.refreshJobs();
        refreshViewJob();
    }

    @FXML
    void editJob() {
        disableFields(false);
    }

    private void clearFields() {
        client.getSelectionModel().clearSelection();
        jobPrice.getSelectionModel().clearSelection();
        jobQtd.getValueFactory().setValue(1);
        shipping.getValueFactory().setValue(0.0);
        date.setValue(LocalDate.now());
        repetition.setSelected(false);
        nocost.setSelected(false);
    }

    private void refreshViewJob() {
        listJobs = FXCollections.observableArrayList(DBJob.getList());
        viewJob.getItems().clear();
        viewJob.getItems().addAll(listJobs);
    }

    private void disableFields(boolean d) {
        client.setDisable(d);
        jobPrice.setDisable(d);
        jobQtd.setDisable(d);
        shipping.setDisable(d);
        date.setDisable(d);
        repetition.setDisable(d);
        nocost.setDisable(d);
    }

    private void loadJob(Job j) {
        findClient(j.getClient());
        findJobPrice(j.getJobPrice());
        jobQtd.getValueFactory().setValue(j.getQtd());
        shipping.getValueFactory().setValue(j.getShipping());
        date.setValue(j.getDate().toLocalDate());
        repetition.setSelected(j.isRepetition());
        nocost.setSelected(j.isNocost());

        disableFields(true);
    }

    private void findClient(Client c) {
        for (Client c1 : listClient) {
            if (c1.getId() == c.getId())
                client.getSelectionModel().select(c1);
        }
    }

    private void findJobPrice(JobPrice j) {
        for (JobPrice j1 : listJobPrice) {
            if (j1.getId() == j.getId())
                jobPrice.getSelectionModel().select(j1);
        }
    }

    public void refreshClients() {
        listClient = FXCollections.observableArrayList(DBClient.getList());
        client.getItems().clear();
        client.getItems().addAll(listClient);
        client.getSelectionModel().select(0);
    }

    public void refreshJobTypes() {
        listJobPrice = FXCollections.observableArrayList(DBJobPrice.getList());
        jobPrice.getItems().clear();
        jobPrice.getItems().addAll(listJobPrice);
        jobPrice.getSelectionModel().select(0);
    }

    @FXML
    void loadJobPrice() {
        Client c = client.getSelectionModel().getSelectedItem();

        if (c == null)
            return;

        jobPrice.getItems().clear();
        for (JobPrice jp : listJobPrice) {
            if (jp.getPriceTable().getId() == c.getPriceTable().getId())
                jobPrice.getItems().add(jp);
        }
    }

    @FXML
    void calcTotal() {
        JobPrice jp = jobPrice.getSelectionModel().getSelectedItem();

        if (jp == null)
            return;

        if (jobQtd.getValue() == null)
            jobQtd.getValueFactory().setValue(0);

        if (shipping.getValue() == null)
            shipping.getValueFactory().setValue(0.0);

        if (nocost.isSelected()) {
            lblTotal.setText(String.valueOf(0));
        }
        else {
            double total = jp.getPrice() * jobQtd.getValue() + shipping.getValue();
            lblTotal.setText(String.valueOf(total));
        }
    }
}
