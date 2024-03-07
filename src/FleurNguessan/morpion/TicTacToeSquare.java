package FleurNguessan.morpion;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class TicTacToeSquare extends TextField {

	private static TicTacToeModel model = TicTacToeModel.getInstance();
	private ObjectProperty<Owner> ownerProperty = new SimpleObjectProperty<>(Owner.NONE);
	private BooleanProperty winnerProperty = new SimpleBooleanProperty(false);

	public ObjectProperty<Owner> ownerProperty() {
		return ownerProperty;
	}

	public BooleanProperty winnerProperty() {
		return winnerProperty;
	}

	public TicTacToeSquare(final int row, final int column) {

		setEditable(false);
		setFont(new Font(40));
		setAlignment(Pos.CENTER);
		setMaxWidth(Double.MAX_VALUE);
		setMaxHeight(Double.MAX_VALUE);

		Background backgroundNormal = new Background(new BackgroundFill(Color.WHITE, new CornerRadii(0), null));
		Background backgroundGreen = new Background(new BackgroundFill(Color.LIGHTGREEN, new CornerRadii(0), null));
		Background backgroundRed = new Background(new BackgroundFill(Color.RED, new CornerRadii(0), null));
		BorderStroke borderStroke = new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
				new BorderWidths(0.5));
		Border border = new Border(borderStroke);

		setBackground(backgroundNormal);
		setBorder(border);

		setOnMouseEntered(event -> {
			if (!model.gameOver().get()) {
				setBackground(backgroundGreen);
			} else {
				setBackground(backgroundRed);
			}
		});

		setOnMouseExited(event -> {
			setBackground(backgroundNormal);
		});

		setOnMouseClicked(event -> {
			if (model.legalMove(row, column).get()) {
				model.play(row, column);
				ownerProperty.set(model.getSquare(row, column).get());

			}
		});

		this.ownerProperty.addListener((observable, oldValue, newValue) -> {
			setText(newValue.equals(Owner.FIRST) ? "X" : newValue.equals(Owner.SECOND) ? "O" : "");
			setDisable(newValue != Owner.NONE);
		});

		this.winnerProperty.addListener((observable, oldValue, newValue) -> {
			if (newValue) {
				setFont(new Font(70));
			} else {
				setFont(new Font(40));
			}
		});
	}

}
