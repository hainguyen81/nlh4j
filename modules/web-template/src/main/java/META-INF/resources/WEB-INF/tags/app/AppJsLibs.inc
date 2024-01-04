<%@ include file="/WEB-INF/tags/Common.inc" %>

<%-- ------------------------------------------------- --%>
<%-- Base Javascript library version --%>
<%-- ------------------------------------------------- --%>
<nlh4jTags:JsLib src="jquery/${verJquery}/jquery${verMinSuffix}.js" />
<nlh4jTags:JsLib src="jquery-ui/${verJqueryUI}/jquery-ui${verMinSuffix}.js" />
<nlh4jTags:JsLib src="angular/${verAngular}/angular${verMinSuffix}.js" />
<nlh4jTags:JsLib src="bootstrap/${verBoostrap}/js/bootstrap${verMinSuffix}.js" />

<!--
    IE8 support, see AngularJS Internet Explorer Compatibility http://docs.angularjs.org/guide/ie
    For Firefox 3.6, you will also need to include jQuery and ECMAScript 5 shim
-->
<!--[if lt IE 7]>
    <nlh4jTags:JsLib src="helpers/simple-line-icons/${verSimpleLineIcons}/icons-lte-ie7${verMinSuffix}.js" />
<![endif]-->
<!--[if lt IE 9]>
    <nlh4jTags:JsLib src="helpers/html5shiv/${verHtml5shiv}/html5shiv-printshiv${verMinSuffix}.js" />
    <nlh4jTags:JsLib src="helpers/html5shiv/${verHtml5shiv}/html5shiv${verMinSuffix}.js" />
    <nlh4jTags:JsLib src="helpers/respond/${verRespond}/respond.matchmedia.addListener${verMinSuffix}.js" />
    <nlh4jTags:JsLib src="helpers/respond/${verRespond}/respond${verMinSuffix}.js" />
    <nlh4jTags:JsLib src="helpers/es5-shim/${verEs5Shim}/es5-shim${verMinSuffix}.js" />
    <nlh4jTags:JsLib src="angular-file-upload/${verAngularFileUpload}/ng-file-upload-shim${verMinSuffix}.js" />
     <script>
		document.createElement('ui-select');
		document.createElement('ui-select-match');
		document.createElement('ui-select-choices');
    </script>
<![endif]-->

<%-- ------------------------------------------------- --%>
<%-- Helper Javascript library version --%>
<%-- ------------------------------------------------- --%>
<nlh4jTags:JsLib src="helpers/moment/${verMoment}/moment-with-locales${verMinSuffix}.js" />
<nlh4jTags:JsLib src="helpers/moment/${verMoment}/moment${verMinSuffix}.js" />
<nlh4jTags:JsLib src="helpers/underscore/${verUnderscore}/underscore${verMinSuffix}.js" />
<c:if test="${nlh4j:isSocketProfile()}">
<nlh4jTags:JsLib src="helpers/sockjs/${verSockjs}/sockjs${verMinSuffix}.js" />
<nlh4jTags:JsLib src="helpers/stomp/${verStomp}/stomp${verMinSuffix}.js" />
</c:if>
<!-- Offline.js -->
<nlh4jTags:JsLib src="helpers/offline/${verOffline}/offline${verMinSuffix}.js" />
<!-- JsBarcode -->
<nlh4jTags:JsLib src="helpers/jsbarcode/${verJsBarcode}/JsBarcode.codabar.min.js" />
<nlh4jTags:JsLib src="helpers/jsbarcode/${verJsBarcode}/JsBarcode.code128.min.js" />
<nlh4jTags:JsLib src="helpers/jsbarcode/${verJsBarcode}/JsBarcode.code39.min.js" />
<nlh4jTags:JsLib src="helpers/jsbarcode/${verJsBarcode}/JsBarcode.ean-upc.min.js" />
<nlh4jTags:JsLib src="helpers/jsbarcode/${verJsBarcode}/JsBarcode.itf-14.min.js" />
<nlh4jTags:JsLib src="helpers/jsbarcode/${verJsBarcode}/JsBarcode.itf.min.js" />
<nlh4jTags:JsLib src="helpers/jsbarcode/${verJsBarcode}/JsBarcode.msi.min.js" />
<nlh4jTags:JsLib src="helpers/jsbarcode/${verJsBarcode}/JsBarcode.pharmacode.min.js" />
<nlh4jTags:JsLib src="helpers/jsbarcode/${verJsBarcode}/JsBarcode.all.min.js" />

<%-- ------------------------------------------------- --%>
<%-- Jquery Plugins Javascript library version --%>
<%-- ------------------------------------------------- --%>
<!-- jQuery Ajax form -->
<nlh4jTags:JsLib src="jquery-ajaxsubmit/${verJqueryAjaxSubmit}/jquery.ajaxsubmit${verMinSuffix}.js" />
<!-- jQuery Animsition -->
<nlh4jTags:JsLib src="jquery-animsition/${verJqueryAnimsition}/js/jquery.animsition${verMinSuffix}.js" />
<!-- jQuery MetisMenu -->
<nlh4jTags:JsLib src="jquery-metismenu/${verJqueryMetisMenu}/metisMenu${verMinSuffix}.js" />
<!-- jQuery Autohide Nav -->
<nlh4jTags:JsLib src="jquery-bootstrap-autohidingnavbar/${verJqueryBsAutoHideNav}/jquery.bootstrap.autohidingnavbar${verMinSuffix}.js" />
<!-- jQuery Boostrap Clock Picker -->
<nlh4jTags:JsLib src="jquery-bootstrap-clockpicker/${verJqueryBsClockPicker}/jquery.bootstrap.clockpicker${verMinSuffix}.js" />
<!-- jQuery Boostrap Sidebar -->
<nlh4jTags:JsLib src="jquery-bootstrap-sidebar/${verJqueryBsSidebar}/sidebar${verMinSuffix}.js" />
<%-- Require jQuery MetisMenu libraries --%>
<!-- jQuery StartBoostrap Sidebar Admin -->
<nlh4jTags:JsLib src="jquery-startbootstrap-sb-admin-2/${verJqueryBsSidebarAdmin}/js/sb-admin-2${verMinSuffix}.js" />
<!-- jQuery Chart -->
<nlh4jTags:JsLib src="jquery-chart/${verJqueryChart}/jquery.chart${verMinSuffix}.js" />
<!-- jQuery Choosen -->
<nlh4jTags:JsLib src="jquery-chosen/${verJqueryChosen}/jquery.chosen.proto${verMinSuffix}.js" />
<nlh4jTags:JsLib src="jquery-chosen/${verJqueryChosen}/jquery.chosen${verMinSuffix}.js" />
<%-- Require jQuery DateTime utilities --%>
<!-- jQuery DateTime Picker -->
<nlh4jTags:JsLib src="jquery-datetime-picker/${verJqueryDtPicker}/jquery.datetimepicker${verMinSuffix}.js" />
<!-- jQuery Dialogue -->
<nlh4jTags:JsLib src="jquery-dialogue/${verJqueryDialogue}/jquery.alerts${verMinSuffix}.js" />
<nlh4jTags:JsLib src="jquery-dialogue/${verJqueryDialogue}/jquery.dialogue${verMinSuffix}.js" />
<!-- jQuery File Downloader -->
<nlh4jTags:JsLib src="jquery-filedownload/${verJqueryFileDownload}/jquery.filedownload${verMinSuffix}.js" />
<!-- jQuery Full Calendar -->
<nlh4jTags:JsLib src="jquery-fullcalendar/${verJqueryFullCalendar}/fullcalendar${verMinSuffix}.js" />
<nlh4jTags:JsLib src="jquery-fullcalendar/${verJqueryFullCalendar}/gcal${verMinSuffix}.js" />
<nlh4jTags:JsLib src="jquery-fullcalendar/${verJqueryFullCalendar}/locale-all${verMinSuffix}.js" />
<!-- jQuery ScrollTo -->
<nlh4jTags:JsLib src="jquery-scrollTo/${verJqueryScrollTo}/jquery.scrollTo${verMinSuffix}.js" />
<!-- jQuery Spin.Js -->
<nlh4jTags:JsLib src="jquery-spin.js/${verJquerySpin}/spin${verMinSuffix}.js" />
<nlh4jTags:JsLib src="jquery-spin.js/${verJquerySpin}/jquery.spin${verMinSuffix}.js" />
<!-- jQuery Scanner Detection -->
<nlh4jTags:JsLib src="jquery-scanner-detection/${verJqueryScannerDetection}/jquery.scannerdetection.compatibility${verMinSuffix}.js" />
<nlh4jTags:JsLib src="jquery-scanner-detection/${verJqueryScannerDetection}/jquery.scannerdetection${verMinSuffix}.js" />
<!-- jQuery Template -->
<nlh4jTags:JsLib src="jquery-template/${verJqueryTemplate}/jquery.template${verMinSuffix}.js" />
<!-- jQuery Utilities -->
<nlh4jTags:JsLib src="jquery-utils/${verJqueryUtils}/jquery.array.utils${verMinSuffix}.js" />
<nlh4jTags:JsLib src="jquery-utils/${verJqueryUtils}/jquery.datetime.utils${verMinSuffix}.js" />
<nlh4jTags:JsLib src="jquery-utils/${verJqueryUtils}/jquery.string.utils${verMinSuffix}.js" />
<nlh4jTags:JsLib src="jquery-utils/${verJqueryUtils}/jquery.utils${verMinSuffix}.js" />
<!-- jQuery TableSorter -->
<nlh4jTags:JsLib src="jquery-tablesorter/${verJqueryTableSorter}/jquery.tablesorter${verMinSuffix}.js" />
<!-- jQuery TableSaw -->
<nlh4jTags:JsLib src="jquery-tablesaw/${verJqueryTableSaw}/js/jquery.tablesaw${verMinSuffix}.js" />
<nlh4jTags:JsLib src="jquery-tablesaw/${verJqueryTableSaw}/js/jquery.tablesaw.init${verMinSuffix}.js" />

<%-- ------------------------------------------------- --%>
<%-- Angular Plugins Javascript library version --%>
<%-- ------------------------------------------------- --%>
<!-- Angular Animation -->
<nlh4jTags:JsLib src="angular/${verAngular}/angular-animate${verMinSuffix}.js" />
<!-- Angular Touch -->
<nlh4jTags:JsLib src="angular/${verAngular}/angular-touch${verMinSuffix}.js" />
<!-- Angular Route -->
<nlh4jTags:JsLib src="angular/${verAngular}/angular-route${verMinSuffix}.js" />
<!-- Angular Sanitize -->
<nlh4jTags:JsLib src="angular/${verAngular}/angular-sanitize${verMinSuffix}.js" />
<%-- Require jQuery and moment libraries --%>
<!-- Angular DateTime Picker -->
<%-- <nlh4jTags:JsLib src="angular-bootstrap3-datepicker/${verAngularBsDtPicker}/ng-bs3-datepicker${verMinSuffix}.js" /> --%>
<%-- Require jQuery-chart library --%>
<!-- Angular Chart -->
<nlh4jTags:JsLib src="angular-chart/${verAngularChart}/angular-chart${verMinSuffix}.js" />
<!-- Angular Digital Clock -->
<nlh4jTags:JsLib src="angular-clock/${verAngularClock}/angular-clock${verMinSuffix}.js" />
<!-- Angular Confirm -->
<nlh4jTags:JsLib src="angular-confirm/${verAngularConfirm}/angular-confirm${verMinSuffix}.js" />
<!-- Angular Context Menu -->
<nlh4jTags:JsLib src="angular-contextmenu/${verAngularCtxMenu}/angular-contextmenu${verMinSuffix}.js" />
<!-- Angular Date Parser -->
<nlh4jTags:JsLib src="angular-dateparser/${verAngularDateParser}/angular-dateparser${verMinSuffix}.js" />
<!-- Angular Defer Boostrap -->
<nlh4jTags:JsLib src="angular-defer-bootstrap/${verAngularDeferBs}/angular-defer-bootstrap${verMinSuffix}.js" />
<!-- Angular Ellipsis -->
<nlh4jTags:JsLib src="angular-ellipsis/${verAngularEllipsis}/angular-ellipsis${verMinSuffix}.js" />
<!-- Angular File Upload -->
<nlh4jTags:JsLib src="angular-file-upload/${verAngularFileUpload}/ng-file-upload${verMinSuffix}.js" />
<!-- Angular Gridster -->
<nlh4jTags:JsLib src="angular-gridster/${verAngularGridster}/angular-gridster${verMinSuffix}.js" />
<!-- Angular Image Cropper -->
<nlh4jTags:JsLib src="angular-imgcrop/${verAngularImgCrop}/ng-img-crop${verMinSuffix}.js" />
<!-- Angular Listview -->
<nlh4jTags:JsLib src="angular-listview/${verAngularListview}/angular-listview${verMinSuffix}.js" />
<!-- Angular Loading Bar -->
<nlh4jTags:JsLib src="angular-loading-bar/${verAngularLoadingBar}/loading-bar${verMinSuffix}.js" />
<!-- Angular Local Storage -->
<nlh4jTags:JsLib src="angular-local-storage/${verAngularLocalStorage}/angular-local-storage${verMinSuffix}.js" />
<!-- Angular Modal Service -->
<nlh4jTags:JsLib src="angular-modal-service/${verAngularModal}/angular-modal-service${verMinSuffix}.js" />
<!-- Angular Once -->
<nlh4jTags:JsLib src="angular-once/${verAngularOnce}/angular-once${verMinSuffix}.js" />
<!-- Angular Progress -->
<nlh4jTags:JsLib src="angular-progress/${verAngularProgress}/ngProgress${verMinSuffix}.js" />
<!-- Angular Resize -->
<nlh4jTags:JsLib src="angular-resize/${verAngularResize}/ng-resize${verMinSuffix}.js" />
<!-- Angular Responsive -->
<nlh4jTags:JsLib src="angular-responsive/${verAngularResponsive}/responsive${verMinSuffix}.js" />
<nlh4jTags:JsLib src="angular-responsive/${verAngularResponsive}/responsive-directive${verMinSuffix}.js" />
<!-- Angular Sanitize -->
<nlh4jTags:JsLib src="angular-sanitize/${verAngularSanitize}/angular-sanitize${verMinSuffix}.js" />
<!-- Angular SlimScroll -->
<nlh4jTags:JsLib src="angular-slimscroll/${verAngularSlimscroll}/ngSlimscroll${verMinSuffix}.js" />
<%-- Angular Socket --%>
<c:if test="${nlh4j:isSocketProfile()}">
<!-- Angular Stomp -->
<nlh4jTags:JsLib src="angular-socket/${verAngularSocket}/ngStomp${verMinSuffix}.js" />
</c:if>
<!-- Angular Sortable -->
<nlh4jTags:JsLib src="angular-sortable/${verAngularSortable}/ng-sortable${verMinSuffix}.js" />
<!-- Angular Spring Security CSRF Interceptor -->
<nlh4jTags:JsLib src="angular-spring-security-csrf-token-interceptor/${verAngularCsrfSecurity}/spring-security-csrf-token-interceptor${verMinSuffix}.js" />
<!-- Angular Table -->
<nlh4jTags:JsLib src="angular-table/${verAngularTable}/ng-table${verMinSuffix}.js" />
<!-- Angular Time Picker -->
<nlh4jTags:JsLib src="angular-timepicker/${verAngularTimePicker}/angular-timepicker${verMinSuffix}.js" />
<!-- Angular Toaster -->
<nlh4jTags:JsLib src="angular-toaster/${verAngularToaster}/toaster${verMinSuffix}.js" />
<!-- Angular UI Boostrap -->
<nlh4jTags:JsLib src="angular-ui-bootstrap/${verAngularUiBs}/ui-bootstrap-tpls${verMinSuffix}.js" />
<!-- Angular UI Calendar -->
<nlh4jTags:JsLib src="angular-ui-calendar/${verAngularUiCalendar}/angular-ui-calendar${verMinSuffix}.js" />
<!-- Angular UI Grid -->
<nlh4jTags:JsLib src="angular-ui-grid/${verAngularUiGrid}/ui-grid${verMinSuffix}.js" />
<!-- Angular UI Router -->
<nlh4jTags:JsLib src="angular-ui-router/${verAngularUiRouter}/angular-ui-router${verMinSuffix}.js" />
<!-- Angular UI Select -->
<nlh4jTags:JsLib src="angular-ui-select/${verAngularUiSelect}/select${verMinSuffix}.js" />
<!-- Angular X-Editable -->
<nlh4jTags:JsLib src="angular-xeditable/${verAngularXeditable}/js/xeditable${verMinSuffix}.js" />
<!-- Angular Audio -->
<nlh4jTags:JsLib src="angular-audio/${verAngularAudio}/angular.audio${verMinSuffix}.js" />
<!-- Angular Barcode -->
<nlh4jTags:JsLib src="angular-barcode-listener/${verAngularBarcode}/angular-barcode-listener${verMinSuffix}.js" />
<nlh4jTags:JsLib src="angular-scanner-detection/${verAngularScannerDetection}/angular.scannerdetection.compatibility${verMinSuffix}.js" />
<nlh4jTags:JsLib src="angular-scanner-detection/${verAngularScannerDetection}/angular.scannerdetection${verMinSuffix}.js" />
<!-- Angular Print -->
<nlh4jTags:JsLib src="angular-print/${verAngularPrint}/angularPrint${verMinSuffix}.js" />
<!-- Angular Autocomplete -->
<nlh4jTags:JsLib src="angucomplete-alt/${verAngularAutocomplete}/angucomplete-alt${verMinSuffix}.js" />
<!-- Angular Sprintf -->
<nlh4jTags:JsLib src="angular-sprintf/${verAngularSprintf}/sprintf${verMinSuffix}.js" />
<nlh4jTags:JsLib src="angular-sprintf/${verAngularSprintf}/angular-sprintf${verMinSuffix}.js" />
<!-- Angular Cell Cursor -->
<nlh4jTags:JsLib src="angular-cell-cursor/${verAngularCellCursor}/cellCursor${verMinSuffix}.js" />
<!-- Angular App.Cache -->
<nlh4jTags:JsLib src="angular-appcache/${verAngularAppCache}/appcache${verMinSuffix}.js" />
<!-- Angular IndexedDB -->
<nlh4jTags:JsLib src="angular-indexed-db/${verAngularIndexedDB}/angular-indexed-db${verMinSuffix}.js" />
<!-- Angular DragTable -->
<nlh4jTags:JsLib src="angular-dragtable/${verAngularDragTable}/angular-dragtable${verMinSuffix}.js" />

<%-- ------------------------------------------------- --%>
<%-- Angular Wiz Validation Plugins Javascript library version --%>
<%-- ------------------------------------------------- --%>
<!-- wizValidation: atLeastOne -->
<nlh4jTags:JsLib src="angular-wiz-validation/${verAngularWizValidation}/src/atLeastOne/atLeastOne.mod.js" />
<nlh4jTags:JsLib src="angular-wiz-validation/${verAngularWizValidation}/src/atLeastOne/atLeastOne.svc.js" />
<nlh4jTags:JsLib src="angular-wiz-validation/${verAngularWizValidation}/src/atLeastOne/atLeastOne.dir.js" />
<!-- wizValidation: blackList -->
<nlh4jTags:JsLib src="angular-wiz-validation/${verAngularWizValidation}/src/blacklist/blacklist.mod.js" />
<nlh4jTags:JsLib src="angular-wiz-validation/${verAngularWizValidation}/src/blacklist/blacklist.dir.js" />
<!-- wizValidation: conditions -->
<nlh4jTags:JsLib src="angular-wiz-validation/${verAngularWizValidation}/src/conditions/conditions.mod.js" />
<nlh4jTags:JsLib src="angular-wiz-validation/${verAngularWizValidation}/src/conditions/conditions.dir.js" />
<!-- wizValidation: date of birth -->
<nlh4jTags:JsLib src="angular-wiz-validation/${verAngularWizValidation}/src/dateOfBirth/dateOfBirth.mod.js" />
<nlh4jTags:JsLib src="angular-wiz-validation/${verAngularWizValidation}/src/dateOfBirth/dateOfBirth.dir.js" />
<!-- wizValidation: decimal -->
<nlh4jTags:JsLib src="angular-wiz-validation/${verAngularWizValidation}/src/decimal/decimal.mod.js" />
<nlh4jTags:JsLib src="angular-wiz-validation/${verAngularWizValidation}/src/decimal/decimal.dir.js" />
<!-- wizValidation: endsWith -->
<nlh4jTags:JsLib src="angular-wiz-validation/${verAngularWizValidation}/src/endsWith/endsWith.mod.js" />
<nlh4jTags:JsLib src="angular-wiz-validation/${verAngularWizValidation}/src/endsWith/endsWith.dir.js" />
<!-- wizValidation: equalsTo -->
<nlh4jTags:JsLib src="angular-wiz-validation/${verAngularWizValidation}/src/equalTo/equalTo.mod.js" />
<nlh4jTags:JsLib src="angular-wiz-validation/${verAngularWizValidation}/src/equalTo/equalTo.svc.js" />
<nlh4jTags:JsLib src="angular-wiz-validation/${verAngularWizValidation}/src/equalTo/equalTo.dir.js" />
<!-- wizValidation: file -->
<nlh4jTags:JsLib src="angular-wiz-validation/${verAngularWizValidation}/src/file/file.mod.js" />
<nlh4jTags:JsLib src="angular-wiz-validation/${verAngularWizValidation}/src/file/file.dir.js" />
<!-- wizValidation: integer -->
<nlh4jTags:JsLib src="angular-wiz-validation/${verAngularWizValidation}/src/integer/integer.mod.js" />
<nlh4jTags:JsLib src="angular-wiz-validation/${verAngularWizValidation}/src/integer/integer.dir.js" />
<!-- wizValidation: notEqualTo -->
<nlh4jTags:JsLib src="angular-wiz-validation/${verAngularWizValidation}/src/notEqualTo/notEqualTo.mod.js" />
<nlh4jTags:JsLib src="angular-wiz-validation/${verAngularWizValidation}/src/notEqualTo/notEqualTo.svc.js" />
<nlh4jTags:JsLib src="angular-wiz-validation/${verAngularWizValidation}/src/notEqualTo/notEqualTo.dir.js" />
<!-- wizValidation: postCode -->
<nlh4jTags:JsLib src="angular-wiz-validation/${verAngularWizValidation}/src/postcode/postcode.mod.js" />
<nlh4jTags:JsLib src="angular-wiz-validation/${verAngularWizValidation}/src/postcode/postcode.dir.js" />
<!-- wizValidation: startsWith -->
<nlh4jTags:JsLib src="angular-wiz-validation/${verAngularWizValidation}/src/startsWith/startsWith.mod.js" />
<nlh4jTags:JsLib src="angular-wiz-validation/${verAngularWizValidation}/src/startsWith/startsWith.dir.js" />
<!-- wizValidation: unique -->
<nlh4jTags:JsLib src="angular-wiz-validation/${verAngularWizValidation}/src/unique/unique.mod.js" />
<nlh4jTags:JsLib src="angular-wiz-validation/${verAngularWizValidation}/src/unique/unique.svc.js" />
<nlh4jTags:JsLib src="angular-wiz-validation/${verAngularWizValidation}/src/unique/unique.dir.js" />
<!-- wizValidation: whiteList -->
<nlh4jTags:JsLib src="angular-wiz-validation/${verAngularWizValidation}/src/whitelist/whitelist.mod.js" />
<nlh4jTags:JsLib src="angular-wiz-validation/${verAngularWizValidation}/src/whitelist/whitelist.dir.js" />
<!-- wizValidation: zipCode -->
<nlh4jTags:JsLib src="angular-wiz-validation/${verAngularWizValidation}/src/zipcode/zipcode.mod.js" />
<nlh4jTags:JsLib src="angular-wiz-validation/${verAngularWizValidation}/src/zipcode/zipcode.dir.js" />
<!-- wizValidation: common -->
<nlh4jTags:JsLib src="angular-wiz-validation/${verAngularWizValidation}/src/wizValidation.mod.js" />
<nlh4jTags:JsLib src="angular-wiz-validation/${verAngularWizValidation}/wizValidation${verMinSuffix}.js" />

<%-- ------------------------------------------------- --%>
<%-- Angular Locale Language Javascript library version --%>
<%-- ------------------------------------------------- --%>
<!-- Angular i18n -->
<c:if test="${currentLocale == null || fn:length(currentLocale) <= 0 || currentLocale == 'en' || currentLocale == 'us'}">
	<nlh4jTags:JsLib src="angular/${verAngular}/i18n/angular-locale_en-us.js" />
	<nlh4jTags:JsLib src="jquery-datetime-picker/${verJqueryDtPicker}/jquery.datetime.en-us.js" />
</c:if>
<c:if test="${currentLocale == 'jp' || currentLocale == 'ja' || currentLocale == 'ja-jp'}">
	<nlh4jTags:JsLib src="angular/${verAngular}/i18n/angular-locale_ja-jp.js" />
	<nlh4jTags:JsLib src="jquery-datetime-picker/${verJqueryDtPicker}/jquery.datetime.ja-jp.js" />
</c:if>
<c:if test="${currentLocale == 'vi' || currentLocale == 'vn' || currentLocale == 'vi-vn'}">
	<nlh4jTags:JsLib src="angular/${verAngular}/i18n/angular-locale_vi-vn.js" />
	<nlh4jTags:JsLib src="jquery-datetime-picker/${verJqueryDtPicker}/jquery.datetime.vi-vn.js" />
</c:if>
<!-- END::angular i18n -->
