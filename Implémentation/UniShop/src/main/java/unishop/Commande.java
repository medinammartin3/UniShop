package unishop;

import static unishop.Main.*;

import java.util.*;

/**
 * Cette classe représente une commande avec ses différents états, coûts, points et les produits associés.
 */
public class Commande {

    private final String[] etats = new String[]{"en production", "en cours de livraison", "livré"};
    private short etat;
    private float coutTotal;
    private int pointsTotal;
    private String acheteur;
    private String date;
    private long reception;
    private int id;
    private ArrayList<Produit> produits;
    private String adresse;

    /**
     * Constructeur de la classe Commande.
     *
     * @param etat       L'état initial de la commande.
     * @param coutTotal  Le coût total de la commande.
     * @param pointsTotal Le nombre total de points générés par la commande.
     */
    public Commande (short etat, float coutTotal, int pointsTotal) {
        this.etat = etat;
        this.coutTotal = coutTotal;
        this.pointsTotal = pointsTotal;
        this.produits = new ArrayList<>();
    }
    /**
     * Crée une copie de la commande.
     *
     * @return Une copie de la commande.
     */
    public Commande copy() {
        Commande c = new Commande(etat, coutTotal, pointsTotal);
        c.addPastInfo(id, date, adresse, acheteur, reception);
        c.produits = new ArrayList<>(this.produits);
        return c;
    }
    /**
     * Renvoie l'identifiant de la commande.
     *
     * @return L'identifiant de la commande.
     */
    public int getId() { return id; }

    /**
     * Renvoie les points totaux de la commande
     *
     * @return les points totaux de la commande
     */
    public int getPointsTotal() { return pointsTotal; }

    /**
     * Renvoie le coût total de la commande
     *
     * @return le coût total de la commande
     */


    public float getCoutTotal() { return coutTotal; }

    /**
     * Cette méthode recense l'acheteur
     * @return  L'acheteur
     */
    public String getAcheteur() { return this.acheteur; }
    /**
     * Renvoie l'état de la commande
     * @return l'état de la commande
     */
    public String getEtat() { return etats[etat]; }
    /**
     * Renvoie les produits de la commande
     * @return les produits  de la commande
     */
    public ArrayList<Produit> getProduits() { return new ArrayList<>(this.produits);}

    /**
     * Renvoie le temps de réception de la commande
     * @return le temps de réception de la commande
     */
    public long getTempsReception() {
        return this.reception;
    }

    /**
     * Renvoie les revendeurs de la commande
     * @return les revendeurs de la commande
     */
    public ArrayList<String> getRevendeurs() {
        ArrayList<String> revs = new ArrayList<>();
        for (Produit p : produits) {
            if (!revs.contains(p.getNomReven()))
                revs.add(p.getNomReven());
        }
        return revs;
    }

    /**
     * Vérifie si la commande est vide
     * @return True si la commande est vide
     */
    public boolean estVide() {
        return produits.isEmpty();
    }

    /**
     * Obtient le menu display de la commande
     * @return Le menu display de la commande
     */
    public String getMenuDisplay() {
        return "ID: " + id + " ; Date: " + date + " ; Total: " + coutTotal + "$";
    }

    /**
     * Vérifie si la commande est en production
     *
     * @return True si la commande est en production, sinon False
     */
    public boolean estEnProduction() {return etat == 0;}
    /**
     * Vérifie si la commande est en cours de livraison
     *
     * @return True si la commande est en cours de livraison et False sinon
     */
    public boolean estEnLivraison() {return etat == 1;}

    /** Vérifie si la commande a été livré
     *
     * @return True si la commande a été livré et False sinon
     */
    public boolean estLivre() {return etat == 2;}
    /**
     * Met la commande est état de livraison
     */
    public void mettreEnLivraison() {
        ++this.etat;
        save();
    }
    /**
     * Confirme la livraison de la commande
     *
     * @return 0 si la commande est en cours de livraison, 2 si la commande à été livré et 1 sinon
     */
    public short confirmerLivraison() {
        if (this.estEnLivraison()) {
            ++this.etat;
            this.reception = obtenirTempsEnSecondes();
            save();
            return 0;
        }
        else if (estLivre())
            return 2;
        else
            return 1;
    }
    /**
     * Ajoutes les informations mises en paramètre au produit
     * @param id L'id que l'on souhaite ajouter
     * @param date La date que l'on souhaite ajouter
     * @param adresse L'adresse que l'on souhaite ajouter
     * @param reception La réception que l'on souhaite ajouter
     */
    public void addPastInfo(int id, String date, String adresse, String acheteur, long reception) {
        this.id = id;
        this.date = date;
        this.adresse = adresse;
        this.acheteur = acheteur;
        this.reception = reception;
    }
    /**
     * Ajoutes un prooduit au panier en mettant à jour le coût total, le nombre de points total de celui-ci
     * @param p Le produit que l'on souhaite ajouter au panier
     */
    public void addInitial(Produit p) {
        produits.add(p);
    }
    /**
     * Ajoutes un produit à echanger au panier
     * @param p Le produit que l'on souhaite ajouter au panier
     */
    public void addProduit(Produit p) {
        this.produits.add(p);
        this.coutTotal = arrondirPrix(this.coutTotal + p.getPrix());
        this.pointsTotal += p.getPoints();
        savePanier();
    }

    /**
     * Retires un produit du panier
     * @param p Le produit que l'on souhaite retirer du panier
     */
    public void removeProduit(Produit p) {
        this.produits.remove(p);
        this.coutTotal = arrondirPrix(this.coutTotal - p.getPrix());
        this.pointsTotal -= p.getPoints();
        savePanier();
    }

    /**
     * Permet d'éffectuer un échange de produit contre une compensation
     * @param p Le produit à échanger
     * @param prix Le prix du produit
     */
    public void setEchange(Produit p, float prix) {
        this.produits.add(p);
        this.coutTotal = prix;
    }
    /** Affiche le coût et le nombre de points du panier. En plus du coût et du nombre de points des produits du panier
     * @return Renvoies le coût et le nombre de points du panier. En plus du coût et du nombre de points des produits du panier
     */
    public String afficher() {
        StringJoiner sj = new StringJoiner("\n");
        sj.add("Total: " + coutTotal + "$; Points à accumuler: " + pointsTotal);
        for(Produit p : this.produits)
            sj.add(p.getTitre() +  "; " + p.getPrix() + "$; " + p.getPoints() + " points");
        return sj.toString();
    }
    /**
     * Passe une commande
     * @param adresse Les informations de la commande
     * @return La  commande
     */
    public Commande passerCommande(String nom, String adresse) {
        String[] ids = lireFichierEnEntier(IDS);
        String[] fs = ids[0].split(",");
        id = Integer.parseInt(fs[0]);
        int produitID = Integer.parseInt(fs[1]);
        date = new java.util.Date().toString();
        StringJoiner sj = new StringJoiner("\n");
        String[] base = new String[] {String.valueOf(id), date, "0", String.valueOf(coutTotal),
                String.valueOf(pointsTotal), adresse, nom};
        sj.add(String.join(",", base));
        for(Produit p : this.produits) {
            sj.add(p.getTitre() + "," + produitID);
            p.commander();
            p.setUniqueId(produitID);
            ++produitID;
        }
        this.adresse = adresse;
        this.acheteur = nom;
        ecrireFichierEntier(IDS, (id + 1) + "," + produitID);
        ecrireFichierEntier(COMMANDES_PATH + id + CSV, sj.toString());
        return this;
    }

    /**
     * Vide le panier
     */
    public void vider() {
        pointsTotal = 0;
        coutTotal = 0;
        produits.clear();
        savePanier();
    }
    /**
     * Enregistre le panier
     */
    public void savePanier(){
        String path = ACHETEURS_PATH + getConnectedUsername() + "/" + PANIER;
        StringJoiner sj = new StringJoiner("\n");
        sj.add(coutTotal + "," + pointsTotal);
        for(Produit p : produits)
            sj.add(p.getTitre());
        ecrireFichierEntier(path, sj.toString());
    }
    /**
     * Enregistre les données
     */
    public void save() {
        StringJoiner sj = new StringJoiner("\n");
        String[] base = new String[] {String.valueOf(id), date, String.valueOf(etat), String.valueOf(coutTotal),
                String.valueOf(pointsTotal), adresse, acheteur, String.valueOf(reception)};
        sj.add(String.join(",", base));
        for(Produit p : this.produits)
            sj.add(p.getTitre() + "," + p.getId());
        ecrireFichierEntier(COMMANDES_PATH + id + CSV, sj.toString());
    }
}
