package museu.dao;

import java.util.List;

import javax.persistence.EntityManager;

import museu.modelo.HorarioMuseu;

public class HorarioMuseuDAO extends DaoGenerico<HorarioMuseu> {

    private final EntityManager manager;

    public HorarioMuseuDAO(EntityManager manager) {
        super(manager, HorarioMuseu.class);
        this.manager = manager;
    }

    public List<HorarioMuseu> listarPorMuseu(Long museuId) {

        return manager.createQuery(
                "SELECT h FROM HorarioMuseu h "
                + "WHERE h.museu.id = :museuId "
                + "ORDER BY h.horaInicio",
                HorarioMuseu.class)
                .setParameter("museuId", museuId)
                .getResultList();
    }
    
}//class HorarioMuseuDAO.