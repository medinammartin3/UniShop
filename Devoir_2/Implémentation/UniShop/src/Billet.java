import java.util.StringJoiner;

public class Billet {
    final int id;
    final String nomAche;
    final String produitInitial;
    final String probAche;
    final boolean estRetour;
    final String produitRempla;

    private boolean iniLivre; //Revendeur peut modifier
    private String probRev;

    private boolean remplaLivre; //Acheteur



    public Billet(int id, String nomAche, String produitInitial, String probAche, boolean estRetour, boolean iniLivre,
                  String probRev, String produitRempla, boolean remplaLivre) {
        this.id = id;
        this.nomAche = nomAche;
        this.produitInitial = produitInitial;
        this.probAche = probAche;
        this.estRetour = estRetour;
        this.iniLivre = iniLivre;
        this.probRev = probRev;
        this.produitRempla = produitRempla;
        this.remplaLivre = remplaLivre;
    }

    public boolean isRemplaLivre() {
        return remplaLivre;
    }

    public String saveFormat() {
        return String.join(",", String.valueOf(id), nomAche, produitInitial, probAche,
                String.valueOf(estRetour), String.valueOf(iniLivre), probRev, produitRempla,
                String.valueOf(remplaLivre));
    }
    public String afficher(){
        StringJoiner sj = new StringJoiner("\n");
        sj.add("ID: " + id);
        if (estRetour)
            sj.add("Retour d'un produit");
        else
            sj.add("Échange d'un produit");
        sj.add("Billet créé par: " + nomAche);
        sj.add("Produit problématique: " + produitInitial);
        sj.add("Description du problème par l'acheteur: " + probAche);
        if (probRev.isEmpty())
            sj.add("Le revendeur n'a pas encore ajouté de solution.");
        else
            sj.add("Solution du revendeur: " + probRev);
        if (iniLivre)
            sj.add(produitInitial + " a été livré à l'entrepôt.");
        else
            sj.add(produitInitial + " n'a pas encore été livré à l'entrepôt");
        if(!estRetour) {
            sj.add("Produit de remplacement: " + produitRempla);
            if(remplaLivre)
                sj.add(produitRempla + " a été livré à l'acheteur.");
            else
                sj.add(produitRempla + " n'a pas encore été livré à l'acheteur");
        }
        return sj.toString();
    }
    public String afficherMenu(){
        return "ID: " + id + "; Acheteur: " + nomAche + "; Produit problématique: " + produitInitial +
                (estRetour ? "; Retour" : "; Échange");
    }
    public boolean pasDeSolution() {return probRev.isEmpty();}
    public void setProbRev(String probRev) {
        this.probRev = probRev;
    }
    public boolean comfirmerLivraisonInitial() {
        if (this.iniLivre)
            return false;
        else {
            this.iniLivre = true;
            return true;
        }
    }

    public boolean comfirmerLivraisonRempla() {
        if (this.remplaLivre)
            return false;
        else {
            this.remplaLivre = true;
            return true;
        }
    }
}
