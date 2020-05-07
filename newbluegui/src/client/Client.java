package client;

import pricetable.PriceTable;

public class Client {
    private int id;
    private String name;
    private String cpf;
    private String tel;
    private String cel;
    private String respName;
    private String respCpf;
    private String respTel;
    private String respCel;
    private String address;
    private int number;
    private String compl;
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
        return name;
    }

    
    public void setClientName(String clientName) {
        this.name = clientName;
    }

    
    public String getClientCpf() {
        return cpf;
    }

    
    public void setClientCpf(String clientCpf) {
        this.cpf = clientCpf;
    }

    
    public String getClientTel() {
        return tel;
    }

    
    public void setClientTel(String clientTel) {
        this.tel = clientTel;
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
        return compl;
    }

    
    public void setComplement(String complement) {
        this.compl = complement;
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
        return cel;
    }


    public void setClientCel(String clientCel) {
        this.cel = clientCel;
    }


    public String getRespCel() {
        return respCel;
    }


    public void setRespCel(String respCel) {
        this.respCel = respCel;
    }


    @Override
    public String toString() {
        return this.name;
    }
}
