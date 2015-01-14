/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gerenciapessoal.service;

import br.com.gerenciapessoal.model.Banco;
import br.com.gerenciapessoal.repository.Bancos;
import br.com.gerenciapessoal.util.jpa.Transactional;
import java.io.Serializable;
import javax.inject.Inject;

/**
 *
 * @author jalima
 */
public class CadastroBancoService implements Serializable {

    @Inject
    private Bancos bancos;

    @Transactional
    public Banco salvar(Banco banco) {
        return bancos.gardar(banco);
    }
}
