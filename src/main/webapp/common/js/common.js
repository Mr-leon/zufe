/*
 * 计算字符串长度，单字节为1，双字节为2
 */
function strlen(str){
    var len = 0;
    for (var i=0; i<str.length; i++) { 
     var c = str.charCodeAt(i); 
    //单字节加1 
     if ((c >= 0x0001 && c <= 0x007e) || (0xff60<=c && c<=0xff9f)) { 
       len++; 
     } 
     else { 
      len+=2; 
     } 
    } 
    return len;
}
//验证非负实数，最多2位小数
function IWS_CheckDecimal(obj) {
    var reg = /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/;
    if (reg.test(obj)) {
         return true;
    }else{
         return false;
    };
}
//验证正整数
function IWS_CheckNum(obj){
	var reg = /(^[1-9]\d*$)/;
    if (reg.test(obj)) {
        return true;
   }else{
        return false;
   };
}
//验证非负整数（正整数 + 0）
function CheckNum(obj){
	var reg = /^[1-9]\d*|0$/;
    if (reg.test(obj)) {
        return true;
   }else{
        return false;
   };
}

function CheckDateTime(str) {
	var reg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2})$/; 
	var r = str.match(reg); 
	if(r==null)return false; 
	var d= new Date(r[1], r[3]-1,r[4],r[5],r[6]); 
	return (d.getFullYear()==r[1]&&(d.getMonth()+1)==r[3]&&d.getDate()==r[4]&&d.getHours()==r[5]&&d.getMinutes()==r[6]);
}

function contrastTime(str) {
	var reg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2})$/; 
	var r = str.match(reg); 
	if(r==null)return false; 
	var d= new Date(r[1], r[3]-1,r[4],r[5],r[6]); 
	var dt = new Date();
	var st = dt.getFullYear()+"-"+(dt.getMonth()+1)+"-"+dt.getDate() + " " + dt.getHours() + ":" + dt.getMinutes();//获取当前实际日期
	if (Date.parse(st) > d) {//时间戳对比
	       return false;
	}else{
		return true;
	}
}

function IWS_CheckPhone(phone){
	 var myreg=/^[1][3,4,5,7,8][0-9]{9}$/;
     if (!myreg.test(phone)) {
         return false;
     } else {
         return true;
     }
}



/**
 * 减法运算，避免数据相减小数点后产生多位数和计算精度损失。
 * 
 * @param num1被减数  |  num2减数
 */
function numSub(num1, num2) {
	var baseNum, baseNum1, baseNum2;
	var precision;// 精度
	try {
		baseNum1 = num1.toString().split(".")[1].length;
	} catch (e) {
		baseNum1 = 0;
	}
	try {
		baseNum2 = num2.toString().split(".")[1].length;
	} catch (e) {
		baseNum2 = 0;
	}
	baseNum = Math.pow(10, Math.max(baseNum1, baseNum2));
	precision = (baseNum1 >= baseNum2) ? baseNum1 : baseNum2;
	return ((num1 * baseNum - num2 * baseNum) / baseNum).toFixed(precision);
};
'use strict';
function formatDig(num){
    return num>9?''+num:'0'+num;
};
function formatDate(mill){
    var y=new Date(mill);
    let raws= [
        y.getFullYear(),
        formatDig(y.getMonth()+1),
        formatDig(y.getDate())
    ];
    let format=['-','-'];
    return String.raw({raw:raws},...format);
};
function* createWeeks(year){
    const ONE_DAY=24*3600*1000;
    let start=new Date(year,0,1),
           end=new Date(year,11,31);
    let firstDay=start.getDay()|| 7,
            lastDay=end.getDay()||7;
    let startTime=+start,
            endTime=startTime+(7-firstDay)*ONE_DAY,
            _endTime=end-(7-lastDay)*ONE_DAY;
    yield [startTime,endTime];
    startTime=endTime+ONE_DAY;
    endTime=endTime+7*ONE_DAY;
    while(endTime<_endTime){
        yield [startTime,endTime];
        startTime=endTime+ONE_DAY;
        endTime=endTime+7*ONE_DAY;
    }
    yield [startTime,+end];
};

/**************************************时间格式化处理************************************/
function dateFtt(fmt,date)   
{ //author: meizz   
  var o = {   
    "M+" : date.getMonth()+1,                 //月份   
    "d+" : date.getDate(),                    //日   
    "h+" : date.getHours(),                   //小时   
    "m+" : date.getMinutes(),                 //分   
    "s+" : date.getSeconds(),                 //秒   
    "q+" : Math.floor((date.getMonth()+3)/3), //季度   
    "S"  : date.getMilliseconds()             //毫秒   
  };   
  if(/(y+)/.test(fmt))   
    fmt=fmt.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length));   
  for(var k in o)   
    if(new RegExp("("+ k +")").test(fmt))   
  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
  return fmt;   
} 

/**
 * 计算字符串所占的内存字节数，默认使用UTF-8的编码方式计算，也可制定为UTF-16
 * UTF-8 是一种可变长度的 Unicode 编码格式，使用一至四个字节为每个字符编码
 * 
 * 000000 - 00007F(128个代码)      0zzzzzzz(00-7F)                             一个字节
 * 000080 - 0007FF(1920个代码)     110yyyyy(C0-DF) 10zzzzzz(80-BF)             两个字节
 * 000800 - 00D7FF 
   00E000 - 00FFFF(61440个代码)    1110xxxx(E0-EF) 10yyyyyy 10zzzzzz           三个字节
 * 010000 - 10FFFF(1048576个代码)  11110www(F0-F7) 10xxxxxx 10yyyyyy 10zzzzzz  四个字节
 * 
 * 注: Unicode在范围 D800-DFFF 中不存在任何字符
 * {@link http://zh.wikipedia.org/wiki/UTF-8}
 * 
 * UTF-16 大部分使用两个字节编码，编码超出 65535 的使用四个字节
 * 000000 - 00FFFF  两个字节
 * 010000 - 10FFFF  四个字节
 * 
 * {@link http://zh.wikipedia.org/wiki/UTF-16}
 * @param  {String} str 
 * @param  {String} charset utf-8, utf-16
 * @return {Number}
 */
var sizeof = function(str, charset){
    var total = 0,
        charCode,
        i,
        len;
    charset = charset ? charset.toLowerCase() : '';
    if(charset === 'utf-16' || charset === 'utf16'){
        for(i = 0, len = str.length; i < len; i++){
            charCode = str.charCodeAt(i);
            if(charCode <= 0xffff){
                total += 2;
            }else{
                total += 4;
            }
        }
    }else{
        for(i = 0, len = str.length; i < len; i++){
            charCode = str.charCodeAt(i);
            if(charCode <= 0x007f) {
                total += 1;
            }else if(charCode <= 0x07ff){
                total += 2;
            }else if(charCode <= 0xffff){
                total += 3;
            }else{
                total += 4;
            }
        }
    }
    return total;
}
