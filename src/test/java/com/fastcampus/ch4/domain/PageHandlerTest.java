package com.fastcampus.ch4.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class PageHandlerTest {
    @Test
    public void test() {
        PageHandler ph = new PageHandler(250, 1, 10);
        ph.print();
        assertTrue(ph.getBeginPage() == 1);
        assertTrue(ph.getEndPage() == 10);
        assertTrue(ph.isShowPrev() == false);
        assertTrue(ph.isShowNext() == true);
        assertTrue(ph.getTotalPage() == 25);
    }

    @Test
    public void test2() {
        PageHandler ph = new PageHandler(250, 11, 10);
        ph.print();
        assertTrue(ph.getBeginPage() == 11);
        assertTrue(ph.getEndPage() == 20);
        assertTrue(ph.isShowPrev() == true);
        assertTrue(ph.isShowNext() == true);
        assertTrue(ph.getTotalPage() == 25);
    }

    @Test
    public void test3() {
        PageHandler ph = new PageHandler(255, 25, 10);
        ph.print();
        assertTrue(ph.getBeginPage() == 21);
        assertTrue(ph.getEndPage() == 26);
        assertTrue(ph.isShowPrev() == true);
        assertTrue(ph.isShowNext() == false);
        assertTrue(ph.getTotalPage() == 26);
    }

    @Test
    public void test4() {
        PageHandler ph = new PageHandler(255, 20, 10);
        ph.print();
        assertTrue(ph.getBeginPage() == 11);
        assertTrue(ph.getEndPage() == 20);
        assertTrue(ph.isShowPrev() == true);
        assertTrue(ph.isShowNext() == true);
        assertTrue(ph.getTotalPage() == 26);
    }
}