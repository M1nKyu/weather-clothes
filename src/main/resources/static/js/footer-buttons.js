function scrollToSectionByClass(className) {
    const elements = document.getElementsByClassName(className);
    if (elements.length > 0) {
        const element = elements[0];
        const headerHeight = document.querySelector('header').offsetHeight; // 헤더 높이 계산
        const elementPosition = element.getBoundingClientRect().top + window.scrollY; // 요소의 실제 위치 계산

        // 헤더 높이만큼 스크롤 위치를 보정
        window.scrollTo({
            top: elementPosition - headerHeight - 10, // 약간의 여유 공간 추가 (-10)
            behavior: 'smooth'
        });
    } else {
        console.error(`No elements with class '${className}' found.`);
    }
}
