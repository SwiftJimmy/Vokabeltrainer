package htw.GameManagmentInter;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NamedQuery;
import htw.PlayerManagementInter.Player;

@Entity
@NamedQuery(name="Game.findAllByPlayer", query="SELECT i FROM Game AS i WHERE i.player1.id = :playerId or i.player2.id = :playerId Order By i.start DESC")
public class Game {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int gameID;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	private Player player1;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	private Player player2;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "gameID")
	@Fetch(FetchMode.JOIN)
	private List<Round> rounds;
	private int pointPlayer1;
	private int pointPlayer2;
	private String fromLanguage;
	private String toLanguage;
	private long start;
	private boolean status;
	
	@Version
	private long version;
	
	public Game(Player player1, Player player2, String from,String to, List<Round> rounds) {
		super();
		this.player1 = player1;
		this.player2 = player2;
		this.pointPlayer1 = 0;
		this.pointPlayer2 = 0;
		this.fromLanguage = from;
		this.toLanguage = to;
		this.start = System.currentTimeMillis();
		this.rounds = rounds;
		this.status = true;
	}
	
	public Game() {}
	
	public int getGameID() {
		return gameID;
	}
	
	public void setGameID(int gameID) {
		this.gameID = gameID;
	}
	
	public Player getPlayer1() {
		return player1;
	}
	
	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}
	
	public Player getPlayer2() {
		return player2;
	}
	
	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}
	
	public List<Round> getRounds() {
		return rounds;
	}
	
	public void setRounds(List<Round> rounds) {
		this.rounds = rounds;
	}
	
	public int getPointPlayer1() {
		return pointPlayer1;
	}
	
	public void setPointPlayer1(int pointPlayer1) {
		this.pointPlayer1 = pointPlayer1;
	}
	
	public int getPointPlayer2() {
		return pointPlayer2;
	}
	
	public void setPointPlayer2(int pointPlayer2) {
		this.pointPlayer2 = pointPlayer2;
	}

	public String getFromLanguage() {
		return fromLanguage;
	}

	public void setFromLanguage(String fromLanguage) {
		this.fromLanguage = fromLanguage;
	}

	public String getToLanguage() {
		return toLanguage;
	}

	public void setToLanguage(String toLanguage) {
		this.toLanguage = toLanguage;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getStart() {
		return start;
	}

	public boolean isStatus() {
		return status;
	}
	
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "Game " + gameID;
	}
}
