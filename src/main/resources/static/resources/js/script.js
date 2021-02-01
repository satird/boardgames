//мобильный гамбургер
$('#hamburger').click(function () {
    $(this).toggleClass('active');
    $('.script-menu').toggleClass('active');
    $('html').toggleClass('noscroll');
});

//input[type=file] change text button
$(document).ready(function () {
    $('input[type="file"]').change(function () {
        var value = $("input[type='file']").val();
        $('.js-value').text(value);
        console.log("value - " + value)
    });
});

//change page size
$('.js-filter-handler').click(function () {
    // $('.js-filter-dd').slideUp(300);
    $(this).next('.js-filter-dd').slideToggle(300);
    $(this).toggleClass('active');
});
$('.js-filter-dd a').click(function () {
    let val = $(this).text();

    // следующие два пункта добавил для выделение активного пункта
    $(this).closest('.js-filter-item').find('.js-filter-dd a').removeClass('active');
    $(this).addClass('active');

    $(this).closest('.js-filter-item').find('.js-filter-handler').text(val);
    val = $(this).data('id');
    $(this).closest('.js-filter-item').find('.page-size').val(val);
    console.log($(this).closest('.js-filter-item').find('input').val());
});

// я так понимаю для активации формы, времменно комментирую
$('.filter-form .js-filter-dd a').click(function () {
    $('.filter-form').submit();
});
$('body').on('click', function (e) {
    if ($(e.target).closest('.js-filter-handler').length === 0) {
        $('.js-filter-dd').slideUp(300);
    }
});