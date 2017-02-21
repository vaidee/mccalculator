var calcApp = angular.module('calcApp',['ngRoute']);
      
calcApp.config(configFunction);

function configFunction($routeProvider,$logProvider,$locationProvider) {
  $logProvider.debugEnabled(true);
  $routeProvider.when('/calc',{
   templateUrl:'index.html'
 });
};

      calculate();
      
});

