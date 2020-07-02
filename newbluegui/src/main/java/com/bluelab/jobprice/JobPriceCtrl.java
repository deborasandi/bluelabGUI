package com.bluelab.jobprice;


import java.util.ArrayList;
import java.util.List;

import com.bluelab.database.DBJobPrice;
import com.bluelab.database.DBJobType;
import com.bluelab.database.DBPriceTable;
import com.bluelab.jobtype.JobType;
import com.bluelab.pricetable.PriceTable;
import com.bluelab.util.FxmlInterface;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
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

    private ObservableList<PriceTable> listPriceTable;

    private ObservableList<JobType> listJobType;

    private ObservableList<JobPrice> listJobPrice;

    private List<JobPrice> newPrices;

    public void initialize() {
        viewPriceTable.setEditable(true);
        viewJobType.setEditable(true);
        viewJobPrice.setEditable(true);

        listPriceTable = DBPriceTable.list();
        listJobType = DBJobType.getList();
        listJobPrice = DBJobPrice.getList();

        viewPriceTable.setItems(listPriceTable);
        viewJobType.setItems(listJobType);
        viewJobPrice.setItems(listJobPrice);

        newPrices = new ArrayList<JobPrice>();

        priceTableColumns();
        jobTypeColumns();
        jobPriceColumns();

        paneInfo.getChildren().remove(labelNotif);

        tabpane.getSelectionModel().selectedItemProperty()
                .addListener((ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) -> {
                    if (newTab.equals(tabTable) || newTab.equals(tabJob)) {
                        btnCreate.setDisable(false);
                        btnSave.setDisable(true);
                    }
                    else {
                        btnCreate.setDisable(true);
                        btnSave.setDisable(false);
                    }
                });

    }

    private void jobPriceColumns() {
        colTable.setCellValueFactory(new PropertyValueFactory<>("priceTable"));
        colTable.setCellFactory(ComboBoxTableCell.forTableColumn(listPriceTable));
        colTable.setOnEditCommit(event -> {
            JobPrice j = event.getTableView().getItems().get(event.getTablePosition().getRow());
            j.setPriceTable(event.getNewValue());
            newPrices.add(j);
            viewJobPrice.refresh();

            if (!paneInfo.getChildren().contains(labelNotif))
                paneInfo.getChildren().add(labelNotif);
        });

        colJob.setCellValueFactory(new PropertyValueFactory<>("jobType"));
        colJob.setCellFactory(ComboBoxTableCell.forTableColumn(listJobType));
        colJob.setOnEditCommit(event -> {
            JobPrice j = event.getTableView().getItems().get(event.getTablePosition().getRow());
            j.setJobType(event.getNewValue());
            newPrices.add(j);
            viewJobPrice.refresh();

            if (!paneInfo.getChildren().contains(labelNotif))
                paneInfo.getChildren().add(labelNotif);
        });

        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colPrice.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        colPrice.setOnEditCommit(event -> {
            JobPrice j = event.getTableView().getItems().get(event.getTablePosition().getRow());
            j.setPrice(event.getNewValue());
            newPrices.add(j);
            viewJobPrice.refresh();

            if (!paneInfo.getChildren().contains(labelNotif))
                paneInfo.getChildren().add(labelNotif);
        });
    }

    private void priceTableColumns() {
        colTableId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTableName.setCellValueFactory(new PropertyValueFactory<>("name"));

        colTableName.setCellFactory(TextFieldTableCell.forTableColumn());

        colTableName.setOnEditCommit(event -> {
            PriceTable p = event.getTableView().getItems().get(event.getTablePosition().getRow());

            if (p.getId() == 0) {
                p.setName(event.getNewValue());

                DBPriceTable.insert(p);

                p = DBPriceTable.get(p.getName());

                List<JobPrice> list = new ArrayList<JobPrice>();

                for (JobType jobType : listJobType) {
                    list.add(new JobPrice(jobType, p, 0.0));
                }

                DBJobPrice.insert(list);
            }
            else {
                p.setName(event.getNewValue());
                DBPriceTable.update(p);
            }
        });
    }

    private void jobTypeColumns() {
        colJobId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colJobName.setCellValueFactory(new PropertyValueFactory<>("name"));

        colJobName.setCellFactory(TextFieldTableCell.forTableColumn());

        colJobName.setOnEditCommit(event -> {
            JobType j = event.getTableView().getItems().get(event.getTablePosition().getRow());

            if (j.getId() == 0) {
                j.setName(event.getNewValue());

                DBJobType.insert(j);

                j = DBJobType.get(j.getName());

                List<JobPrice> list = new ArrayList<JobPrice>();

                for (PriceTable p : listPriceTable) {
                    list.add(new JobPrice(j, p, 0.0));
                }

                DBJobPrice.insert(list);
            }
            else {
                j.setName(event.getNewValue());
                DBJobType.update(j);
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
        }
    }

    @FXML
    @Override
    public void delete() {
        if (tabTable.isSelected()) {
            PriceTable p = viewPriceTable.getSelectionModel().getSelectedItem();
            if (p != null && p.getId() != 0) {
                DBPriceTable.delete(p.getId());
            }
        }
        else if (tabJob.isSelected()) {
            JobType j = viewJobType.getSelectionModel().getSelectedItem();
            if (j != null && j.getId() != 0) {
                DBJobType.delete(j.getId());
            }
        }
        else {
            JobPrice jp = viewJobPrice.getSelectionModel().getSelectedItem();
            if (jp != null && jp.getId() != 0) {
                DBJobPrice.update(jp);
            }
        }
    }

    @Override
    public void disableFields(boolean b) {

    }
}
