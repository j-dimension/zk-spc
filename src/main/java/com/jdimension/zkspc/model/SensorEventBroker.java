/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jdimension.zkspc.model;

import java.util.ArrayList;

/**
 *
 * @author jens
 */
public class SensorEventBroker {
    
    private static SensorEventBroker instance=null;
    private ArrayList<SensorEventConsumer> consumers=new ArrayList<SensorEventConsumer>();
    
    private SensorEventBroker() {
        
    }
    
    public static synchronized SensorEventBroker getInstance() {
        if(instance==null)
            instance=new SensorEventBroker();
        return instance;
    }
    
    public void emit(SensorEvent e) {
        //System.out.println("Sensor " + e.getSensor().getName() + " emitted value " + e.getValue());
        for(SensorEventConsumer c: consumers)
            c.onSensorEvent(e);
    }
    
    public void outOfControl(SensorEvent e) {
        //System.out.println("Sensor " + e.getSensor().getName() + " emitted value " + e.getValue());
        for(SensorEventConsumer c: consumers)
            c.onOutOfControl(e);
    }
    
    public void subscribe(SensorEventConsumer consumer) {
        if(!consumers.contains(consumer))
            consumers.add(consumer);
    }
    
}
