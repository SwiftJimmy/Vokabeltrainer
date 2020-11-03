package htw.VocabularyManagmentInter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@Entity
@NamedQueries({
	@NamedQuery(name="Word.allWordsByLanguage", query="SELECT i FROM Word AS i WHERE i.language = :language"),
	@NamedQuery(name="Word.getWordByLanguageAndWord", query="SELECT i FROM Word AS i WHERE i.language = :language and i.word = :word")
})
public class Word {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int wordId;
	private String word; 
	private String language;
	
	public Word(String word, String language) {
		super();
		this.word = word;
		this.language = language;
	}
	
	public Word() {}

	public int getWordId() {
		return wordId;
	}

	public void setWordId(int wordId) {
		this.wordId = wordId;
	}

	public String getWord() {
		return word;
	}
	
	public void setWord(String word) {
		this.word = word;
	}
	
	public String getLanguage() {
		return language;
	}
	
	public void setLanguage(String language) {
		this.language = language;
	}

	@Override
	public String toString() {
		return "Word [word=" + word + ", language=" + language + "]";
	}
}
