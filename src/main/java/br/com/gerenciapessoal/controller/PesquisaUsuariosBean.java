/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gerenciapessoal.controller;


import br.com.gerenciapessoal.model.Usuario;
import br.com.gerenciapessoal.repository.Usuarios;
import br.com.gerenciapessoal.repository.filter.UsuarioFilter;
import br.com.gerenciapessoal.util.jsf.FacesUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ViewScoped
public class PesquisaUsuariosBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Usuarios usuarios;

    private UsuarioFilter filtro;
    private List<Usuario> usuariosFiltrados;

    private Usuario usuarioSelecionado;

    public PesquisaUsuariosBean() {
        filtro = new UsuarioFilter();
        usuariosFiltrados = new ArrayList<>();
    }

    public void excluir() {
        usuarios.remover(usuarioSelecionado);
        usuariosFiltrados.remove(usuarioSelecionado);

        FacesUtil.addInfoMessage("Usuário " + usuarioSelecionado.getNome()
                + " excluído com sucesso.");
    }

    public void pesquisar() {
        usuariosFiltrados = usuarios.filtrados(filtro);
    }

    public List<Usuario> getUsuariosFiltrados() {
        return usuariosFiltrados;
    }

    public UsuarioFilter getFiltro() {
        return filtro;
    }

    public Usuario getUsuarioSelecionado() {
        return usuarioSelecionado;
    }

    public void setUsuarioSelecionado(Usuario usuarioSelecionado) {
        this.usuarioSelecionado = usuarioSelecionado;
    }

}
