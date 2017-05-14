
var listService = app.service('listService', function($http, $cookies, $window){

	var token = ''; 
	
	$http.get(serverUrl + '/user/').then(function(response) {
		
		token = response.data.details.tokenValue;
		if (response.data.name) {
		} else {
		}
	}, function() {
	});

	
	
	
	
	this.getUsers = function() {
//		alert($cookies.get('JSESSIONID'));
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

		
		
/*
		$.ajax({
		    beforeSend: function (jqXHR, settings) {
		        jqXHR.setRequestHeader('Authorization', 'Basic ' + btoa('user:password'));
		    },
		    url: 'http://localhost:4444/server/api/user',
		    type: "POST",
			data: user,
			headers: {
				'X-TENANT-ID': appKey
			}
		});
*/
		
/*		
		$.ajax({ 
			   url: 'http://localhost:4444/server/api/user',
			   type: 'POST',
			   contentType:'application/x-www-form-urlencoded',
	            async: false,
	            beforeSend: function(xhr) { 
	                xhr.setRequestHeader("Authorization", "Bearer " + token);
	            },
			   headers: {
			   'Access-Control-Allow-Origin' : 'http://localhost:1111',
			   },
			       data: JSON.stringify({ 'name': '999'}),
			       success: function(data){
			        //On ajax success do this
			             alert(data);
			          }
			     });		
*/		
/*
		$.ajax({
            url: 'http://localhost:4444/server/api/users',
            type: "GET",
            headers: {
				'X-TENANT-ID': appKey,
				"Authorization": "Bearer " + token
			},
			success: function() {
				alert('aaa');
			}
		});
*/		
		
/*		
		$.ajax({
            url: 'http://localhost:4444/server/api/users',
            type: "GET",
            async: false,
            crossDomain: true,
            xhrFields: {
                withCredentials: true
            },
            beforeSend: function(xhr) { 
                xhr.setRequestHeader("Authorization", "Bearer " + token);
            },
            headers: {
				'X-TENANT-ID': appKey,
				"Authorization": "Bearer " + token
			},
			success: function() {
				alert('aaa');
			}
		});
*/		
		
		
/*		
		$.ajax({
            url: 'http://localhost:4444/server/api/user',
            type: "POST",
            dataType: 'json',
            data: { 'name': '999'},
            contentType: 'application/json',
            headers: {
				'X-TENANT-ID': appKey,
				"Authorization": "Bearer " + token
			},
			success: function() {
				alert('aaa');
			}
		});
*/		
		
	}
	
    
	// Upload attachments.
	this.uploadAttachments = function(attachments) {
		
		
        var filePath = "attachment.path";
		var request = {
            method: 'POST',
            url: 'http://localhost:4444/server/api/files/upload?filePath=' + filePath + "&id=1",
            data: attachments,
            withCredentials: true,
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
	            url: 'http://localhost:4444/server/api/files/download?filePath=' + filePath + "&id=1"  + "&fileName=" + encodeURIComponent(fileName),
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
