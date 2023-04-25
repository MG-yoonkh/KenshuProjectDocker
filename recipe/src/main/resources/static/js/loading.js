document.onreadystatechange = function () {
    if (document.readyState === "complete") {
        document.getElementById("loading").style.display = "none";
    }
};


  const expired = /*[[${expired}]]*/ false;

    if (expired) {
        alert('期限切れのページです。');
    }