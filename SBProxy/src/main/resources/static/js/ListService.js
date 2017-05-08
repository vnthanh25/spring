
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
	
    
	// Upload attachments.
	this.uploadAttachments = function(attachments) {
        var filePath = "attachment.path";
		var request = {
            method: 'POST',
            url: serverUrl + '/server/api/files/upload?filePath=' + filePath + "&id=1",
            data: attachments,
            headers: {
                'Content-Type': undefined,
                'X-TENANT-ID': appKey
            }
        };
        return $http(request);
    }
	
	// Download attachments.
	this.downloadAttachments = function(fileName) {
		var filePath = "attachment.path";
		var request = {
	            method: 'GET',
	            url: serverUrl + '/server/api/files/download?filePath=' + filePath + "&id=1"  + "&fileName=" + encodeURIComponent(fileName),
	            responseType: 'arraybuffer',
                cache: false,
	            headers: {
	            	'accept': 'application/zip',
	            	'X-TENANT-ID': appKey
	            }
	        };
	    return $http(request);
	}

});
