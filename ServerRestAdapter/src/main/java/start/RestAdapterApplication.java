package start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import htw.RestAdapter.Errorhandler;
import htw.RestAdapter.GameManagement_Service_Controller;
import htw.RestAdapter.PlayerManagement_Service_Controller;
import htw.RestAdapter.VocabularyManagement_Service_Controller;

@SpringBootApplication(scanBasePackageClasses ={Errorhandler.class, 
												GameManagement_Service_Controller.class, 
												PlayerManagement_Service_Controller.class, 
												VocabularyManagement_Service_Controller.class} )
public class RestAdapterApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestAdapterApplication.class, args);
	}
}