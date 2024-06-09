package ar.edu.utn.dds.k3003.persistance;

import ar.edu.utn.dds.k3003.model.Heladera;
import ar.edu.utn.dds.k3003.model.Temperatura;
import org.hibernate.boot.model.naming.IllegalIdentifierException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.NoSuchElementException;

public class HeladerasRepositoryImpl implements HeladerasRepository{

    private static EntityManagerFactory emf;

    public HeladerasRepositoryImpl (EntityManagerFactory entityManagerFactory){
        emf = entityManagerFactory;
    }

    @Override
    public Heladera save(Heladera heladera) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            if (heladera.getId() == null) {
                em.persist(heladera);
            } else {
                if (em.find(Heladera.class, heladera.getId()) == null) {
                    throw new IllegalArgumentException("No se puede almacenar una heladera con id asignado por el usuario ");
                } else {
                    throw new IllegalIdentifierException("Ya existe la heladera con el id: " + heladera.getId());
                }
            }
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
        return heladera;
    }

    @Override
    public Heladera getHeladeraById(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            Heladera heladera = em.find(Heladera.class, id);
            if (heladera == null) {
                throw new NoSuchElementException("No se encontró una heladera con ID: " + id);
            }
            return heladera;
        } finally {
            em.close();
        }
    }

    @Override
    public void modifyHeladera(Heladera heladera) {
        if (heladera.getId() == null) {
            throw new IllegalArgumentException("La heladera debe tener un ID para ser modificada.");
        }

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            Heladera existingHeladera = em.find(Heladera.class, heladera.getId());
            if (existingHeladera == null) {
                throw new NoSuchElementException("No se encontró una heladera con ID: " + heladera.getId());
            }

            em.merge(heladera);

            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void clean() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.createQuery("DELETE FROM Temperatura").executeUpdate();
            em.createQuery("DELETE FROM Heladera").executeUpdate();
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void agregarTemperatura(Temperatura temperatura) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            Heladera heladera = em.find(Heladera.class, temperatura.getHeladeraid());
            if (heladera == null) {
                throw new NoSuchElementException("No se encontró la heladera con ID: " + temperatura.getHeladeraid());
            }
            temperatura.setHeladera(heladera);
            heladera.getTemperaturas().add(temperatura);
            heladera.setUltimaTemperaturaRegistrada(temperatura.getTemperatura());
            em.persist(temperatura);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

}