package nextstep.helloworld.mvc.handler;

import nextstep.helloworld.mvc.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/return-value")
public class ReturnValueController {
    @GetMapping("/message")
    public ResponseEntity<String> string() {
        return ResponseEntity.ok("message");
    }

    @GetMapping("/users")
    public ResponseEntity<User> responseBodyForUser() {
        return ResponseEntity.ok(new User("name", "email"));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> responseEntity(@PathVariable Long id) {
        return ResponseEntity.ok(new User("name", "email"));
    }

    @GetMapping("/members")
    public ResponseEntity responseEntityFor400() {
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/thymeleaf")
    public String thymeleaf(Model model) {
        model.addAttribute("name", "HK");
        return "sample";
    }
}