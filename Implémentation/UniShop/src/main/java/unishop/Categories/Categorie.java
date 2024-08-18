package unishop.Categories;


/**
 * Représente un objet de la catégorie dans un système de gestion de catégories.
 */
public abstract class Categorie {

     public static final String[] categories = new String[] {"Livres et manuels", "Ressources d'apprentissage",
             "Articles de papeterie", "Matériel informatique", "Équipement de bureau"};

     /** Cette méthode retourne le ID de la catégorie
      * @return L'id de la catégorie
      */
     public abstract short getCatID();

     /**
      *
      * Cette méthode donne la catégorie en fonction de l'id passé en argument
      * @param id l'id dont on souhaite trouver la catégorie
      * @return la catégorie qui est affiliée à l'id donné
      */
     public static String getCat(int id) {
          return categories[id];
     };
     /**
      * Cette méthode donne la catégorie à laquelle appartient un object.
      * @return La catégorie à laquelle appartient un objet
      */
     public String getCat() {return categories[getCatID()];}
     /**
      * Obtient les informations selon le format long de sauvegarde d'objet de la catégorie
      * @return Les informations de l'objet de la catégorie
      */
     public abstract String getFormatSauvegarde();
     /**
      * Obtient les informations selon le format display de sauvegarde d'objet de la catégorie
      * @return Les informations display de l'objet de la catégorie
      */
     public abstract String getFormatDisplay();
}
