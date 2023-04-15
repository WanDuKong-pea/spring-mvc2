package hello.itemservice.validation;
import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;
import static org.assertj.core.api.Assertions.assertThat;
public class MessageCodesResolverTest {
    MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();

    @Test
    void messageCodesResolverObject(){
        //reject("required")_ObjectError 일 때
        //MessageCodesResolver 는 아래의 활동을 자동으로 함
        String[] messageCodes = codesResolver.resolveMessageCodes("required","item");
        assertThat(messageCodes).containsExactly("required.item","required");

    }

    @Test
    void messageCodesResolverField(){
        //rejectValue("itemName","required")_FieldError 일 때
        //MessageCodesResolver 는 아래의 활동을 자동으로 함
        String[] messageCodes = codesResolver.resolveMessageCodes("required","item","itemName",String.class);
        assertThat(messageCodes).containsExactly(
                "required.item.itemName",
                "required.itemName",
                "required.java.lang.String",
                "required"
        );
        // 이후 new FieldError("item","itemName",item.getItemName(),false,messageCodes,null, null);
        // 와 같이 FieldError 를 생성해서 반환하는 것
    }

    //오류 메시지 출력
    //타임리프 화면을 렌더링시 th:error 가 실행될 때 오류가 있다면
    //생성된 오류 메시지 코드(MessageCodesResolver에 의한)를 순서대로 돌아가면서
    //메시지를 찾음 그리고 없으면 디폴트 메시지 출력
}