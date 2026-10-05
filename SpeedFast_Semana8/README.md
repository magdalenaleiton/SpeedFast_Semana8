# SpeedFast - Semana 8

Proyecto Java Swing + JDBC + MySQL para la Actividad Sumativa de Desarrollo Orientado a Objetos II.

## Requisitos
- JDK 17 o superior
- IntelliJ IDEA
- MySQL Server
- Base de datos `speedfast_db`

## 1. Crear base de datos
Ejecutar `sql/speedfast_db.sql` en MySQL Workbench.

## 2. Configurar conexión
Abrir:
`src/main/java/conexion/ConexionDB.java`

Cambiar solamente:
- `USER`
- `PASSWORD`

Por las credenciales de tu MySQL.

## 3. Ejecutar
En IntelliJ abrir el proyecto como proyecto Maven y ejecutar:
`app.Main`

La aplicación contiene pestañas para:
- Repartidores: crear, actualizar, eliminar y listar.
- Pedidos: crear, actualizar, eliminar, listar y filtrar.
- Entregas: crear, actualizar, eliminar y listar.

Los DAO utilizan `PreparedStatement` y `ResultSet`. Las operaciones SQL tienen manejo de excepciones y la interfaz valida campos obligatorios.
