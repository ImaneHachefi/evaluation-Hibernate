package ma.projet.service;

import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ProjetService extends AbstractService<Projet> {

    public ProjetService() { super(Projet.class); }

    private void afficherEntete(Projet p) {
        SimpleDateFormat longue = new SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH);
        System.out.println("Projet : " + p.getId() + "      Nom : " + p.getNom()
                + "     Date début : " + longue.format(p.getDateDebut()));
    }

    /** Tâches planifiées pour un projet (dates prévues). */
    public List<Tache> getTachesPlanifiees(int projetId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "select t from Tache t where t.projet.id = :pid order by t.dateDebut", Tache.class)
                    .setParameter("pid", projetId)
                    .list();
        }
    }

    public void afficherTachesPlanifiees(int projetId) {
        Projet p = getById(projetId);
        if (p == null) {
            System.out.println("Projet introuvable : " + projetId);
            return;
        }
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        afficherEntete(p);
        System.out.println("Liste des tâches planifiées :");
        System.out.printf("%-5s%-16s%-20s%s%n", "Num", "Nom", "Date Début Prévue", "Date Fin Prévue");
        for (Tache t : getTachesPlanifiees(projetId)) {
            System.out.printf("%-5d%-16s%-20s%s%n",
                    t.getId(), t.getNom(), f.format(t.getDateDebut()), f.format(t.getDateFin()));
        }
    }

    /** Tâches réalisées d'un projet avec les dates réelles. */
    public List<EmployeTache> getTachesRealisees(int projetId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "select et from EmployeTache et join fetch et.tache t " +
                    "where t.projet.id = :pid order by et.dateDebutReelle", EmployeTache.class)
                    .setParameter("pid", projetId)
                    .list();
        }
    }

    public void afficherTachesRealisees(int projetId) {
        Projet p = getById(projetId);
        if (p == null) {
            System.out.println("Projet introuvable : " + projetId);
            return;
        }
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        afficherEntete(p);
        System.out.println("Liste des tâches:");
        System.out.printf("%-5s%-16s%-20s%s%n", "Num", "Nom", "Date Début Réelle", "Date Fin Réelle");
        for (EmployeTache et : getTachesRealisees(projetId)) {
            System.out.printf("%-5d%-16s%-20s%s%n",
                    et.getTache().getId(),
                    et.getTache().getNom(),
                    f.format(et.getDateDebutReelle()),
                    f.format(et.getDateFinReelle()));
        }
    }
}
