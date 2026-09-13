# GuideMate Degisim Listesi

## Altin Kural - Orantili ve Profesyonel Kod Kalitesi

Android ve backend kodu gercek sirket projelerine yakin; SOLID, dusuk
bagimlilik, okunabilirlik, test edilebilirlik, genisletilebilirlik, dogru
isimlendirme ve gercek kod tekrarinin azaltilmasi hedefleriyle yazilir. Bu
hedefler over-engineering yapmak, mevcut dogru kodda zorla hata aramak veya
mimariyi gereksiz yere buyutmek icin kullanilmaz.

- Mevcut yapi dogru, okunabilir ve ihtiyaci karsiliyorsa oldugu gibi korunur.
  Sirf degisiklik yapmak icin hata veya refactor aranmaz.
- Bir sinif, fonksiyon veya dosya yalniz uzun ya da kalabalik gorundugu icin
  bolunmez. Sorumluluk, business kurali, degisiklik nedeni, tekrar veya test
  izolasyonu gercekten ayrisiyorsa en kucuk davranis-koruyucu refactor yapilir.
- Interface, use-case, factory, manager, helper, base class, generic framework
  veya yeni katman yalniz gelecekte kullanilabilir diye eklenmez. Gercek adapter
  siniri, degisebilir dis bagimlilik, tekrar kullanilan business davranisi veya
  anlamli test seam'i varsa kullanilir.
- Ortak yapi yalniz ayni business anlami ve ayni degisiklik nedeni gercekten
  paylasiliyorsa kurulur. Benzer gorunen fakat farkli lifecycle veya kurala sahip
  rehber ve turist akislari zorla ortaklastirilmaz.
- Feature-first sahiplik ve katman yonu korunur. UI/presentation, domain, data,
  network, storage, navigation ve DI sorumluluklari birbirine karistirilmaz.
  Controller/API siniri, DTO, mapper, service, repository ve domain kendi
  sorumluluklarinda kalir.
- UI veya ViewModel Retrofit, persistence, iyzico, FCM, STOMP ya da benzeri dis
  sistem ayrintisini dogrudan bilmez. Dis sistemler dar interface/adapter
  sinirlarinin arkasinda kalir.
- Android kalici is kurali otoritesi olmaz. Yetki, sahiplik, para, kur,
  kapasite, rezervasyon, iade, kazanc, tur lifecycle ve odeme basarisi backend
  sonucundan gelir.
- Tasarim, navigasyon ve kullanici akisi bilincli urun karari olmadan
  degistirilmez. Yeni ekran, destination veya kullaniciya gorunen akis onaysiz
  eklenmez.
- Isimler domain amacini acikca anlatir. Generic ve belirsiz isimler yalniz dar
  ve gercek bir teknik sorumlulugu ifade ediyorsa kullanilir.
- Degisiklik nedeniyle gercekten bosa dusen kod, import, resource, test, dosya
  veya paket temizlenir. Bilincli ertelenen ya da halen gecis gorevi bulunan kod
  gereksiz diye silinmez.
- Her uygulama sonunda degisen kapsamin kullanim taramasi yapilir. Artik
  cagrilmayan fonksiyon, kullanilmayan kod blogu, import, parametre, property,
  resource, test yardimcisi, dosya veya bos paket kesin olarak bosa dustuyse
  ayni degisiklik kapsaminda temizlenir. Yalniz gelecekte kullanilabilir
  varsayimiyla kod tutulmaz; ancak baska faza bilincli ertelenen, dis sozlesmenin
  parcasi olan veya runtime/reflection/DI tarafindan kullanilan kod kanitsiz
  sekilde silinmez.
- Secret, token, kart verisi, tam IBAN, provider credential, teknik exception
  veya hassas kullanici verisi source control, log veya UI hata metnine sizmaz.
- Her uygulama dilimi sonunda paket/katman, bagimlilik yonu, isimlendirme,
  okunabilirlik, gercek kod tekrari ve kullanilmayan kod kontrol edilir. Bulgu
  yoksa sirf refactor yapmak icin kod degistirilmez.

## Test Altin Kurali - Gercek Risk Kadar Test

- Her degisiklikte once su soru sorulur: Gercek bir sirket GuideMate'i production
  ortamina cikarmadan bu davranisin bozulmasini otomatik testle engellemek ister
  miydi? Cevap evetse test ayni degisiklik kapsaminda yazilir.
- Is kurali, state gecisi, mapper/DTO sozlesmesi, repository davranisi,
  serialization, para/kimlik/tarih donusumu, hata esleme, temizleme ve kritik
  navigasyon verisi otomatik test icin onceliklidir.
- Salt metin, renk, bosluk, ikon, ellipsis veya basit Compose yerlesimi icin
  sirf test sayisini artirmak amaciyla kirilgan test yazilmaz. Bunlar davranis
  riski tasimiyorsa derleme/lint ve ilgili kullanici testiyle dogrulanir.
- Test piramidi korunur: Saf davranis icin hizli JVM unit testi; repository ve
  network sozlesmesi icin odakli contract/integration testi; Android framework
  veya gercek kullanici etkilesimi zorunluysa olculu instrumentation/UI testi
  kullanilir.
- Ayni davranisi ayni seviyede tekrar tekrar test eden, implementation ayrintisina
  baglanan veya gercek regresyon yakalamayan gereksiz test eklenmez.
- Yalniz test yazabilmek icin production koduna anlamsiz interface, manager,
  helper, use-case veya katman eklenmez. Test edilebilirlik gercek sorumluluk
  sinirlari ve constructor dependency injection ile saglanir.
- Testler ilgili feature ve katmanin test paketinde bulunur; Android davranisi
  icin backend, backend davranisi icin Android testi yazilmaz.
- Her partta test dosyalarinin feature ve katman sahipligi, JVM, Robolectric
  veya instrumentation source-set secimi, production ve test bagimlilik yonu
  ile SOLID uyumu kontrol edilir. Gercek bir ihlal varsa ayni partta en kucuk
  cozumle giderilir; test ugruna production mimarisi veya katman yonu bozulmaz.
- Kod yazilirken ilgili otomatik testler yazilip calistirilir. Kapsamli manuel
  cihaz/Sandbox/coklu kullanici testleri ayrica `docs/kullanici-testleri.md`
  uzerinden izlenir.
- Kod ve test icin ayni over-engineering siniri gecerlidir: Eksik guvenlik agi
  birakilmaz, fakat varsayimsal gelecek veya yuzeysel coverage artisi icin proje
  sisirilmez.

## Degisiklikler

### Part 1 - Auth, Oturum ve Yerel Guvenlik Sinirlari

Bu part once uygulanir. Oturumun kaydedilmesi, temizlenmesi, access token
siniri ve cihaza ait kalici kimlikler birlikte dogrulanir.

#### TEST-001 - Auth Repository Oturum Davranisi

- Durum: `TAMAMLANDI`
- `AuthRepositoryImpl` icin basarili giriste token ve kullanici kaydi, eksik
  token iceren basarili cevabin reddedilmesi, terminal oturum hatasinda yerel
  oturumun temizlenmesi ve backend logout basarisiz olsa bile yerel temizligin
  tamamlanmasi test edilmektedir.
- AndroidX credential temizligi gercek bir dis sistem adapter siniri oldugu icin
  repository somut framework sinifi yerine `CredentialSessionCleaner`
  sozlesmesine baglanmistir. Hilt uretimde mevcut manager'i kullanmaya devam
  eder; kullanici davranisi degismemistir.

#### TEST-002 - Auth Preferences Kaliciligi

- Durum: `TAMAMLANDI`
- `AuthPreferencesDataSource` icin tum kullanici alanlarini kaydetme ve geri
  yukleme, StateFlow guncellemesi ve cikista onboarding tercihini koruyarak
  kullanici verisini temizleme test edilmektedir. Nullable alanlarin eski
  degerleri birakmadigi da dogrulanmaktadir.

#### TEST-012 - Auth Interceptor Token Siniri

- Durum: `TAMAMLANDI`
- Access tokenin yalniz korumali GuideMate backend isteklerine eklendigi;
  public endpointlere ve harici adreslere sizmadigi odakli ag testiyle
  dogrulanmaktadir.

#### TEST-013 - Installation ID Kararliligi

- Durum: `TAMAMLANDI`
- `InstallationIdDataSource` icin tekrarli ve es zamanli cagrilarin ayni gecerli
  UUID'yi dondurmesi, bozuk kaydin ise guvenli sekilde yenilenmesi test
  edilmektedir.

#### TEST-014 - Android Keystore Smoke Testi

- Durum: `TAMAMLANDI`
- `AndroidKeystoreSessionStorage` icin gercek Android Keystore gerektiren tek
  bir instrumentation smoke testi yazilmistir. Kaydetme, okuma, ustune yazma
  ve temizleme kapsanmis; ayrintili veya kirilgan framework testi
  uretilmemistir. Test `androidTest` source-set'inde derlenmis ve Android
  emulatorundeki gercek Keystore uzerinde basariyla calistirilmistir.

### Part 2 - Odeme, Para Cekme ve Finans Kaliciligi

Bu part Part 1 sonrasinda uygulanir. Uygulama yeniden acilmasi ve tekrar edilen
para islemlerindeki kayip veya cift islem riskleri birlikte ele alinir.

#### TEST-003 - Pending Payment DataStore Davranisi

- Durum: `BEKLIYOR`
- Gercek `DataStorePendingPaymentStorage` ile payment ID kaydetme, dogru ID ile
  temizleme, yanlis ID ile temizleme isteginde mevcut kaydi koruma ve tum kaydi
  temizleme davranislari test edilecektir.

#### TEST-004 - Para Islemlerinde Idempotency

- Durum: `BEKLIYOR`
- Para yukleme ve para cekme basarisiz olduktan sonra ayni islem tekrarlandiginda
  ayni idempotency anahtarinin kullanilmasi; tutar, hedef veya islem degistiginde
  yeni anahtar uretilmesi test edilecektir. Zaten kapsanan tur checkout
  davranisi ayni seviyede yeniden test edilmeyecektir.

#### TEST-011 - Rehber Banka Hesabi Mutasyonlari

- Durum: `BEKLIYOR`
- Banka hesabi silme, varsayilan yapma, basarisiz islemden sonra mutasyon
  kilidinin acilmasi ve basarili islemden sonra canonical listenin yenilenmesi
  `GuideBankAccountsViewModel` ve gerekli repository sinirinda test edilecektir.

### Part 3 - ViewModel State ve Kritik Kullanici Akislari

Bu part Part 1 ve Part 2 sonrasinda uygulanir. StateFlow, coroutine, pagination,
retry ve typed bildirim hedefleri mevcut davranisi degistirmeden guvenceye
alinir.

#### TEST-005 - Tur Duzenleme Kismi Basari Akisi

- Durum: `BEKLIYOR`
- `GuideTourEditViewModel` icin yalniz session degisikligi ve content basarili
  olduktan sonra session guncellemesinin basarisiz olmasi test edilecektir.
  Retry sirasinda basarili content isleminin ikinci kez gonderilmedigi
  dogrulanacaktir.

#### TEST-006 - Turist Ana Sayfa State Akisi

- Durum: `BEKLIYOR`
- `TouristHomeViewModel` icin ilk yukleme, kategoriye gore dogru sorgu, hata
  davranisi ve review degisikliginden sonra popular tur ile rehber verilerinin
  yenilenmesi test edilecektir.

#### TEST-007 - Bildirim Ekrani State Akisi

- Durum: `BEKLIYOR`
- `NotificationViewModel` icin ilk yukleme, cached icerik varken yenileme hatasi,
  load-more korumalari, tek bildirimi okuma ve tumunu okundu yapma state
  gecisleri test edilecektir.

#### TEST-008 - Sehir Arama State Akisi

- Durum: `BEKLIYOR`
- `CityPickerViewModel` icin debounce, onceki aramanin iptali ve son sorgunun
  kazanmasi, kisa sorguda sonuclarin temizlenmesi, hata/retry ve secimin
  tuketilmesi test edilecektir.

#### TEST-009 - Cuzdan Hareketleri Sayfalama Akisi

- Durum: `BEKLIYOR`
- Rehber ve turist cuzdan hareketleri ViewModel'lerinde ilk sayfa, sonraki
  sayfa, son sayfa korumasi ve append hatasinda mevcut icerigin korunmasi test
  edilecektir. Ayni paging davranisi gereksiz yere farkli seviyelerde tekrar
  test edilmeyecektir.

#### TEST-010 - Bildirim Intent ve Typed Hedef Koprusu

- Durum: `BEKLIYOR`
- `NotificationTargetParser` icin FCM data haritasindan typed hedef uretme,
  hedefi Intent extras'a yazip geri okuma, extras'i tek kullanimdan sonra
  temizleme ve eksik/bilinmeyen veride guvenli fallback test edilecektir.

### Part 4 - Test Mimarisi Temizligi ve Korunacak Kapsam

Bu part son uygulanir. Onceki partlarda eklenen testlerden sonra source-set,
feature sahipligi, tekrar ve kullanilmayan test taramasi yapilir.

#### TEST-015 - Tour Checkout Testini JVM Katmanina Tasimak

- Durum: `BEKLIYOR`
- UI veya gercek cihaz davranisi test etmeyen `TourCheckoutViewModelTest`,
  `androidTest`ten `src/test` altina tasinacak ve gerekli Android API davranisi
  mevcut Robolectric altyapisiyla saglanacaktir. Bes mevcut senaryo aynen
  korunacaktir.

#### TEST-016 - Notification Category Testini Tamamlamak

- Durum: `BEKLIYOR`
- `NotificationCategoryTest`, adinin belirttigi gibi mevcut butun
  `NotificationType` degerlerini kapsayacak ve yeni bir bildirim turunun sessizce
  yanlis kategoriye dusmesini yakalayacaktir.

#### TEST-017 - Java Time Test Sahipligini Duzeltmek

- Durum: `BEKLIYOR`
- `common/network/serialization` altindaki saf zaman adapter testleri feature
  DTO'larina ve uygulama DI'ina bagimli kalmayacaktir. Saf adapter davranisi
  yerel test modeliyle common katmaninda; notification/payment DTO serialization
  sozlesmeleri ise ilgili feature data test paketlerinde tutulacaktir.

#### TEST-018 - Gecerli Testleri Gereksiz Yere Silmemek

- Durum: `KORUNACAK`
- Mevcut taramada dummy, her zaman basarili, `@Ignore` edilmis veya kesin olarak
  obsolete test bulunmamistir. Kanit olmadan test silinmeyecektir.

#### TEST-019 - Coverage Ugruna Test Uretmemek

- Durum: `KORUNACAK`
- Her mapper, DTO, string, renk, basit delegasyon veya yalniz satir kapsami icin
  test yazilmayacaktir. `OnboardingRepositoryImpl` gibi davranis eklemeyen dar
  delegasyon sinirlari icin coverage-theater testi uretilmeyecektir.

#### TEST-020 - Harici SDK Test Sinirini Korumak

- Durum: `KORUNACAK`
- Firebase ve Places SDK siniflari agir ve kirilgan mock katmanlariyla taklit
  edilmeyecektir. Uygulamanin kendi parser, repository ve state sinirlari
  otomatik test edilecek; gercek servis davranisi cihaz ve E2E testleriyle
  dogrulanacaktir.

#### TEST-021 - Guclu Mevcut Test Kapsamini Korumak

- Durum: `KORUNACAK`
- Odeme, sohbet, bildirim repository'si, medya, validation, mapper ve mevcut dort
  use-case testleri farkli gercek sorumluluklari korudugu icin tutulacaktir.
  Benzer isimli testler yalniz bu nedenle tekrar kabul edilmeyecek veya
  birlestirilmeyecektir.

## Uygulama Sirasi ve Part Kapilari

1. `Part 1` tamamlanir; auth, DataStore ve guvenlik siniri testleri calistirilir.
2. `Part 2` tamamlanir; odeme, cuzdan ve finans testleri calistirilir.
3. `Part 3` tamamlanir; ilgili ViewModel ve navigation testleri calistirilir.
4. `Part 4` tamamlanir; source-set ve test sahipligi temizlenir.
5. Her part sonunda ilgili odakli testler, ardindan `testDebugUnitTest` ve
   `compileDebugAndroidTestKotlin` calistirilir.
6. Gercek Android Keystore smoke testi emulator veya cihaz hazir oldugunda
   `connectedDebugAndroidTest` ile calistirilir.
7. Son durumda paket/katman yonu, kullanilmayan test yardimcilari ve test tekrari
   yeniden taranir; bulgu yoksa ek test veya refactor yapilmaz.
