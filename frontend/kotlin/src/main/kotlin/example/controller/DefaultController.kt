package example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class DefaultController {

  @GetMapping("/")
  fun index(): String {
      return "forward:/index.html"
  }
  @GetMapping("/language")
  fun language(): String {
      return "forward:/language.html"
  }
  @GetMapping("/samples")
  fun samples(): String {
      return "forward:/samples.html"
  }
}