<%@ include file="/WEB-INF/tags/TagLibs.inc" %>

<%-- ------------------------------------------------- --%>
<%-- Specify caching resources --%>
<%-- ------------------------------------------------- --%>
<spring:eval var="resourceCache" expression="@properties.getProperty('resource.caching')" scope="application" />
<c:set var="resourceCache" value="${resourceCache eq true || resourceCache eq 'true' || resourceCache eq '1'}" scope="application" />

<%-- ------------------------------------------------- --%>
<%-- Specify using minimum version --%>
<%-- ------------------------------------------------- --%>
<spring:eval var="verMin" expression="@properties.getProperty('ver.min')" scope="application" />
<c:set var="verMin" value="${verMin eq true || verMin eq 'true' || verMin eq '1'}" scope="application" />
<c:set var="verMinSuffix" value="${verMin ? '.min' : ''}" scope="application" />

<%-- ------------------------------------------------- --%>
<%-- Base Javascript library version --%>
<%-- ------------------------------------------------- --%>
<spring:eval var="verJquery" expression="@properties.getProperty('ver.jquery')" scope="application" />
<spring:eval var="verJqueryUI" expression="@properties.getProperty('ver.jquery.ui')" scope="application" />
<spring:eval var="verAngular" expression="@properties.getProperty('ver.angular')" scope="application" />
<spring:eval var="verFontAwsome" expression="@properties.getProperty('ver.fontawesome')" scope="application" />
<spring:eval var="verBoostrap" expression="@properties.getProperty('ver.boostrap')" scope="application" />

<%-- ------------------------------------------------- --%>
<%-- Helper Javascript library version --%>
<%-- ------------------------------------------------- --%>
<spring:eval var="verMoment" expression="@properties.getProperty('ver.moment')" scope="application" />
<spring:eval var="verSockjs" expression="@properties.getProperty('ver.sockjs')" scope="application" />
<spring:eval var="verStomp" expression="@properties.getProperty('ver.stomp')" scope="application" />
<spring:eval var="verHtml5shiv" expression="@properties.getProperty('ver.html5shiv')" scope="application" />
<spring:eval var="verEs5Shim" expression="@properties.getProperty('ver.es5.shim')" scope="application" />
<spring:eval var="verRespond" expression="@properties.getProperty('ver.respond')" scope="application" />
<spring:eval var="verUnderscore" expression="@properties.getProperty('ver.underscore')" scope="application" />
<spring:eval var="verOffline" expression="@properties.getProperty('ver.offline')" scope="application" />
<spring:eval var="verJsBarcode" expression="@properties.getProperty('ver.jsbarcode')" scope="application" />

<%-- ------------------------------------------------- --%>
<%-- Helper CSS library version --%>
<%-- ------------------------------------------------- --%>
<spring:eval var="verBootswatch" expression="@properties.getProperty('ver.bootswatch')" scope="application" />
<spring:eval var="verSimpleLineIcons" expression="@properties.getProperty('ver.simple.line.icons')" scope="application" />

<%-- ------------------------------------------------- --%>
<%-- Jquery Plugins Javascript library version --%>
<%-- ------------------------------------------------- --%>
<spring:eval var="verJqueryChosen" expression="@properties.getProperty('ver.jquery.chosen')" scope="application" />
<spring:eval var="verJqueryChart" expression="@properties.getProperty('ver.jquery.chart')" scope="application" />
<spring:eval var="verJqueryAnimsition" expression="@properties.getProperty('ver.jquery.animsition')" scope="application" />
<spring:eval var="verJqueryDialogue" expression="@properties.getProperty('ver.jquery.dialogue')" scope="application" />
<spring:eval var="verJqueryDtPicker" expression="@properties.getProperty('ver.jquery.datetimepicker')" scope="application" />
<spring:eval var="verJqueryMetisMenu" expression="@properties.getProperty('ver.jquery.metis.menu')" scope="application" />
<spring:eval var="verJqueryBsAutoHideNav" expression="@properties.getProperty('ver.jquery.bootstrap.autohidingnavbar')" scope="application" />
<spring:eval var="verJqueryBsClockPicker" expression="@properties.getProperty('ver.jquery.bootstrap.clockpicker')" scope="application" />
<spring:eval var="verJqueryBsSidebar" expression="@properties.getProperty('ver.jquery.bootstrap.sidebar')" scope="application" />
<spring:eval var="verJqueryBsSidebarAdmin" expression="@properties.getProperty('ver.jquery.startbootstrap.sidebar.admin')" scope="application" />
<spring:eval var="verJqueryAjaxSubmit" expression="@properties.getProperty('ver.jquery.ajaxsubmit')" scope="application" />
<spring:eval var="verJqueryTemplate" expression="@properties.getProperty('ver.jquery.template')" scope="application" />
<spring:eval var="verJqueryFileDownload" expression="@properties.getProperty('ver.jquery.file.download')" scope="application" />
<spring:eval var="verJqueryFullCalendar" expression="@properties.getProperty('ver.jquery.fullcalendar')" scope="application" />
<spring:eval var="verJqueryUtils" expression="@properties.getProperty('ver.jquery.utils')" scope="application" />
<spring:eval var="verJqueryScrollTo" expression="@properties.getProperty('ver.jquery.scrollTo')" scope="application" />
<spring:eval var="verJquerySpin" expression="@properties.getProperty('ver.jquery.spin')" scope="application" />
<%-- Barcode --%>
<spring:eval var="verJqueryScannerDetection" expression="@properties.getProperty('ver.jquery.scanner.detection')" scope="application" />

<%-- ------------------------------------------------- --%>
<%-- Angular Plugins Javascript library version --%>
<%-- ------------------------------------------------- --%>
<spring:eval var="verAngularDeferBs" expression="@properties.getProperty('ver.angular.defer.bootstrap')" scope="application" />
<spring:eval var="verAngularUiBs" expression="@properties.getProperty('ver.angular.ui.bootstrap')" scope="application" />
<spring:eval var="verAngularUiCalendar" expression="@properties.getProperty('ver.angular.ui.calendar')" scope="application" />
<spring:eval var="verAngularOnce" expression="@properties.getProperty('ver.angular.once')" scope="application" />
<spring:eval var="verAngularXeditable" expression="@properties.getProperty('ver.angular.xeditable')" scope="application" />
<spring:eval var="verAngularToaster" expression="@properties.getProperty('ver.angular.toaster')" scope="application" />
<spring:eval var="verAngularGridster" expression="@properties.getProperty('ver.angular.gridster')" scope="application" />
<spring:eval var="verAngularCsrfSecurity" expression="@properties.getProperty('ver.angular.spring.security.csrf.token.interceptor')" scope="application" />
<spring:eval var="verAngularLocalStorage" expression="@properties.getProperty('ver.angular.local.storage')" scope="application" />
<spring:eval var="verAngularDateParser" expression="@properties.getProperty('ver.angular.dateparser')" scope="application" />
<spring:eval var="verAngularTimePicker" expression="@properties.getProperty('ver.angular.timepicker')" scope="application" />
<spring:eval var="verAngularUiRouter" expression="@properties.getProperty('ver.angular.ui.router')" scope="application" />
<spring:eval var="verAngularSanitize" expression="@properties.getProperty('ver.angular.sanitize')" scope="application" />
<%-- Require jQuery-chart library --%>
<spring:eval var="verAngularChart" expression="@properties.getProperty('ver.angular.chart')" scope="application" />
<spring:eval var="verAngularListview" expression="@properties.getProperty('ver.angular.listview')" scope="application" />
<spring:eval var="verAngularSortable" expression="@properties.getProperty('ver.angular.sortable')" scope="application" />
<spring:eval var="verAngularTable" expression="@properties.getProperty('ver.angular.table')" scope="application" />
<spring:eval var="verAngularUiSelect" expression="@properties.getProperty('ver.angular.ui.select')" scope="application" />
<spring:eval var="verAngularSlimscroll" expression="@properties.getProperty('ver.angular.slimscroll')" scope="application" />
<spring:eval var="verAngularConfirm" expression="@properties.getProperty('ver.angular.confirm')" scope="application" />
<spring:eval var="verAngularClock" expression="@properties.getProperty('ver.angular.clock')" scope="application" />
<%-- Require jQuery and moment libraries --%>
<spring:eval var="verAngularBsDtPicker" expression="@properties.getProperty('ver.angular.bootstrap3.datepicker')" scope="application" />
<spring:eval var="verAngularProgress" expression="@properties.getProperty('ver.angular.progress')" scope="application" />
<spring:eval var="verAngularUiGrid" expression="@properties.getProperty('ver.angular.ui.grid')" scope="application" />
<spring:eval var="verAngularResponsive" expression="@properties.getProperty('ver.angular.responsive')" scope="application" />
<spring:eval var="verAngularFileUpload" expression="@properties.getProperty('ver.angular.file.upload')" scope="application" />
<spring:eval var="verAngularCtxMenu" expression="@properties.getProperty('ver.angular.context.menu')" scope="application" />
<spring:eval var="verAngularSocket" expression="@properties.getProperty('ver.angular.socket')" scope="application" />
<spring:eval var="verAngularImgCrop" expression="@properties.getProperty('ver.angular.imgcrop')" scope="application" />
<spring:eval var="verAngularLoadingBar" expression="@properties.getProperty('ver.angular.loading.bar')" scope="application" />
<spring:eval var="verAngularResize" expression="@properties.getProperty('ver.angular.resize')" scope="application" />
<spring:eval var="verAngularModal" expression="@properties.getProperty('ver.angular.modal')" scope="application" />
<spring:eval var="verAngularEllipsis" expression="@properties.getProperty('ver.angular.ellipsis')" scope="application" />
<%-- Sound --%>
<spring:eval var="verAngularAudio" expression="@properties.getProperty('ver.angular.audio')" scope="application" />
<%-- Barcode --%>
<spring:eval var="verAngularBarcode" expression="@properties.getProperty('ver.angular.barcode')" scope="application" />
<spring:eval var="verAngularScannerDetection" expression="@properties.getProperty('ver.angular.scanner.detection')" scope="application" />
<%-- Print --%>
<spring:eval var="verAngularPrint" expression="@properties.getProperty('ver.angular.print')" scope="application" />
<%-- Autocomplete --%>
<spring:eval var="verAngularAutocomplete" expression="@properties.getProperty('ver.angular.autocomplete')" scope="application" />
<%-- Sprintf --%>
<spring:eval var="verAngularSprintf" expression="@properties.getProperty('ver.angular.sprintf')" scope="application" />
<%-- Cell Cursor --%>
<spring:eval var="verAngularCellCursor" expression="@properties.getProperty('ver.angular.cell.cursor')" scope="application" />
<%-- App.Cache --%>
<spring:eval var="verAngularAppCache" expression="@properties.getProperty('ver.angular.appcache')" scope="application" />
<%-- IndexedDB --%>
<spring:eval var="verAngularIndexedDB" expression="@properties.getProperty('ver.angular.indexed.db')" scope="application" />
<%-- Drag-Table --%>
<spring:eval var="verAngularDragTable" expression="@properties.getProperty('ver.angular.drag.table')" scope="application" />

<%-- ------------------------------------------------- --%>
<%-- Angular Wiz Validation Plugins Javascript library version --%>
<%-- ------------------------------------------------- --%>
<spring:eval var="verAngularWizValidation" expression="@properties.getProperty('ver.angular.wiz.validation')" scope="application" />

<%-- ------------------------------------------------- --%>
<%-- Application theme --%>
<%-- ------------------------------------------------- --%>
<spring:eval var="appTheme" expression="@properties.getProperty('app.theme')" scope="application" />
