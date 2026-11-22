package museu.teste;

import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import museu.dao.DaoGenerico;
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
                
            } finally {
                manager.close();
            }
        } finally {
            factory.close();
        }
    }
    
}//class TesteConsultaMuseu.