package job;


import java.sql.Date;

import client.Client;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import jobtype.JobType;


public class Job {

    private int id;
    private Client client;
    private JobType jobType;
    private int qtd;
    private double shipping;
    private Date date;
    private boolean repetition;
    private boolean nocost;
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

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
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
}
