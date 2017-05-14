
var serverUrl = 'http://localhost:1111';
var appKey = 'tenant_1';
var app = angular.module('app', ['ngCookies', 'ngRoute', 'ui.router']);

app.config(['$httpProvider', '$stateProvider', '$urlRouterProvider', function($httpProvider, $stateProvider, $urlRouterProvider){

	$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
	$httpProvider.defaults.headers.common['Accept'] = 'application/json';
	$httpProvider.defaults.withCredentials = true;
	//$httpProvider.defaults.xsrfCookieName = 'csrftoken';
    //$httpProvider.defaults.xsrfHeaderName = 'X-CSRFToken';
    //$http.defaults.headers.post['X-CSRFToken'] = $cookies.get('csrftoken');
    
	$urlRouterProvider.otherwise('/home');
	
	$stateProvider
		.state('main', {
			abstract: true,
			//url: '',
			views: {
				'layout': {
					templateUrl: './template/layout.html'
				},
				'header@main': {
					templateUrl: './template/header.html'
				},
				'left@main': {
					templateUrl: './template/left.html'
				},
				'content@main': {
					templateUrl: './template/content.html'
				},
				'footer@main': {
					templateUrl: './template/footer.html'
				}
			}
		})
		.state('home', {
			parent: 'main',
			url: '/home',
			views: {
				'list@main': {
					templateUrl: './template/list.html',
					controller: 'listController'
				},
				'container@main': {
					templateUrl: './template/detail.html',
					controller: 'detailController'
				}
			}
		})
	;
}]);

