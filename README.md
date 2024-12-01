# Marvel API

Este proyecto es una API que interactúa con la API pública de Marvel para obtener información sobre personajes y cómics. Está construido utilizando **Spring Boot** para el backend y **Java** como lenguaje de programación. Los datos obtenidos se almacenan en una base de datos MySQL.

## Arquitectura

La aplicación sigue una arquitectura de **microservicios** con una capa de servicios que interactúa con la API de Marvel y maneja la persistencia de datos. Utiliza Spring Boot para facilitar la creación y despliegue del servicio backend.

### Componentes principales

1. **Controladores**: Controlan las solicitudes HTTP, enrutando las peticiones al servicio adecuado.
2. **Servicios**: Contienen la lógica del negocio para interactuar con la API de Marvel y almacenar datos en la base de datos.
3. **Repositorios**: Proporcionan una capa de abstracción para interactuar con la base de datos usando JPA.
4. **Modelos**: Representan las entidades de la base de datos, como `Character`, `Comic`, `Search`, entre otras.

### Librerías principales utilizadas

- **Spring Boot**: Framework para el desarrollo de aplicaciones Java basadas en microservicios.
- **Spring Data JPA**: Para la interacción con la base de datos MySQL usando JPA.
- **RestTemplate**: Para realizar solicitudes HTTP a la API de Marvel.
- **Jackson**: Para el mapeo de objetos JSON a clases Java y viceversa.
- **MySQL**: Sistema de gestión de bases de datos utilizado para almacenar la información.

## Requisitos

1. **Java 17 o superior**.
2. **Spring Boot**.
3. **MySQL**: Para la base de datos.
4. **Maven**: Para la gestión de dependencias.

## Instrucciones para construir y ejecutar la aplicación

### 1. Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/marvel-api.git
