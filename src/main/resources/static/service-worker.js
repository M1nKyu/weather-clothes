const CACHE_NAME = 'otneul-v1';
const STATIC_CACHE = [
  '/',
  '/css/mainPage.css',
  '/js/menu-toggle.js',
  '/js/productSwiper.js',
  '/js/product-toggle.js',
  '/js/scroll-buttons.js',
  '/images/otneul-logo.png',
  '/images/otneul-logo2.png',
  '/images/ui-icons/bx-menu.svg',
  '/images/ui-icons/arrow-left.svg',
  '/images/ui-icons/arrow-right.svg',
  'https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.css',
  'https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.js'
];

// 설치 이벤트
self.addEventListener('install', event => {
  event.waitUntil(
    caches.open(CACHE_NAME)
      .then(cache => cache.addAll(STATIC_CACHE))
  );
});

// 네트워크 요청 처리
self.addEventListener('fetch', event => {
  const request = event.request;
  
  // API 요청인 경우 (날씨, 위치 등)
  if (request.url.includes('/api/') || 
      request.url.includes('weather') || 
      request.url.includes('location')) {
    event.respondWith(
      fetch(request)
        .catch(error => {
          // 네트워크 요청 실패시 캐시된 데이터 반환
          return caches.match(request);
        })
    );
    return;
  }

  // 정적 리소스인 경우
  event.respondWith(
    caches.match(request)
      .then(response => {
        // 캐시에 있으면 캐시된 응답 반환
        if (response) {
          return response;
        }
        
        // 캐시에 없으면 네트워크 요청
        return fetch(request).then(
          response => {
            // 유효한 응답이 아니면 그대로 반환
            if (!response || response.status !== 200) {
              return response;
            }

            // 응답을 복제하여 캐시에 저장
            const responseToCache = response.clone();
            caches.open(CACHE_NAME)
              .then(cache => {
                if (STATIC_CACHE.includes(request.url)) {
                  cache.put(request, responseToCache);
                }
              });

            return response;
          }
        );
      })
  );
});

// 주기적으로 날씨 데이터 업데이트
self.addEventListener('periodicsync', event => {
  if (event.tag === 'update-weather') {
    event.waitUntil(updateWeather());
  }
});

// 날씨 정보 업데이트 함수
async function updateWeatherInfo() {
    try {
        // 위치 정보 가져오기
        const position = await getCurrentPosition();
        
        // 날씨 정보 업데이트
        const weatherResponse = await fetch('/api/weather', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                latitude: position.coords.latitude,
                longitude: position.coords.longitude
            })
        });

        if (weatherResponse.ok) {
            const weatherData = await weatherResponse.json();
            updateUIWithWeather(weatherData);
        }
    } catch (error) {
        console.error('날씨 정보 업데이트 실패:', error);
    }
}

// 위치 정보 가져오기
function getCurrentPosition() {
    return new Promise((resolve, reject) => {
        navigator.geolocation.getCurrentPosition(resolve, reject);
    });
}

// 주기적으로 날씨 정보 업데이트
setInterval(updateWeatherInfo, 5 * 60 * 1000); // 5분마다 업데이트

// 페이지 로드시 즉시 업데이트
document.addEventListener('DOMContentLoaded', updateWeatherInfo);

// 페이지가 포커스를 받았을 때 업데이트
document.addEventListener('visibilitychange', () => {
    if (document.visibilityState === 'visible') {
        updateWeatherInfo();
    }
}); 