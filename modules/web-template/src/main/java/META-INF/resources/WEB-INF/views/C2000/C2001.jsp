<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/tags/Common.inc" %>

<nlh4jTags:Html module="${pageModule}">
	<nlh4jTags:Head title="${pageModule}.title">
		<nlh4jTags:Css src="${pagePath}.css" theme="${appTheme}" />
		<nlh4jTags:Js src="${pagePath}.js" theme="${appTheme}" />
	</nlh4jTags:Head>
	<nlh4jTags:Body appController="C2000Ctrl"
	fnInit="onInit(${pageModule})" cssClass="${pageName}"
	hideChat="${!nlh4j:isSocketChatProfile()}"
	hideNotification="${!nlh4j:isSocketNotificationProfile()}">
		<nlh4jTags:Layout>
			<div class="row dashboard hi-icon-wrap hi-icon-effect-8" gridster="gridsterOptions">
				<div class="col-lg-3 col-md-6" gridster-item
				size-x="5" size-y="5"
				col="{{(($index + 1) * 6 + (2 * $index) - 4)}}"
				row="0"
				ng-if="model.modules && model.modules.length <= 4"
				ng-repeat="module in model.modules" ng-cloak
				title="{{module.display}}">
					<div class="hi-icon hi-icon-fa hi-icon-fa-icon {{module.css}}"
					such-href="{{module.link}}" ng-if="module.childs <= 0">
						<span class="title">{{module.display}}</span>
					</div>
					<div class="hi-icon hi-icon-fa hi-icon-fa-icon {{module.css}}"
					ng-click="onModule(module.id)" ng-if="module.childs > 0">
						<span class="title">{{module.display}}</span>
					</div>
				</div>
				<div class="col-lg-3 col-md-6" gridster-item
				size-x="5" size-y="5"
				ng-if="model.modules && model.modules.length > 4"
				ng-repeat="module in model.modules" ng-cloak
				title="{{module.display}}">
					<div class="hi-icon hi-icon-fa hi-icon-fa-icon {{module.css}}"
					such-href="{{module.link}}" ng-if="module.childs <= 0">
						<span class="title">{{module.display}}</span>
					</div>
					<div class="hi-icon hi-icon-fa hi-icon-fa-icon {{module.css}}"
					ng-click="onModule(module.id)" ng-if="module.childs > 0">
						<span class="title">{{module.display}}</span>
					</div>
				</div>
			</div>
		</nlh4jTags:Layout>
	</nlh4jTags:Body>
</nlh4jTags:Html>