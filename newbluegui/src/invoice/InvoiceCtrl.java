package invoice;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;

import client.Client;
import database.DBClient;
import database.DBJob;
import database.DBJobType;
import database.DBProductColor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import job.Job;
import jobtype.JobType;
import productcolor.ProductColor;
import util.ImageTableCell;


public class InvoiceCtrl {

    @FXML
    private JFXComboBox<Client> client;

    @FXML
    private JFXComboBox<JobType> jobType;
    
    @FXML
    private JFXComboBox<ProductColor> productColor;

    @FXML
    private JFXDatePicker initDate;

    @FXML
    private JFXDatePicker endDate;

    @FXML
    private JFXComboBox<String> isRepetition;

    @FXML
    private JFXComboBox<String> isNoCost;

    @FXML
    private JFXComboBox<String> isPaid;

    @FXML
    private VBox contentPane;

    @FXML
    private HBox filterPane;

    @FXML
    private Tab tabTable;

    @FXML
    private TableView<Job> viewJob;

    @FXML
    private TableColumn<Job, Client> colClient;

    @FXML
    private TableColumn<Job, JobType> colJobType;
    
    @FXML
    private TableColumn<Job, ProductColor> colProductColor;

    @FXML
    private TableColumn<Job, Integer> colQtd;

    @FXML
    private TableColumn<Job, Double> colShipping;

    @FXML
    private TableColumn<Job, Date> colDate;

    @FXML
    private TableColumn<Job, Boolean> colRepetion;

    @FXML
    private TableColumn<Job, Boolean> colNoCost;
    
    @FXML
    private TableColumn<Job, Double> colTotal;

    @FXML
    private TableColumn<Job, Boolean> colPaid;

    private ObservableList<Client> listClient;

    private ObservableList<JobType> listJobType;
    
    private ObservableList<ProductColor> listProductColor;

    private ObservableList<Job> listJob;

    public void initialize() {
        viewJob.setEditable(true);
        listClient = FXCollections.observableArrayList(DBClient.getList());
        listClient.add(0, new Client("Todos"));
        client.getItems().addAll(listClient);
        client.getSelectionModel().select(0);

        listJobType = FXCollections.observableArrayList(DBJobType.getList());
        listJobType.add(0, new JobType("Todos"));
        jobType.getItems().addAll(listJobType);
        jobType.getSelectionModel().select(0);
        
        listProductColor = FXCollections.observableArrayList(DBProductColor.getList());
        listProductColor.add(0, new ProductColor("Todos"));
        productColor.getItems().addAll(listProductColor);
        productColor.getSelectionModel().select(0);

        listJob = FXCollections.observableArrayList(DBJob.getList());
        viewJob.getItems().addAll(listJob);

        List<String> listAux = new ArrayList<String>();
        listAux.add("Todos");
        listAux.add("Sim");
        listAux.add("Não");

        isRepetition.getItems().addAll(listAux);
        isRepetition.getSelectionModel().select(0);
        isNoCost.getItems().addAll(listAux);
        isNoCost.getSelectionModel().select(0);
        isPaid.getItems().addAll(listAux);
        isPaid.getSelectionModel().select(0);

        createColumns();
    }

    private void createColumns() {
        colClient.setCellValueFactory(new PropertyValueFactory<>("client"));
        colJobType.setCellValueFactory(new PropertyValueFactory<>("jobPrice"));
        colProductColor.setCellValueFactory(new PropertyValueFactory<>("productColor"));
        colQtd.setCellValueFactory(new PropertyValueFactory<>("qtd"));
        colShipping.setCellValueFactory(new PropertyValueFactory<>("shipping"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colRepetion.setCellValueFactory(new PropertyValueFactory<>("repetition"));
        colNoCost.setCellValueFactory(new PropertyValueFactory<>("nocost"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colPaid.setCellValueFactory(new PropertyValueFactory<>("paid"));

        colPaid.setCellFactory(new Callback<TableColumn<Job, Boolean>, TableCell<Job, Boolean>>() {

            @Override
            public TableCell<Job, Boolean> call(TableColumn<Job, Boolean> param) {
                return new util.CheckBoxTableCell();
            }
        });

        colRepetion.setCellFactory(new Callback<TableColumn<Job, Boolean>, TableCell<Job, Boolean>>() {

            @Override
            public TableCell<Job, Boolean> call(TableColumn<Job, Boolean> arg0) {
                return new ImageTableCell();
            }
        });

        colNoCost.setCellFactory(new Callback<TableColumn<Job, Boolean>, TableCell<Job, Boolean>>() {

            @Override
            public TableCell<Job, Boolean> call(TableColumn<Job, Boolean> arg0) {
                return new ImageTableCell();
            }
        });
    }

    @FXML
    void filter() {
        Client c = client.getSelectionModel().getSelectedItem();
        
        List<Job> filter = new ArrayList<Job>();
        if(c.getId() != 0) {
            
            for (Job j : listJob) {
                if(j.getClient().getId() == c.getId()) {
                    filter.add(j);
                }
            }
        }else {
            // todos os clientes
        }
        
        listJob = FXCollections.observableArrayList(filter);
        viewJob.getItems().clear();
        viewJob.getItems().addAll(listJob);
    }

    public void refreshClients() {
        listClient = FXCollections.observableArrayList(DBClient.getList());
        listClient.add(0, new Client("Todos"));
        client.getItems().clear();
        client.getItems().addAll(listClient);
        client.getSelectionModel().select(0);
    }
    
    public void refreshJobTypes() {
        listJobType = FXCollections.observableArrayList(DBJobType.getList());
        jobType.getItems().clear();
        jobType.getItems().addAll(listJobType);
        jobType.getSelectionModel().clearSelection();
    }

    public void refreshJobs() {
        listJob = FXCollections.observableArrayList(DBJob.getList());
        viewJob.getItems().clear();
        viewJob.getItems().addAll(listJob);
    }
}
