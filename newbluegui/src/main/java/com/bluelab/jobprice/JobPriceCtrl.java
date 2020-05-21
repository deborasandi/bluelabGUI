package com.bluelab.jobprice;


import java.util.ArrayList;
import java.util.List;

import com.bluelab.database.DBJobPrice;
import com.bluelab.database.DBJobType;
import com.bluelab.database.DBPriceTable;
import com.bluelab.jobtype.JobType;
import com.bluelab.main.Main;
import com.bluelab.pricetable.PriceTable;
import com.bluelab.util.AlertDialog;
import com.bluelab.util.FxmlInterface;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.util.converter.DoubleStringConverter;


public class JobPriceCtrl implements FxmlInterface {

    @FXML
    private JFXButton btnCreate;

    @FXML
    private JFXButton btnSave;

    @FXML
    private Label labelNotif;

    @FXML
    private HBox paneInfo;

    @FXML
    private Tab tabTable;

    @FXML
    private Tab tabJob;

    @FXML
    private Tab tabPrice;

    @FXML
    private JFXTabPane tabpane;

    @FXML
    private TableView<PriceTable> viewTable;

    @FXML
    private TableColumn<PriceTable, Integer> colTableId;

    @FXML
    private TableColumn<PriceTable, String> colTableName;

    @FXML
    private TableView<JobType> viewJob;

    @FXML
    private TableColumn<JobType, Integer> colJobId;

    @FXML
    private TableColumn<JobType, String> colJobName;

    @FXML
    private TableView<JobPrice> viewPrice;

    @FXML
    private TableColumn<JobPrice, PriceTable> colTable;

    @FXML
    private TableColumn<JobPrice, JobType> colJob;

    @FXML
    private TableColumn<JobPrice, Double> colPrice;

    private ObservableList<PriceTable> listPriceTable;

    private ObservableList<JobType> listJobType;

    private ObservableList<JobPrice> listPrice;

    private List<JobPrice> newPrices;

    public void initialize() {
        viewTable.setEditable(true);
        viewJob.setEditable(true);
        viewPrice.setEditable(true);

        listPriceTable = FXCollections.observableArrayList(DBPriceTable.getList());
        viewTable.getItems().addAll(listPriceTable);

        listJobType = FXCollections.observableArrayList(DBJobType.getList());
        viewJob.getItems().addAll(listJobType);

        listPrice = FXCollections.observableArrayList(DBJobPrice.getList());
        viewPrice.getItems().addAll(listPrice);

        newPrices = new ArrayList<JobPrice>();

        priceTableColumns();
        jobTypeColumns();
        jobPriceColumns();

        paneInfo.getChildren().remove(labelNotif);

        tabpane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {

            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) {
                if (newTab.equals(tabTable) || newTab.equals(tabJob)) {
                    btnCreate.setDisable(false);
                    btnSave.setDisable(true);
                }
                else {
                    btnCreate.setDisable(true);
                    btnSave.setDisable(false);
                }
            }
        });

    }

    private void jobPriceColumns() {
        colTable.setCellValueFactory(new PropertyValueFactory<>("priceTable"));
        colTable.setCellFactory(ComboBoxTableCell.forTableColumn(listPriceTable));
        colTable.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<JobPrice, PriceTable>>() {

            @Override
            public void handle(CellEditEvent<JobPrice, PriceTable> event) {
                JobPrice j = event.getTableView().getItems().get(event.getTablePosition().getRow());
                j.setPriceTable(event.getNewValue());
                newPrices.add(j);
                viewPrice.refresh();

                if (!paneInfo.getChildren().contains(labelNotif))
                    paneInfo.getChildren().add(labelNotif);
            }
        });

        colJob.setCellValueFactory(new PropertyValueFactory<>("jobType"));
        colJob.setCellFactory(ComboBoxTableCell.forTableColumn(listJobType));
        colJob.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<JobPrice, JobType>>() {

            @Override
            public void handle(CellEditEvent<JobPrice, JobType> event) {
                JobPrice j = event.getTableView().getItems().get(event.getTablePosition().getRow());
                j.setJobType(event.getNewValue());
                newPrices.add(j);
                viewPrice.refresh();

                if (!paneInfo.getChildren().contains(labelNotif))
                    paneInfo.getChildren().add(labelNotif);
            }
        });

        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colPrice.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        colPrice.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<JobPrice, Double>>() {

            @Override
            public void handle(CellEditEvent<JobPrice, Double> event) {
                JobPrice j = event.getTableView().getItems().get(event.getTablePosition().getRow());
                j.setPrice(event.getNewValue());
                newPrices.add(j);
                viewPrice.refresh();

                if (!paneInfo.getChildren().contains(labelNotif))
                    paneInfo.getChildren().add(labelNotif);
            }
        });
    }

    private void priceTableColumns() {
        colTableId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTableName.setCellValueFactory(new PropertyValueFactory<>("name"));

        colTableName.setCellFactory(TextFieldTableCell.forTableColumn());

        colTableName.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<PriceTable, String>>() {

            @Override
            public void handle(CellEditEvent<PriceTable, String> event) {
                PriceTable p = event.getTableView().getItems().get(event.getTablePosition().getRow());

                if (p.getId() == 0) {
                    p.setName(event.getNewValue());

                    DBPriceTable.insert(p);

                    Main.refreshPriceTables();

                    p = DBPriceTable.get(p.getName());

                    List<JobPrice> list = new ArrayList<JobPrice>();

                    for (JobType jobType : listJobType) {
                        list.add(new JobPrice(jobType, p, 0.0));
                    }

                    DBJobPrice.insert(list);
                    AlertDialog.successAlert(p);

                    Main.refreshJobPrices();
                }
                else {
                    String old = new String(p.getName());
                    p.setName(event.getNewValue());

                    if (AlertDialog.updateAlert(p, old)) {
                        DBPriceTable.update(p);
                        Main.refreshPriceTables();
                        Main.refreshJobPrices();
                    }
                    else {
                        p.setName(old);
                        viewTable.refresh();
                    }

                }

            }
        });
    }

    private void jobTypeColumns() {
        colJobId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colJobName.setCellValueFactory(new PropertyValueFactory<>("name"));

        colJobName.setCellFactory(TextFieldTableCell.forTableColumn());

        colJobName.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<JobType, String>>() {

            @Override
            public void handle(CellEditEvent<JobType, String> event) {
                JobType j = event.getTableView().getItems().get(event.getTablePosition().getRow());

                if (j.getId() == 0) {
                    j.setName(event.getNewValue());

                    DBJobType.insert(j);
                    refreshViewJob();
                    Main.refreshJobTypes();

                    j = DBJobType.get(j.getName());

                    List<JobPrice> list = new ArrayList<JobPrice>();

                    for (PriceTable p : listPriceTable) {
                        list.add(new JobPrice(j, p, 0.0));
                    }

                    DBJobPrice.insert(list);
                    AlertDialog.successAlert(j);

                    Main.refreshJobPrices();
                }
                else {
                    String old = new String(j.getName());
                    j.setName(event.getNewValue());

                    if (AlertDialog.updateAlert(j, old)) {
                        DBJobType.update(j);
                        Main.refreshJobTypes();
                        Main.refreshJobPrices();
                    }
                    else {
                        j.setName(old);
                        refreshViewJob();
                    }
                }
            }
        });
    }

    private void refreshViewTable() {
        listPriceTable = FXCollections.observableArrayList(DBPriceTable.getList());
        viewTable.getItems().clear();
        viewTable.getItems().addAll(listPriceTable);

        colTable.setCellFactory(ComboBoxTableCell.forTableColumn(listPriceTable));
    }

    private void refreshViewJob() {
        listJobType = FXCollections.observableArrayList(DBJobType.getList());
        viewJob.getItems().clear();
        viewJob.getItems().addAll(listJobType);

        colJob.setCellFactory(ComboBoxTableCell.forTableColumn(listJobType));
    }

    @FXML
    @Override
    public void create() {
        if (tabTable.isSelected()) {
            PriceTable p = new PriceTable();
            viewTable.getItems().add(p);
        }
        else if (tabJob.isSelected()) {
            JobType j = new JobType();
            viewJob.getItems().add(j);
        }
        else {
            JobPrice jp = new JobPrice();
            viewPrice.getItems().add(jp);
        }
    }

    @Override
    public void edit() {

    }

    @FXML
    @Override
    public void save() {
        if (tabPrice.isSelected()) {
            for (JobPrice jp : newPrices) {
                DBJobPrice.update(jp);
            }

            if (!newPrices.isEmpty()) {
                Main.refreshPriceTables();
                Main.refreshJobPrices();
                newPrices.clear();
            }

            if (paneInfo.getChildren().contains(labelNotif))
                paneInfo.getChildren().remove(labelNotif);

            AlertDialog.successAlert(new JobPrice());

        }
    }

    @FXML
    @Override
    public void delete() {
        if (tabTable.isSelected()) {
            PriceTable p = viewTable.getSelectionModel().getSelectedItem();
            if (p != null && p.getId() != 0 && AlertDialog.deleteAlert(p)) {
                DBPriceTable.delete(p.getId());
                Main.refreshPriceTables();
                Main.refreshJobPrices();
                refreshViewTable();
            }
        }
        else if (tabJob.isSelected()) {
            JobType j = viewJob.getSelectionModel().getSelectedItem();
            if (j != null && j.getId() != 0 && AlertDialog.deleteAlert(j)) {
                DBJobType.delete(j.getId());
                Main.refreshJobTypes();
                Main.refreshJobPrices();
                refreshViewJob();
            }
        }
        else {
            JobPrice jp = viewPrice.getSelectionModel().getSelectedItem();
            if (jp != null && jp.getId() != 0 && AlertDialog.deleteAlert(jp)) {
                DBJobPrice.updatePrice(jp);
                // DBJobPrice.delete(jp.getId());
                Main.refreshPriceTables();
                Main.refreshJobPrices();
            }
        }
    }

    @Override
    public void disableFields(boolean b) {
        // TODO Auto-generated method stub

    }

    public void refreshJobPrice() {
        listPrice = FXCollections.observableArrayList(DBJobPrice.getList());
        viewPrice.getItems().clear();
        viewPrice.getItems().addAll(listPrice);
    }
}
