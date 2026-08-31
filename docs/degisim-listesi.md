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

- Durum: `ONAYLANDI`
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
- Uygulama izni: VERILMEDI; sonraki maddelerle birlikte toplu `uygula` komutu
  bekleniyor.
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

## Toplu Uygulama Kontrol Noktasi

- Son kaydedilen madde: `DEG-012`
- Uygulama izni: DEG-012 icin VERILMEDI
- Kod degisikligi: DEG-001 - DEG-011 YAPILDI; DEG-012 YAPILMADI
- Otomatik dogrulama: ktfmt, JVM testleri, Android test kaynak derlemesi, lint
  ve debug APK uretimi DEG-001 - DEG-011 icin basarili.
- Kullanici dogrulamasi: DEG-001 - DEG-011 tamamlandi.
- Kapanis: Ilk degisim listesi kapsami kodsal ve kullanici testi acisindan
  tamamlandi.
- Siradaki is: Yeni kullanici bulgularini DEG-013'ten itibaren sirayla
  kaydetmek; toplu uygulama komutundan once bu dosyadaki Altin Kural ve Test
  Altin Kurali'ni yeniden okumak.
- Baglam yenilenirse bu dosya okunur ve yalniz `BEKLIYOR`, `NETLESTIRILECEK`
  veya `ONAYLANDI` durumundaki maddeler uzerinden devam edilir.
