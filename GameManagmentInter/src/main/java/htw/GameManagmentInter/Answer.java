package htw.GameManagmentInter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import htw.PlayerManagementInter.Player;
import htw.PlayerManagementInter.ScoreResult;

@Entity
public class Answer {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int answerId;
	
	@ManyToOne
	private Player player;
	
	private String answer;
	private boolean correctAnswer;
	
	private ScoreResult resultat;
	
	public Answer( Player player) {
		super();
		this.player = player;
		this.answer = null;
		this.resultat = null;
		this.correctAnswer = false;
	}
	
	public Answer() {
		super();
	}

	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public String getAnswer() {
		return answer;
	}
	
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	public ScoreResult getResultat() {
		return resultat;
	}
	
	public void setResultat(ScoreResult resultat) {
		this.resultat = resultat;
	}

	public int getAnswerId() {
		return answerId;
	}

	public void setAnswerId(int answerId) {
		this.answerId = answerId;
	}

	public boolean isCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(boolean correctAnswer) {
		this.correctAnswer = correctAnswer;
	}
}
