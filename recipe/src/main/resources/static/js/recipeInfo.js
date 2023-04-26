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
