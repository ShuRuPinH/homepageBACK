package classes;


import org.springframework.web.bind.annotation.*;

@RestController
public class WebController {

    @GetMapping ("/notes")
    String json (){
        return "Json String";

    }

    @PostMapping("/add")
    String put (@RequestBody String str){
       return ("ADD :\n" +str);

    }


    @PostMapping("/sms")
    String sms (@RequestBody String str){
        return  ("SMS :\n" +str);

    }


}
