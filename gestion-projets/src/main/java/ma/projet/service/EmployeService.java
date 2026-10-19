package ma.projet.service;

import ma.projet.classes.Employe;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;

import java.text.SimpleDateFormat;
import java.util.List;

public class EmployeService extends AbstractService<Employe> {

    public EmployeService() { super(Employe.class); }

    /** Tâches réalisées par un employé (avec les dates réelles). */
    public List<EmployeTache> getTachesRealisees(int employeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "select et from EmployeTache et " +
                    "join fetch et.tache t join fetch t.projet " +
                    "where et.employe.id = :eid order by et.dateDebutReelle", EmployeTache.class)
                    .setParameter("eid", employeId)
                    .list();
        }
    }

    public void afficherTachesRealisees(int employeId) {
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        Employe e = getById(employeId);
        if (e == null) {
            System.out.println("Employé introuvable : " + employeId);
            return;
        }
        System.out.println("Tâches réalisées par " + e.getPrenom() + " " + e.getNom() + " :");
        System.out.printf("%-5s%-16s%-18s%-20s%s%n",
                "Num", "Nom", "Projet", "Date Début Réelle", "Date Fin Réelle");
        for (EmployeTache et : getTachesRealisees(employeId)) {
            System.out.printf("%-5d%-16s%-18s%-20s%s%n",
                    et.getTache().getId(),
                    et.getTache().getNom(),
                    et.getTache().getProjet().getNom(),
                    f.format(et.getDateDebutReelle()),
                    f.format(et.getDateFinReelle()));
        }
    }

    /** Projets gérés par un employé (chef de projet). */
    public List<Projet> getProjetsGeres(int employeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "select p from Projet p where p.chefProjet.id = :eid order by p.dateDebut", Projet.class)
                    .setParameter("eid", employeId)
                    .list();
        }
    }

    public void afficherProjetsGeres(int employeId) {
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        Employe e = getById(employeId);
        if (e == null) {
            System.out.println("Employé introuvable : " + employeId);
            return;
        }
        System.out.println("Projets gérés par " + e.getPrenom() + " " + e.getNom() + " :");
        System.out.printf("%-5s%-22s%s%n", "Num", "Nom", "Date Début");
        for (Projet p : getProjetsGeres(employeId)) {
            System.out.printf("%-5d%-22s%s%n", p.getId(), p.getNom(), f.format(p.getDateDebut()));
        }
    }
}
