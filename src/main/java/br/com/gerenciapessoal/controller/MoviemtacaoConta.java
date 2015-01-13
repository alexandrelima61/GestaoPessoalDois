/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gerenciapessoal.controller;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author jalima
 */
@Named
@RequestScoped
public class MoviemtacaoConta {

    private CartesianChartModel model;

    public void preRender() {
        this.model = new CartesianChartModel();
        
        addiconarSerie("Todos os pedidos");
        addiconarSerie("Meus Pedidos");
    }

    public CartesianChartModel getModel() {
        return model;
    }

    private void addiconarSerie(String rotulo) {
        ChartSeries series = new ChartSeries(rotulo);

        series.set("1", Math.random() * 1000);
        series.set("2", Math.random() * 1000);
        series.set("3", Math.random() * 1000);
        series.set("4", Math.random() * 1000);
        series.set("5", Math.random() * 1000);

        this.model.addSeries(series);
    }

}
