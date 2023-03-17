
window.onload = function () {
    displayRememberId();
}

function checkValue() {
    var user_email = document.getElementById("user_email").value;
    var user_password = document.getElementById("user_password").value;
    if (user_email == "") {
        alert("Please enter ID.");
        document.getElementById("user_email").focus();
        return false;
    }
    if (user_password == "") {
        alert("Please enter Password.");
        document.getElementById("user_password").focus();
        return true;
    }
    if (user_email != "" && user_password != "") {
        confirmSubmit(user_email, user_password);
    }
}

function confirmSubmit(user_email, user_password) {
    var agree = confirm("Are you sure you wish to continue?");
    if (agree) return login(user_email, user_password)
    else return false;
}

var rightId = "aaa@gmail.com";
var rightPw = "12345";
function login(id, pw) {
    if (id == rightId) {
        if (pw == rightPw) {
            setTimeout(document.write("Welcome, " + id), 30000);
        }
        else {
            alert("Invalid Password.");
            displayRememberId();
        }
    }
    else {
        alert("Invalid ID");
        displayRememberId();
    }
}

if ($('#remember_check').is(':checked')) {
    $.cookie('remember_check', $('#id').val());
} else {
    $.cookie('remember_check', '');
}

function displayRememberId() {
    let remember_check = $.cookie('remember_check');
    if (remember_check == '') {
        $('#user_email').val('');
        $('#remember_check').prop('checked', false); // check 해제
    } else {
        $('#user_email').val(remember_check);
        $('#remember_check').prop('checked', true); // check 해제
    }
}
