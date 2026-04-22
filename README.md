# 🏥 Mediconsulta - Sistema Integral de Gestión de Consultas Médicas

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.5-brightgreen?style=for-the-badge&logo=spring-boot)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.1-005f0f?style=for-the-badge&logo=thymeleaf)
![Tests](https://img.shields.io/badge/Tests-7%2F7%20Passed-success?style=for-the-badge)

## 📖 Sobre el Proyecto
**Mediconsulta** es una solución robusta desarrollada para digitalizar y optimizar la administración de citas en centros médicos. A diferencia de un CRUD convencional, este sistema implementa capas de validación empresarial (Business Rules) que garantizan que el flujo de trabajo clínico sea coherente, seguro y libre de errores humanos.

El objetivo principal es proporcionar a los administrativos una herramienta confiable donde la integridad de los datos sea la prioridad, asegurando que cada paciente sea asignado al especialista correcto en el horario adecuado.

---

## 🚀 Arquitectura y Diseño
El proyecto sigue el patrón de diseño **MVC (Model-View-Controller)** y se adhiere a los principios de **Clean Code**, facilitando el mantenimiento y la escalabilidad:

* **Capa de Modelo:** Entidades JPA con validaciones de Jakarta (Bean Validation) y RegEx avanzado.
* **Capa de Servicio:** Centraliza la lógica de negocio compleja, desacoplando la base de datos de la interfaz de usuario.
* **Capa de Controlador:** Manejo dual de peticiones (Vistas con Thymeleaf y API REST para integraciones futuras).
* **Inyección de Dependencias:** Implementada mediante constructores con Lombok para asegurar la inmutabilidad y facilitar el testing.

---

## ✨ Funcionalidades y Reglas de Negocio

### 🛡️ Validación Estricta de Identidad
Se ha implementado un filtro de seguridad mediante **Expresiones Regulares (RegEx)** para el campo de pacientes:
* Bloqueo automático de caracteres especiales y números en nombres.
* Soporte completo para tildes y caracteres especiales del español (Ñ, Á, É, etc.).

### 🕒 Control Inteligente de Tiempos
El sistema posee "conciencia temporal" para evitar errores logísticos:
* **Restricción de Pasado:** Es imposible agendar citas en fechas o de horas que ya han ocurrido.
* **Disponibilidad Médica:** Validación en tiempo real para evitar que un mismo médico tenga dos consultas programadas en el mismo minuto.

### 🚨 Sistema de Turnos Diferenciado
Implementamos una lógica de horarios basada en roles médicos:
* **Especialistas:** Disponibles en horario administrativo estándar (06:00 - 18:00).
* **Unidad de Emergencias:** Flujo exclusivo para la guardia nocturna (22:00 - 07:00), restringiendo el acceso a especialistas no autorizados en horarios críticos.

---

## 🛠️ Stack Tecnológico
* **Lenguaje:** Java 21.
* **Framework:** Spring Boot 4.0.5
* **Motor de Plantillas:** Thymeleaf (Renderizado dinámico del lado del servidor).
* **Estilos:** Bootstrap 5 + Custom CSS para una experiencia de usuario (UX) médica limpia.
* **Persistencia:** Spring Data JPA con H2 Database (entorno de desarrollo).
* **Herramientas:** Lombok, Maven, Git.

---

## 🧪 Calidad Garantizada (Unit Testing)
La fiabilidad del sistema está respaldada por una cobertura de pruebas exhaustiva utilizando **JUnit 5** y **Mockito**. Se validan mecánicamente los siguientes escenarios:

| Test Case | Descripción |
| :--- | :--- |
| **Nombre Inválido** | Verifica que el sistema rechace nombres con números o símbolos. |
| **Fecha Pasada** | Asegura que no se guarden registros en tiempos pretéritos. |
| **Duplicidad de Cita** | Valida que un médico no sea sobre-agendado. |
| **Horario Especialista** | Bloquea citas de especialistas fuera de su turno legal. |
| **Flujo Emergencias** | Confirma la correcta asignación en turnos de madrugada. |
| **Cita Inexistente** | Manejo de excepciones al intentar editar registros nulos. |
| **Eliminación Segura** | Verificación de flujo de borrado en base de datos. |

---

## 👤 Autor
DarkMafia312
