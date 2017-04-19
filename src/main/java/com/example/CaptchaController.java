package com.example;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class CaptchaController {
	
	@Value("${validationUrl}")
	private String validationUrl;
	
	@Value("${secret}")
	private String secret;
	
    @RequestMapping(value="/captcha", method=RequestMethod.GET)
    public String captchaGet(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "captcha";
    }
    
    @RequestMapping(value="/captcha", method=RequestMethod.POST)
    public String captchaPost(@ModelAttribute("g-recaptcha-response") String captchaResponse, Model model, HttpServletRequest req) throws JsonParseException, JsonMappingException, IOException {
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    	MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
    	map.add("secret", secret);
    	map.add("response", captchaResponse);
    	map.add("remoteip", req.getRemoteAddr());

    	HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

    	RestTemplate restTemplate = new RestTemplate();
    	ResponseEntity<String> response = restTemplate.postForEntity( validationUrl, request , String.class );
    	
    	ObjectMapper mapper = new ObjectMapper();
    	Map<String, Object> responseMap = new HashMap<String, Object>();
    	
    	responseMap = mapper.readValue(response.getBody(),  new TypeReference<Map<String, Object>>() {});
    	
    	model.addAttribute("success", responseMap.get("success"));
    	model.addAttribute("challenge_ts", responseMap.get("challenge_ts"));
    	model.addAttribute("hostname", responseMap.get("hostname"));
    	model.addAttribute("errorcodes", responseMap.get("error-codes"));
    	
        return "captcha";
    }

}
