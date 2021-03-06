package com.example;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Controller
public class CaptchaController {
	
	private Log logger = LogFactory.getLog(CaptchaController.class);
	
	@Value("${validationUrl}")
	private String validationUrl;
	
	@Value("${secret}")
	private String secret;
	
    @RequestMapping(value="/captcha", method=RequestMethod.GET)
    public String captchaGet() {
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
    	ResponseEntity<CaptchaResponse> response = restTemplate.postForEntity(validationUrl, request, CaptchaResponse.class);
    	
    	logger.info("response status code: " + response.getStatusCode() + " " 
    			+ response.getStatusCode().getReasonPhrase());
    	logger.info("response: " + response.getBody());
    	
    	model.addAttribute("captchaResponse", response.getBody());
    	
        return "captcha";
    }

}
