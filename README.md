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

# Sicherheitskonzept der ChatApp

## Allgemeine Übersicht
Die `SecurityConfiguration` der ChatApp stellt sicher, dass die Anwendung verschiedene Sicherheitsstandards und -praktiken implementiert, um die Datenintegrität und den Datenschutz zu gewährleisten. Diese Sicherheitseinstellungen verwenden Spring Security, um umfassenden Schutz auf mehreren Ebenen zu bieten.

## CORS-Konfiguration
- **Erlaubte Ursprünge**: Alle Ursprünge sind erlaubt (`"*"`). Diese Einstellung sollte in Produktionsumgebungen angepasst werden, um nur spezifische, vertrauenswürdige Ursprünge zuzulassen.
- **Erlaubte Methoden**: GET, POST.
- **Erlaubte Header**: Authorization, Cache-Control, Content-Type.
- **Credentials**: Zugelassen, was für Anfragen notwendig ist, die Benutzerauthentifizierung erfordern.

## CSRF-Schutz
- **CSRF-Schutz**: Deaktiviert, da JWTs (die im `Authorization` Header übertragen werden) von Natur aus gegen CSRF-Angriffe immun sind, weil sie nicht automatisch von Browsern mit jeder Anfrage gesendet werden.

## Authentifizierung und Autorisierung
- **Offene Endpunkte**: `/api/auth/**`, `/swagger-ui/**`, `/v3/api-docs/**` sind ohne Authentifizierung zugänglich.
- **Admin-Zugang**: Nur Benutzer mit der Rolle ADMIN haben Zugriff auf `/api/v*/team/admin/**`.
- **Support-Zugang**: Zugang für Benutzer mit den Rollen ADMIN oder SUPPORTER zu bestimmten Support-Endpunkten.
- **Benutzerzugang**: Verschiedene Benutzerrollen (ADMIN, SUPPORTER, MVP, VIP, USER) haben Zugriff auf `/api/v1/user/**`.
- **Standardzugang**: Alle anderen Anfragen erfordern eine Authentifizierung.

## Sitzungsmanagement
- **Sitzungspolitik**: Stateless. Dies bedeutet, dass die Serverseite keine Sitzungsinformationen speichert, was die Skalierbarkeit verbessert und die Auswirkungen von Sitzungs-Hijacking reduziert.

## JWT-Filter
- **JWT-Authentifizierung**: Ein `JwtAuthenticationFilter` wird vor dem `UsernamePasswordAuthenticationFilter` hinzugefügt, um die Sicherheit durch Überprüfung von JWTs zu gewährleisten.

## Ausnahmebehandlung
- **Zugriff verweigert**: Ein `CustomAccessDeniedHandler` wird verwendet, um 403 Forbidden-Antworten zu verwalten, falls ein Benutzer versucht, auf Ressourcen zuzugreifen, für die er nicht autorisiert ist.
- **Authentifizierung fehlgeschlagen**: Ein `HttpStatusEntryPoint` wird verwendet, um eine 401 Unauthorized-Antwort zu liefern, falls die Authentifizierung fehlschlägt.

## Logout Handling
- **Logout**: Benutzer können sich über den Logout-Endpunkt abmelden. Der Logout-Handler entfernt die Sicherheitskontexte, um sicherzustellen, dass die Sitzung vollständig beendet wird.

## Passwortverschlüsselung
- **Verschlüsselung**: Passwörter werden mittels `BCryptPasswordEncoder` gehasht, was eine sichere Speicherung von Benutzerpasswörtern ermöglicht.


# ChatApp API Vollständige Endpunkte Dokumentation

## Authentifizierung und Benutzerverwaltung

### Registrieren eines neuen Benutzers
- **URL**: /api/auth/register
- **Methode**: POST
- **Auth Erforderlich**: Nein
- **Beschreibung**: Ermöglicht das Registrieren eines neuen Benutzers.
- **Payload**:
  ```json
  {
    "username": "string",
    "password": "string",
    "email": "string",
    "backUpEmail": "string",
    "phone": "string"
  }
  ```
- **Erfolgreiche Antwort**:
  - **Code**: 200 OK
  - **Inhalt**:
    ```json
    {
      "token": "JWT_TOKEN"
    }
    ```

### Login
- **URL**: /api/auth/login
- **Methode**: POST
- **Auth Erforderlich**: Nein
- **Beschreibung**: Authentifiziert einen Benutzer und gibt einen JWT zurück.
- **Payload**:
  ```json
  {
    "username": "string",
    "password": "string"
  }
  ```
- **Erfolgreiche Antwort**:
  - **Code**: 200 OK
  - **Inhalt**:
    ```json
    {
    "token": "JWT_TOKEN"
    }
    ```

## Benutzerprofileinstellungen

### Benutzername ändern
- **URL**: /api/v1/user/username
- **Methode**: POST
- **Auth Erforderlich**: Ja
- **Beschreibung**: Aktualisiert den Benutzernamen eines angemeldeten Benutzers.
- **Payload**:
  ```json
  {
    "username": "neuer_benutzername"
  }
  ```

### Telefonnummer ändern
- **URL**: /api/v1/user/phone
- **Methode**: POST
- **Auth Erforderlich**: Ja
- **Beschreibung**: Aktualisiert die Telefonnummer eines Benutzers.
- **Payload**:
  ```json
  {
    "phone": "neue_telefonnummer"
  }
  ```

### Passwort ändern
- **URL**: /api/v1/user/password
- **Methode**: POST
- **Auth Erforderlich**: Ja
- **Beschreibung**: Ermöglicht einem Benutzer, sein Passwort zu ändern.
- **Payload**:
  ```json
  {
    "password": "aktuelles_passwort",
    "newPassword": "neues_passwort"
  }
  ```

### E-Mail-Adresse ändern
- **URL**: /api/v1/user/email
- **Methode**: POST
- **Auth Erforderlich**: Ja
- **Beschreibung**: Aktualisiert die E-Mail-Adresse des Benutzers.
- **Payload**:
  ```json
  {
    "email": "neue_email_adresse"
  }
  ```

### Backup-E-Mail-Adresse ändern
- **URL**: /api/v1/user/backupemail
- **Methode**: POST
- **Auth Erforderlich**: Ja
- **Beschreibung**: Aktualisiert die Backup-E-Mail-Adresse des Benutzers.
- **Payload**:
  ```json
  {
    "backupEmail": "neue_backup_email"
  }
  ```

### Benutzer löschen
- **URL**: /api/v1/user/delete
- **Methode**: POST
- **Auth Erforderlich**: Ja
- **Beschreibung**: Löscht das Benutzerkonto.
- **Payload**:
  ```json
  {
    "username": "zu_löschender_benutzername"
  }
  ```

### Benutzeravatar ändern
- **URL**: /api/v1/user/avatar
- **Methode**: POST
- **Auth Erforderlich**: Ja
- **Beschreibung**: Aktualisiert das Benutzeravatarbild.
- **Payload**:
  ```json
  {
    "avatar": "url_zum_bild"
  }
  ```

## Chat Management

### Chat erstellen
- **URL**: /api/v1/chat/create
- **Methode**: POST
- **Auth Erforderlich**: Ja
- **Beschreibung**: Erstellt einen neuen Chat.
- **Payload**:
  ```json
  {
    "chatName": "string",
    "userLimit": "integer"
  }
  ```

### Chatnamen ändern
- **URL**: /api/v1/chat/name
- **Methode**: POST
- **Auth Erforderlich**: Ja
- **Beschreibung**: Ändert den Namen eines bestehenden Chats.
- **Payload**:
  ```json
  {
    "chatID": "integer",
    "chatName": "neuer_chatname"
  }
  ```

### Benutzerlimit für einen Chat setzen
- **URL**: /api/v1/chat/limit
- **Methode**: POST
- **Auth Erforderlich**: Ja
- **Beschreibung**: Setzt oder ändert das Benutzerlimit eines Chats.
- **Payload**:
  ```json
  {
    "chatID": "integer",
    "userLimit": "integer"
  }
  ```

### Chat löschen
- **URL**: /api/v1/chat/delete
- **Methode**: POST
- **Auth Erforderlich**: Ja
- **Beschreibung**: Löscht einen Chat.
- **Payload**:
  ```json
  {
    "chatID": "integer"
  }
  ```

## Nachrichten

### Nachricht senden
- **URL**: /api/v1/message/send
- **Methode**: POST
- **Auth Erforderlich**: Ja
- **Beschreibung**: Sendet eine Nachricht in einem bestimmten Chat.
- **Payload**:
  ```json
  {
    "chatid": "integer",
    "messageText": "string",
    "userid": "integer"
  }
  ```

### Nachrichten eines Chats abrufen
- **URL**: /api/v1/message/get/{chatID}
- **Methode**: GET
- **Auth Erforderlich**: Ja
- **Beschreibung**: Ruft alle Nachrichten eines Chats ab.
- **Parameter**:
  - `chatID`: ID des Chats, dessen Nachrichten abgerufen werden sollen.

## Mitgliedschaftsverwaltung

### Benutzerrolle ändern
- **URL**: /api/v1/member/setRole
- **Methode**: POST
- **Auth Erforderlich**: Ja
- **Beschreibung**: Setzt die Rolle eines Benutzers in einem bestimmten Chat.
- **Payload**:
  ```json
  {
    "membershipID": "integer",
    "userRole": "CHAT_OWNER|CHAT_MODERATOR|CHAT_USER|CHAT_LOCKED"
  }
  ```

### Chat verlassen
- **URL**: /api/v1/member/leave/{chatId}
- **Methode**: POST
- **Auth Erforderlich**: Ja
- **Beschreibung**: Entfernt den angemeldeten Benutzer aus dem angegebenen Chat.
- **Parameter**:
  - `chatId`: ID des Chats, den der Benutzer verlassen möchte.

### Chat beitreten
- **URL**: /api/v1/member/join
- **Methode**: POST
- **Auth Erforderlich**: Ja
- **Beschreibung**: Erlaubt dem Benutzer, einem Chat mithilfe eines Einladungscodes beizutreten.
- **Payload**:
  ```json
  {
    "invite": "einladungscode"
  }
  ```

## Einladungsmanagement

### Einladung erstellen
- **URL**: /api/v1/invite/create
- **Methode**: POST
- **Auth Erforderlich**: Ja
- **Beschreibung**: Erstellt eine Einladung für einen spezifischen Chat.
- **Payload**:
  ```json
  {
    "chatID": "integer",
    "inviteName": "string",
    "expirationDate": "date-time"
  }
  ```

### Einladung aktualisieren
- **URL**: /api/v1/invite/update
- **Methode**: POST
- **Auth Erforderlich**: Ja
- **Beschreibung**: Aktualisiert den Status einer Einladung (aktiviert oder deaktiviert).
- **Payload**:
  ```json
  {
    "inviteID": "integer",
    "active": "boolean"
  }
  ```