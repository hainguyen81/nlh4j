<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/tags/Common.inc" %>

<nlh4jTags:Html module="errModule">
	<nlh4jTags:Head title="400">
		<nlh4jTags:Js src="error/error.js"></nlh4jTags:Js>
	</nlh4jTags:Head>
	<nlh4jTags:Body appController="ErrorCtrl"
	hideChat="${!nlh4j:isSocketChatProfile()}"
	hideNotification="${!nlh4j:isSocketNotificationProfile()}">
		<nlh4jTags:Layout>
			<div style="width: 100%; text-align: center;">
				<nlh4j:message code="400" number="400" systemError="true" />
			</div>
			<div style="width: 100%; text-align: center; padding-top: 10px">
				<c:url var="backUrl" value="/dashboard" />
				<a href="${backUrl}" class="btn btn-xs btn-back"
				title="<spring:message code="button.back" />">
					<spring:message code="button.back" />
				</a>
			</div>
		</nlh4jTags:Layout>
	</nlh4jTags:Body>
</nlh4jTags:Html>