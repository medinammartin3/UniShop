package unishop;

import unishop.Categories.*;
import unishop.Users.Acheteur;
import unishop.Users.Revendeur;


import java.util.ArrayList;
import java.util.List;

import static unishop.Main.*;

/**
 * Cette méthode émule les contrôles des revendeurs
 */
public class ControleurRevendeur {
    private static short choix;
    private static Revendeur revendeur;

    /**
     * Cette méthode émule le menu d'un revendeur
     * @param r Le revendeur dont on aimerait émuler le menu
     */
    static void menuRevendeur(Revendeur r) {
        revendeur = r;
        while (true) {
            System.out.println("\nVoici le menu Revendeur:");
            choix = selectionChoix(new String[]{"Offrir un nouveau produit", "Accéder aux commandes", "Voir les billets",
                    "Gérer les produits", "Changer les informations du profil", "Afficher les métriques",
                    "Voir les notifications", "Se déconnecter"});
            switch (choix) {
                case 1 -> offrirProduit();
                case 2 -> gererCommandes();
                case 3 -> gererBillets();
                case 4 -> modifierProduit();
                case 5 -> changerInformations();
                case 6 -> System.out.println(revendeur.afficherMetriques());
                case 7 -> System.out.println("\n" + revendeur.voirNotifications());
                case 8 -> {return;}
            }
        }
    }
    /**
     * Cette méthode permet au revendeur de offrir un produit
     */
    static void offrirProduit() {
        System.out.println("\nVeuillez remplir les informations concernant votre produit.");
        System.out.println("Commencez par choisir une catégorie:");
        choix = selectionChoix(Categorie.categories);
        System.out.print("Quel est le titre de votre produit? (Ne rien mettre pour retourner au menu): ");
        String titre = demanderString();
        if (titre.isEmpty())
            return;
        List<String> titres = fichiersDansDossier(PRODUITS_PATH);
        while (titres.contains(titre + CSV)) {
            System.out.print("Ce nom de produit existe déjà. Veuillez en entrer un autre: ");
            titre = demanderString();
        }
        System.out.print("Veuillez entrer une description: ");
        String description = demanderString();

        System.out.print("Veuillez entrer des liens pour des images (Séparés pas des \",\"): ");
        String[] images = demanderString().split(",");
        System.out.print("Veuillez entrer des liens pour des vidéos (Séparés pas des \",\"): ");
        String[] videos = demanderString().split(",");
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
        int points = (int) Math.floor(prix);
        int pointsMax = (int) Math.floor(prix * 19);
        if(choixOuiNon()) {
            System.out.print("Entrez un nombre de points (Plus petit ou égal à " + pointsMax + "): ");
            int pts = demanderIntPositif("un nombre de points");
            while (pts > pointsMax) {
                System.out.print("Vous avez entré un nombre de points trop grand! Veuillez réessayer: ");
                pts = demanderIntPositif("un nombre de points");
            }
            points += pts;
        }
        System.out.print("Veuillez entrer une quantité initiale à mettre dans l'inventaire: ");
        int quantite = demanderIntPositif("une quantité");
        Produit p = new Produit(revendeur.getUsername(), titre, description, prix, quantite, points, images, videos,
                c, new ArrayList<>(), new ArrayList<>());
        p.save();
        revendeur.ajouterProduit(p);
        if (c != null)
            revendeur.ajouterCatVendu(c.getCat());
        System.out.println("Votre nouveau produit " + titre + " a été ajouté avec succès!");

        for (String a : revendeur.getFollowers()){
            Acheteur acheteur = initialiserAcheteur(a);
            acheteur.addNotifications(new Notification(1, a, revendeur.getUsername(), p.getTitre(), 0));
        }
    }
    /**
     * Cette méthode permet aux revendeurs d'offrir un livre
     * @return Le livre que l'on souhaite offrir
     */
    static Categorie offrirLivre() {
        System.out.println("Veuillez choisir le genre de votre livre:");
        String genre = CLivres.genres[selectionChoix(CLivres.genres) - 1];
        System.out.print("Entrez le ISBN: ");
        long isbn = demanderLong("un ISBN");
        System.out.print("Entrez l'auteur: ");
        String auteur = demanderString();
        System.out.print("Entrez la maison d'édition: ");
        String maison = demanderString();
        System.out.print("Entrez la date de parution (JJ/MM/AAAA): ");
        String date = demanderString();
        System.out.print("Entrez le numéro d'édition: ");
        int numEdition = demanderIntPositif("un numéro d'édition");
        System.out.print("Entrez le numéro de volume: ");
        int numVolume = demanderIntPositif("un numéro de volume");
        return new CLivres(auteur, maison, genre, isbn, date, numEdition, numVolume);

    }

    /**
     * Cette méthode permet d'offrir une ressource
     * @return  La ressource que l'on souhaite offrir
     */
    static Categorie offrirRessource() {
        System.out.println("Est-ce un produit en ligne ou imprimé?");
        String type = CRessources.types[selectionChoix(CRessources.types) - 1];
        System.out.print("Entrez le ISBN: ");
        long isbn = demanderLong("un ISBN");
        System.out.print("Entrez l'auteur: ");
        String auteur = demanderString();
        System.out.print("Entrez l'organisation: ");
        String organisation = demanderString();
        System.out.print("Entrez la date de parution (JJ/MM/AAAA): ");
        String date = demanderString();
        System.out.print("Entrez le numéro d'édition: ");
        int numEdition = demanderIntPositif("un numéro d'édition");
        return new CRessources(auteur, organisation, type, isbn, date, numEdition);
    }
    /**
     * Cette méthode permet d'offrir un produit de papeterie
     * @return  Un produit de papeterie que l'on souhaite offrir
     */
    static Categorie offrirPapeterie() {
        System.out.println("Veuillez choisir une sous-catégorie: ");
        String sousCat = CPapeterie.sousCats[selectionChoix(CPapeterie.sousCats) - 1];
        System.out.print("Entrez la marque: ");
        String marque = demanderString();
        System.out.print("Entrez le modèle: ");
        String modele = demanderString();
        return new CPapeterie(marque, modele, sousCat);
    }
    /**
     * Cette méthode permet de offrir un produit informatique
     * @return  Le produit informatique que l'on souhaite offrir
     */
    static Categorie offrirInfo() {
        System.out.println("Veuillez choisir une sous-catégorie: ");
        String sousCat = CInformatique.sousCats[selectionChoix(CInformatique.sousCats) - 1];
        System.out.print("Entrez la marque: ");
        String marque = demanderString();
        System.out.print("Entrez le modèle: ");
        String modele = demanderString();
        System.out.print("Entrez la date de parution (JJ/MM/AAAA): ");
        String date = demanderString();
        return new CInformatique(marque, modele, sousCat, date);
    }
    /**
     * Cette méthode permet de offrir un produit de bureau
     * @return  Le produit de bureau que l'on souhaite offrir
     */
    static Categorie offrirBureau() {
        System.out.println("Veuillez choisir une sous-catégorie: ");
        String sousCat = CBureau.sousCats[selectionChoix(CBureau.sousCats) - 1];
        System.out.print("Entrez la marque: ");
        String marque = demanderString();
        System.out.print("Entrez le modèle: ");
        String modele = demanderString();
        return new CBureau(marque, modele, sousCat);
    }
    /**
     * Cette méthode permet aux revendeurs  de gérer leurs commandes
     */
    static void gererCommandes() {
        ArrayList<Commande> cmds = revendeur.getCommandes();
        if (cmds.isEmpty()) {
            System.out.println("\nVous n'avez aucunes commandes!");
            return;
        }
        System.out.println("\nChoisissez une commande: ");
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
        if (cmd.estEnProduction()) {
            System.out.println("\nChoisissez une action: ");
            choix = selectionChoix(new String[]{"Changer l'état de la commande",  "Retourner au menu"});
            if (choix == 1) {
                cmd.mettreEnLivraison();
                System.out.println("\nLa commande #" + cmd.getId() + " est maintenant en livraison!");
            }
        }
    }

    /**
     * Cette méthode permet aux revendeurs de gérer leurs billets
     */
    static void gererBillets() {
        ArrayList<Billet> ba = revendeur.getBillets();
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
            if (b.isIniLivre() && b.aSolution())
                continue;
            System.out.println("\nQue voulez-vous faire?");
            choix = selectionChoix(new String[] {"Donner une solution", "Confirmer l'arrivée du produit problématique",
                    "Retourner au menu"});
            if (choix == 1) {
                if (!b.aSolution()) {
                    System.out.print("Entrez votre solution: ");
                    String solution = demanderString();
                    b.setProbRev(solution);
                    Acheteur a = initialiserAcheteur(b.nomAche);
                    a.addNotifications(new Notification(5, a.getUsername(), revendeur.getUsername(),
                            b.produitInitial, 0));
                    System.out.println("\nVous avez ajouté une solution au billet!");
                }
                else {
                    System.out.println("\nVous avez déjà ajouté une solution à ce billet!");
                }
            }
            else if (choix == 2) {
                if (b.comfirmerLivraisonInitial()) {
                    System.out.println("\nVous avez confirmer la livraison du produit problématique à l'entrepôt!");
                }
                else
                    System.out.println("\nVous avez déjà confirmé la réception du produit problématique pour ce " +
                            "billet!");
            }
            else
                break;
        }

    }

    /**
     * Cette méthode permet aux revendeurs de modifier un produit
     */
    static void modifierProduit () {
        if (revendeur.nbProduitsOfferts() == 0) {
            System.out.println("\nVous n'avez aucun produit en vente!");
            return;
        }
        while (true) {
            Produit p = choisirProduit(revendeur.getProduits());
            if (p == null)
                return;
            System.out.println(p.getFormatDisplay());
            System.out.println("\n" + p.getEvaluationsDisplay());
            System.out.println("\nChoisir une option: ");
            choix = selectionChoix(new String[] {"Restocker un produit", "Gérer une promotion", "Ajouter des médias",
                    "Retourner au menu"});
            if (choix == 4)
                return;

            switch (choix) {
                case 1 -> {
                    System.out.print("Entrez la quantité que vous voulez ajouter à l'inventaire: ");
                    p.restocker(demanderIntPositif("une quantité"));
                    System.out.println("\nVous avez maintenant " + p.getQuantite() + " " + p.getTitre() +
                            " en inventaire!");
                }
                case 2 -> {
                    while (true) {
                        System.out.println("\nQue voulez-vous faire avec la promotion?");
                        choix = selectionChoix(new String[] {"Enlever la promotion", "Modifier la promotion", "" +
                                "Retourner au menu"});
                        if (choix == 3)
                            break;
                        if (choix == 1) {
                            if (p.estEnPromotion()) {
                                p.enleverPromotion();
                                System.out.println("\nVous avez enlevé la promotion de " + p.getTitre() + "!");
                            }
                            else
                                System.out.println("\nCe produit n'est pas en promotion!");

                        }
                        else {
                            int pointsMax = (int) Math.floor(p.getPrix() * 19);
                            System.out.print("Entrez un nombre de points (Plus petit ou égal à " + pointsMax + "): ");
                            int pts = demanderIntPositif("un nombre de points");
                            while (pts > pointsMax || pts == 0) {
                                System.out.print("Vous avez entré un nombre de points invalide! " +
                                        "Veuillez réessayer: ");
                                pts = demanderIntPositif("un nombre de points");
                            }
                            p.changerPromotion((int)Math.floor(p.getPrix()) + pts);
                            for (String a : revendeur.getFollowers()) {
                                Acheteur acheteur = initialiserAcheteur(a);
                                acheteur.addNotifications(new Notification(2, a, revendeur.getUsername(),
                                        "", 0));
                            }
                            for(String a : p.getLikes()) {
                                Acheteur acheteur = initialiserAcheteur(a);
                                acheteur.addNotifications(new Notification(2, a, "", p.getTitre(),
                                        -1));
                                for (String follower : acheteur.getFollowers()) {
                                    Acheteur f = initialiserAcheteur(follower);
                                    f.addNotifications(new Notification(4, a, "", p.getTitre(),
                                            -1));
                                }
                            }
                            System.out.println("\n" + p.getTitre() + " a maintenant une promotion de " + pts +
                                    " points!");
                        }
                    }
                }
                case 3 -> {
                    System.out.println("Voulez-vous ajouter des images ou des vidéos?");
                    if (1 == selectionChoix(new String[] {"Images", "Vidéos"})) {
                        System.out.print("Entrer les liens vers les images séparés par des virgules: ");
                        String[] imgs = demanderString().split(",");
                        p.ajouterImages(imgs);
                    }
                    else {
                        System.out.print("Entrer les liens vers les vidéos séparés par des virgules: ");
                        String[] vids = demanderString().split(",");
                        p.ajouterVideos(vids);
                    }
                }
            }
        }
    }

    /**
     * Cette méthode permet aux revendeurs de changer les informations de leur profil
     */
    static void changerInformations() {
        System.out.println("\nChoississez ce que vous voulez modifier: ");
        choix = selectionChoix(new String[]{"Mot de passe", "Adresse courriel", "Téléphone", "Adresse",
                "Retourner au menu"});
        switch (choix) {
            case 1 -> {
                System.out.print("Entrez un nouveau mot de passe: ");
                String password = demanderString();
                revendeur.setPassword(password);
                System.out.println("\nVous avez changé votre mot de passe!");
            }
            case 2 -> {
                System.out.print("Entrez une nouvelle adresse de courriel: ");
                String email = demanderString();
                revendeur.setEmail(email);
                System.out.println("\nVous avez changé votre adresse courriel!");
            }
            case 3 -> {
                System.out.print("Entrez un nouveau numéro de téléphone: ");
                long telephone = demanderLong("un numéro");
                revendeur.setPhone(telephone);
                System.out.println("\nVous avez changé votre numéro de téléphone!");
            }
            case 4 -> {
                System.out.print("Entrez une nouvelle adresse: ");
                String adresse = demanderString();
                revendeur.setAddress(adresse);
                System.out.println("\nVous avez changé votre adresse!");
            }
        }
    }
}