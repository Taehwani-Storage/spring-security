<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<html>
<head>
    <title>Uploaded Files</title>
</head>
<body>
<h1>Uploaded Files</h1>
<c:forEach items="${list}" var="i">
    <h2>${file.fileName}</h2>
    <img src="/upload/${i.filePath}" alt="${i.fileName}"/>
</c:forEach>
</body>
</html>
