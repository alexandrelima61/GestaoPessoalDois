/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gerenciapessoal.repository;

import br.com.gerenciapessoal.model.Usuario;
import br.com.gerenciapessoal.repository.filter.UsuarioFilter;
import br.com.gerenciapessoal.security.Seguranca;
import br.com.gerenciapessoal.util.cdi.CDIServiceLocator;
import br.com.gerenciapessoal.util.jpa.Transactional;
import br.com.gerenciapessoal.util.service.NegocioException;
import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author jalima
 */
public class Usuarios implements Serializable {

    @Inject
    private EntityManager manager;

    private final Seguranca s;

    public Usuarios() {
        s = CDIServiceLocator.getBean(Seguranca.class);
    }

    public Usuario gardarUsuario(Usuario usuario) {
        return manager.merge(usuario);
    }

    public Usuario usuario() {
        return manager.find(Usuario.class, s.getIdUsuario());
    }

    @SuppressWarnings("JPQLValidation")
    public Usuario porEmail(String email) {
        Usuario usuario = null;
        try {
            usuario = this.manager.createQuery("from Usuario where lower(email) = :email", Usuario.class)
                    .setParameter("email", email.toLowerCase()).getSingleResult();

        } catch (NoResultException e) {
            //nenhum usuário encontradom com e e-mail informado.
            System.out.print("nenhum usuário encontradom com e e-mail informado.");
        }
        return usuario;
    }

    public Usuario porId(Long id) {
        return this.manager.find(Usuario.class, id);
    }

    @Transactional
    public void remover(Usuario usuario) {
        try {
            usuario = porId(usuario.getId());
            //manager.remove(usuario().getGrupos());
            
            manager.remove(usuario);
            manager.flush();
        } catch (PersistenceException e) {
            throw new NegocioException("Usuário não pode ser excluído.");
        }
    }

    @SuppressWarnings("unchecked")
    public List<Usuario> filtrados(UsuarioFilter filtro) {
        Session session = manager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(Usuario.class);

        if (StringUtils.isNotBlank(filtro.getNome())) {
            criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
        }

        if (StringUtils.isNotBlank(filtro.getEmail())) {
            criteria.add(Restrictions.ilike("email", filtro.getEmail(), MatchMode.ANYWHERE));
        }

        return criteria.addOrder(Order.asc("nome")).list();
    }
}
