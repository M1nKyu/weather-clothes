let deferredPrompt;

const installButton = document.getElementById('install-button');

// 'beforeinstallprompt' 이벤트 처리
window.addEventListener('beforeinstallprompt', (event) => {
    // 기본 동작을 막고, 이벤트를 저장
    event.preventDefault();
    deferredPrompt = event;
    
    // 아이콘 추가 버튼을 보이게 함
    installButton.style.display = 'block';

    // 버튼 클릭 시 PWA 설치 요청
    installButton.addEventListener('click', () => {
        // PWA 설치 알림을 표시
        deferredPrompt.prompt();
        
        // 사용자가 설치를 완료하거나 취소하면 'appinstalled' 이벤트 발생
        deferredPrompt.userChoice.then((choiceResult) => {
            if (choiceResult.outcome === 'accepted') {
                console.log('사용자가 PWA 설치를 완료했습니다.');
            } else {
                console.log('사용자가 PWA 설치를 취소했습니다.');
            }
            // 이후 버튼을 숨기기
            installButton.style.display = 'none';
        });
    });
});

// 이미 설치된 경우, 아이콘 추가 버튼 숨기기
window.addEventListener('appinstalled', (event) => {
    console.log('설치되었습니다.');
    installButton.style.display = 'none';
});