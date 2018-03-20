//发送消息 日期控件

var month_big = new Array("1","3","5","7","8","10","12"); //包含所有大月的数组
var month_small = new Array("4","6","9","11"); //包含所有小月的数组 
export default {
	init(){ //初始化年份和月份
		var year = [] , month = [] , minutes = [] , hours = [], obj = {};
		//将年份初始化
		for(var i = 1980; i <= 2025; i++){
			 year.push(i);
		}
		//将月份选项初始化，从1到12
		for(var i = 1; i <= 12; i++)
	    {
	        month.push(i);
	    }
	    //初始化小时
	    for(var i=0; i <=23; i++ ){
	    	
	    	hours.push(i);
	    }
	    //初始化分
	    for(var i=0; i <=59; i++ ){
	    	
	    	minutes.push(i);
	    }
	    obj['year'] =year ;
	    obj['month'] =month ;
	    obj['hours'] =hours ;
	    obj['minutes'] =minutes ;
        return obj
	},
	init_day (year , month) { //初始化日期
		var day = [];
		if(array_contain(month_big , month)){ //判断月份是大月则初始化为31天 
			
			for(var i = 1; i <= 31; i++){
	            day.push(i);
	        }
			
		}else if(array_contain(month_small , month)){ //判断月份是小月则初始化为30天 
			
			for(var i = 1; i <= 30; i++){
	            day.push(i);
	        }
			
		}else { //根据年份来判断是否闰年来初始化二月的日期选项
			
			var year = parseInt(year);
			//判断是否是闰年 
			if(isLeapYear(year)){
				
				for(var i = 1; i <= 29; i++){
			        day.push(i); 
			    }
				 
			}else{
				
				for(var i = 1; i <= 28; i++){
			        day.push(i); 
			    }
			}
		}
		return day;
	}
}

//是否包含
function array_contain(array , obj){  //是否包含
	for(var i = 0; i <= array.length; i++){
        if(array[i] == obj){
			 return true;
		}
    }
	return false;
}

//是否是闰年
 function isLeapYear(year){
 	var a = year % 4;
    var b = year % 100;
    var c = year % 400;
    if( ( (a == 0) && (b != 0) ) || (c == 0) )
    {
        return true;
    }
    return false;
 }
