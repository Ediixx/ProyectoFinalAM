# Prevención de Duplicados en Doctores

Se ha reforzado la integridad de la base de datos para asegurar que los doctores no se repitan dentro de una misma especialidad.

## Cambios Realizados

### Capa de Datos
- **[DoctorEntity.kt](file:///C:/ProyectoIIBM-AM/app/src/main/java/com/example/proyectoiibm/data/local/entity/DoctorEntity.kt)**: Se añadió una restricción de **índice único** combinando el nombre del doctor y su especialidad. Esto garantiza a nivel de sistema que no se puedan insertar registros idénticos por error.
- **[SanaYaDatabase.kt](file:///C:/ProyectoIIBM-AM/app/src/main/java/com/example/proyectoiibm/data/local/SanaYaDatabase.kt)**: Se incrementó la versión a la **5** para forzar una limpieza de datos antiguos y reconstruir el catálogo con estas nuevas reglas de integridad.

## Verificación
- El proyecto compila correctamente.
- Al iniciar la aplicación, la base de datos se recrea automáticamente aplicando las restricciones de unicidad, lo que garantiza una lista de doctores limpia y organizada en cada especialidad.

> [!TIP]
> Recuerda que al subir de versión la base de datos, deberás crear un nuevo usuario de prueba para continuar testeando la app.
