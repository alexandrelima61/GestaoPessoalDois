/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gerenciapessoal.repository;

import br.com.gerenciapessoal.model.Lancamento;
import br.com.gerenciapessoal.repository.filter.LancamentoFilter;
import br.com.gerenciapessoal.util.jpa.Transactional;
import br.com.gerenciapessoal.util.service.NegocioException;
import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

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
}
