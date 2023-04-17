package hello.itemservice.validation;
import hello.itemservice.domain.item.Item;
import org.junit.jupiter.api.Test;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
public class BeanValidationTest {
    @Test
    void beanValidation() {
        //참고만 하기 - 스프링과 통합하면 우리가 직접 아래 코드를 작성하지 않음
        //이렇게 사용하는구나 정도만 알아두기
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Item item = new Item();

        //아래 오류 값들 셋팅
        item.setItemName(" "); //공백
        item.setPrice(0);
        item.setQuantity(10000);

        Set<ConstraintViolation<Item>> violations = validator.validate(item);
        //violations가 빈값이면 오류가 없는 것
        //violations에 값이 있다면 오류가 있는 것

        for (ConstraintViolation<Item> violation : violations) {
            System.out.println("violation=" + violation);
            System.out.println("violation.message=" + violation.getMessage());
        }

    }
}