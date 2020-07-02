package com.bluelab.jobprice;


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

import com.bluelab.jobtype.JobType;
import com.bluelab.pricetable.PriceTable;

@Entity
@Table(name = "jobprice")
public class JobPrice implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobtype_id")
    private JobType jobType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pricetable_id")
    private PriceTable priceTable;
    
    @Column(name = "price")
    private double price;

    public JobPrice(int id, JobType job, PriceTable priceTable, double price) {
        super();
        this.id = id;
        this.jobType = job;
        this.priceTable = priceTable;
        this.price = price;
    }

    public JobPrice(JobType job, PriceTable priceTable, double price) {
        this.jobType = job;
        this.priceTable = priceTable;
        this.price = price;
    }

    public JobPrice() {
        // TODO Auto-generated constructor stub
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public PriceTable getPriceTable() {
        return priceTable;
    }

    public void setPriceTable(PriceTable priceTable) {
        this.priceTable = priceTable;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return this.getJobType().getName();
    }

    public String getClassName() {
        return "Preço";
    }
}
