# Biblioteka — szybka instrukcja

Wymagania:
- JDK 21
- Maven 3

Kompilacja:
```bash
mvn -DskipTests package
```

Uruchomienie (zalecane):
```bash
mvn -Dexec.mainClass=pl.edu.wszib.library.App exec:java
```

Domyślni użytkownicy:
- admin / admin (ADMIN)
- user / user (USER)

Plik bazy: `biblioteka-db.mv.db` w katalogu projektu
