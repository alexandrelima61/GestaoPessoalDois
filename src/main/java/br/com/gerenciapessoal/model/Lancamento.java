/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gerenciapessoal.model;

import br.com.gerenciapessoal.enumeradores.TipoLancamento;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author jalima
 */
@Entity
@Table(name = "lancamentos")
public class Lancamento implements Serializable {

    private Long id;
    private String descricao;
    private Categoria categoria;
    private BigDecimal vlTotal;
    private BigDecimal valorLanca;
    private BigDecimal saldo;
    private Date dataEmissao;
    private Date dataVencimento;
    private boolean baixa;
    private TipoLancamento tipoMov;
    private Conta conta;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotBlank
    @Column(name = "descricao", nullable = false, length = 255)
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @NotNull
    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @NotNull(message = "é obrigatório")
    @Column(name = "valor_lanc", nullable = false, precision = 10, scale = 2)
    public BigDecimal getValorLanca() {
        return valorLanca;
    }

    public void setValorLanca(BigDecimal valorLanca) {
        this.valorLanca = valorLanca;
    }

    @NotNull(message = "é obrigatório")
    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    public BigDecimal getVlTotal() {
        return vlTotal;
    }

    public void setVlTotal(BigDecimal vlTotal) {
        this.vlTotal = vlTotal;
    }

    @Column(name = "saldo", nullable = false, precision = 10, scale = 2)
    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "data_emissao", nullable = false)
    public Date getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(Date dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "data_vencimento", nullable = false)
    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    @Column(nullable = false)
    public boolean isBaixa() {
        return baixa;
    }

    public void setBaixa(boolean baixa) {
        this.baixa = baixa;
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    public TipoLancamento getTipoMov() {
        return tipoMov;
    }

    public void setTipoMov(TipoLancamento tipoMov) {
        this.tipoMov = tipoMov;
    }

    @NotNull
    @ManyToOne
    @JoinColumn(name = "conta_id", nullable = false)
    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Lancamento other = (Lancamento) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    public void valorizaSaldo() {
        BigDecimal vlSaldo = BigDecimal.ZERO;
        if (this.getTipoMov().getTpES().equals("DESPESA")) {
            vlSaldo = vlSaldo.add(this.getVlTotal().subtract(this.getValorLanca()));
        } else {
            vlSaldo = vlSaldo.add(this.getValorLanca().subtract(this.getVlTotal()));
        }
        setSaldo(vlSaldo);
    }

    public void atualizarSaldoConta(boolean lExtorno) {
        BigDecimal saldoConta = this.getValorLanca();

        if (lExtorno) {
            if (this.getTipoMov().getTpES().equals("DESPESA")) {
                //Caso extorno devolvo saldo para a conta
                saldoConta = saldoConta.multiply(new BigDecimal(-1.00));
                this.getConta().setSaldo(saldoConta.add(this.getConta().getSaldo()));
            } else {
                //Caso extorno devolvo saldo para a conta
                saldoConta = saldoConta.multiply(new BigDecimal(-1.00));
                this.getConta().setSaldo(this.getConta().getSaldo().add(saldoConta));

            }
        } else {
            if (this.getTipoMov().getTpES().equals("DESPESA")) {
                //Caso despesa subtraio do saldo da conta
                this.getConta().setSaldo(this.getConta().getSaldo().add(saldoConta));
            } else {
                //Caso receita adciono ao saldo da conta
                this.getConta().setSaldo(saldoConta.add(this.getConta().getSaldo()));
            }
        }
    }

    public String baixaRealizada() {
        if (this.isBaixa()) {
            return "Ok";
        } else {
            return "Pendente";
        }
    }

    public void atualizaTotais() {
        if (this.getTipoMov().getTpES().equals("DESPESA")) {
            setValorLanca(getValorLanca().multiply(new BigDecimal(-1.0)));
            setVlTotal(getVlTotal().multiply(new BigDecimal(-1.0)));
        }
    }
}
