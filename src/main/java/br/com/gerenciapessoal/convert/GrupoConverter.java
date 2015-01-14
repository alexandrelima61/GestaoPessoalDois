/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gerenciapessoal.convert;


import br.com.gerenciapessoal.model.Grupo;
import br.com.gerenciapessoal.repository.Grupos;
import br.com.gerenciapessoal.util.cdi.CDIServiceLocator;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author FamilaLimaFeitoza
 */
@FacesConverter(forClass = Grupo.class)
public class GrupoConverter implements Converter {

    //	@Inject Inject não funciona com conversores
    private final Grupos grupos;

    public GrupoConverter() {
        grupos = CDIServiceLocator.getBean(Grupos.class);
    }

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        Grupo retorno = null;

        if (value != null) {
            String nome = value;
            retorno = grupos.grupoPorNome(nome);
        }

        return retorno;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        if (o != null) {
            Grupo grupo = (Grupo) o;
            return grupo.getNome().toString();
        }
        return "";
    }

}
