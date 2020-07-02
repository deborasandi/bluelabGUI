package com.bluelab.client;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.bluelab.pricetable.PriceTable;

@Entity
@Table(name = "client")
public class Client implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "cpf")
    private String cpf;
    
    @Column(name = "tel")
    private String tel;
    
    @Column(name = "cel")
    private String cel;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "respname")
    private String respName;
    
    @Column(name = "respcpf")
    private String respCpf;
    
    @Column(name = "resptel")
    private String respTel;
    
    @Column(name = "respcel")
    private String respCel;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "number")
    private int number;
    
    @Column(name = "compl")
    private String compl;
    
    @Column(name = "district")
    private String district;
    
    @Column(name = "city")
    private String city;
    
    @Column(name = "state")
    private String state;
    
    @Column(name = "cep")
    private String cep;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pricetable_id")
    private PriceTable priceTable;
    
//    private double total;

    public Client() {

    }

    public Client(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCel() {
        return cel;
    }

    public void setCel(String cel) {
        this.cel = cel;
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

    public String getRespCel() {
        return respCel;
    }

    public void setRespCel(String respCel) {
        this.respCel = respCel;
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

    public String getCompl() {
        return compl;
    }

    public void setCompl(String compl) {
        this.compl = compl;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
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

//    public double getTotal() {
//        return total;
//    }
//
//    public void setTotal(double total) {
//        this.total = total;
//    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public String getClassName() {
        return "Cliente";
    }
}
