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
- Kod yazilirken ilgili otomatik testler yazilip calistirilir. Kapsamli manuel
  cihaz/Sandbox/coklu kullanici testleri ayrica `docs/kullanici-testleri.md`
  uzerinden izlenir.
- Kod ve test icin ayni over-engineering siniri gecerlidir: Eksik guvenlik agi
  birakilmaz, fakat varsayimsal gelecek veya yuzeysel coverage artisi icin proje
  sisirilmez.

## Degisiklikler

### DEG-001 - Kazanc Ekranindaki Ilk Yenileme Tekrarini Kaldirmak

- Durum: `TAMAMLANDI`
- `GuideEarningsViewModel` olusturulurken `init` icinde `refresh()` cagriliyor;
  kazanc veya rehber cuzdan rotasi composition'a girdiginde navigation katmani
  ayni veriyi yeniden istiyor. Bu durum ilk acilista gereksiz ve iptal edilip
  yeniden baslatilan bir backend istegi olusturabilir.
- `LaunchedEffect(Unit)` ekrandan ayrilip tekrar girildiginde yeniden calisir.
  Bu nedenle eski rapordaki "yalniz ilk composition'da calisir" gerekcesi esas
  alinmayacaktir.
- En sade cozum, ViewModel `init` icindeki eager `refresh()` cagrisini kaldirip
  rota acilisindaki yenilemeyi korumaktir. Boylece veri yalniz gercekten gereken
  ekran acildiginda ve ekrana yeniden girildiginde guncellenir.
- Bildirim kaynakli `EARNING_AVAILABLE` yenilemesi, secili yil davranisi, ekran
  tasarimi, navigation ve backend sozlesmesi degismeyecektir. Yeni lifecycle
  helper'i, use-case, repository veya ekran eklenmeyecektir.
- Test karari: Mevcut ViewModel testleri eager yukleme varsayiyorsa ekran
  sahipligindeki yeni davranisa gore guncellenecektir. Salt `LaunchedEffect`
  calistigini kanitlamak icin gereksiz Compose testi yazilmayacaktir; derleme ve
  mevcut kazanc state testleri yeterlidir.

### DEG-002 - Bildirim Islem State'ini Isimli Hale Getirmek

- Durum: `TAMAMLANDI`
- `NotificationViewModel` icindeki nested `combine`, ek islem durumlarini
  `Triple<Boolean, Boolean, String?>` ile tasiyor. Kod calisiyor ancak
  `first`, `second` ve `third` alanlari kendi anlamlarini aciklamiyor.
- Yalniz ViewModel sahipliginde dar bir `NotificationOperationState` modeli
  kullanilarak `isLoadingMore`, `isMarkingAllRead` ve `errorMessage` alanlari
  isimli hale getirilecektir.
- Repository'nin canonical bildirim, okunmamis sayi ve sayfalama akislarinin
  sahipligi korunacaktir. Butun state tek bir mutable UI state'e tasinmayacak;
  manuel senkronizasyon veya ikinci bir state kaynagi olusturulmayacaktir.
- Bildirim yenileme, FCM/STOMP, rozet, okundu senkronizasyonu, hata mesaji,
  tasarim ve kullanici davranisi degismeyecektir. Bu yalniz okunabilirlik ve
  isimlendirme refactor'udur.
- Test karari: Davranis degismedigi icin yeni test yazilmayacaktir. Mevcut
  `NotificationViewModel` testleri ve derleme, refactor'un ayni state'i
  urettigini dogrulamak icin yeterlidir.

### DEG-003 - Kesfet Arama Tetikleyicisinin Niyetini Aciklamak

- Durum: `TAMAMLANDI`
- `TouristExploreViewModel` arama akisi tab, arama metni ve filtre degisikliklerini
  dinliyor; ancak `collectLatest { (tab, _, _) -> ... }` ifadesi arama metni ve
  filtrenin neden combine'a dahil edildigini okuyucuya aciklamiyor.
- Arama metni ve filtre degerleri sonuc lambdasinda kullanilmasa bile degisiklik
  tetikleyicileridir; gercek sorgu yenileme aninda guncel UI state'ten uretilmeye
  devam edecektir.
- Akis, bu niyeti isimlerle gosteren dar bir trigger degerine veya acik bir
  combine donusumune cevrilecektir. Yeni genel event framework'u, use-case,
  repository veya ek state kaynagi kurulmayacaktir.
- Debounce, `collectLatest`, request-generation korumasi, filtreleme, sayfalama,
  hata/retry davranisi ve ekran tasarimi aynen korunacaktir.
- Test karari: Part 1'de eklenen kesfet ViewModel testleri arama metni, filtre,
  gec sonuc ve sayfalama davranisini zaten kapsadigi icin yeni bir test
  yazilmayacaktir. Mevcut testler refactor sonrasinda yeniden calistirilacaktir.

### DEG-004 - Rehber Profilinde Yinelenen Popular Tur Istegini Onlemek

- Durum: `TAMAMLANDI`
- `GuideProfileViewModel` ve `GuideProfilePreviewViewModel`, onbellekte rehber
  profili varsa popular turlari `init` sirasinda isteyebiliyor. Ardindan profil
  yenilemesi basarili olunca ayni rehber icin ayni istek yeniden baslatiliyor.
  Tek ekran acilisinda ayni verinin iki kez istenmesi gereksiz backend ve ag
  yuku olusturabilir; gec tamamlanan iki sonuc da birbiriyle yarismamalidir.
- Ortak helper, base ViewModel veya yalniz repository metodunu cagiran bos bir
  use-case eklenmeyecektir. Public rehber profilinin uc turluk on izleme,
  bagimsiz hata ve retry davranisi farkli oldugu icin bu akis zorla
  ortaklastirilmayacaktir.
- Kendi profil ve on izleme ViewModel'lerinde acilis karari dar tutulacaktir.
  Profil yenilemesi basariliysa guncel `guideId`, yenileme basarisiz fakat
  onbellekte profil varsa cached `guideId` kullanilarak popular turlar yalniz
  bir kez istenecektir. Gerekli is iptali veya aktif is korumasi ilgili
  ViewModel sahipliginde kalacaktir.
- Profil, popular tur kartlari, hata davranisi, navigation, ekran tasarimi ve
  backend sozlesmesi degismeyecektir. Degisiklik yalniz yinelenen istegi ve
  olasi sonuc yarisini kaldiracaktir.
- Test karari: Onbellekte profil varken basarili ve cached-fallback acilislarinda
  popular tur repository cagrisinin yalniz bir kez yapildigi odakli ViewModel
  testleriyle dogrulanacaktir. Public profil akisi veya Compose gorunumu ayni
  davranis icin yeniden test edilmeyecektir.

### DEG-005 - Bildirim Ayarlari Ortak Ekran State Host'unu Ayirmak

- Durum: `TAMAMLANDI`
- Rehber ve turist bildirim ayari ekranlari ayni
  `NotificationPreferencesViewModel` state'ini lifecycle-aware toplama,
  kullanici mesajini Toast olarak gosterip temizleme ve ortak loading/error/retry
  davranisini birebir tekrarliyor. Rol bazli switch secenekleri ve metinler ise
  farkli business sunumlari olarak kalmalidir.
- Yalniz ortak ekran state sahipligi
  `notification/presentation/settings/NotificationSettingsScreenHost.kt`
  icinde toplanacaktir. Host state toplama, mesaj lifecycle'i ve
  `GuideMateContentState` sinirini yonetecek; rehber ve turist content
  composable'lari kendi mevcut paketlerinde kalacaktir.
- Ortak host Retrofit, DTO, repository implementation, FCM, STOMP veya
  navigation bilmeyecektir. ViewModel ve domain repository sozlesmesi mevcut
  yonleriyle korunacak; yapi `common` paketine tasinmayacaktir cunku yalniz
  notification feature'ina aittir.
- Role ozel ekranlar ortak host'u kullanacak ancak kendi switch callback'lerini,
  string resource'larini ve tasarimlarini koruyacaktir. Yeni ekran, destination,
  backend degisikligi veya genel amacli Compose framework'u eklenmeyecektir.
- Test karari: Bu davranis degistirmeyen presentation refactor'u icin yeni ve
  kirilgan Compose testi yazilmayacaktir. Mevcut notification preference
  ViewModel testleri korunacak; derleme, lint ve degisen kapsamin kullanim
  taramasi yeterli olacaktir.

### DEG-006 - Rehber Profil On Izlemesinde State ve Modifier Sahipligini Netlestirmek

- Durum: `TAMAMLANDI`
- `GuideProfilePreviewScreen`, lifecycle-aware toplanan state'i diger ekranlardan
  farkli olarak `State<T>` seklinde tutup her kullanimda `.value` ile aciyor.
  Mevcut kullanim teknik olarak dogru olsa da proje genelindeki `by` delegate
  sozlesmesiyle uyumlu ve daha okunabilir hale getirilecektir.
- Ayni ekranda cagirandan gelen `modifier`, hem dis `GuideMateContentState` hem
  de ic `GuideProfileContent` dugumune uygulanmaktadir. Bos modifier ile mevcut
  gorunum etkilenmese de ileride padding, boyut, tiklama, test etiketi veya
  semantics eklendiginde ayni etkinin iki kez uygulanmasi riski vardir.
- Cagirandan gelen modifier yalniz ekranin en dis dugumune uygulanacak; ic icerik
  kendi bagimsiz `Modifier` degerini kullanacaktir. Mevcut etkili padding,
  boyutlar, renkler, fontlar, state akisi ve kullanici davranisi degismeyecektir.
- Cozum yalniz `GuideProfilePreviewScreen` presentation sinirinda tutulacaktir.
  ViewModel, repository, domain, navigation, backend, yeni helper, use-case veya
  ortak Compose framework'u eklenmeyecektir.
- Test karari: `by` ve modifier sahipligi davranis-koruyucu Compose temizligidir.
  Sirf bu satirlar icin kirilgan UI testi yazilmayacak; derleme, lint ve kullanim
  taramasi yeterli olacaktir.

### DEG-007 - Turist Sohbet Rotasinda State Okuma Bicimini Tutarlilastirmak

- Durum: `TAMAMLANDI`
- `TouristNavGraph` icindeki sohbet destination'i lifecycle-aware Compose
  state'ini `State<T>` olarak tutup `.value` ile okumaktadir. Bu kullanim teknik
  olarak dogru ve guvenlidir; mesaj state'i, recomposition veya lifecycle
  davranisinda hata olusturmamaktadir.
- Projenin diger ekran ve destination'larinda kullanilan Kotlin delegation
  sozlesmesiyle tutarlilik icin state `by collectAsStateWithLifecycle()` ile
  acilacak ve `ChatListScreen`e dogrudan asil UI state verilecektir.
- Degisiklik yalniz `TouristNavGraph` presentation/navigation baglanti noktasinda
  tutulacaktir. Chat ViewModel, repository, state modeli, rota, back stack,
  ekran tasarimi ve backend sozlesmesi degismeyecektir.
- Test karari: Bu davranis degistirmeyen iki satirlik okunabilirlik refactor'u
  icin yeni test yazilmayacaktir. Derleme, lint ve kullanilmayan import taramasi
  yeterli olacaktir.

### DEG-008 - Rehber Ana Sayfa Para Birimi Varsayilanini Tekillestirmek

- Durum: `TAMAMLANDI`
- `GuideHomeUiState` backend cevabi gelmeden once kullanilacak para birimini
  dogrudan `"USD"` olarak tanimliyor. Projede ayni platform varsayilani icin
  zaten `PLATFORM_CURRENCY_CODE` bulundugundan iki ayri kaynak tutulmayacaktir.
- Varsayilan state degeri `PLATFORM_CURRENCY_CODE` kullanacaktir. Bu sabit
  yalniz ekranin ilk, bos veya backend cevabi henuz gelmemis state'i icin UI
  fallback'idir; finansal is kurali ya da kalici para birimi otoritesi degildir.
- Dashboard basariyla yuklendiginde backend'in dondurdugu `currencyCode` mevcut
  davranistaki gibi varsayilan degerin uzerine yazilacak ve kesin deger olmaya
  devam edecektir. Android sabiti backend sonucunu ezmeyecek; bakiye, kazanc,
  odeme ve hareket para birimleri backend sozlesmesinden alinacaktir.
- Cozum `GuideHomeUiState` presentation modelinde dar tutulacaktir. Nullable
  para birimi ve her formatlama noktasina gereksiz null kontrolu, yeni config
  katmani, repository, use-case veya backend degisikligi eklenmeyecektir.
- Tasarim, gosterilen mevcut `USD` degeri, hesaplamalar ve kullanici akisi
  degismeyecektir. Test karari: Bu tek kaynak refactor'u icin yeni test
  yazilmayacak; derleme, lint ve kullanilmayan import taramasi yeterli olacaktir.

### DEG-009 - Tur Detay UI State'ini Anlamli Sorumluluklara Ayirmak

- Durum: `TAMAMLANDI`
- `TourDetailUiState` su anda tur, oturum, rehber ve yorum bilgilerini ayni
  seviyede tasiyan 26 alanli bir presentation modelidir. Alanlarin tamami ayni
  anda degisen tek bir kavrama ait olmadigi icin yalniz rehber alanlarini
  birlestirerek modeli 23 alana dusurmek yeterli bir cozum olmayacaktir.
- Ana state, gercek UI sorumluluklariyla uyumlu dort anlamli sinira
  ayrilacaktir: `tour`, `session`, `guide` ve `reviews`. Tur kimligi, baslik,
  medya, puan, aciklama, kategori ve dil bilgileri tur grubunda; tarih, sure,
  konum, bulusma noktasi, fiyat, kapasite, katilimci ve durum bilgileri session
  grubunda; rehber kimligi, adi ve profil gorseli guide grubunda kalacaktir.
- Kalan kapasite veya finansal deger gibi backend otoritesindeki bilgiler
  Android tarafinda yeniden uretilmeyecek; yalniz mevcut backend/domain
  sonucunun presentation icin anlamli yapida tasinmasi saglanacaktir.
- Alt modeller `tour/presentation/detail/model` sahipliginde kalacaktir.
  `DrawableRes` ve formatlanmis UI degerleri icerdikleri icin domain ya da data
  katmanina tasinmayacak, genel `common` paketine alinmayacaktir. Farkli bir
  lifecycle'a ait `TourPublishGuideState` veya baska ekran modelleri sirf alanlari
  benziyor diye yeniden kullanilmayacaktir.
- Mapper'lar ve UI kullanimlari yeni anlamli alan yollarina uyarlanacak; ana
  `TourDetailUiState` yaklasik dort ust seviye parametre tasiyacaktir. Backend
  DTO'su, repository sozlesmesi, navigation, ekran tasarimi, gosterilen veri ve
  kullanici akisi degismeyecektir.
- Refactor sonunda eski duz alanlara ait kullanilmayan import, property ve
  yardimci kodlar temizlenecektir. Yalniz parametre sayisini dusurmek icin ek
  interface, use-case, manager, generic model veya yeni katman eklenmeyecektir.
- Test karari: Mevcut mapper ve ViewModel testleri yeni gruplu modele
  uyarlanacaktir. Tur, session ve rehber alanlarinin dogru gruba aktarildigini
  kapsayan mevcut dogrulamalar korunacak; salt nested property yolu degisti diye
  ayni davranisi tekrarlayan yeni testler yazilmayacaktir.

### DEG-010 - Rehber Turlarim Sayfalama Tetikleyicisini Footer'a Tasimak

- Durum: `TAMAMLANDI`
- `GuideMyToursScreen` yeni sayfa yukleme kararini son tur kartinin
  `itemsIndexed` blogu icinde vermektedir. Mevcut ViewModel yinelenen istegi
  engelledigi icin bu kritik bir hata degildir; ancak kart cizimi ile liste
  sayfalama lifecycle'ini ayni ogeye baglayarak sorumluluklari karistirmaktadir.
- Tur kartlari yalniz kendilerini gosterecek sekilde normal `items` blogunda
  kalacaktir. Listenin sonunda `isLoadingMore`, `appendFailed` ve `canLoadMore`
  durumlarini sirayla yoneten bagimsiz footer ogeleri kullanilacaktir.
- Yeni sayfa tetikleyicisi, footer composition'a girdiginde mevcut
  `viewModel.loadMore()` metodunu `uiState.tours.size` anahtariyla cagiracaktir.
  Boylece her basarili eklemede yeni liste boyutu yeni sayfaya izin verirken,
  ayni boyut icin gereksiz tekrar tetiklenmeyecektir. ViewModel'deki mevcut
  `canLoadMore`, `isLoadingMore` ve aktif is korumalari aynen korunacaktir.
- Yalniz `GuideMyToursScreen` presentation kodu duzenlenecektir. ViewModel,
  repository, backend, API, tab davranisi, kart tasarimi, loading ve retry
  gorunumu degismeyecektir. Generic paging delegate, helper, use-case veya yeni
  katman eklenmeyecektir.
- Test karari: Sayfalama business ve state gecisleri mevcut ViewModel testleriyle
  korunacaktir. Salt `LaunchedEffect` konumunu kanitlamak icin kirilgan Compose
  testi yazilmayacak; ilgili mevcut testler, derleme ve lint calistirilacaktir.

### DEG-011 - Mevcut Dimension Tokenlarini Tutarli Kullanmak

- Durum: `TAMAMLANDI`
- `TouristHomeScreen` icindeki `4.dp` yatay liste boslugu, projede zaten ayni
  degeri temsil eden `R.dimen.spacing_tiny` bulunmasina ragmen dogrudan
  yazilmistir. Ayni tasarim tokeni iki ayri kaynaktan yonetilmeyecektir.
- Mevcut `dimen` kaynaklariyla anlam ve deger olarak birebir eslesen dogrudan
  `dp` kullanimlari `dimensionResource` uzerinden alinacaktir. Bu kapsamda
  yatay liste boslugu `spacing_tiny` kullanacaktir.
- `20.dp`, `24.dp`, `152.dp` ve `168.dp` gibi belirli bir composable veya
  yerlesime ait, mevcut ortak tokenla eslesmeyen tekil olculer sirf XML'e
  tasinmis olmak icin yeni resource'a donusturulmeyecektir. Ayni sekilde Material
  elevation degerleri spacing tokeni olarak ele alinmayacaktir.
- Yeni bir dimension kaynagi ancak ayni tasarim anlami gercekten birden fazla
  yerde paylasildiginda veya cihaz/resource varyanti gerektiginde eklenecektir.
  Sayi benzerligi tek basina ortaklastirma gerekcesi olmayacaktir.
- Degisiklik yalniz presentation kaynak kullanimidir. Ekran olculeri, bosluklar,
  kart boyutlari, tasarim, kullanici akisi ve backend davranisi degismeyecektir.
- Test karari: Salt ayni `4dp` degerinin mevcut resource'tan okunmasi icin
  kirilgan UI testi yazilmayacak; derleme, lint ve gorsel davranisin korunmasi
  yeterli olacaktir.
