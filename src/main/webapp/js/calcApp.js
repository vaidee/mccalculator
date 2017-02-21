var calcApp = angular.module('calcApp',[]);


calcApp.controller('calcCtrl',function($scope,$http) {
	
$scope.submitForm = function()
{
	console.log("submitting form...");
    $http({
            url: "http://mccalculator.herokuapp.com/rest/calc?expression="+$scope.expression,
            method: "GET",
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        }).success(function(data, status, headers, config) {
        	console.log(data);
        	console.log(status);
            $scope.status = status;
            $scope.results = data;
        }).error(function(data, status, headers, config) {
            $scope.status = status;
            $scope.error = data;
        });
}
});
      


