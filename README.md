# 🧩 Microservice - Gestion des Absences & Congés

Ce dépôt contient le **microservice de gestion des absences et des congés** du personnel universitaire, développé dans le cadre de mon stage de Master 2 à l’Université Assane Seck de Ziguinchor.

---

## 🎯 Objectif

Ce microservice permet :
- L’enregistrement des absences du personnel
- La gestion des congés administratifs et maternité
- L’ajout d’arrêtés en pièces jointes (PDF)
- Le lien avec les affectations organisationnelles du personnel

---

## 🏗️ Architecture

Ce projet s'inscrit dans une architecture **microservices**.  
Ce service dépend de deux autres services :

| Microservice            | Rôle                                 | Port par défaut |
|-------------------------|--------------------------------------|-----------------|
| Personnel Service       | Données des agents (matricule, etc.) | 8081            |
| Organisation Service    | Données des affectations             | 8082            |

Les communications se font via **RestTemplate** ou API REST.

---

## 🛠️ Technologies utilisées

- Java 17
- Spring Boot
- Spring Data JPA (avec PostgreSQL)
- Lombok
- Maven
- Git & GitHub

---

## ⚙️ Configuration

Dans le fichier `src/main/resources/application.properties`, configure :

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/gestion_conge
spring.datasource.username=postgres
spring.datasource.password=your_password
```

---

## ▶️ Démarrage

```bash
./mvnw spring-boot:run
```

> ⚠️ Les microservices **Personnel** et **Organisation** doivent aussi être lancés pour que tout fonctionne.

---



## 🌐 API REST principales

| Méthode | Endpoint                  | Description                         |
|---------|---------------------------|-------------------------------------|
| GET     | /api/absences             | Lister toutes les absences          |
| POST    | /api/absences             | Ajouter une nouvelle absence        |
| GET     | /api/conges               | Lister les congés                   |
| POST    | /api/conges               | Ajouter un congé administratif      |
| GET     | /api/personnels/{matricule} | Récupérer un personnel par matricule |

> La documentation Swagger peut être ajoutée pour visualiser les endpoints.

---

## 📌 Remarques

- Les fichiers des arrêtés sont stockés dans le dossier `/uploads`
- Ce projet a été déployé sur le serveur de l’université durant le stage
- Cette version est personnelle et simplifiée pour présentation

---

## 👨🏾‍💻 Auteur

**Abou Diagne Gaye**  
Master 2 Informatique – Génie Logiciel  
Université Assane Seck de Ziguinchor (UASZ)  
📍 Ziguinchor, Sénégal  
📧gayeaboudiagne@gmail.com  
🔗 [Mon GitHub](https://github.com/AbouDG)
