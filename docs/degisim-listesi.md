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

### DEG-056 - Iyzico Ret Cevaplarini Terminal Sonuca Donusturmek

- Durum: `UYGULANDI`
- Kullanici testinde yetersiz bakiye karti sonrasinda odemenin `VERIFYING`
  durumunda kaldigi dogrulandi.
- Canli Sandbox incelemesinde iyzico `status=failure`,
  `paymentStatus=FAILURE` ve `errorCode=10051` dondururken ret cevabinda
  `signature` alaninin bulunmadigi goruldu. Token ve conversation kimligi
  dogru eslesmesine ragmen backend tum cevaplarda imza zorunlu tuttugu icin ret
  sonucu `FAILED` durumuna ulasamiyordu.
- Basarili odemede gecerli response signature zorunlulugu korunmustur. Belirsiz
  cevaplar ile mevcut fakat gecersiz imzali cevaplar kabul edilmez. Yalniz
  provider'in acikca `failure/FAILURE` olarak bildirdigi imzasiz ret, sonraki
  katmanda token ve conversation kimligi sabit zamanli karsilastirmayla
  dogrulandiktan sonra mevcut failure mapper uzerinden terminal `FAILED`
  sonucuna donusturulur.
- Android provider cevabini yorumlamaz; backend canonical `FAILED` ve guvenli
  hata kodunu dondurdugunde mevcut ortak odeme sonuc ekranini kullanir.
- Odakli gateway, failure mapping ve payment result testleri basarili
  tamamlandi. Yetersiz bakiye ve kayip kart akislarinin sonsuz loading yerine
  uygun hata ekranina ulastigi gercek iyzico Sandbox kullanici testinde
  dogrulanacaktir.

### DEG-057 - Kesin Odeme Reddinde Tek ve Dogru Cikis Eylemi

- Durum: `UYGULANDI`
- `FAILED` odeme ekranindaki `Tekrar Dene` ve `Odemeden Cik` eylemleri ayni
  cikis callback'ini kullandigi icin yaniltici tekrar eylemi kaldirildi.
- Kesin reddedilen odemede yalniz `Odemeden Cik` ana eylemi ortak `EditButton`
  tasarimiyla gosterilir. Kullanici yeni odeme denemesini onceki satin alma veya
  para yukleme ekranindan baslatir.
- `TIMEOUT` ve diger odeme durumlarinin mevcut sunum ve davranislari
  degistirilmedi. Backend ve navigasyon sozlesmesinde degisiklik yapilmadi.
