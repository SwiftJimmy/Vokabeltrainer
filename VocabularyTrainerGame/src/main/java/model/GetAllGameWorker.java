package model;

import java.util.List;
import htw.ClientAdapter.GameClientAdapter;
import htw.GameManagmentInter.Game;
import htw.PlayerManagementInter.Player;
import javafx.concurrent.Task;

public class GetAllGameWorker extends Task<List<Game>> {
	
	private Player player;
	private GameClientAdapter grs;
	private List<Game> games;
	private String errorMessage;
	
	public GetAllGameWorker(Player player) {
		super();
		this.player = player;
		this.grs = new GameClientAdapter();
	}

	@Override
	protected List<Game> call() {
		try {
			this.games =  grs.getAllGamesByPlayer(this.player.getId());
		} catch (RuntimeException e) {
			this.errorMessage = e.getMessage();
		}
		return games;
	}

	public List<Game> getGames() {
		return games;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
