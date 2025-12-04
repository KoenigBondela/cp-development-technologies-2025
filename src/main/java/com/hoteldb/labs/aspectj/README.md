# Лабораторная работа №7: AspectJ

## Важно!

Для правильной работы аспектов проект **должен компилироваться через Maven**, так как AspectJ требует специальной компиляции (weaving).

## Запуск через Maven

### Компиляция проекта:
```bash
mvn clean compile
```

### Запуск MainClass:
```bash
mvn exec:java -Dexec.mainClass="com.hoteldb.labs.aspectj.MainClass"
```

## Запуск через IntelliJ IDEA

### 🚀 Быстрый способ (Рекомендуется)

**Шаг 1:** Откройте встроенный терминал IntelliJ IDEA:
- Нажмите `Alt + F12` (Windows/Linux) или `Option + F12` (Mac)
- Или: View → Tool Windows → Terminal

**Шаг 2:** Выполните команды:
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.hoteldb.labs.aspectj.MainClass"
```

Готово! Вы увидите работу всех аспектов.

---

### Вариант 1: Через Maven панель

1. **Откройте Maven панель:**
   - В правой части экрана найдите вкладку "Maven" 
   - Или: View → Tool Windows → Maven
   - Или нажмите: `Alt + 1` (Windows/Linux) / `Cmd + 1` (Mac)

2. **Скомпилируйте проект:**
   - Разверните: `hotel-db-labs` → `Lifecycle`
   - Двойной клик на `clean`, затем двойной клик на `compile`
   - Это применит аспекты к классам

3. **Запустите через Maven:**
   - Разверните: `hotel-db-labs` → `Plugins` → `exec`
   - Двойной клик на `exec:java` (или правой кнопкой → Run)
   - Если не видите `exec`, используйте терминал (см. выше)

### Вариант 2: Через конфигурацию запуска (после Maven компиляции)

1. **Сначала скомпилируйте через Maven** (см. шаги 1-3 выше)

2. **Создайте конфигурацию запуска:**
   - Кликните правой кнопкой на `MainClass.java`
   - Выберите `Run 'MainClass.main()'`
   - Или создайте конфигурацию вручную: `Run` → `Edit Configurations...` → `+` → `Application`
   - Main class: `com.hoteldb.labs.aspectj.MainClass`
   - **Важно:** Убедитесь, что используется класс из `target/classes`, а не IDE компиляция

3. **Настройте компиляцию:**
   - File → Settings → Build, Execution, Deployment → Compiler
   - Или отключите авто-компиляцию для этого проекта
   - Лучше использовать: `Build` → `Rebuild Project` после Maven компиляции

### Вариант 3: Через встроенный терминал IntelliJ IDEA

1. Откройте встроенный терминал: `Alt + F12` (Windows/Linux) или `Option + F12` (Mac)
2. Выполните:
   ```bash
   mvn clean compile
   mvn exec:java -Dexec.mainClass="com.hoteldb.labs.aspectj.MainClass"
   ```

### Важно!

⚠️ **Если аспекты не работают:**
- Убедитесь, что выполнили `mvn clean compile` перед запуском
- Проверьте, что классы берутся из `target/classes`, а не перекомпилируются IDE
- В конфигурации запуска проверьте Classpath — должен включать `aspectjrt`
- При необходимости перезагрузите Maven проект: правой кнопкой на `pom.xml` → `Maven` → `Reload project`

## Требуемый вывод

При правильной работе аспектов вывод должен быть примерно таким:

```
Good day!
How do you do?
Nice to meet you!
Entering method without say execution(void HelloWorld.setName(String)) Timestamp:...
Something was getted. execution(void HelloWorld.setName(String)) Timestamp:...
Leaving method without say execution(void HelloWorld.setName(String)) Timestamp:...
Without say method is called
Entering method without say execution(void HelloWorld.setFamilyName(String)) Timestamp:...
Something was getted. execution(void HelloWorld.setFamilyName(String)) Timestamp:...
Leaving method without say execution(void HelloWorld.setFamilyName(String)) Timestamp:...
Without say method is called
Entering method without say execution(String HelloWorld.getName()) Timestamp:...
Something was getted. execution(String HelloWorld.getName()) Timestamp:...
Leaving method without say execution(String HelloWorld.getName()) Timestamp:...
Without say method is called
Good day!
John Jackson-san, how do you do?
Nice to meet you!
```

