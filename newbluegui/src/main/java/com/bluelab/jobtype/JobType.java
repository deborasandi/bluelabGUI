package com.bluelab.jobtype;

public class JobType {
    private int id;
    private String name;

    public JobType(int id, String name) {
    }

    public JobType() {
    }

    public JobType(String name) {
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

    @Override
    public String toString() {
        return name;
    }
    
    
}
