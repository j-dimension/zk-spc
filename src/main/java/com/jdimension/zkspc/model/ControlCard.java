/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jdimension.zkspc.model;

/**
 *
 * @author jens
 */
public class ControlCard {
    
    private double upperLimit=Double.MAX_VALUE;
    private double lowerLimit=Double.MIN_VALUE;
    
    public ControlCard() {
        
    }
    
    public boolean isOutOfControl(double value) {
        if(value>upperLimit || value < lowerLimit)
            return true;
        return false;
    }

    /**
     * @return the upperLimit
     */
    public double getUpperLimit() {
        return upperLimit;
    }

    /**
     * @param upperLimit the upperLimit to set
     */
    public void setUpperLimit(double upperLimit) {
        this.upperLimit = upperLimit;
    }

    /**
     * @return the lowerLimit
     */
    public double getLowerLimit() {
        return lowerLimit;
    }

    /**
     * @param lowerLimit the lowerLimit to set
     */
    public void setLowerLimit(double lowerLimit) {
        this.lowerLimit = lowerLimit;
    }
    
    
    
}
