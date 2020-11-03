package htw.RestAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.injectors.ConstructorInjection;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import htw.VocabularyManagmentImpl.VocabularyManagementDaoImpl;
import htw.VocabularyManagmentImpl.VocabularyManagement_Service_Impl;
import htw.VocabularyManagmentInter.DouplicateVocabularyListException;
import htw.VocabularyManagmentInter.Vocabulary;
import htw.VocabularyManagmentInter.VocabularyList;
import htw.VocabularyManagmentInter.VocabularyListDTO;
import htw.VocabularyManagmentInter.VocabularyManagement_Service;


@RestController
@RequestMapping("/vocab_service")
public class VocabularyManagement_Service_Controller  {
	
	@GetMapping("/get")
	public List<VocabularyList> getAllVocabularyLists()  {
		VocabularyManagement_Service vocabularyManagmenetService = registerComponents();
		List<VocabularyList> vocabularyLists =  vocabularyManagmenetService.getAllVocabularyLists();
		return vocabularyLists;
	}
	
	@GetMapping("/get/maininfo")
	public List<VocabularyListDTO> getAllVocabularyListsMainInformation()  {
		VocabularyManagement_Service vocabularyManagmenetService = registerComponents();
		List<VocabularyListDTO> vocabularyListsMI =  vocabularyManagmenetService.getAllVocabularyListsMainInformation();
		return vocabularyListsMI;
	}

	@PutMapping("/create")
	public VocabularyList createVocabularyList(@RequestBody Map<String,Object> map) throws DouplicateVocabularyListException {
		VocabularyManagement_Service vocabularyManagmenetService = registerComponents();
		ObjectMapper objectMapper = new ObjectMapper();
		List<Vocabulary> vocabularys = new ArrayList<Vocabulary>();
		
		String book =  objectMapper.convertValue(map.get("book"), String.class);
		String chapter =  objectMapper.convertValue(map.get("chapter"), String.class);
		String language1 =  objectMapper.convertValue(map.get("language1"), String.class);
		String language2 =  objectMapper.convertValue(map.get("language2"), String.class);
		List<Map<String,Object>> vocabularyMaps = objectMapper.convertValue(map.get("vocabularys"), List.class );
		
		for(Map vocab: vocabularyMaps) {
			vocabularys.add(objectMapper.convertValue(vocab,Vocabulary.class));
		}
		VocabularyList vocabList = vocabularyManagmenetService.createVocabularyList(book, chapter, vocabularys, language1,language2);
		return vocabList;
	}
		
	private VocabularyManagement_Service registerComponents() {
		MutablePicoContainer container = new DefaultPicoContainer(new ConstructorInjection());	
		EntityManager em = EntityManagerService.getInstance().getEntityManagerFactory().createEntityManager();
		container.addComponent(VocabularyManagementDaoImpl.class);
		container.addComponent(VocabularyManagement_Service_Impl.class);
		container.addComponent(em);
		return container.getComponent(VocabularyManagement_Service_Impl.class);
	}
}
