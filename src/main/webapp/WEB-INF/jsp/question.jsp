<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ms" uri="http://epam.mksn.by/MentalAid/tag/msTags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:bundle basename="pagecontent" prefix="question.">
  <c:set var="isAdmin" value="${(not empty sessionScope.user) and (sessionScope.user.role eq 1)}"/>
  <html lang="${sessionScope.locale}">
    <head>
      <title><c:out value="${requestScope.question.title}"/> | Mental Aid</title>
      <jsp:include page="template/links.jsp"/>
      <link rel="stylesheet" href="<c:url value="/css/question.css"/>">
      <link rel="stylesheet" href="<c:url value="/css/stars.css"/>">
    </head>
    <body>
      <jsp:include page="template/navbar.jsp">
        <jsp:param name="isNavbarLess" value="false"/>
        <jsp:param name="fromUrl" value="question+${requestScope.question.id}"/>
      </jsp:include>
      <div class="container">
        <c:set var="isQuestionOwner"
               value="${(not empty sessionScope.user) and (requestScope.question.creatorId eq sessionScope.user.id)}"/>
        <div class="question">
          <h3><c:out value="${requestScope.question.title}"/></h3>
          <hr class="question-separator">
          <p class="description"><c:out value="${requestScope.question.description}"/></p>
          <h5 class="text-right">
            Asked by <a class="${isQuestionOwner ? 'owner' : ''}" href="#">${requestScope.question.creatorUsername}</a>
            at <span class="glyphicon glyphicon-time"></span> <fmt:formatDate
              value="${requestScope.question.createdAt}" type="both" dateStyle="long" timeStyle="short"/>
          </h5>
          <h6 class="edited-caption ${(requestScope.question.createdAt ne requestScope.question.modifiedAt) ? '' : 'hidden'}">
            <span class="glyphicon glyphicon-edit"></span> Edited at <fmt:formatDate
              value="${requestScope.question.modifiedAt}" type="both" dateStyle="long" timeStyle="short"/></h6>
          <c:if test="${isAdmin or isQuestionOwner}">
            <div class="text-right">
              <a type="button" class="btn btn-custom btn-edit">Edit</a>
              <a type="button" class="btn btn-custom btn-delete">Delete</a>
            </div>
          </c:if>
        </div>

        <div class="answers">
          <div class="answers-header">
            <a class="anchor" id="answers"></a>
            <h3>${requestScope.question.answerCount}
              <c:choose>
                <c:when test="${requestScope.question.answerCount eq 1}">
                  Answer
                </c:when>
                <c:otherwise>
                  Answers
                </c:otherwise>
              </c:choose>
            </h3>
            <hr class="question-separator">
          </div>
          <c:forEach items="${requestScope.answers}" var="answer">
            <c:set var="isAnswerOwner"
                   value="${(not empty sessionScope.user) and (answer.creatorId eq sessionScope.user.id)}"/>
            <c:if test="${isAnswerOwner}">
              <c:set var="isAnswerExists" value="true"/>
            </c:if>
            <div class="answer" id="answer-${answer.id}">
              <div class="media">
                <div class="media-body ${isAnswerOwner ? 'owner-border' : ''}">
                  <p class="answer-text">
                    <c:out value="${answer.text}"/>
                  </p>
                  <div>
                    <div class="col-md-6 text-left">
                      <ms:starRating value="${answer.averageMark}"/>
                    </div>
                    <h5 class="media-heading col-md-6 text-right">by <a class="${isAnswerOwner ? 'owner' : ''}"
                                                                        href="#">${answer.creatorUsername}</a> at <span
                        class="glyphicon glyphicon-time"></span><span
                        class="create-date"><fmt:formatDate value="${answer.createdAt}"
                                                            type="both" dateStyle="long" timeStyle="short"/></span></h5>
                    <h6 class="edited-caption ${(answer.createdAt ne answer.modifiedAt) ? '' : 'hidden'}"><span
                        class="glyphicon glyphicon-edit"></span> Edited at <span class="edit-date"><fmt:formatDate
                        value="${answer.modifiedAt}" type="both" dateStyle="long" timeStyle="short"/></span></h6>
                  </div>
                  <c:if test="${isAdmin or isAnswerOwner}">
                    <div class="col-xs-12 text-right">
                      <a type="button" class="btn btn-custom btn-edit">Edit</a>
                      <a type="button" class="btn btn-custom btn-delete">Delete</a>
                    </div>
                  </c:if>
                </div>
                <hr>
              </div>
            </div>
          </c:forEach>
        </div>
        <div class="well">
          <c:choose>
            <c:when test="${(not empty sessionScope.user) and (not isQuestionOwner) and (empty isAnswerExists)}">
              <form role="form">
                <div class="form-group">
                  <h4><label for="new-answer">Leave an answer:</label></h4>
                  <textarea id="new-answer" class="form-control" rows="3"></textarea>
                </div>
                <button type="submit" class="btn btn-primary">Submit</button>
              </form>
            </c:when>
            <c:when test="${isQuestionOwner}">
              You cannot answer to your own question, you can edit your question instead.
            </c:when>
            <c:when test="${isAnswerExists}">
              You cannot post two answers, edit existed answer instead.
            </c:when>
            <c:otherwise>
              To leave an answer, <a
                href="<c:url value="/controller?cmd=login&from=question+${requestScope.question.id}"/>">Login</a> or <a
                href="<c:url value="/controller?cmd=register&from=question+${requestScope.question.id}"/>">Register</a>
            </c:otherwise>
          </c:choose>
        </div>
      </div>
      <jsp:include page="template/footer.jsp"/>
    </body>
  </html>
</fmt:bundle>