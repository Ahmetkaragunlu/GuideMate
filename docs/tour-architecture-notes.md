# Tour Architecture Notes (Guide First)

Bu not, tur yayinlama ve tur karti verisi icin alinmis mimari kararlarin unutulmamasini saglar.

## Su Anki Karar (MVP)

- Uygulama su an `guide` scope'unda ilerleyecek.
- Turist entegrasyonu simdilik yapilmayacak.
- Ancak guide tarafinda kurulan yapi, turist entegrasyonuna uygun olacak.

## Temel Veri Yaklasimi

- `GuideTourPublishDraft`: 1-2-3-4 adim form verisini tutar (gecici/form state).
- `TourItem` benzeri ana model: yayinlanmis tur verisini tutar (kalici/ortak kaynak adayi).
- Publish aninda draft verisi ana modele map edilir.

## Veri Akişi

1. Rehber turu adim adim doldurur (`Draft`).
2. `Yayinla` aksiyonunda `Draft -> TourItem` donusumu yapilir.
3. Olusan tur, guide tarafindaki shared kaynak/listesine eklenir.
4. `Turlarim > Yayinda` kartlari bu ortak kaynaktan okunur.

## Mimari Kurallar

- Ayni tur bilgisi iki yerde manuel tutulmayacak.
- UI ekranlari form state gosterir; ana veri tek kaynakta tutulur.
- UI card modelleri (`GuideTourUiModel`, ileride `PopularTourCardUiModel`) ana modelden map edilir.
- Over-engineering yapilmadan sade ama genisletilebilir yapi korunur.

## Gelecek Asama (Turist Entegrasyonu)

- Turist tarafi eklenince ana tur modeli ve store `common` katmana tasinabilir.
- `guide` publish akisi role-specific oldugu icin guide paketinde kalabilir.
- Turist ekranlari ayni ana kaynaktan farkli mapper ile beslenecek.

## Onemli Not

- Bu kararlar ileride (1 ay sonra da dahil) aynen referans alinacak.
- Turist tarafi eklense bile once guide'da kurulan bu cizgi bozulmayacak; yalnizca ortaklastirma adimi eklenecek.
