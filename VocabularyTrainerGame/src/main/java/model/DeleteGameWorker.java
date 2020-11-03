package model;

import htw.ClientAdapter.GameClientAdapter;
import htw.GameManagmentInter.Game;
import htw.GameManagmentInter.GameNotFoundException;
import javafx.concurrent.Task;

public class DeleteGameWorker extends Task<Boolean> {
	
	private Game game;
	private String errorMessage;
	private GameClientAdapter gca;
	private boolean result = false;
	
	public DeleteGameWorker(Game game) {
		super();
		this.game = game;
		this.gca = new GameClientAdapter();
	}

	@Override
	protected Boolean call() {
		try {
			result = gca.deleteGame(game.getGameID());
		} catch (RuntimeException | GameNotFoundException e) {
			this.errorMessage = e.getMessage();
		}
		return result;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	
	public boolean getResult() {
		return result;
	}

	public Game getGame() {
		return game;
	}
}
