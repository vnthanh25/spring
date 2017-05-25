angular.module('hello', []).controller('home', function($http) {
	var self = this;
	$http.get('http://localhost:9000/resource/').then(function(response) {
		self.greeting = response.data;
	})
});
