let selectedItems = [];
let savedList = [];
let commonList = [];
let tableBody;
localStorage.clear();



// Modal Click
$(document).ready(function () {

    document.querySelector(".modal-ing").addEventListener("click", function () {

    removeAlert("main");
    removeAlert("sub");
    removeAlert("ingredient");
    removeAlert("qty");
    removeAlert("unit");

        // Modal Open
        var modal = document.querySelector('#staticBackdrop');
        $(modal).modal('show');

        // 1. データ: mainCategory (DB) 出力
        var mainCategoryData = localStorage.getItem('mainCategoryData');
        if (!mainCategoryData) {
            $.ajax({
                type: "GET",
                url: "/ingredient/main",
                dataType: "json",
                success: function (response) {
                    // localStorageにデータを保存
                    localStorage.setItem('mainCategoryData', JSON.stringify(response));
                    // main-category-dropdownを呼び出す
                    populateMainCategoryDropdown(response);
                },
                error: function (xhr, textStatus, errorThrown) {
                    console.log("Error: " + errorThrown);
                }
            });
        }

        // 2. データ: Unit (DB) 出力
        var unitData = localStorage.getItem('unitData');
        if (!unitData) {
            $.ajax({
                type: "GET",
                url: "/ingredient/unit",
                dataType: "json",
                success: function (response) {
                    // localStorageにデータを保存
                    localStorage.setItem('unitData', JSON.stringify(response));
                    // unit-dropdownを呼び出す
                    populateUnitDropdown(response);
                },
                error: function (xhr, textStatus, errorThrown) {
                    console.log("Error: " + errorThrown);
                }
            });
        }

        // リセット
        resetMain();
        resetSub();
        resetIng();
        resetUnit();
        resetQty();

        // 3. データ: savedList (Object) 出力
        var savedListData = localStorage.getItem('savedList');
        savedList = savedListData ? JSON.parse(savedListData) : [];

        tableBody = document.querySelector("#selected-items");
        tableBody.innerHTML = "";
        paintTableOnModal(savedList, tableBody);

    });
    console.log('localStorage: ' + localStorage.length);
}); // Modal Open

function populateMainCategoryDropdown(response) {
    // リセット
    $("#main-category-dropdown").empty();

    // 「選択してください」を入れる
    var option = $("<option>").text("選択してください").attr("value", "").attr("disabled", true).attr("selected", true).attr("hidden", true);
    $("#main-category-dropdown").append(option);
    // メインをリストに入れる
    $.each(response, function (index, main) {
        var option = $("<option>").text(main.name).attr("value", main.id);
        $("#main-category-dropdown").append(option);
    });
}

// Function to populate the unit dropdown
function populateUnitDropdown(response) {
    // リセット
    $("#unit-dropdown").empty();

    // 「選択してください」を入れる
    var option = $("<option>").text("選択してください").attr("value", "").attr("disabled", true).attr("selected", true).attr("hidden", true);
    $("#unit-dropdown").append(option);

    // Unitを入れる
    $.each(response, function (index, unit) {
        var option = $("<option>").text(unit.name).attr("value", unit.id);
        $("#unit-dropdown").append(option);
    });
}


// 詳細カテゴリー
$(document).ready(function () {
    $("#main-category-dropdown").change(function () {

        removeAlert("main");

        var ingredientCategoryId = $(this).val();
        $.ajax({
            type: "POST",
            url: "/ingredient/sub",
            data: { ingredientCategoryId: ingredientCategoryId },
            dataType: "json",
            success: function (response) {

                // リセット
                $("#sub-category-dropdown").empty();
                $("#sub-category-dropdown").removeAttr("disabled");


                // メインカテゴリーを選えらび直すと → 以下のリストリセット
                resetIng();
                resetQty();
                resetUnit();

                // 詳細カテゴリーをリストに入れる
                $.each(response, function (index, subcategory) {

                    // 詳細カテゴリーがメインカテゴリーと一緒
                    if (subcategory.level == 0 && index == 0) {
                        var option = $("<option>").text("-").attr("value", "-");
                        $("#sub-category-dropdown").append(option);
                        $("#sub-category-dropdown").attr("disabled", true);
console.log("subcategory.level == 0 && index == 0 subcategory.id: " + subcategory.id);
//                        dropdownSub(subcategory.id);

                    }

                    // 「選択してください」を入れる
                    if (subcategory.level == 1 && index == 0) {
                        var option = $("<option>").text("選択してください").attr("value", "").attr("disabled", true).attr("selected", true).attr("hidden", true);
                        $("#sub-category-dropdown").append(option);
                    }

                    // 詳細カテゴリーを出力
                    if (subcategory.level == 1) {
                        var option = $("<option>").text(subcategory.name).attr("value", "");
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
        var valuee = document.querySelector("#main-category-dropdown").value;
        console.log("valuee: " + valuee);

        //dropdownSub(categoryId);

    });
});

function dropdownSub(categoryId) {
    $.ajax({
        type: "POST",
        url: "/ingredient/ing",
        data: { categoryId: categoryId },
        dataType: "json",
        success: function (response) {

            // リセット
            resetIng();

            // 詳細カテゴリーを選えらび直すと → 以下のリストリセット
            resetQty();
            resetUnit();

            // 材料をリストに入れる
            $.each(response, function (index, ingredient) {
                if (response.length === 1) {
                    var option = $("<option>").text(ingredient.name).attr("value", "-");
                    $("#ingredient-dropdown").append(option);
                     $("#ingredient-dropdown").attr("disabled", true);
                  } else {
                    // 「選択してください」を入れる
                      if (index == 0) {
                          var option = $("<option>").text(ingredient.name).attr("value", ingredient.id);
                          $("#ingredient-dropdown").append(option);
                      } else { // 材料を出力
                          var option = $("<option>").text(ingredient.name).attr("value", ingredient.id);
                          $("#ingredient-dropdown").append(option);
                      }
                  }

                  removeAlert("sub");
            });
        },
        error: function (xhr, textStatus, errorThrown) {
            console.log("Error: " + errorThrown);
        }
    });
}

// 材料を選えらび直すと → 以下のリストリセット
$(document).ready(function () {
    $("#ingredient-dropdown").change(function () {
        resetQty();
        resetUnit();
        removeAlert("ingredient");
    });
});

$(document).ready(function () {
    $("#qty-dropdown").change(function () {
        removeAlert("qty");
    });
});

$(document).ready(function () {
    $("#unit-dropdown").change(function () {
        removeAlert("unit");
    });
});


savedList = JSON.parse(localStorage.getItem('savedList')) || [];

document.querySelector("#reset-button").addEventListener("click", function () {
    resetMain()
    resetSub();
    resetIng();
    resetUnit();
    resetQty();
});


// Dynamic Table 材料
document.querySelector("#add-button").addEventListener("click", function () {

    // 出力用
    let mainCategory = document.querySelector("#main-category-dropdown option:checked").textContent;
    let subCategory = document.querySelector("#sub-category-dropdown option:checked").textContent;
    $("#sub-category-dropdown").removeAttr("disabled");
    let ingredient = document.querySelector("#ingredient-dropdown option:checked").textContent;
    $("#ingredient-dropdown").removeAttr("disabled");
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

    let mainCategoryValue = document.querySelector("#main-category-dropdown").value;
    let subCategoryValue = document.querySelector("#sub-category-dropdown").value;


    let mainAlert = document.querySelector(".mainAlert");
    let subAlert = document.querySelector(".subAlert");
    let ingredientAlert = document.querySelector(".ingredientAlert");
    let qtyAlert = document.querySelector(".qtyAlert");
    let unitAlert = document.querySelector(".unitAlert");

    if (mainCategoryValue === "") {
      if (!mainAlert.querySelector("label")) {
        let mainLabel = document.createElement("label");
        mainLabel.innerHTML = "メインカテゴリーを選択してください";
        mainLabel.style.color = "red";
        mainAlert.appendChild(mainLabel);
      }
    }

    if (subCategoryValue === "") {
        if (!subAlert.querySelector("label")) {
        let subLabel = document.createElement("label");
        subLabel.innerHTML = "詳細カテゴリーを選択してください";
        subLabel.style.color = "red";
        document.querySelector(".subAlert").appendChild(subLabel);
        }
    }


    if (ingredientValue === "") {
        if (!ingredientAlert.querySelector("label")) {
            let ingredientLabel = document.createElement("label");
            ingredientLabel.innerHTML = "材料を選択してください";
            ingredientLabel.style.color = "red";
            document.querySelector(".ingredientAlert").appendChild(ingredientLabel);
        }
    }

    if (qtyValue === "") {
        if (!qtyAlert.querySelector("label")) {
            let qtyLabel = document.createElement("label");
            qtyLabel.innerHTML = "計量を選択してください";
            qtyLabel.style.color = "red";
            document.querySelector(".qtyAlert").appendChild(qtyLabel);
        }
    }

    if (unitValue === "") {
        if (!unitAlert.querySelector("label")) {
            let unitLabel = document.createElement("label");
            unitLabel.innerHTML = "単位を選択してください";
            unitLabel.style.color = "red";
            document.querySelector(".unitAlert").appendChild(unitLabel);
        }
        return;
    }

    // Object: selectedItemsに保存
    selectedItems.push({
        mainCategory: mainCategory,
        subCategory: subCategory,
        ingredient: ingredient,
        qty: qty,
        unit: unit,
        ingredientValue: ingredientValue,
        qtyValue: qtyValue,
        unitValue: unitValue
    });

    resetMain();
    resetSub();
    resetIng();
    resetQty();
    resetUnit();

    removeAlert("main");
    removeAlert("sub");
    removeAlert("ingredient");
    removeAlert("qty");
    removeAlert("unit");

    // 出力
    tableBody = document.querySelector("#selected-items");
    tableBody.innerHTML = "";

    // if savedList is already exist
    var savedListData = localStorage.getItem('savedList');
    savedList = savedListData ? JSON.parse(savedListData) : [];
    if (savedList.length > 0) {
        paintTableOnModal(savedList, tableBody);
    }
    paintTableOnModal(selectedItems, tableBody);

});

function removeAlert(className) {
  let alertElement = document.querySelector("." + className + "Alert");

  if (alertElement.querySelector("label")) {
    alertElement.removeChild(alertElement.querySelector("label"));
  }
}


// データ保存
const saveButton = document.getElementById("save-button");
tableBody = document.getElementById("selected-items");
console.log('tableBody.rows.length: ' + tableBody.rows.length);

saveButton.addEventListener("click", () => {

    savedIngList();

    // Modal Close
    closeModal();

    getDataFromLocalStorageAndDisplay();
});

// Modal Close
function closeModal() {

    var modal = document.querySelector('#staticBackdrop');
    $(modal).modal('hide');

    $(modal).on('hidden.bs.modal', function () {
        $('body').css('padding-right', '0px');
    });

    $('html, body').animate({
        scrollTop: $('#ing-tag').offset().top
    }, 150, 'swing', function () {
        requestAnimationFrame(function () {
            $('html, body').stop();
        });
    });

    selectedItems = [];
   const table = document.getElementById("riListTable");
   if (table) {
       if (table.style.display === "none") {
           table.style.display = "table-row-group";
       } else {
           table.style.display = "none";
       }
       resetQty();
   }

}


// 材料 保存
function savedIngList() {

    // 保存の前、初期化
    localStorage.removeItem("savedList");
    savedList = [];
    selectedItems = [];

    if (tableBody.rows.length != 0) {
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

    localStorage.setItem("savedList", JSON.stringify(savedList));
    console.log("local modified: " + localStorage.getItem("savedList"));

    // selectedItems リセット
    selectedItems = [];
}

// Dynamic材料テーブルをModalに出力
function paintTableOnModal(commonList, tableBody) {
    console.log('commonList.length: ' + commonList.length);
    for (let i = 0; i < commonList.length; i++) {
        let row = document.createElement("tr");
        row.className = "draggable";
        // row.draggable = true;

        let cell0 = document.createElement("div");
        cell0.className = "el";

        let cell1 = document.createElement("td");
        cell1.className = "table-ing-name";
        // mainCategoryデータがあれば
        if (commonList[i].mainCategory) {
            // subCategoryデータがなければ 
            if (commonList[i].subCategory == "-") {
                cell1.textContent = commonList[i].ingredient + ' (' + commonList[i].mainCategory + ')';
            } else { // subCategoryデータがあれば
                cell1.textContent = commonList[i].ingredient + ' (' + commonList[i].subCategory + ', ' + commonList[i].mainCategory + ')';
            }
            // mainCategoryデータがなければ
        } else {
            cell1.textContent = commonList[i].ingredient;
        }
        cell1.value = commonList[i].ingredientValue;

        let cell2 = document.createElement("td");
        cell2.className = "table-ing-qty";
        cell2.textContent = commonList[i].qty;
        cell2.value = commonList[i].qtyValue;

        let cell3 = document.createElement("td");
        cell3.className = "table-ing-unit";
        cell3.textContent = commonList[i].unit;
        cell3.value = commonList[i].unitValue;

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

function paintListOnMain(commonList, listElement) {
    for (let i = 0; i < commonList.length; i++) {
        let listItem = document.createElement("tr");
        listItem.className = "";

        let orderNum = document.createElement("th");
        orderNum.textContent = i + 1;
        listItem.appendChild(orderNum);

        let ingredient = document.createElement("td");
        if (commonList[i].mainCategory) {
            ingredient.textContent = commonList[i].ingredient + ' (' + commonList[i].subCategory + ', ' + commonList[i].mainCategory + ')';
        } else {
            ingredient.textContent = commonList[i].ingredient;
        }
        ingredient.className = "";
        listItem.appendChild(ingredient);

        let quantity = document.createElement("td");
        quantity.textContent = commonList[i].qty;
        quantity.className = "";
        listItem.appendChild(quantity);

        let unit = document.createElement("td");
        unit.textContent = commonList[i].unit;
        unit.className = "";
        listItem.appendChild(unit);

        listElement.appendChild(listItem);

    }
}



// メインカテゴリーをリセット
function resetMain() {
    $("#main-category-dropdown option:first").prop("selected", true);
}

// 詳細カテゴリーをリセット
function resetSub() {
    $("#sub-category-dropdown").empty();
    $("#sub-category-dropdown").append($("<option>").text("選択してください").attr("value", "").attr("disabled", true).attr("selected", true).attr("hidden", true));
}

// 材料をリストをリセット
function resetIng() {
    $("#ingredient-dropdown").empty();
    $("#ingredient-dropdown").append($("<option>").text("選択してください").attr("value", "").attr("disabled", true).attr("selected", true).attr("hidden", true));
}

// 容量をリストをリセット
function resetQty() {
    $("#qtyDirect").hide(); // Input Hidden
    $("#qty-dropdown").change(function () {
        if ($("#qty-dropdown").val() == "direct") {
            $("#qtyDirect").show().val(""); // Input Display and clear the value
        } else {
            $("#qtyDirect").hide().val(""); // Input Hidden and clear the value
        }
    });
    $("#qty-dropdown option:first").prop("selected", true);
}


// 単位をリストをリセット
function resetUnit() {
    $("#unit-dropdown option:first").prop("selected", true);
}

// 材料を削除
document.addEventListener('click', function (event) {
    if (event.target && event.target.id === 'deleteBtn') {
        console.log("deleteBtn Clicked");
        let row = event.target.closest('tr');
        let tableIngName = event.target.dataset.tableIngName;
        row.parentNode.removeChild(row);
        for (let i = 0; i < selectedItems.length; i++) {
            if (selectedItems[i].ingredientValue === tableIngName) {
                selectedItems.splice(i, 1);
                break;
            }
        }
        for (let i = 0; i < savedList.length; i++) {
            console.log('ingredientId: ' + savedList[i].ingredientId);
            console.log('tableIngName: ' + savedList[i].tableIngName);
            if (savedList[i].ingredientId === savedList[i].tableIngName) {
                savedList.splice(i, 1);
                break;
            }
        }
    }

});

function getDataFromLocalStorageAndDisplay() {
    var savedListData = localStorage.getItem('savedList');
    savedList = savedListData ? JSON.parse(savedListData) : [];
    listElement = document.querySelector(".display-items");
    listElement.innerHTML = "";
    if (savedList) {
        paintListOnMain(savedList, listElement);
    }
}


var imgUrl = /*[[${ist.imgUrl}]]*/ null;
var imgPreview = document.getElementById("img-preview");
var imgFile = document.getElementById("img-file");

if (imgFile) {
    if (imgUrl != null) {
        imgPreview.src = imgUrl;
    }

    imgFile.addEventListener("change", function() {
        var file = imgFile.files[0];
        var reader = new FileReader();

        reader.addEventListener("load", function() {
            imgPreview.src = reader.result;
        }, false);

        if (file) {
            reader.readAsDataURL(file);
        }
    });
}



//$(document).ready(function() {
//// 隠しフィールドからレシピ名を取得する
//  const recipeId = document.getElementById('recipeId').value;
//  console.log('recipeId:' + recipeId);
//
//    if (recipeId) {
//      // サーバーからriListデータを取得する
//      $.ajax({
//        url: '/getRiList',
//        type: 'GET',
//        data: {
//          recipeId: recipeId
//        },
//        success: function(recipeId) {
//          // 取得したriListデータを変数riListに代入する
//          var riList = response;
//          console.log(riList);
//
//          // riListデータをlocalStorageに保存する処理を続ける
//          if (riList != null && !riList.isEmpty()) {
//            console.log('riListは空ではありません。');
//
//            // localStorageに既にデータがあるかどうかを確認する
//            if (localStorage.getItem('savedList') !== null) {
//              console.log('localStorageに既にsavedListのデータがあります。既存のデータを削除します.');
//              // 既存のデータを削除する
//              localStorage.removeItem('savedList');
//            }
//
//            // riListをJSON文字列に変換する
//            const riListJson = JSON.stringify(riList);
//
//            // riListをlocalStorageに保存する
//            localStorage.setItem('savedList', riListJson);
//            console.log('riListのデータがlocalStorageに保存されました。');
//
//            // localStorageからリストを表示する関数を呼び出す
//            getDataFromLocalStorageAndDisplay();
//          } else {
//            console.log('riListは空またはnullです。localStorageに保存しません。');
//          }
//        },
//        error: function(xhr, status, error) {
//          console.log('recipeIngredientの取得中にエラーが発生しました: ' + error);
//        }
//      });
//    }
//});









// writeFormSubmitの前
document.getElementById('writeForm').addEventListener('submit', function (evt) {
    evt.preventDefault();

    var recipeName = document.getElementById("floatingInput").value;
    if(recipeName == ""){
        alert("レシピ名を入力してください。")
        return false;
    }
    var instructions = document.getElementById("instructions").value;
    if(instructions == ""){
        alert("作り方を入力してください。")
        return false;
    }

    var videoUrl = document.getElementById("video-url").value;
    console.log(videoUrl);
    var pattern = /^(https?\:\/\/)?(www\.youtube\.com|youtu\.?be)\/.+$/;
    if(videoUrl != ""){
        if(!pattern.test(videoUrl)){
                alert("正しいURLを入力してください。")
                return false;
        }
    }
    var savedListData = localStorage.getItem('savedList');
    savedList = savedListData ? JSON.parse(savedListData) : [];

    // savedListをJSON.stringにして、input hiddenに入れる
    $('#send-list-input').val(JSON.stringify(savedList));
    this.submit();
})




























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

function handleUpdate(event) {
  const preview = document.getElementById("preview");
  const dragIcon = document.querySelector(".dragicon");

  const fileList = event.target.files;
  const file = fileList[0];
  const reader = new FileReader();

  reader.addEventListener("load", (event) => {
    const img = document.createElement("img");
    img.className = "embed-img";
    img.src = event.target.result;

    const imgContainer = document.createElement("div");
    imgContainer.className = "container-img";
    imgContainer.appendChild(img);

    preview.innerHTML = ""; // clear any existing preview
    preview.appendChild(imgContainer);
    dragIcon.style.display = "none";
  });

  reader.readAsDataURL(file);
}


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


$(document).ready(function () {
    // セクションの数を追跡する
    var sectionCount = 1;

    $("#add-section-btn").click(function () {
      // セクションの数を増やす
      sectionCount++;

      // 新しいセクションのHTMLを作成する
      var newSection = `
  <div class="row justify-content-center" id="section${sectionCount}">
    <div class="d-flex align-items-center">
      <label for="" class="p-2 text-start section-label">${sectionCount}番目</label>
      <button type="button" class="btn btn-danger delete-section-btn" data-section-id="${sectionCount}">
        削除
      </button>
    </div>
    <div class="col-md-8 p-2">
      <textarea class="form-control" name="description" id="instructions" rows="5"
        placeholder="作り方を入力"></textarea>
    </div>
    <div class="col-md-4">
      <label class="label2" for="input2_${sectionCount}">
        <div class="inner2">
          <img src="/assets/icon/dragndrop.png" class="dragicon2_${sectionCount} img-fluid recipe_image border border-secondary rounded" alt="">
          <div class="preview2" id="preview2_${sectionCount}"></div>
        </div>
      </label>
      <input id="input2_${sectionCount}" class="input2" accept="image/*" type="file" name="imgUrl" required="true"
                          multiple="true" hidden="true" data-section-id="${sectionCount}">
    </div>
  </div>
`;

      // 新しいセクションのHTMLをフォームに追加する
      $("#dynamic-section").append(newSection);

      // すべてのセクションのラベルを更新する
      updateSectionLabels();
    });

    $(document).on("click", ".delete-section-btn", function () {
      var sectionId = $(this).data("section-id");
      // セクションが最初のセクションでないかどうかを確認する
      if (sectionId > 1) {
        // HTMLからセクションを削除する
        $("#section" + sectionId).remove();
        // セクションの数を減らす
        sectionCount--;
        // 残りのすべてのセクションのラベルを更新する
        updateSectionLabels();
      }
    });

    function updateSectionLabels() {
      $(".section-label").each(function (index) {
        $(this).text((index + 1) + "番目");
      });
    }
  });


  $(document).on("change", ".input2", function (e) {
    var input = e.target;
    if (input.files && input.files[0]) {
      var file = input.files[0];
      var reader = new FileReader();
      reader.onload = function (e) {
        var dataURL = e.target.result;
        // Use .closest() method to find the parent section element
        var section = $(input).closest(".row");
        // Get the section number from the data-section-id attribute
        var sectionNum = section.find(".input2").data("section-id");
        // Set the preview image source of the selected section to the data URL
        section.find(`#preview2_${sectionNum}`).html('<img src="' + dataURL + '" class="img-fluid recipe_image border border-secondary rounded">');
        // Hide the dragicon image element of the selected section
        section.find(`.dragicon2_${sectionNum}`).hide();
      };
      reader.readAsDataURL(file);
    }
  });
  

  function handleImageUpload(input) {
    var files = input.files;
    var preview = $(input).siblings(".preview2");
    var sectionNum = $(input).data("section-id");
  
    if (files && files[0]) {
      var reader = new FileReader();
      reader.onload = function(e) {
        var dataURL = e.target.result;
        preview.html('<img src="' + dataURL + '">');
        preview.siblings(`.dragicon2_${sectionNum}`).hide();
      };
      reader.readAsDataURL(files[0]);
    }
  }
  