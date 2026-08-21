# GuideMate Final Android Entegrasyon Devir Belgesi

Bu belge, tamamlanmis GuideMateBackend ile Android uygulamasinin final
entegrasyonunu yonetecek tek calisma dosyasidir. Her entegrasyon/refactor
adimindan once bu dosya yeniden okunur ve yalniz tamamlanmis denetimlerin
dogruladigi kararlar uygulanir.

Son guncelleme: 2026-08-21

Projeler:

- Android: `/Users/ahmetkaragunlu/AndroidStudioProjects/GuideMate`
- Backend: `/Users/ahmetkaragunlu/IdeaProjects/GuideMateBackend`
- Ayrintili backend plani:
  `/Users/ahmetkaragunlu/AndroidStudioProjects/GuideMate/docs/backend-implementation-handoff.md`
- Backend calisma kurallari:
  `/Users/ahmetkaragunlu/IdeaProjects/GuideMateBackend/BACKEND_RULES.md`

## Altin Kural - Orantili ve Profesyonel Kod Kalitesi

- Kod gercek sirket projesine yakin; SOLID, dusuk bagimlilik, okunabilirlik,
  test edilebilirlik, genisletilebilirlik, anlamli isimlendirme ve gercek kod
  tekrarinin azaltilmasi hedefleriyle yazilir.
- Bu hedefler mevcut dogru kodda zorla hata aramak, her uzun dosyayi bolmek,
  gelecekte gerekebilir diye katman eklemek veya mimariyi gereksiz buyutmek icin
  kullanilmaz.
- Bir sinif, fonksiyon veya paket yalniz uzun ya da kalabalik gorundugu icin
  degistirilmez. Sorumluluk, bagimlilik, degisiklik nedeni, tekrar veya test
  izolasyonu acisindan somut fayda varsa en kucuk davranis-koruyucu refactor
  yapilir.
- Ortak yapi yalniz ayni business anlami ve ayni degisiklik nedeni gercekten
  paylasiliyorsa kurulur. Benzer gorunen fakat farkli lifecycle/kurala sahip
  rehber ve turist akislar zorla ortaklastirilmaz.
- Android kalici is kurali otoritesi olmaz. Yetki, sahiplik, para, kur,
  kapasite, rezervasyon, iade, kazanc, tur lifecycle ve odeme basarisi backend
  sonucundan gelir.
- UI tasarimi, mevcut navigasyon davranisi ve kullanici akisi bilincli urun
  karari olmadan degistirilmez. Refactor tasarimi veya runtime davranisini
  sessizce degistiremez.
- Yeni kod feature-first hedef yapida dogru feature ve katmanda bulunur.
  Presentation, domain, data, network, storage ve navigation sorumluluklari
  birbirine karistirilmaz.
- Dis sistemler ve degisebilir data kaynaklari dar interface/adapter
  sinirlarinin arkasinda kalir. UI veya ViewModel Retrofit, iyzico, FCM, STOMP,
  Coil ya da persistence ayrintisini dogrudan bilmez.
- Entegrasyon sirasinda yeni use-case sinifi eklenmez. Mevcut auth use-case'leri
  yalniz repository metodunu ileten pass-through siniflar oldugu icin
  feature-first auth refactor'unda kaldirilir; ilgili ViewModel'lar dogrudan
  `AuthRepository` arayuzune baglanir.
- Use-case ancak birden fazla repository veya tekrar kullanilan anlamli bir is
  akisini orkestre eden somut ihtiyac dogarsa final toplu denetimde eklenebilir.
  Sirf katman sayisini artirmak icin use-case yazilmaz.
- Saf, tekrar kullanilan istemci giris politikalari domain'in parcasidir.
  `EmailPolicy` ve `NumericPasswordPolicy` `auth/domain/validation` altinda
  kalir; backend validation otoritesinin yerine gecmez.
- Sifir deger saglayan bos repository, use-case, manager, helper, factory, base
  class veya generic framework eklenmez.
- Bir degisiklik eski mock, store, mapper, fonksiyon, dosya veya paketi tamamen
  bosa dusururse ve bilincli gecis gorevi kalmadiysa temizlenir. Kullaniciya ait
  ilgisiz degisiklikler geri alinmaz.
- Secret, token, kart verisi, tam IBAN, provider credential, teknik exception
  veya hassas kullanici verisi source control'e, loga ya da UI hata metnine
  sizmaz.
- Her dikey entegrasyon dilimi compile, format, test ve gercek sozlesme
  kontrolleriyle kapatilir. Bulgu yoksa sirf degisiklik yapmak icin refactor
  yapilmaz.

## Belge Otoritesi ve Celiski Sirasi

Entegrasyon kararinda kaynaklar su sirayla kullanilir:

1. O anda yeniden taranmis backend ve Android kaynak kodu.
2. Flyway migration'lari, backend testleri ve uretilen `/v3/api-docs`
   sozlesmesi.
3. Kullanicinin en son acik karari.
4. Bu final entegrasyon belgesindeki tamamlanmis adimlar.
5. `BACKEND_RULES.md` ve `backend-implementation-handoff.md`.
6. Eski notlar, anayasa ozetleri ve sohbet hafizasi.

Eski belgede plan olarak yazilmis bir alan guncel kaynak/OpenAPI ile
celisiyorsa kaynak kod ve testle dogrulanmis sozlesme esas alinir. Celiski
sessizce tahmin edilmez; bu belgeye gercek durum ve entegrasyon sonucu yazilir.

## Her Kod Fazinda Zorunlu Kaynak Dogrulama Dongusu

Her refactor ve backend entegrasyon fazinda asagidaki sira eksiksiz uygulanir:

1. Final dosyasinin ilgili bolumleri okunur.
2. Guncel Android kodu, backend kodu ve canli OpenAPI sozlesmesi dogrulanir.
3. Celiski varsa tahmin yurutmeden kullanicinin en son acik karari ve canli
   sozlesme esas alinir; yanlis taraf kaynak kodla birlikte belirlenir.
4. Gerekirse kod yazmadan once bu final dosyasi guncellenir.
5. Ardindan kod yazilir, ilgili mock ve bosa dusen yapilar kaldirilir; o fazin
   format, compile, test, contract ve UI smoke kapilari calistirilir.

Bu dongu tavsiye degil, her faz icin baglayici calisma kuralidir. Final dosyasi
tek operasyonel yol haritasidir; ancak canli kod ve OpenAPI yerine gecen donmus
bir gerceklik kaynagi degildir. Eski sohbet veya anayasa notlari rutin olarak
yeniden taranmaz; final dosyada aciklanmayan gercek bir celiski bulunursa belge
otoritesi sirasina gore destekleyici kaynak olarak kullanilir.

## Yedi Adimli Hazirlik Plani

| Adim | Kapsam | Durum |
| --- | --- | --- |
| 1 | Backend kodu, Flyway, `BACKEND_RULES`, handoff, test ve API sozlesmesi | TAMAMLANDI |
| 2 | Android kaynak kodu, ekranlar, store'lar, mevcut API/auth ve paketler | TAMAMLANDI |
| 3 | Tum anayasalar, ertelenen Android maddeleri ve yeni kararlar | TAMAMLANDI |
| 4 | Endpoint/DTO/domain/UI esleme matrisi ve gercek entegrasyon bosluklari | TAMAMLANDI |
| 5 | Android feature-first hedef paket ve dosya tasima plani | TAMAMLANDI |
| 6 | Dikey entegrasyon, mock kaldirma ve uygulama sirasi | TAMAMLANDI |
| 7 | Test, E2E, temizlik ve tamamlanma kriterleri | TAMAMLANDI |

Bu adimlar sirayla tamamlanir. Bir sonraki adim, onceki adimin bulgulari bu
dosyaya yazilip dogrulanmadan kodlama gerekcesi olarak kullanilmaz.

## Adim 1 - Dogrulanmis Backend Temeli

### Denetim Kapsami

2026-08-18 tarihinde su kaynaklar yeniden tarandi:

- Backend calisma agaci ve feature paketleri.
- `pom.xml`, runtime/config sinirlari ve security ayarlari.
- Flyway `V1`-`V13` migration dosyalari.
- Controller, request/response DTO, domain enum, mapper, service, repository ve
  scheduler sinirlari.
- Ortak `ErrorCode`/`ErrorResponse`, validation ve security error writer.
- REST, WebSocket/STOMP, FCM, media, iyzico, FX, wallet ve scheduler akislari.
- `BACKEND_RULES.md` ile Android'deki
  `docs/backend-implementation-handoff.md`.
- Backend kalici test paketi ve OpenAPI contract testleri.

Denetim sirasinda backend source degistirilmedi. Android kaynak kodu
degistirilmedi. Android calisma agacinda daha onceden var olan
`docs/backend-implementation-handoff.md` degisikligi korunmustur.

### Backend Mimari Sonucu

Backend tek Spring Boot uygulamasi icinde feature-first modular monolith olarak
kurulmustur. Dogrudan feature kokleri sunlardir:

- `auth`
- `chat`
- `common`
- `demo`
- `media`
- `notification`
- `payment`
- `profile`
- `reservation`
- `review`
- `tour`
- `user`
- `wallet`

Feature'lar ihtiyacina gore `controller`, `domain`, `dto`, `mapper`,
`repository`, `service`, `config`, `gateway` veya `storage` alt sinirlarini
kullanir. Controller HTTP sinirinda, DTO API sozlesmesinde, service is
kuralinda/transaction'da, repository persistence erisiminde ve domain state
kurallarinda kalir. Dis sistemler adapter sinirlari arkasindadir.

Bu yapi Android icin su karari destekler: Android de tek Gradle `app` modulu
icinde dogrudan feature koklerine gecmeyi hedefleyebilir; fakat kesin paket ve
dosya tasima plani Adim 5'te mevcut Android bagimliliklari tarandiktan sonra
yazilacaktir. Bu adimda Android dosyasi tasinmaz.

### Teknik Temel

- Spring Boot: `3.5.16`
- Java derleme hedefi: `17`
- PostgreSQL JDBC: `42.7.13`
- Flyway: `12.8.1`
- Springdoc OpenAPI: `2.8.17`
- Testcontainers: `2.0.5`
- Firebase Admin: `9.10.0`
- iyzipay Java: `2.0.142`
- JWT: JJWT `0.13.0`
- Kalici veri tabani: PostgreSQL
- Sema yonetimi: Flyway; Hibernate `ddl-auto=validate`
- Kimlik: mevcut auth tablolarinda `Long`, yeni business entity'lerinde UUID
- Business zamani: `Instant` ve PostgreSQL `TIMESTAMPTZ`
- Canonical para: USD minor unit `long/BIGINT`
- Provider charge parasi: backend quote snapshot'i ile desteklenen
  `USD/TRY/EUR/GBP`

Backend test komutu Java 26 runtime ile calismis olsa da proje derleme hedefi
Java 17'dir. Android entegrasyonu backend Java runtime secimine baglanmaz.

### Flyway Sema Otoritesi

Temiz PostgreSQL uzerinde su migration zinciri dogrulanmistir:

1. `V1`: auth rolleri, kullanicilar, e-posta dogrulama, sifre sifirlama ve
   refresh token tablolari.
2. `V2`: eski auth provider alaninin kaldirilmasi.
3. `V3`: medya, rehber profili ve rehber dilleri.
4. `V4`: tur, tur dili, session ve tur degisiklik/onay akisi.
5. `V5`: rezervasyon ve yorum.
6. `V6`: session iptali idempotency destegi.
7. `V7`: payment, wallet, ledger, refund, guide earning, banka hesabi ve
   withdrawal.
8. `V8`: provider customer ve provider-backed kayitli odeme yontemleri.
9. `V9`: bildirim, bildirim tercihleri ve cihaz token altyapisi.
10. `V10`: sohbet, mesaj ve okundu state'i.
11. `V11`: cihaz token kaydinin Firebase Installation ID kaydina gecisi.
12. `V12`: scheduler recovery, retry, deduplication ve reminder state'i.
13. `V13`: FX quote, payment charge ve refund charge snapshot'lari.

Android entegrasyonu yeni tablo veya Flyway migration gerektirmez. Yeni bir
gercek contract boslugu bulunursa backend degisikligi ayri onaylanir; Android
eksigi gizlemek icin local kalici otorite uretmez.

### Kimlik, Auth ve Yetki Siniri

- REST JWT ile stateless calisir.
- Sahiplik request ile gelen `userId/guideId` degerinden degil authenticated
  principal'dan belirlenir.
- Auth endpoint'leri register, login, Google login, refresh, logout, role
  secimi, current user, sifre degistirme, dogrulama e-postasi, sifre sifirlama
  ve web link sonucunu kapsar.
- Login, Google login, refresh ve logout akislari `X-Installation-Id` header'i
  kullanir.
- Refresh rotation/replay, rate limit, e-posta dogrulama, Google hesap
  eslestirme ve role kurallari backend'de uygulanir.
- Guide, tourist ve admin endpoint'leri method security ile ayrilir.
- Public projection endpoint'leri yalniz public rehber/tur/medya verisini
  dondurur.
- Android user/guide kimligi uretmez ve request'e otorite olarak eklemez.

### Ortak Hata Sozlesmesi

REST handler ve Spring Security filtresi ayni response bicimini kullanir:

```text
ErrorResponse
- code: String
- message: String
- timestamp: Instant
- fieldErrors: List<FieldErrorResponse>
```

Validation hatalari alan bazli `fieldErrors` dondurur. Beklenmeyen exception
sunucuda loglanir, ancak teknik exception mesaji istemciye gonderilmez. Rate
limit cevabi `Retry-After` header'i tasir.

Android eski handoff'taki ornek kod listesini degil, guncel backend
`ErrorCode`/OpenAPI sozlesmesini map edecektir. Dogrulanan kod gruplari:

- Auth/user: `USER_NOT_FOUND`, `EMAIL_ALREADY_EXISTS`,
  `ACCOUNT_PENDING_VERIFICATION`, `ACCOUNT_DISABLED`, `INVALID_CREDENTIALS`,
  sifre/role/installation/token/Google kodlari.
- Profil/medya: `GUIDE_PROFILE_NOT_FOUND`, `INVALID_LANGUAGE_CODE`,
  `MEDIA_NOT_FOUND`, `MEDIA_INVALID_TYPE`, `MEDIA_TOO_LARGE`,
  `MEDIA_STORAGE_FAILED`, `MEDIA_IN_USE`, `MEDIA_PURPOSE_MISMATCH`.
- Tur/session: `TOUR_NOT_FOUND`, `TOUR_NOT_APPROVED`,
  `TOUR_CHANGE_PENDING`, `TOUR_LOCATION_LOCKED`, `TOUR_NOT_ARCHIVABLE`,
  review-state/kategori/ulke/time-zone kodlari, `SESSION_NOT_FOUND`,
  `SESSION_NOT_BOOKABLE`, `SESSION_ALREADY_STARTED`,
  `SESSION_HAS_RESERVATIONS`, `CAPACITY_NOT_AVAILABLE`,
  `CAPACITY_BELOW_BOOKED_COUNT`, `SESSION_STATUS_NOT_MANAGEABLE`,
  `SCHEDULE_CONFLICT`, `CONCURRENT_UPDATE`.
- Rezervasyon/yorum: `RESERVATION_NOT_FOUND`,
  `RESERVATION_ALREADY_EXISTS`, `RESERVATION_NOT_CANCELLABLE`,
  `INVALID_PARTICIPANT_COUNT`, `REVIEW_NOT_ALLOWED`,
  `REVIEW_ALREADY_EXISTS`, `IDEMPOTENCY_CONFLICT`.
- Odeme/finans: payment, FX quote, kart, refund, amount, wallet balance,
  withdrawable balance ve banka hesabi kodlari.
- Mesaj/bildirim: `NOTIFICATION_NOT_FOUND`, `CHAT_NOT_FOUND`,
  `CHAT_PARTICIPANT_INVALID`, `CHAT_MESSAGE_NOT_FOUND`,
  `CHAT_MESSAGE_TOO_LONG`.
- Ortak: `UNAUTHORIZED`, `FORBIDDEN`, `VALIDATION_FAILED`,
  `MALFORMED_REQUEST`, `DATA_CONFLICT`, `RATE_LIMITED`,
  `EMAIL_DELIVERY_FAILED`, `INTERNAL_SERVER_ERROR`.

Eski handoff'taki `MESSAGE_TOO_LONG` ifadesi guncel kaynakta
`CHAT_MESSAGE_TOO_LONG` olarak uygulanmistir. Android hata mapper'i guncel kodu
kullanmalidir. Duplicate chat mesaji ayri `MESSAGE_DUPLICATE` hatasi degildir;
`clientMessageId` idempotency kurali mevcut mesaji dondurur veya celiskide
`IDEMPOTENCY_CONFLICT` uretir.

### REST Endpoint Envanteri

Auth:

- `POST /api/v1/auth/register`
- `POST /api/v1/auth/login`
- `POST /api/v1/auth/google`
- `POST /api/v1/auth/refresh-token`
- `POST /api/v1/auth/logout`
- `POST /api/v1/auth/select-role`
- `GET /api/v1/auth/me`
- `POST /api/v1/auth/change-password`
- `POST /api/v1/auth/resend-verification`
- `POST /api/v1/auth/forgot-password`
- `POST /api/v1/auth/reset-password`
- `GET /api/v1/auth/confirm`
- `GET /api/v1/auth/reset-password-form`

Profil ve medya:

- `POST /api/v1/media`
- `GET /api/v1/media/{mediaId}/content`
- `DELETE /api/v1/media/{mediaId}`
- `GET/PATCH /api/v1/guides/me/profile`
- `GET /api/v1/guides/{guideId}/public-profile`
- `GET /api/v1/guides/search`
- `GET /api/v1/guides/top`

Tur ve dashboard:

- `POST/GET /api/v1/guide/tours`
- `GET /api/v1/guide/tours/{tourId}`
- `POST /api/v1/guide/tours/{tourId}/change-requests`
- `POST /api/v1/guide/tours/{tourId}/sessions`
- `PATCH /api/v1/guide/sessions/{sessionId}`
- `POST /api/v1/guide/sessions/{sessionId}/open`
- `POST /api/v1/guide/sessions/{sessionId}/close`
- `POST /api/v1/guide/sessions/{sessionId}/cancel`
- `POST /api/v1/guide/tours/{tourId}/archive`
- `GET /api/v1/guides/me/dashboard`
- `GET /api/v1/tours/search`
- `GET /api/v1/tours/popular`
- `GET /api/v1/tours/{tourId}`
- `GET /api/v1/tour-sessions/{sessionId}`
- Admin review liste/detay/onay/red endpoint'leri

Rezervasyon ve yorum:

- `GET /api/v1/reservations/me`
- `GET /api/v1/reservations/{reservationId}`
- `POST /api/v1/reservations/{reservationId}/cancel`
- `POST /api/v1/reservations/{reservationId}/reviews`
- `GET /api/v1/tours/{tourId}/reviews`

Odeme, kart, wallet ve rehber finans:

- `GET /api/v1/payments/checkout/currencies`
- `POST /api/v1/payments/checkout/tour/quote`
- `POST /api/v1/payments/checkout/wallet-top-up/quote`
- `POST /api/v1/payments/checkout/tour`
- `POST /api/v1/payments/checkout/wallet-top-up`
- `GET /api/v1/payments/{paymentId}`
- `POST /api/v1/payments/{paymentId}/cancel`
- Public iyzico callback ve webhook endpoint'leri
- `GET /api/v1/payment-methods/cards`
- `DELETE /api/v1/payment-methods/cards/{savedPaymentMethodId}`
- `PUT /api/v1/payment-methods/cards/{savedPaymentMethodId}/default`
- `GET /api/v1/wallet`
- `GET /api/v1/wallet/transactions`
- `GET /api/v1/guide/earnings`
- `GET /api/v1/guide/earnings/monthly`
- `GET/POST /api/v1/guide/bank-accounts`
- `POST /api/v1/guide/bank-accounts/{bankAccountId}/default`
- `DELETE /api/v1/guide/bank-accounts/{bankAccountId}`
- `GET/POST /api/v1/guide/withdrawals`

Mesaj ve bildirim:

- `POST /api/v1/chats/with-user/{remoteUserId}`
- `GET /api/v1/chats`
- `GET/POST /api/v1/chats/{chatId}/messages`
- `POST /api/v1/chats/{chatId}/read`
- `GET /api/v1/chats/unread-count`
- `GET /api/v1/notifications`
- `GET /api/v1/notifications/unread-count`
- `POST /api/v1/notifications/{notificationId}/read`
- `POST /api/v1/notifications/read-all`
- `GET/PATCH /api/v1/notifications/preferences`
- `POST /api/v1/devices/fcm-registration`
- `DELETE /api/v1/devices/fcm-registration/{installationId}`

Standart sayfali response `content`, `page`, `size`, `totalElements`,
`totalPages`, `first`, `last` alanlarini tasir. Chat message history UUID cursor
(`before`) ile sayfalanir. Android yuklenen sayfa boyutunu toplam sayi kabul
etmeyecektir.

### Android'in Dogrudan Kullanacagi Kritik Response Sozlesmeleri

`GuideTourCardResponse`:

- Tur ve session UUID'leri/versiyonlari.
- Baslik, konum, time zone, kategori, dil ve cover.
- Baslangic, sure, USD fiyat, `bookedCount` ve toplam `capacity`.
- Tur bazli `averageRating`/`reviewCount`.
- Session bazli nullable `netEarningsMinor`; kazanc yoksa veya session iptalse
  `null`.
- Approval/session status, red nedeni ve archive yetenegi.

`GuideDashboardResponse`:

- Aktif session ve bekleyen review sayilari.
- Tamamlanan tur, toplam katilimci, ortalama puan ve yorum sayisi.
- Rehber seviye projection'i.
- Bu ayki kazanc ve currency code.

`TourSearchItemResponse`, `TourDetailResponse`, `TourSessionResponse`:

- Rehber ve turist ekranlarinin ayni kalici Tour/Session kaynagindan
  beslenmesini saglar.
- Search item satinalinabilir session projection'idir.
- Detail tur icerigi, public rehber ve session listesini tasir.
- Session toplam kapasite, dolu ve kalan kapasiteyi ayri alanlarla tasir.

`ReservationResponse` ve `ReservationCancellationResponse`:

- Reservation/session UUID, version, katilimci sayisi, unit/total fiyat,
  currency, lifecycle state ve hold expiry.
- Iptal aktoru/nedeni/zamani, refund eligibility, policy code/version.
- Satin alma snapshot'i ve varsa review.
- Iptal response'u canonical rezervasyon ile refund kimlik/durumunu birlikte
  tasir.

`PaymentQuoteResponse` ve `PaymentResponse`:

- Quote; canonical USD tutari ile provider charge tutari/para birimini, FX
  rate/source ve expiry'yi ayri tasir.
- Payment; payment/purpose/method/status, canonical ve charge snapshot,
  hosted URL/expiry, reservation/refund kimlik ve state'leri ile failure code'u
  tasir.
- Android callback JSON'unu veya WebView kapanisini basari saymaz; payment ID
  ile bu canonical response'u sorgular.

`SavedPaymentMethodResponse`:

- Internal saved method ID, maskeli banka/kart metadata'si, son dort hane,
  expiry ve default bilgisi.
- Ham kart numarasi, CVV ve kart secret'i Android/backend'e gelmez.

Wallet/earning:

- Wallet response canonical balance projection'idir.
- Wallet transaction `referenceTitle` degeri turla baglantili hareketlerde
  backend snapshot'indan gelir; top-up/withdrawal icin nullable'dir.
- Aylik kazanc `year`, `month`, `netEarningsMinor`, `currencyCode` tasir.
  Android ayrintili earning sayfalarini indirip aylik toplami hesaplamaz.

Notification/chat:

- Notification kullanici kaynakli olayda nullable `actorDisplayName` tasir;
  sistem olayinda `null` olur.
- Android aktor adi icin ek user sorgusu atmaz.
- Chat conversation tek rehber-turist cifti icin tektir ve rezervasyondan
  bagimsiz baslatilabilir.
- Message `clientMessageId` ile idempotenttir; server kimligi/zamani ve delivery
  state backend sonucundan gelir.

### Dogrulanmis State Machine ve Otorite Kurallari

- Tur approval: `PENDING_REVIEW`, `APPROVED`, `REJECTED`, `ARCHIVED`.
- Session: `OPEN_FOR_BOOKING`, `CLOSED`, `COMPLETED`, `CANCELLED`.
- Tour session iptal aktoru yalniz `GUIDE` veya `ADMIN` olabilir.
- Reservation: `PENDING_PAYMENT`, `CONFIRMED`, `COMPLETED`, `CANCELLED`,
  `EXPIRED`.
- Reservation iptal aktoru `TOURIST`, `GUIDE`, `ADMIN`, `SYSTEM` olabilir.
- Payment: `PENDING`, `REQUIRES_ACTION`, `VERIFYING`, `SUCCEEDED`, `FAILED`,
  `CANCELLED`, `TIMEOUT`.
- Refund: `REQUESTED`, `PROCESSING`, `SUCCEEDED`, `FAILED`, `MANUAL_REVIEW`.
- Guide earning: `PENDING`, `AVAILABLE`, `REVERSED`.
- Withdrawal: `PENDING`, `PROCESSING`, `COMPLETED`, `FAILED`, `CANCELLED`.

Android enumlari daha sonra bu gercek sozlesmeyle birebir karsilastirilacak.
UI icin turetilmis `upcoming/past`, loading veya secili tab state'leri network
domain state'i olarak backend'e gonderilmeyecektir.

### Transaction, Concurrency ve Idempotency

- Capacity ve son koltuk yarisi backend transaction/lock sinirinda korunur.
- Guide session iptali, turist rezervasyon iptali, hosted checkout, wallet
  checkout ve withdrawal mutation'lari idempotency key kullanir.
- Chat gonderimi `clientMessageId` kullanir.
- Wallet tur satin alimi ayni transaction'da payment, reservation ve ledger
  debit olusturur.
- Wallet/provider iadesi refund state'i ve ledger reversal ile izlenir.
- Gec gelen verified payment kapasiteyi yeniden kontrol eder; yer yoksa tek
  idempotent tam iade akisi baslar.
- Android optimistic bakiye, kapasite, rezervasyon veya basari karari uretmez;
  mutation sonucundan sonra canonical projection'lari yeniler.

### Scheduler ve Recovery Otoritesi

Backend su arka plan islerini uygular:

- Suresi dolan `PENDING_PAYMENT` reservation hold'larini expire etme.
- Bitmis open/closed session'lari tamamlama ve rezervasyonlari gecmise alma.
- Belirsiz hosted payment'lari provider ile yeniden uzlastirma.
- Requested/failed/stale-processing refund'lari retry/manual-review akisi.
- `PENDING` guide earning'i zamani gelince `AVAILABLE` yapma.
- Basarisiz FCM teslimatini bounded retry etme.
- Yaklasan turist/rehber tur hatirlatmalarini deduplication ile olusturma.
- Inaktif/eski Firebase installation kayitlarini pasiflestirme/silme.
- Sahipsiz veya silinmek uzere isaretli medyayi grace period sonrasi temizleme.
- Eski auth tokenlarini ve rate-limit kayitlarini temizleme.

Android WorkManager bu canonical lifecycle'lari tekrar etmez. Android'in gorevi
backend state'ini yenilemek, realtime/push olayini tuketmek ve kullaniciya
gorunur state sunmaktir.

### WebSocket/STOMP ve FCM Sozlesmesi

- STOMP endpoint: `/ws`
- Application prefix: `/app`
- User destination prefix: `/user`
- Private chat delivery: `/user/queue/chat-messages`
- Private realtime error: `/user/queue/chat-errors`
- Client send destination: `/app/chats/{chatId}/messages`
- STOMP CONNECT bearer JWT ile yetkilendirilir.
- Kullanici yalniz katilimcisi oldugu sohbete mesaj gonderebilir.
- Backend kalici chat/notification kaydini REST projection'lariyla sunar;
  WebSocket ve FCM hizli teslimat kanallaridir, tek veri kaynagi degildir.
- FCM kaydi Firebase Installation ID ile GuideMate installation ID'yi birlikte
  kullanir.
- Bildirim tercihi in-app kaydi veya unread sayisini silmez; yalniz ilgili push
  teslimatini etkiler.

Android istemci kutuphane ve adapter plani Adim 4-6'da mevcut Gradle/DI yapisi
tarandiktan sonra kesinlestirilecektir.

### Medya Sozlesmesi

- Android `content://`/`file://` URI'yi yalniz gecici secim/onizleme icin tutar.
- Multipart upload backend'den canonical `mediaAssetId` ve HTTP(S) content URL
  dondurur.
- Profil/tur request'i dosya yolu yerine `mediaAssetId` tasir.
- Backend owner/public erisimini ve sahipsiz upload cleanup'ini uygular.
- Android final entegrasyonda ortak image loader ile local URI ve remote URL'yi
  tek presentation sinirinda gosterecektir; auth token URL'ye eklenmez.

### Odeme ve Harici Sistem Siniri

- Iyzico Checkout Form hosted akistir; native ham kart formu otorite degildir.
- Callback/webhook sonucu CF Retrieve ile dogrulanmadan basari kabul edilmez.
- WebView POST callback nedeniyle yalniz `shouldOverrideUrlLoading` olayina
  guvenmez; lifecycle sonrasi backend payment polling yapar.
- SSL hatasi bypass edilmez, `addJavascriptInterface` bridge eklenmez.
- Canonical platform/wallet/tur parasi USD'dir. Android kur veya charge tutari
  hesaplamaz; backend currency-options ve quote response'unu kullanir.
- Rehber payout modu gercek provider yetkisi olmadigi icin bilincli olarak
  `SIMULATED` kalir; Android bunu gercek banka transferi gibi garanti etmez.
- Sandbox buyer kimlik/adres bilgisi backend local config'indedir; Android
  toplamaz veya gondermez.

Harici E2E onkosullari source code degildir:

- SMTP gercek auth e-posta testi oncesinde local secret olarak hazirlanir.
- Iyzico callback/webhook E2E testi oncesinde yeni HTTPS Quick Tunnel acilir ve
  backend/panel ayarlari guncellenir.
- LAN base URL mevcut Mac IP'sine gore local, Git disi ayarda yenilenir.
- Firebase credential ve iyzico secret degerleri Git disinda kalir.

Bu degerlerin hicbiri bu belgeye veya Android source'a acik deger olarak
yazilmaz.

### Dogrulama Kaniti

2026-08-18 tarihinde backend kokunde `./mvnw test` calistirildi:

- Sonuc: `BUILD SUCCESS`
- Test: `72`
- Failure: `0`
- Error: `0`
- Skipped: `0`
- Test veritabani: PostgreSQL `18.6` Testcontainers
- Flyway: temiz semada `V1`-`V13` migration basarili
- Spring context ve Hibernate validate basarili
- OpenAPI contract, auth lifecycle, security, ownership, medya, tour,
  reservation, concurrency/locking, payment/refund/FX, wallet, chat,
  notification/FCM ve persistence testleri basarili

Maven/Spring kaynakli reflective-access ve Hibernate follow-on locking uyarilari
test basarisizligi degildir. Bu adimda Android entegrasyonunu engelleyen backend
compile, migration, context veya contract hatasi bulunmamistir.

### Handoff ile Guncel Kaynak Arasindaki Notlar

- Backend handoff genis bir plan ve tarihce belgesidir; tamamlanan bazi konular
  eski paragraflarda hala gelecek zamanla anlatilabilir. Guncel source,
  `BACKEND_RULES`, test ve OpenAPI sonucu tamamlanmis gercektir.
- Multi-charge-currency, monthly earnings, guide card metrics, notification
  actor adi, wallet reference title, FID, STOMP ve scheduler altyapisi backend'de
  tamamlanmistir; Android tarafinda tuketim beklemektedir.
- Handoff'taki hata kodu ornekleri guncel `ErrorCode` enumundan farkliysa enum
  esas alinir. Ozellikle mesaj limiti `CHAT_MESSAGE_TOO_LONG`dur.
- Backend integration sirasinda H2 kullanilmaz; canonical test DB PostgreSQL
  Testcontainers'dir.
- Chat rezervasyon kosuluna bagli degildir. Rehber ve turist uygun public
  akislar uzerinden konusma baslatabilir; tek kisi cifti sohbeti korunur.

### Adim 1 Sonucu ve Kapisi

Backend, Android final entegrasyonuna baslamak icin yeterli ve dogrulanmis
durumdadir. Bu sonuc backend'e bir daha hic dokunulmayacagi anlamina gelmez;
Adim 4'te gercek Android model/ekran ihtiyaci ile OpenAPI arasinda kanitli bir
contract boslugu bulunursa once raporlanir ve ayri onayla ele alinir.

Su anda backend kaynak kodunda degisiklik gerektiren bilinen bir entegrasyon
blokaji yoktur. Bir sonraki calisma Adim 2'dir: mevcut Android kaynak kodunun
tam taramasi ve gercek entegrasyon envanterinin bu belgeye eklenmesi.

## Adim 2 - Android Mevcut Durum Denetimi

Durum: TAMAMLANDI.

### Denetim Kapsami

2026-08-18 tarihinde Android projesinde su alanlar yeniden tarandi:

- Gradle modulu, plugin ve kutuphane bagimliliklari.
- Manifest, debug network ayari, backup kurallari ve local secret siniri.
- Auth API, repository, token yenileme, DataStore, Android Keystore ve root
  navigation.
- Rehber ve turist graph'lari, type-safe destination'lar, account/tour/payment
  alt akislari ve ortak scaffold yapisi.
- Tum `@HiltViewModel` siniflari, `@Singleton` mock store/provider siniflari,
  UI state modelleri ve mapper'lar.
- Tur yayinlama/duzenleme/detay, turist arama, rezervasyon, yorum, odeme,
  wallet, banka hesabi, kazanc, profil, chat, bildirim, medya ve secim
  akislari.
- Unit testler, bos klasorler, bos callback'ler, hardcoded/mock veri ve eksik
  istemci kutuphaneleri.

Bu adimda Android veya backend source degistirilmedi. Yalniz bu final
entegrasyon belgesi guncellendi. Calisma agacinda daha onceden bulunan
`docs/backend-implementation-handoff.md` degisikligine dokunulmadi.

### Modul ve Teknik Temel

- Proje tek Gradle `:app` moduludur. Mevcut proje buyuklugu ve tek gelistirici
  siniri icin bu dogru tercihtir; final entegrasyonda multi-module gecisi
  yapilmayacaktir.
- `compileSdk/targetSdk` 36, `minSdk` 30 ve Java hedefi 21'dir.
- Compose Material 3, Hilt, type-safe Navigation, Kotlin Serialization,
  Retrofit/Gson, OkHttp, DataStore, Android Credentials, Firebase Messaging
  bootstrap'i ve Google Places mevcuttur.
- `BuildConfig.GUIDEMATE_API_BASE_URL` ve Places anahtari Git disi local
  properties uzerinden gelir. Secret degerleri source'a yazilmaz.
- Production cleartext HTTP kapali, debug cleartext HTTP LAN testi icin aciktir.
- Manifest'te internet, network state, notification izni ve kamera icin gerekli
  `FileProvider` vardir.
- Auth session/user/installation verileri cloud backup ve device transfer'dan
  haric tutulmustur.

### Mevcut Paket Mimarisi

Kod su anda layer-first ile role/screen gruplamasinin karisimidir:

```text
com.ahmetkaragunlu.guidemate
|- common
|- components
|- data
|- di
|- domain
|- navigation
|- screens
|  |- common
|  |- guide
|  `- tourist
`- ui
```

Bu yapi calisir ve derlenir; ancak backend feature-first sinirlariyla dikey
entegrasyonda yeni repository/data kodunu tek global `data` ve `domain`
paketlerinde biriktirmek okunabilirligi azaltacaktir. Kesin hedef agac ve dosya
tasima sirasi Adim 5'te yazilacaktir. Bu denetim paket tasimasi yapmaz.

`screens/common` altinda gercekten ortak olan tur karti/detayi, chat UI,
selection, tab, rating, para formatlama, help-support, legal agreements ve
change-password yapilari bulunur. Rehber/turist farkli business davranislarini
zorla ortaklastiran bir generic ekran bulunmamistir. Bu sinir korunacaktir.

Bos dosya bulunmamistir. Dorde ayrilmis eski help-support/legal `components`
klasoru fiziksel olarak bostur; Git tarafinda kod veya package uretmez. Adim
5'te paket tasimasi sirasinda yerel klasor temizligi yapilabilir.

### Gercek ve Korunacak Auth Altyapisi

Auth, Android'de backend'e gercekten bagli olan tek tamamlanmis feature'dir:

- `AuthApi`, guncel backend auth endpoint'lerini kapsar.
- Request/response DTO'lari `userId`, e-posta, ad, soyad, role ve role-secim
  bilgisini tasir.
- `AuthRepository`, `UserRepository` ve `OnboardingRepository` interface'leri
  presentation'i data implementation'dan ayirir.
- `AuthRepositoryImpl`, Retrofit sonucunu merkezi `DataResult/AppError`
  sozlesmesine cevirir.
- Access/refresh token'lar Android Keystore AES/GCM anahtariyla sifrelenmis
  private storage'da tutulur.
- User cache, role, onboarding ve installation UUID DataStore'da tutulur.
- `AuthInterceptor` bearer token ekler; `TokenAuthenticator` refresh rotation
  ve terminal session temizligini yonetir.
- `RootNavigationViewModel`, local session'i restore eder, `/auth/me` ile
  dogrular ve auth/role/guide/tourist root hedefini user state'ten uretir.
- Logout hem backend session'ini hem local credential state'ini temizler.

Bu yapi final entegrasyonda sifirdan kurulmayacak. Feature-first tasimada ayni
contract ve davranis korunacak; yalniz dosya sinirlari hedef mimariye
uyarlanacak.

### Merkezi Hata Yonetiminin Mevcut Durumu

- `ApiErrorParser`, backend `code/message/fieldErrors/timestamp` cevabini tek
  noktada parse eder.
- `AppError`, `AppFieldError`, `AppErrorMessage` ve `ResourceProvider` auth
  ekranlarinda yerellesmis hata sunumuna hazir bir temel saglar.
- `BackendErrorCode` su anda auth ve ortak hata kodlariyla sinirlidir.
- Profil, medya, tur, session, rezervasyon, review, payment, refund, wallet,
  banka hesabi, withdrawal, chat ve notification hata kodlari henuz Android
  enum/mapping'inde yoktur.

Gercek feature entegrasyonunda hata kodlari tek seferde dev bir liste olarak
eklenmeyecek; ilgili dikey dilimde guncel OpenAPI/backend enumundan eklenip
yerellesmis UI sonucuna map edilecektir. Teknik backend mesaji dogrudan
kullaniciya gosterilmeyecektir.

### Navigation ve Composition Root

- Root, auth, guide, tourist, guide-account, tourist-account, guide-tour ve
  tourist-payment hedefleri `@Serializable` type-safe destination kullanir.
- Rehber ve turist kendi NavHost/scaffold/bottom-bar gecmislerini korur.
- Topbar ve bottom bar composable'lari navigation kararini dogrudan vermez;
  config ve callback alir.
- Account ekranlari role root graph'inin disinda full-screen flow olarak
  acilir. Mevcut tasarim ve geri-stack davranisi korunmalidir.
- Payment success sonrasi stack temizleme ve bottom-bar hedefi mevcut graph'ta
  acikca yonetilir.
- Bazi ViewModel'lar `SavedStateHandle.toRoute()` ile navigation destination
  tipini dogrudan bilir. Bu calisan bir yapi olmakla birlikte feature-first
  sinirinda route argument okuma yerinin graph mu feature presentation mi
  olacagi Adim 5'te dosya bazinda kararlastirilacaktir.

Navigation'da final entegrasyonu engelleyen bir runtime/compile hatasi
bulunmamistir. Tasarim veya ekran gorunurlugu bu hazirlik adimlarinda
degistirilmeyecektir.

### MVP Store Otoriteleri

Auth disindaki is alanlari presentation paketlerindeki somut `@Singleton`
store'lara baglidir. Bunlar MVP boyunca ekranlar arasi tek mock kaynak saglamak
icin dogru ve bilincli bir gecis cozumudur; ancak artik backend contract'i
bulundugu icin kalici repository yerine kullanilamaz.

| Mevcut gecici kaynak | Besledigi ana akislar | Finaldeki otorite |
| --- | --- | --- |
| `TourCatalogStore` | yayinlama, duzenleme, rehber turlari, guide/tourist detay, turist home/checkout/trips/profil | tour/profile/reservation repository projection'lari |
| `TouristReservationStore` | geziler, snapshot, iptal, yorum | reservation ve review repository |
| `TouristPaymentStore` | checkout status/success, wallet top-up | payment repository ve hosted checkout status |
| `TouristWalletStore` | turist bakiye, kartlar, hareketler | wallet ve payment-method repository |
| `GuideWalletStore` | banka hesabi, bakiye, withdrawal, hareketler | bank-account, withdrawal, wallet/earning repository |
| `GuidePerformanceStore` | dashboard istatistikleri, seviye, profil | guide dashboard/public-profile repository |
| `ChatStore` | sohbet listesi, mesaj, unread | REST + STOMP tabanli chat repository |
| `GuideProfileSharedStore` | rehber hakkinda, dil ve profil resmi | guide profile/media repository |

Bu siniflar toplu olarak yalniz isim degistirip repository yapilmayacaktir. Her
dikey feature baglandiginda once interface contract, network DTO ve data
implementation gelecek; tum consumer'lari gercek akisa gecen store ve mock data
dosyasi o dilimin sonunda silinecektir.

### Tur ve Ortak Veri Yapisi

Mevcut tur temeli dogru kurulmustur:

- `Tour` kalici tur icerigini, `TourSession` tarih/fiyat/kapasite/lifecycle
  oturumunu ayirir.
- `TourCatalogStore` MVP'de tek ortak kaynak rolundedir.
- Rehber karti, turist popular karti ve ortak detay ayri UI mapper'lariyla ayni
  `TourWithSession` kaynagindan uretilir.
- Turist rezervasyon modeli satin alma ani snapshot'ini korur; sonradan tur
  icerigi/fiyati degisse bile gecmis rezervasyonun temel verisi ayrilabilir.
- Rehber/turist detaylari ayni `TourDetailContent` presentation'ini farkli mode
  ve aksiyonlarla kullanir.
- `TourCatalogState` aktif, satinalinabilir, inceleme ve gecmis listelerini tek
  lifecycle kuraliyla ayirir; biten session icin Android UI guvenlik filtresi
  vardir.

Backend entegrasyonunda bu ortak-veri ilkesi korunur. Ancak mevcut `Tour` ve
`TourSession`, drawable fallback ile backend/domain alanlarini ayni modelde
tasiyan MVP modelleridir. Network DTO, uygulama modeli ve UI modeli sinirlari
feature-first refactor sirasinda ayrilmalidir.

Mevcut model/contract farklari:

- Backend tur/session UUID ve version alanlari Android modellerinde tam degildir.
- `TouristReservationStatus` yalniz `CONFIRMED/CANCELLED` degerlerine sahiptir;
  backend `PENDING_PAYMENT/CONFIRMED/COMPLETED/CANCELLED/EXPIRED` dondurur.
- Payment network contract'i `HOSTED_CARD` kullanirken mevcut UI modeli
  `SAVED_CARD` adini kullanir.
- Payment UI status'u backend'deki `PENDING` ve `REQUIRES_ACTION` durumlarini
  birebir temsil etmez.
- Rehber kartindaki `earningsMinor`, backend'deki nullable session
  `netEarningsMinor` alanina map edilmelidir; cekilebilir bakiye sayilmamalidir.
- Guide level ve dashboard istatistikleri Android'de yeniden hesaplanmamalidir;
  backend projection'i canonical olmalidir.

Network enumlariyla UI enumlarini zorla tek tipe cevirmek yerine dar mapper
siniri kullanilacaktir. UI'ya ozgu secili tab, loading, dialog veya gorunum
durumlari backend enumu olmayacaktir.

### Tur Yayinlama, Duzenleme ve Session Islemleri

- Dort adimli yayinlama formu typed `LocalDate`, `LocalTime`, sure, kategori,
  dil, USD fiyat minor unit'e donusebilir input, kapasite ve local cover URI
  tutar.
- Yayinla aksiyonu su anda Android'de UUID ve hardcoded mock guide uretip
  `TourCatalogStore`a ekler.
- Profil/tur fotografi secimi ortak `ImageSourcePicker` ile galeri/kamera ve 10
  MiB local on-kontrol kullanir.
- Duzenleme kritik degisikligi, kilitli alanlari, reservation durumunu ve review
  hedefini MVP icinde ayirir.
- Session ac/kapat/iptal, yeni session olusturma ve archive aksiyonlari local
  store sonucundan gelir.

Final entegrasyonda:

- Android tur/guide/session kimligi uretmeyecek.
- Local cover URI once media upload'a gidecek; create/change request
  `mediaAssetId` tasiyacak.
- Request version alanlari backend optimistic locking icin korunacak.
- Backend error code ve canonical response snackbar/dialog sonucunu
  belirleyecek.
- Publish formunda secimden sonra `timeZoneId` guncellenmiyor; mock Istanbul
  degeri kalabiliyor. Edit akisi cihaz zone'unu, yeni session akisi tur zone'unu
  kullaniyor. Bu karisik mevcut durum Adim 3'te kabul edilen MVP time-zone
  karariyla tek kurala indirgenecek; yeni LocationResolver katmani otomatik
  varsayilmayacak.

### Tourist Home, Explore ve Public Guide Akisi

- Tourist home popular kartlari ortak tur katalogundan gelir; bu dogru pattern
  korunacaktir.
- `bestGuides` halen sabit dummy listedir; backend `/guides/top` projection'i
  ile degisecektir.
- Explore ViewModel yalniz query/filter draft state'i tutar.
- Tours ve guides sekmelerinde sonuc listesi, loading, empty, error, retry ve
  pagination yoktur.
- Filter ekranindaki `Uygula` callback'i bostur.
- Explore ve Filter destination'lari varsayilan olarak ayri Hilt ViewModel
  instance'i alabildigi icin uygulanmis filter state'i geri aktarilmaz.
- Rehber profil onizleme tasarimi vardir; turistin gercek public guide profile
  akisi ve popular tur/message aksiyonlari henuz bagli degildir. Preview'daki
  bos click'ler rehberin salt-okunur onizlemesinde bilincli olabilir; gercek
  turist profile ekranina kopyalanarak bos birakilamaz.

Arama/filtreleme yalniz repository eklemekle bitmis sayilmayacak. Applied
filter sahipligi, debounced query, paged result state, tour/guide kart
navigasyonu ve empty/error/retry davranisi birlikte tamamlanacaktir.

### Rezervasyon ve Yorum Akisi

- Yaklasan/gecmis geziler `TouristReservationStore` snapshot'i ve ortak tur
  katalogundan mapper ile uretilir.
- Rezervasyon iptali su anda yalniz local status'u `CANCELLED` yapar; kapasite,
  refund ve wallet sonucu degismez.
- Tour payment success mevcut kodda yeni rezervasyon olusturmaz, kapasiteyi
  azaltmaz ve geziler listesini yenilemez.
- Yorum formu puan/yorum validasyonu yapar; local store'a yazar.
- UI seviyesinde tamamlanmis, satin alinmis ve daha once yorumlanmamis tur
  kontrolu bulunur; kalici yetki backend'e aittir.

Final repository, `ReservationResponse` snapshot ve tam lifecycle state'ini
kullanacak. Iptal sonucu refund kimligi/durumuyla birlikte map edilecek;
mutation sonrasi reservation, session capacity, wallet ve ilgili notification
projection'lari yenilenecektir. Basarisiz/degismis state kullaniciya sessizce
yutulmayacaktir.

### Odeme, Kayitli Kart ve Tourist Wallet

Mevcut odeme UI'si gercek entegrasyon icin tasarlanmis fakat davranis mock'tur:

- `TouristPaymentStore` local payment attempt uretir.
- `PaymentStatusScreen` iki adet bes saniyelik delay ile
  `REDIRECTING -> VERIFYING -> SUCCEEDED` yapar.
- Tour checkout local katalogdan capacity, local wallet'tan bakiye ve local
  kart secimi kontrol eder.
- Payment success sonrasinda backend payment/reservation/wallet sonucu
  alinmaz.
- Wallet top-up success bakiyeyi artirmaz ve yeni transaction uretmez.
- Native sandbox kart ekleme ekrani kart numarasi, SKT ve CVV'yi yalniz Compose
  state'inde isler; `SandboxCardCatalog` ile sahte banka/kart metadata'si
  uretir.
- Kayitli kart listesi guvenli metadata modeli kullanir; son dort hane,
  banka/kart ailesi, expiry ve default bilgisi tasir.
- Para formatlama canonical platform USD'yi `Long` minor unit ile yapar. Bu
  tur/wallet parasi icin dogrudur; hosted provider charge currency/tutari icin
  response currency'sini kullanan ayri presentation map'i gerekecektir.

Final akista mevcut tasarim kabugu korunarak:

- Currency options ve quote backend'den alinacak.
- Hosted iyzico URL guvenli WebView'da acilacak.
- Callback JSON'u, URL degisimi veya WebView kapanmasi basari sayilmayacak.
- Payment ID ile backend status polling yapilacak.
- Yalniz `SUCCEEDED + CONFIRMED` tur satin alma basarisi sayilacak.
- Refund veya `MANUAL_REVIEW` canonical sonucuyla gosterilecek.
- Top-up basarisi payment sonucu ve yenilenmis wallet projection'iyla
  dogrulanacak.
- Native kart formu ve `SandboxCardCatalog` kaldirilacak; kayitli kart metadata
  listesi provider-backed endpoint'ten beslenecek.
- Ham kart, CVV, provider token veya secret GuideMate ViewModel/repository/log
  sinirina girmeyecek.

Projede su anda WebView/hosted checkout implementation'i yoktur. JavaScript
bridge eklenmeyecek, SSL hatasi bypass edilmeyecek ve POST callback icin yalniz
`shouldOverrideUrlLoading` kullanilmayacaktir.

### Guide Finance, Earnings ve Wallet

- `GuideWalletStore` mock bakiye, banka hesabi, wallet hareketi ve withdrawal
  uretir.
- Withdrawal local bakiye kontrolu yapip local `PENDING` hareket ekler.
- Available balance local pending withdrawal toplamina gore turetilir.
- IBAN yaziminda `TurkishBankCatalog` hizli banka onizlemesi, validator ise
  format/checksum kontrolu saglar.
- Monthly earnings ay/yil secimi ve `MonthlyEarningUiModel` ile hazirdir; veri
  halen ViewModel icinde mock'tur.

Finalde backend wallet/earning/bank-account/withdrawal otorite olacaktir.
Android IBAN banka adini yalniz onizleyebilir; kesin dogrulama backend'den
gelir. Tam IBAN yalniz hesap ekleme request'inde HTTPS ile gonderilir; listede
maskeli response ve `bankAccountId` kullanilir. Withdrawal sonrasi bakiye ve
ledger hareketi canonical projection'dan yenilenir. Aylik toplam Android'de
earning sayfalari indirilerek hesaplanmaz; monthly endpoint map edilir.

### Chat

Mevcut ortak chat modeli iyi bir MVP contract taslagidir:

- Conversation, participant, message, `chatId`, `clientMessageId`, `senderId`,
  `Instant` ve delivery state ayri tutulur.
- `isFromMe`, sender ile current user karsilastirmasindan mapper'da uretilir.
- Guide ve tourist ayni ortak chat UI/viewmodel yapisini kullanir.
- Liste, detay, bos durum, unread badge ve mesaj uzunlugu 2000 karakter UI'da
  bulunur.

Eksik gercek akis:

- Current user kimligi `UserRepository.userId` yerine role gore mock ID'den
  uretilir.
- Mesaj aninda local `SENT` olur; pending/retry/backend server ID ve server
  zamani uygulanmaz.
- Sohbet listesi/history/read/unread REST'e bagli degildir.
- STOMP client, JWT CONNECT, reconnect ve kacirilan mesaj REST sync'i yoktur.
- Bildirim/deep-link ile `chatId` acma gercek push akisi yoktur.

Final chat repository REST'i kalici kaynak, STOMP'u hizli teslimat kanali
olarak birlestirecek. Tek rehber-turist cifti sohbeti ve rezervasyondan bagimsiz
baslatma karari korunacaktir.

### Notification ve FCM

- Guide topbar unread badge ve bildirim bottom sheet tasarimi vardir.
- Guide notification listesi ve actor/tur/rating/comment/withdrawal alanlari
  mock ViewModel'dan gelir; read aksiyonu kalici degildir.
- Guide ve tourist notification setting ekranlari role ozgu local state tutar.
- Tourist notification settings vardir; turist icin gercek notification
  center/topbar entry akisi mevcut degildir ve urun karari Adim 3'te
  netlestirilmelidir.
- Firebase BOM/Messaging ve Google Services plugin'i eklenmistir.
- `FirebaseMessagingService`, Firebase Installation ID kaydi, runtime
  notification permission akisi, notification channel, foreground/background
  handling ve token refresh registration kodu yoktur.

Finalde backend preference contract'i role UI modellerine map edilecek;
security tercihi salt-okunur kalacak. Notification `actorDisplayName` dogrudan
mevcut `actorName` UI alanina map edilecek ve ek user sorgusu atilmayacak. FCM
yalniz hizli teslimat olacak; bildirim listesi ve unread sayisi REST canonical
projection'dan gelecektir.

### Profil ve Guide Performance

- Tourist profil adi/e-postasi gercek `UserRepository.userState`ten gelir;
  bakiye mock finance store'dandir.
- Guide about, dil ve secilen profil resmi tek shared store ile profil/onizleme
  ekranlarina yansir.
- Guide profile popular turlari ortak tur katalogundan mapper ile uretilir.
- Guide dashboard/performance ve level Android'de mock sayilardan hesaplanir.
- Current guide filtrelemesi hardcoded `MOCK_CURRENT_GUIDE_ID` kullanir.

Finalde `/guides/me/profile`, `/guides/me/dashboard`, public profile, top/search
projection'lari ayri repository metotlariyla map edilecek. Authenticated guide
ID request otoritesi olmayacak. Backend'in level/istatistik sonucu UI'ya
map edilecek; Android calculator yalniz preview/test degeri kalmiyorsa
temizlenecektir.

### Medya ve Image Loading

- Ortak `ImageSourcePicker` kamera/galeri URI'si, cache FileProvider ve local
  10 MiB kontrolu saglar.
- `GuideMateImage`, `content://` ve `file://` URI'leri arka plan thread'inde
  sample ederek acar; drawable fallback'i korur.
- HTTP/HTTPS image URL'si su anda yuklenmez, fallback drawable gosterilir.
- Multipart upload, progress, `mediaAssetId`, remote loading/error ve auth
  header gerektiren draft image destegi yoktur.
- Gradle'da Coil veya baska remote image loader yoktur.

Finalde ortak image component local URI ve remote URL'yi tek noktada yonetecek;
Coil benzeri loader data/network ayrintisini ekranlara sizdirmayacak. Upload
repository backend multipart contract'ini kullanacak. Token URL query'sine
eklenmeyecek ve mevcut fallback/tasarim korunacaktir.

### Selection, Places ve Yerellesme Temeli

- Country/language katalogu platform locale bilgisinden UI secenekleri
  uretir.
- City arama `CitySearchService` interface'i arkasindaki Google Places adapter
  ile calisir; Hilt DI ve debounce vardir.
- Country/city/language bottom sheet'leri ortak presentation olarak guide ve
  tourist tarafinda tekrar kullanilir.
- Places harici bir repository katmani eklemek su anda gereksizdir; mevcut dar
  interface yeterlidir.
- Kullaniciya ait genel UI metinleri resource'tadir. Mock content icindeki
  Turkce baslik/biyografi/banka adlari kalici yerellesme kaynagi degildir ve
  mock store'larla birlikte kalkacaktir.
- Yalniz `values` klasoru bulunur; ek dil resource setleri henuz yoktur. Bu
  final backend baglantisinin teknik blokaji degildir ve Adim 3 urun/erteleme
  kararlarinda ele alinacaktir.

### UI State, Loading, Pagination ve Process Death

- Auth ve city search gercek async loading/error state'e sahiptir.
- Mock store kullanan feature'larin cogu loading, network error, retry, empty
  ve paged append state'i tasimaz.
- Backend listeleri sayfali oldugu halde guide tour, reservation, wallet,
  earning, notification ve chat listeleri mevcutta tum local listeyi tek
  seferde kullanir.
- Selected tab/filter/form gibi presentation state'leri uygun olarak
  ViewModel/Compose state'indedir; fakat mutation idempotency key, pending
  payment ID ve hosted checkout recovery process death icin kalici degildir.

Her dikey entegrasyon kendi gercek ihtiyaci kadar `loading/content/empty/error`
ve pagination state'i ekleyecek. Tek generic `BaseUiState`, global paginator
veya tum ekranlara ayni retry framework'u eklenmeyecektir. Payment/reservation
gibi kritik devam eden islemler SavedStateHandle veya backend lookup ile geri
kazanilacaktir.

### Test ve Kalite Durumu

Mevcut unit testler su alanlari kapsar:

- Currency formatting.
- Tour catalog lifecycle/operation, booking availability ve popular mapper.
- Guide level hesaplama.
- Review availability ve reservation mapper.
- Tourist/guide finance store ve wallet filter state'leri.
- Sandbox card input.
- Turkish IBAN validator ve banka katalogu.
- Bank account form state.

Auth repository/interceptor/authenticator, navigation, ViewModel async state,
Retrofit DTO mapper, FCM, STOMP, media upload/loader, hosted payment, pagination
ve gercek repository testleri henuz yoktur. `ExampleUnitTest` ve
`ExampleInstrumentedTest` placeholder'lari final temizlikte kaldirilmalidir.

2026-08-18 tarihinde Android kokunde su komut birlikte calistirildi:

```text
./gradlew ktfmtCheck testDebugUnitTest compileDebugKotlin
```

Sonuc: `BUILD SUCCESSFUL`. Format, mevcut unit testler, Hilt/KSP ve debug Kotlin
derlemesi basarilidir.

### Dogrulanmis Entegrasyon Bosluklari

Oncelik sirasiyla gercek bosluklar:

1. Auth disindaki feature'larda Retrofit API/repository/data implementation
   yok; somut presentation mock store'lari kalici otorite durumunda.
2. Hosted iyzico WebView, quote/status polling, canonical payment/reservation
   sonucu ve process recovery yok.
3. Tour payment/top-up/iptal/withdrawal local state'i backend bakiye,
   kapasite, refund ve ledger state'ini degistirmiyor.
4. Media upload ve HTTP/HTTPS image loading yok.
5. Chat REST + STOMP ve current authenticated user baglantisi yok.
6. FCM registration/service/channel/permission/deep-link akisi yok.
7. Explore/search/filter sonuclari ve public guide profile turist akisi eksik.
8. Backend feature hata kodlari, sayfalama, loading/empty/error/retry ve
   idempotent mutation sonuclari Android'e map edilmemis.
9. Tour/payment/reservation enumlari guncel backend state machine'iyle tam
   eslesmiyor.
10. Mevcut package yapisi yeni feature repository/data kodlari eklenmeden once
    dosya bazli feature-first plana ihtiyac duyuyor.

Bunlar backend kaynak hatasi olarak kaydedilmez; mevcut Android entegrasyon
isidir. Adim 3 karar mutabakati ve Adim 4 sozlesme matrisi tamamlanmadan kod
yazilmayacaktir.

### Korunacak Temeller

- Tek Gradle modul ve Hilt composition root.
- Type-safe navigation, role bazli NavHost ve tek role scaffold tasarimi.
- Auth repository/API/token/DataStore/Keystore/error siniri.
- Tour/Session ayrimi ve rehber-turist ortak kalici veri fikri.
- Ayrik guide/tourist UI mapper'lari ve ortak tour detail/card presentation'i.
- Rezervasyon snapshot kavrami.
- `Long` minor unit ve canonical USD kurali.
- `chatId`/`clientMessageId`, sender tabanli mesaj yonu ve ortak chat UI.
- Local URI image picker ile drawable fallback davranisi.
- Ortak selection, tab, rating, dialog ve benzer gercek component sinirlari.
- Backend otoritesi ve kullaniciya yerellesmis gorunur hata ilkesi.

### Adim 2 Sonucu ve Kapisi

Android projesi derlenen, mevcut unit testleri gecen ve MVP UI akislarini ortak
mock kaynaklarla gosteren saglam bir presentation temelidir. Auth disindaki
feature'lar henuz gercek backend entegrasyonu degildir; bu bilincli gecis hali
artik kalici mimari olarak korunmayacaktir.

Bu adimda entegrasyonu engelleyen beklenmedik compile veya mevcut test hatasi
bulunmamistir. Gercek ve planlanmis bosluklar yukarida eksiksiz envantere
alinmistir. Bir sonraki calisma Adim 3'tur: tum anayasa, ertelenen Android isi
ve en son kullanici kararlarini bu guncel backend/Android gercegiyle
uzlastirmak.

## Adim 3 - Anayasa ve Ertelenen Is Mutabakati

Durum: TAMAMLANDI.

### Denetim Kapsami ve Kaynak Otoritesi

Bu adimda su karar kaynaklari birlikte uzlastirildi:

- `odeme anayasasi`
- `ortak veri anayasasi`
- `bildirim yasasi`
- `tur yayinlama anayasasi`
- `tur satin alma anayasasi`
- `mesaj anayasasi`
- Auth backend devir kararlari.
- Iptal/iade, yorum/puan/rehber performansi, medya, sahiplik, gorunur islem
  sonucu ve yerel ag kararlarinin ilgili anayasalara eklenen uzantilari.
- Guncel `backend-implementation-handoff.md` ve `BACKEND_RULES.md`.
- Adim 1'de dogrulanan backend kaynak/Flyway/test/OpenAPI gercegi.
- Adim 2'de dogrulanan Android kaynak kodu ve mevcut MVP davranisi.

Alti isimli anayasa aktif ve kalici karar kumesidir. Iptal/iade,
yorum/performans, medya ve hata/UX icin ayri ve rakip anayasalar
olusturulmamistir; bunlar ilgili ana anayasanin tamamlayici kurallaridir.
Eski bir sohbet notu guncel backend sozlesmesiyle celisiyorsa Adim 1'de
dogrulanan kaynak ve bu adimda yazilan son karar esas alinir.

Bu denetimde Android veya backend kaynak kodu degistirilmedi. Yalniz bu final
entegrasyon belgesi guncellendi. Calisma agacinda daha once var olan
`docs/backend-implementation-handoff.md` degisikligine dokunulmadi.

### Aktif Anayasa Kumesi

| Anayasa | Kalici sorumluluk | Guncel durum |
| --- | --- | --- |
| Odeme | Hosted kart, wallet, kazanc, banka hesabi, cekim, iade ve guvenlik siniri | Backend tamam; Android gercek entegrasyonu bekliyor |
| Ortak veri | Rehber ve turistin ayni kalici tur/oturum kaynagindan farkli mapper'larla beslenmesi | Backend tamam; Android mock otoriteler kaldirilacak |
| Bildirim | Kalici history/unread, FCM teslimati, tercihler, semantic hedef ve read davranisi | Backend tamam; Android FID/FCM/repository/navigation bekliyor |
| Tur yayinlama | Tour/TourSession lifecycle, admin incelemesi, duzenleme, medya, aktif/inceleme/gecmis | Backend tamam; Android API/repository/mutation baglantisi bekliyor |
| Tur satin alma | Gorunurluk, kapasite, hold, rezervasyon snapshot'i, iptal/iade, yorum ve performans | Backend tamam; Android canonical akis bekliyor |
| Mesaj | Rehber-turist cifti basina tek sohbet, REST history, STOMP realtime, unread ve FCM | Backend tamam; Android REST/STOMP/FCM entegrasyonu bekliyor |

### Odeme Anayasasi - Kesin Son Karar

Guvenlik ve otorite:

- Ham kart numarasi, kart sahibi, SKT ve CVV GuideMate Android'e veya Spring
  Boot'a girmez; kaydedilmez, loglanmaz ve DTO'larda tasinmaz.
- Kart ekleme ve kartli odeme iyzico hosted Checkout Form uzerinden yapilir.
  GuideMate mevcut tasarim kabugunu, aciklama ve sonuc ekranlarini korur; ham
  kart giris alanlari provider ekranina tasinir.
- Android yalniz backend'in dondurdugu internal payment/saved method kimlikleri
  ile banka, kart ailesi, Visa/Mastercard ve son dort hane gibi maskeli
  metadata'yi gosterir. Provider token'i backend sinirinda kalir.
- Callback sayfasi veya WebView kapanmasi basari kaniti degildir. Backend
  callback/webhook ve iyzico retrieve sonucunu dogrular; Android internal
  `paymentId` ile canonical durumu sorgular.
- Android payment, bakiye, fiyat, kur, kontenjan, rezervasyon, kazanc, iade veya
  cekim sonucunu yerel state mutasyonu ile kesinlestirmez.
- Secret, API key, callback credential ve provider anahtarlari yalniz backend
  environment/secret yapisinda kalir. Android veya Git'e yazilmaz.

Para ve tahsilat:

- Platformun canonical parasi USD'dir. Tur fiyati, wallet, ledger, kazanc ve
  withdrawal degerleri `Long` minor unit olarak tasinir.
- Eski "tum kart tahsilati yalniz USD, kur donusumu yok" karari guncel backend
  sozlesmesiyle genisletilmistir. Hosted kart tahsilatinda backend'in etkin
  `USD/TRY/EUR/GBP` alt kumesi, sureli FX quote'u ve charge snapshot'i
  kullanilir.
- Android kur hesaplamaz. Bolge/dil yalniz varsayilan tahsilat para birimi
  onerisi olabilir; kesin secenek, kur, charge tutari ve quote expiry backend
  response'undan gelir.
- Locale para birimi otoritesi degildir; yalniz sayi/metin formatini etkiler.
  Android canonical veya charge currency code'u backend response'undan okur.
- `Double` para otoritesi olarak kullanilmaz. API/domain sinirinda minor unit
  `Long`, gerekli provider donusumunde backend `BigDecimal` kullanir.

Islem akislari:

- Wallet top-up yalniz verified payment sonucundan sonra backend ledger credit
  ile basarili olur; Android bakiyeyi kendisi artirmaz.
- Tur satin alma kart veya wallet ile yapilsa da payment, reservation ve refund
  acisindan ayni canonical response sozlesmesini kullanir.
- Kartla tur aliminda `SUCCEEDED` payment tek basina yeterli degildir;
  reservation da `CONFIRMED` olmalidir.
- Gec odeme veya kapasite kaybi sonucunda reservation olusmazsa backend tek ve
  idempotent iade akisini baslatir. Android `REQUESTED/PROCESSING`, `FAILED`
  veya `MANUAL_REVIEW` sonucunu acikca gosterir.
- Android ayni odeme niyeti icin tek idempotency key uretir. Recomposition,
  retry veya process recreation yeni payment olusturmaz; devam eden payment
  yeniden acilir veya canonical status'u sorgulanir.
- Hosted WebView'da POST callback nedeniyle yalniz
  `shouldOverrideUrlLoading` kullanilmaz. WebView lifecycle sonrasinda backend
  polling yapilir; SSL hatasi atlanmaz ve `addJavascriptInterface` eklenmez.
  Checkout Form gerektiriyorsa normal JavaScript destegi acilabilir.
- Provider locale'i Android'in secili uygulama dilinden `tr` veya `en` olarak
  backend initialize istegine aktarilir; hardcoded provider dili kullanilmaz.

Kazanc, banka hesabi ve cekim:

- Guide earning yalniz `PENDING`, `AVAILABLE`, `REVERSED` durumlarini kullanir.
  `WITHDRAWN` earning durumu yoktur; cekim wallet/ledger ve withdrawal kaydiyla
  yonetilir.
- Rehber toplam/cekilebilir bakiyeyi tek tek kazanc satirlarindan Android'de
  hesaplamaz. Backend wallet projection'i canonical kaynaktir.
- Android normalize edilmis IBAN'i banka hesabi olustururken bir kez gonderir.
  Backend format/MOD97/banka kodunu yeniden dogrular ve Android'e
  `bankAccountId`, banka adi ve maskeli IBAN dondurur.
- `TurkishIbanValidator` ve `TurkishBankCatalog` hizli format/banka onizleme UX'i
  icin Android'de kalabilir; para transferi otoritesi degildir.
- Cekimde Android yalniz `bankAccountId`, amount minor ve idempotency bilgisini
  gonderir. Tam IBAN her cekimde tasinmaz.
- Iyzico Marketplace/Mass Payout yetkisi olmadigi icin rehber payout modu
  acikca `SIMULATED` kalir. Bu durum gercek Sandbox tahsilat/iade akisiyla
  karistirilmaz ve sessiz fallback sayilmaz.

Android'de kaldirilacak gecici odeme yapilari:

- Ham kart alanli native kart ekleme davranisi.
- `SandboxCardCatalog` ve kart numarasindan Android tarafinda banka/kart
  otoritesi ureten kod.
- Mock saniye gecisleriyle otomatik basari.
- Local bakiye artirma/azaltma, local kontenjan kesinlestirme ve local refund
  basarisi.
- Gercek repository baglandiktan sonra `TouristPaymentStore`,
  `TouristWalletStore` ve `GuideWalletStore` kalici kaynak olarak kalmaz.

### Ortak Veri Anayasasi - Kesin Son Karar

- Backend `Tour` kalici deneyim kimligi, `TourSession` tarihli/satin alinabilir
  oturum kimligidir. Android bu iki kavrami tek bir genis UI modeline
  eritmeyecektir.
- Rehberin `Turlarim`, turistin popular/search/detail kartlari ve rehber profil
  turlari ayni backend tur/oturum kaynagindan gelir.
- Ayni domain verisinin rehber ve turist icin iki ayri kalici store kopyasi
  tutulmaz. Farkli kart gorunumleri role-specific mapper/UI model ile uretilir.
- Ortaklastirma "ayni Composable'i zorla kullanmak" degildir. Ayni business
  verisi ve davranisi ortak feature/domain kaynaginda; role ozel aksiyon,
  metin ve layout kendi presentation sinirinda kalir.
- Tur detayi, rezervasyon snapshot'i ve public rehber projection'i backend
  kimlikleriyle cozulur. Route'a tam nesne, ad, fotograf veya para bilgisi
  tasinmaz.
- `GuidePerformanceSummary` backend projection'i rehber ana sayfasi, profil,
  public rehber ozeti ve seviye gorunumunu besler. Android ortalama puan,
  tamamlanan tur, katilimci veya seviye girdilerini ekran bazinda yeniden
  hesaplamaz.
- Tur puani/yorum sayisi kalici `Tour` seviyesindeki gecerli yorumlardan gelir;
  session kazanci session projection'idir. Cekilebilir bakiye ile
  `netEarningsMinor` ayni kavram degildir.
- `TourCatalogStore`, `GuideProfileSharedStore`, `GuidePerformanceStore` ve
  `TouristReservationStore` yalniz MVP gecis kaynaklaridir. Ilgili gercek
  repository akisi baglandiginda kalici otorite rolleri ve bosa dusen mock
  mapper/veriler silinir.
- Shared repository, mapper veya component yalniz gercek ortak davranis varsa
  kurulur. Sirf iki ekranda benzer alan var diye farkli lifecycle'lar tek
  soyutlamaya zorlanmaz.

### Bildirim Yasasi - Kesin Son Karar

- PostgreSQL bildirim history, unread ve preference state'inin kalici
  kaynagidir. FCM teslimat kanalidir; bildirim kaydinin yerine gecmez.
- Guide topbar badge, notification panel ve recent activity ayni notification
  repository akisindan beslenir. Wallet hareketleri ayri finansal read
  modelidir; ayni domain olayi iki projection uretebilir.
- Paneli acmak tum bildirimleri otomatik okundu yapmaz. Tek bildirime tiklamak
  yalniz onu okur; ayri `Tumunu okundu isaretle` aksiyonu backend endpoint'ini
  kullanir.
- Backend Android route adi gondermez. Notification `type` ve `tourId`,
  `sessionId`, `reservationId`, `reviewId`, `chatId`, `transactionId` gibi
  semantic kimlikler tasir; Android bunlari type-safe navigation hedefine map
  eder.
- Kullanici kaynakli bildirim adi `actorDisplayName` alanindan gelir; sistem
  bildiriminde alan `null` olabilir. Android aktor adini bulmak icin ek user
  sorgusu atmaz.
- Turla iliskili wallet notification/activity basligi backend
  `referenceTitle` snapshot'ini kullanir. Genel islem metni ve islem tipi
  Android string resource'larindan yerellestirilir.
- Rehber ve turist notification preference ekranlari ayni backend preference
  sozlesmesinden role-specific UI modeline map edilir. Preference kapatmak
  in-app history/unread veya chat mesajini silmez; yalniz ilgili push/reminder
  teslimatini durdurur.
- Guvenlik bildirimi tercihleri UI'da gorunur fakat degistirilemez; PATCH
  request'ine eklenmez.
- Android Firebase Installation ID ile GuideMate installation ID'yi birlikte
  backend'e kaydeder. Logout/account switch cihazi silmez; ilgili kullanici
  kaydini pasiflestirir. Yeni login canonical kaydi yeniden etkinlestirir.
- Android 13+ runtime permission, notification channel ve
  `FirebaseMessagingService` final entegrasyonda yazilir.
- WorkManager push bildirim mekanizmasi degildir. Server reminder'lari backend
  scheduler + FCM ile gelir; WorkManager yalniz gerekirse retry/sync icin
  kullanilir.
- Guide icin mevcut notification panel tasarimi korunur ve gercek veriye
  baglanir. Turist icin yeni bir notification center ekrani bu adimda sessizce
  icat edilmez; mevcut product entry bulunursa ayni ortak repository/panel
  davranisi kullanilir, bulunmazsa FCM semantic hedef ve preference akisi
  korunur. Bu UI kapsam noktasi Adim 4 matrisinde acikca isaretlenecektir.

### Tur Yayinlama Anayasasi - Kesin Son Karar

- `Tour` baslik, aciklama, kategori, konum, dil, medya, rehber, puan ve yorum
  gibi tekrar kullanilan kalici deneyim bilgisidir.
- `TourSession` baslangic zamani, sure, fiyat, kapasite, dolu kisi sayisi ve
  `OPEN_FOR_BOOKING`, `CLOSED`, `COMPLETED`, `CANCELLED` lifecycle'ini tasir.
- Icerik inceleme durumlari `PENDING_REVIEW`, `APPROVED`, `REJECTED` ve archive
  siniriyla backend'de yonetilir. Admin onayi guide/tourist Android rolune
  eklenmez; backend admin endpoint'i ayri yetkilidir.
- Rehber sekmeleri `Aktif`, `Incelemede`, `Gecmis` olarak korunur. Siniflandirma
  local mock liste boyundan degil backend canonical state/projection'larindan
  gelir.
- Switch yalniz gelecekteki onayli session'i yeni rezervasyonlara acar veya
  kapatir. `CLOSED` session gecmise tasinmaz, mevcut rezervasyonlari iptal
  etmez ve `COMPLETED` yerine kullanilmaz.
- Turist yalniz onayli, gelecekteki, `OPEN_FOR_BOOKING` ve bos kapasiteli
  session'i satin alinabilir gorur. Yeni bir session acilinca ayni `Tour`
  puan/yorum gecmisiyle tekrar kesfe acilir.
- Ulke/sehir kimligi veya temel rota gibi Tour kimligini degistiren kritik
  degisiklik eski puanlari yeni deneyime tasimaz; gerekli durumda yeni `Tour`
  olusturulur.
- Kritik icerik degisikligi pending revision/change request ile yeniden admin
  incelemesine gider; son onayli veri red halinde bozulmaz.
- Rezervasyonsuz gelecekteki session icin izin verilen tarih/saat/sure/meeting
  point degisikligi backend kurallariyla yapilir. Rezervasyonlu session sessizce
  degistirilmez; iptal/iade/bildirim ve yeni session akisi gerekir.
- `tourId`, `sessionId`, `guideId`, puan, yorum, kazanc, rezervasyon sayisi,
  approval ve cancellation history formdan degistirilemez.
- Kategori final submit'te stabil kategori koduyla zorunludur. MVP'de adim
  gecisini acmak icin gevsetilen validation kalici degildir.
- Kamera/galeri secimi local `content://` URI'dir. Publish/edit once multipart
  upload yapar, backend `READY mediaAssetId/imageUrl` dondurur; sonra tur
  mutation'i durable `mediaAssetId` ile gonderilir. Upload basarisizsa publish
  veya edit basarili gosterilmez.
- Tek avatar ve tek tour cover mevcut kapsamdir; coklu galeri eklenmez.
- Android konumla birlikte `ZoneId.systemDefault().id` gonderir. Backend IANA
  formatini dogrular, saklar ve canonical `timeZoneId` dondurur. Portfolio/LAN
  kapsaminda Google Time Zone API, koordinat veya server LocationResolver
  eklenmez.
- Yalniz hic yayinlanmamis ve history baglantisi olmayan draft fiziksel
  silinebilir. Yayinlanmis Tour/Session rezervasyon, yorum, odeme ve kazanc
  history'sini korur; close/cancel/archive kullanilir.
- Mutation `Boolean` veya sessiz basarisizlikla kapanmaz. Backend'in typed
  sonuc/hata kodu canonical state korunarak localized ve gorunur UX'e map
  edilir.

### Tur Satin Alma Anayasasi - Kesin Son Karar

- Satinalma uygunlugu backend'de onay, gelecek tarih, session status, kapasite,
  mevcut rezervasyon ve ownership kurallariyla yeniden kontrol edilir. Android
  hizli on kontrol yapabilir fakat otorite degildir.
- `capacity` toplam kapasitedir; azaltilan alan degildir. Dolu kisi sayisi
  `bookedCount`, kalan kapasite `capacity - bookedCount` olarak turetilir.
- Ayni son koltuk icin eszamanli iki talepte backend lock/transaction ile yalniz
  uygun talebi kabul eder. Android stale ekranda buton gosterse bile canonical
  `CAPACITY_NOT_AVAILABLE` sonucunu acikca gosterir.
- Kartli odeme oncesi katilimci sayisi icin gecici hold/reservation olusur.
  Basarisiz/iptal/timeout odemede hold serbest birakilir. Gec verified payment
  kapasiteyi asamaz; yer yoksa tek iade akisi baslar.
- Wallet satin aliminda payment `SUCCEEDED`, reservation `CONFIRMED` ve ledger
  debit ayni backend transaction'inda olusur.
- Reservation, satin alma anindaki tur basligi, tarih/saat, bulusma noktasi,
  fiyat, para birimi, participant count ve iptal politikasi gibi gerekli
  snapshot'i korur. Sonraki tur edit'i satin alinmis gecmisi degistirmez.
- Rehber session iptali etkilenen turistler icin tam iade akisina gider.
  Turist yalniz kendi reservation'ini iptal eder; session'i iptal etmez.
- Turist iptal politikasi backend'de satin alma anindaki policy snapshot/version
  ile uygulanir. Kesin MVP kuralinda baslangica 48 saat veya daha fazla varsa
  tam iade, 48 saatten azsa iadesiz iptal uygulanir.
- `CANCELLED` reservation, refund'un basarili oldugu anlamina gelmez. Refund
  `REQUESTED`, `PROCESSING`, `SUCCEEDED`, `FAILED`, `MANUAL_REVIEW` gibi ayri
  durumlarla gosterilir.
- Iptal; koltuk serbest birakma, session availability, payment/refund,
  guide earning reversal, wallet/ledger ve notification sonucuyla tutarli
  kalir. Android bunlari ayri local store'larda optimistic olarak uydurmaz.
- Gecmis kart/detay tamamlanan ve iptal edilen rezervasyonu ayirir; iptal nedeni
  ve refund durumu backend response'undan gelir.
- Turist yalniz tamamlanmis, kendisine ait, iptal/iade edilmemis reservation
  icin bir kez 1-5 puan ve opsiyonel yorum gonderebilir.
- Tour average/count gecerli yorumlardan; guide average tum gecerli tekil
  yorumlardan hesaplanir. Android tur ortalamalarinin ortalamasini almaz.
- `GuidePerformanceSummary` tamamlanan tur, katilimci, yorum/puan ve seviye
  girdilerinin tek backend projection'idir.
- Popular siralama ham ortalama puan degildir. Backend weighted rating ve
  deterministic tie-break kuraliyla yalniz satin alinabilir adaylari siralar.
- Backend sync gecikirse Android `endsAt` gecmis session'i satin alinabilir/aktif
  gostermez; ancak kalici `COMPLETED` lifecycle otoritesi backend'dir.

### Mesaj Anayasasi - Kesin Son Karar

- Ayni guide-tourist cifti icin rezervasyon ve turdan bagimsiz tek kalici
  sohbet vardir. Kullanici turu satin almadan rehbere soru sorabilir.
- Sohbet tamamlanan veya iptal edilen turdan sonra history olarak acik kalir.
  Her rezervasyon icin yeni sohbet olusturulmaz.
- Backend-generated UUID `chatId` tek route kimligidir. Route'a kullanici adi,
  fotograf, role, `remoteUserId`, `tourId` veya `reservationId` eklenmez.
- Chat list/detail/topbar/unread ayni repository kaynagindan beslenir. Rehber ve
  turist ayni chat domain/data/presentation davranisini paylasir; role graph'i
  yalniz navigation/scaffold baglantisini yapar.
- Mesaj kaynagi en az `messageId`, `chatId`, `senderId`, `body`, `sentAt`,
  `clientMessageId` ve delivery state tasir. `isFromMe`, authenticated
  `currentUserId` ile `senderId` karsilastirmasindan mapper'da turetilir.
- Android'deki sabit role ve `mockCurrentUserId` bridge'i gercek repository
  baglandiginda kaldirilir. `UserState.userId/role` viewer identity olur.
- REST conversation list/history, find-or-create, send fallback, read ve unread
  akislarini tasir. STOMP yalniz private realtime teslimat icindir; kacirilan
  mesajlar reconnect sonrasinda REST resync ile tamamlanir.
- Mesaj UI'da `PENDING`, `SENT`, `FAILED/retry` davranisi korunur.
  `clientMessageId` ayni mesaji iki kez olusturmayi engeller.
- Bos mesaj ve 2.000 karakter ustu mesaj Android'de hizli engellenir; backend
  `CHAT_MESSAGE_TOO_LONG` ve field validation sonucuyla otorite olmaya devam
  eder.
- Sohbet acikken read state backend'e gonderilir; bottom-bar badge ayri manuel
  sayac degil backend unread projection'idir.
- Uygulama aktifken STOMP, pasifken semantic `chatId` tasiyan FCM kullanilir.
  WorkManager mesaj veya push teslimat mekanizmasi degildir.

### Tamamlayici Backend-Sonrasi Kararlar

Auth ve kullanici kimligi:

- Auth Android'de gercek repository/API ile tamamlanmis tek feature'dir;
  yeniden yazilmayacak, mevcut `AuthRepository`, `UserRepository`,
  `OnboardingRepository`, DataStore, Android Keystore, interceptor,
  authenticator ve root navigation sinirlari korunacaktir.
- ADMIN Android rolune geri eklenmeyecek. Admin tour approval backend admin
  endpoint/Swagger/Postman gibi ayri yetkili yuzeyden yapilabilir.
- Google login yalniz onceden kayitli GuideMate hesabina giris yapar; otomatik
  Google registration eklenmez.
- Authenticated `userId`, email ve role profile/chat/tour/notification
  repository'lerine canonical viewer context saglar. Request body'sindeki
  guide/tourist kimligi yetki kaniti sayilmaz.
- FCM register/logout entegrasyonu mevcut installation ID lifecycle'ini
  kullanir; ikinci bir cihaz kimligi sistemi kurulmaz.

Medya ve image loading:

- `GuideMateImage`, local URI/drawable fallback ile backend HTTP/HTTPS URL'sini
  tek presentation sinirinda destekleyecek sekilde Coil Compose'a baglanir.
- Ekranlar ayri remote image loader yazmaz. Loading/error mevcut olculeri ve
  drawable fallback'i bozmaz.
- Public medya normal URL ile; owner-only draft medya gerekiyorsa ortak loader
  auth header ile yuklenir. Token URL query parametresine eklenmez.
- Backend `imageUrl` kalici gosteren degerdir; local filesystem path/storage key
  UI'ya sizmaz.

Hata ve kullanici deneyimi:

- Tum yeni repository'ler ortak `DataResult/AppError` ve tek backend error
  parser sinirini kullanir. Ayni JSON hata ayrisma kodu feature'larda
  tekrarlanmaz.
- Guncel backend `ErrorCode` isimleri esas alinir; eski metin veya eski enum
  varsayimi kullanilmaz.
- Field error mevcut `supportingText` alanina; genel islem hatasi uygulamanin
  mevcut Toast/Dialog one-shot davranisina gider. Yeni paralel Snackbar hata
  sistemi kurulmaz.
- Loading, empty, pagination, retry, timeout, double-submit, cancellation,
  refund ve manual-review durumlari UI state'te ayri temsil edilir.
- Backend mutation hatasinda local optimistic state canonical sonucu ezmez.
  Kullaniciya ne oldugu ve yeniden deneyip deneyemeyecegi localized metinle
  gosterilir.

Yerel ortam ve dis servisler:

- Android base URL ve backend public/callback URL local, Git disi
  configuration'dir. `localhost`, emulator IP'si, LAN IP'si veya Quick Tunnel
  production source'a hardcode edilmez.
- Ayni Wi-Fi'daki coklu cihaz testi Mac'in guncel LAN IP'si ile yapilir. IP
  degisince local configuration iki tarafta yenilenir.
- Quick Tunnel kalici domain degildir; odeme E2E oncesi yeniden acilir ve
  backend/iyzico panelindeki callback URL guncellenir. Tunnel URL Android'e
  yazilmaz.
- SMTP ve Firebase credentials local secret'tir; eksikse ilgili E2E testinin
  on kosulu olarak acikca bildirilir.

### Onceki Kararlardan Guncellenen veya Gecersiz Kalanlar

| Eski veya belirsiz karar | Kesin son karar |
| --- | --- |
| Telefon locale'i paranin cinsini belirler | Locale yalniz format/varsayilan oneridir; canonical USD ve charge currency backend'den gelir |
| Kart tahsilati her durumda yalniz USD, FX yok | Canonical muhasebe USD; hosted charge backend quote ile etkin USD/TRY/EUR/GBP olabilir |
| GuideMate native kart formundan iyzico'ya kart verisi yollar | Ham kart hosted iyzico formuna girilir; Android/backend ham kart verisini gormez |
| Sandbox kart numarasindan Android banka/Visa tespiti kalici olabilir | `SandboxCardCatalog` final entegrasyonda silinir; provider metadata kullanilir |
| Payment callback/WebView sayfasi basari demektir | Basari yalniz backend retrieve ile dogrulanmis canonical payment ve gerekiyorsa reservation sonucudur |
| Guide earning `WITHDRAWN` olur | Earning `PENDING/AVAILABLE/REVERSED`; cekim withdrawal + wallet ledger ile yonetilir |
| Cekim otomatik uc is gununde bankaya gider | Kullanici manuel withdrawal olusturur; portfolio backend payout modu acikca `SIMULATED`dir |
| `CANCELLED` kaydi iadenin tamamlandigini kanitlar | Cancellation ve refund ayri lifecycle'lardir; UI refund durumunu ayri gosterir |
| Turist rezervasyon yapmadan rehbere yazamaz | Sohbet rezervasyondan bagimsizdir; guide-tourist cifti basina tek conversation vardir |
| Her tur/rezervasyon icin yeni sohbet | Ayni iki kullanici icin tek kalici `chatId` korunur |
| WorkManager push bildirim getirir | FCM push kanalidir; WorkManager yalniz retry/sync icin degerlendirilir |
| Bildirime tiklaninca backend Android route adi yollar | Backend semantic type/id yollar; Android type-safe route'a map eder |
| Notification panel acilinca tumu okundu olur | Panel acilmasi read mutation yapmaz; tekli read ve ayri read-all kullanilir |
| `CLOSED` tur gecmise gider veya iptal olur | `CLOSED` yalniz yeni booking'e kapalidir; history/completion/cancellation ayri state'tir |
| Session kapasitesi her alista azalir | `capacity` toplam, `bookedCount` dolu kisi, kalan ikisinin farkidir |
| Android tur bitis saatini kalici olarak tamamlar | Backend lifecycle otoritesidir; Android yalniz stale response'a karsi gecmis zamani satin alinabilir gostermez |
| Sehir icin Google Time Zone API/LocationResolver gerekir | Portfolio MVP'de cihaz IANA ZoneId gonderir; backend dogrular ve saklar |
| Rehber/turist icin ayri kalici tour/chat/payment store'lari | Tek backend source + uygun mapper/repository; role ozel presentation korunur |
| Her repository icin iki satirlik use-case zorunludur | Mevcut pass-through auth use-case'leri refactor'da kaldirilir; ViewModel repository arayuzunu dogrudan kullanir. Yalniz gercek orchestration ihtiyaci final denetimde use-case gerekcesi olabilir |
| Provider payout yoksa sessizce gercek gibi davranilir | Payout modu acikca `SIMULATED`; gercek transfer iddiasi yoktur |

### Adim 4'e Aktarilan Zorunlu Android Isleri

Adim 4 endpoint/DTO/domain/UI matrisinde su alanlar eksiksiz eslenecektir:

1. Authenticated current-user identity'nin profil, tur, mesaj ve bildirim
   repository'lerine aktarilmasi; hardcoded kullanici/rol kimliklerinin
   kaldirilmasi.
2. Media upload/status/delete ve Coil-backed remote image akisi.
3. Guide profile, public guide, guide performance, level ve guide language
   mapping'i.
4. Tour publish/edit/change-review/session lifecycle, guide list/detail,
   dashboard ve monthly earnings.
5. Tourist tour/guide search, applied filters, pagination, popular ranking ve
   public detail/profile.
6. Reservation hold/booking/trips/detail/cancellation/refund/review ve
   availability refresh.
7. Payment currency-options/quote/initialize/WebView/status recovery, wallet,
   saved methods, bank account, withdrawal, transaction ve earning history.
8. Notification history/preferences/read/unread/FID/FCM/semantic navigation ve
   recent-activity projection'i.
9. Chat list/detail/find-or-create/send/read/unread REST, STOMP lifecycle,
   reconnect resync ve FCM chat target'i.
10. Backend error code/field error/localized UX, loading/empty/retry,
    idempotency ve canonical mutation refresh davranisi.
11. Gercek repository baglandikca ilgili mock store, demo kimlik, sandbox kart
    algilama, gecici mapper ve bosa dusen kaynaklarin kontrollu temizligi.

Bu liste endpoint veya dosya tasima plani degildir. Somut API metodu, DTO,
repository ve mapper sahipligi Adim 4'te; feature-first hedef dosya agaci Adim
5'te; uygulama sirasi Adim 6'da kesinlestirilecektir.

### Bilincli Kapsam Sinirlari

- Tek Gradle `app` modulu korunur; multi-module gecisi bu entegrasyonun hedefi
  degildir.
- Backend modular monolith olarak kalir; microservice, Kafka, Redis, genel
  outbox veya cache varsayilan olarak eklenmez.
- Server-side Google Places Details/Time Zone API ve koordinat tabanli timezone
  cozumlemesi eklenmez.
- Coklu tur fotograf galerisi eklenmez; avatar ve cover mevcut kapsamdir.
- Gercek iyzico guide payout zorunlu degildir; `SIMULATED` portfolio karari
  acikca korunur.
- Android'de ham kart, provider secret, banka transfer otoritesi veya para
  mutasyonu olusturulmaz.
- Sirf katman gorunsun diye use-case, manager, base class, generic repository
  veya bos interface eklenmez.
- Bu adim tasarim, navigation davranisi, package yapisi veya runtime akis
  degisikligi yapmaz.

### Adim 3 Sonucu ve Kapisi

Alti ana anayasa ile bunlarin backend-sonrasi uzantilari guncel backend ve
Android gercegiyle uzlastirilmistir. Para birimi, hosted kart, chat yetkisi,
timezone, notification read/routing, cancellation/refund ve payout konularinda
eski notlarla guncel sozlesme arasindaki farklar acikca sonuca baglanmistir.

Backend tarafinda bu kararlar icin yeni bir tablo veya migration boslugu
bulunmamistir. Android tarafinda yapilacaklar yukaridaki zorunlu is listesine
aktarilmistir. Bir sonraki calisma Adim 4'tur: her ekran ve aksiyonun gercek
endpoint, DTO, repository, mapper, hata ve canonical refresh sozlesmesini
dosya bazinda eslemek.

## Adim 4 - Sozlesme Esleme ve Bosluk Matrisi

Durum: TAMAMLANDI.

### Denetim Kapsami

2026-08-18 tarihinde Adim 1'de dogrulanan backend endpoint/DTO sozlesmeleri ile
Adim 2'deki Android ekran, ViewModel, store ve navigation yapisi yeniden
eslendi. Adim 3'te kesinlestirilen anayasalar bu eslemenin davranis siniri
olarak kullanildi.

Bu adimda Android veya backend kaynak kodu degistirilmedi. Asagidaki repository
adlari mantiksal sahipligi belirtir; kesin paket ve dosya yollari Adim 5'te
feature-first hedef agac ile belirlenecektir.

### Katman ve Mapping Kurali

Her gercek feature akisi su yonde kurulacaktir:

`API DTO -> data mapper -> domain model/result -> UI mapper -> screen UiState`

- Retrofit request/response DTO'lari presentation paketine sizmaz.
- Backend enum metinleri kullaniciya dogrudan yazdirilmaz; typed enum/domain
  state'e map edilir ve gorunen metin Android string resource'undan gelir.
- UUID alanlari Android'de `String` olarak tasinabilir; UI bunlari uretmez veya
  yorumlamaz. Yalniz `clientMessageId` ve `Idempotency-Key` gibi istemci
  kimlikleri UUID olarak Android'de uretilir.
- Para `Long` minor unit + `currencyCode` olarak korunur. `Double`, locale ile
  parse edilmis kalici deger veya Android tarafinda kur donusumu kullanilmaz.
- Zaman `Instant`/ISO-8601 olarak data/domain katmaninda tutulur. Retrofit Gson
  icin tek merkezi `Instant` adapter kullanilir; ekran mapper'i locale'e gore
  formatlar.
- `PageResponse<T>` icin tek generic network DTO kullanilir. Her feature kendi
  sayfalama modelini yeniden yazmaz.
- Ortak backend `ErrorResponse` ve `fieldErrors` mevcut merkezi API hata
  parser'ina girer. ViewModel ham HTTP kodu veya backend mesaj metni yorumlamaz.
- Ekran UiState'i loading/empty/content/error ve gerekiyorsa refreshing veya
  append-loading durumunu acikca tasir. Sirf ortak gorunsun diye generic base
  ViewModel/UiState olusturulmaz.
- Yeni iki satirlik use-case eklenmez. Repository birden fazla islemi anlamli
  bir is kuralinda orkestre etmedikce ViewModel repository'yi dogrudan kullanir.

### Repository Sozlesmesi Matrisi

Bu interface'ler yalniz gercek endpoint ve gercek tuketiciyle birlikte
olusturulacaktir. Bos veya gelecege donuk repository acilmayacaktir.

| Mantiksal repository | Backend sahipligi | Android tuketicileri | Degisecek MVP kaynagi |
| --- | --- | --- | --- |
| Mevcut `AuthRepository` | `/api/v1/auth/**` | Auth ekranlari, root session | Mevcut gercek yapi korunur |
| Mevcut `UserRepository` | `/auth/me` ve auth response | Root, profil basliklari, kimlik/rol | Hardcoded kullanici ve rol |
| `MediaRepository` | `/api/v1/media` | Guide avatar, publish/edit cover | Local URI'yi kalici URL sanan akislari |
| `GuideProfileRepository` | Own/public profile, guide search/top | Guide profil/about/preview, tourist guide UI | `GuideProfileSharedStore`, profil mocklari |
| `GuideTourRepository` | Private guide tour, session, dashboard | Home, publish, Turlarim, guide detail/edit | `TourCatalogStore`un guide mutation otoritesi |
| `TourDiscoveryRepository` | Public tour search/popular/detail/session | Tourist home/explore/public detail | `TourCatalogStore`un public discovery otoritesi |
| `ReservationRepository` | `/api/v1/reservations` | Trips, reservation detail/cancel | `TouristReservationStore` |
| `ReviewRepository` | Reservation review ve public tour reviews | Review form, detail yorumlari, puan refresh | Local review mutation/listeleri |
| `PaymentRepository` | Quote, checkout, payment status/cancel | Tour checkout, top-up, payment status/WebView | `TouristPaymentStore` |
| `SavedPaymentMethodRepository` | `/payment-methods/cards` | Kayitli kart listesi/default/delete | Sandbox kart listesi ve algilama |
| `WalletRepository` | `/api/v1/wallet` | Tourist wallet ve iki rolde transaction UI | `TouristWalletStore`un wallet kismi |
| `GuideFinanceRepository` | Earnings, bank account, withdrawal | Guide wallet/earnings/bank/withdrawal | `GuideWalletStore` |
| `ChatRepository` | Chat REST + STOMP | Ortak chat list/detail, badge | `ChatStore` |
| `NotificationRepository` | Notification REST + device/FCM | Guide panel, iki rol preferences/badge/deep link | Guide notification mocklari |

`GuideDashboardResponse`, private guide tur endpoint'lerinin bir parcasi
oldugu icin ayri tek metotluk dashboard repository acilmayacak;
`GuideTourRepository` icinde yer alacaktir. Benzer sekilde current user icin
ikinci bir profil/session repository olusturulmayacak.

### Mevcut Android Tuketici Dosyalari

Adim 5 tasima planinda ve Adim 6 dikey entegrasyonunda su mevcut dosyalar
baslangic tuketicileridir:

| Feature | Mevcut temel dosyalar | Adim 6 gorevi |
| --- | --- | --- |
| Auth/session | `data/remote/api/AuthApi.kt`, `data/repository/AuthRepositoryImpl.kt`, `data/repository/UserRepositoryImpl.kt` | Korunacak gercek altyapi; ortak network sozlesmelerine uyum |
| Media | `components/ImageSourcePicker.kt`, `components/GuideMateImage.kt` | 5 MB precheck, multipart upload ve Coil remote source |
| Guide profile | `screens/guide/profile/GuideProfileViewModel.kt`, `screens/guide/profile/shared/GuideProfileSharedStore.kt`, about/preview ViewModel'leri | Tek profile repository Flow'u ve role uygun mapper |
| Guide publish | `screens/guide/tourpublish/viewmodel/GuideTourPublishViewModel.kt` | Draft'i media + create request akisina map etme |
| Guide tours | `screens/guide/tours/GuideMyToursViewModel.kt`, `detail/GuideTourDetailViewModel.kt`, `edit/GuideTourEditViewModel.kt` | Private guide repository, paging, iki kimlikli route ve ayri mutation sonuclari |
| Tourist discovery | `screens/tourist/home/TouristHomeViewModel.kt`, `screens/tourist/explore/TouristExploreViewModel.kt`, `screens/tourist/tours/TouristTourDetailViewModel.kt` | Public search/popular/detail repository ve mapper |
| Reservations/reviews | `screens/tourist/trips/TouristTripsViewModel.kt`, `screens/tourist/reservations/store/TouristReservationStore.kt` | Reservation snapshot, cancel/refund/review ve reservation route |
| Checkout/payment | `screens/tourist/booking/checkout/TourCheckoutViewModel.kt`, `screens/tourist/payment/PaymentStatusViewModel.kt`, `screens/tourist/payment/store/TouristPaymentStore.kt` | Quote, hosted WebView, polling ve typed terminal state |
| Saved cards | `screens/tourist/profile/account/savedcards/viewmodel/TouristSavedCardsViewModel.kt`, `AddSavedCardViewModel.kt`, `sandbox/SandboxCardCatalog.kt` | Provider metadata listesi; native form/sandbox algilama temizligi |
| Tourist wallet | `wallet/presentation/tourist/TouristWalletViewModel.kt`, `wallet/presentation/tourist/transactions/TouristWalletTransactionsViewModel.kt`, `wallet/data/mock/tourist/TouristWalletStore.kt` | Wallet/transaction canonical repository |
| Guide finance | `wallet/presentation/guide/GuideMyWalletViewModel.kt`, `wallet/presentation/guide/earnings/GuideEarningsViewModel.kt`, `wallet/data/mock/guide/GuideWalletStore.kt` | Earnings/monthly/bank/withdrawal repository |
| Chat | `screens/common/chat/viewmodel/ChatListViewModel.kt`, `ChatDetailViewModel.kt`, `store/ChatStore.kt` | Ortak REST + STOMP repository ve unread Flow |
| Notification | `screens/guide/notifications/viewmodel/GuideNotificationsViewModel.kt`, iki rol notification settings ViewModel'i | REST preferences/history, FID/FCM ve semantic routing |
| Navigation | `navigation/guide/tours/GuideTourDestination.kt`, `navigation/tourist/TouristDestination.kt` | Guide `tourId+sessionId` ve tourist reservation-detail kimlik ayrimi |

Bu tablo hedef paket agaci degildir; mevcut kodda hangi tuketicinin hangi
sozlesmeye gecirilecegini kaybetmemek icindir.

### Auth ve Current User Eslemesi

| Ekran/aksiyon | Endpoint ve sozlesme | Android hedefi | Basari ve hata davranisi |
| --- | --- | --- | --- |
| Uygulama acilisi | `GET /api/v1/auth/me` | Mevcut `AuthRepository` + `UserRepository` | Token varsa canonical user/role yenilenir; 401 refresh zincirinden sonra root Auth'a doner |
| Register/login/Google/role | Mevcut auth request/response DTO'lari | Mevcut auth mapper/VM'ler | `ACCOUNT_PENDING_VERIFICATION`, Google ve field error davranisi korunur |
| Sifre degistirme/sifirlama | Mevcut auth endpoint'leri | Mevcut ekranlar | Basarida session temizligi ve Sign In yonlendirmesi korunur |
| Logout | `POST /api/v1/auth/logout` | Mevcut logout akisi | Remote hata olsa dahi local secret/session temizligi guvenli tamamlanir |

`userId`, `email`, `displayName` ve `role` yalniz authenticated user
sozlesmesinden gelir. Profil, chat, notification veya tour mapper'i sabit Ahmet,
Hans, turist/rehber kimligi uretmez.

### Medya ve Guide Profil Eslemesi

| Ekran/aksiyon | Endpoint/DTO | Repository ve mapper | Canonical sonuc |
| --- | --- | --- | --- |
| Avatar secme | `POST /api/v1/media?purpose=GUIDE_AVATAR`, multipart -> `MediaUploadResponse` | `MediaRepository.uploadImage(localUri, GUIDE_AVATAR)`; response -> `MediaAsset` | Donen `mediaAssetId` profile patch'e verilir; local URI kalici model olmaz |
| Cover secme | `POST /api/v1/media?purpose=TOUR_COVER` | `MediaRepository.uploadImage(localUri, TOUR_COVER)` | Donen ID create/change request'e girer |
| Goruntu okuma | `GET /api/v1/media/{mediaId}/content`, response `imageUrl` | Coil-backed ortak `GuideMateImage` | Local content/file URI ve HTTP/HTTPS tek noktada; fallback tasarimi korunur |
| Kullanilmayan draft silme | `DELETE /api/v1/media/{mediaId}` | `MediaRepository.deleteUnreferenced` | Yalniz backend izin verirse silinir; `MEDIA_IN_USE` gorunur hata olur |
| Kendi guide profili | `GET/PATCH /api/v1/guides/me/profile` | `GuideProfileRepository`; DTO -> domain -> Guide UI mapper | About, preview ve guide profil tek canonical Flow'dan beslenir |
| Public guide profili | `GET /api/v1/guides/{guideId}/public-profile` | Ayni repository, ayri public mapper | Tourist rehber gorunumu guide paketindeki store'a baglanmaz |
| Guide arama/top | `GET /api/v1/guides/search`, `/top` | Sayfali/search mapper | Tourist explore/top guide alanlari gercek sonuc alir |

Medya icin ayri status polling endpoint'i yoktur. Upload response icindeki
`status` ve sonraki profile/tour response icindeki media reference otoritedir.

Kapatilan uyumsuzluk: Android `ImageSourcePicker`, multipart adapter ve gorunen
metin backend ile ayni 5 MB sinirina indirildi. JPEG, PNG ve WebP dosya imzasi
yerelde kontrol edilir; backend dogrulamasi otorite olmaya devam eder.

### Guide Tur, Dashboard ve Earnings Eslemesi

| Ekran/aksiyon | Endpoint/request/response | Repository ve mapper | Refresh/hedef |
| --- | --- | --- | --- |
| Guide ana sayfa | `GET /api/v1/guides/me/dashboard` -> `GuideDashboardResponse` | `GuideTourRepository.observeDashboard` -> home UiState | Yayin/onay sayaclari liste boyutundan veya manuel `+1`den uretilmez |
| Turlarim sekmeleri | `GET /api/v1/guide/tours?tab=ACTIVE|REVIEW|PAST&page&size` -> `PageResponse<GuideTourCardResponse>` | Card DTO -> canonical guide tour/session domain -> tab UI mapper | Sekme basina refresh + append; siralama backend sonucunu korur |
| Yeni tur yayinlama | Once gerekli cover upload, sonra `POST /api/v1/guide/tours` -> `TourDetailResponse` | Draft -> `CreateGuideTourRequest` mapper | Basarida draft temizlenir, REVIEW listesi ve dashboard yenilenir |
| Guide tur detay | `GET /api/v1/guide/tours/{tourId}` -> `TourDetailResponse` | Private detail mapper + secili `sessionId` | Detail, reviews ve guide action mode canonical response'tan uretilir |
| Kritik tur bilgisi edit | `POST /api/v1/guide/tours/{tourId}/change-requests` + `baseVersion` | Content form -> `TourContentRequest` | REVIEW ve dashboard pending yenilenir; onayli veri hemen ezilmez |
| Session edit | `PATCH /api/v1/guide/sessions/{sessionId}` + `version` | Session form -> update request | Detail/list/dashboard yeniden fetch edilir |
| Yeniden yayinlama/yeni tarih | `POST /api/v1/guide/tours/{tourId}/sessions` | New session form mapper | Eski session korunur, yeni session kimligiyle listeler yenilenir |
| Ac/kapat | `POST /api/v1/guide/sessions/{sessionId}/open|close` | Typed operation result | Basarisizlik nedeni snackbar/dialog ile gorunur; toggle localde zorla kalmaz |
| Session iptal | `POST /api/v1/guide/sessions/{sessionId}/cancel`, `Idempotency-Key` | Cancel intent + reason mapper | Guide list/detail/dashboard yenilenir; iade sonucu backend state'inden izlenir |
| Tour archive | `POST /api/v1/guide/tours/{tourId}/archive` | Repository operation | Yalniz `canArchive`; list/detail/dashboard yenilenir |
| Earnings history | `GET /api/v1/guide/earnings?page&size` | Earnings DTO -> history UI | Sayfali detay liste |
| Aylik earnings | `GET /api/v1/guide/earnings/monthly` | `year/month/netEarningsMinor/currencyCode` -> aylik UI | Android tum history sayfalarini indirip toplamaz |

`GuideTourCardResponse.capacity` toplam kapasite, `bookedCount` dolu kisi,
kalan ise `capacity - bookedCount`tur. `netEarningsMinor` session kazancidir;
cekilebilir bakiye gibi sunulmaz ve null oldugunda kazanc uydurulmaz.

Kategori kodlari Android ile backend arasinda birebir dogrulanmistir:
`culture`, `food`, `nature`, `art`, `entertainment`, `adventure`.

#### Guide Detail/Edit Navigation Kimligi

Mevcut guide detail ve edit route'lari yalniz `sessionId` tasiyor. Backend
private detail endpoint'i `tourId` ile calisiyor ve response icinde session
listesi donuyor. Entegrasyonda route yalniz kimlik tasimaya devam edecek fakat
`tourId` ve `sessionId` birlikte tasinacak:

- `tourId`: private tour detail/change request kaynagi.
- `sessionId`: ekranda secili session ve session mutation hedefi.

Tam tur veya UI model navigation argument'i yapilmayacak.

#### Tek Edit Ekrani ile Iki Backend Islemi

Mevcut edit formu tur icerigi ve session alanlarini tek `saveChanges()` ile
local store'a yaziyor. Backend bunlari bilincli olarak iki ayri otoriteyle
yonetir: kritik content change request ve dogrudan session update.

Entegrasyonda ViewModel dirty alanlari `contentChanges` ve `sessionChanges`
olarak ayiracak. Tek ekrani ve mevcut gorsel duzeni korumak mumkundur; ancak iki
istek tek atomik islem gibi sunulmayacaktir:

- Yalniz content degistiyse change request gonderilir.
- Yalniz session degistiyse session patch gonderilir.
- Ikisi de degistiyse her islemin sonucu ayri izlenir; kismi basari sessizce
  tam basari sayilmaz. Kullaniciya hangi bolumun kaydedildigi/hangisinin
  basarisiz oldugu gosterilir ve ekran canonical response ile yenilenir.
- Basarisiz istegi tekrar etmek, basarili istegi ikinci kez gondermez.

Bu ayrim backend transaction sinirini Android'de taklit etmez ve kullaniciya
yanlis tam-basari gostermeyi engeller.

### Tourist Discovery ve Public Detail Eslemesi

| Ekran/aksiyon | Endpoint/response | Repository ve mapper | UI davranisi |
| --- | --- | --- | --- |
| Tourist home popular | `GET /api/v1/tours/popular?page&size` | `TourDiscoveryRepository`; search item -> popular card mapper | Guide'in yayinladigi ayni canonical tour/session kaynagi kullanilir |
| Tourist home top guides | `GET /api/v1/guides/top` | Public guide mapper | Rehber performance ozeti backend'den gelir |
| Tour explore/filter | `GET /api/v1/tours/search` + query/filter/page | Filter UiState -> query mapper | Uygula aksiyonu gercek listeyi yeniler; bos/retry/pagination tamamlanir |
| Guide explore | `GET /api/v1/guides/search` | Guide search mapper | Guide ve tour sonuc state'leri birbirine karistirilmaz |
| Public tour detail | `GET /api/v1/tour-sessions/{sessionId}` -> `TourDetailResponse` | Public detail + secili session mapper | Booking uygunlugu backend session state/capacity ile uretilir |
| Public yorumlar | `GET /api/v1/tours/{tourId}/reviews?page&size` | Review mapper | Detail yorumlari sayfali gelir; sabit yorum kullanilmaz |
| Public guide detail | `GET /api/v1/guides/{guideId}/public-profile` | Public profile mapper | Mesaj ve guide tur aksiyonlari `guideId` kullanir |

Turist public detail route'u icin mevcut `sessionId` dogru kimliktir. Public
detail ekrani guide private endpoint'ine veya guide store'una baglanmaz.

### Reservation, Trips, Cancellation ve Review Eslemesi

| Ekran/aksiyon | Endpoint/request/response | Repository ve mapper | Canonical davranis |
| --- | --- | --- | --- |
| Yaklasan/gecmis geziler | `GET /api/v1/reservations/me?status=UPCOMING|PAST&page&size` | `ReservationRepository`; snapshot -> trip card mapper | Kartlar mevcut tour degeriyle yeniden yazilmaz |
| Satin alinmis gezi detayi | `GET /api/v1/reservations/{reservationId}` -> `ReservationResponse` | Reservation snapshot -> ortak detail content mapper | Fiyat, katilimci, title ve cancellation/refund bilgisi satin alma anini korur |
| Turist iptali | `POST /api/v1/reservations/{reservationId}/cancel`, `Idempotency-Key`, version/reason | Cancel mapper -> typed result | Reservation/trips/session capacity/refund/payment/wallet refresh |
| Review gonderme | `POST /api/v1/reservations/{reservationId}/reviews` | Rating 1-5, comment en fazla 2000 | Basarida reservation detail, reviews, popular, guide performance/dashboard yenilenir |
| Review listeleme | `GET /api/v1/tours/{tourId}/reviews` | Sayfali review mapper | UI backend ortalamasi ve review count'u uydurmaz |

Mevcut Trips karti detaya `sessionId` ile gidiyor ve public detail ile satin
alma snapshot'ini ayni ViewModel'de birlestiriyor. Bu final entegrasyonda
duzeltilecek gercek kimlik hatasidir:

- Discovery/home karti -> `TourDetail(sessionId)`.
- Trips karti -> yeni type-safe `ReservationDetail(reservationId)`.
- Iki destination ayni `TourDetailContent`i kullanabilir; fakat farkli
  repository kaynagi, mode ve aksiyon seti kullanir.

Boylece guide tur bilgisini sonradan degistirse bile turistin satin aldigi
snapshot, iptal/refund ve review yetkisi kaybolmaz.

### Payment, Saved Card, Wallet ve Guide Finance Eslemesi

| Ekran/aksiyon | Endpoint/request/response | Repository ve mapper | Basari otoritesi/refresh |
| --- | --- | --- | --- |
| Odeme para birimleri | `GET /api/v1/payments/checkout/currencies` | Payment repository -> selector UiState | Android desteklenen birimi tahmin etmez |
| Tour quote | `POST /api/v1/payments/checkout/tour/quote` | session/participant/charge currency -> `PaymentQuoteResponse` | Quote amount, rate ve expiry aynen gosterilir; local FX yok |
| Top-up quote | `POST /api/v1/payments/checkout/wallet-top-up/quote` | amount + charge currency | Quote expiry sonrasi yeniden quote alinir |
| Tour checkout | `POST /api/v1/payments/checkout/tour`, `Idempotency-Key` | `TourCheckoutRequest` -> `PaymentResponse` | Yalniz `SUCCEEDED + CONFIRMED` booking basarisidir |
| Wallet top-up | `POST /api/v1/payments/checkout/wallet-top-up`, `Idempotency-Key` | `WalletTopUpRequest` -> payment | `SUCCEEDED` sonrasi wallet yeniden fetch edilmeden bakiye basarisi kesinlesmez |
| Hosted card | Backend `paymentPageUrl` | Guvenli WebView screen/state holder | Callback JSON basari sayilmaz; WebView sonrasi payment polling yapilir |
| Payment recovery | `GET /api/v1/payments/{paymentId}` | Payment status mapper | Process death/reopen durumunda terminal state'e kadar kontrollu polling |
| Payment cancel | `POST /api/v1/payments/{paymentId}/cancel` | Typed result | Yalniz cancellable durumda; payment/reservation yeniden fetch |
| Kayitli kartlar | `GET /api/v1/payment-methods/cards` | Saved method mapper | Banka/network/last4 provider metadata'sindan gelir |
| Default/sil | `PUT /api/v1/payment-methods/cards/{id}/default`, `DELETE /api/v1/payment-methods/cards/{id}` | Saved method repository | Liste canonical olarak yenilenir |
| Tourist/guide wallet | `GET /api/v1/wallet` | Wallet DTO -> role-specific wallet UI mapper | Ayni backend bakiye, farkli presentation |
| Wallet hareketleri | `GET /api/v1/wallet/transactions?page&size` | Transaction mapper | `referenceTitle` tur basligi; type metni Android resource'undan |
| Guide banka hesaplari | `GET/POST /api/v1/guide/bank-accounts`, default ve delete endpoint'leri | Guide finance mapper | Android masked IBAN gosterir; full IBAN kalici UI modelde tutulmaz |
| Para cekme | `POST /api/v1/guide/withdrawals`, `Idempotency-Key` | bankAccountId + amount mapper | Backend balance kontrolu; wallet/transactions/withdrawals refresh |
| Para cekme gecmisi | `GET /api/v1/guide/withdrawals?page&size` | Withdrawal mapper | `SIMULATED` payout modu gercek banka transferi gibi sunulmaz |

WebView kurali:

- SSL hatalari atlanmaz.
- `addJavascriptInterface` eklenmez.
- Checkout Form gerektiriyorsa normal JavaScript acilabilir.
- POST navigation nedeniyle yalniz `shouldOverrideUrlLoading` callback'ine
  guvenilmez.
- WebView kapanisi/return sonrasi `paymentId` ile backend status polling
  yapilir; provider HTML/JSON'u basari kaniti degildir.
- `refundStatus == MANUAL_REVIEW` veya devam eden refund kullaniciya ayri state
  olarak gosterilir.

#### Standalone Kayitli Kart Boslugu

Backend bilerek standalone kart ekleme endpoint'i sunmuyor. Kart provider
hosted checkout sirasinda kaydedilir. Bu nedenle mevcut native
`AddSavedCardScreen`, ham kart formu ve `SandboxCardCatalog` gercek endpoint'e
map edilemez ve entegrasyonda kaldirilacaktir.

Kayitli Kartlar ekranindaki ekleme aksiyonu ham kart formu acmayacak. Kullanici
kartin guvenli odeme sirasinda kaydedilecegi konusunda uygulama tasarimina uygun
bir aciklama gorecek. Sirf kart kaydetmek icin sahte/0 tutarli top-up
olusturulmayacak. Provider daha sonra gercek standalone tokenization
desteklerse once backend/OpenAPI sozlesmesi eklenmeden Android formu yazilmaz.

IBAN icin `TurkishBankCatalog` yalniz hizli on gosterim olarak kalabilir;
backend banka/IBAN sonucunun otoritesidir. Kart bankasi icin Android sandbox
algilama kalmaz.

### Chat REST, STOMP ve FCM Eslemesi

| Ekran/aksiyon | Sozlesme | Repository/mapper | Davranis |
| --- | --- | --- | --- |
| Sohbet bul/olustur | `POST /api/v1/chats/with-user/{remoteUserId}` | `ChatRepository.findOrCreate` | Rezervasyon zorunlulugu yok; ayni kullanici cifti tek chat |
| Sohbet listesi | `GET /api/v1/chats` | Conversation DTO -> list UI mapper | Last message/unread canonical gelir |
| Mesaj gecmisi | `GET /api/v1/chats/{chatId}/messages?page&size` | Sayfali message mapper | Ilk sayfa + yukari dogru eski mesaj pagination |
| Mesaj gonder | REST `POST /api/v1/chats/{chatId}/messages` veya STOMP `/app/chats/{chatId}/messages` | `clientMessageId` ile optimistic pending | Server response SENT/FAILED sonucunu belirler; duplicate engellenir |
| Okundu | `POST /api/v1/chats/{chatId}/read` | Repository mutation | Conversation ve global unread count yenilenir |
| Badge | `GET /api/v1/chats/unread-count` | Shared unread Flow | Guide/tourist bottom bar ayni kaynagi kullanir |
| Realtime | `/ws`, `/user/queue/chat-messages`, `/user/queue/chat-errors` | Authenticated STOMP session | Reconnect sonrasi REST resync; socket tek kalici veri kaynagi degildir |
| Uygulama kapali | FCM `CHAT_MESSAGE` + `chatId` | Semantic notification mapper | Type-safe chat detail route'una gider |

Mesaj body istemci limiti backend ile 2000 karakterdir. `isFromMe` response'tan
gelmez; `senderId == currentUserId` mapper kuralidir. Karsi kullanici adi route
argument'i degil conversation response kaynagidir.

### Notification, Preferences ve Semantic Routing Eslemesi

| Ekran/aksiyon | Endpoint/DTO | Repository/mapper | Davranis |
| --- | --- | --- | --- |
| Bildirim listesi | `GET /api/v1/notifications?page&size` | Notification DTO -> UI mapper | `actorDisplayName` actor UI alanina; sistemde null olabilir |
| Unread badge | `GET /api/v1/notifications/unread-count` | Shared unread Flow | Topbar badge iki rolde ayni canonical sayiyi kullanir |
| Tekli okundu | `POST /api/v1/notifications/{id}/read` | Mutation + local canonical update/refetch | Bildirime tiklama yalniz o kaydi okur |
| Tumunu okundu | `POST /api/v1/notifications/read-all` | Repository mutation | Paneli acmak otomatik read-all yapmaz |
| Preferences | `GET/PATCH /api/v1/notifications/preferences` | Yedi typed tercih mapper'i | Security alert immutable/always-on kuralina uyulur |
| Device register | `POST /api/v1/devices/fcm-registration`, `DELETE /api/v1/devices/fcm-registration/{installationId}` | FID + app installation ID mapper | Login/refresh/register ve logout/account switch lifecycle'ina baglanir |
| FCM alma | Backend notification type + payload | Merkezi semantic target mapper | Ham route string backend'den alinmaz |

`NotificationType` backend enumu birebir data enumuna map edilir; bilinmeyen
yeni tip uygulamayi cokertmez ve guvenli generic bildirim olarak gosterilir.
`actorDisplayName` icin ek user sorgusu atilmaz. Tur/odeme/rezervasyon detayina
yonlendirme payload kimlikleriyle type-safe destination olusturur; terminal
veya yetkisiz hedefte guvenli ana/liste ekrani fallback'i kullanilir.

Mevcut guide paneli full history UI olarak korunur. Tourist tarafinda su an
yalniz preferences ve FCM/deep-link tuketicisi vardir. Backend ortak olsa da
yeni tourist notification history ekrani bu adimda sessizce icat edilmez;
istenirse ortak panel icin ayri urun/tasarim karari verilir.

### Merkezi Hata ve UX Sozlesmesi

Tum repository'ler ortak API hata parser'ini kullanacak ve sonuc en az su
siniflari ayirt edecektir:

- `Validation(fieldErrors)`: ilgili field supporting/error text'ine map edilir.
- `Unauthorized`: refresh zinciri; basarisizsa local session temizligi ve Auth.
- `Forbidden`: rol/yetki mesaji, local state'i basarili gibi degistirmez.
- `Conflict(code)`: lifecycle, capacity, version veya idempotency nedeni
  kullaniciya yerellestirilmis operation failure olarak gosterilir.
- `RateLimited`: tekrar deneme mesaji; otomatik hizli retry dongusu yoktur.
- `NetworkUnavailable` ve `ServerUnavailable`: mevcut icerik korunabiliyorsa
  korunur, retry aksiyonu sunulur.
- `Unknown`: backend detail'i sizdirmayan genel hata.

Yuksek onemli typed conflict mapping'leri:

- Tour/session: `TOUR_CHANGE_PENDING`, `TOUR_LOCATION_LOCKED`,
  `SESSION_NOT_BOOKABLE`, `SESSION_HAS_RESERVATIONS`,
  `CAPACITY_NOT_AVAILABLE`, `CAPACITY_BELOW_BOOKED_COUNT`,
  `CONCURRENT_UPDATE`.
- Reservation/review: `RESERVATION_ALREADY_EXISTS`,
  `RESERVATION_NOT_CANCELLABLE`, `REVIEW_NOT_ALLOWED`,
  `REVIEW_ALREADY_EXISTS`.
- Payment/wallet: `FX_QUOTE_EXPIRED`, `CARD_INSUFFICIENT_FUNDS`,
  `PAYMENT_METHOD_DECLINED`, `INSUFFICIENT_WALLET_BALANCE`,
  `IDEMPOTENCY_CONFLICT`.
- Media/chat: `MEDIA_TOO_LARGE`, `MEDIA_IN_USE`,
  `CHAT_PARTICIPANT_INVALID`, `CHAT_MESSAGE_TOO_LONG`.

ViewModel mutation sonucunu yok saymaz. Switch, submit veya cancel localde
basarili gorunmeden once typed backend sonucu alinir; basarisizlik mevcut
state'i korur ve kullaniciya gorunur mesaj verir.

### Loading, Empty, Retry ve Pagination Kurali

- Ilk acilis loading'i ile pull-to-refresh birbirinden ayrilir; refresh mevcut
  icerigi gereksiz yere beyaz ekrana cevirmez.
- Bos liste backend basarisindan sonra empty state'tir; ag/HTTP hatasi bos liste
  gibi gosterilmez.
- Sayfali listeler `page/size` ile ilerler, kimlige gore duplicate ayiklar ve
  son sayfada append'i durdurur.
- Filtre/query degisince eski paging cursor/page sifirlanir.
- Retry yalniz basarisiz operasyonu tekrarlar. Basarili idempotent mutation
  farkli key ile istemeden yeniden uretilmez.
- Process death sonrasinda form draft'i gereken yerde `SavedStateHandle` ile,
  canonical server verisi repository refetch ile geri kurulur.

### Canonical Mutation Refresh Matrisi

| Mutation | Basaridan sonra yenilenecek canonical kaynaklar |
| --- | --- |
| Guide profile patch | Own profile, preview, guide home profile ozeti, public guide cache |
| Tour create/change request | Guide REVIEW listesi, private detail, dashboard pending count |
| Session create/update/open/close | Guide list/detail/dashboard; public tour search/detail cache invalidation |
| Guide session cancel | Guide list/detail/dashboard; ilgili reservation/refund/payment/wallet/notification verileri sonraki role refresh'te backend'den |
| Tour checkout success | Payment, reservation/trips, public session capacity; wallet methodiyse wallet/transactions |
| Reservation cancel | Reservation detail/list, public session capacity, payment/refund, wallet/transactions, notification count |
| Review submit | Reservation detail, tour reviews/detail/popular, guide performance/profile/dashboard |
| Wallet top-up | Payment status, wallet balance, wallet transactions |
| Saved method default/delete | Saved method list; acik islemde secim yeniden dogrulanir |
| Bank account/default/delete | Bank account list ve guide wallet default account projection'i |
| Withdrawal create | Withdrawal list, guide wallet balances, wallet transactions |
| Chat send/read | Message page, conversation list, chat unread count |
| Notification read/read-all | Notification page ve notification unread count |

Bu liste Android'in baska kullanicinin state'ini localde mutate edecegi anlamina
gelmez. Backend transaction tamamlar; Android kendi gorunur canonical
kaynaklarini yeniden sorgular veya realtime event ile invalidate eder.

### Bilincli Olarak Backend Repository Almayacak Alanlar

- `CitySearchService` Google Places kaynagini kullanmaya devam eder.
- Ulke/dil kataloglari ve `TourCategoryCatalog` local, yerellestirilebilir UI
  metadata'sidir; backend kodlarinin gorunen metni degildir.
- Currency/date/relative-time formatter'lari UI/core formatter olarak kalir.
- Yardim, yasal metinler ve uygulama surumu gibi statik ekranlar backend'e
  baglanmaz.
- Admin tour review endpoint'leri Android guide/tourist uygulamasina eklenmez;
  mevcut backend admin araci kullanilir.
- Android lifecycle scheduler'i backend COMPLETED/refund/earning otoritesinin
  yerine gecmez.

### Dogrulanmis Gercek Entegrasyon Bosluklari

Adim 6'da kod yazilirken atlanmayacak somut farklar:

1. Auth ve medya disindaki feature'larda Retrofit API, DTO, repository ve mapper yok;
   somut MVP store'lar ViewModel'lere enjekte ediliyor.
2. Payment icin WebView, realtime chat icin STOMP ve push icin FCM/Firebase
   Installation runtime entegrasyonu yok.
3. Guide detail/edit route'lari backendin ihtiyac duydugu `tourId`yi tasimiyor.
4. Guide edit tek local mutation yapiyor; content change request ile session
   update sonucunu ayirmiyor.
5. Trips detail public `sessionId` route'unu kullaniyor; reservation snapshot
   icin `reservationId` destination'i yok.
6. Native saved-card formu ve `SandboxCardCatalog` backend/provider
   sozlesmesinde karsiliksiz.
7. Payment quote/currency selector, hosted WebView, polling/recovery ve
   refund/manual-review state'leri gercek backend'e bagli degil.
8. Explore filter sonuc/pagination, guide search ve backend popular/top akisi
   tamamlanmamis.
9. Notification device/FID, FCM receive, semantic routing ve iki rolde ortak
    unread state gercek kaynaga bagli degil.
10. Chat REST/STOMP reconnect/resync ve FCM chat target'i yerine MVP store var.
11. Hardcoded/demo user, guide, avatar, email, actor ve mock kimlikler ilgili
    repository canonical oldugunda kontrollu temizlenmeli.

### Mock Temizleme Esigi

Bir mock store yalniz su kosullarin tamami saglandiginda kaldirilir:

1. Ilgili endpoint request/response DTO'lari yazilmis ve OpenAPI ile
   dogrulanmistir.
2. Repository interface/implementation ve mapper test edilebilir durumdadir.
3. Ekranin loading/empty/error/content ve mutation sonucu gercek repository'ye
   baglidir.
4. Navigation kimligi ve canonical refresh davranisi calismaktadir.
5. Preview veya tasarim ornegi gerekiyorsa runtime store yerine preview fixture
   kullanilir.

Bu esik tamamlanmadan mock silinip ekran bos birakilmaz; tamamlandiktan sonra da
mock runtime fallback olarak tutulmaz.

### Adim 4 Sonucu ve Kapisi

Backend endpoint/DTO sozlesmeleri Android ekran ve aksiyonlariyla
eslestirilmistir. Repository sorumluluklari, domain/UI mapper siniri, typed
hata, pagination, idempotency ve canonical refresh kurallari kesinlesmistir.

Backend tarafinda yeni endpoint veya migration gerektiren zorunlu bosluk
bulunmamistir. Standalone kart ekleme, tourist notification history ve admin
mobil UI gibi backendde karsiligi olmayan alanlar bilincli urun siniri olarak
isaretlenmistir. Android tarafindaki gercek entegrasyon farklari yukarida
dosya-planina girecek netlikte listelenmistir.

Bir sonraki calisma Adim 5'tir: bu mantiksal repository ve feature
sahipliklerini tek Gradle `app` modulu icinde feature-first paket/dosya agacina
donusturmek, tasima sirasini ve bagimlilik yonunu kesinlestirmek. Adim 5
tamamlanmadan toplu package refactor'u veya entegrasyon kodu yazilmayacaktir.

## Adim 5 - Feature-First Hedef Mimari

Durum: TAMAMLANDI (hedef mimari ve tasima plani).

Bu adimda Android veya backend kaynak kodu degistirilmedi. Yalniz mevcut paket
agaci, import yonleri, Hilt modulleri, navigation sahiplikleri, ortak UI
sinirlari, mock store'lar ve testler yeniden taranarak hedef yapi
kesinlestirildi. Fiziksel package refactor'u, yedi hazirlik adimi tamamlanip
kullanici acik komut verdikten sonra uygulanacaktir.

### Refactor Icin Baglayici Altin Kural

Feature-first gecisi yeni bir mimari icat etme calismasi degildir. Mevcut dogru
isleyisin sahipligini daha gorunur hale getiren davranis-koruyucu bir paket
refactor'udur.

- UI tasarimi, stringler, paddingler, renkler, ekran akislar, navigation stack,
  type-safe destination kimlikleri ve tek scaffold davranisi degistirilmez.
- `Tour` + `TourSession`, ortak tur detay renderer'i, chat, guide profile ozeti,
  locale secimleri ve para formatlama gibi gercek ortak yapilar kopyalanmaz;
  dogru ortak sinirda kalir.
- Guide ve tourist yalniz benzer gorundugu icin ortaklastirilmaz. Ayni business
  anlami ve ayni degisiklik nedeni varsa feature icindeki `components`,
  `detail`, `status`, `account` gibi sorumlulugu anlatan paket kullanilir.
- Bagimlilik yonu tersine cevrilmez. Presentation somut API/store
  implementasyonunu; `common` ise herhangi bir feature'i bilmez.
- Package tasimasi sirasinda repository, use-case, manager, base ViewModel veya
  generic framework eklenmez. Gercek endpoint baglantisi olmayan MVP store'a
  sirf SOLID gorunmesi icin bos interface yazilmaz.
- Mevcut sekiz auth use-case'i ek is kurali tasimayan pass-through siniflar
  oldugu icin auth refactor'unda kaldirilir. ViewModel'lar `AuthRepository`
  arayuzunu dogrudan kullanir; yeni use-case eklenmez.
- Kaldirilacak siniflar: `LoginUseCase`, `GoogleLoginUseCase`,
  `RegisterUseCase`, `ForgotPasswordUseCase`, `ResendVerificationUseCase`,
  `SelectRoleUseCase`, `ChangePasswordUseCase` ve `ClearSessionUseCase`.
- `EmailPolicy` ve `NumericPasswordPolicy` saf, birden fazla auth akisinda
  kullanilan istemci politikalaridir. `auth/domain/validation` altinda
  korunurlar; her feature icin otomatik `validation` paketi acilmaz.
- Refactor ile backend entegrasyonu ayni degisiklik diliminde karistirilmaz.
  Once paket tasimasi compile ve test ile kapanir; sonra Adim 6'daki gercek
  repository/DTO/mapper baglantisi yapilir.
- Her tasimada eski package'ta kopya dosya birakilmaz. Bosa dusen import,
  fonksiyon, dosya ve klasor kontrollu temizlenir; ilgisiz kullanici
  degisikliklerine dokunulmaz.

### Kesin Mimari Karari

- Proje tek Gradle `:app` modulu olarak kalacaktir.
- Bu asamada `:core:*` ve `:feature:*` Gradle modulleri acilmayacaktir. Tek
  gelistiricili mevcut boyutta bunun build karmasasi, DI/navigation wiring'i ve
  tasima maliyeti somut faydasindan fazladir.
- Kotlin package kokunde gereksiz bir `features/` sarmalayicisi olmayacaktir.
  Backend'deki okunabilir feature-first yaklasima benzer bicimde feature'lar
  dogrudan kokte bulunacaktir.
- En dis sahiplikler `common`, merkezi `navigation`, uygulama-seviyesi `di` ve
  dogrudan feature kokleridir. Genel `data`, `domain`, `screens` ve
  `components` uygulama seviyesinde yatay cop kutulari olarak kalmayacaktir.
- Mevcut merkezi `navigation` yapisi bilincli istisnadir. Root, auth, chat,
  guide ve tourist back-stack composition'i burada kalir; navigation dosyalari
  feature paketlerine dagitilmaz.
- Ust seviye `di` yalniz birden fazla feature'i birlestiren uygulama
  composition'ini tutar. Feature'a ozel API/repository binding'leri ilgili
  feature'in `di` paketinde, ortak teknik binding'ler ilgili `common` alaninda
  bulunur.
- Bir feature yalniz ihtiyac duydugu alt katmanlari acar. Endpoint'i olmayan
  `home` veya `discovery` icin bos `data/domain/repository` paketleri
  olusturulmaz.

### Hedef Kok Agac

```text
com.ahmetkaragunlu.guidemate/
  GuideMateApplication.kt
  MainActivity.kt

  common/
    network/
    result/
    storage/
    location/
    ui/

  navigation/
    auth/
    chat/
    guide/
      account/
      finance/
      tours/
    tourist/
      account/
      payment/

  di/

  auth/
  media/
  profile/
  tour/
  discovery/
  home/
  reservation/
  review/
  payment/
  wallet/
  chat/
  notification/
```

`tour`, `reservation`, `review`, `payment`, `chat`, `notification`, `media` ve
`profile` isimleri backend bounded-context adlariyla uyumludur. `wallet`,
Android'deki wallet, guide earnings, banka hesabi ve para cekme ekranlarini tek
uyumlu kullanici yeteneginde toplar. `home` ile `discovery` ise birden fazla
repository projection'ini birlestiren presentation feature'laridir; kendi
backend otoritelerini uretmezler.

### Uygulama Composition Sorumlulugu

Kok application siniflari, merkezi `navigation` ve ust seviye `di` uygulama
composition root'unu olusturur:

```text
GuideMateApplication.kt
MainActivity.kt

navigation/
  GuideMateNavigation.kt
  RootDestination.kt
  RootNavigationViewModel.kt
  NavControllerExtensions.kt
  NavigationUiConfig.kt
  components/
    AppTopBar.kt
    AppBottomBar.kt
    BottomBarItem.kt
  auth/
  chat/
  guide/
    account/
    finance/
    tours/
  tourist/
    account/
    payment/

di/
  NetworkModule.kt
```

- Root auth/role/guide/tourist secimi, guide/tourist tek scaffold shell'leri ve
  account alt graph'lari mevcut merkezi `navigation` sahipliginde kalir.
- `navigation/guide` ve `navigation/tourist`, rol bazli scaffold, bottom bar,
  topbar config ve bagimsiz back-stack gecmislerini sahiplenir.
- `navigation/auth`, `navigation/chat`, guide altindaki `account/finance/tours`
  ve tourist altindaki `account/payment` mevcut flow gruplarini korur. Bir
  package'ta az dosya olmasi, sahiplik anlami aciksa tasima nedeni degildir.
- `AppTopBar`, `AppBottomBar`, `BottomBarItem`, `NavigationUiConfig` feature
  component'i degil shell UI'sidir. `navigation/components` altinda tutularak
  `common` alaninin chat, notification veya destination bilgisi edinmesi
  engellenir.
- Feature composable'lari `NavController` almaz. Navigation tarafindan veri ve
  callback alir; mevcut bu dogru davranis korunur.
- Ortak OkHttp/Retrofit istemcisinin auth interceptor/authenticator ile
  birlestirilmesi feature ve common'i birbirine baglamamak icin ust seviye
  `di/NetworkModule.kt` icinde yapilir. API interface provider'lari kendi
  feature `di` paketinde bulunur.
- `GuideMateApplication.kt` ve `MainActivity.kt` icin yapay `app` paketi
  acilmaz; mevcut manifest sinif yollari ve startup davranisi korunur.

### `common` Sorumlulugu

`common` yalniz feature'dan bagimsiz, birden fazla feature'in gercekten
kullandigi teknik ve UI temellerini tutar:

```text
common/
  result/
    AppError.kt
    AppFieldError.kt
    BackendErrorCode.kt
    DataResult.kt

  network/
    error/
      ApiErrorParser.kt
      ApiErrorResponse.kt
    model/
      PageResponseDto.kt
    serialization/
      InstantAdapter.kt

  storage/
    secure/
      SecureStringStorage.kt
      AndroidKeystoreSecureStringStorage.kt
    preferences/
    installation/

  location/
    data/
    model/
    presentation/
    di/

  ui/
    components/
    error/
    formatting/
    image/
    resource/
      di/
    theme/
```

- `AppErrorMessage.kt` ve `ResourceProvider` Android resource bilgisi tasidigi
  icin `common/ui/error` ve `common/ui/resource` altinda bulunur; saf result
  modellerine `R` bagimliligi verilmez.
- Retrofit/OkHttp/Gson hata parsing'i ve ortak wire modelleri `common/network`
  altindadir. Feature DTO'lari burada toplanmaz.
- Token/session manager auth'a aittir. Yalniz Keystore tabanli genel secret
  adapter'i `common/storage/secure` altinda kalir.
- Mevcut `AppPreferencesDataSource` user, onboarding ve installation ID
  sorumluluklarini birlestiriyor. Davranis-koruyucu refactor'da user/onboarding
  saklama auth'a; auth ve notification'in ortak kullandigi kurulum kimligi ise
  `common/storage/installation` sinirina ayrilir. Bu, gercek iki tuketici oldugu
  icin gerekli bir sinirdir; genel storage framework'u kurulmaz.
- Ulke, sehir ve dil secimi birden fazla feature tarafindan ayni davranisla
  kullanilir. `CitySearchService`, Google Places adapter'i, locale kataloglari,
  picker ViewModel ve ortak bottom sheet'ler `common/location` altinda birlikte
  kalir. Bunlar `common/ui`ya dagitilmaz.
- Edit field/button/dialog/picker, tab, rating, currency formatter, theme,
  local/remote image renderer ve camera/gallery source picker gercek ortak UI
  olduklari icin `common/ui` altinda kalir.
- `common` hicbir feature, navigation destination'i, role-specific UiState
  veya feature repository implementasyonu import etmez.

### Feature Ici Standart

Her feature asagidaki kalibi yalniz ihtiyaci kadar kullanir:

```text
feature/
  data/
    remote/
      api/
      dto/
    mapper/
    repository/
    mock/          # Yalniz gecici MVP kaynagi varsa
  domain/
    model/
    repository/
    validation/    # Yalniz gercekten paylasilan saf is/giris politikasi varsa
  presentation/
    components/    # Yalniz tekrar kullanilan feature UI parcalari varsa
    detail/        # Gercek bir ortak ekran akisi varsa
    guide/
    tourist/
  di/
```

- DTO -> domain mapper `data/mapper`dadir.
- Domain -> UI mapper `presentation/.../mapper`dadir.
- Repository interface `domain/repository`, implementasyonu
  `data/repository` altindadir.
- Screen, ViewModel, UiState ve o ekrana ozel component ayni presentation
  akisi altinda tutulur.
- Bir veya iki dosya icin yapay `components/content/model/viewmodel` zinciri
  acilmaz. Alt paket ancak gercek bir ekran akisini veya anlamli dosya grubunu
  ayiriyorsa kullanilir.
- `components`, butun ortak kodun genel adi degildir; yalniz tekrar kullanilan
  UI parcalarini tutar. Ortak tam ekran veya akislar `detail`, `status`,
  `account`, `list` gibi sorumluluk adlariyla paketlenir.
- Feature icinde ikinci bir `common` paketi acilmaz. Uygulama genelindeki tek
  `common`, feature'lar arasi gercek ortak teknik ve UI temelidir.
- Navigation destination, graph ve shell dosyalari feature'lara tasinmaz;
  merkezi `navigation` paketinde flow/rol bazli gruplanir.
- `Store` adi yalniz gecici MVP kaynaginda kullanilir. Gercek entegrasyondan
  sonra runtime store fallback'i kalmaz.

### Feature Sahiplikleri

| Feature | Sahip oldugu alan | Bilincli sinir |
| --- | --- | --- |
| `auth` | Auth API/DTO/repository, current user session, onboarding, role, password policy ve auth ekranlari | `UserRepository` ile `OnboardingRepository` auth icinde gruplanir; ayri `user` feature acilmaz |
| `media` | Upload/delete/media reference repository ve DTO'lari | Camera/gallery picker ve image renderer `common/ui/image` altindadir |
| `profile` | Own/public guide profile, guide performance/level, guide/tourist profil ve ortak account statik ekranlari | Banka hesabi, kart ve notification settings profile icine gomulmez |
| `tour` | `Tour`, `TourSession`, kategori, lifecycle modelleri, public/private tour repository'leri, publish/edit/detail/card UI | Guide ve tourist farkli mapper/aksiyon kullanir; canonical tour/session modeli ortaktir |
| `discovery` | Tourist explore, filter ve guide/tour sonuc orkestrasyonu | Data kaynagi uretmez; tour ve profile repository sozlesmelerini kullanir |
| `home` | Guide/tourist ana sayfa projection'larini UI'da birlestirme | `HomeRepository` acilmaz; sahip feature repository Flow'lari kullanilir |
| `reservation` | Reservation snapshot, trips, reservation detail/cancel ve tour checkout baslatma | Public tour detayiyla ayni renderer kullanabilir; kaynak ve kimlik `reservationId`dir |
| `review` | Review list/submit contract'i, form ve eligibility sunumu | Satin alma/rezervasyon yetkisini localde uydurmaz |
| `payment` | Quote, hosted checkout/WebView, payment state, provider kayitli kart metadata'si | Wallet bakiyesi ve withdrawal wallet'a aittir; ham kart formu kalici hedef degildir |
| `wallet` | Tourist/guide wallet, transactions, earnings, bank account ve withdrawal | Para otoritesi backend'dir; ortak money action UI bu feature'in `presentation/components` alanidir |
| `chat` | Conversation/message modelleri, REST/STOMP repository, ortak chat list/detail ve unread | Guide/tourist icin ayri mesaj veri kaynagi olusturulmaz |
| `notification` | Notification REST, preferences, FCM device/receiver, semantic target ve guide paneli | Tourist history ekrani urun karari olmadan eklenmez; iki rol ortak unread/preferences kaynagini kullanir |

### Mevcut -> Hedef Paket Tasima Matrisi

Bu matris fiziksel refactor sirasinda dosya sahipligini belirler. Bir satirin
hedefi, o klasordeki her dosyanin korlemesine ayni alt pakete atilacagi anlamina
gelmez; DTO/domain/UI ayrimi yukaridaki katman kuralina gore yapilir.

| Mevcut paket/dosyalar | Hedef sahiplik | Not |
| --- | --- | --- |
| Kok `GuideMateApplication.kt`, `MainActivity.kt` | Mevcut kok package | Yapay `app` paketi ve manifest tasimasi yapilmaz |
| `navigation/GuideMateNavigation.kt`, `RootDestination.kt`, `RootNavigationViewModel.kt`, `NavControllerExtensions.kt`, `NavigationUiConfig.kt` | Mevcut `navigation/` | Root composition korunur |
| `navigation/auth`, `navigation/chat`, `navigation/guide`, `navigation/tourist` ve mevcut alt flow paketleri | Mevcut merkezi `navigation/` hiyerarsisi | Navigation feature'lara dagitilmaz; type-safe route ve back-stack semantigi korunur |
| `components/AppTopBar.kt`, `AppBottomBar.kt` ve `BottomBarItem` | `navigation/components` | Uygulama shell UI'si |
| Diger `components/Edit*.kt` | `common/ui/components` | Gercek tasarim sistemi primitives |
| `components/GuideMateImage.kt`, `ImageSourcePicker.kt` | `common/ui/image` | Local/remote render ve kamera/galeri secimi |
| `ui/theme` | `common/ui/theme` | Tek tema kaynagi |
| `common/AppError*`, `AppFieldError`, `BackendErrorCode`, `DataResult` | `common/result` ve `common/ui/error` | Saf sonuc ile resource mapping ayrilir |
| `common/ResourceProvider*` | `common/ui/resource` | Android resource adapter'i |
| `data/remote/error` | `common/network/error` | Tum feature'larin ortak backend hata sozlesmesi |
| `data/remote/interceptor`, `TokenRefreshException` | `auth/data/remote/session` | Auth session davranisi; ust seviye DI ortak client'a baglar |
| `data/local/TokenManager`, `AuthSessionManager`, `CredentialSessionManager` | `auth/data/local/session` | Auth'a ozel session yonetimi |
| `data/local/SecureStringStorage*` | `common/storage/secure` | Keystore adapter'i feature bagimsizdir |
| `data/local/preferences/AppPreferencesDataSource.kt` | `auth/data/local/preferences` + `common/storage/installation` | Sorumluluk davranis degismeden iki gercek sahibe ayrilir |
| `data/remote/api/AuthApi.kt`, auth DTO'lari, `data/mapper/AuthMapper.kt`, uc repository impl | `auth/data` | Auth dikey dilimi tek yerde |
| `domain/model/User*`, uc repository interface ve auth validation politikalari | `auth/domain/model|repository|validation` | Sekiz pass-through auth use-case kaldirilir; ViewModel'lar `AuthRepository` arayuzunu dogrudan kullanir |
| `screens/auth`, `screens/common/changepassword` | `auth/presentation` | Role/onboarding/password dahil |
| `di/RepositoryModule.kt` | `auth/di/AuthRepositoryModule.kt` | Yalniz auth binding'leri |
| `di/NetworkModule.kt` | Ust seviye `di/NetworkModule.kt` + `common/network` + `auth/di` | Client composition ustte, ortak network tipleri common'da, AuthApi provider auth'ta |
| `di/PreferencesModule.kt`, `StorageModule.kt` | `common/storage/di` | Scope degismez |
| `di/ResourceModule.kt` | `common/ui/resource/di` | Resource adapter binding'i |
| `data/remote/places`, `di/SelectionModule`, `screens/common/selection` | `common/location` | Contract/model presentation'dan ayrilir; service, adapter, picker ve locale katalogu tek sahiplikte kalir |
| `screens/common/formatting`, `rating`, `tab` | `common/ui/formatting|components` | Birden fazla feature'in gercek ortak UI'si |
| `screens/common/tours` | `tour/domain`, `tour/data/mock`, `tour/presentation/components|detail|category` | Model, gecici store ve UI sorumluluklari ayrilir; feature icinde `common` acilmaz |
| `screens/guide/tourpublish`, `screens/guide/tours` | `tour/presentation/guide` | Ayni guide tour feature'i, ayri publish/manage akislar |
| `screens/tourist/tours`, `screens/tourist/category` | `tour/presentation/tourist|detail|category` | Public detail ve kategori karti |
| `screens/common/guide`, `screens/common/profile` | `profile/domain` ve `profile/presentation/components|level|publicprofile` | Guide public summary, performance, level ve ortak menu |
| `screens/common/helpsupport`, `legalagreements` | `profile/presentation/account` | Iki rolde ayni statik ekranlar |
| `screens/guide/profile` | `profile/presentation/guide` ve gecici `profile/data/mock` | About/preview ayni profile kaynagini kullanir |
| `screens/tourist/profile` | `profile/presentation/tourist` | Saved card/settings alt alanlari kendi feature'ina ayrilir |
| `screens/guide/home`, `screens/tourist/home` | `home/presentation/guide|tourist` | Projection birlestirir, repository sahiplenmez |
| `screens/tourist/explore` | `discovery/presentation/tourist` | Tour/guide search sonuclarini ayri state'lerle sunar |
| `screens/tourist/booking`, `reservations`, `trips` | `reservation/domain`, `reservation/data/mock`, `reservation/presentation` | Snapshot ve `reservationId` otoritesi |
| `screens/tourist/reviews` ve review mutation modelleri | `review/domain|presentation` | Review repository Adim 6'da eklenecek |
| `screens/tourist/payment` | `payment/presentation` ve gecici `payment/data/mock` | Hosted WebView/status entegrasyonu burada |
| Tourist `profile/account/savedcards` ve kart metadata modelleri | `payment/presentation/savedpaymentmethod` ve gecici `payment/data/mock` | Native ham kart formu Adim 6'da kaldirilir |
| `screens/common/moneyaction` | `wallet/presentation/components` | Yalniz money akislarinda gercek ortak UI |
| `screens/guide/earnings`, `wallet`, `finance`, guide `bankaccounts` | `wallet/presentation/guide`, `wallet/data/mock` | Earnings/bank/withdrawal tek feature |
| `screens/tourist/finance`, `wallet` | `wallet/presentation/tourist`, `wallet/data/mock` | Wallet/transaction; kart metadata payment'a ayrilir |
| `screens/common/chat` | `chat/domain`, `chat/data/mock`, `chat/presentation/list|detail|components` | Tek guide-tourist sohbet kaynagi; feature icinde `common` acilmaz |
| `screens/guide/notifications`, iki rol notification settings | `notification/presentation/guide|tourist|settings|components` | FCM/REST sonradan ayni feature'a baglanir |

### Ortak Yapi Sinirlari

Feature-first geciste asagidaki yapilar kopyalanmayacak veya role paketlerine
dagitilmayacaktir:

1. `Tour` ve `TourSession` tek `tour/domain` kaynagidir. Guide karti, tourist
   popular karti, public detail ve reservation snapshot farkli mapper ile
   uretilir; ayni UI modeli veri otoritesi yapilmaz.
2. `TourDetailContent` `tour/presentation/detail`, tekrar kullanilan card
   parcalari `tour/presentation/components` altinda
   kalir. Guide public preview, guide management detail ve tourist detail mode
   ile aksiyonlarini disaridan alir; role-specific ViewModel ortak renderer'a
   tasinmaz.
3. Chat list/detail akislarina gore `chat/presentation/list|detail`, tekrar
   kullanilan bubble/input `chat/presentation/components` altindadir. Guide ve
   tourist yalniz shell/destination callback farki tasir.
4. Guide level/performance/profile ozeti `profile` feature'indadir. Tourist'in
   rehber profilini gormesi bu modelleri tourist profile paketine kopyalamaz.
5. Location/language picker `common/location`; currency/date genel formatter
   `common/ui/formatting`; tour'a ozel tarih etiketi `tour/presentation/components`
   altindadir.
6. Money action bottom sheet `wallet/presentation/components` altindadir. Kart
   provider metadata modeli `payment`, banka hesabi modeli `wallet` sahibi
   olarak kalir; ortak bottom sheet yalniz UI projection'i alir.
7. Help/legal ekranlari `profile/presentation/account` altindadir;
   role-specific FAQ/clause listeleri gerekliyse yalniz veri listesi olarak
   guide/tourist altinda kalir.
8. App scaffold guide/tourist navigation shell'lerinde; topbar ve bottombar
   `navigation/components` altindadir. Her feature veya ekran icin yeni
   scaffold olusturulmaz.

### Bagimlilik Yonu

Izin verilen temel yon:

```text
navigation -> feature presentation + feature domain kimlik sozlesmeleri
ust seviye di -> common teknik adapter'lar + feature data/domain binding'leri
feature presentation -> ayni feature domain + common ui/result
feature data -> ayni feature domain + common network/storage/result
feature domain -> Kotlin/JDK + gerekli saf common result modelleri
common -> hicbir feature veya navigation
```

Ek kurallar:

- Presentation Retrofit API, DTO, repository impl veya mock store import
  etmez. ViewModel domain repository interface'ine baglanir. Bu kural gercek
  repository Adim 6'da yazildigi anda uygulanir; salt package refactor'u icin
  MVP store etrafina gecici interface eklenmez.
- Mevcut ViewModel -> somut MVP store bagimliligi, package refactor'u ile daha
  kotu veya feature'lar arasi hale getirilmeden aynen tasinan tek bilincli gecis
  istisnasidir. Bu istisna hedef mimarinin parcasi sayilmaz ve ilgili feature
  Adim 6'da gercek repository'ye gectigi anda sona erer.
- Bir feature baska feature'in `data` paketini import etmez.
- Cross-feature kullanim yalniz domain contract/model veya acikca paylasilan
  kucuk presentation renderer'i uzerinden tek yonlu olur. Baska feature'in
  ekran ViewModel'i, role-specific UiState'i veya internal component'i
  kullanilmaz.
- `home` guide tarafinda tour/profile/wallet/notification; tourist tarafinda
  tour/profile repository Flow'larini birlestirebilir. Bunun icin ayri
  `HomeRepository` acilmaz.
- `discovery`, tour ve public profile repository'lerini kullanir; arama
  sonucunu kendi local mock listesiyle ikinci kaynak yapmaz.
- `reservation`, public tour/session kimligini kullanir ve payment akisini
  callback/domain contract ile baslatir. `payment`, reservation UI'sini bilmez.
- `wallet`, saved payment method secimi icin payment domain projection'ini
  kullanabilir; payment wallet bakiyesini sahiplenmez.
- Notification semantic hedefi ham route string tasimaz. Notification target'i
  feature tarafinda typed anlami tasir, merkezi `navigation` bunu destination'a
  cevirir.
- Hilt binding feature `di` paketindedir. Yalniz birden fazla feature'in teknik
  adapter'larini birlestiren wiring ust seviye `di`de bulunur.
- Feature composable ve ViewModel'lari `NavController` veya graph registration
  bilmez. `SavedStateHandle` ile yalniz type-safe destination contract'indan
  basit ID okumak, merkezi navigation ile feature arasindaki bilincli ve dar
  istisnadir; tam model veya UI metni route argumani yapilmaz.

### MVP Store Tasima ve Kaldirma Plani

Salt package refactor'unda mock store'larin davranisi degistirilmeyecek ve
ekranlar bos birakilmayacaktir. Gecici hedefleri ile final sahipleri sunlardir:

| Mevcut MVP kaynagi | Refactor sonrasi gecici yer | Adim 6 final kaynagi |
| --- | --- | --- |
| `TourCatalogStore` ve mock/timeline | `tour/data/mock` | `GuideTourRepository` + `TourDiscoveryRepository`; ortak tour domain |
| `GuideProfileSharedStore`, `GuidePerformanceStore` | `profile/data/mock` | `GuideProfileRepository` |
| `TouristReservationStore` | `reservation/data/mock` | `ReservationRepository` |
| `TouristPaymentStore` | `payment/data/mock` | `PaymentRepository` |
| `TouristWalletStore` | Gecici olarak `wallet/data/mock` | Wallet kismi `WalletRepository`, kart kismi `SavedPaymentMethodRepository` |
| `GuideWalletStore` | `wallet/data/mock` | `GuideFinanceRepository` |
| `ChatStore` ve `ChatMockData` | `chat/data/mock` | `ChatRepository` REST + STOMP |
| Guide notification mock state | `notification/data/mock` veya ViewModel fixture'i | `NotificationRepository` + FCM |

- Store birden fazla final repository'ye ayriliyorsa veriler refactor sirasinda
  kopyalanmaz. Gercek sozlesme baglanana kadar tek mevcut store korunur.
- Bir ekran canonical repository'ye gectiginde ilgili runtime mock ayni dilimde
  kaldirilir; `if backend yoksa mock` fallback'i kalmaz.
- Compose preview ihtiyaci runtime store ile degil feature
  `presentation/preview` fixture'i ile karsilanir.
- `SandboxCardCatalog`, native Add Card formu ve buna ait input testleri
  payment provider entegrasyonu tamamlaninca kaldirilir. Salt package
  refactor'unda davranis degistirmemek icin gecici olarak `payment/data/mock`
  altina tasinabilir.

### Navigation Koruma Kurali

- Root, auth, chat, guide ve tourist navigation dosyalari mevcut merkezi
  `navigation` hiyerarsisinde kalir; feature paketlerine tasinmaz.
- `GuideMateNavigation` root composition'i, guide/tourist navigation shell'leri
  tek scaffold ve bagimsiz back-stack'i, alt NavGraph dosyalari destination
  registration'i sahiplenmeye devam eder.
- Her ekran icin ayri navigation dosyasi acilmaz; yalniz mevcut gercek
  cok-ekranli account, tour, finance ve payment akislarinin ayrimi korunur.
- Type-safe `@Serializable` route yapisi korunur. String route birlestirme,
  model parcel etme veya UI metnini argument yapma geri getirilmez.
- Guide tour detail/edit kimligi planlandigi gibi `tourId + sessionId`, tourist
  public detail `sessionId`, satin alinmis gezi detayi `reservationId` olur.
  Bu kimlik davranisi package refactor'unda degil ilgili Adim 6 dikey
  entegrasyonunda degistirilir.
- `navigateTo`, `navigateBottomBar` ve root gecis semantigi korunur. Yeni
  extension yalniz tekrar eden farkli bir back-stack semantigi gercekten
  dogarsa eklenir.
- Feature screen'leri scaffold veya NavController sahiplenmez; topbar/bottombar
  config'i shell tarafinda kalir.

### Paket Derinligi ve Isimlendirme

- Package adlari kucuk harf ve is sorumlulugunu anlatir: `tour`, `reservation`,
  `savedpaymentmethod`, `bankaccount`, `notificationsettings` gibi.
- Rol, feature kokunde degil presentation altinda kullanilir:
  `tour/presentation/guide` ve `tour/presentation/tourist`.
- `GuideTourPublishStep1LocationDateScreen` gibi mevcut acik ekran adlari
  davranis-koruyucu tasimada korunabilir. Sadece package degisti diye genis
  isimlendirme kampanyasi yapilmaz.
- `Dto`, `Domain`, `UiModel`, `UiState`, `Repository`, `RepositoryImpl` ekleri
  katman belirsizligini giderdigi yerde kullanilir; her modele zorla eklenmez.
- Bir dosyanin yalniz bir data class ve ona cok yakin enumlari varsa birlikte
  kalabilir. Sirf dosya sayisini artirmak veya azaltmak kalite hedefi degildir.
- Derin `profile/account/x/model/viewmodel/components` zinciri yerine ekran
  akisi ayni pakette okunabiliyorsa daha sade
  `profile/presentation/guide/about` gibi sahiplik kullanilir.

### Fiziksel Refactor Uygulama Sirasi

Bu sira backend entegrasyon sirasi degil, yalniz package tasimasini guvenli
tutma sirasidir:

1. Mevcut git durumu, compile/format ve calisan UI akislarina ait baseline
   kaydedilir. Kullanici degisiklikleri ayrilir.
2. `common/result`, `common/ui`, `common/storage`, `common/network` ve
   `common/location` davranis degistirmeden tasinir; ilgili unit test package'lari
   ayni anda guncellenir.
3. Shell component'leri `navigation/components` altina alinir; merkezi
   navigation hiyerarsisi, manifest ve startup siniflari tasinmaz. Ust seviye
   DI yalniz app composition sorumluluguyla korunur.
4. Calisan gercek dikey dilim olan `auth` data/domain/presentation/di
   birlikte tasinir. Pass-through auth use-case'leri kaldirilip ViewModel
   bagimliliklari `AuthRepository` arayuzune alinir; auth davranisi degismez.
5. `media`, `profile` ve `tour` sahiplikleri tasinir. Ortak tour/profile
   modelleri once hedefe alinip guide/tourist importlari ayni dilimde
   guncellenir; kopya model birakilmaz.
6. `reservation`, `review`, `payment` ve `wallet` tasinir. Kart metadata,
   wallet ve bank account sinirlari yukaridaki matrise gore ayrilir; mock
   davranisi korunur.
7. `chat` ve `notification` ortak role akisiyla birlikte tasinir.
8. `home` ve `discovery` en son tasinir; boylece baglandiklari feature
   sahiplikleri onceden sabitlenmis olur.
9. Merkezi navigation importlari yeni feature presentation yerlerine gore
   guncellenir; destination, graph, route semantigi ve paket sahipligi korunur.
10. Eski `screens`, genel `data`, genel `domain` ve genel `components`
    klasorleri bosaldikca silinir. Ust seviye `navigation` ile composition
    `di` korunur. Bos paket,
    compatibility wrapper veya duplicate type birakilmaz.

Her madde ayri davranis-koruyucu dilimdir. Bir dilim compile olmadan sonraki
feature'a gecilmez ve package tasimasi ile endpoint entegrasyonu ayni committe
biriktirilmez.

### Testlerin Tasima Kurali

- Unit test package'i urettigi sinifin yeni package'ini birebir izler.
- `CurrencyFormatterTest` `common/ui/formatting`; guide level testleri `profile`;
  tour lifecycle/mapper/store testleri `tour`; IBAN ve wallet testleri
  `wallet`; reservation/review testleri ilgili feature altina tasinir.
- Mock store testleri store kaldirilana kadar korunur. Gercek repository
  entegrasyonunda bunlar repository/mapper/ViewModel testleriyle degistirilir;
  ayni davranisi iki kez test eden eski mock testi tutulmaz.
- `ExampleUnitTest` ve `ExampleInstrumentedTest` yalniz Android Studio sablonu
  olarak kaldigi dogrulanirsa refactor temizliginde silinir.
- Salt package tasimasi en az `ktfmtCheck`, `compileDebugKotlin` ve mevcut unit
  testleri gecmeden tamamlanmis sayilmaz. Tasarim degismedigi icin kritik guide
  ve tourist akislarinda UI smoke kontrolu de yapilir.

### Adim 5 Sonucu ve Kapisi

Tek modullu, dogrudan feature koklu hedef mimari; common/navigation/di siniri,
tum mevcut paketlerin hedef sahipligi, ortak kod kurali, bagimlilik yonu, mock
gecis plani, navigation siniri, isimlendirme ve test tasima kurali
kesinlesmistir.

Bu adim paket refactor'unu uygulamamis, yalniz uygulama planini tamamlamistir.
Adim 6 ve Adim 7 planlari tamamlanmadan ve kullanici acik komut vermeden Kotlin
kaynaklari toplu tasinmayacaktir. Sonraki hazirlik calismasi Adim 6'dir:
feature'larin backend'e hangi dikey sirayla baglanacagi ve her mock kaynagin
hangi tamamlanma kapisinda kaldirilacagi kesinlestirilecektir.

## Adim 6 - Fazlara Ayrilmis Dikey Entegrasyon Sirasi

Durum: PLAN TAMAMLANDI. KOD UYGULANMADI.

Bu adim backend entegrasyonunun hangi sirayla uygulanacagini, her fazin
hangi kapidan gecerek tamamlanacagini ve runtime mock kaynaklarin ne zaman
kaldirilacagini kesinlestirir. Bu bolum bir toplu kod yazma talimati degildir.
Kotlin kaynaklari ancak kullanici acik uygulama komutu verdiginde, asagidaki
sira ve kapilar korunarak degistirilecektir.

Her faz ayni zamanda tek bir dikey entegrasyon dilimidir. Faz numarasi calisma
sirasini, dikey dilim ise contract'tan UI ve temizlige kadar ayni feature'in
uctan uca kapatilmasini anlatir.

### Faz Kurali

Her feature asagidaki sira ile uctan uca tamamlanir:

1. Guncel backend OpenAPI endpoint/request/response/error sozlesmesi yeniden
   dogrulanir.
2. Feature'a ozel Retrofit API ve DTO'lar `data/remote` altinda yazilir.
3. DTO -> domain ve domain intent -> request mapper'lari yazilir; DTO
   presentation'a sizmaz.
4. Yalniz gercek tuketicisi olan repository interface'i `domain/repository`,
   implementation'i `data/repository` altinda kurulur ve Hilt binding'i
   feature `di` paketinde yapilir.
5. ViewModel repository arayuzune baglanir; loading/empty/content/error,
   pagination ve mutation sonuclari typed state ile yonetilir.
6. Navigation yalniz gerekli ID'leri tasir; ekran, DTO veya UI modeli route
   argumani yapilmaz.
7. Basarili mutation sonrasi Adim 4'teki canonical refresh matrisi uygulanir.
8. Repository/mapper/ViewModel davranisi dogrulanir; ekranin mevcut tasarimi
   ve kullanici akisi smoke kontrolunden gecirilir.
9. Ancak bu kapilar tamamlaninca ilgili runtime mock/store ve bosa dusen
   mapper/model/fonksiyonlar ayni dilimde kaldirilir.

Yeni iki satirlik use-case eklenmez. ViewModel repository arayuzunu dogrudan
kullanir. Use-case ancak birden fazla repository'yi tekrar kullanilan anlamli
bir is kuralinda orkestre eden somut ihtiyac final denetimde kanitlanirsa
degerlendirilir. Generic base repository, base ViewModel, global event bus veya
tek tip UiState framework'u kurulmaz.

### Her Faz Icin Zorunlu Tamamlanma Kapisi

Bir faz asagidakilerin tamami saglanmadan tamamlandi sayilmaz:

- Endpoint ve DTO alanlari guncel OpenAPI ile birebir uyumludur.
- Kimlik ve yetki client'ta tahmin edilmez; JWT/current-user ve backend object
  authorization sonucu kullanilir.
- Para, kapasite, lifecycle, refund, unread ve puan gibi authoritative state
  Android tarafinda uydurulmaz.
- Repository interface'i presentation'in somut data adapter'ini bilmesini
  engeller; feature data paketi baska feature'in data paketine baglanmaz.
- Bu fazda gercek repository akisina gecirilen feature'in `data` katmani,
  `presentation` model, mapper veya component'ini import etmez. Fazi henuz
  gelmeyen gecici mock feature'lar yalniz kendi fazlarina kadar istisnadir.
- Hata sonucu merkezi `DataResult/AppError` sozlesmesine map edilir; ham backend
  mesaji veya HTTP kodu ViewModel'da yorumlanmaz.
- Liste ekraninda loading, empty, retry, refresh ve gerekiyorsa append-loading
  gorunur ve test edilebilir durumdadir.
- Mutation sonrasi ilgili canonical kaynaklar yeniden okunur veya gercek zamanli
  event ile invalidate edilir; local sayac `+1/-1` ile otorite uretilmez.
- Runtime mock fallback kalmaz. Preview ve test verisi gerekiyorsa yalniz
  `presentation/preview` veya test fixture'i olur.
- `ktfmtCheck`, `compileDebugKotlin` ve ilgili unit testleri gecer; mevcut
  tasarim ve navigation davranisi korunur.

### Faz Yol Haritasi

| Faz | Kapsam | Ana cikis |
| --- | --- | --- |
| 0 | Davranis-koruyucu feature-first refactor | Hedef paket agaci, temiz importlar, auth pass-through use-case temizligi |
| 1 | Ortak teknik temel ve mevcut auth | Calisan session/network/error omurgasi |
| 2 | Media | Multipart upload ve local/remote `GuideMateImage` |
| 3 | Guide profile ve public guide | Tek canonical profile/performance kaynagi |
| 4 | Ortak tour domain ve guide tour yonetimi | Private tour/session/dashboard repository akisi |
| 5 | Tourist discovery, public tour ve home | Search/popular/detail ve gercek filter/pagination |
| 6 | Reservation ve trips | Reservation snapshot/detail/cancel akisi |
| 7 | Review | Eligibility, submit ve projection refresh |
| 8 | Wallet ve saved payment method | Canonical balance/history ve provider metadata |
| 9 | Payment, hosted checkout ve top-up | Quote/WebView/polling ve canonical sonuc |
| 10 | Guide finance | Earnings, bank account ve withdrawal |
| 11 | Chat | REST/STOMP, unread ve resync |
| 12 | Notification | REST preferences/history, FCM ve semantic navigation |

### Faz Durum Takibi

Fazlar bir aralik veya tek toplu gorev olarak izlenmeyecektir. `Faz 0` ile
`Faz 12` arasindaki her fazin kendi kapsami, durumu, kapanis kaniti ve acik
capraz-faz kaydi bulunur. Planin yazilmis olmasi, fazin uygulanmis veya
tamamlanmis oldugu anlamina gelmez.

Kullanilabilecek durumlar:

- `BEKLIYOR`: Uygulama calismasi baslamadi.
- `DEVAM EDIYOR`: Fazin kodlama veya dogrulama calismasi suruyor.
- `KISMEN TAMAMLANDI`: Fazin kendi dikey dilimi calisiyor ancak daha sonraki
  bir fazin kapatacagi acik entegrasyon veya E2E kaniti bulunuyor.
- `TAMAMLANDI`: Fazin kendi kapilari gecti ve ona ait acik capraz-faz kaydi
  kalmadi.
- `BLOKE`: Dis hesap, secret, backend contract'i, servis veya kullanici karari
  olmadan ilerlenemiyor.

Guncel durumlar:

| Faz | Durum | Acik takip |
| --- | --- | --- |
| Faz 0 - Feature-first refactor | `TAMAMLANDI` | Format, compile, unit test, lint ve debug APK kapilari gecti |
| Faz 1 - Ortak teknik temel ve auth | `TAMAMLANDI` | Auth/OpenAPI uyumu, ortak pagination, hata parsing, LAN ve kalite kapilari dogrulandi |
| Faz 2 - Medya | `KISMEN TAMAMLANDI` | Medya altyapisi ve loader tamam; avatar Faz 3, tour cover Faz 4 E2E baglantisini bekliyor |
| Faz 3 - Guide profile ve public guide | `BEKLIYOR` | Uygulama ve dogrulama baslamadi |
| Faz 4 - Tour ve guide tour yonetimi | `BEKLIYOR` | Uygulama ve dogrulama baslamadi |
| Faz 5 - Tourist discovery ve public tour | `BEKLIYOR` | Uygulama ve dogrulama baslamadi |
| Faz 6 - Reservation ve trips | `BEKLIYOR` | Uygulama ve dogrulama baslamadi |
| Faz 7 - Review | `BEKLIYOR` | Uygulama ve dogrulama baslamadi |
| Faz 8 - Wallet ve saved payment method | `BEKLIYOR` | Uygulama ve dogrulama baslamadi |
| Faz 9 - Payment, hosted checkout ve top-up | `BEKLIYOR` | Uygulama ve dogrulama baslamadi |
| Faz 10 - Guide finance | `BEKLIYOR` | Uygulama ve dogrulama baslamadi |
| Faz 11 - Chat | `BEKLIYOR` | Uygulama ve dogrulama baslamadi |
| Faz 12 - Notification | `BEKLIYOR` | Uygulama ve dogrulama baslamadi |

### Capraz-Faz Bagimlilik ve Geri Donus Kurali

Bir fazin daha sonraki bir fazla baglantili isi bilincli olarak ertelenebilir;
ancak bu eksik sessizce birakilamaz ve erken faz `TAMAMLANDI` sayilamaz.

- Ertelenen her is icin kaynak faz, isi kapatacak hedef faz, erteleme nedeni,
  etkilenen kullanici akisi, kapanis olcutu ve durum yazilir.
- Yeni bir faza baslamadan once onceki fazlarin acik takipleri taranir. Mevcut
  faz bunlardan birini kapatabiliyorsa is kendi kapsamina eklenir.
- Sonraki faz, onceki fazdaki eksigi tamamladiginda yalniz mevcut faz degil,
  onceki fazin durumu ve kapanis kaniti da ayni calismada guncellenir.
- Sonraki bir contract veya urun karari, tamamlanmis bir fazin varsayimini
  bozarsa o faz yeniden `DEVAM EDIYOR` veya `KISMEN TAMAMLANDI` durumuna alinir.
- Bir fazi erken tamamlanmis gostermek icin gecici duplicate repository, mock,
  singleton veya UI otoritesi eklenmez.
- Final tamamlanma icin butun fazlar `TAMAMLANDI` olmali ve acik capraz-faz
  kaydi kalmamalidir.

Capraz-faz takip kaydi su formatta tutulur:

| Kayit | Kaynak faz | Hedef faz | Bagimli is | Kapanis kaniti | Durum |
| --- | --- | --- | --- | --- | --- |
| MEDIA-PROFILE-01 | Faz 2 | Faz 3 | Avatar local URI upload sonucu `mediaAssetId` ile profile patch'e baglanacak; hata durumunda eski avatar korunacak | Guide avatar degisikliginin own/public profile'da iki cihazdan canonical URL ile gorunmesi | `BEKLIYOR` |
| MEDIA-TOUR-01 | Faz 2 | Faz 4 | Cover local URI once upload edilecek; create/change request yalniz `mediaAssetId` tasiyacak ve terk edilen draft kontrollu silinecek | Publish/edit sonrasi guide ve tourist tour gorunumlerinin ayni canonical cover URL'yi gostermesi | `BEKLIYOR` |

Bu tablo, faz uygulamalari sirasinda gercek bir bagimlilik ortaya ciktiginda
somut kayitlarla guncellenir. Yalniz olasi bir ihtimal icin hayali borc veya
gereksiz katman uretilmez.

### Faz 0 - Davranis-Koruyucu Feature-First Refactor

Durum: `TAMAMLANDI` (2026-08-21).

Backend baglantisi ile binlerce package/import tasimasi ayni degisiklikte
karistirilmayacaktir. Once Adim 5'teki fiziksel refactor uygulanir:

- Kokte `auth`, `media`, `profile`, `tour`, `discovery`, `home`,
  `reservation`, `review`, `payment`, `wallet`, `chat`, `notification`,
  `common`, `navigation` ve `di` sahiplikleri kurulur.
- Mevcut navigation hiyerarsisi merkezi kalir; route ve back-stack davranisi
  degismez.
- Mevcut mock/store davranisi bu fazda degistirilmez ve ekranlar bos
  birakilmaz.
- Sekiz pass-through auth use-case kaldirilir. Auth ViewModel'lari mevcut
  `AuthRepository` arayuzunu dogrudan kullanir; `EmailPolicy` ve
  `NumericPasswordPolicy` korunur.
- Kullanilmayan eski paket, import, dosya ve bos klasorler kontrollu temizlenir.
- Refactor ayri format/compile/test/smoke kapisiyla kapanir. Bu kapi gecmeden
  gercek backend feature entegrasyonuna baslanmaz.

Kapanis kaniti:

- Feature sahiplikleri fiziksel paketlere tasindi; merkezi navigation, mevcut
  UI kaynaklari, route/back-stack davranisi ve runtime mock davranisi korundu.
- Tek ekran akislari icindeki yapay tek dosyalik `model`, `viewmodel`, `content`
  ve `components` zincirleri secici olarak duzlestirildi. Auth sifre degistirme,
  bildirim ayarlari, rehber profil onizleme, tur yayinlama adimlari ve rehber
  kazanc akislari kendi anlamli presentation paketlerinde toplandi.
- `PopularTourCardUiModel`, Android presentation ayrintilari tasidigi icin
  `tour/domain` sinirindan cikarilip `tour/presentation/model` sahipligine
  tasindi. Modelin alanlari ve ekran davranisi degistirilmedi.
- User/onboarding tercihleri auth sahipligine, installation ID ortak storage
  sahipligine ayrildi; mevcut DataStore dosyasi ve anahtarlari degismedi.
- Sekiz pass-through auth use-case kaldirildi; auth ViewModel'lari
  `AuthRepository` arayuzunu dogrudan kullanmaya basladi.
- Eski package/importlar, gecici refactor scriptleri ve bos paket klasorleri
  temizlendi. `media` icin Faz 2'den once bos placeholder paket uretilmedi.
- `./gradlew ktfmtCheck compileDebugKotlin testDebugUnitTest` basarili.
- `./gradlew lintDebug assembleDebug` basarili.
- Debug APK `Pixel_8a` emulatore yuklendi; `MainActivity` cold start basarili ve
  uygulama sureci calisir durumda dogrulandi.
- Faz 0'a ait acik capraz-faz kaydi bulunmuyor; runtime mocklarin ilgili
  entegrasyon fazina kadar korunmasi planli davranistir, Faz 0 eksigi degildir.

### Faz 1 - Ortak Teknik Temel ve Mevcut Auth

Durum: `TAMAMLANDI` (2026-08-21).

Amac yeni auth yazmak degil, calisan gercek auth dikey dilimini hedef
feature-first yapida korumak ve sonraki repository'lerin kullanacagi ortak
teknik omurgayi sabitlemektir.

Yapilacaklar:

- Retrofit/OkHttp composition, `DataResult`, `AppError`, backend error parser,
  token interceptor/authenticator ve pagination response modeli ortak teknik
  sahiplerine tasinir.
- `AuthApi`, auth DTO/mapper/repository implementation, DataStore ve Android
  Keystore session akisi `auth` feature'i altinda korunur.
- `GET /api/v1/auth/me` ile canonical `userId`, e-posta, ad, soyad ve role uygulama
  acilisinda yenilenir.
- LAN base URL BuildConfig/local property uzerinden kullanilir; source'a IP,
  token veya secret yazilmaz.

Kapi:

- Register, normal login, Google login, role selection, refresh, logout,
  password change/reset ve pending verification davranisi gerilemez.
- Kullanici kimligi icin hardcoded role veya mock current-user bridge kalmaz.
- Bu fazda kaldirilacak runtime auth mock'u yoktur; mevcut gercek altyapi
  korunur.

Kapanis kaniti:

- Retrofit/OkHttp/Gson composition'i ust seviye `di/NetworkModule.kt`, auth API
  provider'i feature `auth/di`, hata/result modelleri `common` sahipliginde
  dogrulandi; gereksiz yeni repository veya use-case katmani eklenmedi.
- Auth API, `AuthResponse` ve `CurrentUserResponse`; canli OpenAPI'deki
  `userId`, `email`, `firstName`, `lastName`, `roleSelected` ve `role`
  sozlesmesiyle birebir eslesti.
- Uygulama acilisinda cached kullanici sonrasinda `GET /api/v1/auth/me` ile
  canonical kullanici yenilemesi, terminal refresh hatasinda session temizligi
  ve gecici ag/sunucu hatasinda cached oturumun korunmasi dogrulandi.
- Backend ortak `PageResponse<T>` sekline karsilik gelen
  `common/network/model/ApiPageResponse.kt` eklendi; feature DTO'lari ortak
  pakete tasinmadi.
- Backend bos validation listesinde `fieldErrors` alanini JSON'dan
  cikardiginda Android parser'in `Unknown` hataya dusme riski giderildi.
  Eksik alan ve dolu validation listesi icin kalici `ApiErrorParserTest`
  senaryolari eklendi.
- Canli local backend PostgreSQL'e baglandi, 13 Flyway migration'i dogruladi ve
  OpenAPI dokumani LAN adresinden alindi. Zararsiz gecersiz login istegi
  yapilandirilmis `401 INVALID_CREDENTIALS` govdesi dondurdu.
- Android ve backend'in Git disi local base URL degerleri Mac'in guncel
  `192.168.68.102` LAN adresinde esitlendi; emulatorden `8080` port erisimi
  basarili oldu. Kaynak koda IP, token veya secret yazilmadi.
- `./gradlew ktfmtCheck compileDebugKotlin testDebugUnitTest lintDebug
  assembleDebug` basarili.
- `bash mvnw -q test` basarili; Testcontainers PostgreSQL 18.6 uzerinde Flyway
  `V1-V13`, auth lifecycle, guvenlik ve OpenAPI kontrolleri gecti.
- Guncel debug APK emulatore yuklendi ve launcher cold start basarili oldu.
- Faz 1'e ait acik capraz-faz kaydi bulunmuyor; sonraki business repository
  entegrasyonlari Faz 2 ve devaminda kendi sozlesmeleriyle eklenecek.

### Faz 2 - Medya

Durum: `KISMEN TAMAMLANDI` (2026-08-21).

Medya profil ve turdan once tamamlanir; cunku avatar ve cover kalici backend
referansi olmadan profil/tur mutation'lari canonical hale gelemez.

Yapilacaklar:

- `MediaRepository`, media API/DTO/mapper ve multipart upload adapter'i eklenir.
- Kamera/galeri sonucu local URI olarak form state'inde kalir; upload sonrasi
  donen `mediaAssetId` mutation request'ine verilir.
- Android dosya boyutu ve gorunen metin backend ile uyumlu 5 MB olur; JPEG,
  PNG ve WebP siniri korunur.
- Coil Compose eklenir. Ortak `GuideMateImage` local `content://`/`file://`
  ile HTTP/HTTPS URL'yi tek noktadan, mevcut loading/error drawable fallback
  tasarimini koruyarak render eder.
- Token URL query'sine eklenmez. Korumali medya gerekiyorsa OkHttp request
  header mekanizmasi kullanilir.

Mock temizleme kapisi:

- Local URI kalici `imageUrl` gibi saklanmaz.
- Profil ve tur preview fixture'larindaki drawable fallback korunabilir; ancak
  runtime canonical medya yerine gecemez.
- Upload/read/delete, process retry ve `MEDIA_IN_USE` hata davranisi
  dogrulanmadan local media kodu kaldirilmaz.

Uygulananlar ve kapanis kaniti:

- `media` feature'i altinda `MediaApi`, DTO/mapper, `MediaRepository` ve gercek
  implementation eklendi. Yeni use-case veya gecici store uretilmedi.
- `MediaPartFactory` Android dosya erisim sinirini repository'den ayirdi;
  `ContentResolverMediaPartFactory` local `content://`/`file://` verisini
  bellege topluca almadan OkHttp multipart body'sine aktarir.
- Kamera/galeri secimi ve multipart hazirligi ayni dosya imzasi kontrolunu
  kullanir. Android siniri backend ile ayni 5 MB; desteklenen formatlar JPEG,
  PNG ve WebP'dir.
- Coil Compose `3.4.0` eklendi. Ortak `GuideMateImage` local URI ve HTTP/HTTPS
  kaynaklari mevcut drawable placeholder/error/fallback tasarimini koruyarak
  tek noktadan yukler.
- Coil backend'in owner-only draft medyasini okuyabilsin diye mevcut OkHttp
  istemcisini kullanir. Authorization ve refresh davranisi yalniz yapilandirilmis
  GuideMate backend origin'inde calisir; baska hostlara token tasinmaz ve URL
  query'sine token eklenmez.
- Backend media hata kodlari merkezi Android hata sozlesmesine eklendi;
  `MEDIA_IN_USE`, format, boyut, bulunamama, storage ve purpose hatalari
  yerellestirilmis kullanici mesajlarina map edilir.
- Media mapper, repository upload/local validation/delete error, dosya imzasi,
  backend-origin siniri ve refresh hata tasima davranisi birim testleriyle
  dogrulandi.
- `./gradlew ktfmtCheck compileDebugKotlin testDebugUnitTest lintDebug
  assembleDebug` basarili. Backend `bash mvnw -q test` PostgreSQL 18.6,
  Flyway `V1-V13`, media validator/storage/access/cleanup ve OpenAPI/security
  testleriyle basarili.
- Calisan emulator bulunmadigi icin bu calismada launcher ve gercek cihaz
  media E2E smoke testi yapilmadi. Avatar mutation'i `MEDIA-PROFILE-01`, tour
  cover mutation'i `MEDIA-TOUR-01` kayitlariyla hedef fazlara baglandi; bu iki
  kayit kapanmadan Faz 2 `TAMAMLANDI` yapilmayacak.

### Faz 3 - Guide Profil ve Public Guide Projection

Yapilacaklar:

- `GuideProfileRepository` own profile, public profile, guide search ve top
  projection'larini kapsar; farkli UI modelleri mapper ile ayni canonical
  domain kaynagindan uretilir.
- About, profile, preview, tourist public guide gorunumu ve guide
  level/performance alanlari repository Flow'undan beslenir.
- Avatar degisikligi Faz 2 media sonucu ile profile patch'e baglanir.
- Sabit ad, e-posta, avatar, guide ID, puan ve seviye degerleri temizlenir.
- Tourist guide search/top state'leri sayfali gercek sonuc, empty ve retry
  davranisi alir.

Mock temizleme kapisi:

- `GuideProfileSharedStore` ve `GuidePerformanceStore`, own/public profile,
  preview ve guide summary ekranlari gercek repository'den beslendigi ayni
  fazda kaldirilir.
- Profil preview icin ayri mutable veri kaynagi tutulmaz.
- Compose preview verileri immutable fixture olarak kalabilir.

### Faz 4 - Ortak Tour Domain ve Guide Tour Yonetimi

Yapilacaklar:

- Ortak `Tour`/`TourSession` domain modelleri backend lifecycle, version,
  capacity, booked count, rating, review count ve session earning alanlariyla
  eslenir.
- `GuideTourRepository`; private list/detail, dashboard, publish, change
  request, session create/update/open/close/cancel ve archive islemlerini
  kapsar.
- Turlarim sekmeleri backend pagination ve siralamasini kullanir.
- Guide detail/edit route'u `tourId` ve secili `sessionId`yi birlikte tasir.
- Edit ViewModel content change request ile session update'i ayirir; kismi
  basari tam basari gibi gosterilmez.
- Dashboard aktif/onay bekleyen/tamamlanan/katilimci/puan/kazanc alanlari
  backend projection'indan gelir; liste boyutu veya local sayac kullanilmaz.
- Publish/edit cover akisi Faz 2 `MediaRepository` sonucunu kullanir.

Mock temizleme kapisi:

- `TourCatalogStore`un guide mutation ve private guide projection sorumlulugu;
  ACTIVE/REVIEW/PAST liste, detail, publish/edit ve dashboard repository'ye
  gectiginde kaldirilir.
- Public tourist projection heniz Faz 5'e gecmediyse ayni store'un kalan
  public mock kismini kopyalamak yerine gecici dar fixture korunabilir. Faz 5
  sonunda store tamamen silinir.
- Switch/open/close basarisizligi typed operation sonucu ile gorunur olmadan
  Boolean/sessiz fallback kaldirilmaz.

### Faz 5 - Tourist Discovery, Public Tour ve Home

Yapilacaklar:

- `TourDiscoveryRepository`; popular, search, public tour detail ve public
  session endpoint'lerini kapsar.
- Tourist popular kart, explore sonucu ve detail ayni backend Tour/Session
  kaynagindan farkli UI mapper'lariyla uretilir.
- Explore query; metin, kategori, ulke/sehir, dil, tarih, fiyat ve pagination
  parametrelerini typed request mapper ile gonderir.
- Guide search/top Faz 3 profile repository'sinden, tour search/popular bu
  repository'den gelir; `discovery` ikinci veri kaynagi olusturmaz.
- Tourist home, tour ve profile projection'larini UI seviyesinde birlestirir;
  `HomeRepository` acilmaz.
- Satinalma butonunun gorunurlugu backend session state/capacity ve mevcut
  reservation bilgisiyle uretilir; checkout aninda backend tekrar dogrular.

Mock temizleme kapisi:

- `TourCatalogStore` ve `TourCatalogMockData` public runtime kaynagi olmaktan
  tamamen cikarilir.
- Tourist home/popular/explore/detail sabit tour, guide ve image verileri
  kaldirilir.
- Filter uygula aksiyonu gercek listeyi yenilemeden discovery entegrasyonu
  tamamlanmis sayilmaz.

### Faz 6 - Reservation ve Trips

Yapilacaklar:

- `ReservationRepository`; kendi reservation listesi, detail ve cancellation
  islemlerini kapsar.
- Upcoming/past listesi reservation state ve backend siralamasindan uretilir.
- Satin alma snapshot'i, participant count, unit/total amount, cancellation ve
  refund bilgisi reservation domain modelinde korunur.
- Trips detail route'u public `sessionId` yerine `reservationId` tasir; satin
  alinmis gecmis sonradan degisen tur icerigiyle ezilmez.
- Tourist cancellation sonucu canonical reservation ve refund state'iyle
  gosterilir; capacity veya para Android'de local olarak otoriter
  degistirilmez.

Mock temizleme kapisi:

- `TouristReservationStore`, upcoming/past/detail/cancel ekranlari gercek
  repository ve reservation ID navigation'ina gectigi ayni fazda kaldirilir.
- Iptal edilmis reservation normal tamamlanmis gezi gibi sunulmaz.
- Payment ile olusan yeni reservation Faz 9 tamamlanana kadar runtime mock
  eklenerek simule edilmez; mevcut reservation listesi backend refetch ile
  yenilenir.

### Faz 7 - Review

Yapilacaklar:

- `ReviewRepository`, tur yorum listesi ve reservation uzerinden tek review
  gonderme sozlesmesini kapsar.
- Eligibility backend reservation response'undan gelir. Android satinalmadigi,
  tamamlanmamis, iptal edilmis veya daha once yorumlanmis reservation icin
  mutation uydurmaz.
- Review basarisinda reservation detail, tour detail/reviews/popular ve guide
  performance/profile/dashboard Adim 4 matrisine gore yenilenir.
- Rating ve guide level Android'de yeniden aggregate edilmez; backend
  projection'i kullanilir.

Mock temizleme kapisi:

- Local review mutation ve manuel yorum listeleri, review formu ve detail
  listeleri gercek repository'ye gectiginde kaldirilir.
- Review gonderme ekrani yalniz tasarim olarak kalip aksiyonu bos birakilirsa bu
  faz tamamlanmis sayilmaz.

### Faz 8 - Wallet ve Kayitli Odeme Yontemleri Temeli

Payment checkout'tan once kullanicinin canonical wallet bakiyesi ve provider
tarafindan donen guvenli kayitli kart metadata'si baglanir.

Yapilacaklar:

- `WalletRepository`; wallet balance ve sayfali transaction history'yi
  kapsar. Iki rol ayni canonical wallet/transaction sozlesmesini role uygun UI
  mapper ile kullanir.
- Transaction tur baglantiliysa backend `referenceTitle`, diger gorunen islem
  metinleri Android string resource'lariyla yerellestirilir.
- `SavedPaymentMethodRepository`; list/default/delete islemlerini kapsar.
- Android yalniz internal ID, maskeli metadata, banka/kart ailesi, son dort
  hane, expiry ve default bilgisini kullanir.
- Para `Long` minor unit ve backend currency code ile tasinir; formatter UI
  sinirinda calisir.

Mock temizleme kapisi:

- `TouristWalletStore`un wallet/transaction kismi gercek `WalletRepository`
  akisina gecince kaldirilir.
- `SandboxCardCatalog`, kart numarasindan banka/marka tahmini, native ham kart
  formu ve bunlara ait runtime state tamamen kaldirilir.
- Provider checkout icindeki kart kaydetme tercihi ve backend
  `SavedPaymentMethodResponse` disinda kart kaydi otoritesi kalmaz.

### Faz 9 - Payment, Hosted Checkout ve Top-Up

Dis on kosullar koddan once kontrol edilir: iyzico Sandbox erisimi, backend
payment secret'lari, guncel Quick Tunnel/callback-webhook adresi ve test
senaryolari. Provider secret'i veya tunnel adresi Android'e yazilmaz.

Yapilacaklar:

- `PaymentRepository`; currency, quote, tour checkout, wallet top-up,
  payment status ve cancel endpoint'lerini kapsar.
- Kullanici once backend quote'unu gorur. Canonical USD ile provider charge
  currency/tutar/fx snapshot'i birbirine karistirilmaz.
- Backend'in hosted URL'si guvenli WebView'da acilir. SSL hatasi atlanmaz,
  `addJavascriptInterface` eklenmez ve callback JSON'u basari sayilmaz.
- WebView lifecycle/callback sonrasinda `paymentId` ile backend status polling
  yapilir. Yalniz canonical `SUCCEEDED` ve tur aliminda `CONFIRMED` reservation
  basari ekrani acar.
- `FAILED`, `CANCELLED`, `TIMEOUT`, refund ve `MANUAL_REVIEW` ayri typed UI
  sonucudur.
- Top-up basarisi yalniz payment sonucu ile degil, yenilenmis wallet balance ve
  transaction ile dogrulanir.
- Tour checkout basarisinda payment, reservation/trips, public session
  capacity ve wallet yontemiyse wallet kaynaklari yenilenir.

Mock temizleme kapisi:

- `TouristPaymentStore`, sahte gecis timer'i ve local basari/bakiye mutation'i
  repository polling/recovery tamamlandiginda kaldirilir.
- Uygulama kapanip acildiginda terminal olmayan payment backend ID ile yeniden
  sorgulanmadan payment entegrasyonu tamamlanmis sayilmaz.
- Hosted form saglanmadan Android ham kart verisi alan alternatif akisa
  dusmez.

### Faz 10 - Guide Finance, Earnings, Banka Hesabi ve Withdrawal

Yapilacaklar:

- `GuideFinanceRepository`; earnings history/monthly projection, bank account
  list/create/default/delete ve withdrawal list/create islemlerini kapsar.
- Aylik kazanc backend `year/month/netEarningsMinor/currencyCode` projection'i
  ile gosterilir; Android history sayfalarini indirip toplamaz.
- IBAN girisinde local format ve `TurkishBankCatalog` hizli on gosterim olarak
  kalabilir; kesin banka/IBAN sonucu backend response'udur.
- Withdrawal yalniz `bankAccountId`, amount minor ve idempotency key gonderir.
  Kullanilabilir bakiye, pending reserve ve final status backend otoritesidir.
- Para cekme ve earning reversal wallet transaction history ile canonical
  yenilenir; otomatik uc gun transfer metni geri eklenmez.

Mock temizleme kapisi:

- `GuideWalletStore`, wallet/earnings/bank/withdrawal ekranlarinin tamami
  gercek repository'ye gectigi ayni fazda kaldirilir.
- Manuel bakiye azaltma, sabit earning history ve her hesaba ait ayri sahte
  bakiye kaldirilir.
- Banka adini Android tahmini para islemi otoritesi olarak kullanmaz.

### Faz 11 - Chat REST ve STOMP

Yapilacaklar:

- `ChatRepository`; conversation list/create, paged message history, send,
  read ve unread count REST sozlesmesini kapsar.
- Tek guide-tourist cifti tek `chatId` kullanir; sohbet rezervasyondan bagimsiz
  baslatilabilir ve yalniz route'ta `chatId` tasinir.
- `currentUserId` `UserRepository` kaynagindan gelir; `isFromMe` mapper'da
  `senderId == currentUserId` ile hesaplanir.
- Mesaj once `clientMessageId` ile pending gosterilebilir; backend ACK ile
  canonical server ID/time/status alir, hata retry edilebilir olur.
- STOMP adapter'i reconnect, resubscribe ve REST resync uygular. STOMP event'i
  kalici otorite degil, repository cache/list invalidation girdisidir.
- Sohbet acilinca read endpoint'i ve unread Flow'u conversation listesi ile
  shell badge'ini yeniler.

Mock temizleme kapisi:

- `ChatStore`, `ChatMockData`, sabit Hans/Ahmet kimlikleri ve graph-provided
  viewer role bridge REST list/detail/send/read ile STOMP resync calistiginda
  kaldirilir.
- Mesaj gonderme yalniz acik detail listesini degil conversation last-message
  ve unread state'ini de canonical kaynaktan guncellemeden faz tamamlanmaz.

### Faz 12 - Notification, FCM ve Semantic Navigation

Notification son feature fazidir; cunku tur, reservation, payment, chat ve
profile hedeflerinin typed destination kimliklerini bilerek semantic routing
kurar.

Yapilacaklar:

- `NotificationRepository`; paged history, unread count, read/read-all,
  preferences ve device FCM registration islemlerini kapsar.
- Firebase Installation ID ve FCM token backend'e authenticated device
  registration ile gonderilir; logout/account switch'te registration temizligi
  uygulanir.
- FCM receiver payload'i bildirim metnini yerellestirilmis Android resource ve
  backend semantic type/target ID ile guvenli sekilde sunar.
- `actorDisplayName` dogrudan notification DTO'sundan map edilir; ek user
  sorgusu atilmaz.
- Merkezi navigation semantic target'i typed destination'a cevirir. Ham route
  string, full model veya UI metni push payload'indan navigate edilmez.
- Chat bildirimi `chatId`, tur/reservation/payment bildirimleri kendi canonical
  ID'leri ile ilgili ekrana gider. Desteklenmeyen veya yetkisiz hedef guvenli
  notification liste/fallback davranisina duser.
- Iki rolde ortak unread state topbar ve gerekli badge'leri besler; guide
  notification paneli ayni repository kaynagini kullanir.

Mock temizleme kapisi:

- Guide notification mock listesi, manuel unread sayaci ve sabit actor adlari
  REST/FCM repository akisina gecince kaldirilir.
- Notification preferences mevcut backend boolean sozlesmesiyle birebir
  baglanmadan mock ayar kaydi kaldirilmaz.
- Uygulama foreground, background ve kapali durum FCM hedefleri; read/unread
  senkronizasyonu ve logout device temizligi dogrulanmadan faz tamamlanmaz.

### Cross-Feature Bagimlilik ve Refresh Sirasi

Feature'lar birbirinin repository implementation'ini veya data paketini
import etmez. Gerekli cross-feature akislari su sinirla kurulur:

- `home`, guide icin tour/profile/wallet/notification; tourist icin
  tour/profile repository Flow'larini presentation seviyesinde birlestirir.
- `discovery`, public tour ve public profile repository interface'lerini
  kullanir; kendine ikinci kalici veri kaynagi acmaz.
- `reservation`, public session ID ve snapshot'ini sahiplenir; payment UI veya
  provider detayini bilmez.
- `payment`, checkout sonucunda donen reservation ID/state'i domain sonucu
  olarak tasir; reservation ekran veya ViewModel'ini import etmez.
- `wallet`, provider saved method metadata'sini gerekiyorsa dar payment domain
  projection'iyle kullanir; payment data paketine baglanmaz.
- `chat` ve `notification`, current user kimligini auth `UserRepository`
  sozlesmesinden okur; sabit role/user ID uretmez.
- Notification semantic target'i feature domain ID/type degerini merkezi
  navigation'a verir; feature ViewModel'i `NavController` bilmez.

Mutation refresh'leri global mutable singleton veya genel event bus ile
yapilmayacaktir. Ilgili repository'nin explicit `refresh/invalidate` islemi,
paylasilan canonical Flow'u veya STOMP/FCM invalidation olayi kullanilir.

### Mock Kaldirma Ozeti

| Mock/runtime kaynak | Son kullanim sahibi | Kesin kaldirma fazi |
| --- | --- | --- |
| Sekiz pass-through auth use-case | Auth ViewModel -> `AuthRepository` | Faz 0 |
| `GuideProfileSharedStore`, `GuidePerformanceStore` | Guide/public profile ve performance | Faz 3 |
| `TourCatalogStore`, tour mock/timeline | Guide private + tourist public tour | Guide parcasi Faz 4, kalan tum store Faz 5 |
| `TouristReservationStore` | Trips/detail/cancel snapshot | Faz 6 |
| Local review list/mutation | Reviews/detail/performance refresh | Faz 7 |
| `TouristWalletStore` wallet kismi | Wallet balance/transactions | Faz 8 |
| `SandboxCardCatalog`, native Add Card state | Provider saved methods | Faz 8 |
| `TouristPaymentStore` | Quote/WebView/polling/payment result | Faz 9 |
| `GuideWalletStore` | Earnings/bank/withdrawal | Faz 10 |
| `ChatStore`, `ChatMockData`, mock viewer identity | Chat REST/STOMP | Faz 11 |
| Guide notification mocklari/manual unread | Notification REST/FCM | Faz 12 |

Bir store iki fazda kullaniliyorsa ilk fazda kopyalanmaz veya yeni gecici
singleton uretilmez. Kalan tuketici icin mevcut kaynagin en dar davranis-koruyucu
parcasi, son repository kapisi tamamlanana kadar tutulur ve son fazda tamamen
silinir.

### Her Fazin Uygulama Is Akisi

Kodlama sirasinda her faz icin ayni kucuk ve denetlenebilir akista
ilerlenecektir:

1. Final dosyasinin ilgili Adim 1-7 bolumleri okunur.
2. Faz durum tablosu ve onceki fazlarin acik capraz-faz kayitlari incelenir;
   mevcut fazin kapatabilecegi eski isler kapsama alinir.
3. Guncel Android kodu, backend kodu ve canli OpenAPI sozlesmesi dogrulanir.
4. Celiski varsa tahmin yurutmeden kullanicinin en son acik karari ve canli
   sozlesme esas alinir; gerekirse once final dosyasi guncellenir.
5. Degisecek dosya/paket listesi ve dis on kosullar kullaniciya bildirilir;
   API/DTO/domain/repository/mapper ve DI yazilir.
6. Tek ekran veya birbirinden ayrilamaz kucuk ekran grubu repository'ye
   baglanir.
7. Mutation refresh, loading/error/empty/pagination ve navigation ID davranisi
   tamamlanir.
8. Ilgili mock ve bosa dusen kod ayni degisiklikte temizlenir.
9. Format, compile, contract, ilgili test ve UI smoke sonucu alinmadan sonraki faza
   gecilmez.
10. Faz kapanirken mevcut fazin durumu, kapanis kaniti ve kapatilan onceki faz
    takipleri birlikte guncellenir. Acik bagimlilik varsa durum
    `KISMEN TAMAMLANDI` olarak kalir.

### Adim 6 Sonucu ve Kapisi

Feature-first fiziksel refactor on kosulu, on iki dikey entegrasyon fazinin
sirasi, her feature'in repository/mapper/ViewModel sorumlulugu, cross-feature
bagimlilik siniri, canonical refresh davranisi ve tum runtime mocklarin kesin
kaldirma kapisi belirlenmistir.

Bu adim plan disinda Kotlin veya backend kaynak kodu degistirmemistir. Sonraki
hazirlik calismasi Adim 7'dir: final format/compile/test, iki kullanicili LAN,
auth, media, tour lifecycle, reservation/review, payment/refund/wallet,
STOMP/FCM, process death, logout/account switch, security, mock/secret ve dead
code tamamlanma kriterlerini uygulanabilir kontrol listesine donusturmek.

## Adim 7 - Final Dogrulama ve Tamamlanma Kriteri

Durum: PLAN TAMAMLANDI. DOGRULAMA HENUZ UYGULANMADI.

Bu adim, Adim 6'daki dikey entegrasyonlar tamamlandikca calistirilacak kalite
kapilarini ve projenin finalde gercekten tamamlanmis sayilacagi kosullari
belirler. Bu bolumun yazilmasi testlerin gectigi, E2E akisin calistigi veya
mocklarin silindigi anlamina gelmez. Her sonuc gercek komut, cihaz ve backend
kanitiyla entegrasyon sirasinda kaydedilecektir.

### Dogrulama Stratejisi

Test piramidi GuideMate olcegine orantili tutulur:

- Saf domain kurali ve mapper'lar hizli unit testlerle dogrulanir.
- Repository; fake API veya gerekli yerde MockWebServer ile request, mapping,
  pagination ve hata davranisi acisindan test edilir.
- ViewModel; fake repository, coroutine test dispatcher ve StateFlow
  beklentileriyle loading/content/empty/error/mutation akislarinda test edilir.
- Compose UI/instrumentation testleri yalniz kritik kullanici aksiyonlari,
  navigation ve geri donus davranislarina yazilir; her composable icin test
  uretilmez.
- Spring Boot transaction, locking, idempotency ve PostgreSQL ozellikleri
  backend test/Testcontainers katmaninda kalir; Android bunlari taklit etmez.
- Provider, iki kullanici, process death, FCM ve STOMP gibi sinirlar gercek
  local/Sandbox E2E ile tamamlanir.

Sirf test sayisini artirmak icin anlamsiz test, generic test base'i veya tum
ekranlari kapsayan kirilgan dev UI senaryosu kurulmaz. Her test gercek bir is
kurali, mapping, state transition, hata, yetki veya entegrasyon riskini
korumalidir.

### Her Fazda Calisacak Android Kalite Kapisi

Adim 6'daki her faz sonunda en az su komutlar calistirilir:

```bash
./gradlew ktfmtCheck
./gradlew :app:compileDebugKotlin
./gradlew :app:testDebugUnitTest
```

Ilgili fazda manifest, resource, network security, Compose semantics veya
Android API davranisi degistiyse ayrica:

```bash
./gradlew :app:lintDebug
./gradlew :app:assembleDebug
```

Emulator veya fiziksel cihaz hazir oldugunda kritik instrumentation paketi:

```bash
./gradlew :app:connectedDebugAndroidTest
```

Bir komut mevcut ortam, cihaz veya dis servis eksigi nedeniyle
calistirilamiyorsa basarili gibi raporlanmaz. Hangi komutun neden
calistirilamadigi, kalan risk ve tekrar kosma kosulu acikca yazilir.

Final kapanista yukaridaki Android komutlarinin tamami temiz checkout'a yakin
bir durumda yeniden calistirilir. Yalniz `compileDebugKotlin` gecmesi final
kalite kaniti sayilmaz.

### Backend ve Canli Sozlesme Kapisi

Android repository/DTO baglantisinin final dogrulamasi yalniz eski dokumana
bakilarak yapilmaz. Backend kaynak ve canli OpenAPI yeniden dogrulanir:

```bash
cd /Users/ahmetkaragunlu/IdeaProjects/GuideMateBackend
bash mvnw -q test
```

Backend testleri su kanitlari korumalidir:

- Spring context baslar.
- Flyway migration'lari temiz PostgreSQL/Testcontainers uzerinde uygulanir.
- Hibernate schema validation gecer.
- Repository/service transaction, locking, authorization ve idempotency
  testleri gecer.
- OpenAPI contract testleri gecer.

8080 portunda eski surec riski varsa yeniden derlenen backend gecici bir portta
baslatilir, `/v3/api-docs` oradan alinir ve surec kontrollu kapatilir. Android
DTO nullable alanlari, enum degerleri, UUID/Instant, pagination, error code,
minor-unit para ve currency alanlari bu guncel contract ile karsilastirilir.

Contract uyumsuzlugu bulunursa Android'de tahmin/fallback yazilmaz. Otorite
sirasina gore backend veya Android'in yanlis tarafi duzeltilir, OpenAPI yeniden
uretilir ve ayni test tekrarlanir.

### Mimari ve Bagimlilik Denetimi

Feature-first refactor ve tum entegrasyonlardan sonra su kurallar yeniden
taranir:

- Presentation paketi Retrofit API, DTO, repository implementation veya MVP
  store import etmez.
- Feature `data` katmani `presentation` model, mapper veya component'ini import
  etmez.
- Feature `data` paketi baska feature'in `data` paketini import etmez.
- Feature domain katmani Android UI, Retrofit, provider SDK veya navigation
  tipini bilmez.
- `common`, feature veya navigation'a baglanmaz.
- Navigation yalniz ID ve typed destination tasir; tam model, DTO veya UI metni
  tasimaz.
- ViewModel `NavController`, iyzico, STOMP, FCM, Coil veya raw persistence
  adapter'ini dogrudan bilmez.
- Hilt binding feature `di`, cross-feature teknik composition ust seviye `di`
  sahibi olarak kalir.
- Auth disinda endpoint'i olmayan bos repository acilmaz; iki satirlik use-case
  wrapper'i geri eklenmez.
- Ayni backend kaydini temsil eden guide/tourist ekranlari paralel mutable veri
  kaynagi olusturmaz; role uygun UI mapper'lari canonical domain kaynagini
  kullanir.
- Generic base repository/ViewModel/UiState, global mutable singleton veya
  genel event bus bulunmaz.

Bu maddeler yalniz paket adina bakilarak degil import ve gercek sorumluluk
okunarak denetlenir. Dogru ve cohesive kod sirf daha fazla katman gostermek icin
degistirilmez.

### Unit ve Integration Test Kapsami

Auth:

- Email normalize/validation ve numeric password policy.
- Login/register/Google/role/password response mapping.
- Structured backend field error ve terminal session hata mapping'i.
- Refresh rotation, tek retry, local session clear ve logout davranisi.

Medya ve profil:

- 5 MB precheck, MIME/uzanti kabul-red siniri ve upload response mapping.
- Local URI ile remote URL ayrimi; drawable/error fallback.
- Own/public profile mapper'lari, guide performance/level ve nullable avatar.
- Profile patch basari/hata state'i ve canonical refresh.

Tour ve discovery:

- Tour/session DTO-domain-UI mapper'lari; status, version, capacity ve
  `bookedCount` ayrimi.
- Guide ACTIVE/REVIEW/PAST tab mapping, backend siralamasi ve pagination.
- Publish/edit dirty-field ayrimi; content change request ve session update
  kismi basari sonucu.
- Open/close/cancel/archive typed operation failure mapping.
- Search/filter query mapping, popular/public detail ve empty/retry/append.
- Guide/private ile tourist/public projection'in ayni canonical ID'leri
  korumasi.

Reservation ve review:

- Reservation snapshot, upcoming/past/cancelled mapping ve reservation ID
  navigation.
- Cancel response ile refund eligibility/status mapping.
- Review eligibility, duplicate engeli ve submit sonrasi refresh tetikleri.
- Iptal edilen veya tamamlanmayan reservation'in review aksiyonu almamasi.

Payment, wallet ve finance:

- Quote canonical/charge amount, currency, FX source/rate ve expiry mapping.
- Payment terminal/non-terminal state, refund ve manual-review mapping.
- Polling timeout/backoff/recovery ve process restart'ta payment ID ile devam.
- Wallet balance/transaction ve `referenceTitle` mapping.
- Saved method default/delete ve maskeli provider metadata mapping.
- Aylik earnings projection, bank account ve withdrawal operation sonucu.
- `Long` minor unit formatter siniri; `Double` ile authoritative para hesabinin
  bulunmamasi.

Chat ve notification:

- `senderId == currentUserId` mesaj yonu mapping'i.
- `clientMessageId` pending/ACK/failure/retry ve duplicate event davranisi.
- Cursor pagination, read state ve unread count.
- STOMP reconnect/resubscribe/REST resync.
- Notification actor, preferences, read/read-all ve semantic target mapping.
- Bilinmeyen veya yetkisiz semantic hedefte guvenli fallback.

Eski MVP store testleri ilgili store kaldirildiginda ayni davranisi koruyan
repository/mapper/ViewModel testleriyle degistirilir. `ExampleUnitTest` ve
`ExampleInstrumentedTest` yalniz Android Studio sablonu olarak kaldigi
dogrulanirsa silinir. `SandboxCardInputRulesTest` ve mock-store testleri runtime
mocklariyla birlikte bosa dusuyorsa tutulmaz.

### Auth E2E Kontrol Listesi

Emulator ve en az bir fiziksel cihazda su akislar dogrulanir:

- Ilk kurulum onboarding -> Sign In; tamamlanmis onboarding tekrar acilmaz.
- Register -> email verification linki -> role selection -> dogru root.
- Bekleyen verification, resend ve suresi gecmis/kullanilmis link sonucu.
- Normal login ve yanlis sifre/rate-limit kullanici mesaji.
- Mevcut ACTIVE hesapla Google login; bulunmayan, pending ve mismatch sonucu.
- Access token expiry -> tek refresh -> istegin devam etmesi.
- Gecersiz/revoked refresh -> local session temizligi -> Auth root.
- Password change -> local session clear -> Sign In; yeni sifreyle login.
- Logout remote hata alsa bile local secret/session/cache temizligi.
- Uygulama kapat/ac ve process recreation sonrasinda dogru root/role.

Sign In -> Sign Up/Forgot Password -> Sign In geri donusunde stack tekrar eski
forma gitmez. Back-stack ve `switchRoot` davranisi kullanici rolunu veya onceki
hesabin ekranlarini sizdirmaz.

### Medya, Profil, Tour ve Discovery E2E

Rehber hesabi:

1. Galeriden ve kameradan avatar secilir, upload edilir ve profil/preview'da
   ayni remote gorsel gorulur.
2. About/title/language alanlari guncellenir; uygulama yeniden acildiginda
   backend degeri korunur.
3. Cover secilerek tur olusturulur ve admin review'e gider.
4. Admin mobil uygulamadan degil mevcut backend admin araci ile approve/reject
   eder.
5. Guide ACTIVE/REVIEW/PAST, dashboard sayaclari ve detay canonical backend
   state'ini gosterir.
6. Session update, content change request, open/close, yeni tarih, cancel ve
   archive kurallari basari/red durumlariyla dogrulanir.

Turist hesabi:

1. Onayli ve acik session popular/search/filter sonucunda bulunur.
2. Kart, detail, guide profili, cover, fiyat, dil, konum, rating ve kapasite
   guide'in yayinladigi ayni backend kaydiyla uyumludur.
3. Kapali, iptal, suresi gecmis veya dolu session aramadan/detailden yanlis
   satin alinabilir gorunmez; race olursa backend hata mesaji gorunur.
4. Filter uygulama, empty, retry, refresh ve pagination gercek backend sonucu
   ile calisir.

Media URL fiziksel cihazdan erisilebilir olmalidir. Local `content://`, drawable
ID veya Mac'e ozel `file://` degeri backend kalici kaydinda bulunamaz.

### Reservation, Cancellation ve Review E2E

- Tourist checkout basarisindan sonra reservation Trips/Upcoming'ta ayni
  `reservationId` ve satin alma snapshot'i ile gorulur.
- Tur sonradan edit edilse bile satin alinmis reservation'in tarihi/fiyati ve
  temel snapshot'i gecmiste degismez.
- Tourist cancellation policy sonucu, capacity iadesi ve refund state'i
  backend cevabiyla uyumlu gorulur.
- Guide session cancellation ilgili reservation/refund/notification
  sonucunu iki rolde dogru gosterir.
- Tamamlanan reservation Past'e gecer; iptal edilen tamamlanmis gibi
  sunulmaz.
- Yalniz uygun tamamlanmis reservation bir kez rating/review gonderebilir.
- Review sonrasi tour rating/review count, guide performance/level, dashboard,
  public detail/popular ve notification projection'lari yenilenir.

Capacity race backend integration testinde zorunludur: ayni son koltugu iki
tourist ayni anda almaya calistiginda kapasite asilmaz; bir islem canonical
basari, digeri anlamli red alir. Android red sonucunu sessizce basari veya bos
ekran gibi gostermemelidir.

### Payment, Wallet ve Finance Sandbox E2E

Testten once iyzico Sandbox credential'lari backend environment'inda, guncel
Quick Tunnel callback/webhook adresi backend ve iyzico panelinde hazirlanir.
Android'e provider secret veya tunnel adresi yazilmaz.

Zorunlu senaryolar:

- Desteklenen tahsilat para birimleri backend'den gelir; kullanici secimiyle
  quote yenilenir ve canonical USD/provider charge snapshot'i dogru gosterilir.
- Hosted Checkout Form WebView'da acilir; SSL hatasi bypass edilmez ve raw kart
  verisi Android/backend loguna girmez.
- Basarili 3DS/callback sonrasi JSON veya WebView kapanisi degil, backend
  `SUCCEEDED` ve gerekiyorsa reservation `CONFIRMED` sonucu basari sayilir.
- Decline, invalid card/CVV, 3DS failure, user cancel ve timeout ayri gorunur
  sonuclar verir.
- Gec gelen callback/webhook, duplicate request ve duplicate webhook kapasite,
  reservation veya wallet'i iki kez degistirmez.
- Hold suresi bittikten sonra gec basari kapasite asmaz; canonical refund veya
  manual-review sonucu Android'de gosterilir.
- Top-up ancak backend payment verification sonrasi wallet balance ve
  transaction history'de gorulur.
- Wallet ile tur aliminda payment, reservation ve ledger sonucu birlikte
  yenilenir; yetersiz bakiye local tahminle asilmaya calisilmaz.
- Saved card list/default/delete yalniz provider/backend maskeli metadata'sini
  kullanir; native raw-card formu yoktur.
- Refund wallet/payment state ve transaction history'de tek kez gorulur.
- Guide earning PENDING/AVAILABLE/REVERSED, monthly projection ve session
  earning birbiriyle uyumludur.
- Bank account create/default/delete ve withdrawal PENDING/PROCESSING/
  COMPLETED/FAILED durumlari; yetersiz bakiye ve duplicate idempotency
  senaryolari dogrulanir.

Gercek banka transferi iyzico Sandbox urun siniriyla desteklenmiyorsa bu kisim
backend'in belirlenmis Sandbox/manual provider adapter'i ve canonical state'i
uzerinden test edilir; calismayan gercek transfer basariliymis gibi raporlanmaz.

### Chat REST/STOMP E2E

Iki farkli cihaz veya emulator/fiziksel cihaz kombinasyonunda guide ve tourist
ayni backend'e baglanir:

- Tourist rezervasyon yapmadan erisilebilir guide profilinden sohbet
  baslatabilir.
- Ayni guide-tourist cifti tekrar baslatildiginda ayni `chatId` acilir.
- Gonderilen mesaj karsi cihazda STOMP ile anlik gorunur, conversation son
  mesaji ve sirasi iki tarafta guncellenir.
- Uygulama acik degilken mesaj PostgreSQL'de kalir; geri donuste REST history
  ile eksik mesaj tamamlanir.
- Baglanti kopup geldikten sonra reconnect/resubscribe ve REST resync duplicate
  mesaj uretmez.
- Pending -> sent/failed/retry, `clientMessageId` idempotency ve server zamani
  dogrulanir.
- Sohbet acilinca read state ve bottom-bar unread badge iki tarafta canonical
  sonuc ile guncellenir.
- Bir kullanici baska `chatId`yi tahmin ederek yetkisiz mesaj/history okuyamaz.

### Notification ve FCM E2E

- Android 13+ notification izni uygun zamanda istenir; red durumunda guide
  paneli, ortak unread ve preferences calismaya devam eder. Tourist icin yeni
  notification history ekrani icat edilmez.
- Notification channel'lari olusturulur ve Firebase token/FID authenticated
  kullanici/installation ile backend'e kaydedilir.
- Token refresh backend registration'i gunceller; logout/account switch eski
  kullanicinin backend device registration'ini pasiflestirir ve local
  notification state'ini temizler. Kalici installation ID cihazdan silinmez.
- Tour approval/reject, purchase, rating/comment, cancellation/refund/payment,
  withdrawal ve chat olaylarinda backend notification history olusturur.
- Uygulama foreground, background ve kapali durumdayken FCM gorunur davranis
  ayri ayri test edilir.
- Bildirime tiklama `chatId`, `tourId`, `reservationId`, `paymentId` veya ilgili
  canonical ID ile dogru typed destination'a gider.
- Silinmis, yetkisiz veya artik acilamayan hedef uygulamayi cokertmez; guvenli
  fallback/history davranisi gosterir.
- Read/read-all sonrasi topbar ve ilgili badge sayisi backend unread count ile
  uyumludur.
- Preferences backend boolean sozlesmesini iki rolde korur.

FCM push icin WorkManager eklenmez. WorkManager yalniz gercek bir retry/sync
ihtiyaci kanitlanirsa kullanilir; push teslim kanali Firebase'dir.

### Iki Kullanicili LAN Kabul Senaryosu

Final kabul testi icin PC'de Spring Boot ve PostgreSQL calisir. Mac'in guncel
LAN IP'si backend `PUBLIC_BASE_URL` ve Android debug base URL konfigurasyonunda
ayni Wi-Fi'a uygun olur; source'a sabitlenmez. Backend `0.0.0.0` uzerinden
erisilebilir, firewall ve media URL'leri fiziksel cihazdan kontrol edilir.

En az su kimlikler hazirlanir:

- Guide A: profil/tur/finance sahibi.
- Guide B: Guide A private verisine erisememesi icin yetki testi.
- Tourist A: discovery/reservation/payment/review/chat ana kullanicisi.
- Tourist B: capacity race ve veri izolasyonu testi.

Ayni anda iki cihaz yeterlidir; hesaplar sirayla degistirilerek dort kimlik de
test edilebilir. Capacity race backend otomasyonuyla da zorunlu korunur.

Uctan uca ana senaryo:

1. Guide A profilini gunceller ve cover ile tur yayinlar.
2. Admin araci turu onaylar.
3. Tourist A ayni turu arar, gercek guide/tur verisini gorur ve Sandbox
   checkout ile satin alir.
4. Guide A dashboard, participant ve ilgili earning projection'ini backend
   state'ine gore gorur.
5. Guide A ile Tourist A anlik mesajlasir; unread ve notification iki cihazda
   guncellenir.
6. Cancellation/refund veya completion/review akislarindan biri uctan uca
   tamamlanir ve iki roldeki tum projection'lar yenilenir.
7. Guide B ve Tourist B'nin yetkisiz private kayitlara erisemedigi dogrulanir.

### Process Death, Offline ve Hesap Izolasyonu

- Liste/detail ekraninda process recreation canonical veriyi repository
  refetch ile kurar; singleton process-memory otoritesine bagli kalmaz.
- Publish/edit/profile formunda korunmasi gereken draft yalniz uygun
  `SavedStateHandle`/local form state ile geri gelir; server verisi gibi
  saklanmaz.
- Hosted payment sirasinda process kapanirsa payment ID ile backend status
  yeniden sorgulanir; local timer basari uretmez.
- Offline durumda cached olmayan ekran sonsuz loading yerine retry edilebilir
  hata gosterir. Mutation basariliymis gibi local kalici state yazmaz.
- Logout ve account switch; access/refresh secret, current user, repository
  cache, unread, chat subscription, pending form owner ve role-specific state'i
  temizler.
- Guide A'dan Guide B'ye veya Tourist A'dan Tourist B'ye geciste onceki hesabin
  profil, tur, wallet, reservation, chat veya notification verisi bir kare bile
  canonical content olarak kullanilmaz.

### Security ve Secret Denetimi

Final taramada su maddeler kanitlanir:

- `local.properties`, backend secret properties, `.env`, keystore ve provider
  credential dosyalari Git'e girmez.
- Source, resource, BuildConfig release veya loglarda iyzico secret, JWT
  signing key, refresh token, SMTP password, raw Google ID token, tam kart
  numarasi, CVV veya tam IBAN bulunmaz.
- Android raw kart numarasi/SKT/CVV alan native form veya model tasimaz.
- Tokenlar Android Keystore anahtariyla sifrelenmis private storage'da kalir;
  DataStore hassas secret deposu yapilmaz.
- Release cleartext kapali kalir. Local HTTP izni yalniz debug manifest/config
  sinirindadir.
- WebView SSL error bypass, `addJavascriptInterface`, mixed-content acma veya
  arbitrary external URL navigation bulunmaz.
- Logcat/OkHttp release logu auth header, body secret, IBAN, payment/provider
  token veya teknik exception ayrintisini sizdirmaz.
- FCM/deep-link ID'si yetki kaniti sayilmaz; backend her detail isteginde
  object authorization uygular.
- FileProvider export edilmez; media upload content/MIME ve boyut backend
  tarafinda yeniden dogrulanir.

Secret taramasi yalniz anahtar kelime aramasi degildir; tracked dosya listesi,
Gradle/resource konfigurasyonu ve debug/release ayrimi birlikte incelenir.
Supheli bir deger final raporda maskelenir, cevaba veya loga tam degeri
yazilmaz.

### Mock, Dead Code ve Paket Temizligi

Adim 6 tamamlandiktan sonra su runtime kalintilar bulunmamalidir:

- `TourCatalogStore`, `GuideProfileSharedStore`, `GuidePerformanceStore`.
- `TouristReservationStore`, `TouristPaymentStore`, `TouristWalletStore`.
- `GuideWalletStore`, `ChatStore`, `ChatMockData` ve guide notification
  mocklari.
- `SandboxCardCatalog`, native Add Card raw-card formu ve mock verification
  timer/fonksiyonlari.
- `mockCurrentUserId`, sabit viewer role, Ahmet/Hans/Elif runtime kimlikleri ve
  hardcoded guide/tourist/profile/payment verileri.
- Sekiz pass-through auth use-case ve bosa dusen DI binding/importlar.
- `if backend yoksa mock` runtime fallback'i.

Asagidakiler mock sayilip yanlislikla silinmez:

- Compose preview ve test fixture'lari.
- Yerellesebilir ulke/dil/tour kategori kataloglari.
- `TurkishBankCatalog` hizli IBAN banka on gosterimi.
- Google Places `CitySearchService`.
- Drawable fallback ve statik yardim/yasal metinler.

Bos paket/klasor, sahipsiz dosya, kullanilmayan resource/string/drawable,
wildcard import, TODO/FIXME ve eski package referanslari taranir. TODO/FIXME
bilincli sonraki urun kapsamiysa acik gerekcesi olur; final akisin bos aksiyonu
olarak kalamaz.

### UI, Navigation ve Yerellesme Kabul Kriteri

- Mevcut renk, typography, icon boyutu, padding, card, bottom sheet, dialog,
  topbar, bottom bar ve tek-scaffold davranisi bilincli tasarim karari olmadan
  degismez.
- Loading/empty/error/retry eklenirken layout ziplama, ust uste scaffold veya
  geri tusu dongusu olusmaz.
- Guide ve tourist bagimsiz bottom-bar history'si korunur.
- Payment final ekrani, auth root switch ve full-screen account akislarinda
  sistem geri tusu/back-stack urun kararina uyar.
- Landscape dil/kategori picker, LazyColumn uzun listeler ve klavye/IME
  davranisi smoke test edilir.
- Backend enum veya hata metni dogrudan UI'ya basilmaz; gorunen metin string
  resource ile yerellestirilir.
- Tarih, saat, para ve relative-time gorunumu locale formatter kullanir;
  authoritative amount/currency degeri backend sozlesmesinden gelir.
- En az varsayilan locale ve Ingilizce cihaz locale'inde kritik akislar tasma,
  kirpilma ve anlamsiz sabit Turkce metin acisindan kontrol edilir.

### Final Kanit Kaydi

Her faz kapandiginda bu belgeye veya ayni task'in final raporuna su kanit
yazilir:

| Alan | Kaydedilecek kanit |
| --- | --- |
| Kod | Degisen feature, repository ve kaldirilan mock listesi |
| Contract | Kullanilan OpenAPI endpoint/DTO surumu ve bulunan uyumsuzluk |
| Otomasyon | Calisan Gradle/Maven komutlari ve sonucu |
| Cihaz | Emulator/fiziksel cihaz, Android surumu ve test edilen rol |
| Dis sistem | iyzico Sandbox, Quick Tunnel, FCM, STOMP veya SMTP hazirlik sonucu; secret degeri olmadan |
| E2E | Basarili/basarisiz senaryolar ve kalan gercek risk |

Kanit kaydina ek olarak faz durum tablosu ve capraz-faz takip tablosu ayni
calismada guncellenir. Daha sonraki faz bir onceki fazin acik entegrasyonunu
tamamlarsa iki fazin kaniti birbirine referans verir.

Kanitsiz `calisiyor`, `backend'e hazir` veya `tamamlandi` ifadesi
kullanilmayacaktir.

### Proje Sonrasi Mulakat ve Kod Anlatim Standardi

Proje tamamlandiktan sonra DI, interface, repository, DTO, mapper, ViewModel,
API, state yonetimi, navigation ve benzeri mimari konular; kullanicinin bu
kavramlari onceden bildigi varsayilmadan anlatilacaktir. Yalnizca "SOLID'e
uygun" veya "profesyonel" gibi soyut ifadeler kullanilmayacak, gercek GuideMate
akisi ve mevcut kod uzerinden neden-sonuc iliskisi kurulacaktir.

Her konu su sirayla ele alinacaktir:

1. Kullanicinin uygulamada yaptigi islem basit bir GuideMate senaryosuyla
   anlatilir.
2. Akisin basladigi ekran ve kullanici aksiyonu gosterilir.
3. Kodun `Screen -> ViewModel -> Repository -> API -> Backend` ve donus yolu
   gercek dosya, sinif ve fonksiyonlar uzerinden adim adim takip edilir.
4. Her sinifin ve parametrenin ne ise yaradigi, nereden geldigi, nereye gittigi
   ve neden gerekli oldugu temel seviyede aciklanir.
5. Ilgili gercek kod parcasi gosterilir ve satirlar sade dille yorumlanir.
6. Kod kaldirilirsa veya yanlis katmana tasinirsa kullanici akisi ve mimaride
   neyin bozulacagi somut bir ornekle aciklanir.
7. Gerekiyorsa alternatif yaklasim ve mevcut tercihin neden GuideMate icin
   uygun oldugu, overengineering siniri dahil, belirtilir.
8. Konu sonunda mulakatta kullanilabilecek bir veya iki cumlelik net cevap
   hazirlanir.

Bu anlatimlarda teknik terimler ilk kullanimda tanimlanacak; aciklama bilen bir
gelistiriciye kisaltma yapilarak degil, konuya ilk kez bakan birinin veri ve kod
akisini takip edebilecegi seviyede verilecektir.

### GuideMate Android Final Tamamlanma Tanimi

Proje ancak asagidakilerin tamami saglandiginda final backend entegrasyonu
tamamlanmis sayilir:

1. Adim 5 feature-first fiziksel refactor'u ve Adim 6'daki on iki entegrasyon fazi
   uygulanmistir.
2. Auth disindaki tum gercek ekran/aksiyonlar canonical repository/API akisina
   baglanmistir; bilincli statik/local kataloglar disinda runtime mock otorite
   kalmamistir.
3. Backend OpenAPI ile Android DTO/repository contract'i uyumludur.
4. Format, compile, unit test, lint, assemble ve mevcut cihazda instrumentation
   kapilari gecer; calistirilamayan test varsa proje final sayilmaz veya kalan
   risk acikca kullanici karariyla kabul edilir.
5. Iki kullanicili LAN ana senaryosu; profile/media, tour publish/discovery,
   reservation/review, payment/wallet/finance, chat/STOMP ve notification/FCM
   akislarinda kanitlanmistir.
6. Payment basarisi, capacity, refund, wallet, earning, withdrawal, lifecycle,
   rating ve unread otoritesi backend'dedir; Android local sonuc uydurmaz.
7. Process death, offline, logout ve account switch onceki kullanicinin veya
   gecici MVP state'inin sizmasina yol acmaz.
8. Secret/raw-card/CVV/token sizintisi, yetkisiz object access, WebView SSL
   bypass veya release cleartext bulunmaz.
9. Bos aksiyon, sahipsiz paket/dosya, kullanilmayan runtime mock, gereksiz
   pass-through use-case ve kritik TODO/FIXME kalmamistir.
10. Mevcut tasarim, role uygun navigation ve yerellestirilmis kullanici hata
    deneyimi korunmustur.
11. Faz 0 ile Faz 12'nin her biri ayri olarak `TAMAMLANDI` durumundadir ve acik
    capraz-faz takip kaydi kalmamistir.

Bu on kosuldan biri eksikse proje o feature acisindan devam ediyor kabul edilir;
eksik madde `sonra yapilir` denilerek sessizce final rapordan dusurulmez.

### Adim 7 Sonucu ve Uygulama Kapisi

Android ve backend otomatik test komutlari, mimari/contract denetimi, feature
bazli E2E senaryolari, iyzico/FCM/STOMP dis sistem kontrolleri, iki kullanicili
LAN akisi, process death/account isolation, security, mock/dead-code temizligi
ve on maddelik final tamamlanma tanimi kesinlestirilmistir.

Yedi hazirlik adiminin tamami artik plan seviyesinde tamamlanmistir. Kotlin ve
backend kaynak kodu bu adimda degistirilmemistir. Sonraki uygulama isi Adim
6'da tanimlanan `Faz 0 - Davranis-Koruyucu Feature-First Refactor`dur. Her
sonraki kod fazindan once bu dosya yeniden okunacak ve ilgili Adim 4-7
sozlesme, sahiplik, entegrasyon ve dogrulama kapilari birlikte uygulanacaktir.
