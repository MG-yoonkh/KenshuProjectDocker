"use strict";

window.onload = function () {
    displayRememberId();
}

function checkValue() {
    var user_email = document.getElementById("user_email").value;
    var user_password = document.getElementById("user_password").value;
    if (!user_email) {
        alert("メールアドレスを入力してください。");
        document.getElementById("user_email").focus();
        return false;
    }
    if (!user_password) {
        alert("パスワードを入力してください。");
        document.getElementById("user_password").focus();
        return false;
    }
    if (!user_email && !user_password) {
        confirmSubmit(user_email, user_password);
    }
}

function confirmSubmit(user_email, user_password) {
    var agree = confirm("ログインする。");
    if (agree) return login(user_email, user_password)
    else return false;
}

// var rightId = "aaa@gmail.com";
// var rightPw = "12345";

function login(id, pw) {
    if (id == rightId) {
        if (pw == rightPw) {
            setTimeout(document.write("Welcome, " + id), 30000);
        }
        else {
            alert("有効なパスワードを入力してください。");
        }
    }
    else {
        alert("有効なメールアドレスを入力してください。");
    }
}

if ($('#js-remember_check').is(':checked')) {
    $.cookie('remember_check', $('#id').val());
} else {
    $.cookie('remember_check', '');
}

function displayRememberId() {
    let remember_check = $.cookie('remember_check');
    if (remember_check == '') {
        $('#user_email').val('');
        $('#js-remember_check').prop('checked', false); // check 해제
    } else {
        $('#user_email').val(remember_check);
        $('#js-remember_check').prop('checked', true); // check 해제
    }
}
