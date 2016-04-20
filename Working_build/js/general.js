// Loading drop down forms

<!--
function MM_jumpMenu(selObj,restore){ //v3.0 
var url = selObj.options[selObj.selectedIndex].value; 
if (url) window.open(url); 
if (restore) selObj.selectedIndex=0;
}
//-->

<!--
function MM_jumpMenuGo(objId,targ,restore){ //v9.0
  var selObj = null;  with (document) { 
  if (getElementById) selObj = getElementById(objId);
  if (selObj) eval(targ+".location='"+selObj.options[selObj.selectedIndex].value+"'");
  if (restore) selObj.selectedIndex=0; }
}
//-->

// homepage tabbing menu 
/*
var rotateSpeed = 5000000; // Milliseconds to wait until switching tabs.
var currentTab = 0; // Set to a different number to start on a different tab.
var numTabs; // These two variables are set on document ready.
var autoRotate;

function openTab(clickedTab) {
   var thisTab = $(".tabbed-box .tabs a").index(clickedTab);
   $(".tabbed-box .tabs li a").removeClass("active");
   $(".tabbed-box .tabs li a:eq("+thisTab+")").addClass("active");
   $(".tabbed-box .tabbed-content").hide();
   $(".tabbed-box .tabbed-content:eq("+thisTab+")").show();
   currentTab = thisTab;
}

function rotateTabs() {
   var nextTab = (currentTab == (numTabs - 1)) ? 0 : currentTab + 1;
   openTab($(".tabbed-box .tabs li a:eq("+nextTab+")"));
}

$(document).ready(function() {
   $(".tabbed-content").equalHeights();
   numTabs = $(".tabbed-box .tabs li a").length;
   $(".tabbed-box .tabs li a").click(function() { 
      openTab($(this)); return false; 
   });
   $(".tabbed-box").mouseover(function(){clearInterval(autoRotate)})
   .mouseout(function(){autoRotate = setInterval("rotateTabs()", rotateSpeed)});
   
   $(".tabbed-box .tabs li a:eq("+currentTab+")").click()
   $(".tabbed-box").mouseout();
   
});
*/

// Email a friend 

function mailpage()
{
mail_str = "mailto:?subject=Check out the " + document.title;
mail_str += "&body=I thought you might be interested in the " + document.title;
mail_str += ". You can view it at, " + location.href; 
location.href = mail_str;
}

// increase/decrease font size

var min=8;
var max=18;
function increaseFontSize() {
   var p = document.getElementsByTagName('p');
   for(i=0;i<p.length;i++) {
      if(p[i].style.fontSize) {
         var s = parseInt(p[i].style.fontSize.replace("px",""));
      } else {
         var s = 12;
      }
      if(s!=max) {
         s += 1;
      }
      p[i].style.fontSize = s+"px"
   }
}
function decreaseFontSize() {
   var p = document.getElementsByTagName('p');
   for(i=0;i<p.length;i++) {
      if(p[i].style.fontSize) {
         var s = parseInt(p[i].style.fontSize.replace("px",""));
      } else {
         var s = 12;
      }
      if(s!=min) {
         s -= 1;
      }
      p[i].style.fontSize = s+"px"
   }   
}


isSafari3 = false;
   if(window.devicePixelRatio) isSafari3 = true; 



// Social bookmarking/share with
/*
<!--
function schnipp() { 
var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}
function schnupp(n, d) { 
  var p,i,x; if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
  d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=schnupp(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
  }
function schnapp() { 
  var i,j=0,x,a=schnapp.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
  if ((x=schnupp(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
  }
  //-->
*/