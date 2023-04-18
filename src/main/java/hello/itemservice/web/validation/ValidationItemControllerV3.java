package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.SaveCheck;
import hello.itemservice.domain.item.UpdateCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/validation/v3/items")
@RequiredArgsConstructor
public class ValidationItemControllerV3 {

    //@RequiredArgsConstructor + 생성자 하나 = 자동 생성자 주입
    //@AutoWired 사용 안해도 됨
    private final ItemRepository itemRepository;
    //아래 코드 생략_Bean Validation 적용을 위함(검증기 중복)
    //private final ItemValidator itemValidator;


    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v3/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v3/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v3/addForm";
    }

    /*
    @PostMapping("/add")
    public String addItem(@Validated @ModelAttribute Item item, BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {
        //@Validated: 검증기를 실행하라는 애노테이션, 겁증 대상(item) 앞에 붙임.
        //@Validated 애노테이션이 붙으면 자동으로 Bean Validator를 인지하고 스프링에 통합함
            //<- spring-boot-starter-validation 라이브러리가 추가되어 있어야 함
            //스프링 부트는 자동으로 LocalValidatorFactroyBean을 글로벌 Validator로 등록
            //이 Validator는 @NotNull 같은 애노테이션을 보고 검증을 수행
                //(@Valid, @Validated만 적용하면 됨)
        //@ModelAttribute는 각각의 필드 타입 변환시도 변환에 성공한 필드만 BeanValidation 적용
            //타입 변환에 실패한 필드는 typeMisMatch로 FieldError 추가됨.

        //글로벌 에러
        if(item.getPrice()!= null && item.getQuantity() != null){
            int resultPrice = item.getPrice()*item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.reject("totalPriceMin", new Object[]{10000,
                resultPrice}, null);
            }
        }

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "validation/v3/addForm";
        }
        //성공로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
    }
     */

    @PostMapping("/add")
    public String addItemV2(@Validated(SaveCheck.class) @ModelAttribute Item item, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {
        //@Validated(그룹인터페이스.class)로 groups 적용

        //글로벌 에러
        if(item.getPrice()!= null && item.getQuantity() != null){
            int resultPrice = item.getPrice()*item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.reject("totalPriceMin", new Object[]{10000,
                        resultPrice}, null);
            }
        }

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "validation/v3/addForm";
        }
        //성공로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v3/editForm";
    }

    /*
    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @Validated @ModelAttribute Item item,
                       BindingResult bindingResult) {
        //글로벌 에러
        if(item.getPrice()!= null && item.getQuantity() != null){
            int resultPrice = item.getPrice()*item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.reject("totalPriceMin", new Object[]{10000,
                        resultPrice}, null);
            }
        }

        if(bindingResult.hasErrors()){
            log.info("errors={}",bindingResult);
            return "validation/v3/editForm";
        }

        //검증 성공 로직
        itemRepository.update(itemId, item);
        return "redirect:/validation/v3/items/{itemId}";
    }
    */

    @PostMapping("/{itemId}/edit")
    public String editV2(@PathVariable Long itemId, @Validated(UpdateCheck.class) @ModelAttribute Item item,
                       BindingResult bindingResult) {
        //글로벌 에러
        if(item.getPrice()!= null && item.getQuantity() != null){
            int resultPrice = item.getPrice()*item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.reject("totalPriceMin", new Object[]{10000,
                        resultPrice}, null);
            }
        }

        if(bindingResult.hasErrors()){
            log.info("errors={}",bindingResult);
            return "validation/v3/editForm";
        }

        //검증 성공 로직
        itemRepository.update(itemId, item);
        return "redirect:/validation/v3/items/{itemId}";
    }
}

