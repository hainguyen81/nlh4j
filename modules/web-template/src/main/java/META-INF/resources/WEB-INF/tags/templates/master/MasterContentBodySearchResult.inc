<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/tags/templates/master/MasterCommon.inc" %>


<!-- START::page data -->
<c:if test="${showResult}">
	<div class="table-reponsive">
		<!-- default search result -->
		<c:if test="${fn:length(customSearchResult) <= 0}">
			<table
			ng-keydown="onResultKeydown($event)"
			ng-keyup="onResultKeyup($event)"
			ng-keypress="onResultKeypress($event)"
			<c:if test="${(showBaseActions || showItemActions) && (viewable or insertable or updatable or deletable)}">
			class="table table-bordered table-hover table-condensed"
			</c:if>
			<c:if test="${(!showBaseActions && !showItemActions) or (!viewable && !insertable && !updatable && !deletable)}">
			class="table table-bordered no-action table-hover table-condensed"
			</c:if>
			>
				<thead>
					<tr>
						<tiles:insertAttribute name="listColumns" />
						<c:if test="${(showBaseActions || showItemActions) && (viewable or insertable or updatable or deletable)}">
							<th type="action" width="10%"
							ng-if="getData() && getData().length > 0"
							data-ar-responsive="{ 'sm': true, 'md': true, 'lg': true }"></th>
						</c:if>
					</tr>
				</thead>
				<tbody ng-if="getData() && getData().length > 0"
				ng-toggle-show
				ng-perform-show="adjustActionColumn">
					<c:if test="${fn:length(customListItems) <= 0}">
						<tr ng-repeat="data in getData()" ng-cloak
						<c:if test="${(viewable or insertable or updatable or deletable)}">
						data-action-view="<nlh4j:message code="button.viewdetail" />"
						data-action-update="<nlh4j:message code="button.edit" />"
						data-action-delete="<nlh4j:message code="button.delete" />"
						data-delete-confirm="<nlh4j:message code="COM_MSG_ID_INF_004" />"
						data-delete-confirm-title="<nlh4j:message code="${pageModule}.title" />"
						data-delete-confirm-ok="<nlh4j:message code="button.yes" />"
						data-delete-confirm-cancel="<nlh4j:message code="button.no" />"
						data-context-menu="menuOptions(data, ${viewable}, ${insertable}, ${updatable}, ${deletable})"
						</c:if>
						data-ng-form="dataRowForm_{{$index}}"
						>
							<tiles:insertAttribute name="listItems" />
							<c:if test="${(showBaseActions || showItemActions) && (viewable or insertable or updatable or deletable)}">
								<td type="action" class="text-right"
								data-ar-responsive="{ 'sm': true, 'md': true, 'lg': true }">
									<div class="action">
										<c:if test="${showItemActions}">
											<tiles:insertAttribute name="itemActions" />
										</c:if>
										<c:if test="${showBaseActions && viewable}">
											<button class="btn btn-xs btn-view"
											type="button" ng-click="onView(data)"
											title="<nlh4j:message code="button.viewdetail" />"></button>
										</c:if>
										<c:if test="${showBaseActions && (insertable || updatable)}">
											<button class="btn btn-xs btn-edit"
											type="button" ng-click="onUpdate(data)"
											title="<nlh4j:message code="button.edit" />"></button>
										</c:if>
										<c:if test="${showBaseActions && (insertable || deletable)}">
											<button class="btn btn-xs btn-delete"
											type="button" ng-click="onDelete(data)"
											data-confirm='<nlh4j:message code="COM_MSG_ID_INF_004" />'
											data-confirm-title='<nlh4j:message code="${pageModule}.title" />'
											data-confirm-ok='<nlh4j:message code="button.yes" />'
											data-confirm-cancel='<nlh4j:message code="button.no" />'
											data-success='${deleteSuccess}'
											title="<nlh4j:message code="button.delete" />"></button>
										</c:if>
									</div>
								</td>
							</c:if>
						</tr>
					</c:if>
					<c:if test="${fn:length(customListItems) > 0}">
						<tiles:insertAttribute name="customListItems" />
					</c:if>
				</tbody>
				<tbody ng-if="!getData() || getData().length <= 0">
					<tr>
						<td colspan="100%" class="border-left-light border-right-light">
							<div class="spinner style2"
							ng-if="isSearching()">
								<div class="bar"></div>
								<div class="bar"></div>
								<div class="bar"></div>
								<div class="bar"></div>
							</div>
							<span ng-if="!isSearching()">
								<nlh4j:message code="COM_MSG_ID_ERR_013" />
							</span>
						</td>
					</tr>
				</tbody>
				<c:if test="${fn:length(listFooter) > 0}">
				<tfoot>
					<tiles:insertAttribute name="listFooter" />
				</tfoot>
				</c:if>
			</table>
		</c:if>
		
		<!-- custom search result -->
		<c:if test="${fn:length(customSearchResult) > 0}">
			<tiles:insertAttribute name="customSearchResult" />
		</c:if>
	</div>
</c:if>
<!-- END::page data -->
