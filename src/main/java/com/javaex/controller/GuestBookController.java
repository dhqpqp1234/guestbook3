package com.javaex.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.javaex.dao.GuestBookDao;
import com.javaex.vo.GuestBookVo;

@Controller
public class GuestBookController {
	//리스트
	@RequestMapping(value="/addList", method = {RequestMethod.GET, RequestMethod.POST})
	public String list(Model model) {
		System.out.println("addList");
		
		GuestBookDao guestbookDao = new GuestBookDao();
		List<GuestBookVo> gList = guestbookDao.guestbookList();
		System.out.println(gList);
		model.addAttribute("gList", gList);
		return "/WEB-INF/views/addList.jsp";
	}
	//등록
	@RequestMapping(value="/add", method= {RequestMethod.POST, RequestMethod.GET})
	public String add(@RequestParam ("name") String name,
					@RequestParam ("password") String password,
					@RequestParam("content")String content) {
		GuestBookVo guestbookVo = new GuestBookVo(name, password, content);
		GuestBookDao guestbookDao = new GuestBookDao();
		guestbookDao.insert(guestbookVo);
		System.out.println("add");
		return "redirect:/addList";
	}
	//삭제폼
	@RequestMapping(value="/deleteForm", method= {RequestMethod.POST, RequestMethod.GET})
	public String deleteForm() {
		System.out.println("deleteForm");
		return"/WEB-INF/views/deleteForm.jsp";
	}
	//삭제
	@RequestMapping(value="/delete", method = {RequestMethod.POST, RequestMethod.GET})
	public String delete(@RequestParam("no") int no,
						@RequestParam("password") String password) {
		System.out.println("delete");
		GuestBookDao guestbookDao = new GuestBookDao();
		GuestBookVo guestbookVo = guestbookDao.getGuest(no);
		
		if(guestbookVo.getPassword().equals(password)) {
			guestbookDao.delete(guestbookVo);
			
		}
		return "redirect:/addList";
		
	}
}