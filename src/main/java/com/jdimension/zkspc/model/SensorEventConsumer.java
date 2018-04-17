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
public interface SensorEventConsumer {
    
    public void onSensorEvent(SensorEvent e);
    
    public void onOutOfControl(SensorEvent e);
    
}
