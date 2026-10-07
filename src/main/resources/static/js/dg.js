/*Thanks to Alistapart*/

var TEXT_SIZES = {
	"Standard": "",
	"Larger": "text-larger",
	"Largest": "text-largest"
};

var TEXT_SIZE_KEY = "textSize";

function getSavedTextSize() {
	try {
		var size = localStorage.getItem(TEXT_SIZE_KEY);
		return TEXT_SIZES.hasOwnProperty(size) ? size : "Standard";
	} catch (e) {
		return "Standard";
	}
}

function saveTextSize(size) {
	try {
		localStorage.setItem(TEXT_SIZE_KEY, size);
	} catch (e) {
	}
}

function applyTextSize(size) {
	var html = document.documentElement;
	html.classList.remove("text-larger", "text-largest");
	if (TEXT_SIZES[size]) {
		html.classList.add(TEXT_SIZES[size]);
	}
}

applyTextSize(getSavedTextSize());

// Setting existing old browser cookies to expire immediately. To be removed in next release
document.cookie = "style=; path=/; max-age=0";

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

	// Buttons with aria-pressed so screen readers announce which size is selected
	$("#TextSize")
		.attr({ "role": "group", "aria-labelledby": "TextSizeLabel" })
		.html(
			"<p id=\"TextSizeLabel\">Resize text:</p>" +
			"<ul>" +
			"<li>" +
			"<button type=\"button\" data-text-size=\"Standard\">" +
			"<span class=\"access\">Resize text to standard </span>A" +
			"</button>" +
			"</li>" +
			"<li class=\"medium\">" +
			"<button type=\"button\" data-text-size=\"Larger\">" +
			"<span class=\"access\">Resize text to larger </span>A" +
			"</button>" +
			"</li>" +
			"<li class=\"large\">" +
			"<button type=\"button\" data-text-size=\"Largest\">" +
			"<span class=\"access\">Resize text to largest </span>A" +
			"</button>" +
			"</li>" +
			"</ul>"
		);

	var savedTextSize = getSavedTextSize();
	$("#TextSize button[data-text-size]").each(function() {
		$(this).attr("aria-pressed", String($(this).data("text-size") === savedTextSize));
	});

	$("#TextSize").on("click", "button[data-text-size]", function() {
		var size = $(this).data("text-size");
		applyTextSize(size);
		saveTextSize(size);

		$("#TextSize button[data-text-size]").attr("aria-pressed", "false");
		$(this).attr("aria-pressed", "true");
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
