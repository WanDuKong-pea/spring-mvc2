package hello.itemservice.domain.item;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
//@ScriptAssert(lang = "javascript", script = "_this.price * _this.quantity >= 10000")
//@ScriptAssert는 제약이 많고 복잡, 해당 객체의 범위를 넘어서는 검증 또한 불가.
public class Item {

    //Bean Validation 한계
    //수정 추가 요구사항 -> id값이 필수, quantity에 최댓값 제약 없음
    //수정은 잘 동작하지만 등록에서 문제가 발생
    //등록과 수정은 같은 Bean Validation을 적용할 수 없음

    @NotNull
    private Long id;

    @NotBlank(message="상품 이름은 필수입니다.")
    private String itemName;
    //@NotBlank : 빈값 + 공백만 있는 경우를 허용하지 않음
    //@NotBlank(message="공백X")와 같이 기본 메시지를 지정해둘 수 있음

    @NotNull
    @Range(min = 1000, max = 1000000)
    private Integer price;
    //@NotNull : null을 허용하지 않음
    //@Range(min = 값, max = 값): 범위 안의 값이어야 함

    //@Max(9999)
    @NotNull
    private Integer quantity;
    //@Max(값): 최대치를 넘기는 값 허용하지 않음

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }


}
