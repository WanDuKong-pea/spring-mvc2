package hello.itemservice.domain.item;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class Item {

    private Long id;

    @NotBlank
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
