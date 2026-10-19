package ma.projet.test;

import ma.projet.beans.*;
import ma.projet.service.*;
import ma.projet.util.HibernateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    private static Date d(String s) throws Exception { return SDF.parse(s); }

    private static void titre(String t) {
        System.out.println("\n===== " + t + " =====");
    }

    public static void main(String[] args) throws Exception {
        HommeService hs = new HommeService();
        FemmeService fs = new FemmeService();
        MariageService ms = new MariageService();

        // ---- 10 femmes ----
        Femme karima = new Femme("Alami", "Karima", "0661000001", "Rabat", d("1965-03-12"));
        Femme salima = new Femme("Rami", "Salima", "0661000002", "Casablanca", d("1968-07-21"));
        Femme amal   = new Femme("Ali", "Amal", "0661000003", "Fès", d("1972-11-02"));
        Femme wafa   = new Femme("Alaoui", "Wafa", "0661000004", "Marrakech", d("1978-01-30"));
        Femme hind   = new Femme("Benali", "Hind", "0661000005", "Tanger", d("1962-05-18"));
        Femme nadia  = new Femme("Tazi", "Nadia", "0661000006", "Agadir", d("1970-09-09"));
        Femme samira = new Femme("Idrissi", "Samira", "0661000007", "Meknès", d("1975-12-25"));
        Femme layla  = new Femme("Fassi", "Layla", "0661000008", "Oujda", d("1980-04-14"));
        Femme zineb  = new Femme("Berrada", "Zineb", "0661000009", "Tétouan", d("1983-08-08"));
        Femme malika = new Femme("Chraibi", "Malika", "0661000010", "Kénitra", d("1958-02-27"));
        for (Femme f : new Femme[]{karima, salima, amal, wafa, hind, nadia, samira, layla, zineb, malika}) {
            fs.create(f);
        }

        // ---- 5 hommes ----
        Homme safi    = new Homme("Said", "Safi", "0662000001", "Rabat", d("1960-01-15"));
        Homme karim   = new Homme("Bennis", "Karim", "0662000002", "Casablanca", d("1964-06-10"));
        Homme youssef = new Homme("Lahlou", "Youssef", "0662000003", "Fès", d("1958-10-03"));
        Homme omar    = new Homme("Tahiri", "Omar", "0662000004", "Marrakech", d("1966-03-22"));
        Homme rachid  = new Homme("Amrani", "Rachid", "0662000005", "Tanger", d("1955-12-01"));
        for (Homme h : new Homme[]{safi, karim, youssef, omar, rachid}) {
            hs.create(h);
        }

        // ---- Mariages ----
        // Safi Said : 3 mariages en cours + 1 échoué
        ms.create(new Mariage(safi, karima, d("1989-09-03"), d("1990-09-03"), 0));
        ms.create(new Mariage(safi, salima, d("1990-09-03"), null, 4));
        ms.create(new Mariage(safi, amal,   d("1995-09-03"), null, 2));
        ms.create(new Mariage(safi, wafa,   d("2000-11-04"), null, 3));
        // Karim Bennis
        ms.create(new Mariage(karim, karima, d("1992-05-10"), null, 1));
        ms.create(new Mariage(karim, nadia,  d("1998-06-01"), null, 2));
        // Youssef Lahlou
        ms.create(new Mariage(youssef, hind,   d("1985-07-01"), d("1993-03-15"), 2));
        ms.create(new Mariage(youssef, samira, d("1999-09-09"), null, 3));
        // Omar Tahiri
        ms.create(new Mariage(omar, hind, d("1996-02-02"), null, 1));
        // Rachid Amrani
        ms.create(new Mariage(rachid, malika, d("1980-04-04"), null, 5));
        ms.create(new Mariage(rachid, layla,  d("2005-05-05"), null, 1));

        // ---- Tests ----
        titre("Liste des femmes");
        fs.afficherListe();

        titre("Femme la plus âgée");
        fs.afficherPlusAgee();

        titre("Épouses d'un homme entre deux dates");
        hs.afficherEpousesEntreDates(safi.getId(), d("1985-01-01"), d("2005-12-31"));

        titre("Nombre d'enfants d'une femme entre deux dates (requête native nommée)");
        fs.afficherNbEnfantsEntreDates(hind.getId(), d("1980-01-01"), d("2000-12-31"));

        titre("Femmes mariées deux fois ou plus (requête nommée)");
        fs.afficherMarieesAuMoinsDeuxFois();

        titre("Hommes mariés à quatre femmes entre deux dates (API Criteria)");
        hs.afficherHommesMariesAQuatreFemmes(d("1985-01-01"), d("2005-12-31"));

        titre("Mariages d'un homme avec tous les détails");
        hs.afficherMariages(safi.getId());

        HibernateUtil.shutdown();
    }
}
