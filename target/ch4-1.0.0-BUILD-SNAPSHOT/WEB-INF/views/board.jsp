<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>fastcampus</title>
  <link rel="stylesheet" href="<c:url value='/css/menu.css'/>">
  <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
</head>
<body>
<div id="menu">
  <ul>
    <li id="logo">fastcampus</li>
    <li><a href="<c:url value='/'/>">Home</a></li>
    <li><a href="<c:url value='/board/list'/>">Board</a></li>
    <li><a href="<c:url value='/login/login'/>">login</a></li>
    <li><a href="<c:url value='/register/add'/>">Sign in</a></li>
    <li><a href=""><i class="fas fa-search small"></i></a></li>
  </ul>
</div>
<script>
  let msg = "${msg}";
  if(msg == "WRT_ERR") alert("글쓰기 실패 ㅠㅠ");
  if(msg == "MOD_ERR") alert("수정 실패 ㅠㅠ");
</script>
<div style="text-align:center">
  <h2>${mode == "new" ? "게시물 쓰기" : "게시물 읽기"}</h2>
  <form action="" id="form">
    <input type="hidden" name="bno" value="${boardDto.bno}">
    <input type="text" name="title" value="${boardDto.title}" ${mode == "new" ? "" : "readonly='readonly'"}>
    <textarea name="content" id="" cols="30" rows="10" ${mode == "new" ? "" : "readonly='readonly'"}>${boardDto.content}</textarea>
    <button type="button" id="writeBtn" class="btn">글쓰기</button>
    <button type="button" id="modifyBtn" class="btn">수정</button>
    <button type="button" id="removeBtn" class="btn">삭제</button>
    <button type="button" id="listBtn" class="btn">목록</button>
  </form>
</div>
<script>
  $(document).ready(function() { //main()
    $("#listBtn").on("click",function() {
      location.href="<c:url value="/board/list?page=${page}&pageSize=${pageSize}"/>"
    });
    $("#removeBtn").on("click",function() {
      if(!confirm("정말로 삭제하시겠습니까?")) return;
      let form = $("#form");
      form.attr("action", "<c:url value='/board/remove?page=${page}&pageSize=${pageSize}'/>");
      <%--form.attr("action", "<c:url value='/board/remove'/>?page=${page}&pageSize=${pageSize}");--%>
      form.attr("method", "post");
      form.submit();
    });
    $("#writeBtn").on("click",function() {
      let form = $("#form");
      form.attr("action", "<c:url value='/board/write'/>");
      form.attr("method", "post");
      form.submit();
    });
    $("#modifyBtn").on("click",function() {
      // 1. 읽기 상태이면 수정 상태로 변경
      /*
        (1) form에 대한 참조 얻기
        (2) title input 태그가 readonly인지 읽어오기
        (3) 제목의 readonly가 true이면 제목, 내용의 readonly를 false로 변경
        (4) modifyBtn이름을 등록으로 변경
        (5) h2 태그의 게시물 읽기 -> 게시물 수정으로 변경
       */
      let form = $("#form");
      let isReadOnly = $("input[name=title]", "#form").attr('readonly');
      if (isReadOnly == 'readonly') {
        $("input[name=title]", "#form").attr('readonly', false); //title
        $("textarea", "#form").attr('readonly', false); //content
        $("#modifyBtn").html("등록");
        $("h2").html("게시물 수정");
        return;
      }

      // 2. 수정 상태이면, 수정된 내용을 서버로 전송
      form.attr("action", "<c:url value='/board/modify?page=${page}&pageSize=${pageSize}'/>");
      form.attr("method", "post");
      form.submit();
    });
  })
</script>
</body>
</html>