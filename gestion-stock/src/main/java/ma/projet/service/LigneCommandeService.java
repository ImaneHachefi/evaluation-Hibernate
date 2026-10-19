package ma.projet.service;

import ma.projet.classes.LigneCommandeProduit;
import ma.projet.classes.LigneCommandeProduitPK;

public class LigneCommandeService extends AbstractService<LigneCommandeProduit> {

    public LigneCommandeService() { super(LigneCommandeProduit.class); }

    public LigneCommandeProduit getById(int produitId, int commandeId) {
        return getById(new LigneCommandeProduitPK(produitId, commandeId));
    }
}
