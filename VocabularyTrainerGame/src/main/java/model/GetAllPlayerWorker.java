package model;

import java.util.List;
import htw.ClientAdapter.PlayerClientAdapter;
import htw.PlayerManagementInter.Player;
import javafx.concurrent.Task;

public class GetAllPlayerWorker extends Task<List<Player>> {
	
	private PlayerClientAdapter pca;
	private List<Player> players;
	private String errorMessage;
	
	public GetAllPlayerWorker() {
		super();
		this.pca = new PlayerClientAdapter();
	}

	@Override
	protected List<Player> call() {
		try {
			this.players =  pca.getAllPlayer();
		} catch (RuntimeException e) {
			this.errorMessage = e.getMessage();
		}
		return players;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
