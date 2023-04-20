let selectedItems = [];
let savedList = [];
let commonList = [];
let tableBody;
localStorage.clear();



// Modal Click
$(document).ready(function () {

    document.querySelector(".modal-ing").addEventListener("click", function () {

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
        resetMain()
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
    var option = $("<option>").text("選択してください").attr("value", "");
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
    var option = $("<option>").text("選択してください").attr("value", "");
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
                        var option = $("<option>").text("-").attr("value", subcategory.id);
                        $("#sub-category-dropdown").append(option);
                        $("#sub-category-dropdown").attr("disabled", true);
                        dropdownSub(subcategory.id);
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
        dropdownSub(categoryId);
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
                console.log("response.length " + response.length);
                if (response.length === 1) {
                    console.log("response.length! " + response.length);
                    var option = $("<option>").text(ingredient.name).attr("value", ingredient.id);
                    $("#ingredient-dropdown").append(option);
                     $("#ingredient-dropdown").attr("disabled", true);
                  } else {
                    // 「選択してください」を入れる
                    console.log("response.length === 1 !!");
                      if (index == 0) {
                          var option = $("<option>").text(ingredient.name).attr("value", ingredient.id);
                          $("#ingredient-dropdown").append(option);
                      } else { // 材料を出力
                          var option = $("<option>").text(ingredient.name).attr("value", ingredient.id);
                          $("#ingredient-dropdown").append(option);
                      }
                  }
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
    });
});

savedList = JSON.parse(localStorage.getItem('savedList')) || [];

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

// Exit Button
const closeButton = document.getElementById("close-ingredient-modal");

closeButton.addEventListener("click", () => {
    closeModal();
});


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
        let listItem = document.createElement("div");
        listItem.className = "displayed-item-list";

        let ingredient = document.createElement("div");
        if (commonList[i].mainCategory) {
            ingredient.textContent = commonList[i].ingredient + ' (' + commonList[i].subCategory + ', ' + commonList[i].mainCategory + ')';
        } else {
            ingredient.textContent = commonList[i].ingredient;
        }
        ingredient.className = "displayed-item-ingredient";
        listItem.appendChild(ingredient);

        let listItem2 = document.createElement("div");
        listItem2.className = "displayed-item-list2";

        let quantity = document.createElement("div");
        quantity.textContent = commonList[i].qty;
        quantity.className = "displayed-item-qty";
        listItem2.appendChild(quantity);

        let unit = document.createElement("div");
        unit.textContent = commonList[i].unit;
        unit.className = "displayed-item-unit";
        listItem2.appendChild(unit);

        listElement.appendChild(listItem);
        listElement.appendChild(listItem2);
    }
}



// メインカテゴリーをリセット
function resetMain() {
    $("#main-category-dropdown option:first").prop("selected", true);
}

// 詳細カテゴリーをリセット
function resetSub() {
    $("#sub-category-dropdown").empty();
    $("#sub-category-dropdown").append($("<option>").text("選択してください").attr("value", ""));
}

// 材料をリストをリセット
function resetIng() {
    $("#ingredient-dropdown").empty();
    $("#ingredient-dropdown").append($("<option>").text("選択してください").attr("value", ""));
}

// 容量をリストをリセット
function resetQty() {
    $("#qtyDirect").hide(); // Input Hidden
    $("#qty-dropdown").change(function () {
        if ($("#qty-dropdown").val() == "direct") {
            $("#qtyDirect").show(); // Input Display
        } else {
            $("#qtyDirect").hide(); // Input Hidden
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

/*<![CDATA[*/
var imgUrl = /*[[${ist.imgUrl}]]*/ null;
var imgPreview = document.getElementById("img-preview");
var imgFile = document.getElementById("img-file");

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
/*]]>*/




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
