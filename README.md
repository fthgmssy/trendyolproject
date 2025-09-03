# Java Selenium Maven Test Otomasyonu

## 📋 Önkoşullar
- **Java JDK** 24.0.2 (11+) (OpenJDK, Homebrew)  
  ```
  openjdk version "24.0.2" 2025-07-15  
  OpenJDK Runtime Environment Homebrew (build 24.0.2)  
  OpenJDK 64-Bit Server VM Homebrew (build 24.0.2, mixed mode, sharing)
  ```
- **Apache Maven** 3.9.11  
  ```
  Apache Maven 3.9.11 (3e54c93a704957b63ee3494413a2b544fd3d825b)  
  Maven home: /usr/local/Cellar/maven/3.9.11/libexec  
  Java version: 14.0.2, vendor: AdoptOpenJDK, runtime: /Users/fatihgumussoy/Library/Java/JavaVirtualMachines/adopt-openjdk-14.0.2/Contents/Home  
  Default locale: en_TR, platform encoding: UTF-8  
  OS name: "mac os x", version: "10.16", arch: "x86_64", family: "mac"
  ```
- **Chrome** veya **Firefox** tarayıcı
- (Opsiyonel) **Allure CLI** rapor görüntülemek için

---

## 🖥️ Sistem Gereksinimleri

| Bileşen     | Versiyon / Bilgi |
|-------------|------------------|
| İşletim Sistemi | macOS 10.16 (x86_64) |
| Java JDK    | OpenJDK 24.0.2 (Homebrew) |
| Maven       | Apache Maven 3.9.11 |
| Java (Maven runtime) | AdoptOpenJDK 14.0.2 |
| Tarayıcı    | Google Chrome / Mozilla Firefox |
| Encoding    | UTF-8 |
| Locale      | en_TR |

---

## 📜 Kurulum ve Çalıştırma Adımları
1. **Java kurulu mu kontrol et**  
   ```bash
   java -version
   ```

2. **Maven kurulu mu kontrol et**  
   ```bash
   mvn -version
   ```

3. **Projeyi klonla**  
   ```bash
   git clone <repo-url>
   cd <proje-klasörü>
   ```

4. **Bağımlılıkları yükle**  
   ```bash
   mvn clean install -DskipTests
   ```

5. **Tarayıcı driver’ını ayarla**  
   - **Selenium Manager** kullanıyorsan ek işlem gerekmez.  
   - Manuel driver kullanıyorsan indir ve PATH’e ekle.

6. **(Opsiyonel) Allure CLI yükle**  
   - **macOS**: `brew install allure`  
   - **Windows**: [Allure Releases](https://github.com/allure-framework/allure2/releases) sayfasından zip indir ve PATH’e ekle.  
   - Kontrol:  
     ```bash
     allure --version
     ```

7. **Testleri çalıştır**  
   ```bash
   mvn clean test
   # veya
   mvn clean test -Dbrowser=chrome -Dheadless=true
   ```

8. **Raporu görüntüle (Allure CLI yüklü ise)**  
   ```bash
   allure serve allure-results
   # veya
   allure generate allure-results -o allure-report --clean
   allure open allure-report
   ```

---

## 📌 Örnek Test Çalıştırma Komutları
- **Varsayılan tüm testler**  
  ```bash
  mvn clean test
  ```

- **Chrome tarayıcıda test çalıştırma**  
  ```bash
  mvn clean test -Dbrowser=chrome
  ```

- **Firefox tarayıcıda test çalıştırma**  
  ```bash
  mvn clean test -Dbrowser=firefox
  ```

- **Headless modda test çalıştırma**  
  ```bash
  mvn clean test -Dbrowser=chrome -Dheadless=true
  ```

- **Belirli TestNG suite çalıştırma**  
  ```bash
  mvn clean test -DsuiteXmlFile=src/test/resources/testng-smoke.xml
  ```

- **Belirli test sınıfını çalıştırma (JUnit)**  
  ```bash
  mvn -Dtest=LoginTests test
  ```

- **Belirli test metodunu çalıştırma (JUnit)**  
  ```bash
  mvn -Dtest=LoginTests#validLogin test
  ```

- **Smoke testi çalıştırma (profil ile)**  
  ```bash
  mvn clean test -Psmoke
  ```

- **Regression testi çalıştırma (profil ile)**  
  ```bash
  mvn clean test -Pregression
  ```

---

## ⚙️ CI/CD Pipeline Örneği (GitHub Actions)

`.github/workflows/tests.yml`
```yaml
name: UI Tests

on:
  push:
    branches: [ "main" ]
  pull_request:
    branches: [ "main" ]

jobs:
  ui-tests:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - name: Set up JDK
        uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: '17'

      - name: Cache Maven
        uses: actions/cache@v4
        with:
          path: ~/.m2/repository
          key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
          restore-keys: ${{ runner.os }}-m2-

      - name: Run tests
        run: mvn -B clean test -Dbrowser=chrome -Dheadless=true

      - name: Upload Allure results
        uses: actions/upload-artifact@v4
        with:
          name: allure-results
          path: allure-results
```

---

## 🐞 Sorun Giderme
- **Allure CLI yok** → `brew install allure` (macOS) veya Windows için PATH’e ekle.
- **Driver hatası** → Selenium Manager veya manuel driver yükle.
- **Tarayıcı açılmıyor** → Tarayıcı kurulu ve versiyonu uyumlu olmalı.


## 🏋️ Performance Testing (k6)

Performance tests are located under the `performance/` folder.

Run the following command to execute the load test:

```bash
k6 run performance/search-load-test.js
```


## 🛠️ CI/CD Pipeline Örneği (Jenkins)

`Jenkinsfile` örneği:

```groovy
pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK17'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean install -DskipTests'
            }
        }

        stage('Run Tests') {
            steps {
                sh 'mvn clean test -Dbrowser=chrome -Dheadless=true'
            }
        }

        stage('Allure Report') {
            steps {
                allure([
                    includeProperties: false,
                    jdk: '',
                    results: [[path: 'allure-results']]
                ])
            }
        }

        stage('Performance Test (k6)') {
            steps {
                sh 'k6 run performance/search-load-test.js'
            }
        }
    }

    post {
        always {
            junit '**/target/surefire-reports/*.xml'
        }
    }
}
```


---

## ⚙️ CI/CD Pipeline Örneği (Jenkins)

Aşağıdaki örnek **Declarative Pipeline** bir Jenkinsfile'ıdır. Maven testlerini **headless Chrome** ile koşar, **Allure** sonuçlarını arşivler ve (plugin yüklüyse) Allure raporunu yayınlar. İsteğe bağlı olarak **k6 performans testini** de bir aşama olarak çalıştırabilirsiniz.

> **Önkoşullar (Jenkins Master/Agent):**
> - JDK ve Maven global tools olarak tanımlı olmalı (Manage Jenkins → Global Tool Configuration).  
> - (Opsiyonel) **Allure Jenkins Plugin** kurulu olmalı.  
> - (Opsiyonel) **k6** ajan üzerinde kurulu ya da Docker ile çalıştırılabilir olmalı.

`Jenkinsfile`:
```groovy
pipeline {
  agent any

  tools {
    // Manage Jenkins > Global Tool Configuration'da tanımlı isimler
    jdk    'temurin-17'
    maven  'Maven 3.9.11'
  }

  options {
    timestamps()
    ansiColor('xterm')
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build & Test (Headless Chrome)') {
      steps {
        sh 'mvn -B clean test -Dbrowser=chrome -Dheadless=true'
      }
      post {
        always {
          // Allure sonuçlarını arşivle (rapor için gereklidir)
          archiveArtifacts artifacts: 'allure-results/**', fingerprint: true, onlyIfSuccessful: false
          junit allowEmptyResults: true, testResults: '**/surefire-reports/*.xml'
        }
      }
    }

    stage('Publish Allure Report') {
      when { expression { return fileExists('allure-results') } }
      steps {
        // Allure Jenkins Plugin kurulu ise:
        allure includeProperties: false, jdk: '', results: [[path: 'allure-results']]
      }
    }

    stage('Performance Test (k6)') {
      when { expression { return fileExists('performance/search-load-test.js') } }
      steps {
        // 1) Ajan üzerinde k6 kuruluysa:
        // sh 'k6 run performance/search-load-test.js'

        // 2) Ya da Docker ile k6 çalıştır:
        sh '''
          docker run --rm -v "$PWD":/work -w /work grafana/k6 \
          run performance/search-load-test.js
        '''
      }
      post {
        always {
          // k6 çıktısı ve summary dosyalarını arşivle (varsa)
          archiveArtifacts artifacts: 'k6-results/**', fingerprint: true, onlyIfSuccessful: false
        }
      }
    }
  }

  post {
    always {
      // Jenkins build sonrasında workspace boyutu vs. temizlik/görünürlük için
      echo "Build finished. Check test reports and artifacts."
    }
  }
}
```

> **Notlar:**
> - `tools` bloktaki isimler, Jenkins'te tanımladığınız global tool isimleriyle aynı olmalıdır.  
> - Allure raporu için plugin kurulu değilse `Publish Allure Report` aşamasını kaldırabilir ve raporu iş sonrası adımda `allure generate` ile HTML’e çevirip `archiveArtifacts` ile arşivleyebilirsiniz.  
> - k6 için Docker seçeneğini kullanıyorsanız, ajan üzerinde Docker engine erişimi olmalı.
