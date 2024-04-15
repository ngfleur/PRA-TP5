package FleurNguessan.morpion;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.NumberExpression;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class TicTacToeModel {

	/**
	 * Taille du plateau de jeu (pour être extensible).
	 */
	private final static int BOARD_WIDTH = 3;
	private final static int BOARD_HEIGHT = 3;

	public int getWidth() {
		return BOARD_WIDTH;
	}

	public int getHeight() {
		return BOARD_HEIGHT;
	}

	/**
	 * Nombre de pièces alignés pour gagner (idem).
	 */
	private final static int WINNING_COUNT = 3;
	/**
	 * Joueur courant.
	 */
	private final ObjectProperty<Owner> turn = new SimpleObjectProperty<>(Owner.FIRST);
	/**
	 * Vainqueur du jeu, NONE si pas de vainqueur.
	 */
	private final ObjectProperty<Owner> winner = new SimpleObjectProperty<>(Owner.NONE);
	/**
	 * Plateau de jeu.
	 */
	private final ObjectProperty<Owner>[][] board;
	/**
	 * Positions gagnantes.
	 */
	private final BooleanProperty[][] winningBoard;

	private final IntegerProperty countX = new SimpleIntegerProperty(0);
	private final IntegerProperty countO = new SimpleIntegerProperty(0);
	private final IntegerProperty countLibre = new SimpleIntegerProperty(BOARD_HEIGHT * BOARD_WIDTH);

	/**
	 * Constructeur privé.
	 */
	TicTacToeModel() {
		this.board = new SimpleObjectProperty[BOARD_HEIGHT][BOARD_WIDTH];
		this.winningBoard = new SimpleBooleanProperty[BOARD_HEIGHT][BOARD_WIDTH];

		for (int i = 0; i < BOARD_HEIGHT; i++) {
			for (int j = 0; j < BOARD_WIDTH; j++) {
				board[i][j] = new SimpleObjectProperty<>(Owner.NONE);
				winningBoard[i][j] = new SimpleBooleanProperty(false);

			}
		}

	}

	/**
	 * @return la seule instance possible du jeu.
	 */
	public static TicTacToeModel getInstance() {
		return TicTacToeModelHolder.INSTANCE;
	}

	/**
	 * Classe interne selon le pattern singleton.
	 */
	private static class TicTacToeModelHolder {
		private static final TicTacToeModel INSTANCE = new TicTacToeModel();
	}

	public void restart() {

		for (int i = 0; i < BOARD_HEIGHT; i++) {
			for (int j = 0; j < BOARD_WIDTH; j++) {
				board[i][j].set(Owner.NONE);
				winningBoard[i][j].set(false);
			}
		}
		countX.set(0);
		countO.set(0);
		countLibre.set(9);
		turn.set(Owner.FIRST);
		winner.set(Owner.NONE);

	}

	public final ObjectProperty<Owner> turnProperty() {
		return turn;
	}

	public final ObjectProperty<Owner> winnerProperty() {
		return winner;
	}

	public final ObjectProperty<Owner> getSquare(int row, int column) {
		return board[row][column];
	}

	public final BooleanProperty getWinningSquare(int row, int column) {
		return winningBoard[row][column];
	}

	/**
	 * Cette fonction ne doit donner le bon résultat que si le jeu est terminé.
	 * L’affichage peut être caché avant la fin du jeu. *
	 * 
	 * @return résultat du jeu sous forme de texte
	 */
	public final StringExpression getEndOfGameMessage() {
		return new StringBinding() {
			{
				bind(winnerProperty(), getScore(Owner.NONE));
			}

			@Override
			protected String computeValue() {
				if (gameOver().get()) {
					return switch (winnerProperty().get()) {
					case FIRST -> "GAME OVER  LE GAGNANT EST LE PREMIER";
					case SECOND -> "GAME OVER LE GAGNANT EST LE DEUXIEME";
					default -> "GAME OVER MATCH NUL";
					};
				} else {
					return "";
				}
			}
		};

	}

	public void setWinner(Owner winner) {
		this.winner.set(winner);
	}

	public boolean validSquare(int row, int column) {
		return 0 <= row && row < BOARD_HEIGHT && 0 <= column && column < BOARD_WIDTH;
	}

	public void nextPlayer() {
		turn.set(turn.get().opposite());
	}

	/**
	 * Jouer dans la case (row, column) quand c’est possible.
	 */
	public void play(int row, int column) {
		if (legalMove(row, column).get()) {
			getSquare(row, column).set(turn.get());

			// Augmentation du score du joueur courant
			if (turn.get().equals(Owner.FIRST)) {
				countX.set(countX.get() + 1);
			} else {
				countO.set(countO.get() + 1);
			}
			// Diminuer les cases libres
			countLibre.set(countLibre.get() - 1);

			// Detecteur de cas de victoire
			Win();

			nextPlayer();
		}
	}

	public void Win() {

		// Verifier les lignes
		for (int i = 0; i < BOARD_HEIGHT; i++) {
			if (checkRow(i))
				return;
		}

		// Verifier les colonnes
		for (int j = 0; j < BOARD_WIDTH; j++) {
			if (checkColumn(j))
				return;
		}

		// Vérifier les diagonales
		if (checkDiagonals())
			return;

		// Pas de gagnant, toutes les cases remplies
		if (isBoardFull().get()) {
			setWinner(Owner.NONE); // Match nul
		}
	}

	private boolean checkRow(int row) {
		Owner firstCell = getSquare(row, 0).get();

		// Cas où la ligne peut pas etre gagnante
		if (firstCell == Owner.NONE)
			return false;

		for (int j = 1; j < BOARD_WIDTH; j++) {
			if (!getSquare(row, j).get().equals(firstCell)) {
				return false;
			}
		}

		// Sinon Marquer la ligne comme gagnante
		markWinningRow(row);
		setWinner(firstCell);
		return true;
	}

	private boolean checkColumn(int column) {
		Owner firstCell = getSquare(0, column).get();

		// Cas où la colonne peut pas etre gagnante
		if (firstCell == Owner.NONE)
			return false;

		for (int i = 1; i < BOARD_HEIGHT; i++) {
			if (!getSquare(i, column).get().equals(firstCell)) {
				return false;
			}
		}

		// Sinon Marquer la colonne comme gagnante
		markWinningColumn(column);
		setWinner(firstCell);
		return true;
	}

	private boolean checkDiagonals() {

		// Vérifier la diagonale principale
		Owner topLeft = getSquare(0, 0).get();
		boolean vrai = true;

		for (int i = 1; i < BOARD_HEIGHT; i++) {
			if (topLeft != Owner.NONE && topLeft == getSquare(i, i).get()) {
				vrai &= true;
			} else {
				vrai &= false;
			}
		}
		if (vrai) {
			markWinningDiagonal(false);
			setWinner(topLeft);
			return true;
		}

		// Vérifier la diagonale inverse
		Owner topRight = getSquare(0, 2).get();
		vrai = true;
		for (int i = 1; i < BOARD_HEIGHT; i++) {
			if (topRight != Owner.NONE && topRight == getSquare(i, BOARD_WIDTH - 1 - i).get()) {
				vrai &= true;
			} else {
				vrai &= false;
			}
		}
		if (vrai) {
			markWinningDiagonal(true);
			setWinner(topRight);
			return true;
		}

		return false;
	}

	/**
	 * Verifier que la grille est pleine
	 */
	private BooleanBinding isBoardFull() {
		return getScore(Owner.NONE).isEqualTo(0);
	}

	/**
	 * Marquer une ligne comme gagnante
	 */
	private void markWinningRow(int row) {
		for (int j = 0; j < BOARD_WIDTH; j++) {
			getWinningSquare(row, j).set(true);
		}
	}

	/**
	 * Marquer une diagonale comme gagnante
	 */
	private void markWinningDiagonal(boolean reverse) {
		if (!reverse) {
			// Marquer la diagonale principale
			for (int i = 0; i < BOARD_HEIGHT; i++) {
				getWinningSquare(i, i).set(true);
			}
		} else {
			// Marquer la diagonale secondaire
			for (int i = 0; i < BOARD_HEIGHT; i++) {
				getWinningSquare(i, BOARD_WIDTH - 1 - i).set(true);
			}
		}
	}

	/**
	 * Marquer une colonne comme gagnante
	 */
	private void markWinningColumn(int column) {
		for (int i = 0; i < BOARD_HEIGHT; i++) {
			getWinningSquare(i, column).set(true);
		}
	}

	/**
	 * @return true s’il est possible de jouer dans la case c’est-à-dire la case est
	 *         libre et le jeu n’est pas terminé
	 */
	public BooleanBinding legalMove(int row, int column) {
		return getSquare(row, column).isEqualTo(Owner.NONE).and(gameOver().not());
	}

	/**
	 * @return le score d'un joueur
	 */

	public NumberExpression getScore(Owner owner) {
		switch (owner) {
		case FIRST:
			return countX;
		case SECOND:
			return countO;
		default:
			return countLibre;
		}
	}

	/**
	 * @return true si le jeu est terminé (soit un joueur a gagné, soit il n’y a
	 *         plus de cases à jouer)
	 */
	public BooleanBinding gameOver() {

		return winner.isNotEqualTo(Owner.NONE).or(new BooleanBinding() {
			{
				bind(winner, getScore(Owner.NONE));
			}

			@Override

			protected boolean computeValue() {
				return winner.get().equals(Owner.NONE) && getScore(Owner.NONE).intValue() == 0;

			}
		});

	}

}
