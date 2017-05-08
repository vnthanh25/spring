
var detailController = app.controller('detailController', function($rootScope, $scope, listService){
	
	/*createUser*/
	$scope.save = function() {
		var user = {
				name: $scope.name
		}
		listService.createUser(user).then(
			//success
			function(response) {
				alert('success');
				$scope.name = '';
				$rootScope.$broadcast('refreshList');
			},
			//error
			function(response) {
				alert('error');
			}
		);
	}

	$scope.$on('itemDetail', function(event, data){
		$scope.name = data.name;
	});
	
	
});