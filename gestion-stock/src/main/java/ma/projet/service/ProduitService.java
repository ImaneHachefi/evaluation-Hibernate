package ma.projet.service;

import ma.projet.classes.Categorie;
import ma.projet.classes.LigneCommandeProduit;
import ma.projet.classes.Produit;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProduitService extends AbstractService<Produit> {

    public ProduitService() { super(Produit.class); }

    /** Liste des produits d'une catégorie donnée. */
    public List<Produit> getByCategorie(Categorie categorie) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "select p from Produit p where p.categorie.id = :cid", Produit.class)
                    .setParameter("cid", categorie.getId())
                    .list();
        }
    }

    public void afficherProduitsParCategorie(Categorie categorie) {
        System.out.println("Produits de la catégorie : " + categorie.getLibelle());
        for (Produit p : getByCategorie(categorie)) {
            System.out.println(" - " + p.getReference() + "  " + p.getPrix() + " DH");
        }
    }

    /** Produits commandés entre deux dates (sans doublons). */
    public List<Produit> getCommandesEntreDates(Date debut, Date fin) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "select distinct l.produit from LigneCommandeProduit l " +
                    "where l.commande.date between :d1 and :d2", Produit.class)
                    .setParameter("d1", debut)
                    .setParameter("d2", fin)
                    .list();
        }
    }

    public void afficherProduitsEntreDates(Date debut, Date fin) {
        SimpleDateFormat f = new SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH);
        System.out.println("Produits commandés entre " + f.format(debut) + " et " + f.format(fin) + " :");
        for (Produit p : getCommandesEntreDates(debut, fin)) {
            System.out.println(" - " + p.getReference() + "  " + p.getPrix() + " DH");
        }
    }

    /** Affiche les produits d'une commande (référence, prix, quantité). */
    public void afficherProduitsParCommande(int commandeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<LigneCommandeProduit> lignes = session.createQuery(
                    "select l from LigneCommandeProduit l " +
                    "join fetch l.produit join fetch l.commande " +
                    "where l.commande.id = :cid", LigneCommandeProduit.class)
                    .setParameter("cid", commandeId)
                    .list();

            if (lignes.isEmpty()) {
                System.out.println("Aucune ligne pour la commande " + commandeId);
                return;
            }
            SimpleDateFormat f = new SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH);
            System.out.println("Commande : " + commandeId + "     Date : "
                    + f.format(lignes.get(0).getCommande().getDate()));
            System.out.println("Liste des produits :");
            System.out.printf("%-12s%-10s%s%n", "Référence", "Prix", "Quantité");
            for (LigneCommandeProduit l : lignes) {
                System.out.printf("%-12s%-10s%d%n",
                        l.getProduit().getReference(),
                        (int) l.getProduit().getPrix() + " DH",
                        l.getQuantite());
            }
        }
    }

    /** Produits dont le prix est supérieur à 100 DH (requête nommée). */
    public List<Produit> getPrixSuperieurA100() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Produit.findByPrixSuperieur", Produit.class)
                    .setParameter("prix", 100f)
                    .list();
        }
    }
}
