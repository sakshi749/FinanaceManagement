package com.mycompany.financemanagementsystem;

import java.io.Serializable;
import java.util.Date;

public class Expenses implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long expensesId; 
    private Long categoryId;  
    private Float amount;
    private Date date;
    private String remark;

    public Expenses() {
        this.expensesId = System.currentTimeMillis(); 
    }

    public Expenses(Long categoryId, Float amount, Date date, String remark) {
        this.expensesId = System.currentTimeMillis(); 
        this.categoryId = categoryId;
        this.amount = amount;
        this.date = date;
        this.remark = remark;
    }

    public Long getExpensesId() {
        return expensesId;
    }

    public void setExpensesId(Long expensesId) {
        this.expensesId = expensesId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
