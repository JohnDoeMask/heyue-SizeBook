package com.example.heyue_sizebook;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * The class of Record which provides all the attributes.
 *
 * @author Heyue Huang
 * @version 1.4.2 17-01-28
 * @since 1.0
 */
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

    /**
     * Instantiates a new Record.default constructor
     */
    public Record() {

    }

    /**
     * Gets person name.
     *
     * @return the person name
     */
    public String getPersonName() {
        if (personName == null) return null;
        if (personName.isEmpty()) return null;
        return personName;
    }

    /**
     * Gets date.
     *
     * @return the date
     */
    public Calendar getDate() {
        if (date == null) return null;
        return date;
    }

    /**
     * Gets neck size.
     *
     * @return the neck size
     */
    public double getNeckSize() {
        return neckSize;
    }

    /**
     * Gets bust size.
     *
     * @return the bust size
     */
    public double getBustSize() {
        return bustSize;
    }

    /**
     * Gets chest size.
     *
     * @return the chest size
     */
    public double getChestSize() {
        return chestSize;
    }

    /**
     * Gets waist size.
     *
     * @return the waist size
     */
    public double getWaistSize() {
        return waistSize;
    }

    /**
     * Gets hip size.
     *
     * @return the hip size
     */
    public double getHipSize() {
        return hipSize;
    }

    /**
     * Gets inseam size.
     *
     * @return the inseam size
     */
    public double getInseamSize() {
        return inseamSize;
    }

    /**
     * Gets comment.
     *
     * @return the comment
     */
    public String getComment() {
        if (comment == null) return null;
        return comment;
    }

    /**
     * Sets person name.
     *
     * @param personName the person name
     * @throws InvalidRecordException the invalid record exception
     */
    public void setPersonName(String personName) throws InvalidRecordException{
        if (personName.isEmpty()) {
            throw new InvalidRecordException();
        }

        this.personName = personName;
    }

    /**
     * Sets date.
     *
     * @param date the date
     */
    public void setDate(Calendar date) {
        this.date = date;
    }

    /**
     * Sets neck size.
     *
     * @param neckSize the neck size
     */
    public void setNeckSize(double neckSize) {
        this.neckSize = neckSize;
    }

    /**
     * Sets bust size.
     *
     * @param bustSize the bust size
     */
    public void setBustSize(double bustSize) {
        this.bustSize = bustSize;
    }

    /**
     * Sets chest size.
     *
     * @param chestSize the chest size
     */
    public void setChestSize(double chestSize) {
        this.chestSize = chestSize;
    }

    /**
     * Sets waist size.
     *
     * @param waistSize the waist size
     */
    public void setWaistSize(double waistSize) {
        this.waistSize = waistSize;
    }

    /**
     * Sets hip size.
     *
     * @param hipSize the hip size
     */
    public void setHipSize(double hipSize) {
        this.hipSize = hipSize;
    }

    /**
     * Sets inseam size.
     *
     * @param inseamSize the inseam size
     */
    public void setInseamSize(double inseamSize) {
        this.inseamSize = inseamSize;
    }

    /**
     * Sets comment.
     *
     * @param comment the comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Sow part of the content of a record on the initial interface
     *
     * @return person name, bust size, chest size, waist size, inseam size
     */
    @Override
    public String toString(){
        return "Name: " + this.personName + "\n" + "Bust: " + this.bustSize
                + "\n" + "Chest: " + this.chestSize + "\n" + "Waist: " + this.waistSize
                + "\n" + "Inseam: " + this.inseamSize;
    }
}