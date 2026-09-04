# GuideMate Detayli Kullanici Kabul Testleri

## Belge Durumu

- Durum: HAZIR - UYGULANMADI
- Son guncelleme: 2026-08-29
- Son tamamlanan tarama: Final handoff/fazlar, backend rules/implementation,
  demo verisi, guncel Android ekran/navigation/ViewModel/repository/API/DTO,
  backend controller/endpoint/hata/guvenlik/dis servis sozlesmeleri ve acik
  kullanici kararlari capraz kontrol edildi. 33 bolum, 1.078 kimlikli test,
  82/82 backend endpoint ve 42/42 Android Screen kapsami yazildi.
- Siradaki is: `RUN-A01` ile ortam hazirligini kanitlayip manuel kabul turunu
  sirayla uygulamak; her sonucu bu dosyada isaretlemek.
- Tamamlanma kosulu: Bu dosyadaki her senaryo `GECTI`, `KALDI`, `DUZELTILDI`
  veya kanitli bir `KAPSAM DISI` sonucu almadan manuel kabul turu tamamlanmis
  sayilmaz.

## Amac ve Kapsam

Bu dosya GuideMate Android uygulamasinin yalniz fazlarda ertelenen E2E
testlerini degil, kullanicinin gorebildigi ve tetikleyebildigi tum davranislari
tek yerden test etmek icindir. Kapsam sunlari birlikte icerir:

- Auth, onboarding, rol secimi ve oturum yasam dongusu
- Rehber ve turist ana sayfa, profil ve hesap ekranlari
- Medya, ortak seciciler, tur yayinlama/yonetme/kesif akislari
- Rezervasyon, iptal, yorum, odeme, wallet ve rehber finance
- Chat REST/STOMP, notification/FCM ve semantic navigation
- Loading, empty, error, retry, pagination, offline ve process death
- Portrait/landscape, klavye, uzun metin, yerellesme ve erisilebilirlik
- Iki kullanicili LAN, hesap izolasyonu, yetki ve hassas veri sinirlari

Bu belge otomatik unit/instrumentation testlerinin yerine gecmez. Buradaki
senaryolar gercek kullanici, cihaz, backend, PostgreSQL ve gerekli dis servis
davranislarini kanitlar.

## Kaynak Otoritesi

Senaryolar su guncel kaynaklar birlikte taranarak uretilir:

1. `docs/final-android-integration-handoff.md`
2. `docs/backend-implementation-handoff.md`
3. Backend `BACKEND_RULES.md` ve `DEMO.md`
4. Guncel Android navigation, Screen/Content, ViewModel, repository, API/DTO,
   hata eslestirmesi ve XML kaynaklari
5. Guncel backend controller, state machine, yetki, transaction, scheduler ve
   OpenAPI sozlesmesi
6. Kullanicinin en son acik kararlari

Celiskide en son acik kullanici karari ve canli kod/OpenAPI esas alinir. Eski
mock davranisi veya tamamlanmis faz plani guncel runtime davranisi yerine kabul
edilmez.

## Sonuc ve Kanit Formati

Her test satiri su durumlardan biriyle isaretlenir:

- `[ ]` Henuz test edilmedi
- `[x]` GECTI
- `[!]` KALDI
- `[~]` DUZELTILDI ve yeniden test bekliyor
- `[-]` KAPSAM DISI; gerekce ayni satira yazilir

Bir test icin gerekirse su kanit eklenir:

- Cihaz/emulator modeli ve Android surumu
- Kullanici rolu ve maskeli test hesabi etiketi
- Backend profili, PostgreSQL ve LAN ortami
- iyzico Sandbox/Quick Tunnel, FCM veya STOMP durumu
- Ekran goruntusu, Logcat veya maskeli backend log referansi
- Beklenen, gerceklesen, hata kaydi ve yeniden test sonucu

## Test Ortami ve On Kosullar

### Cihaz Matrisi

- [ ] Pixel/API 36 emulator temiz kurulumla hazir.
- [ ] En az bir fiziksel Android cihaz ayni Wi-Fi uzerinden backend'e erisiyor.
- [ ] Mumkunse ikinci fiziksel cihaz veya ikinci emulator iki kullanicili akisa
  hazir.
- [ ] En az bir cihaz Turkce, bir cihaz veya test turu Ingilizce locale ile
  calisiyor.
- [ ] Portrait ve landscape destekleyen cihaz/emulator hazir.
- [ ] Sistem font olcegi normal ve buyuk degerde denenebiliyor.
- [ ] Android 13+ notification runtime izni test edilebiliyor.

### Backend ve Dis Servisler

- [ ] Spring Boot backend guncel kaynakla calisiyor ve `/v3/api-docs`
  erisilebilir.
- [ ] PostgreSQL guncel Flyway migration'lariyla hazir.
- [ ] Android debug base URL ve backend public/media URL ayni guncel LAN
  adresini kullaniyor.
- [ ] Fiziksel cihaz backend ve media URL'lerine ayni Wi-Fi'dan erisebiliyor.
- [ ] SMTP gercek test e-postalarini teslim edecek sekilde hazir.
- [ ] Firebase credential/config hazir; FCM delivery backend'de etkin.
- [ ] STOMP `/ws` endpoint'i JWT ile erisilebilir.
- [ ] iyzico Sandbox credential'lari ve guncel Quick Tunnel callback/webhook
  adresi hazir.
- [ ] Secret degerler Android kaynak koduna, bu belgeye veya ekran goruntusune
  yazilmadi.

### Test Hesaplari ve Veri

- [ ] Yeni kayit icin daha once kullanilmamis e-posta hesabi hazir.
- [ ] E-posta dogrulamasi bekleyen hesap hazir.
- [ ] ACTIVE fakat rolu secilmemis hesap hazir.
- [ ] Guide A: profil, tur, wallet ve finance ana test hesabi hazir.
- [ ] Guide B: Guide A private verilerine yetkisiz erisim testi icin hazir.
- [ ] Tourist A: discovery, reservation, payment, review ve chat ana hesabi
  hazir.
- [ ] Tourist B: kapasite yarisi ve hesap izolasyonu icin hazir.
- [ ] OPEN, CLOSED, FULL, CANCELLED, COMPLETED, REVIEW/PENDING ve REJECTED
  durumlarini temsil eden tur/session verileri hazir.
- [ ] Upcoming, past, cancelled ve review-eligible reservation verileri hazir.
- [ ] Wallet'ta yeterli/yetersiz bakiye ve sayfali transaction verileri hazir.
- [ ] Rehber icin earning, banka hesabi ve withdrawal durum verileri hazir.
- [ ] Sayfalama, filtre, empty state ve duplicate kontrolu icin yeterli buyuklukte
  tur, rehber, mesaj ve bildirim verisi hazir.

## Ayrintili Test Senaryolari

### 1. Kurulum, Baslangic ve Root Navigation

#### Temiz Kurulum ve Onboarding

- [ ] `ROOT-001` Uygulama ilk kez kuruldugunda launcher acilir; crash, siyah
  ekran veya sonsuz root loading gorulmez.
- [ ] `ROOT-002` Ilk kurulumda onboarding ilk sayfadan baslar ve pager sirasi
  dogrudur.
- [ ] `ROOT-003` Onboarding ileri/geri kaydirma, sayfa gostergesi ve son sayfa
  aksiyonu dogru calisir.
- [ ] `ROOT-004` Onboarding portrait ve landscape durumda tasma/kirpma olmadan
  kullanilir; son aksiyon erisilebilir kalir.
- [ ] `ROOT-005` Onboarding tamamlaninca Sign In acilir ve sistem geri tusu
  onboarding'e donmez.
- [ ] `ROOT-006` Uygulama kapatip acildiginda tamamlanmis onboarding yeniden
  gosterilmez.
- [ ] `ROOT-007` Uygulama verisi temizlenince onboarding yeniden gosterilir.
- [ ] `ROOT-008` Onboarding sirasinda process oldurulup uygulama yeniden
  acildiginda bozuk/yarim navigation stack olusmaz.

#### Oturum ve Role Gore Baslangic

- [ ] `ROOT-009` Oturumu olmayan kullanici Sign In root'una gider.
- [ ] `ROOT-010` ACTIVE fakat role secmemis kullanici Role Selection ekranina
  gider.
- [ ] `ROOT-011` GUIDE rolu olan kullanici guide root/home'a gider.
- [ ] `ROOT-012` TOURIST rolu olan kullanici tourist root/home'a gider.
- [ ] `ROOT-013` Root belirlenirken kullaniciya onceki hesabin ekrani bir kare
  bile gosterilmez.
- [ ] `ROOT-014` Root loading hizli backend cevabinda gereksiz yanip sonme
  olusturmaz; yavas cevapta anlasilir loading gosterir.
- [ ] `ROOT-015` Gecici backend/ag hatasinda gecerli cached session varsa oturum
  gereksiz yere silinmez.
- [ ] `ROOT-016` Terminal session/refresh hatasinda local session temizlenir ve
  Auth root acilir.
- [ ] `ROOT-017` Uygulama yeniden yaratildiginda ayni root birden fazla kez
  stack'e eklenmez.
- [ ] `ROOT-018` Guide ve tourist bottom-bar gecmisleri birbirine karismaz.
- [ ] `ROOT-019` Full-screen account akisindan geri donunce dogru role/root ve
  onceki ekran korunur.
- [ ] `ROOT-020` Uygulama arka plandan geldiginde notification/payment recovery
  yoksa mevcut ekran gereksiz yere degismez.

### 2. Ortak UI, Ag ve Hata Durumlari

#### Tam Ekran Loading, Error, Empty ve Retry

- [ ] `COMMON-001` Ilk veri yuklemede 36 dp brand color progress doner ve XML
  kaynakli `Yukleniyor` metni gorunur.
- [ ] `COMMON-002` Backend hizli cevap verirse loading zorunlu bekleme yapmadan
  content'e gecer.
- [ ] `COMMON-003` Ilk veri istegi hata verirse 36 dp sabit refresh oku ve XML
  kaynakli `Tekrar Dene` metni gorunur.
- [ ] `COMMON-004` Yalniz refresh ikonu ve metninin olusturdugu hedef
  tiklanabilir; tum sayfa tiklanabilir olmaz.
- [ ] `COMMON-005` Retry tiklaninca ikon/loading durumu degisir ve yalniz bir
  yeni istek atilir.
- [ ] `COMMON-006` Hizli ardisik retry dokunuslari yinelenen paralel istek
  olusturmaz.
- [ ] `COMMON-007` Retry basarili olursa error kaybolur ve content gorunur.
- [ ] `COMMON-008` Retry yine hata verirse sonsuz spinner yerine retry durumu
  geri gelir.
- [ ] `COMMON-009` Bos sonuc hata gibi sunulmaz; ozellige uygun empty mesaj ve
  aksiyon gorunur.
- [ ] `COMMON-010` Mevcut content varken refresh, tam ekran loading ile veriyi
  kapatmaz.
- [ ] `COMMON-011` Append loading listenin sonunda gorunur; ilk loading ile
  karismaz.
- [ ] `COMMON-012` Append hatasi mevcut listeyi silmez ve sayfa-sonu retry ile
  devam eder.
- [ ] `COMMON-013` Buton ici loading kontrast content rengi kullanir; buton
  metni ve progress ayni anda okunmaz hale gelmez.
- [ ] `COMMON-014` Image loading/error placeholder'i tam ekran load state ile
  karistirilmaz.

#### Merkezi Hata Deneyimi

- [ ] `COMMON-015` Internet kapaliyken `NoInternet` kullaniciya yerellestirilmis
  ve anlasilir mesaj verir.
- [ ] `COMMON-016` Backend'e erisilemiyorsa `NoResponseFromServer` genel bilinmeyen
  hata gibi kaybolmaz.
- [ ] `COMMON-017` 401 terminal session sonucu uygun oturum yenileme/Sign In
  akisina gider; ayni anda iki hata mesaji gostermez.
- [ ] `COMMON-018` 403 yetki hatasi uygulamayi cokertmez ve kullaniciya uygun
  mesaj verir.
- [ ] `COMMON-019` 429 rate-limit cevabinda varsa kalan saniye gorunur ve buton
  geri sayim bitene kadar yeni istek atmaz.
- [ ] `COMMON-020` Validation `fieldErrors` ilgili TextField altinda gorunur;
  genel hata ilgili alan mesajini ezmez.
- [ ] `COMMON-021` `fieldErrors` JSON'da hic yoksa gercek backend hata kodu yine
  dogru okunur.
- [ ] `COMMON-022` Bilinmeyen backend hata kodu teknik enum/exception basmadan
  guvenli genel mesaja duser.
- [ ] `COMMON-023` Hata Toast/dialog/snackbar hangi ekran deseninde tasarlandiysa
  ayni ozellikte tutarli kullanilir.
- [ ] `COMMON-024` Tek seferlik hata/mesaj ekran yeniden cizilince tekrar tekrar
  gosterilmez.
- [ ] `COMMON-025` Teknik exception, endpoint, SQL veya stack trace kullaniciya
  gosterilmez.

#### Genel Form, Dialog ve Bottom Sheet Davranisi

- [ ] `COMMON-026` Bos zorunlu alanlarda istek atilmadan uygun alan/genel hata
  gorunur.
- [ ] `COMMON-027` Hata duzeltilmek icin alan degistirilince eski field error
  temizlenir.
- [ ] `COMMON-028` Klavye acikken aktif alan ve ana aksiyon gorulebilir veya
  kaydirilabilir kalir.
- [ ] `COMMON-029` IME Next/Done odak sirasini dogru ilerletir ve istenmeyen
  cift submit yapmaz.
- [ ] `COMMON-030` Submit devam ederken ilgili buton devre disi kalir.
- [ ] `COMMON-031` Dialog disina dokunma ve sistem geri tusu, islemin kritik
  olup olmamasina gore dogru dismiss davranisi verir.
- [ ] `COMMON-032` Mutation devam ederken kritik dialog/bottom sheet yanlislikla
  kapanip belirsiz sonuc birakmaz.
- [ ] `COMMON-033` Bottom sheet landscape modda ana onay aksiyonunu kaybettirmez
  ve icerik kaydirilabilir.
- [ ] `COMMON-034` Uzun liste bottom sheet'leri secimi, scroll'u ve aramayi
  kullanilabilir tutar.
- [ ] `COMMON-035` Switch/radio/checkbox secili renkleri GuideMate tasarimiyla
  uyumludur ve disabled durum ayirt edilir.

### 3. Auth ve Hesap Yasam Dongusu

#### Sign In

- [ ] `AUTH-001` Sign In ekraninda e-posta, yalniz sayisal sifre, sifre
  gorunurlugu, forgot password, sign up ve Google aksiyonlari gorunur.
- [ ] `AUTH-002` E-posta veya sifre bosken Sign In istegi atilmaz ve alanlari
  doldurma mesaji gorunur.
- [ ] `AUTH-003` Gecersiz e-posta formatinda alan altinda dogru hata gorunur.
- [ ] `AUTH-004` Sifre alanina harf/sembol yapistirilinca numeric password
  politikasina gore sanitize edilir; uygulama cokmez.
- [ ] `AUTH-005` Sekiz haneden kisa veya gecersiz numeric sifreyle istek
  atilmaz.
- [ ] `AUTH-006` Sifre goz ikonuyla gosterilip gizlenebilir; deger degismez.
- [ ] `AUTH-007` E-postadaki bas/son bosluk ve buyuk harf normalize edilerek
  dogru hesapla giris yapilir.
- [ ] `AUTH-008` Dogru bilgilerle login tek istek atar ve role uygun root acilir.
- [ ] `AUTH-009` Yanlis sifrede `INVALID_CREDENTIALS` anlasilir mesaj verir;
  bilinmeyen hata gosterilmez.
- [ ] `AUTH-010` Bulunmayan kullanici icin backend sozlesmesine uygun mesaj
  gorunur ve baska hesap verisi acilmaz.
- [ ] `AUTH-011` Disabled hesap uygun mesaj alir ve root'a gecmez.
- [ ] `AUTH-012` Bekleyen e-posta dogrulamasi normal login'de verification
  dialogunu acar ve dogru e-postayi gosterir.
- [ ] `AUTH-013` Verification dialogu kapatilabilir; form state gereksiz yere
  silinmez.
- [ ] `AUTH-014` Verification e-postasini tekrar gonderme basarili mesaj verir.
- [ ] `AUTH-015` Resend cooldown sirasinda buton/geri sayim yeni istek atmayi
  engeller.
- [ ] `AUTH-016` Resend rate-limit backend saniyesini kullanir ve bittiginde
  tekrar aktif olur.
- [ ] `AUTH-017` Login rate-limit sirasinda submit engellenir ve kalan sure
  dogru azalir.
- [ ] `AUTH-018` Login istegi sirasinda Sign In ve Google butonlari yinelenen
  istek olusturmaz.
- [ ] `AUTH-019` Internet kesilince form degerleri korunur ve tekrar deneme
  mumkundur.
- [ ] `AUTH-020` Sign Up/Forgot Password'tan Sign In'e dondukten sonra sistem
  geri tusu eski forma geri goturmez.

#### Google Sign In

- [ ] `AUTH-021` Google butonu hesap seciciyi acar ve loading durumu dogru
  gorunur.
- [ ] `AUTH-022` Kullanici Google seciciyi iptal ederse loading kapanir ve hata
  gosterilmez.
- [ ] `AUTH-023` Credential/Google servisi hata verirse yerellestirilmis Google
  login hatasi gorunur.
- [ ] `AUTH-024` Mevcut ACTIVE GuideMate hesabinin Google kimligiyle login
  basarili olur.
- [ ] `AUTH-025` GuideMate'te bulunmayan Google hesabi otomatik kayit edilmez ve
  `GOOGLE_ACCOUNT_NOT_FOUND` mesaji gorunur.
- [ ] `AUTH-026` Pending verification Google hesabi uygun bilgilendirme alir ve
  root'a gecmez.
- [ ] `AUTH-027` Baska Google `sub` ile eslesmeyen hesapta
  `GOOGLE_ACCOUNT_MISMATCH` mesaji gorunur.
- [ ] `AUTH-028` Raw Google ID token Logcat, network body logger veya local
  storage'da acik metin gorunmez.

#### Sign Up ve E-posta Dogrulama

- [ ] `AUTH-029` Sign Up ekraninda ad, soyad, e-posta, numeric sifre, sifre
  tekrar ve kosullar alani dogru gorunur.
- [ ] `AUTH-030` Ad/soyad bosken veya minimum uzunlugun altindayken istek
  atilmaz.
- [ ] `AUTH-031` Ad en az 3, soyad en az 2 karakter ve izinli isim karakterleri
  kuralina uygun dogrulanir.
- [ ] `AUTH-032` Ad/soyad alaninda sayi veya gecersiz karakter uygun hata verir.
- [ ] `AUTH-033` Gecersiz e-posta alan altinda gorunur ve duzeltildiginde hata
  temizlenir.
- [ ] `AUTH-034` Sifre klavyesi sayisal acilir; harf/sembol girisi kabul
  edilmez/sanitize edilir.
- [ ] `AUTH-035` Sifre ve tekrar sifre eslesmiyorsa kayit istegi atilmaz.
- [ ] `AUTH-036` Sifre gorunurluk ikonlari bagimsiz calisir.
- [ ] `AUTH-037` Kosullar checkbox'i dogrudan kabul etmez; once kosul bottom
  sheet'i acilir.
- [ ] `AUTH-038` Kosul metni sonuna kadar okunabilir, landscape modda onay
  aksiyonu gorunur kalir.
- [ ] `AUTH-039` Kosullar okunmadan/kabul edilmeden kayit yapilamaz.
- [ ] `AUTH-040` Kabul edilen kosullar tekrar checkbox ile kaldirilabilir.
- [ ] `AUTH-041` Gecerli bilgilerle tek register istegi atilir ve basari durumu
  kullaniciyi e-posta dogrulamaya yonlendirir.
- [ ] `AUTH-042` Ayni e-postayla ikinci kayit `EMAIL_ALREADY_EXISTS` mesajini
  gosterir.
- [ ] `AUTH-043` Backend field validation ad/soyad/e-posta/sifre alanlarina
  dogru dagilir.
- [ ] `AUTH-044` SMTP teslim hatasi gercek kayit basarisi gibi gosterilmez.
- [ ] `AUTH-045` Gelen dogrulama linki fiziksel telefonda LAN public URL ile
  acilir.
- [ ] `AUTH-046` Gecerli link hesap dogrulama basari web sonucunu gosterir.
- [ ] `AUTH-047` Suresi gecmis link kullanici dostu web sonucu gosterir.
- [ ] `AUTH-048` Daha once kullanilmis link tekrar kullanildiginda uygun sonuc
  verir ve ikinci kez state degistirmez.
- [ ] `AUTH-049` Dogrulama sonrasi login yapilir ve yalniz bir kez role selection
  acilir.

#### Role Selection

- [ ] `AUTH-050` Role secmeden devam edilirse uygun hata gorunur.
- [ ] `AUTH-051` Tourist karti secimi gorsel olarak belirginlesir.
- [ ] `AUTH-052` Guide karti secimi gorsel olarak belirginlesir.
- [ ] `AUTH-053` Role secimi degistirilebilir ve son secim gonderilir.
- [ ] `AUTH-054` Submit devam ederken kartlar/confirm yinelenen istek atmaz.
- [ ] `AUTH-055` Tourist secimi backend'de kalici olur ve tourist root acilir.
- [ ] `AUTH-056` Guide secimi backend'de kalici olur ve guide root acilir.
- [ ] `AUTH-057` Uygulama yeniden acilinca role selection tekrar gosterilmez.
- [ ] `AUTH-058` Ayni hesap ikinci kez role secmeye zorlanmaz; backend
  `ROLE_ALREADY_SELECTED` state'i sessiz role degisikligine yol acmaz.
- [ ] `AUTH-059` Client/admin rolunu secemez ve request degistirilse backend
  bunu kabul etmez.

#### Forgot Password ve Web Reset

- [ ] `AUTH-060` Forgot Password e-posta bosken istek atmaz.
- [ ] `AUTH-061` Gecersiz e-posta alan altinda dogru hata gosterir.
- [ ] `AUTH-062` Gecerli e-posta istegi basari dialogunu gosterir.
- [ ] `AUTH-063` Bulunmayan e-posta hesap varligini sizdiran guvensiz farkli
  sonuc uretmez; backend urun sozlesmesine uygun davranir.
- [ ] `AUTH-064` Ardisik forgot-password istegi rate-limit/cooldown ile
  sinirlanir.
- [ ] `AUTH-065` Basari dialogu kapatilinca Sign In'e donus stack'i duzgundur.
- [ ] `AUTH-066` Reset linki fiziksel telefonda acilir ve token web formuna
  tasinir.
- [ ] `AUTH-067` Yeni sifre web formunda numeric ve minimum 8 hane kuralina
  uyar.
- [ ] `AUTH-068` Sifre tekrari eslesmezse reset tamamlanmaz.
- [ ] `AUTH-069` Gecerli reset sonrasi yeni sifreyle login olur, eski sifreyle
  olunmaz.
- [ ] `AUTH-070` Suresi gecmis/kullanilmis reset token'i kullanici dostu sonuc
  verir ve tekrar kullanilamaz.
- [ ] `AUTH-071` Reset sonrasi mevcut refresh oturumlari guvenlik sozlesmesine
  uygun gecersiz olur.

#### Change Password, Refresh ve Logout

- [ ] `AUTH-072` Change Password ekraninda mevcut/yeni/tekrar numeric alanlar ve
  gorunurluk ikonlari calisir.
- [ ] `AUTH-073` Bos veya gecersiz alanlarda API istegi atilmaz.
- [ ] `AUTH-074` Yanlis mevcut sifre `CURRENT_PASSWORD_INCORRECT` mesajini verir.
- [ ] `AUTH-075` Yeni sifre mevcut sifreyle ayniysa uygun hata gorunur.
- [ ] `AUTH-076` Yeni sifre ve tekrar eslesmiyorsa uygun hata gorunur.
- [ ] `AUTH-077` Basarili degisimde geriyle kapatilamayan basari dialogu gorunur.
- [ ] `AUTH-078` Basari dialogunda Tamam local session'i temizler ve Sign In
  root'una goturur.
- [ ] `AUTH-079` Yeni sifreyle login basarili, eski sifreyle basarisizdir.
- [ ] `AUTH-080` Sifre degisimi SECURITY_ALERT notification history/push olayi
  uretir.
- [ ] `AUTH-081` Access token suresi dolunca tek refresh istegi yapilir ve ilk
  business istegi bir kez devam eder.
- [ ] `AUTH-082` Eszamanli birden fazla 401 gereksiz birden fazla refresh veya
  login dongusu olusturmaz.
- [ ] `AUTH-083` Revoked/expired/replayed refresh terminal session temizligi
  yapar.
- [ ] `AUTH-084` Logout backend basariliysa token, current user, cache, unread,
  STOMP ve role state temizlenir.
- [ ] `AUTH-085` Logout remote hata alsa bile local secret/session temizlenir ve
  Sign In acilir.
- [ ] `AUTH-086` Logout sonrasi sistem geri tusu onceki guide/tourist ekrana
  donmez.
- [ ] `AUTH-087` Farkli hesapla login sonrasi onceki hesabin profil/tur/wallet/
  chat/notification verisi gorunmez.

### 4. Ortak Seciciler, Form Bilesenleri ve Yerellesme

#### Ulke, Sehir ve Konum

- [ ] `SELECT-001` Ulke bottom sheet'i acilir, tam liste kaydirilabilir ve
  secim yapilabilir.
- [ ] `SELECT-002` Ulke aramasi aksan/buyuk-kucuk harf kurallarinda beklenen
  sonucu verir.
- [ ] `SELECT-003` Ingilizce cihazda `Turkey` ve Turkce cihazda `Turkiye`
  aramasinin locale katalog davranisi kullaniciya tutarli gorunur.
- [ ] `SELECT-004` Ulke secilmeden sehir secimine gecilemez veya anlasilir bos
  durum gosterilir.
- [ ] `SELECT-005` Ulke secilince onceki farkli ulkeye ait sehir secimi
  temizlenir.
- [ ] `SELECT-006` Sehir aramasinda minimum sorgu uzunlugunun altinda eski
  sonuclar ekranda kalmaz.
- [ ] `SELECT-007` Bas/son bosluklu sehir sorgusu normalize edilir ve ayni
  sonucu verir.
- [ ] `SELECT-008` Sehir aramasi loading durumunu gosterir; hizli sorgu
  degisiminde eski istek yeni sonucu ezmez.
- [ ] `SELECT-009` Google Places basarili sonucunda sehir adi ve canonical
  alanlar dogru secilir.
- [ ] `SELECT-010` Sehir sonucu bulunmazsa empty state hata gibi sunulmaz.
- [ ] `SELECT-011` Internet/Places permission hatasinda kontrollu mesaj ve retry
  gorunur; uygulama cokmez.
- [ ] `SELECT-012` API anahtari bulunmayan debug build sehir secimine
  dokunuldugunda crash yerine kontrollu hata verir.
- [ ] `SELECT-013` Retry ayni ulke baglaminda tekrar arama yapar.
- [ ] `SELECT-014` Sehir secilince bottom sheet kapanir ve ekranda secilen
  ulke/sehir gorunur.
- [ ] `SELECT-015` Ulke/sehir secimi ekran rotasyonunda kaybolmaz.
- [ ] `SELECT-016` Konum secimi iptal edilirse onceki gecerli secim korunur.

#### Dil ve Kategori

- [ ] `SELECT-017` Dil bottom sheet'i tum katalog dillerini gosterir ve uzun
  liste kaydirilabilir.
- [ ] `SELECT-018` Dil isimleri XML/locale kaynagindan gelir; desteklenen dilde
  dogru ceviri gorunur.
- [ ] `SELECT-019` Dillerin temsil bayraklari katalog secimiyle tutarlidir;
  ayni dili konusan tum ulkeler tekrarlanmaz.
- [ ] `SELECT-020` Tekli/coklu dil secimi kullanan ekran kendi urun kuralina
  uygun davranir.
- [ ] `SELECT-021` Secili dil tekrar secildiginde duplicate eklenmez.
- [ ] `SELECT-022` Dil kaldirma chip/aksiyonu dogru dili kaldirir.
- [ ] `SELECT-023` Dil bottom sheet'i landscape modda `Tamam` aksiyonunu
  erisilebilir tutar.
- [ ] `SELECT-024` Dil secimi iptalinde ekrandaki onceki liste korunur.
- [ ] `SELECT-025` Kategori secimi ortak katalogdaki tum kategorileri ve dogru
  ikonlari gosterir.
- [ ] `SELECT-026` Kategori adlari locale XML kaynagindan gelir; enum adi UI'ya
  basilmaz.
- [ ] `SELECT-027` Kategori bottom sheet ikon/renk/yuvarlak tasarimi tourist
  home kartlariyla tutarlidir.
- [ ] `SELECT-028` Kategori secilince ilgili form/filter state'i guncellenir ve
  duplicate kaynak olusmaz.
- [ ] `SELECT-029` Kategori bottom sheet landscape ve buyuk fontta onay/iptal
  aksiyonlarini kaybetmez.

#### Tarih, Saat, Sure ve Sayisal Alanlar

- [ ] `SELECT-030` Tarih picker gecmiste gecersiz tarih secimine izin vermez
  veya uygun validation gosterir.
- [ ] `SELECT-031` Tarih secimi iptalinde onceki tarih korunur.
- [ ] `SELECT-032` Saat picker secimi dogru form alanina yazilir.
- [ ] `SELECT-033` Sure dropdown yalniz desteklenen degerleri gosterir ve
  secimi korur.
- [ ] `SELECT-034` Tarih/saat/sure rotasyonda kaybolmaz.
- [ ] `SELECT-035` Fiyat, kapasite, top-up ve withdrawal alanlari izin verilmeyen
  karakterleri kabul etmez.
- [ ] `SELECT-036` Cok buyuk sayi, overflow ve copy/paste girdisi crash
  olusturmaz; uygun hata verir.
- [ ] `SELECT-037` Para girisindeki ekranda kullanici major unit yazar; backend'e
  giden deger minor unit olarak dogru temsil edilir.
- [ ] `SELECT-038` Gorunen para formatinda backend currency code kullanilir;
  cihaz locale'i yalniz format/sembol siralamasini etkiler.

### 5. Medya, Kamera, Galeri ve Gorsel Yukleme

#### Kaynak Secimi ve Izinler

- [ ] `MEDIA-001` Avatar/cover degistirme aksiyonu kamera-galeri kaynak sheet'ini
  acar.
- [ ] `MEDIA-002` Bottom sheet disina dokunma/geri ile iptal edilince mevcut
  gorsel korunur.
- [ ] `MEDIA-003` Galeri izin kabul edilince secici acilir.
- [ ] `MEDIA-004` Kamera izin kabul edilince kamera acilir ve FileProvider URI
  ile sonuc doner.
- [ ] `MEDIA-005` Izin reddinde anlasilir mesaj gorunur; ekran kullanilmaya
  devam eder.
- [ ] `MEDIA-006` Kalici izin reddinde Ayarlar'a yonlendirme davranisi ve metni
  kullanici dostudur.
- [ ] `MEDIA-007` Galeri/kamera acilip secim yapmadan kapatilinca upload
  baslamaz.
- [ ] `MEDIA-008` Kamera cekimi iptal edilince gecici bos URI canonical profile/
  tour state'e yazilmaz.
- [ ] `MEDIA-009` Gecersiz veya artik okunamayan URI kontrollu hata verir.

#### Dosya Dogrulama, Upload ve Gosterim

- [ ] `MEDIA-010` JPEG, PNG ve WebP kabul edilir.
- [ ] `MEDIA-011` Desteklenmeyen MIME/uzanti local precheck'te uygun hata verir.
- [ ] `MEDIA-012` 5 MB sinirinin altindaki dosya upload edilir.
- [ ] `MEDIA-013` 5 MB ustundeki dosya backend isteginden once veya backend
  cevabiyla uygun hata verir.
- [ ] `MEDIA-014` Dosya boyutu belirsiz/stream okunamiyorsa uygulama cokmez.
- [ ] `MEDIA-015` Upload sirasinda ayni aksiyon cift upload baslatmaz.
- [ ] `MEDIA-016` Upload internet hatasinda eski avatar/cover korunur ve retry
  edilebilir mesaj gorunur.
- [ ] `MEDIA-017` Upload basarili fakat attach mutation basarisizsa yeni sahipsiz
  medya cleanup edilir ve eski canonical gorsel korunur.
- [ ] `MEDIA-018` `MEDIA_IN_USE` silme hatasi kullaniciya teknik detay vermeden
  gosterilir.
- [ ] `MEDIA-019` Medya purpose mismatch profile resmi/tur cover'ina yanlis
  attach edilmez.
- [ ] `MEDIA-020` Basarili upload sonrasi backend `mediaAssetId` mutation'a
  gider; local URI kalici remote URL gibi saklanmaz.
- [ ] `MEDIA-021` Remote HTTP/HTTPS avatar ve cover emulator/fiziksel cihazda
  gorunur.
- [ ] `MEDIA-022` Local preview URI ve remote URL ortak GuideMate image
  bileseninde dogru render edilir.
- [ ] `MEDIA-023` Image loading placeholder ve hata drawable fallback'i mevcut
  tasarimla uyumludur.
- [ ] `MEDIA-024` Kirik/silinmis media URL tum ekranin cokmesine yol acmaz.
- [ ] `MEDIA-025` Avatar/cover URL'sine token query parametresi eklenmez.
- [ ] `MEDIA-026` Uygulama yeniden acildiginda remote canonical gorsel korunur.

#### Ortak Avatar Tutarliligi

- [ ] `MEDIA-027` Tourist avatar degisikligi tourist profilinde gorunur.
- [ ] `MEDIA-028` Guide avatar degisikligi guide profilinde ve preview'da
  gorunur.
- [ ] `MEDIA-029` Guide avatar degisikligi tourist public guide profilinde
  gorunur.
- [ ] `MEDIA-030` Guncel avatar chat list/detail topbar projection'ina yansir.
- [ ] `MEDIA-031` Guncel avatar yorum/rezervasyon snapshot urun kuralina uygun
  canonical veya satin alma snapshot'i olarak gorunur.
- [ ] `MEDIA-032` Avatar degisimi diger hesabin avatarini etkilemez.

#### Upload Normalizasyonu ve Cihaz Kabulu

- [ ] `MEDIA-033` Tam cozunurlukte `1x` kamera fotografi ham dosya 5 MB'i assa
  bile secilebilir; yukleme icin normalize edilmis kopya uretilir ve istek
  basarili olur.
- [ ] `MEDIA-034` Galeriden secilen desteklenen buyuk JPEG, PNG ve WebP profil
  fotografi/tur kapagi dogru EXIF yonuyla gorunur ve normalize cikti backend'in
  5 MB sinirini asmaz.
- [ ] `MEDIA-035` Normalizasyon kaynak kamera/galeri gorselini degistirmez;
  upload basari, hata veya iptalinden sonra gecici normalize dosya sonraki
  yuklemelere birikmez.

### 6. Ortak Profil, Hesap ve Statik Icerikler

#### Tourist Profil

- [ ] `PROFILE-001` Tourist profil acilisinda canonical ad, soyad, e-posta ve
  avatar dogru gorunur.
- [ ] `PROFILE-002` Profil ilk istekte loading, hata ve retry durumlarini ortak
  tasarimla gosterir.
- [ ] `PROFILE-003` Avatar kamera/galeri degisimi basarili oldugunda profil
  aninda canonical URL ile yenilenir.
- [ ] `PROFILE-004` Wallet aksiyonu tourist wallet ekranini acar.
- [ ] `PROFILE-005` Kayitli Kartlar, Sifre Degistir, Bildirim Ayarlari, Yasal
  Metinler ve Yardim menuleri dogru account ekranini acar.
- [ ] `PROFILE-006` Account ekranindan topbar/sistem geri dogru tourist profile
  doner; bottom bar account icinde yanlis gorunmez.
- [ ] `PROFILE-007` Profil refresh/logout/account switch onceki turist verisini
  gostermeye devam etmez.

#### Guide Own Profil, About ve Preview

- [ ] `PROFILE-008` Guide profil canonical ad, avatar, about, diller, performans
  ve tur ozetini gosterir.
- [ ] `PROFILE-009` Profil loading/error/retry durumlari sonsuz spinner veya
  sessiz bos ekran birakmaz.
- [ ] `PROFILE-010` Guide kendi avatarini degistirince own profile ve preview
  ayni gorseli kullanir.
- [ ] `PROFILE-011` Profil preview turistin gorecegi ortak GuideProfileContent
  tasarimini kullanir fakat owner'a uygun metin/aksiyonlari korur.
- [ ] `PROFILE-012` Level info bottom sheet own profilde `seviyeniz`, public
  gorunumde `rehberin seviyesi` anlamini verir.
- [ ] `PROFILE-013` Level, ortalama puan, review count, tamamlanan tur ve
  katilimci degerleri backend performance projection'iyla tutarlidir.
- [ ] `PROFILE-014` Tek bir yuksek puan rehberi haksiz sekilde ust seviyeye
  tasimaz; backend reviewCount/tur kurali gorunume yansir.
- [ ] `PROFILE-015` About ekraninda title/specialty, biyografi ve diller mevcut
  backend degerleriyle acilir.
- [ ] `PROFILE-016` About alanlarinda bos/gecersiz/uzun degerler uygun
  validation verir.
- [ ] `PROFILE-017` Dil ekleme, duplicate engeli ve kaldirma dogru calisir.
- [ ] `PROFILE-018` About save sirasinda cift istek engellenir.
- [ ] `PROFILE-019` About save basarili olunca profil/preview canonical veriyle
  yenilenir ve uygun geri donus olur.
- [ ] `PROFILE-020` Save hata verirse form degerleri kaybolmaz ve kullanici
  yeniden deneyebilir.
- [ ] `PROFILE-021` Guide profil menusu Banka Hesaplari, Hakkimda, Sifre,
  Bildirim, Yasal ve Yardim hedeflerine dogru gider.

#### Public Guide Profil

- [ ] `PROFILE-022` Tourist home veya guide search kartindan public profil
  dogru `guideId` ile acilir.
- [ ] `PROFILE-023` Public profil ad, avatar, biyografi, diller, seviye,
  performans ve populer turlari canonical backend kaynagindan gosterir.
- [ ] `PROFILE-024` Public profil bulunamaz/erisilemezse kontrollu error ve retry
  gorunur.
- [ ] `PROFILE-025` Public profildeki tur karti dogru session detail'ine gider.
- [ ] `PROFILE-026` Mesaj Gonder rezervasyon olmadan find-or-create yapar ve
  mevcut/yeni ayni `chatId` detail'ini acar.
- [ ] `PROFILE-027` Kendi guide hesabina veya gecersiz role mesaj baslatma backend
  tarafindan guvenli reddedilir.
- [ ] `PROFILE-028` Geri tusu public profili acan home/search konumuna dogru
  doner ve scroll/bottom bar davranisi bozulmaz.

#### Bildirim Ayarlari

- [ ] `PROFILE-029` Tourist ayarlari upcoming reminder, chat, reservation,
  review request ve payment tercihlerini backend degeriyle acar.
- [ ] `PROFILE-030` Guide ayarlari chat, reservation/purchase, payments/earnings
  ve new review tercihlerini role uygun gosterir.
- [ ] `PROFILE-031` Security alert tercihi zorunlu/disabled ise kullanici onu
  kapatamaz ve nedenini anlayabilir.
- [ ] `PROFILE-032` Her switch degisimi yalniz ilgili nullable preference alanini
  backend'e gonderir.
- [ ] `PROFILE-033` Preference mutation basarisizsa switch canonical onceki
  duruma doner ve hata mesaji gorunur.
- [ ] `PROFILE-034` Preference degisimi uygulama yeniden acilinca korunur.
- [ ] `PROFILE-035` Guide ve tourist preference alanlari birbirine karismaz.

#### Kayitli Kartlar, Yasal Metin ve Yardim

- [ ] `PROFILE-036` Kayitli Kartlar ekrani yalniz provider/backend maskeli kart
  metadata'sini gosterir.
- [ ] `PROFILE-037` Kartta banka/marka, son dort hane ve varsayilan durumu dogru
  gorunur; tam kart/SKT/CVV yoktur.
- [ ] `PROFILE-038` Standalone kart ekleme FAB'i veya native ham kart formu
  bulunmaz.
- [ ] `PROFILE-039` Kart varsayilan yapma onayindan sonra liste sirasi/default
  state canonical olarak yenilenir.
- [ ] `PROFILE-040` Varsayilan kart silme urun/backend kuralina gore uygun onay
  veya red verir.
- [ ] `PROFILE-041` Kart silme onayi/iptali ve provider hata durumu dogru
  gorunur.
- [ ] `PROFILE-042` Empty saved-card state kullaniciyi sahte kart ekleme akisina
  gondermez; kartin gercek hosted odemede kaydedilecegini dogru anlatir.
- [ ] `PROFILE-043` Guide/tourist yasal metinleri role uygun XML icerigini,
  basliklari ve uzun scroll davranisini gosterir.
- [ ] `PROFILE-044` Yardim/SSS role uygun listeyi acar; accordion/uzun metin
  davranisi kullanilabilir.
- [ ] `PROFILE-045` Iletisim aksiyonu bilincli olarak destekleniyorsa dogru
  uygulamayi acar; desteklenmiyorsa bos tiklanabilir aksiyon gibi sunulmaz.
- [ ] `PROFILE-046` Yasal/Yardim ekranlari Turkce/Ingilizce ve buyuk fontta
  tasma/kirpma olmadan kullanilir.

### 7. Rehber Ana Sayfa ve Dashboard

- [ ] `GUIDE-HOME-001` Guide root acilinca Home, Chat, Turlarim/Wallet/Profile
  bottom-bar yapisi tasarlandigi sekilde gorunur.
- [ ] `GUIDE-HOME-002` Dashboard ilk yuklemede ortak loading/error/retry
  davranisini kullanir.
- [ ] `GUIDE-HOME-003` Tamamlanan Tur, Katilimci ve Ortalama Puan canonical
  dashboard projection'indan gelir.
- [ ] `GUIDE-HOME-004` Bu ayki kazanc backend `currentMonthEarningsMinor` ve
  currency code ile dogru formatlanir.
- [ ] `GUIDE-HOME-005` Aktif/onay bekleyen tur sayilari sayfali listenin sadece
  gorunen boyutundan hesaplanmaz.
- [ ] `GUIDE-HOME-006` Dashboard refresh mevcut veriyi tam ekran kapatmadan
  canonical degerleri yeniler.
- [ ] `GUIDE-HOME-007` Kazanc karti Guide Earnings ekranina gider ve geri donus
  Home state'ini bozmaz.
- [ ] `GUIDE-HOME-008` Son bildirimler ortak notification repository verisinden
  gelir; ayri mock liste veya manuel unread sayaci yoktur.
- [ ] `GUIDE-HOME-009` Yeni purchase/review/earning bildirimi geldikten sonra
  ilgili dashboard projection'i uygulamayi kapatmadan yenilenir.
- [ ] `GUIDE-HOME-010` Empty/henüz turu olmayan guide icin sifir degerler ve
  uygun yonlendirme anlamsiz hata gibi gorunmez.
- [ ] `GUIDE-HOME-011` Buyuk sayilar, uzun rehber adi ve buyuk font ozet
  kartlarini tasirmaz.

### 8. Rehber Tur Yayinlama Akisi

#### Akisa Giris ve Draft/Navigation

- [ ] `PUBLISH-001` Tur yayinlama aksiyonu Step 1'den baslar; ayni akis ikinci
  kez stack'e eklenmez.
- [ ] `PUBLISH-002` Step geri/ileri aksiyonlari mevcut draft state'i korur.
- [ ] `PUBLISH-003` Sistem geri tusu ile akistan cikista doldurulmus draft icin
  urun kararina uygun uyari/koruma vardir.
- [ ] `PUBLISH-004` Ekran rotasyonu ve process recreation'da korunmasi gereken
  form alanlari kaybolmaz; draft server verisi gibi kalici hale gelmez.
- [ ] `PUBLISH-005` Publish akisi boyunca bottom bar/topbar ve tek scaffold
  gorunumu belirlenen navigation tasarimini korur.

#### Step 1 - Konum, Tarih ve Saat

- [ ] `PUBLISH-006` Ulke ve sehir ortak location picker ile secilir.
- [ ] `PUBLISH-007` Secim `countryCode`, gorunen ulke/sehir, `cityPlaceId` ve
  `timeZoneId` alanlarini birlikte doldurur.
- [ ] `PUBLISH-008` Eksik konumla Ileri aksiyonu Step 1 validation hatasi verir.
- [ ] `PUBLISH-009` Gecmis tarih/saat ile ilerlenemez.
- [ ] `PUBLISH-010` Gelecek tarih, saat ve sifirdan buyuk sureyle ilerlenebilir.
- [ ] `PUBLISH-011` Sure secilmeden veya sifir/gecersiz sureyle ilerlenemez.
- [ ] `PUBLISH-012` Cihaz saat dilimi urun kapsaminda oldugu gibi kullanilir;
  secilen ekranda tarih/saat kendi kendine degismez.
- [ ] `PUBLISH-013` Step 1 portrait/landscape, picker ve klavye davranisi
  tasarimi bozmaz.

#### Step 2 - Kategori, Diller, Fiyat ve Kapasite

- [ ] `PUBLISH-014` Kategori secilmeden ilerlenemez.
- [ ] `PUBLISH-015` Kategori secimi ortak katalog ad/ikonuyla gorunur.
- [ ] `PUBLISH-016` En az bir dil secilmeden ilerlenemez.
- [ ] `PUBLISH-017` Dil secimi duplicate olusturmaz ve kaldirma dogru calisir.
- [ ] `PUBLISH-018` Sifir, negatif, bos veya gecersiz fiyatla ilerlenemez.
- [ ] `PUBLISH-019` Guide'in girdigi major-unit fiyat backend'e USD minor unit
  olarak dogru gider.
- [ ] `PUBLISH-020` Sifir, negatif, bos, ondalikli veya overflow kapasiteyle
  ilerlenemez.
- [ ] `PUBLISH-021` Gecerli fiyat/kapasite degerleri rotasyon ve ileri/geri
  gecisinde korunur.

#### Step 3 - Baslik, Aciklama, Bulusma ve Cover

- [ ] `PUBLISH-022` Bos tur adiyla ilerlenemez.
- [ ] `PUBLISH-023` Bos aciklamayla ilerlenemez.
- [ ] `PUBLISH-024` Bos bulusma noktasi ile ilerlenemez.
- [ ] `PUBLISH-025` Cover secmeden ilerlenemez.
- [ ] `PUBLISH-026` Kamera/galeri cover secimi preview'i dogru gunceller.
- [ ] `PUBLISH-027` Gecersiz/buyuk medya hatasi Step 3 formunu kaybettirmez.
- [ ] `PUBLISH-028` Uzun tur adi/aciklama alanlarinda karakter siniri ve UI
  scroll davranisi backend validation ile uyumludur.
- [ ] `PUBLISH-029` Klavye acikken bulusma noktasi ve Ileri aksiyonu
  erisilebilir kalir.

#### Step 4 - Preview ve Publish

- [ ] `PUBLISH-030` Preview Step 1-3'te girilen tum canonical aday degerleri
  eksiksiz gosterir.
- [ ] `PUBLISH-031` Preview turist detail tasariminin ortak content yapisini
  kullanir; owner'a ozel satin alma aksiyonu gostermez.
- [ ] `PUBLISH-032` Preview cover, guide avatar, kategori, dil, tarih/saat,
  sure, fiyat, kapasite, aciklama ve bulusma noktasini dogru gosterir.
- [ ] `PUBLISH-033` Publish cift tiklamada tek media upload ve tek create request
  atar.
- [ ] `PUBLISH-034` Upload basarisizsa tur create edilmez, draft korunur ve hata
  gorunur.
- [ ] `PUBLISH-035` Upload basarili/create basarisizsa sahipsiz medya cleanup
  edilir.
- [ ] `PUBLISH-036` Basarili publish `PENDING_REVIEW` tur olusturur ve Turlarim
  Review sekmesine yonlendirir/yeniler.
- [ ] `PUBLISH-037` Basarili publish sonrasi draft temizlenir; yeniden akisa
  girince eski tur alanlari gelmez.
- [ ] `PUBLISH-038` Publish hata kodlari teknik enum yerine yerellestirilmis
  mesaj verir.
- [ ] `PUBLISH-039` Backend schedule conflict veya concurrent state reddinde
  basari gosterilmez ve form yeniden denenebilir kalir.

### 9. Rehber Turlarim, Detay, Duzenleme ve Lifecycle

#### Turlarim Listesi ve Sekmeler

- [ ] `GUIDE-TOUR-001` ACTIVE, REVIEW ve PAST sekmeleri backend list type/state
  siniflandirmasiyla uyumludur.
- [ ] `GUIDE-TOUR-002` Her sekmenin loading, empty, error, retry ve pagination
  durumlari ayri dogru calisir.
- [ ] `GUIDE-TOUR-003` Sekme degistirince onceki sekmenin kayitlari yeni sekmeye
  karismaz.
- [ ] `GUIDE-TOUR-004` Liste karti ile detail ayni `tourId/sessionId`, baslik,
  cover, tarih, fiyat, kapasite ve status degerlerini gosterir.
- [ ] `GUIDE-TOUR-005` Yalniz giris yapan guide'in turlari gorunur; Guide B,
  Guide A'nin private turunu listeleyemez.
- [ ] `GUIDE-TOUR-006` Pagination duplicate/kayip kayit veya tekrar basa donme
  uretmez.
- [ ] `GUIDE-TOUR-007` Refresh mevcut listeyi gereksiz temizlemez ve yeni
  canonical sirayi gosterir.
- [ ] `GUIDE-TOUR-008` Net earning yoksa/iptał turda `Kazanc: $0` gibi yaniltici
  alan gosterilmez.

#### Approval ve Review Durumlari

- [ ] `GUIDE-TOUR-009` Yeni tur REVIEW/PENDING kartinda dogru durum ve aksiyonlar
  gorunur.
- [ ] `GUIDE-TOUR-010` Admin approve sonrasi bildirim gelir ve tur ACTIVE
  sekmesine canonical olarak gecer.
- [ ] `GUIDE-TOUR-011` Admin reject sonrasi sebep gorunur; uygun duzenle/archive
  aksiyonlari vardir.
- [ ] `GUIDE-TOUR-012` Rejected tur archive onayi/iptali dogru calisir.
- [ ] `GUIDE-TOUR-013` Archive basarisizsa kart sessizce listeden kaybolmaz.
- [ ] `GUIDE-TOUR-014` Pending change request varken ikinci kritik degisiklik
  uygun backend hatasiyla engellenir.
- [ ] `GUIDE-TOUR-015` Change approve/reject bildirimi detail/list state'ini
  uygulamayi yeniden baslatmadan yeniler.

#### Booking Availability Switch

- [ ] `GUIDE-TOUR-016` OPEN session switch kapatilinca backend CLOSE islemi
  yapar ve canonical durum geri gelir.
- [ ] `GUIDE-TOUR-017` CLOSED session switch acilinca backend OPEN islemi yapar.
- [ ] `GUIDE-TOUR-018` Completed/cancelled terminal session switch'i disabled
  gorunur.
- [ ] `GUIDE-TOUR-019` Backend `SESSION_NOT_BOOKABLE`, schedule/capacity veya
  concurrent red verirse switch canonical eski konuma doner.
- [ ] `GUIDE-TOUR-020` Switch reddinde snackbar/mesaj ekranin altinda uygun
  konumda, okunabilir ve tek seferlik gorunur.
- [ ] `GUIDE-TOUR-021` Hizli switch tiklamalari birden fazla celisen mutation
  uretmez.
- [ ] `GUIDE-TOUR-022` Guide switch'i kapattiginda tur mevcut reservation sahibi
  tourist'in detay/gezi snapshot'ini silmez, yeni satin alimi engeller.

#### Guide Tour Detail ve Yeni Session

- [ ] `GUIDE-TOUR-023` Detail loading/error/retry ve bulunamayan tur durumunu
  dogru gosterir.
- [ ] `GUIDE-TOUR-024` Detail owner-only aksiyonlarini yalniz tur sahibi guide'a
  gosterir; backend ID tahminiyle yetkisiz islem kabul etmez.
- [ ] `GUIDE-TOUR-025` Yeni session sheet tarih, saat, sure, meeting point,
  fiyat ve kapasite alanlarini gosterir.
- [ ] `GUIDE-TOUR-026` Eksik/gecersiz yeni session formu submit olmaz ve sheet
  acik kalir.
- [ ] `GUIDE-TOUR-027` Gecmis tarih veya schedule conflict anlamli hata verir.
- [ ] `GUIDE-TOUR-028` Basarili yeni session detail/list/public projection'a
  canonical olarak eklenir.
- [ ] `GUIDE-TOUR-029` Mutation sirasinda sheet yanlislikla kapanmaz/cift submit
  olmaz.

#### Session Iptali

- [ ] `GUIDE-TOUR-030` Cancel dialog neden alani bosken confirm etmez.
- [ ] `GUIDE-TOUR-031` Cancel dialog submitting sirasinda dismiss/ikinci confirm
  engellenir.
- [ ] `GUIDE-TOUR-032` Basarili cancel session'i CANCELLED yapar ve guide list/
  detail yenilenir.
- [ ] `GUIDE-TOUR-033` Session cancel ilgili tourist reservation'larini cancel
  eder ve refund state'lerini olusturur.
- [ ] `GUIDE-TOUR-034` Cancel nedeni tourist reservation detail'inde gorunur.
- [ ] `GUIDE-TOUR-035` Cancel, guide earning reversal/wallet history ve
  notification projection'larini dogru yeniler.
- [ ] `GUIDE-TOUR-036` Ayni cancel istegi retry/idempotency ile ikinci iade veya
  ikinci reversal olusturmaz.
- [ ] `GUIDE-TOUR-037` Terminal veya yonetilemez session iptal edilemez ve uygun
  hata gorunur.

#### Tur Duzenleme

- [ ] `GUIDE-TOUR-038` Edit ekrani mevcut canonical tur/session alanlariyla
  dolar.
- [ ] `GUIDE-TOUR-039` Hicbir alan degismeden Save gereksiz request atmaz veya
  urun kararina uygun disabled/mesaj davranisi verir.
- [ ] `GUIDE-TOUR-040` Degisiklik yapip geri cikarken discard dialogu gorunur.
- [ ] `GUIDE-TOUR-041` Discard onayinda degisiklikler kaybolur; iptalde edit
  formu korunur.
- [ ] `GUIDE-TOUR-042` Session'a ait tarih/saat/fiyat/kapasite degisiklikleri
  session update endpoint'ine gider.
- [ ] `GUIDE-TOUR-043` Tur baslik/aciklama/kategori/dil/cover gibi icerik
  degisiklikleri gerekiyorsa change request'e gider.
- [ ] `GUIDE-TOUR-044` Session ve content birlikte degisince kismi basari sonucu
  kullaniciya dogru anlatilir; tamamı basariliymis gibi gosterilmez.
- [ ] `GUIDE-TOUR-045` Booked count altina kapasite dusurme backend tarafindan
  reddedilir ve form canonical degerle duzeltilir.
- [ ] `GUIDE-TOUR-046` Locked location degisikligi uygun hata verir.
- [ ] `GUIDE-TOUR-047` Cover degisimi upload/attach/cleanup kurallarini korur.
- [ ] `GUIDE-TOUR-048` Save cift tiklamada duplicate change request/session
  update uretmez.
- [ ] `GUIDE-TOUR-049` Save basarisi sonrasi detail/list/public projection
  gereken approval durumuna gore yenilenir.
- [ ] `GUIDE-TOUR-050` Turist daha once satin aldiysa reservation snapshot eski
  satin alma bilgilerini korur.

### 10. Rehber Wallet, Earnings, Banka Hesabi ve Para Cekme

#### Wallet ve Hareketler

- [ ] `GUIDE-WALLET-001` Guide wallet available/withdrawable balance'i backend
  currency/minor unit ile gosterir.
- [ ] `GUIDE-WALLET-002` PENDING earning cekilebilir bakiye gibi sunulmaz.
- [ ] `GUIDE-WALLET-003` Recent earnings ve recent transactions canonical
  backend listelerinden gelir.
- [ ] `GUIDE-WALLET-004` `Tumunu Gor` Earnings ve Transactions hedeflerini dogru
  acar.
- [ ] `GUIDE-WALLET-005` Wallet loading/error/retry mevcut tasarimi korur.
- [ ] `GUIDE-WALLET-006` Transaction title turla baglantiliysa backend
  `referenceTitle`, diger turlerde yerel XML metni kullanir.
- [ ] `GUIDE-WALLET-007` GUIDE_EARNING, WITHDRAWAL ve EARNING_REVERSAL yon/
  renk/statuslari dogru gorunur.
- [ ] `GUIDE-WALLET-008` Transaction listesi LazyColumn/sayfalama ile buyuk
  veride duplicate veya donma uretmez.
- [ ] `GUIDE-WALLET-009` ALL, Tour Income, Withdrawal ve Reversal filtreleri
  canonical sonucu dogru daraltir.

#### Earnings

- [ ] `GUIDE-WALLET-010` Aylik kazanc backend monthly projection'dan gelir;
  Android history'yi indirip kendisi toplamaz.
- [ ] `GUIDE-WALLET-011` Yil secimi dogru yilin aylarini gosterir.
- [ ] `GUIDE-WALLET-012` Hizli yil degisiminde eski istek yeni yil sonucunu
  ezmez.
- [ ] `GUIDE-WALLET-013` PENDING, AVAILABLE ve REVERSED earning durumlari ayirt
  edilir.
- [ ] `GUIDE-WALLET-014` REVERSED earning toplam net kazanca dahil edilmez.
- [ ] `GUIDE-WALLET-015` Gross, platform fee ve net tutarlar birbirine uygun
  gosterilir.
- [ ] `GUIDE-WALLET-016` Empty yil/ay sonucu sifir/empty state olarak sunulur,
  hata gibi gorunmez.

#### Banka Hesabi

- [ ] `GUIDE-WALLET-017` Banka hesaplari canonical masked IBAN, banka adi ve
  default durumunu gosterir.
- [ ] `GUIDE-WALLET-018` Empty state ve ekleme aksiyonu dogru gorunur.
- [ ] `GUIDE-WALLET-019` Add sheet'te hesap sahibi adi ve sabit `TR` on ekli
  IBAN alani vardir; banka adi elle girilmez.
- [ ] `GUIDE-WALLET-020` IBAN yazildikca `TR` sabit kalir ve kalan 24 hane
  okunabilir gruplarla formatlanir.
- [ ] `GUIDE-WALLET-021` Harf/bosluk/yapistirma girdisi sanitize edilir ve 24
  haneyi asmaz.
- [ ] `GUIDE-WALLET-022` Mod-97 gecersiz IBAN submit edilemez.
- [ ] `GUIDE-WALLET-023` Gecerli IBAN'da Android TurkishBankCatalog hizli banka
  on gosterimi yapar.
- [ ] `GUIDE-WALLET-024` Katalogda bilinmeyen fakat format/mod-97 gecerli IBAN
  backend dogrulamasina gidebilir; Android otorite olmaz.
- [ ] `GUIDE-WALLET-025` Backend banka adini/IBAN'i canonical dogrular ve masked
  sonucu dondurur.
- [ ] `GUIDE-WALLET-026` Ayni IBAN ikinci kez eklenince uygun hata gorunur.
- [ ] `GUIDE-WALLET-027` Varsayilan hesap degistirme onayindan sonra tek default
  hesap kalir.
- [ ] `GUIDE-WALLET-028` Hesap secimi bottom sheet'indeki bir kerelik secim
  global default hesabi degistirmez.
- [ ] `GUIDE-WALLET-029` Hesap silme onay/iptal ve backend red durumlari dogru
  gorunur.
- [ ] `GUIDE-WALLET-030` Tam IBAN UI disinda log/notification/hata mesajina
  sizmaz.

#### Para Cekme

- [ ] `GUIDE-WALLET-031` Para Cek bottom sheet'i default banka hesabi ve
  available balance ile acilir.
- [ ] `GUIDE-WALLET-032` Banka hesabi yoksa kullanici banka hesaplarina uygun
  sekilde yonlendirilir/bilgilendirilir.
- [ ] `GUIDE-WALLET-033` Hesap degistirme yalniz bu cekim isteginin hedefini
  degistirir.
- [ ] `GUIDE-WALLET-034` `Tumunu Cek` available balance'i input'a yazar.
- [ ] `GUIDE-WALLET-035` Sifir/negatif/gecersiz tutar confirm olmaz.
- [ ] `GUIDE-WALLET-036` Bakiyeden fazla tutarda yetersiz bakiye dialogu
  gorunur ve API istegi atilmaz veya backend canonical red gosterilir.
- [ ] `GUIDE-WALLET-037` Backend minimum/maksimum limit reddi yerellestirilmis
  mesaja map edilir.
- [ ] `GUIDE-WALLET-038` Ilk onayda cekilen tutari onaylama dialogu dogru miktar
  ve masked hesabi gosterir.
- [ ] `GUIDE-WALLET-039` Onay iptalinde cekim istegi olusmaz.
- [ ] `GUIDE-WALLET-040` Onay devam ederken tekrar tiklama ayni idempotency key
  ile tek islem uretir.
- [ ] `GUIDE-WALLET-041` Basarili SIMULATED withdrawal sonuc dialogu ve
  canonical durumla gorunur.
- [ ] `GUIDE-WALLET-042` Withdrawal wallet reserve/balance, transaction ve
  withdrawal history'yi birlikte yeniler.
- [ ] `GUIDE-WALLET-043` PENDING/PROCESSING/COMPLETED/FAILED/CANCELLED statuslari
  desteklenen durumda dogru gorunur.
- [ ] `GUIDE-WALLET-044` Cift tiklama/process recreation ayni cekimi iki kez
  olusturmaz.
- [ ] `GUIDE-WALLET-045` Otomatik 3 is gunu transfer vaadi gosterilmez; manuel
  cekim ve canonical durum metni korunur.

### 11. Turist Ana Sayfa, Kesif, Arama ve Filtre

#### Tourist Home

- [ ] `TOURIST-HOME-001` Tourist root acilinca Home, Explore, Trips, Chat ve
  Profile bottom-bar hedefleri dogru gorunur.
- [ ] `TOURIST-HOME-002` Home popular tours ve best guides ilk yuklemede ortak
  loading/error/retry durumlarini dogru kullanir.
- [ ] `TOURIST-HOME-003` Popular tour ve best guide bolumleri birbirinden
  bagimsiz hata/yenileme durumunda diger gecerli icerigi silmez.
- [ ] `TOURIST-HOME-004` Kategori secimi popular listeyi canonical backend
  sorgusuyla yeniler.
- [ ] `TOURIST-HOME-005` Kategori temizlenince tum popular turlar geri gelir.
- [ ] `TOURIST-HOME-006` Popular kart baslik, cover, guide, konum, fiyat,
  rating, kapasite ve session kimligini backend'den kullanir.
- [ ] `TOURIST-HOME-007` Popular kart detayi ayni tur/session verilerini
  gosterir; farkli mock bilgi gorunmez.
- [ ] `TOURIST-HOME-008` Daha once satin alinmis popular tur detail'inde ikinci
  rezervasyon aksiyonu gorunmez.
- [ ] `TOURIST-HOME-009` Satin alinmamis ve bookable popular tur detail'inde
  rezervasyon aksiyonu gorunur.
- [ ] `TOURIST-HOME-010` Best guide karti dogru public guideId ile public
  profile gider.
- [ ] `TOURIST-HOME-011` Home refresh yeni puan/populerlik/capacity
  projection'larini canonical olarak gunceller.
- [ ] `TOURIST-HOME-012` Empty popular/best guide bolumu layout'u bozmaz ve
  sahte veri gostermez.
- [ ] `TOURIST-HOME-013` Buyuk veri/listelerde horizontal/vertical scroll
  akici, kart tiklama hedefleri ayrik ve bottom bar sabittir.

#### Explore Sekmeleri ve Arama

- [ ] `EXPLORE-001` Explore acilinca Tours sekmesi ve Guides sekmesi dogru
  basliklarla gorunur.
- [ ] `EXPLORE-002` Sekmeler kendi arama metni, sonuc, loading ve pagination
  state'ini korur.
- [ ] `EXPLORE-003` Tour arama metni yazildikca belirlenen debounce/istek
  davranisiyla backend sorgusu yapilir; her tus duplicate istek uretmez.
- [ ] `EXPLORE-004` Guide aramasi ad/profil alanlarina gore canonical sonucu
  getirir.
- [ ] `EXPLORE-005` Arama bas/son bosluklari normalize edilir.
- [ ] `EXPLORE-006` Hizli sorgu degisiminde eski response yeni sorgunun
  listesini ezmez.
- [ ] `EXPLORE-007` Sonuc sayisi backend totalElements ile uyumludur.
- [ ] `EXPLORE-008` Sonuc yoksa `Sonuc bulunamadi` empty state'i gorunur; retry
  oku gosterilmez.
- [ ] `EXPLORE-009` Internet/sunucu hatasi empty state'ten farkli olarak ortak
  error ve `Tekrar Dene` gosterir.
- [ ] `EXPLORE-010` Retry ayni aktif sorgu/filtrelerle tekrar istek atar.
- [ ] `EXPLORE-011` Arama metni temizlenince normal sonuc listesi geri gelir.
- [ ] `EXPLORE-012` `Aramayi ve filtreleri temizle` hem query hem applied filter
  state'ini temizler.
- [ ] `EXPLORE-013` Tour result karti dogru session detail'ine gider.
- [ ] `EXPLORE-014` Guide result karti dogru public profile gider.
- [ ] `EXPLORE-015` Geri donuste arama sorgusu, secili tab ve uygulanmis filtre
  urun kararina uygun korunur.

#### Tour Filtreleme

- [ ] `FILTER-001` Filter ekrani Explore back-stack'e ait ayni ViewModel state'ini
  kullanir.
- [ ] `FILTER-002` Filter ekraninda kategori, rating, fiyat araligi, ulke,
  sehir ve diller gorunur.
- [ ] `FILTER-003` Ulke degisince onceki ulkeye ait sehir temizlenir.
- [ ] `FILTER-004` Rating secimi/sifirlama dogru draft filter state'ini
  gunceller.
- [ ] `FILTER-005` Fiyat slider minimum/maksimum ve aralik degerlerini dogru
  gosterir.
- [ ] `FILTER-006` Dil secimi duplicate olusturmaz ve landscape modda
  tamamlanabilir.
- [ ] `FILTER-007` Kategori secimi ortak katalog ve ikonlarini kullanir.
- [ ] `FILTER-008` Filter alanlarini degistirip geri tusuyla cikmak uygulanmis
  filtreleri istemeden degistirmez.
- [ ] `FILTER-009` `Uygula` draft filtreleri applied filtre yapar, Explore'a
  doner ve gercek backend sorgusu baslatir.
- [ ] `FILTER-010` `Temizle` tum draft alanlarini varsayilana getirir.
- [ ] `FILTER-011` Uygulanmis filtreler sonucu bulunamazsa aktif filtreler
  gorunur/temizlenebilir kalir.
- [ ] `FILTER-012` Filter ekraninda bottom bar urun kararina gore gizlidir ve
  geri tusu Explore'a doner.
- [ ] `FILTER-013` Fiyat, kategori, rating, konum ve dil request query alanlari
  backend sozlesmesiyle uyumludur.

#### Pagination ve Liste Tutarliligi

- [ ] `EXPLORE-016` Listenin sonuna gelince yalniz sonraki sayfa istenir.
- [ ] `EXPLORE-017` `isLastPage` sonrasi gereksiz istek atilmaz.
- [ ] `EXPLORE-018` Append sonucu mevcut listenin sonuna sirayi bozmadan
  eklenir.
- [ ] `EXPLORE-019` Ayni kayit sayfalar arasinda gelirse duplicate kart
  olusmaz/contract hatasi gorulur.
- [ ] `EXPLORE-020` Append hatasi ilk sayfa sonucunu silmez.
- [ ] `EXPLORE-021` Append retry sonraki ayni sayfayi tekrar ister; sayfa atlamaz.
- [ ] `EXPLORE-022` Yeni arama/filtre onceki pagination cursor/page state'ini
  sifirlar.
- [ ] `EXPLORE-023` Buyuk demo veri setinde scroll belirgin ANR/donma uretmez.

### 12. Tourist Public Tour Detail ve Satin Alinabilirlik

- [ ] `TOUR-DETAIL-001` Home/search/profile kartindan detail dogru `sessionId`
  ile acilir.
- [ ] `TOUR-DETAIL-002` Detail cover, title, guide, category, diller, konum,
  tarih/saat, sure, fiyat, kapasite, aciklama, bulusma ve review'lari canonical
  backend verisinden gosterir.
- [ ] `TOUR-DETAIL-003` Kart ile detail ayni tur/session temel bilgilerini
  gosterir.
- [ ] `TOUR-DETAIL-004` Detail tablari ve uzun aciklama/review listesi dogru
  scroll olur.
- [ ] `TOUR-DETAIL-005` Guide alani public guide profiline gidebiliyorsa dogru
  kimligi kullanir.
- [ ] `TOUR-DETAIL-006` Review listesi sayfali backend kaynagindan gelir ve
  empty state dogrudur.
- [ ] `TOUR-DETAIL-007` OPEN, gelecekte, approved ve bos kapasitesi olan session
  satin alma aksiyonu gosterir.
- [ ] `TOUR-DETAIL-008` CLOSED session yeni kullaniciya satin alinabilir gibi
  sunulmaz.
- [ ] `TOUR-DETAIL-009` FULL session capacity/bookedCount'u dogru gosterir ve
  satin alma aksiyonu disabled/gizli olur.
- [ ] `TOUR-DETAIL-010` CANCELLED veya COMPLETED session satin alma aksiyonu
  gostermez.
- [ ] `TOUR-DETAIL-011` Suresi gecmis session backend gecikse bile UI'da yeni
  satin alim icin uygun gorunmez.
- [ ] `TOUR-DETAIL-012` Mevcut reservation sahibi detail'i gorebilir fakat
  ikinci satin alma aksiyonu gormez.
- [ ] `TOUR-DETAIL-013` Detail acikken guide session'i kapatir/iptal eder veya
  son koltuk dolar; Checkout'a geciste backend canonical red mesaji gorunur.
- [ ] `TOUR-DETAIL-014` Silinmis/yetkisiz/gecersiz session ID kontrollu error ve
  guvenli geri donus verir.
- [ ] `TOUR-DETAIL-015` Loading/error/retry tasarimi ortak davranisi korur.
- [ ] `TOUR-DETAIL-016` Bottom bar detail ekraninda urun kararina uygun
  gorunur/gizlenir ve geri onceki kart listesine doner.

### 13. Rezervasyon, Gezilerim, Iptal ve Snapshot

#### Checkout Oncesi Reservation Kurallari

- [ ] `RESERVATION-001` Checkout acilisinda session canonical olarak tekrar
  okunur; eski kart verisiyle dogrudan payment baslamaz.
- [ ] `RESERVATION-002` Session artik bookable degilse kullaniciya neden
  gosterilir ve payment olusturulmaz.
- [ ] `RESERVATION-003` Katilimci sayisi minimum 1'den asagi dusmez.
- [ ] `RESERVATION-004` Katilimci sayisi kalan kapasiteyi asamaz.
- [ ] `RESERVATION-005` Katilimci sayisi degistikce toplam canonical USD tutari
  dogru gosterilir; backend quote yine otoritedir.
- [ ] `RESERVATION-006` Ayni kullanicinin ayni session icin mevcut reservation'i
  varsa ikinci reservation engellenir.
- [ ] `RESERVATION-007` Iki turist son koltugu ayni anda almaya calistiginda
  yalniz biri basarili olur; digeri anlamli kapasite reddi gorur.

#### Gezilerim Listesi

- [ ] `TRIPS-001` Upcoming ve Past sekmeleri backend list type/status ile dogru
  ayrilir.
- [ ] `TRIPS-002` CONFIRMED gelecekteki reservation Upcoming'ta gorunur.
- [ ] `TRIPS-003` COMPLETED reservation Past'te gorunur.
- [ ] `TRIPS-004` CANCELLED reservation tamamlanmis gibi sunulmaz ve iptal
  durumu ayirt edilir.
- [ ] `TRIPS-005` EXPIRED/PENDING_PAYMENT urun kuralina gore yanlis aktif gezi
  olarak gorunmez.
- [ ] `TRIPS-006` Kart ve reservation detail ayni `reservationId` ve satin alma
  snapshot'ini gosterir.
- [ ] `TRIPS-007` Upcoming/Past empty, loading, error, retry ve pagination
  durumlari ayri dogru calisir.
- [ ] `TRIPS-008` Sekme degisimi ve refresh listeleri birbirine karistirmaz.
- [ ] `TRIPS-009` Buyuk listede pagination duplicate/kayip/gecersiz sira
  uretmez.
- [ ] `TRIPS-010` Kart tiklamasi typed ReservationDetail route'una gider.
- [ ] `TRIPS-011` Upcoming ve tamamlanmis Past karti
  `Rezervasyonunuz: X kisi` gosterir; detail ayni session icin
  `bookedCount/capacity` gosterir. Iptal karti ve detail yaniltici doluluk
  gostermez.
- [ ] `TRIPS-012` Upcoming/Past reservation detail canonical tur ortalama puani
  ve toplam yorum sayisini normal tur detail ile ayni gosterir; yeni yorumdan
  sonra iki deger backend sonucuyla yenilenir.

#### Reservation Snapshot Detail

- [ ] `RESERVATION-008` Detail satin alma anindaki tur title, cover, guide,
  tarih/saat, fiyat, katilimci ve temel snapshot alanlarini gosterir.
- [ ] `RESERVATION-009` Guide daha sonra turu edit etse bile satin alinmis
  snapshot'in tarih/fiyat/temel bilgileri geriye donuk degismez.
- [ ] `RESERVATION-010` Reservation status, cancellation actor/reason ve refund
  bilgisi dogru gorunur.
- [ ] `RESERVATION-011` Detail bir fis/gezi ozeti gibi kullanilir; yeniden
  `Turu Rezerve Et` aksiyonu gostermez.
- [ ] `RESERVATION-012` Silinmis/yetkisiz reservation ID uygulamayi cokertmez ve
  backend 403/404 uygun gosterilir.
- [ ] `RESERVATION-013` Tourist B, Tourist A reservation detail'ini ID tahmin
  ederek acamaz.

#### Tourist Cancellation ve Refund

- [ ] `CANCEL-001` Yalniz cancellable reservation iptal aksiyonu gosterir.
- [ ] `CANCEL-002` Iptal onay/iptal dialogu dogru reservation/tur bilgisini
  gosterir.
- [ ] `CANCEL-003` Double tap ayni idempotency key ile tek cancellation uretir.
- [ ] `CANCEL-004` Basarili cancellation reservation'i Past/cancelled duruma
  tasir.
- [ ] `CANCEL-005` Full refund eligibility ve REQUESTED/PROCESSING/SUCCEEDED
  durumlari kullaniciya dogru gosterilir.
- [ ] `CANCEL-006` No-refund policy basarili full refund gibi sunulmaz.
- [ ] `CANCEL-007` Refund FAILED ve MANUAL_REVIEW ayri, anlasilir sonuc verir.
- [ ] `CANCEL-008` Cancellation sonrasi kapasite backend tarafinda bir kez geri
  artar.
- [ ] `CANCEL-009` Wallet/card refund transaction/payment state'e yalniz bir kez
  yansir.
- [ ] `CANCEL-010` Terminal/not-cancellable reservation reddinde kart/list state
  sessizce degismez.
- [ ] `CANCEL-011` Guide session iptali tourist gezi/detail, sebep, refund ve
  notification'i canonical olarak yeniler.

### 14. Review ve Puanlama

- [ ] `REVIEW-001` Yalniz COMPLETED ve review-eligible reservation `Degerlendir`
  aksiyonu gosterir.
- [ ] `REVIEW-002` Upcoming, cancelled, expired veya tamamlanmamis reservation
  review aksiyonu gostermez.
- [ ] `REVIEW-003` Review bottom sheet acilinca rating secimi ve yorum alani
  dogru gorunur.
- [ ] `REVIEW-004` Rating secmeden submit olmaz.
- [ ] `REVIEW-005` Yorum bos birakilabiliyorsa/degilse backend ve UI kuralina
  uygun davranir.
- [ ] `REVIEW-006` Cok uzun yorum client/backend limitinde uygun hata verir.
- [ ] `REVIEW-007` Klavye ve landscape modda submit aksiyonu erisilebilir
  kalir.
- [ ] `REVIEW-008` Submit devam ederken sheet kapanmaz/cift review olusmaz.
- [ ] `REVIEW-009` Basarili review success dialogunu gosterir ve tekrar review
  aksiyonunu kaldirir.
- [ ] `REVIEW-010` Ayni reservation'a ikinci review `REVIEW_ALREADY_EXISTS`
  mesaji verir.
- [ ] `REVIEW-011` Uygun olmayan reservation backend
  `REVIEW_NOT_ALLOWED` mesaji verir.
- [ ] `REVIEW-012` Tourist B, Tourist A reservation'ina review gonderemez.
- [ ] `REVIEW-013` Review sonrasi tour average rating ve review count canonical
  olarak yenilenir.
- [ ] `REVIEW-014` Review sonrasi guide average rating, level/progress,
  dashboard ve public profile yenilenir.
- [ ] `REVIEW-015` Popular/search/detail kartlari yeni rating'i kullanir.
- [ ] `REVIEW-016` Guide cihazinda rating/comment notification history ve FCM
  olayi olusur.
- [ ] `REVIEW-017` Review listesinde actor adi/avatar, rating, yorum ve tarih
  dogru gorunur.
- [ ] `REVIEW-018` Review submit sonrasi uygulama restart etmeden iki roldeki
  ilgili projection'lar yenilenir.

### 15. Tourist Wallet ve Cuzdana Para Yukleme

#### Wallet Gorunumu ve Hareketler

- [ ] `TOURIST-WALLET-001` Wallet canonical bakiye ve currency code'u backend
  degeriyle gosterir.
- [ ] `TOURIST-WALLET-002` Default saved card provider metadata'siyla gorunur;
  kart yoksa empty state dogrudur.
- [ ] `TOURIST-WALLET-003` `Kayitli Kartlari Yonet` account Saved Cards ekranina
  gider.
- [ ] `TOURIST-WALLET-004` Recent transactions backend sirasi ve tutarlariyla
  gorunur.
- [ ] `TOURIST-WALLET-005` `Tumunu Gor` sayfali Wallet Transactions ekranini
  acar.
- [ ] `TOURIST-WALLET-006` ALL, Top Up, Tour Purchase ve Refund filtreleri dogru
  backend query/state sonucunu gosterir.
- [ ] `TOURIST-WALLET-007` Credit/debit yonu, renk, isaret, status ve
  `referenceTitle` dogru gorunur.
- [ ] `TOURIST-WALLET-008` Loading/empty/error/retry/append state'leri ortak
  tasarim ve pagination davranisina uyar.
- [ ] `TOURIST-WALLET-009` Wallet refresh basarili top-up/purchase/refund
  sonrasi canonical bakiye ve hareketleri birlikte yeniler.
- [ ] `TOURIST-WALLET-010` Dar fiziksel cihaz, buyuk yazi olcegi ve yatay/dikey
  gorunumde `Yonet` tek satirda kalir; soldaki kart bilgisi tasma olmadan
  ellipsis uygular ve aksiyon dogru ekrani acar.

#### Top-Up Girisi ve Quote

- [ ] `TOPUP-001` Para Yukle bottom sheet'i acilir ve tutar alani/preset
  degerler gorunur.
- [ ] `TOPUP-002` Preset secimi tutar alanina dogru major-unit degeri yazar.
- [ ] `TOPUP-003` Sifir, negatif, bos, gecersiz veya overflow tutar devam etmez.
- [ ] `TOPUP-004` Backend min/max top-up limit reddi yerellestirilmis mesaj
  verir.
- [ ] `TOPUP-005` Desteklenen charge currency listesi backend'den gelir;
  Android sabit liste uydurmaz.
- [ ] `TOPUP-006` USD/TRY/EUR/GBP yalniz backend config'te etkinse gorunur.
- [ ] `TOPUP-007` Currency degisince quote yeniden istenir ve eski quote yeni
  secimi ezmez.
- [ ] `TOPUP-008` Quote platform USD amount ile provider charge amount/currency
  degerlerini birbirine karistirmadan gosterir.
- [ ] `TOPUP-009` FX rate/source/expiry urun tasariminda gosteriliyorsa backend
  snapshot'iyla uyumludur; Android kur hesaplamaz.
- [ ] `TOPUP-010` Quote alinamazsa/expired olursa kullanici yeni quote alabilir;
  eski quote ile checkout baslamaz.
- [ ] `TOPUP-011` Devam Et cift tiklamada tek payment intent olusur.

### 16. Tour Checkout, Wallet Purchase ve iyzico Hosted Payment

#### Checkout Ozeti ve Odeme Yontemi

- [ ] `PAYMENT-001` Checkout tur title, tarih, kisi basi fiyat, katilimci ve
  toplam platform USD tutarini dogru gosterir.
- [ ] `PAYMENT-002` Katilimci sayisi degisiminde toplam gorunum guncellenir;
  final tutar backend quote'tan gelir.
- [ ] `PAYMENT-003` Wallet ve Hosted Card odeme yontemleri urun kuralina uygun
  secilebilir.
- [ ] `PAYMENT-004` Radio/checkbox secili renkleri brand tasarimina uyar.
- [ ] `PAYMENT-005` Wallet seciminde mevcut bakiye canonical olarak gorunur.
- [ ] `PAYMENT-006` Wallet bakiyesi yetersizse checkout baslatmadan anlasilir
  hata gorunur; backend de ayni siniri korur.
- [ ] `PAYMENT-007` Hosted Card seciminde charge currency ve quote secimi
  kullanilabilir.
- [ ] `PAYMENT-008` Kosullar okunup kabul edilmeden checkout devam etmez.
- [ ] `PAYMENT-009` Checkout devam ederken buton disabled/loading olur ve cift
  payment olusmaz.
- [ ] `PAYMENT-010` Ayni kullanici niyetinde retry/recomposition/process
  recreation mevcut idempotency key/payment'i yeniden kullanir.

#### Wallet ile Tur Satin Alma

- [ ] `PAYMENT-011` Yeterli bakiye ve bookable session ile wallet purchase
  atomik basarili olur.
- [ ] `PAYMENT-012` Basari yalniz payment `SUCCEEDED` ve reservation
  `CONFIRMED` birlikteyse gosterilir.
- [ ] `PAYMENT-013` Payment, reservation ve ledger debit ayni islem sonucunda
  birlikte olusur.
- [ ] `PAYMENT-014` Wallet bakiye/transaction, Trips ve session kapasitesi
  basari sonrasi birlikte yenilenir.
- [ ] `PAYMENT-015` Yetersiz bakiye, dolu/kapali session veya duplicate
  reservation durumunda hicbir kismi debit/reservation kalmaz.
- [ ] `PAYMENT-016` Double submit wallet'tan iki kez dusmez.

#### Hosted iyzico WebView

- [ ] `PAYMENT-017` Backend `REQUIRES_ACTION` sonucu HTTPS payment page URL ile
  HostedPayment ekranini acar.
- [ ] `PAYMENT-018` HTTP, file, content veya guvenilmeyen arbitrary URL WebView'da
  acilmaz.
- [ ] `PAYMENT-019` WebView secili uygulama dili icin backend'e TR/EN locale
  gonderir; desteklenmeyen dil guvenli EN fallback kullanir.
- [ ] `PAYMENT-020` iyzico hosted kart alanlari provider tarafinda gorunur;
  Android native ham kart formu yoktur.
- [ ] `PAYMENT-021` Checkout Form gerekiyorsa JavaScript calisir fakat
  `addJavascriptInterface` bridge yoktur.
- [ ] `PAYMENT-022` SSL hatasi bypass edilmez; payment iptal/hata durumuna gider.
- [ ] `PAYMENT-023` Mixed content ve local file access kapali kalir.
- [ ] `PAYMENT-024` WebView geri/topbar davranisi kullaniciyi belirsiz payment
  state'inde birakmaz; urun cancel akisi kullanilir.
- [ ] `PAYMENT-025` Callback HTML/JSON veya sayfanin kapanmasi tek basina basari
  sayilmaz.
- [ ] `PAYMENT-026` POST callback `shouldOverrideUrlLoading` tek kanit kabul
  edilmeden page lifecycle sonrasi backend status polling baslar.
- [ ] `PAYMENT-027` WebView page load hatasi retry/cancel aksiyonlarini dogru
  gosterir.
- [ ] `PAYMENT-028` Hosted ekranda `karti kaydet` secilirse provider-backed saved
  method olusur ve sonra listede maskeli gorunur.
- [ ] `PAYMENT-029` Raw kart numarasi, SKT, CVV, provider token veya checkout
  form body Android state/log/storage'a girmez.

#### Payment Status, Sonuclar ve Recovery

- [ ] `PAYMENT-030` PENDING/VERIFYING durumunda backend kontrollu polling yapilir
  ve UI anlasilir bekleme gosterir.
- [ ] `PAYMENT-031` Polling timeout sonsuz spinner yerine TIMEOUT veya tekrar
  sorgulanabilir canonical sonuc verir.
- [ ] `PAYMENT-032` SUCCEEDED tour booking ancak CONFIRMED reservation ile
  success ekranina gider.
- [ ] `PAYMENT-033` SUCCEEDED top-up ancak yenilenmis wallet balance ve yeni
  transaction okununca success gosterir.
- [ ] `PAYMENT-034` FAILED sonucu yerellestirilmis provider/backend hata
  kategorisini gosterir.
- [ ] `PAYMENT-035` Invalid card/CVV, insufficient card funds, decline ve 3DS
  failure birbirine uygun hata sonucu verir.
- [ ] `PAYMENT-036` User cancel `CANCELLED` sonucu verir; success gostermez.
- [ ] `PAYMENT-037` Payment not cancellable sonucu canonical status'a doner ve
  kullaniciyi belirsiz birakmaz.
- [ ] `PAYMENT-038` Refund REQUESTED/PROCESSING/SUCCEEDED/FAILED/MANUAL_REVIEW
  ayri metin ve aksiyonlarla gorunur.
- [ ] `PAYMENT-039` Gec callback kapasite varsa reservation'i tek kez confirm
  eder; kapasite yoksa tek full refund/manual review sonucunu gosterir.
- [ ] `PAYMENT-040` Duplicate callback/webhook/payment status kapasite, wallet
  veya reservation'i iki kez degistirmez.
- [ ] `PAYMENT-041` Success ekraninda GuideMate tasarimi, sonuc ikonu/gorseli,
  referans ve Tamam aksiyonu dogru gorunur.
- [ ] `PAYMENT-042` Tour success Tamam Trips'e gider; sonra Home bottom bar
  dogru Home'u acar, payment ekranina geri donmez.
- [ ] `PAYMENT-043` Top-up success Tamam Wallet'a doner ve bakiye yenilenmistir.
- [ ] `PAYMENT-044` Final success ekrani topbar/sistem geri tusu urun kararina
  gore kilitlidir; yalniz Tamam ile cikilir.
- [ ] `PAYMENT-045` Uygulama payment sirasinda oldurulurse terminal olmayan
  `paymentId` DataStore'dan okunur ve backend status yeniden sorgulanir.
- [ ] `PAYMENT-046` Recovery yeni payment intent/quote olusturmaz.
- [ ] `PAYMENT-047` Terminal sonuc cozulunce pending payment storage temizlenir;
  sonraki acilista eski sonuc yeniden acilmaz.
- [ ] `PAYMENT-048` Farkli kullanici login olunca onceki hesabin pending payment'i
  acilmaz.

### 17. Chat REST, STOMP ve Kullanici Deneyimi

#### Sohbet Baslatma ve Liste

- [ ] `CHAT-001` Tourist rezervasyon yapmadan public guide profilinden mesaj
  baslatabilir.
- [ ] `CHAT-002` Find-or-create yeni guide-tourist cifti icin tek conversation
  olusturur.
- [ ] `CHAT-003` Ayni cifte tekrar mesaj baslatmak ayni `chatId`yi acar.
- [ ] `CHAT-004` Guide kendi private chat listesini, tourist kendi listesini
  gorur; baska kullanicinin sohbeti sizmaz.
- [ ] `CHAT-005` Conversation listesi remote ad/avatar, last message, zaman ve
  unread count'u canonical backend verisinden gosterir.
- [ ] `CHAT-006` Yeni mesaj conversation'i listenin dogru sirasina getirir.
- [ ] `CHAT-007` Bos liste uygun empty state gosterir.
- [ ] `CHAT-008` Liste loading/error/retry mevcut mesajlari gereksiz silmez.
- [ ] `CHAT-009` Bottom-bar chat badge ayni repository unread Flow'undan gelir.

#### Mesaj Detayi ve Gonderim

- [ ] `CHAT-010` Detail typed route yalniz `chatId` tasir ve dogru conversation'i
  acar.
- [ ] `CHAT-011` Topbar remote kullanici adini ve avatarini backend projection'dan
  gosterir.
- [ ] `CHAT-012` Mesaj yonu `senderId == currentUserId` ile dogru sag/sol
  balona map edilir.
- [ ] `CHAT-013` Bos/yalniz whitespace mesaj gonderilmez.
- [ ] `CHAT-014` 2000 karakter siniri UI/backend ile uyumludur; asan mesaj uygun
  hata verir.
- [ ] `CHAT-015` Uzun mesaj satir kirar ve ekran genisligini bozmaz.
- [ ] `CHAT-016` Send tiklaninca mesaj `PENDING` olarak hemen gorunur.
- [ ] `CHAT-017` REST ACK/STOMP echo ayni mesaji server id/time ile `SENT` yapar;
  duplicate balon olusmaz.
- [ ] `CHAT-018` Ag hatasinda mesaj `FAILED` gorunur ve retry yalniz basarisiz
  mesajda erisilebilir.
- [ ] `CHAT-019` Retry ayni `clientMessageId` ile tek canonical mesaj olusturur.
- [ ] `CHAT-020` Basarili retry FAILED gorunumunu SENT'e cevirir.
- [ ] `CHAT-021` Klavye acilinca input ve son mesaj gorunur, scroll davranisi
  kullanilabilir kalir.
- [ ] `CHAT-022` Eski mesajlar yukariya kaydirinca cursor ile sayfalanir.
- [ ] `CHAT-023` Cursor append duplicate/kayip mesaj veya sira bozuklugu
  olusturmaz.
- [ ] `CHAT-024` Sunucu zamani cihaz saatinden farkli olsa bile mesaj sirasi
  canonical zamana gore dogrudur.

#### Realtime, Read ve Offline

- [ ] `CHAT-025` Iki cihaz ayni backend'e bagliyken mesaj karsi cihazda STOMP
  ile anlik gorunur.
- [ ] `CHAT-026` Acik conversation'a gelen mesaj read endpoint/unread state'i
  urun kuralina gore gunceller.
- [ ] `CHAT-027` Sohbet acilinca unread badge backend canonical degerine iner.
- [ ] `CHAT-028` Uygulama baska sekmedeyken gelen mesaj chat badge ve
  conversation listesine yansir.
- [ ] `CHAT-029` Socket kopunca reconnect/resubscribe olur ve REST resync eksik
  mesaji tamamlar.
- [ ] `CHAT-030` Reconnect + REST + STOMP ayni mesaji duplicate etmez.
- [ ] `CHAT-031` Uygulama kapaliyken mesaj PostgreSQL'de kalir; donuste history'de
  gorunur.
- [ ] `CHAT-032` Logout eski STOMP subscription'i kapatir; yeni hesaba onceki
  mesaj gelmez.
- [ ] `CHAT-033` Tourist/Guide baska `chatId` tahmin ederek history okuyamaz veya
  mesaj gonderemez.
- [ ] `CHAT-034` Chat FCM bildirimi tiklaninca auth/root hazir olduktan sonra
  dogru `chatId` detail'ine gider.
- [ ] `CHAT-035` Tourist chat detail topbar'inda rehber avatar/adi birlikte
  tiklaninca dogru public rehber profili acilir ve geri ayni sohbete doner;
  Guide chat topbar'i public tourist profil olmadigi icin tiklanabilir olmaz.
- [ ] `CHAT-036` Rehber ve turist sohbet listesinde satir sola kaydirilinca
  yalniz silme aksiyonu kadar acilir; sohbet karti tamamen ekran disina cikmaz.
- [ ] `CHAT-037` Acik aksiyonda yalniz kirmizi cop ikonu gorunur; kirmizi arka
  plan veya `Sil` metni eklenmez ve kart kendi rengini korur.
- [ ] `CHAT-038` Cop ikonuna basinca ortak silme onayi acilir; vazgecince sohbet
  silinmez ve satir kapali konumuna doner.
- [ ] `CHAT-039` Bir sohbet satiri acikken baska satir sola kaydirilinca onceki
  kapanir; onayli silme sonrasi alttaki sohbetler dogal sirayla yukari gelir.
- [ ] `CHAT-040` Silme onay dialogu sohbet edilen kullanicinin guncel adini
  dinamik olarak gosterir ve teknik hesap detayi icermeden acik bir onay metni
  sunar.

### 18. Notification History, FCM ve Semantic Navigation

#### Izin, Cihaz Kaydi ve Channel

- [ ] `NOTIFY-001` Android 13+ notification izni uygun zamanda ve acik
  gerekceyle istenir.
- [ ] `NOTIFY-002` Izin kabul edilince GuideMate notification channel'i ve
  sistem bildirimi calisir.
- [ ] `NOTIFY-003` Izin reddedilince uygulama ici history, unread ve preferences
  calismaya devam eder.
- [ ] `NOTIFY-004` Kalici redde uygulama crash/sonsuz tekrar izin dialogu
  olusturmaz.
- [ ] `NOTIFY-005` Authenticated cihaz kaydi Firebase Installation ID ve
  GuideMate installation ID ile backend'e gider.
- [ ] `NOTIFY-006` Kayit istegi raw FCM token/secret'i gereksiz loglamaz.
- [ ] `NOTIFY-007` Firebase installation/token yenilenmesi backend cihaz kaydini
  dogru gunceller.
- [ ] `NOTIFY-008` Logout backend device registration'ini pasiflestirir ve local
  notification/unread state'i temizler.
- [ ] `NOTIFY-009` Logout kalici app installation ID'yi gereksiz silmez.
- [ ] `NOTIFY-010` Hesap degistirince ayni cihaz yeni authenticated kullaniciya
  kaydolur, eski kullanici push'i gelmez.

#### Uygulama Ici Bildirimler

- [ ] `NOTIFY-011` Guide ve tourist topbar notification aksiyonu ortak bottom
  sheet'i acar.
- [ ] `NOTIFY-012` Bildirim history loading/error/retry ve pagination durumlari
  dogru calisir.
- [ ] `NOTIFY-013` Empty history uygun bos durum gosterir.
- [ ] `NOTIFY-014` Notification item type ikonu, baslik, actor adi, govde ve
  zamani dogru gosterir.
- [ ] `NOTIFY-015` Sistem bildiriminde actor null ise `null`/bos anlamsiz ad
  basilmaz.
- [ ] `NOTIFY-016` Kullanici kaynakli bildirim `actorDisplayName` degerini
  backend DTO'sundan kullanir; ek user sorgusu atmaz.
- [ ] `NOTIFY-017` Paneli yalniz acmak tum bildirimleri otomatik read yapmaz.
- [ ] `NOTIFY-018` Tek bildirime tiklamak yalniz o bildirimi read yapar.
- [ ] `NOTIFY-019` `Tumunu okundu isaretle` backend unread count'u sifirlar.
- [ ] `NOTIFY-020` Read/read-all hata verirse local badge sessizce yanlis sifira
  dusmez.
- [ ] `NOTIFY-021` Topbar badge, bottom sheet ve guide home son bildirimleri ayni
  canonical read/unread sonucunu gosterir.
- [ ] `NOTIFY-022` Liste sonuna gelince page append duplicate/kayip bildirim
  olusturmaz.

#### Sistem Bildirimi Tasarimi

- [ ] `NOTIFY-023` Sistem cubugunda uygulama etiketi `GuideMate` gorunur.
- [ ] `NOTIFY-024` Gecici small icon Android kurallarina uygun tek renk ikon
  olarak gorunur; default bos/kare ikon olmaz.
- [ ] `NOTIFY-025` TOUR, CHAT, COMMENT, RATING, PAYMENT, SECURITY ve GENERAL
  kategori ikonlari uygulama ici gorunumle anlamsal olarak tutarlidir.
- [ ] `NOTIFY-026` Sistem ikon rengi notr/kararlastirilan tasarimdadir; rastgele
  brand color zorlanmaz.
- [ ] `NOTIFY-027` Baslik ve govde NotificationType'a gore XML resource'tan
  yerellestirilir.
- [ ] `NOTIFY-028` Uzun actor/tur adi sistem bildirimi ve bottom sheet layout'unu
  bozmaz.
- [ ] `NOTIFY-029` Foreground'da push geldiginde hem uygulama state'i hem urun
  kararina gore sistem bildirimi tutarli davranir.
- [ ] `NOTIFY-030` Background'da push sistem cubugunda gorunur.
- [ ] `NOTIFY-031` Uygulama tamamen kapaliyken push gorunur ve tiklama cold start
  akisini tamamlar.
- [ ] `NOTIFY-032` FCM gecikir/kaybolursa foreground/panel refresh REST history
  ve unread'i canonical backend'den toparlar.

#### Tum Notification Turleri

- [ ] `NOTIFY-033` `TOUR_APPROVED` guide'a gelir, tur detail/ACTIVE state'ine
  gider.
- [ ] `NOTIFY-034` `TOUR_REJECTED` guide'a gelir, reject reason gorulebilen tur
  hedefini acar.
- [ ] `NOTIFY-035` `TOUR_CHANGE_APPROVED` guide detail/list canonical state'ini
  yeniler.
- [ ] `NOTIFY-036` `TOUR_CHANGE_REJECTED` guide'a uygun sebep/hedef gosterir.
- [ ] `NOTIFY-037` `TOUR_PURCHASED` guide'a tourist actor adi ve ilgili tur/
  session hedefiyle gelir.
- [ ] `NOTIFY-038` `RESERVATION_CONFIRMED` tourist'e ilgili reservation/trips
  hedefiyle gelir.
- [ ] `NOTIFY-039` `RESERVATION_CANCELLED` ilgili role reservation/tur hedefi ve
  canonical state verir.
- [ ] `NOTIFY-040` `TOUR_CANCELLED` tourist reservation detail/refund state'ine
  guvenli sekilde gider.
- [ ] `NOTIFY-041` `TOUR_COMPLETED` ilgili gezi/tur hedefini acar.
- [ ] `NOTIFY-042` `REVIEW_REQUEST` yalniz uygun completed reservation'a gider.
- [ ] `NOTIFY-043` `RATING_RECEIVED` guide'a actor ve session/tur hedefiyle gelir.
- [ ] `NOTIFY-044` `COMMENT_RECEIVED` guide'a actor ve session/tur hedefiyle
  gelir.
- [ ] `NOTIFY-045` `PAYMENT_SUCCEEDED` ilgili payment/reservation veya wallet
  sonucuna gider.
- [ ] `NOTIFY-046` `PAYMENT_FAILED` ilgili payment durumuna gider ve success
  gostermez.
- [ ] `NOTIFY-047` `REFUND_REQUESTED` canonical refund durumunu acar.
- [ ] `NOTIFY-048` `REFUND_COMPLETED` wallet/payment/reservation projection'ini
  yeniler.
- [ ] `NOTIFY-049` `REFUND_FAILED` uygun hata/inceleme hedefini acar.
- [ ] `NOTIFY-050` `REFUND_MANUAL_REVIEW` kullaniciya incelemede oldugunu acik
  gosterir.
- [ ] `NOTIFY-051` `EARNING_AVAILABLE` guide earnings/wallet hedefini ve yeni
  bakiyeyi yeniler.
- [ ] `NOTIFY-052` `WITHDRAWAL_COMPLETED` guide wallet transactions hedefini
  acar.
- [ ] `NOTIFY-053` `CHAT_MESSAGE` dogru `chatId` detail'ine gider.
- [ ] `NOTIFY-054` `UPCOMING_TOUR_REMINDER` role uygun reservation/tur hedefini
  acar ve duplicate reminder olusmaz.
- [ ] `NOTIFY-055` `SECURITY_ALERT` role uygun profil/hesap guvenlik hedefini
  acar veya guvenli root'a duser.
- [ ] `NOTIFY-056` `UNKNOWN` type uygulamayi cokertmez ve guvenli role home/
  notification fallback'ine gider.

#### Semantic Target Guvenligi ve Stack

- [ ] `NOTIFY-057` Payload ham route/UI metni degil canonical ID'ler tasir.
- [ ] `NOTIFY-058` `chatId`, `tourId`, `sessionId`, `reservationId` ve
  `paymentId` ilgili typed destination'a dogru map edilir.
- [ ] `NOTIFY-059` Eksik target ID uygulamayi cokertmez ve role home/history
  fallback'i kullanir.
- [ ] `NOTIFY-060` Silinmis veya artik erisilemeyen hedef controlled error/
  fallback verir.
- [ ] `NOTIFY-061` Yetkisiz hedef ID backend object authorization'dan gecmez;
  push ID'si yetki kaniti sayilmaz.
- [ ] `NOTIFY-062` Uygulama kapaliyken target root/auth hazir olana kadar bir kez
  bekler ve sonra acilir.
- [ ] `NOTIFY-063` Hedef acildiktan sonra geri tusu anlamsiz duplicate stack veya
  baska role ekranina donmez.
- [ ] `NOTIFY-064` Ayni notification intent configuration change'de tekrar
  tekrar navigate etmez.

### 19. Navigation, Geri Tusu ve Ekran Gecisleri

- [ ] `NAV-001` Her bottom-bar hedefi tek kopya ve kendi scroll/state gecmisiyle
  acilir.
- [ ] `NAV-002` Ayni bottom-bar ikonuna tekrar tiklama duplicate destination
  olusturmaz.
- [ ] `NAV-003` Guide/tourist root switch tum onceki role stack'ini temizler.
- [ ] `NAV-004` Account graph full-screen acilir ve role bottom bar'i account
  icinde yanlis gorunmez.
- [ ] `NAV-005` Tour publish/edit/detail, filter, reservation detail, chat detail,
  wallet transaction ve payment ekranlarinda topbar baslik/geri gorunurlugu
  dogrudur.
- [ ] `NAV-006` Filter'dan geri Explore'a, public guide'dan geri acan listeye,
  detail'den geri acan karta doner.
- [ ] `NAV-007` Payment Hosted -> Status -> Success stack'i geriye donerek eski
  payment sayfalarini tekrar acmaz.
- [ ] `NAV-008` Tour purchase success Trips'e, top-up success Wallet'a gider.
- [ ] `NAV-009` Trips'ten Home'a basinca Home gorunur; eski success ekrani geri
  gelmez.
- [ ] `NAV-010` Sign In -> Sign Up/Forgot -> Sign In stack'i eski formu geri
  acmaz.
- [ ] `NAV-011` Change password success root switch sonrasi account ekranina
  geri donulamaz.
- [ ] `NAV-012` Dialog/bottom sheet acikken sistem geri once overlay'i kapatir;
  kritik submitting durum istisnasi korunur.
- [ ] `NAV-013` Typed route gecersiz/eksik ID'de crash yerine guvenli state
  verir.
- [ ] `NAV-014` Uygulama background/foreground sonrasi aktif destination
  beklenmedik sekilde degismez.

### 20. Offline, Process Death ve Hesap Izolasyonu

#### Offline ve Ag Gecisleri

- [ ] `OFFLINE-001` Uygulama internet kapaliyken acildiginda cached session/root
  politikasina uygun davranir ve sonsuz spinner kalmaz.
- [ ] `OFFLINE-002` Cached olmayan liste/detail ortak retry edilebilir hata
  gosterir.
- [ ] `OFFLINE-003` Content yükluyken internet kesilirse mevcut veri korunur ve
  non-blocking hata gorunur.
- [ ] `OFFLINE-004` Offline mutation (profil, tur, rezervasyon, review, wallet,
  chat) kalici basari gibi local state yazmaz.
- [ ] `OFFLINE-005` Internet geri gelince retry/refresh canonical backend
  sonucunu getirir.
- [ ] `OFFLINE-006` Ag gidip gelirken bekleyen REST/STOMP/FCM/polling donguleri
  sinirsiz istek veya ANR uretmez.

#### Process Recreation ve Uygulamanin Oldurulmesi

- [ ] `PROCESS-001` Liste/detail process recreation sonrasi repository refetch
  ile canonical state'i kurar.
- [ ] `PROCESS-002` Publish/edit/profile formunda korunmasi gereken draft
  `SavedStateHandle`/form state ile uygun olcude geri gelir.
- [ ] `PROCESS-003` Gecici dialog/snackbar/toast process recreation sonrasi
  anlamsiz tekrar gosterilmez.
- [ ] `PROCESS-004` Payment sirasinda process death backend status recovery ile
  devam eder.
- [ ] `PROCESS-005` Chat detail recreation ayni chatId ve history'yi tekrar
  kurar; duplicate pending mesaj uretmez.
- [ ] `PROCESS-006` Notification cold-start target yalniz bir kez navigate eder.
- [ ] `PROCESS-007` Rotation sirasinda aktif network istegi gereksiz duplicate
  mutation olusturmaz.

#### Hesap ve Role Izolasyonu

- [ ] `ISOLATION-001` Guide A logout -> Guide B login sonrasi A'nin profil/tur/
  wallet/banka/chat/bildirim verisi gorunmez.
- [ ] `ISOLATION-002` Tourist A logout -> Tourist B login sonrasi A'nin trips/
  wallet/payment/chat/bildirim verisi gorunmez.
- [ ] `ISOLATION-003` Guide -> Tourist hesap degisiminde role-specific cache,
  bottom bar ve navigation tamamen ayrilir.
- [ ] `ISOLATION-004` Pending form/payment/chat subscription owner degisince
  yeni hesaba tasinmaz.
- [ ] `ISOLATION-005` Current user ad/avatar/email yeni login sonrasinda
  canonical `/auth/me` ile yenilenir.
- [ ] `ISOLATION-006` Bir onceki hesabin local notification'i tiklanirsa yeni
  hesapta yetkisiz icerik acilmaz.

### 21. Yetki, Guvenlik ve Hassas Veri Kabul Testleri

#### Object Authorization

- [ ] `SECURITY-001` Guide B, Guide A tur detail/edit/open/close/cancel/archive
  endpoint'lerine ID tahminiyle erisemez.
- [ ] `SECURITY-002` Tourist B, Tourist A reservation/payment/wallet kaydina ID
  tahminiyle erisemez.
- [ ] `SECURITY-003` Chat katilimcisi olmayan kullanici history/send/read
  islemlerini yapamaz.
- [ ] `SECURITY-004` Tourist guide-only finance/tour management endpoint'lerine,
  guide tourist-only reservation/payment akislarina erisemez.
- [ ] `SECURITY-005` Normal kullanici admin tour review endpoint'lerini
  cagiramaz.
- [ ] `SECURITY-006` FCM/deep-link route ID'si backend yetkilendirmesini
  atlatamaz.

#### Secret, Storage ve Network

- [ ] `SECURITY-007` Git tracked dosyalarda iyzico/JWT/SMTP/Firebase/Google
  secret bulunmaz.
- [ ] `SECURITY-008` Access/refresh tokenlar Android Keystore anahtariyla
  sifrelenmis private session storage'da tutulur.
- [ ] `SECURITY-009` DataStore raw token/kart/CVV/IBAN/secret deposu olarak
  kullanilmaz.
- [ ] `SECURITY-010` Logcat ve OkHttp logunda Authorization header, refresh
  token, Google ID token veya provider token gorunmez.
- [ ] `SECURITY-011` Android model/state/DTO'larinda ham kart numarasi, SKT veya
  CVV bulunmaz.
- [ ] `SECURITY-012` Tam IBAN yalniz banka hesabini ekleme isteginde HTTPS ile
  backend'e gider; sonraki response/UI masked IBAN kullanir.
- [ ] `SECURITY-013` Debug local HTTP yalniz debug config/manifestte aciktir;
  release cleartext kapali kalir.
- [ ] `SECURITY-014` Source'a sabit LAN IP, Quick Tunnel veya callback URL
  yazilmaz.
- [ ] `SECURITY-015` FileProvider export edilmez ve baska uygulama private
  dosyalara genel erisim alamaz.
- [ ] `SECURITY-016` WebView SSL bypass, mixed content, arbitrary URL ve JS
  bridge bulunmaz.
- [ ] `SECURITY-017` Backend hata response'u teknik exception/SQL/path/secret
  sizdirmaz.
- [ ] `SECURITY-018` Screenshot/log kanitlarinda e-posta, tam IBAN, token ve
  kart metadata'si gerekenden fazla acik edilmez.

### 22. Iki Kullanicili LAN ve Ana Uctan Uca Senaryolar

#### Ana Guide-Tourist Akisi

- [ ] `E2E-001` Guide A profil/avatar/about/dillerini gunceller; Tourist A public
  profilde ayni canonical veriyi gorur.
- [ ] `E2E-002` Guide A cover ile tur yayinlar ve REVIEW sekmesinde gorur.
- [ ] `E2E-003` Admin araci turu approve eder; Guide A notification/ACTIVE state
  ve Tourist A discovery sonucunu gorur.
- [ ] `E2E-004` Tourist A turu home/search/filter ile bulur; kart/detail bilgisi
  Guide A kaydiyla aynidir.
- [ ] `E2E-005` Tourist A hosted Sandbox veya wallet ile turu satin alir.
- [ ] `E2E-006` Tourist A Trips/Reservation detail'de snapshot'i gorur; Guide A
  dashboard participant/earning projection'ini gorur.
- [ ] `E2E-007` Public session capacity/bookedCount satin alimdan sonra iki
  cihazda canonical olarak yenilenir.
- [ ] `E2E-008` Tourist A Guide A ile rezervasyondan bagimsiz/sonra sohbet eder;
  mesaj, unread ve notification iki cihazda guncellenir.
- [ ] `E2E-009` Tamamlanmis geziye Tourist A review verir; Guide A rating/
  comment notification'i, dashboard/profile/level guncellemesini gorur.
- [ ] `E2E-010` Guide A session'i veya Tourist A reservation'i iptal eder;
  refund, capacity, wallet/earning ve iki roldeki notification tutarli yenilenir.

#### Coklu Kullanici, Race ve Izolasyon

- [ ] `E2E-011` Tourist A ve Tourist B ayni son koltuga eszamanli istek atar;
  kapasite asilmaz.
- [ ] `E2E-012` Kaybeden turist anlamli `CAPACITY_NOT_AVAILABLE` sonucu gorur;
  payment/wallet kalintisi olusmaz.
- [ ] `E2E-013` Guide B, Guide A private tur/profile finance verisine erisemez.
- [ ] `E2E-014` Tourist B, Tourist A reservation/payment/chat private verisine
  erisemez.
- [ ] `E2E-015` Ayni guide-tourist cifti iki cihazdan sohbet baslatinca tek
  conversation olusur.
- [ ] `E2E-016` Bir cihaz Wi-Fi'dan cikinca diger cihaz calismaya devam eder;
  geri gelen cihaz REST resync ile canonical state'i toparlar.

#### Scheduler ve Gec Gelen Olaylar

- [ ] `E2E-017` Suresi dolan PENDING_PAYMENT reservation EXPIRED olur ve hold
  kapasitesi dogru serbest kalir.
- [ ] `E2E-018` Gec verified payment kapasite varsa reservation'i tek kez
  confirm eder.
- [ ] `E2E-019` Gec verified payment kapasite yoksa tek refund/manual review
  olusturur.
- [ ] `E2E-020` PENDING earning zamani gelince AVAILABLE olur ve guide wallet/
  notification yenilenir.
- [ ] `E2E-021` Yaklasan tur reminder'i iki kez uretilmez.
- [ ] `E2E-022` FCM retry sonunda delivery/history duplicate notification
  olusturmaz.
- [ ] `E2E-023` Orphan media cleanup yalniz sahipsiz dosyalari siler; aktif
  avatar/cover kaybolmaz.

### 23. Tasarim, Yerellesme ve Erisilebilirlik

Bu bolum yalniz ekranin acilmasini degil, kullanicinin gercek cihazda metni
okuyabilmesini, aksiyonu bulabilmesini ve farkli ekran kosullarinda akisi
tamamlayabilmesini dogrular. Yeni veya degisen ekranlar once kendi ozellik
senaryosuyla, sonra bu ortak matrisle birlikte test edilir.

#### Ortak Loading, Error, Empty ve Content Durumlari

- [ ] `VISUAL-001` Tam ekran ilk yuklemede yalniz ortak loading gorunumu
  kullanilir; indicator 36 dp, hareketli ve uygun kontrastli renktedir.
- [ ] `VISUAL-002` Acik zemindeki tam ekran loading indicator brand color,
  loading metni standart text color kullanir.
- [ ] `VISUAL-003` Brand renkli buton icindeki loading indicator brand color
  degil, butonla kontrast olusturan renktir.
- [ ] `VISUAL-004` Backend cok hizli cevap verirse kullanici yapay bir bekleme
  veya zorunlu minimum loading suresi gormez.
- [ ] `VISUAL-005` Tam ekran hata durumunda statik dairesel refresh oku ve
  `Tekrar dene` metni gorunur; spinner donmeye devam etmez.
- [ ] `VISUAL-006` Hata durumunda yalniz refresh ikonu ve metni tiklanabilir;
  tum ekran gorunmez buyuk bir tiklama alani gibi davranmaz.
- [ ] `VISUAL-007` Retry tiklaninca ikon loading indicator'a doner, tek istek
  atilir ve sonuc content/error olarak yenilenir.
- [ ] `VISUAL-008` Bos sonuc, ag hatasi gibi `Tekrar dene` aksiyonu gostermez;
  filtreyi temizleme veya geri donme gibi baglama uygun aksiyon sunar.
- [ ] `VISUAL-009` Pagination append loading'i tam ekrani kapatmaz; mevcut liste
  gorunur kalir.
- [ ] `VISUAL-010` Pagination append hatasi mevcut listeyi silmez ve retry ayni
  sayfayi tekrar ister.
- [ ] `VISUAL-011` Refresh sirasinda mevcut content gereksiz yere bos ekrana
  donmez.
- [ ] `VISUAL-012` Snackbar, toast, dialog ve field error ayni hatayi ayni anda
  tekrarlamaz; hata baglamina uygun tek kanal kullanilir.

#### Ekran Boyutu, Yon ve Klavye

- [ ] `VISUAL-013` Tum ana ekranlar telefon portrait modunda tasma, ust uste
  binme ve kesilme olmadan gorunur.
- [ ] `VISUAL-014` Tum scroll edilebilir formlar landscape modunda son alana ve
  ana butona kadar kaydirilabilir.
- [ ] `VISUAL-015` Dil, kategori, ulke, sehir, tarih, saat ve sure bottom
  sheet'lerinde landscape modunda `Tamam`/secim aksiyonu erisilebilirdir.
- [ ] `VISUAL-016` Kucuk ekran/yuksek navigation bar cihazinda alt buton sistem
  cubugunun altinda kalmaz.
- [ ] `VISUAL-017` Buyuk ekran veya tablet benzeri boyutta content anlamsiz
  sekilde uzamaz ya da sol koseye yigilmaz.
- [ ] `VISUAL-018` Klavye acildiginda aktif TextField ve hata metni gorunur
  kalir; ana aksiyona kaydirarak ulasilabilir.
- [ ] `VISUAL-019` IME Next/Done sirasi form alanlariyla uyumludur ve Done
  duplicate submit uretmez.
- [ ] `VISUAL-020` Sayisal alanlar uygun sayisal klavye, e-posta alani e-posta
  klavyesi, gizli alanlar sifre klavyesi acar.
- [ ] `VISUAL-021` 1.3x ve 1.5x font olceginde metinler kesilmez; kritik buton
  ve dialog aksiyonlari erisilebilir kalir.
- [ ] `VISUAL-022` Uzun tur basligi, rehber adi, banka adi, bildirim ve mesaj
  metni komsu ikon/fiyat/tarih alanlarini bozmaz.
- [ ] `VISUAL-023` Rotation state kaybi, duplicate navigation, duplicate dialog
  veya duplicate API mutation olusturmaz.

#### Renk, Tipografi, Ikon ve Etkilesim

- [ ] `VISUAL-024` Brand color yalniz vurgu/aktif secim/uygun loading alaninda
  kullanilir; text ve zemin kontrasti korunur.
- [ ] `VISUAL-025` Radio button, checkbox ve secili durum indikatorleri karar
  verilen brand color ile tutarlidir.
- [ ] `VISUAL-026` Basari, bekleme, hata ve iptal durumlari yalniz renkle degil
  ikon/metinle de ayirt edilir.
- [ ] `VISUAL-027` Disabled buton secilebilir gibi gorunmez; enabled oldugunda
  renk ve dokunma davranisi birlikte degisir.
- [ ] `VISUAL-028` Tiklanabilir ikon ve satirlar en az 48 dp mantiksal dokunma
  alanina sahiptir; gorunur ikon gereksiz buyutulmez.
- [ ] `VISUAL-029` Top bar geri, bildirim, favori, filtre, FAB ve overflow
  ikonlarinin contentDescription degeri anlamlidir.
- [ ] `VISUAL-030` Dekoratif avatar/kapak ikonlari TalkBack'te gereksiz tekrar
  okunmaz.
- [ ] `VISUAL-031` Loading indicator ve progress bar TalkBack kullanicisina
  anlamsiz yuzde veya tekrarli metin okumaz.
- [ ] `VISUAL-032` Focus sirasi gorsel sirayla uyumludur; bottom sheet ve dialog
  acikken focus arkadaki ekrana kacmaz.
- [ ] `VISUAL-033` Dialog baslik/aciklama bosluklari ve yazi boyutlari para
  cekme onay/sonuc ekranlarinda karar verilen ortak gorunumdedir.
- [ ] `VISUAL-034` Snackbar bottom barin hemen ustunde, belirlenen 24 dp duzenle
  gorunur ve bottom bar tarafindan kapanmaz.

#### Yerellesme ve Formatlama

- [ ] `LOCALE-001` Kullaniciya gorunen statik Android metinleri string resource
  uzerinden gelir; kod icinde yeni hardcoded UI metni bulunmaz.
- [ ] `LOCALE-002` Backend yerellestirilmis `kazanc`, `para cekme` veya hata
  cumlesi gondermez; Android type/code degerini kendi resource'u ile gosterir.
- [ ] `LOCALE-003` Backend'den gelen tur adi, rehber adi, cancellation reason,
  comment ve mesaj kullanici verisi olarak aynen korunur.
- [ ] `LOCALE-004` Secili uygulama/cihaz dili iyzico hosted istegine desteklenen
  locale olarak iletilir; desteklenmiyorsa guvenli varsayilan kullanilir.
- [ ] `LOCALE-005` iyzico hosted kart alanlari Android stringleriyle yeniden
  yazilmaz; provider kendi locale'ine gore gosterir.
- [ ] `LOCALE-006` Ulke aramasi cihaz locale'ine uygun adlari bulur; `Turkey`
  yerine yalniz `Turkiye` bulunmasi gibi provider veri davranislari coklu dil
  destegi diye yanlis yorumlanmaz.
- [ ] `LOCALE-007` Dil seciminde locale tarafindan uretilen temsil bayraklari,
  dil adlari ve secimler uygulama yeniden acildiginda tutarlidir.
- [ ] `LOCALE-008` USD platform para degerleri `$1,500.00` gibi sabit platform
  para birimiyle, Long minor unit uzerinden yuvarlama hatasi olmadan gosterilir.
- [ ] `LOCALE-009` Provider odeme para birimi USD/EUR/TRY secimine gore ayri
  gosterilir; kullanici platform fiyati ile provider charge tutarini karistirmaz.
- [ ] `LOCALE-010` Tarih/saat ve goreli zamanlar cihaz locale'ine uygun okunur
  bicimdedir; backend Instant degeri kaybolmaz.
- [ ] `LOCALE-011` Telefon dili veya bolgesi degisince uygulama crash olmaz;
  para/tarih/ulke/dil alanlari desteklenen fallback ile acilir.
- [ ] `LOCALE-012` Cok uzun ceviri senaryosunda buton, tab, chip, bottom sheet
  ve dialog metinleri kesilmeden okunabilir.

#### Ozellik Bazli Gorsel Kontrol Envanteri

- [ ] `DESIGN-001` Auth: onboarding, sign in/up, forgot/reset/change password,
  role selection, validation ve sonuc dialoglari mevcut GuideMate diliyle
  uyumludur.
- [ ] `DESIGN-002` Ortak seciciler: ulke/sehir/dil/kategori/tarih/saat/sure
  portrait ve landscape modunda ayni tasarim dilini korur.
- [ ] `DESIGN-003` Rehber: home sayaçlari, tour publish/edit/detail/list,
  profile/level/about, wallet/earnings/bank/withdrawal ekranlari tutarlidir.
- [ ] `DESIGN-004` Turist: home, explore/filter/result, public guide profile,
  tour detail, trips/reservation/review ve account ekranlari tutarlidir.
- [ ] `DESIGN-005` Odeme: currency secimi, checkout, hosted WebView loading/
  failure, payment status, recovery, wallet top-up ve saved method listesi
  tasarim olarak kopuk bir uygulama hissi vermez.
- [ ] `DESIGN-006` Chat: conversation list, unread, message bubble, pending/
  failed/retry, pagination ve keyboard durumlari tutarlidir.
- [ ] `DESIGN-007` Notification: app history, unread badge, kategori ikon/renk,
  system tray title/body/icon ve semantic hedefler ayni anlam dilini kullanir.
- [ ] `DESIGN-008` Remote gorsel loading/error fallback, local preview ve gercek
  imageUrl sonucu kart/detail/profile boyunca ayni kirpma politikasini korur.
- [ ] `DESIGN-009` Empty/error/loading durumlari farkli feature'larda ayni ortak
  component dilini kullanir fakat baglama ozel mesaj/aksiyonunu korur.
- [ ] `DESIGN-010` Uygulama ikonu gecici sistem/default ikon olarak bilincli
  kabul edilir; final ikon geldikten sonra launcher ve system notification
  small icon yeniden gorsel kontrolden gecirilir.

### 24. Dis Servis, Backend ve Altyapi Hata Matrisi

#### LAN, Backend ve PostgreSQL

- [ ] `INFRA-001` Mac, emulator ve fiziksel cihaz ayni Wi-Fi'dayken Android
  guncel LAN base URL ile backend health/OpenAPI/API'ye erisir.
- [ ] `INFRA-002` Mac'in LAN IP'si degistiginde yalniz Git disi Android ve
  backend local ayarlari guncellenir; production source degismez.
- [ ] `INFRA-003` Backend kapaliyken uygulama crash/sonsuz spinner yerine ortak
  baglanti hatasi ve retry gosterir.
- [ ] `INFRA-004` Backend uygulama acikken yeniden baslatilir; tokenli ekranlar
  tekrar baglanir ve canonical veriyi yeniler.
- [ ] `INFRA-005` PostgreSQL baglantisi yokken teknik JDBC/SQL mesaji UI veya
  response'a sizmaz.
- [ ] `INFRA-006` Yanlis base URL/DNS/timeout/connection reset durumlari ayri
  teknik exception yerine kullanici dostu network hatasina donusur.
- [ ] `INFRA-007` Bozuk veya beklenmeyen JSON response parser crash'i yerine
  guvenli generic hata gosterir ve onceki content'i korur.
- [ ] `INFRA-008` 401, 403, 404, 409, 422, 429 ve 500 response'lari merkezi
  typed hata sinirinda ayrilir; `Bilinmeyen hata` gereksiz yere kullanilmaz.
- [ ] `INFRA-009` Validation `fieldErrors` alani yok, null veya dolu geldiginde
  ApiErrorResponse guvenli parse edilir.
- [ ] `INFRA-010` Rate limit response'unda kullanici tekrar tekrar istek atmaya
  yonlendirilmez; uygun bekleme/mesaj davranisi gorur.

#### SMTP ve Auth Web Linkleri

- [ ] `EXTERNAL-001` SMTP gercek local secret ile calisir; register, resend ve
  forgot-password e-postalari dogru aliciya tek kez gelir.
- [ ] `EXTERNAL-002` SMTP kullanilamazsa hesap/token state yarim basari gibi
  kalmaz ve kullanici e-postanin gonderilemedigini anlar.
- [ ] `EXTERNAL-003` E-posta confirm/reset linki PUBLIC_BASE_URL ile fiziksel
  telefondan acilir; localhost linki uretilmez.
- [ ] `EXTERNAL-004` Confirm/reset web sonucu basari, suresi dolmus, kullanilmis
  ve gecersiz token durumlarini teknik detay vermeden ayirt eder.

#### Google ve Places

- [ ] `EXTERNAL-005` Google Web Client ID Android/backend tarafinda aynidir;
  ACTIVE hesap login olabilir.
- [ ] `EXTERNAL-006` Google servis/credential iptali genel crash yerine iptal
  veya kullanici dostu hata sonucu verir.
- [ ] `EXTERNAL-007` Places API key kisitlari dogru package/SHA ile fiziksel
  cihaz ve emulatorde sehir aramasina izin verir.
- [ ] `EXTERNAL-008` Places quota, permission 9011, ag veya billing hatasi
  kontrollu sehir yuklenemedi sonucu verir; gizli accessibility logu yanlis
  neden olarak yorumlanmaz.
- [ ] `EXTERNAL-009` API key build'de yoksa sehir secimine dokunmak uygulamayi
  cokkertmez ve guvenli yapilandirma hatasi verir.

#### Medya ve Remote Gorseller

- [ ] `EXTERNAL-010` PUBLIC_BASE_URL ile uretilen media URL Mac LAN IP'siyle
  Android API base URL'e uyumludur ve gercek HTTP 200 image content doner.
- [ ] `EXTERNAL-011` Media GET 404/403/timeout durumunda Coil/GuideMateImage
  loading veya drawable fallback gosterir; layout bozulmaz.
- [ ] `EXTERNAL-012` Upload yarida kesilirse sahiplenilmemis kayit profil/tura
  baglanmaz; kullanici tekrar deneyebilir.
- [ ] `EXTERNAL-013` Buyuk/uygunsuz content type dosya backend tarafindan
  reddedilir ve Android anlamli hata gosterir.

#### Doviz ve iyzico Sandbox

- [ ] `EXTERNAL-014` Doviz saglayicisi calisirken USD platform tutari secilen
  TRY/EUR provider tutarina backend quote ile dogru cevrilir.
- [ ] `EXTERNAL-015` Doviz saglayicisi kullanilamazsa stale/uydurma kurla
  odemeye gecilmez; kullanici quote alinamadigini gorur.
- [ ] `EXTERNAL-016` Quote suresi dolarsa Android eski tutarla checkout
  baslatmaz, yeni quote ister.
- [ ] `EXTERNAL-017` iyzico Sandbox API key/secret/callback ayarlari gecerlidir;
  hosted checkout acilir.
- [ ] `EXTERNAL-018` Quick Tunnel kapali veya callback URL gecersizse Android
  callback JSON'unu basari saymaz; polling gercek backend sonucunu bekler.
- [ ] `EXTERNAL-019` iyzico timeout/decline/3D failure/cancel durumlari ayri
  payment status olarak gorunur.
- [ ] `EXTERNAL-020` Provider ayni callback/webhook'u tekrar gonderirse tek
  payment/reservation/ledger/refund sonucu olusur.
- [ ] `EXTERNAL-021` Provider basarisi hold suresinden sonra gelirse kapasite
  asilmaz; refund veya MANUAL_REVIEW kullaniciya gorunur.

#### FCM ve STOMP

- [ ] `EXTERNAL-022` Firebase yapilandirmasi ve FCM token alma emulator/
  fiziksel cihazda calisir; token backend'e kaydolur.
- [ ] `EXTERNAL-023` FCM gecici kullanilamazsa notification history kaybi
  olmaz; app acilinca REST ile okunur.
- [ ] `EXTERNAL-024` Eski/gecersiz FCM registration backend retry politikasini
  bozmadan temizlenir.
- [ ] `EXTERNAL-025` STOMP handshake gecerli access token ile basarili olur;
  baska kullanicinin topic'ine abone olunamaz.
- [ ] `EXTERNAL-026` STOMP baglantisi kopunca kontrollu reconnect yapar;
  duplicate mesaj/unread uretmez.
- [ ] `EXTERNAL-027` STOMP mesaji kacirilirsa conversation REST resync ile
  eksik mesaji tamamlar.
- [ ] `EXTERNAL-028` Logout sonrasi STOMP subscription ve FCM user baglantisi
  eski hesap adina veri almaya devam etmez.

### 25. Android-Backend API Sozlesme Izlenebilirligi

Bu bolum bir endpoint smoke envanteridir. Her satir en az bir basarili gercek
istek, dogru Android DTO/mapper/UI sonucu ve ilgili ozellik bolumundeki hata/
yetki senaryosuyla birlikte kanitlanir. Admin ve provider callback endpoint'leri
Android ekrani degil, kabul akisinin hazirlik/sonuc adimi olarak test edilir.

#### Auth ve Oturum

- [ ] `API-AUTH-001` `POST /api/v1/auth/register` Sign Up request/response ile
  uyumludur.
- [ ] `API-AUTH-002` `POST /api/v1/auth/login` token, user ve role state'ini
  dogru kurar.
- [ ] `API-AUTH-003` `POST /api/v1/auth/google` mevcut Google hesabi akisini
  dogru kurar.
- [ ] `API-AUTH-004` `POST /api/v1/auth/refresh-token` rotation ve retry
  sinirinda tek kez kullanilir.
- [ ] `API-AUTH-005` `POST /api/v1/auth/logout` local/remote session'i guvenli
  temizler.
- [ ] `API-AUTH-006` `POST /api/v1/auth/select-role` ilk ve tek rol secimini
  kalici hale getirir.
- [ ] `API-AUTH-007` `GET /api/v1/auth/me` userId, email, firstName, lastName,
  role ve avatar state'inin canonical kaynagidir.
- [ ] `API-AUTH-008` `POST /api/v1/auth/change-password` basari sonrasi tum
  session'lari gecersizlestirir.
- [ ] `API-AUTH-009` `POST /api/v1/auth/resend-verification` pending hesap ve
  rate limit davranisiyla uyumludur.
- [ ] `API-AUTH-010` `POST /api/v1/auth/forgot-password` hesap varligini
  sizdirmayan UX ile uyumludur.
- [ ] `API-AUTH-011` `POST /api/v1/auth/reset-password` tek kullanimlik token
  ve 8 haneli sayisal sifre kuralini uygular.
- [ ] `API-AUTH-012` `GET /api/v1/auth/confirm` mobil tarayici e-posta
  dogrulamasini sonuclandirir.
- [ ] `API-AUTH-013` `GET /api/v1/auth/reset-password-form` mobil tarayicida
  guvenli reset formunu acar.

#### Medya ve Profil

- [ ] `API-MEDIA-001` `POST /api/v1/media` multipart avatar/cover upload eder
  ve mediaAssetId/imageUrl doner.
- [ ] `API-MEDIA-002` `GET /api/v1/media/{mediaId}/content` Android remote image
  yuklemesini destekleyen dogru content type doner.
- [ ] `API-MEDIA-003` `DELETE /api/v1/media/{mediaId}` yalniz sahibinin uygun
  sahipsiz medyasini siler.
- [ ] `API-PROFILE-001` `PUT /api/v1/users/me/avatar` ortak kullanici avatarini
  gunceller.
- [ ] `API-PROFILE-002` `GET /api/v1/guides/me/profile` rehber profil edit
  formunun canonical kaynagidir.
- [ ] `API-PROFILE-003` `PATCH /api/v1/guides/me/profile` about/language/profile
  alanlarini gunceller.
- [ ] `API-PROFILE-004` `GET /api/v1/guides/{guideId}/public-profile` turist
  public profilini ve performans ozetini besler.
- [ ] `API-PROFILE-005` `GET /api/v1/guides/search` rehber arama/filtre/page
  sonucunu besler.
- [ ] `API-PROFILE-006` `GET /api/v1/guides/top` top guide kartlarini canonical
  siralamayla besler.

#### Tur, Kesif ve Admin Inceleme

- [ ] `API-TOUR-001` `POST /api/v1/guide/tours` draft/media/category/location
  alanlariyla tur olusturur.
- [ ] `API-TOUR-002` `GET /api/v1/guide/tours` rehberin yalniz kendi paged tur
  kartlarini ve sekme durumlarini getirir.
- [ ] `API-TOUR-003` `GET /api/v1/guide/tours/{tourId}` edit/detail canonical
  verisini getirir.
- [ ] `API-TOUR-004` `POST /api/v1/guide/tours/{tourId}/change-requests`
  onayli tur degisikligini incelemeye yollar.
- [ ] `API-TOUR-005` `POST /api/v1/guide/tours/{tourId}/sessions` yeni session
  olusturur.
- [ ] `API-TOUR-006` `PATCH /api/v1/guide/sessions/{sessionId}` izinli session
  alanlarini gunceller.
- [ ] `API-TOUR-007` `POST /api/v1/guide/sessions/{sessionId}/open` lifecycle
  kuralina gore satisi acar.
- [ ] `API-TOUR-008` `POST /api/v1/guide/sessions/{sessionId}/close` mevcut
  rezervasyonlari bozmadan yeni satisi kapatir.
- [ ] `API-TOUR-009` `POST /api/v1/guide/sessions/{sessionId}/cancel` neden,
  refund ve notification akisini baslatir.
- [ ] `API-TOUR-010` `POST /api/v1/guide/tours/{tourId}/archive` yalniz uygun
  turu arsivler.
- [ ] `API-TOUR-011` `GET /api/v1/guides/me/dashboard` page boyundan bagimsiz
  guide sayaç/puan/participant projection'i doner.
- [ ] `API-TOUR-012` `GET /api/v1/tours/search` turist filtre ve pagination
  sonucunu doner.
- [ ] `API-TOUR-013` `GET /api/v1/tours/popular` canonical popular kartlarini
  doner.
- [ ] `API-TOUR-014` `GET /api/v1/tours/{tourId}` public tur detayini doner.
- [ ] `API-TOUR-015` `GET /api/v1/tour-sessions/{sessionId}` guncel capacity,
  bookedCount ve booking state'i doner.
- [ ] `API-ADMIN-001` `GET /api/v1/admin/tour-reviews` admin aracinda inceleme
  kuyrugunu getirir.
- [ ] `API-ADMIN-002` `GET /api/v1/admin/tour-reviews/{reviewId}` admin detayini
  getirir.
- [ ] `API-ADMIN-003` `POST /api/v1/admin/tour-reviews/{reviewId}/approve`
  publish/change-request onay akisini tetikler.
- [ ] `API-ADMIN-004` `POST /api/v1/admin/tour-reviews/{reviewId}/reject`
  anlamli red nedeni ve notification uretir.

#### Rezervasyon ve Review

- [ ] `API-RES-001` `GET /api/v1/reservations/me` upcoming/past/cancelled ve
  pagination sonucunu doner.
- [ ] `API-RES-002` `GET /api/v1/reservations/{reservationId}` satin alma
  snapshot'ini ve guncel lifecycle durumunu doner.
- [ ] `API-RES-003` `POST /api/v1/reservations/{reservationId}/cancel` refund,
  capacity ve notification akisini baslatir.
- [ ] `API-REVIEW-001` `POST /api/v1/reservations/{reservationId}/reviews`
  yalniz uygun tamamlanmis rezervasyona tek review olusturur.
- [ ] `API-REVIEW-002` `GET /api/v1/tours/{tourId}/reviews` public paged yorum
  listesini ve rating ozetini besler.

#### Payment, Saved Card, Wallet ve Guide Finance

- [ ] `API-PAY-001` `GET /api/v1/payments/checkout/currencies` desteklenen
  provider para birimlerini doner.
- [ ] `API-PAY-002` `POST /api/v1/payments/checkout/tour/quote` authoritative
  katilimci/tutar/kur quote'u doner.
- [ ] `API-PAY-003` `POST /api/v1/payments/checkout/wallet-top-up/quote`
  authoritative top-up quote'u doner.
- [ ] `API-PAY-004` `POST /api/v1/payments/checkout/tour` WALLET veya HOSTED_CARD
  niyetini idempotent baslatir.
- [ ] `API-PAY-005` `POST /api/v1/payments/checkout/wallet-top-up` hosted top-up
  istegini idempotent baslatir.
- [ ] `API-PAY-006` `GET /api/v1/payments/{paymentId}` Android'in tek gercek
  payment/reservation/refund sonuc kaynagidir.
- [ ] `API-PAY-007` `POST /api/v1/payments/{paymentId}/cancel` yalniz iptal
  edilebilir pending islemi sonlandirir.
- [ ] `API-PAY-008` `POST /api/v1/payments/iyzico/callback` provider token'ini
  verify eder; callback JSON Android basarisi sayilmaz.
- [ ] `API-PAY-009` `POST /api/v1/payments/iyzico/webhook` imza, tekrar ve gec
  sonuc davranislarini idempotent uygular.
- [ ] `API-CARD-001` `GET /api/v1/payment-methods/cards` yalniz provider-backed
  masked kart metadata'sini listeler.
- [ ] `API-CARD-002` `DELETE /api/v1/payment-methods/cards/{id}` secili karti
  provider/backend kurallariyla siler.
- [ ] `API-CARD-003` `PUT /api/v1/payment-methods/cards/{id}/default` default
  karti canonical olarak degistirir.
- [ ] `API-WALLET-001` `GET /api/v1/wallet` owner/currency/available balance
  projection'ini doner.
- [ ] `API-WALLET-002` `GET /api/v1/wallet/transactions` paged ledger
  hareketlerini referenceTitle ile doner.
- [ ] `API-FINANCE-001` `GET /api/v1/guide/earnings` paged earning detayini
  doner.
- [ ] `API-FINANCE-002` `GET /api/v1/guide/earnings/monthly` aylik projection'i
  Android'in liste sayfalarini toplamasina gerek birakmadan doner.
- [ ] `API-FINANCE-003` `GET /api/v1/guide/bank-accounts` masked IBAN ve default
  hesabi doner.
- [ ] `API-FINANCE-004` `POST /api/v1/guide/bank-accounts` tam IBAN'i bir kez
  dogrulayip bankAccountId olusturur.
- [ ] `API-FINANCE-005` `POST /api/v1/guide/bank-accounts/{id}/default` default
  banka hesabini degistirir.
- [ ] `API-FINANCE-006` `DELETE /api/v1/guide/bank-accounts/{id}` uygun hesabi
  siler ve default invariant'ini korur.
- [ ] `API-FINANCE-007` `GET /api/v1/guide/withdrawals` paged para cekme
  gecmisini getirir.
- [ ] `API-FINANCE-008` `POST /api/v1/guide/withdrawals` available bakiyeyi
  rezerve ederek idempotent talep olusturur.

#### Chat, Notification ve Cihaz Kaydi

- [ ] `API-CHAT-001` `POST /api/v1/chats/with-user/{remoteUserId}` rezervasyona
  gerek olmadan tek conversation bulur veya olusturur.
- [ ] `API-CHAT-002` `GET /api/v1/chats` current user conversation listesini
  unread/last message ile doner.
- [ ] `API-CHAT-003` `GET /api/v1/chats/{chatId}/messages` cursor pagination ile
  history getirir.
- [ ] `API-CHAT-004` `POST /api/v1/chats/{chatId}/messages` clientMessageId ile
  idempotent mesaj gonderir.
- [ ] `API-CHAT-005` `POST /api/v1/chats/{chatId}/read` read pointer/unread
  degerini gunceller.
- [ ] `API-CHAT-006` `GET /api/v1/chats/unread-count` top-level badge
  projection'ini doner.
- [ ] `API-DEVICE-001` `POST /api/v1/devices/fcm-registration` installationId,
  token ve platform kaydini gunceller.
- [ ] `API-DEVICE-002` `DELETE /api/v1/devices/fcm-registration/{installationId}`
  logout/izin kapatma durumunda user-device bagini kaldirir.
- [ ] `API-NOTIFY-001` `GET /api/v1/notifications` paged history ve semantic
  hedef ID'lerini doner.
- [ ] `API-NOTIFY-002` `GET /api/v1/notifications/unread-count` app badge'i
  besler.
- [ ] `API-NOTIFY-003` `POST /api/v1/notifications/{id}/read` tek bildirimi
  okundu yapar.
- [ ] `API-NOTIFY-004` `POST /api/v1/notifications/read-all` tum bildirimleri
  okundu yapar.
- [ ] `API-NOTIFY-005` `GET /api/v1/notifications/preferences` role/current
  user preference formunu getirir.
- [ ] `API-NOTIFY-006` `PATCH /api/v1/notifications/preferences` Android'deki
  boolean sozlesmeyle tercihleri kalici gunceller.

### 26. Gercekci Demo Veri ve Gorunur Veri Tutarliligi

Demo kabul turu yalniz `guidemate_demo` veritabaninda yapilir. Normal
`guidemate_db`, normal medya koku ve gercek kullanici verileri demo hazirligi
icin degistirilmez. Test parolasi yalniz Git disi demo secret dosyasindan
alinir; bu belgeye yazilmaz.

#### Dataset Guvenligi ve Butunlugu

- [ ] `DEMO-001` Aktif datasource veritabani adi testten once acikca
  `guidemate_demo` olarak dogrulanir.
- [ ] `DEMO-002` `guidemate_db` uzerinde seed/reset/delete komutu calistirilmaz.
- [ ] `DEMO-003` Demo seed normal uygulama baslangicinda tekrar calismaz;
  `DEMO_DATASET_ENABLED=false` ile veri korunur.
- [ ] `DEMO-004` Dataset 306 demo-domain user, 480 media, 180 tour, 560
  session, 1300 reservation, 420 review, 1435 payment, 2370 ledger, 180 chat,
  2400 message ve 1200 notification kaydini beklenen sayida tasir.
- [ ] `DEMO-005` Demo medya URL'leri guncel PUBLIC_BASE_URL ile gercek JPEG
  content doner; Android kart/profile/detail'de fallback yerine gorsel gosterir.
- [ ] `DEMO-006` Demo hesap, medya ve finans kayitlari normal test/production
  profile'larinda kendiliginden olusmaz.
- [ ] `DEMO-007` Demo verisi silinecekse yalniz demo DB, demo media root ve
  demo kodu ayri acik onayla temizlenir.

#### Temsili Turist Hesaplari

- [ ] `DEMO-008` `tourist001@demo.guidemate.test` bos state turistidir; avatar
  disinda reservation/chat/notification gecmisi yoktur.
- [ ] `DEMO-009` `tourist028@demo.guidemate.test` 60 mesajli conversation ile
  cursor pagination, unread ve scroll davranisini gosterir.
- [ ] `DEMO-010` `tourist061@demo.guidemate.test` upcoming ve pending payment
  dahil reservation lifecycle ekranlarini gosterir.
- [ ] `DEMO-011` `tourist196@demo.guidemate.test` cancelled ve expired
  reservation durumlarini birbirinden ayirir.
- [ ] `DEMO-012` `tourist233@demo.guidemate.test` 21 past trip, 12.500 minor
  unit wallet ve 19 notification ile liste/pagination gorunumunu doldurur.

#### Temsili Rehber Hesaplari

- [ ] `DEMO-013` `guide001@demo.guidemate.test` tur, earning ve banka hesabi
  olmayan yeni rehber empty state'lerini gosterir.
- [ ] `DEMO-014` `guide006@demo.guidemate.test` pending tour/change request,
  earning ve iki banka hesabi akislarini gosterir.
- [ ] `DEMO-015` `guide016@demo.guidemate.test` APPROVED seviye siniri ve
  review'lu tamamlanmis turlari gosterir.
- [ ] `DEMO-016` `guide039@demo.guidemate.test` SILVER seviyesini canonical
  backend ozetiyle gosterir.
- [ ] `DEMO-017` `guide046@demo.guidemate.test` SUPER seviyesini canonical
  backend ozetiyle gosterir.
- [ ] `DEMO-018` `guide050@demo.guidemate.test` LEGENDARY, 100 completed
  session, 50 review ve monthly earning ile yogun profil/dashboard'u gosterir.

#### Auth ve Admin Sinir Hesaplari

- [ ] `DEMO-019` `pending.valid@demo.guidemate.test` gecerli confirmation
  tokenli pending hesap akisini gosterir.
- [ ] `DEMO-020` `pending.expired@demo.guidemate.test` expired confirmation ve
  resend akisini gosterir.
- [ ] `DEMO-021` `role.selection@demo.guidemate.test` ACTIVE fakat role
  secilmemis root akisini gosterir.
- [ ] `DEMO-022` `disabled.tourist@demo.guidemate.test` disabled account login
  hatasini gosterir.
- [ ] `DEMO-023` `disabled.guide@demo.guidemate.test` disabled account login
  hatasini gosterir.
- [ ] `DEMO-024` `admin@demo.guidemate.test` yalniz admin API/araci icin
  kullanilir; Android role seciminde ADMIN secenegi gorunmez.

#### Liste ve Projection Tutarliligi

- [ ] `DEMO-025` Home popular tur, search sonucu ve detail ayni tour/session
  kimliginde baslik, fiyat, capacity, bookedCount ve cover bilgisini korur.
- [ ] `DEMO-026` Guide own tour karti, detail, dashboard ve turist public karti
  ayni canonical kayittan role uygun projection gosterir.
- [ ] `DEMO-027` Review sayisi/ortalama puan tur karti, detail, public guide
  profil, guide dashboard ve level ozetinde backend kurallarina uygun tutarlidir.
- [ ] `DEMO-028` Wallet bakiyesi ledger toplamiyla, monthly earning projection'i
  REVERSED haric earning kayitlariyla uyumludur.
- [ ] `DEMO-029` Notification actorDisplayName ve wallet referenceTitle ek user/
  tour sorgusu olmadan dogru gorunur.
- [ ] `DEMO-030` Empty, az veri, tek sayfa ve cok sayfa hesaplarinda UI ayni
  componentleri dogru state ile kullanir.
- [ ] `DEMO-031` Baslamamis CONFIRMED demo rezervasyonu eski politika kodu
  kaynakli `DATA_CONFLICT` vermeden iptal edilir; 48 saat sinirina gore
  FULL_REFUND veya NO_REFUND sonucu dogru doner.

### 27. Bilincli Kapsam Sinirlari ve Yanlis Hata Kaydi Acmayacak Kararlar

Asagidaki maddeler unutulmus is degildir. Test sirasinda bunlar mevcut karar
siniri icinde degerlendirilir; karar disina cikan gercek bir davranis varsa hata
acilir.

- `SCOPE-001` Android uygulamasinda ADMIN rolu veya admin inceleme ekrani yoktur.
  Admin approval/rejection backend araci/API ile hazirlanir; guide/tourist sonucu
  Android'de dogrulanir.
- `SCOPE-002` Uygulama tek modullu feature-first yapidadir. Sirf portfoy icin
  multi-module'a gecmek bu kabul turunun parcasi degildir.
- `SCOPE-003` Backend bitene kadar kullanilan MVP store/mock kaynaklari gercek
  repository baglantilariyla kaldirilmistir. Demo veri Android mock'u degil,
  ayri backend demo veritabanidir.
- `SCOPE-004` Standalone native kart ekleme formu yoktur. Kart yalniz gercek
  hosted iyzico purchase/top-up akisi icindeki provider secenegiyle kaydedilir.
  Saved Cards FAB'inin kaldirilmis olmasi dogru davranistir.
- `SCOPE-005` Android ham kart numarasi, SKT ve CVV toplamaz/saklamaz. Provider
  ekraninin gorunmesi native kart formu eksigi sayilmaz.
- `SCOPE-006` SandboxCardCatalog kaldirilmistir. Kart bankasi/markasi/son dort
  hane provider metadata'sindan gelir; Android kart numarasindan tahmin etmez.
- `SCOPE-007` TurkishBankCatalog yalniz IBAN yazarken hizli on gosterim icin
  kalabilir; kesin IBAN/banka sonucu backend otoritesidir.
- `SCOPE-008` Gercek banka transferi ve KYC/alt uye isyeri onboarding bu local
  portfoy kapsaminda degildir. Withdrawal backend ledger/rezervasyon ve
  `SIMULATED` provider sonucuyla gercekci lifecycle'i korur.
- `SCOPE-009` Uygulama platform fiyatlarini USD minor unit ile tutar. Provider
  checkout icin backend quote ile desteklenen USD/EUR/TRY secilebilir; Android
  kur carpimi hesaplamaz.
- `SCOPE-010` Tur zamanlari backend Instant olarak korunur; farkli sehir icin
  Android'de kapsamli timeZoneId resolver eklemek bilincli olarak yapilmamistir.
  Cihaz locale/tarih gorunumu test edilir, kitalar arasi yayin politikasi test
  zorunlulugu degildir.
- `SCOPE-011` Sohbet rezervasyon satin alma kosuluna bagli degildir. Turist public
  rehber profiline ulasip satin almadan mesaj baslatabilir.
- `SCOPE-012` `Yaziyor...`, online presence ve read receipt'in mesaj-bazli
  ayrintili gosterimi su an kapsamda degildir. Mesaj delivery, retry, unread ve
  read endpoint davranisi kapsam dahilindedir.
- `SCOPE-013` FCM gercek zamanli chat transport'u degildir; foreground chat
  STOMP, history/resync REST, arka plan bildirimi FCM ile test edilir.
- `SCOPE-014` Hosted checkout callback sayfasi JSON donebilir. Android sayfa
  metnini basari saymaz; backend payment status polling otoritedir.
- `SCOPE-015` Uygulama public cloud deployment kullanmaz. Ayni Wi-Fi LAN ve
  gecici Quick Tunnel local/Sandbox E2E icin yeterlidir.
- `SCOPE-016` Quick Tunnel adresi kalici degildir; her odeme E2E turu oncesinde
  yenilenmesi hata degil ortam hazirligidir.
- `SCOPE-017` Tam coklu Android UI dili henuz eklenmemistir. Statik metinlerin
  resource'tan gelmesi ve hosted provider locale/fallback davranisi test edilir;
  olmayan ceviri paketi varmis gibi kabul edilmez.
- `SCOPE-018` Launcher/system notification ikonu final tasarim asset'i gelene
  kadar gecici/default olabilir. Bildirim category iconu ve semantic davranis
  yine test edilir.
- `SCOPE-019` Her composable icin ayri UI testi yazmak zorunlu degildir. Bu
  belge manuel kabul turudur; unit/instrumentation test envanteri ayri kalite
  kapisinda risk bazli tutulur.
- `SCOPE-020` Yardim/destek/yasal metinlerde gosterilen tiklanabilir aksiyon bos
  callback olamaz. Islev planlanmadiysa aksiyon tiklanabilir gorunmemeli;
  gorunuyorsa uygun sistem intent'i veya acik bilgilendirme vermelidir.

### 28. Faz Kapanis Izlenebilirligi

Faz 0 ve Faz 1 kod/otomatik kalite kapilariyla tamamlanmistir. Faz 2-12'nin
`KISMEN TAMAMLANDI` durumu, burada listelenen cihaz/Sandbox/iki kullanici
kanitlari gecince `TAMAMLANDI` olarak guncellenir. Bir sonraki faz testi onceki
fazin eksigini de tamamliyorsa her iki fazin kaydi birlikte guncellenir.

- [ ] `PHASE-000` Faz 0 feature-first refactor sonrasi root/auth/guide/tourist
  navigation, tum temel ekranlar ve rol gecisleri `ROOT`, `NAV`, `ISOLATION` ve
  `VISUAL` senaryolariyla davranis-koruyucu dogrulanir.
- [ ] `PHASE-001` Faz 1 ortak teknik temel/auth; `AUTH`, `COMMON`, `INFRA`,
  `API-AUTH` ve LAN/email/Google senaryolariyla kapanir.
- [ ] `PHASE-002` Faz 2 medya; `MEDIA`, `API-MEDIA`, remote image ve orphan
  cleanup senaryolariyla kapanir.
- [ ] `PHASE-003` Faz 3 guide/public profile; own/public profil, search/top,
  avatar, performance/level ve iki kullanicili projection senaryolariyla kapanir.
- [ ] `PHASE-004` Faz 4 tour/guide management; publish, approval, edit,
  lifecycle, dashboard, media ve object authorization senaryolariyla kapanir.
- [ ] `PHASE-005` Faz 5 tourist discovery; home, search/filter/pagination,
  public detail, review read ve canonical kart/detail senaryolariyla kapanir.
- [ ] `PHASE-006` Faz 6 reservation/trips; checkout oncesi availability,
  snapshot, upcoming/past/cancel/refund ve Faz 9 payment sonucuyla kapanir.
- [ ] `PHASE-007` Faz 7 review; eligibility, duplicate prevention, rating/
  comment projection ve Faz 12 iki cihazli notification invalidation ile kapanir.
- [ ] `PHASE-008` Faz 8 wallet/saved methods; iki rol wallet/history, default/
  delete ve Faz 9 hosted kart kaydetme sonucu ile kapanir.
- [ ] `PHASE-009` Faz 9 payment; currency quote, wallet/card purchase, top-up,
  hosted WebView, status/recovery/refund/manual-review ve Sandbox testleriyle
  kapanir.
- [ ] `PHASE-010` Faz 10 guide finance; earning/monthly projection, banka
  hesabi, withdrawal lifecycle, ledger ve simulated payout testleriyle kapanir.
- [ ] `PHASE-011` Faz 11 chat; find/create, REST list/history/send/read, STOMP
  reconnect/resync, iki kullanici ve Faz 12 FCM target senaryolariyla kapanir.
- [ ] `PHASE-012` Faz 12 notification; permission, FID registration, history/
  preferences/read, tum kategori mesaj/ikonlari, foreground/background/closed
  FCM ve semantic navigation senaryolariyla kapanir.

### 29. Stabilite, Akicilik ve Kaynak Kullanimi

Bu testler mikro benchmark degildir. Gercek kullanicinin fark edecegi donma,
ANR, tekrarli istek, asiri yeniden cizim veya liste bozulmasini yakalamak icin
uygulanir.

- [ ] `STABILITY-001` Cold start kullaniciya siyah/bos ekran birakmadan root
  loading uzerinden uygun hedefe ulasir.
- [ ] `STABILITY-002` Home, explore, tours, trips, wallet, chat ve notification
  uzun listeleri kaydirilirken belirgin donma veya ANR olmaz.
- [ ] `STABILITY-003` Remote gorseller liste kaydirirken yanlis karta atlamaz;
  tekrar gorunen item dogru gorseli kullanir.
- [ ] `STABILITY-004` Hizli tab/bottom bar gecisleri duplicate ViewModel state,
  duplicate request veya bozuk navigation uretmez.
- [ ] `STABILITY-005` Arama alanina hizli yazma debounce/cancellation ile eski
  sonucun yeni sorgunun ustune yazilmasini engeller.
- [ ] `STABILITY-006` 60 mesaj, 21 trip, 19 notification ve uzun transaction
  listelerinde pagination item kaybi/tekrari olmadan akar.
- [ ] `STABILITY-007` Chat ekrani 30 dakika acik kalip ag gidip geldiginde
  reconnect dongusu CPU/batarya/log spam olusturmaz.
- [ ] `STABILITY-008` Payment polling basari/failed/timeout/uygulamadan cikis
  sonrasi kesin olarak durur; arka planda sinirsiz calismaz.
- [ ] `STABILITY-009` Notification foreground listener ve STOMP subscription
  ekran/hesap degisiminde cogalmaz.
- [ ] `STABILITY-010` Kamera/galeri/media upload sonrasi gecici bitmap veya
  stream kaynaklari bellek baskisi/crash olusturmaz.
- [ ] `STABILITY-011` Ardisik profil/tur/avatar ekran gecislerinde eski hesabin
  image/state'i kisa sure gorunmez.
- [ ] `STABILITY-012` Uygulama arka plana alip geri getirme, ekran kilitleme ve
  process recreation sonrasinda kullanilabilir kalir.
- [ ] `STABILITY-013` Debug APK temiz kurulum, mevcut surum ustune update ve
  uninstall/reinstall senaryolarinda beklenen onboarding/session davranisini
  korur.
- [ ] `STABILITY-014` Logcat'te tekrarlanan uncaught exception, strict mode,
  serialization veya Compose snapshot hatasi bulunmaz.
- [ ] `STABILITY-015` Sistem hidden API/accessibility loglari uygulama hatasi
  sanilmaz; yalniz GuideMate kaynakli actionable hata kaydi acilir.

### 30. Manuel Kabul Turu Calistirma Sirasi

Testler asagidaki sirayla calistirilir. Bir blok kritik sekilde kalirsa ona
bagli sonraki blokta uydurma veri veya gecici Android mock'u kullanilarak
`gecti` sonucu uretilmez. Hata duzeltilir, ilgili blok yeniden kosulur.

#### Tur A - Ortam ve Temiz Baslangic

- [ ] `RUN-A01` Demo DB/normal DB izolasyonu, LAN URL, backend, PostgreSQL,
  OpenAPI ve media URL on kosullari kanitlanir.
- [ ] `RUN-A02` SMTP, Google, Places, FCM, STOMP ve iyzico/Quick Tunnel gereken
  testlerden once ayri ayri hazirlik kontrolu yapilir.
- [ ] `RUN-A03` Temiz emulator install ile ROOT/COMMON/VISUAL smoke turu kosulur.

#### Tur B - Auth ve Hesap Yasam Dongusu

- [ ] `RUN-B01` Yeni kayit -> e-posta confirm -> login -> role selection tam
  akisi kosulur.
- [ ] `RUN-B02` Google, pending, expired, forgot/reset, change password,
  refresh/logout ve disabled/rate-limit negatif akislari kosulur.
- [ ] `RUN-B03` Guide/Tourist account/profile/settings/yasal/yardim ekranlari ve
  role/account isolation kosulur.

#### Tur C - Rehber Uretim Akisi

- [ ] `RUN-C01` Rehber profil/avatar/about/language ve level gorunumleri kosulur.
- [ ] `RUN-C02` Media ile tour publish -> admin approve/reject -> notification ->
  active/review/past sekmeleri tam akisi kosulur.
- [ ] `RUN-C03` Tour detail/edit/change request/session/open/close/cancel/archive
  ve tum gecersiz lifecycle islemleri kosulur.
- [ ] `RUN-C04` Dashboard, wallet/history, earnings/monthly, banka hesabi ve
  withdrawal akislari kosulur.

#### Tur D - Turist Kesif ve Rezervasyon

- [ ] `RUN-D01` Home popular/top guide/public profile, explore tour/guide,
  search/filter/clear/pagination/empty/error akislari kosulur.
- [ ] `RUN-D02` Public tour/detail/review/session availability ve canonical kart
  tutarliligi kosulur.
- [ ] `RUN-D03` Trips upcoming/past/cancelled, reservation snapshot, cancel ve
  review eligibility/submit/duplicate akislari kosulur.

#### Tur E - Payment, Wallet ve Finance

- [ ] `RUN-E01` Wallet purchase yeterli/yetersiz bakiye ve eszamanli kapasite
  durumlari kosulur.
- [ ] `RUN-E02` Hosted card tour purchase tum currency, card save, success,
  failed, cancel ve timeout durumlariyla kosulur.
- [ ] `RUN-E03` Wallet top-up, balance/ledger yenileme ve provider saved card
  list/default/delete akislari kosulur.
- [ ] `RUN-E04` App/process death payment recovery, late callback, refund ve
  MANUAL_REVIEW akislari kosulur.
- [ ] `RUN-E05` Guide earning/wallet/withdrawal sonucu turist purchase/refund
  sonucuyla uctan uca karsilastirilir.

#### Tur F - Iki Cihaz Chat ve Notification

- [ ] `RUN-F01` Guide A ve Tourist A iki cihazda profil uzerinden find/create
  chat, REST history/send/read ve STOMP realtime akisini kosar.
- [ ] `RUN-F02` Offline send/retry, reconnect/resync, duplicate clientMessageId,
  cursor pagination ve unread badge kosulur.
- [ ] `RUN-F03` Notification tercihleri ve tum NotificationType'lar app ici,
  foreground, background ve uygulama kapali durumda kosulur.
- [ ] `RUN-F04` Her system notification tiklamasi read-state ve typed semantic
  navigation hedefiyle dogrulanir.

#### Tur G - Negatif, Guvenlik ve Dayaniklilik

- [ ] `RUN-G01` Guide B/Tourist B ile object authorization ve account isolation
  matrisi kosulur.
- [ ] `RUN-G02` Offline, backend restart, malformed response, rate limit, token
  expiry ve dis servis kesintileri kosulur.
- [ ] `RUN-G03` Race, idempotency, scheduler, late payment ve duplicate webhook/
  FCM/STOMP olaylari kosulur.
- [ ] `RUN-G04` Secret/log/storage/WebView/network security maddeleri kontrol
  edilir.

#### Tur H - Tasarim ve Erisilebilirlik

- [ ] `RUN-H01` Onceki turlarda gorulen tum yeni/degisen ekranlar DESIGN
  envanterinde tek tek isaretlenir.
- [ ] `RUN-H02` Portrait/landscape, kucuk ekran, buyuk font, klavye, uzun metin,
  loading/error/empty/content durumlari kosulur.
- [ ] `RUN-H03` TalkBack/contentDescription/focus/dokunma alani/kontrast ve
  locale/formatlama kontrolleri kosulur.
- [ ] `RUN-H04` Kullanici akis testinde daha once gecen ama tasarim/refactor
  sonrasi degisen ekranlar yeniden regresyona alinir.

#### Tur I - Final Regresyon ve Kapanis

- [ ] `RUN-I01` Tum `KALDI` ve `DUZELTILDI` maddeler ayni ortam/veriyle yeniden
  kosulur.
- [ ] `RUN-I02` Kritik guide-tourist-payment-chat-notification happy path temiz
  session ile bastan sona tekrar kosulur.
- [ ] `RUN-I03` Android otomatik kalite/test kapilari ve backend tam test paketi
  yeniden calistirilir; manuel test sonucu yerine sadece destek kaniti sayilir.
- [ ] `RUN-I04` Faz 0-12 durumlari gercek kanitlara gore guncellenir; E2E
  bekleyen faz `TAMAMLANDI` diye isaretlenmez.
- [ ] `RUN-I05` Acik bug, bilinmeyen davranis, test edilmemis kritik senaryo ve
  gecici mock kalmadigi dogrulanir.

### 31. Android Ekran Kapsam Izlenebilirligi

Bu envanter guncel kaynakta bulunan her `...Screen` composable'in en az bir
ozellik testi ve ortak gorsel/erisilebilirlik matrisiyle eslestigini dogrular.
Yeni ekran eklenirse bu liste ve ilgili ozellik testleri ayni degisiklikte
guncellenir.

- [ ] `SCREEN-001` `OnboardingScreen` -> ROOT ve VISUAL.
- [ ] `SCREEN-002` `SignInScreen` -> AUTH Sign In ve COMMON hata/form.
- [ ] `SCREEN-003` `SignUpScreen` -> AUTH Sign Up/dogrulama.
- [ ] `SCREEN-004` `ForgotPasswordScreen` -> AUTH Forgot/Reset.
- [ ] `SCREEN-005` `ChangePasswordScreen` -> AUTH Change Password.
- [ ] `SCREEN-006` `RoleSelectionScreen` -> AUTH Role Selection ve ROOT.
- [ ] `SCREEN-007` `GuideHomeScreen` -> GUIDE-HOME/dashboard/notification.
- [ ] `SCREEN-008` `GuideProfileScreen` -> PROFILE own guide.
- [ ] `SCREEN-009` `GuideProfilePreviewScreen` -> PROFILE preview/public
  ortak gorunum.
- [ ] `SCREEN-010` `GuideAboutScreen` -> PROFILE about/languages.
- [ ] `SCREEN-011` `GuideMyToursScreen` -> GUIDE-TOUR tabs/pagination.
- [ ] `SCREEN-012` `GuideTourDetailScreen` -> GUIDE-TOUR detail/lifecycle.
- [ ] `SCREEN-013` `GuideTourEditScreen` -> GUIDE-TOUR edit/change request.
- [ ] `SCREEN-014` `GuideTourPublishStep1LocationDateScreen` -> PUBLISH step 1.
- [ ] `SCREEN-015` `GuideTourPublishStep2CategoryPriceScreen` -> PUBLISH step 2.
- [ ] `SCREEN-016` `GuideTourPublishStep3DetailsMediaScreen` -> PUBLISH step 3.
- [ ] `SCREEN-017` `GuideTourPublishStep4PreviewPublishScreen` -> PUBLISH step 4.
- [ ] `SCREEN-018` `GuideMyWalletScreen` -> GUIDE-WALLET summary/withdraw.
- [ ] `SCREEN-019` `GuideWalletTransactionsScreen` -> GUIDE-WALLET history.
- [ ] `SCREEN-020` `GuideEarningsScreen` -> GUIDE-WALLET earnings/monthly.
- [ ] `SCREEN-021` `GuideBankAccountsScreen` -> GUIDE-WALLET bank accounts.
- [ ] `SCREEN-022` `GuideNotificationSettingsScreen` -> PROFILE notification
  settings ve NOTIFY preference.
- [ ] `SCREEN-023` `TouristHomeScreen` -> TOURIST-HOME.
- [ ] `SCREEN-024` `TouristExploreScreen` -> EXPLORE/pagination.
- [ ] `SCREEN-025` `TouristFilterScreen` -> FILTER/selectors.
- [ ] `SCREEN-026` `GuidePublicProfileScreen` -> PROFILE public guide.
- [ ] `SCREEN-027` `TouristTourDetailScreen` -> TOUR-DETAIL/reviews.
- [ ] `SCREEN-028` `TourCheckoutScreen` -> RESERVATION/PAYMENT checkout.
- [ ] `SCREEN-029` `TouristTripsScreen` -> TRIPS/pagination.
- [ ] `SCREEN-030` `TouristReservationDetailScreen` -> RESERVATION snapshot/
  cancellation/review.
- [ ] `SCREEN-031` `TouristWalletScreen` -> TOURIST-WALLET/top-up.
- [ ] `SCREEN-032` `TouristWalletTransactionsScreen` -> TOURIST-WALLET history.
- [ ] `SCREEN-033` `TouristSavedCardsScreen` -> PROFILE saved provider cards.
- [ ] `SCREEN-034` `TouristProfileScreen` -> PROFILE tourist/account.
- [ ] `SCREEN-035` `TouristNotificationSettingsScreen` -> PROFILE notification
  settings ve NOTIFY preference.
- [ ] `SCREEN-036` `HostedPaymentScreen` -> PAYMENT WebView/security/recovery.
- [ ] `SCREEN-037` `PaymentStatusScreen` -> PAYMENT redirecting/verifying/
  failure/cancel/timeout/manual review.
- [ ] `SCREEN-038` `PaymentSuccessScreen` -> PAYMENT success/result navigation.
- [ ] `SCREEN-039` `ChatListScreen` -> CHAT list/unread/empty/pagination.
- [ ] `SCREEN-040` `ChatDetailScreen` -> CHAT history/send/retry/realtime.
- [ ] `SCREEN-041` `HelpSupportScreen` -> PROFILE help/support actions.
- [ ] `SCREEN-042` `LegalAgreementsScreen` -> PROFILE legal content/navigation.

### 32. Hata Kaydi ve Kanit Sablonu

Her `KALDI` sonucu icin ayni kimlik korunarak asagidaki alanlar doldurulur:

```text
Test ID:
Tarih/saat:
Cihaz / Android surumu:
Hesap etiketi ve rol:
Backend profili / DB:
On kosul:
Adimlar:
Beklenen:
Gerceklesen:
Ekran goruntusu veya video:
Maskeli Android/backend log referansi:
Ilgili request/response code (secret ve kisisel veri olmadan):
Ilk tahmin degil, dogrulanan kok neden:
Duzeltilen dosya/commit:
Yeniden test sonucu:
Etkilenen diger test ID'leri:
```

Bir hata duzeltildiginde yalniz ilgili satir degil, ayni ortak component,
repository, mapper, navigation veya backend state transition'ini kullanan tum
etkilenmis senaryolar yeniden kosulur.

### 33. Tamamlanma Karari

GuideMate kullanici kabul turu ancak asagidaki kosullarin tamami saglandiginda
tamamlanmis sayilir:

- [ ] `CLOSE-001` Bu belgedeki her checkbox kanitli bir sonuc almistir.
- [ ] `CLOSE-002` Kritik/seviyesi yuksek acik kullanici, para, kapasite,
  yetkilendirme, veri sizintisi veya navigation hatasi yoktur.
- [ ] `CLOSE-003` Kapsam disi isaretlenen her madde bu belgedeki acik kararlardan
  birine dayanir; test edilemedigi icin kapsam disi yapilmamistir.
- [ ] `CLOSE-004` Faz 2-12 E2E eksikleri ilgili PHASE maddeleriyle kapanmistir.
- [ ] `CLOSE-005` Yeni/degisen tasarimlar ve ortak loading/error/empty sistemi
  gorsel kabulden gecmistir.
- [ ] `CLOSE-006` Demo verisi ana veritabani ve normal medya alanini
  degistirmemistir.
- [ ] `CLOSE-007` Final Android/backend otomatik test ve build sonuclari ayri ve
  dogru sayilarla raporlanmistir.
- [ ] `CLOSE-008` Bulunan tum duzeltmelerden sonra kritik uctan uca regresyon
  yeniden gecmistir.
- [ ] `CLOSE-009` Son durum, acik riskler ve bilinen kapsam sinirlari final proje
  dokumaninda guncellenmistir.

## Devam Kontrol Noktasi

- Belge yazimi: TAMAMLANDI
- Kapsam capraz kontrolu: TAMAMLANDI (`82/82` endpoint, `42/42` Screen,
  duplicate checklist ID yok, Markdown diff kontrolu temiz)
- Son tamamlanan bolum: `33. Tamamlanma Karari`
- Siradaki is: `RUN-A01` demo DB/LAN/backend/PostgreSQL/OpenAPI/media ortam
  hazirligi ile manuel kullanici kabul turuna baslamak.
- Baglam yenilenirse bu kontrol noktasindan devam edilir; tamamlanan bolumler
  yeniden yazilmaz veya onceki kararlarla degistirilmez.
