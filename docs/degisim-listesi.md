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

### DEG-069 - Odeme Zaman Asiminda Guvenli Durum Yenileme

- Durum: `TAMAMLANDI`
- Odeme sonucu `TIMEOUT` oldugunda `Tekrar Dene` aksiyonu odeme ekranindan
  cikmayacak; mevcut `paymentId` icin backend durum sorgusunu yeniden
  baslatacaktir.
- Bu aksiyon yeni odeme, rezervasyon, koltuk hold'u veya idempotency anahtari
  olusturmayacak ve karttan yeniden tahsilat denemeyecektir. Yalniz mevcut
  odemenin son durumu sorgulanacaktir.
- Sonuc kesinlesmisse kullanici basarili, basarisiz, iptal, iade veya manuel
  inceleme durumuna yonlendirilecektir. Sonuc halen dogrulaniyorsa mevcut polling
  davranisi devam edecektir.
- `FAILED` durumundaki yetersiz bakiye, kayip kart, gecersiz CVV ve banka reddi
  gibi kesin hatalarda yalniz `Odemeden Cik` aksiyonu korunacaktir.
- Mevcut `PaymentStatusViewModel.refresh()` ve repository sozlesmesi
  kullanilacaktir. Yeni endpoint, repository, use-case, ekran, navigation rotasi
  veya genel amacli tekrar deneme katmani eklenmeyecektir.
- Test karari: `TIMEOUT` durumundaki birincil aksiyonun ayni `paymentId` icin
  durum sorgusunu yeniden baslattigi ve cikis callback'ini tetiklemedigi odakli
  bir davranis testiyle dogrulanmalidir. Kesin `FAILED` durumunda yalniz cikis
  aksiyonunun kaldigi mevcut testte korunmali veya ayni odakli kapsamda
  dogrulanmalidir.

### DEG-070 - Uluslararasi Ad ve Soyad Dogrulamasi

- Durum: `TAMAMLANDI`
- Ad ve soyad dogrulamasi Turkce ve ASCII harflerle sinirli olmayacak; butun
  Unicode harfleri ile gercek isimlerde kullanilan tek bosluk, tire ve kesme
  isaretini destekleyecektir. `Jose`, `José`, `Anne-Marie` ve `O'Connor` gibi
  gecerli isimler kabul edilecektir.
- Basta ve sondaki bosluklar normalize edilecek, art arda bosluk veya ayiraclar
  kabul edilmeyecektir. Minimum uzunluk ayiraclarla degil harf sayisiyla
  hesaplanacaktir: ad en az 3, soyad en az 2 harf icerecektir. Bu nedenle
  `aa `, `a a` veya yalniz bosluklarla minimum uzunluk asilamayacaktir.
- Android ayni kurali erken UX dogrulamasi icin uygulayacak ve gecersiz
  karakter ile yetersiz harf sayisini XML kaynakli, acik hata metinleriyle
  gosterecektir.
- Backend ayni kurali kayit API sinirinda otoriter olarak uygulayacaktir.
  Android kontrolu atlanarak API dogrudan cagrilsa bile gecersiz ad veya soyad
  kaydedilmeyecektir. Normalize edilmis deger dogrulanip saklanacak; DTO
  dogrulamasi ile kaydedilen deger arasinda bosluk kaynakli tutarsizlik
  kalmayacaktir.
- Yeni ekran, navigation rotasi, use-case veya genel amacli validation
  framework'u eklenmeyecektir. Mevcut Android form dogrulamasi ve backend kayit
  siniri, ayni acik business kuraliyla en kucuk kapsamda guncellenecektir.
- Test karari: Android ve backend tarafinda kabul edilen uluslararasi isimler ile
  bosluk/ayirac kullanarak minimum uzunlugu asmayan gecersiz girdiler odakli
  parametrik testlerle dogrulanmalidir. Ayni regex implementation ayrintisi
  tekrar tekrar test edilmeyecektir.

### DEG-071 - Sozlesme, KVKK ve SSS Metinlerini Gercek Akisla Eslemek

- Durum: `TAMAMLANDI`
- Kayit sozlesmesi, profil altindaki yasal metinler ve SSS cevaplari uygulamanin
  gercek odeme, iptal, iade ve veri isleme davranislarini birbiriyle tutarli
  anlatacaktir.
- Odeme metni yalniz kart odemesi varmis gibi yazilmayacak; kart ve GuideMate
  cuzdan odemelerini kapsayacaktir. Ham kart bilgilerinin GuideMate sunucularinda
  saklanmadigi ve kart islemlerinin odeme saglayicisi tarafindan yurutuldugu
  gercek siniriyla ifade edilecektir.
- Iptal metinlerinin tamami canonical politikayla eslenecektir: tur baslangicina
  en az 48 saat varken turist iptalinde tam iade, 48 saatten az varken iade yok,
  rehber kaynakli iptalde tam iade. Gercekte bulunmayan `standart kesinti` veya
  telafi odemesi vaatleri kaldirilacaktir.
- Veri isleme aciklamasi yalniz rezervasyonla sinirli tutulmayacak; hesap ve
  e-posta dogrulama, guvenlik, rezervasyon, mesajlasma, bildirim ve odeme
  islemlerini kapsayan sade ve gercek bir aciklama kullanacaktir.
- Uygulamada kokart yukleme veya dogrulama akisi bulunmadigi surece GuideMate'in
  rehber kokartini zorunlu olarak dogruladigi izlenimi veren ifade
  kullanilmayacaktir. Gercekte uygulanmayan bir kontrol metinle vaat
  edilmeyecektir.
- Local MVP'de dogrulanamayan `256-bit SSL` gibi kesin teknik guvenlik vaatleri
  kaldirilacak; metin odeme saglayicisi ve guvenli hosted odeme sinirini
  anlatacaktir. Production yayinindan once nihai metin hukuk uzmani tarafindan
  ayrica incelenmelidir.
- Yalniz XML metin kaynaklari guncellenecektir. Backend is kurallari, ekranlar,
  navigation, odeme ve iptal akislarinda davranis degisikligi yapilmayacaktir.
- Test karari: Salt metin uyumu icin kirilgan otomatik UI testi yazilmayacak;
  resource/derleme kontrolu ve odeme-iptal akislarini kapsayan kullanici testi
  yeterli olacaktir.

### DEG-072 - Kismi Veri Hatasini Gercek Bos Durumdan Ayirmak

- Durum: `TAMAMLANDI`
- Rehber public profili basariyla yuklenirken tur onizleme istegi basarisiz
  olursa tur bolumu bos liste gibi kaybolmayacaktir. Profil gorunmeye devam
  edecek; yalniz tur bolumunde XML kaynakli `Turlar yuklenemedi` mesaji ve
  `Tekrar Dene` aksiyonu gosterilecektir.
- Turist rezervasyon detayi basariyla yuklenirken yorum istegi basarisiz olursa
  bu sonuc `Henuz yorum yok` olarak gosterilmeyecektir. Rezervasyon detayi
  korunacak; yalniz yorum bolumunde XML kaynakli hata ve `Tekrar Dene` aksiyonu
  gosterilecektir.
- Gercek bos durum yalniz backend basarili cevapla bos liste dondurdugunde
  gosterilecektir. Ag, sunucu veya parsing hatasi bos veriye donusturulmeyecek;
  mevcut merkezi `AppError` eslemesi kullaniciya uygun mesaja donusecektir.
- `Tekrar Dene` yalniz basarisiz olan alt bolumun repository istegini yeniden
  calistiracaktir. Internet geri geldiyse tur veya yorumlar ayni ekranda
  gorunecek; profil ya da rezervasyonun basariyla yuklenmis ana verisi yeniden
  kaybedilmeyecektir.
- Yeni backend endpoint'i, ekran, navigation rotasi, use-case veya genel amacli
  yukleme framework'u eklenmeyecektir. Mevcut repository arayuzleri ve ortak
  yukleme/hata bilesenleri, ilgili feature'in dar alt-bolum state'iyle
  kullanilacaktir.
- Test karari: Basarisiz alt istegin bos durum yerine hata durumuna donustugu,
  tekrar denemenin yalniz ilgili istegi yeniden calistirdigi ve sonraki basarili
  cevabin veriyi gosterdigi odakli ViewModel testleriyle dogrulanmalidir. Salt
  hata metni veya Compose gorunumu icin gereksiz UI testi yazilmayacaktir.

### DEG-073 - Rehber Cuzdaninda Acik Bos Durumlar

- Durum: `TAMAMLANDI`
- Rehber cuzdaninda aylik kazanc listesi basarili cevapla bos geldiyse sabit ve
  aciklamasiz alan yerine XML kaynakli `Henuz kazanciniz bulunmuyor` mesaji
  gosterilecektir.
- Son cuzdan hareketleri basarili cevapla bos geldiyse XML kaynakli `Henuz
  cuzdan hareketiniz bulunmuyor` mesaji gosterilecektir.
- Bos durum metinleri uygulamadaki diger bos liste mesajlariyla ayni
  `R.color.text_color` ve body typography kullanimi ile gosterilecektir. Gereksiz
  sabit bosluk kucultulecektir.
- `Tumunu Gor` aksiyonlari liste bosken de korunacaktir. Bu aksiyonlar kullaniciya
  aylik kazanc gecmisi ile `Tumu`, tur kazanclari, para cekme ve diger cuzdan
  hareketi filtrelerinin bulundugu tam ekranlari kesfetme imkani verir. Hedef
  ekranlar bos sonucu kendi XML kaynakli aciklamalariyla gostermeye devam
  edecektir.
- Turist ana sayfasindaki `Henuz uygun bir rehber bulunamadi` bos durum metni de
  tema varsayilanina birakilmayacak; diger bos durumlarla tutarli olarak acikca
  `R.color.text_color` kullanacaktir.
- Ilgili veri olustugunda bos durum metninin yerini mevcut onizleme listesi
  alacaktir. Basarili bos cevap ile ag/sunucu hatasi birbirine
  karistirilmayacak; hata durumu `DEG-072` kapsamindaki hata ve yeniden deneme
  davranisini kullanacaktir.
- Backend, repository, navigation ve ekran yapisi degismeyecek; yeni genel
  amacli empty-state framework'u kurulmayacaktir. Degisiklik mevcut rehber
  cuzdan iceriginde orantili bir presentation duzenlemesi olarak kalacaktir.
- Test karari: Salt metin, renk ve bosluk icin kirilgan UI testi yazilmayacak;
  derleme ve kullanici testi yeterli olacaktir. Bos liste ile hata ayriminin
  state davranisi `DEG-072` test kapsaminda dogrulanacaktir.
