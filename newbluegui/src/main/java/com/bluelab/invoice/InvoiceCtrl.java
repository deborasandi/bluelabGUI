package com.bluelab.invoice;


import java.sql.Date;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;

import com.bluelab.client.Client;
import com.bluelab.database.DBClient;
import com.bluelab.database.DBJob;
import com.bluelab.database.DBJobType;
import com.bluelab.database.DBProductColor;
import com.bluelab.job.Job;
import com.bluelab.jobtype.JobType;
import com.bluelab.productcolor.ProductColor;
import com.bluelab.util.CheckBoxTableCell;
import com.bluelab.util.ComboBoxSearch;
import com.bluelab.util.ListFilter;
import com.bluelab.util.RealTableCell;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;


public class InvoiceCtrl {

    private static Locale ptBr = new Locale("pt", "BR");

    @FXML
    private VBox vbox;

    private ComboBoxSearch<Client> cbxClient;

    @FXML
    private JFXComboBox<JobType> cbxJobType;

    @FXML
    private JFXComboBox<ProductColor> cbxProductColor;

    @FXML
    private JFXDatePicker datepickerInit;

    @FXML
    private JFXDatePicker datepickerEnd;

    @FXML
    private ToggleButton toggleRepetition;

    @FXML
    private ToggleGroup repgroup;

    @FXML
    private ToggleButton toggleNormal;

    @FXML
    private ToggleButton toggleNoCost;

    @FXML
    private ToggleGroup costgroup;

    @FXML
    private ToggleButton toggleCost;

    @FXML
    private ToggleButton togglePaid;

    @FXML
    private ToggleGroup paidgroup;

    @FXML
    private ToggleButton toggleOwing;

    @FXML
    private Label labelOwing;

    @FXML
    private Label labelQtdFiltered;

    @FXML
    private ImageView imgFilter;

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
    private TableColumn<Job, Date> colDate;

    @FXML
    private TableColumn<Job, Boolean> colRepetion;

    @FXML
    private TableColumn<Job, Boolean> colNoCost;

    @FXML
    private TableColumn<Job, Double> colShipping;

    @FXML
    private TableColumn<Job, Double> colTotal;

    @FXML
    private TableColumn<Job, Double> colTotalPaid;

    @FXML
    private TableColumn<Job, Boolean> colPaid;

    private ObservableList<Client> listClient;

    private ObservableList<JobType> listJobType;

    private ObservableList<ProductColor> listProductColor;

    private ObservableList<Job> listJob;
    
    private double total;

    public void initialize() {
        viewJob.setEditable(true);

        listClient = DBClient.getList();
        listJobType = DBJobType.getList();
        listProductColor = DBProductColor.getList();
        listJob = DBJob.getList();

        listClient.addListener((Change<? extends Client> c) -> {
            updateCboxClient();
        });

        listJobType.addListener((Change<? extends JobType> c) -> {
            updateCboxJobType();
        });

        listProductColor.addListener((Change<? extends ProductColor> c) -> {
            updateCboxProductColor();
        });

        listJob.addListener((Change<? extends Job> c) -> {
            filter();

            if (cbxClient.getValue().getId() > 0) {
                labelOwing.setText(NumberFormat.getCurrencyInstance(ptBr).format(total));
            }
        });

        cbxClient = new ComboBoxSearch<Client>();
        vbox.getChildren().add(1, cbxClient);
        cbxClient.setMaxWidth(Double.MAX_VALUE);

        cbxClient.setOnAction(event -> {
            filter();

            if (cbxClient.getValue() == null)
                return;
            
            if (cbxClient.getValue().getId() > 0) {
                labelOwing.setText(NumberFormat.getCurrencyInstance(ptBr).format(total));
            }
            else {
                labelOwing.setText("-");
            }

        });

        updateCboxClient();
        updateCboxJobType();
        updateCboxProductColor();

        viewJob.setItems(listJob);

        createColumns();

    }

    private void createColumns() {
        viewJob.setEditable(true);

        colClient.setCellValueFactory(new PropertyValueFactory<>("client"));

        colJobType.setCellValueFactory(new PropertyValueFactory<>("jobPrice"));

        colProductColor.setCellValueFactory(new PropertyValueFactory<>("productColor"));

        colQtd.setCellValueFactory(new PropertyValueFactory<>("qtd"));

        colShipping.setCellValueFactory(new PropertyValueFactory<>("shipping"));
        colShipping.setCellFactory(c -> new RealTableCell<Job>(false));

        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        colRepetion.setCellValueFactory(new PropertyValueFactory<>("repetition"));
        colRepetion.setCellFactory(c -> new CheckBoxTableCell());

        colNoCost.setCellValueFactory(new PropertyValueFactory<>("nocost"));
        colNoCost.setCellFactory(c -> new CheckBoxTableCell());

        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colTotal.setCellFactory(c -> new RealTableCell<Job>(false));

        colTotalPaid.setCellValueFactory(new PropertyValueFactory<>("totalPaid"));
        colTotalPaid.setCellFactory(c -> new RealTableCell<Job>(false));

        colPaid.setCellValueFactory(new PropertyValueFactory<>("paid"));
        colPaid.setCellFactory(c -> new CheckBoxTableCell());
    }

    @FXML
    void filter() {
        ListFilter<Job> filter = new ListFilter<Job>(listJob);

        Client c = cbxClient.getValue();
        total = filter.filterClient(c);

        JobType j = cbxJobType.getValue();
        filter.filterJobType(j);

        ProductColor p = cbxProductColor.getValue();
        filter.filterProductColor(p);

        filter.filterIsRepetition(toggleRepetition.isSelected(), toggleNormal.isSelected());

        filter.filterIsNoCost(toggleCost.isSelected(), toggleNoCost.isSelected());

        filter.filterIsPaid(togglePaid.isSelected(), toggleOwing.isSelected());

        LocalDate d = datepickerInit.getValue();
        filter.filterInitDate(d);

        d = datepickerEnd.getValue();
        filter.filterEndDate(d);

        if (filter.getNumFilter() > 0) {
            viewJob.setItems(FXCollections.observableArrayList(filter));
            labelQtdFiltered.setText("(" + String.valueOf(filter.getNumFilter()) + ")");
        }
        else {
            removeFilter();
        }
    }

    @FXML
    void removeFilter() {
        labelQtdFiltered.setText("(0)");

        listJob = FXCollections.observableArrayList(DBJob.getList());
        viewJob.setItems(listJob);

        cbxClient.getSelectionModel().select(0);
        cbxJobType.getSelectionModel().select(0);
        cbxProductColor.getSelectionModel().select(0);
        toggleRepetition.setSelected(false);
        toggleNormal.setSelected(false);
        toggleCost.setSelected(false);
        toggleNoCost.setSelected(false);
        togglePaid.setSelected(false);
        toggleOwing.setSelected(false);
        datepickerInit.getEditor().clear();
        datepickerEnd.getEditor().clear();
    }

    private void updateCboxClient() {
        cbxClient.setItems(FXCollections.observableArrayList(listClient));
        cbxClient.getItems().add(0, new Client("Todos"));
        cbxClient.getSelectionModel().select(0);
    }

    private void updateCboxJobType() {
        cbxJobType.setItems(FXCollections.observableArrayList(listJobType));
        cbxJobType.getItems().add(0, new JobType("Todos"));
        cbxJobType.getSelectionModel().select(0);
    }

    private void updateCboxProductColor() {
        cbxProductColor.setItems(FXCollections.observableArrayList(listProductColor));
        cbxProductColor.getItems().add(0, new ProductColor("Todos"));
        cbxProductColor.getSelectionModel().select(0);
    }
}
