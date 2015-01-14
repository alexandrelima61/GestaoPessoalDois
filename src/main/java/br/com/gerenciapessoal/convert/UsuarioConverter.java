/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gerenciapessoal.convert;


import br.com.gerenciapessoal.model.Usuario;
import br.com.gerenciapessoal.repository.Usuarios;
import br.com.gerenciapessoal.util.cdi.CDIServiceLocator;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author FamilaLimaFeitoza
 */
@FacesConverter(forClass = Usuario.class)
public class UsuarioConverter implements Converter {

    //@Inject
    private final Usuarios usuarios;

    public UsuarioConverter() {
        usuarios = CDIServiceLocator.getBean(Usuarios.class);
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Usuario retorno = null;

        if (value != null) {
            Long id = new Long(value);
            retorno = usuarios.porId(id);
        }

        return retorno;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null) {
            Usuario usuario = (Usuario) value;
            return usuario.getId() == null ? null : usuario.getId().toString();
        }

        return "";
    }

}
