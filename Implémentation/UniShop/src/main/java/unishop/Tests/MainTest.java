package unishop.Tests;
import static unishop.Main.*;

import org.junit.jupiter.api.Test;
import unishop.Main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    public void selectionChoix() {
        Object[] choix = {"Option 1", "Option 2", "Option 3"};

        // Input Valide
        String inputValide = "2\n";

        // Creation du "fake" input et son output
        java.io.ByteArrayInputStream fakeInput = new java.io.ByteArrayInputStream(inputValide.getBytes());
        System.setIn(fakeInput);
        java.io.ByteArrayOutputStream fakeOutput = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(fakeOutput));

        br = new BufferedReader(new InputStreamReader(System.in));

        short resultat = Main.selectionChoix(choix);

        System.setIn(System.in);
        System.setOut(System.out);

        // Retour correct
        assertEquals(2, resultat);
    }

    @Test
    public void demanderIntPositif()  {
        String demande = "entier";

        // Input Valide
        String inputValide = "2\n";

        // Creation du "fake" input et son output
        java.io.ByteArrayInputStream fakeInput = new java.io.ByteArrayInputStream(inputValide.getBytes());
        System.setIn(fakeInput);
        java.io.ByteArrayOutputStream fakeOutput = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(fakeOutput));

        br = new BufferedReader(new InputStreamReader(System.in));

        int resultat = Main.demanderIntPositif(demande);

        System.setIn(System.in);
        System.setOut(System.out);

        // Retour correct
        assertEquals(2, resultat);
    }

    @Test
    public void demanderLong()  {
        String demande = "entier";

        // Input Valide
        String inputValide = "1234567890\n";

        // Creation du "fake" input et son output
        java.io.ByteArrayInputStream fakeInput = new java.io.ByteArrayInputStream(inputValide.getBytes());
        System.setIn(fakeInput);
        java.io.ByteArrayOutputStream fakeOutput = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(fakeOutput));

        br = new BufferedReader(new InputStreamReader(System.in));

        long resultat = Main.demanderLong(demande);

        System.setIn(System.in);
        System.setOut(System.out);

        // Retour correct
        assertEquals(1234567890, resultat);
    }

    @Test
    public void demanderFloat()  {
        String demande = "entier";

        // Input Valide
        String inputValide = "1.5\n";

        // Creation du "fake" input et son output
        java.io.ByteArrayInputStream fakeInput = new java.io.ByteArrayInputStream(inputValide.getBytes());
        System.setIn(fakeInput);
        java.io.ByteArrayOutputStream fakeOutput = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(fakeOutput));

        br = new BufferedReader(new InputStreamReader(System.in));

        float resultat = Main.demanderFloat(demande);

        System.setIn(System.in);
        System.setOut(System.out);

        // Retour correct
        assertEquals(1.5, resultat);
    }

    @Test
    public void arrondirPrix() {
        float prix = 5.829247f;

        float resultat = Main.arrondirPrix(prix);

        assertEquals(5.83, resultat, 0.001);
    }

    @Test
    public void iniArrayList() {
        ArrayList<String> expectedOutput = new ArrayList<>();
        expectedOutput.add("String");
        expectedOutput.add("pour");
        expectedOutput.add("tester");

        // String avec son premier élément non vide
        String stringNonVide = "String,pour,tester";
        ArrayList<String> resultat1 = Main.iniArrayList(stringNonVide);
        assertEquals(expectedOutput, resultat1);

        // String avec son premier élément vide
        String stringVide = ",String,pour,tester";
        ArrayList<String> resultat2 = Main.iniArrayList(stringVide);
        assertEquals(expectedOutput, resultat2);
    }

    @Test
    public void choixOuiNon_OUI() {
        // Choix pour OUI
        String choixOui = "1\n";

        // Creation du "fake" input et son output
        java.io.ByteArrayInputStream fakeInput = new java.io.ByteArrayInputStream(choixOui.getBytes());
        System.setIn(fakeInput);
        java.io.ByteArrayOutputStream fakeOutput = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(fakeOutput));

        br = new BufferedReader(new InputStreamReader(System.in));
        boolean resultat = Main.choixOuiNon();

        System.setIn(System.in);
        System.setOut(System.out);

        // Retour correct pour OUI
        assertTrue(resultat);
    }

    @Test
    public void choixOuiNon_NON() {
        // Choix pour NON
        String choixOui = "2\n";

        // Creation du "fake" input et son output
        java.io.ByteArrayInputStream fakeInput = new java.io.ByteArrayInputStream(choixOui.getBytes());
        System.setIn(fakeInput);
        java.io.ByteArrayOutputStream fakeOutput = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(fakeOutput));

        br = new BufferedReader(new InputStreamReader(System.in));
        boolean resultat = Main.choixOuiNon();

        System.setIn(System.in);
        System.setOut(System.out);

        // Retour correct pour NON
        assertFalse(resultat);
    }
}