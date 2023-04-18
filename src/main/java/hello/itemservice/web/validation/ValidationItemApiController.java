package hello.itemservice.web.validation;

import hello.itemservice.domain.item.ItemSaveForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController //Http Message Body에 return 값 담음
@RequestMapping("/validation/api/items")
public class ValidationItemApiController {
    @PostMapping("/add")
    public Object addItem(@RequestBody @Validated ItemSaveForm form, BindingResult bindingResult){

        //HttpMessageConverter 단계에서 실패(타입 오류)하면 예외가 발생.
        // 예외 발생시 원하는 모양으로 예외를 처리하는 방법은 예외 처리 부분에서 다룸

        log.info("API 컨트롤러 호출");
        if(bindingResult.hasErrors()){
            log.info("검증 오류 발생 errors={}",bindingResult);
            return bindingResult.getAllErrors();
                //bindingResult가 가지고 있는 모든 에러
        }
        log.info("성공 로직 실행");
        return form;
    }
}
