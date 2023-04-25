// Ajax 通信のため
var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
$(document).ajaxSend(function (e, xhr, options) { xhr.setRequestHeader(header, token); });

// defaultイメージ設定
function setDefaultImage(img) {
    img.src = '/assets/icon/cooking.png';
}