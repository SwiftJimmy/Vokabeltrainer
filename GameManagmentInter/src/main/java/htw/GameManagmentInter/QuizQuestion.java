package htw.GameManagmentInter;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import htw.VocabularyManagmentInter.Word;

@Entity
public class QuizQuestion {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int quizQuestionId;
	
	@ManyToOne
	private Word questionWord;
	
	@ManyToOne
	private Word answerWord;
	
	@ManyToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Word> wrongAnswers; // beinhaltet alle falsche Antworten
	

	public QuizQuestion(Word questionWord, Word answerWord, List<Word> wrongAnswers) {
		super();
		this.questionWord = questionWord;
		this.answerWord = answerWord;
		this.wrongAnswers = wrongAnswers;
	}
	
	public QuizQuestion() {
		super();
	}

	public Word getQuestionWord() {
		return questionWord;
	}
	
	public void setQuestionWord(Word questionWord) {
		this.questionWord = questionWord;
	}
	
	public Word getAnswerWord() {
		return answerWord;
	}
	
	public void setAnswerWord(Word answerWord) {
		this.answerWord = answerWord;
	}
	
	public List<Word> getWrongAnswers() {
		return wrongAnswers;
	}
	
	public void setWrongAnswers(List<Word> wrongAnswers) {
		this.wrongAnswers = wrongAnswers;
	}
	
	public int getQuizQuestionId() {
		return quizQuestionId;
	}

	public void setQuizQuestionId(int quizQuestionId) {
		this.quizQuestionId = quizQuestionId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((answerWord == null) ? 0 : answerWord.hashCode());
		result = prime * result + ((questionWord == null) ? 0 : questionWord.hashCode());
		result = prime * result + quizQuestionId;
		result = prime * result + ((wrongAnswers == null) ? 0 : wrongAnswers.hashCode());
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
		QuizQuestion other = (QuizQuestion) obj;
		if (answerWord == null) {
			if (other.answerWord != null)
				return false;
		} else if (!answerWord.equals(other.answerWord))
			return false;
		if (questionWord == null) {
			if (other.questionWord != null)
				return false;
		} else if (!questionWord.equals(other.questionWord))
			return false;
		if (quizQuestionId != other.quizQuestionId)
			return false;
		if (wrongAnswers == null) {
			if (other.wrongAnswers != null)
				return false;
		} else if (!wrongAnswers.equals(other.wrongAnswers))
			return false;
		return true;
	}
}
