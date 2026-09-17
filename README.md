# Library Management

## О проекте

Учебный Java проект для управления библиотекой.

Проект демонстрирует:

- структуру Maven приложения
    
- работу с Java 21
    
- модульное и интеграционное тестирование
    
- контроль качества кода
    
- проверку зависимостей на известные уязвимости
    
- генерацию Software Bill Of Materials (SBOM)
    

Основная цель проекта - показать прозрачный процесс разработки: от компиляции и тестирования до анализа безопасности зависимостей.

---

## Технологии

- Java 21
    
- Maven 3.9.6+
    
- JUnit 5
    
- JaCoCo
    
- Checkstyle
    
- OWASP Dependency-Check
    
- CycloneDX SBOM
    

---

## Требования

Перед запуском необходимо установить:

```bash
java -version
```

Ожидается:

```
Java 21
```

Проверка Maven:

```bash
mvn -version
```

Ожидается:

```
Apache Maven 3.9.x
Java version: 21
```

---

# Быстрый запуск

Полная стандартная проверка проекта:

```bash
mvn clean verify
```

Команда выполняет:

```
clean
 |
compile
 |
test
 |
package
 |
integration-test
 |
reports
```

Результат:

```
BUILD SUCCESS
```

---

# Структура проекта

```
library-management

├── src
│   ├── main
│   │   └── java
│   │       └── application source code
│   │
│   └── test
│       └── automated tests
│
├── config
│   ├── checkstyle.xml
│   └── suppressions.xml
│
├── pom.xml
│
├── README.md
├── SECURITY.md
└── VERSION
```

---

# Тестирование

## Unit tests

Запуск:

```bash
mvn test
```

Проверяет бизнес-логику отдельных компонентов.

Пример результата:

```
Tests run: 61
Failures: 0
Errors: 0
```

---

## Integration tests

Интеграционные тесты запускаются автоматически через:

```bash
mvn verify
```

Они проверяют взаимодействие нескольких компонентов системы.

---

# Проверка качества кода

## Checkstyle

Проверка стиля кода:

```bash
mvn verify
```

Checkstyle выполняется как часть Maven lifecycle.

Проверяет:

- форматирование
    
- структуру кода
    
- нарушения правил Java Style Guide
    

---

# Покрытие кода

Инструмент:

JaCoCo

Создает отчет покрытия тестами.

После сборки:

```
target/site/jacoco/index.html
```

Отчет показывает:

- какие классы покрыты тестами
    
- какие строки выполнялись
    
- какие участки кода не проверяются
    

---

# SBOM - Software Bill Of Materials

Инструмент:

CycloneDX

Генерация:

```bash
mvn org.cyclonedx:cyclonedx-maven-plugin:makeBom
```

Создаются файлы:

```
target/bom.xml
target/bom.json
```

SBOM содержит список компонентов проекта:

- библиотеки
    
- версии
    
- зависимости
    

Используется для контроля состава программного обеспечения.

---

# Security scanning

## OWASP Dependency-Check

Запуск проверки:

```bash
mvn org.owasp:dependency-check-maven:check
```

Что делает:

1. Анализирует зависимости проекта из `pom.xml`
    
2. Определяет версии библиотек
    
3. Проверяет наличие известных уязвимостей CVE
    
4. Формирует отчет
    

Результат:

```
target/dependency-check-report.html
```

---

## NVD

OWASP Dependency-Check использует данные:

National Vulnerability Database (NVD)

Процесс:

```
Project dependencies

        |

Dependency-Check

        |

NVD vulnerability database

        |

CVE analysis

        |

Security report
```

Если библиотека имеет известную уязвимость(наш проект имеет такую уязвимость - исключительно для демонстрации), проверка может завершить сборку с ошибкой.

---

# Security profile

Для строгой проверки безопасности(данная проверка как раз определит в результате уязвимость):

```bash
mvn clean verify -Psecurity-enforce
```

Этот режим дополнительно контролирует:

- известные уязвимости зависимостей
    
- установленный порог CVSS
    
- требования безопасности проекта
    

Пример:

```
CVE detected

CVSS >= configured threshold

BUILD FAILURE
```

Это ожидаемое поведение: сборка блокируется при обнаружении критичных проблем.

---

# Почему security проверки могут занимать больше времени

OWASP Dependency-Check зависит от базы уязвимостей NVD.

При запуске происходит:

```
Dependency-Check

        |

Local vulnerability database

        |

NVD updates

        |

Dependency analysis

        |

Report generation
```

Возможны задержки из-за:

- обновления базы NVD
    
- сетевого соединения
    
- большого количества зависимостей
    
- первого запуска после установки
    

---

# Генерируемые файлы

После выполнения Maven появляются:

```
target/
```

Внутри:

- compiled classes
    
- test reports
    
- JaCoCo report
    
- Dependency-Check report
    
- SBOM files
    
- build artifacts
    

Каталог `target` не является частью исходного проекта.

---
# Build artifact - результат сборки

Создание результата сборки:

```bash
mvn package
```

После выполнения появляется:

```
target/library-management-1.2.6.jar
```

Важно понимать разницу между типами JAR.

## Library JAR

Library JAR содержит Java классы, которые могут использоваться другим проектом.

Обычно он:

- содержит скомпилированные классы
    
- может содержать метаданные проекта
    
- не имеет точки запуска приложения
    

Пример:

```
library-management-1.2.6.jar
```

Такой файл нельзя запускать напрямую:

```bash
java -jar library-management-1.2.6.jar
```

если в нем нет `Main-Class`.

---

## Executable JAR

Executable JAR - самостоятельное приложение.

Обычно содержит:

- `Main-Class`
    
- необходимые зависимости
    
- настройки запуска
    

Запуск:

```bash
java -jar application.jar
```

---

В данном проекте создается именно:

```
library-management-1.2.6.jar
```

Это Library JAR.

Такой результат является нормальным для учебного проекта, который демонстрирует структуру Java приложения, Maven lifecycle, тестирование и security проверки.

---

# Основные команды

|Назначение|Команда|
|---|---|
|Очистка проекта|`mvn clean`|
|Компиляция|`mvn compile`|
|Unit tests|`mvn test`|
|Полная проверка|`mvn clean verify`|
|SBOM|`mvn org.cyclonedx:cyclonedx-maven-plugin:makeBom`|
|Dependency scan|`mvn org.owasp:dependency-check-maven:check`|
|Security pipeline|`mvn clean verify -Psecurity-enforce`|

---


---

# Принцип проекта

Проект построен вокруг идеи:

```
Code

 |

Build

 |

Test

 |

Quality checks

 |

Security analysis

 |

Release artifact
```

Каждый этап выполняется стандартными Maven командами без скрытой автоматизации.
