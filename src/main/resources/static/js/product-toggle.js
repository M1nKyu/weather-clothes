function showProducts(category) {
    // 모든 상품 목록을 숨김
    const allProducts = document.querySelectorAll('.product-list');
    allProducts.forEach(list => list.classList.add('hidden'));

    // 모든 버튼의 active 클래스 제거
    const allButtons = document.querySelectorAll('.sub-category-button');
    allButtons.forEach(button => button.classList.remove('active'));

    // 선택된 카테고리의 상품만 표시
    const categoryWithHyphen = category.split(" ").join("-");  // 모든 공백을 하이픈으로 변환
    console.log(categoryWithHyphen);  // 출력값 확인
    const selectedProducts = document.getElementById('products-' + categoryWithHyphen);
    if (selectedProducts) {
        selectedProducts.classList.remove('hidden');
    }

    // 클릭된 버튼에 active 클래스 추가
    const clickedButton = document.querySelector(`[data-category="${category}"]`);
    if (clickedButton) {
        clickedButton.classList.add('active');
    }
}

// Swiper 이벤트 리스너 추가
document.addEventListener('DOMContentLoaded', function() {
    const swiper = document.querySelector('.swiper').swiper;

    swiper.on('slideChangeTransitionEnd', function () {
        // 현재 활성화된 슬라이드에서 첫 번째 서브카테고리 버튼 찾기
        const activeSlide = document.querySelector('.swiper-slide-active');
        const firstButton = activeSlide.querySelector('.sub-category-button');

        if (firstButton) {
            const firstCategory = firstButton.getAttribute('data-category');
            showProducts(firstCategory);
        }
    });

    // 초기 페이지 로드 시 첫 번째 카테고리 표시
    const urlParams = new URLSearchParams(window.location.search);
    const categoryParam = urlParams.get('category');

    if (categoryParam) {
        showProducts(categoryParam);
    } else {
        const firstButton = document.querySelector('.swiper-slide-active .sub-category-button');
        if (firstButton) {
            const firstCategory = firstButton.getAttribute('data-category');
            showProducts(firstCategory);
        }
    }
});