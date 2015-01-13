
import br.com.gerenciapessoal.model.Lancamento;
import br.com.gerenciapessoal.model.Usuario;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jalima
 */
public class TesteConsulta {

    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("gestaofinPU");
        EntityManager manager = factory.createEntityManager();

        Session session = manager.unwrap(Session.class);
        Map<Date, BigDecimal> valores = valoresTotaisPorData(15, null, null);

        Criteria criteria = session.createCriteria(Lancamento.class);

        criteria.setProjection(Projections.projectionList()
                .add(Projections.sqlGroupProjection("date(data_emissao) as data",
                                "date(data_emissao)",
                                new String[]{"data"}, new Type[]{StandardBasicTypes.DATE})));

        manager.close();

    }

    @SuppressWarnings("UnusedAssignment")
    public static Map<Date, BigDecimal> valoresTotaisPorData(Integer numeroDeDias,
            Lancamento lancamento, Session session) {

        Calendar dataInicial = Calendar.getInstance();
        dataInicial = DateUtils.truncate(dataInicial, Calendar.DAY_OF_MONTH);
        dataInicial.add(Calendar.DAY_OF_MONTH, numeroDeDias * -1);

        Map<Date, BigDecimal> resultado = criarMapaVazio(numeroDeDias, dataInicial);

        return resultado;
    }

    private static Map<Date, BigDecimal> criarMapaVazio(Integer numeroDeDias,
            Calendar dataInicial) {

        dataInicial = (Calendar) dataInicial.clone();

        Map<Date, BigDecimal> mapaInicial = new TreeMap<>();

        for (int i = 0; i < numeroDeDias; i++) {
            mapaInicial.put(dataInicial.getTime(), BigDecimal.ZERO);
            dataInicial.add(Calendar.DAY_OF_MONTH, 1);
        }

        return mapaInicial;
    }
}
