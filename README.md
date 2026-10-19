# Evaluation Hibernate / JPA

Ce dépôt regroupe trois applications Java qui mettent en pratique le mapping objet-relationnel avec **Hibernate 5.6 (JPA)**, **MySQL 8** et **Maven**. Chaque application suit la même architecture en couches : entités, interface `IDao`, services (HQL, requêtes nommées, API Criteria) et une classe `Test` qui insère un jeu de données puis affiche les résultats dans la console.

| Exercice | Dossier | Sujet | Notions principales |
|---|---|---|---|
| 1 | `gestion-stock` | Catégories, produits, commandes | `@ManyToOne`, `@OneToMany`, classe d'association avec clé composite |
| 2 | `gestion-projets` | Projets, tâches, employés | Composition, classe d'association, requête nommée |
| 3 | `gestion-etat-civil` | Personnes et mariages | Héritage `JOINED`, API Criteria, requêtes natives nommées |

## Prérequis

- Java 11 ou plus, Maven, DBeaver
- MySQL local (exercice 1) ou Docker (exercices 2 et 3)

> Les exercices 2 et 3 utilisent tous deux le port 3307. N'en lancer qu'un à la fois (`docker compose down` avant de passer à l'autre).

---

## Exercice 1 : Gestion de stock

### Description

Application de gestion d'un stock de produits regroupés par catégorie. Les produits sont commandés par lots : une commande contient plusieurs produits, et un produit peut apparaître dans plusieurs commandes, avec une quantité propre à chaque ligne.

### Modèle de données

- `Categorie` (id, code, libelle) : une catégorie regroupe plusieurs produits.
- `Produit` (id, reference, prix) : appartient à une seule catégorie.
- `Commande` (id, date).
- `LigneCommandeProduit` (quantite) : classe d'association entre `Produit` et `Commande`, identifiée par une clé composite `LigneCommandeProduitPK` (produit + commande).


### Base de données

MySQL local sur le port **3306**, utilisateur `root` sans mot de passe. La base `gestion_stock` et ses tables sont créées automatiquement par Hibernate (`hbm2ddl.auto=update`).

### Fonctionnalités (`ProduitService`)

| Méthode | Rôle |
|---|---|
| `afficherProduitsParCategorie(Categorie)` | Liste les produits d'une catégorie |
| `afficherProduitsEntreDates(Date, Date)` | Liste les produits commandés pendant une période, sans doublons |
| `afficherProduitsParCommande(int)` | Affiche pour une commande la référence, le prix et la quantité de chaque produit |
| `getPrixSuperieurA100()` | Produits à plus de 100 DH, via la requête nommée `Produit.findByPrixSuperieur` |

### Lancer

Exécuter la classe `ma.projet.test.Test` depuis l'IDE. Elle crée 2 catégories, 4 produits, 2 commandes et 4 lignes de commande, puis lance les 4 requêtes ci-dessus.

### Captures

**Diagramme de classes**

<img width="857" height="477" alt="image" src="https://github.com/user-attachments/assets/837d2340-6a45-4074-a927-3dfbee7edfc0" />

**Tables dans DBeaver** (diagramme ER avec les clés étrangères et la clé composite)

<img width="668" height="797" alt="Capture d&#39;écran 2026-10-19 225416" src="https://github.com/user-attachments/assets/eaa96dfc-91fe-41dd-9a59-6f68b5c500cd" />
<img width="1101" height="696" alt="Capture d&#39;écran 2026-10-19 225434" src="https://github.com/user-attachments/assets/531a2258-c4dd-4f95-a87a-caa3338e81f9" />
<img width="1053" height="705" alt="Capture d&#39;écran 2026-10-19 225442" src="https://github.com/user-attachments/assets/558fddda-1a99-4b41-8d1d-9a46d1b9596d" />
<img width="991" height="704" alt="Capture d&#39;écran 2026-10-19 225451" src="https://github.com/user-attachments/assets/88c1ef1e-d424-44e0-95d9-b7aeea190a6b" />
<img width="1027" height="693" alt="Capture d&#39;écran 2026-10-19 225501" src="https://github.com/user-attachments/assets/e30571b3-abd8-4a30-b7d1-74d8a80f5b91" />
<img width="1514" height="597" alt="Capture d&#39;écran 2026-10-19 225012" src="https://github.com/user-attachments/assets/63c0881a-89d8-4422-a251-a3708c15aa2f" />

**Résultats dans la console** (les 4 requêtes du `Test`)

<img width="1770" height="843" alt="Capture d&#39;écran 2026-10-19 225327" src="https://github.com/user-attachments/assets/b1af7be6-e7a2-4c6d-af3d-2ed62197a548" />

---


## Exercice 2 : Gestion de projets

### Description

Application de suivi des projets d'une entreprise : planification des tâches, temps réellement passé par les employés et coût des tâches. Chaque projet a un chef de projet et se décompose en tâches. Les dates prévues sont portées par la tâche, les dates réelles par l'affectation d'un employé à cette tâche.

### Modèle de données

- `Employe` (id, nom, prenom, telephone) : peut être chef de plusieurs projets.
- `Projet` (id, nom, dateDebut, dateFin) : un chef de projet (`Employe`) et plusieurs tâches.
- `Tache` (id, nom, dateDebut, dateFin prévues, prix) : appartient à un seul projet (composition).
- `EmployeTache` (dateDebutReelle, dateFinReelle) : classe d'association entre `Employe` et `Tache`, avec clé composite `EmployeTachePK`.

### Base de données

MySQL 8 dans Docker (conteneur `mysql-projets`), port **3307**, utilisateur `root` / mot de passe `root`, base `gestion_projets`. Pour DBeaver, ajouter la propriété de driver `allowPublicKeyRetrieval=true`.

### Fonctionnalités

| Service | Méthode | Rôle |
|---|---|---|
| `EmployeService` | `afficherTachesRealisees(int)` | Tâches réalisées par un employé |
| `EmployeService` | `afficherProjetsGeres(int)` | Projets gérés par un employé |
| `ProjetService` | `afficherTachesPlanifiees(int)` | Tâches planifiées d'un projet |
| `ProjetService` | `afficherTachesRealisees(int)` | Tâches réalisées d'un projet avec leurs dates réelles |
| `TacheService` | `afficherPrixSuperieurA1000()` | Tâches à plus de 1000 DH (requête nommée `Tache.findByPrixSuperieur`) |
| `TacheService` | `afficherRealiseesEntreDates(Date, Date)` | Tâches réalisées pendant une période |

### Lancer

```bash

cd gestion-projets
docker compose up -d
mvn compile exec:java
```

### Captures

**Diagramme de classes**

<img width="394" height="538" alt="Capture d&#39;écran 2026-10-19 162358" src="https://github.com/user-attachments/assets/91ce485d-8b8c-41f7-9797-542213a52597" />

**Tables dans DBeaver** (diagramme ER)

<img width="1515" height="661" alt="Capture d&#39;écran 2026-10-19 223405" src="https://github.com/user-attachments/assets/7e6d551a-e1fd-401c-a249-23d96f0f2bcc" />
<img width="659" height="796" alt="Capture d&#39;écran 2026-10-19 224126" src="https://github.com/user-attachments/assets/89e89bdc-2ec6-40a8-94c1-014f66b545b9" />
<img width="1082" height="654" alt="Capture d&#39;écran 2026-10-19 224149" src="https://github.com/user-attachments/assets/8be659d1-4932-4351-9309-10cbc32d9a45" />
<img width="1239" height="663" alt="Capture d&#39;écran 2026-10-19 224201" src="https://github.com/user-attachments/assets/46a22efe-b217-4f30-9a84-ca7b0bf86bb6" />
<img width="1333" height="667" alt="Capture d&#39;écran 2026-10-19 224222" src="https://github.com/user-attachments/assets/e310b17f-7724-42c2-9bee-6c209cb4974f" />

**Résultats dans la console** (les 6 requêtes du `Test`)

<img width="915" height="732" alt="Capture d&#39;écran 2026-10-19 224057" src="https://github.com/user-attachments/assets/042993c4-1743-42f2-8a3b-d071d21e82ae" />
<img width="1103" height="594" alt="Capture d&#39;écran 2026-10-19 224108" src="https://github.com/user-attachments/assets/1c22ea73-89d1-49f1-b7fc-116bbfd99867" />

---

## Exercice 3 : Gestion de l'état civil

### Description

Application de gestion des citoyens d'une province et de leurs relations matrimoniales. Un homme peut avoir plusieurs épouses (successivement ou en même temps), et une femme peut se marier plusieurs fois. Chaque mariage garde sa date de début, sa date de fin éventuelle et le nombre d'enfants.

### Modèle de données

- `Personne` (abstraite) : id, nom, prenom, telephone, adresse, dateNaissance. Stratégie d'héritage `JOINED` (une table par classe).
- `Homme` et `Femme` : sous-classes de `Personne`.
- `Mariage` : classe d'association entre `Homme` et `Femme` (dateDebut, dateFin, nbrEnfant). Un mariage en cours a une `dateFin` nulle. Il possède son propre identifiant, ce qui permet à un même couple de se marier, divorcer, puis se remarier.


### Base de données

MySQL 8 dans Docker (conteneur `mysql-etatcivil`), port **3307**, utilisateur `root` / mot de passe `root`, base `gestion_etat_civil`. Pour DBeaver, ajouter la propriété de driver `allowPublicKeyRetrieval=true`.

### Fonctionnalités

Un mariage est compté dans la période [d1, d2] s'il la chevauche : `dateDebut <= d2` et (`dateFin` nulle ou `dateFin >= d1`).

| Service | Méthode | Technique |
|---|---|---|
| `FemmeService` | `afficherListe()`, `afficherPlusAgee()` | HQL |
| `HommeService` | `afficherEpousesEntreDates(id, d1, d2)` | HQL |
| `FemmeService` | `afficherNbEnfantsEntreDates(id, d1, d2)` | Requête native nommée `Femme.nbEnfantsEntreDates` |
| `FemmeService` | `afficherMarieesAuMoinsDeuxFois()` | Requête nommée `Femme.mariesAuMoinsDeuxFois` |
| `HommeService` | `afficherHommesMariesAQuatreFemmes(d1, d2)` | API Criteria |
| `HommeService` | `afficherMariages(id)` | HQL : mariages en cours et mariages échoués, avec détails |

### Lancer

```bash
cd gestion-etat-civil
docker compose up -d
mvn compile exec:java
```

Le `Test` crée 10 femmes, 5 hommes et 11 mariages, puis exécute toutes les requêtes.

### Captures

**Diagramme de classes**

<img width="453" height="370" alt="Capture d&#39;écran 2026-10-19 162108" src="https://github.com/user-attachments/assets/41675366-956e-4219-afa8-959f2e139ecf" />

**Tables dans DBeaver** (diagramme ER : `personne`, `homme`, `femme`, `mariage`)

<img width="1519" height="651" alt="Capture d&#39;écran 2026-10-19 215914" src="https://github.com/user-attachments/assets/c1fe4018-8a0e-40b1-93f2-94104175016f" />
<img width="750" height="795" alt="Capture d&#39;écran 2026-10-19 222848" src="https://github.com/user-attachments/assets/a68f1bb6-13dc-46b0-a652-27ead83070b5" />
<img width="941" height="673" alt="Capture d&#39;écran 2026-10-19 223119" src="https://github.com/user-attachments/assets/e98a95bc-6996-4433-b0e3-4bb6185201c9" />
<img width="911" height="665" alt="Capture d&#39;écran 2026-10-19 223130" src="https://github.com/user-attachments/assets/31280865-cc16-4ea3-b1b2-51a06421de52" />
<img width="1500" height="676" alt="Capture d&#39;écran 2026-10-19 223141" src="https://github.com/user-attachments/assets/83ffe5fa-38f3-42c8-8161-aa33e1f0ecad" />
<img width="1490" height="663" alt="Capture d&#39;écran 2026-10-19 223152" src="https://github.com/user-attachments/assets/079244a6-185b-4fac-9742-f9eb8c5f087f" />

**Résultats dans la console** (liste des femmes, plus âgée, épouses, enfants, mariées deux fois, Criteria, mariages d'un homme)

<img width="936" height="813" alt="Capture d&#39;écran 2026-10-19 222910" src="https://github.com/user-attachments/assets/04ecbf94-46a7-4198-bf82-18001d9d31e3" />
<img width="1727" height="653" alt="Capture d&#39;écran 2026-10-19 222928" src="https://github.com/user-attachments/assets/dcc3d7f0-396c-45c7-96fc-47b07ea3efce" />

---

## Auteur

- Nom : Imane Hachefi
- Filière : ...
- Année universitaire : ...
