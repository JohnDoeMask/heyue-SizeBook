package com.example.heyue_sizebook;

/**
 * Created by heyuehuang on 2017-01-28.
 */
import java.util.ArrayList;
import java.util.Calendar;

public class Record {
    private String personName;
    private Calendar date;
    private double neckSize;
    private double bustSize;
    private double chestSize;
    private double waistSize;
    private double hipSize;
    private double inseamSize;
    private String comment;

    public Record() {

    }

    public String getPersonName() {
        return personName;
    }

    public Calendar getDate() {
        if (date == null) return null;
        return date;
    }

    public double getNeckSize() {
        return neckSize;
    }

    public double getBustSize() {
        return bustSize;
    }

    public double getChestSize() {
        return chestSize;
    }

    public double getWaistSize() {
        return waistSize;
    }

    public double getHipSize() {
        return hipSize;
    }

    public double getInseamSize() {
        return inseamSize;
    }

    public String getComment() {
        if (comment == null) return null;
        return comment;
    }

    public void setPersonName(String personName) throws InvalidRecordException{
        if (personName.isEmpty()) {
            throw new InvalidRecordException();
        }
        this.personName = personName;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public void setNeckSize(double neckSize) {
        this.neckSize = neckSize;
    }

    public void setBustSize(double bustSize) {
        this.bustSize = bustSize;
    }

    public void setChestSize(double chestSize) {
        this.chestSize = chestSize;
    }

    public void setWaistSize(double waistSize) {
        this.waistSize = waistSize;
    }

    public void setHipSize(double hipSize) {
        this.hipSize = hipSize;
    }

    public void setInseamSize(double inseamSize) {
        this.inseamSize = inseamSize;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString(){
        return this.personName;
    }
}