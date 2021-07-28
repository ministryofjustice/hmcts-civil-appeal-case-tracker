// JavaScript Document
var objOpener = window.opener;
var NN6 = (document.getElementById && navigator.appName.indexOf("Netscape")>=0 )? true: false;
var NN4 = (navigator.appName.indexOf("Netscape")>=0 && !document.getElementById)? true : false;

function ReturnDate(CONTROL){
DAY = CONTROL.toString();
MONTH = (parseInt(CalendarForm.cboMonth.options[CalendarForm.cboMonth.selectedIndex].value) + 1);
YEAR = CalendarForm.cboYear.options[CalendarForm.cboYear.selectedIndex].value;
var DateOutput = FormatDate();
objOpener.SetDate(DateOutput);
window.close();
} //End Function

function FormatDate(){
var FormatAs = objOpener.FormatAs;


	switch (FormatAs){
	case "dd/mm/yy":
	FillZero();
	return DAY + "/" + MONTH + "/" + YEAR;
	
	case "mm/dd/yyyy":
	FillZero();
	return MONTH + "/" + DAY + "/" + YEAR;
	
	case "dd/mmm/yyyy":
	return DAY + "-" + arrMonths[MONTH -1].substring(0,3) + "-" + YEAR;
	
	case "mmm/dd/yyyy":
	FillZero();
	return DAY + "-" + arrMonths[MONTH -1].substring(0,3)  + "-" + YEAR;
	
	case "dd/mmmm/yyyy":
	FillZero();
	return DAY + "-" + arrMonths[MONTH -1] + "-" + YEAR;	
	
	case "mmmm/dd/yyyy":
	FillZero();
	return arrMonths[MONTH -1] + " " + DAY + " " + YEAR;
	}


FillZero();
return DAY + "-" + arrMonths[MONTH -1].substring(0,3)  + "-" + YEAR.substring(2,4);
}//End Function

function FillZero(){
if(DAY.length < 2){
DAY = "0" + DAY;
}
if(MONTH.toString().length < 2){
MONTH = "0" + MONTH;
}
}//End Function

var Today = new Date();
var Day = Today.getDay();
var Month = Today.getMonth();
var Year = Today.getYear();

var strDays = "Monday,Tuesday,Wednesday,Thursday,Friday,Saturday,Sunday";
var arrDays = strDays.split(",");
var strMonths = "January,February,March,April,May,June,July,August,September,October,November,December";
var arrMonths = strMonths.split(",");
var CalendarForm = document.CalendarForm;
var IsToday = Today.getDate() + "/" + (Month + 1) + "/" + Year;
var StartYear;
var EndYear;


StartYear = parseInt(objOpener.StartYear);
EndYear = parseInt(objOpener.EndYear);


if(!StartYear){
StartYear = Year - 50;
}
if(!EndYear){
EndYear = Year + 1;
}

FillYears(StartYear,EndYear);

FillMonths();
WriteMonth(Month,Year);


function FillMonths(){
for(var i = 0; i < arrMonths.length;i++){
CalendarForm.cboMonth.options[i] = new Option(arrMonths[i], i);
}
CalendarForm.cboMonth.options[Month].selected = true;
}

function FillYears(START_YEAR,END_YEAR){
	START_YEAR = parseInt(START_YEAR);
	END_YEAR = parseInt(END_YEAR);
	var i =0;
	var today = new Date();
	var currentYear = today.getFullYear();
	
	while(START_YEAR <= currentYear){
		CalendarForm.cboYear.options[i] = new Option(START_YEAR, START_YEAR);
		if(START_YEAR==currentYear){
			CalendarForm.cboYear.options[i].selected = true;
		}

		START_YEAR +=1;
		i+=1;
	}
} //End Function

function SetMonth(){
WriteMonth(CalendarForm.cboMonth.selectedIndex,CalendarForm.cboYear.options[CalendarForm.cboYear.selectedIndex].value);
}

function WriteMonth(MONTH,YEAR){
var CELLSPACING = 0;
if(NN6){
CELLSPACING = 1;
}
var strCalendar = "<br /><table border=0 cellspacing=0 cellpadding=" + CELLSPACING + ">";
strCalendar += "<tr>";
for(var i = 0; i < arrDays.length; i++){
strCalendar += "<td class=SmallFont align=center>" + arrDays[i].substring(0,2) + "</td>";
}

strCalendar += "</tr>";

var strTemp = "1 " + arrMonths[MONTH] + " " + YEAR;
var DateTemp = new Date(strTemp);
var FirstDay = DateTemp.getDay();
var iStart;
if(FirstDay==0){
FirstDay=6;
}
else{
FirstDay = FirstDay - 1;
}

for(var RowCount = 0; RowCount < 42; RowCount = RowCount + 7){
strCalendar += "<tr>";

	for(var i = RowCount; i < (RowCount + 7);i++){	

	strTemp = ((i-FirstDay) + 1) + "/" + (MONTH + 1) + "/" + YEAR;
		if(i >= FirstDay && i < (31 + FirstDay)){		
			if(isDate(strTemp)){
			strCalendar += "<td align=center width=15%>";
			if(strTemp == IsToday && EndYear > Year-1){

			strCalendar += "<a class=CalButtonToday href='#' onclick='ReturnDate(" + ((i-FirstDay) + 1) + ");'>" + ((i-FirstDay) + 1);	
			}
			else{
			strCalendar += "<a class=CalButton href='#' onclick='ReturnDate(" + ((i-FirstDay) + 1) + ");'>" + ((i-FirstDay) + 1);
			}
			strCalendar += "</td>";
			}
			else{
			strCalendar += "<td width=15%> </td>";
			}
		}
		else{
		strCalendar += "<td width=15%> </td>";
		}

	}//End For

strCalendar += "</tr>";
}


strCalendar += "</table>";
WriteInnerHTML("ButtonPanel",strCalendar);

} //End Function

function isDate(DateToCheck){
var arrDate = DateToCheck.split("/");
var myDAY = arrDate[0];
var myMONTH = arrDate[1];
var myYEAR = arrDate[2];
var strDate;
strDate = myMONTH + "/" + myDAY + "/" + myYEAR;
var testDate=new Date(strDate);
if(testDate.getMonth()+1==myMONTH){
return true;
} 
else{
return false;
}
}//end function



function WriteInnerHTML(ElementID, Value){
if(document.getElementById){
document.getElementById(ElementID).innerHTML = Value;
}
else if(document.all){
document.all[ElementID].innerHTML = Value;
}
else if(NN4){
document.layers[ElementID].document.open();
document.layers[ElementID].document.write(Value);
document.layers[ElementID].document.close();
}
}


if(NN6){
document.getElementById("ButtonPanel").style.top= 33;
document.getElementById("ButtonPanel").style.left= 25;
}
