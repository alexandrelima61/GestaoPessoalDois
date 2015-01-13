/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gerenciapessoal.controller;

import br.com.gerenciapessoal.model.Lancamento;
import br.com.gerenciapessoal.repository.Lancamentos;
import br.com.gerenciapessoal.repository.filter.LancamentoFilter;
import br.com.gerenciapessoal.service.CadastroContaService;
import br.com.gerenciapessoal.util.jsf.FacesUtil;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author jalima
 */
@Named
@ViewScoped
public class HistLancamentoBean implements Serializable {

    @Inject
    private Lancamentos lancamentos;
    @Inject
    private CadastroContaService cadastroContaService;

    private LancamentoFilter filterLanc;

    private BigDecimal saldoConta = BigDecimal.ZERO;

    private Lancamento lancSelecionado;
    private List<Lancamento> lancamentosFiltrados;

    public HistLancamentoBean() {
        filterLanc = new LancamentoFilter();
        lancamentosFiltrados = new ArrayList<>();
    }

    public void consultaHist() {
        try {
            lancamentosFiltrados = lancamentos.lancamentoFiltrados(filterLanc);
            calculaSaldoEmconta();
        } catch (NoSuchElementException e) {
            saldoConta = BigDecimal.ZERO;
            FacesUtil.addErrorMessage("Para esta conta selecionada, não há movimentação!");
        }
    }

    public void extornaLanca() {
        try {
            lancSelecionado.atualizarSaldoConta(true);
            lancSelecionado.setConta(cadastroContaService.salvarConta(lancSelecionado.getConta()));
            calculaSaldoEmconta();

            lancamentos.remover(lancSelecionado);
            lancamentosFiltrados.remove(lancSelecionado);

            FacesUtil.addInfoMessage("Lançamento extornado com sucesso!");
        } catch (NoSuchElementException e) {

        }
    }

    public LancamentoFilter getFilterLanc() {
        return filterLanc;
    }

    public List<Lancamento> getLancamentosFiltrados() {
        return lancamentosFiltrados;
    }

    public void setLancamentosFiltrados(List<Lancamento> lancamentosFiltrados) {
        this.lancamentosFiltrados = lancamentosFiltrados;
    }

    public Lancamento getLancSelecionado() {
        return lancSelecionado;
    }

    public void setLancSelecionado(Lancamento lancSelecionado) {
        this.lancSelecionado = lancSelecionado;
    }

    public BigDecimal getSaldoConta() {
        return saldoConta;
    }

    public void setSaldoConta(BigDecimal saldoConta) {
        this.saldoConta = saldoConta;
    }

    private void calculaSaldoEmconta() {
        saldoConta = lancamentosFiltrados.iterator().next().getConta().getSaldo();
    }

}
