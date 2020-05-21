package com.bluelab.data;


import com.bluelab.client.Client;
import com.bluelab.database.DBClient;
import com.bluelab.database.DBJob;
import com.bluelab.database.DBJobPrice;
import com.bluelab.database.DBJobType;
import com.bluelab.database.DBPriceTable;
import com.bluelab.job.Job;
import com.bluelab.jobprice.JobPrice;
import com.bluelab.jobtype.JobType;
import com.bluelab.pricetable.PriceTable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class BlueData {

    private static ObservableList<PriceTable> listPriceTable;
    private static ObservableList<JobType> listJobType;
    private static ObservableList<JobPrice> listJobPrice;
    private static ObservableList<Job> listJob;
    
    public static void init() {
        listPriceTable = FXCollections.observableArrayList(DBPriceTable.getList());
        listJobType = FXCollections.observableArrayList(DBJobType.getList());
        listJobPrice = FXCollections.observableArrayList(DBJobPrice.getList());
        listJob = FXCollections.observableArrayList(DBJob.getList());
    }

    public static ObservableList<PriceTable> getListPriceTable() {
        return listPriceTable;
    }

    public static void setListPriceTable(ObservableList<PriceTable> listPriceTable) {
        BlueData.listPriceTable = listPriceTable;
    }

    public static ObservableList<JobType> getListJobType() {
        return listJobType;
    }

    public static void setListJobType(ObservableList<JobType> listJobType) {
        BlueData.listJobType = listJobType;
    }

    public static ObservableList<JobPrice> getListJobPrice() {
        return listJobPrice;
    }

    public static void setListJobPrice(ObservableList<JobPrice> listJobPrice) {
        BlueData.listJobPrice = listJobPrice;
    }

    public static ObservableList<Job> getListJob() {
        return listJob;
    }

    public static void setListJob(ObservableList<Job> listJob) {
        BlueData.listJob = listJob;
    }

}
