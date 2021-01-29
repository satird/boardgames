//мобильный гамбургер
$('#hamburger').click(function() {
    $(this).toggleClass('active');
    $('.script-menu').toggleClass('active');
    $('html').toggleClass('noscroll');
});

//input[type=file] change text button
$(document).ready(function() {
    $('input[type="file"]').change(function(){
        var value = $("input[type='file']").val();
        $('.js-value').text(value);
        console.log("value - " + value)
    });
});