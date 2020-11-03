package model;

import htw.ClientAdapter.GameClientAdapter;
import htw.GameManagmentInter.Game;
import htw.GameManagmentInter.GameNotCreatableException;
import htw.GameManagmentInter.OnePlayerTwoTimesInGameException;
import htw.PlayerManagementInter.Player;
import javafx.concurrent.Task;

public class CreateGameWorker extends Task<Game> {
	
	private Player player1;
	private Player player2;
	private String from;
	private String to;
	private int vocabularyListId;
	private GameClientAdapter grs;
	private Game game;
	private String errorMessage;
	
	public CreateGameWorker(Player player1, Player player2, String from, String to, int vocabularyListId) {
		super();
		this.player1 = player1;
		this.player2 = player2;
		this.from = from;
		this.to = to;
		this.vocabularyListId = vocabularyListId;
		this.grs = new GameClientAdapter();
	}

	@Override
	protected Game call() {
		try {
			this.game =  grs.createGame(player1, player2, from, to, vocabularyListId);
		} catch (RuntimeException | OnePlayerTwoTimesInGameException | GameNotCreatableException e) {
			this.errorMessage = e.getMessage();
		}
		return game;
	}

	public Game getGame() {
		return game;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
