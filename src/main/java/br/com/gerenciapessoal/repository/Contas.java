/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gerenciapessoal.repository;

import br.com.gerenciapessoal.model.Conta;
import br.com.gerenciapessoal.model.Usuario;
import br.com.gerenciapessoal.repository.filter.ContaFilter;
import br.com.gerenciapessoal.security.Seguranca;
import br.com.gerenciapessoal.util.cdi.CDIServiceLocator;
import br.com.gerenciapessoal.util.jpa.Transactional;
import br.com.gerenciapessoal.util.service.NegocioException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author jalima
 */
public class Contas implements Serializable {

    @Inject
    private EntityManager manager;

    public Seguranca s;

    public Contas() {
        s = CDIServiceLocator.getBean(Seguranca.class);
    }

    public Conta porId(Long id) {
        return manager.find(Conta.class, id);
    }

    public Conta gardarConta(Conta conta) {
        return manager.merge(conta);
    }

    @SuppressWarnings("JPQLValidation")
    public List<Conta> listaConta() {
        try {
            //Usuario u = manager.find(Usuario.class, s.getIdUsuario());

            return manager.createQuery("from Conta where usuario_id = :usuario", Conta.class)
                    .setParameter("usuario", s.getIdUsuario())
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    public List<Conta> pesquisarConta(ContaFilter contaFilter) {
        Session session = this.manager.unwrap(Session.class);

        Criteria criteria = session.createCriteria(Conta.class)
                .createAlias("banco", "b")
                .createAlias("usuario", "u");

        //Usuario u = manager.find(Usuario.class, s.getIdUsuario());

        criteria.add(Restrictions.eq("u.id", s.getIdUsuario()));

        if (contaFilter.getBanco() != null) {
            criteria.add(Restrictions.eq("b.id", contaFilter.getBanco().getId()));
        }

        if (contaFilter.getAgencia() != null) {
            criteria.add(Restrictions.eq("agencia", contaFilter.getAgencia()));
        }

        if (contaFilter.getDvAgencia() != null) {
            criteria.add(Restrictions.eq("dvAgencia", contaFilter.getDvAgencia()));
        }

        if (contaFilter.getConta() != null) {
            criteria.add(Restrictions.eq("conta", contaFilter.getConta()));
        }

        if (contaFilter.getDvConta() != null) {
            criteria.add(Restrictions.eq("dvConta", contaFilter.getDvConta()));
        }

        if (contaFilter.getTipoConta() != null) {
            criteria.add(Restrictions.eq("tipoConta", contaFilter.getTipoConta()));
        }

        //return criteria.addOrder(Order.asc("id")).list();
        return criteria.addOrder(Order.desc("id")).list();

    }

    @Transactional
    public void remover(Conta conta) {
        try {
            conta = porId(conta.getId());
            manager.remove(conta);
            manager.flush();

        } catch (PersistenceException e) {
            throw new NegocioException("Esta Conta não pode ser excluido!");
        }
    }
}
