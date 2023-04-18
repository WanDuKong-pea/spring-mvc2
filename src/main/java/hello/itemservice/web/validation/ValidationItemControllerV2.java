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
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {

    //@RequiredArgsConstructor + 생성자 하나 = 자동 생성자 주입
    //@AutoWired 사용 안해도 됨
    private final ItemValidator itemValidator;
    private final ItemRepository itemRepository;

    /*
    //위의 코드로 대체 됨.
    @Autowired
    public ValidationItemControllerV2(ItemValidator itemValidator, ItemRepository itemRepository) {
        this.itemValidator = itemValidator;
        this.itemRepository = itemRepository;
    }
    */


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

    /*
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
    */

    /*
    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes){
        if(!StringUtils.hasText(item.getItemName())){
            bindingResult.addError(new FieldError("item","itemName",
                    item.getItemName(),false,new String[]{"required.item.itemName"},
                    null, null));
        }

        if(item.getPrice()==null||item.getPrice() <1000 || item.getPrice()>1000000){
            bindingResult.addError(new FieldError("item","price",item.getPrice(),
                    false, new String[]{"range.item.price"},new Object[]{1000,1000000},null));
        }

        if(item.getQuantity()==null||item.getQuantity()>10000){
            bindingResult.addError(new FieldError("item","quantity",item.getQuantity(),
                    false, new String[]{"max.item.quantity"},new Object[]{9999}, null));
        }

        //글로벌 에러
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.addError(new ObjectError("item",
                        new String[]{"totalPriceMin"},new Object[]{10000, resultPrice}, null));
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
    */

    /*
    @PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes){

        //스프링이 자동으로 만든 오류 코드(ex_타입오류)가 있다면
        //바로 반환하는 방법도 있음
        if(bindingResult.hasErrors()){
            log.info("errors={}",bindingResult);
            return "validation/v2/addForm";
        }

        if(!StringUtils.hasText(item.getItemName())){
            //bindingResult.addError(new FieldError("item","itemName",
            //        item.getItemName(),false,new String[]{"required.item.itemName"},
            //        null, null));

            bindingResult.rejectValue("itemName","required");
        }


        if(item.getPrice()==null||item.getPrice() <1000 || item.getPrice()>1000000){
            bindingResult.rejectValue("price","range", new Object[]{1000,1000000},null);
        }

        if(item.getQuantity()==null||item.getQuantity()>10000){
            bindingResult.rejectValue("quantity","max", new Object[]{9999},null);
        }

        //글로벌 에러
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.reject("totalPriceMin",new Object[]{10000, resultPrice}, null);
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
    */

    /*
    @PostMapping("/add")
    public String addItemV5(@ModelAttribute Item item, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes){

        itemValidator.validate(item,bindingResult);

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
    */

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
            return "validation/v2/addForm";
        }
        //성공로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId",savedItem.getId());
        redirectAttributes.addAttribute("status",true);
        return "redirect:/validation/v2/items/{itemId}";
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
        return "redirect:/validation/v2/items/{itemId}";
    }

}

