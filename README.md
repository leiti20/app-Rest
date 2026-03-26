# sepa26 - Service REST Spring Boot
Service REST basé sur **Spring Boot** permettant de gérer et publier des flux XML au format **ISO 20022 (SEPA)**. Le projet implémente les opérations CRUD via les méthodes HTTP (GET, POST, DELETE) et expose des données structurées en XML. Les transactions SEPA sont persistées dans une base de données **MongoDB**.

---

##  Technologies utilisées

| Technologie | Version |
|---|---|
| Java | 21 |
| Spring Boot | 4.0.3 |
| Maven | 3.9.x |
| Jakarta XML Bind (JAXB) | - |
| MongoDB | 7 |
| Docker | - |
| Docker Compose | - |
| Jenkins | 2.541.3 |

---

##  Structure du projet

```
sepa26/
├── src/
│   ├── main/
│   │   ├── java/fr/univrouen/sepa26/
│   │   │   ├── controllers/
│   │   │   │   ├── IndexController.java         # GET /
│   │   │   │   ├── GetController.java           # GET /resume, /guid, /xml, /sepa26
│   │   │   │   ├── PostController.java          # POST /testpost, /testload
│   │   │   │   ├── TransactionController.java   # CRUD MongoDB /transactions
│   │   │   │   └── CustomErrorController.java   # Gestion des erreurs personnalisée
│   │   │   ├── model/
│   │   │   │   ├── Amount.java
│   │   │   │   ├── AccountId.java
│   │   │   │   ├── AgentId.java
│   │   │   │   ├── BICElement.java
│   │   │   │   ├── CstmrDrctDbtInitn.java       # Classe racine SEPA
│   │   │   │   ├── DirectDebitTransactionInfo.java
│   │   │   │   ├── GroupHeader.java
│   │   │   │   ├── IBANElement.java
│   │   │   │   ├── MandateInfo.java
│   │   │   │   ├── MandateRelatedInfo.java
│   │   │   │   ├── NamedElement.java
│   │   │   │   ├── PaymentInformation.java
│   │   │   │   ├── SepaTransaction.java         # Modèle MongoDB
│   │   │   │   ├── Sepa26.java
│   │   │   │   ├── TestSepa26.java
│   │   │   │   └── package-info.java            # Namespace XML
│   │   │   ├── repository/
│   │   │   │   └── SepaTransactionRepository.java  # Repository MongoDB
│   │   │   └── Application.java
│   │   └── resources/
│   │       ├── static/
│   │       │   └── favicon.ico
│   │       ├── xml/
│   │       │   └── testsepa.xml
│   │       └── application.properties
│   └── test/
│       └── java/fr/univrouen/sepa26/
│           ├── ApplicationTests.java
│           └── GetControllerTest.java
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

##  Prérequis

- **Java 21** — [Télécharger OpenJDK 21](https://jdk.java.net/archive)
- **Maven 3.9+** — [Télécharger Maven](https://maven.apache.org/download.cgi)
- **Docker** + **Docker Compose** (pour le déploiement)

### Variables d'environnement (Windows)

```bash
JAVA_HOME=C:\Program Files\Java\jdk-21.0.2
MAVEN_HOME=C:\Program Files\apache-maven-3.9.x
# Ajouter au PATH : %JAVA_HOME%\bin et %MAVEN_HOME%\bin
```

---

##  Installation et lancement

### 1. Cloner le projet

```bash
git clone <url-du-depot>
cd sepa26
```

### 2. Lancer MongoDB avec Docker Compose

```bash
docker-compose up -d mongodb
```

### 3. Compiler le projet

```bash
mvn install
```

### 4. Lancer le serveur

```bash
mvn spring-boot:run
```

Le service démarre sur **http://localhost:8100**

---

##  Routes disponibles

### GET

| URL | Description |
|---|---|
| `GET /` | Page d'accueil — `Hello sepa26 !` |
| `GET /resume` | Liste des flux SEPA enregistrés |
| `GET /guid?guid=<valeur>` | Détail d'une transaction SEPA |
| `GET /test?nb=<n>&search=<s>` | Test avec paramètres |
| `GET /xml` | Retourne un objet Sepa26 en XML |
| `GET /sepa26` | Retourne un message complet ISO 20022 en XML |
| `GET /transactions/all` | Liste toutes les transactions MongoDB |
| `GET /transactions/{id}` | Récupère une transaction par ID |

### POST

| URL | Description | Content-Type |
|---|---|---|
| `POST /testpost` | Reçoit un flux XML SEPA | `application/xml` |
| `POST /testload` | Charge et retourne `testsepa.xml` | `application/xml` |
| `POST /transactions/save` | Sauvegarde une transaction en MongoDB | `application/json` |

### DELETE

| URL | Description |
|---|---|
| `DELETE /transactions/{id}` | Supprime une transaction par ID |

---

##  Tester avec Bruno

Installer **Bruno** : [https://www.usebruno.com](https://www.usebruno.com)

### Exemple — Requête POST XML

```
URL     : http://localhost:8100/testpost
Méthode : POST
Headers : Content-Type: application/xml
          Accept: application/xml
Body    :
<DrctDbtTxInf>
    <PmtId>REF OPE A123B</PmtId>
    <InstdAmt Ccy="EUR">1100.07</InstdAmt>
</DrctDbtTxInf>
```

### Exemple — Sauvegarder une transaction MongoDB

```
URL     : http://localhost:8100/transactions/save
Méthode : POST
Headers : Content-Type: application/json
Body    :
{
    "pmtId": "REF OPE AAAA",
    "amount": 1100.07,
    "currency": "EUR",
    "debtorName": "Mr Debiteur N1",
    "debtorIban": "FR763004136210001234567811",
    "creditorName": "Societe XX",
    "creditorIban": "FR7610041010050500013M02606",
    "creditorBic": "BANKFRPP",
    "remittanceInfo": "Facture N1"
}
```

---

##  Format XML ISO 20022 (SEPA)

Le service respecte la norme **ISO 20022** pour les prélèvements SEPA :

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Document xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xmlns="http://univ.fr/sepa">
  <CstmrDrctDbtInitn>
    <GrpHdr>
      <MsgId>MSGID-123456</MsgId>
      <CreDtTm>2026-03-26T14:25:00</CreDtTm>
      <NbOfTxs>1</NbOfTxs>
      <CtrlSum>1100.07</CtrlSum>
      <InitgPty><Nm>Societe XX</Nm></InitgPty>
    </GrpHdr>
    <PmtInf>
      <PmtInfId>REF Remise 123</PmtInfId>
      <Cdtr><Nm>Societe XX</Nm></Cdtr>
      <CdtrAcct><Id><IBAN>FR7610041010050500013M02606</IBAN></Id></CdtrAcct>
      <CdtrAgt><FinInstnId><BIC>BANKFRPP</BIC></FinInstnId></CdtrAgt>
      <DrctDbtTxInf>
        <PmtId>REF OPE AAAA</PmtId>
        <InstdAmt Ccy="EUR">1100.07</InstdAmt>
        <DrctDbtTx>
          <MndtRltdInf>
            <MndtId>MANDAT NO 55555</MndtId>
            <DtOfSgntr>2009-09-01</DtOfSgntr>
          </MndtRltdInf>
        </DrctDbtTx>
        <Dbtr><Nm>Mr Debiteur N1</Nm></Dbtr>
        <DbtrAcct><Id><IBAN>FR763004136210001234567811</IBAN></Id></DbtrAcct>
        <RmtInf>Facture N1</RmtInf>
      </DrctDbtTxInf>
    </PmtInf>
  </CstmrDrctDbtInitn>
</Document>
```

---

##  Déploiement avec Docker

### Lancer MongoDB + Service Spring avec Docker Compose

```bash
docker-compose up -d
```

### Ou manuellement

```bash
# Lancer MongoDB
docker-compose up -d mongodb

# Construire l'image Spring
mvn install
docker build -t sepa26-server .

# Lancer le service
docker run -p 8100:8100 sepa26-server
```

### Vérifier les conteneurs

```bash
docker ps -a
```

Le service est accessible sur **http://\<IP_VM\>:8100**

---

##  Intégration Continue avec Jenkins

Le projet est entièrement automatisé avec Jenkins :

**Job 1 — `construction maven`**
- Se déclenche automatiquement à chaque push git
- Compile le projet avec `mvn package`
- Déclenche automatiquement le Job 2 en cas de succès

**Job 2 — `service docker`**
- Rebuild l'image Docker
- Supprime l'ancien conteneur
- Relance le service dans un nouveau conteneur

**Accès Jenkins :** `http://<IP_VM>:8080`

---

##  Persistance des données — MongoDB

Les transactions SEPA sont stockées dans **MongoDB** avec persistance garantie via un volume Docker.

### Configuration `application.properties`

```properties
server.port=8100
server.error.path=/erreur
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=sepa26db
spring.autoconfigure.exclude=org.springframework.boot.webmvc.autoconfigure.error.ErrorMvcAutoConfiguration
```

### `docker-compose.yml`

```yaml
version: '3.8'
services:
  mongodb:
    image: mongo:7
    container_name: sepa26-mongodb
    ports:
      - "27017:27017"
    volumes:
      - mongo-data:/data/db

  sepa26:
    image: sepa26-server
    container_name: servicesepa26
    ports:
      - "8100:8100"
    depends_on:
      - mongodb
    environment:
      SPRING_DATA_MONGODB_HOST: mongodb
      SPRING_DATA_MONGODB_PORT: 27017
      SPRING_DATA_MONGODB_DATABASE: sepa26db

volumes:
  mongo-data:
```

---

##  Dépendances principales `pom.xml`

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
<dependency>
    <groupId>jakarta.xml.bind</groupId>
    <artifactId>jakarta.xml.bind-api</artifactId>
</dependency>
<dependency>
    <groupId>org.glassfish.jaxb</groupId>
    <artifactId>jaxb-runtime</artifactId>
</dependency>
```

---

##  Annotations utilisées

### Spring
| Annotation | Rôle |
|---|---|
| `@RestController` | Contrôleur REST — retourne directement des données |
| `@GetMapping` | Mappe les requêtes HTTP GET |
| `@RequestMapping` | Mappe toutes les méthodes HTTP |
| `@ResponseBody` | Sérialise la valeur retournée dans la réponse |
| `@RequestBody` | Désérialise le corps de la requête |

### XML (JAXB)
| Annotation | Rôle |
|---|---|
| `@XmlRootElement` | Définit la balise XML racine |
| `@XmlElement` | Mappe un champ vers une balise XML enfant |
| `@XmlAttribute` | Mappe un champ vers un attribut XML |
| `@XmlAccessorType` | Définit le mode d'accès JAXB |
| `@XmlSchema` | Définit le namespace XML |

### MongoDB
| Annotation | Rôle |
|---|---|
| `@Document` | Mappe la classe vers une collection MongoDB |
| `@Id` | Définit l'identifiant unique du document |

---

##  Tests JUnit

```bash
mvn test
```
