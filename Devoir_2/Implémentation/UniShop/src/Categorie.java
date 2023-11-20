public abstract class Categorie {

     public static String[] categories = new String[] {"Livres et manuels", "Ressources d'apprentissage",
             "Articles de papeterie", "Matériel informatique", "Équipement de bureau"};

     public abstract short getCatID();

     public static String getCat(int id) {
          return categories[id];
     };
     public String getCat() {return categories[getCatID()];}

     public abstract String getFormatSauvegarde();

     public abstract String getFormatDisplay();
}
