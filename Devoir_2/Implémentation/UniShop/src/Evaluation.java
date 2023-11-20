public class Evaluation {
    final String nomAcheteur;
    final int note;
    private final String commentaire;

    public Evaluation(String nomAcheteur, int note, String commentaire) {
        this.nomAcheteur = nomAcheteur;
        this.note = note;
        this.commentaire = commentaire;
    }
    public String getSaveFormat() {
        return nomAcheteur + "," + note + "," + commentaire;
    }
    public String getDisplayFormat() {
        return "Acheteur: " + nomAcheteur + "\nNote sur 5: " + note + "\n" + commentaire;
    }
}
