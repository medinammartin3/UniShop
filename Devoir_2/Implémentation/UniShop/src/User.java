import java.util.ArrayList;
import java.util.Stack;

public abstract class User {

    protected final String username;
    protected String password;
    protected String email;
    protected long phone;
    protected String address;
    protected final ArrayList<Billet> billets;
    protected final ArrayList<Commande> commandes;
    protected Stack<Notification> notifications;

    protected User(String username, String password, String email, long phone, String address,
                   ArrayList<Billet> billets, ArrayList<Commande> commandes) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.billets = new ArrayList<>(billets);
        this.commandes = new ArrayList<>(commandes);

    }
    protected abstract void save();
    public abstract boolean isAcheteur();
    public abstract String afficherMetriques();
    public void addBillet(Billet b) {
        this.billets.add(b);
        save();
    }
    public ArrayList<Billet> getBillets() {
        return new ArrayList<>(billets);
    }
    public Billet trouverBillet(int id) {
        for (Billet b : billets) {
            if (b.id == id)
                return b;
        }
        return null;
    }
    public ArrayList<Commande> getCommandes() {
        return new ArrayList<>(commandes);
    }
    public abstract void ajouterCommande(Commande c);

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
