package unishop;

import unishop.Users.*;
import unishop.Categories.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


import static unishop.ControleurAcheteur.menuAcheteur;
import static unishop.ControleurInvite.menuInvite;
import static unishop.ControleurRevendeur.menuRevendeur;
/**
 * Cette classe émule et structure le fonctionnement de Unishop
 */
public class Main {

    public final static String DATABASE_PATH = "Database/";
    public final static String PRODUITS_PATH = DATABASE_PATH + "Produits/";
    public final static String USERS_PATH = DATABASE_PATH + "Users/";
    public final static String ACHETEURS_PATH = USERS_PATH + "Acheteurs/";
    public final static String REVENDEURS_PATH = USERS_PATH + "Revendeurs/";
    public final static String COMMANDES_PATH = DATABASE_PATH + "Commandes/";
    public final static String BILLETS_PATH = DATABASE_PATH + "Billets/";
    public final static String CSV = ".csv";
    public final static String IDS = DATABASE_PATH + "IDs.csv";
    public final static String EMAILS = DATABASE_PATH + "emails.csv";
    public final static String PANIER = "Panier.csv";
    public final static String INFOS = "Infos.csv";
    public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    private static User connectedUser = null;
    private static short choix;
    /**
     * Cette méthode permet de simuler le fonctionnement d'Unishop
     * @param args Les instructions à passer à Unishop
     */
    public static void main(String[] args) {
        menuPrincipal();
        System.out.println("\nAu plaisir de vous revoir!");
    }
    /**
     * Cette méthode émule le menu principal
     */
    static void menuPrincipal(){
        while (true) {
            System.out.println("\nBienvenue sur UniShop!");
            choix = selectionChoix(new String[]{"Se connecter", "Créer un compte", "Continuer en tant qu'invité",
                    "Quitter UniShop"});
            switch (choix) {
                case 1 -> {
                    connectedUser = connecterUser();
                    if (connectedUser == null)
                        continue;
                    System.out.print("\nRebonjour " + connectedUser.getUsername() + " ! ");
                    connectedUser.save();
                    if (connectedUser.isAcheteur())
                        menuAcheteur((Acheteur) connectedUser);
                    else
                        menuRevendeur((Revendeur) connectedUser);
                    connectedUser = null;
                }
                case 2 -> creerCompte();
                case 3 -> menuInvite();
                case 4 -> {return;}
            }
        }
    }

    /**
     * Cette méthode simule un choix oui ou non
     * @return True si oui et False sinon
     */
    public static boolean choixOuiNon() {
        System.out.println("1. Oui\n2. Non");
        while (true) {
            System.out.print("Choisir une option: ");
            String reponseS = demanderString();
            try {
                short reponse = Short.parseShort(reponseS);
                if (reponse == 1 || reponse == 2)
                    return reponse == 1;
                else
                    System.out.println("Choix invalide! Veuillez entrer un choix de 1 à 2");
            }
            catch(NumberFormatException e){
                System.out.println("Veuillez entrer un chiffre de 1 à 2");
            }
        }
    }

    /**
     * Cette méthode émule la sélection d'un choix
     * @param choix  Les diverses options que l'on offre
     * @return Le choix qui a été éffectué
     */
    public static short selectionChoix(Object[] choix) {
        int nbChoix = choix.length;
        for (int i = 0; i < nbChoix; ++i) {
            System.out.println(i + 1 + ". " + choix[i]);
        }
        while (true) {
            System.out.print("Choisir une option: ");
            String reponseS = demanderString();
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
    /**
     * Cette méthode ermet au utilisateur de se créer un compte
     */
    static void creerCompte() {
        System.out.println("\nChoisissez le type de compte à créer:");
        choix = selectionChoix(new String[]{"Acheteur", "Revendeur", "Revenir au menu principal"});
        if (choix == 3)
            return;
        System.out.println("\nRemplissez le formulaire pour créer un compte:");
        System.out.print("Entrez votre username: ");
        String username = demanderString();
        List<String> acheteurs = fichiersDansDossier(ACHETEURS_PATH);
        List<String> revendeurs = fichiersDansDossier(REVENDEURS_PATH);
        while (acheteurs.contains(username) || revendeurs.contains(username)) {
            System.out.print("Ce Username existe déjà. Veuillez entrer un autre Username: ");
            username = demanderString();
        }
        System.out.print("Entrez votre mot de passe: ");
        String motDePasse = demanderString();
        System.out.print("Entrez votre courriel: ");
        String courriel = demanderCourrielUnique();
        System.out.print("Entrez votre téléphone: ");
        long telephone = demanderLong("un téléphone");
        System.out.print("Entrez votre adresse: ");
        String adresse = demanderString();
        if (choix == 1) {
            String basePath =  ACHETEURS_PATH + username + "/";
            System.out.print("Entrez votre prénom: ");
            String prenom = demanderString();
            System.out.print("Entrez votre nom: ");
            String nom = demanderString();
            if (new File(basePath).mkdir()) {
                String[] infos = new String[] {motDePasse, courriel, "" + telephone , adresse, nom, prenom,
                        "0,0", "" + obtenirTempsEnSecondes()};
                ecrireFichierEntier(basePath + INFOS, String.join(",", infos) + "\n\n\n\n\n");
                ecrireFichierEntier(basePath + PANIER, "0,0");
                System.out.println("Inscription du compte acheteur " + username + " réussi!");
            }
            else
                System.out.println("Erreur lors de la création du dossier. Veuillez recommencer");

        } else {
            String basePath = REVENDEURS_PATH + username + "/";
            if (new File(basePath).mkdir()) {
                String[] infos = new String[] {motDePasse, courriel, "" + telephone , adresse, "0.0,0, , ",
                        "" + obtenirTempsEnSecondes() };
                ecrireFichierEntier(basePath + INFOS, String.join(",", infos) + "\n\n\n\n\n");
                System.out.println("Inscription du compte revendeur " + username + " réussi!");
            }
            else
                System.out.println("Erreur lors de la création du dossier. Veuillez recommencer");
        }
    }

    /**
     * Cette méthode permet aux utilisateurs de se connecter
     * @return le compte auquel l'utilisateur se connecte
     */
    static User connecterUser() {
        System.out.println("\nConnection (Ne rien rentrer retourne au menu principal): ");
        String categorie = "";
        String username = "";
        while (categorie.isEmpty()) {
            System.out.print("Entrez votre Username: ");
            username = demanderString();
            if (username.isEmpty())
                return null;
            File dossier = new File (ACHETEURS_PATH + username);
            if (dossier.exists()) {
                categorie = "Acheteurs/";
            }
            dossier = new File (REVENDEURS_PATH + username);
            if (dossier.exists()) {
                categorie = "Revendeurs/";
            }
            if (categorie.isEmpty()) {
                System.out.println("Username inconnu. Veuillez réessayer");
            }
        }

        String[] data = lireFichierEnEntier(USERS_PATH + categorie + username + "/" + INFOS);
        String[] infos = data[0].split(",");
        String password = infos[0];
        System.out.print("Entrez votre mot de passe: ");
        String passwordEntre = demanderString();
        if (passwordEntre.isEmpty())
            return null;
        while (!(password.equals(passwordEntre))) {
            System.out.print("Mauvais mot de passe! Veuillez recommencer: ");
            passwordEntre = demanderString();
        }
        if (infos.length == 9) {
            long creationTime = Long.parseLong(infos[8]);
            if (obtenirTempsEnSecondes() - creationTime > 86400) {
                System.out.println("\nCe compte a été créé il y a plus de 24h et est donc invalide! Veuillez créer"+
                        "un nouveau compte!");
                effacerFichier(USERS_PATH + categorie + username);
            }
            else
                System.out.println("Votre compte est maintenant activé!");
        }
        if (categorie.equals("Acheteurs/")){
            return initialiserAcheteur(username);
        }
        else {
            return initialiserRevendeur(username);
        }
    }

    /**
     * Cette méthode permet d'initialiser un acheteur
     * @param username L'username de l'acheteur
     * @return un acheteur
     */
    public static Acheteur initialiserAcheteur(String username) {
        String path = ACHETEURS_PATH + username + "/";
        String[] data = lireFichierEnEntier( path+ INFOS);
        String[] infos = data[0].split(",");
        ArrayList<String> as =  iniArrayList(data[1]);
        ArrayList<String> rl =  iniArrayList(data[2]);
        ArrayList<Commande> cmds = new ArrayList<>();
        for (String id : iniArrayList(data[3]))
            cmds.add(initialiserCommande(Integer.parseInt(id)));
        ArrayList<Billet> bis = new ArrayList<>();
        for (String id : iniArrayList(data[4]))
            bis.add(initialiserBillet(Integer.parseInt(id)));
        Stack<Notification> notifs = new Stack<>();
        for (int i = 5; i < data.length; ++i) {
            String[] n = data[i].split(",");
            notifs.push(new Notification(Integer.parseInt(n[0]), n[1], n[2], n[3], Integer.parseInt(n[4])));
        }
        String[] dataPanier = lireFichierEnEntier(path + PANIER);
        String[] infosPanier = dataPanier[0].split(",");
        float coutT = Float.parseFloat(infosPanier[0]);
        int pts = Integer.parseInt(infosPanier[1]);
        Commande panier = new Commande((short) 0, coutT, pts);
        for (int j = 1; j < dataPanier.length; ++j) {
            Produit p = initialiserProduit(dataPanier[j]);
            panier.addInitial(p);
        }
        return new Acheteur(username, infos[0], infos[1], Long.parseLong(infos[2]),
                infos[3], infos[4], infos[5], Integer.parseInt(infos[6]), as, rl, bis,
                panier, cmds, notifs);


    }

    /** Cette méthode permet d'initialiser un revendeur
     * @param username  L'username du revendeur
     * @return Le revendeur
     */
    static Revendeur initialiserRevendeur(String username) {
        String path = REVENDEURS_PATH + username + "/";
        String[] data = lireFichierEnEntier( path+ INFOS);
        String[] infos = data[0].split(",");
        ArrayList<String> followers = iniArrayList(data[1]);
        ArrayList<String> cats = iniArrayList(data[2]);
        ArrayList<Commande> cmds = new ArrayList<>();
        for (String id : iniArrayList(data[3]))
            cmds.add(initialiserCommande(Integer.parseInt(id)));
        ArrayList<Billet> bis = new ArrayList<>();
        for (String id : iniArrayList(data[4]))
            bis.add(initialiserBillet(Integer.parseInt(id)));
        Stack<Notification> notifs = new Stack<>();
        for (int i = 5; i < data.length; ++i) {
            String[] n = data[i].split(",");
            notifs.push(new Notification(Integer.parseInt(n[0]), n[1], n[2], n[3], Integer.parseInt(n[4])));
        }
        ArrayList<Produit> ps = new ArrayList<>();
        for(String pc : fichiersDansDossier(PRODUITS_PATH)){
            String r = lireFichierEnEntier(PRODUITS_PATH + pc)[0].split(",")[0];
            if (r.equals(username))
                ps.add(initialiserProduit(pc));
        }
        return new Revendeur(username, infos[0], infos[1], Long.parseLong(infos[2]), infos[3],
                Float.parseFloat(infos[4]), Integer.parseInt(infos[5]), followers, bis, ps, cmds, cats, notifs);
    }
    /** Cette méthode permet d'initialiser un produit
     * @param titreProduit Le nom du produit
     * @return un nouveau produit
     *
     */
    static Produit initialiserProduit(String titreProduit) {
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
        ArrayList<Evaluation> evals = new ArrayList<>();
        for (int i = 5; i < s.length; ++i) {
            String[] e = s[i].split(",");
            ArrayList<String> elikes = new ArrayList<>(Arrays.asList(e).subList(4, e.length));
            evals.add(new Evaluation(e[0], Integer.parseInt(e[1]), e[2], Boolean.parseBoolean(e[3]), elikes));
        }
        return new Produit(f[0], f[1], f[2], Float.parseFloat(f[3]), Integer.parseInt(f[4]), Integer.parseInt(f[5]),
                images, videos, c, likes, evals);
    }

    /**
     * Cette méthode permet d'initialiser une commande
     * @param id L'id de la commande à initialiser
     * @return La commande que l'on souhaite initialiser
     */
    static Commande initialiserCommande(int id) {
        String[] lines = lireFichierEnEntier(COMMANDES_PATH + id + CSV);
        String[] fstLine = lines[0].split(",");
        Commande c = new Commande(Short.parseShort(fstLine[2]), Float.parseFloat(fstLine[3]),
                Integer.parseInt(fstLine[4]));
        long rec = 0;
        if (c.estLivre())
            rec = Long.parseLong(fstLine[7]);
        c.addPastInfo(Integer.parseInt(fstLine[0]), fstLine[1], fstLine[5], fstLine[6], rec);
        for (int j = 1; j < lines.length; ++j) {
            String[] line = lines[j].split(",");
            Produit p = initialiserProduit(line[0]);
            p.setUniqueId(Integer.parseInt(line[1]));
            c.addInitial(p);
        }
        return c;
    }
    /** Cette méthode permet d'initialiser un billet
     * @param id L'id du billet
     * @return L'objet billet
     */
    static Billet initialiserBillet(int id) {
        String[] bs = lireFichierEnEntier(BILLETS_PATH + id + CSV)[0].split(",");
        return new Billet(Integer.parseInt(bs[0]), bs[1], bs[2], bs[3], Boolean.parseBoolean(bs[4]),
                Boolean.parseBoolean(bs[5]) , bs[6], bs[7], Boolean.parseBoolean(bs[8]));
    }
    /**
     * Permet de demander un entier positif
     * @param demande L'entier qu'on demande
     * @return L'entier qu'on veut
     */
    public static int demanderIntPositif(String demande) {
        int i;
        while (true) {
            try {
                i = Integer.parseInt(demanderString());
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
    /**
     * Permet de demander un nombre  de type long
     * @param demande la description du nombre  qu'on demande
     * @return Le nombre qu'on veut
     */
    public static long demanderLong(String demande) {
        long l;
        while (true) {
            try {
                l = Long.parseLong(demanderString());
                return l;
            }
            catch (NumberFormatException e) {
                System.out.print("Veuillez entrer " + demande + " valide: ");
            }
        }
    }
    /**
     * Permet de demander un float
     * @param demande La description du float  qu'on demande
     * @return Le float  qu'on recoit
     */
    public static float demanderFloat(String demande) {
        float prix;
        while (true) {
            try {
                prix = arrondirPrix(Float.parseFloat(demanderString()));
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
    /**
     * Cette méthode permet de demander un string
     * @return  Le string rentrer
     */

    public static String demanderString() {
        try {
            return br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }
    /**
     * Permet d'arrondir un prix
     * @param prix  Le prix que l'on souhaite arrondir
     * @return Le prix suite à l'arrondie
     */
    // TEST
    public static float arrondirPrix(float prix) {
        return Math.round((prix) * 100) / 100f;
    }
    /**
     * Permet le calcul du temps en seconde
     * @return Le temps  en seconde
     */
    public static long obtenirTempsEnSecondes() {
        return System.currentTimeMillis() / 1000;
    }

    /** Permet lire un fichier entier
     * @param path Endroit où l'on souhaite lire le fichier
     */
    public static String[] lireFichierEnEntier(String path) {
        try {
            return Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8).toArray(new String[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return new String[0];
        }
    }
    /** Permet d'écrire un fichier entier
     * @param path Endroit où l'on souhaite écrire le fichier
     * @param toWrite Ce que l'on souhaite écrire dans le fichier
     */
    public static void ecrireFichierEntier(String path, String toWrite) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(path));
            bw.write(toWrite);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Retournes les fichiers dans un certain dossier passé en argument
     * @param path addresse du dossier à lire
     * @return les fichiers dans le dossier
     */
    static List<String> fichiersDansDossier(String path) {
        return Arrays.asList(Objects.requireNonNull(new File(path).list()));
    }
    /**
     * Crées une arraylist de string
     * @param s Le string initial
     * @return Le Arraylist
     */
    public static ArrayList<String> iniArrayList(String s) {
        String[] tab = s.split(",");
        if (tab[0].isEmpty())
            return new ArrayList<>(Arrays.asList(tab).subList(1, tab.length));
        else
            return new ArrayList<>(Arrays.asList(tab));

    }

    /**
     * Cette méthode connecte un utilisateur
     * @return L'utilisateur est conecté
     */
    public static String getConnectedUsername() {
        return connectedUser.getUsername();
    }
    /**
     * Éfface un fichier
     * @param path  le path du fichier à effacer
     */
    public static void effacerFichier(String path) {
        File file = new File(path);
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                effacerFichier(f.getPath());
            }
        }
        if (!file.delete())
            System.out.println("Fichier non effacé: " + file);
    }

    /**
     * Demander un courriel unique
     * @return Le courriel
     */
    public static String demanderCourrielUnique() {
        String courriel = demanderString();
        ArrayList<String> emails = iniArrayList(lireFichierEnEntier(EMAILS)[0]);
        while (emails.contains(courriel) || courriel.isEmpty()) {
            System.out.print("Un compte a déjà été créé avec ce courriel. Veuillez en entrer un autre: ");
            courriel = demanderString();
        }
        emails.add(courriel);
        ecrireFichierEntier(EMAILS, String.join(",", emails));
        return courriel;
    }

    /**
     * Cette méthode permet de choisir un produit
     * @param produits L'arrayList des produits
     * @return Un produit
     */
    public static Produit choisirProduit(ArrayList<Produit> produits) {
        String[] s = new String[produits.size() + 1];
        int i = 0;
        for (Produit p : produits) {
            s[i] = p.getQuickDisplay();
            ++i;
        }
        s[produits.size()] = "Retourner au menu";
        choix = selectionChoix(s);
        if (choix == s.length)
            return null;
        return produits.get(choix - 1);
    }
}