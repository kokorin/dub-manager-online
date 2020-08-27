package dmo.server.api.v2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/v2/greet")
public class GreetController {

    @GetMapping
    public String greet(@RequestParam(defaultValue = "username") String name) {
        return "Greetings, " + name + "!";
    }
}