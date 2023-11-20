import java.io.IOException;
import java.util.*;

public class Commande {

    private final String[] etats = new String[]{"en production", "en cours de livraison", "livré"};
    private short etat;
    private float coutTotal;
    private int pointsTotal;
    private String date;
    private int id;
    private ArrayList<Produit> produits;
    private String adresse;

    public Commande (short etat, float coutTotal, int pointsTotal) {
        this.etat = etat;
        this.coutTotal = coutTotal;
        this.pointsTotal = pointsTotal;
        this.produits = new ArrayList<>();
    }
    public Commande copy() {
        Commande c = new Commande(etat, coutTotal, pointsTotal);
        c.addPastInfo(id, date, adresse);
        c.produits = new ArrayList<>(this.produits);
        return c;
    }

    public int getId() {
        return id;
    }

    public int getPointsTotal() {
        return pointsTotal;
    }

    public String getEtat(){
        return etats[etat];
    }
    public boolean estEnLivraison() {return etat == 1;}
    public short confirmerLivraison() {
        if (this.estEnLivraison()) {
            ++this.etat;
            return 0;
        }
        else if (etat == 2)
            return 2;
        else
            return 1;
    }
    public void addPastInfo(int id, String date, String adresse) {
        this.id = id;
        this.date = date;
        this.adresse = adresse;
    }
    public void addInitial(Produit p) {
        produits.add(p);
    }
    public void addProduit(Produit p) {
        this.produits.add(p);
        this.coutTotal = Main.arrondirPrix(this.coutTotal + p.prix);
        this.pointsTotal += p.points;
        save();
    }
    public void removeProduit(Produit p) {
        this.produits.remove(p);
        this.coutTotal = Main.arrondirPrix(this.coutTotal - p.prix);
        this.pointsTotal -= p.points;
        save();
    }
    public String afficher() {
        StringJoiner sj = new StringJoiner("\n");
        sj.add("Total: " + coutTotal + "$; Points à accumuler: " + pointsTotal);
        for(Produit p : this.produits)
            sj.add(p.titre +  "; " + p.prix + "$; " + p.points + " points");
        return sj.toString();
    }
    public Commande passerCommande(String path, String adresse) throws IOException {
        String[] ids = Main.lireFichierEnEntier(Main.IDS);
        String[] fs = ids[0].split(",");
        id = Integer.parseInt(fs[0]);
        int produitID = Integer.parseInt(fs[1]);
        date = new java.util.Date().toString();
        StringJoiner sj = new StringJoiner("\n");
        String[] base = new String[] {String.valueOf(id), date, "0", String.valueOf(coutTotal),
                String.valueOf(pointsTotal), adresse};
        sj.add(String.join(",", base));
        for(Produit p : this.produits) {
            sj.add(p.titre + "," + produitID);
            p.commander();
            p.setUniqueId(produitID);
            ++produitID;
        }
        Main.ecrireFichierEntier(Main.IDS, (id + 1) + "," + produitID);
        Main.ecrireFichierEntier(path + "/" + id + Main.CSV, sj.toString());
        return this;
    }
    public void save(){
        String path = Main.USERS_PATH + Main.ACHETEURS + Main.getConnectedUsername() + "/Panier.csv";
        StringJoiner sj = new StringJoiner("\n");
        sj.add(coutTotal + "," + pointsTotal);
        for(Produit p : produits)
            sj.add(p.titre);
        Main.ecrireFichierEntier(path, sj.toString());
    }
    public void saveAfter(String userPath) {
        StringJoiner sj = new StringJoiner("\n");
        String[] base = new String[] {String.valueOf(id), date, String.valueOf(etat), String.valueOf(coutTotal),
                String.valueOf(pointsTotal), adresse};
        sj.add(String.join(",", base));
        for(Produit p : this.produits) {
            sj.add(p.titre + "," + p.getId());
        }
        Main.ecrireFichierEntier(userPath + id + Main.CSV, sj.toString());
    }
    public void vider() {
        pointsTotal = 0;
        coutTotal = 0;
        produits.clear();
        save();
    }
    public Produit getChoixProduit(boolean menuOption) {
        System.out.println("Choisissez un produit: ");
        Produit[] ps = produits.toArray(new Produit[0]);
        String[] s;
        if (menuOption)
            s = new String[ps.length + 1];
        else
            s = new String[ps.length];
        int i = 0;
        boolean isPanier = id == 0;
        for (Produit p : ps) {
            if (isPanier)
                s[i] = p.titre;
            else
                s[i] = p.titre + "; ID: " + p.getId();
            ++i;
        }
        if (menuOption)
            s[ps.length] = "Retour au menu";
        short c = Main.selectionChoix(s);
        if (c == s.length && menuOption)
            return  null;
        return ps[c - 1];
    }
    public String[] getProduits() {
        Produit[] ps = produits.toArray(new Produit[0]);
        String[] s = new String[produits.size()];
        int i = 0;
        for (Produit p : ps) {
            s[i] = p.titre;
            ++i;
        }
        return s;
    }
    public boolean estVide() {
        return produits.isEmpty();
    }
    public String getMenuDisplay() {
        return "ID: " + id + " ; Date: " + date + " ; Total: " + coutTotal + "$";
    }

}
