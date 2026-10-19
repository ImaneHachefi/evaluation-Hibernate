package ma.projet.test;

import ma.projet.classes.*;
import ma.projet.service.*;
import ma.projet.util.HibernateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    private static Date d(String s) throws Exception { return SDF.parse(s); }

    public static void main(String[] args) throws Exception {
        EmployeService es = new EmployeService();
        ProjetService ps = new ProjetService();
        TacheService ts = new TacheService();
        EmployeTacheService ets = new EmployeTacheService();

        // ---- Jeu de données ----
        Employe e1 = new Employe("Alami", "Ahmed", "0600000001");
        Employe e2 = new Employe("Bennani", "Sara", "0600000002");
        Employe e3 = new Employe("Tazi", "Omar", "0600000003");
        es.create(e1); es.create(e2); es.create(e3);

        Projet p1 = new Projet("Gestion de stock", d("2013-01-14"), d("2013-06-30"), e1);
        Projet p2 = new Projet("Site e-commerce", d("2013-02-01"), d("2013-05-31"), e2);
        ps.create(p1); ps.create(p2);

        Tache t1 = new Tache("Analyse", d("2013-02-10"), d("2013-02-20"), 1200, p1);
        Tache t2 = new Tache("Conception", d("2013-03-10"), d("2013-03-15"), 800, p1);
        Tache t3 = new Tache("Développement", d("2013-04-10"), d("2013-04-25"), 1500, p1);
        Tache t4 = new Tache("Maquette", d("2013-03-01"), d("2013-03-05"), 600, p2);
        ts.create(t1); ts.create(t2); ts.create(t3); ts.create(t4);

        ets.create(new EmployeTache(e2, t1, d("2013-02-10"), d("2013-02-20")));
        ets.create(new EmployeTache(e3, t2, d("2013-03-10"), d("2013-03-15")));
        ets.create(new EmployeTache(e2, t3, d("2013-04-10"), d("2013-04-25")));
        ets.create(new EmployeTache(e3, t4, d("2013-03-01"), d("2013-03-05")));

        // ---- Test 1 : tâches réalisées par un employé ----
        System.out.println("\n===== EmployeService : tâches réalisées par un employé =====");
        es.afficherTachesRealisees(e2.getId());

        // ---- Test 2 : projets gérés par un employé ----
        System.out.println("\n===== EmployeService : projets gérés par un employé =====");
        es.afficherProjetsGeres(e1.getId());

        // ---- Test 3 : tâches planifiées d'un projet ----
        System.out.println("\n===== ProjetService : tâches planifiées =====");
        ps.afficherTachesPlanifiees(p1.getId());

        // ---- Test 4 : tâches réalisées d'un projet (dates réelles) ----
        System.out.println("\n===== ProjetService : tâches réalisées (dates réelles) =====");
        ps.afficherTachesRealisees(p1.getId());

        // ---- Test 5 : requête nommée (prix > 1000 DH) ----
        System.out.println("\n===== TacheService : prix > 1000 DH (requête nommée) =====");
        ts.afficherPrixSuperieurA1000();

        // ---- Test 6 : tâches réalisées entre deux dates ----
        System.out.println("\n===== TacheService : tâches réalisées entre deux dates =====");
        ts.afficherRealiseesEntreDates(d("2013-03-01"), d("2013-04-30"));

        HibernateUtil.shutdown();
    }
}
