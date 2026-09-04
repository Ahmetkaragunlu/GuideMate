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

## Calisma Kurali

- Kullanici degisiklikleri arka arkaya soylerken kod yazilmaz.
- Her istek bu dosyaya sirasi korunarak ayri bir madde olarak kaydedilir.
- Yeni madde onceki maddeyle celisiyorsa son kullanici karari esas alinir ve
  celiski acikca not edilir.
- Kullanici acikca `uygula` demeden Android veya backend kodu degistirilmez.
- Uygulama komutu geldiginde bekleyen maddeler birlikte analiz edilir ve
  birbirini etkileyen degisiklikler tek tutarli kapsam halinde uygulanir.
- Yalniz kullanicinin istedigi alanlar degistirilir. Zorunlu baglantili
  duzeltmeler varsa uygulamadan once listede belirtilir.
- Tasarim, kullanici akisi ve mevcut davranis istenmedikce degistirilmez.
- Kod; mevcut mimari, feature sahipligi, SOLID, bagimlilik yonu, ortak yapi,
  isimlendirme, okunabilirlik ve test edilebilirlik kurallarini korur.
- Kullanilmayan kod, import, dosya veya paket yalniz yapilan degisiklik nedeniyle
  kesin olarak bosa dustuyse temizlenir.
- Toplu uygulama sonrasinda ilgili otomatik kontroller calistirilir ve etkilenen
  kullanici testleri `docs/kullanici-testleri.md` dosyasindan belirlenir.

## Durumlar

- `BEKLIYOR`: Kullanici istedi, henuz uygulanmadi.
- `NETLESTIRILECEK`: Karar veya kapsam tamamlanmadi.
- `ONAYLANDI`: Kapsam kesinlesti, toplu uygulama komutu bekleniyor.
- `UYGULANDI`: Kod degisikligi tamamlandi.
- `DOGRULANDI`: Ilgili otomatik ve kullanici testleri gecti.
- `IPTAL`: Kullanici maddeden vazgecti veya daha yeni kararla degistirdi.

## Degisiklikler

### DEG-001 - Cikis Onayi ve Kullanici Oturumu Temizligi

- Durum: `DOGRULANDI`
- Kaynak: Topbar cikis ikonu
- Kullanici istegi:
  - Cikis ikonuna basinca kullanici dogrudan cikis yapmamali.
  - `Cikmak istiyor musunuz?` anlaminda bir onay dialogu acilmali.
  - Yalniz olumlu aksiyona basinca cikis yapilmali.
  - Vazgecme, disariya dokunma veya geri tusu cikis islemi baslatmamali.
  - Dialogda `Evet` ve `Hayir` secenekleri bulunmali.
  - `Evet` yikici cikis islemi oldugu icin
    `MaterialTheme.colorScheme.error` ile kirmizi gosterilmeli.
  - `Hayir` mevcut tema/brand ile uyumlu varsayilan TextButton rengini
    kullanmali; kirmizi olmamali.
- Ortak kullanim:
  - Ayni topbar hem rehber hem turist akisini etkiledigi icin tek ortak dialog/
    callback sinirindan uygulanmali; iki role ayri kopya yazilmamali.
  - Mevcut ortak `EditAlertDialog` tasarimi uygun oldugu surece yeniden
    kullanilmali; sirf cikis icin ikinci bir genel dialog altyapisi acilmamali.
  - Projedeki rezervasyon iptali gibi mevcut yikici dialoglarla ayni renk ve
    buton semantigi korunmali.
- Guncel kodda dogrulanan davranis:
  - `AppTopBar` logout ikonu su anda dogrudan `onLogoutClick` cagiriyor; onay
    adimi yok.
  - `AuthRepositoryImpl.logout()` access/refresh tokenlari once bellekte aliyor,
    sonra local tokenlari, cached user/role/avatar bilgisini ve Credential
    Manager durumunu temizliyor.
  - Backend logout basarili olursa refresh session revoke ediliyor ve mevcut
    installation icin FCM device registration pasiflestiriliyor.
  - `RootNavigationViewModel` notification local state'ini temizliyor.
  - Chat repository userId null olunca realtime baglantiyi kesiyor,
    conversation/message/unread cache'ini temizliyor.
  - Role graph degistigi icin role-scoped ViewModel/UI state'leri yasam
    dongusuyle kaldiriliyor.
- Tamamlanmasi gereken oturum izolasyonu:
  - `PendingPaymentStorage` icindeki kullaniciya ait bekleyen payment ID cikista
    temizlenmeli; sonraki hesaba tasinmamali. Backend payment kaydi silinmez ve
    canonical history/status korunur.
  - `NotificationNavigationCoordinator` icindeki bekleyen hedef cikista
    temizlenmeli; yeni giris yapan hesabi eski bildirime yonlendirmemeli.
  - Guide profile cache mevcut durumda userId ile filtrelendigi icin baska
    hesaba veri sizdirmiyor. Sirf temizleme metodu eklemek icin gereksiz katman
    acilmamali; toplu uygulamada tekrar dogrulanmali.
- Bilincli olarak korunacak cihaz/uygulama verileri:
  - Onboarding tamamlandi bilgisi kullaniciya degil kuruluma ait oldugu icin
    cikista silinmemeli.
  - Installation ID cihaza/kuruluma ait oldugu icin silinmemeli. Backend eski
    kullanici bagini pasiflestirir; yeni login ayni kurulumu yeni kullaniciya
    yeniden kaydeder.
  - Tur, rezervasyon, mesaj, wallet ve profil gibi backend verileri silinmez;
    logout hesap silme degil, yerel oturumu kapatma islemidir.
- Offline siniri:
  - Ag yoksa local oturum yine guvenli sekilde kapanir. Backend refresh session
    o anda revoke edilemeyebilir ve kendi suresi dolana kadar sunucuda kalabilir;
    bu nedenle durum kullaniciya cikis basarisizligi gibi yansitilmaz.
- Karar: Dialog ve iki local oturum temizligi kullanici tarafindan onaylandi.
- Uygulama sonucu: Ortak cikis dialogu ile pending payment ve notification
  target temizligi uygulandi; otomatik test ve kalite kapilari gecti.
- Dogrulama:
  - Dialog olumlu/olumsuz/geri/disariya dokunma ve hizli cift tik davranisi.
  - Rehber ve turist cikisi.
  - Token, cached user, Google credential, notification, chat, pending payment
    ve pending notification target temizligi.
  - Guide A -> Guide B ve Tourist A -> Tourist B hesap izolasyonu.
  - `docs/kullanici-testleri.md` icindeki `AUTH`, `ROOT`, `ISOLATION`,
    `SECURITY`, `PROCESS` ve `NAV` senaryolari.

### DEG-002 - Puanlarin Tek Ondalik Basamakla Gosterilmesi

- Durum: `DOGRULANDI`
- Kullanici istegi:
  - Turist popular tur kartinda `4.96777` gibi cok basamakli puan
    gosterilmemeli.
  - Puan kullaniciya `4.8` gibi noktadan/ondalik ayiracindan sonra tek basamakla
    gosterilmeli.
  - Uygulamada ortalama puanin gorundugu diger yerler de ayni kurali kullanmali.
- Dogrulanan neden:
  - Backend ortalama puanin hassas `Double` degerini dogru sekilde gonderiyor.
  - Android'de bazi presentation kodlari bu degeri dogrudan `toString()` ile
    metne ceviriyor; bu nedenle backend hassasiyetinin tum basamaklari UI'a
    siziyor.
- Dogrulanan hatali kullanimlar:
  - Tourist popular tour karti: `PopularTourMapper` icinde
    `averageRating?.toString()`.
  - Guide home ortalama puan karti: `GuideStatistic` icinde
    `averageRating.toString()`.
  - Guide own/public profil istatistigi: `ProfileStatsRow` icinde
    `rating.toString()`.
- Zaten dogru olan kullanimlar:
  - Tur arama sonucu, tur detayi, upcoming/past trip, guide active/past tour ve
    guide search kartlari su anda bir ondalik basamak kullaniyor.
  - Review girisindeki 1-5 tam yildiz puani bu degisiklikten etkilenmemeli.
- Uygulama karari:
  - Backend, DTO ve domain degeri yuvarlanmamalidir. Siralama, seviye ve ortalama
    hesaplari tam hassasiyetle devam etmelidir.
  - Yuvarlama yalniz presentation/UI sinirinda ve tum ekranlarda ayni ortak
    formatlama kuralindan yapilmalidir.
  - Ayni `String.format`/`toString` kodu farkli ekranlara kopyalanmamalidir.
    Mevcut `common/ui/formatting` sahipligi veya uygun ortak string format
    kaynagi kullanilmalidir.
  - Format locale-aware olmalidir: ornek deger Ingilizce locale'de `4.8`,
    Turkce locale'de `4,8` gorunebilir. Her iki durumda da tek ondalik basamak
    kurali korunur.
  - Puani olmayan kayit mevcut tasarimdaki `-`/gizleme davranisini korumalidir;
    sahte `0.0` puan uretilmemelidir.
- Uygulama sonucu: Locale-aware ortak puan formatter'i eklendi; popular tur,
  rehber ana sayfa, rehber profil ve rehber sonuc kartlari ayni tek ondalik
  sunum kuralina baglandi. Backend/domain hassasiyeti ve puansiz kayitlarin `-`
  davranisi korundu; formatter ve mapper testleri gecti.
- Dogrulama:
  - Popular tour, guide home, own guide profile ve tourist public guide profile.
  - Search, detail, upcoming/past trip ve guide tour kartlarinda regresyon.
  - Puansiz ve 1/5/4.95 gibi sinir degerleri.
  - Turkce/Ing locale ve buyuk font gorunumu.

### DEG-003 - Merkezi Gson Instant ve LocalDate Donusumu

- Durum: `DOGRULANDI`
- Kullanici tarafindan gorulen davranis:
  - Tourist notification badge sayisi gorunuyor ancak notification listesi
    `Bir hata olustu` sonucuna dusuyor.
  - Chat listesi de ayni genel hata sonucuna dusuyor.
- Dogrulanan kok neden:
  - Backend `Instant` degerlerini ISO-8601 metni, `LocalDate` degerlerini ISO
    tarih metni olarak JSON response'a yaziyor.
  - Android `NetworkModule` yalniz duz `Gson()` olusturuyor; `Instant` ve
    `LocalDate` icin serializer/deserializer kaydetmiyor.
  - Notification ve chat DTO'lari JSON zaman metnini dogrudan `Instant` alana
    donusturmesini Gson'dan bekliyor.
  - Notification unread-count response'unda zaman alani olmadigi icin sayac
    calisabilirken liste parse asamasinda hata verebiliyor.
- Etkilenen alanlar:
  - Notification history/read response'lari.
  - Chat conversation ve message response'lari.
  - Payment quote/status response'larindaki `Instant` ve `LocalDate` alanlari.
  - Tourist/guide wallet transaction zamanlari.
  - Guide earning, banka hesabi ve withdrawal zamanlari.
- Zaten farkli ve calisan sinir:
  - Tour, reservation ve review response'larinin bir bolumu tarihi `String`
    alip mapper'da `Instant.parse()` kullaniyor. Merkezi adapter bunlarin String
    alanlarini degistirmemeli veya mevcut davranisini bozmamali.
- Uygulama karari:
  - Ortak network serialization sahipliginde ISO-8601 `Instant` ve ISO
    `LocalDate` adapter'lari olusturulmali.
  - Adapter'lar merkezi `GsonBuilder` uzerinden tek Gson instance'ina
    kaydedilmeli; feature'lara ayri ayri kopya donusum yazilmamali.
  - Backend JSON sozlesmesi, DTO/domain zaman tipi ve kullaniciya gosterilen
    saat/tarih formatlamasi birbirine karistirilmamali.
  - Bu degisiklik timezone/ulkeye gore saat hesaplama degil, JSON metnini zaman
    nesnesine donusturme sorumlulugudur.
  - Gecersiz tarih verisi bos liste/fallback ile gizlenmemeli; merkezi network
    hata sinirina kontrollu bicimde donmelidir.
- Test borcunun nedeni ve kapanisi:
  - Mevcut fake repository/mapper testleri Retrofit Gson converter katmanini
    calistirmadigi icin hata daha once yakalanmadi.
  - Gercek backend bicimine uygun ISO JSON fixture ile `Instant`, nullable
    `Instant` ve `LocalDate` deserialize testleri eklenmeli.
  - Gerekli serializer yonu da ayni adapter sozlesmesinde round-trip veya request
    fixture ile dogrulanmali.
  - Notification/chat DTO parse testi ile payment/wallet/finance tarih alanlari
    icin en azindan temsilci contract testleri calistirilmali.
- Kod kapsami:
  - Ortak zaman adapter dosyasi/dosyalari.
  - Merkezi `NetworkModule` Gson kaydi.
  - Odakli serialization/contract testleri.
  - Feature bazinda gereksiz mapper veya DTO tekrarina gidilmemeli.
- Uygulama sonucu: Merkezi Gson adapter'lari ve temsilci DTO contract testleri
  eklendi; String tabanli tarih alanlari korunarak kalite kapilari gecti.
- Dogrulama:
  - Notification count/list/read/pagination.
  - Chat list/history/send/read ve zaman gosterimi.
  - Payment quote/status/recovery.
  - Tourist/guide wallet transaction history.
  - Guide earnings/bank accounts/withdrawals.
  - Tour/reservation/review tarih regresyonu.

### DEG-004 - Onboarding Sonrasi Kayit Ekranina Yonlendirme

- Durum: `DOGRULANDI`
- Kullanici istegi:
  - Uygulamayi ilk kez acip onboarding akisini tamamlayan kullanici `SignIn`
    yerine `SignUp` ekranina yonlendirilmelidir.
- Dogrulanan mevcut davranis:
  - `AuthNavGraph` onboarding tamamlandiginda `AuthDestination.SignIn` hedefine
    gidiyor ve onboarding destination'ini geri yigindan siliyor.
- Kullanici deneyimi karari:
  - Ilk kurulum/onboarding sonrasi birincil beklenti yeni hesap olusturmak
    oldugu icin varsayilan hedef `SignUp` olmalidir.
  - Mevcut hesabi olan kullanici SignUp ekranindaki `Giris Yap` aksiyonuyla
    SignIn ekranina gecebilmelidir.
- Navigasyon karari:
  - Onboarding, SignUp ve SignIn ayni Auth graph icinde oldugu icin burada
    `switchRoot` kullanilmayacaktir. Uygulamanin auth/guide/tourist root gecisleri
    ve mevcut root navigation davranisi degismeyecektir.
  - Onboarding tamamlanma kaydi yine once kalici olarak yazilacak, onboarding
    geri yigindan `inclusive` bicimde silinecek ve geri tusuyla yeniden
    onboarding'e donulmeyecektir.
  - Yalniz onboarding hedefini SignUp yapmak yeterli degildir. SignUp ekraninin
    mevcut `popBackStack(SignIn)` islemi, onboarding'den dogrudan gelindiginde
    stack'te SignIn bulunmadigi icin calismayabilir.
  - SignUp ekranindaki `Giris Yap` aksiyonu iki giris yolunda da guvenli
    calismalidir: SignIn'den SignUp'a gecildiyse mevcut SignIn'e donmeli;
    onboarding'den dogrudan gelindiyse yeni ve tekil SignIn hedefine gitmelidir.
  - Gereksiz yeni graph, route veya navigation extension olusturulmayacaktir.
- Tasarim ve isleyis siniri:
  - Onboarding, SignUp ve SignIn ekran tasarimlari degismeyecektir.
  - Kayit, e-posta dogrulama, rol secimi ve oturum acma akislarina
    dokunulmayacaktir.
- Uygulama sonucu: Onboarding hedefi SignUp yapildi; SignUp'tan SignIn'e donus
  iki giris yolu icin de guvenli hale getirildi.
- Dogrulama:
  - Ilk kurulum -> onboarding -> SignUp.
  - SignUp -> Giris Yap -> SignIn.
  - SignIn -> SignUp -> Giris Yap -> ayni SignIn'e donus.
  - SignUp ekraninda geri tusu ve onboarding'e geri donmeme davranisi.
  - Uygulama yeniden acildiginda onboarding'in tekrar gosterilmemesi.
  - Basarili auth sonrasi mevcut role selection/root gecisleri.

### DEG-005 - Filtre Draft'ini Geri Cikista Iptal Etme

- Durum: `DOGRULANDI`
- Dogrulanan mevcut davranis:
  - Tourist Explore ile Filter ekrani ayni `TouristExploreViewModel` state'ini
    kullaniyor.
  - Filter alanlari `draftFilters` uzerinde degisiyor ve yalniz `Uygula` ile
    `appliedFilters` haline geliyor; bu kisim dogru.
  - Kullanici `Uygula` demeden sistem geri tusu veya top bar geri aksiyonuyla
    cikarsa canonical sorgu degismiyor fakat vazgectigi draft secimler ViewModel
    icinde kaliyor. Filter yeniden acilinca bu secimler tekrar gorunuyor.
- Kullanici deneyimi karari:
  - Ekran rotasyonu sirasinda devam eden filter draft'i korunmalidir.
  - `Uygula` secilirse draft canonical/applied filtreye donusmeli ve Explore
    sorgusu yenilenmelidir.
  - Kullanici geri cikarsa draft iptal edilmeli; tekrar acilista son uygulanmis
    filtreler gorunmelidir.
  - Explore arama metni, sonuc listesi, secili tab ve uygulanmis filtreler detay
    ekranindan veya bottom bar sekmesinden geri donuste korunmaya devam
    etmelidir.
- Navigasyon karari:
  - Filter ekranini acan mevcut `navigateTo(TouristDestination.Filter)`
    korunacaktir.
  - Geri cikista yeni bir navigation extension yazilmayacak; mevcut
    `navigateUp`/back-stack davranisi kullanilacaktir.
  - State temizligini navigation katmani yapmayacak. Filter draft oturumunu
    baslatma, uygulama ve iptal etme sorumlulugu `TouristExploreViewModel`
    sinirinda kalacaktir.
  - Sistem geri tusu ve top bar geri aksiyonu ayni iptal davranisini
    calistirmalidir. Rotasyon bir geri cikis sayilmayacak ve draft'i
    temizlemeyecektir.
  - Sirf bu davranis icin dorduncu genel navigation extension, yeni graph veya
    feature'a ozel navigation-state bagimliligi olusturulmayacaktir.
- Uygulama sonucu: Filter draft baslatma/uygulama/iptal yasam dongusu ortak
  Explore ViewModel sinirinda tamamlandi ve unit testle dogrulandi.
- Dogrulama:
  - Filter degistir -> geri -> yeniden ac: son applied filtreler gorunur.
  - Filter degistir -> yatay/dikey dondur: draft korunur.
  - Filter degistir -> Uygula: sorgu yenilenir ve tekrar acilista degerler
    korunur.
  - Temizle -> geri: onceki applied filtreler korunur.
  - Temizle -> Uygula: canonical filtreler temizlenir.
  - Top bar geri ile sistem geri tusu ayni sonucu verir.

### DEG-006 - Para Cekme Sheet'i Iptalinde Tutar Temizligi

- Durum: `DOGRULANDI`
- Dogrulanan mevcut davranis:
  - Rehber para cekme tutari `GuideMyWalletScreen` icinde `rememberSaveable`
    state olarak tutuluyor.
  - Basarili para cekme talebinde tutar temizleniyor; ancak kullanici sheet'i
    kapatarak vazgectiginde yalniz gorunurluk kapaniyor ve eski tutar tekrar
    acilista gorunebiliyor.
- Kullanici deneyimi karari:
  - Sheet acikken ekran rotasyonunda girilen tutar korunmalidir.
  - Kullanici sheet'i swipe, disariya dokunma veya geri aksiyonuyla kapatirsa
    islemi iptal etmis sayilmali ve girilen tutar temizlenmelidir.
  - Yetersiz bakiye veya onay dialogu sonrasinda kullanici para cekme sheet'ine
    donuyorsa tutar duzeltilebilmesi icin korunabilir; yalniz asil sheet tamamen
    kapatildiginda temizlenmelidir.
  - Basarili talep sonrasi mevcut temizleme davranisi korunmalidir.
- Mimari sinir:
  - Bu ekran bir navigation destination degisikligi gerektirmez; yeni route,
    graph veya navigation extension eklenmeyecektir.
  - Backend bakiye kontrolu, withdrawal idempotency anahtari, secili banka
    hesabi ve talep state machine'i degismeyecektir.
  - Yalniz presentation draft tutarinin dismiss yasam dongusu
    duzeltilecektir.
- Uygulama sonucu: Yalniz gercek sheet dismiss akisi tutari temizleyecek
  sekilde presentation state duzeltildi; dialog ve rotasyon davranisi korundu.
- Dogrulama:
  - Tutar yaz -> yatay/dikey dondur: tutar korunur.
  - Tutar yaz -> sheet'i kapat -> yeniden ac: alan bostur.
  - Yetersiz bakiye dialogunu kapat -> sheet: tutar duzeltilebilir kalir.
  - Onay dialogunda `Hayir` -> sheet: tutar korunur.
  - Basarili talep -> yeniden ac: alan bostur.

### DEG-007 - Popular Tur Kartinda Dil Metnini Tek Satirda Sinirlama

- Durum: `DOGRULANDI`
- Kullanici tarafindan gorulen davranis:
  - Turist ana sayfasindaki popular tur kartinda bayraklardan sonra gosterilen
    `TR, EN, DE, KO` gibi dil kodlari kart genisligine sigmadiginda alt satira
    geciyor ve kart gorunumunu bozuyor.
- Dogrulanan neden:
  - `PopularTourCard` icindeki `languagesText` icin tek satir siniri, ellipsis
    ve Row'un kalan genisligini kullanan bir agirlik tanimlanmamis.
  - Ayni sunum ihtiyaci `TourSearchResultCard` icinde `maxLines = 1`,
    `TextOverflow.Ellipsis` ve `Modifier.weight(1f)` ile zaten dogru sekilde
    uygulanmis.
- Kullanici deneyimi karari:
  - Dil bayraklari mevcut sirasi ve gorunumu ile solda korunmalidir.
  - Dil kodlari tek satirda kalmali; kart genisligine sigmayan bolum alt satira
    gecmek yerine `...` ile gosterilmelidir.
  - Kartin diger alanlari, boyutu, tipografisi ve tiklama davranisi
    degismemelidir.
- Mimari sinir:
  - Degisiklik yalniz `PopularTourCard` presentation yerlesiminde yapilmalidir.
  - Backend response, domain/UI model, dil katalogu ve mapper verisi
    degistirilmemelidir; tum secili diller modelde korunmaya devam etmelidir.
  - Yeni ortak component veya formatter olusturulmayacaktir. Mevcut Compose
    `Text` sinirlari kullanilacaktir.
- Uygulama sonucu: Dil kodlari Row'un kalan genisliginde tek satir ve ellipsis
  ile sinirlandi; bayraklar, kart boyutu, model ve veri akisi korunarak kalite
  kapilari gecti.
- Dogrulama:
  - Tek, iki ve cok dilli popular tur kartlari.
  - Dar ekran, buyuk font ve portrait/landscape gorunumu.
  - Bayraklarin korunmasi ve dil kodlarinin yalniz gerektiginde `...` olmasi.
  - Tur arama karti ve rehber profilindeki popular tur karti regresyonu.

### DEG-008 - Tur Detayindan Ilgili Rehber Profiline Gitme

- Durum: `DOGRULANDI`
- Kullanici tarafindan gorulen davranis:
  - Turist tur detayindaki `Profili Goruntule` metni tiklanabilir gorunmesine
    ragmen yalnizca `Text` olarak ciziliyor ve herhangi bir ekrana gitmiyor.
- Dogrulanan neden:
  - Ortak `TourDetailContent`/`TourDetailSummary` zincirinde rehber profili
    callback'i bulunmuyor.
  - `TouristTourDetailScreen` yalniz tur satin alma callback'ini aliyor.
  - Tourist graph icindeki tur detay destination'i rehber profiline navigation
    callback'i baglamiyor.
- Kullanici deneyimi karari:
  - Turist `Profili Goruntule` aksiyonuna bastiginda goruntulenen turun gercek
    `guideId` degeriyle o rehberin herkese acik profiline gitmelidir.
  - Rehber profilinden geri donuldugunde yeni bir tur detayi acilmamali; mevcut
    tur detayi ve back stack korunmalidir.
  - Rehberin kendi tur yayinlama/onizleme akisi bu turist aksiyonunu gosterse
    bile gereksiz profile navigation baslatmamalidir.
- Navigasyon karari:
  - Mevcut typed `TouristDestination.GuideProfile(guideId)` hedefi ve
    `navigateTo(...)` uzantisi ileri geciste kullanilmalidir.
  - Rehber profilinden geri donuste `navigateUp()`/sistem geri davranisi
    kullanilmalidir; `navigateTo(TourDetail(...))` ile ikinci bir detay ekrani
    stack'e eklenmemelidir.
  - `switchRoot` kullanilmayacaktir. Tur detayi ile public rehber profili ayni
    tourist root/graph icindedir.
  - Yeni destination, graph veya navigation extension olusturulmayacaktir.
- Mimari ve tip guvenligi:
  - Callback ortak tur detayina opsiyonel olarak iletilmeli; navigation karari
    ortak UI component'i yerine tourist graph tarafinda kalmalidir.
  - Backend'in `Long` olarak dondurdugu rehber kimligi presentation katmaninda
    gereksiz `String` donusumune zorlanmamalidir. Uygulama sirasinda kimlik tipi
    kullanimlari taranarak typed destination ile uyumlu ve tek tip hale
    getirilmelidir; UI icinde `toLongOrNull()` gecici cozumu yazilmamalidir.
  - Profil verisi yeniden uretilmemeli; mevcut `GuidePublicProfileScreen` ve
    `GuideProfileRepository.getPublicProfile(guideId)` akisi kullanilmalidir.
- Uygulama sonucu: Opsiyonel ortak callback, typed tourist navigation ve uctan
  uca Long guide kimligi uygulandi; rehber preview davranisi degistirilmedi.
- Dogrulama:
  - Farkli rehberlere ait en az iki turun detayindan dogru profile gidilmesi.
  - Profil geri aksiyonu -> ayni tur detayina donus.
  - Profildeki tur karti -> tur detayi -> profil akisi stack dongusu
    olusturmamali.
  - Rehber publish preview ve guide tour detail ekranlarinda regresyon olmamali.

### DEG-009 - Ana Sayfa En Iyi Rehberler Basligini Dogrulama

- Durum: `DOGRULANDI`
- Kullanici tarafindan gorulen davranis:
  - Turist ana sayfasindaki rehber bolumu `Bu Bolgedeki En Iyi Rehberler`
    basligini kullaniyor; ancak mevcut istek ve backend siralamasi sehir veya
    bolge filtresi uygulamiyor.
- Dogrulanan mevcut davranis:
  - Android backend'den `limit = 4` ile en iyi rehberleri istiyor.
  - Backend aktif rehberleri agirlikli puan, yorum sayisi ve tamamlanan tur
    sayisina gore siraliyor; bolge parametresi almiyor.
  - UI backend'in dondurdugu listeyi oldugu gibi gosteriyor ve listeyi sahte
    kayitlarla dort rehbere tamamlamiyor.
- Kullanici deneyimi karari:
  - Baslik `En Iyi Rehberler` olarak degistirilmelidir.
  - Ana sayfanin kisa bir secki sunmasi icin en fazla dort rehber gosterimi
    korunmalidir.
  - Bu kapsamda `Tumunu Gor`, yatay liste veya bolgesel filtre
    eklenmeyecektir.
- Isimlendirme ve mimari sinir:
  - String resource adi `best_guides_in_region` yerine davranisi dogru anlatan
    `best_guides` olarak degistirilmeli ve kullanim noktasi guncellenmelidir.
  - Backend endpoint'i, siralama sorgusu, repository sozlesmesi, UI modeli ve
    `limit = 4` degeri degistirilmemelidir.
- Uygulama sonucu: Resource ve kullanim adi `best_guides` olarak
  netlestirildi, baslik `En Iyi Rehberler` yapildi. Backend siralamasi ve dort
  rehberlik ana sayfa limiti korunarak kalite kapilari gecti.
- Dogrulama:
  - Basligin turist ana sayfasinda `En Iyi Rehberler` olarak gorunmesi.
  - Dortten fazla uygun rehber varken yalniz ilk dort rehberin gosterilmesi.
  - Dortten az uygun rehber varken yalniz backend'in dondurdugu kadar kart
    gosterilmesi.
  - Rehber siralamasi ve kart tiklama davranisinda regresyon olmamasi.

### DEG-010 - Tur Aramasinda Bos Sonuc Temizleme Butonunu Sadelestirme

- Durum: `DOGRULANDI`
- Kapsam:
  - Bu madde Kesfet ekraninda hem metinle arama hem de sehir, ulke, kategori,
    dil, puan ve fiyat filtresi sonrasinda olusan bos sonuc gorunumunu kapsar.
- Kullanici tarafindan gorulen mevcut davranis:
  - Kullanici `Efes` gibi bir arama metni girdiginde sonuc yoksa
    `Aramaniza uygun tur bulunamadi.` mesaji ve altinda
    `Aramayi ve Filtreleri Temizle` butonu birlikte gorunuyor.
  - Arama metnini silmek zaten aramayi yeniden calistirip baslangic tur
    listesini getiriyor; metinle arama senaryosunda ek buton ayni isi tekrar
    ediyor.
- Kullanici deneyimi karari:
  - `Aramaniza uygun tur bulunamadi.` mesaji korunmalidir.
  - Mesaj mevcut `text_color` rengini kullanmaya devam etmelidir.
  - Mesajin altindaki temizleme butonu korunmali ve yalniz `Temizle`
    yazmalidir.
  - Buton mevcut ortak `EditButton` component'ini standart gorunumuyle
    kullanmalidir.
  - Butona basildiginda mevcut davranis korunarak arama metni ve uygulanmis
    filtreler temizlenmeli, baslangic tur listesi yeniden yuklenmelidir.
- Mimari sinir:
  - Backend arama endpoint'i, repository, sayfalama, debounce suresi ve tur
    kartlari degistirilmemelidir.
  - Butona verilen ozel `Modifier.fillMaxWidth(0.7f)` kaldirilmali; boyut ve
    yatay bosluklar `EditButton` component'inin ortak standartlarindan
    gelmelidir.
  - Yeni buton, component, route veya navigation extension
    olusturulmayacaktir.
  - `onClearFilters` callback zinciri ve `clearSearchAndFilters()` davranisi
    korunmalidir.
  - `Aramayi ve Filtreleri Temizle` icin kullanilan ozel string resource baska
    yerde kullanilmiyorsa kaldirilmali ve mevcut ortak `Temizle` resource'u
    kullanilmalidir.
- Uygulama sonucu: Bos sonuc mesaji ve mevcut temizleme callback'i korundu;
  ortak `EditButton` standart gorunumuyle yalniz `Temizle` metni kullanildi.
  Artik kullanilmayan ozel string ve yuzde 70 genislik kaldirilarak kalite
  kapilari gecti.
- Dogrulama:
  - Filtre uygulamadan sonuc vermeyen bir metin ara: bos sonuc mesaji ve
    `Temizle` butonu gorunmeli.
  - `Temizle` butonuna bas: arama metni ve filtreler temizlenerek baslangic tur
    listesi yeniden gelmeli.
  - Sonuc vermeyen filtre uygula: ayni standart `Temizle` butonu gorunmeli ve
    tek tikla baslangic listesine donmeli.
  - Buton standart `EditButton` ile ayni renk, sekil, genislik ve bosluklari
    kullanmali; yuzde 70 ozel genislik uygulanmamali.
  - Mesaj rengi `text_color`, yukleme ve hata durumlari mevcut tasarimla ayni
    kalmali.
  - Filtre ikonuyla acilan ekran ve filtre state'i regresyona ugramamali.

### DEG-011 - Tur Filtresinde Minimum Puan Metnini Netlestirme

- Durum: `DOGRULANDI`
- Dogrulanan mevcut davranis:
  - Android secilen yildiz degerini `minimumRating` olarak backend'e gonderiyor.
  - Backend tur ortalama puanini secilen degere `buyuk veya esit` olacak sekilde
    filtreliyor. Ornegin dort yildiz secimi 4.0 ve uzerindeki turlari getiriyor;
    3.9 ve altini ya da puansiz turlari getirmiyor.
- Kullanici deneyimi karari:
  - Filtre ekranindaki `Puan` basligi mevcut davranisi acik anlatan
    `En Az Puan` metniyle degistirilmelidir.
- Mimari sinir:
  - Degisiklik yalniz mevcut XML string resource metninde yapilmalidir.
  - Android sorgu modeli, ViewModel, repository, backend endpoint'i ve puan
    hesaplama/filtreleme sorgusu degistirilmemelidir.
- Uygulama sonucu: Filtre basligi yalniz XML resource uzerinden `En Az Puan`
  olarak guncellendi; minimumRating sorgu ve backend davranisi korunarak kalite
  kapilari gecti.
- Dogrulama:
  - Dort yildiz sec: yalniz 4.0 ve uzerindeki turlar gorunmeli.
  - Baslik `En Az Puan` olmali; tasarim, yildiz secimi ve sonuc kartlari ayni
    kalmali.

### DEG-012 - MVP Ornek Gorsellerini Kaldirma ve Anlamli Placeholder Kullanimi

- Durum: `DOGRULANDI`
- Dogrulanan mevcut davranis:
  - `example.jpg` MVP asamasinda gecici gorsel olarak eklenmis; turist profil
    fotografi, tur kapagi ve sohbet avatari dahil farkli anlamlara sahip
    alanlarda fallback olarak kullaniliyor.
  - `unnamed.jpg` rehber, yorumcu ve diger kullanici avatarlari icin fallback
    olarak kullaniliyor.
  - Backend normal kayitta turist veya rehbere varsayilan avatar atamiyor;
    `avatar_media_id` dogru sekilde `null` kaliyor.
  - Rehber gercek kapak fotografi secmeden tur yayinlayamiyor. Bu nedenle
    yayinlanmis tura varsayilan/sahte bir tur kapagi atanmasi is kuralimiz
    degildir.
- Kullanici deneyimi karari:
  - Yeni turist ve rehber, kendi fotografini yukleyene kadar ayni notr kisi
    avatarini gorecektir.
  - Notr avatar gercek insan fotografi olmayacak; Android tarafinda cizilen
    sade bir vector kisi silueti olacaktir.
  - Kullanici gercek fotograf yuklediginde backend'den gelen URL notr avatarin
    yerini otomatik olarak alacaktir.
  - Yayinlanmis tur karti/detayinda gercek kapak her zaman backend medya
    URL'sinden gelecektir.
  - Gercek medya URL'si ag, HTTP veya dosya hatasi nedeniyle yuklenemezse sahte
    tur fotografi yerine teknik anlami acik notr bir `image_unavailable` vector
    gorunumu kullanilacaktir. Bu vector tur kapagi sayilmayacak ve backend'e
    kaydedilmeyecektir.
  - Tur yayinlama taslaginda kapak secilmediyse mevcut fotograf ekleme alani ve
    zorunlu kapak dogrulamasi korunacaktir; teknik hata placeholder'i secilmis
    kapak gibi gosterilmeyecektir.
- Ortak yapi ve katman karari:
  - Insan avatarlari icin tek ortak vector resource kullanilmalidir: turist,
    rehber, yorumcu, sohbet katilimcisi ve kullanici kartlari ayni anlami
    paylasir.
  - Medya yukleme hatasi icin insan avatarindan ayri tek ortak
    `image_unavailable` vector resource kullanilmalidir.
  - Mevcut ortak `GuideMateImage` remote URL ile fallback/error painter
    secimini tek noktadan yonetmeye devam etmelidir. Feature'lara kopya image
    loading veya fallback mantigi yazilmamalidir.
  - UI model ve mapper'larda insan ile tur/medya fallback'leri dogru semantik
    resource'a baglanmalidir; backend DTO/domain sozlesmesi degistirilmemelidir.
  - Placeholder resource kimlikleri backend'e, request DTO'ya, persistence'a
    veya kullanici verisine yazilmayacaktir.
- Temizlik karari:
  - `example.jpg` tum kullanimlardan kaldirilip dosya olarak silinecektir.
  - `unnamed.jpg` tum kullanimlardan kaldirilip dosya olarak silinecektir.
  - Silmeden once profil, rehber, yorumcu, sohbet, rezervasyon, tur karti,
    detay, yayinlama ve duzenleme akislarindaki tum referanslar semantik vector
    resource'lara tasinmalidir.
  - Degisiklik sonunda iki JPEG'e ait kullanilmayan import/resource/test
    beklentisi kalmadigi `rg`, derleme ve lint ile dogrulanmalidir.
- Backend siniri:
  - Backend kodu veya veritabani degismeyecektir. Avatar istege bagli `null`,
    yayinlanmis tur kapagi ise zorunlu gercek medya olarak kalacaktir.
  - Android teknik fallback'i hicbir zaman canonical profil veya tur medyasi
    kabul etmeyecektir.
- Test karari:
  - Yeni business/state mantigi eklenmedigi icin sirf resource degisimi icin
    gereksiz unit test yazilmayacaktir.
  - Resource referanslari derleme/lint ile; avatar, tur kapagi, sohbet ve medya
    hata gorunumleri ilgili kullanici testleriyle dogrulanacaktir.
- Uygulama sonucu:
  - Insan avatarlarinin ortak fallback'i `ic_default_avatar` vector resource'una
    tasindi.
  - Tur kapagi ve genel medya yukleme hatalarinin fallback'i
    `ic_image_unavailable` vector resource'una tasindi.
  - `example.jpg` ve `unnamed.jpg` tum kod/test referanslarindan kaldirildi ve
    drawable dosyalari silindi.
  - Backend, DTO, persistence, navigasyon, tasarim ve kullanici akisi
    degistirilmedi.
  - Yeni business davranisi eklenmedigi icin gereksiz yeni test yazilmadi;
    mevcut mapper resource beklentisi yeni semantik fallback ile guncellendi.
  - Temiz build uzerinde `ktfmtCheck`, JVM testleri, Android test kaynak
    derlemesi, `lintDebug` ve `assembleDebug` basarili tamamlandi.
- Dogrulama:
  - Fotograf yuklememis yeni turist ve rehberde notr kisi avatarinin gorunmesi.
  - Gercek avatar yuklenince vector yerine remote fotograf gorunmesi.
  - Rehber, yorumcu ve sohbet katilimcisi avatar fallback'lerinin tutarli
    olmasi.
  - Gercek tur kapagi URL'sinin kart, arama, detay, rezervasyon ve rehber
    yonetim ekranlarinda kullanilmasi.
  - Medya yukleme hatasinda yalniz `image_unavailable` gorunmesi.
  - Kapak secilmeden tur yayinlanamamasi ve placeholder'in kapak sayilmamasi.
  - Projede `example` ve `unnamed` drawable referansi veya dosyasi kalmamasi.

### DEG-013 - FCM FID Kaydi ve Kanitlanmis Kullanilmayan Kod Temizligi

- Durum: `DOGRULANDI`
- FCM davranis karari:
  - Android manifestte FID tabanli Firebase Messaging modu acilmalidir.
  - Firebase yapilandirmasi bulunan uygulama sureci FCM'e `register()` ile
    kaydolmalidir; Firebase bulunmayan JVM/Robolectric ortaminda uygulama
    baslangici cokmemelidir.
  - Guncel FID `onRegistered()` callback'i ile alinmali ve oturum acmis
    kullanicinin mevcut notification repository cihaz kaydi uzerinden backend'e
    gonderilmelidir.
  - Callback oturum acilmadan gelirse yetkisiz backend istegi atilmamalidir.
    Kullanici oturum actiginda mevcut user-state gozlemcisi cihaz kaydini
    tamamlamaya devam etmelidir.
  - Backend'in FID hedefleyen mevcut `setFid()` kullanimi, endpoint'i ve veri
    modeli degistirilmemelidir.
- Katman ve bagimlilik karari:
  - Firebase uygulama kaydi application composition root'ta, Firebase callback'i
    messaging service'te, backend cihaz eslemesi notification repository'de
    kalmalidir.
  - Presentation ve ViewModel katmanlari Firebase SDK ayrintisi bilmemelidir.
  - Messaging service uzun yasamli isi mevcut `ApplicationScope` ile repository
    sinirina devretmelidir.
- Temizlik karari:
  - Kanitlanmis 12 kullanilmayan import, dokuz kullanilmayan string resource,
    `ChatConversation.containsUser()`, erisilemeyen eski SDK kontrolu ve iki bos
    test klasoru kaldirilmalidir.
  - `plurals` donusumu ve Compose `Modifier` parametre sirasi bu kapsamda
    degistirilmeyecektir.
- Test karari:
  - Oturumlu kullanicida callback FID'sinin backend request'ine tasindigi ve
    oturum yokken cihaz kaydi istegi atilmadigi repository unit testleriyle
    korunmalidir.
  - Firebase SDK callback'i icin gereksiz sahte framework katmani veya kirilgan
    UI testi yazilmamalidir; manifest, derleme ve lint ile dogrulanmalidir.
- Uygulama sonucu:
  - Manifest FID modu, guvenli FCM kaydi ve `onRegistered()` callback akisi
    tamamlandi.
  - Oturum kontrolu ile callback FID'sini kullanan idempotent backend cihaz
    kaydi kuruldu; backend kodu degistirilmedi.
  - Kanitlanmis kullanilmayan kod/resource/import ve bos klasorler temizlendi.
  - `ktfmtCheck`, 160 JVM testi, Android test kaynak derlemesi, `lintDebug` ve
    `assembleDebug` basarili tamamlandi; lintte error, unused resource, eski SDK
    ve FCM token callback uyarisi kalmadi.
- Kullanici dogrulamasi:
  - Firebase yapilandirmali backend ve bildirim izni verilmis fiziksel cihazla
    uygulama acik, arka planda ve normal sekilde kapaliyken push teslimi
    denenmelidir.
  - Bildirime dokununca ilgili typed ekrana gidildigi ve uygulama ici okunmamis
    sayisinin yenilendigi dogrulanmalidir.

### DEG-014 - Sifre Guvenlik Bildirimlerini Olaya Ozel Gostermek

- Durum: `UYGULANDI`
- Dogrulanan mevcut davranis:
  - Backend sifre degistirme icin `PASSWORD_CHANGED`, sifre sifirlama icin
    `PASSWORD_RESET` degerini notification payload icindeki `securityEvent`
    alaninda ayri ayri uretiyor.
  - Android notification payload modeli ve mapper'i `securityEvent` alanini
    okumuyor. Bu nedenle uygulama ici bildirimde iki olay da
    `Hesabinizla ilgili bir guvenlik uyarisi var.` genel metnine dusuyor.
  - Backend FCM data payload'ina su anda yalniz notification type ve `Id` ile
    biten hedef alanlarini koyuyor. `securityEvent` telefonun sistem
    bildirimine tasinmadigi icin bildirim cubugunda da ayni genel metin cikiyor.
- Kullanici deneyimi karari:
  - `PASSWORD_CHANGED` olayi sifrenin degistirildigini acikca soylemelidir.
  - `PASSWORD_RESET` olayi sifrenin sifirlandigini ve yeni sifreyle giris
    yapilabilecegini acikca soylemelidir.
  - Taninmayan veya gelecekte eklenecek bir guvenlik olayi mevcut genel
    guvenlik metnine duserek guvenli fallback davranisini korumalidir.
  - Uygulama ici notification listesi ile FCM sistem bildirimi ayni olaya ayni
    anlami veren metni gostermelidir.
- Metin ve yerellestirme karari:
  - Kullaniciya gorunen baslik ve govde metinleri Kotlin veya Java icine sabit
    yazilmayacak; Android XML string resource'larindan alinacaktir.
  - Onerilen govdeler:
    - Sifre degistirme: `Sifreniz degistirildi. Bu islem size ait degilse
      hesabinizi guvene alin.`
    - Sifre sifirlama: `Sifreniz basariyla sifirlandi. Yeni sifrenizle giris
      yapabilirsiniz.`
  - Mevcut genel `Guvenlik uyarisi` basligi ve genel govde yalniz fallback icin
    korunacaktir.
- Backend siniri:
  - Yeni tablo veya endpoint acilmayacak; mevcut `securityEvent` payload alani
    korunacak ve FCM data payload'ina kontrollu/whitelist edilmis alan olarak
    eklenecektir.
  - Diger notification payload alanlari gereksiz yere FCM'e acilmayacak ve
    hassas hesap bilgisi push verisine eklenmeyecektir.
- Android siniri:
  - `securityEvent` notification data mapper ve domain modelinde tip guvenli
    bicimde ele alinacak; bilinmeyen deger fallback'e dusmelidir.
  - Uygulama ici notification ve push text resolver ayni anlam eslemesini
    kullanmali; iki ayri yerde farkli business karari uretilmemelidir.
  - Diger tur, rezervasyon, odeme, chat ve yorum bildirimlerinin tipi,
    navigasyonu, okunma durumu ve metinleri bu degisiklikten etkilenmemelidir.
- Test karari:
  - Android mapper/resolver testleri `PASSWORD_CHANGED`, `PASSWORD_RESET` ve
    bilinmeyen deger fallback'ini korumalidir.
  - Backend push data testi `securityEvent` alaninin tasindigini, alakasiz veya
    hassas payload alanlarinin kendiliginden push'a sizmadigini dogrulamalidir.
  - Salt XML metninin noktalama veya kelime secimi icin kirilgan UI testi
    yazilmayacaktir.
- Uygulama sonucu:
  - `PASSWORD_CHANGED` ve `PASSWORD_RESET` degerleri Android domain modelinde
    tip guvenli hale getirildi; bilinmeyen degerler `UNKNOWN` fallback'ine
    dusuyor.
  - Uygulama ici bildirim ve sistem bildirimi ayni XML metinlerini kullanarak
    olaya ozel aciklama gosteriyor. Diger bildirim tipleri ve navigation
    davranislari degismedi.
  - Backend push verisine yalniz `securityEvent` anahtari whitelist ile eklendi;
    diger serbest metin ve hassas alanlar push verisine sizmiyor.
  - Android mapper/resolver testleri ile backend PostgreSQL push teslim testi
    odakli olarak basarili tamamlandi.

### DEG-015 - Rehber Profilinde Turlarim On Izlemesi ve Tum Turlar Ekrani

- Durum: `UYGULANDI`
- Dogrulanan mevcut davranis:
  - Turistin gordugu public rehber profilinde bolum basligi `Populer Turlar`
    olarak gosteriliyor.
  - Public profil ViewModel'i yalniz uc tur degil, ilk sayfada en fazla 20 tur
    indiriyor; yatay satir nedeniyle ekranda ayni anda yaklasik uc kart
    gorunuyor.
  - `Tumunu Gor` metni tiklanabilir bir aksiyon veya navigation callback'i
    tasimiyor.
  - Profil kartlari mevcut backend `/api/v1/tours/popular` endpoint'ine
    `guideId`, `page` ve `size` gonderilerek canonical tur/session verisinden
    uretiliyor.
- Kullanici deneyimi karari:
  - Rehber profilindeki bolum basligi, profilin mevcut `Hakkimda` anlatimiyla
    uyumlu olacak sekilde `Turlarim` olarak degistirilmelidir.
  - Profilde en fazla ilk uc uygun tur yatay on izleme olarak gosterilmelidir.
  - `Tumunu Gor` gercek ve erisilebilir bir tiklama aksiyonu olmalidir.
  - Aksiyon, secili rehberin turist tarafindan satin alinabilir butun turlarini
    alt alta gosteren yeni bir ekrana gitmelidir.
  - Liste kartina basinca mevcut typed tur detay destination'ina session ID ile
    gidilmeli; geri donuste public rehber profili korunmalidir.
  - Rehberin uygun turu yoksa sahte kayit uretilmeden mevcut tasarim diliyle
    uyumlu bos durum gosterilmelidir.
- Yeni ekran ve navigation karari:
  - Yeni ekran tur feature'ina ait turist sunumu olarak
    `tour/presentation/tourist/guide` altinda konumlandirilmalidir.
  - Ekran, ViewModel ve UI state adlari rehbere gore tur listeleme amacini
    acikca anlatmalidir; profile veya discovery paketine kopya tur business
    mantigi yazilmamalidir.
  - Typed tourist navigation'a yalniz `guideId` tasiyan bir destination
    eklenmelidir. Yeni NavHost veya ayri navigation graph acilmamalidir.
  - Geri donus normal back stack semantigiyle `navigateUp`/sistem geri davranisi
    uzerinden public profile donmelidir; profile yeniden root olarak
    olusturulmamali veya `switchRoot` kullanilmamalidir.
- Ortak kart, veri ve katman karari:
  - Alt alta liste, turist kesfet ekraninda kullanilan mevcut
    `TourSearchResultCard` tasarimini yeniden kullanmalidir; ikinci bir tur karti
    kopyasi yazilmamalidir.
  - Kart verileri mevcut `TourSearchItem` domain modeli ve
    `toSearchResultUiModel()` mapper'i ile uretilmelidir.
  - Mevcut `TourDiscoveryRepository` ve sayfali backend sonucu kullanilmalidir;
    yeni store, paralel veri listesi veya UI tarafinda sahte tur uretilmemelidir.
  - Fiyat, puan, yorum sayisi, tarih, dil, kapak, rehber ve kontenjan bilgileri
    ayni canonical backend tur/session kaynagindan gelmelidir.
  - Liste `LazyColumn` ve backend pagination ile calismali; butun sayfalar tek
    istekte indirilmemelidir.
- Backend ve demo veri siniri:
  - Mevcut `/api/v1/tours/popular?guideId=...&page=...&size=...` sozlesmesi
    rehberin onayli, gelecekteki, satin almaya acik ve bos kontenjani bulunan
    turlarini sayfali dondurdugu icin yeni backend endpoint'i, DTO, tablo veya
    service degisikligi gerekmemektedir.
  - Demo veritabanina yeni tur veya kullanici eklenmeyecektir. Mevcut demo tur,
    session, review ve reservation kayitlari ekran tarafindan aynen okunur.
  - Backend datasource'u ileride `guidemate_demo` yerine normal `guidemate_db`
    olarak calistirildiginda Android kodu degismemelidir. Bos normal veritabaninda
    bos durum; gercek onayli ve acik turlar olustukca gercek liste gorunmelidir.
  - Demo medya kayitlari normal veritabanina tasinmayacak; normal ortamda
    kullanici tarafindan yuklenen canonical medya URL'leri kullanilacaktir.
- Metin karari:
  - `Turlarim`, `Tumunu Gor`, ekran basligi ve bos durum dahil kullaniciya
    gorunen yeni veya degisen butun metinler Android XML string resource'larindan
    gelmelidir.
- Test karari:
  - ViewModel pagination davranisi, ilk sayfa/son sayfa, append hata ve bos
    sonuc icin odakli unit testlerle korunmalidir.
  - Repository veya mapper davranisi zaten ayni seviyede kapsaniyorsa ayni
    testi tekrar eden gereksiz test yazilmamalidir.
  - `guideId` typed destination aktarimi ve karttan dogru session detayina
    yonlendirme gercek regresyon riski tasidigi olcude mevcut navigation/mapper
    testlerine eklenmelidir.
  - Kartin salt gorunumu icin kirilgan ekran goruntusu testi yazilmayacak;
    kesfet kartiyla tasarim esligi ve geri donus kullanici testinde
    dogrulanacaktir.
- Uygulama sonucu:
  - Public rehber profilindeki bolum `Turlarim` olarak adlandirildi ve canonical
    endpoint'ten yalniz uc turluk on izleme istiyor.
  - `Tumunu Gor`, guide ID tasiyan typed tourist destination ile yeni
    `TouristGuideToursScreen` ekranina baglandi. Yeni graph veya scaffold
    olusturulmadi; normal geri yigini ve mevcut tourist shell korundu.
  - Tum turlar ekrani ayni `TourDiscoveryRepository`, `TourSearchItem`, ortak
    mapper ve `TourSearchResultCard` uzerinden sayfali calisiyor. Karttan mevcut
    session ID tabanli tur detayina gidiliyor; paralel store, mock liste veya
    kopya kart yazilmadi.
  - Ilk/son sayfa, append hata ve bos sonuc davranislari odakli
    `TouristGuideToursViewModelTest` ile Robolectric altinda basarili
    dogrulandi.

### DEG-016 - Gezilerim Rezervasyonlarinda Nullable Iade Uygunlugu Sozlesmesi

- Durum: `UYGULANDI`
- Dogrulanan mevcut davranis:
  - Backend hem `UPCOMING` hem `PAST` rezervasyon listesi isteklerine basarili
    `200` cevabi donuyor.
  - Iptal edilmemis rezervasyonlarda `cancellationRefundEligibility` alani
    backend sozlesmesine uygun olarak `null` gelebiliyor.
  - Android DTO'su bu alani zorunlu `String` kabul ediyor ve mapper
    `ReservationRefundEligibility.valueOf(...)` islemini kosulsuz yapiyor.
  - Bos rezervasyon listesinde map edilecek kayit olmadigi icin dogru bos durum
    gosteriliyor; gercek veya demo rezervasyon bulunan hesapta ilk hatali kayit
    butun sayfayi `Tekrar Dene` durumuna dusurebiliyor.
- Uygulama karari:
  - Android remote DTO backend'in nullable sozlesmesiyle birebir uyumlu hale
    getirilecektir.
  - Iptal edilmemis rezervasyondaki `null` deger domain modelinde mevcut
    `ReservationRefundEligibility.NOT_APPLICABLE` degerine map edilecektir.
  - Iptal edilmis rezervasyonlarda backend'in gonderdigi gercek
    `FULL_REFUND`/`NO_REFUND` degeri korunacak; sahte iade sonucu uretilmeyecektir.
  - Kullaniciya gorunen Gezilerim tasarimi, sekmeler, bos durum ve retry
    gorunumu degistirilmeyecektir.
- Backend siniri:
  - Backend'in iptal edilmemis rezervasyonda nullable deger dondurmesi gecerlidir;
    yeni endpoint, tablo veya business kural degisikligi gerekmemektedir.
  - OpenAPI/DTO nullable sozlesmesi mevcut davranisla tutarli kalmalidir.
- Test karari:
  - Android mapper testi, iptal edilmemis ve nullable iade uygunluguna sahip
    rezervasyonun `NOT_APPLICABLE` olarak basariyla map edildigini dogrulamalidir.
  - Iptal edilmis rezervasyonda gercek iade uygunlugu degerinin kaybolmadigi
    mevcut testle kapsanmiyorsa ayni mapper test grubuna odakli senaryo
    eklenmelidir.
  - Bos liste ve dolu liste icin ayni repository/ViewModel davranisini tekrar
    test eden gereksiz test yazilmayacaktir.
- Uygulama sonucu:
  - Android remote DTO nullable backend sozlesmesiyle eslestirildi ve `null`
    deger domain katmaninda `NOT_APPLICABLE` olarak map edildi.
  - Iptal edilmis rezervasyonun gercek iade uygunlugu korunuyor.
  - Odakli `ReservationRepositoryImplTest` JDK 21 ile basarili tamamlandi.

### DEG-017 - Ilk Mesaj Gonderiminde Send ve Mark-Read Yarisini Giderme

- Durum: `UYGULANDI`
- Dogrulanan mevcut davranis:
  - Yeni veya ilk kez mesaj gonderilen bir sohbette Android yerel
    `PENDING` mesaji listeye ekliyor.
  - `ChatDetailViewModel`, son mesaj kimligindeki her degisikligi gelen mesaj
    gibi yorumladigi icin kullanicinin kendi `PENDING` mesaji ile ayni anda
    `markRead` istegi gonderiyor.
  - Canli HTTP logunda ilk `POST /messages` ile `POST /read` istegi ayni anda
    calisiyor; ilk mesaj `500`, read istegi `200` donebiliyor. Ayni
    `clientMessageId` ile retry edildiginde mesaj `200` donuyor ve sonraki
    gonderimler calisiyor.
  - Bu davranis demo verisine ozel degildir; gercek veritabaninda da ayni
    eszamanlilik kosulunda olusabilir.
- Android uygulama karari:
  - Kullanici tarafindan gonderilen yerel `PENDING`, `FAILED` veya canonical
    kendi mesajlari `markRead` tetiklememelidir.
  - Okundu istegi yalniz karsi taraftan gelen ve backend tarafindan taninmis
    mesaja gore gonderilmelidir.
  - Mevcut optimistic `PENDING -> SENT/FAILED`, ayni `clientMessageId` ile retry
    ve mesaj tasarimi korunacaktir.
  - STOMP ile gelen mesaj, ilk gecmis yuklemesi ve sohbet ekrani acikken okundu
    davranisi bozulmayacaktir.
- Backend uygulama karari:
  - Gecerli iki cihazdan ayni sohbet icin gercekten eszamanli send ve mark-read
    istekleri gelebilecegi icin backend bu durumu `500` ile sonlandirmamalidir.
  - Mevcut conversation sahipligi, idempotent `clientMessageId`, transaction ve
    locking kurallari korunarak lock sirasi/transaction kapsami incelenecek ve
    en kucuk guvenli duzeltme yapilacaktir.
  - Android'in gereksiz read istegini kaldirmasi backend dayanikliliginin yerine
    gecmez; iki taraf birbirinden bagimsiz olarak dogru davranmalidir.
- Test karari:
  - Android ViewModel testi, kullanicinin kendi yerel veya canonical mesajinin
    `markRead` cagirmadigini ve karsi taraftan gelen yeni mesajin tam bir kez
    cagirdigini dogrulamalidir.
  - Android repository testi mevcut retry'nin ayni `clientMessageId` degerini
    korudugunu zaten kapsamiyorsa bu kritik idempotency davranisi eklenmelidir;
    kapsiyorsa tekrar test yazilmayacaktir.
  - Backend entegrasyon/concurrency testi ayni sohbette eszamanli send ve
    mark-read isteklerinin ikisinin de basarili oldugunu, tek mesaj olustugunu ve
    okunmamis sayisinin tutarli kaldigini dogrulamalidir.
  - Salt mesaj balonu gorunumu icin kirilgan UI testi eklenmeyecektir.
- Uygulama sonucu:
  - Android okundu gozlemcisi yalniz karsi taraftan gelen canonical `SENT`
    mesajlari izler hale getirildi; kullanicinin kendi `PENDING`, `FAILED` veya
    `SENT` mesaji artik `markRead` istegi uretmiyor.
  - Backend mesaj gonderme servisi kullaniciyi ayri bir satir kilidiyle
    yuklemek yerine conversation kilidiyle yuklenen guncel katilimciyi
    kullaniyor. Conversation sahipligi, hesap/rol/token-version kontrolleri ve
    idempotent `clientMessageId` davranisi korundu.
  - Android `ChatDetailViewModelTest` ve PostgreSQL Testcontainers tabanli
    `ChatPersistenceTest` odakli olarak basarili tamamlandi. Eszamanli ilk
    gonderim/okundu istekleri tek mesaj, gonderende sifir ve alicida bir
    okunmamis mesaj ile sonuclaniyor.

### DEG-018 - Gezilerim Rezervasyon Detayindan Rehber Profiline Gitme

- Durum: `UYGULANDI`
- Dogrulanan mevcut davranis:
  - Yaklasan ve gecmis gezi kartlari ayni typed `ReservationDetail` ekranini
    aciyor.
  - Ortak `TourDetailContent`, `Profili Goruntule` metnini gosteriyor; ancak
    rezervasyon detay ekrani rehber profili callback'ini iletmedigi icin metin
    tiklanabilir degildi.
  - Ana sayfa, Kesfet ve normal turist tur detayinda mevcut typed rehber profili
    navigasyonu dogru baglanmis. Rehberin kendi tur detayi ve yayin on izlemesi
    icin profil navigasyonu olmamasi bilincli davranistir.
- Kullanici deneyimi karari:
  - Turist hem Yaklasan hem Gecmis karttan rezervasyon detayina girdiginde
    snapshot'taki gercek rehberin profiline gidebilmelidir.
  - Profil geri aksiyonu yeni rezervasyon detayi olusturmadan ayni detay ekranina
    donmelidir.
- Mimari ve navigation karari:
  - Mevcut `TourDetailContent` callback sozlesmesi ve typed
    `TouristDestination.GuideProfile(guideId)` hedefi kullanilacaktir.
  - Ileri geciste mevcut `navigateTo`, geri donuste normal `navigateUp`/sistem
    geri yigini kullanilacaktir. `switchRoot`, yeni destination, graph veya
    navigation extension eklenmeyecektir.
  - Rehber kimligi rezervasyonun satin alma snapshot'indan gelen canonical
    `guideId` degeridir; UI sahte rehber veya ikinci veri kaynagi uretmeyecektir.
- Uygulama sonucu:
  - `TouristReservationDetailScreen`, rehber kimligini callback ile tourist
    graph'a iletiyor ve graph mevcut public rehber profiline yonlendiriyor.
  - Yaklasan ve gecmis sekmeleri ortak rezervasyon detayini kullandigi icin iki
    akis tek degisiklikle duzeldi; ortak detail tasarimi ve snapshot verisi
    korunuyor.
- Test karari:
  - Rezervasyon snapshot mapper testi, canonical rehber kimliginin detay UI
    modeline kaybolmadan tasindigini dogrulamalidir.
  - Basit Compose callback baglantisi icin kirilgan UI testi yazilmayacak;
    ileri/geri davranisi kullanici testinde dogrulanacaktir.
- Otomatik dogrulama:
  - `TouristReservationMapperTest`, rezervasyon snapshot rehber kimliginin detay
    UI modelinde korundugunu dogruladi.
  - `ktfmtFormat`, odakli JVM testi, `compileDebugKotlin` ve `lintDebug`
    basarili tamamlandi.

### DEG-019 - Gezilerim Rezervasyon Detayinda Tur Puani ve Yorum Sayisi

- Durum: `UYGULANDI`
- Dogrulanan mevcut davranis:
  - Popular/Kesfet akisi ile acilan normal turist tur detayinda ortalama puan ve
    toplam yorum sayisi gosteriliyor.
  - Yaklasan veya Gecmis gezi kartindan acilan rezervasyon detayinda ayni ortak
    detail tasarimi kullanilmasina ragmen puan ve yorum sayisi gosterilmiyor.
  - Rezervasyon detay mapper'i public yorum listesinin toplam sayisini tasiyor,
    ancak turun ortalama puanini tasimiyor. Ortak detail gorunumu puan ve yorum
    sayisini birlikte bekledigi icin alan tamamen gizleniyor.
  - Yorum gonderimi backend'de turun canonical puan/yorum toplamini guncelliyor;
    ancak rezervasyon response sozlesmesi bu aggregate degerleri Android'e
    tasimadigi icin kullanicinin yeni yorumu da rezervasyon detay basligina
    yansimiyor.
- Kullanici deneyimi karari:
  - Ayni tur, hangi turist rotasindan acilirsa acilsin ayni canonical ortalama
    puani ve toplam yorum sayisini gostermelidir.
  - Rezervasyonun kullaniciya ait tekil `review` verisi ile turun butun
    rezervasyonlarindan hesaplanan `averageRating`/`reviewCount` birbirine
    karistirilmamalidir.
- Mimari ve veri sozlesmesi karari:
  - Backend rezervasyon cevaplarina tur bazli `averageRating` ve `reviewCount`
    eklemeli; mevcut toplu review aggregate sorgusu kullanilarak listelemede
    kart basina ayri istek/N+1 uretilmemelidir.
  - Android DTO, domain model ve mapper bu alanlari mevcut ortak
    `TourDetailUiState` sozlesmesine tasimali; sahte ortalama veya ikinci veri
    kaynagi olusturmamalidir.
  - Yorum basariyla gonderildikten sonra rezervasyon detayi canonical backend
    verisiyle yenilenerek yeni ortalama ve yorum sayisini gostermelidir.
- Test karari:
  - Backend mapper/service testi rezervasyon cevabinda toplu aggregate degerini
    ve yorumsuz tur davranisini dogrulamalidir.
  - Android mapper/ViewModel testi ortalama puan ile yorum sayisinin rezervasyon
    detayina tasindigini ve yorum sonrasi yenilendigini korumalidir.
  - Salt metin gorunumu icin kirilgan Compose UI testi eklenmeyecektir.
- Uygulama sonucu:
  - Backend rezervasyon cevabi tur aggregate degerlerini ve session dolulugunu
    toplu sorgularla donuyor; Android bunlari tek canonical kaynaktan detail
    modeline tasiyor.
  - Yorum sonrasi rezervasyon yeniden yuklenerek guncel ortalama ve toplam yorum
    sayisi gosteriliyor.

### DEG-020 - Rezervasyon Kisi Sayisi ve Tur Dolulugu Sunumu

- Durum: `UYGULANDI`
- Kullanici deneyimi karari:
  - Popular tur kartinin mevcut kompakt tasarimi korunacak ve kartta kontenjan
    bilgisi eklenmeyecektir. Karta basilarak acilan normal tur detayinda mevcut
    `bookedCount/capacity` doluluk bilgisi gosterilecektir.
  - Kesfet/arama sonuc kartinda turist icin karar vermeyi kolaylastiran
    `availableCapacity` degeri `%d kisilik yer kaldi` biciminde gosterilmeye
    devam edecektir. Detaya girildiginde ayni session'in
    `bookedCount/capacity` bilgisi gosterilecektir.
  - Gezilerim Yaklasan ve Gecmis liste kartlarinda turun toplam dolulugu yerine
    kullanicinin kendi rezervasyonu acikca `Rezervasyonunuz: %d kisi` olarak
    gosterilecektir.
  - Yaklasan veya tamamlanmis Gecmis rezervasyon detayinda ise normal tur detay
    mantigiyla session'in gercek `bookedCount/capacity` dolulugu
    gosterilecektir.
  - Iptal edilmis turda doluluk yerine `Tur iptal edildi`; turistin kendi
    rezervasyonu iptal edilmisse `Rezervasyonunuz iptal edildi` durumu oncelikli
    olacaktir. Bu durumlarda doluluk kullaniciyi yaniltacak bicimde ana bilgi
    olarak gosterilmeyecektir.
- Test karari:
  - Android mapper testi Gezilerim kartindaki rezervasyon kisi sayisi ile
    detaydaki tur dolulugunun birbirine karismadigini korumalidir.
  - Salt metin ve yerlesim icin kirilgan Compose UI testi yazilmayacaktir.
- Uygulama sonucu:
  - Gezilerim kartlari yalniz `Rezervasyonunuz: %d kisi`, rezervasyon detayi ise
    backend session `bookedCount/capacity` degerini gosteriyor.
  - Iptal kartlari turist ve tur iptalini actor'a gore ayiriyor; iptal karti ve
    detayinda yaniltici doluluk bilgisi gizleniyor.

### DEG-021 - Turist Sohbetinden Rehber Profiline Gitme

- Durum: `UYGULANDI`
- Dogrulanan mevcut davranis:
  - Turist sohbet detayinin topbar'inda karsi taraf olan rehberin backend'den
    gelen adi, profil fotografi ve `remoteUserId` degeri mevcut.
  - Fotograf ve isim yalniz gorsel olarak ciziliyor; tiklama callback'i olmadigi
    icin rehber profiline gidilemiyor.
- Kullanici deneyimi karari:
  - Turist sohbet detayinda topbar'daki rehber fotografi ve adi birlikte
    tiklanabilir olmali ve ilgili public rehber profilini acmalidir.
  - Geri aksiyonu ayni sohbet detayina donmelidir.
  - Rehber sohbetinde turist icin public profil ekrani bulunmadigindan mevcut
    topbar davranisi degismeyecektir.
- Mimari ve navigation karari:
  - Yeni ekran, destination, graph veya backend endpoint'i eklenmeyecektir.
  - Ortak `AppTopBar` yalniz opsiyonel profil tiklama callback'i alacak;
    navigation karari turist shell/graph sinirinda kalacaktir.
  - Mevcut `ChatUiModel.remoteUserId` ve typed
    `TouristDestination.GuideProfile` kullanilacak; ileri geciste `navigateTo`,
    geri donuste mevcut `navigateUp`/geri yigini korunacaktir.
- Test karari:
  - Kimligin backend chat cevabindan `ChatUiModel.remoteUserId` alanina
    tasinmasi mevcut mapper testlerinde kapsanmiyorsa odakli mapper testi
    eklenecektir; kapsaniyorsa tekrar test yazilmayacaktir.
  - Basit Compose tiklama ve ileri/geri gorunumu kullanici testinde
    dogrulanacak; kirilgan UI testi eklenmeyecektir.
- Uygulama sonucu:
  - Turist sohbet topbar kimligi mevcut `remoteUserId` ile typed public rehber
    profiline gidiyor; rehber sohbet topbar davranisi degismedi.

### DEG-022 - Demo Rezervasyon Iptal Politikasi Uyumu

- Durum: `UYGULANDI`
- Dogrulanan mevcut davranis:
  - Backend turist iptalinde `FULL_REFUND_48_HOURS` kodlu mevcut politikayi
    destekliyor.
  - Demo seed, rezervasyon kolonuna ve purchase snapshot'a eski
    `STANDARD_48_HOUR` kodunu yaziyor. Bu nedenle demo hesaptaki onayli yaklasan
    rezervasyon iptalinde backend guvenli bicimde `DATA_CONFLICT` donuyor.
  - Android guncel rezervasyon `version` degerini dogru gonderiyor; turistin
    iptal yetkisinde ve gercek iptal servisinde hata bulunmuyor.
- Duzeltme karari:
  - Demo seed icindeki iki eski politika kodu canonical
    `FULL_REFUND_48_HOURS` degeriyle eslestirilecektir.
  - Mevcut `guidemate_demo` veritabanindaki ilgili kolon ve JSON snapshot
    degerleri hedefli olarak guncellenecek; demo kullanicilari, mesajlari,
    yorumlari ve diger veriler silinmeyecektir.
  - Normal `guidemate_db`, Android kodu ve backend business/production kodu
    degismeyecektir. Normal veritabanina donuldugunde uygulama davranisi ayni
    kalacaktir.
- Test karari:
  - Demo contract kontrolu, onayli ve baslamamis bir demo rezervasyonunun turist
    tarafindan iptal edilebildigini dogrulamalidir.
  - Production iptal politikasini tekrar test eden gereksiz Android testi
    eklenmeyecektir.
- Uygulama sonucu:
  - Seed ve verifier canonical politika koduna guncellendi. Mevcut
    `guidemate_demo` icindeki 1300 rezervasyonun kolon ve JSON snapshot degeri
    hedefli olarak esitlendi; diger demo verileri ve `guidemate_db` korunuyor.

### DEG-023 - Rol Secimi RadioButton Ic Gostergesi Rengi

- Durum: `UYGULANDI`
- Kullanici deneyimi karari:
  - Rol secimi ekraninda bir secenek isaretlendiginde RadioButton'in merkezinde
    beliren dolu secim noktasi `BrandColor` olmalidir.
  - Degisiklik dis cemberi hedeflemez. Secilmemis RadioButton gorunumu, metinler,
    bosluklar ve ekranin mevcut tasarimi korunmalidir.
- Uygulama siniri:
  - Yalniz rol secimi ekranindaki mevcut RadioButton renk sozlesmesi en kucuk
    kapsamda duzenlenecektir; ortak component veya tema davranisi gereksiz yere
    degistirilmeyecektir.
  - Yeni component, katman veya soyutlama eklenmeyecektir.
- Test karari:
  - Salt renk degisikligi icin kirilgan otomatik Compose testi yazilmayacaktir.
  - Derleme/lint ve rol seciminin gercek cihazdaki secili-secimsiz gorunum
    kontrolu yeterlidir.

### DEG-024 - Rehber Hakkimda Metninde Kosullu Devamini Gor

- Durum: `UYGULANDI`
- Dogrulanan mevcut davranis:
  - Ortak rehber profil icerigi biyografinin gercek uzunluguna bakmadan
    `Devamini Gor` aksiyonunu gosteriyor.
  - Hakkimda yuzeyine kapali ve acik durumlarda sabit yukseklik verildigi icin
    kisa biyografide aksiyon gereksiz yere gorunuyor; tiklandiginda metnin
    doldurmadigi buyuk bir bos alan olusuyor.
  - Ayni ortak `GuideProfileContent` hem rehber profil on izlemesinde hem
    turistin gordugu public rehber profilinde kullaniliyor.
- Kullanici deneyimi karari:
  - `Devamini Gor` yalniz daraltilmis biyografi belirlenen satir sinirini
    gercekten asiyorsa gosterilmelidir.
  - Kisa biyografi dogal yuksekligiyle gosterilmeli; aksiyon ve gereksiz bosluk
    olusmamalidir.
  - Uzun biyografi acildiginda metnin tamami dogal yuksekligiyle gosterilmeli;
    mevcut profil tasarimi, renkleri ve metin stilleri korunmalidir.
- Mimari ve uygulama siniri:
  - Duzeltme ortak `GuideProfileContent` icindeki Hakkimda bolumunde tek kez
    yapilarak on izleme ve public profil birlikte duzeltilmelidir.
  - Sabit metin alani yuksekligi yerine Compose metin tasmasi sonucu esas
    alinmali; ayri ekran kodu, backend degisikligi veya yeni soyutlama
    eklenmemelidir.
- Test karari:
  - Salt olcum ve metin gorunumu icin kirilgan otomatik UI testi yazilmayacaktir.
  - Derleme/lint sonrasinda kisa ve uzun biyografiyle iki ortak profil yuzeyi
    manuel olarak dogrulanmalidir.

### DEG-025 - Kontrollu Dunya Dilleri Katalogu ve Yerellestirilmis Dil Adlari

- Durum: `UYGULANDI`
- Dogrulanan mevcut davranis:
  - Android dil seceneklerini `Locale.getAvailableLocales()` uzerinden otomatik
    uretiyor ve bayragi ICU'nun tahmin ettigi bolgeden aliyor.
  - Bu nedenle Baskca ve Katalanca gibi urun kapsaminda istenmeyen bolgesel
    diller listelenebiliyor; Portekizce gibi dillerde temsilci bayrak cihaz
    verisine gore Brezilya gibi istenmeyen bir bolgeye kayabiliyor.
  - Hakkimda, tur yayinlama/duzenleme ve Kesfet filtreleri ortak dil seciciyi
    kullansa da kart, profil, tur ve rezervasyon gorunumleri ayni kodlar icin
    katalogdan etiket ve bayrak uretiyor.
- Urun ve kullanici deneyimi karari:
  - GuideMate, dunyada yaygin veya ulke duzeyinde kullanilan ana dilleri genis
    fakat kontrollu bir katalogda sunmalidir. Hirvatca ve Rusca gibi ana diller
    korunmali; urun kapsaminda istenmeyen bolgesel/ozel dil ve locale
    varyantlari listelenmemelidir.
  - Her dil yalniz bir canonical dil koduyla bir kez bulunmalidir. ABD
    Ingilizcesi, Brezilya Portekizcesi veya Meksika Ispanyolcasi gibi ayni dilin
    ulke varyantlari ayri secenek olmamalidir.
  - Her dil icin urun tarafindan belirlenen sabit bir temsilci bayrak
    kullanilmalidir. Ornegin Portekizce `PT`, Ispanyolca `ES` ve Ingilizce `GB`
    bayragiyla gosterilmeli; cihaz/ICU bayrak tahmini kullanilmamalidir.
  - Ayni katalog dilin kullanildigi tum Android yuzeylerinin tek kaynagi
    olmalidir: rehber Hakkimda, tur yayinlama ve duzenleme, Kesfet filtresi,
    rehber profili, tur kartlari/detaylari ve rezervasyon gorunumleri.
- Yerellestirme karari:
  - Backend ve domain yalniz standart dil kodunu tasimalidir; yerellestirilmis
    dil adi veya bayrak saklamamalidir.
  - Gorunen dil adi, ortak katalogdaki koddan Android `Locale/ICU` ile aktif
    cihaz/uygulama diline gore otomatik uretilmelidir. Ornegin `en`, Turkce
    ortamda `Ingilizce`, Ingilizce ortamda `English` olarak gorunmelidir.
  - Yuzlerce dil adi XML'e tek tek yazilmayacaktir. Bu otomatik dil adi
    yerellestirmesi normal UI metinlerini cevirmez; buton ve basliklarin
    Ingilizcelesmesi icin ileride yine `values-en` kaynaklari gerekir.
- Mimari ve uygulama siniri:
  - Mevcut ortak `LocaleSelectionCatalog` urun kontrollu canonical dil/bayrak
    kaynagi olacak sekilde duzenlenmeli; ekranlara ayri listeler veya mapperlara
    tekrar eden bayrak tablolari eklenmemelidir.
  - Android'in gonderecegi kodlar backend `LanguageCodePolicy` tarafindan kabul
    edilen standart kodlarla uyumlu kalmalidir. Sozlesme uyumsuzlugu yoksa
    backend is kurali veya veritabani degistirilmemelidir.
  - Mevcut kayitlarda katalog disi bir kodla karsilasilirsa uygulama cokmemeli;
    guvenli kod/varsayilan ikon fallback'i korunmalidir.
- Test karari:
  - Ortak katalog testi canonical kodlarin benzersizligini, bos/uygunsuz kod
    bulunmadigini ve sabit temsilci bayraklarin kararlililigini dogrulamalidir.
  - Turkce ve Ingilizce locale icin secili orneklerin yerellestirilmis dil
    adlari odakli olarak dogrulanmalidir; her dil icin tekrar eden test
    yazilmamalidir.
  - Dil secici ve kartlarin salt gorunumu manuel kullanici testinde kontrol
    edilmelidir.

### DEG-026 - Tur Yayinlama Alan Dogrulamalarinin Backend ile Uyumu

- Durum: `UYGULANDI`
- Dogrulanan mevcut davranis:
  - Android tur yayinlama adimlarinda konum/zaman, kategori/dil/fiyat/kapasite
    ve tur icerigi icin yerel kontrol yapiyor; ancak bazi metin alanlarinda
    yalniz bos olup olmadigina bakiyor.
  - Backend tur adi icin `3-120`, tur aciklamasi icin `20-3000`, bulusma
    noktasi icin en fazla `500` karakter gibi daha ayrintili kurallari son
    otorite olarak uyguluyor.
  - Bu nedenle Android'de dolu gorunen bir alan backend sozlesmesine uymadigi
    halde kullanici son on izleme adimina gecebilir.
  - Backend `VALIDATION_FAILED` cevabinda alan bazli `fieldErrors` donduruyor ve
    ortak Android parser bu bilgiyi koruyor; yayinlama akisi ise ayrintiyi
    kullanmadan genel `Lutfen girdiginiz bilgileri kontrol edin` mesajini
    gosteriyor.
- Kullanici deneyimi karari:
  - Android'deki kullanici tarafindan girilebilen alan kontrolleri backend
    sozlesmesiyle uyumlu olmalidir. Gecersiz alan varsa kullanici ilgili
    adimdan sonraki adima, dolayisiyla son on izlemeye gecememelidir.
  - Hata ilgili alan veya adimda acik ve XML kaynakli metinle gosterilmelidir;
    ornegin `Tur aciklamasi en az 20 karakter olmalidir.`
  - Backend ayni kontrolleri guvenlik ve veri butunlugu icin koruyacak ve son
    otorite olmaya devam edecektir. Android kontrolu backend kontrolunun yerine
    gecmeyecektir.
  - Backend yine reddederse `fieldErrors` icindeki alan bilgisi ilgili yayinlama
    adimina ve kullanicinin anlayacagi XML mesajina eslenmelidir. Teknik alan
    yolu veya backend exception metni dogrudan UI'a yazilmamalidir.
  - Kullanici serbest dil/ulke kodu yazmadigi icin teknik kod dogrulamalari UI'da
    gereksiz yere tekrar edilmeyecektir. Android secim zorunlulugu ve urun
    limitini uygular; backend kod formati ve izinli katalog icin savunma
    kontrolunu korur.
- Mimari ve uygulama siniri:
  - Dogrulama yayinlama feature'inin gercek form sorumlulugunda kalmali; ortak
    hata altyapisi feature'a ozel tur form kurallarini bilmemelidir.
  - Ayni kural adim gecisi ve son yayinlama oncesi kontrolde tek kaynaktan
    kullanilmali; iki farkli kosul listesi olusturulmamalidir.
  - Yeni use-case, genel validation framework'u veya gereksiz katman
    eklenmemelidir. Mevcut state/ViewModel ve `AppError.fieldErrors` sozlesmesi
    en kucuk kapsamda kullanilmalidir.
  - Backend sozlesmesi zaten dogru oldugu icin backend kurali gevsetilmeyecek;
    yalniz canli alan adlari/kodlari Android eslemesiyle dogrulanacaktir.
- Test karari:
  - Adim 1-3 icin sinir degerleri, ozellikle tur adi ve aciklama minimum/maksimum
    uzunluklari, ViewModel veya saf form dogrulama testleriyle korunmalidir.
  - Backend `fieldErrors` cevabinin dogru yayinlama adimina ve mesaja eslenmesi
    odakli olarak test edilmelidir.
  - Salt hata metninin rengi veya yerlesimi icin kirilgan UI testi
    yazilmayacaktir; gorunum manuel kullanici testinde dogrulanacaktir.

### DEG-027 - Tur Yayinlama Sonrasi Inceleme Sekmesine Donus

- Durum: `UYGULANDI`
- Dogrulanan mevcut davranis:
  - Backend yeni turu `PENDING_REVIEW` durumunda olusturuyor; bu tur Aktif
    degil, Turlarim ekranindaki Inceleme sekmesine aittir.
  - Yayinlama graph'i basari sonrasinda `GuideTourTab.REVIEW` sonucunu yazip
    Turlarim destination'ina geri donmeyi amacliyor.
  - Sonuc navigation back-stack entry'nin `SavedStateHandle` alanina yazilirken
    `GuideMyToursViewModel` kendi enjekte edilen `SavedStateHandle` ornegini
    dinliyor. Sonuc ayni kanaldan tuketilmediginde varsayilan `ACTIVE` sekmesi
    ekranda kalabiliyor.
- Kullanici deneyimi karari:
  - `Onaya Gonder` islemi backend tarafinda basariyla tamamlandiginda Turlarim
    ekrani acilmali ve `Inceleme` sekmesi secili olmalidir.
  - Inceleme listesi yenilenerek yeni gonderilen tur backend'den gorunmelidir.
  - Basarisiz yayinlama isteginde kullanici Turlarim ekranina
    yonlendirilmemeli; acik hata mesaji ile yayinlama ekraninda kalmalidir.
- Mimari ve navigation karari:
  - Destination/navigation sonucu Turlarim composable sinirinda dogru
    back-stack entry'den okunup ViewModel'e acik bir sekme secme olayi olarak
    iletilmelidir.
  - Sonuc bir kez tuketilip temizlenmeli; ekran yeniden olustugunda veya geri
    donuldugunde eski `REVIEW` komutu tekrar calismamalidir.
  - Yayinlama, tur duzenleme ve tur detayindan donus ayni sonuc iletim
    yaklasimiyla tutarli olmali; yeni global navigation store, yeni graph veya
    dorduncu genel navigate extension'i eklenmemelidir.
  - Mevcut typed destination ve geri-yigin davranisi korunmalidir.
- Test karari:
  - Yayinlama basari sonucunun `REVIEW` sekmesini sectigi, ilgili listeyi
    yeniledigi ve sonucu yalniz bir kez tukettigi odakli state/navigation
    testiyle korunmalidir.
  - Yayinlama hatasinda yonlendirme olmadigi mevcut ViewModel testiyle
    kapsanmiyorsa eklenmelidir; ayni davranisi tekrar eden kirilgan tam UI testi
    yazilmayacaktir.

### DEG-028 - Rehber Bildirimleri ve Ana Sayfa Canli State Uyumu

- Durum: `UYGULANDI`
- Canli veriyle dogrulanan mevcut davranis:
  - Admin onayi backend'de dogru rehber icin `TOUR_APPROVED` bildirimi
    olusturuyor. Bildirim veritabaninda kalici ve topbar REST yenilemesinde
    gorunuyor.
  - Demo profilinde `DEMO_FCM_ENABLED=false` oldugu icin backend gercek Firebase
    gondericisi yerine no-op push gondericisi kullaniyor. Uygulama acikken
    notification refresh tetigi gelmiyor; cikis-giris veya lifecycle resume
    sonrasinda REST yenilemesiyle bildirim gorunuyor.
  - `GuideNavigation` topbar ve bottom sheet icin canli
    `NotificationUiState` degerini okuyor. Buna karsilik `guideNavGraph`, graph
    ilk kurulurken bos olan `recentNotifications` listesini destination
    closure'inda tutuyor. Repository sonradan guncellense bile rehber ana
    sayfasindaki Son Hareketler ilk bos listeyi gostermeye devam edebiliyor.
  - Okunmus bildirim backend'den veya Son Hareketler'den silinmiyor. Topbar
    rozeti yalniz okunmamis sayisini, topbar listesi ve Son Hareketler ise okunmus
    bildirimler dahil guncel gecmisi gostermelidir.
  - Rehber dashboard yenileme tetikleri tur onayi ve reddini kapsamiyor.
    `PENDING_REVIEW` ile aktif oturum sayilari degistigi halde ana sayfa
    sayaclari eski kalabilir.
- Kullanici deneyimi karari:
  - Yeni bildirim geldiginde topbar listesi, okunmamis rozeti ve rehber ana
    sayfasindaki en son dort hareket ayni repository kaynagindan ve ayni anda
    guncellenmelidir.
  - `TOUR_APPROVED` ve `TOUR_REJECTED` bildirimleri Son Hareketler'de XML
    kaynakli mevcut metin ve tur ikonu ile gorunmelidir.
  - Okundu islemi yalniz okunmamis sayisini azaltmali; bildirimi Son
    Hareketler'den veya bildirim gecmisinden kaldirmamalidir.
  - Tur yayinlama, onay ve red sonrasinda dashboard `Incelemede`/`Aktif`
    sayaclari canonical backend projection'iyla yenilenmelidir; Android manuel
    `+1/-1` yapmamalidir.
- Android mimari ve uygulama siniri:
  - Degisebilen bildirim listesi navigation graph builder'a snapshot `List`
    olarak verilmemelidir. Home destination canli ve lifecycle-aware state'i
    gozlemlemeli; navigation dinamik UI verisini eski closure'da tutmamalidir.
  - Var olan `NotificationRepository` tek kaynak olarak korunacak; ana sayfa
    icin ikinci store, kopya bildirim listesi, polling sistemi veya notification
    STOMP kanali eklenmeyecektir.
  - FCM olayi geldiginde mevcut `onPushReceived -> REST refresh` akisi
    korunacak. REST cevabi bildirim gecmisinin ve okunmamis sayisinin canonical
    kaynagi olmaya devam edecektir.
  - Dashboard yenilemesi yalniz gercekten dashboard projection'ini etkileyen
    tur karar olaylarina eklenecek; tum bildirimler gereksiz yere dashboard
    istegi baslatmayacaktir.
- Yerel demo yapilandirma karari:
  - Demo ortaminda anlik ve arka plan push kullanici testi yapilacagi zaman
    local/Git disi ayarda `DEMO_FCM_ENABLED=true` kullanilacak ve backend yeniden
    baslatilacaktir.
  - Mevcut Firebase service-account dosyasi ve Android device registration
    sozlesmesi korunacak; secret source control'e eklenmeyecektir.
  - Normal local/production FCM ayarlari demo veritabani seciminden bagimsizdir;
    demo veya gercek veritabani tek basina anlik teslim davranisini belirlemez.
- Backend siniri:
  - `TOUR_APPROVED`/`TOUR_REJECTED` notification olusturma kodu ve kalici
    notification modeli dogru calistigi icin sirf bu hata icin yeni bildirim
    turu, tablo, endpoint veya backend business akisi eklenmeyecektir.
  - Backend kodu yalniz canli dogrulamada ayrica bir sozlesme hatasi bulunursa
    degistirilecektir; mevcut bulgu Android state aktarimi ve demo dis
    yapilandirmasiyla sinirlidir.
- Test karari:
  - Repository bildirimleri bos durumdan dolu duruma gectiginde rehber ana sayfa
    son hareketlerinin guncellenmesi odakli state testiyle korunmalidir.
  - Okunmus bildirimin listede kalmasi ve yalniz unread count'un azalmasi mevcut
    repository/ViewModel testlerinde kapsanmiyorsa tek odakli test eklenmelidir.
  - Tur onay/red olaylarinin dashboard refresh tetigini calistirmasi test
    edilmelidir; salt Compose metin/ikon yerlesimi icin kirilgan UI testi
    yazilmayacaktir.
  - FCM gercek teslimi otomatik unit testle taklit edilmeyecek; demo backend,
    gercek emulator/telefon ve Firebase ile manuel E2E kullanici testinde
    dogrulanacaktir.

### DEG-029 - Rehber Dashboard Tur Yasam Dongusu Yenilemesi

- Durum: `UYGULANDI`
- Dogrulanan mevcut backend davranisi:
  - Rehber ana sayfasindaki `Yayinda` sayisi yalniz onayli, gelecekte baslayacak
    ve `OPEN_FOR_BOOKING` durumundaki oturumlardan hesaplanir.
  - Rehber bir oturumu iptal ettiginde backend durumu `CANCELLED` yapar. Bu
    oturum dashboard aktif sorgusundan hemen cikar; yeniden okunan canonical
    dashboard sonucunda `Yayinda 1` degeri `Yayinda 0` olmalidir.
  - Iptal edilen oturum tamamlanmis sayilmaz. `Tamamlanan Tur` sayisini
    artirmaz, ancak terminal `CANCELLED` durumu nedeniyle Turlarim `Gecmis`
    sekmesinde gorunur.
  - Normal bir turun baslangic ani gelince artik gelecekte ve satin alinabilir
    aktif oturum sayilmaz; `Yayinda` sayisindan cikar. Turun planlanan suresi
    bitene kadar devam eden turdur ve henuz `Tamamlanan Tur` sayisini artirmaz.
  - Turun planlanan bitis zamani gectiginde backend lifecycle scheduler'i en
    gec yaklasik bir dakikalik normal gecikmeyle oturumu `COMPLETED` yapar,
    ilgili rezervasyonlari tamamlar ve `TOUR_COMPLETED` bildirimi olusturur.
    Bundan sonra oturum Turlarim `Gecmis` sekmesinde gorunmeli ve rehberin
    `Tamamlanan Tur` sayisi artmalidir.
- Dogrulanan Android eksikligi:
  - Basarili iptal, tur detayini kapatip Turlarim `Gecmis` sekmesini yeniliyor;
    ancak yasamaya devam eden `GuideHomeViewModel` icin dashboard refresh
    tetiklemiyor. Bu nedenle backend dogru olarak `0` dondurecek olsa bile ana
    sayfa bellekteki eski `Yayinda 1` degerini gosterebiliyor.
  - Dogal tamamlanmada `TOUR_COMPLETED` dashboard yenileme olaylari arasinda
    bulunuyor. Bunun anlik calismasi DEG-028'deki FCM ve canli state
    duzeltmesiyle birlikte korunmalidir; REST dashboard her durumda canonical
    kaynak olmaya devam etmelidir.
- Kullanici deneyimi karari:
  - Iptal istegi basarili oldugunda Turlarim listesi ile rehber dashboard'u ayni
    backend sonucuyla uyumlu hale gelmelidir: `Yayinda` hemen azalir, iptal
    edilen oturum Gecmis'e gider ve `Tamamlanan Tur` artmaz.
  - Normal tur tamamlandiginda `Yayinda` azalir, `Tamamlanan Tur` artar ve tur
    Gecmis'e gider. Iptal edilen tur ile tamamlanan tur ayni istatistik olarak
    degerlendirilmez.
  - Android sayaçlara elle `+1/-1` uygulamaz; iptal, yeni oturum ve backend
    lifecycle sonucu sonrasinda gerekli projection'i yeniden okuyarak gercek
    degeri gosterir.
- Mimari karar:
  - Sadece bu is icin global store, ikinci dashboard kaynagi veya yeni genel
    navigation extension'i eklenmeyecektir.
  - Basarili oturum komutunun sonucu, ilgili feature sinirindan mevcut
    graph-scope `GuideHomeViewModel` icin acik ve tek seferlik bir yenileme
    tetigine donusturulecek. UI katmani backend tur durumunu tahmin etmeyecek.
  - DEG-028 ile birlikte ele alinarak bildirim kaynakli ve rehberin kendi
    aksiyonundan kaynakli yenilemeler tek canonical dashboard repository
    cagrisinda birlesmelidir.
- Test karari:
  - Basarili iptal sonucunun dashboard yenilemesini tetikledigi ve yeni backend
    projection'inin `activeCount` degerine yansidigi odakli Android state testi
    yazilmalidir.
  - Backend aktif sorgusunun `CANCELLED` oturumu dislamasi, iptalin tamamlanan
    sayisini artirmamasi ve lifecycle tamamlanmasinin Gecmis/tamamlanan
    projection'ina yansimasi mevcut testlerde kapsanmiyorsa yalniz eksik kalan
    business davranisi icin odakli backend testi eklenmelidir.
  - Scheduler'in gercek bir dakika beklemesi otomatik test edilmeyecek; testte
    kontrol edilebilir `Clock` ve dogrudan lifecycle calistirmasi kullanilacak.

### DEG-030 - Rehber Tur Islemleri Sonrasi Dogru Sekmeye Donus

- Durum: `UYGULANDI`
- Dogrulanan mevcut davranis:
  - Gecmis tur detayindan yeni tarihli oturum olusturuldugunda backend yeni
    oturumu `OPEN_FOR_BOOKING` olarak dogru bicimde kaydediyor ve Android
    ViewModel hedef sekmeyi `ACTIVE` olarak belirliyor.
  - Buna ragmen navigation sonucu onceki back-stack entry'nin
    `SavedStateHandle` alanina yaziliyor. Sonucun tuketilmesi onceki ekranin
    gercekten Turlarim olmasina ve ayni handle'in dinlenmesine bagli oldugu icin
    kullanici yeniden yayinlama sonrasinda Gecmis sekmesinde kalabiliyor.
  - Ayni kirilgan sonuc kanali iptal sonrasi `PAST`, tur/oturum duzenleme
    sonrasi `ACTIVE` veya `REVIEW` secimlerinde de kullaniliyor. Tur detayi
    bildirim ya da baska bir giris noktasindan acilirsa onceki destination
    Turlarim olmayabilir.
  - Yeni tur yayinlama sonrasi `REVIEW` hedefi DEG-027 kapsaminda zaten
    kayitlidir; bu madde ayni navigation sonucunun diger rehber tur islemleriyle
    tutarli hale getirilmesini kapsar.
- Kullanici deneyimi karari:
  - Gecmis turdan yeni tarihli oturum basariyla olusturulunca Turlarim acilmali,
    `Aktif` sekmesi secilmeli ve liste backend'den yenilenerek yeni oturum
    gorunmelidir.
  - Aktif oturum iptal edilince Turlarim `Gecmis` sekmesine donmeli ve iptal
    edilen oturum orada gorunmelidir.
  - Yalniz oturum bilgileri degistirildiyse `Aktif`; tur icerigi yeniden admin
    onayi gerektiriyorsa `Inceleme` sekmesine donulmalidir.
  - Yeni tur yayinlama basarisi `Inceleme` sekmesine donmeye devam etmelidir.
  - Basarisiz islemde herhangi bir sekmeye yonlendirme yapilmamali; kullanici
    mevcut ekranda acik hata mesaji almalidir.
- Navigation ve mimari karari:
  - Sonuc iletimi, Turlarim ekraninin navigation gecmisinde tesadufen onceki
    entry olmasina bagli kalmamalidir. Mevcut typed destination kullanilarak
    varsa Turlarim entry'sine donulecek, yoksa Turlarim guvenli bicimde acilacak
    ve hedef sekme tek seferlik sonuc olarak tuketilecektir.
  - Yeni ekran, global navigation store, yeni genel navigate extension'i veya
    ViewModel'de `NavController` bagimliligi eklenmeyecektir.
  - Hedef sekme secimi tek bir rehber tur navigation sonuc sozlesmesinde
    tutulacak; yayinlama, iptal, yeniden yayinlama ve duzenleme icin kopya
    back-stack kodu olusturulmayacaktir.
  - DEG-027 ve DEG-029 ile birlikte uygulanarak liste sekmesi, dashboard
    projection'i ve backend tur durumu ayni basarili islem sonrasinda tutarli
    yenilenmelidir.
- Benzer akis taramasi sonucu:
  - Reddedilen taslagi arsivleme ayni `Inceleme` listesinde yerel olarak
    kaldirdigi icin yeni destination gerektirmez.
  - Aktif oturumun rezervasyona acik/kapali switch'i ayni kart uzerinde
    guncellendigi icin sekme degistirmemelidir.
  - Normal geri ikonlari islem sonucu degildir; mevcut `navigateUp()` davranisi
    korunmalidir.
- Test karari:
  - Yeni oturum basarisinin `ACTIVE`, iptal basarisinin `PAST`, yeniden onay
    gerektiren duzenlemenin `REVIEW` sonucunu urettigi mevcut ViewModel
    testlerinde eksik kalan senaryolarla korunmalidir.
  - Sonucun Turlarim entry'si onceki ekran olmadiginda da dogru destination ve
    sekmeye ulastigi odakli navigation sonucu testiyle korunmalidir.
  - Salt tab gorunumu icin tekrarlayan kirilgan Compose testi yazilmayacak;
    gercek geri-yigin davranisi manuel kullanici testinde de dogrulanacaktir.

### DEG-031 - Bildirim Bottom Sheet Okundu Aksiyonu Yerlesimi

- Durum: `UYGULANDI`
- Dogrulanan mevcut gorunum:
  - `Tumunu okundu yap` aksiyonu `Bildirimler` basligiyla ayni header satirinda
    ve sag ustte gosteriliyor.
- Tasarim karari:
  - `Bildirimler` basligi mevcut merkez konumunda tek basina kalacak.
  - `Tumunu okundu yap` aksiyonu bildirim listesinin disinda, bottom sheet'in
    sag altinda yer alan sabit bir footer aksiyonu olarak gosterilecek.
  - Uzun bildirim listesinde aksiyon liste item'i olarak scroll icine
    alinmayacak; bottom sheet acikken erisilebilir kalacak.
  - Aksiyon yalniz okunmamis bildirim varsa gorunecek, devam eden istek
    sirasinda devre disi kalacak ve mevcut brand color/metin stili korunacak.
  - Bildirim listesi, pagination, okundu davranisi, bottom sheet yuksekligi ve
    diger tasarim ogeleri degistirilmeyecek.
- Mimari ve backend siniri:
  - Bu yalniz Compose yerlesim degisikligidir. Mevcut `onMarkAllRead` callback'i
    ve `NotificationViewModel` kullanilacak; yeni state, repository veya
    backend degisikligi eklenmeyecektir.
- Test karari:
  - Is kurali degismedigi icin salt konum icin kirilgan UI testi
    yazilmayacaktir. Derleme/lint ve bottom sheet'in okunmamis, islemde ve tumu
    okunmus durumlari manuel kullanici testiyle dogrulanacaktir.

### DEG-032 - Yeni Tur Yayinlama Sonrasi Onay Bekleyen Sayaci

- Durum: `UYGULANDI`
- Dogrulanan mevcut davranis:
  - Backend yeni turu `PENDING_REVIEW` durumunda kaydediyor ve rehber dashboard
    projection'i bu turu `pendingReviewCount` hesabina dogru bicimde dahil
    ediyor.
  - Android yayinlama basarisinda Turlarim ekranina donuyor, ancak yasamaya
    devam eden `GuideHomeViewModel` icin dashboard yenilemesi tetiklemiyor.
    Bu nedenle backend'deki gercek deger `1` oldugu halde ana sayfa bellekteki
    eski `Onay Bekliyor 0` degerini gosterebiliyor.
  - Rehberin kendi gonderimi icin ayrica FCM bildirimi beklemek dogru senkron
    mekanizmasi degildir; basarili komut sonucu dashboard'u gecersiz kilmalidir.
- Kullanici deneyimi karari:
  - Yeni tur basariyla onaya gonderildiginde Turlarim `Inceleme` sekmesine
    donulecek ve ana sayfadaki `Onay Bekliyor` sayisi backend projection'i
    yeniden okunarak ayni anda guncellenecektir.
  - Ornek olarak once bekleyen tur yoksa basarili yayinlama sonrasinda deger
    `0 -> 1` olmalidir. Birden fazla bekleyen tur varsa Android elle artirma
    yapmayacak, backend'in toplam degerini gosterecektir.
  - Admin onayi veya reddi sonrasinda ayni projection yeniden okunarak
    `Onay Bekliyor` azaltilacak; onaylanan gelecekteki acik oturum `Yayinda`
    sayisina backend kurallarina gore dahil edilecektir.
- Mimari karar:
  - DEG-027'deki dogru `Inceleme` sekmesine donus, DEG-028'deki admin karar
    bildirimi ve DEG-029'daki dashboard lifecycle yenilemesiyle tek tutarli
    sonuc akisi olarak uygulanacaktir.
  - Var olan `GuideTourRepository.getDashboard()` canonical kaynak olarak
    korunacak; ikinci sayaç state'i, global store, polling veya Android tarafli
    `+1/-1` hesabi eklenmeyecektir.
  - Yayinlama basari olayi mevcut graph-scope `GuideHomeViewModel` icin acik ve
    tek seferlik refresh tetigi uretmelidir. ViewModel navigation veya backend
    DTO ayrintisini bilmemelidir.
- Backend siniri:
  - `PENDING_REVIEW` kaydi ve dashboard sorgusu dogru oldugu icin yeni endpoint,
    tablo veya backend business degisikligi gerekmemektedir.
- Test karari:
  - Basarili yayinlama sonucunun `REVIEW` sekmesiyle birlikte dashboard
    yenilemesini tetikledigi ve backend'den donen `pendingCount` degerinin ana
    sayfa state'ine yansidigi odakli Android testiyle korunmalidir.
  - Salt sayac metni icin kirilgan Compose testi yazilmayacak; `0 -> 1`, admin
    onayi sonrasi bekleyen sayinin azalmasi ve Yayinda degerinin guncellenmesi
    manuel kullanici testinde de dogrulanacaktir.

### DEG-033 - Katilimcisiz Tur Oturumunun EXPIRED Olmasi

- Durum: `UYGULANDI`
- Dogrulanan mevcut risk:
  - Backend lifecycle scheduler'i planlanan suresi biten `OPEN_FOR_BOOKING` ve
    `CLOSED` oturumlari katilimci bulunup bulunmadigina bakmadan `COMPLETED`
    yapiyor.
  - Bu nedenle hic satin alinmamis veya tum rezervasyonlari iptal edilmis bir
    oturum, yalniz tarihi gectigi icin rehberin `Tamamlanan Tur` sayisini ve
    seviye/performans projection'larini haksiz bicimde artirabilir.
  - Sorun turun gizli olmasi degildir. `CLOSED`, yeni satin alimi durdurur;
    mevcut gecerli rezervasyonlari ve turun gerceklesmesini iptal etmez.
- Backend yasam dongusu karari:
  - Planlanan bitis zamani gecen oturumda en az bir gecerli
    `CONFIRMED`/tamamlanabilir rezervasyon varsa mevcut akis korunarak oturum
    `COMPLETED` yapilacak ve rezervasyonlar tamamlanacaktir.
  - Gecerli rezervasyon yoksa oturum `CANCELLED` veya `COMPLETED` yerine yeni
    terminal `EXPIRED` durumuna gecirilecektir. Bu durum rehber ya da turist
    iptali anlamina gelmez ve iptal nedeni/aktoru uydurulmayacaktir.
  - `EXPIRED` oturum rehber Turlarim `Gecmis` sorgusuna dahil edilecek; aktif ve
    turist kesif/satin alma sorgularindan dislanacaktir.
  - `EXPIRED` oturum tamamlanan tur, toplam katilimci, ortalama puan, rozet/seviye
    ilerlemesi veya kazanc hesaplarini artirmayacaktir. Yorum ve puan olusturma
    yetkisi vermeyecek, iade veya kazanc kaydi uretmeyecektir.
  - Gizlenmis fakat en az bir gecerli rezervasyonu bulunan oturum, planlanan
    suresi bittiginde normal `COMPLETED` olmaya devam edecektir.
- Android sozlesmesi ve tasarim karari:
  - Android session status enum/DTO mapper/detail status eslemesi `EXPIRED`
    degerini tip guvenli bicimde destekleyecektir; bilinmeyen string fallback'i
    ile gizlenmeyecektir.
  - Yeni ekran veya yeni kart tasarimi yapilmayacak. Mevcut Gecmis karti ve tur
    detay yerlesimi korunacaktir.
  - Mevcut durum alaninda XML kaynakli `Gerceklesmedi` metni, detayda ise
    `Katilimci olmadigi icin gerceklesmedi` aciklamasi gosterilecektir.
  - `EXPIRED` kart ve detayinda kazanc, puan verme ve yorum yapma aksiyonlari
    gosterilmeyecektir. Salt yerlesim, renk ve tipografi degistirilmeyecektir.
  - Dashboard ve liste state'i Android tarafinda tahmin edilmeyecek; DEG-029
    kapsamindaki canonical backend refresh davranisi kullanilacaktir.
- Bildirim karari:
  - `TOUR_COMPLETED` bildirimi yalniz gercekten `COMPLETED` olan oturum icin
    uretilmelidir. `EXPIRED` oturum icin tamamlandi veya yorum yapabilirsiniz
    bildirimi gonderilmemelidir.
  - Sirf bu durum icin yeni push bildirimi zorunlu degildir; rehber Gecmis
    sekmesinde sonucu gorebilir. Ileride urun karari olmadan yeni notification
    tipi eklenmeyecektir.
- Veritabani ve migration siniri:
  - Backend enum/check constraint mevcutsa `EXPIRED` degerini ekleyen kontrollu
    Flyway migration yazilacaktir. Mevcut kayitlar topluca yeniden
    siniflandirilmayacak; yalniz kanitlanabilen test/demo kayitlari ayri ele
    alinacaktir.
- Test karari:
  - Gecerli rezervasyonu olan gizli oturumun `COMPLETED`, rezervasyonsuz acik
    veya gizli oturumun `EXPIRED` oldugu kontrollu `Clock` ile backend lifecycle
    testlerinde korunmalidir.
  - `EXPIRED` oturumun Gecmis'te yer alip tamamlanan/katilimci/puan/kazanc
    projection'larini etkilemedigi odakli integration testleri yazilmalidir.
  - Android enum/DTO mapper ve detay modu `EXPIRED` eslemesi test edilmelidir;
    salt metin konumu icin kirilgan Compose testi yazilmayacaktir.

### DEG-034 - Hosted Odeme Dili ve Guvenli Geri/Iptal Akisi

- Durum: `UYGULANDI`
- Dogrulanan mevcut davranis:
  - iyzico hosted sayfasindaki `Sandbox`, kart sahibi ve odeme butonu gibi
    alanlar GuideMate Compose UI'i degil, provider tarafindan uretilen WebView
    icerigidir.
  - `Sandbox` test ortami gostergesidir; uygulama tarafindan DOM/CSS/JavaScript
    mudahalesiyle gizlenmeyecek ve production iyzico ortaminda kendiliginden
    kaybolacaktir.
  - Android odeme dilini su anda `Locale.getDefault()` ile cihaz dilinden
    belirliyor. GuideMate yalniz Turkce XML kaynaklari kullandigi icin uygulama
    Turkce gorunurken cihaz dili Ingilizceyse iyzico'ya `EN` gonderilebiliyor ve
    hosted alanlar `Card holder` gibi Ingilizce gorunuyor.
  - Hosted sayfanin altindaki `Odemeyi Iptal Et` GuideMate'in `EditButton`
    bilesenidir. iyzico'nun kendi tutarli `Odeme Yap` butonuyla alt alta gelerek
    gereksiz gorsel agirlik olusturuyor.
- Dil karari:
  - Mevcut GuideMate surumu yalniz Turkce oldugu icin hosted checkout
    baslatilirken merkezi olarak `CheckoutLocale.TR` gonderilecektir;
    `Locale.getDefault()` odeme provider dili icin kullanilmayacaktir.
  - Backend mevcut locale sozlesmesiyle `TR` degerini iyzico'ya `tr` olarak
    iletmeye devam edecektir; yeni endpoint veya tablo gerekmemektedir.
  - Ileride gercek `values-en` kaynaklari ve uygulama dili secimi eklendiginde
    sabit `TR`, cihaz diline degil secilen uygulama diline bagli `TR/EN`
    eslemesiyle degistirilecektir.
  - Dil yeni checkout oturumu olusturulurken belirlenir. Daha once olusturulmus
    Ingilizce hosted URL sonradan Turkcelestirilmeyecek; yeni odeme denemesiyle
    dogrulanacaktir.
- Tasarim ve kullanici akisi karari:
  - Hosted sayfanin altindaki GuideMate `Odemeyi Iptal Et` butonu tamamen
    kaldirilacak; iyzico sayfasinin kendi `Odeme Yap` butonuna mudahale
    edilmeyecektir.
  - Mevcut `Guvenli Odeme` topbar'inda geri ikonu gosterilecektir.
  - Topbar geri ikonu ve telefonun sistem geri tusu ayni davranisi kullanacak:
    mevcut ortak dialog ile `Odemeyi iptal etmek istiyor musunuz?` onayi
    istenecektir.
  - Kullanici vazgecerse hosted sayfada kalacak. Onaylarsa mevcut
    `cancelPayment(paymentId)` backend istegi calisacak; basarili sonucta mevcut
    payment status dogrulama akisi acilacak, hata halinde hosted ekranda kalinip
    merkezi hata mesaji gosterilecektir.
  - Geri ikonu yalniz `navigateUp()` yapmayacak; backend'deki pending odemeyi
    sahipsiz birakmayacaktir. Iptal devam ederken tekrar tiklama engellenecektir.
  - WebView yatay/dikey gorunumde mevcut kendi dikey kaydirma davranisini
    koruyacak; yeni yerlesim veya provider icerigine mudahale eklenmeyecektir.
- Navigation ve mimari sinir:
  - Yeni ekran, destination, NavGraph, genel navigate extension'i, global odeme
    store'u veya ViewModel icinde `NavController` bagimliligi eklenmeyecektir.
  - Mevcut `TouristPaymentNavGraph`, hosted destination, topbar config ve
    `HostedPaymentViewModel.cancelPayment()` akisi en kucuk gerekli callback
    siniriyla kullanilacaktir.
  - Topbar ve sistem geri olayi ayni tek iptal talebi/dialog state'ini
    kullanacak; iki ayri iptal implementasyonu yazilmayacaktir.
- Test karari:
  - Mevcut uygulama dil sozlesmesinin checkout icin `TR` urettigi ve hem tur
    satin alma hem wallet top-up isteklerinin bu degeri gonderdigi odakli
    Android testleriyle korunmalidir.
  - Geri isteginin dogrudan navigation yapmadigi, onaydan sonra tek iptal istegi
    baslattigi, vazgecmede odeme ekraninda kaldigi ve backend hatasinda cikis
    yapmadigi mevcut ViewModel/state sinirinda test edilmelidir.
  - iyzico'nun hosted HTML metinleri veya `Sandbox` etiketi unit/UI testle
    taklit edilmeyecek; Turkce alanlar gercek sandbox checkout kullanici
    testinde dogrulanacaktir.

### DEG-035 - Sohbet Detayinda Katilimci Profil Fotografini Yenileme

- Durum: `BEKLIYOR`
- Dogrulanan sorun:
  - Sohbet detayindaki topbar fotografi `ChatDetailViewModel` mesaj state'inden
    degil, navigation shell icindeki `ChatListViewModel` sohbet listesinden
    gelir.
  - Karsi kullanici profil fotografini degistirdiginde sohbet listesi yeniden
    cekilmedigi icin eski `avatarUrl` bellekte kalir.
  - Cikis-giris sonrasinda sohbet listesi yeniden yuklendigi icin fotograf
    guncel gorunur.
- Kullanici deneyimi karari:
  - Sohbet detayina girildiginde veya uygulama tekrar one geldiginde mevcut
    sohbet katilimcisi bilgisi yenilenmeli; kullanici cikis-giris yapmak zorunda
    kalmamalidir.
  - Sohbet ekrani acikken karsi taraf fotografini degistirirse, gercek anlik
    guncelleme STOMP profil guncelleme olayi ile saglanmalidir.
- Mimari ve uygulama siniri:
  - Mevcut `ChatRepository` ve `ChatListViewModel` ortak kaynagi korunacak;
    ikinci profil kaynagi, yeni graph veya duplicate sohbet state'i
    eklenmeyecektir.
  - Ilk duzeltme sohbet acilis/lifecycle yenilemesiyle en kucuk kapsamda
    yapilabilir. Gercek ekran-ici anlik davranis gerekiyorsa backend profil
    guncelleme olayini STOMP ile ilgili bagli cihazlara iletecek ve Android
    mevcut sohbet kaynagini yenileyecektir.
  - Surekli polling, gorunur yeni bildirim tipi veya topbar icinde dogrudan
    profil API cagrisi eklenmeyecektir.
- Test karari:
  - Sohbet acildiginda eski cache'e ragmen guncel profil fotografiyle topbarin
    yenilendigi odakli state/repository testi yazilmalidir.
  - STOMP ile baska cihazdan gelen profil guncellemesinin gercek anlik etkisi
    manuel coklu cihaz testinde dogrulanacaktir.

### DEG-036 - Ilgili Detay Acilisinda Bildirimi Okundu Yapma

- Durum: `BEKLIYOR`
- Dogrulanan sorun:
  - Kullanici bir `CHAT_MESSAGE` bildirimine bildirim kartindan degil, bottom
    bar sohbet sekmesinden ulasirsa sohbet mesaji okunur; fakat ayni mesaja ait
    uygulama bildirimi okunmamis kalabilir.
  - Ayni kopukluk, bildirim kartina basmadan ilgili tur, rezervasyon, odeme veya
    iade detayina baska bir uygulama akisindan gidildiginde de olusabilir.
- Kullanici deneyimi karari:
  - Kullanici ilgili detay ekranini basariyla actiginda yalniz o detayla
    iliskili bildirimler okunmus yapilmalidir.
  - Sohbet acilisinda ilgili `chatId` bildirimleri; tur, rezervasyon, odeme ve
    iade akislarinda ilgili `tourId`, `reservationId` veya `paymentId`
    bildirimleri hedeflenmelidir.
  - Ilgisiz bildirimler okunmamis kalmalidir. Sadece bottom bar sekmesine veya
    bildirim listesinin kendisine girmek tum bildirimleri okumamalidir.
  - Detay verisi yuklenemez veya yetki/kimlik kontrolu basarisiz olursa ilgili
    bildirim okunmus sayilmamalidir.
- Mimari ve uygulama siniri:
  - Android mevcut typed navigation target kimliklerini kullanacak; yeni
    bildirim kaynagi veya topbar icinde dogrudan domain API cagrisi
    eklenmeyecektir.
  - Backend, kullanicinin sahip oldugu ve gonderilen hedef kimligiyle eslesen
    okunmamis bildirimleri atomik ve idempotent bicimde guncellemelidir.
  - Mevcut tekil `notificationId` ile okuma ve `Tümünü Okundu Yap` akislarinin
    anlami korunacak; yeni akis yalniz ilgili hedefe gore toplu okumadir.
  - Android tarafinda bildirim sayisi elle azaltmak yerine backend sonucuyla
    canonical `unreadCount` yenilenecektir.
- Test karari:
  - Sohbet bottom bar yoluyla acildiginda ilgili bildirimlerin okundu oldugu,
    sohbet ve topbar rozetlerinin birlikte guncellendigi state/repository
    testi yazilmalidir.
  - Tur, rezervasyon ve odeme detaylarinda hedef kimlige gore yalniz ilgili
    bildirimlerin okundugu odakli backend/API testi yazilmalidir.
  - Ilgisiz bildirimlerin kaldigi, basarisiz detay yuklemesinde bildirimin
    okunmadigi ve tekrarli istegin idempotent oldugu test edilmelidir.
  - Salt bildirim ikonunun veya rozet yerlesiminin gorunumu icin kirilgan UI
    testi yazilmayacak; ilgili davranis manuel kullanici testinde de
    dogrulanacaktir.

### TEST-001 - Admin Onayi Sonrasi 12 Maddelik Son Kontrol

- Durum: `BEKLIYOR`
- Kapsam:
  - `DEG-023` - `DEG-034` arasindaki 12 degisikligin manuel kullanici testi
    basarili tamamlandi. Yalniz admin onayi sonrasi dashboard ve sekme sonucu
    ayri olarak kontrol edilecek.
- Manuel test adimlari:
  - Rehber hesabinda `Onay Bekliyor` sayacinda bekleyen tur bulunmalidir.
  - Admin, Erdem hesabinin `PENDING_REVIEW` turunu onaylamalidir.
  - Rehber tarafinda `Onay Bekliyor` sayisi azalip `Yayinda` sayisi artmalidir.
  - Onaylanan tur `Aktif` sekmesinde gorunmeli ve `TOUR_APPROVED` bildirimi
    topbar/son hareketlerde yer almalidir.
  - Sayaclar cikis-giris yapmadan, canonical backend dashboard sonucu ile
    guncellenmelidir. FCM kapali bir demo ortaminda lifecycle/manuel yenileme
    sonrasinda ayni dogru deger gorulmelidir.
- Basari kriteri:
  - Bekleyen tur `1 -> 0`, yayinlanan tur `0 -> 1` olarak dogru guncellenir;
    tur Aktif sekmesinde gorunur ve bildirim kaybolmazsa test tamamlanir.
- Test siniri:
  - Bu manuel kontrolde yeni kod yazilmayacak; yalniz mevcut admin onayi,
    bildirim, dashboard projection ve navigation sonucu dogrulanacaktir.

## Toplu Uygulama Kontrol Noktasi

- Son kaydedilen kod maddesi: `DEG-034`
- Son kaydedilen bekleyen madde: `DEG-036`
- Son kaydedilen manuel test: `TEST-001`
- Uygulama izni: DEG-023 - DEG-034 icin VERILDI ve uygulama tamamlandi.
- Kod degisikligi: DEG-001 - DEG-034 YAPILDI; DEG-035 ve DEG-036 henuz
  uygulanmadi.
- Otomatik dogrulama: Android 180 JVM testi, `ktfmtCheck`, `lintDebug` ve
  `assembleDebug` basarili. Backend PostgreSQL 18 Testcontainers ve Flyway V15
  migration dogrulamasi dahil 215 test basarili; hata, failure veya skip yok.
  DEG-023 - DEG-034 kapsamindaki publish validasyonu, canonical dil katalogu,
  navigation sekme sonucu, canli dashboard yenilemesi, `EXPIRED` yasam dongusu
  ve hosted odeme iptal/locale davranislari odakli testlerle korunuyor.
- Kullanici dogrulamasi: DEG-001 - DEG-013 ve DEG-023 - DEG-034 kapsamindaki
  manuel kontroller basarili; yalniz `TEST-001` bekliyor.
- Kapanis: DEG-014 - DEG-034 kodsal ve otomatik test olarak tamamlandi. Bu
  maddelerin gercek cihaz, demo FCM, iyzico Sandbox, navigation ve kullanici
  gorunumu kontrolleri manuel kullanici testinde ayrica dogrulanacak. Degisen
  kapsamin kullanilmayan kod/import/resource ve bos paket taramasi temiz.
- Siradaki is: `TEST-001` admin onayi sonrasi dashboard/sekme kontrolu; ardindan
  `DEG-035` ve `DEG-036` maddeleri kullanici karariyla uygulanacaktir.
  Her yeni kod degisikliginden once bu dosyadaki Altin Kural ve Test Altin
  Kurali yeniden okunmalidir.
- Baglam yenilenirse bu dosya okunur ve yalniz `BEKLIYOR`, `NETLESTIRILECEK`
  veya `ONAYLANDI` durumundaki maddeler uzerinden devam edilir.
