package ma.projet.classes;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_commande")
    private Date date;

    @OneToMany(mappedBy = "commande")
    private List<LigneCommandeProduit> lignes = new ArrayList<>();

    public Commande() {}

    public Commande(Date date) { this.date = date; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public List<LigneCommandeProduit> getLignes() { return lignes; }
    public void setLignes(List<LigneCommandeProduit> lignes) { this.lignes = lignes; }

    @Override
    public String toString() {
        return "Commande [id=" + id + ", date=" + date + "]";
    }
}
