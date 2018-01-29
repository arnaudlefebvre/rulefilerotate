# RuleFileRotate
Move, archive or delete files depending on their age


# 1 - PRÉREQUIS : 
- JRE 1.6
- Droits sur les fichiers/dossiers à scanner
- Droits d'écriture dans le répertoire d'exécution ou dans le répertoire de log (property logpath)


# 2 - SYNOPSIS : 

RuleFileRotate est conçu pour étendre les outils de rotation de logs (logrotage, etc.). Il automatise le déplacement, l'archivage ou la suppression de fichier en fonction de leur âge et de règles de conservations souvent spécifiques à chaque application. Il ne se substitue pas aux outils existants, mais apporte la possibilité de traiter des fichiers tout en sauvegardant une partie de ceux-ci. 

RuleFileRotate utilise des règles ayant pour but de chercher des fichiers correspondants à une période donnée, puis d'en sélectionner un nombre donné sur chaque unité de temps donnée dans la période. 
Par exemple : Sélectionner 3 fichiers de logs par jour sur 2 jours glissants 

Des actions peuvent ensuite être exécutées sur tous les autres fichiers. ie : suppression, archivage, déplacement.

Normalement, RuleFileRotate est lancé comme un travail quotidien de cron. Il applique ses règles et appliquera les actions indiquées en option à chaque lancement.

Les règles peuvent être précisées en utilisant l'option -r ou -f (voir help pour + d'infos). Au moins une de ces deux options doit être précisée.

Le répertoire à scanner est requis et doit être précisé en utilisant l'option -p


# 3 - INFORMATION DÉTAILLÉE :

RuleFileRotate se lance par défaut en mode test et permet d'afficher le résultat de la sélection des fichiers par les règles. Pour appliquer des actions, celles-ci doivent être précisées dans les options. Dans ce mode, une confirmation sera demandée avant d'effectuer les actions. l'option -F permet de forcer le lancement automatique 

Un fichier de log par exécution du programme sera créé. Ce fichier contient le récapitulatif des règles utilisées, des fichiers sélectionnés et à traiter et du traitement des actions (si il y en a). Les options -Dlogpath="<yourpath>" -Dlogfilename="<yourlogfilename>" -Dloglevel="<yourlogLevel>" de la JVM permettent de changer le répertoire de destination, le nom des fichiers et le niveau de logs. Il est recommandé d'avoir les droits d'écriture dans le répertoire d'exécution du programme. 


## 3.1 - Règles de sélection :

    La syntaxe des règles est la suivante (à utiliser dans l'option -r ou -f pour un fichier de règles):
    FROM (dyn-date) TO (dyn-date) SELECT (x) EACH (time-unit) [AT (dyn-at-dates) ] [(select-mode)]
    où 
    dyn-date     : date fixe au format dd/MM/yyyy ou dd/MM/yyyy HH:mm:ss.
                    Par exemple : 03/01/2017 
              OU date dynamique calculée à partir de la date d'exécution
              du programme (aujourd'hui).
              La syntaxe est la suivante :
                TODAY[+\-][0-9][TIMEUNIT] 
                où TIMEUNIT : DAY, MONTH, YEAR
              
              Par ex : TODAY-2DAY = 01/01/2017 si nous sommes le 03/01/2017
              
              L'ajout/soustraction d'unité de temps peut être cumulé.
              
              Par exemple TODAY+1YEAR-1YEAR équivaut à aujourd'hui
              
              Il est possible de faire référence à la date de la clause FROM
              dans la clause TO en utilisant le mot clé FROMDATE
              
    x        : entier positif indiquant le nombre de fichiers à sélectionner 
              par unités de temps (time-unit)
    
    time-unit    : Unité de temps : DAY, MONTH, YEAR, HOUR
    
    dyn-at-date    : Liste de dates séparées par des ",". Le nombre de dates doit être
                  supérieur ou égale à (x), chaque date peut être
                  Une date fixe au format dd/MM/yyyy ou dd/MM/yyyy HH:mm:ss.
                  OU une date dynamique : même format que les dates fixes, mais 
                  des étoiles peuvent être utilisées dans la date pour ne pas tenir
                  compte de certaines parties de celle-ci. 
                  Par ex tous les 15 du mois : ****/**/15
                  tous les jours à 21h30 : ****/**/** 21:30:**
                  Voir étape 4 dans §3.2

    select-mode    : RANDOM(par défaut), DEFAULT, FIRST(DEPRECATED), LAST(DEPRECATED). 
                  Voir étape 4 §3.2      

    Exemples : 
     - Sélectionner 1 fichier de logs par jour sur 2 jours glissants =
    FROM TODAY TO TODAY-2DAY SELECT 1 EACH DAY
     - Ensuite, sélectionner 3 fichiers de logs par mois, le 10,15 et 20 du mois sur 6 mois glissants =
    FROM TODAY-2DAY TO FROMDATE-6MONTH SELECT 3 EACH MONTH AT ****/**/10,****/**/15,****/**/20
     - Enfin, 1 fichier de logs par mois sur 1 année glissante =
    FROM TODAY-2DAY-6MONTH TO FROMDATE-1YEAR SELECT 1 EACH MONTH

## 3.2 - Déroulement de l'exécution :

1. Parsing des règles. Les règles sont chargées via la valeur des options -r (une seule règle) ou -f (un fichier de règle). Les règles sont parsées, puis compilées et enfin vérifiées. La compilation construit pour chacune des règles un tableau représentant les périodes de sélection de la règle. Celui-ci est créé en découpant l'intervalle FROM-TO en autant de (time-unit) existants dans celle-ci. Par ex : Si la règle est FROM TODAY TO TODAY-2DAY SELECT x EACH DAY alors le tableau construit contiendra aujourd'hui, demain, et après demain. Chacune de ces périodes servira ensuite à stocker les fichiers dont la clé (au sens date) est incluse dans celle-ci

2. Scan du répertoire précisé. Par défaut les sous-dossiers ne sont pas inclus. L'option -s permet de les inclures. Il est possible de filtrer les fichiers en fonction de leur nom à cette étape. Voir l'option -n 

3. Identification de la date de chaque fichier. Cette date est le critère utilisé pour l'application des règles de sélection. Par défaut cette date est la date de modification du fichier. Il est possible d'extraire cette date à partir du nom du fichier en utilisant l' option -k NAME.
    Dans ce cas les options -e, -c (et -d facultativement) doivent être précisées. 
        l'option -e permet de préciser un pattern de regexp
        l'option -c permet de préciser en utilisant les groupes capturés par l'option -e
        l'option -d permet de préciser le format de conversion en date pour la chaine résultante. 
        par défaut yyyyMMdd.  
        ex : Si filename=myapp_x_20161229_y.log, on extrait le 20161229 pour le convertir en date :
            -e "myapp_.*_([0-9]{4})([0-9]{2})([0-9]{2})_.*.log" -c "$1$2$3" [ -d "yyyyMMdd"]

4. Activation des règles. La liste des fichiers scannés est parcourue. Chacune des règles filtre alors les fichiers en fonction de l'appartenance de leur clé à l'intervalle FROM-TO. Ces fichiers sont répartis dans les périodes de la règle (Cf.#1) en fonction de l'unité de temps séparant chaque sélection (time-unit)

5. Application des règles et sélection des fichiers.
    
    - Par défaut (règle sans clause AT) 
        Pour chacune des règles. Les résultats sont sélectionnés parmi les fichiers correspondants 
        à chaque unité de temps (time-unit) dans la période FROM-TO. La méthode de sélection 
        peut être paramétrée pour chaque règle via select-mode. Voir description détaillée 
        des règles.
        
        Par défaut le mode de sélection est DEFAULT. 
            DEFAULT : Sélection par espacement maximum. Les fichiers sont sélectionnés de manière 
            à laisser le plus de distance possible (au sens date) entre eux et les bornes de 
            l'intervalle. Par ex 3/jour: Sélection des fichiers dont la clé est la plus proche 
            de 6h, puis de 12h, puis de 18h.
            RANDOM : Sélection random
            FIRST(DEPRECATED) : Sélection des premiers résultats (non recommandé)
            LAST(DEPRECATED) : Sélection des derniers résultats (non recommandé)
    
    - Avec sélection par date (règle avec clause AT)
        La sélection est d'abord effectuée en fonction de ces dates. Si pour une des périodes de 
        la règle, un des fichiers n'existe pas à la date demandée, alors 1 fichier est sélectionné
        aléatoirement dans la période (parmi ceux disponibles) (TODO sélectionner le plus proche ?)
        
        Si pour une des périodes de la règle il existe plusieurs fichiers, 
        alors seul le premier fichier trouvé sera sélectionné.

Enfin, un rapport de sélection est affiché/loggé. 

6. Demande de confirmation avant l'application des actions. Cette demande de confirmation peut être désactivée en utilisant l'option -F

7. Applications des actions. Elles sont facultatives. Chaque fichier sélectionné par les règles est archivé, copié ou supprimé en fonction des options précisées. Voir les options -z -m -fd

# 4 - HELP :
Provided by Arnaud Lefebvre (contact@arnaud-lefebvre.fr)  
usage: java [jvm opts] -jar rulefilerotate-0.0.9.jar -p path [-r rule | -f file] [options]  
-h,--help                       Print this message  
-man,--manual                   Print manual  
-p,--path <folder path>         REQUIRED : Folder path to scan, use double-quotes if spaces    
-r,--rule                       REQUIRED : rule to select files, use double-quotes  
-f,--rules-file <file>          REQUIRED : file of rules, one rule by line  
-R,--recursive                  OPTIONAL : Scan subfolders (default : no)  
-k,--key <MODIF|NAME>           OPTIONAL : MODIF(default) or NAME : File key is file last modif date (MODIF) or extracted from file name (NAME)  
-n,--name <file name regex>     OPTIONAL : Filter files by name in path (using regex)  
-o,--overwrite                  OPTIONAL : Overwrite target files if already exist  
-fd,--force-delete              OPTIONAL :/!\Delete all files to process (ie:not selected by rules)   
-m,--move <target folder>       OPTIONAL : Move files to process to <target folder> (ie : not matched by rules). See -fd option to delete original file  
-z,--zip <target folder>        OPTIONAL : Create zipfile in <target folder> for each file to process (ie : not matched by rules). See -fd option to delete original file  
-F,--force                      OPTIONAL : Force apply action on files to process without confirmation  
-da,--dis-autoresolve           OPTIONAL : Disable autoresolve for rule files selection  
-skip,--skip-error              OPTIONAL : Skip error when performing actions on file. Files will not be deleted (-fd option) if previous action failed  
-d,--df <java date pattern>     MANDATORY if key is NAME : Simple Date format used to create date object from date extracted on file name. (Default : yyyyMMdd). See -e and -c options. See https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html  
-c,--dreplace <regex replace>   MANDATORY if key is NAME : Use matching group on file name with extract regex option. See -e and -d options. Example For myapp_x_20161229_y.log :pattern=myapp_.*_([0-9]{4})([0-9]{2})([0-9]{2})_.*.log and replace=$1$2$3  
-e,--extract <regex pattern>    MANDATORY if key is NAME : Simple date extractor from file name. Use regex and groups to extract date. See -c and -d options. Example For myapp_x_20161229_y.log : pattern=myapp_.*_([0-9]{4})([0-9]{2})([0-9]{2})_.*.log and replace=$1$2$3`  
                                
