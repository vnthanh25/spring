
var appUrl = 'http://localhost:1111';
angular.module('hello', [ 'ngRoute' ]).config(function($routeProvider, $httpProvider) {

	$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
	$httpProvider.defaults.headers.common['Accept'] = 'application/json';

}).controller('navigation',

function($rootScope, $http, $location, $route) {

	var self = this;

	self.tab = function(route) {
		return $route.current && route === $route.current.controller;
	};

	$http.get(appUrl + '/user/').then(function(response) {
		if (response.data.name) {
			$rootScope.authenticated = true;
			window.location.href = "ui/";
		} else {
			$rootScope.authenticated = false;
		}
	}, function() {
		$rootScope.authenticated = false;
	});

});
