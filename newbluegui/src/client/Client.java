package client;

import pricetable.PriceTable;

public class Client {
    private int id;
    private String clientName;
    private String clientCpf;
    private String clientTel;
    private String clientCel;
    private String respName;
    private String respCpf;
    private String respTel;
    private String respCel;
    private String address;
    private int number;
    private String complement;
    private String city;
    private String state;
    private String cep;
    private PriceTable priceTable;
    
    public Client() {
        
    }

    
    public int getId() {
        return id;
    }

    
    public void setId(int id) {
        this.id = id;
    }

    
    public String getClientName() {
        return clientName;
    }

    
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    
    public String getClientCpf() {
        return clientCpf;
    }

    
    public void setClientCpf(String clientCpf) {
        this.clientCpf = clientCpf;
    }

    
    public String getClientTel() {
        return clientTel;
    }

    
    public void setClientTel(String clientTel) {
        this.clientTel = clientTel;
    }

    
    public String getRespName() {
        return respName;
    }

    
    public void setRespName(String respName) {
        this.respName = respName;
    }

    
    public String getRespCpf() {
        return respCpf;
    }

    
    public void setRespCpf(String respCpf) {
        this.respCpf = respCpf;
    }

    
    public String getRespTel() {
        return respTel;
    }

    
    public void setRespTel(String respTel) {
        this.respTel = respTel;
    }

    
    public String getAddress() {
        return address;
    }

    
    public void setAddress(String address) {
        this.address = address;
    }

    
    public int getNumber() {
        return number;
    }

    
    public void setNumber(int number) {
        this.number = number;
    }

    
    public String getComplement() {
        return complement;
    }

    
    public void setComplement(String complement) {
        this.complement = complement;
    }

    
    public String getCity() {
        return city;
    }

    
    public void setCity(String city) {
        this.city = city;
    }

    
    public String getState() {
        return state;
    }

    
    public void setState(String state) {
        this.state = state;
    }

    
    public String getCep() {
        return cep;
    }

    
    public void setCep(String cep) {
        this.cep = cep;
    }

    
    public PriceTable getPriceTable() {
        return priceTable;
    }

    
    public void setPriceTable(PriceTable priceTable) {
        this.priceTable = priceTable;
    }


    public String getClientCel() {
        return clientCel;
    }


    public void setClientCel(String clientCel) {
        this.clientCel = clientCel;
    }


    public String getRespCel() {
        return respCel;
    }


    public void setRespCel(String respCel) {
        this.respCel = respCel;
    }


    @Override
    public String toString() {
        return this.clientName;
    }
}
