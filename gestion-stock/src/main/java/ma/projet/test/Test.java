package ma.projet.test;

import ma.projet.classes.*;
import ma.projet.service.*;
import ma.projet.util.HibernateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {

    public static void main(String[] args) throws Exception {
        CategorieService cs = new CategorieService();
        ProduitService ps = new ProduitService();
        CommandeService cms = new CommandeService();
        LigneCommandeService ls = new LigneCommandeService();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // ---- Jeu de données ----
        Categorie c1 = new Categorie("INF", "Informatique");
        Categorie c2 = new Categorie("PER", "Périphériques");
        cs.create(c1);
        cs.create(c2);

        Produit p1 = new Produit("ES12", 120, c1);
        Produit p2 = new Produit("ZR85", 100, c1);
        Produit p3 = new Produit("EE85", 200, c2);
        Produit p4 = new Produit("AB10", 50, c2);
        ps.create(p1); ps.create(p2); ps.create(p3); ps.create(p4);

        Commande cmd1 = new Commande(sdf.parse("2013-03-14"));
        Commande cmd2 = new Commande(sdf.parse("2013-05-20"));
        cms.create(cmd1);
        cms.create(cmd2);

        ls.create(new LigneCommandeProduit(p1, cmd1, 7));
        ls.create(new LigneCommandeProduit(p2, cmd1, 14));
        ls.create(new LigneCommandeProduit(p3, cmd1, 5));
        ls.create(new LigneCommandeProduit(p4, cmd2, 3));

        // ---- Test 1 : produits par catégorie ----
        System.out.println("\n===== Produits par catégorie =====");
        ps.afficherProduitsParCategorie(c1);

        // ---- Test 2 : produits commandés entre deux dates ----
        System.out.println("\n===== Produits commandés entre deux dates =====");
        ps.afficherProduitsEntreDates(sdf.parse("2013-03-01"), sdf.parse("2013-03-31"));

        // ---- Test 3 : produits d'une commande ----
        System.out.println("\n===== Produits d'une commande =====");
        ps.afficherProduitsParCommande(cmd1.getId());

        // ---- Test 4 : requête nommée (prix > 100 DH) ----
        System.out.println("\n===== Produits dont le prix > 100 DH =====");
        for (Produit p : ps.getPrixSuperieurA100()) {
            System.out.println(" - " + p.getReference() + "  " + p.getPrix() + " DH");
        }

        HibernateUtil.shutdown();
    }
}
