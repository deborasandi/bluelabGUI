package com.bluelab.job;


import java.sql.Date;

import com.bluelab.client.Client;
import com.bluelab.jobprice.JobPrice;
import com.bluelab.productcolor.ProductColor;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;


public class Job {

    private int id;
    private Client client;
    private JobPrice jobPrice;
    private ProductColor productColor;
    private int qtd;
    private double shipping;
    private Date date;
    private boolean repetition;
    private boolean nocost;
    private double total;
    private boolean paid;

    private BooleanProperty paidProperty = new SimpleBooleanProperty();

    public Job() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public JobPrice getJobPrice() {
        return jobPrice;
    }

    public void setJobPrice(JobPrice jobPrice) {
        this.jobPrice = jobPrice;
    }

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }

    public double getShipping() {
        return shipping;
    }

    public void setShipping(double shipping) {
        this.shipping = shipping;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isRepetition() {
        return repetition;
    }

    public void setRepetition(boolean repetition) {
        this.repetition = repetition;
        paidProperty.set(paid);
    }

    public BooleanProperty paidProperty() {
        paidProperty.set(paid);
        return paidProperty;
    }

    public boolean isNocost() {
        return nocost;
    }

    public void setNocost(boolean nocost) {
        this.nocost = nocost;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public BooleanProperty getPaidProperty() {
        return paidProperty;
    }

    public void setPaidProperty(BooleanProperty paidProperty) {
        this.paidProperty = paidProperty;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public ProductColor getProductColor() {
        return productColor;
    }

    public void setProductColor(ProductColor productColor) {
        this.productColor = productColor;
    }

    public String getClassName() {
        return "Trabalho";
    }
}