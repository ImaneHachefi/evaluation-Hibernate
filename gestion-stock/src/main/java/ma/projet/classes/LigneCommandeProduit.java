package ma.projet.classes;

import javax.persistence.*;

/** Classe d'association entre Produit et Commande (porte l'attribut quantite). */
@Entity
public class LigneCommandeProduit {

    @EmbeddedId
    private LigneCommandeProduitPK id = new LigneCommandeProduitPK();

    @ManyToOne
    @MapsId("produitId")
    @JoinColumn(name = "produit_id")
    private Produit produit;

    @ManyToOne
    @MapsId("commandeId")
    @JoinColumn(name = "commande_id")
    private Commande commande;

    private int quantite;

    public LigneCommandeProduit() {}

    public LigneCommandeProduit(Produit produit, Commande commande, int quantite) {
        this.produit = produit;
        this.commande = commande;
        this.quantite = quantite;
        this.id = new LigneCommandeProduitPK(produit.getId(), commande.getId());
    }

    public LigneCommandeProduitPK getId() { return id; }
    public void setId(LigneCommandeProduitPK id) { this.id = id; }
    public Produit getProduit() { return produit; }
    public void setProduit(Produit produit) { this.produit = produit; }
    public Commande getCommande() { return commande; }
    public void setCommande(Commande commande) { this.commande = commande; }
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
}
