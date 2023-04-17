package hello.itemservice;

import hello.itemservice.web.validation.ItemValidator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ItemServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItemServiceApplication.class, args);
	}

	//WebDataBinder 글로벌 설정 시 사용. (잘 안씀_BeanValidator가 자동 등록되지 않음)
	//컨트롤러에서 @initBinder를 사용하지 않아도 정상 작동
	//implements WebMvcConfigurer
	//@Override public Validator getValidator() { return new ItemValidator();}

}
