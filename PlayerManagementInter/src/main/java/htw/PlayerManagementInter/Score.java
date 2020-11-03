package htw.PlayerManagementInter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Score {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int scoreId;
	private int amountGames;
	private int won;
	private int drawn; 
	private int lost;
	
	public Score() {
		super();
		this.amountGames = 0;
		this.won = 0;
		this.drawn = 0;
		this.lost = 0;
	}
	
	public int getScoreId() {
		return scoreId;
	}

	public void setScoreId(int scoreId) {
		this.scoreId = scoreId;
	}
	
	public void addDrawn() {
		this.drawn++;
		this.amountGames++;
	}
	
	public void addWon() {
		this.won++;
		this.amountGames++;
	}

	public void addLost() {
		this.lost++;
		this.amountGames++;
	}
		
	public int getAmountGames() {
		return amountGames;
	}
	
	public void setAmountGames(int amountGames) {
		this.amountGames = amountGames;
	}
	
	public int getWon() {
		return won;
	}
	
	public void setWon(int won) {
		this.won = won;
	}
	
	public int getDrawn() {
		return drawn;
	}
	
	public void setDrawn(int drawn) {
		this.drawn = drawn;
	}
	
	public int getLost() {
		return lost;
	}
	
	public void setLost(int lost) {
		this.lost = lost;
	}
	
	@Override
	public String toString() {
		return "Score [amountGames=" + amountGames + ", won=" + won + ", drawn=" + drawn + ", lost=" + lost + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + amountGames;
		result = prime * result + drawn;
		result = prime * result + lost;
		result = prime * result + scoreId;
		result = prime * result + won;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Score other = (Score) obj;
		if (amountGames != other.amountGames)
			return false;
		if (drawn != other.drawn)
			return false;
		if (lost != other.lost)
			return false;
		if (scoreId != other.scoreId)
			return false;
		if (won != other.won)
			return false;
		return true;
	}
}
