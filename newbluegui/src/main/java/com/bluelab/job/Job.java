package com.bluelab.job;


import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.bluelab.client.Client;
import com.bluelab.jobprice.JobPrice;
import com.bluelab.productcolor.ProductColor;

@Entity
@Table(name = "job")
public class Job implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobprice_id")
    private JobPrice jobPrice;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productcolor_id")
    private ProductColor productColor;
    
    @Column(name = "qtd")
    private int qtd;
    
    @Column(name = "shipping")
    private double shipping;
    
    @Column(name = "date")
    private Date date;
    
    @Column(name = "isrepetition")
    private boolean repetition;
    
    @Column(name = "isnocost")
    private boolean nocost;
    
    @Column(name = "total")
    private double total;
    
    @Column(name = "ispaid")
    private boolean paid;
    
    @Column(name = "totalpaid")
    private double totalPaid;
    
    @Column(name = "repvalue")
    private double repValue;
    
    @Column(name = "obs")
    private String obs;

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

    public double getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(double totalPaid) {
        this.totalPaid = totalPaid;
    }

    public double getRepValue() {
        return repValue;
    }

    public void setRepValue(double repValue) {
        this.repValue = repValue;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }
    
    public String getClassName() {
        return "Trabalho";
    }
}
