document.addEventListener('DOMContentLoaded', function () {
    // 각 메인 카테고리의 스크롤 버튼과 컨테이너에 대해 작업
    document.querySelectorAll('.product-categories').forEach(category => {
        const leftButton = category.querySelector('.scroll-button-left');
        const rightButton = category.querySelector('.scroll-button-right');
        const buttonsContainer = category.querySelector('.sub-category-buttons-container');

        // 버튼 이동
        leftButton.addEventListener('click', () => {
            buttonsContainer.scrollBy({ left: -200, behavior: 'smooth' });
        });

        rightButton.addEventListener('click', () => {
            buttonsContainer.scrollBy({ left: 200, behavior: 'smooth' });
        });

        // 스크롤 상태에 따른 버튼 숨김/표시
        function toggleScrollButtons() {
            const isScrolledToEnd = buttonsContainer.scrollWidth === buttonsContainer.scrollLeft + buttonsContainer.clientWidth;
            const isScrolledToStart = buttonsContainer.scrollLeft === 0;

            leftButton.style.display = isScrolledToStart ? 'none' : 'block';
            rightButton.style.display = isScrolledToEnd ? 'none' : 'block';
        }

        // 초기 상태 확인
        toggleScrollButtons();

        // 스크롤할 때마다 버튼 상태 확인
        buttonsContainer.addEventListener('scroll', toggleScrollButtons);
    });
});
