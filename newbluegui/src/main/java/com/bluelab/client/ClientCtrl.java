package com.bluelab.client;


import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

import com.bluelab.database.DBClient;
import com.bluelab.database.DBPriceTable;
import com.bluelab.pricetable.PriceTable;
import com.bluelab.util.AlertDialog;
import com.bluelab.util.ComboBoxSearch;
import com.bluelab.util.FxmlInterface;
import com.bluelab.util.IBGEAPI;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;


public class ClientCtrl implements FxmlInterface {

    @FXML
    private GridPane grid;

    @FXML
    private JFXButton btnPrint;

    @FXML
    private JFXTextField name;

    @FXML
    private JFXTextField tel;
    
    @FXML
    private JFXTextField email;

    @FXML
    private JFXTextField address;

    @FXML
    private JFXTextField complement;

    @FXML
    private JFXTextField cpf;

    @FXML
    private JFXTextField cel;

    @FXML
    private JFXTextField number;

    @FXML
    private ComboBoxSearch<String> city;

    @FXML
    private JFXTextField district;

    @FXML
    private JFXTextField cep;

    @FXML
    private JFXTextField respName;

    @FXML
    private JFXTextField respTel;

    @FXML
    private JFXTextField respCpf;

    @FXML
    private JFXTextField respCel;

    private ComboBoxSearch<String> state;

    @FXML
    private JFXComboBox<PriceTable> priceTable;

    @FXML
    private TableView<Client> viewClient;

    @FXML
    private TableColumn<Client, String> colClient;

    @FXML
    private TableColumn<Client, String> colCity;

    @FXML
    private TableColumn<Client, PriceTable> colTable;

    private Client currentClient;

    private List<Client> selectedClient;

    private IBGEAPI ibgeapi;

    public void initialize() {
        viewClient.setItems(DBClient.getList());
        viewClient.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        selectedClient = new ArrayList<Client>();

        priceTable.setItems(DBPriceTable.list());
        priceTable.getSelectionModel().select(0);

        ibgeapi = new IBGEAPI();

        state = new ComboBoxSearch<String>();
        city = new ComboBoxSearch<String>();

        state.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                city.setItems(FXCollections.observableList(ibgeapi.getCidades(newValue)));
                city.getSelectionModel().clearSelection();
            }
        });

        grid.getChildren().add(state);
        grid.getChildren().add(city);

        state.setMaxWidth(Double.MAX_VALUE);
        city.setMaxWidth(Double.MAX_VALUE);

        GridPane.setColumnIndex(state, 1);
        GridPane.setRowIndex(state, 4);
        GridPane.setColumnIndex(city, 3);
        GridPane.setRowIndex(city, 4);

        state.setItems(FXCollections.observableList(ibgeapi.getUfs()));
        state.getSelectionModel().clearSelection();

        createColumns();

        viewClient.setOnMouseClicked(event -> {
            currentClient = viewClient.getSelectionModel().getSelectedItem();
            if (currentClient != null) {
                loadClient(currentClient);

                if (selectedClient.contains(currentClient)) {
                    selectedClient.remove(currentClient);
                }
                else {
                    selectedClient.add(currentClient);
                }
            }
        });
    }

    private void createColumns() {
        colClient.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colTable.setCellValueFactory(new PropertyValueFactory<>("priceTable"));
    }

    private void loadClient(Client c) {
        name.setText(c.getName());
        cpf.setText(c.getCpf());
        tel.setText(c.getTel());
        cel.setText(c.getCel());
        email.setText(c.getEmail());
        address.setText(c.getAddress());
        number.setText(c.getNumber() == 0 ? "" : String.valueOf(c.getNumber()));
        complement.setText(c.getCompl());
        district.setText(c.getDistrict());
        city.getSelectionModel().select(c.getCity());
        state.getSelectionModel().select(c.getState());
        cep.setText(c.getCep());
        priceTable.getSelectionModel().select(c.getPriceTable());
        respName.setText(c.getRespName());
        respCpf.setText(c.getRespCpf());
        respTel.setText(c.getRespTel());
        respCel.setText(c.getRespCel());

        disableFields(true);
        btnPrint.setDisable(false);
    }

    @Override
    public void disableFields(boolean d) {
        name.setDisable(d);
        cpf.setDisable(d);
        tel.setDisable(d);
        cel.setDisable(d);
        email.setDisable(d);
        address.setDisable(d);
        number.setDisable(d);
        complement.setDisable(d);
        district.setDisable(d);
        city.setDisable(d);
        state.setDisable(d);
        cep.setDisable(d);
        priceTable.setDisable(d);
        respName.setDisable(d);
        respCpf.setDisable(d);
        respTel.setDisable(d);
        respCel.setDisable(d);
    }

    private void clearFields() {
        name.clear();
        cpf.clear();
        tel.clear();
        cel.clear();
        email.clear();
        respName.clear();
        respCpf.clear();
        respTel.clear();
        respCel.clear();
        address.clear();
        number.clear();
        complement.clear();
        district.clear();
        city.getSelectionModel().clearSelection();
        state.getSelectionModel().clearSelection();
        cep.clear();
        priceTable.getSelectionModel().select(0);
    }

    @FXML
    @Override
    public void create() {
        currentClient = null;

        clearFields();
        disableFields(false);
        btnPrint.setDisable(true);
    }

    @FXML
    @Override
    public void edit() {
        disableFields(false);
    }

    @FXML
    @Override
    public void save() {
        Client c = new Client();

        c.setName(name.getText());
        c.setCpf(cpf.getText());
        c.setTel(tel.getText());
        c.setCel(cel.getText());
        c.setEmail(email.getText());
        c.setRespName(respName.getText());
        c.setRespCpf(respCpf.getText());
        c.setRespTel(respTel.getText());
        c.setRespCel(respCel.getText());
        c.setAddress(address.getText());
        c.setNumber(number.getText().equals("") ? 0 : Integer.parseInt(number.getText()));
        c.setDistrict(district.getText());
        c.setCompl(complement.getText());
        c.setCity(city.getSelectionModel().getSelectedItem());
        c.setState(state.getSelectionModel().getSelectedItem());
        c.setCep(cep.getText());
        c.setPriceTable(priceTable.getSelectionModel().getSelectedItem());

        if (currentClient != null) {
            if (AlertDialog.updateAlert(c)) {
                c.setId(currentClient.getId());
                DBClient.update(c);
            }
        }
        else {
            if (AlertDialog.saveNewAlert(c))
                DBClient.insert(c);
        }

        clearFields();
        viewClient.refresh();
        disableFields(false);
        btnPrint.setDisable(true);
    }

    @FXML
    @Override
    public void delete() {
        if (currentClient != null) {
            if (AlertDialog.deleteAlert(currentClient)) {
                DBClient.delete(currentClient.getId());
                clearFields();
                viewClient.refresh();
            }
        }

        disableFields(false);
        btnPrint.setDisable(true);
    }

    @FXML
    void print() {
        if (currentClient == null)
            return;

        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("choosertitle");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        int returnVal = chooser.showOpenDialog(null);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }
        Document document = new Document();

        try {

            PdfWriter.getInstance(document,
                    new FileOutputStream(chooser.getSelectedFile().getAbsolutePath() + "/" + "Etiqueta.pdf"));
            document.open();

            for (Client client : selectedClient) {
                document.add(new Paragraph("Destinatário:"));
                document.add(new Paragraph(client.getName()));
                document.add(new Paragraph(client.getAddress() + ", " + client.getNumber()));
                document.add(new Paragraph(client.getCompl()));
                document.add(new Paragraph(client.getDistrict()));
                document.add(new Paragraph(client.getCity() + "-" + client.getState()));
                document.add(new Paragraph(client.getCep()));
                document.add(new Paragraph("Remetente:"));
                document.add(new Paragraph("Destinatário:"));
                // TODO dados lab
                document.add(new Paragraph("-----------------------------------------------"));
            }
        }
        catch (DocumentException de) {
            System.err.println(de.getMessage());
        }
        catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }

}
