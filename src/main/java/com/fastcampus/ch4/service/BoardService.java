package com.fastcampus.ch4.service;

import com.fastcampus.ch4.domain.BoardDto;

import java.util.List;
import java.util.Map;

public interface BoardService {
    BoardDto read(Integer bno);

    List<BoardDto> getList();

    List<BoardDto> getPage(Map map);

    int write(BoardDto boardDto);

    int modify(BoardDto boardDto);

    int remove(Integer bno, String writer);

    int getCount();
}
