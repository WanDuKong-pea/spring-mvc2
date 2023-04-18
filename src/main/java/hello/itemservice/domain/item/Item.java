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

    //Bean Validation 한계(동일한 모델 객체를 등록, 수정시 각각 다르게 검증 어려움)를 해결하는 방법
    //1. Bean Validation의 groups를 사용(지금 강의)
    //2. Item을 직접 사용하지 않고, 폼 전송을 위한 별도의 모델 객체를 만들어 사용(다음 강의)
        //실무에서는 2번을 주로 사용

    @NotNull(groups = UpdateCheck.class) //Bean Validation 애노테이션에 그룹 적용
    private Long id;

    @NotBlank(groups = {SaveCheck.class, UpdateCheck.class})
    private String itemName;
    //@NotBlank : 빈값 + 공백만 있는 경우를 허용하지 않음
    //@NotBlank(message="공백X")와 같이 기본 메시지를 지정해둘 수 있음

    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    @Range(min = 1000, max = 1000000, groups = {SaveCheck.class, UpdateCheck.class})
    private Integer price;
    //@NotNull : null을 허용하지 않음
    //@Range(min = 값, max = 값): 범위 안의 값이어야 함

    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    @Max(value= 9999, groups = SaveCheck.class)
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
