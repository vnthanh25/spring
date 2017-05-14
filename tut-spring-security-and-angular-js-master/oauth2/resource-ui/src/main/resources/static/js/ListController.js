
var listController = app.controller('listController', function($rootScope, $scope, listService){
	
	$scope.init = function() {
		$scope.getUsers();
	}
	
	/*getUsers*/
	$scope.getUsers = function() {
		listService.getUsers().then(
			//success
			function(response) {
				$scope.items = response.data;
			},
			//error
			function(response) {
				alert('error');
			}
		);
	}
	
	$scope.detail = function(item) {
		$rootScope.$broadcast('itemDetail', item)
	}
	
	$scope.$on('refreshList', function() {
		$scope.getUsers();
	});
});
