# Aktuelle Version 1

# ChatApp
Eine Java Chat-Applikation mit Springboot, React und MySql für das Modul 322. 

## Funktionen
Gruppenchats

Privatnachrichten

REST-Api

# Setup-Anleitung

### Schritt 1: Docker-Container erstellen
Navigieren Sie im Projektverzeichnis zum Unterordner `chatapp`. Starten Sie dort die Docker-Container, indem Sie den Befehl `docker-compose up -d` ausführen.

### Schritt 2: MySQL Shell starten (Optional)
Sobald die Container laufen, können Sie die MySQL-Shell mit folgendem Befehl öffnen:  
`docker exec -it chatapp-db-1 mysql -pa`

### Schritt 3: Applikation starten
Nach erfolgreicher Einrichtung können Sie die Applikation in Ihrer bevorzugten Entwicklungsumgebung (IDE) starten. Der Startvorgang kann je nach verwendeter IDE unterschiedlich sein.


# REST-Api Dokumentation
### Aktuelle API-Version: v1

## Authentifizierung
#### Register
```
localhost:8080/api/auth/register 
Body: JSON
{
    "username":"CoolGuy123",
    "password":"superSecret",
    "email":"megaCool@email.com"
}
```
#### Login
```
localhost:8080/api/auth/login
Body: JSON
{
    "username":"CoolGuy123",
    "password":"superSecret"
}
```
#### Logout
```
localhost:8080/api/auth/logout
Auth: Bearer Token
```
