<%@ tag pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/tags/Common.inc" %>

<!-- START::top navigation -->
<nlh4jTags:Script>$('.navbar.navbar-fixed-top').autoHidingNavbar();</nlh4jTags:Script>
<nav role="navigation"
class="navbar navbar-inverse navbar-fixed-top box-shadow skin-14">
	<c:url var="dashboardUrl" value="/dashboard" />
    <div class="container page-header"
    style="width: 90%;">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="${dashboardUrl}?h=true&<%=System.currentTimeMillis()%>"
            style="font-family: 'Sigmar One', cursive; font-weight: 100; font-size: 22px; overflow: hidden; white-space: nowrap; text-overflow: ellipsis">
            	${appTitle}
           	</a>
        </div>

        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
            	<c:set var="requestURI" value="${pageContext.request.requestURI}" />
            	<!-- START::@TODO Waiting for private home page -->
				<%--
                <c:url var="homepageUrl" value="/index" />
            	--%>
                <!-- END::@TODO Waiting for private home page -->

				<!-- START::@TODO Waiting for private home page -->
				<%--
                <c:set var="homeActive" value="${fn:endsWith(requestURI, 'views/index.jsp')}" />
                --%>
                <!-- END::@TODO Waiting for private home page -->
                <c:set var="dashboardActive" value="${fn:endsWith(requestURI, 'views/dashboard/index.jsp')}" />

				<!-- START::@TODO Waiting for private home page -->
				<%--
                <li class="${homeActive ? 'active' : 'none'}"><a href="${homepageUrl}"><i class="fa fa-home"></i>&nbsp;<nlh4j:message code='login.form.label.homepage' /></a></li>
                --%>
                <!-- END::@TODO Waiting for private home page -->
                <li class="${dashboardActive ? 'active' : 'none'}">
                	<a href="${dashboardUrl}?h=1&<%=System.currentTimeMillis()%>">
                		<i class="fa fa-tachometer"></i>
                		<nlh4j:message code="C2001.title" />
                	</a>
                </li>
                <li class="dropdown"
                ng-if="data && data.pageInfo && data.pageInfo.languages && data.pageInfo.languages.length">
                    <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                    	<i class="fa fa-language"
                    	ng-if="data && data.language && data.language.code && data.language.code.length">
                    		{{data.language.name}}
                    	</i>
                    	<i class="fa fa-language"
                    	ng-if="!data || !data.language || !data.language.code || !data.language.code.length">
                    		${languageCaption}
                    	</i><b class="caret"></b>
                    </a>
                    <ul role="menu" class="dropdown-menu">
                        <li class="{{language.icon}}"
                        ng-repeat="language in data.pageInfo.languages"
                        ng-if="language.code && language.code.length">
                       		<a href="?lang={{language.code}}"
                       		ng-if="data.pageInfo.locale == language.code"><strong>{{language.name}}</strong></a>
                        	<a href="?lang={{language.code}}"
                        	ng-if="data.pageInfo.locale != language.code">{{language.name}}</a>
						</li>
                    </ul>
                </li>
                <sec:authorize access="isAuthenticated()">
                	<c:if test="${companies != null && companies.size() > 0}">
                		<li class="dropdown">
		                    <a data-toggle="dropdown" class="dropdown-toggle" href="#">
		                    	<i class="fa fa-building"></i>
		                   		<c:if test="${company == null}"><nlh4j:message code="application.company" /></c:if>
                   				<c:if test="${company != null}">${company.getCode()}</c:if>
		                   		<b class="caret"></b>
		                    </a>
		                    <ul role="menu" class="dropdown-menu">
		                    	<!-- Empty company (only system administrator) -->
		                    	<c:if test="${systemAdmin}">
			                    	<li>
			                        	<a href="?c=0" style="padding-left: 10px; font-size: 13px;">
		                        			<hr style="border-top: 1px dashed #fff; height: 1px; padding: 0; margin: 10px 0 10px 5px;" />
	                        			</a>
									</li>
								</c:if>
								<!-- Companies list -->
		                    	<c:forEach items="${companies}" var="comp">
									<li>
			                        	<c:if test="${company != null && company.getId() eq comp.getId()}">
			                        		<a href="?c=${comp.getId()}"
			                        		style="padding-left: 10px; font-size: 13px;"
			                        		class="fa fa-building"
			                        		title="${comp.getName()}">
			                        			<span style="font-weight: 300;">
			                        				${comp.getName()}
			                        			</span>
		                        			</a>
			                        	</c:if>
			                        	<c:if test="${company == null || company.getId() ne comp.getId()}">
			                        		<a href="?c=${comp.getId()}"
			                        		style="padding-left: 10px; font-size: 13px;"
			                        		class="fa fa-building"
			                        		title="${comp.getName()}">
			                        			<span>
			                        				${comp.getName()}
			                        			</span>
		                        			</a>
			                        	</c:if>
									</li>
								</c:forEach>
		                    </ul>
		                </li>
                	</c:if>
                </sec:authorize>
                <li style="pointer-events: none;">
                	<ds-widget-clock show-digital digital-format="'EEEE, ${cmnDateTimePattern} HH:mm:ss'" class="clock" />
                </li>
            </ul>

        	<sec:authorize access="isAuthenticated()">
	            <ul class="nav navbar-nav navbar-right">
	                <li class="dropdown">
						<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">
	                   	<nlh4j:message code="application.welcome" />
	                   	<c:set var="userName"><sec:authentication property="principal.username" /></c:set>
	                   	${userName}
	                    <span class="caret"></span></a>
	                    <ul class="dropdown-menu" role="menu">
	                    	<c:url var="logoutUrl" value="/logout" />
	                    	<c:url var="changePassWordUrl" value="/chgpwd/edit" />
		                   	<li>
			                   	<form name="chgpwdForm" modelAttribute="pk" novalidate="novalidate"
			                   	method="POST" action="${changePassWordUrl}">
								  <a type="button" class="btn btn-primary btn-sm btn-block"
								  href="javascript:void(0);"
								  onclick="javascript:$('[name=chgpwdForm]').submit();"
								  style="padding-left: 10px; padding-right: 0; border-radius: 0; margin: 0; text-align: left;">
										<span class="glyphicon glyphicon-lock"></span>
										<nlh4j:message code="button.change.password" />
								  </a>
								  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
								  <input type="hidden" name="username" value="${userName}"/>
								</form>
		                   	</li>
		                   	<li>
			                   	<form action="${logoutUrl}" method="POST">
								  <button type="submit" class="btn btn-primary btn-sm btn-block"
								  style="padding-left: 10px; padding-right: 0; border-radius: 0; margin: 0; text-align: left;">
										<span class="glyphicon glyphicon-log-out"></span>
										<nlh4j:message code="button.logout" />
								  </button>
								  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
								</form>
		                   	</li>
	                    </ul>
	                </li>
	            </ul>
            </sec:authorize>
            <sec:authorize access="isAnonymous()">
				<ul class="nav navbar-nav navbar-right">
					<c:url var="loginUrl" value="/login" />
	                <li><a href="${loginUrl}"><span class="glyphicon glyphicon-log-in"></span>&nbsp;<nlh4j:message code="button.login" /></a></li>
            	</ul>
           	</sec:authorize>
        </div>

        <!--/.nav-collapse -->
    </div>
</nav>
<!-- END::top navigation -->