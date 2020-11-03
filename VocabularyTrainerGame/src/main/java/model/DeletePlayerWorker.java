package model;

import htw.ClientAdapter.PlayerClientAdapter;
import htw.PlayerManagementInter.Player;
import htw.PlayerManagementInter.PlayerNotFoundException;
import javafx.concurrent.Task;

public class DeletePlayerWorker extends Task<Boolean> {
	
	private Player player;
	private String errorMessage;
	private PlayerClientAdapter pca;
	private boolean result = false;
	
	public DeletePlayerWorker(Player player) {
		super();
		this.player = player;
		this.pca = new PlayerClientAdapter();
	}

	@Override
	protected Boolean call() {
		try {
			result = pca.deletePlayer(player.getId());
		} catch (RuntimeException | PlayerNotFoundException e) {
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
