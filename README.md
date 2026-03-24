# Service REST Spring Boot 
Service REST basé sur **Spring Boot** permettant de gérer et publier des flux XML au format **ISO 20022 (SEPA)**. Le projet implémente les opérations CRUD via les méthodes HTTP (GET, POST) et expose des données structurées en XML.
 
---
 
## Technologies utilisées
 
| Technologie | Version |
|---|---|
| Java | 21 |
| Spring Boot | 4.0.3 |
| Maven | 3.9.x |
| Jakarta XML Bind (JAXB) | - |
| Docker | - |
| Jenkins | - |
 
---
 
## Structure du projet
 
```
sepa26/
├── src/
│   ├── main/
│   │   ├── java/fr/univrouen/sepa26/
│   │   │   ├── controllers/
│   │   │   │   ├── IndexController.java       # GET /
│   │   │   │   ├── GetController.java         # GET /resume, /guid, /xml, /sepa26
│   │   │   │   └── PostController.java        # POST /testpost, /testload
│   │   │   ├── model/
│   │   │   │   ├── Amount.java
│   │   │   │   ├── CstmrDrctDbtInitn.java     # Classe racine SEPA
│   │   │   │   ├── DirectDebitTransactionInfo.java
│   │   │   │   ├── GroupHeader.java
│   │   │   │   ├── NamedElement.java
│   │   │   │   ├── PaymentInformation.java
│   │   │   │   ├── Sepa26.java
│   │   │   │   └── TestSepa26.java
│   │   │   └── Application.java
│   │   └── resources/
│   │       ├── static/
│   │       │   └── favicon.ico
│   │       ├── xml/
│   │       │   └── testsepa.xml
│   │       └── application.properties
│   └── test/
├── Dockerfile
├── pom.xml
└── README.md
```
 
---
 
##  Prérequis
 
- **Java 21** — [Télécharger OpenJDK 21](https://jdk.java.net/archive)
- **Maven 3.9+** — [Télécharger Maven](https://maven.apache.org/download.cgi)
- **Docker** (pour le déploiement)
 
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
git clone 
cd sepa26
```
 
### 2. Compiler le projet
 
```bash
mvn install
```
 
### 3. Lancer le serveur
 
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
 
### POST
 
| URL | Description | Content-Type |
|---|---|---|
| `POST /testpost` | Reçoit un flux XML SEPA | `application/xml` |
| `POST /testload` | Charge et retourne le fichier `testsepa.xml` | `application/xml` |
 
---
 
##  Tester avec Bruno
 
Installer **Bruno** : [https://www.usebruno.com](https://www.usebruno.com)
 
### Exemple de requête POST
 
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
 
---
 
##  Format XML ISO 20022 (SEPA)
 
Le service respecte la norme **ISO 20022** pour les prélèvements SEPA. Structure d'un message :
 
```xml
<?xml version="1.0" encoding="UTF-8"?>
<Document xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://univ.fr/sepa tp1.sepa.01.xsd">
  <CstmrDrctDbtInitn>
    <GrpHdr>
      <MsgId>MSGID-123456</MsgId>
      <CreDtTm>2026-03-24T14:25:00</CreDtTm>
      <NbOfTxs>1</NbOfTxs>
      <CtrlSum>1100.07</CtrlSum>
      <InitgPty><Nm>Societe XX</Nm></InitgPty>
    </GrpHdr>
    <PmtInf>
      <PmtInfId>REF Remise 123</PmtInfId>
      <DrctDbtTxInf>
        <PmtId>REF OPE AAAA</PmtId>
        <InstdAmt Ccy="EUR">1100.07</InstdAmt>
        <Dbtr><Nm>Mr Debiteur N1</Nm></Dbtr>
      </DrctDbtTxInf>
    </PmtInf>
  </CstmrDrctDbtInitn>
</Document>
```
 
---
 
##  Déploiement avec Docker
 
### Construire l'image
 
```bash
mvn install
docker build -t sepa26-server .
```
 
### Lancer le conteneur
 
```bash
docker run -p 8100:8100 sepa26-server
```
 
### Vérifier le conteneur
 
```bash
docker ps -a
```
 
Le service est accessible sur **http://<IP_VM>:8100**
 
---
 
##  Configuration
 
### `application.properties`
 
```properties
server.port=8100
logging.level.org.springframework.web=DEBUG
```
 
### `pom.xml` — Dépendances principales
 
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
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
 