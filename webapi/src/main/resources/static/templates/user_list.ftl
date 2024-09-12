<#setting number_format="#">
<#include "head.ftl">
<style style="text/css">
	#createBox{
		width: 100%;
		height: 80px;
		line-height: 80px;
		text-align: left;
	}
	
	#createBox a{
		display: inline-block;
		margin-left: 10px;
	}
</style>
<script type="text/javascript">
	$(function(){
		$('#createNew').click(function(){
			createNewUser();
		});
		
		$('#createBtn').live('click',function(){
			var url = '/user/register';
			var param = {
					"username": $.trim($('#username').val()),
					"password": $.trim($('#password').val()),
					"email": $.trim($('#email').val())
				};
			$.ajax({
			    url: url,
				type:'post',
				dataType:'json',
				contentType: 'application/json',
				data: JSON.stringify(param),
				success: function(data){
				    if(data.code == 1){
				    	window.location.reload();
				    }else{
				    	alert(data.msg);
				    }
				},
				error: function(data,statusText){
					if(data.responseText){
						resp = JSON.parse(data.responseText);
						alert(resp.msg);
					}

				}
			});
		});
		
		$('#editBtn').live('click',function(){
			var url = '/user/update';
			var param = {
					"id": $.trim($('#userId').val()),
					"username": $.trim($('#username').val())
				};
		    $.ajax({
			    url: url,
				type:'put',
				dataType:'json',
				contentType: 'application/json',
				data: JSON.stringify(param),
				success: function(data){
				    if(data.code == 1){
				    	window.location.reload();
				    }else{
				    	alert(data.msg);
				    }
				},
				error: function(data,statusText){
					if(data.responseText){
						resp = JSON.parse(data.responseText);
						alert(resp.msg);
					}

				}
			});
		});
		
		$('.editOld').click(function(){
			var id = $.trim($(this).parent().parent().find('td:eq(0)').text());
			var url = '/user/' + id;
			$.ajax({
			    url: url,
				type:'get',
				contentType: 'application/json',
				success: function(data){
				    if(data.code == 1){
				    	editUser(data.data);
				    }else{
				    	alert(data.msg);
				    }
				}
			});
		});
		
		$('.deleteOld').click(function(){
			var id = $.trim($(this).parent().parent().find('td:eq(0)').text());
			var url = '/user/' + id;
			$.ajax({
			    url: url,
				type:'delete',
				contentType: 'application/json',
				success: function(data){
				    if(data.code == 1){
				    	window.location.reload();
				    }
				}
			});
		});
	});
	
	function editUser(data){
		$('.popTitle').text('Edit Existing User');
		var h = '<div style="width: 500px;height: 300px;overflow: hidden; padding: 5px;">';
		h += '<p><label>Username: </label>';
		h += '<input type="text" name="username" id="username" value="'+ data.username + '"/></p>';
		h += '<p><input type="hidden" id="userId", value="' + data.id + '"/><input type="button" value="Save" id="editBtn" class="cBtn"/></p>';
		h += '</div>';
		$(".popBox").find('.popContent').html(h);
		$(".popBox").OpenDiv(Utils.getScrollTop() + 80);
	}

	function createNewUser(){
		$('.popTitle').text('Create New User');
		var h = '<div style="width: 500px;height: 300px;overflow: hidden; padding: 5px;">';
		h += '<p><label>Username: </label>';
		h += '<input type="text" name="username" id="username"/></p>';
		h += '<p><label>Password: </label>';
		h += '<input type="password" name="password" id="password"/></p>';
		h += '<p><label>Email: </label>';
		h += '<input type="text" name="email" id="email"/></p>';
		h += '<p><input type="button" value="Save" id="createBtn" class="cBtn"/></p>';
		h += '</div>';
		$(".popBox").find('.popContent').html(h);
		$(".popBox").OpenDiv(Utils.getScrollTop() + 80);
	}
</script>
<body>
	<div id="container">
		<div id="createBox">
			<a href="javascript:void(0);" id="createNew">Create New User</a>
		</div>
		<div id="tabContent">
		<table border="0" cellspacing="0" cellpadding="0" class="tblCom" width="100%">
			<thead>
				<tr>
					<td width="5%" class="tdRight5">
						ID
					</td>
					<td width="10%" class="tdLeft10">
						User Name
					</td>
					<td class="tdLeft10">
						Email
					</td>
					<td width="15%">
						Created Time
					</td>
					<td width="15%">
						Updated Time
					</td>
					<td width="10%">
						&nbsp;
					</td>
				</tr>
			</thead>
			<tbody>
				<#if userList ?? && userList? size gt 0>
					<#list userList as bean>
						<tr>
							<td width="5%" class="tdRight5">
							    ${(bean.id)!}
							</td>
							<td width="10%" class="tdLeft10">
								${(bean.username)!}
							</td>
							<td class="tdLeft10">
								${(bean.email?html)!}
							</td>
							<td width="15%">
								${(bean.createdAt?string('yyyy-MM-dd HH:mm:ss'))!}
							</td>
							<td width="15%">
								${(bean.updatedAt?string('yyyy-MM-dd HH:mm:ss'))!}
							</td>
							<td>
								<a href="javascript:void(0)" class="editOld">Edit<a>&nbsp;
								<a href="javascript:void(0)" class="deleteOld">Delete<a>
							</td>
						</tr>
					</#list>
				<#else>
					<tr>
						<td colspan="6">
							<p class="tabNoData">
								No data and please search again.
							</p>
						</td>
					</tr>
				</#if>
			</tbody>
		</table>
		</div>
	</div>
	<div class="popBox" style="display: none;">
			<div class="popTop">
				<span class="popTitle"></span>
				<span class="popClose" title="Close"></span>
				<hr color="#d0d0d0"/>
			</div>
			<div class="popContent"></div>
		</div>
	<#include "foot.ftl">
</body>
</html>