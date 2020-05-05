package client;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import pricetable.PriceTable;


public class ClientCtrl {

    @FXML
    private TextField clientName;

    @FXML
    private TextField clientCpf;

    @FXML
    private TextField clientTel;

    @FXML
    private TextField clientCel;

    @FXML
    private TextField address;

    @FXML
    private TextField number;

    @FXML
    private TextField complement;

    @FXML
    private TextField city;

    @FXML
    private ComboBox<String> state;

    @FXML
    private ComboBox<PriceTable> clientTable;

    @FXML
    private TextField cep;

    @FXML
    private TextField respName;

    @FXML
    private TextField respTel;

    @FXML
    private TextField respCel;

    @FXML
    private TextField respCpf;

    @FXML
    private TableView<Client> viewClient;

    @FXML
    private TableColumn<Client, String> colClient;

    @FXML
    private TableColumn<Client, String> colResp;

    @FXML
    private TableColumn<Client, String> colCity;

    @FXML
    private TableColumn<Client, PriceTable> colTable;

    private ObservableList<Client> listClient;

    private ObservableList<PriceTable> listTables;

    private Client currentClient;

    public void initialize() {
        listClient = FXCollections.observableArrayList(DBConnection.listClients());
        viewClient.getItems().addAll(listClient);

        listTables = FXCollections.observableArrayList(DBConnection.listPriceTable());

        clientTable.getItems().addAll(listTables);
        clientTable.getSelectionModel().select(0);

        String siglasEstados[] = { "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA",
                "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO" };
        List<String> stringList = new ArrayList<String>(Arrays.asList(siglasEstados));
        state.getItems().addAll(stringList);
        state.getSelectionModel().select(0);

        createColumns();

        viewClient.setOnMouseClicked(new EventHandler<Event>() {

            @Override
            public void handle(Event event) {
                currentClient = viewClient.getSelectionModel().getSelectedItem();
                loadClient(currentClient);
            }
        });
    }

    private void createColumns() {
        /* Colunas Tipo de Tabela */
        colClient.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        colResp.setCellValueFactory(new PropertyValueFactory<>("respName"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colTable.setCellValueFactory(new PropertyValueFactory<>("priceTable"));
    }

    private void loadClient(Client c) {
        clientName.setText(c.getClientName());
        clientCpf.setText(c.getClientCpf());
        clientTel.setText(c.getClientTel());
        clientCel.setText(c.getClientCel());
        address.setText(c.getAddress());
        number.setText(String.valueOf(c.getNumber()));
        complement.setText(c.getComplement());
        city.setText(c.getCity());
        state.getSelectionModel().select(c.getState());
        cep.setText(c.getCep());
        findTable(c.getPriceTable());
        respName.setText(c.getRespName());
        respCpf.setText(c.getRespCpf());
        respTel.setText(c.getRespTel());
        respCel.setText(c.getRespCel());
    }

    @FXML
    void deleteClient() {

    }

    @FXML
    void newClient() {
        currentClient = null;

        clientName.clear();
        clientCpf.clear();
        clientTel.clear();
        clientCel.clear();
        respName.clear();
        respCpf.clear();
        respTel.clear();
        respCel.clear();
        address.clear();
        number.clear();
        complement.clear();
        city.clear();
        state.getSelectionModel().select(0);
        cep.clear();
        clientTable.getSelectionModel().select(0);
    }

    @FXML
    void saveClient() {
        Client c = new Client();;

        c.setClientName(clientName.getText());
        c.setClientCpf(clientCpf.getText());
        c.setClientTel(clientTel.getText());
        c.setClientCel(clientCel.getText());
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
        c.setPriceTable(clientTable.getSelectionModel().getSelectedItem());

        if (currentClient != null) {
            c.setId(currentClient.getId());
            DBConnection.updateClient(c);
        }else {
            DBConnection.insertClient(c);
        }      
        

        listClient = FXCollections.observableArrayList(DBConnection.listClients());
        viewClient.getItems().clear();
        viewClient.getItems().addAll(listClient);
    }

    @FXML
    void refresh() {
        listClient = FXCollections.observableArrayList(DBConnection.listClients());
        viewClient.getItems().clear();
        viewClient.getItems().addAll(listClient);

        listTables = FXCollections.observableArrayList(DBConnection.listPriceTable());
        clientTable.getItems().clear();
        clientTable.getItems().addAll(listTables);
    }

    private void findTable(PriceTable p) {
        for (PriceTable p1 : listTables) {
            if (p1.getId() == p.getId())
                clientTable.getSelectionModel().select(p1);
        }
    }
}