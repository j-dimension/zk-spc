/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jdimension.zkspc.model;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author jens
 */
public class Sensor {
    
    private String name=null;
    private SensorEventBroker broker=null;
    private long eventInterval=1000;
    private ControlCard controlCard=null;
    
    public Sensor(SensorEventBroker broker, String name, long eventInterval) {
        this.broker=broker;
        this.name=name;
        this.eventInterval=eventInterval;
        EmitterTask task=new EmitterTask(this);
        new Timer().schedule(task, 500, eventInterval);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the controlCard
     */
    public ControlCard getControlCard() {
        return controlCard;
    }

    /**
     * @param controlCard the controlCard to set
     */
    public void setControlCard(ControlCard controlCard) {
        this.controlCard = controlCard;
    }
    
    
    
    private class EmitterTask extends TimerTask {
        
        private Sensor sensor=null;
        
        public EmitterTask(Sensor s) {
            this.sensor=s;
        }

        @Override
        public void run() {
            double value=18d+Math.random()*14;
            SensorEvent e=new SensorEvent(sensor, value);
            broker.emit(e);
            if(controlCard!=null) {
                if(controlCard.isOutOfControl(value)) {
                    broker.outOfControl(e);
                }
            }
        }
        
        
        
    }
    
}
