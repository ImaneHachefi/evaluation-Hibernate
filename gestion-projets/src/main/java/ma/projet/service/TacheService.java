package ma.projet.service;

import ma.projet.classes.EmployeTache;
import ma.projet.classes.Tache;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TacheService extends AbstractService<Tache> {

    public TacheService() { super(Tache.class); }

    /** Tâches dont le prix est supérieur à 1000 DH (requête nommée). */
    public List<Tache> getPrixSuperieurA1000() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Tache.findByPrixSuperieur", Tache.class)
                    .setParameter("prix", 1000d)
                    .list();
        }
    }

    public void afficherPrixSuperieurA1000() {
        System.out.println("Tâches dont le prix > 1000 DH :");
        System.out.printf("%-5s%-16s%s%n", "Num", "Nom", "Prix");
        for (Tache t : getPrixSuperieurA1000()) {
            System.out.printf("%-5d%-16s%s%n", t.getId(), t.getNom(), (int) t.getPrix() + " DH");
        }
    }

    /** Tâches réalisées (dates réelles) entièrement comprises entre deux dates. */
    public List<EmployeTache> getRealiseesEntreDates(Date debut, Date fin) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "select et from EmployeTache et join fetch et.tache " +
                    "where et.dateDebutReelle >= :d1 and et.dateFinReelle <= :d2 " +
                    "order by et.dateDebutReelle", EmployeTache.class)
                    .setParameter("d1", debut)
                    .setParameter("d2", fin)
                    .list();
        }
    }

    public void afficherRealiseesEntreDates(Date debut, Date fin) {
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println("Tâches réalisées entre " + f.format(debut) + " et " + f.format(fin) + " :");
        System.out.printf("%-5s%-16s%-20s%s%n", "Num", "Nom", "Date Début Réelle", "Date Fin Réelle");
        for (EmployeTache et : getRealiseesEntreDates(debut, fin)) {
            System.out.printf("%-5d%-16s%-20s%s%n",
                    et.getTache().getId(),
                    et.getTache().getNom(),
                    f.format(et.getDateDebutReelle()),
                    f.format(et.getDateFinReelle()));
        }
    }
}
