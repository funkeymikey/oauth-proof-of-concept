package oauth.provider.controllers;

import oauth.provider.objects.Wrapper;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @name Test
 * @description An endpoint to simply tell whether the service is working or not
 */
@Controller
@RequestMapping("test")
public class TestController{

	/**
	 * @uri /test
	 * @method GET
	 * @description Make a GET request, and get back a message
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public Wrapper<String> test() {		
		return new Wrapper<String>("service is up");
	}
}
