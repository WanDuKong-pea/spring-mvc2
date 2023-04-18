package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
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

    /* 아래 코드 생략_Bean Validation 적용을 위함(검증기 중복)
    @InitBinder
    public void init(WebDataBinder dataBinder){
        log.info("init binder {}", dataBinder);
        dataBinder.addValidators(itemValidator);
        //아래와 같이 여러개 validator를 등록 가능
        //validator의 support()메서드로 검증 대상을 구분함
        //dataBinder.addValidators(userValidator);
        //dataBinder.addValidators(xxxValidator);
    }
     */

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
        //global error는 이후 강의에서 설명

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
        return "validation/v1/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v3/items/{itemId}";
    }

}

