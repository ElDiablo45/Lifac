# Memoria del Proyecto

Este documento conserva el contexto operativo que no debe perderse entre conversaciones.

## Estado observado del repositorio

- Repositorio inicializado para trabajo con memoria persistente.
- A partir de esta conversacion, el proyecto pasa a definirse como una app Android nativa.
- El siguiente hito operativo es bootstrapear una app minima funcional tipo `Hello World`.
- El entorno de shell actual no expone `java` ni `gradle` en `PATH`, por lo que la validacion completa desde terminal puede depender de Android Studio.
- El repo ya contiene bootstrap Android real con wrapper, modulo `app`, tests base y UI Compose inicial.

## Decisiones activas

- `Lifac` se implementa como aplicacion Android nativa.
- Plataforma objetivo inicial: Android 16 como base operativa, con compilacion preparada contra Android 17.
- Configuracion SDK actual:
  - `minSdk = 36` para soportar solo Android 16 o superior,
  - `targetSdk = 36` para seguir apuntando formalmente a Android 16,
  - `compileSdk = 37` para compilar con el SDK mas reciente disponible.
- Stack base: Kotlin integrado en AGP 9, Jetpack Compose, Material 3, Gradle Kotlin DSL, version catalog y wrapper de Gradle.
- Arquitectura inicial recomendada: una sola `Activity`, UI en Compose, estado expuesto desde `ViewModel` y flujo de datos unidireccional.
- Namespace y `applicationId` iniciales: `io.github.eldiablo45.lifac`.
- Se elimina el plugin `org.jetbrains.kotlin.android` porque AGP 9 ya aporta soporte Kotlin integrado.
- La documentacion base se actualiza antes de cambios estructurales relevantes.
- El proyecto mantiene memoria persistente basada en `AGENTS.md`, `docs/agent_memory.md`, `docs/app_spec.md` y `docs/roadmap.md`.

## Restricciones acordadas

- No asumir producto o dominio de negocio mas alla de que el primer entregable es una app Android base.
- Priorizar tecnologias estables y recomendadas por Google frente a previews innecesarias para el arranque.
- Mantener continuidad entre conversaciones mediante documentos versionados dentro del repo.
- Priorizar commits pequenos, reversibles y con una unica intencion por hito.
- Hacer push tras cada commit relevante para conservar trazabilidad fuera de la maquina local.
- No introducir complejidad de arquitectura, navegacion o DI antes de que el producto la necesite.

## Riesgos abiertos

- El producto real aun no esta definido mas alla de ser una app Android nueva.
- No hay informacion suficiente para fijar dominio de negocio, usuarios objetivo ni alcance funcional.
- La shell actual no expone JDK ni Gradle, por lo que el primer sync y algunas validaciones podrian depender de Android Studio.
- La compatibilidad exacta con herramientas locales instaladas por el usuario debe comprobarse en el primer sync del IDE.
- Aun no esta verificado en este entorno que todas las dependencias resuelvan y compilen correctamente en tu Android Studio.

## Contexto que no debe perderse

- Este repositorio fue preparado explicitamente para trabajar con memoria persistente y coordinacion de agentes desde el inicio.
- En esta conversacion se define por primera vez la direccion tecnica del proyecto: Android nativo moderno.
- El objetivo inmediato es crear una base fresca, actual y duradera para Android 16 con un primer `Hello World`.
- La arquitectura debe mantenerse simple hasta que el producto real exija capas adicionales.

## Supuestos validos por ahora

- `Lifac` es el nombre de la aplicacion por coincidir con el repositorio.
- El identificador de paquete inicial puede derivarse del remoto GitHub y usarse temporalmente como `io.github.eldiablo45.lifac` hasta que exista uno definitivo.
- La app se orienta solo a Android 16 o superior, por decision explicita del proyecto en esta etapa.
- El primer entregable funcional sera una unica pantalla de bienvenida en Compose.

## Disparadores de actualizacion

Actualizar este documento cuando ocurra cualquiera de estos eventos:

- Se confirme el tipo de producto o problema que resuelve `Lifac`.
- Se elija stack, framework, runtime o estrategia de despliegue.
- Se creen carpetas base de aplicacion, paquetes o servicios.
- Cambien prioridades, restricciones o criterios de calidad.
- Se cierre o aparezca un riesgo que afecte la direccion del proyecto.
