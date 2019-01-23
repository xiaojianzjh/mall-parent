//品牌控制层 
app.controller('brandController', function($scope, $controller,brandService) {
	
	$controller('baseController',{$scope:$scope}); //继承baseController
	
	// 读取列表数据绑定到表单中
	$scope.findAll = function() {
		brandService.findAll().success(function(response) {
			$scope.list = response;
		});
	}


	// 分页
	$scope.findPage = function(page, rows) {
		brandService.findPage(page,rows).success(
				function(response) {
					$scope.list = response.list;
					$scope.paginationConf.totalItems = response.total;// 更新总记录数
				});
	};

	// 保存
	$scope.save = function() {
		var obj = null;
		if ($scope.entity.id != null) 
			obj = brandService.update($scope.entity);
		else
			obj = brandService.add($scope.entity);
		
		obj.success(
			function(response) {
				if (response.success) {
					// 重新查询
					$scope.reloadList();// 重新加载
				} else {
					alert(response.message);
				}
		});
	}

	// 查询实体
	$scope.findOne = function(id) {
		brandService.findOne(id).success(function(response) {
			$scope.entity = response;
		});
	}
	

	// 批量删除
	$scope.dele = function() {
		// 获取选中的复选框
		brandService.dele($scope.selectIds).success(
				function(response) {
					if (response.success) {
						$scope.reloadList();// 刷新列表
					}
				});
	}

	$scope.searchEntity = {};// 定义搜索对象
	// 条件查询
	$scope.search=function(page,rows){
		brandService.search(page,rows,$scope.searchEntity).success(
			function(response){
					$scope.paginationConf.totalItems=response.total;//总记录数 
					$scope.list=response.list;//给列表变量赋值 
			}		
		);				
	}
});
