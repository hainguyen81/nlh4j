'use strict';

var APP = angular.module(APP_NAME, APP_INCLUDES);
var ANIMATIONS = angular.module(MOD_ANIMATIONS, []);
var CONTROLLERS = angular.module(MOD_CONTROLLERS, []);
var DIRECTIVES = angular.module(MOD_DIRECTIVES, []);
var FACTORIES = angular.module(MOD_FACTORIES, []);
var FILTERS = angular.module(MOD_FILTERS, []);
var SERVICES = angular.module(MOD_SERVICES, []);

var MODULES = {
		APP: APP,
		APP_CONFIG: [],
		ANIMATIONS: ANIMATIONS,
		CONTROLLERS: CONTROLLERS,
		DIRECTIVES: DIRECTIVES,
		FACTORIES: FACTORIES,
		FILTERS: FILTERS,
		CHILD_MODULES: [],
		SERVICES: SERVICES,
};

/**
 * Check and ensure child module
 *
 * @param moduleName the child module name
 */
function ensureModule(moduleName, dependencies) {
	if (!MODULES.CHILD_MODULES[moduleName]
		|| !$.inArray(MODULES.CHILD_MODULES, moduleName)) {
		if (dependencies && !angular.isArray(dependencies)) {
			dependencies = [dependencies];
		}
		else if (!dependencies) {
			dependencies = [];
		}
		if (dependencies.indexOf(APP_NAME) < 0) {
			dependencies.push(APP_NAME);
		}
		MODULES.CHILD_MODULES[moduleName] = angular.module(moduleName, dependencies);
		MODULES.CHILD_MODULES[moduleName] = MODULES.CHILD_MODULES[moduleName].config(MODULES.APP_CFG);
	}
	return (MODULES.CHILD_MODULES[moduleName] != null && MODULES.CHILD_MODULES[moduleName] != undefined);
}

/**
 * Ensure dependencies
 *
 * @param dependencies
 */
function ensureDependencies(dependencies) {
	var newdependencies = [];
	if (dependencies) {
		if (angular.isFunction(dependencies)) {
			var fnArgs = $.getFunctionParamNames(dependencies);
			if (fnArgs.length > 0) {
				for(var i = 0; i < fnArgs.length; i++) {
					newdependencies.push(fnArgs[i]);
				}
			}
			newdependencies.push(dependencies);
		}
		else if (!angular.isArray(dependencies)) {
			newdependencies = [dependencies];
		}
		else {
			newdependencies = dependencies;
		}
	}
	return newdependencies;
}

/**
 * Run actions by the specified module
 *
 * @param moduleName the child module name
 * @param actions the actions to run
 */
function runModule(moduleName, actions) {
	// ensure child module
	if (ensureModule(moduleName)) {
		if (angular.isArray(actions)) {
			for(var action in actions) {
				if (angular.isFunction(action)) {
					MODULES.CHILD_MODULES[moduleName] = MODULES.CHILD_MODULES[moduleName].run(action);
				}
			}
		} else if (angular.isFunction(actions)) {
			MODULES.CHILD_MODULES[moduleName] = MODULES.CHILD_MODULES[moduleName].run(actions);
		}
	}
}
function run(actions) {
	if (angular.isArray(actions)) {
		for(var action in actions) {
			if (angular.isFunction(action)) {
				MODULES.APP = MODULES.APP.run(action);
			}
		}
	} else if (angular.isFunction(actions)) {
		MODULES.APP = MODULES.APP.run(actions);
	}
}

/**
 * Add configuration into module
 *
 * @param moduleName the child module name
 * @param settings module settings
 */
function importModuleConfig(moduleName, settings) {
	if (moduleName && moduleName.length > 0) {
		// ensure child module
		if (ensureModule(moduleName)) {
			MODULES.CHILD_MODULES[moduleName] = MODULES.CHILD_MODULES[moduleName].config(
					ensureDependencies(settings));
		}
	}
}
function importConfig(settings) {
	settings = ensureDependencies(settings);
	MODULES.APP = MODULES.APP.config(settings);
	MODULES.APP_CFG = settings;
}

/**
 * Add animation into module
 *
 * @param moduleName the child module name
 * @param animationName animation name
 * @param dependencies animation dependencies
 */
function importModuleAnimation(moduleName, animationName, dependencies) {
	if (moduleName && moduleName.length > 0
			&& animationName && animationName.length > 0) {
		// ensure child module
		if (ensureModule(moduleName)) {
			// add animation into child module
			MODULES.CHILD_MODULES[moduleName].animation(
					animationName, ensureDependencies(dependencies));
		}
	}
	else if (animationName && animationName.length > 0) {
		importAnimation(animationName, dependencies);
	}
}
function importAnimation(animationName, dependencies) {
	if (animationName && animationName.length > 0) {
		MODULES.ANIMATIONS = MODULES.ANIMATIONS.animation(
				animationName, ensureDependencies(dependencies));
	}
}

/**
 * Add controller into module
 *
 * @param moduleName the child module name
 * @param ctrlName controller name
 * @param dependencies controller dependencies
 */
function importModuleController(moduleName, ctrlName, dependencies) {
	if (moduleName && moduleName.length > 0
			&& ctrlName && ctrlName.length > 0) {
		// ensure child module
		if (ensureModule(moduleName)) {
			// add controller into child module
			MODULES.CHILD_MODULES[moduleName].controller(
					ctrlName, ensureDependencies(dependencies));
		}
	}
	else if (ctrlName && ctrlName.length > 0) {
		importController(ctrlName, dependencies);
	}
}
function importController(ctrlName, dependencies) {
	if (ctrlName && ctrlName.length > 0) {
		MODULES.CONTROLLERS = MODULES.CONTROLLERS.controller(
				ctrlName, ensureDependencies(dependencies));
	}
}

/**
 * Add directive into module
 *
 * @param moduleName the child module name
 * @param directiveName directive name
 * @param dependencies directive dependencies
 */
function importModuleDirective(moduleName, directiveName, dependencies) {
	if (moduleName && moduleName.length > 0
			&& directiveName && directiveName.length > 0) {
		// ensure child module
		if (ensureModule(moduleName)) {
			// add directive into child module
			MODULES.CHILD_MODULES[moduleName].directive(
					directiveName, ensureDependencies(dependencies));
		}
	}
	else if (directiveName && directiveName.length > 0) {
		importDirective(directiveName, dependencies);
	}
}
function importDirective(directiveName, dependencies) {
	if (directiveName && directiveName.length > 0) {
		MODULES.DIRECTIVES = MODULES.DIRECTIVES.directive(
				directiveName, ensureDependencies(dependencies));
	}
}

/**
 * Add filter into module
 *
 * @param moduleName the child module name
 * @param filterName filter name
 * @param dependencies filter dependencies
 */
function importModuleFilter(moduleName, filterName, dependencies) {
	if (moduleName && moduleName.length > 0
			&& filterName && filterName.length > 0) {
		// ensure child module
		if (ensureModule(moduleName)) {
			// add filter into child module
			MODULES.CHILD_MODULES[moduleName].filter(
					filterName, ensureDependencies(dependencies));
		}
	}
	else if (filterName && filterName.length > 0) {
		importFilter(filterName, dependencies);
	}
}
function importFilter(filterName, dependencies) {
	if (filterName && filterName.length > 0) {
		MODULES.FILTERS = MODULES.FILTERS.filter(
				filterName, ensureDependencies(dependencies));
	}
}

/**
 * Add service into module
 *
 * @param moduleName the child module name
 * @param srvName service name
 * @param dependencies service depnendencies
 */
function importModuleService(moduleName, srvName, dependencies) {
	if (moduleName && moduleName.length > 0
			&& srvName && srvName.length > 0) {
		// ensure child module
		if (ensureModule(moduleName)) {
			// add service into child module
			MODULES.CHILD_MODULES[moduleName].service(
					srvName, ensureDependencies(dependencies));
		}
	}
	else if (srvName && srvName.length > 0) {
		importService(srvName, dependencies);
	}
}
function importService(srvName, dependencies) {
	if (srvName && srvName.length > 0) {
		MODULES.SERVICES = MODULES.SERVICES.service(
				srvName, ensureDependencies(dependencies));
	}
}

/**
 * Add factory into module
 *
 * @param moduleName the child module name
 * @param factoryName factory name
 * @param dependencies factory dependencies
 */
function importModuleFactory(moduleName, factoryName, dependencies) {
	if (moduleName && moduleName.length > 0
			&& factoryName && factoryName.length > 0) {
		// ensure child module
		if (ensureModule(moduleName)) {
			// add factory into child module
			MODULES.CHILD_MODULES[moduleName].factory(
					factoryName, ensureDependencies(dependencies));
		}
	}
	else if (factoryName && factoryName.length > 0) {
		importFactory(factoryName, dependencies);
	}
}
function importFactory(factoryName, dependencies) {
	if (factoryName && factoryName.length > 0) {
		MODULES.FACTORIES = MODULES.FACTORIES.factory(
				factoryName, ensureDependencies(dependencies));
	}
}

