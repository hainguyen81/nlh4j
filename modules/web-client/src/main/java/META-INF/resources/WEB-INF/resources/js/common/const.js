/**
 * Angular application packages
 */
var APP_CUSTOMER = "NLH4J";
var APP_NAME = 'ngApp';
var APP_CTRL = 'commonCtrl';
var APP_SESSION_STORAGE = 'sessionStorage';
var MOD_CONTROLLERS = APP_NAME + '.controllers';
var MOD_FACTORIES = APP_NAME + '.factories';
var MOD_SERVICES = APP_NAME + '.services';
var MOD_FILTERS = APP_NAME + '.filters';
var MOD_DIRECTIVES = APP_NAME + '.directives';
var MOD_ANIMATIONS = APP_NAME + '.animations';

/**
 * AJAX configuration
 */
var X_REQ_XDOMAIN = true;
var X_REQ_CRIDENTIALS = true;
var X_REQ_CORS = true;
var X_REQ_MOBILE_CORS = true;

/**
 * Page configuration
 */
var BASE_CONTEXT_PATH_KEY = 'Base-Context-Path';
var CSRF_COOKIE_TOKEN_NAME = 'JCSRFTOKENCOOKIE';
var CSRF_NAME_META_HEADER_KEY = '_csrf_header';
var CSRF_PARAM_NAME_META_HEADER_KEY = '_csrf_param';
var CSRF_TOKEN_META_HEADER_KEY = '_csrf';
var ERROR_INTERNET_META_HEADER_KEY = 'errorInternet';
var ERROR_SERVER_META_HEADER_KEY = 'errorServer';
var MAX_DASHBOARD_SKINS_NUMBER = 25;

/**
 * Barcode configuration
 */
var BARCODE_SUPPORT=true;

/**
 * Server Offline checking configuration
 */
var SERVER_CHECKING = false;
var SERVER_CHECKING_INTERVAL = 5000;

/**
 * Logger configuration
 */
var DEBUG = true;
/* SAME LOGBACK LEVELS: DEBUG < INFO < WARN < ERROR < OFF */
var LOG_LEVEL = 'ERROR';

/**
 * Default AJAX header parameters
 */
var DEFAULT_HEADERS = {
		'Access-Control-Allow-Origin': '*',
    	'Access-Control-Allow-Headers': 'Content-Type',
    	'Access-Control-Expose-Headers': '*',
    	'Access-Control-Allow-Credentials': 'true',
    	'X-Requested-With': 'XMLHttpRequest',
    	'withCredentials': 'true',
		'useXDomain': 'true',
};

/**
 * Default client locale messages
 */
var DEFAULT_LOCALE = 'vi';
var DEFAULT_ERROR_MESSAGES = {
		'ja': {
			'errorInternet': 'サーバーに接続できませんでした。ネットワークをチェックしてください。',
			'errorServer': 'サーバー処理中にエラーが発生しました。管理者に問い合わせてください。',
			'e0': 'サーバーに接続できませんでした。ネットワークをチェックしてください。',
			'e401': '資格情報が無効です。',
			'e403': '十分許可されていません。詳細については、管理者に連絡してください。',
			'e404': '対象データが存在しません。他の端末で削除された可能性があります。画面表示を最新にしてご確認ください。',
			'e405': 'データまたは方法は違法必要です。詳細については、管理者に連絡してください。',
			'e409': 'データが他のと競合されていました。入力を確認してください。',
			'e417': 'データが期待されていません。入力を確認してください。',
			'e500': 'サーバー処理中にエラーが発生しました。管理者に問い合わせてください。'
		},
		'en': {
			'errorInternet': 'Could not connect to target server. Please check your internet connection again.',
			'errorServer': 'Internal server error. Please contact with your administrator for more detail.',
			'e0': 'Could not connect to target server. Please check your internet connection again.',
			'e401': 'Your cridenticals is invalid.',
			'e403': 'You don\'t have enough permission. Please contact with your administrator for more detail.',
			'e404': 'Data has been not found. Data may be deleted by another users. Please refresh your screen again.',
			'e405': 'Request data is invalid. Please contact with your administrator for more detail.',
			'e409': 'Data has been existed. Your data is conflicted. Please refresh your screen again.',
			'e417': 'Data has not expected. Please check your input again.',
			'e500': 'Internal server error. Please contact with your administrator for more detail.'
		},
		'vi': {
			'errorInternet': 'Không thể kết nối đến máy chủ. Vui lòng kiểm tra lại kết nối Internet của bạn.',
			'errorServer': 'Xử lý bị lỗi. Vui lòng liên hệ quản trị viên của bạn để biết thêm thông tin.',
			'e0': 'Không thể kết nối đến máy chủ. Vui lòng kiểm tra lại kết nối Internet của bạn.',
			'e401': 'Quyền chứng thực bất hợp lệ.',
			'e403': 'Không có đủ quyền để truy xuất. Vui lòng liên hệ quản trị viên của bạn để biết thêm thông tin.',
			'e404': 'Không tìm thấy thông tin. Thông tin có thể đã bị xóa. Vui lòng làm mới lại màn hình.',
			'e405': 'Dữ liệu hoặc phương thức yêu cầu bất hợp lệ. Vui lòng liên hệ quản trị viên của bạn để biết thêm thông tin.',
			'e409': 'Dữ liệu bị trùng lắp. Vui lòng kiểm tra lại.',
			'e417': 'Dữ liệu không đúng. Vui lòng kiểm tra lại.',
			'e500': 'Xử lý bị lỗi. Vui lòng liên hệ quản trị viên của bạn để biết thêm thông tin.'
		}
};

/**
 * Import libraries
 */
var APP_INCLUDES = [
	'ngAnimate'
	, 'gridster'
	, 'toaster'
	, 'ui.bootstrap'
	, 'ui.calendar'
	, 'ngSanitize'
	, 'ngProgress'
	, 'ngTouch'
	, 'spring-security-csrf-token-interceptor'
	, 'LocalStorageModule'
	, 'chart.js'
	, 'ui.listview'
	, 'as.sortable'
	, 'angular-confirm'
	, 'ds.clock'
	// , 'jkuri.slimscroll'
	, 'ngFileUpload'
	, 'ngLocale'
	, 'ui.select'
	, 'dateParser'
	, 'dnTimepicker'
	, 'ui.router'
	// , 'ng-bs3-datepicker'
	// , 'bp-ngContextMenu'
	, 'ui.bootstrap.contextMenu'
	, 'wiz.validation'
	, 'angular-responsive'
	, 'ngResize'
	, 'angularModalService'
	, 'dibari.angular-ellipsis'
	, 'barcodeListener'
	, 'ngAudio'
	, 'AngularPrint'
	, 'scanner.detection'
	, 'angucomplete-alt'
	, 'sprintf'
	, 'ng-appcache'
	, 'indexedDB'
	, 'Dragtable'
	, MOD_FACTORIES
	, MOD_SERVICES
	, MOD_CONTROLLERS
	, MOD_FILTERS
	, MOD_DIRECTIVES
	, MOD_ANIMATIONS
].concat(typeof APP_ADDITIONAL_MODULES !== 'undefined'
	&& Object.prototype.toString.call(APP_ADDITIONAL_MODULES) === '[object Array]'
	? APP_ADDITIONAL_MODULES || [] : []);
