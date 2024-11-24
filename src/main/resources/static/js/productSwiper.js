document.addEventListener('DOMContentLoaded', function () {
    var productSwiper = new Swiper(".productSwiper", {
        pagination: {
            el: ".swiper-pagination",
            type: "progressbar",
        },
        navigation: {
            nextEl: ".swiper-button-next",
            prevEl: ".swiper-button-prev",
        },
        slidesPerView: 1,
        spaceBetween: 30,
        allowTouchMove: true
    });
});
