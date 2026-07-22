-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: pozzeroterososanaranjo
-- ------------------------------------------------------
-- Server version	5.5.5-10.4.32-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `ciudad`
--

LOCK TABLES `ciudad` WRITE;
/*!40000 ALTER TABLE `ciudad` DISABLE KEYS */;
INSERT INTO `ciudad` VALUES (1,'Resistencia',1),(2,'Corrientes',2);
/*!40000 ALTER TABLE `ciudad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `contrato`
--

LOCK TABLES `contrato` WRITE;
/*!40000 ALTER TABLE `contrato` DISABLE KEYS */;
INSERT INTO `contrato` VALUES (1,'test',30,6,_binary '\0','ACTIVO','2026-07-22',50000.00,2,1);
/*!40000 ALTER TABLE `contrato` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `factura`
--

LOCK TABLES `factura` WRITE;
/*!40000 ALTER TABLE `factura` DISABLE KEYS */;
INSERT INTO `factura` VALUES (1,'test',_binary '\0','PAGADA','2026-07-22','2026-07-22','2026-07-30',50000.00,50000.00,0.00,'EFECTIVO',1);
/*!40000 ALTER TABLE `factura` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `historial_estado_contrato`
--

LOCK TABLES `historial_estado_contrato` WRITE;
/*!40000 ALTER TABLE `historial_estado_contrato` DISABLE KEYS */;
INSERT INTO `historial_estado_contrato` VALUES (1,'ACTIVO','2026-07-22 15:45:17.000000',1);
/*!40000 ALTER TABLE `historial_estado_contrato` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `historial_estado_factura`
--

LOCK TABLES `historial_estado_factura` WRITE;
/*!40000 ALTER TABLE `historial_estado_factura` DISABLE KEYS */;
INSERT INTO `historial_estado_factura` VALUES (1,'PENDIENTE','2026-07-22 15:45:56.000000',1),(2,'PAGADA','2026-07-22 15:46:34.000000',1);
/*!40000 ALTER TABLE `historial_estado_factura` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `historial_estado_incidente`
--

LOCK TABLES `historial_estado_incidente` WRITE;
/*!40000 ALTER TABLE `historial_estado_incidente` DISABLE KEYS */;
/*!40000 ALTER TABLE `historial_estado_incidente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `historial_estado_propiedad`
--

LOCK TABLES `historial_estado_propiedad` WRITE;
/*!40000 ALTER TABLE `historial_estado_propiedad` DISABLE KEYS */;
INSERT INTO `historial_estado_propiedad` VALUES (1,'DISPONIBLE','2026-07-21 13:54:31.000000',1);
/*!40000 ALTER TABLE `historial_estado_propiedad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `historial_estado_publicacion`
--

LOCK TABLES `historial_estado_publicacion` WRITE;
/*!40000 ALTER TABLE `historial_estado_publicacion` DISABLE KEYS */;
INSERT INTO `historial_estado_publicacion` VALUES (1,'ACTIVA','2026-07-22 15:43:47.000000',1);
/*!40000 ALTER TABLE `historial_estado_publicacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `incidente`
--

LOCK TABLES `incidente` WRITE;
/*!40000 ALTER TABLE `incidente` DISABLE KEYS */;
/*!40000 ALTER TABLE `incidente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `persona`
--

LOCK TABLES `persona` WRITE;
/*!40000 ALTER TABLE `persona` DISABLE KEYS */;
INSERT INTO `persona` VALUES (1,'Pozzer','42579253','corrientes 857',_binary '\0','pozzernico@gmail.com','Nicolas','3644433563',1),(2,'Prueba','34758302','albear 353',_binary '\0','roberto123@gmail.com','Roberto','3534334849',1);
/*!40000 ALTER TABLE `persona` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `propiedad`
--

LOCK TABLES `propiedad` WRITE;
/*!40000 ALTER TABLE `propiedad` DISABLE KEYS */;
INSERT INTO `propiedad` VALUES (1,4,'Casa Grande 4 Habitaciones','Tamarugal 245',_binary '\0','ALQUILADA',100,'CASA',1,1);
/*!40000 ALTER TABLE `propiedad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `provincia`
--

LOCK TABLES `provincia` WRITE;
/*!40000 ALTER TABLE `provincia` DISABLE KEYS */;
INSERT INTO `provincia` VALUES (1,'Chaco'),(2,'Corrientes');
/*!40000 ALTER TABLE `provincia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `publicacion`
--

LOCK TABLES `publicacion` WRITE;
/*!40000 ALTER TABLE `publicacion` DISABLE KEYS */;
INSERT INTO `publicacion` VALUES (1,'test','test',_binary '\0','ACTIVA','2026-07-22',150000.00,1);
/*!40000 ALTER TABLE `publicacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `visita`
--

LOCK TABLES `visita` WRITE;
/*!40000 ALTER TABLE `visita` DISABLE KEYS */;
INSERT INTO `visita` VALUES (1,'REALIZADA','2026-07-22 15:46:00.000000',1);
/*!40000 ALTER TABLE `visita` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-22 12:56:27
