var app = angular.module('pinyougou', []);//定义模块	
angular.module('epcui', ['ngSanitize'])// ngSanitize

app.filter('trustHtml',['$sce',function($sce)
	{
		return function(data)
		{
			$sce.trustAsHtml(data);
		}
	}
	
])
app.filter('trustHtml',['$sce',function($sce){
    return function(data){
        return $sce.trustAsHtml(data);
    }
}]);
