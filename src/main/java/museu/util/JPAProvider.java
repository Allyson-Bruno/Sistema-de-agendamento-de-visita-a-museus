package museu.util;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@ApplicationScoped
public class JPAProvider {

    private EntityManagerFactory factory;

    @PostConstruct
    public void inicializar() {
        factory = JPAUtil.criarFabrica();
    }

    public EntityManager getEntityManager() {
        return factory.createEntityManager();
    }

    @PreDestroy
    public void encerrar() {
        if (factory != null && factory.isOpen()) {
            factory.close();
        }
    }
    
}//class JPAProvider