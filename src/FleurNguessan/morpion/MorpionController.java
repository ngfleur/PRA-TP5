package FleurNguessan.morpion;

import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class MorpionController {

	@FXML
	private Button restart;

	@FXML
	private GridPane grid;

	@FXML
	private Label casesO;

	@FXML
	private Label casesX;

	@FXML
	private Label caseLibre;

	@FXML
	private Label gameMsg;

	private static TicTacToeModel model = TicTacToeModel.getInstance();

	public void initialize() {

		for (int i = 0; i < model.getHeight(); i++) {
			for (int j = 0; j < model.getWidth(); j++) {

				TicTacToeSquare square = new TicTacToeSquare(i, j);
				square.ownerProperty().bindBidirectional(model.getSquare(i, j));
				square.winnerProperty().bindBidirectional(model.getWinningSquare(i, j));

				grid.add(square, j, i);

			}
		}

		caseLibre.visibleProperty().bind(model.gameOver().not());

		casesX.textProperty().bind(new StringBinding() {
			{
				bind(model.getScore(Owner.FIRST));
			}

			@Override
			protected String computeValue() {

				int value = model.getScore(Owner.FIRST).intValue();
				if (value > 1) {
					return value + " cases pour X";
				} else {
					return value + " case pour X";
				}
			}
		});

		casesX.styleProperty().bind(new StringBinding() {
			{
				bind(model.turnProperty(), model.gameOver(), model.winnerProperty());
			}

			@Override
			protected String computeValue() {

				if (model.turnProperty().get().equals(Owner.FIRST) & !model.gameOver().get()) {
					return "-fx-background-color: cyan; -fx-text-fill: black;";
				} else {
					return "-fx-background-color: red; -fx-text-fill: white;";
				}
			}
		});

		casesO.textProperty().bind(new StringBinding() {
			{
				bind(model.getScore(Owner.SECOND), model.gameOver());
			}

			@Override
			protected String computeValue() {
				int value = model.getScore(Owner.SECOND).intValue();
				if (value > 1) {
					return value + " cases pour O";
				} else {
					return value + " case pour O";
				}

			}
		});

		casesO.styleProperty().bind(new StringBinding() {
			{
				bind(model.turnProperty(), model.gameOver(), model.winnerProperty());
			}

			@Override
			protected String computeValue() {

				if (model.turnProperty().get().equals(Owner.SECOND) && !model.gameOver().get()) {
					return "-fx-background-color: cyan; -fx-text-fill: black;";
				} else {
					return "-fx-background-color: red; -fx-text-fill: white;";
				}

			}
		});

		caseLibre.textProperty().bind(new StringBinding() {
			{
				bind(model.getScore(Owner.NONE));
			}

			@Override
			protected String computeValue() {
				int value = model.getScore(Owner.NONE).intValue();
				if (value != 0) {
					if (value > 1) {
						return value + " cases libres";
					} else {
						return value + " case libre";
					}
				} else {
					return "";
				}
			}
		});

		gameMsg.textProperty().bind(model.getEndOfGameMessage());

		restart.setOnAction(event -> model.restart());

	}

}
