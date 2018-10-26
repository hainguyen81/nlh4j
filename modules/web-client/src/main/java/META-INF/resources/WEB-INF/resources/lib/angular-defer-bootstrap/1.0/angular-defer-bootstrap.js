//# Set the window.name to signal Angular to delay bootstrapping until `angular.resumeBootstrap()` is called.
//#     See: http://stackoverflow.com/a/21049890/235704 and https://docs.angularjs.org/guide/bootstrap
//#     NOTE: This MUST be included BEFORE angular*.js
window.name = 'NG_DEFER_BOOTSTRAP! ' + window.name;

//####################
//# Setup the jQuery onDocumentLoad event to handle the pseudo-ng-directive of ng-preinit
//####################
$(document).ready(function () {
	var $this, $pre = $('[ng-preinit]'), $app = $('[ng-app]');

	//# If we have some [ng-preinit]'s to process
	if ($pre.length) {
      	// # Traverse the [ng-preinit] attributes, eval'ing/running each and removing them so Angular doesn't freak out
		$pre.each(function() {
			$this = $(this);
			eval($this.attr('ng-preinit'));
			$this.removeAttr('ng-preinit');
		});
	}

	//# Let Angular know it can .resumeBootstrap and remove the flag from window.name
	angular.element(document).ready(function() {
		angular.bootstrap(document, [$app.attr('ng-app')]);
		window.name = window.name.replace('NG_DEFER_BOOTSTRAP! ', '');
	});
});