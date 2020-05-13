package job;


import java.time.LocalDate;
import java.util.Date;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import alert.AlertDialog;
import client.Client;
import database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import jobtype.JobType;


public class JobCtrl {

    @FXML
    private JFXTextField jobQtd;

    @FXML
    private JFXTextField shipping;

    @FXML
    private JFXDatePicker date;

    @FXML
    private JFXComboBox<Client> client;

    @FXML
    private JFXComboBox<JobType> jobType;

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

    private ObservableList<Client> listClient;

    private ObservableList<JobType> listJobType;

    private ObservableList<Job> listJobs;

    private Job currentJob;

    public void initialize() {
        listClient = FXCollections.observableArrayList(DBConnection.getListClient(false));
        client.getItems().addAll(listClient);

        listJobType = FXCollections.observableArrayList(DBConnection.getListJobType(false));
        jobType.getItems().addAll(listJobType);

        listJobs = FXCollections.observableArrayList(DBConnection.getListJob(false));
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

    }

    private void createColumns() {
        colClient.setCellValueFactory(new PropertyValueFactory<>("client"));
        colJobType.setCellValueFactory(new PropertyValueFactory<>("jobType"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        // colReceived.setCellFactory(CheckBoxTableCell.forTableColumn(new TableColumn<Job, Boolean>()));
    }

    @FXML
    void deleteJob(ActionEvent event) {
        if (currentJob != null) {
            if (AlertDialog.showDelete(currentJob)) {
                DBConnection.deleteJob(currentJob.getId());
                refreshViewJob();
                clearFields();
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
        j.setJobType(jobType.getValue());
        j.setQtd(jobQtd.getText().isEmpty() ? 0 : Integer.parseInt(jobQtd.getText()));
        j.setShipping(shipping.getText().isEmpty() ? 0 : Double.parseDouble(shipping.getText()));
        j.setDate(java.sql.Date.valueOf(date.getValue()));
        j.setRepetition(repetition.isSelected());
        j.setNocost(nocost.isSelected());

        if (currentJob != null) {
            if (AlertDialog.showSaveUpdate(j)) {
                j.setId(currentJob.getId());
                DBConnection.updateJob(j);
            }
        }
        else {
            if (AlertDialog.showSaveNew(j))
                DBConnection.insertJob(j);
        }

        clearFields();

        refreshViewJob();
    }

    @FXML
    void editJob() {
        disableFields(false);
    }

    private void clearFields() {
        client.getSelectionModel().clearSelection();
        jobType.getSelectionModel().clearSelection();
        jobQtd.clear();
        shipping.clear();
        date.setValue(LocalDate.now());
        repetition.setSelected(false);
        nocost.setSelected(false);
    }

    private void refreshViewJob() {
        listJobs = FXCollections.observableArrayList(DBConnection.getListJob(true));
        viewJob.getItems().clear();
        viewJob.getItems().addAll(listJobs);
    }

    private void disableFields(boolean d) {
        client.setDisable(d);
        jobType.setDisable(d);
        jobQtd.setDisable(d);
        shipping.setDisable(d);
        date.setDisable(d);
        repetition.setDisable(d);
        nocost.setDisable(d);
    }

    private void loadJob(Job j) {
        findClient(j.getClient());
        findJobType(j.getJobType());
        jobQtd.setText(String.valueOf(j.getQtd()));
        shipping.setText(String.valueOf(j.getShipping()));
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

    private void findJobType(JobType j) {
        for (JobType j1 : listJobType) {
            if (j1.getId() == j.getId())
                jobType.getSelectionModel().select(j1);
        }
    }
}
