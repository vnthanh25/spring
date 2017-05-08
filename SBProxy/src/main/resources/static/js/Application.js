
var serverUrl = 'http://localhost:9999';
var appKey = 'tenant_1';
var app = angular.module('app', ['ui.router']);

app.config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider){
	
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

