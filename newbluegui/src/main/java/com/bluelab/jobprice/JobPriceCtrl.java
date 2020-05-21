package com.bluelab.jobprice;


import java.util.ArrayList;
import java.util.List;

import com.bluelab.database.DBJobPrice;
import com.bluelab.database.DBJobType;
import com.bluelab.database.DBPriceTable;
import com.bluelab.jobtype.JobType;
import com.bluelab.pricetable.PriceTable;
import com.bluelab.util.AlertDialog;
import com.bluelab.util.FxmlInterface;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    private TableView<PriceTable> viewPriceTable;

    @FXML
    private TableColumn<PriceTable, Integer> colTableId;

    @FXML
    private TableColumn<PriceTable, String> colTableName;

    @FXML
    private TableView<JobType> viewJobType;

    @FXML
    private TableColumn<JobType, Integer> colJobId;

    @FXML
    private TableColumn<JobType, String> colJobName;

    @FXML
    private TableView<JobPrice> viewJobPrice;

    @FXML
    private TableColumn<JobPrice, PriceTable> colTable;

    @FXML
    private TableColumn<JobPrice, JobType> colJob;

    @FXML
    private TableColumn<JobPrice, Double> colPrice;

    private List<JobPrice> newPrices;

    public void initialize() {
        viewPriceTable.setEditable(true);
        viewJobType.setEditable(true);
        viewJobPrice.setEditable(true);

        viewPriceTable.setItems(DBPriceTable.getList());
        viewJobType.setItems(DBJobType.getList());
        viewJobPrice.setItems(DBJobPrice.getList());

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
        colTable.setCellFactory(ComboBoxTableCell.forTableColumn(DBPriceTable.getList()));
        colTable.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<JobPrice, PriceTable>>() {

            @Override
            public void handle(CellEditEvent<JobPrice, PriceTable> event) {
                JobPrice j = event.getTableView().getItems().get(event.getTablePosition().getRow());
                j.setPriceTable(event.getNewValue());
                newPrices.add(j);
                viewJobPrice.refresh();

                if (!paneInfo.getChildren().contains(labelNotif))
                    paneInfo.getChildren().add(labelNotif);
            }
        });

        colJob.setCellValueFactory(new PropertyValueFactory<>("jobType"));
        colJob.setCellFactory(ComboBoxTableCell.forTableColumn(DBJobType.getList()));
        colJob.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<JobPrice, JobType>>() {

            @Override
            public void handle(CellEditEvent<JobPrice, JobType> event) {
                JobPrice j = event.getTableView().getItems().get(event.getTablePosition().getRow());
                j.setJobType(event.getNewValue());
                newPrices.add(j);
                viewJobPrice.refresh();

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
                viewJobPrice.refresh();

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

                    p = DBPriceTable.get(p.getName());

                    List<JobPrice> list = new ArrayList<JobPrice>();

                    for (JobType jobType : DBJobType.getList()) {
                        list.add(new JobPrice(jobType, p, 0.0));
                    }

                    DBJobPrice.insert(list);
                    AlertDialog.successAlert(p);
                }
                else {
                    String old = new String(p.getName());
                    p.setName(event.getNewValue());

                    if (AlertDialog.updateAlert(p, old)) {
                        DBPriceTable.update(p);
                    }
                    else {
                        p.setName(old);
                        viewPriceTable.refresh();
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

                    j = DBJobType.get(j.getName());

                    List<JobPrice> list = new ArrayList<JobPrice>();

                    for (PriceTable p : DBPriceTable.getList()) {
                        list.add(new JobPrice(j, p, 0.0));
                    }

                    DBJobPrice.insert(list);
                    AlertDialog.successAlert(j);
                }
                else {
                    String old = new String(j.getName());
                    j.setName(event.getNewValue());

                    if (AlertDialog.updateAlert(j, old)) {
                        DBJobType.update(j);
                    }
                    else {
                        j.setName(old);
                    }
                }
            }
        });
    }

    @FXML
    @Override
    public void create() {
        if (tabTable.isSelected()) {
            PriceTable p = new PriceTable();
            viewPriceTable.getItems().add(p);
        }
        else if (tabJob.isSelected()) {
            JobType j = new JobType();
            viewJobType.getItems().add(j);
        }
        else {
            JobPrice jp = new JobPrice();
            viewJobPrice.getItems().add(jp);
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
            PriceTable p = viewPriceTable.getSelectionModel().getSelectedItem();
            if (p != null && p.getId() != 0 && AlertDialog.deleteAlert(p)) {
                DBPriceTable.delete(p.getId());
            }
        }
        else if (tabJob.isSelected()) {
            JobType j = viewJobType.getSelectionModel().getSelectedItem();
            if (j != null && j.getId() != 0 && AlertDialog.deleteAlert(j)) {
                DBJobType.delete(j.getId());
            }
        }
        else {
            JobPrice jp = viewJobPrice.getSelectionModel().getSelectedItem();
            if (jp != null && jp.getId() != 0 && AlertDialog.deleteAlert(jp)) {
                DBJobPrice.updatePrice(jp);
            }
        }
    }

    @Override
    public void disableFields(boolean b) {

    }
}
