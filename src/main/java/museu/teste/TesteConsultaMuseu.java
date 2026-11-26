package museu.teste;

import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import museu.dao.DaoGenerico;
import museu.dao.HorarioMuseuDAO;
import museu.modelo.HorarioMuseu;
import museu.modelo.Museu;
import museu.util.JPAUtil;

public class TesteConsultaMuseu {

    public static void main(String[] args) {
    	
    	EntityManagerFactory factory = JPAUtil.criarFabrica();
    	
        try {
        	
            EntityManager manager = factory.createEntityManager();

            try {
                
            	DaoGenerico<Museu> dao = new DaoGenerico<>(manager, Museu.class);
            	List<Museu> museus = dao.listarTodos();

            	museus.sort(Comparator.comparing(Museu::getNome));

            	for (Museu museu : museus) {
            	    System.out.println(museu.getNome() + " - "
            	            + museu.getCapacidadePorHorario() + " vagas por horário");
            	}
            	
            	HorarioMuseuDAO daoHorarios = new HorarioMuseuDAO(manager);

            	for (Museu museu : museus) {

            	    System.out.println("\n" + museu.getNome());

            	    List<HorarioMuseu> horarios =
            	            daoHorarios.listarPorMuseu(museu.getId());

            	    System.out.println("Total de horarios: " + horarios.size());

            	    for (HorarioMuseu horario : horarios) {
            	        System.out.println(horario.getHoraInicio());
            	    }
            	}

            } finally {
                manager.close();
            }
        } finally {
            factory.close();
        }
    }
    
}//class TesteConsultaMuseu.