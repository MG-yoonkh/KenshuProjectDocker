// 直接入力
$(function () {
    $("#qtyDirect").hide(); // Input Hidden
    $("#qty-dropdown").change(function () {
      if ($("#qty-dropdown").val() == "direct") { // 入力をセレクトすると、
        $("#qtyDirect").show(); // Input Display
      } else {
        $("#qtyDirect").hide(); // Input Hidden
      }
    })
  });

  // Modal Click
  $(document).ready(function () {

    document.querySelector(".modal-ing").addEventListener("click", function () {

      // 1. データ: mainCategory (DB)
      $.ajax({
        type: "GET",
        url: "/ingredient/main",
        dataType: "json",
        success: function (response) {

          // リセット
          $("#main-category-dropdown").empty();

          // 「選択してください」を入れる
          var option = $("<option>").text("選択してください").attr("value", "");
          $("#main-category-dropdown").append(option);
          // メインをリストに入れる
          $.each(response, function (index, main) {
            var option = $("<option>").text(main.name).attr("value", main.id);
            $("#main-category-dropdown").append(option);
          });

        },
        error: function (xhr, textStatus, errorThrown) {
          console.log("Error: " + errorThrown);
        }
      });

      // 2. データ: Unit (DB)
      $.ajax({
        type: "GET",
        url: "/ingredient/unit",
        dataType: "json",
        success: function (response) {

          // リセット
          $("#unit-dropdown").empty();

          // 「選択してください」を入れる
          var option = $("<option>").text("選択してください").attr("value", "");
          $("#unit-dropdown").append(option);

          // Unitを入れる
          $.each(response, function (index, unit) {
            var option = $("<option>").text(unit.name).attr("value", unit.id);
            $("#unit-dropdown").append(option);
          });

        },
        error: function (xhr, textStatus, errorThrown) {
          console.log("Error: " + errorThrown);
        }
      });

      //localStorage.clear();

      if (selectedItems.length > 0) {

        // 3. データ: savedList (Object)
        for (let i = 0; i < selectedItems.length; i++) {

          // selectedItems リセット
          $("#main-category-dropdown").empty();
          $("#sub-category-dropdown").empty();
          $("#ingredient-dropdown").empty();
          $("#unit-category-dropdown").empty();

          $("#main-category-dropdown").append($("<option>").text("選択してください").attr("value", ""));
          $("#sub-category-dropdown").append($("<option>").text("選択してください").attr("value", ""));
          $("#ingredient-dropdown").append($("<option>").text("選択してください").attr("value", ""));
          $("#unit-dropdown").append($("<option>").text("選択してください").attr("value", ""));

          $(function () {
            $("#qtyDirect").hide(); // Input Hidden
            $("#qty-dropdown").change(function () {
              if ($("#qty-dropdown").val() == "direct") {
                $("#qtyDirect").show(); // Input Display
              } else {
                $("#qtyDirect").hide(); // Input Hidden
              }
            });
            $("#qty-dropdown option:first").prop("selected", true);
          });

          selectedItems = [];
          //localStorage.clear();
          const key = localStorage.key(i);
          const value = localStorage.getItem(key);

          console.log('-----------------');
          console.log('localStorage length: ' + localStorage.length);
          console.log('selectedItems.length: ' + selectedItems.length);
          console.log('savedList.length: ' + savedList.length);
          // console.log('localStorage data: ' + key + " : " + value);
          console.log('-----------------');

          //paintText(localStorage.getItem(savedList));

          // savedList 出力
          let tableBody = document.querySelector("#selected-items");
          tableBody.innerHTML = "";

          for (let i = 0; i < savedList.length; i++) {
            let row = document.createElement("tr");
            row.className = "draggable";
            row.draggable = true;

            let cell0 = document.createElement("div");
            cell0.className = "el";

            let cell1 = document.createElement("td");
            cell1.className = "table-ing-name"; //class 속성 추가
            cell1.textContent = savedList[i].ingredient;
            cell1.value = savedList[i].ingredientValue;

            let cell2 = document.createElement("td");
            cell2.className = "table-ing-qty";
            cell2.textContent = savedList[i].qty;
            cell2.value = savedList[i].qtyValue;

            let cell3 = document.createElement("td");
            cell3.className = "table-ing-unit";
            cell3.textContent = savedList[i].unit;
            cell3.value = savedList[i].unitValue;

            let cell4 = document.createElement("td");
            cell4.className = "table-ing-unit";
            let button = document.createElement("button");
            button.type = "button";
            button.className = "btn-close";
            button.id = "deleteBtn";
            button.dataset.tableIngName = cell1.value;
            cell4.appendChild(button);


            cell0.appendChild(cell1);
            cell0.appendChild(cell2);
            cell0.appendChild(cell3);
            cell0.appendChild(cell4);

            row.appendChild(cell0);
            row.appendChild(cell1);
            row.appendChild(cell2);
            row.appendChild(cell3);
            row.appendChild(cell4);

            tableBody.appendChild(row);




          }

        }

        // document.addEventListener("DOMContentLoaded", function () {
        //   tableBody.addEventListener("click", (event) => {
        //     const target = event.target;
        //     const id = event.target.dataset.tableIngName;
        //     console.log('id 1: ' + id);

        //   });
        // });

      }




    });

  });


  // 詳細カテゴリー
  $(document).ready(function () {
    $("#main-category-dropdown").change(function () {
      var parentId = $(this).val();
      $.ajax({
        type: "POST",
        url: "/ingredient/sub",
        data: { parentId: parentId },
        dataType: "json",
        success: function (response) {

          // リセット
          $("#sub-category-dropdown").empty();

          // メインカテゴリーを選えらび直すと → 以下のリストリセット
          var option = $("<option>").text("選択してください").attr("value", "");

          $("#ingredient-dropdown").empty();
          $("#ingredient-dropdown").append(option);

          $(function () {
            $("#qtyDirect").hide(); // Input Hidden
            $("#qty-dropdown").change(function () {
              if ($("#qty-dropdown").val() == "direct") {
                $("#qtyDirect").show(); // Input Display
              } else {
                $("#qtyDirect").hide(); // Input Hidden
              }
            });
            $("#qty-dropdown option:first").prop("selected", true);
          });

          $("#unit-dropdown option:first").prop("selected", true);

          // 詳細カテゴリーをリストに入れる
          $.each(response, function (index, subcategory) {

            // 詳細カテゴリーがメインカテゴリーと一緒
            if (subcategory.level == 0 && index == 0) {
              var option = $("<option>").text("-").attr("value", subcategory.id);
              $("#sub-category-dropdown").append(option);
            }

            // 「選択してください」を入れる
            if (subcategory.level == 1 && index == 0) {
              var option = $("<option>").text("選択してください").attr("value", subcategory.id);
              $("#sub-category-dropdown").append(option);
            }

            // 詳細カテゴリーを出力
            if (subcategory.level == 1) {
              var option = $("<option>").text(subcategory.name).attr("value", subcategory.id);
              $("#sub-category-dropdown").append(option);
            }
          });
        },
        error: function (xhr, textStatus, errorThrown) {
          console.log("Error: " + errorThrown);
        }
      });
    });
  });

  // 材料
  $(document).ready(function () {
    $("#sub-category-dropdown").change(function () {
      var categoryId = $(this).val();
      $.ajax({
        type: "POST",
        url: "/ingredient/ing",
        data: { categoryId: categoryId },
        dataType: "json",
        success: function (response) {

          // リセット
          var option = $("<option>").text("選択してください").attr("value", "");
          $("#ingredient-dropdown").empty();
          $("#ingredient-dropdown").append(option);

          // 詳細カテゴリーを選えらび直すと → 以下のリストリセット
          $(function () {
            $("#qtyDirect").hide(); // Input Hidden
            $("#qty-dropdown").change(function () {
              if ($("#qty-dropdown").val() == "direct") {
                $("#qtyDirect").show(); // Input Display
              } else {
                $("#qtyDirect").hide(); // Input Hidden
              }
            });
            $("#qty-dropdown option:first").prop("selected", true);
          });

          $("#unit-dropdown option:first").prop("selected", true);

          // 材料をリストに入れる
          $.each(response, function (index, ingredient) {

            // 「選択してください」を入れる
            if (index == 0) {
              var option = $("<option>").text(ingredient.name).attr("value", ingredient.id);
              $("#ingredient-dropdown").append(option);
            } else { // 材料を出力
              var option = $("<option>").text(ingredient.name).attr("value", ingredient.id);
              $("#ingredient-dropdown").append(option);
            }
          });
        },
        error: function (xhr, textStatus, errorThrown) {
          console.log("Error: " + errorThrown);
        }
      });
    });
  });

  // 材料を選えらび直すと → 以下のリストリセット
  $(document).ready(function () {
    $("#ingredient-dropdown").change(function () {
      $(function () {
        $("#qtyDirect").hide(); // Input Hidden
        $("#qty-dropdown").change(function () {
          if ($("#qty-dropdown").val() == "direct") {
            $("#qtyDirect").show(); // Input Display
          } else {
            $("#qtyDirect").hide(); // Input Hidden
          }
        });
        $("#qty-dropdown option:first").prop("selected", true);
      });

      $("#unit-dropdown option:first").prop("selected", true);

    });
  });

  // Dynamic Table 材料
  let selectedItems = [];
  let savedList = [];
  let saveLength;


  document.querySelector("#add-button").addEventListener("click", function () {
    // 出力用
    let mainCategory = document.querySelector("#main-category-dropdown option:checked").textContent;
    let subCategory = document.querySelector("#sub-category-dropdown option:checked").textContent;
    let ingredient = document.querySelector("#ingredient-dropdown option:checked").textContent;
    let qty;
    if (document.querySelector("#qty-dropdown").value === "direct") {
      qty = document.querySelector("#qtyDirect").value;
    } else {
      qty = document.querySelector("#qty-dropdown option:checked").textContent;
    }
    let unit = document.querySelector("#unit-dropdown option:checked").textContent;

    // データ送信用
    let ingredientValue = document.querySelector("#ingredient-dropdown").value; // ingredient.id
    let qtyValue = document.querySelector("#qty-dropdown").value; // quantity
    let unitValue = document.querySelector("#unit-dropdown").value; // unit.id

    // Object: selectedItemsに保存
    selectedItems.push({
      mainCategory: mainCategory,
      subCategory: subCategory,
      ingredient: ingredient,
      qty: qty,
      unit: unit,
      ingredientValue: ingredientValue,
      qtyValue: qty,
      unitValue: unitValue
    });

    $("#main-category-dropdown option:first").prop("selected", true);
    //$("#main-category-dropdown").empty();
    $("#sub-category-dropdown").empty();
    $("#ingredient-dropdown").empty();
    $("#unit-dropdown option:first").prop("selected", true);

    //$("#main-category-dropdown").append($("<option>").text("選択してください").attr("value", ""));
    $("#sub-category-dropdown").append($("<option>").text("選択してください").attr("value", ""));
    $("#ingredient-dropdown").append($("<option>").text("選択してください").attr("value", ""));
    $("#unit-dropdown").append($("<option>").text("選択してください").attr("value", ""));

    $(function () {
      $("#qtyDirect").hide(); // Input Hidden
      $("#qty-dropdown").change(function () {
        if ($("#qty-dropdown").val() == "direct") {
          $("#qtyDirect").show(); // Input Display
        } else {
          $("#qtyDirect").hide(); // Input Hidden
        }
      });
      $("#qty-dropdown option:first").prop("selected", true);
    });

    // 出力
    let tableBody = document.querySelector("#selected-items");
    tableBody.innerHTML = "";

    // if savedList is already exist
    if (savedList.length > 0) {

      saveLength = savedList.length;
      console.log('saveLength: ' + saveLength);

      for (let i = 0; i < savedList.length; i++) {
        let row = document.createElement("tr");
        row.className = "draggable";
        row.draggable = true;

        let cell0 = document.createElement("div");
        cell0.className = "el";

        let cell1 = document.createElement("td");
        cell1.className = "table-ing-name"; //class 속성 추가
        cell1.textContent = savedList[i].ingredient;
        cell1.value = savedList[i].ingredientValue;

        let cell2 = document.createElement("td");
        cell2.className = "table-ing-qty";
        cell2.textContent = savedList[i].qty;
        cell2.value = savedList[i].qtyValue;

        let cell3 = document.createElement("td");
        cell3.className = "table-ing-unit";
        cell3.textContent = savedList[i].unit;
        cell3.value = savedList[i].unitValue;

        let cell4 = document.createElement("td");
        cell4.className = "table-ing-unit";
        let button = document.createElement("button");
        button.type = "button";
        button.className = "btn-close";
        button.id = "deleteBtn";
        button.dataset.tableIngName = cell1.value;
        cell4.appendChild(button);

        cell0.appendChild(cell1);
        cell0.appendChild(cell2);
        cell0.appendChild(cell3);
        cell0.appendChild(cell4);

        row.appendChild(cell0);
        row.appendChild(cell1);
        row.appendChild(cell2);
        row.appendChild(cell3);
        row.appendChild(cell4);

        tableBody.appendChild(row);
      }
    }



    for (let i = 0; i < selectedItems.length; i++) {
      let row = document.createElement("tr");
      row.className = "draggable";
      row.draggable = true;

      let cell0 = document.createElement("div");
      cell0.className = "el";

      let cell1 = document.createElement("td");
      cell1.className = "table-ing-name"; //class 속성 추가
      cell1.textContent = selectedItems[i].ingredient + ' (' + selectedItems[i].subCategory + ', ' + selectedItems[i].mainCategory + ')';
      cell1.value = selectedItems[i].ingredientValue;

      let cell2 = document.createElement("td");
      cell2.className = "table-ing-qty";
      cell2.textContent = selectedItems[i].qty;
      cell2.value = selectedItems[i].qtyValue;

      let cell3 = document.createElement("td");
      cell3.className = "table-ing-unit";
      cell3.textContent = selectedItems[i].unit;
      cell3.value = selectedItems[i].unitValue;

      let cell4 = document.createElement("td");
      cell4.className = "table-ing-unit";
      let button = document.createElement("button");
      button.type = "button";
      button.className = "btn-close";
      button.id = "deleteBtn";
      button.dataset.tableIngName = cell1.value;
      cell4.appendChild(button);

      cell0.appendChild(cell1);
      cell0.appendChild(cell2);
      cell0.appendChild(cell3);
      cell0.appendChild(cell4);

      row.appendChild(cell0);
      row.appendChild(cell1);
      row.appendChild(cell2);
      row.appendChild(cell3);
      row.appendChild(cell4);

      tableBody.appendChild(row);
    }



  });

  // Exit Button
  selectedItems

  // データ保存
  const saveButton = document.getElementById("save-button");
  const tableBody = document.getElementById("selected-items");
  console.log('tableBody.rows.length: ' + tableBody.rows.length);

  saveButton.addEventListener("click", () => {



    // データがあれば
    if (tableBody.rows.length > 0) {
      savedIngList(); // 保存
      //localStorage.setItem("savedList", JSON.stringify(savedList));
    } else {
      console.log('保存するデータがありません');
      // TODO: text append 保存する材料がありません
    }

  });

  function savedIngList() {

    console.log('selectedItems: ' + selectedItems.length);



    if (saveLength > 0) {

      for (let i = saveLength; i < tableBody.rows.length; i++) {

        // TableBodyのi番目のcells
        const cells = tableBody.rows[i].cells;

        // RecipeIngredientに必要なデータ
        let ingredient;
        let qty;
        let unit;
        let ingredientValue; // ingredient.id
        let qtyValue; // quantity
        let unitValue; // unit.id

        // 各cellsのj番目の要素
        for (let j = 0; j < cells.length; j++) {
          switch (j) {
            case 0:
              ingredient = cells[j].textContent;
              ingredientValue = cells[j].value;
              break;
            case 1:
              qty = cells[j].textContent;
              qtyValue = cells[j].value;
              break;
            case 2:
              unit = cells[j].textContent;
              unitValue = cells[j].value;
              break;
          }
        }

        // Object: savedListに保存
        savedList.push({
          ingredient: ingredient,
          unit: unit,
          qty: qty,
          ingredientId: ingredientValue,
          qtyValue: qtyValue,
          unitId: unitValue
        });

      }
    } else {
      for (let i = 0; i < tableBody.rows.length; i++) {

        // TableBodyのi番目のcells
        const cells = tableBody.rows[i].cells;

        // RecipeIngredientに必要なデータ
        let ingredient;
        let qty;
        let unit;
        let ingredientValue; // ingredient.id
        let qtyValue; // quantity
        let unitValue; // unit.id

        // 各cellsのj番目の要素
        for (let j = 0; j < cells.length; j++) {
          switch (j) {
            case 0:
              ingredient = cells[j].textContent;
              ingredientValue = cells[j].value;
              break;
            case 1:
              qty = cells[j].textContent;
              qtyValue = cells[j].value;
              break;
            case 2:
              unit = cells[j].textContent;
              unitValue = cells[j].value;
              break;
          }
        }

        // Object: savedListに保存
        savedList.push({
          ingredient: ingredient,
          unit: unit,
          qty: qty,
          ingredientId: ingredientValue,
          qtyValue: qtyValue,
          unitId: unitValue
        });

      }
    }
  }

  // 削除
  document.addEventListener("DOMContentLoaded", function () {
    tableBody.addEventListener("click", (event) => {
      const target = event.target;
      const id = event.target.dataset.tableIngName;
      console.log('id 1: ' + id);
      console.log('selectedItems.indexOf(id) ' + selectedItems.indexOf(id));
      savedList.splice(savedList.indexOf(id), 1);

    });
  });


  // writeFormSubmitの前
  document.getElementById('writeForm').addEventListener('submit', function (evt) {
    evt.preventDefault();
    // savedListをJSON.stringにして、input hiddenに入れる
    $('#send-list-input').val(JSON.stringify(savedList));
    this.submit();
  })

  // defaultイメージ設定
  function setDefaultImage(img) {
    img.src = '/assets/img/default.png';
  }





  var input = document.getElementById("input");
  var initLabel = document.getElementById("label");

  input.addEventListener("change", (event) => {
    const files = changeEvent(event);
    handleUpdate(files);
  });

  initLabel.addEventListener("mouseover", (event) => {
    event.preventDefault();
    const label = document.getElementById("label");
    label?.classList.add("label--hover");
  });

  initLabel.addEventListener("mouseout", (event) => {
    event.preventDefault();
    const label = document.getElementById("label");
    label?.classList.remove("label--hover");
  });

  initLabel.addEventListener("dragenter", (event) => {
    event.preventDefault();
    console.log("dragenter");
    const label = document.getElementById("label");
    label?.classList.add("label--hover");
  });

  initLabel.addEventListener("dragover", (event) => {
    console.log("dragover");
    event.preventDefault();
  });

  initLabel.addEventListener("dragleave", (event) => {
    event.preventDefault();
    console.log("dragleave");
    const label = document.getElementById("label");
    label?.classList.remove("label--hover");
  });

  initLabel.addEventListener("drop", (event) => {
    event.preventDefault();
    console.log("drop");
    const label = document.getElementById("label");
    label?.classList.remove("label--hover");
    const files = event.dataTransfer?.files;
    handleUpdate([...files]);
  });

  function changeEvent(event) {
    const { target } = event;
    return [...target.files];
  };

  function handleUpdate(fileList) {
    const preview = document.getElementById("preview");

    const file = fileList[0];
    const reader = new FileReader();
    reader.addEventListener("load", (event) => {
      const img = el("img", {
        className: "embed-img",
        src: event.target?.result,
      });
      const imgContainer = el("div", { className: "container-img" }, img);
      preview.innerHTML = ""; // clear any existing preview
      preview.append(imgContainer);
    });
    reader.readAsDataURL(file);
  };

  function el(nodeName, attributes, ...children) {
    const node =
      nodeName === "fragment"
        ? document.createDocumentFragment()
        : document.createElement(nodeName);

    Object.entries(attributes).forEach(([key, value]) => {
      if (key === "events") {
        Object.entries(value).forEach(([type, listener]) => {
          node.addEventListener(type, listener);
        });
      } else if (key in node) {
        try {
          node[key] = value;
        } catch (err) {
          node.setAttribute(key, value);
        }
      } else {
        node.setAttribute(key, value);
      }
    });

    children.forEach((childNode) => {
      if (typeof childNode === "string") {
        node.appendChild(document.createTextNode(childNode));
      } else {
        node.appendChild(childNode);
      }
    });

    return node;
  }
