# GuideMate Backend Uygulama ve Android Entegrasyon Devir Belgesi

Bu belge, GuideMate Android MVP tamamlandiktan sonra Spring Boot/PostgreSQL
backend'inin eksiksiz kurulmasi ve sonrasinda Android'in gercek API'lere
baglanmasi icin tek ana yol haritasidir.

Bu belge yalniz ad verilmis anayasalari degil, sohbet boyunca "kurala kaydet",
"backend sonrasinda yapilacak" ve "entegrasyonda kaldirilacak" denilen tum
kararlari kapsar.

## Altin Kural - Orantili ve Profesyonel Kod Kalitesi

Backend kodu gercek sirket projelerine yakin; SOLID, dusuk bagimlilik,
okunabilirlik, test edilebilirlik, genisletilebilirlik, dogru isimlendirme ve
kod tekrarsizligi hedefiyle yazilir. Bu hedef over-engineering yapmak, mevcut
dogru kodda zorla hata aramak veya mimariyi gereksiz yere buyutmek icin
kullanilmaz.

Degisiklik karari su kurallara uyar:

- Inceleme sirasinda "mutlaka hata bulma" hedefi yoktur. Mevcut yapi dogru,
  okunabilir ve ihtiyaci karsiliyorsa oldugu gibi korunur ve acikca yeterli
  denir.
- Bir sinif veya metod yalniz uzun oldugu icin bolunmez. Tek sorumlulugu
  bozuyorsa, farkli is kurallarini karistiriyorsa, tekrar uretiyorsa, test
  izolasyonunu engelliyorsa veya degisiklik nedenleri gercekten ayrisiyorsa
  parcalanir.
- Interface, use-case, factory, manager, base class, helper veya yeni katman;
  yalniz gelecekte kullanilabilir diye eklenmez. Gercek adapter siniri,
  degisebilir dis bagimlilik, tekrar kullanilan business davranisi veya testte
  anlamli bir seam varsa kullanilir.
- Tek bir ortak veri, tek bir benzer satir veya varsayimsal gelecek ihtiyaci
  ortak abstraction cikarmak icin yeterli degildir. Ortak yapi ayni kuralin
  gercekten tekrarlandigi ve tek kaynaktan yonetimin tutarlilik sagladigi yerde
  kurulur.
- Kod tekrari yalniz gorunus benzerligine gore birlestirilmez. Ayni business
  anlami ve ayni degisiklik nedeni varsa ortak fonksiyon/yapi kullanilir;
  farkli lifecycle veya kurala sahip kodlar zorla ortaklastirilmaz.
- Sinif, fonksiyon, degisken, DTO, endpoint ve paket isimleri domain amacini
  acikca anlatir. Generic ve belirsiz `Manager`, `Helper`, `Util`, `Data` veya
  benzeri isimler ancak gercek ve dar bir teknik sorumlulugu ifade ediyorsa
  kullanilir.
- Her dosya feature-first mimaride dogru feature ve katmanda bulunur. Controller
  HTTP/API sinirinda, DTO request/response sozlesmesinde, mapper donusumde,
  service/application is kurali ve transaction'da, repository persistence
  erisiminde, domain ise entity/value/state kurallarinda kalir.
- Mevcut backend'de somut bir katman veya paket hatasi bulunursa duzeltilir.
  Tasima/refactor oncesinde gercek sorun belirtilir; davranis korunur, etkilenen
  import/test/config noktalarinin tamami guncellenir ve degisiklik gerekli en
  kucuk kapsamda tutulur. Tum mimari sirf duzeltme yapmak icin yeniden kurulmaz.
- Feature servisleri birbirinin repository'sine kontrolsuz baglanmaz. Ortak
  business davranisi uygun service/application sinirindan; iyzico, medya, FCM
  ve benzeri dis sistemler dar interface/adapter sinirindan kullanilir.
- Test edilebilirlik icin constructor dependency injection, kontrol edilebilir
  dis bagimliliklar ve acik transaction sinirlari korunur. Yalniz test yazmak
  ugruna production koduna anlamsiz katman veya interface eklenmez.
- Bir degisiklik eski fonksiyon, degisken, paket veya dosyayi gercekten bosa
  dusururse ve gelecekte kullanilmasi kararlastirilmis degilse temizlenir.
  Bilincli ertelenen ozellikler ve halen gecis gorevi olan kodlar gereksiz diye
  silinmez.
- Once mevcut mimariyle uyumlu en sade profesyonel cozum uygulanir. Mikroservis,
  generic framework veya spekulatif genisletilebilirlik yerine GuideMate'in
  gercek bugunku ihtiyaci ve belgede kesinlestirilmis backend gereksinimleri
  esas alinir.

Her faz sonunda bu altin kurala gore paket/katman konumu, isimlendirme,
bagimlilik yonu, okunabilirlik, test edilebilirlik, gercek kod tekrari ve
kullanilmayan kod kontrol edilir. Bulgu yoksa sirf refactor yapmak icin kod
degistirilmez.

Projeler:

- Backend: `/Users/ahmetkaragunlu/IdeaProjects/GuideMateBackend`
- Android: `/Users/ahmetkaragunlu/AndroidStudioProjects/GuideMate`
- Auth devir belgesi: `/Users/ahmetkaragunlu/AndroidStudioProjects/GuideMate/docs/auth-backend-handoff.md`
- Tur mimari notu: `/Users/ahmetkaragunlu/AndroidStudioProjects/GuideMate/docs/tour-architecture-notes.md`

## Backend Sohbeti Bu Belgeyi Nasil Kullanacak

1. Once mevcut backend checkout'ini ve Android'in ilgili modellerini yeniden
   tarayacak. Bu belge eski kodu gormeden dogrudan kod yazma talimati degildir.
2. Mevcut auth altyapisini yeniden tasarlamayacak; auth devir belgesinde
   tamamlanan JWT, refresh, installation ID, hata sozlesmesi, Flyway ve rol
   guvenligi kurallarini koruyacak.
3. Domainleri bu belgedeki uygulama sirasiyla tamamlayacak. Sonraki domain,
   oncekinin veri ve API sozlesmesi kararlastirilmadan baslatilmayacak.
4. Backend asamasinda Android kaynak kodunu degistirmeyecek. Android'i yalniz
   DTO, endpoint ve davranis sozlesmesini dogru kurmak icin okuyacak.
5. Her faz sonunda migration, endpoint, state transition, hata kodu ve Android
   response sozlesmesini bu belgeye gore kontrol edecek.
6. Tum backend bittiginde Android sohbeti bu belgeyi yeniden okuyacak ve
   `Android Entegrasyon Kontrol Listesi` bolumunu uygulayacak.

## Kapsanan Karar Kaynaklari

- `odeme anayasasi`
- `ortak veri anayasasi`
- `bildirim yasasi`
- `tur yayinlama anayasasi`
- `tur satin alma anayasasi`
- `mesaj anayasasi`
- `backend hazirlik karari`
- Auth icin tamamlanan 15 maddelik backend/Android devir kararlari
- Rehber sahipligi ve JWT principal yetkilendirme kurallari
- Tur duzenleme, admin onayi ve son onayli verinin korunmasi kurallari
- Tur iptali, turist iptali, iade ve kazanc duzeltme kurallari
- Yorum, puan, rehber performansi, seviye ve populerlik kurallari
- Android ulke/sehir/dil secimi ve sade IANA saat dilimi kurallari
- Kalici medya ve Android local URI siniri
- Para cekme, IBAN, banka hesabi ve bakiye rezervasyonu kurallari
- Mock `@Singleton Store` yapilarinin backend sonrasi kaldirilma kurali
- Navigation ve current-user state'inin backend oturumundan beslenme kurali
- Kullaniciya sessiz basarisizlik gostermeme ve typed hata sonucu kurali
- Testlerin tum domainler tamamlandiktan sonra toplu yazilmasi karari

## Degismez Mimari Kurallar

- PostgreSQL ve Spring Boot kalici veri ile is kurallarinin otoritesidir.
- Android form state'i, gecici UI state'i ve presentation mapper'lari yonetir;
  para, kapasite, rezervasyon, yetki veya odeme basarisi karari vermez.
- Kullanici/rehber sahipligi request icindeki `userId` veya `guideId` ile degil,
  JWT principal ile belirlenir.
- Rehber ve turist ekranlari ayni kalici `Tour` ve `TourSession` kaynagindan
  farkli response/UI mapper'lariyla beslenir.
- API entity dondurmez; request/response DTO kullanir.
- Controller is kurali yazmaz. Transaction ve is kurali service/application
  sinirinda kalir.
- Service, Spring Data repository ve gercek dis servis interface'lerine
  baglanir. Iyzico, medya ve FCM implementasyonlari adapter olur.
- Her metod icin ayri use-case sinifi, mikroservis, Redis, Kafka, generic outbox,
  dagitik kilit veya gereksiz history tablosu eklenmez.
- GuideMate'in canonical platform ve muhasebe para birimi `USD` olarak kalir.
  Tur fiyati, rezervasyon snapshot'i, wallet, ledger, rehber kazanci, komisyon
  ve raporlama USD minor unit `BIGINT`/Java `long` kullanir. Kart tahsilatinda
  kullanicinin sectigi provider para birimi bu canonical USD degerinden ayri bir
  odeme snapshot'i olarak tutulabilir; Android kur veya tahsilat tutari
  hesaplamaz. Iyzico ve kur saglayici major-unit sinirinda `BigDecimal`
  kullanilir; `double` kullanilmaz.
- Business zamanlari Java `Instant` ve PostgreSQL `TIMESTAMPTZ` olarak tutulur.
  Turun yerel saati canonical `timeZoneId` ile hesaplanir.
- Yeni migration, yeni Flyway dosyasidir. Uygulanmis migration degistirilmez.
- Teknik exception mesaji, stack trace, secret, token, tam IBAN veya kart verisi
  response ve loglara girmez.
- Business sonucu olan mutation endpoint'leri yalniz `Boolean` dondurmez;
  canonical sonuc veya stabil makine hata kodu dondurur.

## Mevcut Backend Baslangic Noktasi

Mevcut backend Spring Boot auth omurgasini tamamlamistir. Su yapilar korunur:

- `roles`, `users`, `confirmation_tokens`, `password_reset_tokens`,
  `refresh_tokens`
- JWT access token ve hash/rotation kullanan refresh session modeli
- `X-Installation-Id`
- Public rol seciminde yalniz `TOURIST` ve `GUIDE`
- Internal `ADMIN` rolu
- Ortak `ErrorResponse`, `fieldErrors`, 401 ve 403 handler'lari
- Flyway + Hibernate `validate`
- Local/prod profile ve secret ayrimi
- E-posta dogrulama, reset, sifre degistirme ve Google existing-account login

Yeni domainler ayni security ve hata sozlesmesini kullanir; paralel hata modeli
veya ikinci auth sistemi kurulmaz.

## Kimlik, Tip ve Sema Standartlari

- Mevcut auth kullanici kimligi `BIGINT` olarak kalir.
- Yeni public business kimlikleri PostgreSQL `UUID` olur.
- Android UUID'leri `String` olarak tasiyabilir.
- Mevcut `BaseEntity`, `Long id` ve legacy auth audit alanlari nedeniyle yeni
  UUID entity'ler tarafindan extend edilmez; mevcut auth/user/role kayitlari icin
  korunur.
- Yeni mutable UUID aggregate'leri icin sade bir `UuidAuditedEntity` kullanilir.
  Bu mapped superclass yalniz `UUID id`, `Instant createdAt` ve
  `Instant updatedAt` alanlarini tasir. `createdBy`, `updatedBy`, soft-delete
  veya `version` gibi her kayda gerekmeyen davranislar base sinifa eklenmez.
- Composite key kullanan join entity'leri ile immutable ledger/event kayitlari
  `UuidAuditedEntity` kullanmaya zorlanmaz; ihtiyac duyduklari kimlik ve zaman
  alanlarini acikca tanimlar.
- Optimistic-lock `@Version` ortak base sinifta degil, yalniz gercek stale
  update riski bulunan entity'lerde tutulur.
- Enum'lar veritabaninda okunabilir `VARCHAR`/check constraint ile saklanir;
  ordinal enum kullanilmaz.
- Para `amount_minor`, `price_minor`, `total_minor` gibi acik isimlerle tutulur.
- Her foreign key icin ihtiyaca uygun index kurulur.
- Mutable ve stale-update riski olan `tours`, `tour_sessions`, `reservations`
  gibi kayitlarda optimistic-lock `version` kullanilir.
- Fiziksel silme yalniz hic yayinlanmamis ve iliskisiz taslaklarda mumkundur.
  Yayinlanmis domain kayitlari archive/cancel/status ile korunur.

## Veritabani Tablolari

### 1. Mevcut Auth Tablolari

#### `roles`

- Mevcut yapi korunur.
- `ROLE_ADMIN` public request ile atanamaz.

#### `users`

- Mevcut `BIGINT` primary key korunur.
- Auth/current-user cevabi `userId`, `email`, `firstName`, `lastName`, `role`,
  `roleSelected` dondurmeye devam eder.
- Tur, rezervasyon, mesaj, odeme ve bildirim sahipliginin ana kullanici FK'sidir.

#### `confirmation_tokens`, `password_reset_tokens`, `refresh_tokens`

- Auth devir belgesindeki expiry, hash, rotation, replay ve revocation
  kurallariyla korunur.

### 2. Medya ve Rehber Profili

#### `media_assets`

Alanlar:

- `id UUID PK`
- `owner_user_id BIGINT FK users`
- `purpose VARCHAR`: `GUIDE_AVATAR`, `TOUR_COVER`
- `storage_key VARCHAR UNIQUE`
- `original_file_name VARCHAR`
- `content_type VARCHAR`
- `size_bytes BIGINT`
- `status VARCHAR`: `PENDING`, `READY`, `DELETED`
- `created_at TIMESTAMPTZ`, `updated_at TIMESTAMPTZ`

Kurallar:

- Android `content://`, `file://` veya drawable kimligini backend domainine
  gondermez.
- Android multipart upload yapar, backend kalici `mediaAssetId` ve mutlak URL
  dondurur. Tur/profile request'i bu kalici kimligi kullanir.
- Ilk CV/LAN surumunde `MediaStorage` interface arkasinda local filesystem
  adapter yeterlidir. Sonradan S3 benzeri storage'a gecis domaini degistirmez.
- Local kok dizin kaynak koda yazilmaz; `MEDIA_STORAGE_ROOT` environment
  variable'indan gelir ve build/gecici calisma klasorlerinin disinda kalir.
- Dosya binary'si PostgreSQL'e yazilmaz. Veritabaninda yalniz `media_assets`
  metadata'si ve storage key tutulur.
- Yalniz JPEG, PNG ve WebP kabul edilir. Dosya uzantisi tek basina guvenilir
  sayilmaz; MIME/icerik ve maksimum `5 MB` boyut backend'de dogrulanir.
- Kullanici tarafindan gelen dosya adi storage key olarak kullanilmaz. Backend
  tahmin edilemez benzersiz key uretir ve path traversal'a izin vermez.
- Her rehberin tek aktif `GUIDE_AVATAR`, her turun tek aktif `TOUR_COVER`
  medyasi olur. Ilk portfolio surumunde galeri veya coklu tur fotografi yoktur.
- Upload ve degistirme yetkisi JWT principal, medya sahipligi ve ilgili
  tur/profil sahipligiyle dogrulanir. Profil ve onayli tur gorselleri public
  projection icinde erisilebilir URL olarak sunulabilir.
- Backend'in dondurdugu mutlak `imageUrl`, opaque `mediaAssetId` kullanan
  kontrollu binary GET endpoint'ini hedefler. Filesystem kok dizini veya
  `storage_key` dogrudan public resource path olarak acilmaz.
- Public GET yalniz `READY` ve public profile ya da onayli tur gibi erisilebilir
  bir kaynaga bagli asset'i sunar. Public olmayan draft asset'i yalniz sahibi
  JWT ile okuyabilir; `PENDING`, `DELETED` veya erisime kapali asset sunulmaz.
- Binary response dogrulanmis `content_type`, `content_length`, uygun cache
  header'lari ve dosya bulunamazsa guvenli `404` dondurur. Request'ten dosya
  yolu veya dosya adi kabul edilmez.
- Yeni dosya kalici olarak `READY` olmadan profil/tur referansi degistirilmez.
  Basarisiz upload eski fotografi bozmaz; degistirme tamamlaninca sahipsiz eski
  dosya kontrollu bicimde temizlenir.
- Form kaydedilmeden terk edilen veya degistirme sonrasi referanssiz kalan
  `PENDING/READY` asset'ler configurable grace period sonrasinda scheduler ile
  temizlenir. Silmeden hemen once hicbir profile/tura bagli olmadigi yeniden
  kontrol edilir; aktif referansli dosyaya dokunulmaz. Fiziksel silme basarisiz
  olursa islem izlenebilir kalir ve sonraki cleanup calismasinda tekrar denenir.

#### `guide_profiles`

- `user_id BIGINT PK/FK users`
- `specialty_title VARCHAR`
- `biography TEXT`
- `avatar_media_id UUID NULL FK media_assets`
- `created_at`, `updated_at`

Bir `GUIDE` kullanicinin en fazla bir rehber profili olur.

#### `guide_languages`

- `guide_id BIGINT FK users`
- `language_code VARCHAR`
- Composite PK: `(guide_id, language_code)`

Dil katalog tablosu kurulmaz. ISO/uygulama tarafinda desteklenen stabil kodlar
kullanilir; gorunen metin Android resource'larindan gelir.

### 3. Tur ve Oturum

#### `tours`

- `id UUID PK`
- `guide_id BIGINT FK users`
- `title VARCHAR`
- `description TEXT`
- `country_code VARCHAR`
- `city_place_id VARCHAR`
- `city_name VARCHAR`
- `time_zone_id VARCHAR`
- `category_code VARCHAR`
- `cover_media_id UUID FK media_assets`
- `approval_status VARCHAR`: `PENDING_REVIEW`, `APPROVED`, `REJECTED`, `ARCHIVED`
- `submitted_at`, `published_at`, `reviewed_at TIMESTAMPTZ NULL`
- `reviewed_by BIGINT NULL FK users`
- `rejection_reason TEXT NULL`
- `version BIGINT`
- `created_at`, `updated_at`

Kurallar:

- `Tour` kalici deneyim kimligidir. Ad, aciklama, kategori, diller, konum,
  medya, rehber, yorum ve puan bu kimlige aittir.
- Yayinlanmis turun ulke/sehir kimligi yerinde degistirilmez. Maddi rota/sehir
  degisikligi yeni `Tour` olusturur ve eski yorumlari devralmaz.
- Rehber yalniz kendi turunu gorebilir/duzenleyebilir.
- Turist yalniz `APPROVED` tur projection'larini gorebilir.

#### `tour_languages`

- `tour_id UUID FK tours`
- `language_code VARCHAR`
- Composite PK: `(tour_id, language_code)`

#### `tour_sessions`

- `id UUID PK`
- `tour_id UUID FK tours`
- `meeting_point TEXT`
- `starts_at TIMESTAMPTZ`
- `duration_minutes INTEGER`
- `price_minor BIGINT`
- `currency_code VARCHAR`, backend config ile daima `USD`
- `capacity INTEGER`
- `status VARCHAR`: `OPEN_FOR_BOOKING`, `CLOSED`, `COMPLETED`, `CANCELLED`
- `cancellation_actor VARCHAR NULL`: `GUIDE`, `ADMIN`
- `cancellation_reason TEXT NULL`
- `cancelled_at TIMESTAMPTZ NULL`
- `version BIGINT`
- `created_at`, `updated_at`

Kurallar:

- Tarih, saat, sure, fiyat, kapasite ve oturuma ozel bulusma noktasi
  `TourSession` alanidir.
- `CLOSED` yalniz yeni satin almaya kapali demektir; tamamlanmis veya iptal
  edilmis anlamina gelmez.
- `COMPLETED`, `startsAt + durationMinutes` gecince backend tarafindan uygulanir.
- Turist iptali yalniz ilgili `reservation` kaydini etkiler; `TourSession`
  iptal etmez ve session cancellation actor'u olamaz.
- Mevcut kapsamda session'i yalniz rehber veya admin `CANCELLED` yapar. Zaman
  tabanli backend islemi session'i iptal etmek yerine `COMPLETED` yapar.
- `bookedCount` ana kaynak olarak mutable kolon olmaz. Onayli rezervasyonlar ve
  suresi dolmamis odeme bekleyen hold'lar üzerinden hesaplanir.
- Kapasite artabilir. Azaltma, mevcut confirmed + aktif hold katilimci
  toplamindan asagi inemez.
- Rezervasyonu olan oturumun tarih/saat/sure/bulusma bilgisi sessizce degismez;
  gerekiyorsa iptal + yeni oturum + iade/bildirim akisi kullanilir.
- Tamamlanan veya iptal edilen session duzenlenmez. Ayni turu yeniden yapmak
  ayni `tour_id` altinda yeni session olusturur.

#### `tour_change_requests`

- `id UUID PK`
- `tour_id UUID FK tours`
- `base_version BIGINT`
- `proposed_snapshot JSONB`
- `status VARCHAR`: `PENDING`, `APPROVED`, `REJECTED`, `CANCELLED`
- `submitted_by BIGINT FK users`
- `reviewed_by BIGINT NULL FK users`
- `submitted_at`, `reviewed_at TIMESTAMPTZ NULL`
- `rejection_reason TEXT NULL`

Kurallar:

- Bu tablo generic revision motoru degildir; yalniz admin onayi gerektiren
  kritik tur degisikligini saklar.
- Bir tur icin ayni anda en fazla bir `PENDING` change request olabilir.
- Baslik, aciklama, kategori, dil veya medya degisikligi burada bekler.
- Admin reddederse son onayli `tours` kaydi bozulmaz.
- Admin onayinda snapshot dogrulanip `tours` kaydina atomik uygulanir.

### 4. Rezervasyon ve Yorum

#### `reservations`

- `id UUID PK`
- `session_id UUID FK tour_sessions`
- `tourist_id BIGINT FK users`
- `participant_count INTEGER`
- `unit_price_minor BIGINT`
- `total_price_minor BIGINT`
- `currency_code VARCHAR`, daima `USD`
- `status VARCHAR`: `PENDING_PAYMENT`, `CONFIRMED`, `COMPLETED`, `CANCELLED`, `EXPIRED`
- `hold_expires_at TIMESTAMPTZ NULL`
- `cancellation_actor VARCHAR NULL`: `TOURIST`, `GUIDE`, `ADMIN`, `SYSTEM`
- `cancellation_reason TEXT NULL`
- `cancelled_at TIMESTAMPTZ NULL`
- `cancellation_policy_code VARCHAR`
- `cancellation_policy_version INTEGER`
- `snapshot_version INTEGER`
- `purchase_snapshot JSONB`
- `idempotency_key VARCHAR`
- `version BIGINT`
- `created_at`, `updated_at`

Snapshot en az sunlari korur:

- Tur kimligi, basligi, aciklamasi ve kapak URL/asset kimligi
- Rehber kimligi, gorunen adi ve avatar bilgisi
- Ulke, sehir, `placeId`, `timeZoneId`
- Kategori ve diller
- Session tarih/saat, sure ve bulusma noktasi
- Kisi basi fiyat, toplam fiyat, para birimi ve katilimci sayisi
- Satin alma aninda gecerli iptal politikasi

Kurallar:

- Ayni turist/session icin ayni anda bir aktif rezervasyon bulunur.
- `PENDING_PAYMENT` ve suresi dolmamis rezervasyon temporary seat hold'dur;
  ayri `capacity_holds` tablosu kurulmaz.
- `CONFIRMED` ancak odeme veya wallet debit basarili olduktan sonra olusur.
- Payment failure/timeout hold'u `EXPIRED` yapar ve kapasiteyi serbest birakir.
- Session status ile reservation status birbirinin yerine kullanilmaz.

#### `reviews`

- `id UUID PK`
- `reservation_id UUID UNIQUE FK reservations`
- `rating SMALLINT CHECK 1..5`
- `comment TEXT NULL`
- `created_at`, `updated_at`

Kurallar:

- Yorum yalniz kullanicinin sahip oldugu, tamamlanmis ve gerceklesmis
  rezervasyona bir kez yazilir.
- Iptal, refund, failed veya gerceklesmemis rezervasyon yorum yapamaz.
- Tur puani gecerli yorumlardan hesaplanir; mutable ortalama kolon otorite olmaz.
- Rehber ortalamasi tur ortalamalarinin ortalamasi degil, rehbere ait tum
  gecerli bireysel yorumlarin ortalamasidir.

### 5. Odeme, Kart, Cuzdan ve Iade

#### Sandbox Buyer Profili - Tablo Degildir

- GuideMate sandbox/CV kapsaminda kullanicidan gercek T.C. kimlik numarasi,
  telefon veya fatura adresi toplamaz ve bunlari PostgreSQL'de saklamaz.
- Iyzico Checkout Form'un zorunlu buyer/billing alanlari yalniz local/sandbox
  Spring profile'inda calisan `SandboxBuyerProfileProvider` tarafindan test
  configuration'inden uretilir.
- Test kimligi, telefonu ve adresi Android request'ine, Android modeline veya
  UI state'ine girmez.
- Her odemede `buyer.id`, authenticated GuideMate `userId` degerinden benzersiz
  ve provider-safe bicimde uretilir. `paymentId`, `conversationId`,
  `reservationId` ve `idempotencyKey` de ayridir; ortak test adresi/T.C. farkli
  kullanicilarin odeme, kart, rezervasyon veya wallet kayitlarini karistirmaz.
- Provider saved-card destegi kullanilirsa provider customer/card tokeni her
  GuideMate kullanicisina backend'de ayri baglanir; test T.C. sahiplik anahtari
  olarak kullanilmaz.
- Production profile sandbox buyer bilgilerine sessizce dusmez. Gercek canli
  odeme istenirse ayri buyer/billing profili, KVKK/PCI/hukuki gereksinimler ve
  gercek veri toplama UI'i tamamlanmadan uygulama fail-fast davranir.

#### `payment_provider_customers`

Iyzico Card Storage hesabi icin `V8__create_saved_payment_method_schema.sql`
ile uygulanmistir:

- `user_id BIGINT PK/FK users`
- `provider VARCHAR`
- `provider_customer_key_encrypted TEXT`
- `provider_customer_key_fingerprint VARCHAR`
- `version BIGINT`
- `created_at`, `updated_at`

#### `saved_payment_methods`

Provider-backed kayitli kart projection'i olarak ayni `V8` migration'i ile
uygulanmistir:

- `id UUID PK`
- `user_id BIGINT FK users`
- `provider VARCHAR`
- `provider_card_token_encrypted TEXT`
- `provider_card_token_fingerprint VARCHAR`
- `alias VARCHAR NULL`
- `bank_name VARCHAR NULL`
- `bank_code VARCHAR NULL`
- `card_family VARCHAR NULL`
- `card_association VARCHAR NULL`
- `card_type VARCHAR NULL`
- `last_four_digits VARCHAR`
- `expiry_month SMALLINT NULL`, `expiry_year SMALLINT NULL`
- `is_default BOOLEAN`
- `default_guard BOOLEAN NULL`
- `status VARCHAR`: `ACTIVE`, `DELETED`, `EXPIRED`
- `version BIGINT`
- `created_at`, `updated_at`

Tam kart numarasi ve CVV hicbir zaman tutulmaz veya loglanmaz. Android'e
provider tokeni dondurulmaz; yalniz internal `savedPaymentMethodId` ve maskeli
metadata dondurulur.

#### `payment_fx_quotes`

- `id UUID PK`
- `user_id BIGINT FK users`
- `purpose VARCHAR`: `WALLET_TOP_UP`, `TOUR_BOOKING`
- `base_amount_minor BIGINT`, canonical USD tutari
- `base_currency_code VARCHAR`, daima `USD`
- `charge_amount_minor BIGINT`, provider'a gonderilecek tutar
- `charge_currency_code VARCHAR`
- `fx_rate NUMERIC(24,12)`
- `rate_source VARCHAR`
- `expires_at`, `consumed_at TIMESTAMPTZ NULL`
- `created_at TIMESTAMPTZ`

Quote, checkout'tan once backend tarafindan olusturulan kisa omurlu ve
kullaniciya bagli bir snapshot'tir. Android kur, charge tutari veya expiry
uretmez. Ayni quote yalniz ayni kullanici, amac, canonical tutar ve secilen para
birimi icin kullanilir; suresi gecmis ya da farkli intent ile kullanilan quote
reddedilir. Tablo ve ilgili canonical-charge snapshot alanlari `V13` migration'i
ile eklenmistir.

#### `payments`

- `id UUID PK`
- `user_id BIGINT FK users`
- `purpose VARCHAR`: `WALLET_TOP_UP`, `TOUR_BOOKING`
- `method VARCHAR`: `WALLET`, `SAVED_CARD`, `HOSTED_CARD`
- `reservation_id UUID NULL FK reservations`
- `amount_minor BIGINT`, canonical platform tutari
- `currency_code VARCHAR`, daima `USD`
- `charge_amount_minor BIGINT NULL`, provider tahsilat tutari
- `charge_currency_code VARCHAR NULL`, provider tahsilat para birimi
- `fx_quote_id UUID NULL FK payment_fx_quotes`
- `fx_rate NUMERIC(24,12) NULL`, kullanilan kur snapshot'i
- `fx_quoted_at TIMESTAMPTZ NULL`
- `status VARCHAR`: `PENDING`, `REQUIRES_ACTION`, `VERIFYING`, `SUCCEEDED`, `FAILED`, `CANCELLED`, `TIMEOUT`
- `provider VARCHAR NULL`
- `provider_payment_id VARCHAR NULL UNIQUE`
- `provider_token_encrypted TEXT NULL`
- `provider_conversation_id VARCHAR NULL`
- `idempotency_key VARCHAR`
- `expires_at`, `verified_at TIMESTAMPTZ NULL`
- `failure_code VARCHAR NULL`
- `created_at`, `updated_at`

`REFUNDED` gorunumu `refunds` tablosundan turetilir; payment kaydinin basarili
oldugu gercegi silinmez. Wallet ile tur satin alma da bu tabloda
`purpose=TOUR_BOOKING`, `method=WALLET`, `status=SUCCEEDED` olarak kaydedilir;
provider alanlari `NULL` kalir. Wallet odemesi payment kaydi atlanarak yalniz
ledger hareketi veya reservation olarak tutulmaz.

#### `payment_events`

- `id UUID PK`
- `payment_id UUID FK payments`
- `event_type VARCHAR`
- `provider_event_id VARCHAR NULL`
- `payload_hash VARCHAR NULL`
- `provider_status VARCHAR NULL`
- `occurred_at TIMESTAMPTZ`
- `created_at TIMESTAMPTZ`

Ham hassas provider payload'i kaydedilmez. Gerekli guvenli normalize alanlar,
imza sonucu ve payload hash audit/reconciliation icin yeterlidir.

#### `refunds`

- `id UUID PK`
- `payment_id UUID FK payments`
- `requested_by BIGINT FK users`
- `amount_minor BIGINT`, canonical USD iade tutari
- `currency_code VARCHAR`, daima `USD`
- `charge_amount_minor BIGINT NULL`, provider'a asli tahsilat para biriminde
  gonderilen iade tutari
- `charge_currency_code VARCHAR NULL`
- `status VARCHAR`: `REQUESTED`, `PROCESSING`, `SUCCEEDED`, `FAILED`, `MANUAL_REVIEW`
- `provider_refund_id VARCHAR NULL UNIQUE`
- `idempotency_key VARCHAR`
- `failure_code VARCHAR NULL`
- `requested_at`, `completed_at TIMESTAMPTZ NULL`
- `created_at`, `updated_at`

Iptal ve iade farkli state'lerdir. Rezervasyon `CANCELLED` olsa bile iade
`PROCESSING`, `FAILED` veya `MANUAL_REVIEW` olabilir. Uygun wallet iadesi ayni
payment'a bagli refund kaydi ile izlenir; harici provider gerektirmedigi icin
refund `SUCCEEDED` ve wallet ledger credit ayni transaction'da olusur.

#### `wallets`

- `id UUID PK`
- `user_id BIGINT UNIQUE FK users`
- `currency_code VARCHAR`, daima `USD`
- `created_at`, `updated_at`

#### `wallet_ledger_entries`

- `id UUID PK`
- `wallet_id UUID FK wallets`
- `direction VARCHAR`: `CREDIT`, `DEBIT`
- `type VARCHAR`: `TOP_UP`, `TOUR_PURCHASE`, `REFUND`, `GUIDE_EARNING`, `WITHDRAWAL`, `EARNING_REVERSAL`
- `amount_minor BIGINT CHECK > 0`
- `reference_type VARCHAR`
- `reference_id UUID`
- `idempotency_key VARCHAR`
- `occurred_at TIMESTAMPTZ`
- `created_at TIMESTAMPTZ`

Ledger kayitlari immutable olur. Bakiye posted credit eksi posted debit'tir;
pending para cekme rezervasyonlari available balance hesabindan ayrica dusulur.

### 6. Rehber Kazanci ve Para Cekme

#### `guide_earnings`

- `id UUID PK`
- `reservation_id UUID UNIQUE FK reservations`
- `gross_minor BIGINT`
- `platform_fee_minor BIGINT`
- `net_minor BIGINT`
- `currency_code VARCHAR`
- `status VARCHAR`: `PENDING`, `AVAILABLE`, `REVERSED`
- `available_at`, `reversed_at TIMESTAMPTZ NULL`
- `created_at`, `updated_at`

Kazanc, odeme dogrulanip session tamamlandiktan ve ilgili iade/iptal kurallari
uygulandiktan sonra `AVAILABLE` olur. Bu geciste `GUIDE_EARNING` tipinde tek ve
idempotent bir wallet ledger credit kaydi olusturulur. `AVAILABLE`, kazancin
cuzdana islendigini ifade eder; bu kazancin henuz harcanmadigi veya cekilmedigi
anlamina gelmez. Para cekme islemleri kazanc durumunu degistirmez ve cekilebilir
bakiye tek tek kazanc kayitlarindan degil wallet ledger'dan hesaplanir. Aylik
kazanc ayri tablo degil, bu kayittan query/projection ile uretilir.

`PENDING` kazanc cuzdan kredisi olusmadan iptal edilirse dogrudan `REVERSED`
olur. `AVAILABLE` kazanc daha sonra geri alinacaksa durum `REVERSED` yapilir ve
`EARNING_REVERSAL` tipinde tek ve idempotent ledger debit kaydi olusturulur.

#### `bank_accounts`

- `id UUID PK`
- `guide_id BIGINT FK users`
- `iban_encrypted TEXT`
- `iban_fingerprint VARCHAR`
- `masked_iban VARCHAR`
- `bank_code VARCHAR`
- `bank_name VARCHAR`
- `account_holder_name VARCHAR`
- `is_default BOOLEAN`
- `status VARCHAR`: `ACTIVE`, `DISABLED`
- `created_at`, `updated_at`

Backend IBAN formatini, TR uzunlugunu, MOD97'yi ve banka kodunu tekrar
dogrular. Portfolio kapsaminda gercek KYC/hesap sahipligi dogrulamasi yapilmaz.

#### `withdrawals`

- `id UUID PK`
- `wallet_id UUID FK wallets`
- `bank_account_id UUID FK bank_accounts`
- `amount_minor BIGINT`
- `currency_code VARCHAR`
- `status VARCHAR`: `PENDING`, `PROCESSING`, `COMPLETED`, `FAILED`, `CANCELLED`
- `idempotency_key VARCHAR`
- `provider_reference VARCHAR NULL`
- `failure_code VARCHAR NULL`
- `requested_at`, `completed_at TIMESTAMPTZ NULL`
- `created_at`, `updated_at`

Request transaction'i bank account sahipligini ve available balance'i kontrol
eder, `PENDING` kaydi olusturur ve tutari ayni anda rezerve eder. `FAILED` veya
`CANCELLED` rezervasyonu serbest birakir. `COMPLETED` gecisinde rezerve edilen
tutar serbest birakilirken ayni transaction'da `WITHDRAWAL` tipinde tek ve
idempotent ledger debit kaydi olusturulur. Bir cekim birden fazla kazancin
toplamindan veya bir kazancin yalniz bir bolumunden karsilanabilir; bu nedenle
kazanc kayitlari para cekme islemine gore `WITHDRAWN` yapilmaz.

### 7. Mesajlasma

#### `chat_conversations`

- `id UUID PK`
- `guide_id BIGINT FK users`
- `tourist_id BIGINT FK users`
- `created_at`, `updated_at`
- Unique: `(guide_id, tourist_id)`

Ayni rehber-turist cifti rezervasyon veya turdan bagimsiz tek kalici sohbet
kullanir.

#### `chat_messages`

- `id UUID PK`
- `conversation_id UUID FK chat_conversations`
- `sender_id BIGINT FK users`
- `client_message_id UUID`
- `body TEXT CHECK (char_length(body) BETWEEN 1 AND 2000)`
- `sent_at TIMESTAMPTZ`
- Unique: `(sender_id, client_message_id)`

`isFromMe` veritabaninda tutulmaz; Android `senderId == currentUserId` ile
hesaplar. Backend'e yazilan mesaj `SENT` kabul edilir; `PENDING` ve `FAILED`
Android'in gecici UI state'idir. Mesaj DTO'su `@NotBlank` ve `@Size(max = 2000)`
ile dogrulanir. REST ve WebSocket/STOMP ayni application service/command
dogrulamasindan gecmeden mesaj kaydedemez.

#### `chat_read_state`

- `conversation_id UUID FK chat_conversations`
- `user_id BIGINT FK users`
- `last_read_message_id UUID NULL FK chat_messages`
- `read_at TIMESTAMPTZ NULL`
- Composite PK: `(conversation_id, user_id)`

Sohbet listesi, detay, topbar bilgisi ve bottom-bar unread badge ayni read
state'ten turetilir.

### 8. Bildirim

#### `notifications`

- `id UUID PK`
- `recipient_id BIGINT FK users`
- `type VARCHAR`
- `actor_id BIGINT NULL FK users`
- `payload JSONB`
- `read_at TIMESTAMPTZ NULL`
- `push_status VARCHAR NULL`: `NOT_REQUESTED`, `PENDING`, `SENT`, `FAILED`
- `last_push_attempt_at TIMESTAMPTZ NULL`
- `created_at TIMESTAMPTZ`

Payload Android route adi tasimaz. Semantik `type` ve `tourId`, `sessionId`,
`reservationId`, `reviewId`, `chatId`, `paymentId`, `refundId`, `withdrawalId`
gibi kimlikler tasir.

Rehber ana sayfa son hareketleri, topbar unread sayisi ve notification paneli
bu tablodan beslenir. Cuzdan hareketleri ayri ledger projection'idir.

#### `notification_preferences`

- `user_id BIGINT PK/FK users`
- `upcoming_tour_reminders_enabled BOOLEAN NOT NULL DEFAULT TRUE`
- `chat_messages_enabled BOOLEAN NOT NULL DEFAULT TRUE`
- `reservation_updates_enabled BOOLEAN NOT NULL DEFAULT TRUE`
- `review_requests_enabled BOOLEAN NOT NULL DEFAULT TRUE`
- `payments_and_earnings_enabled BOOLEAN NOT NULL DEFAULT TRUE`
- `new_reviews_enabled BOOLEAN NOT NULL DEFAULT TRUE`
- `updated_at TIMESTAMPTZ`

Rehber ve turist icin ayri tablo kurulmaz; tek superset preference kaydi rol
tarafindan kullanilan alanlari dondurur. Android'deki `guideMessages` ve
`touristMessages` ayni `chatMessagesEnabled` API alani ile map edilir.

Bu tercihler domain event'ini, notification kaydini, chat mesajini veya unread
sayisini engellemez; yalniz ilgili FCM push/reminder teslimatini kontrol eder.
Guvenlik uyarilari kullanici tarafindan kapatilamaz. Bu nedenle tabloda
`security_alerts_enabled` tutulmaz; GET response'u Android'in kilitli satiri icin
read-only `securityAlertsEnabled=true` dondurur ve PATCH bu alani kabul etmez.

#### `device_registrations`

- `id UUID PK`
- `user_id BIGINT FK users`
- `installation_id UUID`
- `firebase_installation_id VARCHAR(128) UNIQUE`
- `platform VARCHAR`: `ANDROID`
- `active BOOLEAN`
- `last_seen_at TIMESTAMPTZ`
- `created_at`, `updated_at`

`installation_id` GuideMate kurulum kimligidir; FCM teslimati icin kullanilan
Firebase Installation ID (FID) ayri alanda tutulur. Logout/account switch eski
kurulum iliskisini pasif hale getirir. FID response'a veya log'a yazilmaz. FCM
credential yalniz backend secret/environment'ta bulunur.

## Olusturulmayacak Tablolar

- `categories`: Stabil uygulama kategori kodlari yeterli.
- `countries`, `cities`, `languages`: Google Place ID ve ISO/stabil kodlar
  kullanilir.
- `tour_popularity`: Populerlik query ile turetilir.
- `guide_levels`: Seviye performanstan hesaplanir.
- `guide_performance_summary`: Ilk surumde projection/query olur.
- `monthly_earnings`: `guide_earnings` uzerinden ay/yil sorgulanir.
- `capacity_holds`: `PENDING_PAYMENT reservations` hold gorevini yapar.
- `chat_participants`: Sohbet tam olarak guide-tourist cifti oldugu icin gerekmez.
- Generic `audit_log`, generic revision engine, outbox veya soft-delete tablosu.
- `favorites`: Mevcut urun akisi ve Android modelinde gercek ozellik yoktur.
- `support`, `faq`, `legal`: Simdilik statik localized UI icerigidir.
- `tourist_profiles`: Mevcut ihtiyac auth user, wallet ve reservation
  kayitlariyla karsilanir; gercek yeni turist profil alanlari cikmadan tablo
  eklenmez.
- `payment_customer_profiles`: Sandbox-only GuideMate gercek T.C., telefon veya
  adres toplamaz; zorunlu iyzico buyer/billing alanlari backend sandbox
  configuration'indan gelir.

## Iliski Ozeti

- `users 1:1 guide_profiles`
- `users 1:N media_assets`
- `guides N:N languages` through `guide_languages`
- `guides 1:N tours`
- `tours N:N languages` through `tour_languages`
- `tours 1:N tour_sessions`
- `tours 1:N tour_change_requests`
- `tour_sessions 1:N reservations`
- `tourists 1:N reservations`
- `reservations 1:0..1 reviews`
- `reservations 1:N payments`
- `payments 1:N refunds`
- `reservations 1:0..1 guide_earnings`
- `users 1:1 wallets`
- `wallets 1:N wallet_ledger_entries`
- `guides 1:N bank_accounts`
- `wallets 1:N withdrawals`
- `bank_accounts 1:N withdrawals`
- `guide + tourist 1:1 chat_conversation`, conversation `1:N messages`
- `users 1:N notifications/device_registrations`

### Normalize Iliski Otoritesi

- `reservations.tour_id` tutulmaz; tur `session_id -> tour_sessions.tour_id`
  zincirinden bulunur.
- `reviews` yalniz `reservation_id` ile sahiplik ve uygunluk kazanir. Tur,
  session ve tourist kimlikleri reservation zincirinden turetilir.
- `refunds` yalniz `payment_id` ile asil tahsilata baglanir. Tur iadesinde
  reservation `payments.reservation_id` uzerinden bulunur; `requested_by` ise
  islemi baslatan aktor oldugu icin korunur.
- `guide_earnings` rehberini reservation -> session -> tour zincirinden;
  `withdrawals` sahibini wallet -> user zincirinden bulur. Ayni kimlikler ikinci
  FK olarak saklanmaz.
- `tour_sessions.tour_id` ve `tours.guide_id` sahiplik zinciri olustuktan sonra
  baska kayda tasinamaz. Withdrawal olusturulurken wallet ve bank account'un
  ayni rehbere ait oldugu service katmaninda dogrulanir.
- Reservation `purchase_snapshot` tarihi gorunumu korur; normalize FK
  kolonlarinin kaldirilmasi snapshot icindeki tur/rehber kimligini kaldirmaz.
- Backend response DTO'lari Android'in ihtiyaci olan `tourId`, `sessionId` veya
  owner bilgilerini join/projection ile dondurebilir. Bu alanlar kalici iliski
  otoritesi degildir.

## Kritik Constraint ve Index'ler

- Tum FK kolonlari indexlenir.
- `amount_minor`, `price_minor`, `capacity`, `duration_minutes`,
  `participant_count` pozitif check alir.
- `rating` 1 ile 5 arasinda check alir.
- Ayni tur/rehber dil kodu tekrar edemez.
- Ayni turist/session icin `PENDING_PAYMENT` veya `CONFIRMED` tek aktif
  rezervasyon olabilir.
- Bir tur icin tek `PENDING` change request olabilir.
- Idempotency key kullanici + operation scope'unda unique olur.
- Provider payment, refund ve event kimlikleri tekrar edemez.
- Kullanici basina tek default saved card ve rehber basina tek default banka
  hesabi `default_guard` + unique constraint ile korunur.
- Onemli indexler: session `(tour_id)`, `(status, starts_at)`; tour
  `(guide_id, approval_status)`; reservation `(session_id, status)`,
  `(tourist_id, created_at)`; review unique `(reservation_id)` ve
  `(created_at)`; payment `(reservation_id)`; refund `(payment_id)`;
  withdrawal `(wallet_id, requested_at)`; notification
  `(recipient_id, read_at, created_at)`; message `(conversation_id, sent_at)`;
  ledger `(wallet_id, occurred_at)`.

## State Machine Kurallari

### Tur Onayi

- Yeni tur -> `PENDING_REVIEW`
- Admin onay -> `APPROVED`, `publishedAt` set, uygun gelecek session acilir
- Admin red -> `REJECTED` + kullanici dostu neden
- Artik kullanilmayacak ust tur -> `ARCHIVED`

### Session

- `OPEN_FOR_BOOKING` <-> `CLOSED`: yalniz uygun, gelecek ve approved session
- Zaman dolunca -> `COMPLETED`
- Rehber/admin iptali -> `CANCELLED`
- Switch kapatmak cancel veya history'ye tasimak degildir.

### Rezervasyon

- Seat hold -> `PENDING_PAYMENT`
- Verified payment/wallet debit -> `CONFIRMED`
- Session gerceklesince -> `COMPLETED`
- Timeout -> `EXPIRED`
- Rehber/turist/admin iptali -> `CANCELLED`, refund state ayri izlenir

### Odeme

- `PENDING` -> `REQUIRES_ACTION` -> `VERIFYING` -> `SUCCEEDED`
- Alternatif sonlar: `FAILED`, `CANCELLED`, `TIMEOUT`
- Refund payment gecmisini silmez; `refunds` tablosunda izlenir.

### Iade

- `REQUESTED` -> `PROCESSING` -> `SUCCEEDED`
- Alternatif sonlar: `FAILED`, `MANUAL_REVIEW`

### Kazanc ve Para Cekme

- Kazanc: `PENDING` -> `AVAILABLE`; `PENDING` veya `AVAILABLE` -> `REVERSED`
- Para cekme: `PENDING` -> `PROCESSING` -> `COMPLETED`
- Alternatif sonlar: `FAILED`, `CANCELLED`

## Tur ve Admin Is Kurallari

- Rehberin `Turlarim` sorgusu JWT guide kimligine gore filtrelenir.
- Edit/cancel/session ekleme islemleri `tour.guideId == principal.userId`
  kontrolu yapar.
- Kritik tur editleri admin onayina gider; son onayli veri korunur.
- Rezervasyonsuz gelecek session tarih/saat/sure/bulusma noktasini
  degistirebilir.
- Rezervasyonlu session sessizce plan degistiremez.
- Fiyat degisikligi yalniz yeni rezervasyonlari etkiler; snapshot eski fiyati
  korur.
- Admin endpoint'leri yalniz `ADMIN` rolune acilir.
- Ayri admin uygulamasi zorunlu degildir; Swagger/Postman/IntelliJ HTTP Client
  ile onay/red yapilabilir.
- `Yayinda` sayaci approved + future + `OPEN_FOR_BOOKING` session sayisidir.
- `Onay Bekliyor` sayaci `PENDING_REVIEW` tur/change request projection'idir.
- Android manuel `+1/-1` sayac yapmaz; repository refresh/Flow'dan turetir.

## Arama, Populerlik, Puan ve Rehber Seviyesi

Turist arama/populer adaylari:

- Tur `APPROVED` olmali.
- En az bir gelecek `OPEN_FOR_BOOKING` session olmali.
- Kalan kapasite olmali.
- Filtre country code, city place id, category code, language code, tarih ve
  fiyat araligina gore uygulanabilir.

Populer siralama ilk surumde SQL projection ile weighted rating kullanir.
Tek bir 5 yildizli yorum, cok sayida 4.9 yorumu olan turu otomatik gecmez.
Esitlikte review count, confirmed booking count ve yakin session tarihi gibi
deterministic tie-break kullanilir. Cache/materialized view ilk surumde yoktur.

`GuidePerformanceSummary` backend projection'i sunlari uretir:

- `completedSessionCount`: gerceklesmis, uygun session sayisi
- `totalParticipantCount`: iptal/iade edilmemis tamamlanmis rezervasyonlarin
  `participantCount` toplami
- `averageRating`: rehbere ait tum gecerli bireysel yorumlar
- `reviewCount`
- `level`

### Tur ve Rehber Arama ile Dashboard Projection'lari

`GET /api/v1/guides/search` yalniz aktif `GUIDE` rolune ve public rehber
profiline sahip kullanicilari sayfali dondurur. Ilk portfolio surumunde
PostgreSQL case-insensitive ad, soyad ve `specialty_title` sorgusu yeterlidir;
Elasticsearch, ayri search servisi, materialized view veya cache kurulmaz.
Response item en az `guideId`, ad-soyad, unvan, avatar `imageUrl`, konusulan dil
kodlari, `averageRating`, `reviewCount` ve `completedSessionCount` tasir. Puan ve
tur sayisi Android tarafinda yeniden hesaplanmaz; mevcut
`GuidePerformanceSummary` projection'ina dayanir.

`GET /api/v1/tours/search` sonucu tur kaydi degil, satin alinabilir session
projection'idir. Ayni turun farkli tarihli oturumlari ayri sonuc olabilir.
Backend her sorguda yalniz `APPROVED` tur, gelecek tarihli
`OPEN_FOR_BOOKING` session ve pozitif kullanilabilir kapasite dondurur.

Desteklenen optional filtreler:

- `q`: Tur basligi ve sehir adinda case-insensitive arama
- `countryCode`
- `cityPlaceId`
- `categoryCode`
- `languageCodes`: tekrar eden query parametresi olarak birden fazla stabil kod
- `minRating`
- `minPriceMinor` ve `maxPriceMinor`; para birimi platform standardi `USD`
- `page`, `size` ve stabil `sort`: `STARTS_AT_ASC`, `RATING_DESC`,
  `PRICE_ASC`, `PRICE_DESC`

Backend bilinmeyen kategori/dil kodunu, gecersiz puan araligini, negatif veya
ters fiyat araligini validation hatasiyla reddeder. Arama ilk portfolio
surumunde PostgreSQL query/specification ile yapilir; Elasticsearch, ayri search
servisi veya cache eklenmez.

Tur arama item'i en az `tourId`, `sessionId`, baslik, kategori kodu, sehir/ulke,
`startsAt`, `timeZoneId`, sure, `priceMinor`, `currencyCode`,
`availableCapacity`, dil kodlari, kapak `imageUrl`, `averageRating`,
`reviewCount` ve public rehber ozetini tasir. Android yuklenen sayfayi tekrar
lokal filtreleyerek backend sonucunu degistirmez.

Rehberin kendi tur karti response'u `capacity` alaninda toplam kapasiteyi,
`bookedCount` alaninda dolu katilimci sayisini tasir. `averageRating` ve
`reviewCount` tur bazli; nullable `netEarningsMinor` session bazlidir. Session
kazancina `PENDING` ve `AVAILABLE` dahil, `REVERSED` harictir; iptal edilen veya
henuz kazanc olusmayan session icin `netEarningsMinor=null` dondurulur. Kart
sayfasindaki tur puanlari tour ID listesiyle, kazanc session ID listesiyle toplu
sorgulanir; kart basina ayri sorgu/N+1 olusturulmaz.

`GET /api/v1/guides/me/dashboard` rehberi JWT principal'dan bulur ve tek bir
current-guide projection'i dondurur:

- `activeSessionCount`: Rehber `Turlarim > Aktif` sekmesindeki ayni filtreyi
  karsilayan toplam kart/session sayisi; yuklenen sayfanin `size` degeri degildir.
- `pendingReviewCount`: Ilk yayin icin `PENDING_REVIEW` turlar ile `PENDING` kritik
  tour change request'lerinin toplami.
- `completedSessionCount`, `totalParticipantCount`, `averageRating`,
  `reviewCount`, `level`: ortak `GuidePerformanceSummary` sonucu.
- `currentMonthEarningsMinor`, `currencyCode`: `guide_earnings` projection'i.

Dashboard ayri tablo veya mutable sayac kolonlari tutmaz. Degerler sahiplik,
session zamani/durumu, approval/change-request, gecerli rezervasyon/yorum ve
kazanc kurallariyla query/projection olarak hesaplanir. Recent activity ve
notification listesi kendi endpoint'inden gelmeye devam eder.

Mevcut seviye esikleri korunur:

- `APPROVED`: temel seviye
- `SILVER`: en az 5 tamamlanmis session, 3.7 puan, 3 yorum
- `SUPER`: en az 20 tamamlanmis session, 4.5 puan, 10 yorum
- `LEGENDARY`: en az 100 tamamlanmis session, 4.8 puan, 30 yorum

Bu tek projection rehber ana sayfasi, profil, seviye bottom sheet'i, turistin
gordugu rehber ozeti ve ileride en iyi rehberler listesini besler.

## Rezervasyon, Kapasite ve Transaction Akisi

### Dogrulama Otoritesi ve Limitler

- Android pozitif tutar, en az bir katilimci ve ekranda gorunen bakiye/kontenjan
  icin hizli on kontrol yapar. Bu kontroller yalniz UX'tir; atlatilabilir veya
  ekrandaki veri eski kalabilir.
- Backend tur fiyati, toplam tutar, guncel bakiye, cekilebilir bakiye, session
  durumu, kalan kapasite, katilimci sayisi ve iade edilebilir tutarin tek
  otoritesidir. Ayni kontroller her mutation'da guncel DB verisiyle tekrarlanir.
- PostgreSQL pozitif `amount_minor`, `price_minor`, `capacity` ve
  `participant_count` constraint'leriyle son veri butunlugu katmanidir.
- Tur aliminda Android toplam fiyat gondermez. Yalniz `sessionId` ve
  `participantCount` gonderir; backend guncel kisi basi fiyatla toplam tutari
  guvenli minor-unit aritmetigiyle hesaplar.
- Wallet top-up intent'i yalniz pozitif canonical USD miktari tasir; coklu
  tahsilat quote istegi buna secilen `chargeCurrencyCode` degerini
  ekler, kur veya hesaplanmis charge tutari eklemez. Withdrawal istegi yalniz
  `bankAccountId` ve pozitif miktar tasir. Withdrawal guncel available balance'i
  asamaz ve tutar transaction icinde rezerve edilir.
- Katilimci sayisi en az `1`, en fazla transaction anindaki kalan kapasite
  olabilir. Gerekcesiz kisi basi satin alma ust siniri eklenmez.
- Iade miktari pozitif olur; asil tahsilati ve kalan iade edilebilir miktari
  asamaz.
- Kaynak koda rastgele `$5.000` gibi urun limitleri gomulmez. Iyzico'nun resmi
  veya Sandbox'ta dogrulanan islem siniri varsa provider adapter/config
  seviyesinde uygulanir; ileride gercek business limiti cikarsa backend
  configuration ile yonetilir.
- Iyzico Sandbox pozitif price, sepet-toplam tutarliligi, kart/banka sonucu,
  3DS, kart limiti, provider rate limit ve provider iade sinirlarini kontrol
  eder. GuideMate wallet bakiyesi, tur kontenjani, kisi basi fiyat ve iptal/iade
  politikasini bilmez; bunlar backend'de kalir.
- Wallet bakiyesiyle tur aliminda iyzico cagrilmaz. Kartli tahsilat/top-up ve
  provider destekli iade/payout akislarinda once backend kurallari, sonra iyzico
  kontrolleri uygulanir; verified provider sonucu olmadan local basari yazilmaz.

### Kartla Tur Satin Alma

1. Android `sessionId`, `participantCount` ve `Idempotency-Key` gonderir.
2. Backend JWT turistini bulur; session/tour durumunu, zamani, approval'i ve
   kapasiteyi yeniden kontrol eder.
3. Transaction icinde session row lock alinir, aktif hold + confirmed
   katilimci sayisi hesaplanir.
4. Yer varsa `PENDING_PAYMENT reservation` ve `PENDING payment` olusturulur;
   transaction commit edilir.
5. DB transaction acik tutulmadan iyzico Checkout Form initialize edilir.
6. Basarili initialize sonucu payment `REQUIRES_ACTION` olur ve hosted URL/token
   Android'e dondurulur. Basarisizlikta payment failed, hold expired olur.
7. Callback/webhook geldikten sonra imza kontrol edilir ve iyzico retrieve ile
   sonuc tekrar dogrulanir.
8. Verified success transaction'inda reservation ve session lock altinda tekrar
   okunur. Hold hala gecerliyse veya hold bitmis olsa da session hala satin
   alinabilir ve yeterli kapasite varsa payment `SUCCEEDED`, reservation
   `CONFIRMED` olur.
9. Gec gelen verified success aninda session artik satin alinamiyorsa veya
   kapasite kalmadiysa payment gercegi `SUCCEEDED` olarak korunur, reservation
   `EXPIRED` kalir ve ayni transaction'da idempotent tam iade talebi `REQUESTED`
   olusturulur. Provider refund/cancel cagrisi transaction commit edildikten
   sonra yapilir. Otomatik iade guvenle baslatilamaz ya da sonucu tekrar eden
   reconciliation sonrasinda da kesinlestirilemezse refund `MANUAL_REVIEW` olur.
10. Payment basarili fakat local finalize basarisizsa reconciliation tekrar
    retrieve eder ve ayni lock/finalization kurallarini uygular; ikinci kez
    para, rezervasyon veya iade olusturmaz.
11. Basarisizlik/timeout hold'u serbest birakir ve reservation'i `EXPIRED`
    yapar.

Gec odeme sonucunda otomatik tam iade varsayilandir; `MANUAL_REVIEW` yalniz
provider sonucunun veya iade isleminin guvenli ve otomatik bicimde
kesinlestirilemedigi istisnai durumdur. Odeme basarisi tek basina rezervasyon
basarisi sayilmaz.

### Cuzdanla Tur Satin Alma

1. Android `sessionId`, `participantCount` ve `Idempotency-Key` gonderir; toplam
   tutari veya bakiye sonucunu gondermez.
2. Backend session ve wallet row'larini lock altinda okur; bookable durum,
   kapasite, aktif rezervasyon ve available balance kontrollerini yapar.
3. Tek transaction icinde `method=WALLET`, `purpose=TOUR_BOOKING`,
   `status=SUCCEEDED` payment; `CONFIRMED` reservation ve payment/reservation
   referansli `TOUR_PURCHASE` ledger debit kaydi olusturulur.
4. Payment, reservation veya ledger adimlarindan biri basarisizsa transaction'in
   tamami rollback olur. Tur, odeme ve bakiye birbirinden kopuk kalamaz.
5. Ayni idempotency key ile tekrar gelen istek ikinci payment, reservation veya
   ledger debit olusturmaz; onceki canonical sonucu dondurur.

Wallet odemesinde iyzico cagrilmaz ve gec callback/seat hold beklenmez. Rehber
kazanci satin alma aninda available olmaz; session tamamlanmasi ve mevcut kazanc
kurallari sonrasinda olusur.

### Cuzdan Odeme Iadesi

- Iptal politikasi iade hakki veriyorsa reservation cancellation, ayni wallet
  payment'ina bagli `SUCCEEDED` refund ve `REFUND` tipinde ledger credit tek
  transaction'da olusur.
- Iade tutari backend tarafindan satin alma snapshot'i ve politika uzerinden
  hesaplanir; asil payment'in kalan iade edilebilir tutarini asamaz.
- Refund veya ledger credit olusamazsa transaction rollback olur. "Iade edildi"
  gorunup bakiyenin artmamasi ya da bakiyenin artip refund history'nin eksik
  kalmasi engellenir.
- Ayni refund idempotency key'i ikinci refund veya ikinci wallet credit
  olusturmaz. Payment `SUCCEEDED` olarak korunur; `REFUNDED` gorunumu refund
  kayitlarindan turetilir.

### Ayni Son Koltuk Yarisi

- Iki kullanici ayni son kontenjani isterse row lock ve aktif hold hesabi
  yalniz birine izin verir.
- Diger kullanici stabil `CAPACITY_NOT_AVAILABLE`/benzeri kod alir.
- Android eski ekrani gosterse bile satin alma endpoint'i son karari verir ve
  localized kullanici mesaji gosterilir.

## Iptal, Iade ve Kazanc Tutarliligi

### Rehber Session Iptali

- Session yeni rezervasyona kapanir ve `CANCELLED` olur.
- Etkilenen rezervasyonlar iptal edilir.
- Her basarili kart odemesi icin full refund request olusturulur.
- Wallet odemesi ledger credit ile iade edilir.
- Pending/available rehber kazanci reverse edilir.
- Turist ve rehber icin notification/history kaydi olusturulur.
- Provider call DB transaction icinde acik tutulmaz. Local `REQUESTED` state,
  provider sonucu ve reconciliation idempotent yurutulur.

### Turist Rezervasyon Iptali

- Backend satin alma snapshot'indaki policy code/version'i uygular.
- Uygun iade miktari backend tarafindan hesaplanir.
- Rezervasyon iptal edilir, kapasite tekrar kullanilabilir olur.
- Iade state'i ayrica izlenir.

### Kesin Iptal ve Iade Politikasi

- Turist, session baslangicina 48 saat veya daha fazla varken iptal ederse tam
  iade alir.
- Session baslangicina 48 saatten az kalmissa turist iptalinde iade yapilmaz.
  Koltuk yine serbest birakilir fakat odeme gecmisi silinmez.
- Rehber veya admin session'i iptal ederse kalan sureden bagimsiz olarak
  etkilenen turistlere tam iade baslatilir.
- Turistin 48 saatten az kala yaptigi iadesiz iptalde rehberin net kazanci,
  session planlanan bitis zamanina ulastiktan ve session rehber tarafindan
  iptal edilmedikten sonra `AVAILABLE` olabilir.
- Turist gec iptal ettikten sonra rehber/admin session'i da iptal ederse tam
  iade kurali uygulanir ve ilgili rehber kazanci ters kayitla duzeltilir.
- Rezervasyon, satin alma anindaki `cancellation_policy_code` ve
  `cancellation_policy_version` degerlerini saklar; sonraki politika
  degisiklikleri eski rezervasyonlari geriye donuk etkilemez.
- Android legal, FAQ, support ve iptal onay metinlerindeki 24 saat ifadesi 48
  saat olarak guncellenir. Backend sonucu gelmeden Android iade basarili
  gostermeyecektir.

## Iyzico Sandbox ve Checkout Form

### Coklu Tahsilat Para Birimi Uygulamasi

Backend dikey dilimi tamamlanmistir. Canonical USD ile provider tahsilat parasi
ayri tutulur; final Android odeme entegrasyonu bu kesinlesmis quote ve checkout
sozlesmesini tuketecektir.

Canonical para ile tahsilat parasi ayridir:

- Tur fiyati, reservation snapshot'i, wallet bakiyesi ve hareketleri, rehber
  kazanci, platform komisyonu, withdrawal ve raporlama daima `USD` kalir.
- `chargeCurrencyCode`, yalniz hosted kart tahsilatinda iyzico'ya gonderilen para
  birimidir. Bu secim yeni EUR/TRY/GBP wallet veya ikinci muhasebe bakiyesi
  olusturmaz.
- Wallet ile tur satin alma tamamen canonical USD ledger icinde kalir; iyzico,
  charge currency veya doviz kuru kullanmaz.
- Wallet top-up'ta kullanici yuklenecek canonical USD tutarini belirler. Backend
  secilen charge currency icin quote uretir; dogrulanmis provider basarisindan
  sonra wallet'a quote'taki USD tutar tam bir kez yazilir.
- Kartla tur aliminda backend session fiyati ve katilimci sayisindan canonical
  USD toplami yeniden hesaplar, sonra charge quote uretir. Android toplam tutar
  veya kur gondererek backend kararini degistiremez.

Para birimi secimi ve quote sozlesmesi:

- Android hosted forma gecmeden once backend'in dondurdugu etkin tahsilat para
  birimleri arasindan kullaniciya secim yaptirir. Liste ekranlara dagitilmis
  hardcoded sabitlerden degil backend config/provider capability sonucundan
  gelir.
- Iyzico Checkout Form dokumanindaki `TRY`, `USD`, `EUR`, `GBP`, `NOK`, `CHF`
  provider adaylaridir. GuideMate local/Sandbox varsayilani config-backed
  `USD`, `TRY`, `EUR`, `GBP` alt kumesidir; Android listeyi backend'den alir.
- Backend kullanici profil ulkesi veya Android'in bolge tercihini yalniz
  varsayilan secim onerisi icin kullanabilir. GPS, IP/VPN veya kart numarasindan
  cikarim tahsilat para biriminin otoritesi olmaz; kullanici secimi degistirebilir.
- Desteklenmeyen yerel para birimleri UI'da isterse yaklasik gosterim icin
  kullanilabilir; fakat checkout yalniz backend'in etkinlestirdigi provider para
  birimlerinden biriyle baslar. Yaklasik gosterim odeme taahhudu sayilmaz.
- Backend `quoteId`, `baseAmountMinor`, `baseCurrencyCode=USD`,
  `chargeAmountMinor`, `chargeCurrencyCode`, `fxRate`, `rateSource`, `quotedAt`
  ve `expiresAt` dondurur. Android bunlari gosterir, yeniden hesaplamaz.
- Quote authenticated kullaniciya, amaca ve canonical islem tutarina baglidir;
  kisa omurludur ve checkout initialize sirasinda yeniden dogrulanir. Expired
  quote otomatik basari sayilmaz; Android yeni quote ister ve degisen tutari
  kullaniciya tekrar gosterir.
- Kur ve yuvarlama tek backend servisinde yonetilir. FX hesabinda `BigDecimal`,
  etkin para biriminin fraction-digit bilgisi ve acik tek bir rounding policy
  kullanilir; `double`, Android kuru veya daginik formatter hesabi kullanilmaz.

Backend uygulama kapsami:

- Tamamlandi: FX saglayicisi dar `ExchangeRateProvider` adapter sinirinin
  arkasindaki Frankfurter ECB referans kurudur. API key ve ucret gerektirmez;
  timeout'lar config ile sinirlidir ve sabit/sahte kur fallback'i yoktur.
- Tamamlandi: `V13__add_multi_currency_payment_quotes.sql`,
  `payment_fx_quotes` tablosunu ve `payments`/`refunds` canonical-charge snapshot
  alanlarini ekler; eski hosted USD kayitlari geriye uyumlu backfill edilir.
- Tamamlandi: `GET /api/v1/payments/checkout/currencies`,
  `POST /api/v1/payments/checkout/tour/quote` ve
  `POST /api/v1/payments/checkout/wallet-top-up/quote` sozlesmeleri eklenmistir.
  Hosted initialize endpoint'leri yalniz gecerli `quoteId` kabul eder.
- Tamamlandi: initialize provider'a quote'taki `chargeAmountMinor/chargeCurrencyCode`
  degerlerini yollar. Retrieve, callback, webhook ve reconciliation provider
  payment kimliginin yaninda tahsil edilen tutar ile para birimini de payment
  snapshot'ina karsi dogrular.
- Tamamlandi: idempotency ayni key ile farkli quote, canonical tutar, charge tutari veya para
  birimi kullanilmasini `IDEMPOTENCY_CONFLICT` olarak reddeder.
- Tamamlandi: mevcut urun politikasindaki tam provider iadesi orijinal tahsilat
  para birimiyle ve payment'ta saklanan kalan charge snapshot'ina gore yapilir;
  internal ledger reversal canonical USD kalir. Urunde partial refund davranisi
  olmadigi icin kullanilmayan partial endpoint/politika eklenmez; ileride urun
  kapsamina girerse canonical-charge dagitimi ve cumulative cap birlikte yazilir.
- Tamamlandi: OpenAPI, DTO, domain, repository, service, provider adapter,
  reconciliation, refund, config ve error mapping birlikte guncellenir. Bu
  geciste tamamen bosa dusen USD-only property, validator, helper ve mock para
  birimi kodlari davranis korunarak silinir.

Android uygulama kapsami:

- Wallet top-up ve kartla tur checkout onayinda, hosted iyzico ekrani acilmadan
  once backend destekli para birimi secimi ve quote ozeti eklenir. Wallet ile tur
  satin almada bu secim gosterilmez.
- Android request'i kur veya hesaplanmis charge tutari gondermez; quote isteginde
  secilen `chargeCurrencyCode`, initialize isteginde yalniz `quoteId` ve mevcut
  idempotency bilgisini gonderir.
- Ekran canonical USD tutarini, tam tahsilat tutari/para birimini, kuru ve quote
  suresini backend response'undan gosterir. Quote suresi dolarsa onay aksiyonu
  durur ve yeni quote alinmadan hosted form acilmaz.
- Sabit `$`/USD-only tahsilat metinleri, ekranlara dagilmis formatter'lar ve mock
  doviz hesaplari taranir; canonical USD anlaminda gerekli olanlar korunur,
  tahsilat secimini engelleyen veya bosa dusenler kaldirilir/guncellenir.
- `PAYMENT_CURRENCY_NOT_SUPPORTED`, `FX_QUOTE_UNAVAILABLE` ve
  `FX_QUOTE_EXPIRED` durumlari mevcut GuideMate hata sunum modeline uygun
  localized metinlerle gorunur olur. `PAYMENT_CURRENCY_CARD_NOT_SUPPORTED`
  yalniz provider guvenilir ve ayirt edilebilir bir hata kodu verirse kullanilir;
  Android veya backend kart ulkesi tahmini yapmaz. Provider raw mesaji dogrudan
  gosterilmez.
- Android hosted donusunu yine basari kabul etmez; payment status'u backend'den
  izler. Canonical wallet/rezervasyon state'i yalniz retrieve/webhook ile
  dogrulanmis backend sonucundan guncellenir.

Dogrulama kapsami:

- Her etkin charge currency icin quote, initialize, retrieve ve basarili Sandbox
  akisi; yerel kart/para birimi uyumsuzlugu ve provider decline senaryolari
- Quote expiry, kur degisimi, retry, ayni/different idempotency key ve concurrent
  initialize senaryolari
- Callback, webhook ve reconciliation'da amount/currency mismatch korumasi
- Wallet top-up'ta tam bir canonical USD credit; kartla tur aliminda dogru
  reservation/payment sonucu
- Tam ve partial iadede orijinal charge currency, merkezi yuvarlama, cumulative
  cap ve duplicate iade korumasi
- Android process death/retry sonrasinda quote/payment state recovery ve eski
  mock/USD-only tahsilat otoritesinin kalmadiginin kontrolu

### Guvenlik Siniri

- Iyzico API key ve secret yalniz backend environment/ignore edilen local
  secret config'te bulunur.
- Android hicbir zaman iyzico secret'i, provider card tokeni, tam kart numarasi
  veya CVV almaz.
- Kart bilgisi iyzico hosted Checkout Form/3DS ekraninda girilir.
- Backend yalniz internal payment id, provider token/id, maskeli kart metadata,
  signature sonucu ve canonical payment state saklar.
- Android-backend, backend-iyzico ve callback/webhook hatlari HTTPS kullanir.

### REST/Provider Akisi

Backend Android'e kendi REST endpoint'lerini sunar:

- Ertelenen coklu tahsilat diliminde
  `GET /api/v1/payments/checkout/currencies`
- Ertelenen coklu tahsilat diliminde
  `POST /api/v1/payments/checkout/tour/quote`
- Ertelenen coklu tahsilat diliminde
  `POST /api/v1/payments/checkout/wallet-top-up/quote`
- `POST /api/v1/payments/checkout/tour`
- `POST /api/v1/payments/checkout/wallet-top-up`
- `GET /api/v1/payments/{paymentId}`
- `POST /api/v1/payments/{paymentId}/cancel` uygun state'te
- `POST /api/v1/payments/iyzico/callback` public ama token/signature kontrollu
- `POST /api/v1/payments/iyzico/webhook` public ama signature kontrollu
- `GET /api/v1/payment-methods/cards`
- `DELETE /api/v1/payment-methods/cards/{savedPaymentMethodId}`
- `PUT /api/v1/payment-methods/cards/{savedPaymentMethodId}/default`

Backend iyzico tarafinda su adimlari uygular:

1. Checkout Form initialize
2. Android'i `paymentPageUrl`/hosted forma yonlendirme
3. Callback tokenini alma
4. Checkout Form retrieve ile sonucu resmi olarak dogrulama
5. Webhook imzasini dogrulama
6. Delayed callback/webhook durumunda reconciliation

Proje sonu Android/backend akisi anlatilirken webhook su sade cumleyle de
aciklanir: `Kullanicinin telefonu sonucu iletemese bile iyzico odeme sonucunu
dogrudan backend'e bildirir.` Bu anlatim imza, retrieve ve idempotency teknik
kontrollerinin yerine gecmez; webhook'un kullanici acisindan neden gerekli
oldugunu aciklar.

Iyzico dokumani:

- `https://docs.iyzico.com/en/payment-methods/checkoutform/cf-implementation`
- `https://docs.iyzico.com/en/advanced/response-signature-validation`
- `https://docs.iyzico.com/en/advanced/webhook`

Iyzico callback URL gecerli SSL sertifikali public URL istemektedir. Android'in
LAN API adresi ayridir. Local sandbox testi icin:

- `ANDROID_API_BASE_URL`: Mac LAN IP'si olabilir.
- `PAYMENT_CALLBACK_BASE_URL`: Cloudflare Tunnel/ngrok benzeri gecici public
  HTTPS tunnel olmalidir.
- AWS veya kalici ucretli hosting zorunlu degildir.
- Android emulatoru ve ayni Wi-Fi'daki fiziksel cihazlar odeme akisini ayni
  backend uzerinden test edebilir. Bu cihazlar normal REST cagrilarinda
  `ANDROID_API_BASE_URL` kullanir; Cloudflare Quick Tunnel yalniz iyzico'nun
  callback/webhook endpoint'lerine disaridan erisebilmesi icindir ve cihaza ozel
  degildir.
- Quick Tunnel simdi acik tutulmaz. Payment initialize, callback/webhook,
  retrieve ve status endpoint'leri tamamlandiktan sonra Iyzico Sandbox
  entegrasyon ve end-to-end test fazinda calistirilir.
- Quick Tunnel'in rastgele public URL'si her calistirmada degisebilecegi icin
  kaynak koda yazilmaz. Yeni URL `PAYMENT_CALLBACK_BASE_URL` environment
  variable'ina verilir ve backend yeniden baslatilir; Android kodunda veya test
  cihazinda degisiklik yapilmaz.
- Sandbox test oturumu boyunca Spring Boot, PostgreSQL ve tunnel sureci acik
  kalmalidir. Tunnel kapaliysa callback/webhook ulasamaz; belirsiz odemeler
  backend reconciliation ve retrieve mekanizmasiyla tekrar dogrulanir.

### Kart Kaydetme Karari

Kesin entegrasyon yontemi iyzico hosted Checkout Form'dur. Iyzico Card Storage
ve direct payment API'leri ham kart bilgisi kabul edebilse bile GuideMate,
standalone native kart formundan tam kart numarasi, SKT veya CVV alip kendi
backend'ine gondermeyecektir. Ham kart verisini alan veya ileten bir GuideMate
endpoint'i yazilmayacaktir.

- Iyzico destek cevabi, Card Storage modulunde odeme sonrasinda saklanan kart
  icin response'ta `cardUserKey` ve `cardToken` dondugunu ve bu kartla sonraki
  odemenin tetiklenebildigini yazili olarak dogrulamistir.
- Iyzico Card Storage API/SDK listeleme ve silme islemlerini saglar. GuideMate
  backend'i bu islemleri dar provider gateway sinirindan kullanir; Android
  iyzico tokenlarini hicbir endpoint'ten almaz.
- `cardUserKey` ve `cardToken` backend'de `SensitiveDataCipher` ile sifreli,
  arama/tekillik icin geri dondurulemez HMAC fingerprint ile saklanir. API
  response'u yalniz internal `savedPaymentMethodId`, son dort hane ve guvenli
  maskeli metadata tasir.
- Kayitli provider customer key'i sonraki Checkout Form initialize istegine
  eklenir. Kayitli kartin gercek secimi ve yeni kart girisi yine iyzico hosted
  ekraninda kalir; GuideMate ham kart numarasi, SKT veya CVV islemez.
- Backend'de `V8` migration'i, entity/repository/service/provider adapter'i ve
  listeleme, silme, varsayilan kart endpointleri uygulanmistir. Standalone ham
  kart alan bir `Kart Ekle` endpoint'i yoktur ve eklenmeyecektir.
- Ilk karti kaydeden Sandbox Checkout Form odemesi, maskeli listeleme, sonraki
  checkout'ta kayitli kartla odeme, varsayilan kart, provider-backed silme,
  duplicate callback idempotency ve gercek imzali webhook E2E senaryolari
  basariyla dogrulanmistir. Local PostgreSQL semasi `V8`'e gecmis ve Hibernate
  validate tamamlanmistir.

#### Android Kart Ekleme Tasarimi ve Gecis Kurali

- Mevcut GuideMate `Kart Ekle` ekraninin app bar'i, sayfa yerlesimi, tipografisi,
  brand renkleri, bosluk sistemi ve genel goruntu dili korunur.
- MVP icin bulunan kart numarasi, kart sahibi, SKT ve CVV alanlari gercek iyzico
  entegrasyonunda kaldirilir; bu alanlar GuideMate tarafinda aktif bir odeme
  formu olarak kalmaz.
- Ekran, kullaniciyi guvenli provider akisina hazirlayan kisa ve yerellestirilmis
  aciklama ile uygulamanin mevcut buton stilini kullanan `Guvenli kart ekranina
  devam et` aksiyonunu gosterir.
- Bu aksiyon Android'den ham kart bilgisi gondermez. Backend'de checkout
  initialize islemini baslatir, internal `paymentId` ve hosted URL alir, ardindan
  iyzico ekranini acarak kart girisini tamamen provider sinirinda tutar.
- Provider donusunde Android dogrudan basari kabul etmez; backend retrieve/webhook
  dogrulamasi tamamlanana kadar loading/verifying durumunu gosterir. Basari,
  iptal, timeout ve hata durumlari mevcut GuideMate dialog/metin/component
  diliyle, yerellestirilmis ve kullanici dostu bicimde sunulur.
- Kayitli kart listesi ve kart gorselleri provider-backed veriyle korunur.
  Android mock listeyi backend'in internal `savedPaymentMethodId` ve maskeli
  metadata response'uyla degistirir; silme ve varsayilan kart aksiyonlarini
  backend endpointlerine baglar. Odeme sirasindaki gercek kart secimi iyzico
  hosted ekraninda kalir.
- Bu donusum mevcut ekranlarin tasarimini keyfi bicimde yeniden tasarlama nedeni
  degildir. Yeni buton, durum ve mesajlar var olan GuideMate componentlerini ve
  tasarim olculerini izlemelidir.

### Checkout Dili ve Tasarim Sahipligi

- GuideMate'e ait odeme oncesi ve odeme sonucu ekranlari mevcut Android tasarim
  diliyle tasarlanir; tum metinler Android resource'larindan secili uygulama
  diline gore dinamik gelir, hardcoded metin kullanilmaz.
- Iyzico Checkout Form dili statik tutulmaz. Android'in secili uygulama diline
  gore backend iyzico initialize istegine yalniz `tr` veya `en` locale degerini
  dinamik gonderir; mevcut hardcoded `Locale.EN` davranisi Android odeme
  entegrasyonunda kaldirilir.

### Marketplace ve Para Cekme

- Platform komisyonu kesin olarak yuzde 10'dur. Oran backend'in guvenilir
  configuration degerinden gelir; Android request'inden alinmaz.
- Turistin tur kartinda ve checkout'ta gordugu fiyat nihai toplam fiyattir;
  checkout sirasinda ayrica hizmet bedeli eklenmez.
- Ornegin toplam tur bedeli 100 USD ise `gross_minor=10000`,
  `platform_fee_minor=1000` ve `net_minor=9000` olur.
- Rehber kazanc detayi brut tutar, platform komisyonu ve net kazanci ayri
  gosterebilir.
- Tam iadede hem platform komisyonu hem rehber kazanci ters kayitlarla
  duzeltilir; mevcut ledger/payment/earning kayitlari fiziksel olarak silinmez.
- Iyzico Marketplace/submerchant urunu sandbox hesapta erisilebilirse rehber
  provider submerchant kimligi ve paylasim akisi eklenir.
- Marketplace gercek seller bilgileri/onboarding gerektiriyorsa sahte KYC ile
  provider kullanmis gibi davranilmaz.
- Portfolio kapsaminda tahsilat ve iade iyzico Sandbox ile gercek test akisi
  olur; provider payout erisimi yoksa rehber withdrawal state machine backend
  icinde auditable sekilde simule edilir.

#### Sandbox Urun Yetkisi ve Kapsam Ayrimi

- Iyzico Sandbox'in resmi olarak destekledigi hicbir odeme adimi GuideMate
  tarafinda yeniden veya gereksiz yere simule edilmez. Checkout, 3DS, tahsilat,
  retrieve, callback/webhook, iade, kart saklama, Marketplace ya da payout
  islemlerinden hangisi mevcut merchant yetkileriyle kullanilabiliyorsa o adim
  dogrudan provider'in gercek Sandbox akisi ve test verileriyle calistirilir.
  Yalniz provider'in Sandbox'ta desteklemedigi veya hesaba acmadigi bir portfolio
  akisi, iyzico islemiymis gibi gosterilmeden acikca `SIMULATED` modunda ve audit
  kaydiyla yurutulebilir.
- Sandbox ortaminda Checkout Form, Marketplace ve Mass Payout ayni urun/yetki
  degildir. Standart Sandbox API key/secret sahibi olmak Marketplace veya Mass
  Payout yetkisinin otomatik acik oldugu anlamina gelmez.
- Entegrasyondan once iyzico desteginden mevcut Sandbox merchant icin
  Marketplace/Submerchant ve Mass Payout test yetkisi ile kullanilmasi gereken
  resmi test onboarding verileri sorulur. Panelde menu gorunmemesi tek basina
  urunun Sandbox'ta bulunmadigi anlamina gelmez.
- Yalniz iyzico'nun izin verdigi resmi Sandbox test kimligi/IBAN verileri
  kullanilir; rastgele veya gercek disi KYC bilgileriyle submerchant acilmaz.
- Marketplace istegi yetki hatasi verirse bu sonuc destekle dogrulanir ve
  provider entegrasyonu varmis gibi taklit edilmez.
- Payout modu local/sandbox configuration'da acikca `IYZICO` veya `SIMULATED`
  olarak secilir. Iyzico modu yetkisizken sessizce simulasyona dusmez; fail-fast
  veya acik bir provider-unavailable sonucu verir.

Urunlerin GuideMate akislarindaki gorevi:

- Turist cuzdana para yukleme: Standart iyzico Sandbox Checkout Form yeterlidir;
  Marketplace veya Mass Payout gerekmez. Retrieve/webhook ile basari kesin
  dogrulandiktan sonra backend internal wallet ledger'ini atomik krediler.
- Tur satin alma: Kart tahsilati icin Checkout Form yeterlidir. Marketplace
  varsa ayni odemede rehber/platform paylasimi provider tarafinda yapilabilir;
  yoksa rehber kazanci GuideMate backend ledger'inda izlenir.
- Iade: Asil kart odemesinin iyzico refund akisi kullanilir; Marketplace varsa
  ilgili split/kazanc ters kayitlari da uzlastirilir.
- Rehber para cekme: Gercek banka transferi icin etkin Marketplace settlement
  veya Mass Payout urunu gerekir. Yetki yoksa para transferi yapilmaz;
  `PENDING -> PROCESSING -> COMPLETED/FAILED` akisi backend'de acikca simule
  edilir ve audit kaydi tutulur.
- Turistin internal wallet bakiyesi ile rehberin banka hesabina payout ayni sey
  degildir; para yuklemenin calismasi Marketplace yetkisine baglanmaz.
- Bu portfolio Sandbox karari canli ortamda elektronik para/cuzdan urunu icin
  gereken hukuki, sozlesmesel ve provider uygunluk incelemesinin tamamlandigi
  anlamina gelmez.

## Banka Hesabi ve IBAN

- Android banka hesabi eklerken normalize IBAN'i backend'e bir kez gonderir.
- Android `TurkishIbanValidator` ve `TurkishBankCatalog` ile MOD97/banka kodu
  kontrolunu yalniz yazim sirasinda hizli geri bildirim ve banka adi on
  gosterimi icin yapar. Bu katalog backend veya para islemi otoritesi degildir.
- Backend format, uzunluk, MOD97, banka kodu ve rehber sahipligini yeniden
  dogrular; banka kodu/adi eslesmesini guncel ve guvenilir katalogdan kendisi
  belirler.
- Backend internal `bankAccountId`, banka adi ve maskeli IBAN dondurur.
- Banka hesabi kaydedildikten sonra Android yerel katalogdan yeniden tahmin
  yapmak yerine backend'in dondurdugu banka adini ve maskeli IBAN'i gosterir.
- Android on gosterimi ile backend sonucu uyusmazsa backend sonucu kazanir ve UI
  canonical response ile yenilenir.
- `TurkishBankCatalog` Android'de kalir; banka listesi icin ayri remote endpoint
  veya senkronizasyon katmani bu portfolio kapsaminda gereksizdir. Katalog
  resmi TCMB kodlari temel alinarak bakimli tutulur.
- Bu IBAN katalog karari kart metadata kararindan ayridir. Iyzico Checkout Form
  IBAN bankasi tespiti yerine gecmez. Kart icin `SandboxCardCatalog` kaldirilir
  ve banka/kart bilgisi provider-backed backend metadata'sindan gelir.
- Para cekme request'i tam IBAN degil `bankAccountId`, `amountMinor` ve
  idempotency key tasir.
- Tam IBAN sifreli tutulur; log/response'a girmez.
- Gercek KYC, kimlik-belge dogrulamasi veya banka hesap sahibini dis servisle
  kanitlama portfolio MVP kapsaminda degildir.

## Bildirim ve FCM

- PostgreSQL notification history ve unread state'in otoritesidir.
- FCM yalniz delivery channel'dir; push kacsa bile uygulama REST sync ile
  notification history'yi gorur.
- Domain islemi ayni transaction'da notification row olusturur.
- FCM gonderimi transaction commit sonrasinda yapilir.
- Ilk surumde Kafka/outbox yoktur. `PENDING/FAILED` push kayitlari scheduler ile
  sinirli tekrar denenebilir.
- Android 13+ notification permission ve channel kurulumu Android entegrasyon
  fazinda yapilir.
- WorkManager push almak icin kullanilmaz; retry/sync icindir.
- Exact local reminder gerekirse AlarmManager, server kaynakli tur hatirlatmasi
  icin backend scheduler + FCM kullanilir.
- Notification click payload'i route adi degil semantic type + record ids
  tasir; Android mapper gercek type-safe route'u olusturur.
- Paneli acmak tum notification'lari okundu yapmaz. Tek notification tiklamasi
  yalniz onu okur; ayrica `mark-all-read` endpoint'i bulunabilir.

Onerilen REST endpoint'leri:

- `GET /api/v1/notifications`
- `GET /api/v1/notifications/unread-count`
- `POST /api/v1/notifications/{id}/read`
- `POST /api/v1/notifications/read-all`
- `GET/PATCH /api/v1/notifications/preferences`
- `POST /api/v1/devices/fcm-registration`
- `DELETE /api/v1/devices/fcm-registration/{installationId}`

`GET /api/v1/notifications/preferences` su alanlari tek ve stabil sozlesmeyle
dondurur: `upcomingTourRemindersEnabled`, `chatMessagesEnabled`,
`reservationUpdatesEnabled`, `reviewRequestsEnabled`,
`paymentsAndEarningsEnabled`, `newReviewsEnabled` ve read-only
`securityAlertsEnabled=true`. PATCH yalniz ilk alti alani kabul eder; kullanici
rolu icin kullanilmayan alanlar Android tarafinda gosterilmez. Degisiklikler
partial update olarak uygulanir, response her zaman kaydedilmis canonical
tercihleri dondurur.

## Anlik Mesajlasma

### REST

- `POST /api/v1/chats/with-user/{remoteUserId}`: bul veya olustur
- `GET /api/v1/chats`: conversation listesi ve unread bilgisi
- `GET /api/v1/chats/{chatId}/messages`: cursor/pagination ile history
- `POST /api/v1/chats/{chatId}/messages`: REST send/fallback
- `POST /api/v1/chats/{chatId}/read`: read state guncelle
- `GET /api/v1/chats/unread-count`

Mesaj gonderme request'i bos/yalniz whitespace govdeyi reddeder ve en fazla
2.000 karakter kabul eder. REST controller bu kurali kendi basina tekrar
yazmaz; WebSocket ile ortak mesaj gonderme application service'ini kullanir.

### WebSocket/STOMP

- Spring WebSocket/STOMP kullanilir.
- Handshake endpoint'i `/ws`; CONNECT native `Authorization: Bearer <JWT>`
  header'i ile dogrulanir.
- Mesaj gonderimi `/app/chats/{chatId}/messages` destination'ina yapilir.
- Client yalniz `/user/queue/chat-messages` ve
  `/user/queue/chat-errors` private destination'larina subscribe olabilir;
  public conversation topic'i yoktur.
- Backend mesaji once PostgreSQL'e kaydeder, sonra aktif aliciya publish eder.
- `clientMessageId` duplicate send'i engeller.
- STOMP send payload'i da REST ile ayni bosluk ve 2.000 karakter sinirindan
  gecmeden kaydedilmez veya publish edilmez.
- Baglanti kopup acilinca Android REST history ile kacirilan mesajlari tamamlar.
- Uygulama kapaliysa semantic `chatId` payload'li FCM gonderilir.
- WorkManager WebSocket yerine gecmez; yalniz failed send/sync retry icin
  kullanilabilir.

Android'deki sabit viewer role/current user mock'u entegrasyonda kaldirilir;
gercek `UserState.userId` ve `role` kullanilir.

## Google Places ve Saat Dilimi

- Android ulke icin stabil country code, sehir icin Google `placeId` + display
  name ve yayin anindaki `ZoneId.systemDefault().id` degerini gonderir.
- Backend bu portfolio/LAN surumunde Google Places Details veya Time Zone API
  cagrisi yapmaz; ayri Google server key ya da `LocationResolver` eklenmez.
- Backend country code, zorunlu alanlar ve `timeZoneId` degerinin Java
  `ZoneId` tarafindan taninan gecerli bir IANA kimligi olmasini dogrular.
- `placeId`, localized display name ve dogrulanmis `timeZoneId` `tours`
  tablosunda saklanir. Koordinat saklanmaz.
- Tur/session zamanlari kaydedilen `timeZoneId` ile `Instant` degerine
  donusturulur; response'ta backend'in sakladigi canonical deger kullanilir.
- Server-side place/country dogrulamasi ve konumdan saat dilimi cozme, uygulama
  gercek kullanima acilirsa eklenecek bilincli production hardening adimidir.

## Medya Akisi

1. Android kamera/galeriden local URI alir.
2. Android multipart upload endpoint'ine dosyayi gonderir.
3. Backend MIME, boyut ve sahipligi dogrular.
4. `MediaStorage` dosyayi yazar; `media_assets` `READY` olur.
5. Backend `mediaAssetId` ve kontrollu binary GET endpoint'ini hedefleyen mutlak
   erisilebilir URL dondurur.
6. Tour/profile update request'i local URI degil bu asset ID'yi gonderir.
7. Upload basarisizsa publish/edit devam etmez.
8. Android gecici kamera dosyasini success/cancel/failure sonrasinda temizler.
9. Baska cihazlar public/authorized `imageUrl` uzerinden ayni `READY` dosyayi
   gorur; sahipsiz upload'lar grace period sonrasinda backend tarafindan
   temizlenir.

## REST Endpoint Gruplari

### Profil ve Medya

- `GET/PATCH /api/v1/guides/me/profile`
- `GET /api/v1/guides/{guideId}/public-profile`
- `POST /api/v1/media`
- `GET /api/v1/media/{mediaId}/content`: `READY` asset binary response; public
  projection veya owner yetkisi dogrulanir
- `DELETE /api/v1/media/{mediaId}` yalniz iliskisiz/uygun asset

### Rehber Dashboard ve Tur Yonetimi

- `GET /api/v1/guides/me/dashboard`
- `POST /api/v1/guide/tours`: create + review submit
- `GET /api/v1/guide/tours?tab=ACTIVE|REVIEW|PAST`
- `GET /api/v1/guide/tours/{tourId}`
- `POST /api/v1/guide/tours/{tourId}/change-requests`
- `POST /api/v1/guide/tours/{tourId}/sessions`
- `PATCH /api/v1/guide/sessions/{sessionId}`
- `POST /api/v1/guide/sessions/{sessionId}/open`
- `POST /api/v1/guide/sessions/{sessionId}/close`
- `POST /api/v1/guide/sessions/{sessionId}/cancel`
- `POST /api/v1/guide/tours/{tourId}/archive`

### Admin

- `GET /api/v1/admin/tour-reviews`
- `GET /api/v1/admin/tour-reviews/{id}`
- `POST /api/v1/admin/tour-reviews/{id}/approve`
- `POST /api/v1/admin/tour-reviews/{id}/reject`

### Turist Kesif ve Detay

- `GET /api/v1/tours/search?q=...&countryCode=...&cityPlaceId=...&categoryCode=...&languageCodes=...&minRating=...&minPriceMinor=...&maxPriceMinor=...&page=...&size=...&sort=...`
- `GET /api/v1/tours/popular`
- `GET /api/v1/tours/{tourId}`
- `GET /api/v1/tour-sessions/{sessionId}`
- `GET /api/v1/guides/search?q=...&page=...&size=...&sort=...`
- `GET /api/v1/guides/top`

### Rezervasyon ve Yorum

- `GET /api/v1/reservations/me?status=UPCOMING|PAST`
- `GET /api/v1/reservations/{reservationId}`
- `POST /api/v1/reservations/{reservationId}/cancel`
- `POST /api/v1/reservations/{reservationId}/reviews`
- `GET /api/v1/tours/{tourId}/reviews`

### Cuzdan ve Finans

- `GET /api/v1/wallet`
- `GET /api/v1/wallet/transactions`
- `GET /api/v1/guide/earnings?year=...`
- `GET /api/v1/guide/earnings/monthly?year=...`
- `GET/POST/DELETE /api/v1/guide/bank-accounts`
- `POST /api/v1/guide/bank-accounts/{id}/default`
- `GET/POST /api/v1/guide/withdrawals`
- Provider destekliyorsa `GET/DELETE/default` saved payment method endpoint'leri

Tum listeler pagination kullanir. Chat history cursor, standart listeler ilk
surumde `page`, `size`, `sort` kullanabilir.

## API Sozlesmesi Kurallari

- Owned mutation request'leri `userId`/`guideId` kabul etmez.
- Canonical domain para response/request'i `amountMinor`,
  `currencyCode=USD` kullanir. Coklu tahsilat payment quote/provider
  alanlari ayrica `chargeAmountMinor`, `chargeCurrencyCode` ve gerekli FX
  snapshot alanlarini tasir; canonical ile charge alanlari ayni isimle
  birbirinin yerine kullanilmaz.
- Zaman: ISO-8601 UTC timestamp + gerekli yerde `timeZoneId`.
- Medya: absolute URL + internal asset id.
- Hata: mevcut `ErrorResponse.code`, guvenli fallback `message`, `fieldErrors`.
- Android kontrol akisinda hata mesni degil stabil `code` kullanir.
- Mutation canonical yeni state'i dondurur; UI optimistic degisiklik basarisizsa
  bu state'e geri doner ve localized one-shot mesaj gosterir.
- Stale update icin request version/If-Match benzeri contract kullanilir.
- Payment, booking, cancellation, refund, withdrawal icin `Idempotency-Key`
  gerekir.
- Chat duplicate korumasi `clientMessageId` ile yapilir.
- OpenAPI/Swagger sozlesmesi Android Retrofit DTO'lari yazilmadan once
  dondurulur.

## Stabil Hata Kodlari

Mevcut auth hata sistemine yeni domain kodlari eklenir. En az:

- `RESOURCE_NOT_FOUND`
- `FORBIDDEN_RESOURCE`
- `TOUR_NOT_APPROVED`
- `TOUR_CHANGE_PENDING`
- `SESSION_NOT_BOOKABLE`
- `SESSION_ALREADY_STARTED`
- `SESSION_HAS_RESERVATIONS`
- `CAPACITY_NOT_AVAILABLE`
- `CAPACITY_BELOW_BOOKED_COUNT`
- `CONCURRENT_UPDATE`
- `RESERVATION_ALREADY_EXISTS`
- `RESERVATION_NOT_CANCELLABLE`
- `INVALID_AMOUNT`
- `INVALID_PARTICIPANT_COUNT`
- `REVIEW_NOT_ALLOWED`
- `REVIEW_ALREADY_EXISTS`
- `PAYMENT_INITIALIZATION_FAILED`
- `PAYMENT_VERIFICATION_FAILED`
- `CARD_INSUFFICIENT_FUNDS`
- `PAYMENT_METHOD_DECLINED`
- `PAYMENT_CURRENCY_NOT_SUPPORTED`
- `PAYMENT_CURRENCY_CARD_NOT_SUPPORTED`
- `FX_QUOTE_UNAVAILABLE`
- `FX_QUOTE_EXPIRED`
- `REFUND_FAILED`
- `REFUND_AMOUNT_EXCEEDED`
- `INSUFFICIENT_WALLET_BALANCE`
- `INSUFFICIENT_WITHDRAWABLE_BALANCE`
- `BANK_ACCOUNT_INVALID`
- `IDEMPOTENCY_CONFLICT`
- `CHAT_ACCESS_DENIED`
- `MESSAGE_DUPLICATE`
- `MESSAGE_TOO_LONG`

Provider hata metni dogrudan Android'e dondurulmez; uygun internal code'a
map edilir.

## Transaction ve Concurrency Kurallari

- Son koltuk/seat hold icin session row pessimistic lock kullanilir.
- Wallet debit, withdrawal reservation ve ledger write ayni transaction'da
  olur.
- Wallet tur satin aliminda `SUCCEEDED` payment, `CONFIRMED` reservation ve
  `TOUR_PURCHASE` ledger debit; uygun wallet iadesinde `SUCCEEDED` refund ve
  `REFUND` ledger credit ayni transaction sinirinda olusur.
- Iyzico/FCM gibi network call sirasinda DB transaction acik tutulmaz.
- External call oncesi local intent/state commit edilir; sonuc idempotent ikinci
  transaction ile uygulanir.
- Callback ve webhook duplicate gelebilir; ayni provider/event/idempotency
  kimligi ikinci mutation olusturmaz.
- Hold scheduler'i ile gec callback ayni reservation/session uzerinde
  yaristiginda lock altindaki guncel kapasite ve durum son karari verir. Gec
  verified success kapasiteyi asamaz; rezervasyon kesinlesemiyorsa idempotent
  tam iade akisi baslatilir.
- Tour/session edit stale version ile gelirse `409 CONCURRENT_UPDATE` olur.
- Notification row business transaction'da olusur; push commit sonrasidir.
- Reconciliation yarim kalan provider/local state'i tekrar dogrular.

## Scheduler Isleri

Spring scheduler ilk portfolio surumu icin yeterlidir:

- Suresi dolan `PENDING_PAYMENT` rezervasyonlarini `EXPIRED` yapma
- Suresi biten session'lari `COMPLETED` yapma
- Uygun guide earnings'i `AVAILABLE` yapma
- `VERIFYING`/belirsiz payment'lari iyzico retrieve ile reconcile etme
- Gec verified success icin reservation/kapasite finalization veya idempotent
  tam iade akisini reconcile etme
- `PROCESSING` refund/withdrawal durumlarini reconcile etme
- Grace period'i dolmus ve hicbir domain kaydina bagli olmayan medya
  asset'lerini sahiplik/referans durumunu yeniden kontrol ederek temizleme
- Yaklasan tur hatirlatma notification + FCM
- Inactive/expired device token temizligi

Quartz, queue veya dagitik scheduler ilk surumde eklenmez. Birden fazla backend
instance calismaya baslarsa scheduler locking daha sonra degerlendirilir.

## Backend Paket Yapisi

Feature-first paketleme:

```text
com.ahmetkaragunlu.guidematebackend
|-- auth
|-- user
|-- profile
|-- media
|-- tour
|-- reservation
|-- review
|-- payment
|-- wallet
|-- chat
|-- notification
`-- common
```

Gercek ihtiyaci olan feature icinde:

```text
controller/
dto/
domain/
repository/
service/
mapper/
```

Kurallar:

- Tek siniflik bos alt paket acilmaz.
- JPA entity controller'a sizmaz.
- Feature service baska feature'in tablosunu kontrolsuz repository erisimiyle
  degistirmez; acik service/application siniri kullanir.
- `PaymentProvider`, `MediaStorage` ve `PushNotificationSender` external adapter
  interface'leridir.
- `common/domain/UuidAuditedEntity` yalniz yeni mutable UUID entity'lerin ortak
  kimlik ve `Instant` audit alanlarini tasir; domain lifecycle davranisi veya
  feature'a ozel alanlar bu sinifa yerlestirilmez.
- Business olmayan iki satirlik use-case siniflari eklenmez.

## Calisma Sirasi ve Faz Kapilari

Backend, once tum tablolari sonra tum endpoint'leri yazma seklinde yatay
ilerlemez. Asagidaki fazlar bagimlilik sirasiyla uygulanir ve her feature kendi
icinde migration'dan calisan API sozlesmesine kadar dikey tamamlanir. Sonraki
faza, mevcut fazin veri butunlugu, yetki ve sozlesme kontrolleri bitmeden
gecilmez.

Her fazin kendi icindeki uygulama sirasi:

1. Dis bagimlilik ve karar kapisi kontrolu yapilir; engelleyici gereksinimler
   kullaniciya kod yazmadan once bildirilir.
2. Fazdaki kullanim senaryolari, ownership siniri, request/response sozlesmesi,
   state gecisleri ve stabil hata kodlari netlestirilir.
3. Ilgili Flyway migration'i; tablo, foreign key, constraint ve index'lerle
   yazilir. Uygulanmis migration degistirilmez.
4. Entity, enum ve repository sorgulari eklenir.
5. Service/application katmaninda is kurallari, transaction, concurrency ve
   idempotency uygulanir.
6. Request/response DTO, mapper ve controller endpoint'leri eklenir; JPA entity
   API'ye sizmaz.
7. Authentication, role, ownership ve hata esleme kontrolleri tamamlanir.
8. Unit, controller/security ve gerekli PostgreSQL integration testleri
   calistirilir; OpenAPI sozlesmesi ve Android'in ihtiyac duydugu canonical
   response alanlari dogrulanir.
9. Faz sonunda kullanilmayan kod/paket, hassas log, eksik config ve unutulmus
   dis bagimlilik kontrolu yapilir; faz tamamlanmadan sonraki faza gecilmez.

Bu sira mekanik bir katman zorunlulugu degildir. Ayni feature icinde geri
bildirimle kucuk duzeltmeler yapilabilir; ancak migration, domain kurali, API ve
test birbirinden kopuk ayri teslimler olarak birakilmaz. Ortak yapi yalniz gercek
tekrar veya acik bir adapter siniri varsa cikartilir.

### Dis Bagimlilik ve Kullaniciya Bildirim Kontrolu

Her faz baslamadan ve dis entegrasyon kodu yazilmadan once asagidakiler kontrol
edilip kullaniciya kisa ve acik bir on kosul raporu verilir:

- Gerekli harici hesap, urun yetkisi veya panel ayari
- API key, secret, service account, client ID gibi credential ihtiyaci
- Environment variable veya Git'e girmeyen local secret/config property'leri
- Callback/webhook icin public URL, HTTPS veya ag erisimi gereksinimi
- Maven/Gradle kutuphanesi ve surum uyumlulugu
- Docker, PostgreSQL, Testcontainers veya gerekli local runtime/arac
- Ucret, production etkisi, veri silme ya da geri dondurulemez islem riski

Kod ve konfigurasyon iskeleti, build bagimliliklari, adapter'lar ve dogrulama
Codex tarafindan yapilir. Kullanici yalniz kendi harici hesabina/paneline erisim,
credential'i guvenli alana girme veya riskli/geri dondurulemez isleme onay verme
gerektiginde devreye alinir. Kullaniciya yaptirilabilecek bir kurulum Codex
tarafindan guvenle yapilabiliyorsa kullaniciya birakilmaz.

Engelleyici gereksinim varsa varsayilan veya sahte credential ile gercek
entegrasyon tamamlanmis gibi gosterilmez. Kullaniciya neyin, neden ve hangi
guvenli alanda gerektigi degeri istemeden/gostermeden anlatilir. Engelleyici
degilse calisma devam eder ve ertelenen gereksinim faz sonucunda tekrar
raporlanir.

Secret degerleri sohbete, source control'e, Android'e, response'a veya loglara
girmez. Local secrets/environment icinde tutulur; varligi ve backend tarafindan
okunabildigi degeri yazdirmadan dogrulanir. Ornegin iyzico fazinda sandbox API
key/secret, Checkout Form ve gerekiyorsa Card Storage/Marketplace yetkisi;
bildirim fazinda FCM service account/proje ayari; Testcontainers fazinda Docker
runtime'i bu kontrolun parcasidir.

## Uygulama Fazlari

### Faz 0 - Karar Kilitleri

- Iyzico Card Storage/hosted save destegi: destek cevabi, backend uygulamasi,
  local PostgreSQL `V8` ve provider E2E dogrulamasi tamamlandi
- Iyzico Marketplace/submerchant erisimi

### Faz 1 - Ortak Backend Temeli

- Yeni Flyway migration sirasi
- Legacy Long `BaseEntity` ile yeni sade `UuidAuditedEntity` sinirinin
  kurulmasi
- UUID ve `Instant`/PostgreSQL `TIMESTAMPTZ` audit standardi
- Domain role/ownership method security
- Pagination response ve OpenAPI standardi
- Yeni ErrorCode gruplari
- Local media ve public callback config ayrimi

### Faz 2 - Medya ve Rehber Profili

- Media upload/storage
- Guvenli media binary GET endpoint'i, public/owner erisim kurali ve orphan
  cleanup scheduler'i
- Guide profile/languages/avatar
- Public guide projection

### Faz 3 - Tur, Session ve Admin

- Tour/session schema ve lifecycle
- Publish/review/change request
- Guide-owned query/mutation
- Admin approve/reject
- Public detail/search/popular/top guides
- Public guide search ve current-guide dashboard projection'lari
- Location alanlari ve IANA `timeZoneId` format dogrulamasi

### Faz 4 - Rezervasyon, Iptal, Yorum ve Performans

- Seat hold ve concurrency
- Reservation snapshot
- Tourist trips
- Guide/tourist cancellation state
- Review eligibility ve aggregate query'leri
- GuidePerformanceSummary ve level

### Faz 5 - Iyzico, Wallet ve Finans

- Checkout Form initialize/retrieve/callback/webhook
- Payment events, signature, idempotency, reconciliation
- Wallet top-up ve tour booking
- Refund
- Guide earning
- Bank account ve withdrawal
- Saved card provider destegi dogrulandi; `V8` semasi, provider-backed
  listeleme/silme/varsayilan kart API'leri, sonraki hosted checkout ve imzali
  webhook E2E dogrulamasi tamamlandi

### Faz 6 - Bildirim ve Mesajlasma

- Tamamlandi: `V9` notification, `V10` chat ve `V11` FID migration'lari
- Tamamlandi: notification history/read/preferences ve FID kaydi
- Tamamlandi: commit-sonrasi FCM ve lifecycle notification baglantilari
- Tamamlandi: Chat REST, PostgreSQL ve cursor/read/unread sozlesmeleri
- Tamamlandi: JWT korumali private WebSocket/STOMP teslimati
- Android unread badge, REST resync, FCM ve STOMP tuketimi final Android
  entegrasyonunda bu sozlesmeye baglanir

### Faz 7 - Scheduler, Seed ve Demo Verisi

- Tamamlandi: bounded reservation timeout, session completion, payment
  reconciliation, refund recovery, guide earning availability, FCM retry,
  upcoming tour reminder, stale FID ve media cleanup job'lari
- Tamamlandi: restart sonrasinda kaybolmayan attempt/timestamp/deduplication
  state'i ve gec verified payment icin capacity re-check veya tek tam iade
- Tamamlandi: production migrationina girmeyen, varsayilan kapali ve yalniz
  `local` profilde idempotent guide/tourist hesaplari olusturan demo seed
- Faz tamamlandiginda test profili, Flyway `V1-V12`, tum Maven testleri, OpenAPI
  ve local PostgreSQL `V12` + Hibernate validate + uygulama baslangiciyla
  dogrulandi. Final backend kontrolunde test profili PostgreSQL 18
  Testcontainers'a tasindi ve H2 kaldirildi
- Iki farkli telefon/kullanici ile gercek LAN UI akisi, Android FID/REST/STOMP
  entegrasyonu tamamlandiginda yapilacak final E2E kontroludur

### Tamamlanan Dikey Dilim - Coklu Tahsilat Para Birimi

- Tamamlandi: `V13`, FX quote domain/repository/service, Frankfurter ECB adapter'i,
  currency-options ve tur/wallet quote API'leri, quote-bound hosted initialize,
  dinamik `TR/EN` Checkout Form locale, charge snapshot dogrulamasi ve ayni charge
  currency'de provider iadesi uygulanmistir.
- Canonical platform/wallet/tur/kazanc/withdrawal parasi USD kalmistir; yalniz
  hosted provider tahsilati config ile etkin `USD/TRY/EUR/GBP` arasindan secilir.
- Dikey dilim tamamlandiginda tum Maven testleri, OpenAPI, Frankfurter gercek
  endpoint'i ve local PostgreSQL `V13` + Hibernate validate + uygulama baslangici
  dogrulanmistir. Final backend kontrolunde temiz PostgreSQL 18 Testcontainers
  `V1-V13` migration ve kalici regression paketi de basariyla calistirilmistir.
- Android para birimi secimi/quote UI'i ile her etkin para birimindeki iyzico
  Sandbox E2E, final Android odeme entegrasyonunda yapilacaktir.

### Faz 8 - Final Backend Dogrulamasi

- Teknik backend dogrulamasi tamamlandi: Colima/Docker Homebrew servisi ve
  PostgreSQL 18 Testcontainers test-scope altyapisi kuruldu. Maven testleri
  komuta ozel `DOCKER_HOST` eklemeden calisir; uygulamanin runtime mimarisine veya
  Android akisina yeni servis eklenmez.
- H2, PostgreSQL'e ozel `JSONB`, `TIMESTAMPTZ`, constraint, pessimistic lock ve
  concurrency davranislarinda ek deger saglamadigi icin kaldirildi. Temiz
  PostgreSQL 18.6 uzerinde `V1-V13` Flyway migration'lari ve Hibernate validate
  basariyla calisti.
- 36 test sinifindaki 72 kalici test; repository/sema, capacity ve wallet race,
  idempotency, atomik wallet purchase rollback, wallet iptal-iade-ledger-earning,
  gec odeme capacity re-check/tek iade, auth lifecycle, role/ownership, medya
  MIME/boyut/path traversal/public-draft/orphan cleanup, guide projection,
  chat/FCM/STOMP ve OpenAPI sozlesmelerini dogruladi.
- Payment quote, amount/currency mismatch, merkezi yuvarlama, duplicate provider
  event/refund ve reconciliation sinirlari kalici testlerle korundu. Phase 5'teki
  iyzico Sandbox success/failure/3DS, signed webhook, duplicate callback ve kayitli
  kart provider E2E sonuclari gecerlidir.
- Android para birimi secimi/quote UI'i bulunmadigi icin her etkin charge currency
  ile iyzico Sandbox E2E, Android odeme entegrasyonundaki final cihaz testinde
  yapilir. Bu, backend contract veya implementasyon eksigi degildir.
- Swagger/OpenAPI canli local profilde HTTP 200, 70 path ve JWT bearer semasiyla;
  local PostgreSQL 18.3 ise Flyway `V13`, Hibernate validate, Firebase credential
  ve STOMP broker baslangiciyla dogrulandi.
- Tracked gercek secret, gecici konsol logu, hassas veri logu, TODO/FIXME veya
  production degeri olmayan gecici test bulunmadi. Local secret ve Firebase
  credential dosyalari Git disinda kalir.
- Spring Boot 3.5.x OSS patch hatti, PostgreSQL driver, Flyway 12, springdoc 2.x,
  JJWT, Google API client, Lombok ve Testcontainers uyumlu patch surumlerine
  alindi; uyumsuz major surumlere gecilmedi.
- Altin Kural kapsamindaki paket/katman, SOLID, bagimlilik, isimlendirme,
  okunabilirlik ve gercek kod tekrari incelemesi tamamlanmistir. Arama/siralama
  politikalari domain paketlerine alinmis; tekrar eden version, tur konumu, rol ve
  iade bildirimi kurallari merkezilestirilmis; iade payload'indaki `tourId`
  tutarsizligi giderilmis; kullanilmayan hata kodlari/entity setter'lari
  temizlenmis ve email config'i constructor injection'a alinmistir. Tek
  sorumlulugu koruyan buyuk servisler sirf bolmek icin parcalanmamis, spekulatif
  katman eklenmemistir.

## Android Entegrasyon Kontrol Listesi

Backend tamamen bittikten sonra Android'de su islemler yapilir:

### Ortak Data Katmani

- Her gercek feature icin Retrofit API, network DTO, repository interface ve
  repository implementasyonu eklenir.
- Domain/UI mapper'lari backend response'larindan mevcut ekran modellerini
  uretir.
- Use-case yalniz tekrar kullanilan veya gercek business orchestration olan
  yerde eklenir.
- ViewModel ekran state'i ve UI event sahipligini korur.

### Kaldirilacak Mock Otoriteler

- `TourCatalogStore`
- `TouristReservationStore`
- `TouristPaymentStore`
- `TouristFinanceStore`
- `GuideFinanceStore`
- `GuidePerformanceStore`
- `ChatStore`
- `GuideProfileSharedStore` icindeki kalici veri sorumlulugu

Store isimleri dogrudan repository yapmak yerine ilgili ekranlar yeni
repository/API Flow'larini gozlemler. Kullanilmayan mock dosyalar, mapper'lar,
fixed viewer role ve demo kimlikleri silinir.

### Tur ve Profil

- Kamera/galeriden secilen `content://` URI yalniz Android'in gecici local
  secimidir; domain veya network modelinde kalici fotograf kimligi sayilmaz.
- Profil fotografi ve publish/edit cover dosyasi multipart endpoint'e bir kez
  yuklenir. Backend `READY` asset icin `mediaAssetId` ve `imageUrl` dondurdukten
  sonra profil/tur request'ine yalniz `mediaAssetId` eklenir.
- Upload basarisiz, iptal veya timeout olursa kaydetme/yayinlama tamamlanmis
  gosterilmez; mevcut fotograf korunur ve localized hata/retry durumu sunulur.
- Android yukleme sonrasi backend'in canonical `mediaAssetId`/`imageUrl`
  sonucunu kullanir; local path, drawable ID veya kullanici dosya adi backend'e
  kalici referans olarak gonderilmez.
- Mock drawable fallback yalniz preview/demo icin kalabilir; gercek kayit
  absolute URL kullanir.
- Backend medya endpoint'leri ve OpenAPI sozlesmesi tamamlandiktan sonra Android
  Coil Compose + HTTP network destegi ekler. Ortak `GuideMateImage` local
  `content://`/`file://` onizlemeyi ve backend `http://`/`https://` URL'lerini
  tek noktadan yukler; ekranlar ayri network image kodu yazmaz.
- `GuideMateImage` remote loading/error durumunda mevcut drawable fallback'i ve
  tasarim olculerini korur. Backend'in canonical `imageUrl` degeri kullanilir;
  filesystem yolu, storage key veya provider detayi UI modeline sizmaz.
- Public rehber avatar/tur kapagi normal remote image istegiyle yuklenir. Draft
  medya owner yetkisi gerektiriyorsa ortak image loader request'i mevcut auth
  katmanindan guvenli header alir; token URL'ye query parametresi olarak eklenmez.
- Sabit `Ahmet Yilmaz`, sabit avatar ve sabit guide ID kaldirilir.
- Turlarim, turist popular/search/detail ve rehber profil turlari ayni backend
  Tour/Session kaynagindan mapper'larla uretilir.
- Android secilen konumla birlikte cihaz `ZoneId.systemDefault().id` degerini
  gonderir; backend'in saklayip dondurdugu canonical `timeZoneId` detay ve tarih
  gosterimlerinde kullanilir.
- Portfolio MVP'de farkli cihaz saat diliminden tur duzenleme desteklenmez;
  rehber tur duzenleme formundaki cihaz saat dilimi kullanimi bilincli kapsam
  siniridir ve bu asamada yeniden tasarlanmaz.
- Category publish validation yeniden zorunlu olur.

### Arama ve Dashboard

- `TouristExploreViewModel`, backend OpenAPI tamamlandiktan sonra tur ve rehber
  arama repository akislarini kullanir. Arama metni kontrollu
  debounce/cancellation ile sayfali sorguya donusur; eski sorgunun gec sonucu
  yeni sorguyu ezmez.
- Filtre ekranindaki secimler draft state'tir. `Filtreyi Uygula` aksiyonu draft'i
  stabil `TourSearchFilter`/request parametrelerine cevirip applied state olarak
  kaydeder, filtre ekranini kapatir ve tur aramasini yeniler. Sistem geri tusuyla
  cikmak daha once uygulanmis filtreleri degistirmez.
- Ulke `countryCode`, sehir `cityPlaceId`, kategori/dil stabil kodlari ve fiyat
  araligi USD minor unit olarak backend'e gider. Android yalniz yuklenmis sayfa
  uzerinde ayri bir filtre sonucu uretmez.
- Tur ve rehber arama UI'lari backend item'larini mevcut GuideMate tasarim diliyle
  listeler; loading, empty, retry/error ve pagination durumlari ayri gosterilir.
  Tur sonucuna `sessionId`/`tourId` ile ortak tur detayina, rehber sonucuna
  `guideId` ile public rehber profiline gidilir.
- Arama sonucu ekran tasarimi, kart yerlestirimi ve `LazyColumn` uygulamasi backend
  sozlesmesi tamamlandiktan sonra yapilir; mevcut bos arama/filter MVP ekranlari
  entegrasyon sonunda birakilmaz.
- `GuideHomeViewModel`, paginated `Turlarim` listesinin yuklenen eleman sayisini
  kullanarak aktif/onay bekleyen sayac uretmez. `activeSessionCount` ve
  `pendingReviewCount` degerlerini `GET /api/v1/guides/me/dashboard` canonical
  response'undan alir.
- Dashboard performans kartlari ve bu ayki kazanc ayni response'taki backend
  projection'larindan map edilir. Android puan, katilimci, tamamlanan tur,
  seviye veya kazanc toplamlarini yeniden hesaplamaz.
- Dashboard yenilemesi liste sayfalarini zorla tamamen yuklemez. Tur mutation'i
  sonrasinda ilgili liste ve dashboard repository kaynaklari canonical backend
  sonucuyla invalidate/refresh edilir.

### Rezervasyon ve Odeme

- Android odeme API/DTO/repository kodu, backend OpenAPI ve payment state
  sozlesmesi tamamlanmadan yazilmaz. Boylece gecici endpoint ve model katmani
  olusturulup sonradan yeniden yazilmaz.
- Android bos, sifir veya negatif miktari ve birden az katilimciyi istekten once
  engeller. Gorunen bakiye/kontenjanla hizli uyari verebilir fakat bu sonucu
  guvenlik veya islem basarisi olarak kabul etmez.
- Kritik mutation devam ederken ilgili aksiyon devre disi olur; double-submit
  yalniz UI ile degil backend idempotency ile de engellenir.
- Her kullanici odeme niyeti icin tek idempotency key uretilir; ag retry, yeniden
  cizim ve process recreation durumlarinda ayni key ile mevcut payment/hosted
  checkout yeniden kullanilir. Devam eden islem varken yeni checkout acilmaz;
  aksiyon ancak terminal sonuc, acik iptal veya bilincli yeni odeme niyetinde
  yeniden etkinlesir. Butonun devre disi olmasi UX korumasidir; asil guvence
  backend idempotency, provider event deduplication ve veritabani kisitlaridir.
- Android sandbox buyer T.C./telefon/adres bilgisini bilmez, gondermez,
  saklamaz veya loglamaz. Bu degerler yalniz backend sandbox profile'indadir.
- Tur satin almada Android `sessionId`, `participantCount` ve gerekli
  idempotency bilgisini; wallet top-up'ta canonical USD tutarini; withdrawal'da
  yalniz `bankAccountId`, tutar ve idempotency bilgisini gonderir. Kartli checkout
  oncesi quote istegine secilen
  `chargeCurrencyCode`, initialize istegine yalniz `quoteId` eklenir. Android
  hesaplanmis toplam, kur, charge tutari, bakiye, kontenjan veya basari bayragi
  gondermez.
- Backend Android'e internal `paymentId`, hosted iyzico URL'si ve canonical
  payment state dondurur.
- Android odeme entegrasyonunda wallet top-up ve kartla tur checkout
  onaylarina backend-backed para birimi secimi eklenir. Android backend'in
  currency-options ve quote response'unu kullanir; bolgeyi yalniz varsayilan
  secim onerisi yapar ve kullanicinin secimini degistirmesine izin verir.
- Quote ekraninda canonical USD tutari ile gercek charge tutari/para birimi ve
  expiry birlikte gosterilir. Expired/unavailable/unsupported/card-currency
  uyumsuzlugu localized ve retry edilebilir durumlara map edilir; stale quote
  ile hosted form acilmaz.
- Payment status/detail response'u tur satin aliminda `paymentStatus` ile
  birlikte `reservationStatus` ve varsa `refundStatus` dondurur. Android yalniz
  payment `SUCCEEDED` oldugu icin rezervasyonu basarili kabul etmez.
- Kart ve wallet tur alimlari ayni payment/reservation/refund response
  sozlesmesini kullanir. Wallet seciminde hosted provider ekrani acilmaz;
  Android backend'in atomik canonical sonucunu gosterir.
- Android fiyat/kapasite/bakiye basarisi mutate etmez; backend canonical sonucu
  gosterir ve islem sonrasinda bakiye, kontenjan ve history verisini canonical
  response/repository akisiyla yeniler.
- Android tek tek `guide_earnings` durumlarindan cekilebilir bakiye veya cekilen
  tutar turetmez. Backend'in wallet balance projection'ini ve withdrawal
  durumlarini gosterir; kazanc DTO'su yalniz `PENDING`, `AVAILABLE`, `REVERSED`
  durumlarini tasir. Aylik kazanc response'u `year`, `month`,
  `netEarningsMinor`, `currencyCode` tasir ve yeniden eskiye siralanir; Android
  tekil kazanc sayfalarini indirip ay toplamlarini yeniden hesaplamaz.
- Iyzico hosted URL Custom Tab/WebView icin guvenli secilen mekanizmayla acilir.
- GuideMate odeme oncesi ve sonuc ekranlari mevcut tasarim diliyle, Android
  resource'larindan gelen dinamik secili uygulama diliyle gosterilir.
- Iyzico Checkout Form locale degeri statik degildir; Android'in secili uygulama
  diline gore backend tarafindan `tr` veya `en` olarak initialize istegine
  eklenir ve mevcut hardcoded `Locale.EN` kaldirilir.
- Callback'ten Android'e donus payment ID ile status polling/refresh yapar.
- Iyzico callback'i HTTP POST oldugu icin Android yalniz
  `shouldOverrideUrlLoading` kullanmaz. WebView yasam dongusu callback'lerinden
  sonra canonical payment sonucu backend'den sorgulanir. Callback JSON govdesi
  tek basina basari sayilmaz; SSL hatasi atlanmaz ve JavaScript bridge eklenmez.
  Backend callback JSON + CF Retrieve akisi idempotent kalir; ayni callback/event
  ikinci payment, reservation veya wallet hareketi olusturmaz.
- Android payment `SUCCEEDED` + reservation `CONFIRMED` sonucunda normal basari
  gosterir. Payment `SUCCEEDED` + reservation `EXPIRED` + refund
  `REQUESTED/PROCESSING` sonucunda yerellestirilmis "iadeniz baslatildi";
  refund `MANUAL_REVIEW` sonucunda "isleminiz inceleniyor" durumunu gosterir.
  Bu durumlarda satin alinmis geziyi optimistic olarak listeye eklemez.
- Android wallet satin aliminda bakiyeyi yerel olarak dusmez; wallet iadesinde
  yerel olarak artirmaz. Basarili mutation sonrasinda balance, transaction
  history, reservation ve refund projection'larini repository'den yeniler.
- Mock 2/5 saniyelik basari gecisleri gercek state observation ile degisir.
- Native kart numarasi/SKT/CVV formu kaldirilir; GuideMate tasarim kabugu,
  provider akisina gecis butonu ve yerellestirilmis durum ekranlari korunur.
- Sandbox card detector ve ham karttan Visa/banka bulma kodu kaldirilir.
- Iyzico Card Storage destegi yazili olarak onaylanmis ve backend API'si
  tamamlanmistir. Saved card ekranlari provider-backed internal ID ve yalniz
  maskeli metadata kullanir; ham kart numarasi, SKT veya CVV Android/backend'e
  gelmez.
- Balance, wallet history, earnings, refunds ve withdrawals repository'den gelir.
- Backend ve iyzico kaynakli stabil hata kodlari localized Android metinlerine
  map edilir; provider'in teknik/raw hata mesaji kullaniciya gosterilmez.

### Mesaj ve Bildirim

- Kullanici kaynakli bildirimler backend `actorDisplayName` degerini kullanir;
  sistem bildirimlerinde bu alan `null` olur. Android aktor adi icin ek kullanici
  sorgusu atmaz. Wallet hareketlerindeki `referenceTitle`, turla baglantili
  `TOUR_PURCHASE`, `REFUND`, `GUIDE_EARNING`, `EARNING_REVERSAL` kayitlarinda
  backend'in rezervasyon satin alma snapshot'indan gelir; `TOP_UP` ve
  `WITHDRAWAL` icin `null` kalir ve genel metin Android resource'larindan gelir.

- Faz 6 backend calismasinda yalniz Firebase bootstrap konfigurasyonu icin sinirli
  Android istisnasi vardir: Git disinda tutulan `app/google-services.json`,
  Google Services Gradle plugin'i, Firebase BoM/Messaging bagimliligi ve manifest
  `POST_NOTIFICATIONS` izin bildirimi hazirlanir. Android Kotlin/Java servis,
  Firebase Installation ID (FID) register/refresh, notification channel/runtime
  permission davranisi, repository, UI ve navigation entegrasyonu backend
  tamamlandiktan sonraki Android calismasinda yazilir. STOMP istemci bagimliligi
  backend WebSocket sozlesmesi kesinlesmeden secilmez.
- Backend REST, WebSocket/STOMP ve FCM sozlesmeleri tamamlandiktan sonra Android'e
  Firebase Messaging ve backend protokoluyle uyumlu, bakimi devam eden bir
  STOMP/WebSocket istemci bagimliligi eklenir. Bu bagimliliklar backend
  sozlesmesi kesinlesmeden varsayimla eklenmez.
- FCM ve STOMP istemcileri UI tarafindan dogrudan kullanilmaz; DI ile saglanan
  repository/data source sinirlarinin arkasinda tutulur.
- `UserState.userId/role` gercek viewer identity olur.
- Chat list/detail/unread REST + WebSocket ortak repository'den gelir.
- `clientMessageId`, pending/sent/failed UI akisi korunur.
- Rehber ve turist bildirim ayarlari ortak backend preference sozlesmesinden
  role-specific UI modellerine map edilir. Switch mutation'i backend'in
  dondurdugu canonical response ile guncellenir; basarisizlik sessizce yutulmaz.
- Tercih kapatmak chat mesajini, in-app notification kaydini veya unread sayisini
  silmez; yalniz ilgili push/reminder teslimatini durdurur.
- Guvenlik uyarilari UI'da acik ve degistirilemez kalir; PATCH request'ine
  eklenmez.
- Android'deki 2.000 karakter siniri korunur. Backend'den
  `MESSAGE_TOO_LONG` veya `body` field validation donerse mevcut tasarima
  uygun localized mesaj gosterilir.
- Firebase Messaging FID tabanli teslimat modu etkinlestirilir; SDK'nin verdigi
  FID, GuideMate `installationId` ile birlikte
  `/api/v1/devices/fcm-registration` endpoint'ine kaydedilir. Logout/account
  switch ayni GuideMate `installationId` uzerinden kaydi pasiflestirir.
- Android manifest'te
  `firebase_messaging_installation_id_enabled=true` metadata'si tanimlanir ve
  legacy `onNewToken` yerine `FirebaseMessagingService.onRegistered(fid)`
  callback'i backend FID kaydini yenilemek icin kullanilir.
- Android 13 permission ve notification channel eklenir.
- Semantic notification type/ids type-safe navigation target'ina map edilir.
- Topbar, bottom bar ve recent activity manuel sayac tutmaz.

### Hata ve UX

- Backend stable code'lari Android localized string resource'larina map edilir.
- Field validation hatalari mevcut `supportingText` alanlarina gider.
- Genel request hatalari mevcut uygulama tasarimina uygun Toast/Dialog/one-shot
  mesajla gosterilir; yeni paralel Snackbar sistemi kurulmaz.
- Switch veya mutation basarisizsa canonical state korunur ve neden gorunur.
- Loading/double-submit/retry/timeout/cancel/manual-review durumlari gercek
  backend state'ine baglanir.

### Entegrasyon Sonu Temizlik

- Kullanilmayan mock data, object/singleton store, DTO, mapper, resource ve
  dosyalar silinir.
- Android secret veya iyzico key bulunmadigi dogrulanir.
- Hardcoded guide/tourist ID, `localhost`, `10.0.2.2`, eski LAN IP veya provider
  sonucu bulunmadigi kontrol edilir.
- Process death, logout, account switch ve iki kullanicili LAN testinde state
  sizintisi olmadigi dogrulanir.
- Android ve backend enum, nullability, ID, money ve timestamp sozlesmeleri
  birebir karsilastirilir.

## Backend Tamamlanmis Sayilma Kriteri

Backend su kosullar birlikte saglanmadan tamamlanmis sayilmaz:

- Android ekranlarinin ihtiyac duydugu tum gercek domain endpoint'leri vardir.
- Kalici medya upload sonrasi guvenli absolute URL ile farkli cihazlardan
  goruntulenebilir; public/owner erisimi ve orphan cleanup calisir.
- Rehber arama sayfali ve public-profile kurallarina uygun calisir; current-guide
  dashboard sayaclari paginated liste boyutundan degil tam backend
  projection'indan gelir.
- OpenAPI response'lari mevcut Android UI akislarini besleyecek alanlari tasir.
- Rehber ve turist ayni kalici tur/veri kaynagini gorur.
- Yetki, sahiplik, kontenjan, para ve idempotency backend'de uygulanir.
- Iptal, iade, kazanc ve withdrawal history tutarlidir.
- Callback/webhook/reconciliation provider gecikmesine dayanir; hold sonrasi
  basarili odeme kapasite asimina yol acmaz ve iade sonucu izlenebilir.
- Canonical USD ile provider charge tutari/para
  birimi birbirine karistirilmaz; quote, retrieve, iade ve Android sozlesmesi
  birlikte dogrulanmistir.
- Chat REST/WebSocket/FCM ve notification unread kaynaklari hazirdir.
- Mock store'larin yerine gececek repository sozlesmeleri nettir.
- Android entegrasyonunda yeni domain veya urun akisi tasarlamak gerekmez;
  yalniz API/DTO/repository/mapper baglantisi, provider yonlendirmesi ve mock
  temizligi kalir.

## Bilincli Kapsam Disi

- Gercek para ve production iyzico hesabi
- Vergi, resmi izin, gercek KYC/KVKK/PCI operasyonu
- Ayri React/admin mobil uygulamasi
- AWS zorunlulugu
- Mikroservis, Kafka, Redis, generic outbox ve dagitik sistem
- Gercek banka payout'u Marketplace hesabi erisilebilir degilse
- USD disinda ikinci canonical platform/wallet/muhasebe para birimi; hosted
  kart tahsilatinda backend quote'u ile desteklenen farkli charge currency
  kullanimi kapsam icindedir
- Backend Google Places/Time Zone dogrulamasi ve konumdan saat dilimi cozme
- Gelismis fraud sistemi, MFA, CAPTCHA ve cihaz yonetim paneli

Bu sinirlar disinda kalan veri butunlugu, guvenlik, kullanici magduriyeti veya
para tutarsizligi "MVP" gerekcesiyle eksik birakilmaz.

## Backend Sohbetine Verilecek Baslangic Komutu

```text
Once su devir belgesini tamamen oku:
/Users/ahmetkaragunlu/AndroidStudioProjects/GuideMate/docs/backend-implementation-handoff.md

Belgenin basindaki "Altin Kural - Orantili ve Profesyonel Kod Kalitesi"
bolumunu tum kodlama ve inceleme kararlarinda uygula. Sirf hata bulmak,
dosya/metod uzunlugu veya tek bir benzerlik nedeniyle refactor, abstraction ya
da yeni katman ekleme; somut paket, katman, bagimlilik, tekrar veya
test-edilebilirlik sorunu varsa mimariyi bozmadan gerekli en kucuk duzeltmeyi
yap.

Ardindan mevcut GuideMateBackend kodunun tamamini ve belgede isaret edilen
Android model/ViewModel/store/API sinirlarini yeniden tara. Android kodunu
degistirme. Belgede yer alan anayasa ve ek kurallari tek kaynak kabul ederek
once Faz 0 karar kapilarini ve mevcut backend uyumlulugunu raporla. Ben onay
vermeden kod yazma. Her fazdan once "Dis Bagimlilik ve Kullaniciya Bildirim
Kontrolu"nu uygula; gerekli hesap yetkisi, credential, config, public URL,
kutuphane, local arac veya riskli islemi kodlamadan once degeri acik etmeden
raporla. Onaydan sonra "Calisma Sirasi ve Faz Kapilari"ndaki sirayla fazlari
uygula; her fazda Flyway, entity/DTO/repository/service/controller,
security/ownership, transaction, stable error code, test ve Android response
sozlesmesini birlikte tamamla. Gereksiz katman veya over-engineering ekleme.
```
