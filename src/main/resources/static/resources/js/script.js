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

// активации формы
$('.filter-form .js-filter-dd a').click(function () {
    $('.filter-form').submit();
});
$('body').on('click', function (e) {
    if ($(e.target).closest('.js-filter-handler').length === 0) {
        $('.js-filter-dd').slideUp(300);
    }
});
//Удаляем из url hash вызова модального окна, при закрытие
function removeHash () {
    history.pushState("", document.title, window.location.pathname
        + window.location.search);
}
//open modal
//////////////////////
$('.open-modal').click(function(event) {
    var id = $(this).attr('href');
    $('.modal').hide();
    $('.modal').removeClass('active');
    $('.overlay').fadeIn(200);
    $(id).show();
    $(id).addClass('active');
    $('html, header').width($('html, header').width());
    $('html').css('overflow', 'hidden');
});
// Закрыть модальные
$('.btn-close-modal, .overlay, .close-modal, .close').click(function() {
    $('.modal').hide();
    $('.modal').removeClass('active');
    $('.modal-content').hide();
    $('.btn-arrow-modal').fadeOut(200);
    $('.overlay').fadeOut(200);
    $('html').removeAttr('style');
    removeHash ();
});


// Передача id сообщения в модальное окно
$('.comment-head-right .comment-head_delete').on('click', function () {
    var idComment = $(this).parent().find('.idComment').val();
    $('#deleteModal .commentId').val(idComment);
})
$('.comment-head-right .comment-head_edit').on('click', function () {
    var id = $(this).parent().find('.idComment').val();
    $.ajax({
        type: 'GET',
        url: '/api/comments/find/' + id,
        success: function (comment) {
            $('#editModal .commentId').val(comment.id);
            $('#editModal #editComment').val(comment.text);
        }
    });
})