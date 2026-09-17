# Security

## Проверки безопасности

Проект использует:

- OWASP Dependency-Check - поиск известных уязвимостей зависимостей
- NVD - источник CVE данных
- CycloneDX - создание SBOM

## Запуск

```bash
mvn verify -Psecurity-enforce
```

## Результаты

После проверки отчеты находятся в каталоге target:

- dependency-check-report.html
- dependency-check-report.json
- bom.xml
- bom.json
