package unishop;

import unishop.Categories.Categorie;
import unishop.Users.Acheteur;
import unishop.Users.Revendeur;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static unishop.Main.*;
/**
 * Cette classe émule le contrôle des invités
 */
public class ControleurInvite {
    static short choix;

    /**
     * Cette méthode émule le menu des invité
     */
    static void menuInvite(){
        while (true) {
            System.out.println("\nVoici le menu Invité:");
            choix = selectionChoix(new String[]{"Trouver un acheteur", "Trouver un revendeur", "Trouver un produit",
                    "Retourner au menu principal"});
            switch (choix) {
                case 1 -> trouverAcheteur();
                case 2 -> trouverRevendeur();
                case 3 -> trouverProduits();
                case 4 -> {
                    return;
                }
            }
        }
    }

    /**
     * Cette méthode trouve l'acheteur
     */
    static void trouverAcheteur() {
        while (true) {
            System.out.print("\nEntrer le nom d'un acheteur (N'entrez rien pour la liste de tous les acheteurs): ");
            Acheteur acheteur;
            String aEntre = demanderString();
            ArrayList<String> aSelect = new ArrayList<>();
            for (String a : fichiersDansDossier(ACHETEURS_PATH)) {
                if (a.contains(aEntre))
                    aSelect.add(a);
            }
            if (aSelect.isEmpty()) {
                System.out.println("Aucun acheteur ne correspond à cette recherche! Veuillez réessayer.");
                continue;
            }
            aSelect.add("Faire une nouvelle recherche");
            aSelect.add("Retourner au menu");
            System.out.println("Sélectionnez une option: ");
            choix = selectionChoix(aSelect.toArray());
            if (choix == aSelect.size() - 1)
                continue;
            else if (choix == aSelect.size())
                return;
            acheteur = initialiserAcheteur(aSelect.get(choix - 1));
            while (true) {
                System.out.println("\nVoici les informations sur " + acheteur.getUsername() + ": " +
                        acheteur.afficherMetriques());
                System.out.println("\nQue voulez-vous faire ensuite?");
                choix = selectionChoix(new String[] {"Voir les followers", "Voir les suivis",
                        "Faire une autre recherche", "Retourner au menu"});
                if (choix == 3)
                    break;
                else if (choix == 4)
                    return;
                if (choix == 1)
                    aSelect = acheteur.getFollowers();
                else
                    aSelect = acheteur.getSuivis();
                if (aSelect.isEmpty()) {
                    if (choix == 1)
                        System.out.println("\n" + acheteur.getUsername() + " n'a aucun follower!");
                    else
                        System.out.println("\n" + acheteur.getUsername() + " ne suit aucun acheteur!");
                    continue;
                }
                System.out.println("\nChoississez un acheteur: ");
                choix = selectionChoix(aSelect.toArray());
                acheteur = initialiserAcheteur(aSelect.get(choix - 1));
            }

        }
    }
    /**
     * Cette méthode trouve les produits
     */
    static void trouverProduits() {
        while (true) {
            System.out.println("\nQuel type de recherche voulez-vous faire?");
            ArrayList<String> pSelect = new ArrayList<>();
            List<String> produits = fichiersDansDossier(PRODUITS_PATH);
            boolean estRecherche = 1 == selectionChoix(new String[]{"Recherche par mots-clés",
                    "Recherche par filtre"});
            String recherche = "";
            short choixCat = 0;
            boolean estPrixPlusGrand = false;
            float floatDemande = 0;
            int nbLikesDemande = 0;
            if (estRecherche) {
                System.out.print("Entrez votre recherche: ");
                recherche = demanderString();
            } else {
                System.out.println("Choisissez votre type de filtre: ");
                choix = selectionChoix(new String[]{"Catégorie", "Prix", "Popularité", "Note moyenne",
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

            for (String p : produits) {
                String[] contenu = lireFichierEnEntier(PRODUITS_PATH + p);
                String[] infos = contenu[0].split(",");
                String[] cat = contenu[3].split(",");
                String produitPreview = String.join(", ", infos[1],
                        Categorie.getCat(Integer.parseInt(cat[0])), "Revendeur: " + infos[0]);
                if (estRecherche) {
                    if ((infos[1] + infos[2] + cat[1] + cat[2] + cat[3]).contains(recherche))
                        pSelect.add(produitPreview);
                } else {
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
                continue;
            }
            pSelect.add("Faire une nouvelle recherche");
            pSelect.add("Retourner au menu");
            while (true) {
                System.out.println("\nChoisissez un produit: ");
                choix = selectionChoix(pSelect.toArray());
                if (choix == pSelect.size() - 1)
                    break;
                else if (choix == pSelect.size())
                    return;
                Produit p = initialiserProduit(pSelect.get(choix - 1).split(",")[0]);
                System.out.println("\n" + p.getFormatDisplay());
                while (true) {
                    System.out.println("\nQue voulez-vous faire ensuite?");
                    choix = selectionChoix(new String[]{"Regarder les évaluations", "Voir les likes",
                            "Retourner au résultat de la recherche"});
                    if (choix == 3)
                        break;
                    switch (choix) {
                        case 1 -> System.out.println("\n" + p.getEvaluationsDisplay());
                        case 2 -> {
                            ArrayList<String> ar = p.getLikes();
                            String[] as = ar.toArray(new String[0]);
                            if (as.length == 1)
                                System.out.println("\nCe produit n'a aucun likes.");
                            else
                                System.out.println("\nVoici la liste des likes:\n" + String.join("\n", as));
                        }
                    }
                }
            }
        }
    }
    /**
     * Cette méthode trouve les revendeurs
     */
    static void trouverRevendeur() {
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
                        System.out.print("Entrez votre recherche: ");
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
            revendeurSelect.add("Retourner au menu");
            while (true) {
                System.out.println("\nChoisissez un revendeur: ");
                choix = selectionChoix(revendeurSelect.toArray());
                if (choix == revendeurSelect.size() - 1)
                    break;
                else if (choix == revendeurSelect.size())
                    return;
                Revendeur r = initialiserRevendeur(revendeurSelect.get(choix - 1));
                System.out.println("\n" + r.afficherMetriques() + "\n");

            }
        }
    }
}