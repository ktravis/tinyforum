SyntaxHighlighter.all();
$(document).ready(function () {
$(".hoverhide").hide();
$(".comment * .removebutton").hide(); 
$(".comment").on({
    mouseenter: function() {
        $(this).children(".hoverhide").show(); 
        $(this).find(".removebutton").slideDown(); 
    },
    mouseleave: function() {
        $(this).children(".hoverhide").hide(); 
        $(this).find(".removebutton").slideUp(); 
    }
});
$(".nologin").on({
    click: function() {
        window.location = "/login";
    }});
$(".userconfirm").on({
    click: function(event) {
        f = $(this).attr("does") || "do that?";
        x = confirm("Are you sure you want to " + f);
        if (!x) event.preventDefault();
    }});
$(".doubleconfirm").on({
    click: function(event) {
        f = $(this).attr("does") || "do that?";
        x = confirm("Are you sure you want to " + f);
        if (x) x = confirm("ARE YOU ABSOLUTELY SURE YOU WANT TO " + f);
        if (!x) event.preventDefault();
    }});
$(".message").delay(2000).slideUp(1600);
$(".error").delay(2000).slideUp(1600);
});
