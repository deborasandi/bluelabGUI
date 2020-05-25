package com.bluelab.invoice;


import java.sql.Date;
import java.time.LocalDate;

import com.bluelab.client.Client;
import com.bluelab.database.DBClient;
import com.bluelab.database.DBJob;
import com.bluelab.database.DBJobType;
import com.bluelab.database.DBProductColor;
import com.bluelab.job.Job;
import com.bluelab.jobtype.JobType;
import com.bluelab.productcolor.ProductColor;
import com.bluelab.util.ImageTableCell;
import com.bluelab.util.ListFilter;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;


public class InvoiceCtrl {

    @FXML
    private ImageView imgFilter;

    @FXML
    private HBox contentFilter;

    @FXML
    private Label lblFiltros;

    @FXML
    private Label lblNumFiltros;

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
    private ToggleButton isRepetition;

    @FXML
    private ToggleGroup repgroup;

    @FXML
    private ToggleButton isNoRepetion;

    @FXML
    private ToggleButton isNoCost;

    @FXML
    private ToggleGroup costgroup;

    @FXML
    private ToggleButton isCost;

    @FXML
    private ToggleButton isPaid;

    @FXML
    private ToggleGroup paidgroup;

    @FXML
    private ToggleButton isNoPaid;

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

        listClient = DBClient.getList();
        listJobType = DBJobType.getList();
        listProductColor = DBProductColor.getList();
        listJob = DBJob.getList();

        listClient.addListener(new ListChangeListener<Client>() {

            @Override
            public void onChanged(Change<? extends Client> c) {
                updateCboxClient();
            }

        });

        listJobType.addListener(new ListChangeListener<JobType>() {

            @Override
            public void onChanged(Change<? extends JobType> c) {
                updateCboxJobType();
            }

        });

        listProductColor.addListener(new ListChangeListener<ProductColor>() {

            @Override
            public void onChanged(Change<? extends ProductColor> c) {
                updateCboxProductColor();
            }

        });

        listJob.addListener(new ListChangeListener<Job>() {

            @Override
            public void onChanged(Change<? extends Job> c) {
                filter();
            }

        });

        updateCboxClient();
        updateCboxJobType();
        updateCboxProductColor();

        viewJob.setItems(listJob);

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
                return new com.bluelab.util.CheckBoxTableCell();
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
        ListFilter<Job> filter = new ListFilter<Job>(listJob);

        Client c = client.getValue();
        filter.filterClient(c);

        JobType j = jobType.getValue();
        filter.filterJobType(j);

        ProductColor p = productColor.getValue();
        filter.filterProductColor(p);

        filter.filterIsRepetition(isRepetition.isSelected(), isNoRepetion.isSelected());

        filter.filterIsNoCost(isCost.isSelected(), isNoCost.isSelected());

        filter.filterIsPaid(isPaid.isSelected(), isNoPaid.isSelected());

        LocalDate d = initDate.getValue();
        filter.filterInitDate(d);

        d = endDate.getValue();
        filter.filterEndDate(d);

        viewJob.setItems(FXCollections.observableArrayList(filter));

        if (filter.getNumFilter() > 0) {
            lblNumFiltros.setText("(" + String.valueOf(filter.getNumFilter()) + ")");
        }
        else {
            removeFilter();
        }
    }

    @FXML
    void removeFilter() {
        lblNumFiltros.setText("(0)");

        listJob = FXCollections.observableArrayList(DBJob.getList());
        viewJob.getItems().addAll(listJob);

        client.getSelectionModel().select(0);
        jobType.getSelectionModel().select(0);
        productColor.getSelectionModel().select(0);
        isRepetition.setSelected(false);
        isNoRepetion.setSelected(false);
        isCost.setSelected(false);
        isNoCost.setSelected(false);
        isPaid.setSelected(false);
        isNoPaid.setSelected(false);
        initDate.getEditor().clear();
        endDate.getEditor().clear();
    }

    private void updateCboxClient() {
        client.setItems(FXCollections.observableArrayList(listClient));
        client.getItems().add(0, new Client("Todos"));
        client.getSelectionModel().select(0);
    }

    private void updateCboxJobType() {
        jobType.setItems(FXCollections.observableArrayList(listJobType));
        jobType.getItems().add(0, new JobType("Todos"));
        jobType.getSelectionModel().select(0);
    }

    private void updateCboxProductColor() {
        productColor.setItems(FXCollections.observableArrayList(listProductColor));
        productColor.getItems().add(0, new ProductColor("Todos"));
        productColor.getSelectionModel().select(0);
    }
}
