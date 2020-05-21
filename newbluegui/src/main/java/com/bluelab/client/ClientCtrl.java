package com.bluelab.client;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.bluelab.data.BlueData;
import com.bluelab.database.DBClient;
import com.bluelab.pricetable.PriceTable;
import com.bluelab.util.AlertDialog;
import com.bluelab.util.FxmlInterface;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


public class ClientCtrl implements FxmlInterface {

    @FXML
    private JFXTextField name;

    @FXML
    private JFXTextField tel;

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
    private JFXTextField city;

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

    @FXML
    private JFXComboBox<String> state;

    @FXML
    private JFXComboBox<PriceTable> priceTable;

    @FXML
    private TableView<Client> viewClient;

    @FXML
    private TableColumn<Client, String> colClient;

    @FXML
    private TableColumn<Client, PriceTable> colTable;

    private Client currentClient;
    
    public void initialize() {
        viewClient.setItems(DBClient.getList());

        priceTable.getItems().addAll(BlueData.getListPriceTable());
        priceTable.getSelectionModel().select(0);

        String siglasEstados[] = { "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA",
                "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO" };
        List<String> stringList = new ArrayList<String>(Arrays.asList(siglasEstados));
        state.getItems().addAll(stringList);
        state.getSelectionModel().select("PR");

        createColumns();

        viewClient.setOnMouseClicked(new EventHandler<Event>() {

            @Override
            public void handle(Event event) {
                currentClient = viewClient.getSelectionModel().getSelectedItem();
                if (currentClient != null)
                    loadClient(currentClient);
            }
        });
    }

    private void createColumns() {
        colClient.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        colTable.setCellValueFactory(new PropertyValueFactory<>("priceTable"));
    }

    private void loadClient(Client c) {
        name.setText(c.getClientName());
        cpf.setText(c.getClientCpf());
        tel.setText(c.getClientTel());
        cel.setText(c.getClientCel());
        address.setText(c.getAddress());
        number.setText(c.getNumber() == 0 ? "" : String.valueOf(c.getNumber()));
        complement.setText(c.getComplement());
        city.setText(c.getCity());
        state.getSelectionModel().select(c.getState());
        cep.setText(c.getCep());
        findTable(c.getPriceTable());
        respName.setText(c.getRespName());
        respCpf.setText(c.getRespCpf());
        respTel.setText(c.getRespTel());
        respCel.setText(c.getRespCel());

        disableFields(true);
    }

    @Override
    public void disableFields(boolean d) {
        name.setDisable(d);
        cpf.setDisable(d);
        tel.setDisable(d);
        cel.setDisable(d);
        address.setDisable(d);
        number.setDisable(d);
        complement.setDisable(d);
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
        respName.clear();
        respCpf.clear();
        respTel.clear();
        respCel.clear();
        address.clear();
        number.clear();
        complement.clear();
        city.clear();
        state.getSelectionModel().select("PR");
        cep.clear();
        priceTable.getSelectionModel().select(0);
    }

    private void findTable(PriceTable p) {
        for (PriceTable p1 : BlueData.getListPriceTable()) {
            if (p1.getId() == p.getId()) {
                priceTable.getSelectionModel().select(p1);
                break;
            }
        }
    }

    public void refreshPriceTables() {
        priceTable.getItems().clear();
        priceTable.getItems().addAll(BlueData.getListPriceTable());
        priceTable.getSelectionModel().select(0);
    }

    @FXML
    @Override
    public void create() {
        currentClient = null;

        clearFields();
        disableFields(false);
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

        c.setClientName(name.getText());
        c.setClientCpf(cpf.getText());
        c.setClientTel(tel.getText());
        c.setClientCel(cel.getText());
        c.setRespName(respName.getText());
        c.setRespCpf(respCpf.getText());
        c.setRespTel(respTel.getText());
        c.setRespCel(respCel.getText());
        c.setAddress(address.getText());
        c.setNumber(number.getText().equals("") ? 0 : Integer.parseInt(number.getText()));
        c.setComplement(complement.getText());
        c.setCity(city.getText());
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
    }
}
