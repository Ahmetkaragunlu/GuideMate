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

### DEG-001 - Hesap Degisiminde Eski Kullanici Verisinin Geri Yazilmasini Onlemek

- Durum: `KOD BEKLIYOR`
- Chat ve bildirim repository'lerinde uzun suren yenileme veya realtime
  islemleri, kullanici cikis yaptiktan ya da baska hesaba gectikten sonra
  tamamlanabilir. Eski istek sonucu kontrol edilmeden ortak state'e yazilirsa
  yeni kullanici kisa sureligine onceki hesabin sohbet veya bildirim verisini
  gorebilir.
- Kullanici oturumu gozlemi uygun yerde `collectLatest` ile yapilacak; hesap
  degistiginde repository'ye ait eski yenileme, reconnect ve realtime isleri
  iptal edilecektir.
- Her kullaniciya ait istek baslatilirken mevcut kullanici veya session-generation
  degeri yakalanacak; sonuc state'e yazilmadan once ayni oturumun halen aktif
  oldugu dogrulanacaktir. Eski oturuma ait gecikmis sonuc sessizce yok
  sayilacaktir.
- Yalniz `collectLatest` iptaline guvenilmeyecektir. Network cagrisi iptale gec
  cevap verebilecegi icin is iptali ile stale-result/session-generation korumasi
  birlikte uygulanacaktir.
- Cozum `ChatRepositoryImpl` ve `NotificationRepositoryImpl` sahipliginde dar
  tutulacaktir. Ekranlara veya ViewModel'lara oturum guvenligi dagitilmayacak;
  yeni backend endpoint'i, DTO, tablo, navigation rotasi, ekran ya da genel
  amacli framework eklenmeyecektir.
- Mevcut chat, bildirim, FCM, STOMP, okunma senkronizasyonu ve kullanici deneyimi
  korunacaktir. Degisiklik yalniz eski hesaba ait asenkron sonucun yeni hesabin
  state'ine yazilmasini engelleyecektir.
- Test karari: Geciken chat ve bildirim istekleri devam ederken cikis veya hesap
  degisimi yapildiginda eski sonucun yeni state'e yazilmadigi odakli repository
  testleriyle dogrulanmalidir. Iptal edilen isin state'i yeniden doldurmadigi ve
  yeni kullanicinin kendi verisinin normal sekilde yuklendigi de kapsanmalidir.
  Ayni davranis farkli seviyelerde gereksiz yere tekrar test edilmeyecektir.

### DEG-002 - Kayitli Kart Islem Kilidini Guvenli Kapatmak

- Durum: `KOD BEKLIYOR`
- Kayitli kart silme gibi bir mutation basladiginda
  `isMutationInProgress = true` yapilarak cift tiklama ve yinelenen backend
  istegi engellenmektedir. Basarili islemden sonra `refresh()` ayri coroutine
  baslattigi ve kilit yeniden `false` yapilmadigi icin ayni ekran acikken sonraki
  kart islemi engellenebilir.
- Mevcut `DataResult` sozlesmesi basari ve hata sonucunu degerlendirmeye devam
  edecektir. Mutation, basarili sonuc sonrasindaki liste yenilemesi ve kilidin
  kapatilmasi ayni repository-cagri coroutine'i icinde yonetilecektir.
- `isMutationInProgress` degeri basari, hata veya coroutine iptali fark etmeksizin
  guvenli bicimde kapanacak sekilde `try/finally` ile yonetilecektir.
  `CancellationException` yutulmayacak ve normal coroutine iptali korunacaktir.
- Liste yenilemesi ayri bir `viewModelScope.launch` baslatmayacak; mutation ile
  ayni coroutine icinde calisan suspend yenileme yolu kullanilacaktir. Boylece
  kilit, yenileme tamamlanmadan erken kapanmayacak ve yenileme hatasi ekranin
  kalici olarak kilitli kalmasina yol acmayacaktir.
- Cozum `TouristSavedCardsViewModel` sahipliginde dar tutulacaktir. Yeni backend
  endpoint'i, DTO, repository, use-case, base ViewModel, generic mutation
  framework'u, ekran veya navigation rotasi eklenmeyecektir.
- Mevcut kart silme davranisi, merkezi hata eslemesi ve ekran tasarimi
  korunacaktir. Degisiklik yalniz mutation state yasam dongusunu
  `IDLE -> LOADING -> SUCCESS/ERROR -> IDLE` seklinde guvenli tamamlayacaktir.
- Test karari: Basarili silme ve liste yenileme sonrasinda, repository hatasinda
  ve yenileme hatasinda `isMutationInProgress` degerinin yeniden `false` oldugu
  odakli ViewModel testiyle dogrulanmalidir. Islem surerken ikinci mutation'in
  engellendigi mevcut koruma da ayni kapsamda korunmalidir. Salt buton gorunumu
  icin gereksiz Compose veya instrumentation testi yazilmayacaktir.

### DEG-003 - Yeni Kesfet Aramasinda Eski Sonuclari Gostermemek

- Durum: `KOD BEKLIYOR`
- Turist kesfet ekraninda arama metni veya uygulanmis filtreler degistiginde
  onceki aramanin tur listesi ve sayfalama bilgileri state'te kalabilmektedir.
  Yeni ilk sayfa istegi basarisiz olursa kullanici yeni sorgunun altinda eski
  aramanin turlarini gorebilir; gec tamamlanan eski istek de daha yeni arama
  sonucunu ezebilir.
- Yeni arama kimligi olustugunda onceki sonuc listesi, sayfa numarasi, toplam
  sonuc, `canLoadMore` ve onceki aramaya ait durum bilgileri ilk sayfa isteginden
  once sifirlanacaktir. Yeni sorgu yuklenirken yeni aramaya ait loading durumu
  gosterilecektir.
- Her ilk sayfa istegi basladiginda guncel sorgu veya search-generation degeri
  yakalanacak; sonuc state'e yazilmadan once istegin halen guncel aramaya ait
  oldugu dogrulanacaktir. Gec donen eski arama sonucu sessizce yok
  sayilacaktir.
- Yeni sorgu veya filtre istegi basarisiz olursa eski tur listesi yerine mevcut
  ortak hata gorunumu, hata ikonu ve `Tekrar Dene` aksiyonu gosterilecektir.
  Hata mesaji merkezi `AppError` girisinden ve mevcut ozellik bazli
  gruplandirilmis hata eslemelerinden gelecektir; kesfet icin ikinci bir hata
  sistemi kurulmayacaktir.
- `Tekrar Dene`, ekrandaki guncel arama metni ve uygulanmis filtrelerle ayni ilk
  sayfa istegini yeniden calistiracaktir. Internet geri geldiyse yeni sonuclar
  ayni ekranda gosterilecektir.
- Ayni sorgunun sonraki sayfasi yuklenirken mevcut liste korunacaktir. Devam
  sayfasi hatasi, basariyla yuklenmis sonuclari silmeyecek ve yeni ilk sayfa
  hatasiyla karistirilmayacaktir.
- Cozum `TouristExploreViewModel` ve mevcut kesfet state'i sahipliginde dar
  tutulacaktir. Yeni backend endpoint'i, DTO, repository, use-case, ekran,
  navigation rotasi, Paging 3 gecisi veya generic pagination framework'u
  eklenmeyecektir.
- Test karari: Yeni sorguda eski sonuc ve sayfalama state'inin temizlendigi, yeni
  arama basarisiz oldugunda eski listenin gosterilmedigi, gec donen eski istegin
  guncel sonucu ezmedigi ve devam sayfasi hatasinda mevcut listenin korundugu
  odakli ViewModel testleriyle dogrulanmalidir. Ortak hata bileseninin salt
  gorunumu icin gereksiz Compose veya instrumentation testi yazilmayacaktir.

### DEG-004 - Odeme Durumu Polling'ini Sinirlamak

- Durum: `KOD BEKLIYOR`
- Odeme akisi kesinlesmemis bir sonuc dondurmeye devam ettiginde hosted odeme ve
  dogrulama ekranlarindaki polling donguleri acik bir sure veya deneme siniri
  olmadan backend'e tekrar tekrar durum istegi atabilmektedir. Ekran acik kaldigi
  surece bu dongu teorik olarak sonsuza kadar devam edebilir.
- Polling tamamen kaldirilmayacaktir. Odeme saglayicisi sonucu gecikebildigi icin
  Android mevcut `paymentId` ile backend durum endpoint'ini belirli araliklarla
  sorgulamaya devam edecek; gercek odeme basarisi yalniz backend sonucundan
  alinacaktir.
- Kullanici hosted odeme formunu doldururken gereksiz surekli sorgulama
  yapilmayacaktir. Callback veya odeme tamamlanma asamasi algilandiktan sonra
  kontrollu polling baslatilacak; WebView callback JSON'u tek basina basari
  kaniti sayilmayacaktir.
- Polling ekran ve ViewModel yasam dongusune bagli, sinirli sureli ve olculu
  backoff kullanan bir akis olacaktir. `SUCCEEDED`, `FAILED`, `CANCELLED`, iade
  veya `MANUAL_REVIEW` gibi kesin sonuc geldiginde hemen duracak; belirlenen sure
  doldugunda `TIMEOUT` durumuna gececektir.
- `TIMEOUT` durumundaki `Tekrar Dene` yeni odeme, rezervasyon, koltuk hold'u veya
  idempotency anahtari olusturmayacak; ayni `paymentId` icin durum sorgusunu
  yeniden baslatacaktir. Uygulama kapanirsa mevcut pending-payment storage ve
  recovery akisi odemeyi ayni kimlikle yeniden kontrol edecektir.
- Ayni odeme icin birden fazla polling dongusunun es zamanli calismasi
  engellenecek ve ViewModel kapandiginda ilgili coroutine iptal edilecektir.
- Cozum `HostedPaymentViewModel` ve `PaymentStatusViewModel` sahipliginde dar
  tutulacaktir. Gerekirse yalniz payment feature icinde acik sure/backoff
  sabitleri kullanilacak; yeni backend endpoint'i, DTO, tablo, navigation rotasi,
  ekran, genel polling framework'u veya realtime odeme altyapisi
  eklenmeyecektir.
- Test karari: Hosted form doldurulurken polling baslamadigi, callback sonrasinda
  basladigi, kesin sonuc gelince durdugu, sinir sonunda `TIMEOUT` olustugu,
  `Tekrar Dene` aksiyonunun ayni `paymentId` ile devam ettigi ve es zamanli ikinci
  dongunun acilmadigi odakli ViewModel testleriyle dogrulanmalidir. Test coroutine
  zamanlayicisi kullanilacak; gercek zaman bekleyen yavas ve kirilgan test
  yazilmayacaktir.

### DEG-005 - Alt Istek Hatasini Gercek Bos Listeden Ayirmak

- Durum: `KOD BEKLIYOR`
- Tur detayi basariyla yuklenirken yorum istegi basarisiz oldugunda mevcut akis
  hatayi bos yorum listesine donusturebilmektedir. Kullanici, gercekte yorumlar
  alinamis olsa bile `Henuz yorum yok` sonucunu gorebilir. Benzer sekilde public
  rehber profili yuklenirken tur onizleme istegi basarisiz olursa bu durum
  rehberin turu yokmus gibi gorunebilir.
- Ana ekran verisi ile alt bolum verisinin yukleme durumu ayrilacaktir. Tur veya
  rehber profili basariyla geldiyse ana icerik korunacak; yalniz basarisiz yorum
  ya da tur onizleme bolumunde loading, error ve retry state'i yonetilecektir.
- Gercek bos durum yalniz backend basarili cevapla bos liste dondurdugunde
  gosterilecektir. Ag, sunucu, serialization veya parsing hatasi bos listeye
  cevrilmeyecektir.
- Alt istek basarisiz oldugunda mevcut ortak hata gorunumu, hata ikonu ve
  `Tekrar Dene` aksiyonu kullanilacaktir. Hata mesaji merkezi `AppError`
  girisinden ve mevcut ozellik bazli gruplandirilmis hata eslemelerinden
  gelecektir; ikinci bir hata sistemi ya da yeni hata tasarimi kurulmayacaktir.
- `Tekrar Dene` yalniz basarisiz olan alt bolumun repository istegini yeniden
  calistiracaktir. Basariyla yuklenmis tur veya profil verisi kaybedilmeyecek ve
  gereksiz yere yeniden istenmeyecektir.
- Cozum ilgili tur detayi ve public profil ViewModel/UI state sahipliginde dar
  tutulacaktir. Mevcut repository ve `DataResult` sozlesmeleri korunacak; yeni
  backend endpoint'i, DTO, repository, use-case, ekran, navigation rotasi veya
  genel amacli loading/error framework'u eklenmeyecektir.
- Mevcut tasarim ve ana ekran davranisi korunacaktir. Degisiklik yalniz `veri
  gercekten yok` ile `veri alinamadi` anlamlarini kullaniciya dogru bicimde
  ayiracaktir.
- Test karari: Alt istek basarisiz oldugunda bos durum yerine hata gosterildigi,
  ana verinin korundugu, `Tekrar Dene` aksiyonunun yalniz ilgili istegi
  calistirdigi, sonraki basarili cevabin veriyi gosterdigi ve basarili bos
  cevabin gercek bos duruma donustugu odakli ViewModel testleriyle
  dogrulanmalidir. Hata ikonunun rengi veya Compose yerlesimi icin gereksiz UI
  testi yazilmayacaktir.

### DEG-006 - Suspend Islemlerde Coroutine Iptalini Korumak

- Durum: `KOD BEKLIYOR`
- Credential temizleme ve Firebase installation ID alma islemlerindeki suspend
  cagrilar `runCatching` ile sarildigi icin `CancellationException` normal bir
  hata gibi yakalanabilmektedir. Coroutine ekran, scope veya oturum nedeniyle
  iptal edildiginde bu kontrol sinyali yutulursa artik gerekli olmayan akis
  fallback sonucuyla sonraki adimlara devam edebilir.
- `CancellationException` uygulama hatasi olarak ele alinmayacak ve yakalandigi
  yerde yeniden firlatilarak structured concurrency davranisi korunacaktir.
  Storage, Firebase veya diger beklenen normal hatalar mevcut best-effort ya da
  fallback davranisiyla yonetilmeye devam edecektir.
- Ilgili suspend sinirlarda acik `try/catch` kullanilacaktir: once
  `CancellationException` yakalanip yeniden firlatilacak, ardindan normal
  `Exception` mevcut davranisa gore ele alinacaktir. `runCatching` projeden
  topluca kaldirilmayacak; yalniz iptal tasiyan bu suspend kullanimlar
  duzeltilecektir.
- Cozum `CredentialSessionManager` ve `AuthRepositoryImpl` icindeki iki dar
  noktada uygulanacaktir. Yeni helper, use-case, repository, coroutine
  framework'u, ekran, navigation rotasi veya backend degisikligi
  eklenmeyecektir.
- Mevcut kullanici hata mesajlari, credential temizlemenin best-effort niteligi,
  installation ID fallback'i ve auth kullanici akisi korunacaktir. Teknik
  exception UI'a veya loglara hassas veriyle sizdirilmayacaktir.
- Test karari: Suspend bagimlilik `CancellationException` verdiginde iptalin ust
  katmana iletildigi; normal storage veya installation ID hatasinda mevcut
  fallback/best-effort davranisinin korundugu hizli ve odakli coroutine unit
  testleriyle dogrulanmalidir. Ayni iptal davranisi farkli katmanlarda gereksiz
  yere tekrar test edilmeyecektir.

### DEG-007 - Modifier Sahipligini Duzeltirken Mevcut Gorunumu Korumak

- Durum: `KOD BEKLIYOR`
- Public rehber profili ve rehber cuzdan hareketleri ekranlarinda cagirandan
  gelen `Modifier` hem dis kapsayiciya hem ic icerige aktarilmaktadir. Bu modifier
  su anda varsayilan bos degerle cagrildigi icin gorunur cift padding
  olusturmamaktadir; ancak ileride padding, tiklama, `testTag` veya semantics
  eklendiginde ayni etkinin iki Compose dugumune uygulanmasi riski vardir.
- Cagirandan gelen modifier ilgili ekranin en dis Compose dugumune yalniz bir kez
  uygulanacaktir. Ic icerik gercekten modifier gerektiriyorsa kendi bagimsiz
  `Modifier` zincirini kullanacak; dis modifier ikinci kez aktarilmayacaktir.
- Turist kesfet ekraninin `TourExploreResults` ve `GuideExploreResults`
  iceriklerinde gercek cift padding bulunmaktadir. Dis seviyedeki
  `spacing_medium` 16 dp ile ic seviyedeki `spacing_medium` 16 dp birlikte
  mevcut gorunumde toplam 32 dp bosluk olusturmaktadir.
- Kesfet ekranindaki tekrar kaldirilirken tasarim daraltilmayacaktir. Mevcut
  `16 dp + 16 dp = 32 dp` gorunumu, projede zaten tanimli olan
  `R.dimen.spacing_large` 32 dp degerinin tek sahip tarafindan bir kez
  uygulanmasiyla korunacaktir. Tur ve rehber sekmeleri ayni kurali
  kullanacaktir.
- Refactor oncesinde ve sonrasinda etkili toplam bosluk karsilastirilacaktir.
  Renkler, fontlar, kart boyutlari, icerik, tiklama davranisi, navigation ve
  kullanici akisi degistirilmeyecektir. Public profil ile cuzdan ekranlarinda da
  mevcut gorunur olculer korunacaktir.
- Cozum ilgili uc ekran ve iki kesfet sonuc composable'i sahipliginde dar
  tutulacaktir. Yeni layout framework'u, ortak composable, backend endpoint'i,
  ViewModel, repository, DTO, ekran veya navigation rotasi eklenmeyecektir.
- Degisiklikten sonra bosa dusen modifier parametresi veya import varsa ayni
  kapsamda temizlenecektir. Sirf benzer gorundugu icin diger ekranlar topluca
  degistirilmeyecektir.
- Test karari: Salt padding ve yerlesim icin kirilgan otomatik UI testi
  yazilmayacaktir. `ktfmtCheck`, derleme ve lint calistirilacak; public profil,
  cuzdan hareketleri ile kesfet ekraninin tur ve rehber sekmeleri emulator veya
  fiziksel cihazda gorsel olarak kontrol edilecektir. Mevcut bir semantics ya da
  `testTag` testi etkileniyorsa yalniz ilgili test calistirilacaktir.

### DEG-008 - Ortak Network ve Feature Presentation Sahipligini Duzeltmek

- Durum: `KOD BEKLIYOR`
- Ortak `OkHttpRealtimeClient`, access token almak icin auth feature icindeki
  `AccessTokenProvider` arayuzune bagimlidir. Bu nedenle alt seviyedeki
  `common/network` altyapisi belirli bir feature olan `auth` paketini dogrudan
  tanimakta ve bagimlilik yonu `common/network -> auth/domain` olmaktadir.
- Yalniz minimal `AccessTokenProvider` sozlesmesi
  `common/network/session` sahipligine tasinacaktir. Arayuz tokenin nasil
  saklandigini, yenilendigini veya temizlendigini bilmeyecek; yalniz realtime
  network baglantisinin guncel access tokeni isteyebilmesini saglayacaktir.
- Gercek `TokenManager`, refresh token yonetimi, Android Keystore tabanli guvenli
  session storage ve giris/cikis temizligi auth feature icinde kalacaktir.
  `TokenManager` ortak `AccessTokenProvider` arayuzunu uygulayacak ve mevcut Hilt
  binding'i bu implementasyonu saglayacaktir. `TokenManager` common paketine
  tasinmayacak ve ikinci bir token arayuzu olusturulmayacaktir.
- Bu yapi ortak network katmaninin ihtiyac duydugu dar portu sahiplenmesi ve auth
  data katmaninin gercek implementasyonu saglamasi seklindeki consumer-owned port
  ve dependency inversion yaklasimini uygulayacaktir. Chat ile bildirim
  feature'lari token saklama ayrintisini bilmeyecektir.
- `TourBookingAvailabilityUi` dosyasinda checkout ekranina ait hata mesaji ile
  turist tur detayina ait mesaj eslemesi ayni reservation presentation
  paketinde bulunmaktadir. Bu durum tour presentation katmanini reservation
  presentation ayrintisina baglamaktadir.
- Checkout'a ait `checkoutErrorResId` reservation presentation paketinde
  kalacak; tur detayinin kullandigi `detailMessageResId` ise tour presentation
  altindaki ilgili turist detay paketine tasinacaktir. Her UI mesaji onu kullanan
  feature tarafindan sahiplenilecektir.
- Degisiklik dosya paketleri, importlar ve Hilt binding kapsaminda davranis
  koruyucu bir tasima olacaktir. Backend, API, DTO, ViewModel state'i,
  navigation, ekran tasarimi, STOMP/FCM davranisi ve kullanici akisi
  degismeyecektir.
- Yeni ortak framework, session manager, use-case, repository veya ikinci
  abstraction eklenmeyecektir. Tasima sonrasinda bosa dusen eski dosya, import
  veya paket varsa ayni kapsamda temizlenecektir.
- Test karari: Bu madde davranis degistirmeyen paket ve bagimlilik yonu
  refactor'udur. Yeni ve tekrarli test yazilmayacak; mevcut token saglayici,
  realtime ve booking-availability mapping testleri varsa calistirilacak.
  `ktfmtCheck`, derleme ve lint ile import, Hilt binding ve kaynak eslemelerinin
  korundugu dogrulanacaktir.

### DEG-009 - Tekrarlanan Tercih ve Resume Yenileme Davranisini Sadelestirmek

- Durum: `KOD BEKLIYOR`
- Tur satin alma ve turist cuzdanina para yukleme ViewModel'lerinde ayni tercih
  algoritmasi birebir tekrarlanmaktadir: once cihazin para birimi desteklenen
  charge para birimleri arasinda aranmakta, bulunamazsa backend'in base para
  birimi, o da bulunamazsa ilk desteklenen para birimi secilmektedir. Kural iki
  yerde ayri kalirsa gelecekte yalniz birinin guncellenmesi tutarsiz varsayilan
  secim olusturabilir.
- `CheckoutCurrencies` icin bu saf varsayilan secim algoritmasi payment
  presentation sahipliginde tek, acik isimli bir policy/helper fonksiyonuna
  alinacaktir. Backend desteklenen para birimleri ve kur konusunda otorite
  kalacak; Android yalniz cihaz locale'ine gore ilk UI tercihini belirleyecektir.
- Turist ana sayfasi, turist cuzdan ekrani ve tur satin alma ekraninda ayni `ilk
  resume olayini atla, sonraki resume olaylarinda yenile` Compose kalibi
  tekrarlanmaktadir. Bu davranis, ayni lifecycle anlamini tasiyan kucuk ve acik
  isimli bir `RefreshOnResumeAfterInitialLoad` composable effect'inde
  ortaklastirilacaktir.
- Ortak resume effect'i genel lifecycle veya refresh framework'u olmayacaktir.
  Yalniz ilk veriyi ViewModel `init` akisindan alan ve baska ekrandan geri
  donuldugunde yenilenmesi gereken ekranlarda kullanilacaktir.
- `GuideMyWalletViewModel` ilk olusturuldugunda `refresh()` cagirirken
  `GuideMyWalletScreen` de `LaunchedEffect(Unit)` ile ayni yenilemeyi yeniden
  baslatabilmektedir. Aktif job korumasi es zamanli istegi cogunlukla engellese
  de ilk istek hizli biterse ikinci gereksiz backend istegi olusabilir.
- Rehber cuzdaninin ilk yuklemesi yalniz ViewModel `init` sorumlulugunda
  kalacaktir. Ekrandaki dogrudan ilk acilis yenilemesi kaldirilacak; ekrana geri
  donuste yenileme gerekiyorsa ayni `RefreshOnResumeAfterInitialLoad` davranisi
  kullanilacaktir. Mevcut finance ve bildirim kaynakli realtime yenilemeler
  korunacaktir.
- Degisiklik payment presentation, ortak UI effect'i ve ilgili ekran/ViewModel
  kullanimlariyla sinirli olacaktir. Yeni backend endpoint'i, DTO, repository,
  use-case, base ViewModel, navigation rotasi, ekran veya genel amacli refresh
  manager eklenmeyecektir.
- Tasarim, para birimi secenekleri, odeme otoritesi ve kullanici akisi
  degismeyecektir. Tekrarlanan kod, bosa dusen locale importlari ve eski resume
  state degiskenleri ayni kapsamda temizlenecektir.
- Test karari: Saf para birimi tercih fonksiyonu; cihaz para birimi
  desteklendiginde onu, desteklenmediginde base para birimini, ikisi de yoksa ilk
  degeri sectigi ve bos listede `null` dondurdugu hizli unit testleriyle
  dogrulanmalidir. Resume effect'inin Compose implementation ayrintisi icin agir
  ve kirilgan UI testi yazilmayacak; mevcut yenileme testleri, derleme ve ilgili
  kullanici akisi kontrol edilecektir.

### DEG-010 - Resource, Lint ve Gorsel Varlik Temizligi

- Durum: `KOD BEKLIYOR`
- Kullanim taramasinda gercekten referanssiz oldugu dogrulanan
  `preview_popular_tours_title` ve `error_tour_step3_invalid` string
  kaynaklari kaldirilacaktir. Runtime, reflection, DI veya baska resource
  tarafindan kullanilma ihtimali kanitlanmadan hicbir kaynak silinmeyecektir.
- `PopularTourCard` icindeki kullaniciya gorunen hardcoded `Rehber` metni XML
  string kaynagina alinacaktir. Rehber adi zaten ayni kartta metin olarak
  verildigi icin ona eslik eden dekoratif profil gorselinin tekrarlayan
  `contentDescription` degeri `null` yapilabilir. Projedeki butun
  `contentDescription` degerleri topluca `null` yapilmayacaktir; dekoratif
  gorseller `null`, tiklanabilir veya bilgi tasiyan ogeler ise XML kaynakli ve
  anlamli aciklamalar kullanacaktir.
- Cuzdan/odeme kaynaklarinda belirlenen Turkce yazim hatasi yalniz ilgili XML
  metninde duzeltilecektir. Font, renk, boyut, yerlesim ve ekran davranisi
  degismeyecektir.
- Kullanici fotografi normalizasyonundaki platform
  `android.media.ExifInterface`, bakimi suren AndroidX
  `androidx.exifinterface.media.ExifInterface` sinifina gecirilecektir. Mevcut
  kamera fotografi dondurme, yeniden boyutlandirma, sikistirma ve yukleme boyutu
  kurallari aynen korunacaktir.
- Daha once profesyonel hale getirilen 1x ve 2x kamera fotografi yukleme akisi
  degismeyecektir. Buyuk fotograflarin uygun boyuta normalize edilerek backend
  sinirinin altinda yuklenebilmesi korunacak; bu madde yeni upload limiti veya
  farkli sikistirma davranisi getirmeyecektir.
- Compose lint tarafinda dogrulanan `ModifierParameter` uyarilari, composable
  parametrelerini standart siraya getirerek giderilecektir. Isimli parametreli
  cagrilar ve gerekiyorsa diger cagrilar guvenli bicimde guncellenecek; UI
  davranisi degismeyecektir.
- Uygulamayla paketlenen onboarding ve benzeri bitmap dosyalari, kullanici
  tarafindan yuklenen fotograflardan ayri ele alinacaktir. Her gorsel kullanimina
  gore uygun density veya `drawable-nodpi` konumunda tutulacak; buyuk PNG'ler
  gorunur kaliteyi bozmayacak bicimde WebP'ye donusturulebilecektir. Resource
  kimlikleri ve ekrandaki gorsel olculer korunacaktir.
- Renkler ortak kaynaga yalniz ayni hex degerine, ayni UI anlamina ve birden
  fazla gercek kullanima sahipse alinacaktir. Ayni hex fakat farkli anlamdaki,
  farkli hex fakat benzer gorunen veya yalniz tek bir ozel/dekoratif kullanimda
  bulunan renkler zorla ortaklastirilmayacaktir.
- `brand_color`, hata, bekleme, yildiz, divider, yuzey ve diger mevcut renklerin
  gorunen tonlari degistirilmeyecektir. Farkli yildiz veya pozitif durum tonlari
  tasarim karari olmadan birlestirilmeyecek; yeni tema, dynamic color ya da gece
  modu davranisi eklenmeyecektir.
- Tekrarlanan `380.dp` degerleri yalniz gercekten ayni maksimum icerik genisligi
  politikasini ifade ediyorsa `content_max_width` gibi acik isimli tek dimens
  kaynagina alinacaktir. Tesadufen ayni sayiyi kullanan farkli tasarim amaclari
  zorla ortaklastirilmayacaktir.
- Degisiklikler resource, import, composable imzasi, AndroidX EXIF ve paketlenen
  gorsel varliklarla sinirli olacaktir. Backend, API, DTO, repository,
  navigation, kullanici akisi ve ekran tasarimi degistirilmeyecektir. Bosa dusen
  eski import ve kaynaklar ayni kapsamda temizlenecektir.
- Test karari: Salt metin, renk, parametre sirasi veya dimens tasimasi icin
  gereksiz unit/UI testi yazilmayacaktir. `ktfmtCheck`, derleme, lint ve APK
  resource islemesi calistirilacak; onboarding, populer tur karti ve ilgili
  ekranlar emulator/fiziksel cihazda onceki gorunumle karsilastirilacaktir.
  AndroidX EXIF gecisinde mevcut fotograf normalizasyon testleri calistirilacak;
  gerekirse yalniz gercek yon dondurme riskini kapsayan odakli test
  eklenecektir.

### DEG-011 - Tur Yayinlama ViewModel Sorumluluklarini Ayirmak

- Durum: `KOD BEKLIYOR`
- `GuideTourPublishViewModel` 493 satirda ekran state'i, kullanici aksiyonlari,
  adim dogrulamasi, backend field-error eslemesi, request input donusumu, tarih
  ve para cevirileri ile yayinlama orkestrasyonunu ayni dosyada tasimaktadir.
  Sorun yalniz satir sayisi degil; ViewModel state'inden bagimsiz iki saf
  sorumlulugun ayni sinifta bulunmasidir.
- Adim dogrulamalari, `TourPublishValidationError`, ilk hata bulma, icerik
  dogrulamasi, backend `fieldErrors` degerlerini ilgili yayinlama adimi ve XML
  hata kaynagina esleme ile dogrulama sabitleri ayni publish paketindeki
  `GuideTourPublishValidation.kt` dosyasina tasinacaktir.
- `GuideTourPublishUiState` degerini `CreateGuideTourInput` modeline donusturme,
  tarih ve saati `Instant` degerine cevirme, para minor-unit donusumu, kapasite
  donusumu ve metin normalizasyonu ayni publish paketindeki
  `GuideTourPublishInputMapper.kt` dosyasina tasinacaktir.
- Validator ve mapper state degistirmeyen saf `internal` fonksiyonlar olarak
  kalacaktir. Hilt ile enjekte edilen yeni sinif, interface, manager, factory,
  use-case veya tek dosyalik alt paket olusturulmayacaktir.
- `GuideTourPublishViewModel`; `StateFlow` ve draft state yonetimi, kullanici alan
  aksiyonlari, dogrulama sonucunu UI state'ine yazma, profil bilgisini birlestirme,
  loading/basari/hata gecisleri, coroutine ve ekran orkestrasyonunun sahibi
  olmaya devam edecektir.
- Medya yukleme, tur olusturma ve sahipsiz medya temizleme orkestrasyonunun
  use-case'e tasinip tasinmayacagi `DEG-012` kapsaminda ayrica
  kararlastirilacaktir. Bu madde tek basina bu davranisin katmanini
  degistirmeyecektir.
- Degisiklik kod tasimasi ve sorumluluk ayrimi olacaktir. Backend, API, DTO
  sozlesmesi, ekran tasarimi, navigation, tur yayinlama adimlari, hata mesajlari
  ve kullanici akisi birebir korunacaktir. Bosa dusen import veya ozel fonksiyon
  ayni kapsamda temizlenecektir.
- Test karari: Saf validator; konum/zaman, fiyat/kapasite, baslik/aciklama/kapak
  ve backend field-error eslemelerini odakli unit testlerle dogrulayacaktir. Saf
  mapper; trim, minor unit, tarih-saat/Instant, kapasite ve gecerli
  `CreateGuideTourInput` uretimini test edecektir. Ayni kurallar ViewModel
  testlerinde tekrar edilmeyecek; ViewModel testleri yalniz state ve
  orkestrasyon davranisini koruyacaktir.

### DEG-012 - Gercek Cok Adimli Islemleri Use-Case Sinirina Almak

- Durum: `KOD BEKLIYOR`
- Use-case karari dosya uzunluguna, repository sayisina veya bir repository'nin
  kac ViewModel tarafindan kullanildigina gore verilmeyecektir. Birden fazla
  ViewModel'in ayni basit repository metodunu cagirmasi use-case gerektirmez;
  bir ViewModel'in birden fazla repository'den birbirinden bagimsiz ekran
  verileri almasi da tek basina use-case nedeni degildir.
- Use-case yalniz cagri adimlari tek bir kullanici niyetini ve is akisini
  olusturuyorsa kullanilacaktir. Adimlarin sirasi onemliyse, birden fazla
  repository veya dis adapter birlikte koordine ediliyorsa, basarisizlikta geri
  alma/temizleme gerekiyorsa ya da UI'dan bagimsiz korunup test edilmesi gereken
  gercek bir is kurali varsa use-case anlamli kabul edilecektir.
- Uygulama genelindeki oturum sonlandirma akisi; bildirim yonlendirmesini
  temizleme, sistem bildirimlerini kapatma, auth oturumunu sonlandirma, yerel
  bildirim state'ini ve bekleyen odemeyi temizleme adimlarini tek bir uygulama
  islemi olarak yonetecektir. Bu islem auth feature'ina ait basit logout cagrisi
  olmadigi icin app/application session sahipligindeki dar bir
  `TerminateUserSessionUseCase` sinirina alinacaktir. Bir temizleme adiminin
  hatasi diger guvenlik temizlemelerinin atlanmasina yol acmayacaktir.
- Profil fotografi guncelleme akisi; medyayi yukleme, avatar kaydini backend'de
  guncelleme, basarisizlikta sahipsiz medyayi silme ve basarida kullanici
  onbellegini yenileme adimlarini `UpdateUserAvatarUseCase` altinda
  koordine edecektir. `UserAvatarRepositoryImpl` yalniz avatar veri erisimi
  sorumluluguna daraltilacak; media ve user repository orkestrasyonu data
  repository implementation'i icinde daginik kalmayacaktir.
- Tur yayinlama akisi; kapak medyasini yukleme, dogrulanmis tur girdisiyle turu
  olusturma ve olusturma basarisizsa yuklenen sahipsiz medyayi silme adimlarini
  `PublishGuideTourUseCase` altinda yonetecektir. ViewModel, UI state'i,
  kullanici aksiyonlari, dogrulama sonucu ve navigation sahibi olmaya devam edecek;
  use-case Compose, `StringRes` veya presentation state'i bilmeyecektir.
- Tur icerik degisikligi gonderme akisi; yeni kapak secildiyse medyayi yukleme,
  degisiklik istegini gonderme ve istek basarisizsa yeni medyayi temizleme
  adimlarini `SubmitGuideTourContentChangeUseCase` altinda koordine edecektir.
  ViewModel dirty-check, kismi icerik/session basarisi, UI state'i ve hedef sekme
  davranisinin sahibi olarak kalacaktir.
- Feature'a ait use-case'ler ilgili feature'in `domain/usecase` sahipliginde
  bulunacaktir. Birden fazla feature'i koordine eden oturum sonlandirma islemi
  auth ya da common paketine zorla yerlestirilmeyecek; uygulama kompozisyon
  sinirinda tutulacaktir. Use-case'ler constructor injection kullanacak, yalniz
  bunun icin gereksiz Hilt module, base use-case, generic workflow motoru,
  manager veya factory eklenmeyecektir.
- Login, register, tek repository'den veri okuma, basit CRUD, sayfalama veya
  yalniz repository metodunu tekrar eden iki satirlik islemler icin use-case
  eklenmeyecektir. Bu madde eski ve gereksiz delegasyon use-case'lerini geri
  getirmeyecektir.
- Degisiklik mevcut API/DTO sozlesmesini, ekran tasarimini, navigation'i ve
  kullanici davranisini degistirmeyecektir. Amac cok adimli is akisini tek
  sorumluluk altinda toplamak, telafi adimlarinin unutulmasini engellemek ve
  ViewModel ile data repository implementation'larini kendi katman
  sorumluluklarinda tutmaktir.
- Test karari: Oturum sonlandirmada bir adim hata verse bile gerekli yerel
  temizlemelerin surdugu; avatar guncellemede upload/API/cache sirasi ve API
  hatasinda medya temizligi; tur yayinlama ile icerik degisikliginde upload
  basarisizsa sonraki istegin atilmadigi, ana istek basarisizsa yeni medyanin
  silindigi ve basarida medyanin korundugu odakli use-case testleriyle
  dogrulanacaktir. Basit repository delegasyonlari icin gereksiz test veya
  use-case yazilmayacaktir.

### DEG-013 - Tur Duzenleme State Donusumunu Ayrastirmak

- Durum: `KOD BEKLIYOR`
- `GuideTourEditViewModel` icindeki `setInitialState()` hem tur detayini
  repository'den alma akisinin sonucunu yonetmekte hem de `TourDetails`
  nesnesini uzun bir sekilde `GuideTourEditUiState` degerine cevirmektedir.
  Sorun yalniz dosyanin uzunlugu degildir; UI state'i olusturan saf donusum ile
  veri yukleme ve hata akisinin ayni sorumlulukta bulunmasidir.
- `TourDetails -> GuideTourEditUiState` donusumu mevcut
  `GuideTourEditStateMapper.kt` dosyasinda acik isimli, state degistirmeyen bir
  mapper fonksiyonuna alinacaktir. Tur ve session alanlari, ilgili zaman
  diliminde tarih/saat donusumu, fiyat ve kapasite gorunum degerleri ayni
  donusum sinirinda kalacaktir.
- Mapper session bulunamadiginda UI mesaji veya navigation karari vermeyecek;
  bu durum icin `null` veya acik bir donusum sonucu kullanilabilecektir.
  Session bulunamadi hatasini kullanici state'ine yazmak, `originalState` ve
  `originalApprovalStatus` degerlerini saklamak ViewModel'in sorumlulugunda
  kalacaktir.
- ViewModel repository cagrisi, basari/hata yonetimi, loading state'i,
  kullanici aksiyonlari, dirty-check, medya islemleri, kismi basari ve
  navigation orchestration sahibi olmaya devam edecektir. Yalniz saf state
  kurma kodu mapper'a tasinacak; mevcut `hasChangesFrom`, content/session
  input mapper ve diger ilgili fonksiyonlar gereksiz yere yeniden
  yazilmayacaktir.
- Yeni ekran, navigation rotasi, backend endpoint'i, DTO, repository, use-case,
  Hilt binding veya tek dosyalik alt paket eklenmeyecektir. Mevcut edit
  feature'inin ayni presentation paketindeki mapper kullanilacaktir.
- Tasarim, form alanlari, tarih/saat gosterimi, dirty-check, kaydetme akisi,
  hata mesajlari ve kullanici deneyimi degismeyecektir. Bu davranis-koruyucu
  bir sorumluluk refactor'udur; ViewModel yapay olarak kucuk parcalara
  bolunmeyecektir.
- Test karari: Saf mapper'in basarili `TourDetails` verisini dogru UI state'e
  cevirdigi, session zaman diliminde tarih/saatin korundugu, fiyat/kapasite,
  dil ve kapak bilgilerinin kaybolmadigi odakli unit testlerle
  dogrulanacaktir. Session bulunamama ve hata state'i ViewModel testlerinde,
  dirty-check kurallari ise mevcut mapper/ilgili test seviyesinde bir kez
  dogrulanacak; ayni donusum gereksiz yere tekrar test edilmeyecektir.

### DEG-014 - Kesfet Arama Sorgusu Donusumunu Ayrastirmak

- Durum: `KOD BEKLIYOR`
- `TouristExploreViewModel` arama metnini ve filtre state'ini yonetmenin yaninda
  `ExploreUiState` degerini domain katmaninin kullandigi `TourSearchQuery`
  modeline cevirmekte, ayrica fiyat araligini minor unit'e donusturmektedir.
  Sorun bu kodun yanlis olmasi degil; presentation state'ten domain arama
  modeline yapilan saf donusumun arama lifecycle'i ve sayfalama akisiyla ayni
  sinifta bulunmasidir.
- `ExploreUiState -> TourSearchQuery` donusumu kesfet feature'inin presentation
  sahipligindeki acik isimli bir mapper dosyasina alinacaktir. Mapper; metin,
  ulke, sehir, kategori, dil, minimum rating, minimum/maximum fiyat ve mevcut
  varsayilan filtre kurallarini aynen koruyarak domain query olusturacaktir.
- Fiyat alanlarinin minor unit'e cevrilmesi de ayni saf donusum sinirinda
  tutulacaktir. Backend para, kur ve arama sonucunun otoritesi olmaya devam
  edecek; Android yalniz kullanicinin UI filtresini mevcut arama sozlesmesine
  uygun query alanlarina cevirecektir.
- ViewModel; arama metnini izleme, debounce, filtre duzenleme/uygulama/iptal,
  sayfalama, istek iptali, loading/error/empty state, retry ve repository
  cagrisi sorumluluklarini tasimaya devam edecektir. Yalniz saf query olusturma
  kodu mapper'a alinacak; arama davranisi degistirilmeyecektir.
- Mapper presentation state kullandigi icin domain paketine tasinmayacak;
  kesfet feature'inin mevcut presentation paketinde bulunacaktir. Yeni
  repository, use-case, pagination framework'u, navigation rotasi, ekran veya
  backend endpoint'i eklenmeyecektir.
- Bu degisiklik mevcut filtrelerin secimini, minimum rating anlami, fiyat
  sinirlarini, arama sonucunu, kart tasarimini ve kullanici akislarini
  degistirmeyecektir. ViewModel yapay olarak birden fazla sinifa veya dosyaya
  bolunmeyecek; yalniz bagimsiz ve tekrar test edilebilir donusum ayrilacaktir.
- Test karari: Saf mapper'in metin, ulke, sehir, kategori, dil ve minimum rating
  filtrelerini dogru query alanlarina tasidigi; fiyat araliginin minor unit'e
  dogru cevrildigi; varsayilan veya bos filtrelerin mevcut sozlesmeye uygun
  `null`/bos deger urettigi odakli unit testlerle dogrulanacaktir. Arama
  lifecycle'i ve sayfalama testleri ViewModel kapsaminda kalacak; ayni mapping
  davranisi iki farkli test seviyesinde tekrar edilmeyecektir.
