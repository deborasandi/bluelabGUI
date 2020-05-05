package jobprice;


import database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
import job.Job;
import pricetable.PriceTable;


public class JobPriceCtrl {

    @FXML
    private TableView<PriceTable> viewTable;

    @FXML
    private TableColumn<PriceTable, Integer> colTableId;

    @FXML
    private TableColumn<PriceTable, String> colTableName;

    @FXML
    private TableView<Job> viewJob;

    @FXML
    private TableColumn<Job, Integer> colJobId;

    @FXML
    private TableColumn<Job, String> colJobName;

    @FXML
    private TableView<JobPrice> viewPrice;

    @FXML
    private TableColumn<JobPrice, PriceTable> colTable;

    @FXML
    private TableColumn<JobPrice, Job> colJob;

    @FXML
    private TableColumn<JobPrice, Double> colPrice;

    private ObservableList<PriceTable> listPriceTable;

    private ObservableList<Job> listJob;

    private ObservableList<JobPrice> listPrice;

    public void initialize() {
        viewTable.setEditable(true);
        viewJob.setEditable(true);
        viewPrice.setEditable(true);

        listPriceTable = FXCollections.observableArrayList(DBConnection.listPriceTable());
        viewTable.getItems().addAll(listPriceTable);

        listJob = FXCollections.observableArrayList(DBConnection.listJob());
        viewJob.getItems().addAll(listJob);

        listPrice = FXCollections.observableArrayList(DBConnection.listJobPrice());
        viewPrice.getItems().addAll(listPrice);

        createColumns();
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
                p.setName(event.getNewValue());

                if (p.getId() == 0)
                    DBConnection.insertPriceTable(p);
                else
                    DBConnection.updatePriceTable(p);

                refreshViewTable();
            }
        });

        /* Colunas Tabela de Trabalhos */
        colJobId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colJobName.setCellValueFactory(new PropertyValueFactory<>("name"));

        colJobName.setCellFactory(TextFieldTableCell.forTableColumn());

        colJobName.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Job, String>>() {

            @Override
            public void handle(CellEditEvent<Job, String> event) {
                Job j = event.getTableView().getItems().get(event.getTablePosition().getRow());
                j.setName(event.getNewValue());

                if (j.getId() == 0)
                    DBConnection.insertJob(j);
                else
                    DBConnection.updateJob(j);

                refreshViewJob();
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

                if (j.getJob() != null && j.getPrice() > 0) {
                    if (j.getId() == 0)
                        DBConnection.insertJobPrice(j);
                    else
                        DBConnection.updateJobPrice(j);
                    refreshViewPrice();
                }
            }
        });

        colJob.setCellValueFactory(new PropertyValueFactory<>("job"));
        colJob.setCellFactory(ComboBoxTableCell.forTableColumn(listJob));
        colJob.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<JobPrice, Job>>() {

            @Override
            public void handle(CellEditEvent<JobPrice, Job> event) {
                JobPrice j = event.getTableView().getItems().get(event.getTablePosition().getRow());
                j.setJob(event.getNewValue());

                if (j.getPriceTable() != null && j.getPrice() > 0) {
                    if (j.getId() == 0)
                        DBConnection.insertJobPrice(j);
                    else
                        DBConnection.updateJobPrice(j);
                    refreshViewPrice();
                }
            }
        });

        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colPrice.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        colPrice.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<JobPrice, Double>>() {

            @Override
            public void handle(CellEditEvent<JobPrice, Double> event) {
                JobPrice j = event.getTableView().getItems().get(event.getTablePosition().getRow());
                j.setPrice(event.getNewValue());

                if (j.getPriceTable() != null && j.getJob() != null) {
                    if (j.getId() == 0)
                        DBConnection.insertJobPrice(j);
                    else
                        DBConnection.updateJobPrice(j);
                    refreshViewPrice();
                }
            }

        });
    }

    private void refreshViewTable() {
        listPriceTable = FXCollections.observableArrayList(DBConnection.listPriceTable());
        viewTable.getItems().clear();
        viewTable.getItems().addAll(listPriceTable);
    }

    private void refreshViewJob() {
        listJob = FXCollections.observableArrayList(DBConnection.listJob());
        viewJob.getItems().clear();
        viewJob.getItems().addAll(listJob);
    }

    private void refreshViewPrice() {
        listPrice = FXCollections.observableArrayList(DBConnection.listJobPrice());
        viewPrice.getItems().clear();
        viewPrice.getItems().addAll(listPrice);
    }

    @FXML
    void insertTable() {
        PriceTable p = new PriceTable();
        viewTable.getItems().add(p);
    }

    @FXML
    void insertJob() {
        Job j = new Job();
        viewJob.getItems().add(j);
    }

    @FXML
    void insertPrice() {
        JobPrice jp = new JobPrice();
        viewPrice.getItems().add(jp);
    }

    @FXML
    void deleteTable() {
        PriceTable p = viewTable.getSelectionModel().getSelectedItem();
        if (p != null && p.getId() != 0)
            DBConnection.deletePriceTable(p.getId());
        refreshViewTable();
        refreshViewPrice();
    }

    @FXML
    void deleteJob() {
        Job j = viewJob.getSelectionModel().getSelectedItem();
        if (j != null && j.getId() != 0)
            DBConnection.deleteJob(j.getId());
        refreshViewJob();
        refreshViewPrice();
    }

    @FXML
    void deletePrice() {
        JobPrice jp = viewPrice.getSelectionModel().getSelectedItem();
        if (jp != null && jp.getId() != 0)
            DBConnection.deleteJobPrice(jp.getId());
        refreshViewPrice();
    }
}
