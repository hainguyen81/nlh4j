<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/tags/Common.inc" %>

<!DOCTYPE html>
<html lang="en">
<head>
<%@ include file="/WEB-INF/tags/Meta.inc" %>
<link rel="icon" type="image/png" href="${resourcePath}/favicon.ico" />
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
<title><nlh4j:message code="C001.title" /></title>

<!-- ------------------------------------------------- -->
<!-- STYLESHEET -->
<!-- ------------------------------------------------- -->
<%@ include file="/WEB-INF/tags/app/AppCssLibs.inc" %>

<%-- exex - Styles --%>
<%@ include file="/WEB-INF/tags/app/AppCss.inc" %>

<%-- exex - Screen Style --%>
<nlh4jTags:Css src="C0000/C0001.css" theme="${appTheme}" />

<!-- autocomplete: off, autocorrect: off, autocapitalize: off, spellcheck: false --> 
<tag autocomplete="off" autocorrect="off" autocapitalize="off" spellcheck="false"/>
</head>
<body>
	<div class="page-body">
		<div id="loginModal" style="background-color: #286090;"
			class="modal show" tabindex="-1" role="dialog" aria-hidden="true">
			<div class="modal-dialog" style="width: 400px; margin-top:100px;">
				<div class="modal-content">
					<div class="modal-header" style="padding: 0px;">
						<button type="button" class="close" data-dismiss="modal"
							aria-hidden="true"></button>
						<h1 class="text-center">
							<nlh4j:message code="C001.title" />
						</h1>
					</div>

					<div class="modal-body">
						<form role="form" class="form col-md-12 center-block"
						action="<c:url value='j_spring_security_check' />" method="POST">
							<!-- token -->
							<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

							<!-- errors -->
							<c:if test="${not empty error}">
								<div class="error" role="alert">
									<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
									<span class="sr-only"><nlh4j:message code="C001.error" />:</span> ${error}
								</div>
							</c:if>

							<!-- errors -->
							<div class="form-group">
								<div style="margin-bottom: 15px; width: 100%" class="input-group">
									<span class="input-group-addon border-radius-3 border-radius-left"
									style="width: 35px; background-color: #417aab; border-color: #417aab; color: #ffffff">
										<i class="fa fa-user"></i>
									</span>
									<input id="login-username" type="text"
									autocomplete="off" style="margin-bottom: 0; margin-top: 0"
									class="form-control border-radius-3 border-radius-right" name="username"
									placeholder='<nlh4j:message code="C001.user_name"/>'>
								</div>
							</div>
							<div class="form-group">
								<div style="margin-bottom: 25px; width: 100%" class="input-group">
									<span class="input-group-addon border-radius-3 border-radius-left"
									style="width: 35px; background-color: #417aab; border-color: #417aab; color: #ffffff">
										<i class="fa fa-lock"></i>
									</span>
									<input class="form-control border-radius-3 border-radius-right"
									autocomplete="off"
									placeholder='<nlh4j:message code="C001.password"/>'
									name="password" type="password">
								</div>
							</div>
							<div class="form-group" style="height: 15px">
								<div style="float: left; width: auto;">
									<input id="j_remember" name="rememberMe" type="checkbox">
								</div>
								<div style="float: left; width: auto; margin-left: 5px;">
									<label for="j_remember">
										<nlh4j:message code="C001.remember.password" />
									</label>
								</div>
							</div>
							<div class="form-group" style="text-align: center;">
								<button type="submit" class="btn btn-login" style="width: 50%">
									<nlh4j:message code="button.login" />
								</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>