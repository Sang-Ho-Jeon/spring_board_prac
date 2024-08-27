package com.fastcampus.ch4.dao;

import com.fastcampus.ch4.domain.BoardDto;

import java.util.List;
import java.util.Map;

public interface BoardDao {
    BoardDto select(Integer bno);
    List<BoardDto> selectAll();
    List<BoardDto> selectPage(Map map);

    int insert(BoardDto boardDto);

    int update(BoardDto boardDto);
    int increaseViewCnt(Integer bno);

    int delete(Integer bno, String writer);
    int deleteAll();

    int count();
}
