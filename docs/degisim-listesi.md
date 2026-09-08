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

### DEG-066 - Rehber Tur Detayinda Gercek Yorum Listesi

- Durum: `ONAYLANDI`
- Rehberin `Turlarim > Aktif` ve `Turlarim > Gecmis` akislari ayni rehber tur
  detay ekranini kullanmaya devam edecektir.
- Detayin ustundeki `averageRating` ve `reviewCount` degerleri korunurken yorum
  sekmesi bos birakilmayacak; mevcut `ReviewRepository` arayuzu uzerinden turun
  gercek yorumlari cekilip ortak `TourDetailContent` modeline aktarilacaktir.
- `GuideTourDetailViewModel` somut data implementasyonuna degil domain repository
  arayuzune baglanacaktir. Yeni backend endpoint'i, ekran, navigation destination,
  use-case veya ortak olmayan ek katman olusturulmayacaktir.
- Yorum istegi basarisiz oldugunda tur detayinin tamamini kullanilamaz yapmak
  yerine mevcut detay verisi korunacak; yorum bolumunun hata davranisi uygulama
  sirasinda mevcut ortak hata UX'iyle orantili bicimde ele alinacaktir.
- Test karari: Rehber detayinda basarili yorum listesinin UI modeline aktarildigi
  ve yorum istegi hatasinin ana tur detayini kaybettirmedigi ViewModel testiyle
  dogrulanmalidir. Aktif ve gecmis mod icin ayni davranis tekrar test edilmez.

### DEG-065 - Bildirim Okundu Durumunda Es Zamanli Yenileme Guvenligi

- Durum: `ONAYLANDI`
- Kullanici sistem bildirimine veya uygulama icindeki ilgili icerige dokundugunda,
  hedef ekran basariyla yuklendikten sonra ilgili bildirimler backend otoritesinde
  okundu olarak isaretlenmeye devam edecektir. Hedef yuklenemezse bildirim okundu
  sayilmayacaktir.
- FCM, STOMP ve ekran yenilemesinden ayni anda baslayan eski bildirim liste veya
  okunmamis sayi istekleri, daha sonra tamamlanarak yeni `okundu` sonucunun
  uzerine yazamayacaktir.
- Bildirim repository'sindeki canonical liste, okunmamis sayi ve okundu
  mutasyonlari tek ve sirali bir state guncelleme sinirinda yonetilecektir.
  Cozum yalniz UI'da rozeti gizlemeyecek; backend sonucu ile yerel state'in
  tutarliligini koruyacaktir.
- Davranis sohbet, tur, rezervasyon ve odeme hedefleriyle birlikte tekil okunan
  guvenlik, kazanc ve diger bildirim turlerini kapsayacaktir. Ilgisiz okunmamis
  bildirimler varsa ust bardaki kirmizi rozet gorunmeye devam edecektir.
- Mevcut typed navigation, hedef bazli `markRelatedRead`, tekil `markRead`, FCM ve
  STOMP sorumluluklari korunacak; yeni ekran, gereksiz use-case veya genel amacli
  concurrency framework'u eklenmeyecektir.
- Test karari: Gec baslayan eski yenileme cevabinin basarili `markRead`,
  `markRelatedRead` veya `markAllRead` sonucunu geri alamadigi repository
  seviyesinde deterministik coroutine testleriyle dogrulanmalidir. Salt kirmizi
  rozet gorunumu icin kirilgan UI testi yazilmayacaktir.
