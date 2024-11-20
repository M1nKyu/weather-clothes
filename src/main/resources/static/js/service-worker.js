if ('serviceWorker' in navigator) {
    window.addEventListener('load', () => {
        navigator.serviceWorker.register('/service-worker.js')
            .then(registration => {
                console.log('ServiceWorker 등록 성공:', registration.scope);
                registration.update(); //
            })
            .catch(err => {
                console.log('ServiceWorker 등록 실패:', err);
            });
    });
}