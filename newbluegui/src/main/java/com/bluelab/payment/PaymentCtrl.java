package com.bluelab.payment;


import java.io.IOException;
import java.sql.Date;
import java.util.List;

import com.bluelab.client.Client;
import com.bluelab.database.DBClient;
import com.bluelab.database.DBJob;
import com.bluelab.database.DBJobPayment;
import com.bluelab.database.DBPayment;
import com.bluelab.invoice.InvoiceCtrl;
import com.bluelab.job.Job;
import com.bluelab.util.ComboBoxSearch;
import com.bluelab.util.DateUtil;
import com.bluelab.util.FxmlInterface;
import com.bluelab.util.RealTableCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTabPane;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class PaymentCtrl implements FxmlInterface {

    private ComboBoxSearch<Client> cbxClient;

    @FXML
    private JFXButton btnNew;

    @FXML
    private JFXButton btnEdit;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXTabPane tabpane;

    @FXML
    private AnchorPane paneInvoice;

    @FXML
    private TableView<Payment> viewPayment;

    @FXML
    private TableColumn<Payment, Client> colClient;

    @FXML
    private TableColumn<Payment, Date> colDate;

    @FXML
    private TableColumn<Payment, Double> colValue;

    @FXML
    private VBox vbox;

    @FXML
    private JFXDatePicker datepicker;

    @FXML
    private HBox contentFilter;

    @FXML
    private Label lblFiltros;

    @FXML
    private Label lblNumFiltros;

    @FXML
    private ImageView imgFilter;

    public void initialize() {
        viewPayment.setItems(DBPayment.getList());

        viewPayment.setEditable(true);

        cbxClient = new ComboBoxSearch<Client>();
        vbox.getChildren().add(1, cbxClient);
        cbxClient.setMaxWidth(Double.MAX_VALUE);

        colClient.setCellValueFactory(new PropertyValueFactory<>("client"));
        colClient.setCellFactory(ComboBoxTableCell.forTableColumn(DBClient.getList()));
        colClient.setOnEditCommit(event -> {
            Payment p = event.getTableView().getItems().get(event.getTablePosition().getRow());
            p.setClient(event.getNewValue());

            if (p.getValue() > 0) {
                DBPayment.update(p);
                createJobPayment(p);
            }
        });

        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        colValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        colValue.setCellFactory(c -> new RealTableCell<Payment>(true));
        colValue.setOnEditCommit(event -> {
            Payment p = event.getTableView().getItems().get(event.getTablePosition().getRow());
            p.setValue(event.getNewValue());

            if (p.getClient() != null) {
                DBPayment.update(p);
                createJobPayment(p);
            }
        });

        tabpane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            disableFields(tabpane.getSelectionModel().getSelectedIndex() == 1 ? false : true);
        });

        try {
            FXMLLoader loader = new FXMLLoader(InvoiceCtrl.class.getResource("Invoice.fxml"));
            Parent root = loader.load();
            paneInvoice.getChildren().add(root);

            AnchorPane.setTopAnchor(root, 0.0);
            AnchorPane.setLeftAnchor(root, 0.0);
            AnchorPane.setRightAnchor(root, 0.0);
            AnchorPane.setBottomAnchor(root, 0.0);

        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        cbxClient.setItems(FXCollections.observableArrayList(DBClient.getList()));
        cbxClient.getItems().add(0, new Client("Todos"));
        cbxClient.getSelectionModel().select(0);
    }

    @Override
    public void create() {
        Payment p = new Payment();
        p.setDate(DateUtil.dateNow());

        DBPayment.insert(p);
    }

    @Override
    public void edit() {
        // TODO Auto-generated method stub

    }

    @Override
    public void save() {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() {
        Payment p = viewPayment.getSelectionModel().getSelectedItem();

        List<JobPayment> l = DBJobPayment.list(p);

        for (JobPayment jobPayment : l) {
            Job j = jobPayment.getJob();
            j.setTotalPaid(j.getTotalPaid() - jobPayment.getValue());
            j.setPaid(false);

            DBJob.update(j);
        }

        DBPayment.delete(p.getId());
    }

    @Override
    public void disableFields(boolean b) {
        btnNew.setDisable(b);
        btnEdit.setDisable(b);
        btnSave.setDisable(b);
        btnDelete.setDisable(b);
    }

    @FXML
    void filter() {

    }

    @FXML
    void removeFilter() {

    }

    private void createJobPayment(Payment p) {
        Client c = p.getClient();

        List<Job> aux = DBJob.list(c);

        double tp = p.getValue();

        for (Job job : aux) {

            double falta = job.getTotal() - job.getTotalPaid();

            if (falta > tp) {
                job.setTotalPaid(job.getTotalPaid() + tp);
                tp = 0;
            }
            else {
                job.setTotalPaid(job.getTotal());
                job.setPaid(true);
                tp -= falta;
            }

            if (p != null) {
                DBJobPayment.insert(new JobPayment(job, p, job.getTotalPaid()));
            }

            DBJob.update(job);

            if (tp <= 0)
                break;
        }
    }

}
