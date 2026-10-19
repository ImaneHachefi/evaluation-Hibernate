package ma.projet.service;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Convention : un mariage est "dans la période [d1, d2]" s'il chevauche cette période,
 * c'est-à-dire dateDebut <= d2 ET (dateFin IS NULL OU dateFin >= d1).
 */
public class HommeService extends AbstractService<Homme> {

    private static final SimpleDateFormat F = new SimpleDateFormat("dd/MM/yyyy");

    public HommeService() { super(Homme.class); }

    /** Épouses d'un homme entre deux dates. */
    public List<Femme> getEpousesEntreDates(int hommeId, Date d1, Date d2) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "select m.femme from Mariage m where m.homme.id = :hid " +
                    "and m.dateDebut <= :d2 and (m.dateFin is null or m.dateFin >= :d1) " +
                    "order by m.dateDebut", Femme.class)
                    .setParameter("hid", hommeId)
                    .setParameter("d1", d1)
                    .setParameter("d2", d2)
                    .list();
        }
    }

    public void afficherEpousesEntreDates(int hommeId, Date d1, Date d2) {
        Homme h = getById(hommeId);
        if (h == null) {
            System.out.println("Homme introuvable : " + hommeId);
            return;
        }
        System.out.println("Épouses de " + h.getNomComplet()
                + " entre " + F.format(d1) + " et " + F.format(d2) + " :");
        int i = 1;
        for (Femme f : getEpousesEntreDates(hommeId, d1, d2)) {
            System.out.println(i++ + ". " + f.getNomComplet());
        }
    }

    /** API Criteria : hommes mariés à exactement quatre femmes entre deux dates. */
    public List<Homme> getHommesMariesAQuatreFemmes(Date d1, Date d2) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
            Root<Mariage> m = cq.from(Mariage.class);

            Path<Integer> hommeId = m.get("homme").get("id");

            cq.select(hommeId)
              .where(
                  cb.lessThanOrEqualTo(m.<Date>get("dateDebut"), d2),
                  cb.or(
                      cb.isNull(m.get("dateFin")),
                      cb.greaterThanOrEqualTo(m.<Date>get("dateFin"), d1)))
              .groupBy(hommeId)
              .having(cb.equal(cb.countDistinct(m.get("femme")), 4L));

            List<Integer> ids = session.createQuery(cq).getResultList();
            if (ids.isEmpty()) {
                return new ArrayList<>();
            }
            return session.createQuery("select h from Homme h where h.id in (:ids)", Homme.class)
                    .setParameterList("ids", ids)
                    .list();
        }
    }

    public void afficherHommesMariesAQuatreFemmes(Date d1, Date d2) {
        List<Homme> hommes = getHommesMariesAQuatreFemmes(d1, d2);
        System.out.println("Nombre d'hommes mariés à quatre femmes entre "
                + F.format(d1) + " et " + F.format(d2) + " : " + hommes.size());
        for (Homme h : hommes) {
            System.out.println(" - " + h.getNomComplet());
        }
    }

    /** Affiche les mariages d'un homme (en cours puis échoués) avec les détails. */
    public void afficherMariages(int hommeId) {
        Homme h = getById(hommeId);
        if (h == null) {
            System.out.println("Homme introuvable : " + hommeId);
            return;
        }
        List<Mariage> mariages;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            mariages = session.createQuery(
                    "select m from Mariage m join fetch m.femme " +
                    "where m.homme.id = :hid order by m.dateDebut", Mariage.class)
                    .setParameter("hid", hommeId)
                    .list();
        }

        System.out.println("Nom : " + h.getNomComplet());

        System.out.println("Mariages En Cours :");
        int i = 1;
        for (Mariage m : mariages) {
            if (m.getDateFin() == null) {
                System.out.printf("%d. Femme : %-16s Date Début : %s    Nbr Enfants : %d%n",
                        i++, m.getFemme().getNomComplet(), F.format(m.getDateDebut()), m.getNbrEnfant());
            }
        }

        System.out.println();
        System.out.println("Mariages échoués :");
        i = 1;
        for (Mariage m : mariages) {
            if (m.getDateFin() != null) {
                System.out.printf("%d. Femme : %-16s Date Début : %s%n   Date Fin : %s    Nbr Enfants : %d%n",
                        i++, m.getFemme().getNomComplet(), F.format(m.getDateDebut()),
                        F.format(m.getDateFin()), m.getNbrEnfant());
            }
        }
    }
}
