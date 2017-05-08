
var detailController = app.controller('detailController', function($scope){
	$scope.$on('itemDetail', function(event, data){
		$scope.name = data.name;
	});
});