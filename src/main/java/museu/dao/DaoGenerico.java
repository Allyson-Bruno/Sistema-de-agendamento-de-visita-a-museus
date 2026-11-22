package museu.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class DaoGenerico<T> {

    private final EntityManager manager;
    private final Class<T> tipo;

    public DaoGenerico(EntityManager manager, Class<T> tipo) {
        this.manager = manager;
        this.tipo = tipo;
    }

    public List<T> listarTodos() {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<T> consulta = builder.createQuery(tipo);
        Root<T> entidade = consulta.from(tipo);

        consulta.select(entidade);

        return manager.createQuery(consulta).getResultList();
    }
    
}//class DaoGenerico<T>.