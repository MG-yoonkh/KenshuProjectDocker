// Ajax 通信のため
var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
$(document).ajaxSend(function (e, xhr, options) { xhr.setRequestHeader(header, token); });

function setDefaultImage(img) {
    const randomNum = Math.floor(Math.random() * 10 + 1);
    switch (randomNum){
        case 1:
          img.src = '/assets/icon/cooking.png';
          break;
        case 2:
          img.src = '/assets/icon/cooking2.png';
          break;
        case 3:
          img.src = '/assets/icon/salad.png';
          break;
        case 4:
          img.src = '/assets/icon/egg.png';
          break;
        case 5:
            img.src = '/assets/icon/fish.png';
        break;
        case 6:
          img.src = '/assets/icon/crab.png';
          break;
        case 7:
          img.src = '/assets/icon/garlic.png';
          break;
        case 8:
          img.src = '/assets/icon/lemon.png';
          break;
        case 9:
          img.src = '/assets/icon/salt.png';
          break;
        case 10:
            img.src = '/assets/icon/shrimp.png';
        break;

  }
  }

const toggle_btn = document.querySelector(".nav-toggle");
const toggle_menu = document.querySelector(".nav-menu");

toggle_btn.addEventListener("click", () => {
  toggle_menu.classList.toggle("active");
});



