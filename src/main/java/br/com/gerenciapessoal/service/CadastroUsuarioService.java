/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gerenciapessoal.service;

import br.com.gerenciapessoal.model.Usuario;
import br.com.gerenciapessoal.repository.Usuarios;
import br.com.gerenciapessoal.util.jpa.Transactional;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author jalima
 */
public class CadastroUsuarioService implements Serializable {

    @Inject
    private Usuarios usuarios;

    @Transactional
    public Usuario salvaUsuario(Usuario usuario) {
        return usuarios.gardarUsuario(usuario);
    }

    @SuppressWarnings("null")
    public static String md5(String input) {
        String md5 = null;
        if (!StringUtils.isNotBlank(input)) {
            return null;
        }
        try {
            //Create MessageDigest object for MD5           
            MessageDigest digest = MessageDigest.getInstance("MD5");
            //Update input string in message digest           
            digest.update(input.getBytes(), 0, input.length());
            //Converts message digest value in base 16 (hex)            
            md5 = new BigInteger(1, digest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
        }
        return md5.trim();
    }

}
