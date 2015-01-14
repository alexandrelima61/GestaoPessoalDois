/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gerenciapessoal.repository;

import br.com.gerenciapessoal.model.Grupo;
import br.com.gerenciapessoal.model.Usuario;
import br.com.gerenciapessoal.security.Seguranca;
import br.com.gerenciapessoal.util.cdi.CDIServiceLocator;
import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

/**
 *
 * @author jalima
 */
public class Usuarios implements Serializable {

    @Inject
    private EntityManager manager;

    private Seguranca s;

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
}
