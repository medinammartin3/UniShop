import java.util.ArrayList;
import java.util.StringJoiner;

public class Revendeur extends User{

    private float revenu;
    private int nbProduitsVendus;
    ArrayList<String> followers;
    ArrayList<Produit> produits;
    ArrayList<Commande> commandes;

    public Revendeur(String u, String p, String email, long phone, String address, float revenu,
                     int nbProduitsVendus, ArrayList<String> followers, ArrayList<Billet> b, ArrayList<Produit> ps,
                     ArrayList<Commande> cmds) {
        super(u, p, email, phone, address, b, cmds);
        this.revenu = revenu;
        this.nbProduitsVendus = nbProduitsVendus;
        this.followers = new ArrayList<>(followers);
        this.produits = new ArrayList<>(ps);
    }

    @Override
    public boolean isAcheteur() {
        return false;
    }
    public void ajouterProduit(Produit p) {
        this.produits.add(p);
    }
    @Override
    public void save() {
        StringJoiner sj = new StringJoiner("\n");
        String[] infos = new String[]{this.password, this.email, String.valueOf(this.phone), this.address,
               String.valueOf(revenu), String.valueOf(nbProduitsVendus)};
        sj.add(String.join(",", infos));
        sj.add(String.join(",", followers));
        if (billets.isEmpty())
            sj.add("");
        for (Billet b : this.billets)
            sj.add(b.saveFormat());
        Main.ecrireFichierEntier(Main.USERS_PATH + Main.REVENDEURS + this.username + "/Infos.csv", sj.toString());
    }
    public Produit getProduitAvecChoix() {
        System.out.println("Choisissez un produit parmi ceux offert par le revendeur: ");
        Produit[] ps = produits.toArray(new Produit[0]);
        String[] s;
        s = new String[ps.length];
        int i = 0;
        for (Produit p : ps) {
            s[i] = p.getQuickDisplay();
            ++i;
        }
        return ps[Main.selectionChoix(s) - 1];
    }
    @Override
    public void ajouterCommande(Commande c) {
        commandes.add(c);
        save();
    }
    @Override
    public String afficherMetriques () {
        return "\nNombre de produits offerts: " + produits.size() + "\nRevenu: " + revenu +
                "$\nNombre de produits vendus: " + nbProduitsVendus;
    }
}