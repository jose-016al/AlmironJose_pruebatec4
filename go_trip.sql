-- phpMyAdmin SQL Dump
-- version 5.1.1deb5ubuntu1
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost:3306
-- Tiempo de generación: 18-02-2025 a las 20:06:52
-- Versión del servidor: 10.6.16-MariaDB-0ubuntu0.22.04.1
-- Versión de PHP: 8.1.2-1ubuntu2.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `go_trip`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `flight`
--

CREATE TABLE `flight` (
  `id` bigint(20) NOT NULL,
  `airline` varchar(255) NOT NULL,
  `departure_date` date NOT NULL,
  `destination` varchar(255) NOT NULL,
  `flight_code` varchar(255) NOT NULL,
  `origin` varchar(255) NOT NULL,
  `return_date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `flight`
--

INSERT INTO `flight` (`id`, `airline`, `departure_date`, `destination`, `flight_code`, `origin`, `return_date`) VALUES
(1, 'Iberia', '2025-03-18', 'New York', 'MAD-NEW-3205', 'Madrid', '2025-03-19'),
(2, 'Air France', '2025-03-20', 'Paris', 'BAR-PAR-5900', 'Barcelona', '2025-03-21'),
(3, 'British Airways', '2025-03-22', 'London', 'MAD-LON-7070', 'Madrid', '2025-03-23'),
(4, 'Vueling', '2025-03-24', 'Rome', 'SEV-ROM-7339', 'Sevilla', '2025-03-25'),
(5, 'KLM', '2025-03-27', 'Amsterdam', 'MAL-AMS-2293', 'Malaga', '2025-03-28'),
(6, 'Lufthansa', '2025-03-29', 'Berlin', 'VAL-BER-9354', 'Valencia', '2025-03-30'),
(7, 'Emirates', '2025-04-01', 'Dubai', 'MAD-DUB-5718', 'Madrid', '2025-04-02'),
(8, 'Iberia', '2025-04-05', 'Rome', 'BAR-ROM-5343', 'Barcelona', '2025-04-06'),
(9, 'Air Europa', '2025-04-10', 'Madrid', 'BIL-MAD-8645', 'Bilbao', '2025-04-11'),
(10, 'TAP Portugal', '2025-04-12', 'Lisbon', 'MAL-LIS-6700', 'Malaga', '2025-04-13');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `flight_booking`
--

CREATE TABLE `flight_booking` (
  `id` bigint(20) NOT NULL,
  `total_price` double NOT NULL,
  `user_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `flight_booking`
--

INSERT INTO `flight_booking` (`id`, `total_price`, `user_id`) VALUES
(1, 350, 1),
(2, 80, 2),
(3, 320, 3),
(4, 70, 4),
(5, 370, 5),
(6, 95, 6),
(7, 1200, 7),
(8, 120, 8),
(9, 200, 9),
(10, 85, 10);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `flight_booking_detail`
--

CREATE TABLE `flight_booking_detail` (
  `id` bigint(20) NOT NULL,
  `passenger_name` varchar(255) NOT NULL,
  `passport_number` varchar(255) NOT NULL,
  `flight_booking_id` bigint(20) NOT NULL,
  `seat_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `flight_booking_detail`
--

INSERT INTO `flight_booking_detail` (`id`, `passenger_name`, `passport_number`, `flight_booking_id`, `seat_id`) VALUES
(1, 'Juan Pérez', '12345678', 1, 1),
(2, 'Ana López', '87654321', 1, 4),
(3, 'Carlos García', '23456789', 2, 7),
(4, 'Laura Martínez', '34567890', 3, 18),
(5, 'José Rodríguez', '98765432', 3, 14),
(6, 'Miguel Torres', '45678901', 4, 20),
(7, 'Elena Sánchez', '56789012', 5, 35),
(8, 'Luis Fernández', '11223344', 5, 27),
(9, 'Patricia Jiménez', '67890123', 6, 37),
(10, 'Juan Carlos Pérez', '78901234', 7, 52),
(11, 'Teresa González', '65432101', 7, 45),
(12, 'Sandra Ruiz', '89012345', 8, 55),
(13, 'David Pérez', '90123456', 9, 71),
(14, 'Inés Sánchez', '32109876', 9, 61),
(15, 'Álvaro Fernández', '01234567', 10, 75);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `hotel`
--

CREATE TABLE `hotel` (
  `id` bigint(20) NOT NULL,
  `hotel_code` varchar(255) NOT NULL,
  `location` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `stars` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `hotel`
--

INSERT INTO `hotel` (`id`, `hotel_code`, `location`, `name`, `stars`) VALUES
(1, 'MAD-1897', 'Madrid', 'Hotel Paradiso', 5),
(2, 'BAR-1609', 'Barcelona', 'Hotel Costa Azul', 4),
(3, 'SEV-5750', 'Sevilla', 'Hotel Seville Palace', 5),
(4, 'VAL-2118', 'Valencia', 'Hotel Iberostar', 4),
(5, 'MAD-6719', 'Madrid', 'Hotel El Retiro', 3),
(6, 'MAL-9495', 'Malaga', 'Hotel Ocean Breeze', 5),
(7, 'ALI-6644', 'Alicante', 'Hotel Sol y Mar', 4),
(8, 'BAR-6728', 'Barcelona', 'Hotel Royal', 3),
(9, 'GRA-4133', 'Granada', 'Hotel Plaza Mayor', 3),
(10, 'GIR-4279', 'Girona', 'Hotel Costa Brava', 4);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `hotel_booking`
--

CREATE TABLE `hotel_booking` (
  `id` bigint(20) NOT NULL,
  `check_in` date NOT NULL,
  `check_out` date NOT NULL,
  `total_price` double NOT NULL,
  `user_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `hotel_booking`
--

INSERT INTO `hotel_booking` (`id`, `check_in`, `check_out`, `total_price`, `user_id`) VALUES
(1, '2025-03-15', '2025-03-20', 1250, 1),
(2, '2025-04-01', '2025-04-05', 320, 2),
(3, '2025-03-10', '2025-03-15', 2300, 3),
(4, '2025-04-12', '2025-04-14', 280, 4),
(5, '2025-05-01', '2025-05-05', 280, 5),
(6, '2025-03-25', '2025-03-28', 840, 6),
(7, '2025-03-28', '2025-03-30', 280, 7),
(8, '2025-04-05', '2025-04-10', 375, 8),
(9, '2025-04-20', '2025-04-25', 875, 9),
(10, '2025-03-18', '2025-03-22', 880, 10);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `hotel_booking_detail`
--

CREATE TABLE `hotel_booking_detail` (
  `id` bigint(20) NOT NULL,
  `guest_id_document` varchar(255) NOT NULL,
  `guest_name` varchar(255) NOT NULL,
  `hotel_booking_id` bigint(20) NOT NULL,
  `room_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `hotel_booking_detail`
--

INSERT INTO `hotel_booking_detail` (`id`, `guest_id_document`, `guest_name`, `hotel_booking_id`, `room_id`) VALUES
(1, '12345678', 'Juan Pérez', 1, 1),
(2, '87654321', 'Laura Martínez', 1, 4),
(3, '23456789', 'Carlos García', 2, 10),
(4, '67890123', 'Patricia Jiménez', 3, 26),
(5, '98765432', 'José Rodríguez', 3, 23),
(6, '45678901', 'Miguel Torres', 4, 31),
(7, '56789012', 'Elena Sánchez', 5, 38),
(8, '11223344', 'Luis Fernández', 6, 66),
(9, '89012345', 'Sandra Ruiz', 7, 73),
(10, '90123456', 'David Pérez', 8, 79),
(11, '01234567', 'Álvaro Fernández', 9, 97),
(12, '32109876', 'Inés Sánchez', 9, 91),
(13, '65432101', 'Teresa González', 10, 115);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `room`
--

CREATE TABLE `room` (
  `id` bigint(20) NOT NULL,
  `number` int(11) NOT NULL,
  `price_per_night` double NOT NULL,
  `room_type` enum('DOUBLE','SINGLE','SUITE') NOT NULL,
  `hotel_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `room`
--

INSERT INTO `room` (`id`, `number`, `price_per_night`, `room_type`, `hotel_id`) VALUES
(1, 1, 100, 'SINGLE', 1),
(2, 2, 100, 'SINGLE', 1),
(3, 3, 100, 'SINGLE', 1),
(4, 4, 150, 'DOUBLE', 1),
(5, 5, 150, 'DOUBLE', 1),
(6, 6, 150, 'DOUBLE', 1),
(7, 7, 250, 'SUITE', 1),
(8, 8, 250, 'SUITE', 1),
(9, 9, 250, 'SUITE', 1),
(10, 1, 80, 'SINGLE', 2),
(11, 2, 80, 'SINGLE', 2),
(12, 3, 80, 'SINGLE', 2),
(13, 4, 80, 'SINGLE', 2),
(14, 5, 80, 'SINGLE', 2),
(15, 6, 130, 'DOUBLE', 2),
(16, 7, 130, 'DOUBLE', 2),
(17, 8, 130, 'DOUBLE', 2),
(18, 9, 130, 'DOUBLE', 2),
(19, 10, 200, 'SUITE', 2),
(20, 11, 200, 'SUITE', 2),
(21, 1, 120, 'SINGLE', 3),
(22, 2, 120, 'SINGLE', 3),
(23, 3, 180, 'DOUBLE', 3),
(24, 4, 180, 'DOUBLE', 3),
(25, 5, 180, 'DOUBLE', 3),
(26, 6, 280, 'SUITE', 3),
(27, 1, 95, 'SINGLE', 4),
(28, 2, 95, 'SINGLE', 4),
(29, 3, 95, 'SINGLE', 4),
(30, 4, 95, 'SINGLE', 4),
(31, 5, 140, 'DOUBLE', 4),
(32, 6, 140, 'DOUBLE', 4),
(33, 7, 140, 'DOUBLE', 4),
(34, 8, 140, 'DOUBLE', 4),
(35, 9, 140, 'DOUBLE', 4),
(36, 10, 230, 'SUITE', 4),
(37, 11, 230, 'SUITE', 4),
(38, 1, 70, 'SINGLE', 5),
(39, 2, 70, 'SINGLE', 5),
(40, 3, 70, 'SINGLE', 5),
(41, 4, 70, 'SINGLE', 5),
(42, 5, 70, 'SINGLE', 5),
(43, 6, 70, 'SINGLE', 5),
(44, 7, 120, 'DOUBLE', 5),
(45, 8, 120, 'DOUBLE', 5),
(46, 9, 120, 'DOUBLE', 5),
(47, 10, 120, 'DOUBLE', 5),
(48, 11, 120, 'DOUBLE', 5),
(49, 12, 200, 'SUITE', 5),
(50, 13, 200, 'SUITE', 5),
(51, 14, 200, 'SUITE', 5),
(52, 1, 100, 'SINGLE', 6),
(53, 2, 100, 'SINGLE', 6),
(54, 3, 100, 'SINGLE', 6),
(55, 4, 100, 'SINGLE', 6),
(56, 5, 100, 'SINGLE', 6),
(57, 6, 100, 'SINGLE', 6),
(58, 7, 100, 'SINGLE', 6),
(59, 8, 100, 'SINGLE', 6),
(60, 9, 160, 'DOUBLE', 6),
(61, 10, 160, 'DOUBLE', 6),
(62, 11, 160, 'DOUBLE', 6),
(63, 12, 160, 'DOUBLE', 6),
(64, 13, 160, 'DOUBLE', 6),
(65, 14, 160, 'DOUBLE', 6),
(66, 15, 280, 'SUITE', 6),
(67, 16, 280, 'SUITE', 6),
(68, 17, 280, 'SUITE', 6),
(69, 18, 280, 'SUITE', 6),
(70, 1, 90, 'SINGLE', 7),
(71, 2, 90, 'SINGLE', 7),
(72, 3, 90, 'SINGLE', 7),
(73, 4, 140, 'DOUBLE', 7),
(74, 5, 140, 'DOUBLE', 7),
(75, 6, 140, 'DOUBLE', 7),
(76, 7, 140, 'DOUBLE', 7),
(77, 8, 210, 'SUITE', 7),
(78, 9, 210, 'SUITE', 7),
(79, 1, 75, 'SINGLE', 8),
(80, 2, 75, 'SINGLE', 8),
(81, 3, 75, 'SINGLE', 8),
(82, 4, 75, 'SINGLE', 8),
(83, 5, 75, 'SINGLE', 8),
(84, 6, 75, 'SINGLE', 8),
(85, 7, 75, 'SINGLE', 8),
(86, 8, 125, 'DOUBLE', 8),
(87, 9, 125, 'DOUBLE', 8),
(88, 10, 125, 'DOUBLE', 8),
(89, 11, 220, 'SUITE', 8),
(90, 12, 220, 'SUITE', 8),
(91, 1, 65, 'SINGLE', 9),
(92, 2, 65, 'SINGLE', 9),
(93, 3, 65, 'SINGLE', 9),
(94, 4, 65, 'SINGLE', 9),
(95, 5, 65, 'SINGLE', 9),
(96, 6, 65, 'SINGLE', 9),
(97, 7, 110, 'DOUBLE', 9),
(98, 8, 110, 'DOUBLE', 9),
(99, 9, 110, 'DOUBLE', 9),
(100, 10, 110, 'DOUBLE', 9),
(101, 11, 110, 'DOUBLE', 9),
(102, 12, 190, 'SUITE', 9),
(103, 13, 190, 'SUITE', 9),
(104, 14, 190, 'SUITE', 9),
(105, 1, 80, 'SINGLE', 10),
(106, 2, 80, 'SINGLE', 10),
(107, 3, 80, 'SINGLE', 10),
(108, 4, 80, 'SINGLE', 10),
(109, 5, 140, 'DOUBLE', 10),
(110, 6, 140, 'DOUBLE', 10),
(111, 7, 140, 'DOUBLE', 10),
(112, 8, 140, 'DOUBLE', 10),
(113, 9, 140, 'DOUBLE', 10),
(114, 10, 140, 'DOUBLE', 10),
(115, 11, 220, 'SUITE', 10),
(116, 12, 220, 'SUITE', 10);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `seat`
--

CREATE TABLE `seat` (
  `id` bigint(20) NOT NULL,
  `available` bit(1) NOT NULL,
  `number` int(11) NOT NULL,
  `price` double NOT NULL,
  `seat_type` enum('BUSINESS','ECONOMY') NOT NULL,
  `flight_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `seat`
--

INSERT INTO `seat` (`id`, `available`, `number`, `price`, `seat_type`, `flight_id`) VALUES
(1, b'0', 1, 100, 'ECONOMY', 1),
(2, b'1', 2, 100, 'ECONOMY', 1),
(3, b'1', 3, 100, 'ECONOMY', 1),
(4, b'0', 4, 250, 'BUSINESS', 1),
(5, b'1', 5, 250, 'BUSINESS', 1),
(6, b'1', 6, 250, 'BUSINESS', 1),
(7, b'0', 1, 80, 'ECONOMY', 2),
(8, b'1', 2, 80, 'ECONOMY', 2),
(9, b'1', 3, 80, 'ECONOMY', 2),
(10, b'1', 4, 80, 'ECONOMY', 2),
(11, b'1', 5, 80, 'ECONOMY', 2),
(12, b'1', 6, 220, 'BUSINESS', 2),
(13, b'1', 7, 220, 'BUSINESS', 2),
(14, b'0', 1, 90, 'ECONOMY', 3),
(15, b'1', 2, 90, 'ECONOMY', 3),
(16, b'1', 3, 90, 'ECONOMY', 3),
(17, b'1', 4, 90, 'ECONOMY', 3),
(18, b'0', 5, 230, 'BUSINESS', 3),
(19, b'1', 6, 230, 'BUSINESS', 3),
(20, b'0', 1, 70, 'ECONOMY', 4),
(21, b'1', 2, 70, 'ECONOMY', 4),
(22, b'1', 3, 70, 'ECONOMY', 4),
(23, b'1', 4, 70, 'ECONOMY', 4),
(24, b'1', 5, 70, 'ECONOMY', 4),
(25, b'1', 6, 70, 'ECONOMY', 4),
(26, b'1', 7, 200, 'BUSINESS', 4),
(27, b'0', 1, 110, 'ECONOMY', 5),
(28, b'1', 2, 110, 'ECONOMY', 5),
(29, b'1', 3, 110, 'ECONOMY', 5),
(30, b'1', 4, 110, 'ECONOMY', 5),
(31, b'1', 5, 110, 'ECONOMY', 5),
(32, b'1', 6, 110, 'ECONOMY', 5),
(33, b'1', 7, 110, 'ECONOMY', 5),
(34, b'1', 8, 110, 'ECONOMY', 5),
(35, b'0', 9, 260, 'BUSINESS', 5),
(36, b'1', 10, 260, 'BUSINESS', 5),
(37, b'0', 1, 95, 'ECONOMY', 6),
(38, b'1', 2, 95, 'ECONOMY', 6),
(39, b'1', 3, 95, 'ECONOMY', 6),
(40, b'1', 4, 95, 'ECONOMY', 6),
(41, b'1', 5, 95, 'ECONOMY', 6),
(42, b'1', 6, 240, 'BUSINESS', 6),
(43, b'1', 7, 240, 'BUSINESS', 6),
(44, b'1', 8, 240, 'BUSINESS', 6),
(45, b'0', 1, 350, 'ECONOMY', 7),
(46, b'1', 2, 350, 'ECONOMY', 7),
(47, b'1', 3, 350, 'ECONOMY', 7),
(48, b'1', 4, 350, 'ECONOMY', 7),
(49, b'1', 5, 350, 'ECONOMY', 7),
(50, b'1', 6, 350, 'ECONOMY', 7),
(51, b'1', 7, 350, 'ECONOMY', 7),
(52, b'0', 8, 850, 'BUSINESS', 7),
(53, b'1', 9, 850, 'BUSINESS', 7),
(54, b'1', 10, 850, 'BUSINESS', 7),
(55, b'0', 1, 120, 'ECONOMY', 8),
(56, b'1', 2, 120, 'ECONOMY', 8),
(57, b'1', 3, 120, 'ECONOMY', 8),
(58, b'1', 4, 120, 'ECONOMY', 8),
(59, b'1', 5, 300, 'BUSINESS', 8),
(60, b'1', 6, 300, 'BUSINESS', 8),
(61, b'0', 1, 50, 'ECONOMY', 9),
(62, b'1', 2, 50, 'ECONOMY', 9),
(63, b'1', 3, 50, 'ECONOMY', 9),
(64, b'1', 4, 50, 'ECONOMY', 9),
(65, b'1', 5, 50, 'ECONOMY', 9),
(66, b'1', 6, 50, 'ECONOMY', 9),
(67, b'1', 7, 50, 'ECONOMY', 9),
(68, b'1', 8, 50, 'ECONOMY', 9),
(69, b'1', 9, 50, 'ECONOMY', 9),
(70, b'1', 10, 50, 'ECONOMY', 9),
(71, b'0', 11, 150, 'BUSINESS', 9),
(72, b'1', 12, 150, 'BUSINESS', 9),
(73, b'1', 13, 150, 'BUSINESS', 9),
(74, b'1', 14, 150, 'BUSINESS', 9),
(75, b'0', 1, 85, 'ECONOMY', 10),
(76, b'1', 2, 85, 'ECONOMY', 10),
(77, b'1', 3, 85, 'ECONOMY', 10),
(78, b'1', 4, 85, 'ECONOMY', 10),
(79, b'1', 5, 85, 'ECONOMY', 10),
(80, b'1', 6, 85, 'ECONOMY', 10),
(81, b'1', 7, 230, 'BUSINESS', 10),
(82, b'1', 8, 230, 'BUSINESS', 10);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `user`
--

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `email` varchar(255) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `passport` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `user`
--

INSERT INTO `user` (`id`, `email`, `first_name`, `last_name`, `passport`) VALUES
(1, 'juan.perez@example.com', 'Juan', 'Pérez', '12345678X'),
(2, 'ana.gomez@example.com', 'Ana', 'Gómez', '23456789Y'),
(3, 'carlos.lopez@example.com', 'Carlos', 'López', '34567890Z'),
(4, 'maria.fernandez@example.com', 'María', 'Fernández', '45678901A'),
(5, 'david.martinez@example.com', 'David', 'Martínez', '56789012B'),
(6, 'laura.hernandez@example.com', 'Laura', 'Hernández', '67890123C'),
(7, 'pedro.gonzalez@example.com', 'Pedro', 'González', '78901234D'),
(8, 'sofia.rodriguez@example.com', 'Sofía', 'Rodríguez', '89012345E'),
(9, 'ricardo.sanchez@example.com', 'Ricardo', 'Sánchez', '90123456F'),
(10, 'carmen.perez@example.com', 'Carmen', 'Pérez', '01234567G');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `flight`
--
ALTER TABLE `flight`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKj7j29duht4oswo8ylojjd5x1f` (`flight_code`);

--
-- Indices de la tabla `flight_booking`
--
ALTER TABLE `flight_booking`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKenows5co1dgrri8scbjm37i82` (`user_id`);

--
-- Indices de la tabla `flight_booking_detail`
--
ALTER TABLE `flight_booking_detail`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKq8ju4xkhju2u6d0ivmw4c69ck` (`flight_booking_id`),
  ADD KEY `FKjt27hierjyfu0hulcja5viev0` (`seat_id`);

--
-- Indices de la tabla `hotel`
--
ALTER TABLE `hotel`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `hotel_booking`
--
ALTER TABLE `hotel_booking`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKjfv1on9sx2lvu4w3eklut09ef` (`user_id`);

--
-- Indices de la tabla `hotel_booking_detail`
--
ALTER TABLE `hotel_booking_detail`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK9ira1ajms1kkih8ju2xmghwrm` (`hotel_booking_id`),
  ADD KEY `FKmtrnrae5f416n4wls9ecqj1rd` (`room_id`);

--
-- Indices de la tabla `room`
--
ALTER TABLE `room`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKdosq3ww4h9m2osim6o0lugng8` (`hotel_id`);

--
-- Indices de la tabla `seat`
--
ALTER TABLE `seat`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKeda0njvaxhowgf6120eh6hxpq` (`flight_id`);

--
-- Indices de la tabla `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `flight`
--
ALTER TABLE `flight`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `flight_booking`
--
ALTER TABLE `flight_booking`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `flight_booking_detail`
--
ALTER TABLE `flight_booking_detail`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT de la tabla `hotel`
--
ALTER TABLE `hotel`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `hotel_booking`
--
ALTER TABLE `hotel_booking`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `hotel_booking_detail`
--
ALTER TABLE `hotel_booking_detail`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT de la tabla `room`
--
ALTER TABLE `room`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=117;

--
-- AUTO_INCREMENT de la tabla `seat`
--
ALTER TABLE `seat`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=83;

--
-- AUTO_INCREMENT de la tabla `user`
--
ALTER TABLE `user`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `flight_booking`
--
ALTER TABLE `flight_booking`
  ADD CONSTRAINT `FKenows5co1dgrri8scbjm37i82` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Filtros para la tabla `flight_booking_detail`
--
ALTER TABLE `flight_booking_detail`
  ADD CONSTRAINT `FKjt27hierjyfu0hulcja5viev0` FOREIGN KEY (`seat_id`) REFERENCES `seat` (`id`),
  ADD CONSTRAINT `FKq8ju4xkhju2u6d0ivmw4c69ck` FOREIGN KEY (`flight_booking_id`) REFERENCES `flight_booking` (`id`);

--
-- Filtros para la tabla `hotel_booking`
--
ALTER TABLE `hotel_booking`
  ADD CONSTRAINT `FKjfv1on9sx2lvu4w3eklut09ef` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Filtros para la tabla `hotel_booking_detail`
--
ALTER TABLE `hotel_booking_detail`
  ADD CONSTRAINT `FK9ira1ajms1kkih8ju2xmghwrm` FOREIGN KEY (`hotel_booking_id`) REFERENCES `hotel_booking` (`id`),
  ADD CONSTRAINT `FKmtrnrae5f416n4wls9ecqj1rd` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`);

--
-- Filtros para la tabla `room`
--
ALTER TABLE `room`
  ADD CONSTRAINT `FKdosq3ww4h9m2osim6o0lugng8` FOREIGN KEY (`hotel_id`) REFERENCES `hotel` (`id`);

--
-- Filtros para la tabla `seat`
--
ALTER TABLE `seat`
  ADD CONSTRAINT `FKeda0njvaxhowgf6120eh6hxpq` FOREIGN KEY (`flight_id`) REFERENCES `flight` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
