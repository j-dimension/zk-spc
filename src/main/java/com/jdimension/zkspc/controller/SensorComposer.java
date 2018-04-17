/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jdimension.zkspc.controller;

import com.jdimension.zkspc.model.SensorEvent;
import com.jdimension.zkspc.model.SensorEventBroker;
import com.jdimension.zkspc.model.SensorEventConsumer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import org.zkoss.chart.Charts;
import org.zkoss.chart.PlotBand;
import org.zkoss.chart.Point;
import org.zkoss.chart.Series;
import org.zkoss.chart.Theme;
import org.zkoss.chart.YAxis;
import org.zkoss.chart.plotOptions.SplinePlotOptions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

public class SensorComposer extends SelectorComposer<Window> implements SensorEventConsumer {

    @Wire
    Charts chart;

    @Wire
    Listbox exceptions;

    private Hashtable<String, ArrayList<SensorEvent>> eventQueue = new Hashtable<String, ArrayList<SensorEvent>>();
    private ArrayList<String> exceptionQueue=new ArrayList<String>();

    @Override
    public void onSensorEvent(SensorEvent e) {

        System.out.println("received " + e.getValue() + " from " + e.getSensor().getName());

        if (!eventQueue.containsKey(e.getSensor().getName())) {
            eventQueue.put(e.getSensor().getName(), new ArrayList<SensorEvent>());
        }

        eventQueue.get(e.getSensor().getName()).add(e);
    }

    @Override
    public void onOutOfControl(SensorEvent e) {
        
        System.out.println("out of control event");
        this.exceptionQueue.add(new Date().toString() + ": " + e.getSensor().getName() + " is out of spec");
            
        

    }

    public void doAfterCompose(Window comp) throws Exception {
        super.doAfterCompose(comp);

        exceptions.clearSelection();
        exceptions.getItems().clear();

        chart.setAnimation(true);
        chart.getXAxis().setType("datetime");
        chart.getXAxis().getLabels().setOverflow("justify");
        chart.setTheme(Theme.GRAY);
        chart.getTooltip().setPointFormat("{point.x:%Y-%m-%d %H:%M:%S}<br>{point.y}");
        chart.getXAxis().setTickPixelInterval(150);

        YAxis yAxis = chart.getYAxis();
        yAxis.setTitle("Sensor Value (°C)");
        yAxis.setMinorGridLineWidth(0);
        yAxis.setGridLineWidth(0);
        yAxis.setAlternateGridColor((String) null);

        final String LABEL_STYLE = "color: '#606060'";

        // lower control limit
        PlotBand plotBand1 = new PlotBand(Integer.MIN_VALUE, 20, "rgba(250, 0, 0, 0.1)");
        plotBand1.getLabel().setText("Too low");
        plotBand1.getLabel().setStyle(LABEL_STYLE);
        yAxis.addPlotBand(plotBand1);

        // in spec
        PlotBand plotBand2 = new PlotBand(20, 30, "rgba(0, 0, 0, 0)");
        plotBand2.getLabel().setText("In Spec");
        plotBand2.getLabel().setStyle(LABEL_STYLE);
        yAxis.addPlotBand(plotBand2);

        // upper control limit
        PlotBand plotBand3 = new PlotBand(30, Integer.MAX_VALUE, "rgba(250, 0, 0, 0.1)");
        plotBand3.getLabel().setText("Too high");
        plotBand3.getLabel().setStyle(LABEL_STYLE);
        yAxis.addPlotBand(plotBand3);

        chart.getTooltip().setValueSuffix(" °C");

        SplinePlotOptions spline = chart.getPlotOptions().getSpline();
        spline.setLineWidth(4);
        spline.getStates().getHover().setLineWidth(5);
        spline.getMarker().setEnabled(false);
        spline.setPointInterval(1000);
        spline.setPointStart(System.currentTimeMillis());

        Series series1 = chart.getSeries();
        series1.setName("Chamber 1 Temperature");
        series1.setData(24.3, 25.1, 24.3, 25.2, 25.4, 24.7, 23.5, 24.1, 25.6, 27.4, 26.9,
                27.1, 27.9, 27.9, 27.5, 36.7, 27.7, 27.7, 27.4, 27.0, 27.1, 25.8, 25.9, 27.4,
                28.2, 28.5, 29.4, 28.1, 10.9, 10.4, 10.9, 12.4, 12.1, 29.5, 27.5, 27.1,
                27.5, 28.1, 26.8, 23.4, 22.1, 21.9, 22.8, 22.9, 21.3, 24.4, 24.2, 23.0, 23.0);

        Series series2 = chart.getSeries(1);
        series2.setName("Chamber 2 Temperature");
        series2.setData(20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.1, 20.0, 20.3,
                20.0, 20.0, 20.4, 20.0, 20.1, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0,
                20.0, 20.6, 21.2, 21.7, 20.7, 22.9, 24.1, 22.6, 23.7, 23.9, 21.7, 22.3, 23.0,
                23.3, 24.8, 25.0, 24.8, 25.0, 23.2, 22.0, 20.9, 20.4, 20.3, 20.5, 20.4);
        chart.getNavigation().setMenuItemStyle("fontSize: '10px'");

        SensorEventBroker broker = SensorEventBroker.getInstance();
        broker.subscribe(this);

    }

    @Listen("onTimer = #timer")
    public void updateData() {

        //System.out.println("timer");
        for (int i = 0; i < chart.getSeriesSize(); i++) {
            //System.out.println("series " + chart.getSeries(i).getName());
            if (eventQueue.containsKey(chart.getSeries(i).getName())) {
                
                ArrayList<SensorEvent> events = eventQueue.get(chart.getSeries(i).getName());
                //System.out.println("events: " + events.size());
                for (SensorEvent e : events) {
                    //System.out.println("new chart point");
                    Point p1 = new Point(e.getValue());
                    chart.getSeries(i).addPoint(p1, true, true, true);
                }
                events.clear();
            }

        }
        
        
        for(String s: this.exceptionQueue) {
        this.exceptions.getItems().add(new Listitem(s));
            
            if(this.exceptions.getItems().size()>5)
                this.exceptions.getItems().remove(0);    
        }
        this.exceptionQueue.clear();
        
        

    }

}
