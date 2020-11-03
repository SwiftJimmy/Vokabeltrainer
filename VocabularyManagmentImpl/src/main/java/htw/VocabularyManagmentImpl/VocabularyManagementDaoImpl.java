package htw.VocabularyManagmentImpl;

import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import htw.VocabularyManagmentInter.VocabManagementServiceException;
import htw.VocabularyManagmentInter.VocabularyList;
import htw.VocabularyManagmentInter.VocabularyListDTO;
import htw.VocabularyManagmentInter.VocabularyManagementDao;
import htw.VocabularyManagmentInter.Word;

public class VocabularyManagementDaoImpl implements VocabularyManagementDao {

	private EntityManager entityManager;
	
	public VocabularyManagementDaoImpl(EntityManager entityManager) {
	super();
	this.entityManager = entityManager;
}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	@Override
	public List<Word> getAllWordsByLanguage(String language) {
		try {
			TypedQuery<Word> query = entityManager.createNamedQuery("Word.allWordsByLanguage", Word.class);
			return query.setParameter("language", language).getResultList(); 
			
		} catch (RuntimeException exp) {
			System.err.println(exp.getMessage());
			close();
			throw new VocabManagementServiceException(exp);
		}
		
	}
	
	@Override
	public List<VocabularyList> getAllVocabularyLists() {
		try {
			TypedQuery<VocabularyList> query = entityManager.createNamedQuery("VocabularyList.getAll", VocabularyList.class);
			return query.getResultList(); 
		} catch (RuntimeException exp) {
			System.err.println(exp.getMessage());
			close();
			throw new VocabManagementServiceException(exp);
		}
	}
	
	@Override
	public VocabularyList getVocabularyListByBookAndChapter(String book, String chapter) {
		try {
			
			TypedQuery<VocabularyList> query = entityManager.createNamedQuery("VocabularyList.getByBookAndChapter", VocabularyList.class);
			
			return query.setParameter("book", book).setParameter("chapter", chapter).getSingleResult();		

		} catch (NoResultException exp) {
			return null;
		}catch (RuntimeException exp) {
			System.err.println(exp.getMessage());
			close();
			throw new VocabManagementServiceException(exp);
		}
	}
	
	@Override
	public VocabularyList saveVocabularyList(VocabularyList vocabularyList) {
		try {
			entityManager.persist(vocabularyList);
			return vocabularyList;	
		} catch (RuntimeException exp) {
			System.err.println(exp.getMessage());
			close();
			throw new VocabManagementServiceException(exp);
		}
	}
	
	@Override
	public VocabularyList getVocabularyList(int vocabularyListId) {
		try {
			return entityManager.find(VocabularyList.class, vocabularyListId);
		} catch (RuntimeException exp) {
			System.err.println(exp.getMessage());
			close();
			throw new VocabManagementServiceException(exp);
		}
	}
	
	@Override
	public Word saveWord(Word word) {
		try {
			entityManager.persist(word);
			return word;
		} catch (RuntimeException exp) {
			System.err.println(exp.getMessage());
			close();
			throw new VocabManagementServiceException(exp);
		}
	}

	@Override
	public Word getWordByWordAndLanguage(String word, String language) {
		try {
			
			TypedQuery<Word> query = entityManager.createNamedQuery("Word.getWordByLanguageAndWord", Word.class);
			
			return query.setParameter("language", language).setParameter("word", word).getSingleResult();		

		} catch (NoResultException exp) {
			return null;
		}catch (RuntimeException exp) {
			System.err.println(exp.getMessage());
			close();
			throw new VocabManagementServiceException(exp);
		}
	}
	
	@Override
	public List<VocabularyListDTO> getAllVocabularyListMainInformation() {
		try {
			TypedQuery<VocabularyListDTO> query = entityManager.createNamedQuery("VocabularyList.getMainData", VocabularyListDTO.class);
			return query.getResultList(); 
		} catch (RuntimeException exp) {
			System.err.println(exp.getMessage());
			close();
			throw new VocabManagementServiceException(exp);
		}
	}

	@Override
	public void begin() {
		try {
			if(!this.entityManager.getTransaction().isActive())
				this.entityManager.getTransaction().begin();
		}catch (RuntimeException exp) {
			System.err.println(exp.getMessage());
			close();
			throw new VocabManagementServiceException(exp);
		}
		
	}

	@Override
	public void commit() {
		try {
			this.entityManager.getTransaction().commit();
		}catch (RuntimeException exp) {
			System.err.println(exp.getMessage());
			close();
			throw new VocabManagementServiceException(exp);
		}
	}
	
	@Override
	public void close() {
		try {
			if(this.entityManager.isOpen()) {
				this.entityManager.close();
			};
		}catch (RuntimeException exp) {
			System.err.println(exp.getMessage());
			throw new VocabManagementServiceException(exp);
		}
	}
}
