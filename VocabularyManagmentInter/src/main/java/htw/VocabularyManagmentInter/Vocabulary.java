package htw.VocabularyManagmentInter;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
public class Vocabulary {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int vocabularyId;
	
	@ManyToMany
	@JoinTable(name="word_language_one_list")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Word> word_language_one;
	
	@ManyToMany
	@JoinTable(name="word_language_two_list")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Word> word_language_two;
	
	public Vocabulary(List<Word> word_language_one, List<Word> word_language_two) {
		super();
		this.word_language_one = word_language_one;
		this.word_language_two = word_language_two;
	}

	public Vocabulary() {
		super();
	}

	public List<Word> getWord_language_one() {
		return word_language_one;
	}
	
	public void setWord_language_one(List<Word> word_language_one) {
		this.word_language_one = word_language_one;
	}
	
	public List<Word> getWord_language_two() {
		return word_language_two;
	}
	
	public void setWord_language_two(List<Word> word_language_two) {
		this.word_language_two = word_language_two;
	}
	
	@Override
	public String toString() {
		return word_language_one.toString() + ", word_language_two=" + word_language_two + "]";
	}


	public int getVocabularyId() {
		return vocabularyId;
	}


	public void setVocabularyId(int vocabularyId) {
		this.vocabularyId = vocabularyId;
	}
}
