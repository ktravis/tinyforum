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
});
