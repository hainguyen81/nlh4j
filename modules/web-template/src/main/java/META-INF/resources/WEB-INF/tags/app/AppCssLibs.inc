<%@ include file="/WEB-INF/tags/Common.inc" %>

<!-- START:Google Fonts -->
<%-- <link href='https://fonts.googleapis.com/css?family=Pacifico|Sigmar+One|Abril+Fatface|Open+Sans' rel='stylesheet' type='text/css'> --%>
<!-- END:Google Fonts -->

<%-- ------------------------------------------------- --%>
<%-- Base CSS library version --%>
<%-- ------------------------------------------------- --%>
<nlh4jTags:CssLib src="bootstrap/${verBoostrap}/css/bootstrap${verMinSuffix}.css" />
<nlh4jTags:CssLib src="jquery-ui/${verJqueryUI}/jquery-ui${verMinSuffix}.css" />
<nlh4jTags:CssLib src="font-awesome/${verFontAwsome}/css/font-awesome${verMinSuffix}.css" />

<%-- ------------------------------------------------- --%>
<%-- Helper CSS library version --%>
<%-- ------------------------------------------------- --%>
<!-- <nlh4jTags:CssLib src="helpers/bootswatch/${verBootswatch}/yeti/bootstrap${verMinSuffix}.css" /> -->
<nlh4jTags:CssLib src="helpers/simple-line-icons/${verSimpleLineIcons}/simple-line-icons${verMinSuffix}.css" />

<%-- ------------------------------------------------- --%>
<%-- Boostrap Plugins CSS library version --%>
<%-- ------------------------------------------------- --%>
<nlh4jTags:CssLib src="bootstrap/${verBoostrap}/css/awesome-bootstrap-checkbox${verMinSuffix}.css" />

<%-- ------------------------------------------------- --%>
<%-- Jquery Plugins CSS library version --%>
<%-- ------------------------------------------------- --%>
<!-- jQuery Animation -->
<nlh4jTags:CssLib src="jquery-animsition/${verJqueryAnimsition}/css/animsition${verMinSuffix}.css" />
<!-- jQuery MetisMenu -->
<nlh4jTags:CssLib src="jquery-metismenu/${verJqueryMetisMenu}/metisMenu${verMinSuffix}.css" />
<!-- jQuery Boostrap Clock Picker -->
<nlh4jTags:CssLib src="jquery-bootstrap-clockpicker/${verJqueryBsClockPicker}/jquery.bootstrap.clockpicker${verMinSuffix}.css" />
<!-- jQuery Boostrap Sidebar -->
<!-- <nlh4jTags:CssLib src="jquery-bootstrap-sidebar/${verJqueryBsSidebar}/sidebar${verMinSuffix}.css" /> -->
<%-- Require jQuery MetisMenu libraries --%>
<!-- jQuery StartBoostrap Sidebar Admin -->
<!-- <nlh4jTags:CssLib src="jquery-startbootstrap-sb-admin-2/${verJqueryBsSidebarAdmin}/css/sb-admin-2${verMinSuffix}.css" /> -->
<!-- jQuery Choosen -->
<nlh4jTags:CssLib src="jquery-chosen/${verJqueryChosen}/chosen${verMinSuffix}.css" />
<!-- jQuery DateTime Picker -->
<nlh4jTags:CssLib src="jquery-datetime-picker/${verJqueryDtPicker}/jquery.datetimepicker${verMinSuffix}.css" />
<!-- jQuery Dialogue -->
<nlh4jTags:CssLib src="jquery-dialogue/${verJqueryDialogue}/jquery.alerts${verMinSuffix}.css" />
<nlh4jTags:CssLib src="jquery-dialogue/${verJqueryDialogue}/jquery.dialogue${verMinSuffix}.css" />
<!-- jQuery Full Calendar -->
<nlh4jTags:CssLib src="jquery-fullcalendar/${verJqueryFullCalendar}/fullcalendar${verMinSuffix}.css" />
<nlh4jTags:CssLib src="jquery-fullcalendar/${verJqueryFullCalendar}/fullcalendar.print${verMinSuffix}.css" media="print" />
<!-- jQuery TableSaw -->
<nlh4jTags:CssLib src="jquery-tablesaw/${verJqueryTableSaw}/css/jquery.tablesaw${verMinSuffix}.css" />

<%-- ------------------------------------------------- --%>
<%-- Angular Plugins CSS library version --%>
<%-- ------------------------------------------------- --%>
<%-- Require jQuery and moment libraries --%>
<!-- Angular DateTime Picker -->
<%-- <nlh4jTags:CssLib src="angular-bootstrap3-datepicker/${verAngularBsDtPicker}/ng-bs3-datepicker${verMinSuffix}.css" /> --%>
<%-- Require jQuery-chart library --%>
<!-- Angular Chart -->
<nlh4jTags:CssLib src="angular-chart/${verAngularChart}/angular-chart${verMinSuffix}.css" />
<!-- Angular Digital Clock -->
<nlh4jTags:CssLib src="angular-clock/${verAngularClock}/angular-clock${verMinSuffix}.css" />
<!-- Angular Gridster -->
<nlh4jTags:CssLib src="angular-gridster/${verAngularGridster}/angular-gridster${verMinSuffix}.css" />
<!-- Angular Image Cropper -->
<nlh4jTags:CssLib src="angular-imgcrop/${verAngularImgCrop}/ng-img-crop${verMinSuffix}.css" />
<!-- Angular Listview -->
<nlh4jTags:CssLib src="angular-listview/${verAngularListview}/css/angular-listview${verMinSuffix}.css" />
<!-- Angular Loading Bar -->
<nlh4jTags:CssLib src="angular-loading-bar/${verAngularLoadingBar}/loading-bar${verMinSuffix}.css" />
<!-- Angular Progress -->
<nlh4jTags:CssLib src="angular-progress/${verAngularProgress}/ngProgress${verMinSuffix}.css" />
<!-- Angular Sortable -->
<nlh4jTags:CssLib src="angular-sortable/${verAngularSortable}/ng-sortable${verMinSuffix}.css" />
<!-- Angular Table -->
<nlh4jTags:CssLib src="angular-table/${verAngularTable}/ng-table${verMinSuffix}.css" />
<!-- Angular Toaster -->
<nlh4jTags:CssLib src="angular-toaster/${verAngularToaster}/toaster${verMinSuffix}.css" />
<!-- Angular UI Grid -->
<nlh4jTags:CssLib src="angular-ui-grid/${verAngularUiGrid}/ui-grid${verMinSuffix}.css" />
<!-- Angular UI Select -->
<nlh4jTags:CssLib src="angular-ui-select/${verAngularUiSelect}/select${verMinSuffix}.css" />
<nlh4jTags:CssLib src="angular-ui-select/${verAngularUiSelect}/select2${verMinSuffix}.css" />
<!-- Angular X-Editable -->
<nlh4jTags:CssLib src="angular-xeditable/${verAngularXeditable}/css/xeditable${verMinSuffix}.css" />
<!-- Angular Print -->
<nlh4jTags:CssLib src="angular-print/${verAngularPrint}/angularPrint${verMinSuffix}.css" />
<!-- Angular Autocomplete -->
<nlh4jTags:CssLib src="angucomplete-alt/${verAngularAutocomplete}/angucomplete-alt${verMinSuffix}.css" />
<!-- Angular Cell Cursor -->
<nlh4jTags:CssLib src="angular-cell-cursor/${verAngularCellCursor}/cellCursor${verMinSuffix}.css" />

<%-- Common style for angular --%>
<nlh4jTags:Stylesheet>
[ng\:cloak], [ng-cloak], .ng-cloak { display: none !important; visibility: hidden; }
</nlh4jTags:Stylesheet>