// 특수문자 입력 방지
function characterCheck(obj) {
    var regExp = /[ \{\}\[\]\/?.,;:|\)*~`!^\-_+┼<>@\#$%&\'\"\\\(\=]/gi;
    if (regExp.test(obj.value)) {
      alert("특수문자는 입력하실수 없습니다.");
      obj.value = obj.value.substring(0, obj.value.length - 1); // 입력한 특수문자 한자리 지움
    }
  }
