<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>로그인 페이지</title>
</head>
<body>
<h1>로그인 페이지</h1>
<img src="/resources/Img.jpg">
<form action="/user/auth" method="post">
    아이디: <input type="text" name="username">
    비밀번호: <input type="password" name="password">
    기억하기: <input type="checkbox" id="remember-me" name="remember-me">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

    <input type="submit" value="로그인">
</form>
<a href="/user/oAuth2/kakao">카카오 로그인</a>
</body>
</html>
