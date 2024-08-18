package unishop.Tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unishop.Billet;

import static org.junit.jupiter.api.Assertions.*;

class BilletTest {

    private Billet billet;

    @BeforeEach
    void setUp() {
        billet = new Billet(123, "Samuel", "livre", "probAche", true,
                true, "probRev", "ordinateur", true);
    }

    @Test
    void afficherMenu() {
        String resultat = billet.afficherMenu();
        assertEquals("ID: 123; Acheteur: Samuel; Produit problématique: livre; Retour", resultat);
    }

    @Test
    void comfirmerLivraisonRempla() {
        boolean resultat = billet.comfirmerLivraisonRempla();
        assertFalse(resultat);
    }
}