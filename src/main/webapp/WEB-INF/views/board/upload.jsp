<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="/user/logout" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    <input type="submit" value="로그아웃하기">
</form>
<%-- 이진법으로 변환해서 POST로 보내달라는 의미(파일명말고 파일 데이터를 쪼개서 보내달라는 의미 --%>
<%-- 데이터의 경우, 기본적으로 text 형태로 보내짐 --%>
<%-- 파일의 경우, 기본적으로 파일명만 보내줌 --%>
<a href="/user/logout">로그아웃하기</a>
<form action="/board/upload" method="post" enctype="multipart/form-data">
  <input type="file" name="file" multiple="multiple">
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
  <input type="submit" value="저장하기">
</form>
</body>
</html>
