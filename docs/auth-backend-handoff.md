# GuideMate Auth Backend Devir Belgesi

Bu belge, mevcut auth backend taramasinda bulunan 15 madde icin alinan kesin
kararlari tek yerde toplar.

Projeler:

- Backend: `/Users/ahmetkaragunlu/IdeaProjects/GuideMateBackend`
- Android: `/Users/ahmetkaragunlu/AndroidStudioProjects/GuideMate`

## Uygulama Sirasi

1. Backend sohbeti once mevcut Spring Boot auth kodunu ve bu belgenin
   `Backend'de Yapilacaklar` bolumunu tekrar kontrol ederek uygular.
2. Backend sohbeti Android kodunu bu asamada degistirmez. Android bolumunu API
   sozlesmesini dogru kurmak ve sonraki mobil isleri bilmek icin okur.
3. Auth backend tamamlaninca bu belgenin aynisi Android sohbetine verilir ve
   `Backend Tamamlandiktan Sonra Android'de Yapilacaklar` bolumu uygulanir.
4. Auth iki tarafta uyumlu hale geldikten sonra tum uygulamanin tablo, endpoint,
   iliski, transaction ve altyapi plani ayrica hazirlanir.
5. Bu belge tur, rezervasyon, odeme, mesajlasma ve bildirim backend'ini simdiden
   tasarlama veya yazma talimati degildir.

## Backend'de Yapilacaklar

### 1. Public Rol Secimi ve Admin Guvenligi

- Public kayit ve rol secimi yalnizca `TOURIST` ve `GUIDE` kabul edecek.
- Public request modeli backend'in dahili `ROLE_ADMIN` degerini aciga
  cikarmayan ayri bir secilebilir rol tipi kullanacak.
- `ROLE_ADMIN` Spring Security yetkilendirme modelinde kalacak fakat public
  endpoint ile atanamayacak.
- Admin hesabi kontrollu seed, migration veya yonetim islemiyle olusturulacak.
- Tur onay/red islemleri ileride yalniz ADMIN yetkili endpoint'lerden
  yapilacak. MVP'de ayri admin paneli zorunlu degildir; IntelliJ HTTP Client
  veya Postman kullanilabilir.
- Rol secimi mevcut JWT principal uzerinden kullaniciyi bulacak ve yalniz bir
  kez yapilacak.

### 2. JWT, 401 ve 403 Hata Sozlesmesi

- Gecersiz, bozuk veya suresi dolmus access token sessizce yutulmayacak.
- Kimlik dogrulama gerektiren durumlar ortak `ErrorResponse` ile
  `401 Unauthorized` donecek.
- Kimligi dogrulanmis fakat yetkisi yetersiz kullanici ayni hata sozlesmesiyle
  `403 Forbidden` alacak.
- Spring Security icin kontrollu `AuthenticationEntryPoint` ve
  `AccessDeniedHandler` kurulacak.
- Teknik exception, stack trace, ham JWT veya hassas bilgi istemciye
  sizdirilmayacak.
- Gecici sunucu/ag hatasi terminal oturum hatasi gibi raporlanmayacak.

### 3. Korumali Sifre Degistirme Endpoint'i

- Kimligi dogrulanmis kullanici icin
  `POST /api/v1/auth/change-password` eklenecek.
- Kullanici request icindeki `userId` veya e-postadan degil JWT principal
  uzerinden bulunacak.
- Mevcut sifre `PasswordEncoder.matches` ile dogrulanacak.
- Yeni sifre ortak parola politikasina uyacak ve mevcut sifreyle ayni
  olmayacak.
- Backend yalniz mevcut ve yeni sifreyi alacak. `confirmPassword` Android UI
  kontrolu olarak kalacak.
- Yeni sifre yalniz BCrypt hash olarak saklanacak; ham sifre loglanmayacak.
- Basarili degisiklikten sonra kullanicinin aktif refresh oturumlari revoke
  edilecek ve yeniden giris zorunlu olacak.
- Yanlis mevcut sifre ve parola politikasi ihlali sabit hata kodlariyla
  ayrilacak.

### 4. Auth Kullanici Sozlesmesi

- Auth ve current-user cevabi en az `userId`, `email`, `firstName`,
  `lastName`, `role` ve `roleSelected` alanlarini dondurecek.
- Alan adlari, tipleri ve nullability Android DTO ile birebir
  kesinlestirilecek.
- Backend sahiplik ve yetkilendirme icin Android'den gelen `userId` degerine
  guvenmeyecek; her zaman JWT principal kullanacak.
- Ayrintili profil, banka, tur, rezervasyon ve odeme bilgileri auth cevabina
  doldurulmayacak.

### 5. E-posta Dogrulama ve Yeniden Gonderme

- Mevcut e-postadaki link ile acilan
  `GET /api/v1/auth/confirm?token=...` web dogrulama akisi korunacak.
- `POST /api/v1/auth/resend-verification` endpoint'i eklenecek.
- Resend yalniz dogrulanmamis local hesap icin yeni, sureli ve tek kullanimlik
  token uretecek; onceki aktif tokenlar gecersiz kilinacak.
- Aktif hesap icin yeni token uretilmeyecek.
- Resend kisa cooldown/rate-limit ile korunacak.
- SMTP/e-posta gonderim hatasi sessizce yutulmayacak; pasif kullanici kaydi
  silinmeden kurtarma yolu sunulacak.
- Public cevap gereksiz hesap varligi bilgisi sizdirmayacak.
- Basarili, suresi dolmus, kullanilmis ve gecersiz token icin teknik olmayan
  ayri web sonucu gosterilecek.
- Token, SMTP parolasi ve hassas e-posta icerigi loglanmayacak.

### 6. Ortak Sayisal Parola Politikasi ve Login Korumasi

- Local hesap parolasi yalnizca `0-9` karakterlerinden olusacak ve en az
  8 haneli olacak.
- Kayit, sifre degistirme ve sifre sifirlama ayni merkezi, test edilebilir
  parola politikasini kullanacak; kural DTO'lara kopyalanmayacak.
- Login request'i parola guc politikasini yeniden uygulamayacak; gercek kimlik
  dogrulamayi `PasswordEncoder.matches` yapacak.
- Parola yalniz BCrypt hash olarak saklanacak ve hicbir response/log icinde
  bulunmayacak.
- Basarisiz login denemeleri hesap ve istemci/IP sinirlariyla backend'de
  sinirlandirilacak.
- Kalici kilit yerine konfigurable, gecici ve artan bekleme uygulanacak.
- Rate-limit sabit hata kodu, uygun HTTP status ve mumkunse `Retry-After`
  bilgisi dondurecek.
- Yanlis e-posta ve yanlis parola hesap varligini aciklamayan ortak
  `INVALID_CREDENTIALS` cevabini kullanacak.
- CAPTCHA, MFA, Redis tabanli dagitik limit veya agir risk motoru bu MVP'ye
  pesinen eklenmeyecek.
- Backend web reset formu da en az 8 rakam kuralina gecirilecek.

### 7. Refresh Token, Oturum ve Installation ID

- Mevcut `deviceId` kavrami `installationId`, `X-Device-Id` header'i
  `X-Installation-Id` olarak yeniden adlandirilacak.
- Installation ID Android kurulumuna ait resetlenebilir UUID olacak; IMEI veya
  fiziksel cihaz kimligi kullanilmayacak.
- Installation ID kimlik dogrulama veya yetkilendirme kaniti olmayacak.
- Register ve sade rol secimi installation ID almayacak.
- Local ve Google login basarisinda kullanici + installation icin kontrollu
  oturum olusturulacak. Ayni installation yeniden girerse eski oturum
  revoke/replace edilecek; kontrolsuz token satiri birikmeyecek.
- Refresh token tahmin edilemez opaque secret olacak; veritabaninda ham token
  yerine hash saklanacak.
- Session kaydi en az user, installation ID, token hash, created/expiry ve
  revocation durumunu tutacak.
- Her basarili refresh transaction icinde eski refresh tokeni revoke edip yeni
  access + refresh token cifti uretecek.
- Eski refresh tokenin yeniden kullanimi replay olarak ele alinacak ve ilgili
  session ailesi revoke edilerek yeniden giris istenecek.
- Refresh sirasinda expiry, revocation ve installation uyumu backend'de
  dogrulanacak.
- Rol secimi yeni role sahip access token dondurebilir fakat ikinci refresh
  session olusturmayacak.
- Logout ilgili backend sessionini revoke edecek.
- Sifre degistirme ve sifre sifirlama kullanicinin aktif refresh oturumlarini
  revoke edecek.
- DPoP, mTLS, Redis session, gelismis risk motoru veya cihaz yonetim paneli bu
  MVP kapsaminda olmayacak.

### 8. Sifremi Unuttum Gizliligi

- `ForgotPasswordRequest` yalniz normalize edilmis e-posta tasiyacak; ad ve
  soyad istemeyecek.
- Kayitli ve kayitsiz e-posta icin ayni public cevap kullanilacak:
  "Uygun bir hesap varsa sifre sifirlama baglantisi gonderildi."
- Public cevap `USER_NOT_FOUND`, provider turu veya ad/soyad uyusmazligi ile
  hesap varligini sizdirmayacak.
- Uygun local hesapta onceki aktif reset tokenlari gecersiz kilinip yeni,
  rastgele, sureli ve tek kullanimlik token olusturulacak.
- Yalniz en son gecerli token sifreyi degistirebilecek.
- Basarili reset tokeni kullanilmis yapacak ve aktif refresh sessionlarini
  revoke edecek.
- Endpoint e-posta + istemci/IP sinirli cooldown/rate-limit ile korunacak.
- Kayitsiz hesap icin e-posta/token uretilmeyecek fakat public davranis hesap
  varligini aciklamayacak.
- Reset linki backend web sayfasini acacak; basarili, gecersiz, suresi dolmus,
  kullanilmis token ve parola politikasi durumlari teknik olmayan sonuc
  gosterecek.

### 9. Flyway, Profiller ve Secret Guvenligi

- Hibernate `ddl-auto=update` kaldirilacak; Flyway sema ve migration
  gecmisinin tek otoritesi olacak.
- Hibernate migration sonrasinda `ddl-auto=validate` kullanacak.
- Mevcut local test verisi korunmak zorunda degildir; temiz PostgreSQL semasi
  ve auth `V1` migration'i olusturulabilir.
- Uygulanmis migration degistirilmeyecek; her sema degisikligi yeni migration
  olacak.
- `application-local`, `application-test` ve `application-prod` sorumluluklari
  ayrilacak.
- Production local DB, placeholder credential veya guvensiz fallback'e
  dusmeyecek.
- JWT, DB, mail, Google ve iyzico secretlari kaynak kod/Git disinda
  environment veya ignore edilen local configuration ile verilecek.
- Eksik kritik production configuration fail-fast davranacak.
- Dev seed/test verisi production migrationina gomulmeyecek.
- SQL sorgu/parametre loglari production'da kapali olacak.
- Gereksiz Vault, container orkestrasyonu veya agir DevOps altyapisi bu MVP
  icin zorunlu olmayacak.

### 10. Tum API'ler Icin Ortak Hata Sozlesmesi

- Ortak hata sozlesmesi yeni domain endpoint'lerinden once kurulacak.
- Business, bean validation, malformed request, authentication,
  authorization, conflict, rate-limit ve beklenmeyen hatalar tek guvenli
  envelope kullanacak.
- Envelope en az sabit makine hata kodu, guvenli fallback mesaj, timestamp ve
  validation durumunda opsiyonel `fieldErrors` tasiyacak.
- Her field error alan adi, sabit hata kodu ve guvenli fallback mesaj
  tasiyabilecek.
- HTTP semantigi korunacak: `400`, `401`, `403`, `409`, `429`, `500`.
- Beklenmeyen exception mesaji/stack trace cevaba eklenmeyecek; ayrinti yalniz
  guvenli backend logunda kalacak.
- Android davranis belirlemek icin mesaj metni degil sabit hata kodu
  kullanacak.
- Gereksiz agir error framework veya izleme altyapisi eklenmeyecek; mevcut
  `ErrorResponse` ihtiyac kadar genisletilecek.

### 11. Testlerin Uygulama Zamani

- Kapsamli Android ve backend testleri projenin tum domain kodlari
  tamamlandiktan sonra birlikte yazilacak; bu karar testleri iptal etmez.
- Mevcut yalniz `contextLoads` testi yeterli kabul edilmeyecek.
- Final test fazinda backend unit, controller, security ve PostgreSQL
  integration testleri eklenecek.
- Auth icin public ADMIN reddi, register/confirm/login, 401/403, refresh
  rotation/replay, logout, resend, forgot/reset/change password,
  rate-limit, hata envelope ve e-posta normalizasyonu kapsanacak.
- Test profile local/production gercek veritabanina baglanmayacak.
- Bu asamada sirf test yazilmadi diye kararlastirilmis auth backend
  implementasyonu durdurulmayacak.

### 12. Yerel Ag ve URL Yapilandirmasi

- CV/MVP demosunda Spring Boot ve PostgreSQL Mac'te calisacak; AWS veya
  ucretli public hosting zorunlu olmayacak.
- Spring Boot yerel agdan erisilebilir olacak; local profilde gerekirse
  `0.0.0.0` bind edilecek.
- Telefon PostgreSQL'e dogrudan degil yalniz Spring Boot HTTP/WebSocket
  sinirina baglanacak.
- E-posta dogrulama ve reset linklerinde hardcoded `localhost`
  kullanilmayacak.
- `PUBLIC_BASE_URL` profile/environment'tan alinacak ve fiziksel telefondan
  erisilebilir local LAN adresini kullanabilecek.
- Reset web sayfasi backend POST isteginde sabit host yerine ayni origin'e gore
  relative `/api/v1/auth/reset-password` kullanacak.
- LAN IP secret degildir fakat kaynak koda gomulmeyecek; ag degisince yalniz
  local configuration guncellenecek.
- Production config local IP, emulator adresi veya localhost'a sessizce
  dusmeyecek ve gercek deployment'ta HTTPS kullanacak.

### 13. BaseEntity, Audit ve Silme Politikasi

- Ortak `BaseEntity` butun tablolara generic soft-delete davranisi vermeyecek.
- `deleted`, `deletedAt` ve kullanilmayan `softDelete()` ortak base sinifindan
  kaldirilacak.
- `BaseEntity` yalniz gercekten ortak kimlik/audit alanlarini tasiyacak.
- User silme/pasife alma acik hesap status'u ile; tur, rezervasyon, odeme,
  mesaj ve token yasam donguleri kendi domain durumlariyla yonetilecek.
- Tur/rezervasyon/finans gecmisi generic delete ile kaybedilmeyecek.
- Confirmation, reset ve refresh token kayitlari kendi expiry/revocation
  cleanup kurallariyla fiziksel silinebilecek.
- Her entity'ye gizli Hibernate soft-delete filtresi zorla eklenmeyecek.
- Temiz Flyway `V1`, ihtiyaci olmayan tablolara `deleted/deleted_at`
  kolonlarini koymayacak.

### 14. Google Yalnizca Mevcut Hesaba Giris

- Google ile yeni GuideMate hesabi olusturulmayacak.
- Google girisi yalniz onceden normal kayit olmus ve aktif mevcut hesaba
  alternatif giris yontemi olacak.
- Backend Google ID tokeni Google kutuphanesiyle dogrulayacak; imza, audience,
  issuer, expiry ve dogrulanmis e-posta kontrollerini uygulayacak.
- Google'in degismeyen `sub` kimligi mevcut hesaba guvenli sekilde baglanacak
  ve sonraki girislerde asil dis kimlik olarak kullanilacak.
- Hesap yoksa otomatik user/role olusturulmayacak; kararli ve
  yerellestirilebilir "Once kayit olun" hata kodu donecek.
- Yalniz aktif hesap oturum acabilecek.
- Ham Google ID token kalici saklanmayacak veya loglanmayacak.

### 15. Ortak E-posta Normalizasyonu ve Benzersizlik

- Kayit, giris, sifre sifirlama, dogrulama, current-user ve Google girisi dahil
  her kimlik akisinda ayni e-posta normalizasyonu uygulanacak.
- GuideMate normalizasyonu `strip/trim + lowercase(Locale.ROOT)` olacak.
- Gmail noktasi veya `+etiket` gibi saglayiciya ozel donusum yapilmayacak.
- Backend her kayit ve sorgudan once normalizasyon uygulayacak; istemciden gelen
  bicime guvenmeyecek.
- Veritabaninda yalniz normalize edilmis e-posta saklanacak ve uzerinde
  `UNIQUE` constraint bulunacak.
- Servis `existsByEmail` kontrolu tek guvence sayilmayacak.
- Eszamanli iki kayit isteginde DB constraint son guvence olacak ve ihlal
  kararli `EMAIL_ALREADY_EXISTS` hatasina cevrilecek.

## Backend Tamamlandiktan Sonra Android'de Yapilacaklar

### 1. Rol Secimi ve Root Yonlendirme

- Android modellerinden ADMIN kaldirilmis durumda korunacak; admin UI
  eklenmeyecek.
- Gercek rol secimi mevcut `SelectRoleUseCase` ile backend'e baglanacak.
- Auth cevabindaki `roleSelected` ve `role` degerlerine gore kullanici rol
  secimine, rehber root'una veya turist root'una yonlendirilecek.
- MVP icin sabit rehber baslangici kaldirilacak.

### 2. JWT Hatalari ve Oturum Kaybi

- Mevcut `TokenAuthenticator`, access token `401` aldiginda sessiz refresh
  denemesi icin korunacak.
- Refresh token terminal olarak gecersiz/suresi dolmus/replay ise tokenlar ve
  `UserState` birlikte temizlenecek.
- Kullanici Sign In ekranina yalniz bir kez yonlendirilecek; eszamanli `401`
  cevaplari birden fazla navigation olusturmayacak.
- Gecici internet, timeout veya `5xx` kullaniciyi oturumdan cikarmayacak.
- `401` ile `403` ve ag hatasi birbirine karistirilmayacak.

### 3. Sifre Degistirme UX ve Veri Akisi

- Mevcut ekran, ViewModel, use case ve repository yapisi korunacak.
- API request'inden `confirmPassword` kaldirilacak; eslesme UI kontrolu olacak.
- Istek sirasinda loading ve tekrar tiklama engeli uygulanacak.
- Basarida ortak tasarimla uyumlu, kapatilamaz ve yalniz `Tamam` aksiyonlu
  dialog gosterilecek.
- `Tamam` ile access/refresh token ve `UserState` temizlenecek; tum yetkili
  back stack temizlenerek Sign In ekranina gidilecek.
- Kullanici geri hareketiyle eski yetkili ekrana donemeyecek.
- Yanlis mevcut sifre/validation hatasinda oturum kapatilmayacak.
- Hassas auth request body'leri debug BODY loglarinda gorunmeyecek.

### 4. Minimum UserState ve Mapping

- Android `AuthResponse`, `userId`, `email`, `firstName`, `lastName`, `role` ve
  `roleSelected` alanlarini karsilayacak.
- `UserState` yalniz bu minimum oturum kimligini tutacak.
- Network DTO repository katmaninda domain/local state'e map edilecek; UI ve
  ViewModel backend DTO'suna baglanmayacak.
- `UserManager` minimum state'i kalici saklayip `StateFlow` ile yayinlayacak.
- Ayrintili profil, banka, tur, rezervasyon ve odeme verisi `UserState` icine
  eklenmeyecek.
- Android `userId` veri esleme icindir; backend yetkilendirme kaniti degildir.

### 5. E-posta Dogrulama ve Resend UX

- Kayit sonrasi mevcut e-posta dogrulama bilgilendirmesi korunacak.
- Dogrulama e-postadaki link ve backend web sayfasi uzerinden tamamlanacak.
- Sign In `ACCOUNT_NOT_ACTIVE` aldiginda teknik olmayan aciklama ve
  `Dogrulama E-postasini Tekrar Gonder` aksiyonu sunulacak.
- Resend sirasinda loading/tekrar tiklama korumasi ve cooldown bekleme durumu
  gosterilecek.
- Basarili resend, gecici ag/SMTP hatasi ve tekrar deneme durumu ayri
  gosterilecek.
- Metinler string resource ile yerellestirilebilir olacak.

### 6. Ortak Sayisal Parola UI Politikasi

- Sign Up, Sign In, Change Password ve Android icindeki reset alanlari sayisal
  klavye ve yalniz rakam girisini kullanacak.
- Kayit, degistirme ve reset ekranlari ortak Android parola politikasi ile en az
  8 rakami kontrol edecek; ayni regex farkli ViewModel'lara kopyalanmayacak.
- Android kontrolu hizli geri bildirimdir; backend otoritesi korunacak.
- Login sirasinda loading ve tekrar tiklama korumasi olacak.
- `INVALID_CREDENTIALS`, rate-limit/bekleme ve gecici ag hatasi birbirinden
  ayrilacak.

### 7. Token ve Installation Saklama

- `deviceId` isimleri `installationId`, header ise `X-Installation-Id` olacak.
- Ayni app kurulumu icin tek resetlenebilir UUID uretilip saklanacak; logout
  installation ID'yi silmeyecek.
- Login, Google login ve refresh gerekli installation header'ini gonderecek;
  register ve sade rol secimi gondermeyecek.
- Her refresh cevabinda access + refresh token yeni cift olarak atomik/tutarli
  saklanacak.
- Eszamanli `401` cevaplari tek refresh isteginde birlestirilecek ve sonsuz
  retry engellenecek.
- Terminal session hatasi tokenlari ve `UserState`i temizleyecek; gecici ag
  hatasi temizlemeyecek.
- Deprecated `EncryptedSharedPreferences/MasterKey` final auth
  entegrasyonunda `TokenManager` siniri arkasinda Android Keystore destekli
  guncel saklamayla degistirilecek.
- Token, installation ID ve hassas body loglanmayacak.

### 8. Sifremi Unuttum Ekrani

- Formdan ad ve soyad alanlari/callback'leri kaldirilacak; yalniz e-posta
  kalacak.
- Form state, request DTO, repository ve use case yalniz e-posta tasiyacak.
- Mevcut GuideMate baslik, tipografi, renk, bosluk, `EditTextField` ve
  `EditButton` tasarimi korunacak.
- Yerel e-posta format/bosluk hatasi field supporting text ile gosterilecek.
- Public basari mesaji hesap varligini aciklamayacak.
- Rate-limit, cooldown ve gecici ag/servis hatasi kullaniciya anlasilir
  gosterilecek.
- Basari/uyari icin mevcut Toast, supporting text veya gerekli dialog
  kullanilacak; yeni mesaj sistemi eklenmeyecek.

### 9. Ortam Bazli Android Base URL

- Backend secretlari Android `BuildConfig`, `local.properties`, APK veya
  istemci modellerine konulmayacak.
- Genel backend root URL local property/environment'tan BuildConfig'e
  aktarilacak; yalniz `/api/v1/auth/` yoluna kilitlenmeyecek.
- Emulator icin `10.0.2.2`, fiziksel cihaz icin Mac'in guncel LAN IP'si
  kullanilabilecek.
- Gelistiriciye ozel LAN IP Git'e gomulmeyecek.
- Local HTTP yalniz debug/local testte kontrollu desteklenecek; production
  cleartext HTTP'ye sessizce izin vermeyecek.

### 10. Ortak Android Hata Isleme

- Repository'ler tek ortak API hata parser'i kullanacak; JSON ayrisma kodu
  tekrarlanmayacak.
- `AppError.Backend`, sabit hata kodu, guvenli fallback mesaj ve varsa
  `fieldErrors` tasiyacak.
- Bilinen backend kodlari Android string resource metinlerine map edilecek;
  bilinmeyen kod guvenli genel mesaja dusecek.
- UI backend mesaj metnine gore karar vermeyecek.
- Yerel bos alan, e-posta bicimi ve parola kurali kontrolleri korunacak.
- Backend alan hatasi mevcut `supportingText` ile ilgili field'a
  baglanabilecek; alan duzenlenince eski hata temizlenecek.
- Genel auth hatalari mevcut Toast ile gosterilecek; yeni Snackbar sistemi
  eklenmeyecek.
- Dialog yalniz basari/onay/kullanici aksiyonu gereken mevcut akislar icin
  kullanilacak.
- ViewModel'ler tamamen yeniden yazilmayacak; mevcut form/submit state'i
  yapilandirilmis hatayi uygun UI state'e map edecek sekilde genisletilecek.

### 11. Android Testleri

- Android testleri projenin tamamlanmasindan sonraki ortak test fazinda
  yazilacak.
- Final fazda auth hata parser'i, ViewModel validation ve navigation/session
  kaybi senaryolari test edilecek.
- Access refresh, terminal refresh, tek seferlik Sign In yonlendirmesi, rol
  secimi, Google login, resend, forgot/reset/change password ve e-posta
  normalizasyonu kapsanacak.
- Testlerin ertelenmesi, bu davranislarin final kontrolden cikarildigi anlamina
  gelmez.

### 12. Fiziksel Cihaz ve Coklu Cihaz Testi

- Fiziksel telefon `localhost` veya `10.0.2.2` yerine Mac'in guncel LAN IP'sine
  baglanacak.
- Telefon, emulator ve ikinci cihaz ayni Wi-Fi'da ayni Spring Boot backend'i ve
  PostgreSQL verisini kullanacak.
- Ag degisince kaynak kod degil local URL configuration guncellenecek.
- Backend/DB kapali, firewall engeli veya yanlis IP veri/model hatasi gibi
  sunulmayacak.
- Tum domainler tamamlandiginda rehber ve turist farkli cihazlardan ortak tur
  verisini ve WebSocket mesajlarini gorebilecek.

### 13. Entity Silme Politikasinin Android Etkisi

- Bu madde Android ekran, ViewModel veya tasarim degisikligi gerektirmez.
- Android ileride backend'in acik hesap/tur/rezervasyon durumlarini
  gosterecek; generic `deleted` alanina baglanmayacak.
- Pasif/engelli hesap veya domain durumu ortak hata ve DTO sozlesmesiyle
  islenecek.

### 14. Google Giris Android Akisi

- Android mevcut Google ID tokenini backend'e gonderecek; `email_verified` veya
  `sub` guvenlik karari istemcide verilmeyecek.
- Google kayit ekrani ve otomatik hesap olusturma eklenmeyecek.
- Hesap bulunamazsa backend'in sabit hata kodu mevcut Toast tasarimiyla
  "Bu e-posta ile kayitli hesap bulunamadi. Once kayit olun." anlaminda
  yerellestirilmis gosterilecek.
- Backend tamamlandiginda Google basari sonucunun `roleSelected/role`
  yonlendirmesi yeniden dogrulanacak.

### 15. Android E-posta Normalizasyonu

- Sign Up, Sign In ve Forgot Password e-postayi gondermeden once
  `trim + lowercase(Locale.ROOT)` uygulayacak.
- Gerekli ortak Android normalizasyonu tek yerde tutulacak; ayni donusum
  ViewModel'lara kopyalanmayacak.
- Gmail noktasi veya `+etiket` silinmeyecek.
- Android normalizasyonu kullanici deneyimini iyilestirecek; benzersizlik ve
  veri butunlugu otoritesi backend + PostgreSQL olacak.

## Auth Tamamlama Kontrolu

Backend ve Android auth uyarlamasi bittiginde asagidaki akislara birlikte
bakilacak:

- Kayit, e-posta dogrulama ve resend
- Normal giris ve yalniz mevcut hesap icin Google girisi
- Rol secimi ve rol bazli root navigation
- Access token yenileme, refresh rotation/replay ve logout
- Forgot/reset/change password
- Ortak hata envelope, field error ve yerellestirilmis Android mesajlari
- E-posta normalizasyonu ve eszamanli duplicate kayit korumasi
- Local Wi-Fi uzerinde emulator ve fiziksel cihaz baglantisi
- Secret, token, parola ve teknik exception bilgilerinin log/response'a
  sizmamasi

Bu kontroller tamamlandiktan sonra tum uygulamanin kalici domain tablolari,
endpoint'leri, iliskileri, transaction sinirlari ve altyapi servisleri icin
ayri backend planlama asamasina gecilecek.
