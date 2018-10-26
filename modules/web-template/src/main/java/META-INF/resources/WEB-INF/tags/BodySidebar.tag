<%@ tag pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/tags/Common.inc" %>

<!-- START::sidebar -->
<sec:authorize access="isAuthenticated()">
	<nav class="navbar navbar-inverse box-shadow sidebar" role="navigation">
	    <div class="container-fluid">
			<div class="collapse navbar-collapse" id="bs-sidebar-navbar-collapse-1">
				<ul class="nav navbar-nav">
					<c:if test="${sidebar != null && sidebar.size() > 0}">
						<c:forEach var="child" items="${sidebar}">
				            <nlh4jTags:SidebarTree node="${child}" activeUrl="${curUrl}" />
				        </c:forEach>
					</c:if>
					<c:if test="${sidebar == null || sidebar.size() <= 0}">
						<nlh4jTags:Script>
							// iterate scope prototype
							angular.element(document).ready(function() {
								var angularAppEl = document.querySelector('[ng-app]');
								var angularScope = angular.element(angularAppEl).scope();
								angularScope && angularScope.$apply(function() {
									// call AJAX to showing side-bar if session invalid
									angularScope.getSidebar();
								});
							});
						</nlh4jTags:Script>
						<sidebar module="item" ng-repeat="item in sidebar" ng-cloak />
					</c:if>
				</ul>
			</div>
		</div>
	</nav>
</sec:authorize>
<!-- END::sidebar -->