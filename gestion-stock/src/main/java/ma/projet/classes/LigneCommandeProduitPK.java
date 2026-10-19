package ma.projet.classes;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class LigneCommandeProduitPK implements Serializable {

    @Column(name = "produit_id")
    private int produitId;

    @Column(name = "commande_id")
    private int commandeId;

    public LigneCommandeProduitPK() {}

    public LigneCommandeProduitPK(int produitId, int commandeId) {
        this.produitId = produitId;
        this.commandeId = commandeId;
    }

    public int getProduitId() { return produitId; }
    public void setProduitId(int produitId) { this.produitId = produitId; }
    public int getCommandeId() { return commandeId; }
    public void setCommandeId(int commandeId) { this.commandeId = commandeId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LigneCommandeProduitPK)) return false;
        LigneCommandeProduitPK that = (LigneCommandeProduitPK) o;
        return produitId == that.produitId && commandeId == that.commandeId;
    }

    @Override
    public int hashCode() { return Objects.hash(produitId, commandeId); }
}
