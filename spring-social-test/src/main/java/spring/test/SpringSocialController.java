package spring.test;

import javax.servlet.http.HttpServletRequest;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SpringSocialController {
	
	@RequestMapping(value = "/")
	@ResponseBody
	public String index(final HttpServletRequest request) {
		if(request.getParameter("code")!=null){
			return SpringSocial.getInstance().getID("facebook", request.getParameter("code"));
		}else{
			return "<a href=\""+SpringSocial.getInstance().getURL("facebook")+"\">Code link</a>";
		}
	}

}
