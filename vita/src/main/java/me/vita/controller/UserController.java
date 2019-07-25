package me.vita.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import me.vita.domain.UserVO;
import me.vita.dto.UserDTO;
import me.vita.security.Auth;
import me.vita.service.UserService;

@Controller
@RequestMapping("/user")
/*
 * 회원가입페이지, 로그인페이지, 회원가입 처리, 로그인처리(interceptor), 유저정보
 */
public class UserController {
	
	@Autowired
	UserService service;
	
	@GetMapping("/new")
	public String signupview(){
		return "signup";
	}
	
	@GetMapping("/rank")
	public String rankview(){
		return "ranking";
	}
	
	@PostMapping(value="/new")
	public String register(UserVO userVO, RedirectAttributes rttr)throws Exception {
		
		service.register(userVO);
		return "redirect:login";
	}
	
	@PostMapping(value="/idcheck", produces ="application/json; charset=UTF-8")
	@ResponseBody
	public Map<Object, Object> idcheck(@RequestBody String userId){
		int count = service.getUserIdcnt(userId);
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("cnt", count);
		
		return map;

		
	}
	
	
	@GetMapping("/login")
	public String view() {
		return "login";
	}
	
	//로그인
	@PostMapping("/login")
	//interceptor 처리 / 로그인 실패시 작동
	public ModelAndView login(@RequestParam("id") String id, @RequestParam("pw") String pw, ModelAndView mav) {
		String password = service.getPw(id);
		if(password==null){
			//입력한 id에 해당하는 pw가 db에 없을때
			mav.setViewName("loginfail");
			mav.addObject("response", "id가 존재하지 않음");
		}else{
			if(password.equals(pw)){
				//id와 pw가 모두 db에 존재할때
				if(service.getAuthstatus(id).equals("T")){
					//이메일 인증 완료시
					mav.setViewName("loginsuccess");
					mav.addObject("response", "id,pw 일치, email인증 완료");
				}else{
					//이메일 인증 미완료
					mav.setViewName("emailAuth");
					mav.addObject("id", id);
					mav.addObject("response", "id,pw 일치, email인증 필요");
				}
			}else{
				//id는 DB에 있지만 pw가 다를때
				mav.setViewName("loginfail");
				mav.addObject("response", "id존재 pw불일치");
			}
		}
		return mav;
	}
	

	@GetMapping("/{userId}")
	@ResponseBody
	@Auth
	public UserDTO get(@SessionAttribute("authUser") UserVO user, @PathVariable("userId") String userId) {
		UserDTO userDTO = service.get(user.getUserId(), userId);
		if(user.getUserId().equals(userId)) {
			userDTO.setIsFollow("me");
		}
		return userDTO;
	}
	
	
	
}
