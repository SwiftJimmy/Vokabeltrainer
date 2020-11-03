package model;

import htw.ClientAdapter.PlayerClientAdapter;
import htw.PlayerManagementInter.DuplicatePlayerException;
import htw.PlayerManagementInter.Player;
import javafx.concurrent.Task;

public class CreatePlayerWorker extends Task<Player> {
	
	private String name;
	private PlayerClientAdapter pca;
	private Player player;
	private String errorMessage;
	
	public CreatePlayerWorker(String name) {
		super();
		this.name = name;
		this.pca = new PlayerClientAdapter();
	}

	@Override
	protected Player call() {
		try {
			this.player =  pca.createPlayer(name);
		} catch (RuntimeException | DuplicatePlayerException  e) {
			this.errorMessage = e.getMessage();
		}
		return player;
	}

	public Player getPlayer() {
		return player;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
