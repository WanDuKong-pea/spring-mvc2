package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
@Slf4j
public class ValidationItemControllerV2 {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }

    /*
    //BindingResult2 강의 <- BindingResult에 대한 상세 설명
    //https://round-backbone-23e.notion.site/BindingResult2-46ff1f43ccf1421ba30a0e5a0f1e01f7
    @PostMapping("/add")
    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult,
                          RedirectAttributes redirectAttributes, Model model) {

        //검증 로직
        if(!StringUtils.hasText(item.getItemName())){
            //FieldError <- 필드 단위의 에러들을 저장하는 스프링 제공 객체
            bindingResult.addError(new FieldError("item","itemName","상품 이름은 필수 잆니다."));
        }

        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
            bindingResult.addError(new FieldError("item","price","가격은 1,000~1,000,000 까지 허용합니다."));
        }
        if(item.getQuantity() == null || item.getQuantity() >= 9999){
            bindingResult.addError(new FieldError("item","quantity","수량은 최대 9,999까지 허용합니다."));
        }

        //특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice()*item.getQuantity();
            if(resultPrice < 10000){
                //필드 에러가 아닌 글로벌에러 -> ObjectError,
                //objectName은 모델에트리뷰트 파라미터 변수를 담음
                bindingResult.addError(new ObjectError("item","가격 * 수량은 10,000원 이상이어야 합니다. " +
                        "현재 값 = " + resultPrice));
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()){
            log.info("errors={}",bindingResult);
            //모델에 담지 않아도 됨
            //BindingResult는 자동으로 뷰에 같이 넘어감
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }
    */

    @PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes){
        if(!StringUtils.hasText(item.getItemName())){
            bindingResult.addError(
                    new FieldError("item", "itemName", item.getItemName(),
                    false, null, null,"상품 이름은 필수 입니다."));
        }

        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
            bindingResult.addError(
                    new FieldError("item","price", item.getPrice(), false,
                    null, null, "가격은 1,000 ~ 1,000,000까지 허용합니다"));
        }

        if(item.getQuantity() == null || item.getQuantity() > 10000){
            bindingResult.addError(
                    new FieldError("item","quantity",item.getQuantity(), false,
                    null, null, "수량은 최대 9,999까지 허용합니다."));
        }

        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.addError(new ObjectError("item",null,null,
                        "가격*수량의 합은 10,000원 이상이어야 합니다. 현재 값 " + resultPrice));
            }
        }

        if(bindingResult.hasErrors()){
            log.info("errors={}",bindingResult);
            return "validation/v2/addForm";
        }

        //성공로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }

}

