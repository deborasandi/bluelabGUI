package jobprice;

import jobtype.JobType;
import pricetable.PriceTable;

public class JobPrice {
    private int id;
    private JobType job;
    private PriceTable priceTable;
    private double price;
    
    public JobPrice(int id, JobType job, PriceTable priceTable, double price) {
        super();
        this.id = id;
        this.job = job;
        this.priceTable = priceTable;
        this.price = price;
    }
    
    public JobPrice() {
        
    }

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public JobType getJob() {
        return job;
    }
    
    public void setJob(JobType job) {
        this.job = job;
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
    
    
}
