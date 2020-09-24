package com.bluelab.payment;

import java.sql.Date;

import com.bluelab.client.Client;
import com.bluelab.job.Job;

public class Payment {

	private int id;
	private Client client;
	private Job job;
	private Date date;

	public Payment() {

	}

	public Payment(Client client, Job job, Date date) {
		super();
		this.client = client;
		this.job = job;
		this.date = date;
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

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
