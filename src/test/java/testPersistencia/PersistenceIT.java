package testPersistencia;

import ar.edu.utn.dds.k3003.model.Heladera;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersistenceIT {

    static EntityManagerFactory entityManagerFactory ;
    EntityManager entityManager ;

    @BeforeAll
    public static void setUpClass() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("heladerasdb");
    }
    @BeforeEach
    public void setup() throws Exception {
        entityManager = entityManagerFactory.createEntityManager();
    }
    @Test
    public void testConectar() {
        // vacío, para ver que levante el ORM
    }

    @Test
    public void testGuardarHeladera() throws Exception {
        Heladera heladeraAPersistir = new Heladera(null, "persistida", 5);

        entityManager.getTransaction().begin();
        entityManager.persist(heladeraAPersistir);
        entityManager.getTransaction().commit();
        entityManager.close();

        entityManager = entityManagerFactory.createEntityManager();
        Heladera heladeraARecuperar = (Heladera)entityManager.createQuery("from Heladera where nombre = 'persistida'").getSingleResult();

        assertEquals(heladeraAPersistir.getNombre(), heladeraARecuperar.getNombre()); // también puede redefinir el equals
    }

}
