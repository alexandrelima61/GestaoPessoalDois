/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gerenciapessoal.controller;

import br.com.gerenciapessoal.model.Grupo;
import br.com.gerenciapessoal.model.Usuario;
import br.com.gerenciapessoal.repository.Grupos;
import br.com.gerenciapessoal.repository.Usuarios;
import br.com.gerenciapessoal.service.CadastroUsuarioService;
import br.com.gerenciapessoal.util.jsf.FacesUtil;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author FamilaLimaFeitoza
 */
@Named
@ViewScoped
public class CadastroUsuarioBean implements Serializable {

    private Usuario usuario;

    @Inject
    private Usuarios usuarios;

    @Inject
    private CadastroUsuarioService cadastroUsuarioService;

    private List<Grupo> gruposDisponiveis;

    private String confirmeSenha;
    private boolean hblBtnSalvar;

    @Inject
    private Grupos grupos;

    private Grupo novoGrupo;

    public CadastroUsuarioBean() {
        limpar();
    }

    public void validaSenha() {
        if (((this.usuario.getSenha() != null) && (confirmeSenha != null))) {
            if (!this.usuario.getSenha().equals(confirmeSenha)) {
                hblBtnSalvar = true;
                FacesUtil.addErrorMessage("As Senhas não conferem, favor, verificar para proseguir!");
            } else {
                hblBtnSalvar = false;
            }
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getConfirmeSenha() {
        return confirmeSenha;
    }

    public void setConfirmeSenha(String confirmeSenha) {
        this.confirmeSenha = confirmeSenha;
    }

    public boolean isHblBtnSalvar() {
        return hblBtnSalvar;
    }

    public void setHblBtnSalvar(boolean hblBtnSalvar) {
        this.hblBtnSalvar = hblBtnSalvar;
    }

    private void limpar() {
        setUsuario(new Usuario());
        hblBtnSalvar = false;
    }

    /**
     * @return the novoGrupo
     */
    public Grupo getNovoGrupo() {
        return novoGrupo;
    }

    /**
     * @param novoGrupo the novoGrupo to set
     */
    public void setNovoGrupo(Grupo novoGrupo) {
        this.novoGrupo = novoGrupo;
    }

    public List<Grupo> getGruposDisponiveis() {
        return gruposDisponiveis;
    }

    public void setGruposDisponiveis(List<Grupo> gruposDisponiveis) {
        this.gruposDisponiveis = gruposDisponiveis;
    }

    public void salvarUsuario() {
        this.usuario.setSenha(CadastroUsuarioService.md5(this.usuario.getSenha()));
        this.usuario = cadastroUsuarioService.salvaUsuario(usuario);
        limpar();
        FacesUtil.addInfoMessage("Usuário cadastrado com sucesso!");
    }

    public void adicionarGrupo() {
        this.usuario.getGrupos().add(novoGrupo);
    }

    public boolean isEditando() {
        return this.usuario.getId() != null;
    }

    public void inicializar() {
        if (FacesUtil.isNotPostback()) {
            gruposDisponiveis = grupos.todos();
        }
    }

}
