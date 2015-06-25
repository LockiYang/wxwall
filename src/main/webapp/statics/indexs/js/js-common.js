// JavaScript Document

/**
 * 给canvas画圆
 * 
 * @param	int		quantity	需要画圆的数量
 * @param	string	idstr		ID
 * @param	int		radius		半径
 * @param	array	color		颜色数据
 * @param	array	data		比例数据
 * @return	void
 */
function drawCircle(quantity, idstr, radius){
	//alert(quantity+' , '+idstr+' , '+radius+' , '+canvas_color+' , '+canvas_data);
	for(var j=0; j<quantity; j++){
		//alert(quantity+','+j);
		var canvas = document.getElementById(idstr+j);
		var ctx = canvas.getContext("2d");
		var startPoint = 1.5 * Math.PI;
		
		var temp_color = new Array();
		if(canvas_color[j] instanceof Array) temp_color = canvas_color[j]; else temp_color = canvas_color; 
		
		var temp_data = new Array();
		if(canvas_data[j] instanceof Array) temp_data = canvas_data[j]; else temp_data = canvas_data; 
		
		for(var i=0;i<temp_data.length;i++){  
			//alert(j+','+i);
			ctx.fillStyle = temp_color[i];  
			ctx.strokeStyle = temp_color[i];  
			ctx.beginPath();  
			ctx.moveTo(radius,radius);  
			ctx.arc(radius,radius,radius,startPoint,startPoint-Math.PI*2*(temp_data[i]/100),true);  
			ctx.fill();  
			ctx.stroke();  
			startPoint -= Math.PI*2*(temp_data[i]/100);  
		}
	}
}