# Prevención de Duplicados en Doctores

Este plan detalla la implementación de restricciones de integridad para asegurar que no existan doctores duplicados dentro de la misma especialidad.

## User Review Required

> [!IMPORTANT]
> Se incrementará la versión de la base de datos a **5**. Esto provocará una limpieza de los datos actuales (usuarios y citas de prueba) para aplicar las nuevas reglas de unicidad de forma correcta.

## Proposed Changes

### 1. Capa de Datos (Room)

#### [MODIFY] [DoctorEntity.kt](file:///C:/ProyectoIIBM-AM/app/src/main/java/com/example/proyectoiibm/data/local/entity/DoctorEntity.kt)
- Añadir un índice único compuesto por los campos `nombre` y `especialidadId`. Esto impedirá que un mismo doctor sea registrado más de una vez en la misma especialidad.

#### [MODIFY] [SanaYaDatabase.kt](file:///C:/ProyectoIIBM-AM/app/src/main/java/com/example/proyectoiibm/data/local/SanaYaDatabase.kt)
- Incrementar la versión de la base de datos a **5**.

## Verification Plan

### Manual Verification
1.  **Limpieza**: Al iniciar la aplicación, verificar que la lista de doctores en cada especialidad sea única y no presente nombres repetidos.
2.  **Robustez**: Intentar agendar citas y navegar entre especialidades para asegurar que la integridad de la base de datos se mantiene.
