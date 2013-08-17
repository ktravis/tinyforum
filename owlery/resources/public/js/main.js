$(document).ready(function () {
$(".hoverhide").hide();
$(".comment").on({
    mouseenter: function() {
        $(this).children(".hoverhide").show(); 
    },
    mouseleave: function() {
        $(this).children(".hoverhide").hide(); 
    }
});
$(".nologin").on({
    click: function() {
        window.location = "/login";
    }});
$(".message").delay(2000).slideUp(1600);
});
