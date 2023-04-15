package hello.itemservice.web.validation;
import hello.itemservice.domain.item.Item;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
@Component
public class ItemValidator implements Validator {
    //Validator 인터페이스 (스프링이 제공하는 검증기)

    /**
     * 해당 검증기를 지원하는 여부 확인(뒤에서 설명)
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return Item.class.isAssignableFrom(clazz);
        //item == clazz 인가
        //item == subItem 인가 (자식 클래스인가)
    }

    /**
     * 검증 대상 객체와 BindingResult
     * @param target the object that is to be validated
     * @param errors contextual state about the validation process
     */
    @Override
    public void validate(Object target, Errors errors) {//Errors 는 BindingResult 의 부모 클래스
        Item item = (Item) target;

//기존 검증 절차
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "itemName",
                "required");
        if (item.getPrice() == null || item.getPrice() < 1000 ||
                item.getPrice() > 1000000) {
            errors.rejectValue("price", "range", new Object[]{1000, 1000000},
                    null);
        }
        if (item.getQuantity() == null || item.getQuantity() > 10000) {
            errors.rejectValue("quantity", "max", new Object[]{9999}, null);
        }
        //특정 필드 예외가 아닌 전체 예외
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                errors.reject("totalPriceMin", new Object[]{10000,
                        resultPrice}, null);
            }
        }
//기존 검증 절차
    }
}