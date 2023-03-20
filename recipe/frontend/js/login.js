"use strict";

//　sessionStorageを使用してメールアドレスを記憶　ー＞　作動しない
window.onload = function () {
    displayRememberId();
}

// メールアドレスの有効性チェック
function CheckEmail(str) {
    var reg_email = /^([0-9a-zA-Z_\.-]+)@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}$/;
    if (!reg_email.test(str)) {
        return false;
    }
    else {
        return true;
    }
}

//　ID、パスワード
var rightId = "aaa@gmail.com";
var rightPw = "12345";

//　ログイン
function login(id, pw) {
    if (id == rightId) {
        if (pw == rightPw) {
            setTimeout(document.write("Welcome, " + id), 30000);
        }
        else {
            alert("パスワードを確認してください。");
        }
    }
    else {
        alert("メールアドレスを確認してください。");
    }
}

//　ログインボタン、又はenterキーが押されたら
function checkValue() {
    var user_email = document.getElementById("user_email");
    var user_password = document.getElementById("user_password");
    if (!user_email.value) {　
        alert("メールアドレスを入力してください。");
        user_email.focus();
        return false;
    }
    else {
        if (!CheckEmail(user_email.value)) { // 有効性チェック
            alert("有効なメールアドレスを入力してください。");
            user_email.focus();
            return false;
        }
    }
    if (!user_password.value) {
        alert("パスワードを入力してください。");
        user_password.focus();
        return false;
    }
    if (user_email.value && user_password.value) {
        login(user_email.value, user_password.value); // ログイン
    }

}

// チェックしたらsessionStorageを使用してメールアドレスを記憶　ー＞　作動しない
if ($('#js-remember_check').is(':checked')) {
    sessionStorage.setItem("remember_check", $('#user_email').val());
} 

function displayRememberId() {
    let saveIdValue = sessionStorage.getItem("remember_check");
    if(!saveIdValue){
        $(f1).find('input[name=id]').val(saveIdValue);
    }
}


// チェックしたらクッキーを使用してメールアドレスを記憶　　ー＞　作動しない
// if ($('#js-remember_check').is(':checked')) {
//     $.cookie('remember_check', $('#user_email').val());
// } else {
//     $.cookie('remember_check', '');
// }

// function displayRememberId() {
//     let remember_check = $.cookie('remember_check');
//     if (remember_check == '') {
//         $('#user_email').val('');
//         $('#js-remember_check').prop('checked', false); // check 해제
//     } else {
//         $('#user_email').val(remember_check);
//         $('#js-remember_check').prop('checked', true); // check 해제
//     }
// }