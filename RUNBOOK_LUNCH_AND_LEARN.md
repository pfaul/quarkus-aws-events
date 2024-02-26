# Lunch And Learn

### Vorstellung
* Producer vorstellen
* Consumer vorstellen

### Start
* Producer starten
* Consumer starten
* Problem: Separierte Localstack Instanzen
* Start Reihenfolge ändern

### Workaround: Nur ein Lokalstack Container
* Producer: 
  * SQS Dependency hinzufügen 
  * Shared Flag löschen
* Consumer: Endpoint Override setzen
* quarkus.sns.endpoint-override=http://localhost:PORT
* Problem: Docker Port wird dynamisch erzeugt
* Lösungsansatz: Port fix einstellen beim Localstack start
* PR auf GitHub erstellt: https://github.com/quarkiverse/quarkus-amazon-services/pull/1114

### Recherche
* Nachforschung: https://docs.quarkiverse.io/quarkus-amazon-services/2.12.x/amazon-sns.html
* Git Rollback - Producer: 
  * SQS Dependency entfernen 
  * SNS Shared Flag setzen
* Service Name bei Producer und Consumer setzen
* quarkus.sns.devservices.service-name=mylocalstack
* Problemstellung: Weiterhin separierte Localstack Instanzen
### GitHub Issue
https://github.com/quarkiverse/quarkus-amazon-services/issues/1123
* Lösung: Bug im Service-Name!