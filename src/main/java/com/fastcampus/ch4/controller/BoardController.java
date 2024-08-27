package com.fastcampus.ch4.controller;

import com.fastcampus.ch4.domain.BoardDto;
import com.fastcampus.ch4.domain.PageHandler;
import com.fastcampus.ch4.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/board")
public class BoardController {
    @Autowired
    BoardService boardService;

    @PostMapping("/remove")
    public String remove(Integer bno, Integer page, Integer pageSize, Model m, HttpSession session, RedirectAttributes rattr) {
        /*
            (1) 세션에 저장된 작성자 id가져오기
            (2) boardService.remove(bno, writer)로 해당 게시물 삭제
            (3) 삭제 실패(삭제된 row == 0) -> 예외발생, 에러 메시지 전달
            (4) 삭제 성공 -> 성공 메시지 전달
            메시지 전달에 RedirectAttribute 활용. 세션에 일회용 저장.
         */
        String writer = (String)session.getAttribute("id");

        try {
            m.addAttribute("page", page);
            m.addAttribute("pageSize", pageSize);

            int rowCnt = boardService.remove(bno,writer);
            if (rowCnt != 1)
                throw new Exception("board remove error");

            rattr.addFlashAttribute("msg", "DEL_OK");
        } catch (Exception e) {
            e.printStackTrace();
            rattr.addFlashAttribute("msg", "DEL_ERR");
        }

        return "redirect:/board/list";
    }

    @GetMapping("/read")
    public String read(Integer bno, Integer page, Integer pageSize, Model m) {
        /*
            (1) boardService.read(bno)로 게시물 하나(BoardDto) 읽어오기
            (2) boardDto객체와 page, pageSize를 board.jsp에 model로 전달
         */
        try {
            BoardDto boardDto = boardService.read(bno);
            m.addAttribute(boardDto);
            m.addAttribute("page", page);
            m.addAttribute("pageSize", pageSize);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return "board";
    }

    @GetMapping("/list")
    public String list(Integer page, Integer pageSize, Model m, HttpServletRequest request) {
        if(!loginCheck(request))
            return "redirect:/login/login?toURL="+request.getRequestURL();  // 로그인을 안했으면 로그인 화면으로 이동

        /*
            (1) page와 pageSize를 매개변수로 받아서 offset과 pageSize를 map에 저장
            (2) boardService.getCount()로 총 게시물 개수 받아온 다음 PageHandler객체 생성
            (3) boardService.getPage(map)로 게시판 목록받아오기
            (4) PageHandler객체와 게시판 목록을 모델로 boardList.jsp에 전달
         */
        if(page == null) page = 1;
        if(pageSize == null) pageSize = 10;

        try {
            Map map = new HashMap();
            map.put("offset", (page - 1) * pageSize);
            map.put("pageSize", pageSize);

            int totalCnt = boardService.getCount();
            PageHandler ph = new PageHandler(totalCnt, page, pageSize);

            List<BoardDto> list = boardService.getPage(map);
            m.addAttribute("list", list);
            m.addAttribute("ph", ph);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return "boardList"; // 로그인을 한 상태이면, 게시판 화면으로 이동
    }

    private boolean loginCheck(HttpServletRequest request) {
        // 1. 세션을 얻어서
        HttpSession session = request.getSession();
        // 2. 세션에 id가 있는지 확인, 있으면 true를 반환
        return session.getAttribute("id")!=null;
    }
}