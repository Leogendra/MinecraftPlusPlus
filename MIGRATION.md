# Migration de Minecraft++ vers un mod Fabric pour Minecraft 26.1.2

Ce document est le plan de migration du mod, du jar-mod MCP 1.12 actuel vers un mod Fabric pour Minecraft 26.1.2. Il découpe le travail en 37 commits. Pour chacun, il donne le message de commit prévu, les fichiers touchés et les tests associés. Le plan et ses décisions (section 2) ont été validés le 2026-10-06.

Le document sert ensuite de suivi : la colonne « Statut » du sommaire est mise à jour à chaque commit terminé.

## 1. Règles de travail

- **Plan d'abord.** Les décisions ouvertes (section 2) sont tranchées avant le premier commit. Toute ambiguïté rencontrée en cours de route est posée en question, jamais résolue par supposition.
- **Un commit, une étape qui compile.** Chaque commit laisse `./gradlew build` au vert, compilation et tests compris, et apporte ses propres tests. Après chaque étape, les tests sont lancés et leur résultat est rapporté.
- **Git pour cette migration uniquement.** Par exception, autorisée le 2026-10-06, l'assistant crée la branche `migration/fabric-26.1.2`, le tag `v1.12-final` et un commit par étape. Il ne pousse rien et ne fusionne rien : la fusion dans `main` reste à toi.
- **Messages de commit** : la convention du dépôt est conservée (`Chore: add 1.12 native jars`), c'est-à-dire `Type: description` en anglais à l'impératif. Types utilisés : `Build`, `Feat`, `Refactor`, `Test`, `Chore`, `Docs`.
- **Revue finale.** Une fois la migration terminée et tous les tests au vert, une revue par un agent indépendant (skill `louis-code-review`) sera proposée. Elle porte sur l'architecture et les conventions, sans modifier le code.

## 2. Décisions

Décisions prises le 2026-10-06 :

| # | Décision |
|---|---|
| D1 | (a) Pack en mémoire, sans dépendance copyleft. |
| D2 | JUnit et Fabric GameTest uniquement. ArchUnit est remplacé par un test JUnit qui lit les imports des sources de `core`. `fabric-loader-junit` n'est ajouté que si un test l'exige. |
| D3 | (a) Style actuel : tabulations, accolades sur leur propre ligne. |
| D4 | (a) Couleur du nom par le composant `item_name`. |
| D5 | `config/minecraftpp/MppConfig.mpp`, format `seed=<nombre>` ; `wordGen.mpp` embarqué. |
| D6 | Recommandation retenue (périmètre 1.12, variante deepslate, hauteurs décalées de −64, pas de minerais bruts, Nether vanilla), **plus le cuivre** : nouveau rôle attribué par le solveur, minerai récolté à la pioche en pierre, outils entre la pierre et le fer qui ne minent pas au-dessus du fer, variantes du cuivre vanilla. Ajouté par un commit dédié (4.4b), après la validation de la phase 2 contre la 1.12. |
| D7 | Inclure (golem de fer, piglins, patron du golem). |
| D8 | Type de dégâts généré par set, avec le nom de l'objet dans le message de mort. |
| D9 | Pas d'EULA dans la configuration : pas de GameTest client. Les vérifications client et en jeu sont faites à la main par toi. |
| D10 | Apache-2.0 (`"license": "Apache-2.0"` dans `fabric.mod.json`). Le fichier `LICENSE` est ajouté par toi. |
| D11 | Tag `v1.12-final` sur `main`, tout le travail sur `migration/fabric-26.1.2`, un commit par étape. |
| D12 | (a) Programme de capture compilé contre l'arbre MCP, non committé. |
| D13 | Les probabilités et paramètres des traits sont regroupés dans un catalogue typé de `core` dès les commits 2.5 et 2.7, sans changement de résultat. Le fichier JSON de configuration vient après la migration (section 9). |

Options étudiées avant la décision :

| # | Sujet | Options | Recommandation |
|---|---|---|---|
| D1 | Pack généré à l'exécution (ressources et données issues de la seed) | (a) Implémentation maison : un `PackResources` en mémoire, injecté par deux mixins dans les dépôts de packs serveur et client. (b) [ARRP](https://modrinth.com/mod/arrp) `0.13.0+build.1`, une bibliothèque qui génère des packs à l'exécution, sous licence **MPL-2.0** (copyleft faible), maintenue par une seule personne. Le module `fabric-resource-loader-v1` de Fabric API ne gère que des packs statiques inclus dans le jar. | **(a)** : pas de dépendance copyleft, pas de mod supplémentaire à installer, et le volume généré reste faible. |
| D2 | Dépendances de test | JUnit 6.1.3 (**EPL-2.0**, copyleft faible, utilisé seulement pour les tests et jamais distribué), `fabric-loader-junit` 0.19.5 (Apache-2.0), ArchUnit 1.5.1 (Apache-2.0, vérifie l'architecture), Fabric GameTest (inclus dans Fabric API, Apache-2.0) | Accepter les quatre. JUnit est le standard Java ; son copyleft ne touche pas le jar livré. |
| D3 | Style de code | (a) Style actuel : tabulations, accolades sur leur propre ligne, fixé dans `.editorconfig`. (b) Google Java Style : 2 espaces, accolades en fin de ligne. Il est prioritaire selon tes conventions, mais contredit ta règle « tabulations » et reformaterait tous les fichiers déplacés. | **(a)** : diffs minimaux sur le code déplacé, historique `git blame` préservé. |
| D4 | Rareté à 6 niveaux (`FAMILIAR` et `LEGENDARY` en plus des 4 vanilla) | (a) Couleur du nom portée par le composant d'objet `item_name`, avec la couleur du niveau. (b) Ramener aux 4 raretés vanilla. (c) Bibliothèque d'extension d'enums. Fabric ne sait pas étendre les enums ([issue #492](https://github.com/FabricMC/fabric/issues/492)). | **(a)** |
| D5 | Fichiers du mod sur le disque | `MppConfig.mpp` : dossier de configuration Fabric (`config/minecraftpp/MppConfig.mpp`) ou racine du jeu comme en 1.12. `wordGen.mpp` : embarqué dans le jar, ou copié à la main comme aujourd'hui. | `config/minecraftpp/`, même format `seed=<nombre>`. `wordGen.mpp` embarqué, ce qui supprime une étape d'installation. |
| D6 | Génération du monde en 26.1.2 | Minerais remplacés : périmètre 1.12 (charbon, fer, or, redstone, diamant, lapis, émeraude) ou en plus le cuivre. Deepslate : un second bloc par minerai, ou un seul bloc à fond de pierre. Hauteurs : conversion des hauteurs max de `OreRarity` (128, 64, 32, 16) vers un monde de −64 à 320. Minerais bruts (`raw_*`) : non, le minerai se récupère lui-même comme en 1.12. | Périmètre 1.12, cuivre et minerais du Nether laissés vanilla. Variante deepslate (+7 blocs). Hauteurs décalées de −64 (une hauteur max de 16 devient −48). Pas de minerais bruts. |
| D7 | Variantes dans le code Java codé en dur : réparation du golem de fer, troc des piglins, patron du golem en citrouille | Inclure ou laisser vanilla (commit 5.2) | Inclure, pour rester fidèle à la 1.12 où les variantes remplacent les objets vanilla partout. |
| D8 | Dégâts au contact des blocs | Un type de dégâts généré par set (message de mort propre au set, comme en 1.12), ou le type vanilla `hot_floor` | Type généré (commit 4.8). |
| D9 | Tests client (GameTest client, écran de liste des mondes) | Ils exigent `eula = true` dans la configuration Loom, ce qui revient à accepter l'EULA de Mojang. Cette acceptation t'appartient. Sinon : tests serveur et JUnit seulement, et vérifications client manuelles. | À toi de décider. Le plan marque « (client) » les tests concernés. |
| D10 | Licence du mod | Le dépôt n'a pas de licence ; `fabric.mod.json` demande un champ `license` | À toi de choisir. Sans choix : `All-Rights-Reserved`. |
| D11 | Préparation Git (faite par toi) | `git tag v1.12-final` sur `main`, puis `git switch -c migration/fabric-26.1.2` | Oui : la version 1.12 reste récupérable après la suppression de l'espace MCP (commit 7.1). |
| D12 | Capture des sorties de référence de la 1.12 (commit 2.1) | (a) Petit programme compilé contre l'arbre MCP actuel, que l'assistant écrit et lance sans le committer. (b) Sortie de `/mppinfo` relevée par toi dans le jeu 1.12. | **(a)** : reproductible, sans lancer le jeu. |

## 3. Contexte technique

Faits établis lors de l'étude de faisabilité et vérifiés dans le jar 26.1.2 et la documentation Fabric :

- **Version et outils.** Minecraft 26.1.2 n'est plus obfusqué et demande Java 25. Versions du modèle officiel Fabric pour 26.1.2 :
  - Fabric Loader `0.19.5` ;
  - Fabric API `0.155.3+26.1.2` ;
  - plugin Gradle `net.fabricmc.fabric-loom` 1.18 ;
  - Gradle 9.7.1.
  
  Sans obfuscation, le remapping disparaît : on utilise `implementation` et `jar`.
- **Objets.** Tous les objets passent par des composants de données. Les matériaux d'outils et d'armures sont des `record` publics (`ToolMaterial`, `ArmorMaterial`). Un `ItemStack` ne peut plus être créé avant le chargement d'un monde (utiliser `ItemStackTemplate`).
- **Systèmes pilotés par des JSON.** Recettes, tables de loot, tags (niveaux de récolte, balise), génération des minerais, échanges des villageois (`villager_trade`), modèles d'objets avec teintes (`items/*.json`, la classe `ItemColors` a disparu), « equipment assets » des armures, types de dégâts.
- **Restes codés en dur en Java** : `EnchantmentMenu` (lapis), `FuelValues` (charbon), `FireBlock.setFlammable` (privé), `IronGolem`, `PiglinAi`, `CarvedPumpkinBlock`.
- **Ce que Fabric API couvre** :

  | Besoin | Module | API |
  |---|---|---|
  | Couleurs des blocs | `fabric-rendering-v1` | `BlockColorRegistry` |
  | Commandes | `fabric-command-api-v2` | `CommandRegistrationCallback` |
  | Onglets créatifs | `fabric-creative-tab-api-v1` | `CreativeModeTabEvents` |
  | Inflammabilité, combustibles | `fabric-content-registries-v0` | `FlammableBlockRegistry`, `FuelValueEvents` |
  | Modification des biomes | `fabric-biome-api-v1` | `BiomeModifications` |
  | Tests en jeu | `fabric-gametest-api-v1` | GameTest |
  | Numérotation des états des blocs ajoutés, synchronisation des registres client-serveur | `fabric-registry-sync-v0` | — |

  Le dernier module élimine le piège trouvé lors de l'étude (identifiants vanilla décalés) ; un test du commit 3.3 le vérifiera.
- **Code 1.12 devenu inutile.**
  - `Blueprint` et les 18 classes de recettes servaient de moteur de correspondance en 1.12 ; le jeu fait aujourd'hui ce travail sur les recettes JSON. Seuls les patrons de recettes sont conservés.
  - Les 3 classes `inventory/*` et les 13 classes d'outils et d'armures servaient à contourner des enums figés ; elles disparaissent.

## 4. Architecture cible

```text
build.gradle, settings.gradle, gradle.properties, gradlew, .editorconfig
src/
├── main/java/fr/minecraftpp/
│   ├── MinecraftPlusPlus.java       point d'entrée commun : assemble les modules
│   ├── core/                        logique métier, AUCUNE dépendance à Minecraft ou Fabric
│   │   ├── config/                  lecture du format "seed=<n>"
│   │   ├── naming/                  générateur de noms (Tree, Word, map/*)
│   │   ├── solver/                  solveur de contraintes (Backtrack, CSP, constraint/*)
│   │   ├── random/                  loi normale (Gamma, Erf…)
│   │   ├── ore/                     OreProperties, OreRarity, HarvestLevel, Rarity, Color…
│   │   ├── set/                     définitions immuables (records) et générateurs de sets
│   │   ├── recipe/                  définitions de recettes et patrons
│   │   ├── variant/                 rôles vanilla et catalogue des variantes
│   │   ├── text/                    noms affichés, texte de /mppinfo
│   │   └── world/                   statut de seed d'un monde
│   ├── config/                      fichiers MppConfig.mpp et wordGen.mpp (entrées-sorties)
│   ├── content/                     adaptateurs Minecraft : blocs, objets, matériaux, enregistrement
│   ├── pack/                        pack généré : asset/*, data/*, data/vanilla/*
│   ├── world/                       fichier mppSeed.mpp
│   ├── command/                     /mppinfo
│   └── mixin/                       points d'accroche commun (enchantement, golem, piglin, packs)
├── main/resources/                  fabric.mod.json, mixins, textures, lang, wordGen.mpp
├── client/java/fr/minecraftpp/client/   teintes des blocs, liste des mondes, ouverture d'un monde
├── test/java/…                      JUnit (core sans Minecraft ; content avec Bootstrap)
└── gametest/java/…                  GameTest serveur et, selon D9, client
```

Principes appliqués :

- **Couches.** `core` ne dépend de rien : un test ArchUnit l'interdit (commit 2.2). Les adaptateurs (`content`, `pack`, `config`, `world`, `command`, `client`) dépendent de `core`, jamais l'inverse.
- **Seed déterministe.** Les générateurs de `core` sont des fonctions pures de la seed. **L'ordre des tirages aléatoires de la 1.12 est conservé**, et des tests de référence comparent la sortie à celle de la 1.12 pour une même seed (commits 2.1 et 2.7).
- **Contrats explicites** : `OreSetGenerator` (sets simples, matériaux, métaux), `BlockBehaviourModule` (comportements de blocs, par composition plutôt que par héritage), `GeneratedResourceWriter` (une classe par type de ressource ; un nouveau type est une nouvelle classe, sans toucher aux autres).
- **JSON** : écrit avec Gson (fourni par Minecraft, Apache-2.0) depuis des records typés, et non plus par concaténation de chaînes comme dans `ModModelManager`.
- **Organisation** : une classe par fichier, paquets en minuscules et regroupés par sujet. Les paquets `anotation`, `damageSource` et `enumeration` disparaissent.

## 5. Sommaire des commits

Fichiers touchés : C = créés, M = modifiés, R = déplacés (avec ou sans retouches), D = supprimés. Taille : S ≤ 5 fichiers, M ≤ 15, L ≤ 30, XL au-delà.

| # | Commit | C | M | R | D | Total | Taille | Statut |
|---|---|---:|---:|---:|---:|---:|---|---|
| 1.1 | `Build: add Fabric Loom build for Minecraft 26.1.2` | 8 | 2 | | | 10 | M | [x] |
| 1.2 | `Feat: bootstrap an empty Fabric mod with smoke tests` | 8 | 2 | | | 10 | M | [x] |
| 1.3 | `Build: run build and tests in GitHub Actions` | 1 | | | | 1 | S | [x] |
| 2.1 | `Test: capture 1.12 generator outputs as golden fixtures` | 4 | | | | 4 | S | [x] |
| 2.2 | `Refactor: move the constraint solver to the core package` | 3 | 1 | 13 | | 17 | L | [x] |
| 2.3 | `Refactor: move the name generator to the core package` | 1 | | 8 | | 9 | M | [x] |
| 2.4 | `Refactor: move ore value types and distributions to the core package` | 5 | | 13 | | 18 | L | [x] |
| 2.5 | `Refactor: describe ore sets as immutable definitions` | 10 | | | | 10 | M | [x] |
| 2.6 | `Refactor: compute tool and armor stats in the core` | 2 | 1 | 2 | 5 | 10 | M | [x] |
| 2.7 | `Refactor: generate the seven ore sets as definitions` | 7 | | 6 | 2 | 15 | M | [x] |
| 2.8 | `Refactor: express variants and recipes as core definitions` | 8 | | 1 | 20 | 29 | L | [x] |
| 2.9 | `Refactor: parse seeds and compare world seeds in the core` | 3 | | 1 | | 4 | S | [x] |
| 3.1 | `Feat: generate the ore catalog from the configured seed` | 4 | 1 | | 1 | 6 | M | [x] |
| 3.2 | `Feat: port the mppinfo command to Brigadier` | 2 | 1 | | 1 | 4 | S | [x] |
| 3.3 | `Feat: register ore and storage blocks` | 7 | 1 | | 5 | 13 | M | [x] |
| 3.4 | `Feat: add falling, absorbing, damaging and powered block behaviours` | 7 | 3 | | 3 | 13 | M | [x] |
| 3.5 | `Feat: register generated items with data components` | 4 | 1 | | 8 | 13 | M | [x] |
| 3.6 | `Feat: register tools and armors from generated materials` | 4 | 1 | | 16 | 21 | L | [x] |
| 3.7 | `Feat: list generated content in creative tabs` | 2 | 1 | | | 3 | S | [x] |
| 3.8 | `Feat: register fuel values and flammability` | 3 | 1 | | | 4 | S | [x] |
| 4.1 | `Feat: serve a generated in-memory pack` | 12 | 4 | | | 16 | L | [x] |
| 4.2 | `Feat: generate block states, models and item definitions` | 20 | 4 | 24 | 19 | 67 | XL | [x] |
| 4.3 | `Feat: generate recipes, loot tables and tags` | 12 | 7 | | | 19 | L | [x] |
| 4.4 | `Feat: generate ore features and replace vanilla ores` | 8 | 4 | | 1 | 13 | M | [x] |
| 4.4b | `Feat: add copper as a generated ore role` | | | | | | M | [ ] |
| 4.5 | `Feat: make vanilla recipes accept generated variants` | 4 | 1 | | | 5 | S | [ ] |
| 4.6 | `Feat: replace vanilla loot with generated variants` | 2 | 1 | | | 3 | S | [ ] |
| 4.7 | `Feat: pay villagers with the generated currency` | 3 | 1 | | | 4 | S | [ ] |
| 4.8 | `Feat: generate translations and walk damage types` | 4 | 2 | | 1 | 7 | M | [ ] |
| 4.9 | `Feat: generate tinted equipment assets for armors` | 3 | 2 | 6 | | 11 | M | [ ] |
| 5.1 | `Feat: use the generated enchanting currency in the enchanting table` | 2 | 2 | 1 | | 5 | S | [ ] |
| 5.2 | `Feat: accept generated variants in golem and piglin interactions` | 4 | 2 | | | 6 | M | [ ] |
| 6.1 | `Feat: store the Minecraft++ seed in new worlds` | 4 | 1 | | | 5 | S | [ ] |
| 6.2 | `Feat: refuse to open worlds created with another seed` | 2 | 2 | | | 4 | S | [ ] |
| 6.3 | `Feat: show the Minecraft++ seed status in the world list` | 2 | 2 | | | 4 | S | [ ] |
| 7.1 | `Chore: remove the MCP workspace and bundled Mojang files` | | 1 | | 2 896 | 2 897 | XL | [ ] |
| 7.2 | `Docs: rewrite the README for the Fabric version` | 1 | 1 | | | 2 | S | [ ] |

Hors suppression finale (7.1), la migration touche environ 350 fichiers, dont 75 déplacés. Sur les 111 fichiers du mod :

- 32 sont déplacés tels quels ou presque (seul `CSP` perd une ligne) ;
- 11 sont réécrits ;
- 68 sont supprimés : 3 sont fusionnés dans les générateurs, les autres sont remplacés par des composants, des données générées ou l'API Fabric.

## 6. Détail des commits

### Phase 1 — Socle Fabric

À la fin de cette phase, un mod Fabric vide se construit, se charge et passe un test de démarrage. L'ancien arbre `src/minecraft/` reste en place, hors du build Gradle, jusqu'au commit 7.1.

#### 1.1 `Build: add Fabric Loom build for Minecraft 26.1.2`
- **Contenu** :
  - wrapper Gradle 9.7.1 ;
  - `build.gradle` avec le plugin `net.fabricmc.fabric-loom` 1.18, `splitEnvironmentSourceSets()` et Java 25 ;
  - `gradle.properties` : `minecraft_version=26.1.2`, `loader_version=0.19.5`, `fabric_api_version=0.155.3+26.1.2` ;
  - `.editorconfig` selon D3 ;
  - mise à jour de `.gitignore` (`build/`, `.gradle/`, `run/`) et de `.gitattributes` (fins de ligne de `gradlew` et `*.bat`, `*.jar` en binaire).
- **Fichiers** : C `gradlew`, `gradlew.bat`, `gradle/wrapper/gradle-wrapper.jar`, `gradle/wrapper/gradle-wrapper.properties`, `settings.gradle`, `build.gradle`, `gradle.properties`, `.editorconfig` ; M `.gitignore`, `.gitattributes`.
- **Tests** : `./gradlew build` réussit (pas encore de source).

#### 1.2 `Feat: bootstrap an empty Fabric mod with smoke tests`
- **Contenu** :
  - `fabric.mod.json` : id `minecraftpp`, licence selon D10, dépendances `fabricloader` et `fabric-api` ;
  - les deux configurations de mixins (commune et client) ;
  - les points d'entrée `MinecraftPlusPlus` et `MinecraftPlusPlusClient`, pour l'instant vides ;
  - l'icône ;
  - dans `build.gradle`, la configuration des GameTest (`fabricApi.configureTests`). Les dépendances JUnit arrivent au commit 2.2, avec les premiers tests unitaires.
- **Fichiers** : C `src/main/resources/fabric.mod.json`, `src/main/resources/minecraftpp.mixins.json`, `src/client/resources/minecraftpp.client.mixins.json`, `MinecraftPlusPlus.java`, `client/MinecraftPlusPlusClient.java`, `assets/minecraftpp/icon.png`, `src/gametest/resources/fabric.mod.json`, `gametest/ModLoadingGameTest.java` ; M `build.gradle`, `gradle.properties`.
- **Tests** : GameTest « le serveur démarre avec `minecraftpp` chargé ».

#### 1.3 `Build: run build and tests in GitHub Actions` (facultatif)
- **Contenu** : un workflow qui lance `./gradlew build` sur Java 25 à chaque push.
- **Fichiers** : C `.github/workflows/build.yml`.
- **Tests** : le workflow passe au vert.

### Phase 2 — Cœur métier sans Minecraft

Le code Java pur est déplacé dans `core`. La logique couplée aux classes Minecraft 1.12 est réécrite en générateurs qui produisent des définitions immuables. Le critère de sortie de la phase : pour 3 seeds de référence, `core` produit exactement les mêmes 7 sets que la 1.12.

#### 2.1 `Test: capture 1.12 generator outputs as golden fixtures`
- **Contenu** :
  - pour 3 seeds, sortie complète de `SetManager.getInfoString()` de la 1.12 (nom, rareté, attributs, répartition, niveau de récolte), complétée par les couleurs et les identifiants de textures ;
  - capture selon D12, avant toute modification de l'ancien code ;
  - un README qui explique comment les fichiers ont été obtenus et pourquoi ils ne doivent pas être modifiés à la main.
- **Fichiers** : C `src/test/resources/golden/seed-*.txt` (4), `src/test/resources/golden/README.md`.
- **Tests** : données de référence, utilisées par les commits 2.3 et 2.7.
- **Réalisé** : 4 seeds au lieu de 3 (26, 30, 42, −7046029254386353131), choisies parmi 43 seeds capturées parce qu'ensemble elles couvrent tous les types de sets et tous les traits. Chaque fichier contient le texte de `/mppinfo`, le détail de chaque valeur générée et les noms affichés. La capture tourne sur un Java 8 : le solveur 1.12 évalue ses contraintes avec le moteur JavaScript Nashorn, supprimé depuis Java 15 (voir 2.2).

#### 2.2 `Refactor: move the constraint solver to the core package`
- **Contenu** :
  - `randomizer/backtrack/**` devient `core/solver/**`, paquet `constraints` renommé `constraint` ;
  - `CSP` perd son seul import vanilla et son effet de bord global : il ne remplace plus `System.err` ; c'est la source des messages parasites qui est supprimée ;
  - `Evaluator` évaluait les contraintes en intension avec le moteur JavaScript Nashorn, absent de Java 25 : il est remplacé par `BooleanExpression`, un évaluateur limité à la grammaire produite par `Pretreatment` (entiers, `==`, `&&`, `||`, parenthèses) ;
  - ajout de JUnit (D2) ; la règle d'architecture est un test JUnit qui lit les imports.
- **Fichiers** : R 12, plus `OreProperties` (prévu en 2.4, déplacé ici car `Pretreatment` en dépend) ; D `Evaluator` ; M `build.gradle`, `gradle.properties` ; C `BooleanExpression`, `GoldenFixture` (lecture des fichiers de référence), `BooleanExpressionTest`, `BacktrackTest`, `ConstraintTest`, `CoreArchitectureTest`.
- **Réalisé** : `CSP` reçoit le `Random` en paramètre au lieu de le lire dans un champ statique de `Backtrack`. Un test supplémentaire vérifie que, pour les 4 seeds de référence, le solveur donne exactement les types de sets et les rôles de la 1.12.
- **Tests** :
  - la solution satisfait toutes les contraintes, pour 100 seeds ;
  - même seed, même solution ;
  - évaluation des expressions produites par `Pretreatment` ;
  - règle d'architecture : aucune source de `fr.minecraftpp.core` n'importe `net.minecraft` ni `net.fabricmc`.

#### 2.3 `Refactor: move the name generator to the core package`
- **Contenu** :
  - `util/nameGenerator/**` devient `core/naming/**` ;
  - `WordGen` devient `NameGenerator` : une instance, sans état statique, qui reçoit le dictionnaire en entrée au lieu de lire un fichier ;
  - `jars/wordGen.mpp` est déplacé dans les ressources du mod (D5).
- **Fichiers** : R 6 + `WordGen.java` + `wordGen.mpp` ; C `NameGeneratorTest`.
- **Tests** : les noms produits pour les 3 seeds de référence sont identiques à ceux de la 1.12.

#### 2.4 `Refactor: move ore value types and distributions to the core package`
- **Contenu** :
  - déplacement vers `core/random` et `core/ore` de la loi normale (4 classes), de `UniqueArrayList`, `Color`, des 5 énumérations, de `OreRarity` et `IColored` ;
  - nouvelle énumération `core/ore/Rarity` à 6 niveaux, reprise du `Rarity.java` modifié de la 1.12 ; la couleur est portée par une valeur RGB et non plus par une classe Minecraft.
- **Fichiers** : R 13 ; C `Rarity`, `NormalDistributionTest`, `OreRarityTest`, `ColorTest`, `RarityTest`.
- **Tests** :
  - valeurs connues de la loi normale ;
  - bornes de `OreRarity` (au plus 20 filons, densité au plus 20) ;
  - `Rarity.next()` plafonne à `LEGENDARY`.
- **Réalisé** :
  - déplacés : la loi normale (4 classes), `Color` (rendue immuable), `FlammabilityOf`, `HarvestLevel`, `ToolType` (`SPADE` devient `SHOVEL`), `OreRarity` ;
  - non déplacés, car inutiles avec des définitions immuables et des tags : `UniqueArrayList` (liste de paiement de la balise, remplacée par un tag), `IColored`, `ModelType`. Ils disparaissent avec l'ancien arbre ;
  - bug latent corrigé dans `ContinuedFraction` : `equals(double, double, double)` s'appelait lui-même à l'infini. Les couleurs de la 1.12 n'atteignaient jamais cette branche, donc les tirages ne changent pas ; `NormalDistributionTest` couvre le cas.

#### 2.5 `Refactor: describe ore sets as immutable definitions`
- **Contenu** :
  - records immuables : `OreSetDefinition`, `OreTraits`, `StorageBlockTraits`, `ItemTraits`, `FoodDefinition`, `MaterialDefinition`, `VanillaRole` (charbon, fer, or, diamant, redstone, teinture bleue, monnaie, enchantement, balise, combustible) ;
  - `ContentIds`, qui fixe la règle de nommage des identifiants (`xyzium_ore`, `xyzium_block`…) ;
  - les valeurs sont validées à la construction, par exemple une lumière comprise entre 0 et 15.
- **Fichiers** : C 8 + `OreSetDefinitionTest`, `ContentIdsTest`.
- **Tests** : les valeurs hors bornes sont refusées ; les identifiants restent stables et en minuscules.
- **Réalisé** :
  - records `OreSetDefinition`, `ItemTraits`, `StorageBlockTraits`, `OreTraits`, `OreDrop` (le minerai se récupère lui-même, ou donne des objets et de l'expérience), `OreGeneration`, `FoodDefinition`, `MaterialDefinition`, avec les énumérations `SetType`, `VanillaRole` et `ArmorPiece` ;
  - catalogue des traits (D13) dans `core/trait` : `Trait` fixe l'ordre des tirages et les chances de la 1.12, `TraitCatalog` est la seule source de ces chances. Les paramètres de chaque trait restent tirés avec les formules de la 1.12 jusqu'à la configuration JSON (section 9) ;
  - tests : `OreSetDefinitionTest`, `ContentIdsTest`, `TraitCatalogTest`.

#### 2.6 `Refactor: compute tool and armor stats in the core`
- **Contenu** :
  - `ToolSet`, `ArmorSet`, `MetalToolSet`, `MetalArmorSet` et la partie statistique de `DynamicMaterial` deviennent `ToolStatsGenerator` et `ArmorStatsGenerator` ;
  - ces générateurs produisent durabilité, dégâts et vitesse par type d'outil, protection par pièce, robustesse et enchantabilité ;
  - `IToolMaterial` et `IArmorMaterial` sont supprimées.
- **Fichiers** : R 2 (`ToolSet` devient `ToolStatsGenerator`, `ArmorSet` devient `ArmorStatsGenerator`) ; D 5 (`MetalToolSet`, `MetalArmorSet` et `DynamicMaterial`, fusionnés dans les générateurs, plus les deux interfaces) ; M `MaterialDefinition` ; C `ToolStatsGeneratorTest`, `ArmorStatsGeneratorTest`.
- **Tests** : statistiques identiques à celles calculées par la 1.12 pour un tirage donné.
- **Réalisé** :
  - tout ce qui concerne le matériau est regroupé dans `core/set/material` : `MaterialDefinition`, `ToolStats`, `ArmorStats`, `ToolType` (venu de `core/ore`), `ArmorPiece` et les deux générateurs ;
  - `ToolStats` garde les bonus de la 1.12 et calcule l'attaque finale de chaque outil comme les classes d'outils 1.12 (épée à −2,4 de vitesse, houe à 0 de dégâts et −4 de vitesse) ;
  - les tests comparent les générateurs aux formules 1.12 recopiées telles quelles.

#### 2.7 `Refactor: generate the seven ore sets as definitions`
- **Contenu** :
  - `SimpleSet`, `MaterialSet`, `MetalSet` deviennent `SimpleSetGenerator`, `MaterialSetGenerator` et `MetalSetGenerator`, derrière l'interface `OreSetGenerator` (ancien `ISet`) ;
  - `SetFactory` est conservée ;
  - la partie pure de `SetManager` devient `OreCatalogGenerator`, qui produit un `OreCatalog` ;
  - les tirages restent dans l'ordre de la 1.12 : toutes les constructions de sets d'abord, puis tous les `setupEffects` ;
  - `SetInfoFormatter` (texte de `/mppinfo`) et `DisplayNameFormatter` (logique de nom de `ModLanguage`) sont ajoutés ;
  - `ModManager` et `IDynamic` sont supprimés.
- **Fichiers** : R 6 ; D 2 ; C `OreCatalog`, `SetInfoFormatter`, `DisplayNameFormatter`, `OreCatalogGoldenTest`, `SetFactoryTest`, `SetInfoFormatterTest`, `DisplayNameFormatterTest`.
- **Tests** : **test de non-régression principal** : pour les 3 seeds, le texte produit est identique aux fichiers de référence du commit 2.1.
- **Réalisé** :
  - générateurs dans `core/set/generator`, en deux passes comme en 1.12 (construction de tous les sets, puis effets de tous les sets). Les bizarreries de la 1.12 sont reproduites et commentées : minerai des sets matériau et métal toujours à la pioche en pierre, palier du matériau toujours tiré, divisions entières ;
  - **tirages cachés découverts** : en 1.12, le rôle monnaie touchait la classe `EntityVillager`, dont l'initialisation statique (la table des échanges) cherchait une variante aléatoire de 22 objets vanilla avec la même suite aléatoire que la génération. `LegacyVillagerTradeDraws` reproduit ces tirages ; `GenerationContext` suit les variantes déjà enregistrées, et `VariantRules` (dans `core/variant`) dit quels objets vanilla chaque rôle remplace ;
  - `OreCatalogGoldenTest` compare, pour les 4 seeds, le texte de `/mppinfo`, chaque valeur générée et les noms affichés. Vérification ponctuelle supplémentaire : aucun écart sur les 44 seeds capturées au commit 2.1 ;
  - autres tests : `SetFactoryTest`, `LegacyVillagerTradeDrawsTest`, `SetInfoFormatterTest`, `DisplayNameFormatterTest`.

#### 2.8 `Refactor: express variants and recipes as core definitions`
- **Contenu** :
  - `Variant` devient `VariantCatalog`, avec des identifiants texte (`minecraft:iron_ingot` vers les objets générés) ;
  - les recettes deviennent des records : `ShapedRecipeDefinition`, `ShapelessRecipeDefinition`, `SmeltingRecipeDefinition` (XP égale à (rareté + 1) × 0,15), derrière l'interface scellée `RecipeDefinition` ;
  - `RecipePatterns` reprend les patrons des outils, armures, compactage, décompactage, pépites, pépite de fer, teinture bleue et redstone ;
  - `RecipePlanner` calcule les recettes à partir du catalogue ;
  - suppression du moteur de recettes 1.12 : `crafting/**` (18 fichiers) et `manager/crafting/**` (2 fichiers).
- **Fichiers** : R 1 ; C 6 + `RecipePlannerTest`, `VariantCatalogTest` ; D 20.
- **Tests** :
  - patron de la pioche ;
  - XP de fusion ;
  - un set de métal reçoit les recettes de pépite ;
  - pas de recette de teinture bleue pour un métal ;
  - les variantes de fer sont listées.
- **Réalisé** :
  - `VariantCatalog` est construit à partir du catalogue et de `VariantRules` (commit 2.7), qui reste la seule règle « quel rôle remplace quel objet vanilla » ;
  - recettes : `Ingredient` (un objet, une liste d'objets ou un tag), `RecipeCategory`, et les trois records derrière `RecipeDefinition`. `SmeltingRecipeDefinition` couvre le four et le haut fourneau : comme les minerais vanilla, les minerais et l'équipement en métal se cuisent aussi au haut fourneau ;
  - les deux variantes de minerai (pierre et deepslate) se cuisent ;
  - la classe 1.12 `IronNuggetRecipe` n'était qu'un garde-fou : seul le vrai lingot de fer donnait des pépites de fer vanilla. Elle devient une règle du commit 4.5 : une recette vanilla dont le résultat a des variantes n'est pas réécrite.

#### 2.9 `Refactor: parse seeds and compare world seeds in the core`
- **Contenu** :
  - la partie analyse de `MppConfig` devient `MppSeedParser`, qui produit une erreur explicite si le format est faux ;
  - `WorldSeedStatus` donne le statut d'un monde : `VALID`, `WRONG` ou `VANILLA` selon la seed enregistrée dans le monde, absente ou non.
- **Fichiers** : R 1 ; C `WorldSeedStatus`, `MppSeedParserTest`, `WorldSeedStatusTest`.
- **Tests** : formats valides, invalides et vides ; les trois statuts.
- **Réalisé** : `MppSeedParser` lit et écrit aussi `mppSeed.mpp` (le nombre en première ligne, puis l'avertissement de la 1.12, qui pointe désormais vers `config/minecraftpp`). Une erreur de format lève `MalformedSeedException`, une exception vérifiée dont le message est destiné au joueur. Un monde dont le fichier de seed est illisible sera traité comme `WRONG` (commit 6.2), pour ne jamais ouvrir un monde avec d'autres minerais.

### Phase 3 — Contenu Minecraft

Les blocs et objets générés sont enregistrés dans le jeu. À la fin de la phase, ils existent en jeu mais sans modèles (textures manquantes) et sans recettes.

#### 3.1 `Feat: generate the ore catalog from the configured seed`
- **Contenu** :
  - `MppConfigFile` crée `MppConfig.mpp` avec une seed aléatoire s'il est absent, puis le lit (D5) ;
  - `WordGenDictionary` charge le dictionnaire embarqué ;
  - au démarrage, `MinecraftPlusPlus` génère le catalogue et écrit son résumé dans le journal ;
  - suppression de `ModBootstrap`.
- **Fichiers** : C `config/MppConfigFile`, `config/WordGenDictionary`, `MppConfigFileTest`, `WordGenDictionaryTest` ; M `MinecraftPlusPlus` ; D `init/ModBootstrap`.
- **Tests** : fichier créé au premier lancement ; fichier mal formé signalé clairement ; dictionnaire chargé.
- **Réalisé** : un fichier mal formé arrête le jeu avec un message qui donne le chemin du fichier et la marche à suivre. La tâche Gradle `writeGameTestSeed` écrit `seed=42` dans la configuration du serveur GameTest avant chaque lancement : les GameTest connaissent ainsi le contenu généré. Au démarrage du serveur de test, le journal affiche les 7 minerais de la seed 42, identiques à la capture 1.12.

#### 3.2 `Feat: port the mppinfo command to Brigadier`
- **Contenu** : `/mppinfo` (niveau de permission 2, c'est-à-dire les commandes activées) enregistrée par `CommandRegistrationCallback` et affichant `SetInfoFormatter`. Suppression de `CommandMppInfo`.
- **Fichiers** : C `command/MppInfoCommand`, `MppInfoCommandGameTest` ; M `MinecraftPlusPlus` ; D 1.
- **Tests** : GameTest, la commande renvoie 7 lignes.

#### 3.3 `Feat: register ore and storage blocks`
- **Contenu** :
  - `ContentRegistrar` enregistre, pour chaque set, le minerai, sa variante deepslate (D6) et le bloc de stockage, avec leurs objets-blocs ;
  - `RegisteredContent` retrouve un bloc ou un objet à partir de son identifiant ;
  - `BlockPropertiesFactory` traduit les traits en `BlockBehaviour.Properties` : dureté, lumière, `friction`, `speedFactor`, outil requis ;
  - suppression des anciennes classes de blocs et de `ModBlock`.
- **Fichiers** : C `ContentRegistrar`, `RegisteredContent`, `DynamicBlock`, `DynamicOreBlock`, `BlockPropertiesFactory`, `ContentRegistrarTest`, `BlockPlacementGameTest` ; M `MinecraftPlusPlus` ; D 5.
- **Tests** :
  - les 21 blocs attendus sont enregistrés pour la seed de référence ;
  - l'air garde l'identifiant d'état 0 et l'aller-retour identifiant ↔ état est exact (garde contre le piège de l'étude) ;
  - un bloc posé émet la lumière prévue.
- **Réalisé** :
  - dureté et résistance de la 1.12 : 3 et 5 pour les minerais (4,5 et 5 en deepslate), 5 et 10 pour les blocs de stockage. Le minerai de gemme donne l'expérience tirée en 1.12, le minerai de métal n'en donne pas ;
  - les tests de contenu sont des GameTest (seed 42), sans `fabric-loader-junit` (D2) ;
  - **bug corrigé** : environ une seed sur cent (177 sur 20 000) donnait le même nom à deux sets. Les identifiants étaient alors en double, ce que les registres 26.1.2 refusent. `OreCatalogGenerator` retire un nom déjà pris. Le générateur de noms a sa propre suite aléatoire : les minerais ne changent pas, et les seeds sans doublon restent identiques à la 1.12. Test : `SetNameUniquenessTest`.

#### 3.4 `Feat: add falling, absorbing, damaging and powered block behaviours`
- **Contenu** :
  - interface `BlockBehaviourModule` et ses modules : `FallingModule`, `AbsorbingModule`, `WalkDamageModule`, `RedstonePowerModule`, `PoweredOreModule` (minerai qui s'allume au contact, comme la redstone) ;
  - l'opacité passe par `getLightDampening` ;
  - `WalkDamageModule` utilise provisoirement `hot_floor`, remplacé au commit 4.8 (D8) ;
  - suppression de `IFalling`, `IAbsorbing` et `ModDamageSource`.
- **Fichiers** : C 6 + `BlockBehaviourGameTest` ; M `DynamicBlock`, `DynamicOreBlock`, `BlockPropertiesFactory` ; D 3.
- **Tests** (GameTest) :
  - un bloc sans support tombe ;
  - l'eau voisine est absorbée ;
  - une entité qui marche dessus prend des dégâts ;
  - signal de redstone de 15.
- **Réalisé** :
  - `BlockBehaviourModules` choisit les modules d'un bloc d'après ses traits et leur transmet chaque événement ;
  - le minerai alimenté reproduit la 1.12 : des particules de redstone, envoyées par le serveur, quand on le pose, marche dessus, l'utilise ou le frappe. Il ne s'allume pas comme le minerai de redstone vanilla, ce qui demanderait un état et des modèles en plus ;
  - opacité : seules les valeurs 1.12 inférieures à 15 laissent passer de la lumière, puisque la lumière ne dépasse jamais 15 ;
  - les modules d'absorption et de dégâts, absents de la seed 42, sont testés seuls dans le monde de test.

#### 3.5 `Feat: register generated items with data components`
- **Contenu** :
  - `ItemPropertiesFactory` construit les composants : nourriture (`food` et `consumable`), brillance, rareté par la couleur du nom (D4), objet acceptant les enchantements ;
  - `DynamicItem` déclenche le feu à l'usage (ancien `LighterUse`) ;
  - les pépites sont des objets ordinaires ;
  - suppression de 8 fichiers : objets, nourriture et `ModItem`.
- **Fichiers** : C `DynamicItem`, `ItemPropertiesFactory`, `ItemComponentsTest`, `FireStarterGameTest` ; M `ContentRegistrar` ; D 8.
- **Tests** :
  - valeurs nutritives et saturation conformes à la définition ;
  - couleur de rareté ;
  - l'objet briquet allume un feu.
- **Réalisé** :
  - **seed des GameTest** : passage de 42 à −7046029254386353131, une des seeds de référence, qui couvre en jeu la nourriture, le briquet, la brillance, les dégâts au contact, la redstone, la monnaie, l'enchantement, le charbon, le diamant et les métaux fer et or. `GameTestSets` documente ses sets. La chute, absente de cette seed, est testée par son module seul ;
  - couleur de rareté (D4) : le jeu réécrit le composant `item_name` après les propriétés de l'objet. La couleur est donc appliquée par `getName` dans `DynamicItem` et `DynamicBlockItem`, ce qui donne le même résultat à l'affichage ;
  - nourriture : en 1.12, la saturation était un modificateur. Le constructeur vanilla `saturationModifier` fait le même calcul (deux fois la nutrition multipliée par le modificateur). Le statut « nourriture pour loup » passera par le tag `wolf_food` (commit 4.3) ;
  - le briquet allume un feu comme le briquet vanilla et se consomme, comme en 1.12. L'objet « monnaie d'enchantement » n'est pas un composant : c'est le tag du commit 5.1.

#### 3.6 `Feat: register tools and armors from generated materials`
- **Contenu** :
  - `MaterialFactory` construit les `ToolMaterial` (avec les tags générés `incorrect_for_*` et de réparation) et les `ArmorMaterial` (avec une clé d'equipment asset) ;
  - `ToolItemFactory` et `ArmorItemFactory` créent les objets ;
  - suppression des 5 outils, des 8 armures et des 3 classes `inventory/*`.
- **Fichiers** : C 3 + `ToolAndArmorTest` ; M `ContentRegistrar` ; D 16.
- **Tests** : durabilité, dégâts et protection par pièce conformes aux définitions du commit 2.6.
- **Réalisé** :
  - les outils utilisent les tags vanilla `incorrect_for_*_tool` du niveau de minage du matériau, ce qui évite de générer ces tags : les minerais générés seront rangés dans les tags vanilla `needs_*_tool` (commit 4.3), et outils vanilla et générés se comportent alors de la même façon ;
  - le bonus de dégâts du matériau vanilla vaut 0 : chaque outil reçoit son attaque complète, calculée dans `core` comme en 1.12 ;
  - la hache, la pelle et la houe dérivent des classes vanilla, pour écorcer, créer des chemins et labourer ;
  - la réparation passe par le tag `minecraftpp:<nom>_repair_materials` (règle de nommage dans `core/set/TagIds`), rempli au commit 4.3.

#### 3.7 `Feat: list generated content in creative tabs`
- **Contenu** : `CreativeTabEntries` ajoute le contenu généré aux onglets créatifs via `CreativeModeTabEvents`, avec un ordre stable, set par set.
- **Fichiers** : C `CreativeTabEntries`, `CreativeTabEntriesTest` ; M `MinecraftPlusPlus`.
- **Tests** : ordre et contenu des onglets.
- **Réalisé** : les onglets suivent ceux du vanilla moderne. Les minerais vont dans « Blocs naturels », les blocs de stockage dans « Construction », les objets et pépites dans « Ingrédients », les outils dans « Outils », l'épée, la hache et l'armure dans « Combat ».

#### 3.8 `Feat: register fuel values and flammability`
- **Contenu** : enregistrement des durées de combustion (`FuelValueEvents`) et de l'inflammabilité (`FlammableBlockRegistry`) des objets et blocs concernés.
- **Fichiers** : C `FuelRegistration`, `FlammabilityRegistration`, `FuelAndFlammabilityGameTest` ; M `MinecraftPlusPlus`.
- **Tests** : un four brûle l'objet pendant la durée prévue ; valeurs d'inflammabilité enregistrées.
- **Réalisé** : les durées et les blocs inflammables sont calculés par des fonctions testées en JUnit (seed 42 : un bloc qui brûle comme la vigne). La durée de combustion est vérifiée en GameTest. Un bloc qui brûle à l'infini, comme le netherrack, n'est pas inflammable : il ira dans le tag `infiniburn_overworld` (commit 4.3).

### Phase 4 — Pack généré (ressources et données)

Tout ce que la 1.12 injectait en Java devient des fichiers JSON générés depuis la seed. Chaque `GeneratedResourceWriter` est testé en comparant son JSON à un fichier de référence.

#### 4.1 `Feat: serve a generated in-memory pack`
- **Contenu (D1 = a)** :
  - `GeneratedPack` (un `PackResources` en mémoire), `GeneratedPackContents`, `GeneratedPackSource`, `PackJson` (aide Gson) ;
  - un mixin côté serveur et un côté client rendent le pack toujours actif.
- **Fichiers** : C 4 + 2 mixins + `GeneratedPackTest`, `GeneratedPackGameTest` ; M les 2 configurations de mixins et les 2 points d'entrée.
- **Variante D1 = b** : un adaptateur ARRP ; M `build.gradle` et `fabric.mod.json` ; environ 5 fichiers.
- **Tests** : le pack liste et sert les fichiers écrits ; un tag de test généré est visible par le serveur.
- **Réalisé** :
  - **Un seul mixin, commun**, au lieu de deux. Les packs de données du serveur (`ServerPacksSource`) et les packs de ressources du client (`ClientPackSource`) héritent tous deux de `BuiltInPackSource`. `BuiltInPackSourceMixin` ajoute le pack généré à la fin de `loadPacks`, avec le type de la source, lu par `@Shadow` sur le champ privé `packType` : pas d'accesseur. La configuration de mixins client et `MinecraftPlusPlusClient` restent inchangées.
  - **Pack** : il s'appelle `minecraftpp_generated`. Il est obligatoire, placé en haut pour pouvoir remplacer les fichiers vanilla (4.5, 4.6), et sans fichier `pack.mcmeta`. Ses métadonnées sont construites en mémoire avec la version de format du jeu en cours (`SharedConstants.getCurrentVersion().packVersion(type)`) : aucun numéro de format n'est écrit en dur.
  - **Contrat des générateurs** : `GeneratedResourceWriter` est une fonction pure du catalogue qui rend des `GeneratedFile` (type de pack, emplacement, texte JSON). `GeneratedPackContents` les rassemble au démarrage et refuse deux fichiers au même emplacement, l'un masquerait l'autre.
  - **Écart : de vrais tags au lieu d'un tag de test.** Le « tag de test » prévu aurait demandé un point d'entrée réservé aux tests dans le code du mod. À la place, le premier générateur écrit les vrais tags de réparation `<nom>_repair_materials`, que les matériaux référencent depuis le commit 3.6 sans qu'ils existent. Les outils et armures se réparent donc avec l'objet principal de leur set, ce que vérifie le GameTest. `TagJson` et ce générateur sont repris par les tags du commit 4.3.
  - 8 tests JUnit (pack en mémoire, tags de la seed 42) et 2 GameTest (pack actif sur le serveur, réparation).

#### 4.2 `Feat: generate block states, models and item definitions`
- **Contenu** :
  - `BlockStateWriter` ;
  - `BlockModelWriter` : minerai superposé sur la pierre ou la deepslate, bloc teinté ;
  - `ItemModelWriter` : `items/*.json` avec teintes `minecraft:constant` ;
  - côté client, `DynamicBlockTints` passe par `BlockColorRegistry` ;
  - textures déplacées vers `assets/minecraftpp/textures/block|item/` : 8 de blocs et 16 d'objets ;
  - suppression des 14 textures Scenarium, inutilisées depuis le commit « Removed Scenarium », ainsi que de `ModModelManager`, `ModRenderItem` et `DynamicColor`.
- **Fichiers** : C 4 + `AssetWritersTest` et 3 JSON de référence ; M les 2 points d'entrée ; R 24 ; D 17.
- **Tests** : JSON identiques aux références ; vérification visuelle en jeu, ou GameTest client selon D9.
- **Réalisé** :
  - **Rendu de la 1.12 conservé.** Le bloc de stockage est un cube teinté, le minerai une surimpression teintée sur la pierre ou la deepslate. Les deux modèles parents (`tinted_cube`, `tinted_overlay_cube`) sont des fichiers statiques du jar. Chaque bloc reçoit un état de bloc et un modèle générés, qui ne nomment que ses textures.
  - **Objets** : un modèle par objet à texture unique et une définition `items/*.json` par objet, teinte `minecraft:constant` à la couleur du set. Les blocs portés en main utilisent le modèle de leur bloc. Écart mineur : seuls les outils sont tenus comme des outils (`item/handheld`) ; les autres objets utilisent `item/generated`, alors qu'en 1.12 tout était en `handheld`.
  - **Teinte des blocs posés** : `DynamicBlockTints` (client) enregistre une couleur constante par bloc avec `BlockColorRegistry`. Aucun calque de rendu n'est à déclarer : 26.1.2 déduit le calque de chaque face de la transparence de sa texture.
  - Les générateurs sont listés dans `GeneratedPackWriters`. `ContentIds.blocks` donne les trois blocs d'un set, `TextureIds` la règle de nommage des textures.
  - 24 textures déplacées. Suppressions : les 14 textures Scenarium, `ModModelManager`, `ModRenderItem`, `DynamicColor`, ainsi que `IColored` et `ModelType`, devenus inutiles.
  - 3 tests JUnit : 5 fichiers comparés à leurs références (seed 42) ; une définition par objet ; pour les 4 seeds de référence, chaque modèle et chaque texture du mod cités existent.
  - **À vérifier en jeu** : aspect des blocs, des minerais (pierre et deepslate) et des objets.

#### 4.3 `Feat: generate recipes, loot tables and tags`
- **Contenu** :
  - `RecipeWriter` traduit les `RecipeDefinition` en JSON ;
  - `LootTableWriter` : minerai de gemme avec bonus de fortune, minerai de métal et bloc de stockage qui se récupèrent eux-mêmes ;
  - `TagWriter` : `mineable/pickaxe`, `needs_*_tool`, `incorrect_for_*_tool`, réparation, `beacon_payment_items`, `beacon_base_blocks`, tags de variantes.
- **Fichiers** : C 3 + `DataWritersTest`, 3 JSON de référence, `RecipeAndLootGameTest` ; M `MinecraftPlusPlus`.
- **Tests** :
  - la fusion d'un minerai donne le bon objet ;
  - miner avec un outil trop faible ne donne rien ;
  - le bloc de set sert de base de balise.
- **Réalisé** :
  - `RecipeWriter` traduit les `RecipeDefinition` du `core` dans le format de recette de 26.1.2 (ingrédient écrit comme un objet, une liste d'objets ou `#tag`). `LootTableWriter` : bloc de stockage et minerai de métal se récupèrent eux-mêmes ; minerai de gemme : lui-même avec Toucher de soie, sinon ses objets avec le bonus de Fortune vanilla. L'expérience reste portée par le bloc (`DropExperienceBlock`).
  - `TagWriter` complète les tags vanilla, sans les remplacer : `mineable/pickaxe`, `needs_stone_tool`, `needs_iron_tool`, `needs_diamond_tool` (rien pour le niveau bois), `beacon_base_blocks`, `beacon_payment_items`, `wolf_food`, `infiniburn_overworld`. Les tags `incorrect_for_*` vanilla incluent déjà les `needs_*` : inutile de les écrire.
  - **Report** : les tags de variantes partent au commit 4.5, où ils sont utilisés pour la première fois. Les tags de réparation restent dans `RepairMaterialTagWriter` (4.1).
  - Les records JSON (`RecipeJson`, `LootTableJson`) omettent leurs champs nuls, ce qui donne des fichiers proches des fichiers vanilla.
  - 2 tests JUnit : 5 fichiers comparés à leurs références (seed 42) ; pour les 4 seeds, tout contenu du mod nommé par une recette, une table de loot ou un tag existe. 4 GameTest : cuisson d'un minerai de gemme et d'un minerai de métal, niveau d'outil requis, objets lâchés, balise.

#### 4.4 `Feat: generate ore features and replace vanilla ores`
- **Contenu** :
  - `OreFeatureWriter` écrit les `configured_feature` et `placed_feature` ;
  - `OreHeightMapping` (dans `core`) convertit les hauteurs selon D6 ;
  - `OreBiomeModifications` retire les minerais vanilla et ajoute ceux du mod via `BiomeModifications` ;
  - suppression de `OreRegistry`.
- **Fichiers** : C `OreFeatureWriter`, `OreBiomeModifications`, `OreHeightMapping`, `OreHeightMappingTest`, `OreGenerationGameTest` ; M `MinecraftPlusPlus` ; D 1.
- **Tests** : conversion des hauteurs ; dans un tronçon de monde généré, présence des minerais du mod et absence des minerais vanilla.
- **Réalisé** :
  - Une veine 1.12 devient une feature `minecraft:ore` : nombre de veines par tronçon, taille de veine, hauteur uniforme. La veine pose le minerai deepslate là où elle traverse de la deepslate (tags vanilla `stone_ore_replaceables` et `deepslate_ore_replaceables`), alors que la 1.12 ne remplaçait que la pierre.
  - Hauteurs (D6) : `OreHeightMapping` (`core/world`) donne `[-64, -64 + max - 1]`, la 1.12 excluant le maximum. Une hauteur max de 16 donne donc des veines jusqu'à -49, sous -48.
  - `OreBiomeModifications` retire les 17 features vanilla du périmètre 1.12 (charbon, fer, or, redstone, diamant, lapis, émeraude) et ajoute les 7 minerais du mod. Le cuivre et les minerais du Nether restent vanilla.
  - **Écart** : les biomes sont choisis par le tag `minecraft:is_overworld` au lieu de `BiomeSelectors.foundInOverworld()`. Ce dernier ne retient que les biomes que le monde en cours génère : dans un monde plat, ou dans le monde des GameTest, les autres biomes gardaient les minerais vanilla. C'est le GameTest qui l'a révélé.
  - Le GameTest lit les features des biomes (plaines, badlands, pics dentelés) au lieu de creuser un tronçon : le monde des GameTest est plat. **À vérifier en jeu** : minerais visibles dans un nouveau monde, plus de minerais vanilla du périmètre.

#### 4.4b `Feat: add copper as a generated ore role` (D6)
- **Contenu** :
  - nouveau rôle `copper` dans le solveur : un seul minerai le reçoit, il n'est ni charbon ni métal du groupe vanilla déjà pris ;
  - le minerai se récolte à la pioche en pierre ; s'il porte des outils, leur niveau est celui du cuivre vanilla (entre la pierre et le fer, ne mine pas au-dessus du fer) ;
  - ses objets deviennent des variantes du cuivre vanilla (lingot, bloc, pépite) ;
  - le cuivre vanilla est retiré de la génération, comme les autres minerais remplacés.
- **Tests** : de nouveaux fichiers de référence sont capturés pour les 3 seeds, car l'ajout d'un rôle change les tirages. Les fichiers 1.12 restent dans l'historique du commit 2.1.

#### 4.5 `Feat: make vanilla recipes accept generated variants`
- **Contenu** :
  - `VanillaDataSource` lit les JSON du pack vanilla ;
  - `VanillaRecipeRewriter` remplace dans les ingrédients `iron_ingot`, `gold_ingot`, `diamond`, `coal`… par le tag de variantes correspondant.
- **Fichiers** : C 2 + `VanillaRecipeRewriterTest`, `VariantCraftingGameTest` ; M `MinecraftPlusPlus`.
- **Tests** : réécriture d'un JSON d'exemple ; fabriquer un seau avec un lingot généré.

#### 4.6 `Feat: replace vanilla loot with generated variants`
- **Contenu** : `VanillaLootTableRewriter` remplace chaque objet vanilla ayant des variantes par un tirage équiprobable entre ses variantes. C'est l'équivalent JSON du patch `LootTable` de la 1.12.
- **Fichiers** : C `VanillaLootTableRewriter`, `VanillaLootTableRewriterTest` ; M `MinecraftPlusPlus`.
- **Tests** : réécriture d'une table de coffre d'exemple.

#### 4.7 `Feat: pay villagers with the generated currency`
- **Contenu** : `VillagerTradeRewriter` remplace `minecraft:emerald` par la monnaie générée dans les 387 fichiers `villager_trade`, et les objets demandés par leurs variantes.
- **Fichiers** : C `VillagerTradeRewriter`, `VillagerTradeRewriterTest`, `VillagerTradeGameTest` ; M `MinecraftPlusPlus`.
- **Tests** : réécriture d'un échange d'exemple ; un villageois propose la monnaie générée.

#### 4.8 `Feat: generate translations and walk damage types`
- **Contenu** :
  - `LanguageWriter` produit `en_us` pour les noms générés et les messages de mort ;
  - `DamageTypeWriter` écrit un type de dégâts par set qui blesse au contact ;
  - `en_us.json` statique pour les messages du mod ;
  - `WalkDamageModule` passe au type généré ;
  - suppression de `ModLanguage`.
- **Fichiers** : C `LanguageWriter`, `DamageTypeWriter`, `assets/minecraftpp/lang/en_us.json`, `LanguageWriterTest` ; M `WalkDamageModule`, `MinecraftPlusPlus` ; D 1.
- **Tests** : noms au format « Xyzium Block » identiques à la 1.12 ; message de mort propre au set.

#### 4.9 `Feat: generate tinted equipment assets for armors`
- **Contenu** :
  - `EquipmentAssetWriter` écrit un fichier par matériau : une couche teintable (`dyeable`, couleur du set) et, pour les textures `generic_2`, une couche de surimpression non teintée ;
  - les 6 textures d'armure sont déplacées vers `textures/entity/equipment/humanoid[_leggings]/`.
- **Fichiers** : C `EquipmentAssetWriter`, `EquipmentAssetWriterTest`, 1 JSON de référence ; M `ArmorItemFactory`, `MinecraftPlusPlus` ; R 6.
- **Tests** : JSON identiques aux références ; armure visible et teintée en jeu (vérification manuelle ou client selon D9).

### Phase 5 — Règles du jeu codées en dur (Mixins)

#### 5.1 `Feat: use the generated enchanting currency in the enchanting table`
- **Contenu** :
  - `EnchantmentMenuMixin` remplace les deux tests `Items.LAPIS_LAZULI` par le tag `minecraftpp:enchanting_currency`, écrit par `TagWriter` ;
  - la texture d'emplacement de la 1.12 (`enchanting_table.png`) devient l'icône d'emplacement `container/slot/lapis_lazuli`.
- **Fichiers** : C `EnchantmentMenuMixin`, `EnchantingCurrencyGameTest` ; M `minecraftpp.mixins.json`, `TagWriter` ; R 1.
- **Tests** : la table accepte la monnaie générée et refuse le lapis.

#### 5.2 `Feat: accept generated variants in golem and piglin interactions` (selon D7)
- **Contenu** : trois mixins.
  - `IronGolemMixin` : réparation avec les variantes de fer ;
  - `PiglinAiMixin` : troc avec les variantes d'or ;
  - `CarvedPumpkinBlockMixin` : patron du golem avec les blocs de variantes de fer.
- **Fichiers** : C 3 + `VariantInteractionsGameTest` ; M `minecraftpp.mixins.json`, `TagWriter`.
- **Tests** : réparation, troc et invocation du golem avec une variante.

### Phase 6 — Seed Minecraft++ et sauvegardes

#### 6.1 `Feat: store the Minecraft++ seed in new worlds`
- **Contenu** : `MppSeedFile` lit et écrit `mppSeed.mpp`, au même format qu'en 1.12. Un mixin client sur la création de monde l'écrit avant le premier démarrage du serveur intégré.
- **Fichiers** : C `world/MppSeedFile`, `client/mixin/WorldCreationMixin`, `MppSeedFileTest`, `WorldCreationClientGameTest` (client) ; M `minecraftpp.client.mixins.json`.
- **Tests** : lecture et écriture du fichier ; un nouveau monde contient `mppSeed.mpp` (client).

#### 6.2 `Feat: refuse to open worlds created with another seed`
- **Contenu** : `WorldOpenFlowsMixin` bloque l'ouverture d'un monde `WRONG` ou `VANILLA` et affiche un message expliquant quoi faire, comme en 1.12.
- **Fichiers** : C `WorldOpenFlowsMixin`, `WorldOpenGuardClientGameTest` (client) ; M `minecraftpp.client.mixins.json`, `en_us.json`.
- **Tests** : la logique est déjà testée par `WorldSeedStatusTest` ; l'ouverture d'un monde à mauvaise seed est refusée (client).

#### 6.3 `Feat: show the Minecraft++ seed status in the world list`
- **Contenu** : `WorldListEntryMixin` ajoute « Valid Mpp Seed », « Wrong Mpp Seed » ou l'avertissement de monde vanilla dans la liste des mondes.
- **Fichiers** : C `WorldListEntryMixin`, `WorldListClientGameTest` (client) ; M `minecraftpp.client.mixins.json`, `en_us.json`.
- **Tests** : libellé affiché pour chacun des trois statuts (client).

### Phase 7 — Nettoyage et documentation

#### 7.1 `Chore: remove the MCP workspace and bundled Mojang files`
- **Préalable** : `src/minecraft/fr/` est vide (vérifié par `git ls-files`), les phases 2 à 6 sont terminées et le tag `v1.12-final` existe (D11).
- **Contenu** : suppression du code décompilé de Minecraft et des binaires Mojang, que la licence interdit de redistribuer ; nettoyage des entrées MCP de `.gitignore`.
- **Fichiers** :
  - D `src/minecraft/net/**`, `src/minecraft/mcp/**` et `Start.java` (2 169) ;
  - D `src/.gitignore` ;
  - D `jars/**` (723 : monde de développement MCP, jars et natives 1.12, `eula.txt`) ;
  - D `assets/.gitignore` ;
  - D les 2 annotations `anotation/*` ;
  - M `.gitignore`.
- **Tests** : `./gradlew build` complet ; partie manuelle en jeu (création de monde, minage, fabrication, enchantement, échange).

#### 7.2 `Docs: rewrite the README for the Fabric version`
- **Contenu** :
  - présentation ;
  - installation : Fabric Loader 0.19.5, Fabric API, le jar du mod ;
  - les deux seeds et l'emplacement de `MppConfig.mpp` ;
  - construction depuis les sources (`./gradlew build`) ;
  - deux exemples concrets : jouer la seed d'un ami, relire les minerais avec `/mppinfo` ;
  - le fichier `LICENSE` selon D10.
- **Fichiers** : M `README.md` ; C `LICENSE`.
- **Tests** : relecture ; la procédure d'installation suivie pas à pas fonctionne.

## 7. Devenir des fichiers actuels

### Code du mod (111 fichiers de `src/minecraft/fr/minecraftpp/`)

| Paquet actuel | Fichiers | Devenir | Commit |
|---|---:|---|---|
| `randomizer/backtrack/**` | 13 | Déplacés vers `core/solver` | 2.2 |
| `util/nameGenerator/**`, `init/WordGen` | 7 | Déplacés vers `core/naming` | 2.3 |
| `util/normalDistribution/**`, `util/UniqueArrayList`, `color/Color`, `enumeration/*`, `generation/OreRarity`, `item/material/IColored` | 13 | Déplacés vers `core/random`, `core/ore`, `core/util` | 2.4 |
| `randomizer/set/` (outils, armures), `item/material/DynamicMaterial` | 5 | 2 réécrits en générateurs de statistiques, 3 fusionnés dedans | 2.6 |
| `item/material/IToolMaterial`, `IArmorMaterial` | 2 | Supprimés | 2.6 |
| `randomizer/set/` (sets, `ISet`, `SetFactory`), `manager/SetManager` | 6 | Réécrits en générateurs de définitions | 2.7 |
| `manager/ModManager`, `randomizer/IDynamic` | 2 | Supprimés | 2.7 |
| `variant/Variant` | 1 | Réécrit en `VariantCatalog` | 2.8 |
| `crafting/**`, `manager/crafting/**` | 20 | Supprimés (remplacés par les définitions de recettes) | 2.8 |
| `init/MppConfig` | 1 | Réécrit en `MppSeedParser` | 2.9 |
| `init/ModBootstrap` | 1 | Supprimé (remplacé par `MinecraftPlusPlus`) | 3.1 |
| `command/CommandMppInfo` | 1 | Supprimé (remplacé par `MppInfoCommand`) | 3.2 |
| `block/DynamicBlock`, `block/IDynamicBlock`, `block/ore/*`, `manager/block/ModBlock` | 5 | Supprimés (nouvelle implémentation dans `content/block`) | 3.3 |
| `block/IFalling`, `block/IAbsorbing`, `damageSource/ModDamageSource` | 3 | Supprimés (modules de comportement) | 3.4 |
| `item/*` (4), `item/food/*` (3), `manager/item/ModItem` | 8 | Supprimés (composants de données) | 3.5 |
| `item/tool/*` (5), `item/armor/*` (8), `inventory/*` (3) | 16 | Supprimés (fabriques d'outils et d'armures) | 3.6 |
| `manager/renderer/*`, `color/DynamicColor` | 3 | Supprimés (writers de ressources, teintes client) | 4.2 |
| `manager/block/OreRegistry` | 1 | Supprimé (génération JSON et `BiomeModifications`) | 4.4 |
| `language/ModLanguage` | 1 | Supprimé (`LanguageWriter`, `DisplayNameFormatter`) | 4.8 |
| `anotation/Mod`, `anotation/Todo` | 2 | Supprimés | 7.1 |

### Autres fichiers suivis par Git

| Élément | Fichiers | Devenir | Commit |
|---|---:|---|---|
| Textures de blocs et d'objets utilisées | 24 | Déplacées vers `assets/minecraftpp/textures/block|item/` | 4.2 |
| Textures Scenarium (inutilisées) | 14 | Supprimées | 4.2 |
| Textures d'armure `generic_*` | 6 | Déplacées vers `textures/entity/equipment/` | 4.9 |
| `gui/container/enchanting_table.png` | 1 | Converti en icône d'emplacement | 5.1 |
| `jars/wordGen.mpp` | 1 | Déplacé dans les ressources du mod | 2.3 |
| Reste de `jars/` | 723 | Supprimé | 7.1 |
| Client 1.12 décompilé (`net/`, `mcp/`, `Start.java`) | 2 169 | Supprimé | 7.1 |
| `src/.gitignore`, `assets/.gitignore` | 2 | Supprimés | 7.1 |
| `README.md`, `.gitignore`, `.gitattributes` | 3 | Mis à jour | 1.1, 7.1, 7.2 |

## 8. Hors périmètre

- **Conversion des mondes 1.12** : les blocs à identifiant numérique (1000 et plus) ne survivent pas à la conversion vanilla.
- **Serveur dédié** : non distribué, comme en 1.12. Le mod fonctionne côté serveur, mais la synchronisation de la seed entre client et serveur n'est pas prévue. `fabric-registry-sync-v0` refusera la connexion si les registres diffèrent ; ce comportement sera constaté, pas amélioré.
- **Montée vers 26.2 ou plus** : à planifier séparément une fois la 26.1.2 stable.

## 9. Après la migration : configuration des traits en JSON

Objectif : passer par une architecture de configuration pour activer, désactiver et personnaliser les traits des objets et des blocs, à partir du catalogue typé introduit par D13.

Exemple de format visé :

```json
{
	"format_version": 1,
	"traits": {
		"edible": { "one_in": 7, "food_restoration": { "low": 1, "high": 10 } },
		"shiny": { "one_in": 0 },
		"falls": { "one_in": 15 }
	}
}
```

Règles déjà fixées :

- JSON strict, sans commentaires. La documentation des clés est tenue à part.
- `one_in: 0` désactive un trait. Le tirage correspondant est quand même consommé, pour que désactiver un trait ne change pas les autres minerais de la seed.
- La nourriture se compte en points du jeu : 1 point vaut une demi-cuisse.
- L'ordre des tirages reste fixé par le code ; le fichier ne donne que des paramètres, retrouvés par leur identifiant.
- Le fichier est lu au démarrage du mod (les blocs sont enregistrés avant le chargement des datapacks), depuis `config/minecraftpp/`, par un adaptateur de `config/` qui produit les records de `core`.
- Pas de trait `is_burnable` pour l'instant : le combustible est un rôle du solveur, lié au charbon, et n'est pas modifié.

Questions à trancher au moment de planifier cette phase :

- Une même seed avec deux fichiers différents donne deux jeux de minerais. Faut-il enregistrer une empreinte du fichier dans `mppSeed.mpp` et l'ajouter à `WorldSeedStatus` ?
- Quelles bornes exposer pour chaque trait, et faut-il corriger au passage les bizarreries héritées de la 1.12 (lumière à 15 ou 0, glissance toujours à 0,4, accélération à 0,5 ou 1,5, vitesse de la hache toujours à −3,2, houe à −4 de vitesse d'attaque, donc 0 attaque par seconde) ?
- En 1.12, le troisième paramètre de la nourriture tiré à 1/5 est « nourriture pour loup », pas « toujours mangeable ».

## 10. Point de reprise (2026-10-06)

**État** : commits 1.1 à 4.4 faits sur `migration/fabric-26.1.2` (tag `v1.12-final` sur `main`). Les phases 1, 2 et 3 sont terminées, ainsi que 4.1 à 4.4 : 107 tests JUnit et 24 GameTest au vert. **Prochaine étape : 4.4b**, le rôle cuivre.

**Environnement** :

- le `JAVA_HOME` du système pointe vers un JDK 17. Gradle doit tourner avec le JDK 26 installé : `JAVA_HOME="/c/Program Files/Java/jdk-26.0.2" ./gradlew build` (environ 20 s, GameTest compris) ;
- les sources décompilées de Minecraft 26.1.2 s'obtiennent avec `./gradlew genSources`. Elles sont dans `.gradle/loom-cache/minecraftMaven/net/minecraft/minecraft-{common,clientOnly}-*/26.1.2/*-sources.jar`, à décompresser pour y chercher les API ;
- les sources de Fabric API se téléchargent depuis `maven.fabricmc.net` (artefacts `-sources.jar`) ;
- les GameTest tournent avec la seed −7046029254386353131 (`writeGameTestSeed` dans `build.gradle`) ; leurs sets sont décrits dans `GameTestSets`.

**Pack généré (4.1)** : un générateur rend des `GeneratedFile` (type de pack, emplacement sous `assets/` ou `data/`, texte JSON écrit par `PackJson` depuis un record). Un tag s'écrit avec `TagJson.toFile(TagKey)`. Les fichiers générés écrasent ceux des packs placés en dessous, vanilla compris. Les textures restent des fichiers statiques du jar, servis par le pack de mod de Fabric.

**Points à reporter dans les commits suivants** :

- 4.5 : écrire les tags de variantes (reportés de 4.3) ; ne pas réécrire une recette vanilla dont le résultat a des variantes (ancien `IronNuggetRecipe`) ;
- 4.8 : remplacer `hotFloor()` dans `ContentRegistrar.registerBlocks` par le type de dégâts généré. Traduire aussi les tags d'objets générés (`tag.item.minecraftpp.<nom>_repair_materials`) : Fabric signale en développement les tags sans traduction ;
- 4.9 : l'equipment asset de chaque matériau est `minecraftpp:<nom>` (`MaterialFactory.equipmentAsset`).
