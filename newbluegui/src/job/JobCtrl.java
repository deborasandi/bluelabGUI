package job;

import java.time.LocalDate;
import java.util.Date;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import client.Client;
import database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    private TableColumn<Job, Boolean> colReceived;
    
    private ObservableList<Client> listClient;
    
    private ObservableList<JobType> listJobType;

    public void initialize() {
        listClient = FXCollections.observableArrayList(DBConnection.listClients());
        client.getItems().addAll(listClient);
        
        listJobType = FXCollections.observableArrayList(DBConnection.listJobType());
        jobType.getItems().addAll(listJobType);
        
        date.setValue(LocalDate.now());
        
    }
    
    @FXML
    void deleteJob(ActionEvent event) {

    }

    @FXML
    void newJob(ActionEvent event) {

    }

    @FXML
    void refresh(ActionEvent event) {

    }

    @FXML
    void saveJob(ActionEvent event) {

    }

}
