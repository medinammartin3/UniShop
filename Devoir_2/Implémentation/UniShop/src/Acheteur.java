import java.util.ArrayList;
import java.util.StringJoiner;

public class Acheteur extends User{

    final String nom;
    final String prenom;
    private int points;
    private final int likes;
    final ArrayList<String> acheteursSuivis;
    final ArrayList<String> revendeursLikes;
    final Commande panier;

    public Acheteur(String u, String p, String em, long phone, String address, String nom,
                    String prenom, int points, int likes, ArrayList<String> acheteursSuivis,
                    ArrayList<String> revendeursLikes, ArrayList<Billet> b, Commande panier, ArrayList<Commande> cmds){
        super(u, p, em, phone, address, b, cmds);
        this.nom = nom;
        this.prenom = prenom;
        this.points = points;
        this.likes = likes;
        this.acheteursSuivis = new ArrayList<>(acheteursSuivis);
        this.revendeursLikes = new ArrayList<>(revendeursLikes);
        this.panier = panier;
    }

    @Override
    public boolean isAcheteur() {
        return true;
    }
    @Override
    public void ajouterCommande(Commande c) {
        commandes.add(c);
        points += c.getPointsTotal();
        save();
    }
    @Override
    public void save() {
        StringJoiner sj = new StringJoiner("\n");
        String[] infos = new String[] {this.password, this.email, String.valueOf(this.phone), this.address, nom,
                prenom, String.valueOf(points), String.valueOf(likes)};
        sj.add(String.join(",", infos));
        sj.add(String.join(",", acheteursSuivis));
        sj.add(String.join(",", revendeursLikes));
        if (billets.isEmpty())
            sj.add("");
        for (Billet b : billets)
            sj.add(b.saveFormat());
        Main.ecrireFichierEntier(Main.USERS_PATH + Main.ACHETEURS + this.username + "/Infos.csv", sj.toString());
    }
    public String suivre(String acheteur) {
        if (this.username.equals(acheteur))
            return "Vous ne pouvez pas vous suivre vous-même!";
        else if (acheteursSuivis.contains(acheteur))
            return "Vous suivez déjà cet acheteur!";
        else {
            acheteursSuivis.add(acheteur);
            save();
            return "Vous suivez maintenant " + acheteur + "!";
        }
    }
    public boolean aAcheteProduit(String nomProduit) {
        for(Commande c : commandes) {
            for(String pt : c.getProduits()) {
                if (nomProduit.equals(pt))
                    return true;
            }
        }
        return false;
    }
    public void ajouterPoints(int pts) {
        this.points += pts;
    }
    public boolean billetExiste(int id) {
        for (Billet b : this.billets){
            if (b.id == id)
                return true;
        }
        return false;
    }
    @Override
    public String afficherMetriques() {
        return "\nNombre de points: " + points + "\nNombre total de commandes effectuées: " + commandes.size() +
                "\nNombre de followers: " + likes;
    }
}
