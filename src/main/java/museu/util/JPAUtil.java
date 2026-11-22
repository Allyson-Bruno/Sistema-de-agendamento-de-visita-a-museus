package museu.util;

import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class JPAUtil {

    private JPAUtil() {
    	
    }

    public static EntityManagerFactory criarFabrica() {
        String usuario = System.getenv("MUSEU_DB_USER");
        String senha = System.getenv("MUSEU_DB_PASSWORD");

        if (usuario == null || usuario.isBlank() || senha == null || senha.isBlank()) {
            throw new IllegalStateException(
                    "Configure MUSEU_DB_USER e MUSEU_DB_PASSWORD antes de iniciar o projeto.");
        }

        return Persistence.createEntityManagerFactory(
                "visitaMuseuPU",
                Map.of(
                        "javax.persistence.jdbc.user", usuario,
                        "javax.persistence.jdbc.password", senha));
    }
    
}//class JPAUtil.