
var detailController = app.controller('detailController', function($window, $rootScope, $scope, listService){
	var user = {id: -1}
	/*createUser*/
	$scope.save = function() {
		user.name = $scope.name;
		
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
	

	////////////////////////////////////////
	// Attachments.
	////////////////////////////////////////
	
	$scope.attachments = new FormData();
	$scope.fileNames = [];
	$scope.files = [];

	$scope.saveAttachments = function() {
		// Upload attachments.
		$scope.uploadAttachments($scope.attachments);
		$scope.attachments = new FormData();
		// Set fileNames from attachments of this user.
		angular.forEach($scope.attachments, function(attachment, idx) {
			$scope.fileNames.push(attachment.fileName);
		});
	}
	
	// Add attachments.
	$scope.addAttachments = function($files) {
		angular.forEach($files, function(file, idx) {
			if($.inArray(file.name, $scope.fileNames) < 0) {
				$scope.fileNames.push(file.name);
				$scope.attachments.append("files", file);
				var attachment = { fileName: file.name, fileSize: file.size, mimeType: file.type };
				$scope.files.push(attachment);
			}
		});
		$scope.$apply();
	}
	// Clear value of file element.
	$scope.fileClear = function (event) {
		$(event.target).val(null);
	};
	// Delete attachments.
	$scope.deleteAttachments = function(fileName) {
		if(!$window.confirm('Are you sure to delete?')) {
			return;
		}
		$scope.fileNames = $.grep($scope.fileNames, function (pValue) {
                                return pValue != fileName;
                            });
		
		var files = $scope.attachments.getAll('files');
		var fileLength = files.length;
		for(var idx = 0; idx < fileLength; idx++) {
			if(files[idx].name == fileName) {
				files.splice(idx, 1);
				fileLength = -1;
			}
		}
		$scope.attachments = new FormData();
		angular.forEach(files, function(file, idx) {
			$scope.attachments.append("files", file);
		});

		$scope.files = $.grep($scope.files, function (attachment) {
                                return attachment.fileName != fileName;
                            });
	}
    // Upload attachments.
    $scope.uploadAttachments = function (attachments) {
			listService.uploadAttachments(attachments).then(
			// success.
			function(response, status, headers, config) {
				$window.alert('Upload success!');
			},
			// error.
			function(response, status, headers, config) {
				$window.alert('Upload error!');
			}
		);
    }
	// Download attachments.
	$scope.downloadAttachments = function(fileName) {
		listService.downloadAttachments(fileName).then(
			// success.
			function(response, status, headers, config) {
				var file = new Blob([response.data], {type: 'application/*'});
				var fileURL = (window.URL || window.webkitURL).createObjectURL(file);
				var downloadLink = angular.element('<a></a>');
				downloadLink.attr('href', fileURL);
	            downloadLink.attr('download', fileName);
				downloadLink[0].click();
			},
			// error.
			function(response, status, headers, config) {
				alert(response);
				//$scope.showMessage('Error!', 'alert-danger', true);
			}
		);
	}
	
});