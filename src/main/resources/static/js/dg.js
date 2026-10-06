/*Thanks to Alistapart*/

function setActiveStyleSheet(title) {
	var html = document.documentElement;

	if (title !== "Standard" &&
		title !== "Larger" &&
		title !== "Largest") {
		title = "Standard";
	}

	html.classList.remove("text-larger", "text-largest");

	if (title === "Larger") {
		html.classList.add("text-larger");
	} else if (title === "Largest") {
		html.classList.add("text-largest");
	}

	saveTextSize(title);
}

function getSavedTextSize() {
	try {
		return localStorage.getItem("textSize") || "Standard";
	} catch (e) {
		return "Standard";
	}
}

function saveTextSize(title) {
	try {
		localStorage.setItem("textSize", title);
	} catch (e) {
		// Ignore storage errors
	}
}

$(document).ready(function(){

	$(".pagehelp").prepend("<a href='#'>Show help</a>");
	$(".pagehelp > div").addClass("access");
	$(".pagehelp > a").click( function() {
		$(this).next("div").toggleClass('show');
	});
	var links = $(".pagehelp > a");
	var strongs = $(".pagehelp > div > p > strong");
	var link;
	for (var i=0; i < links.length; i++) {
		link = links[i];
		strong = strongs[i];
		temp = "#help"+i;
		link.href = temp;
		strong.id = "help"+i;
		var temp2 = "<span class='access'> "+strong.innerHTML+"</span>"
		$(link).append(temp2);
	}

	$(".pagehelp > a").toggle(
		function() {
			var temp3 = $(this).children();
			$(this).text("Hide help");
			var temp4 = "<span class='access'> "+temp3[0].innerHTML+"</span>";
			$(this).append(temp4);},
		function() {
			var temp3 = $(this).children();
			$(this).text("Show help");
			var temp4 = "<span class='access'> "+temp3[0].innerHTML+"</span>";
			$(this).append(temp4);}
	);

	$("#TextSize").html(
		"<p>Resize text:</p>" +
		"<ul>" +
		"<li>" +
		"<a href=\"#standard\" data-text-size=\"Standard\">" +
		"<span class=\"access\">Resize text to standard </span>A" +
		"</a>" +
		"</li>" +
		"<li class=\"medium\">" +
		"<a href=\"#larger\" data-text-size=\"Larger\">" +
		"<span class=\"access\">Resize text to larger </span>A" +
		"</a>" +
		"</li>" +
		"<li class=\"large\">" +
		"<a href=\"#largest\" data-text-size=\"Largest\">" +
		"<span class=\"access\">Resize text to largest </span>A" +
		"</a>" +
		"</li>" +
		"</ul>"
	);

	var savedTextSize = getSavedTextSize();
	setActiveStyleSheet(savedTextSize);

	$("#TextSize").on("click", "a[data-text-size]", function(event) {
		event.preventDefault();
		var title = $(this).data("text-size");
		setActiveStyleSheet(title);

		$("#TextSize a[data-text-size]").removeClass("active");
		$(this).addClass("active");
	});

	$("#TextSize a[data-text-size]").each(function() {
		$(this).toggleClass(
			"active",
			$(this).data("text-size") === savedTextSize
		);
	});

	$(".close").css({display:"block"}).addClass("right function").append("<span class='tl'></span><span class='tr'><span></span></span><a href='#' onclick='closeWindow();'>Close<span class='access'> window</span></a><span class='bl'></span><span class='br'></span>");

	$(".newwindow").click(addVariable);
});

function addVariable() {
	var origin = window.location;
	var target = $(this).attr("href");
	$(this).attr("href", target+"?backtoPage="+origin);
}

function closeWindow() {
	var backto = gup("backtoPage");

	if (window.opener && !window.opener.closed) {

		try {
			var url = new URL(backto, window.location.origin);

			if (url.origin === window.location.origin) {
				window.opener.location = url.pathname + url.search;
			}
		}
		catch (e) {
			// Ignore invalid URLs
		}

		window.close();
	}
	else {
		window.close();
	}
}

function gup(name) {
	name = name.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');

	var regex = new RegExp("[\\?&]" + name + "=([^&#]*)");
	var results = regex.exec(window.location.href);

	return results ? results[1] : "";
}
