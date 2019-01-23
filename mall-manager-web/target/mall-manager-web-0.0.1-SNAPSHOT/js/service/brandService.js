/**
 * 自定义服务,实现前端分层开发
 */
app.service('brandService', function($http) {
	this.findAll = function()
	{
		return $http.get('/brand/findAll');	
	}
	this.findPage = function(page, rows) {
		return $http.get('/brand/findPage?page=' + page + '&rows=' + rows);
	}
	this.add = function(entity) {
		return $http.post('/brand/add', entity);
	}
	this.update = function(entity) {
		return $http.post('/brand/update', entity);
	}
	this.findOne = function(id) {
		return $http.get('/brand/findOne?id=' + id);
	}
	this.dele = function(selectIds) {
		return $http.get('/brand/delete?ids='+selectIds);
	}
	this.search = function(page,rows,searchEntity) {
		return $http.post('/brand/search?page='+page+"&rows="+rows, searchEntity);
	}
	//下拉列表数据
	this.selectOptionList=function(){
		return $http.get('/brand/selectOptionList');
	}

})