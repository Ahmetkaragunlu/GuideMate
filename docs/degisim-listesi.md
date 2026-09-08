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

### DEG-062 - Tur Satin Alimi Sonrasi Yaklasan Gezileri Acmak

- Durum: `ONAYLANDI`
- Basarili tur satin alimi tamamlanip kullanici sonuc ekranindaki `Tamam`
  eylemine bastiginda `Gezilerim` icindeki `Yaklasan` sekmesi acilmalidir.
- Mevcut davranista payment akisi yalniz `TouristDestination.Trips` rotasina
  gider. Bottom-bar navigation onceki destination state'ini geri yukledigi icin
  kullanici en son `Gecmis` sekmesinde kaldiysa yeni satin aldigi gelecek tur
  yerine yeniden `Gecmis` sekmesini gorebilir.
- Duzeltme yalniz basarili `TOUR_BOOKING` sonucuna ozel, tek kullanimlik bir
  `Yaklasan` sekme talebi olmalidir. Kullanici bottom bar uzerinden normal
  bicimde `Gezilerim` ekranina dondugunde son sectigi sekmenin korunmasi devam
  etmelidir; global `restoreState` davranisi kapatilmamalidir.
- Yeni ekran, graph, repository, backend endpoint'i veya kalici state katmani
  eklenmeyecektir. Navigation yalniz presentation niyetini tasiyacak; rezervasyon
  verisi mevcut `ReservationRepository` kaynagindan yuklenmeye devam edecektir.
- Test karari: Basarili tur odemesi sonrasinda tek kullanimlik talebin
  `Yaklasan` sekmesini sectigi ve normal bottom-bar donusunun son sekmeyi korudugu
  odakli navigation/state testiyle korunmalidir. Salt gorunum icin ayri kirilgan
  UI testi yazilmayacaktir.

### DEG-063 - Bekleyen Rehber Kazancini Acik Gostermek

- Durum: `ONAYLANDI`
- Turist odemesi basarili oldugunda olusan rehber kazanci, tur tamamlanana kadar
  backend otoritesinde `PENDING` kalmaya devam edecektir. Bu tutar aylik kazanca
  dahil olacak fakat cekilebilir bakiyeye veya wallet ledger hareketlerine erken
  eklenmeyecektir.
- Backend aylik kazanc projection/DTO sozlesmesinde ilgili ayda bekleyen kazanc
  bulunup bulunmadigini kesin olarak bildirecektir. Android tarih veya ekran
  verisinden durum tahmini yapmayacak; backend sonucunu domain ve UI modeline map
  edecektir. Yeni tablo acilmayacak ve mevcut tekil kazanc `status` sozlesmesi
  korunacaktir.
- Aylik kazanc satirinda mevcut tutar yalniz bir kez gosterilecek. Backend ilgili
  ay icin bekleyen kazanc bildirdiginde tutarin yaninda veya altinda yalniz
  `Beklemede` etiketi gosterilecek; metin XML kaynagindan, renk hardcoded
  `Color.Red` yerine mevcut tema/resource hata renginden alinacaktir.
- Ilgili aydaki bekleyen kazanc kalmadiginda `Beklemede` etiketi kalkacaktir.
  Yerine `Cekilebilir` etiketi veya ayni tutarin ikinci bir kopyasi
  gosterilmeyecektir.
- Bekleyen kazanc `Son Hareketler` ve `Tum Islemler` listelerine eklenmeyecektir;
  henuz gerceklesmis bir wallet ledger hareketi degildir. Kazanc backend tarafinda
  `AVAILABLE` oldugunda mevcut atomik akis cüzdani kredileyecek, cekilebilir
  bakiyeyi artiracak ve `GUIDE_EARNING` hareketini hem on izlemede hem tum
  islemlerde gosterecektir.
- Test karari: Backend aylik projection'inin bekleyen kazanc bilgisini dogru
  urettigi ve `PENDING -> AVAILABLE` gecisinin tek bir wallet kredisi olusturdugu
  test edilmelidir. Android DTO/domain/UI mapper'i ile etiket gorunurluk karari
  odakli unit testle korunmalidir; salt renk ve yerlesim icin kirilgan UI testi
  yazilmayacaktir.
