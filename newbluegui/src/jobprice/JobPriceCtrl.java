package jobprice;


import java.util.ArrayList;
import java.util.List;

import alert.AlertDialog;
import application.Main;
import database.DBJobPrice;
import database.DBJobType;
import database.DBPriceTable;
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
import jobtype.JobType;
import pricetable.PriceTable;


public class JobPriceCtrl {

    @FXML
    private Label labelNotif;

    @FXML
    private HBox paneInfo;

    @FXML
    private Tab tabTable;

    @FXML
    private Tab tabJob;

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

        createColumns();

        paneInfo.getChildren().remove(labelNotif);
    }

    private void createColumns() {
        /* Colunas Tipo de Tabela */
        colTableId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTableName.setCellValueFactory(new PropertyValueFactory<>("name"));

        colTableName.setCellFactory(TextFieldTableCell.forTableColumn());

        colTableName.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<PriceTable, String>>() {

            @Override
            public void handle(CellEditEvent<PriceTable, String> event) {
                PriceTable p = event.getTableView().getItems().get(event.getTablePosition().getRow());
                String old = new String(p.getName());
                p.setName(event.getNewValue());

                if (p.getId() == 0) {
                    DBPriceTable.insert(p);
                    p = DBPriceTable.get(p.getName());

                    List<JobPrice> list = new ArrayList<JobPrice>();

                    for (JobType jobType : listJobType) {
                        list.add(new JobPrice(jobType, p, 0.0));
                    }

                    DBJobPrice.insert(list);

                    refreshViewPrice();

                    Main.refreshPriceTables();
                }
                else {
                    if (AlertDialog.showSaveTable(p, old)) {
                        DBPriceTable.update(p);
                        refreshViewPrice();
                    }
                    else {
                        p.setName(old);
                        viewTable.refresh();
                    }

                }

            }
        });

        /* Colunas Tabela de Trabalhos */
        colJobId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colJobName.setCellValueFactory(new PropertyValueFactory<>("name"));

        colJobName.setCellFactory(TextFieldTableCell.forTableColumn());

        colJobName.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<JobType, String>>() {

            @Override
            public void handle(CellEditEvent<JobType, String> event) {
                JobType j = event.getTableView().getItems().get(event.getTablePosition().getRow());
                String old = new String(j.getName());
                j.setName(event.getNewValue());

                if (j.getId() == 0) {
                    DBJobType.insert(j);

                    j = DBJobType.get(j.getName());

                    List<JobPrice> list = new ArrayList<JobPrice>();

                    for (PriceTable p : listPriceTable) {
                        list.add(new JobPrice(j, p, 0.0));
                    }

                    DBJobPrice.insert(list);

                    refreshViewPrice();

                    Main.refreshJobTypes();
                }
                else {
                    if (AlertDialog.showSaveJobType(j, old)) {
                        DBJobType.update(j);
                        refreshViewPrice();
                    }
                    else {
                        j.setName(old);
                        refreshViewJob();
                    }
                }

            }
        });

        /* Colunas Tabela de Preços */
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

        colJob.setCellValueFactory(new PropertyValueFactory<>("job"));
        colJob.setCellFactory(ComboBoxTableCell.forTableColumn(listJobType));
        colJob.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<JobPrice, JobType>>() {

            @Override
            public void handle(CellEditEvent<JobPrice, JobType> event) {
                JobPrice j = event.getTableView().getItems().get(event.getTablePosition().getRow());
                j.setJob(event.getNewValue());
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

    private void refreshViewPrice() {
        listPrice = FXCollections.observableArrayList(DBJobPrice.getList());
        viewPrice.getItems().clear();
        viewPrice.getItems().addAll(listPrice);
    }

    @FXML
    void newTable() {
        if (tabTable.isSelected()) {
            PriceTable p = new PriceTable();
            viewTable.getItems().add(p);
        }
        else {
            JobType j = new JobType();
            viewJob.getItems().add(j);
        }
    }

    @FXML
    void insertPrice() {
        JobPrice jp = new JobPrice();
        viewPrice.getItems().add(jp);
    }

    @FXML
    void deleteTable() {
        if (tabTable.isSelected()) {
            PriceTable p = viewTable.getSelectionModel().getSelectedItem();
            if (p != null && p.getId() != 0 && AlertDialog.showDeleteTable(p)) {
                DBPriceTable.delete(p.getId());
                refreshViewTable();
                refreshViewPrice();
            }
        }
        else {
            JobType j = viewJob.getSelectionModel().getSelectedItem();
            if (j != null && j.getId() != 0 && AlertDialog.showDeleteJobType(j)) {
                DBJobType.delete(j.getId());
                refreshViewJob();
                refreshViewPrice();
            }
        }
    }

    @FXML
    void deletePrice() {
        JobPrice jp = viewPrice.getSelectionModel().getSelectedItem();
        if (jp != null && jp.getId() != 0 && AlertDialog.showDeletePrice(jp)) {
            DBJobPrice.delete(jp.getId());
            refreshViewPrice();
        }
    }

    @FXML
    void savePrice() {
        for (JobPrice jp : newPrices) {
            DBJobPrice.update(jp);
        }

        if (paneInfo.getChildren().contains(labelNotif))
            paneInfo.getChildren().remove(labelNotif);

        AlertDialog.showUpdateSuccess();

        newPrices.clear();
    }
}
