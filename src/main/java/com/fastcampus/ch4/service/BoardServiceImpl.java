package com.fastcampus.ch4.service;

import com.fastcampus.ch4.dao.BoardDao;
import com.fastcampus.ch4.domain.BoardDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BoardServiceImpl implements BoardService {
    @Autowired
    BoardDao boardDao;

    /*
        1. 게시물 한개 읽기 + 게시물 조회수 증가
        2. 게시물 전체 읽기
        3. 게시물 페이지 읽기(offset, pageSize)
        4. 게시물 등록
        5. 게시물 수정
        6. 게시물 삭제
        7. 게시물 개수 카운트
    */

    @Override
    public BoardDto read(Integer bno) throws Exception {
        BoardDto boardDto = boardDao.select(bno);
        boardDao.increaseViewCnt(bno);

        return boardDto;
    }

    @Override
    public List<BoardDto> getList() throws Exception {
        return boardDao.selectAll();
    }

    @Override
    public List<BoardDto> getPage(Map map) throws Exception {
        return boardDao.selectPage(map);
    }

    @Override
    public int write(BoardDto boardDto) throws Exception {
        return boardDao.insert(boardDto);
    }

    @Override
    public int modify(BoardDto boardDto) throws Exception {
        return boardDao.update(boardDto);
    }

    @Override
    public int remove(Integer bno, String writer) throws Exception {
        return boardDao.delete(bno, writer);
    }

    @Override
    public int getCount() throws Exception {
        return boardDao.count();
    }
}
