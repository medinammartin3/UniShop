import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
//import java.util.regex.*;

public class Main {

    final static String INITIAL_PATH = "src/";
    final static String DATABASE_PATH = INITIAL_PATH + "Database/";
    final static String PRODUITS_PATH = DATABASE_PATH + "Produits/";
    final static String USERS_PATH = DATABASE_PATH + "Users/";
    final static String ACHETEURS = "Acheteurs/";
    final static String REVENDEURS = "Revendeurs/";
    final static String CSV = ".csv";
    final static String IDS = DATABASE_PATH + "IDs.csv";

    private final static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    private static User connectedUser = null;
    private static Acheteur acheteurConnecte;
    private static Revendeur revendeurConnecte;
    private static short choix;

    public static void main(String[] args) {
        menuPrincipal();
        System.out.println("\nAu plaisir de vous revoir!");
    }
    static void menuPrincipal(){
        while (true) {
            System.out.println("\nBienvenue sur UniShop!");
            choix = selectionChoix(new String[] {"Se connecter", "Créer un compte", "Quitter UniShop"});
            if (choix == 1) {
                connectedUser = connecterUser();
                if (connectedUser == null)
                    continue;
                System.out.print("\nRebonjour " + connectedUser.username + " ! ");
                if (connectedUser.isAcheteur())
                    menuAcheteur();
                else
                    menuRevendeur();
                connectedUser = null;
            }
            else if (choix == 2)
                creerCompte();
            else
                break;
        }
    }
    static short selectionChoix(Object[] choix) {
        int nbChoix = choix.length;
        for (int i = 0; i < nbChoix; ++i) {
            System.out.println(i + 1 + ". " + choix[i]);
        }
        try {
            while (true) {
                System.out.print("Choisir une option: ");
                String reponseS = br.readLine();
                try {
                    short reponse = Short.parseShort(reponseS);
                    if (reponse < nbChoix + 1 && reponse > 0)
                        return reponse;
                    else
                        System.out.println("Choix invalide! Veuillez entrer un choix de 1 à " + nbChoix);
                }
                catch(NumberFormatException e){
                    System.out.println("Veuillez entrer un chiffre de 1 à " + nbChoix);
                }
            }
        }
        catch(IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    static void creerCompte() {
        System.out.println("\nChoisissez le type de compte à créer:");
        choix = selectionChoix(new String[]{"Acheteur", "Revendeur", "Revenir au menu principal"});
        if (choix == 3)
            return;
        System.out.println("Remplissez le formulaire pour créer un compte:");
        System.out.print("Entrez votre username: ");
        try {
            String username = br.readLine();
            List<String> acheteurs = fichiersDansDossier(USERS_PATH + ACHETEURS);
            List<String> revendeurs = fichiersDansDossier(USERS_PATH + REVENDEURS);
            while (acheteurs.contains(username) || revendeurs.contains(username)) {
                System.out.print("Ce Username existe déja. Veuillez entrer un autre Username: ");
                username = br.readLine();
            }
            System.out.print("Entrez votre mot de passe: ");
            String motDePasse = br.readLine();
            System.out.print("Entrez votre courriel: ");
            String courriel = br.readLine();
            System.out.print("Entrez votre téléphone: ");
            long telephone = demanderLong("un téléphone");
            System.out.print("Entrez votre adresse: ");
            String adresse = br.readLine();
            if (choix == 1) {
                String basePath =  USERS_PATH + ACHETEURS + username;
                System.out.print("Entrez votre nom: ");
                String nom = br.readLine();
                System.out.print("Entrez votre prénom: ");
                String prenom = br.readLine();
                if (new File(basePath).mkdir() && new File(basePath + "/Commandes").mkdir()) {
                    String[] infos = new String[] {motDePasse, courriel, "" + telephone , adresse, nom, prenom,
                            "0,0"};
                    ecrireFichierEntier(basePath + "/Infos.csv", String.join(",", infos) + "\n\n\n");
                    ecrireFichierEntier(basePath + "/Panier.csv", "0,0");
                    System.out.println("Inscription du compte acheteur " + username + " réussi!");
                }
                else
                    System.out.println("Erreur lors de la création du dossier. Veuillez recommencer");

            } else {
                String basePath = USERS_PATH + REVENDEURS + username;
                if (new File(basePath).mkdir() && new File(basePath + "/Commandes").mkdir()) {
                    String[] infos = new String[] {motDePasse, courriel, "" + telephone , adresse, "0,0,0" };
                    ecrireFichierEntier(basePath + "/Infos.csv", String.join(",", infos) + "\n\n");
                    System.out.println("Inscription du compte revendeur " + username + " réussi!");
                }
                else
                    System.out.println("Erreur lors de la création du dossier. Veuillez recommencer");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static User connecterUser() {
        try {
            System.out.println("\nConnection (Ne rien rentrer retourne au menu principal): ");
            String categorie = "";
            String username = "";
            while (categorie.isEmpty()) {
                System.out.print("Entrez votre Username: ");
                username = br.readLine();
                if (username.isEmpty())
                    return null;
                File dossier = new File (USERS_PATH + ACHETEURS + username);
                if (dossier.exists()) {
                    categorie = ACHETEURS;
                }
                dossier = new File (USERS_PATH + REVENDEURS + username);
                if (dossier.exists()) {
                    categorie = REVENDEURS;
                }
                if (categorie.isEmpty()) {
                    System.out.println("Username inconnu. Veuillez réessayer");
                }
            }

            String[] data = lireFichierEnEntier(USERS_PATH + categorie + username + "/Infos.csv");
            String[] infos = data[0].split(",");
            String password = infos[0];
            System.out.print("Entrez votre mot de passe: ");
            String passwordEntre = br.readLine();
            if (passwordEntre.isEmpty())
                return null;
            while (!(password.equals(passwordEntre))) {
                System.out.print("Mauvais mot de passe! Veuillez recommencer: ");
                passwordEntre = br.readLine();
            }
            if (categorie.equals(ACHETEURS)){
                return acheteurConnecte = initialiserAcheteur(username);
            }
            else {
                return revendeurConnecte = initialiserRevendeur(username);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    static void menuAcheteur() {
        System.out.println("\nVoici le menu Acheteur:");
        choix = selectionChoix(new String[]{"Accéder aux commandes", "Accéder au panier d'achat", "Voir les billets",
                "Changer les informations du profil", "Rechercher un produit",
                "Rechercher un revendeur", "Gérer les acheteur suivis",
                "Afficher les métriques", "Voir les notifications", "Se déconnecter"});
        switch (choix) {
            case 1 -> gererCommandesAcheteur();
            case 2 -> allerAuPanier();
            case 3 -> gererBilletsAcheteur();
            case 4 -> changerInformations();
            case 5 ->  {
                short c = rechercherProduits();
                while (c == 1)
                    c = rechercherProduits();
                if (c == 2)
                    allerAuPanier();
            }
            case 6 -> rechercherRevendeur();
            case 7 -> gererAcheteursSuivis();
            case 8 -> System.out.println(connectedUser.afficherMetriques());
            case 9 -> System.out.println(afficherNotifications());
            case 10 -> {return;}
        }
        menuAcheteur();
    }
    static void menuRevendeur() {
        System.out.println("\nVoici le menu Revendeur:");
        choix = selectionChoix(new String[]{"Offrir un nouveau produit", "Accéder aux commandes", "Voir les billets",
                "Modifier un produit", "Changer les informations du profil", "Afficher les métriques",
                "Voir les notifications", "Se déconnecter"});
        switch (choix) {
            case 1 -> offrirProduit();
            case 2 -> gererCommandesRevendeur();
            case 3 -> gererBilletsRevendeur();
            case 4 -> modifierProduit();
            case 5 -> changerInformations();
            case 6 -> System.out.println(connectedUser.afficherMetriques());
            case 7 -> System.out.println(afficherNotifications());
            case 8 -> {return;}
        }
        menuRevendeur();
    }
    //Fonctions pour acheteurs
    static short rechercherProduits() {
        System.out.println("\nQuel type de recherche voulez-vous faire?");
        try {
                ArrayList<String> pSelect = new ArrayList<>();
                List<String> produits = fichiersDansDossier(PRODUITS_PATH);
                boolean estRecherche = 1 == selectionChoix(new String[] {"Recherche par mots-clés",
                        "Recherche par filtre"});
                String recherche = "";
                short choixCat = 0;
                boolean estPrixPlusGrand = false;
                float floatDemande = 0;
                int nbLikesDemande = 0;
                if (estRecherche) {
                    System.out.print("Entrez votre recherche: ");
                    recherche = br.readLine();
                }
                else {
                    System.out.println("Choisissez votre type de filtre: ");
                    choix = selectionChoix(new String[] {"Catégorie", "Prix", "Popularité", "Note moyenne",
                            "Promotion"});
                    switch (choix) {
                        case 1 -> {
                            System.out.println("Choisissez une catégorie: ");
                            choixCat = selectionChoix(Categorie.categories);
                            --choixCat;
                        }
                        case 2 -> {
                            System.out.println("Filtrer par plus petit ou plus grand?");
                            estPrixPlusGrand = 1 == selectionChoix(new String[]{"Plus petit", "Plus grand"});
                            System.out.print("Entrez un prix: ");
                            floatDemande = demanderFloat("un prix");
                        }
                        case 3 -> {
                            System.out.print("Entrez un nombre minimum de likes: ");
                            nbLikesDemande = demanderIntPositif("un nombre de likes");
                        }
                        case 4 -> {
                            System.out.print("Entrez une note moyenne minimale: ");
                            floatDemande = demanderFloat("une note");
                        }
                    }
                }

                for(String p : produits) {
                    String[] contenu = lireFichierEnEntier(PRODUITS_PATH + p);
                    String[] infos = contenu[0].split(",");
                    String[] cat = contenu[3].split(",");
                    String produitPreview = String.join(", ", infos[1],
                            Categorie.getCat(Integer.parseInt(cat[0])), "Revendeur: " + infos[0]);
                    if (estRecherche) {
                        if ((infos[1] + infos[2] + cat[1] + cat[2] + cat[3]).contains(recherche))
                            pSelect.add(produitPreview);
                    }
                    else {
                        switch (choix) {
                            case 1 -> {
                                short pCat = Short.parseShort(cat[0]);
                                if (choixCat == pCat)
                                    pSelect.add(produitPreview);
                            }
                            case 2 -> {
                                float prix = Float.parseFloat(infos[3]);
                                if (estPrixPlusGrand) {
                                    if (prix <= floatDemande)
                                        pSelect.add(produitPreview);
                                } else {
                                    if (prix >= floatDemande)
                                        pSelect.add(produitPreview);
                                }
                            }
                            case 3 -> {
                                String[] nbLikesS = contenu[4].split(",");
                                int nbLikes = nbLikesS.length;
                                if (nbLikesS[0].isEmpty())
                                    nbLikes = 0;
                                if (nbLikes >= nbLikesDemande)
                                    pSelect.add(produitPreview);
                            }
                            case 4 -> {
                                float noteMoyenne = Float.parseFloat(infos[6]);
                                if (noteMoyenne >= floatDemande)
                                    pSelect.add(produitPreview);
                            }
                            case 5 -> {
                                if (Integer.parseInt(infos[5]) != 0)
                                    pSelect.add(produitPreview);
                            }
                        }
                    }
                }
                if (pSelect.isEmpty()) {
                    System.out.println("Aucun résultat pour cette recherche. Veuillez réessayer.");
                    rechercherProduits();
                }
                pSelect.add("Faire une nouvelle recherche");
                pSelect.add("Retourner au menu acheteur");
                while (true) {
                    System.out.println("\nChoisissez un produit: ");
                    choix = selectionChoix(pSelect.toArray());
                    if (choix == pSelect.size() - 1)
                        return 1;
                    else if (choix == pSelect.size())
                        return 0;
                    Produit p = initialiserProduit(pSelect.get(choix - 1).split(",")[0]);
                    System.out.println("\n" + p.getFormatDisplay());
                    while (true) {
                        System.out.println("\nQue voulez-vous faire ensuite?");
                        choix = selectionChoix(new String[] {"Liker le produit", "Ajouter le produit au panier",
                                "Regarder les évaluations", "Écrire une évaluation", "Voir les likes",
                                "Retourner au résultat de la recherche" });
                        if (choix == 6)
                            break;
                        switch (choix) {
                            case 1 -> System.out.println("\n" + p.liker(connectedUser.username));
                            case 2 -> {
                                if (p.getQuantite() == 0) {
                                    System.out.println("\nLe produit " + p.titre + " doit être restocké.");
                                    continue;
                                }
                                acheteurConnecte.panier.addProduit(p);
                                System.out.println("\nVous avez ajouté " + p.titre + " au panier!");
                                System.out.println("\nVoulez-vous allez au panier?");
                                if (1 == selectionChoix(new String[] {"Oui", "Non"}))
                                    return 2;
                            }
                            case 3 -> System.out.println("\n" + p.getEvaluationsDisplay());
                            case 4 -> {
                                if (acheteurConnecte.aAcheteProduit(p.titre)) {
                                    ecrireEvaluation(p);
                                }
                                else {
                                    System.out.println("\nVous devez acheter ce produit avant de l'évaluer!");
                                }
                            }
                            case 5 -> {
                                ArrayList<String> ar = p.voirLikes();
                                ar.add("Retourner au produit");
                                String[] as = ar.toArray(new String[0]);
                                if (as.length == 1) {
                                    System.out.println("\nCe produit n'a aucun likes.");
                                }
                                else {
                                    System.out.println("\nChoisir un acheteur à suivre: ");
                                    short c = selectionChoix(as);
                                    if (c == as.length)
                                        continue;
                                    System.out.println("\n" + acheteurConnecte.suivre(p.getLike(c - 1)));
                                }
                            }
                        }
                    }
                }
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Quelque chose s'est mal passé. Veuillez réessayer.");
            rechercherProduits();
        }
        return 0;

    }
    static void gererCommandesAcheteur() {
        System.out.println("\nChoisissez une commande: ");
        ArrayList<Commande> cmds = acheteurConnecte.getCommandes();
        String[] cs = new String[cmds.size() + 1];
        for (int i = 0; i < cmds.size(); ++i) {
            cs[i] = cmds.get(i).getMenuDisplay();
        }
        cs[cmds.size()] = "Retourner au menu";
        choix = selectionChoix(cs);
        if (choix == cs.length)
            return;
        Commande cmd = cmds.get(choix - 1);
        System.out.println("\nCommande #" + cmd.getId());
        System.out.println(cmd.afficher());
        System.out.println("Votre commande est " + cmd.getEtat() + ".");
        while (true) {
            System.out.println("\nChoisissez une action: ");
            choix = selectionChoix(new String[]{"Confirmer la livraison", "Retourner un produit", "Évaluer un produit",
                    "Retourner au menu"});
            switch (choix) {
                case 1 -> {
                    switch (cmd.confirmerLivraison()) {
                        case 0 -> {
                            System.out.println("\nL'état de votre commande a été changé avec succès!");
                            cmd.saveAfter(USERS_PATH + ACHETEURS + connectedUser.username + "/Commandes/");
                            //TODO Update commande revendeur
                        }
                        case 1 -> System.out.println("\nVotre commande est toujours en production.");
                        case 2 -> System.out.println("\nVous avez déjà confirmé la livraison de cette commande!");

                    }
                }
                case 2 -> {
                    Produit p = cmd.getChoixProduit(true);
                    if (p == null)
                        continue;
                    else if (acheteurConnecte.billetExiste(p.getId())) {
                        System.out.println("\nVous avez déjà fait un billet pour ce produit!");
                        continue;
                    }
                    System.out.print("Quel est le problème avec ce produit? ");
                    try {
                        String a = br.readLine();
                        System.out.println("Voulez-vous effectuer un retour ou un échange?");
                        boolean estRetour = 1 == selectionChoix(new String[] {"Retour", "Échange"});
                        Revendeur r = initialiserRevendeur(p.nomReven);
                        String produitRempla = "";
                        if (!estRetour) {
                            produitRempla = r.getProduitAvecChoix().titre;
                        }
                        Billet b = new Billet(p.getId(), connectedUser.username, p.titre, a, estRetour, false,
                                "", produitRempla, false);
                        acheteurConnecte.addBillet(b);
                        r.addBillet(b);
                        //TODO créer nouvelle commande pour échange
                        System.out.println("\nVotre demande " + (b.estRetour ? "de retour" : "d'échange") + " a été " +
                                "traitée avec succès!");
                        System.out.println("Votre ID pour ce billet est: " + p.getId());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                case 3 -> {
                    Produit p = cmd.getChoixProduit(true);
                    if (p == null)
                        continue;
                    ecrireEvaluation(p);
                }
                case 4 -> {
                    return;
                }
            }
        }
    }
    static void allerAuPanier() {
        if (acheteurConnecte.panier.estVide()) {
            System.out.println("\nVotre panier est vide!");
            return;
        }

        System.out.println("\nVoici votre panier:");
        System.out.println(acheteurConnecte.panier.afficher());
        System.out.println("\nChoisissez une action: ");
        choix = selectionChoix(new String[] {"Enlever un produit", "Passer la commande", "Retourner au menu"});
        switch (choix) {
            case 1 -> {
                Produit p = acheteurConnecte.panier.getChoixProduit(false);
                acheteurConnecte.panier.removeProduit(p);
                System.out.println("\nVous avez enlevé " + p.titre + " du panier.");
                allerAuPanier();
            }
            case 2 -> {
                try {
                    System.out.println("Voulez-vous utiliser la même adresse que votre compte?");
                    String adresse = connectedUser.address;
                    if (2 == selectionChoix(new String[] {"Oui", "Non"})) {
                        System.out.print("Entrez une nouvelle adresse: ");
                        adresse = br.readLine();
                    }
                    Commande c = acheteurConnecte.panier.passerCommande(USERS_PATH + ACHETEURS +
                            connectedUser.username + "/Commandes", adresse);
                    acheteurConnecte.ajouterCommande(c.copy());
                    acheteurConnecte.panier.vider();
                    System.out.println("\nVotre commande a été passée avec succès!");
                    System.out.println("Votre identifiant de commanque unique est: " + c.getId());
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    static void gererBilletsAcheteur() {
        ArrayList<Billet> ba = acheteurConnecte.getBillets();
        if (ba.isEmpty()) {
            System.out.println("\nVous n'avez aucun billet!");
            return;
        }
        while (true) {
            System.out.println("\nChoissisez un billet: ");
            String[] bs = new String[ba.size() + 1];
            for(int i = 0; i < ba.size(); ++i) {
                bs[i] = ba.get(i).afficherMenu();
            }
            bs[bs.length - 1] = "Retour au menu";
            choix = selectionChoix(bs);
            if (choix == bs.length)
                return;
            Billet b = ba.get(choix - 1);
            System.out.println("\n" + b.afficher());
            if (!b.estRetour && !b.isRemplaLivre()) {
                System.out.println("\nQue voulez-vous faire ensuite?");
                choix = selectionChoix(new String[] {"Confirmer l'arrivée du produit de remplacement",
                        "Retourner aux billets", "Retourner au menu"});
                if (choix == 1) {
                    if (b.comfirmerLivraisonRempla()) {
                        try {
                            Revendeur r = initialiserRevendeur(initialiserProduit(b.produitInitial).nomReven);
                            r.trouverBillet(b.id).comfirmerLivraisonRempla();
                            r.save();
                            acheteurConnecte.save();
                            System.out.println("\nVous avez confirmé la livraison du produit de remplacement!");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                        System.out.println("\nVous avez déjà confirmé la réception du produit de remplacement!");
                }
                else if (choix == 3)
                    return;
            }
        }

    }
    static void changerInformations() {
        System.out.println("\nTODO");
    }
    static String afficherNotifications() {
        return "\nTODO";
    }
    static void gererAcheteursSuivis() { System.out.println("\nTODO"); }
    static void rechercherRevendeur() {
        System.out.println("\nTODO");
    }
    //Fonctions pour revendeurs
    static void offrirProduit() {
        System.out.println("\nVeuillez remplir les informations concernant votre produit.");
        System.out.println("Commencez par choisir une catégorie:");
        choix = selectionChoix(Categorie.categories);
        try {
            System.out.print("Quel est le titre de votre produit? ");
            String titre = br.readLine();
            List<String> titres = fichiersDansDossier(PRODUITS_PATH);
            while (titres.contains(titre + CSV)) {
                System.out.print("Ce nom de produit existe déjà. Veuillez en entrer un autre: ");
                titre = br.readLine();
            }
            System.out.print("Veuillez entrer une description: ");
            String description = br.readLine();

            System.out.print("Veuillez entrer des liens pour des images (Séparés pas des \",\"): ");
            String[] images = br.readLine().split(",");
            System.out.print("Veuillez entrer des liens pour des vidéos (Séparés pas des \",\"): ");
            String[] videos = br.readLine().split(",");
            Categorie c = null;
            switch(choix) {
                case 1 -> c = offrirLivre();
                case 2 -> c = offrirRessource();
                case 3 -> c = offrirPapeterie();
                case 4 -> c = offrirInfo();
                case 5 -> c = offrirBureau();
            }
            System.out.print("Veuillez entrer un prix: ");
            float prix = demanderFloat("un prix");
            System.out.println("Voulez vous offrir une promotion en points?");
            int points = 0;
            if(1 == selectionChoix(new String[] {"Oui", "Non"})) {
                System.out.print("Entrez le nombre de points: ");
                points = demanderIntPositif("un nombre de points");
            }
            System.out.print("Veuillez entrer une quantité initiale à mettre dans l'inventaire: ");
            int quantite = demanderIntPositif("une quantité");
            Produit p = new Produit(connectedUser.username, titre, description, prix, quantite, points, images, videos,
                    c, new ArrayList<>(), new ArrayList<>());
            p.save();
            revendeurConnecte.ajouterProduit(p);
            System.out.println("Votre nouveau produit " + titre + " a été ajouté avec succès!");
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Quelque chose s'est mal passé. Veuillez réessayer.");
            offrirProduit();
        }
    }
    static Categorie offrirLivre() throws IOException {
        System.out.println("Veuillez choisir le genre de votre livre:");
        String genre = CLivres.genres[selectionChoix(CLivres.genres) - 1];
        System.out.print("Entrez le ISBN: ");
        long isbn = demanderLong("un ISBN");
        System.out.print("Entrez l'auteur: ");
        String auteur = br.readLine();
        System.out.print("Entrez la maison d'édition: ");
        String maison = br.readLine();
        System.out.print("Entrez la date de parution (JJ/MM/AAAA): ");
        String date = br.readLine();
        System.out.print("Entrez le numéro d'édition: ");
        int numEdition = demanderIntPositif("un numéro d'édition");
        System.out.print("Entrez le numéro de volume: ");
        int numVolume = demanderIntPositif("un numéro de volume");
        return new CLivres(auteur, maison, genre, isbn, date, numEdition, numVolume);

    }
    static Categorie offrirRessource() throws IOException{
        System.out.println("Est-ce un produit en ligne ou imprimé?");
        String type = CRessources.types[selectionChoix(CRessources.types) - 1];
        System.out.print("Entrez le ISBN: ");
        long isbn = demanderLong("un ISBN");
        System.out.print("Entrez l'auteur: ");
        String auteur = br.readLine();
        System.out.print("Entrez l'organisation: ");
        String organisation = br.readLine();
        System.out.print("Entrez la date de parution (JJ/MM/AAAA): ");
        String date = br.readLine();
        System.out.print("Entrez le numéro d'édition: ");
        int numEdition = demanderIntPositif("un numéro d'édition");
        return new CRessources(auteur, organisation, type, isbn, date, numEdition);
    }
    static Categorie offrirPapeterie() throws IOException {
        System.out.println("Veuillez choisir une sous-catégorie: ");
        String sousCat = CPapeterie.sousCats[selectionChoix(CPapeterie.sousCats) - 1];
        System.out.print("Entrez la marque: ");
        String marque = br.readLine();
        System.out.print("Entrez le modèle: ");
        String modele = br.readLine();
        return new CPapeterie(marque, modele, sousCat);
    }
    static Categorie offrirInfo() throws IOException {
        System.out.println("Veuillez choisir une sous-catégorie: ");
        String sousCat = CInformatique.sousCats[selectionChoix(CInformatique.sousCats) - 1];
        System.out.print("Entrez la marque: ");
        String marque = br.readLine();
        System.out.print("Entrez le modèle: ");
        String modele = br.readLine();
        System.out.print("Entrez la date de parution (JJ/MM/AAAA): ");
        String date = br.readLine();
        return new CInformatique(marque, modele, sousCat, date);
    }
    static Categorie offrirBureau() throws IOException {
        System.out.println("Veuillez choisir une sous-catégorie: ");
        String sousCat = CBureau.sousCats[selectionChoix(CBureau.sousCats) - 1];
        System.out.print("Entrez la marque: ");
        String marque = br.readLine();
        System.out.print("Entrez le modèle: ");
        String modele = br.readLine();
        return new CBureau(marque, modele, sousCat);
    }
    static void gererCommandesRevendeur() {
        System.out.println("\nTODO");
    }
    static void gererBilletsRevendeur() {
        ArrayList<Billet> ba = revendeurConnecte.getBillets();
        if (ba.isEmpty()) {
            System.out.println("\nVous n'avez aucun billet!");
            return;
        }
        while (true) {
            System.out.println("\nChoissisez un billet: ");
            String[] bs = new String[ba.size() + 1];
            for(int i = 0; i < ba.size(); ++i) {
                bs[i] = ba.get(i).afficherMenu();
            }
            bs[bs.length - 1] = "Retour au menu";
            choix = selectionChoix(bs);
            if (choix == bs.length)
                return;
            Billet b = ba.get(choix - 1);
            System.out.println("\n" + b.afficher());
            if (!b.comfirmerLivraisonInitial() && !b.pasDeSolution())
                continue;
            System.out.println("\nQue voulez-vous faire?");
            choix = selectionChoix(new String[] {"Donner une solution", "Confirmer l'arrivée du produit problématique",
                    "Retourner au menu"});
            if (choix == 1) {
                if (b.pasDeSolution()) {
                    System.out.print("Entrez votre solution: ");
                    try {
                        String solution = br.readLine();
                        b.setProbRev(solution);
                        Acheteur a = initialiserAcheteur(b.nomAche);
                        a.trouverBillet(b.id).setProbRev(solution);
                        a.save();
                        revendeurConnecte.save();
                        System.out.println("\nVous avez ajouté une solution au billet!");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    System.out.println("\nVous aviez déjà ajouté une solution à ce billet!");
                }
            }
            else if (choix == 2) {
                if (b.comfirmerLivraisonInitial()) {
                    try {
                        Acheteur a = initialiserAcheteur(b.nomAche);
                        a.trouverBillet(b.id).comfirmerLivraisonInitial();
                        a.save();
                        revendeurConnecte.save();
                        System.out.println("\nVous avez confirmer la livraison du produit problématique à l'entrepôt!");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                    System.out.println("\nVous aviez déjà confirmé la réception du produit problématique pour ce " +
                            "billet!");
            }
            else
                break;
        }

    }
    static void modifierProduit () {
        System.out.println("\nTODO");
    }
    static void ecrireEvaluation (Produit p) {
        System.out.println("\nÉvaluation du produit " + p.titre + ":");
        System.out.print("Veuillez entrer une note entière entre 1 et 5: ");
        try {
            int note;
            note = demanderIntPositif("une note");
            while (note > 5 || note == 0) {
                System.out.print("Veuillez entrer une note plus petite ou égale à 5: ");
                note = demanderIntPositif("une note");
            }
            System.out.print("Entrez un commentaire sur le produit: ");
            String comment = br.readLine();
            p.addEvaluation(new Evaluation(connectedUser.username, note, comment));
            System.out.println("\nVotre évaluation a été écrite avec succès!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static Acheteur initialiserAcheteur(String username) throws IOException {
        String path = USERS_PATH + ACHETEURS + username + "/";
        String[] data = lireFichierEnEntier( path+ "Infos.csv");
        String[] infos = data[0].split(",");
        ArrayList<String> as =  iniArrayList(data[1]);
        ArrayList<String> rl =  iniArrayList(data[2]);
        ArrayList<Billet> bis = new ArrayList<>();
        for (int i = 3; i < data.length; ++i) {
            String[] bs = data[i].split(",");
            bis.add(new Billet(Integer.parseInt(bs[0]), bs[1], bs[2], bs[3], Boolean.parseBoolean(bs[4]),
                     Boolean.parseBoolean(bs[5]) , bs[6], bs[7], Boolean.parseBoolean(bs[8])));
        }
        String[] s = lireFichierEnEntier(path +"Panier.csv");
        String[] i = s[0].split(",");
        float coutT = Float.parseFloat(i[0]);
        int pts = Integer.parseInt(i[1]);
        Commande panier = new Commande((short) 0, coutT, pts);
        for (int j = 1; j < s.length; ++j) {
            Produit p = initialiserProduit(s[j]);
            panier.addInitial(p);
        }

        ArrayList<Commande> cmds = new ArrayList<>();
        String commandesPath = path + "Commandes/";
        for (String l : fichiersDansDossier(commandesPath)) {
            String[] all = lireFichierEnEntier(commandesPath + l);
            String[] fstLine = all[0].split(",");
            Commande c = new Commande(Short.parseShort(fstLine[2]), Float.parseFloat(fstLine[3]),
                    Integer.parseInt(fstLine[4]));
            c.addPastInfo(Integer.parseInt(fstLine[0]), fstLine[1], fstLine[5]);
            for (int j = 1; j < all.length; ++j) {
                String[] line = all[j].split(",");
                Produit p = initialiserProduit(line[0]);
                p.setUniqueId(Integer.parseInt(line[1]));
                c.addInitial(p);
            }
            cmds.add(c);
        }
        return new Acheteur(username, infos[0], infos[1], Long.parseLong(infos[2]),
                infos[3], infos[4], infos[5], Integer.parseInt(infos[6]), Integer.parseInt(infos[7]), as, rl, bis,
                panier, cmds);
    }
    static Revendeur initialiserRevendeur(String username) throws IOException {
        String path = USERS_PATH + REVENDEURS + username + "/";
        String[] data = lireFichierEnEntier( path+ "Infos.csv");
        String[] infos = data[0].split(",");
        ArrayList<String> followers = iniArrayList(data[1]);
        ArrayList<Billet> bis = new ArrayList<>();
        for (int i = 2; i < data.length; ++i) {
            String[] bs = data[i].split(",");
            bis.add(new Billet(Integer.parseInt(bs[0]), bs[1], bs[2], bs[3], Boolean.parseBoolean(bs[4]),
                    Boolean.parseBoolean(bs[5]) , bs[6], bs[7], Boolean.parseBoolean(bs[8])));
        }
        ArrayList<Produit> ps = new ArrayList<>();
        for(String pc : fichiersDansDossier(PRODUITS_PATH)){
            String r = lireFichierEnEntier(PRODUITS_PATH + pc)[0].split(",")[0];
            if (r.equals(username))
                ps.add(initialiserProduit(pc));
        }
        return new Revendeur(username, infos[0], infos[1], Long.parseLong(infos[2]), infos[3],
                Float.parseFloat(infos[4]), Integer.parseInt(infos[5]), followers, bis, ps, new ArrayList<>());
    }
    static Produit initialiserProduit(String titreProduit) throws IOException{
        String path = PRODUITS_PATH + titreProduit;
        if (!titreProduit.endsWith(CSV))
            path += CSV;
        String[] s = lireFichierEnEntier(path);
        String[] f = s[0].split(",");
        String[] images = s[1].split(",");
        String[] videos = s[2].split(",");
        String[] cs = s[3].split(",");
        Categorie c = null;
        switch (Short.parseShort(cs[0])) {
            case 0 -> c = new CLivres(cs[1], cs[2], cs[3], Long.parseLong(cs[4]), cs[5], Integer.parseInt(cs[6]),
                    Integer.parseInt(cs[7]));
            case 1 -> c = new CRessources(cs[1], cs[2], cs[3], Long.parseLong(cs[4]), cs[5], Integer.parseInt(cs[6]));
            case 2 -> c = new CPapeterie(cs[1], cs[2], cs[3]);
            case 3 -> c = new CInformatique(cs[1], cs[2], cs[3], cs[4]);
            case 4 -> c = new CBureau(cs[1], cs[2], cs[3]);
        }
        ArrayList<String> likes =  iniArrayList(s[4]);
        Evaluation[] evals = new Evaluation[s.length - 5];
        for (int i = 5; i < s.length; ++i) {
            String[] e = s[i].split(",");
            evals[i - 5] = new Evaluation(e[0], Byte.parseByte(e[1]), e[2]);
        }
        ArrayList<Evaluation> evalsL = new ArrayList<>(Arrays.asList(evals));
        return new Produit(f[0], f[1], f[2], Float.parseFloat(f[3]), Integer.parseInt(f[4]), Integer.parseInt(f[5]),
                images, videos, c, likes, evalsL);
    }
    static int demanderIntPositif(String demande) throws IOException {
        int i;
        while (true) {
            try {
                i = Integer.parseInt(br.readLine());
                if (i >= 0)
                    return i;
                else {
                    System.out.print("Veuillez entrer un entier positif: ");
                }
            }
            catch (NumberFormatException e) {
                System.out.print("Veuillez entrer " + demande + " valide: ");
            }
        }
    }
    static long demanderLong(String demande) throws IOException {
        long l;
        while (true) {
            try {
                l = Long.parseLong(br.readLine());
                return l;
            }
            catch (NumberFormatException e) {
                System.out.print("Veuillez entrer " + demande + " valide: ");
            }
        }
    }
    static float demanderFloat(String demande) throws IOException {
        float prix;
        while (true) {
            try {
                prix = arrondirPrix(Float.parseFloat(br.readLine()));
                if (prix >= 0)
                    return arrondirPrix(prix);
                else
                    System.out.print("Veuillez entrer un chiffre à virgule positif: ");
            }
            catch (NumberFormatException e) {
                System.out.print("Veuillez entrer " + demande + " valide (Nombre à virgule): ");
            }
        }
    }
    static float arrondirPrix(float prix) {
        return Math.round((prix) * 100) / 100f;
    }

    static String[] lireFichierEnEntier(String path) throws IOException {
        return Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8).toArray(new String[0]);
    }
    static void ecrireFichierEntier(String path, String toWrite) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(path));
            bw.write(toWrite);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static List<String> fichiersDansDossier(String path) {
        return Arrays.asList(Objects.requireNonNull(new File(path).list()));
    }
    static ArrayList<String> iniArrayList(String s) {
        String[] tab = s.split(",");
        if (tab[0].isEmpty())
            return new ArrayList<>(Arrays.asList(tab).subList(1, tab.length));
        else
            return new ArrayList<>(Arrays.asList(tab));

    }
    public static String getConnectedUsername() {
        return connectedUser.username;
    }
}