public class Notification {
    final int type;
    final String acheteur;
    final String revendeur;
    final String produit;
    final int commandeID; //-1 Pour vide

    public Notification(int type, String acheteur, String revendeur, String produit, int commandeID) {
        this.type = type;
        this.acheteur = acheteur;
        this.revendeur = revendeur;
        this.produit = produit;
        this.commandeID = commandeID;
    }

    public String afficher(){
        String s = "";
        switch (type) {
            case 0 -> s = "L'acheteur " + acheteur + "suit votre profil!";
            case 1 -> s = "Le revendeur " + revendeur + " que vous avez liké offre nouveau produit: " + produit;
            case 2 -> {
                if(produit.isEmpty())
                    s = "Le revendeur " + revendeur + " a ajouté une nouvelle promotion!";
                else
                    s = "Le produit " + produit + " a reçu une promotion!";
            }
            case 3 -> s = "L'état de la commande avec l'ID " + commandeID + " a changé!";
            case 4 -> s = "Le produit " + produit + " que l'acheteur " + acheteur + " a liké a reçu une promotion!";
            case 5 -> s = "Une solution a été ajouté par " + revendeur + " pour le produit " + produit + "!";
            case 6 -> s = "Une nouvelle commande a été passée pour le produit " + produit + "!";
            case 7 -> s = "Une nouvelle évaluation a été ajoutée pour le produit " + produit + "!";
            case 8 -> s = "L'acheteur " + acheteur + " a signalé un problème pour le produit " + produit + "!";
        }
        return s;
    }
    public String saveFormat() {
        return String.join(",", String.valueOf(type), acheteur, revendeur, produit, String.valueOf(commandeID));
    }
}
