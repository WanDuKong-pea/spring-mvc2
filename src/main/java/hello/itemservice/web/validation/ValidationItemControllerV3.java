package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
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
    private final ItemValidator itemValidator;
    private final ItemRepository itemRepository;

    /*
    //위의 코드로 대체 됨.
    @Autowired
    public ValidationItemControllerV3(ItemValidator itemValidator, ItemRepository itemRepository) {
        this.itemValidator = itemValidator;
        this.itemRepository = itemRepository;
    }
    */


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

    /**
     * WebDataBinder에 검증기를 추가하면 해당 컨트롤러에서는
     * 검증기를 자동으로 적용할 수 있음
     * @initBinder: 해당 컨트롤러에만 영향
     * 글로벌은 별도 설정 필요
     * @param dataBinder
     */
    @InitBinder
    public void init(WebDataBinder dataBinder){
        log.info("init binder {}", dataBinder);
        dataBinder.addValidators(itemValidator);
        //아래와 같이 여러개 validator를 등록 가능
        //validator의 support()메서드로 검증 대상을 구분함
        //dataBinder.addValidators(userValidator);
        //dataBinder.addValidators(xxxValidator);
    }

    @PostMapping("/add")
    public String addItemV6(@Validated @ModelAttribute Item item, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes){
        //파라미터에 @Validated 추가됨 <- 검증기를 실행하라는 애노테이션
        //겁증 대상(item) 앞에 붙임
        //@Validated 애노테이션이 붙으면
        //앞서 WebDataBinder에 등록한 검증기를 찾아 실행후 BindingResult 에 검증 결과를 담음

        //이전 버전 코드 생략 됨.
        //itemValidator.validate(item,bindingResult);

        if(bindingResult.hasErrors()){
            log.info("errors={}",bindingResult);
            return "validation/v3/addForm";
        }
        //성공로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId",savedItem.getId());
        redirectAttributes.addAttribute("status",true);
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

