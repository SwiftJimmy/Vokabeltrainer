package htw.VocabularyManagmentInter;

import java.util.ArrayList;
import java.util.List;

public class VocabularyListDTO {
	
	private int id;
	private String book;
	private String chapter;
	private List<String> languages;
	
	public VocabularyListDTO(int id, String book, String chapter, String language1, String language2) {
		super();
		languages = new ArrayList<String>();
		languages.add(language1);
		languages.add(language2);
		this.id = id;
		this.book = book;
		this.chapter = chapter;
	}

	public int getId() {
		return id;
	}

	public String getBook() {
		return book;
	}

	public String getChapter() {
		return chapter;
	}

	public List<String> getLanguages() {
		
		return languages;
	}

	@Override
	public String toString() {
		return  book + " " + chapter ;
	}
}
