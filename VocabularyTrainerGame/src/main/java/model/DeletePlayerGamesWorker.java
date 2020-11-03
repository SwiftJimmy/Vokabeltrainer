package model;

import htw.ClientAdapter.GameClientAdapter;
import htw.PlayerManagementInter.Player;
import javafx.concurrent.Task;

public class DeletePlayerGamesWorker extends Task<Boolean> {
	
	private Player player;
	private String errorMessage;
	private GameClientAdapter gca;
	private boolean result= false;
	
	public DeletePlayerGamesWorker(Player player) {
		super();
		this.player = player;
		this.gca = new GameClientAdapter();
	}

	@Override
	protected Boolean call() {
		
		try {
			result = gca.deleteAllGamesByPlayer(player.getId());
		} catch (RuntimeException e) {
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
	
	public Player getPlayer() {
		return player;
	}
}
