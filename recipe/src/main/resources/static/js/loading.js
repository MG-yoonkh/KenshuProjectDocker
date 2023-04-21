document.onreadystatechange = function () {
    if (document.readyState === "complete") {
        document.getElementById("loading").style.display = "none";
    }
};


function setDefaultImage(img) {
    img.src = '/assets/img/default.png';
  }

  const expired = /*[[${expired}]]*/ false;

    if (expired) {
        alert('期限切れのページです。');
    }