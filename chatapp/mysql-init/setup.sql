-- Ensure the database exists
CREATE DATABASE IF NOT EXISTS chatapp_db;
USE chatapp_db;

-- Create a user for the application with appropriate privileges
CREATE USER IF NOT EXISTS 'chatapp_admin' IDENTIFIED BY 'admin';
GRANT ALL PRIVILEGES ON chatapp_db.* TO 'chatapp_admin';
FLUSH PRIVILEGES;

-- Create a table to store user information
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
    avatar ENUM('RED', 'GREEN', 'YELLOW', 'BLUE', 'BLACK', 'WHITE', 'PINK') NOT NULL DEFAULT 'GREEN'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create a table for chat rooms
CREATE TABLE IF NOT EXISTS chats (
    chatID BIGINT AUTO_INCREMENT PRIMARY KEY,
    ownerID BIGINT NOT NULL,
    creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_limit SMALLINT NOT NULL DEFAULT 2,
    last_activity TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    chat_name VARCHAR(255) NOT NULL,
    CONSTRAINT fk_owner FOREIGN KEY (ownerID) REFERENCES users(userID) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create a table for chat memberships
CREATE TABLE IF NOT EXISTS memberships (
    membershipID BIGINT AUTO_INCREMENT PRIMARY KEY,
    chatID BIGINT NOT NULL,
    userID BIGINT NOT NULL,
    user_role ENUM('CHAT_OWNER', 'CHAT_MODERATOR', 'CHAT_USER', 'CHAT_LOCKED') NOT NULL DEFAULT 'CHAT_USER',
    joinDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    chatTimeout DATETIME,
    banned BOOLEAN DEFAULT FALSE,
    ban_date DATETIME,
    ban_reason VARCHAR(255),
    CONSTRAINT fk_membership_chat FOREIGN KEY (chatID) REFERENCES chats(chatID) ON DELETE CASCADE,
    CONSTRAINT fk_membership_user FOREIGN KEY (userID) REFERENCES users(userID) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create a table for messages
CREATE TABLE IF NOT EXISTS messages (
    messageID BIGINT AUTO_INCREMENT PRIMARY KEY,
    chatID BIGINT NOT NULL,
    userID BIGINT NOT NULL,
    message_text VARCHAR(2000),
    sending_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_message_chat FOREIGN KEY (chatID) REFERENCES chats(chatID) ON DELETE CASCADE,
    CONSTRAINT fk_message_user FOREIGN KEY (userID) REFERENCES users(userID) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create a table for chat invitations
CREATE TABLE IF NOT EXISTS invites (
    inviteID BIGINT AUTO_INCREMENT PRIMARY KEY,
    chatID BIGINT NOT NULL,
    invited_by_userID BIGINT NOT NULL,
    invite_name VARCHAR(255),
    expiration_date DATETIME,
    active BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_invite_chat FOREIGN KEY (chatID) REFERENCES chats(chatID) ON DELETE CASCADE,
    CONSTRAINT fk_invite_user FOREIGN KEY (invited_by_userID) REFERENCES users(userID) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create a table for tokens
CREATE TABLE IF NOT EXISTS tokens (
    tokenID BIGINT AUTO_INCREMENT PRIMARY KEY,
    logged_out BOOLEAN NOT NULL DEFAULT false,
    token VARCHAR(255) NOT NULL,
    userID BIGINT NOT NULL,
    CONSTRAINT fk_token_user FOREIGN KEY (userID) REFERENCES users(userID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
