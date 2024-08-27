package com.fastcampus.ch4.controller;

import com.fastcampus.ch4.domain.BoardDto;
import com.fastcampus.ch4.domain.PageHandler;
import com.fastcampus.ch4.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/board")
public class BoardController {
    @ExceptionHandler({DataIntegrityViolationException.class, DuplicateKeyException.class, UncategorizedSQLException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String catcher() {
        return "error";
    }

    @Autowired
    BoardService boardService;

    @PostMapping("/modify")
    public String modify(BoardDto boardDto, Integer page, Integer pageSize, HttpSession session, Model m, RedirectAttributes rattr) {
        /*
            (1) 작성자 id 세션에서 받아와 boardDto 객체에 저장 - 작성자와 수정자 일치하는지 검증
            (2) boardDto를 서버로 전송
            (3) 성공시 page, pageSize /board/list로 전달하고 성공 메시지 띄우기
            (4) 실패시 boardDto객체를 다시 board.jsp로 넘겨서 수정 내용을 유지시키고 실패 메시지 띄우기
         */
        System.out.println(page + " "+ pageSize);
        String writer = (String) session.getAttribute("id");
        boardDto.setWriter(writer);
        try {
            int rowCnt = boardService.modify(boardDto);
            if (rowCnt != 1)
                throw new Exception("board modify failed");
            rattr.addAttribute("page", page);
            rattr.addAttribute("pageSize", pageSize);
            rattr.addFlashAttribute("msg", "MOD_OK");
            return "redirect:/board/list";
        } catch (Exception e) {
            m.addAttribute(boardDto);
            m.addAttribute("msg", "MOD_ERR");
            return "board";
        }
    }

    @PostMapping("/write")
    public String write(BoardDto boardDto, HttpSession session, Model m, RedirectAttributes rattr) {
        /*
            (1) 작성자 id 세션객체에서 받아서 boardDto에 저장
            (2) DB에 게시글 저장
            (3) 성공시 /board/list로 redirect 후 성공 메시지
            (4) 실패시 boardDto와 실패 메시지를 모델에 담아서 board.jsp에 전달
         */
        String writer = (String)session.getAttribute("id");
        boardDto.setWriter(writer);
        try {
            int rowCnt = boardService.write(boardDto);
            if (rowCnt != 1)
                throw new Exception("board write failed");
            rattr.addFlashAttribute("msg", "WRT_OK");
            return "redirect:/board/list";
        } catch (Exception e) {
            m.addAttribute(boardDto);
            m.addAttribute("msg", "WRT_ERR");
            return "board";
        }
    }
    @GetMapping("/write")
    public String write(Model m) {
        /*
            (1) mode(읽기/수정 상태)를 모델에 저장하고 board.jsp에게 넘겨주기
         */
        m.addAttribute("mode", "new");
        return "board"; // 읽기와 쓰기에 사용. 쓰기에 사용할때는 mode = new
    }

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
            rattr.addAttribute("msg", "DEL_ERR");
            rattr.addAttribute("page", page);
            rattr.addAttribute("pageSize", pageSize);

            int rowCnt = boardService.remove(bno,writer);
            if (rowCnt != 1)
                throw new Exception("board remove error");
            rattr.addFlashAttribute("msg", "DEL_OK");
        } catch (Exception e) {
            rattr.addFlashAttribute("msg", "DEL_ERR");
        }

        return "redirect:/board/list";
    }

    @GetMapping("/read")
    public String read( Integer bno, Integer page, Integer pageSize, Model m) {
        /*
            (1) boardService.read(bno)로 게시물 하나(BoardDto) 읽어오기
            (2) boardDto객체와 page, pageSize를 board.jsp에 model로 전달
         */

        BoardDto boardDto = boardService.read(bno);
        m.addAttribute(boardDto);
        m.addAttribute("page", page);
        m.addAttribute("pageSize", pageSize);

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


        Map map = new HashMap();
        map.put("offset", (page - 1) * pageSize);
        map.put("pageSize", pageSize);

        int totalCnt = boardService.getCount();
        PageHandler ph = new PageHandler(totalCnt, page, pageSize);

        List<BoardDto> list = boardService.getPage(map);
        m.addAttribute("list", list);
        m.addAttribute("ph", ph);

        return "boardList"; // 로그인을 한 상태이면, 게시판 화면으로 이동
    }

    private boolean loginCheck(HttpServletRequest request) {
        // 1. 세션을 얻어서
        HttpSession session = request.getSession();
        // 2. 세션에 id가 있는지 확인, 있으면 true를 반환
        return session.getAttribute("id")!=null;
    }
}