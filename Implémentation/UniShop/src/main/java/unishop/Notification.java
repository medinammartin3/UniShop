package unishop;
/**
 *  Cette classe représente une notification en fonction du type, acheteur, revendeur, produit et le ID commande.
 */
public class Notification {
    final int type;
    final String acheteur;
    final String revendeur;
    final String produit;
    final int commandeID; //-1 Pour vide

    /**
     * Crée une instance de notification en fonction du type, acheteur, revendeur, produit et le ID commande
     *
     * @param type Type de notification
     * @param acheteur Acheteur associé à la commande
     * @param revendeur Revendeur associé à la commande
     * @param produit Produit associé à la commande
     * @param commandeID Id de la commande
     */
    public Notification(int type, String acheteur, String revendeur, String produit, int commandeID) {
        this.type = type;
        this.acheteur = acheteur;
        this.revendeur = revendeur;
        this.produit = produit;
        this.commandeID = commandeID;
    }

    /**
     * Cette méthode affiche un message
     * @return le message à afficher
     */
    public String afficher(){
        String s = "";
        switch (type) {
            case 0 -> s = "L'acheteur " + acheteur + " suit votre profil!";
            case 1 -> s = "Le revendeur " + revendeur + " que vous avez liké offre nouveau produit: " + produit;
            case 2 -> {
                if(produit.isEmpty())
                    s = "Le revendeur " + revendeur + " a ajouté une nouvelle promotion!";
                else
                    s = "Le produit " + produit + " a reçu une promotion!";
            }
            case 3 -> s = "L'état de la commande #" + commandeID + " a changé!";
            case 4 -> s = "Le produit " + produit + " que l'acheteur " + acheteur + " a liké a reçu une promotion!";
            case 5 -> s = "Une solution a été ajouté par " + revendeur + " pour le produit " + produit + "!";
            case 6 -> s = "Une nouvelle commande a été passée pour le produit " + produit + "!";
            case 7 -> s = "Une nouvelle évaluation a été ajoutée pour le produit " + produit + "!";
            case 8 -> s = "L'acheteur " + acheteur + " a signalé un problème pour le produit " + produit + "!";
        }
        return s;
    }

    /**
     * Cette méthode sauvegarde la notification selon un format spécifique
     * @return  La notification selon le format spécifique
     */
    public String saveFormat() {
        return String.join(",", String.valueOf(type), acheteur, revendeur, produit, String.valueOf(commandeID));
    }
}
