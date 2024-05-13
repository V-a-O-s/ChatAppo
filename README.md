# Aktuelle Version 1

# ChatApp
Eine Java-Chat-Anwendung, die **Springboot**, **React** und **MySql** verwendet und für das Modul 322 entwickelt wurde.

## Funktionen
- Unterstützt Gruppenchats
- Privatnachrichten
- Eine umfangreiche REST-API

## Datenbankeinrichtung
Automatische Einrichtung der Datenbank mit **Docker**.

## Installationsanleitung

### Schritt 1: Erstellen des Docker-Containers
Navigiere im Projektverzeichnis zum Unterordner `chatapp`. Starte die Docker-Container, indem du den Befehl `docker-compose up -d` ausführst.

### Schritt 2: Applikation starten
Nachdem die Einrichtung erfolgreich abgeschlossen ist, kann die Anwendung in der bevorzugten Entwicklungsumgebung (IDE) gestartet werden. Der Startvorgang kann je nach verwendeter IDE variieren.

# User Stories

## MUS 1: Gruppen
**Als** Benutzer, der sich gerne in Gruppen vernetzt,  
**möchte ich** in Gruppen chatten und diese erstellen können,  
**damit** ich mich mit meiner Freundesgruppe im selben Chatraum unterhalten kann.

**Akzeptanzkriterien:**
- Die User können Gruppen erstellen.
- Es können mehrere Benutzer den Gruppen beitreten und darin schreiben.

## MUS 2: Sicherheit
**Wir als** Benutzer mit Sicherheitsbedenken,  
**wünschen uns,** dass die Nachrichten verschlüsselt werden,  
**sodass** niemand diese abfangen, verändern oder lesen kann und der Empfänger diese auch im "Originalzustand" zugestellt bekommt.

**Akzeptanzkriterien:**
- Die Nachricht darf während dem Transport nicht manipulierbar sein.
- Sie muss anonym übermittelt werden.

## MUS 3: Nachrichten senden und empfangen
**Als** Benutzer,  
**möchte ich** Nachrichten senden und empfangen können,  
**um** mit anderen Mitgliedern der Chat-App kommunizieren zu können.

**Akzeptanzkriterien:**
- Benutzer können Textnachrichten in einem Chatraum senden.
- Benutzer erhalten Nachrichten in Echtzeit von anderen Teilnehmern im selben Chatraum.
- Die Chat-Oberfläche aktualisiert sich automatisch, um gesendete und empfangene Nachrichten ohne Benutzerinteraktion anzuzeigen.
- Benutzer können den Verlauf der Konversation im Chatraum einsehen, auch nachdem sie die App verlassen und wieder betreten haben.


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
- **Benutzerzugang**: Verschiedene Benutzerrollen (ADMIN, SUPPORTER, USER) haben Zugriff auf `/api/v1/user/**`.
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

# Frontend Dokumentation

## Überblick
JChat ist eine umfangreiche Chat-Applikation entwickelt mit modernen Technologien wie React, Axios und Backend-Services, die eine Echtzeit-Kommunikation ermöglichen. Die App bietet Funktionen für das Registrieren, Anmelden, Erstellen und Beitreten von Chats sowie das Austauschen von Nachrichten.

## Technischer Stack
- **Framework**: React
- **State Management**: React Router für Navigation
- **HTTP Client**: Axios für API-Anfragen
- **Styling**: CSS

## Installation

### Voraussetzungen
- Node.js installiert
- NPM oder Yarn als Paketmanager

### Installationsanleitung
```
Klonen Sie das Repository auf Ihren lokalen Rechner.
Navigieren Sie zum Projektverzeichnis und installieren Sie die Abhängigkeiten:
npm install
Starten Sie den Entwicklungsserver:
npm run dev
```

## Dateistruktur

- **index.html**: HTML-Vorlage mit Wurzelelement für React.
- **main.jsx**: Einstiegspunkt für die React-Anwendung, initialisiert den Router und rendert den `RouterProvider`.
- **styles/index.css**: Basis-CSS-Datei für globale Stile.

## Komponenten

### Home
- **Pfad**: `./components/home/homepage`
- Zeigt die Hauptchat-Oberfläche für eingeloggte Benutzer.

### Login
- **Pfad**: `./components/login/login`
- Verwaltet die Authentifizierung der Benutzer.

### Welcome
- **Pfad**: `./components/welcome/welcome`
- Öffentliche Startseite für alle Benutzer.

## Routing

Implementiert mit React Router; definiert in `main.jsx`:
- `/`: Welcome-Seite.
- `/login`: Login-Seite.
- `/home`: Startseite (geschützter Bereich für eingeloggte Benutzer).

## Services

### AuthService
Verwaltet Authentifizierungsoperationen wie Login, Logout und Registrierung:
- **login(email, password)**: Loggt den Benutzer ein und speichert das Token im lokalen Speicher.
- **logout()**: Entfernt Benutzerdetails aus dem lokalen Speicher.
- **getCurrentUser()**: Ruft die aktuellen Benutzerdetails aus dem lokalen Speicher ab.
- **isLoggedIn()**: Überprüft, ob der Benutzer durch Verifizierung des Tokens eingeloggt ist.

### MessagesService
Verwaltet die Interaktionen bezüglich der Nachrichten im Chat:
- **getMessages(chatId)**: Ruft alle Nachrichten eines spezifischen Chats ab.
- **sendMessage(chatId, message)**: Sendet eine Nachricht im spezifischen Chat.

## Sicherheit
- Alle sensiblen Operationen benötigen Authentifizierung.
- Passwörter werden im Backend vor der Speicherung gehasht.


## Deployment

- Die Anwendung wird für die Produktionsumgebung mit dem Befehl vorbereitet:
```
npm run dev
```
- Stellen Sie sicher, dass alle Umgebungsvariablen korrekt für die Produktion eingestellt sind, einschließlich API-URLs und anderer Drittanbieterintegrationen.



## Quellen

| Quelle | URL |
|--------|-----|
| LearnWithIfte | [https://www.youtube.com/@LearnWithIfte](https://www.youtube.com/@LearnWithIfte) |
| ChatGPT | [https://chatgpt.com/](https://chatgpt.com/) |