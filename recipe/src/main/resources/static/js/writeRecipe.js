let selectedItems = [];
let savedList = [];
let commonList = [];
let tableBody;
localStorage.clear();



// Modal Click
$(document).ready(function () {

    document.querySelector(".modal-ing").addEventListener("click", function () {

    resetSavedList();
    removeAlert("main");
    removeAlert("sub");
    removeAlert("ingredient");
    removeAlert("qty");
    removeAlert("unit");

    let ingredientValue = document.querySelector("#ingredient-dropdown").value; // ingredient.id
    let qtyValue = document.querySelector("#qty-dropdown").value; // quantity
    let unitValue = document.querySelector("#unit-dropdown").value; // unit.id

    let subCategoryValue = document.querySelector("#sub-category-dropdown").value;


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

        var selectedItemsData = localStorage.getItem('selectedItems');
        selectedItems = selectedItemsData ? JSON.parse(selectedItemsData) : [];
        if(selectedItems) {
            paintTableOnModal(selectedItems,tableBody);
            return false;
        }

       if(savedList) {
        paintTableOnModal(savedList, tableBody);
        return false;
       }

    });
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
        removeAlert("sub");
        removeAlert("ingredient");
        removeAlert("qty");
        removeAlert("unit");

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
                        dropdownSub(subcategory.id);
                        console.log("dropdownSub run")
                    }

                    // 「選択してください」を入れる
                    if (subcategory.level == 1 && index == 0) {
                        var option = $("<option>").text("選択してください").attr("value", "").attr("disabled", true).attr("selected", true).attr("hidden", true);
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
        var valuee = document.querySelector("#main-category-dropdown").value;

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
            $("#qty-dropdown").removeAttr("disabled");
            resetUnit();

            // 材料をリストに入れる subcategory.level == 0 && index == 0
            $.each(response, function (index, ingredient) {
                if (response.length === 1) {
                    var option = $("<option>").text(ingredient.name).attr("value", ingredient.id);
                    $("#ingredient-dropdown").append(option);
                    $("#ingredient-dropdown option:last-child").prop("selected", true);
//                    $("#ingredient-dropdown").attr("disabled", true);
                  } else {
                    // 「選択してください」を入れる
                      if (index == 0) {
                          var option = $("<option>").text(ingredient.name).attr("value", ingredient.id);
                          $("#ingredient-dropdown").append(option);
                      } else { // 材料を出力
                          var option = $("<option>").text(ingredient.name).attr("value", ingredient.id);
                          $("#ingredient-dropdown").append(option);
                      }
                      $("#ingredient-dropdown").removeAttr("disabled");
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
        $("#qty-dropdown").removeAttr("disabled");
        resetUnit();
        removeAlert("ingredient");
    });
});

$(document).ready(function () {
    $("#qty-dropdown").change(function () {
        $("#unit-dropdown").removeAttr("disabled");
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

    // max check
    resetSavedList();
    var savedListData = localStorage.getItem('savedList');
    savedList = savedListData ? JSON.parse(savedListData) : [];
    console.log('savedList.length: ' + savedList.length);
    if (savedList.length > 9) {
        console.log("savedList.length > 9");
        alert('材料は10個まで追加できます。')
        return false;
    } else {
        console.log("savedList.length <= 9");
    }

    //材料重複チェック
    let ingredientValue = document.querySelector("#ingredient-dropdown").value; // ingredient.id
    let duplicate = savedList.some(item => item.ingredientId === ingredientValue);
    if (duplicate) {
        alert('この材料はすでに追加されています。');
        return false;
    }

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
//    let ingredientValue = document.querySelector("#ingredient-dropdown").value;
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
    }

    if (mainCategoryValue === "" || subCategoryValue === "" || ingredientValue === "" || qtyValue === "" || unitValue === "") {
      return false;
    }

    // Object: selectedItemsに保存
    savedList.push({
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
    console.log('tableBody: '+tableBody);
    tableBody.innerHTML = "";


//    if (savedList.length > 0) {
//        paintTableOnModal(savedList, tableBody);
//    }
//    paintTableOnModal(selectedItems, tableBody);
paintTableOnModal(savedList, tableBody);

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

saveButton.addEventListener("click", () => {

    savedIngList();

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

    // savedList リセット
    resetSavedList();

    // selectedItems リセット
    localStorage.removeItem("selectedItems");
    selectedItems = [];
}

function updateTableLabels() {
    index = 0;
  $(".table-ing-index").each(function (index) {
    $(this).text((index + 1) + ". ");
  });
}


function resetSavedList() {


    // 保存の前、初期化
    localStorage.removeItem("savedList");
    console.log("local removed: " + localStorage.getItem("savedList"));
    savedList = [];
    console.log('table length: ' + tableBody.rows.length);
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
    console.log("local reset: " + localStorage.getItem("savedList"));

    updateTableLabels();
}


// Dynamic材料テーブルをModalに出力
function paintTableOnModal(commonList, tableBody) {
    console.log('paintTableOnModal commonList.length: ' + commonList.length);

    if(commonList.length>0) {
        tableBody = document.querySelector("#selected-items");
        tableBody.innerHTML = "";
    }
    for (let i = 0; i < commonList.length; i++) {
        let row = document.createElement("tr");
        row.className = "draggable";
        // row.draggable = true;

        let cell0 = document.createElement("div");
        cell0.className = "el";

        let cell6 = document.createElement("th");
        cell6.textContent = (i + 1) + ". ";
        cell6.className = "table-ing-index";


        let cell1 = document.createElement("td");
        cell1.className = "table-ing-name";
        // mainCategoryデータがあれば
        if (commonList[i].mainCategory) {
            // subCategoryデータがなければ 
            if (commonList[i].subCategory == "-" || commonList[i].subCategory == commonList[i].mainCategory) {
                if(commonList[i].mainCategory == commonList[i].ingredient) {
                    cell1.textContent = commonList[i].ingredient;
                } else {
                    cell1.textContent = commonList[i].ingredient + ' (' + commonList[i].mainCategory + ')';
                }
            } else {
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

        cell0.appendChild(cell6);
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
        orderNum.textContent = (i + 1) + ".";
        listItem.appendChild(orderNum);

        let ingredient = document.createElement("td");
        if (commonList[i].mainCategory) {
            if(commonList[i].subCategory == commonList[i].mainCategory) {
                if(commonList[i].mainCategory == commonList[i].ingredient) {
                    ingredient.textContent = commonList[i].ingredient;
                } else {
                    ingredient.textContent = commonList[i].ingredient + ' (' + commonList[i].mainCategory + ')';
                }

            } else {
                ingredient.textContent = commonList[i].ingredient + ' (' + commonList[i].subCategory + ', ' + commonList[i].mainCategory + ')';
            }
        } else {
            ingredient.textContent = commonList[i].ingredient;
        }
        ingredient.className = "list-ing-name";
        listItem.appendChild(ingredient);

        let quantity = document.createElement("td");
        quantity.textContent = commonList[i].qty;
        quantity.className = "list-ing-qty";
        listItem.appendChild(quantity);

        let unit = document.createElement("td");
        unit.textContent = commonList[i].unit;
        unit.className = "list-ing-unit";
        listItem.appendChild(unit);

        let blank = document.createElement("td");
        listItem.appendChild(blank);

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
    document.querySelector("#sub-category-dropdown").disabled = true;
}

// 材料をリストをリセット
function resetIng() {
    $("#ingredient-dropdown").empty();
    $("#ingredient-dropdown").append($("<option>").text("選択してください").attr("value", "").attr("disabled", true).attr("selected", true).attr("hidden", true));
    document.querySelector("#ingredient-dropdown").disabled = true;
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
    document.querySelector("#qty-dropdown").disabled = true;
}


// 単位をリストをリセット
function resetUnit() {
    $("#unit-dropdown option:first").prop("selected", true);
    document.querySelector("#unit-dropdown").disabled = true;
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
            console.log('deleteBtn ingredientId: ' + savedList[i].ingredientId);
            console.log('deleteBtn tableIngName: ' + savedList[i].tableIngName);
            if (savedList[i].ingredientId === savedList[i].tableIngName) {
                savedList.splice(i, 1);
                break;
            }
        }
        resetSavedList();
    }



});

function getDataFromLocalStorageAndDisplay() {
    var savedListData = localStorage.getItem('savedList');
    savedList = savedListData ? JSON.parse(savedListData) : [];
    listElement = document.querySelector(".display-items");
    listElement.innerHTML = "";
    if (savedList) {
        paintListOnMain(savedList, listElement);
        var riListTable = document.getElementById("riListTable");
        if(riListTable) {
            riListTable.style.display = "none";
        }
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

$(document).ready(function () {
    var tmp = parseInt($("#test_obj").css('top'));

    $(window).scroll(function () {
        var scrollTop = $(window).scrollTop();
        var obj_position = scrollTop + tmp + "px";

        $("#test_obj").css("top", obj_position);
    }).scroll();
});


function scrollToElementSmooth(id) {
  document.getElementById(id).scrollIntoView({ behavior: 'smooth' });
}

const moveToTopSmooth = () => scrollToElementSmooth('top');
const moveToRecipeTitleSmooth = () => scrollToElementSmooth('recipe-title');
const moveToRecipeImageSmooth = () => scrollToElementSmooth('recipe-image');
const moveToRecipeCategorySmooth = () => scrollToElementSmooth('recipe-category');
const moveToRecipeIngredientSmooth = () => scrollToElementSmooth('recipe-ingredient');
const moveToRecipeInstructionSmooth = () => scrollToElementSmooth('recipe-instruction');
const moveToBottomSmooth = () => scrollToElementSmooth('recipe-youtube');


//var moveToBottomSmooth = function() {
//  window.scrollTo({ top: document.body.scrollHeight, behavior: 'smooth' });
//};

$(document).ready(function() {
  // Handle click event on the first button
  $(".sub-btn").click(function() {
    // Trigger a click event on the submit button
    $(".submit-btn").click();
  });
});


// writeFormSubmitの前
document.getElementById('writeForm').addEventListener('submit', function (evt) {
    evt.preventDefault();

    var recipeName = document.getElementById("floatingInput").value;
    if(recipeName == ""){
        alert("レシピ名を入力してください。")
        moveToTopSmooth();
        return false;
    }

    const input = document.getElementById('input');
    const preview = document.getElementById('preview');

    if (!input.value && preview.querySelector('img')) {
      const imgSrc = preview.querySelector('img').getAttribute('src');
      const thumbInput = document.createElement('input');
      thumbInput.setAttribute('type', 'hidden');
      thumbInput.setAttribute('id', 'thumbFile2');
      thumbInput.setAttribute('name', 'thumbFile');
      thumbInput.setAttribute('value', imgSrc);
      preview.appendChild(thumbInput);
    }

    var thumbFile2Input = document.getElementById("thumbFile2");
    var recipe_image = "";

    if (thumbFile2Input) {
      recipe_image = thumbFile2Input.value;
    } else {
      recipe_image = document.getElementById("preview").querySelector('img').getAttribute('src');
    }

    if(recipe_image == ""){
        alert("レシピのイメージを登録してください。")
        moveToRecipeImageSmooth();
        return false;
    }

    var savedListData = localStorage.getItem('savedList');
    savedList = savedListData ? JSON.parse(savedListData) : [];

    if (savedListData == null || savedList.length === 0) {
        checkOriginalList();
        var originalItemsData = localStorage.getItem('originalItems');
        if (originalItemsData == null) {
            alert("レシピの材料を登録してください。");
            moveToRecipeIngredientSmooth();
            return false;
        }
        originalItems = JSON.parse(originalItemsData);
        $('#send-list-input').val(JSON.stringify(originalItems));
    } else {
        $('#send-list-input').val(JSON.stringify(savedList));
    }

    var instructions = document.getElementById("instructions").value;
    if(instructions == ""){
        alert("作り方を入力してください。");
        moveToRecipeInstructionSmooth();
        return false;
    }

    var videoUrl = document.getElementById("video-url").value;
    console.log(videoUrl);
    var pattern = /^(https?\:\/\/)?(www\.youtube\.com|youtu\.?be)\/.+$/;
    if(videoUrl != ""){
        if(!pattern.test(videoUrl)){
                alert("正しいURLを入力してください。")
                moveToBottomSmooth();
                return false;
        }
    }

    var ist = document.getElementById("input2_1").value;
    if(confirm('レシピを投稿しますか?')) {
        this.submit();
        alert("投稿しました!");
    }
})




















const label = document.getElementById("label");
const input = document.getElementById("input");
const preview = document.getElementById("preview");
const dragIcon = document.querySelector(".dragicon");

function handleFiles(files) {
  const file = files[0];
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

function handleInputChange(event) {
  const files = event.target.files;
  if (!files) {
    return;
  }
  handleFiles(files);
}

function handleDragEnter(event) {
  event.preventDefault();
  label.classList.add("label--hover");
}

function handleDragOver(event) {
  event.preventDefault();
}

function handleDragLeave(event) {
  event.preventDefault();
  label.classList.remove("label--hover");
}

function handleDrop(event) {
  event.preventDefault();
  label.classList.remove("label--hover");
  const files = event.dataTransfer.files;
  if (!files) {
    return;
  }
  handleFiles(files);
}

input.addEventListener("change", handleInputChange);

label.addEventListener("mouseover", (event) => {
  event.preventDefault();
  label.classList.add("label--hover");
});

label.addEventListener("mouseout", (event) => {
  event.preventDefault();
  label.classList.remove("label--hover");
});

label.addEventListener("dragenter", handleDragEnter);

label.addEventListener("dragover", handleDragOver);

label.addEventListener("dragleave", handleDragLeave);

label.addEventListener("drop", handleDrop);


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

  if (nodeName === "input" && attributes.type === "file") {
    const sectionId = attributes["data-section-id"];
    const inputId = `input${sectionId}_${Math.floor(Math.random() * 100)}`;
    const label = node.parentElement;
    const preview = label.querySelector(".preview2");
    const dragIcon = label.querySelector(".dragicon2_1");

    label.addEventListener("dragover", (event) => {
      event.preventDefault();
    });

    label.addEventListener("dragenter", (event) => {
      event.preventDefault();
      label.classList.add("dragover");
    });

    label.addEventListener("dragleave", (event) => {
      event.preventDefault();
      label.classList.remove("dragover");
    });

    // add handleDrop listener to label element
    label.addEventListener("drop", handleDrop);

    const input = node;
    input.id = inputId;
    input.addEventListener("change", (event) => {
      const files = event.target.files;
      if (!files) {
        return;
      }
      handleFiles(files);
    });
  }

  return node;
}





//
//
//var input = document.getElementById("input");
//var initLabel = document.getElementById("label");
//
//input.addEventListener("change", (event) => {
//    const files = changeEvent(event);
//    handleUpdate(files);
//});
//
//initLabel.addEventListener("mouseover", (event) => {
//    event.preventDefault();
//    const label = document.getElementById("label");
//    label?.classList.add("label--hover");
//});
//
//initLabel.addEventListener("mouseout", (event) => {
//    event.preventDefault();
//    const label = document.getElementById("label");
//    label?.classList.remove("label--hover");
//});
//
//initLabel.addEventListener("dragenter", (event) => {
//    event.preventDefault();
//    console.log("dragenter");
//    const label = document.getElementById("label");
//    label?.classList.add("label--hover");
//});
//
//initLabel.addEventListener("dragover", (event) => {
//    console.log("dragover");
//    event.preventDefault();
//});
//
//initLabel.addEventListener("dragleave", (event) => {
//    event.preventDefault();
//    console.log("dragleave");
//    const label = document.getElementById("label");
//    label?.classList.remove("label--hover");
//});
//
//initLabel.addEventListener("drop", (event) => {
//    event.preventDefault();
//    console.log("drop");
//    const label = document.getElementById("label");
//    label?.classList.remove("label--hover");
//    const files = event.dataTransfer?.files;
//    handleUpdate([...files]);
//});
//
//function changeEvent(event) {
//    const { target } = event;
//    return [...target.files];
//};
//
//function handleUpdate(event) {
//  const preview = document.getElementById("preview");
//  const dragIcon = document.querySelector(".dragicon");
//
//  const fileList = event.target.files;
//  if (!fileList) {
//    return; // exit the function if no file is selected
//  }
//
//  const file = fileList[0];
//  const reader = new FileReader();
//
//  reader.addEventListener("load", (event) => {
//    const img = document.createElement("img");
//    img.className = "embed-img";
//    img.src = event.target.result;
//
//    const imgContainer = document.createElement("div");
//    imgContainer.className = "container-img";
//    imgContainer.appendChild(img);
//
//    preview.innerHTML = ""; // clear any existing preview
//    preview.appendChild(imgContainer);
//    dragIcon.style.display = "none";
//  });
//
//  reader.readAsDataURL(file);
//}
//
//
//function el(nodeName, attributes, ...children) {
//    const node =
//        nodeName === "fragment"
//            ? document.createDocumentFragment()
//            : document.createElement(nodeName);
//
//    Object.entries(attributes).forEach(([key, value]) => {
//        if (key === "events") {
//            Object.entries(value).forEach(([type, listener]) => {
//                node.addEventListener(type, listener);
//            });
//        } else if (key in node) {
//            try {
//                node[key] = value;
//            } catch (err) {
//                node.setAttribute(key, value);
//            }
//        } else {
//            node.setAttribute(key, value);
//        }
//    });
//
//    children.forEach((childNode) => {
//        if (typeof childNode === "string") {
//            node.appendChild(document.createTextNode(childNode));
//        } else {
//            node.appendChild(childNode);
//        }
//    });
//
//    return node;
//}

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

    function handleDrop2(event) {
      event.preventDefault();
      var sectionId = event.target.dataset.sectionId;
      var fileList = event.dataTransfer.files;
      if (fileList.length > 0) {
        var file = fileList[0];
        var reader = new FileReader();
        reader.onload = function (event) {
          var image = new Image();
          image.src = event.target.result;
          image.onload = function () {
            var preview = document.getElementById(`preview2_${sectionId}`);
            var dragIcon = document.querySelector(`.dragicon2_${sectionId}`);
            preview.innerHTML = `
              <div class="img-preview-container">
                <img src="${image.src}" alt="preview" class="img-preview" />
                <button type="button" class="btn btn-sm btn-danger remove-img-btn" data-section-id="${sectionId}">Remove</button>
              </div>
            `;
            dragIcon.style.display = "none";
          };
        };
        reader.readAsDataURL(file);
      }
    }


 });

    var ingredientInput = document.getElementById("ingredient");
    checkIngredientList();
    function checkIngredientList() {
        if (ingredientInput && ingredientInput.value) {

            if (localStorage.getItem('selectedItems')) {
                localStorage.removeItem('selectedItems');
            }

          let selectedItems = [];

          let inputs = document.querySelectorAll('#riListTable input[type="hidden"]');

          for (let i = 0; i < inputs.length; i += 8) {
            let item = {
              mainCategory: inputs[i].value,
              subCategory: inputs[i + 1].value,
              ingredient: inputs[i + 2].value,
              ingredientValue: inputs[i + 3].value,
              qty: inputs[i + 4].value,
              qtyValue: inputs[i + 5].value,
              unit: inputs[i + 6].value,
              unitValue: inputs[i + 7].value
            };
            selectedItems.push(item);
          }
          localStorage.setItem('selectedItems', JSON.stringify(selectedItems));
          let checked = JSON.parse(localStorage.getItem('selectedItems'));
        }
    }

    function checkOriginalList() {
            if (ingredientInput && ingredientInput.value) {

                if (localStorage.getItem('originalItems')) {
                    localStorage.removeItem('originalItems');
                }

              let originalItems = [];

              let inputs = document.querySelectorAll('#riListTable input[type="hidden"]');

              for (let i = 0; i < inputs.length; i += 8) {
                let item = {
                  ingredient: inputs[i + 2].value,
                  ingredientId: inputs[i + 3].value,
                  qty: inputs[i + 4].value,
                  qtyValue: inputs[i + 5].value,
                  unit: inputs[i + 6].value,
                  unitId: inputs[i + 7].value
                };
                originalItems.push(item);
              }
              localStorage.setItem('originalItems', JSON.stringify(originalItems));
              let checked = JSON.parse(localStorage.getItem('originalItems'));
            }
        }
