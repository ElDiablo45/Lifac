# Especificacion Inicial de la Aplicacion

## Resumen del proyecto

`Lifac` se define inicialmente como una aplicacion Android nativa. El objetivo de esta fase es crear una base moderna, actual y mantenible, alineada con las recomendaciones de Google, con foco en Android 16 y un primer entregable minimo de tipo `Hello World`.

## Objetivos funcionales actuales

- Mantener una base documental versionada que permita retomar el contexto del proyecto en futuras sesiones.
- Bootstrapear una app Android que abra correctamente y muestre una pantalla inicial simple.
- Establecer una base tecnica moderna sobre la que el producto real pueda crecer sin retrabajo innecesario.

## No objetivos iniciales

- No construir todavia funcionalidad de negocio mas alla del `Hello World`.
- No introducir navegacion, persistencia, red o inyeccion de dependencias sin necesidad real.
- No modularizar prematuramente el proyecto antes de conocer el producto.

## Arquitectura actual o propuesta

### Arquitectura actual verificada

- Capa documental persistente:
  - `AGENTS.md` como indice operativo.
  - `docs/agent_memory.md` como memoria persistente.
  - `docs/app_spec.md` como especificacion viva.
  - `docs/roadmap.md` como plan operativo.
- Capa de aplicacion implementada en el bootstrap:
  - una sola `Activity`,
  - UI en Jetpack Compose,
  - `ViewModel` como state holder,
  - flujo de datos unidireccional desde estado hacia UI,
  - modulo unico `app`.

### Arquitectura propuesta para la siguiente fase

- Aplicacion Android con `compileSdk` y `targetSdk` 36.
- `minSdk` inicial 29.
- Kotlin como lenguaje principal.
- Soporte Kotlin integrado en AGP 9, sin aplicar `org.jetbrains.kotlin.android`.
- Compose como toolkit de UI recomendado por Google.
- Material 3 para componentes y tema.
- `ViewModel` y `StateFlow` para manejo de estado.
- Estructura inicial simple de modulo unico `app`, con posibilidad de modularizar mas adelante si el producto lo exige.

## Requisitos tecnicos conocidos

- El repositorio debe mantener documentacion persistente y versionada desde el inicio.
- La base debe alinearse con recomendaciones modernas de Android:
  - Compose,
  - arquitectura por capas,
  - `ViewModel`,
  - UDF,
  - Material 3.
- El historial git debe mantenerse limpio, pequeno y reversible.
- La configuracion debe ser apta para abrirse directamente en Android Studio y sincronizarse con Gradle.

## Requisitos tecnicos pendientes de definir

- `minSdk` definitivo segun objetivo comercial real.
- Estrategia de persistencia de datos.
- Navegacion de la app.
- Entorno de despliegue y distribucion.
- CI/CD.
- Politicas de observabilidad, seguridad y configuracion.

## Plan de validacion

Antes de ampliar la aplicacion real, validar como minimo:

1. Definicion del producto y del usuario objetivo.
2. Alcance del primer entregable.
3. Si el bootstrap Android abre, sincroniza y compila correctamente en Android Studio.
4. Criterios de exito del MVP.
5. Convenciones de desarrollo, testing y despliegue.

Despues de esa validacion, la implementacion deberia arrancar con un bootstrap minimo y verificable.

## Fases de implementacion

### Fase 0: Fundacion documental

- Crear memoria persistente y documentos operativos.
- Dejar explicito el estado real del repositorio.
- Preparar el terreno para decisiones futuras.

### Fase 1: Bootstrap Android

- Crear el proyecto Android base.
- Alinear herramientas y versiones modernas.
- Dejar una pantalla inicial funcional.
- Resultado esperado de esta fase: proyecto sincronizable en Android Studio y ejecutable como `Hello World`.

### Fase 2: Definicion de producto

- Describir el problema que resuelve `Lifac`.
- Identificar usuarios, casos de uso y alcance inicial.
- Convertir incertidumbre en decisiones documentadas.

### Fase 3: Decisiones tecnicas de producto

- Elegir stack y arquitectura inicial.
- Definir estructura de carpetas, calidad y despliegue.
- Registrar riesgos y tradeoffs.

### Fase 4: Implementacion del primer entregable real

- Construir el MVP o primera capacidad operativa.
- Validar funcionalidad contra criterios documentados.
- Mantener roadmap y memoria sincronizados con el avance real.
