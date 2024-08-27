package com.fastcampus.ch4.dao;

import com.fastcampus.ch4.domain.BoardDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BoardDaoImpl implements BoardDao {

    @Autowired
    private SqlSession session;
    private static String namespace = "com.fastcampus.ch4.dao.BoardMapper.";

    /*
        1. 게시물 한개 읽기
        2. 게시물 전체 읽기
        3. 게시물 페이지 읽기(offset, pageSize)
        4. 게시물 등록
        5. 게시물 수정
        6. 게시물 조회수 증가
        7. 게시물 삭제
        8. 게시물 전체 삭제
        9. 게시물 개수 카운트
    */

    @Override
    public BoardDto select(Integer bno) throws Exception { // 발생한 예외를 서비스 계층으로 넘겨줘야됨
        return session.selectOne(namespace + "select", bno);
    }
    @Override
    public List<BoardDto> selectAll() throws Exception {
        return session.selectList(namespace + "selectAll");
    }
    @Override
    public List<BoardDto> selectPage(Map map) throws Exception {
        return session.selectList(namespace + "selectPage", map);
    }

    @Override
    public int insert(BoardDto boardDto) throws Exception {
        return session.insert(namespace + "insert", boardDto);
    }

    @Override
    public int update(BoardDto boardDto) throws Exception {
        return session.update(namespace + "update", boardDto);
    }
    @Override
    public int increaseViewCnt(Integer bno) throws Exception {
        return session.update(namespace + "increaseViewCnt", bno);
    }

    @Override
    public int delete(Integer bno, String writer) throws Exception {
        Map map = new HashMap();
        map.put("bno", bno);
        map.put("writer", writer);
        return session.delete(namespace + "delete", map);
    }
    @Override
    public int deleteAll() throws Exception {
        return session.delete(namespace + "deleteAll");
    }

    @Override
    public int count() throws Exception {
        return session.selectOne(namespace + "count");
    }
}
