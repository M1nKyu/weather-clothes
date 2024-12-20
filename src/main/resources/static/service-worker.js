const CACHE_NAME = 'otneul-v1';
const STATIC_CACHE = [
  '/',
  '/js/menu-toggle.js',
  '/js/productSwiper.js',
  '/js/product-toggle.js',
  '/js/scroll-buttons.js',
  '/js/footer-buttons.js',
  '/css/global.css',
  '/css/mainPage/category-suggestions.css',
  '/css/mainPage/outfit-display.css',
  '/css/mainPage/weather-info.css',
  '/images/otneul-logo3.png',
  '/images/otneul-logo4.png',
  '/images/ui-icons/bx-menu.svg',
  '/images/ui-icons/arrow-left.svg',
  '/images/ui-icons/arrow-right.svg',
  '/images/ui-icons/arrow-left-bold.svg',
  '/images/ui-icons/arrow-right-bold.svg',
  'https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.css',
  'https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.js'
];

// 동적 콘텐츠를 위한 네트워크 우선 전략 적용
const DYNAMIC_ROUTES = [
  '/',
  '/location/select'
];

self.addEventListener('install', event => {
  event.waitUntil(
    caches.open(CACHE_NAME)
      .then(cache => cache.addAll(STATIC_CACHE))
  );
});

self.addEventListener('fetch', event => {
  const url = new URL(event.request.url);

  // chrome-extension://로 시작하는 요청은 캐시하지 않음
  if (url.protocol === 'chrome-extension:') {
    event.respondWith(fetch(event.request));
    return;
  }

  // POST 요청은 네트워크로 직접 전달
  if (event.request.method !== 'GET') {
    event.respondWith(fetch(event.request));
    return;
  }

  // 동적 라우트인 경우 네트워크 우선 전략 사용
  if (DYNAMIC_ROUTES.includes(url.pathname)) {
    event.respondWith(
      fetch(event.request)
        .then(response => {
          const responseToCache = response.clone();
          caches.open(CACHE_NAME)
            .then(cache => {
              try {
                cache.put(event.request, responseToCache);
              } catch (err) {
                console.error('Failed to cache dynamic route:', err);
              }
            });
          return response;
        })
        .catch(() => caches.match(event.request)) // 네트워크 실패 시 캐시에서 반환
    );
  } else {
    // 정적 리소스는 캐시 우선 전략 사용
    event.respondWith(
      caches.match(event.request)
        .then(response => {
          if (response) {
            return response;
          }
          return fetch(event.request)
            .then(response => {
              if (!response || response.status !== 200) {
                return response;
              }
              const responseToCache = response.clone();
              caches.open(CACHE_NAME)
                .then(cache => {
                  try {
                    cache.put(event.request, responseToCache);
                  } catch (err) {
                    console.error('Failed to cache static resource:', err);
                  }
                });
              return response;
            })
        })
    );
  }
});

self.addEventListener('activate', event => {
  event.waitUntil(
    caches.keys().then(cacheNames => {
      return Promise.all(
        cacheNames.map(cacheName => {
          if (cacheName !== CACHE_NAME) {
            return caches.delete(cacheName);
          }
        })
      );
    })
  );
});
