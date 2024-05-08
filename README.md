# Aktuelle Version 1

# ChatApp
Eine Java Chat-Applikation mit Springboot, React und MySql für das Modul 322. 

## Features
Gruppenchats
Privatnachrichten
Umfangreiche REST-Api

Automatisches aufsetzten der Datenbank mit Docker

# Setup-Anleitung

### Schritt 1: Docker-Container erstellen
Navigieren Sie im Projektverzeichnis zum Unterordner `chatapp`. Starten Sie dort die Docker-Container, indem Sie den Befehl `docker-compose up -d` ausführen.

### Schritt 2: Applikation starten
Nach erfolgreicher Einrichtung, kann die Applikation in der bevorzugten Entwicklungsumgebung (IDE) gestartet werden. Der Startvorgang kann je nach verwendeter IDE unterschiedlich sein.

# Sicherheitskonzept
## Platform
Admin, Supporter, User

## Chat
Owner, Moderator, User, Locked/Gebannt

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
    "email":"megaCool@email.com",
    "password":"superSecret"
}
```
#### Logout
```
localhost:8080/api/auth/logout
Auth: Bearer Token
```
#### Verify
localhost:8080/api/auth/verify
Auth: Bearer Token
{
    "email": "string@c2oaam.ch",
    "password": "string"
}