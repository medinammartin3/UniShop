package unishop.Tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unishop.Evaluation;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EvaluationTest {

    private Evaluation evaluation;

    @BeforeEach
    void setUp() {
        ArrayList<String> likes = new ArrayList<>();
        likes.add("User1");
        likes.add("User2");
        evaluation = new Evaluation("Etienne", 3, "moyen", false,
                likes);
    }

    @Test
    void signaler() {
        boolean resultat = evaluation.signaler();
        assertTrue(resultat);
    }

    @Test
    void ajouterLike() {
        evaluation.ajouterLike("Test");
        assertEquals(3, evaluation.getNbLikes());
    }
}