package FleurNguessan.morpion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TicTacToeTest {
	TicTacToeModel morpions;

	@BeforeEach
	public void setUp() {
		morpions = TicTacToeModel.getInstance();
		morpions.restart();
	}

	@Test
	public void testInit() {
		assertEquals(Owner.FIRST, morpions.turnProperty().get(), "Le premier doit jouer");
		testInvariant();

		assertTrue(!morpions.gameOver().get(), "Debut de partie, partie pas finie");
		testInvariant();

		assertEquals(morpions.getScore(Owner.FIRST).intValue() + morpions.getScore(Owner.SECOND).intValue(), 0,
				"Le nombre de coup doit être à0");
		testInvariant();

		assertEquals(morpions.winnerProperty().get(), Owner.NONE, "Debut de partie, pas de gagnant");

		for (int i = 0; i < morpions.getHeight(); i++) {

			for (int j = 0; j < morpions.getWidth(); j++) {
				assertEquals(morpions.getSquare(i, j).get(), Owner.NONE, "Les cases doivent être vides");

				assertTrue(morpions.legalMove(i, j).get());
				testInvariant();
			}
		}

	}

	@Test
	public void testGetJoueur() {
		assertEquals(Owner.FIRST, morpions.turnProperty().get(), "Le premier doit jouer");// Verifier le joueur courant
		testInvariant();

		morpions.nextPlayer();

		assertEquals(Owner.SECOND, morpions.turnProperty().get(), "Le deuxieme doit jouer");// Verifier le joueur
																							// suivant
		testInvariant();
	}

	@Test
	public void testPremierCoup() {
		testInit();
		morpions.play(0, 0);
		assertEquals(Owner.FIRST, morpions.getSquare(0, 0).get(),
				"La case (0,0) est censée appartenir au premier joueur"); // Verifier
		testInvariant();
	}

	@Test
	public void testPartiePasFinie() {
		assertTrue(!morpions.gameOver().get(), "La partie n'est pas terminée");
		testInvariant();

		assertEquals(Owner.NONE, morpions.winnerProperty().get(), "Non fin de partie, pas de gagnant");
		testInvariant();

		// Recherche d'une case libre
		boolean free = false;
		for (int i = 0; i < morpions.getHeight(); i++) {
			for (int j = 0; j < morpions.getWidth(); j++) {

				if (morpions.legalMove(i, j).get()) {
					free = true;
					break;
				}
			}
		}

		assertTrue(free, "Partie pas finie, il doit avoir au moins une case libre");
		testInvariant();

	}

	@Test
	public void testGetVainqueur() {

		// le PREMIER joueur gagne en diagonale

		for (int i = 0; i < morpions.getHeight(); i++) {
			for (int j = 0; j < morpions.getWidth(); j++) {

				if (morpions.getScore(Owner.FIRST).intValue() + morpions.getScore(Owner.SECOND).intValue() < 7) {
					assertTrue(morpions.legalMove(i, j).get(), "La case est censée être libre");
					morpions.play(i, j);
				}
			}
		}

		assertEquals(7, morpions.getScore(Owner.FIRST).intValue() + morpions.getScore(Owner.SECOND).intValue(),
				"Le nombre de coup à 7");
		assertEquals(Owner.FIRST, morpions.winnerProperty().get(), "Le premier doit gagner");
		assertTrue(morpions.gameOver().get(), " la partie est terminée ");
		testInvariant();
	}

	@Test
	public void testFinPartie() {

		// le deuxieme joueur gagne en vertical

		assertTrue(morpions.legalMove(1, 1).get(), "La case est censée être libre");
		morpions.play(1, 1);
		assertEquals(morpions.getScore(Owner.FIRST).intValue() + morpions.getScore(Owner.SECOND).intValue(), 1,
				"Le nombre de coup à 0");
		assertEquals(Owner.NONE, morpions.winnerProperty().get(), "Aucun vainqueur");
		testInvariant();

		assertTrue(morpions.legalMove(0, 2).get(), "La case est censée être libre");
		morpions.play(0, 2);
		assertEquals(morpions.getScore(Owner.FIRST).intValue() + morpions.getScore(Owner.SECOND).intValue(), 2,
				"Le nombre de coup à 0");
		assertEquals(Owner.NONE, morpions.winnerProperty().get(), "Aucun vainqueur");
		testInvariant();

		assertTrue(morpions.legalMove(0, 1).get(), "La case est censée être libre");
		morpions.play(0, 1);
		assertEquals(morpions.getScore(Owner.FIRST).intValue() + morpions.getScore(Owner.SECOND).intValue(), 3,
				"Le nombre de coup à 0");
		assertEquals(Owner.NONE, morpions.winnerProperty().get(), "Aucun vainqueur");
		testInvariant();

		assertTrue(morpions.legalMove(2, 1).get(), "La case est censée être libre");
		morpions.play(2, 1);
		assertEquals(morpions.getScore(Owner.FIRST).intValue() + morpions.getScore(Owner.SECOND).intValue(), 4,
				"Le nombre de coup à 0");
		assertEquals(Owner.NONE, morpions.winnerProperty().get(), "Aucun vainqueur");
		testInvariant();

		assertTrue(morpions.legalMove(2, 2).get(), "La case est censée être libre");
		morpions.play(2, 2);
		assertEquals(morpions.getScore(Owner.FIRST).intValue() + morpions.getScore(Owner.SECOND).intValue(), 5,
				"Le nombre de coup à 0");
		assertEquals(Owner.NONE, morpions.winnerProperty().get(), "Aucun vainqueur");
		testInvariant();

		assertTrue(morpions.legalMove(0, 0).get(), "La case est censée être libre");
		morpions.play(0, 0);
		assertEquals(morpions.getScore(Owner.FIRST).intValue() + morpions.getScore(Owner.SECOND).intValue(), 6,
				"Le nombre de coup à 0");
		assertEquals(Owner.NONE, morpions.winnerProperty().get(), "Aucun vainqueur");
		testInvariant();

		assertTrue(morpions.legalMove(1, 2).get(), "La case est censée être libre");
		morpions.play(1, 2);
		assertEquals(morpions.getScore(Owner.FIRST).intValue() + morpions.getScore(Owner.SECOND).intValue(), 7,
				"Le nombre de coup à 0");
		assertEquals(Owner.NONE, morpions.winnerProperty().get(), "Aucun vainqueur");
		testInvariant();

		assertTrue(morpions.legalMove(1, 0).get(), "La case est censée être libre");
		morpions.play(1, 0);
		assertEquals(morpions.getScore(Owner.FIRST).intValue() + morpions.getScore(Owner.SECOND).intValue(), 8,
				"Le nombre de coup à 0");
		assertEquals(Owner.NONE, morpions.winnerProperty().get(), "Aucun vainqueur");
		testInvariant();

		assertTrue(morpions.legalMove(2, 0).get(), "La case est censée être libre");
		morpions.play(2, 0);
		assertEquals(morpions.getScore(Owner.FIRST).intValue() + morpions.getScore(Owner.SECOND).intValue(), 9,
				"Le nombre de coup à 0");
		assertEquals(Owner.NONE, morpions.winnerProperty().get(), "Le deuxième doit gagner");

		assertTrue(morpions.gameOver().get());
		testInvariant();
	}

	@Test
	public void testControle() {

		for (int i = 0; i < morpions.getHeight(); i++) {
			for (int j = 0; j < morpions.getWidth(); j++) {

				if ((morpions.getScore(Owner.FIRST).intValue() + morpions.getScore(Owner.SECOND).intValue()) < 7) {
					assertTrue(morpions.legalMove(i, j).get(), "La case est censée être libre");
					morpions.play(i, j);
					assertTrue(!morpions.legalMove(i, j).get(), "La case est censée être remplie");

				}
			}
		}

		assertTrue(morpions.gameOver().get());
		assertTrue(!morpions.legalMove(2, 1).get(), "La case est censée être libre");
		assertTrue(!morpions.legalMove(2, 2).get(), "La case est censée être libre");

	}

	/**
	 * Fonction à utiliser après chaque action, pour tester les conditions qui
	 * doivent toujours être vraies
	 */
	private void testInvariant() {
		assertTrue(morpions.getScore(Owner.FIRST).intValue() + morpions.getScore(Owner.SECOND).intValue() >= 0,
				"Nombre de coups >= 0");
		assertTrue(
				morpions.getScore(Owner.FIRST).intValue()
						+ morpions.getScore(Owner.SECOND).intValue() <= morpions.getWidth() * morpions.getHeight(),
				"Nombre de coups <= " + morpions.getWidth() * morpions.getHeight());

		// ----------------------
		// SÉQUENCE À COMPLÉTER
		// ----------------------
	}

}
