package ma.projet.service;

import ma.projet.beans.Femme;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;

import javax.persistence.TemporalType;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FemmeService extends AbstractService<Femme> {

    private static final SimpleDateFormat F = new SimpleDateFormat("dd/MM/yyyy");

    public FemmeService() { super(Femme.class); }

    public void afficherListe() {
        System.out.printf("%-5s%-24s%s%n", "Id", "Nom complet", "Date de naissance");
        for (Femme f : getAll()) {
            System.out.printf("%-5d%-24s%s%n", f.getId(), f.getNomComplet(), F.format(f.getDateNaissance()));
        }
    }

    /** Femme la plus âgée (date de naissance la plus ancienne). */
    public Femme getPlusAgee() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Femme> l = session.createQuery(
                    "from Femme f order by f.dateNaissance asc", Femme.class)
                    .setMaxResults(1)
                    .list();
            return l.isEmpty() ? null : l.get(0);
        }
    }

    public void afficherPlusAgee() {
        Femme f = getPlusAgee();
        if (f != null) {
            System.out.println("Femme la plus âgée : " + f.getNomComplet()
                    + " (née le " + F.format(f.getDateNaissance()) + ")");
        }
    }

    /** Requête native nommée : nombre d'enfants d'une femme entre deux dates. */
    public int getNbEnfantsEntreDates(int femmeId, Date d1, Date d2) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Object r = session.createNamedQuery("Femme.nbEnfantsEntreDates")
                    .setParameter("fid", femmeId)
                    .setParameter("d1", d1, TemporalType.DATE)
                    .setParameter("d2", d2, TemporalType.DATE)
                    .getSingleResult();
            return ((Number) r).intValue();
        }
    }

    public void afficherNbEnfantsEntreDates(int femmeId, Date d1, Date d2) {
        Femme f = getById(femmeId);
        if (f == null) {
            System.out.println("Femme introuvable : " + femmeId);
            return;
        }
        System.out.println("Nombre d'enfants de " + f.getNomComplet() + " entre "
                + F.format(d1) + " et " + F.format(d2) + " : "
                + getNbEnfantsEntreDates(femmeId, d1, d2));
    }

    /** Requête nommée : femmes mariées au moins deux fois. */
    public List<Femme> getMarieesAuMoinsDeuxFois() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Femme.mariesAuMoinsDeuxFois", Femme.class).list();
        }
    }

    public void afficherMarieesAuMoinsDeuxFois() {
        System.out.println("Femmes mariées deux fois ou plus :");
        for (Femme f : getMarieesAuMoinsDeuxFois()) {
            System.out.println(" - " + f.getNomComplet());
        }
    }
}
