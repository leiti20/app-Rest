# Documentation du Déploiement Docker - Projet `sepa26`

Ce document décrit l'architecture de déploiement et la configuration de l'application Spring Boot avec MongoDB via Docker et Jenkins.

## Architecture de Déploiement

Le projet utilise une approche de conteneurisation robuste pour garantir des builds reproductibles et une configuration flexible.

### 1. Multi-Stage Dockerfile (Build & Runtime)
Le processus de build a été déplacé à l'intérieur de Docker pour éviter les dépendances externes et les builds obsolètes.
- **Stage 1 (Build)** : Utilise un environnement Maven pour compiler le code source (`src/`) et générer le fichier `.war`.
- **Stage 2 (Run)** : Image JRE légère qui récupère uniquement l'artéfact construit pour l'exécution.
  - *Avantage* : L'image finale est plus légère et contient systématiquement la dernière version du code source.

### 2. Orchestration Docker Compose
Le fichier `docker-compose.yml` gère deux services principaux :
- **mongodb** : Base de données NoSQL stockant les données SEPA.
- **sepa26** : L'application Spring Boot, configurée avec une dépendance sur le service `mongodb`.

## Configuration de la Base de Données

La connexion à MongoDB est configurée avec une **triple redondance** pour éviter tout conflit de configuration :

| Niveau | Méthode | Détail |
| :--- | :--- | :--- |
| **Primaire** | Variables d'environnement | `SPRING_DATA_MONGODB_URI` injectée par Docker Compose. |
| **Secondaire** | Profil Spring Active | Profil `docker` forcé au démarrage (`-Dspring.profiles.active=docker`). |
| **Tertiaire** | Fallback Properties | URI par défaut dans `application.properties`. |

### URI de Connexion
L'URI standard de connexion est : `mongodb://mongodb:27017/sepa26db`

## Maintenance et Mise à jour

Pour mettre à jour l'application après une modification du code source, utilisez les commandes suivantes sur le serveur :

```bash
# Reconstruction complète sans cache pour garantir la prise en compte des changements
docker-compose build --no-cache
docker-compose up -d
```

## Intégration Continue (Jenkins)
Le pipeline Jenkins automatisé réalise les étapes suivantes à chaque push :
1. **Checkout** du code.
2. **Build & Test** via Maven (`./mvnw test`).
3. **Mise à jour Docker** avec reconstruction d'image (`docker-compose up -d --build`).
4. **Health Check** pour vérifier que les services sont opérationnels.
