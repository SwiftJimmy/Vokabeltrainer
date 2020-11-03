package model;

import htw.ClientAdapter.GameClientAdapter;
import htw.GameManagmentInter.Game;
import htw.GameManagmentInter.GameNotFoundException;
import javafx.concurrent.Task;

public class UpdateGameWorker extends Task<Boolean> {
	
	private Game game;
	private GameClientAdapter grs;
	private boolean result = false;
	private String errorMessage;
	
	public UpdateGameWorker(Game game) {
		super();
		this.game = game;
		this.grs = new GameClientAdapter();
	}

	@Override
	protected Boolean call() {
		try {
			this.result =  grs.updateGame(game);
		} catch (RuntimeException | GameNotFoundException e) {
			this.errorMessage = e.getMessage();
		}
		return result;
	}

	public boolean getResult() {
		return result;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
}
