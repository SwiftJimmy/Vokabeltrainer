package htw.GameManagmentInter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Round {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private int number;	
	
	@OneToOne(cascade = CascadeType.ALL)
	private Answer answerPlayer1;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Answer answerPlayer2;
	
	@OneToOne(cascade = CascadeType.ALL)
	private QuizQuestion quizQuestion;
	private boolean status;
	
	public Round(int number, QuizQuestion quizQuestion,Answer answerPlayer1,Answer answerPlayer2 ) {
		super();
		this.number = number;
		this.answerPlayer1 = answerPlayer1;
		this.answerPlayer2 = answerPlayer2;
		this.quizQuestion = quizQuestion;
		this.status = true;
	}
	
	public Round() {
		super();
	}

	public int getNumber() {
		return number;
	}
	
	public void setNumber(int number) {
		this.number = number;
	}
	
	public Answer getAnswerPlayer1() {
		return answerPlayer1;
	}
	
	public void setAnswerPlayer1(Answer answerPlayer1) {
		this.answerPlayer1 = answerPlayer1;
	}
	
	public Answer getAnswerPlayer2() {
		return answerPlayer2;
	}
	
	public void setAnswerPlayer2(Answer answerPlayer2) {
		this.answerPlayer2 = answerPlayer2;
	}
	
	public QuizQuestion getQuizQuestion() {
		return quizQuestion;
	}
	
	public void setQuizQuestion(QuizQuestion quizQuestion) {
		this.quizQuestion = quizQuestion;
	}
	
	public boolean isStatus() {
		return status;
	}
	
	public void setStatus(boolean status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}