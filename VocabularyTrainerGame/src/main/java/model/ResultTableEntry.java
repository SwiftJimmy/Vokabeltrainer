package model;

public class ResultTableEntry {
	
	private String round;
	private String question;
	private String ansewrPlayer1;
	private String player1Result; 
	private String ansewrPlayer2;
	private String player2Result;
	
	public ResultTableEntry(String round, String question, String ansewrPlayer1, String player1Result,
			String ansewrPlayer2, String player2Result) {
		super();
		this.round = round;
		this.question = question;
		this.ansewrPlayer1 = ansewrPlayer1;
		this.player1Result = player1Result;
		this.ansewrPlayer2 = ansewrPlayer2;
		this.player2Result = player2Result;
	}

	public String getRound() {
		return round;
	}

	public String getQuestion() {
		return question;
	}

	public String getAnsewrPlayer1() {
		return ansewrPlayer1;
	}

	public String getPlayer1Result() {
		return player1Result;
	}

	public String getAnsewrPlayer2() {
		return ansewrPlayer2;
	}

	public String getPlayer2Result() {
		return player2Result;
	}
}
