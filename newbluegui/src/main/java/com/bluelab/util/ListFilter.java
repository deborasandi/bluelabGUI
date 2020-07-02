package com.bluelab.util;


import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.bluelab.client.Client;
import com.bluelab.jobtype.JobType;
import com.bluelab.productcolor.ProductColor;


public class ListFilter<Job> extends ArrayList<Job> {

    private int numFilter;

    private boolean filterClient;
    private boolean filterJobType;
    private boolean filterProductColor;
    private boolean filterIsRepetition;
    private boolean filterIsNoCost;
    private boolean filterIsPaid;
    private boolean filterInitDate;
    private boolean filterEndDate;

    public ListFilter(List<Job> l) {
        this.addAll(l);

        numFilter = 0;
    }

    public double filterClient(Client f) {
        if (f == null || f.getId() == 0) {
            if (filterClient) {
                filterClient = false;
                numFilter--;
            }
            return 0;
        }

        List<Job> aux = new ArrayList<Job>();
        double total = 0;
        for (Job j : this) {
            if (((com.bluelab.job.Job) j).getClient().getId() == f.getId()) {
                aux.add(j);
                
                if(!((com.bluelab.job.Job) j).isPaid()) {
                    total += ((com.bluelab.job.Job) j).getTotal() - ((com.bluelab.job.Job) j).getTotalPaid();
                }
            }
        }

        this.clear();
        this.addAll(aux);

        filterClient = true;
        numFilter++;
        
        return total;
    }

    public void filterJobType(JobType f) {
        if (f == null || f.getId() == 0) {
            if (filterJobType) {
                filterJobType = false;
                numFilter--;
            }
            return;
        }

        List<Job> aux = new ArrayList<Job>();

        for (Job j : this) {
            if (((com.bluelab.job.Job) j).getJobPrice().getJobType().getId() == f.getId())
                aux.add(j);
        }

        this.clear();
        this.addAll(aux);

        filterJobType = true;
        numFilter++;
    }

    public void filterProductColor(ProductColor f) {
        if (f == null || f.getId() == 0) {
            if (filterProductColor) {
                filterProductColor = false;
                numFilter--;
            }
            return;
        }

        List<Job> aux = new ArrayList<Job>();

        for (Job j : this) {
            if (((com.bluelab.job.Job) j).getProductColor() != null
                    && ((com.bluelab.job.Job) j).getProductColor().getId() == f.getId())
                aux.add(j);
        }

        this.clear();
        this.addAll(aux);

        filterProductColor = true;
        numFilter++;
    }

    public void filterIsRepetition(boolean f, boolean g) {
        if (!f && !g) {
            if (filterIsRepetition) {
                filterIsRepetition = false;
                numFilter--;
            }
            return;
        }

        List<Job> aux = new ArrayList<Job>();

        for (Job j : this) {
            if (((com.bluelab.job.Job) j).isRepetition() == f)
                aux.add(j);
        }

        this.clear();
        this.addAll(aux);

        filterIsRepetition = true;
        numFilter++;
    }

    public void filterIsNoCost(boolean f, boolean g) {
        if (!f && !g) {
            if (filterIsNoCost) {
                filterIsNoCost = false;
                numFilter--;
            }
            return;
        }

        List<Job> aux = new ArrayList<Job>();

        for (Job j : this) {
            if (((com.bluelab.job.Job) j).isNocost() == g)
                aux.add(j);
        }

        this.clear();
        this.addAll(aux);

        filterIsNoCost = true;
        numFilter++;
    }

    public void filterIsPaid(boolean f, boolean g) {
        if (!f && !g) {
            if (filterIsPaid) {
                filterIsPaid = false;
                numFilter--;
            }
            return;
        }

        List<Job> aux = new ArrayList<Job>();

        for (Job j : this) {
            if (((com.bluelab.job.Job) j).isPaid() == f)
                aux.add(j);
        }

        this.clear();
        this.addAll(aux);

        filterIsPaid = true;
        numFilter++;
    }

    public void filterInitDate(LocalDate f) {
        if (f == null || f == null) {
            if (filterInitDate) {
                filterInitDate = false;
                numFilter--;
            }
            return;
        }

        Date d = java.sql.Date.valueOf(f);

        List<Job> aux = new ArrayList<Job>();

        for (Job j : this) {
            if (((com.bluelab.job.Job) j).getDate().after(d) || ((com.bluelab.job.Job) j).getDate().equals(d))
                aux.add(j);
        }

        this.clear();
        this.addAll(aux);

        filterInitDate = true;
        numFilter++;
    }

    public void filterEndDate(LocalDate f) {
        if (f == null || f == null) {
            if (filterEndDate) {
                filterEndDate = false;
                numFilter--;
            }
            return;
        }

        Date d = java.sql.Date.valueOf(f);

        List<Job> aux = new ArrayList<Job>();

        for (Job j : this) {
            if (((com.bluelab.job.Job) j).getDate().before(d) || ((com.bluelab.job.Job) j).getDate().equals(d))
                aux.add(j);
        }

        this.clear();
        this.addAll(aux);

        filterEndDate = true;
        numFilter++;
    }

    public int getNumFilter() {
        return numFilter;
    }

}
