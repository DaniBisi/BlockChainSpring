package magenta.BlockChainSpring;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingsRestController {
	@GetMapping("/hi")
	String hi() {
		return "hello";
	}

}
