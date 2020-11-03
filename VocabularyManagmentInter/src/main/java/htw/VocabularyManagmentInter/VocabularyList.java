package htw.VocabularyManagmentInter;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.NamedNativeQuery;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@Entity
@NamedQueries({
	@NamedQuery(name="VocabularyList.getAll", query="SELECT a FROM VocabularyList a"),
	@NamedQuery(name="VocabularyList.getByBookAndChapter", query="SELECT i FROM VocabularyList AS i WHERE i.book = :book and i.chapter = :chapter")
})
@NamedNativeQuery(name = "VocabularyList.getMainData",query = "SELECT i.vocabularyListId, i.book, i.chapter, i.language1, i.language2  FROM VocabularyList i", resultSetMapping = "VocabularyListDTO")
@SqlResultSetMapping(name = "VocabularyListDTO",classes = @ConstructorResult(targetClass = VocabularyListDTO.class,columns = {@ColumnResult(name = "vocabularyListId"),@ColumnResult(name = "book"),@ColumnResult(name = "chapter"),@ColumnResult(name = "language1"),@ColumnResult(name = "language2")}))
public class VocabularyList {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int vocabularyListId;
	private String chapter; 
	private String book;
	private String language1;
	private String language2;
	
	@OneToMany(cascade = CascadeType.ALL )
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Vocabulary> vocabularies;
	
	public VocabularyList(String chapter, String book, List<Vocabulary> vocabularyList, String language1, String language2) {
		super();
		this.language1 = language1;
		this.language2 = language2;
		this.chapter = chapter;
		this.book = book;
		this.vocabularies = vocabularyList;
	}
	
	public VocabularyList() {
		super();
	}

	public VocabularyList(int vocabularyListId, String chapter, String book, List<Vocabulary> vocabularyList) {
		super();
		this.vocabularyListId = vocabularyListId;
		this.chapter = chapter;
		this.book = book;
		this.vocabularies = vocabularyList;
	}

	public List<Vocabulary> getVocabularies() {
		return vocabularies;
	}
	
	public void setVocabularies(List<Vocabulary> vocabularies) {
		this.vocabularies = vocabularies;
	}

	public String getChapter() {
		return chapter;
	}

	public void setChapter(String chapter) {
		this.chapter = chapter;
	}

	public String getBook() {
		return book;
	}

	public void setBook(String book) {
		this.book = book;
	}
	
	public int getVocabularyListId() {
		return vocabularyListId;
	}

	public void setVocabularyListId(int vocabularyListId) {
		this.vocabularyListId = vocabularyListId;
	}
	
	public String getLanguage1() {
		return language1;
	}

	public void setLanguage1(String language1) {
		this.language1 = language1;
	}

	public String getLanguage2() {
		return language2;
	}

	public void setLanguage2(String language2) {
		this.language2 = language2;
	}

	@Override
	public String toString() {
		return this.book + " " + this.chapter;
	}
}

