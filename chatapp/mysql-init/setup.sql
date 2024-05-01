CREATE DATABASE IF NOT EXISTS chatapp_db;
use chatapp_db;

CREATE USER IF NOT EXISTS 'chatapp_admin' IDENTIFIED BY 'admin';
GRANT ALL ON chatapp_db.* TO 'chatapp_admin';

CREATE TABLE IF NOT EXISTS users (
    userID BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    back_up_email VARCHAR(255),
    phone VARCHAR(20),
    creation_Date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    banned BOOLEAN NOT NULL DEFAULT false,
    enabled BOOLEAN NOT NULL DEFAULT false,
    verified BOOLEAN NOT NULL DEFAULT false,
    role_on_platform ENUM('ADMIN', 'SUPPORTER', 'MVP', 'VIP', 'VERIFIED', 'USER') NOT NULL DEFAULT 'USER',
    avatar ENUM('RED', 'GREEN', 'YELLOW', 'BLUE', 'BLACK', 'WHITE', 'PURPLE', 'ORANGE', 'TEAL', 'PINK', 'GRAY', 'MAGENTA', 'SUNSET', 'OCEAN', 'FOREST', 'BERRY', 'AURORA', 'TROPICAL', 'NEON') NOT NULL DEFAULT 'GREEN'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS chats (
    chatID BIGINT AUTO_INCREMENT PRIMARY KEY,
    ownerID BIGINT NOT NULL,
    creation_date DATETIME NOT NULL,
    user_limit SMALLINT DEFAULT 2,
    last_message DATETIME NOT NULL,
    FOREIGN KEY (ownerID) REFERENCES users(userID)
);

CREATE TABLE IF NOT EXISTS memberships (
    membershipID BIGINT AUTO_INCREMENT PRIMARY KEY,
    chatID BIGINT,
    userID BIGINT,
    user_role ENUM('OWNER', 'MODERATOR', 'USER') NOT NULL,
    joinDate DATETIME NOT NULL,
    chatTimeout DATETIME,
    banned BOOLEAN DEFAULT FALSE,
    ban_date DATETIME,
    ban_reason NVARCHAR(255),
    FOREIGN KEY (chatID) REFERENCES chats(chatID),
    FOREIGN KEY (userID) REFERENCES users(userID)
);

CREATE TABLE IF NOT EXISTS messages (
    messageID BIGINT AUTO_INCREMENT PRIMARY KEY,
    chatID BIGINT,
    userID BIGINT,
    message_text NVARCHAR(2000),
    sending_time TIMESTAMP NOT NULL,
    FOREIGN KEY (chatID) REFERENCES chats(chatID),
    FOREIGN KEY (userID) REFERENCES users(userID)
);

CREATE TABLE IF NOT EXISTS invites (
    inviteID BIGINT AUTO_INCREMENT PRIMARY KEY,
    chatID BIGINT,
    invited_by_userID BIGINT,
    invite_name NVARCHAR(255),
    expiration_date DATETIME,
    active BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (chatID) REFERENCES chats(chatID),
    FOREIGN KEY (invited_by_userID) REFERENCES users(userID)
);