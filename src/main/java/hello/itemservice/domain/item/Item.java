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

    @NotNull
    @Max(9999)
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
