/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gerenciapessoal.controller;

import br.com.gerenciapessoal.model.Conta;
import br.com.gerenciapessoal.repository.Contas;
import br.com.gerenciapessoal.repository.Lancamentos;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
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

    private static DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM");

    @Inject
    private Lancamentos lancamentos;

    @Inject
    private Contas contas;

    private CartesianChartModel model;

    private List<Conta> listConta;

    public void preRender() {

        listConta = contas.listaConta();

        this.model = new CartesianChartModel();

        for (Conta conta : listConta) {
            addiconarSerie("Ag: " + conta.getAgencia().toString() + "/"
                    + conta.getDvAgencia() + "-"
                    + " Cont: " + conta.getConta().toString() + "/"
                    + conta.getDvConta().toString() + "-"
                    + " Banco " + conta.getBanco().getNumBanco() + "-"
                    + conta.getBanco().getNome() + "-"
                    + " Tipo Cont: " + conta.getTipoConta().getDescicao(),
                    conta);
        }

    }

    public CartesianChartModel getModel() {
        return model;
    }

    private void addiconarSerie(String rotulo, Conta c) {
        Map<Date, BigDecimal> valoresPorData = this.lancamentos.valoresTotaisPorData(15, c);

        ChartSeries series = new ChartSeries(rotulo);

        for (Date data : valoresPorData.keySet()) {
            series.set(DATE_FORMAT.format(data), valoresPorData.get(data));
        }

        this.model.addSeries(series);
    }

}
