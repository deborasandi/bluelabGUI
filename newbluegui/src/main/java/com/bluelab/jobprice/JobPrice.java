package com.bluelab.jobprice;

import com.bluelab.jobtype.JobType;
import com.bluelab.pricetable.PriceTable;

public class JobPrice {
    private int id;
    private JobType jobType;
    private PriceTable priceTable;
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
