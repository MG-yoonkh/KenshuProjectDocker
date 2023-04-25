document.onreadystatechange = function () {
    if (document.readyState === "complete") {
        document.getElementById("loading").style.display = "none";
    }
};


function setDefaultImage(img) {
    const randomNum = Math.floor(Math.random() * 5 + 1);
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

  }
  }

  const expired = /*[[${expired}]]*/ false;

    if (expired) {
        alert('期限切れのページです。');
    }