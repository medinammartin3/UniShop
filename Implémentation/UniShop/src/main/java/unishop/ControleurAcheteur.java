package unishop;

import unishop.Categories.Categorie;
import unishop.Users.Acheteur;
import unishop.Users.Revendeur;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static unishop.Main.*;
/**
 * Cette classe émule les commandes possibles par les acheteurs
 */
public class ControleurAcheteur {
    private static short choix;
    private static Acheteur acheteur;

    /**
     * Émule un menu pour pour l'acheteur passé en argument
     * @param a L'acheteur pour lequel on veut émuler le menu
     */
    static void menuAcheteur(Acheteur a)  {
        acheteur = a;
        while (true) {
            System.out.println("\nVoici le menu Acheteur:");
            choix = selectionChoix(new String[]{"Accéder aux commandes", "Accéder au panier d'achat", "Voir les billets",
                    "Changer les informations du profil", "Rechercher un produit",
                    "Rechercher un revendeur", "Gérer les acheteur suivis",
                    "Afficher les métriques", "Voir les notifications", "Se déconnecter"});
            switch (choix) {
                case 1 -> gererCommandes();
                case 2 -> allerAuPanier();
                case 3 -> gererBillets();
                case 4 -> changerInformations();
                case 5 -> {
                    short c = rechercherProduits();
                    while (c == 1)
                        c = rechercherProduits();
                    if (c == 2)
                        allerAuPanier();
                }
                case 6 -> rechercherRevendeur();
                case 7 -> gererAcheteursSuivis();
                case 8 -> System.out.println(acheteur.afficherMetriques());
                case 9 -> System.out.println("\n" + acheteur.voirNotifications());
                case 10 -> {
                    return;
                }
            }
        }
    }
    /**
     * Cette méthode éffectue la recherche d'un produit par un acheteur
     * @return Les produits qui sont conformes à la recherche
     */
    static short rechercherProduits() {
        System.out.println("\nQuel type de recherche voulez-vous faire?");
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
            recherche = demanderString();
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
                        if (Integer.parseInt(infos[5]) > Math.floor(Float.parseFloat(infos[3])))
                            pSelect.add(produitPreview);
                    }
                }
            }
        }
        if (pSelect.isEmpty()) {
            System.out.println("\nAucun résultat pour cette recherche. Veuillez réessayer.");
            return 1;
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
                    case 1 -> {
                        if (p.liker(acheteur.getUsername()))
                            System.out.println("\nVous avez liké le produit " + p.getTitre() + "!");
                        else
                            System.out.println("\nVous avez déjà liké ce produit!");
                    }
                    case 2 -> {
                        if (p.getQuantite() == 0) {
                            System.out.println("\nLe produit " + p.getTitre() + " doit être restocké.");
                            continue;
                        }
                        acheteur.panier.addProduit(p);
                        System.out.println("\nVous avez ajouté " + p.getTitre() + " au panier!");
                        System.out.println("\nVoulez-vous aller au panier?");
                        if (choixOuiNon())
                            return 2;
                    }
                    case 3 -> {
                        ArrayList<Evaluation> es = p.getEvaluations();
                        if (es.isEmpty()) {
                            System.out.println("\nCe produit n'a aucune évaluations!");
                            break;
                        }
                        System.out.println("\nChoississez une évaluation:");

                        ArrayList<String> ess = new ArrayList<>();
                        for (Evaluation e : es)
                            ess.add(e.getDisplayFormat() + "\n");
                        ess.add("Retourner au produit");
                        choix = selectionChoix(ess.toArray());
                        if (choix == ess.size())
                            continue;
                        Evaluation e = es.get(choix - 1);
                        System.out.println("\n" + e.getDisplayFormat());
                        if (e.nomAcheteur.equals(acheteur.getUsername())) {
                            System.out.println("\nC'est votre propre évaluation!");
                            continue;
                        }
                        System.out.println("\nQue voulez-vous faire?");
                        choix = selectionChoix(new String[] {"Liker l'évaluation",
                                "Signaler l'évaluation comme inappropriée", "Retourner au produit"});
                        if (choix == 3)
                            continue;
                        Acheteur a = initialiserAcheteur(e.nomAcheteur);
                        if (choix == 1) {
                            if (e.getNbLikes() == 0)
                                a.ajouterPoints(10);
                            if (e.ajouterLike(acheteur.getUsername())) {
                                p.save();
                                System.out.println("\nVous avez liké l'évaluation de " + a.getUsername() + "!");
                            }
                            else
                                System.out.println("\nVous avez déjà liké cette évaluation!");
                        }
                        else {
                            if (e.signaler()) {
                                if (e.getNbLikes() == 0)
                                    a.ajouterPoints(-10);
                                e.signaler();
                                p.save();
                                System.out.println("\nVous avez signalé cette évaluation!");
                            }
                            else
                                System.out.println("\nCette évaluation était déjà signalée!");
                        }

                    }
                    case 4 -> {
                        if (acheteur.aAcheteProduit(p.getTitre())) {
                            ecrireEvaluation(p);
                        }
                        else {
                            System.out.println("\nVous devez acheter ce produit avant de l'évaluer!");
                        }
                    }
                    case 5 -> {
                        ArrayList<String> ar = p.getLikes();
                        ar.add("Retourner au produit");
                        String[] as = ar.toArray(new String[0]);
                        if (as.length == 1) {
                            System.out.println("\nCe produit n'a aucun likes.");
                        }
                        else {
                            System.out.println("\nChoisir un acheteur à suivre: ");
                            choix = selectionChoix(as);
                            if (choix == as.length)
                                continue;
                            suivreAcheteur(ar.get(choix - 1));
                        }
                    }
                }
            }
        }
    }

    /**
     * Cette méthode permet aux acheteurs de gérer leurs commandes
     */
    static void gererCommandes() {
        ArrayList<Commande> cmds = acheteur.getCommandes();
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
        while (true) {
            System.out.println("\nChoisissez une action: ");
            choix = selectionChoix(new String[]{"Confirmer la livraison", "Retourner un produit", "Évaluer un produit",
                    "Annuler la commande", "Regarder la date d'arrivée estimée", "Retourner au menu"});
            switch (choix) {
                case 1 -> {
                    switch (cmd.confirmerLivraison()) {
                        case 0 -> System.out.println("\nL'état de votre commande a été changé avec succès!");
                        case 1 -> System.out.println("\nVotre commande est toujours en production.");
                        case 2 -> System.out.println("\nVous avez déjà confirmé la livraison de cette commande!");
                    }
                }
                case 2 -> {
                    if (!cmd.estLivre()) {
                        System.out.println("\nVotre commande n'a pas encore été livrée!");
                        continue;
                    }
                    if (obtenirTempsEnSecondes() - cmd.getTempsReception() > 2592000) {
                        System.out.println("\nVous avez passé le délai de 30 jours pour retourner une commande!");
                        continue;
                    }

                    Produit p = choisirProduit(cmd.getProduits());
                    if (p == null)
                        continue;
                    else if (acheteur.billetExiste(p.getId())) {
                        System.out.println("\nVous avez déjà fait un billet pour ce produit!");
                        continue;
                    }
                    System.out.print("Quel est le problème avec ce produit? ");
                    String a = demanderString();
                    Revendeur r = initialiserRevendeur(p.getNomReven());
                    Billet b;
                    System.out.println("Voulez-vous effectuer un retour ou un échange?");
                    if (1 == selectionChoix(new String[] {"Retour", "Échange"})) {
                        b = new Billet(p.getId(), acheteur.getUsername(), p.getTitre(), a, true, false,
                                "", "", false);
                    }
                    else {
                        System.out.println("Choisissez un produit parmi ceux offert par le revendeur: ");
                        Produit produitRemplacement = choisirProduit(r.getProduits());
                        if (produitRemplacement == null)
                            continue;
                        b = new Billet(p.getId(), acheteur.getUsername(), p.getTitre(), a, false, false,
                               "", produitRemplacement.getTitre(), false);
                        float differencePrix = arrondirPrix(produitRemplacement.getPrix() - p.getPrix());
                        System.out.println("La différence de prix est de " + differencePrix + "$.");
                        Commande echange = new Commande((short)0, differencePrix, 0);
                        echange.setEchange(produitRemplacement, differencePrix);
                        echange.passerCommande(acheteur.getUsername(), acheteur.getAddress());
                        acheteur.ajouterCommande(echange);
                        r.ajouterCommande(echange);
                        System.out.println("\nL'ID de votre commande pour l'échange est " + echange.getId());
                    }
                    b.save();
                    acheteur.addBillet(b);
                    r.addBillet(b);
                    Notification notifRev = new Notification(8, acheteur.getUsername(), p.getNomReven(),
                            p.getTitre(), -1);
                    Revendeur rev = initialiserRevendeur(p.getNomReven());
                    rev.addNotifications(notifRev);
                    System.out.println("\nVotre demande " + (b.estRetour ? "de retour" : "d'échange") + " a été " +
                            "traitée avec succès!\nVotre ID pour ce billet est: " + p.getId());
                }
                case 3 -> {
                    if (!cmd.estLivre()) {
                        System.out.println("\nVotre commande n'a pas encore été livrée!");
                        continue;
                    }
                    Produit p = choisirProduit(cmd.getProduits());
                    if (p == null)
                        continue;
                    ecrireEvaluation(p);
                }
                case 4 -> {
                    if (cmd.estEnProduction()) {
                        System.out.println("\nVoulez-vous vraiment annuler votre commande?");
                        if (choixOuiNon()) {
                            acheteur.annulerCommande(cmd);
                            for (String re : cmd.getRevendeurs()) {
                                Revendeur r = initialiserRevendeur(re);
                                r.annulerCommande(r.trouverCommande(cmd.getId()));
                            }
                            effacerFichier(COMMANDES_PATH + cmd.getId() + CSV);
                            acheteur.ajouterPoints(-cmd.getPointsTotal());
                            System.out.println("\nVous avez annulé votre commande #" + cmd.getId() + "!");
                            return;
                        }
                    }
                    else
                        System.out.println("\nVous ne pouvez plus annuler votre commande!");

                }
                case 5 -> {
                    if (cmd.estLivre())
                        System.out.println("\nVotre commande est déjà livrée!");
                    else
                        System.out.println("\nVotre commande devrait arriver aujourd'hui le " + new java.util.Date());
                }
                case 6 -> {return;}
            }
        }
    }
    /**
     * Cette méthode permet aux acheteurs de visiter leur panier.
     */
    static void allerAuPanier() {
        if (acheteur.panier.estVide()) {
            System.out.println("\nVotre panier est vide!");
            return;
        }

        System.out.println("\nVoici votre panier:");
        System.out.println(acheteur.panier.afficher());
        System.out.println("\nChoisissez une action: ");
        choix = selectionChoix(new String[] {"Enlever un produit", "Passer la commande", "Retourner au menu"});
        switch (choix) {
            case 1 -> {
                Produit p = choisirProduit(acheteur.panier.getProduits());
                if (p == null)
                    break;
                acheteur.panier.removeProduit(p);
                System.out.println("\nVous avez enlevé " + p.getTitre() + " du panier.");
                allerAuPanier();
            }
            case 2 -> {
                System.out.println("Voulez-vous utiliser la même adresse que votre compte?");
                String adresse = acheteur.getAddress();
                if (!choixOuiNon()) {
                    System.out.print("Entrez une nouvelle adresse: ");
                    adresse = demanderString();
                }
                System.out.println("Voulez-vous payer avec vos points?");
                if (choixOuiNon()) {
                    float pointsEnDollars = arrondirPrix(acheteur.viderPoints() / 100f);
                    float nouveauTotal = arrondirPrix(acheteur.panier.getCoutTotal() - pointsEnDollars);
                    System.out.println("Vous avez l'équivalent de " + pointsEnDollars + "$ en points, ce qui " +
                            "amène votre total à " + nouveauTotal + "$.");
                }
                System.out.print("Entrez votre numéro de carte de crédit: ");
                demanderLong("un numéro de carte de crédit");
                System.out.print("Entrez la date d'expiration de votre carte en format MMAA: ");
                demanderIntPositif("une date d'expiration");
                System.out.print("Entrez le CVV: ");
                demanderIntPositif("un CVV");
                Commande c = acheteur.panier.passerCommande(acheteur.getUsername(), adresse);
                acheteur.ajouterCommande(c.copy());

                ArrayList<String> revens = new ArrayList<>();
                for (Produit p : c.getProduits()) {
                    Revendeur r = initialiserRevendeur(p.getNomReven());
                    r.ajouterVente(p.getPrix());
                    Notification notifRev = new Notification(6, acheteur.getUsername(), p.getNomReven(),
                            p.getTitre(), c.getId());
                    r.addNotifications(notifRev);
                    if (!revens.contains(p.getNomReven())) {
                        r.ajouterCommande(c);
                        revens.add(r.getUsername());
                    }
                }
                acheteur.panier.vider();
                System.out.println("\nVotre commande a été passée avec succès!");
                System.out.println("Votre identifiant de commanque unique est: " + c.getId());
            }
        }
    }
    /**
     * Cette méthode permet aux acheteurs de gérer leurs billets
     */
    static void gererBillets() {
        ArrayList<Billet> ba = acheteur.getBillets();
        if (ba.isEmpty()) {
            System.out.println("\nVous n'avez aucun billet!");
            return;
        }
        while (true) {
            System.out.println("\nChoississez un billet: ");
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
                    if (b.comfirmerLivraisonRempla())
                        System.out.println("\nVous avez confirmé la livraison du produit de remplacement!");
                    else
                        System.out.println("\nVous avez déjà confirmé la réception du produit de remplacement!");
                }
                else if (choix == 3)
                    return;
            }
        }

    }

    /**
     * Cette méthode permet aux acheteurs d'écrire des évaluaations
     * @param p Le produit que l'on souhaite évaluer
     */
    static void ecrireEvaluation (Produit p) {
        System.out.println("\nÉvaluation du produit " + p.getTitre() + ":");
        System.out.print("Veuillez entrer une note entière entre 1 et 5: ");
        int note;
        note = demanderIntPositif("une note");
        while (note > 5 || note == 0) {
            System.out.print("Veuillez entrer une note plus petite ou égale à 5: ");
            note = demanderIntPositif("une note");
        }
        System.out.print("Entrez un commentaire sur le produit: ");
        String comment = demanderString();
        Evaluation e = new Evaluation(acheteur.getUsername(), note, comment, false, new ArrayList<>());
        p.addEvaluation(e);
        System.out.println("\nVotre évaluation a été écrite avec succès!");

        Revendeur r = initialiserRevendeur(p.getNomReven());
        r.addNotifications(new Notification(7, acheteur.getUsername(), p.getNomReven(), p.getTitre(), -1));
    }

    /**
     * Cette méthode permet aux acheteurs de modifier les informations de leur profil
     */
    static void changerInformations() {
        System.out.println("\nChoississez ce que vous voulez modifier: ");
        choix = selectionChoix(new String[]{"Mot de passe", "Adresse courriel", "Téléphone", "Adresse",
                "Prénom", "Nom", "Retourner au menu"});
        switch (choix) {
            case 1 -> {
                System.out.print("Entrez un nouveau mot de passe: ");
                String password = demanderString();
                acheteur.setPassword(password);
                System.out.println("\nVous avez changé votre mot de passe!");
            }
            case 2 -> {
                System.out.print("Entrez une nouvelle adresse de courriel: ");
                String email = demanderCourrielUnique();
                acheteur.setEmail(email);
                System.out.println("\nVous avez changé votre adresse courriel!");
            }
            case 3 -> {
                System.out.println("Entrez un nouveau numéro de téléphone: ");
                long telephone = demanderLong("un numéro");
                acheteur.setPhone(telephone);
                System.out.println("\nVous avez changé votre numéro de téléphone!");
            }
            case 4 -> {
                System.out.print("Entrez une nouvelle adresse: ");
                String adresse = demanderString();
                acheteur.setAddress(adresse);
                System.out.println("\nVous avez changé votre adresse!");
            }
            case 5 -> {
                System.out.print("Entrez un nouveau prénom: ");
                String prenom = demanderString();
                acheteur.setPrenom(prenom);
                System.out.println("\nVous avez changé votre prénom!");
            }
            case 6 -> {
                System.out.print("Entrez un nouveau nom: ");
                String nom = demanderString();
                acheteur.setNom(nom);
                System.out.println("\nVous avez changé votre nom!");
            }
        }
    }
    /**
     *  Cette méthode permet de gérer les acheteurs suivis
     */
    static void gererAcheteursSuivis() {
        System.out.println("\nQue voulez-vous faire ?");
        choix = selectionChoix(new String[]{"Chercher un acheteur à suivre",
                "Voir le classement des acheteurs que vous suivez", "Retourner au menu principal"});
        switch (choix) {
            case 1 -> {
                while (true) {
                    ArrayList<String> acheteurSelect = new ArrayList<>();
                    List<String> acheteurs = fichiersDansDossier(ACHETEURS_PATH);
                    System.out.print("\nEntrez votre recherche: ");
                    String demandeUtilisateur = demanderString();
                    for (String a : acheteurs) {
                        if (a.contains(demandeUtilisateur))
                            acheteurSelect.add(a);
                    }

                    if (acheteurSelect.isEmpty()) {
                        System.out.println("\nAucun résultat pour cette recherche. Veuillez réessayer.");
                        continue;
                    }
                    acheteurSelect.add("Faire une nouvelle recherche");
                    acheteurSelect.add("Retourner au menu acheteur");
                    while (true) {
                        System.out.println("\nChoisissez un acheteur: ");
                        choix = selectionChoix(acheteurSelect.toArray());
                        if (choix == acheteurSelect.size() - 1)
                            break;
                        else if (choix == acheteurSelect.size())
                            return;
                        Acheteur a = initialiserAcheteur(acheteurSelect.get(choix - 1));
                        System.out.println(a.afficherMetriques());
                        System.out.println("\nQue voulez-vous faire ensuite?");
                        choix = selectionChoix(new String[]{"Suivre l'acheteur", "Retourner au résultat de la recherche"});
                        if (choix == 1)
                            suivreAcheteur(a.getUsername());
                    }
                }
            }
            case 2 -> {
                ArrayList<Acheteur> acheteurs = new ArrayList<>();
                acheteurs.add(acheteur);
                for (String a : acheteur.getSuivis())
                    acheteurs.add(initialiserAcheteur(a));
                acheteurs.sort(Collections.reverseOrder());
                int i = 1;
                System.out.println();
                for (Acheteur a : acheteurs) {
                    System.out.println(i + ". " + a.getUsername() + " avec " + a.getPoints() + " points");
                    ++i;
                }
            }
        }
    }
    /**
     * Cette méthode permet aux revendeurs de rechercher un revendeur
     */
    static void rechercherRevendeur() {
        while (true) {
            System.out.println("\nQuel type de recherche voulez-vous faire?");
            ArrayList<String> revendeurSelect = new ArrayList<>();
            List<String> revendeurs = fichiersDansDossier(REVENDEURS_PATH);
            String demandeUtilisateur = "";
            boolean estRecherche = 1 == selectionChoix(new String[]{"Recherche par mots-clés",
                    "Recherche par filtre"});
            if (estRecherche) {
                System.out.print("Entrez votre recherche: ");
                demandeUtilisateur = demanderString();
            } else {
                System.out.println("Choisissez votre type de filtre: ");
                choix = selectionChoix(new String[]{"Catégorie de produits vendues", "Nom", "Adresse"});
                switch (choix) {
                    case 1 -> {
                        System.out.println("Choisissez une catégorie: ");
                        demandeUtilisateur = Categorie.getCat(selectionChoix(Categorie.categories) - 1);

                    }
                    case 2 -> {
                        System.out.print("Entrez un nom: ");
                        demandeUtilisateur = demanderString();

                    }
                    case 3 -> {
                        System.out.print("Entrez une adresse: ");
                        demandeUtilisateur = demanderString();
                    }
                }
            }

            for (String r : revendeurs) {
                String[] contenu = lireFichierEnEntier(REVENDEURS_PATH + r + "/" + INFOS);
                String adresse = contenu[0].split(",")[3];
                List<String> categories = Arrays.asList(contenu[2].split(","));

                if (estRecherche) {
                    if (r.contains(demandeUtilisateur))
                        revendeurSelect.add(r);
                } else {
                    switch (choix) {
                        case 1 -> {
                            if (categories.contains(demandeUtilisateur))
                                revendeurSelect.add(r);
                        }
                        case 2 -> {
                            if (r.contains(demandeUtilisateur))
                                revendeurSelect.add(r);
                        }
                        case 3 -> {
                            if (adresse.contains(demandeUtilisateur))
                                revendeurSelect.add(r);
                        }
                    }
                }
            }
            if (revendeurSelect.isEmpty()) {
                System.out.println("\nAucun résultat pour cette recherche. Veuillez réessayer.");
                continue;
            }
            revendeurSelect.add("Faire une nouvelle recherche");
            revendeurSelect.add("Retourner au menu acheteur");
            while (true) {
                System.out.println("\nChoisissez un revendeur: ");
                choix = selectionChoix(revendeurSelect.toArray());
                if (choix == revendeurSelect.size() - 1)
                    break;
                else if (choix == revendeurSelect.size())
                    return;
                Revendeur r = initialiserRevendeur(revendeurSelect.get(choix - 1));
                System.out.println(r.afficherMetriques());
                System.out.println("\nQue voulez-vous faire ensuite?");
                choix = selectionChoix(new String[]{"Liker le revendeur", "Retourner au résultat de la recherche"});
                if (choix == 1) {
                    if (r.ajouterFollower(acheteur.getUsername()))
                        System.out.println("\nVous suivez maintenant le revendeur " + r.getUsername() + "!");
                    else
                        System.out.println("\nVous suivez déjà ce revendeur!");
                }
            }
        }
    }

    /**
     * Cette méthode permet aux acheteurs de suivre un autre acheteur
     * @param nom Nom de l'acheteur
     */
    static void suivreAcheteur(String nom) {
        switch (acheteur.suivre(nom)) {
            case 0 -> {
                System.out.println("\nVous suivez maintenant l'acheteur " + nom + "!");
                Acheteur a = initialiserAcheteur(nom);
                a.addNotifications(new Notification(0, acheteur.getUsername(), "", "",
                        -1));
                acheteur.ajouterPoints(5);
                a.ajouterPoints(5);
            }
            case 1 -> System.out.println("\nVous suivez déjà cet acheteur!");
            case 2 -> System.out.println("\nVous ne pouvez pas vous suivre vous-même!");
        }

    }
}
