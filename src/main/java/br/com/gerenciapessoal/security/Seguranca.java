/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gerenciapessoal.security;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 *
 * @author jalima
 */
@Named
@RequestScoped
public class Seguranca {

    public String getNomeUsuario() {
        String nome = null;

        UsuarioSistema usuarioLogado = getNomeUsuarioLogado();

        if (usuarioLogado != null) {
            nome = usuarioLogado.getUsuario().getNome();
        }

        return nome;
    }

    public Long getIdUsuario() {
        Long id = null;

        UsuarioSistema usuarioLogado = getNomeUsuarioLogado();

        if (usuarioLogado != null) {
            id = usuarioLogado.getUsuario().getId();
        }

        return id;
    }

    @Produces
    @UsuarioLogado
    public UsuarioSistema getNomeUsuarioLogado() {
        UsuarioSistema usuario = null;

        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) 
                FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();

        if (auth != null && auth.getPrincipal() != null) {
            usuario = (UsuarioSistema) auth.getPrincipal();
        }

        return usuario;
    }
}
