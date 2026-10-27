-- MySQL schema for the Hotel Management System.
-- The application can create/update these tables through Hibernate (ddl-auto=update).
-- For a new database, execute this script first if you prefer managing schema manually.
CREATE DATABASE IF NOT EXISTS hotel_management_db
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE hotel_management_db;

CREATE TABLE IF NOT EXISTS rooms (
  id BIGINT NOT NULL AUTO_INCREMENT,
  room_number VARCHAR(20) NOT NULL,
  room_type VARCHAR(40) NOT NULL,
  capacity INT NOT NULL,
  price_per_night DECIMAL(10,2) NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
  PRIMARY KEY (id),
  UNIQUE KEY uk_rooms_room_number (room_number)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS guests (
  id BIGINT NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(60) NOT NULL,
  last_name VARCHAR(60) NOT NULL,
  email VARCHAR(150) NOT NULL,
  phone VARCHAR(25),
  PRIMARY KEY (id),
  UNIQUE KEY uk_guests_email (email)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS bookings (
  id BIGINT NOT NULL AUTO_INCREMENT,
  guest_id BIGINT NOT NULL,
  room_id BIGINT NOT NULL,
  check_in DATE NOT NULL,
  check_out DATE NOT NULL,
  total_price DECIMAL(10,2) NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'CONFIRMED',
  notes VARCHAR(500),
  PRIMARY KEY (id),
  KEY idx_booking_dates (check_in, check_out),
  KEY idx_booking_status (status),
  CONSTRAINT fk_bookings_guest FOREIGN KEY (guest_id) REFERENCES guests(id),
  CONSTRAINT fk_bookings_room FOREIGN KEY (room_id) REFERENCES rooms(id)
) ENGINE=InnoDB;
