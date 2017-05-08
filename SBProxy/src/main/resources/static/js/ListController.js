
var listController = app.controller('listController', function($rootScope, $scope){
	$scope.items = [
		{id: 1, name: 'name 1'},
		{id: 2, name: 'name 2'},
		{id: 3, name: 'name 3'}
	];
	
	$scope.detail = function(item) {
		$rootScope.$broadcast('itemDetail', item)
	}
});
