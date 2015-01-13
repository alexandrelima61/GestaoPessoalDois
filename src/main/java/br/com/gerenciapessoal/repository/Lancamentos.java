/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gerenciapessoal.repository;

import br.com.gerenciapessoal.model.Conta;
import br.com.gerenciapessoal.model.Lancamento;
import br.com.gerenciapessoal.model.vo.DataValor;
import br.com.gerenciapessoal.repository.filter.LancamentoFilter;
import br.com.gerenciapessoal.util.jpa.Transactional;
import br.com.gerenciapessoal.util.service.NegocioException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import org.apache.commons.lang.time.DateUtils;
import org.eclipse.jdt.internal.core.util.Messages;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

/**
 *
 * @author jalima
 */
public class Lancamentos implements Serializable {

    @Inject
    private EntityManager manager;

    public Lancamento gardar(Lancamento lancamento) {
        return manager.merge(lancamento);
    }

    public List<Lancamento> lancamentoFiltrados(LancamentoFilter filterLanc) {

        Session session = manager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(Lancamento.class)
                .createAlias("conta", "c");

        if (filterLanc.getConta() != null) {
            criteria.add(Restrictions.eq("c.id", filterLanc.getConta().getId()));

            if (filterLanc.getEmissaode() != null) {
                criteria.add(Restrictions.ge("dataEmissao", filterLanc.getEmissaode()));
            }

            if (filterLanc.getEmissaoate() != null) {
                criteria.add(Restrictions.le("dataEmissao", filterLanc.getEmissaoate()));
            }

        } else {
            throw new NegocioException("Você deve selecionar uma conta\n"
                    + "antes de proceguir!");
        }

        return criteria.addOrder(Order.asc("dataEmissao")).list();

    }

    @Transactional
    public void remover(Lancamento lancamento) {
        try {
            lancamento = porId(lancamento.getId());

            manager.remove(lancamento);
            manager.flush();
        } catch (PersistenceException e) {
            throw new NegocioException("Este lançamento não pode ser extornado");
        }
    }

    public Lancamento porId(Long id) {
        return manager.find(Lancamento.class, id);
    }

    @SuppressWarnings("UnusedAssignment")
    public Map<Date, BigDecimal> valoresTotaisPorData(Integer numeroDeDias, Conta conta) {
        Session session = manager.unwrap(Session.class);

        Calendar dataInicial = Calendar.getInstance();
        dataInicial = DateUtils.truncate(dataInicial, Calendar.DAY_OF_MONTH);
        dataInicial.add(Calendar.DAY_OF_MONTH, numeroDeDias * -1);

        Map<Date, BigDecimal> resultado = criarMapaVazio(numeroDeDias, dataInicial);

        Criteria criteria = session.createCriteria(Lancamento.class)
                .createAlias("conta", "c");

        criteria.setProjection(Projections.projectionList()
                .add(Projections.sqlGroupProjection("date(data_emissao) as data",
                                "date(data_emissao)",
                                new String[]{"data"},
                                new Type[]{StandardBasicTypes.DATE}))
                .add(Projections.sum("valorLanca").as("valor")))
                .add(Restrictions.ge("dataEmissao", dataInicial.getTime()));

        if (conta != null) {
            criteria.add(Restrictions.eq("c.id", conta.getId()));
        }
        List<DataValor> valoresPorData = criteria
                .setResultTransformer(Transformers.aliasToBean(DataValor.class)).list();

        for (DataValor dataValor : valoresPorData) {
            resultado.put(dataValor.getData(), dataValor.getValor());
        }

        return resultado;
    }

    private static Map<Date, BigDecimal> criarMapaVazio(Integer numeroDeDias,
            Calendar dataInicial) {

        dataInicial = (Calendar) dataInicial.clone();

        Map<Date, BigDecimal> mapaInicial = new TreeMap<>();

        for (int i = 0; i < numeroDeDias; i++) {
            mapaInicial.put(dataInicial.getTime(), BigDecimal.ZERO);
            dataInicial.add(Calendar.DAY_OF_MONTH, 1);
        }

        return mapaInicial;
    }
}
