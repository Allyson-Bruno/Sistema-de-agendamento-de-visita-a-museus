package museu.bean;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import museu.dao.DaoGenerico;
import museu.modelo.Museu;
import museu.util.JPAProvider;

@Named
@RequestScoped
public class TesteCdiBean {

    @Inject
    private JPAProvider provider;

    public String getMensagem() {

        EntityManager manager = provider.getEntityManager();

        try {

            DaoGenerico<Museu> dao = new DaoGenerico<>(manager, Museu.class);

            int quantidade = dao.listarTodos().size();

            return "Conexão realizada! Museus cadastrados: "
                    + quantidade;

        } finally {
            manager.close();
        }
    }
    
}//class TesteCdiBean.