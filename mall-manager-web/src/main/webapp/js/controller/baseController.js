/** 
 *  抽取公共部分，可以给多个页面使用
 */
app.controller('baseController', function($scope) {

	// 重新加载列表 数据
	$scope.reloadList = function() {
		// 切换页码
		/*
		 * $scope.findPage($scope.paginationConf.currentPage,
		 * $scope.paginationConf.itemsPerPage);
		 */

		$scope.search($scope.paginationConf.currentPage,
				$scope.paginationConf.itemsPerPage);
	}

	$scope.paginationConf = {
		currentPage : 1,
		totalItems : 15,
		itemsPerPage : 15,
		pagesLength : 15,
		perPageOptions : [ 10, 20, 30, 40, 50 ],
		onChange : function() {
			$scope.reloadList();// 重新加载
		}
	};

	//初始化selectIds，保存着每条数据的id
	$scope.selectIds = [];
	//勾选单个checkbox
	$scope.check = function($event, id) {
		if ($event.target.checked) {
			$scope.selectIds.push(id);
			//如果选择的checkbox的个数等于页面数据的个数，则表示全选，全选框勾选
			if($scope.selectIds.length==$scope.list.length)
				$scope.select_all = true;
		} else {
			var index = $scope.selectIds.indexOf(id);
			$scope.selectIds.splice(index, 1);
			//只要有一个框不勾选，则全选框不勾选
			$scope.select_all = false;
		}
	}
	//实现全选
	$scope.selectAll = function () {
		//选择
        if($scope.select_all) {
            $scope.selectIds = [];
            angular.forEach($scope.list, function (i) {
            	//所有的checkebox勾选
                i.checked = true;
                $scope.selectIds.push(i.id);
            })
            
        }else {
        	//不选择
            angular.forEach($scope.list, function (i) {
            	//所有的checkebox去掉勾选
                i.checked = false;
                $scope.selectIds = [];
            })
        }
        console.log($scope.checked);
    };
	
	
	//提取json字符串数据中某个属性，返回拼接字符串 逗号分隔
	$scope.jsonToString=function(jsonString,key){
		var json=JSON.parse(jsonString);//将json字符串转换为json对象
		var value="";
		for(var i=0;i<json.length;i++){		
			if(i>0){
				value+=","
			}
			value+=json[i][key];			
		}
		return value;
	}


});