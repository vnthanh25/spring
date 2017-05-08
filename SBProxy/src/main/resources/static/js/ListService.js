
var listService = app.service('listService', function($http){
	
	this.getUsers = function() {
		return $http({
			method: 'GET',
			url: serverUrl + '/server/api/users',
			headers: {
				'X-TENANT-ID': appKey
			}
		});
	}
	
	this.createUser = function(user) {
		return $http({
			method: 'POST',
			url: serverUrl + '/server/api/user',
			data: user,
			headers: {
				'X-TENANT-ID': appKey
			}
		});
	}
});
