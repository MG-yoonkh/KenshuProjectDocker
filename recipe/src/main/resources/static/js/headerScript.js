document.addEventListener("DOMContentLoaded", function () {
  const btn_search = document.getElementById("btn_search");
  if (btn_search) {
    btn_search.addEventListener("click", function () {
      document.getElementById("kw").value = document.getElementById("search_kw").value;
      document.getElementById("searchForm").submit();
    });
  }
});