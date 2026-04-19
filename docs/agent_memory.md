# Memoria del Proyecto

Este documento conserva el contexto operativo que no debe perderse entre conversaciones.

## Estado observado del repositorio

- Repositorio inicializado para trabajo con memoria persistente.
- A partir de esta conversacion, el proyecto pasa a definirse como una app Android nativa.
- El bootstrap Android actual ya es el estado base validado del proyecto.
- El entorno de shell actual no expone `java` ni `gradle` en `PATH`, por lo que la validacion completa desde terminal puede depender de Android Studio.
- El repo ya contiene bootstrap Android real con wrapper, modulo `app`, tests base y UI Compose inicial.
- El bootstrap ya fue validado por el usuario en Android Studio y ejecutado con exito en un movil real.
- La primera iteracion visual del producto ya reemplaza el `Hello World` por un shell navegable del MVP.
- La seccion `Clientes` ya es la primera capacidad real persistida localmente.
- `Nueva factura` ya puede seleccionar clientes reales guardados localmente.

## Decisiones activas

- `Lifac` se implementa como aplicacion Android nativa.
- Producto actual: app para generar facturas de la forma mas sencilla posible.
- Enfoque de datos: local-first, sin backend y sin subir datos operativos del usuario a servidores propios.
- Primer usuario objetivo: pequeno negocio, concretamente una pequena constructora.
- Los destinatarios de las facturas podran ser tanto empresas como particulares.
- Salida principal del sistema: factura completa en PDF.
- El sistema debe incluir desde el inicio catalogo local de clientes y catalogo local de productos o conceptos.
- Debe soportar facturas completas con datos fiscales, IVA, serie o numero y fecha.
- La numeracion debe ser automatica por serie desde el primer dia.
- Los datos fijos del emisor deben guardarse en ajustes y reutilizarse sin pedirlos en cada factura.
- Las lineas de factura se modelan inicialmente como `conceptos`, sin separar todavia entre producto y servicio.
- Copias de seguridad exportables son deseables a futuro, con posibilidad prevista de backup a Google Drive.
- Propuesta operativa actual del MVP:
  - `Inicio`, `Facturas`, `Clientes`, `Conceptos` y `Ajustes` como pantallas base,
  - flujo principal de nueva factura en pasos cortos,
  - numeracion automatica por serie,
  - borradores guardables localmente,
  - PDF como salida final del flujo.
- El estado actual de UI usa datos de ejemplo y placeholders intencionados en campos todavia no decididos.
- El resto de secciones siguen usando datos de ejemplo, pero `Clientes` ya guarda datos reales en base local Room.
- El flujo de borrador de factura sigue siendo parcial, pero su paso de cliente ya no depende de datos ficticios.
- Plataforma objetivo inicial: Android 16 como base operativa, con compilacion preparada contra Android 17.
- Configuracion SDK actual:
  - `minSdk = 36` para soportar solo Android 16 o superior,
  - `targetSdk = 36` para seguir apuntando formalmente a Android 16,
  - `compileSdk = 37` para compilar con el SDK mas reciente disponible.
- Tooling base actual:
  - AGP `9.1.1`,
  - plugin `org.gradle.toolchains.foojay-resolver-convention` aplicado en `settings.gradle.kts`,
  - `buildToolsVersion = "36.1.0"` fijado en el modulo `app`.
- Stack base: Kotlin integrado en AGP 9, Jetpack Compose, Material 3, Gradle Kotlin DSL, version catalog y wrapper de Gradle.
- Arquitectura inicial recomendada: una sola `Activity`, UI en Compose, estado expuesto desde `ViewModel` y flujo de datos unidireccional.
- Namespace y `applicationId` iniciales: `io.github.eldiablo45.lifac`.
- Se elimina el plugin `org.jetbrains.kotlin.android` porque AGP 9 ya aporta soporte Kotlin integrado.
- El estado base funcional incluye tambien el tema actual `android:Theme.DeviceDefault.NoActionBar`.
- La documentacion base se actualiza antes de cambios estructurales relevantes.
- El proyecto mantiene memoria persistente basada en `AGENTS.md`, `docs/agent_memory.md`, `docs/app_spec.md` y `docs/roadmap.md`.
- La logica fiscal inicial no debe asumir un unico caso fijo de construccion; debe contemplar al menos IVA normal y dejar preparada una via para operaciones con inversion del sujeto pasivo.

## Restricciones acordadas

- No asumir producto o dominio de negocio mas alla de que el primer entregable es una app Android base.
- Priorizar tecnologias estables y recomendadas por Google frente a previews innecesarias para el arranque.
- Mantener continuidad entre conversaciones mediante documentos versionados dentro del repo.
- Priorizar commits pequenos, reversibles y con una unica intencion por hito.
- Hacer push tras cada commit relevante para conservar trazabilidad fuera de la maquina local.
- No introducir complejidad de arquitectura, navegacion o DI antes de que el producto la necesite.

## Riesgos abiertos

- El producto real ya esta definido a alto nivel, pero faltan los detalles exactos del MVP.
- El flujo fiscal de una pequena constructora puede no reducirse siempre a "solo IVA"; hay casos oficiales de inversion del sujeto pasivo en ejecuciones de obra.
- La shell actual no expone JDK ni Gradle, por lo que el primer sync y algunas validaciones podrian depender de Android Studio.
- La validacion CLI sigue pendiente en este entorno, aunque el usuario ya confirmo sync, build y ejecucion correctos desde Android Studio.

## Contexto que no debe perderse

- Este repositorio fue preparado explicitamente para trabajar con memoria persistente y coordinacion de agentes desde el inicio.
- En esta conversacion se define por primera vez la direccion tecnica del proyecto: Android nativo moderno.
- El objetivo de producto ahora es facturacion extremadamente simple para pequeno negocio.
- El valor principal es rapidez, simplicidad y privacidad local.
- La arquitectura debe mantenerse simple hasta que el producto real exija capas adicionales.
- Este bootstrap ya debe considerarse el estado base estable desde el que arrancar el desarrollo del producto.

## Supuestos validos por ahora

- `Lifac` es el nombre de la aplicacion por coincidir con el repositorio.
- El identificador de paquete inicial puede derivarse del remoto GitHub y usarse temporalmente como `io.github.eldiablo45.lifac` hasta que exista uno definitivo.
- La app se orienta solo a Android 16 o superior, por decision explicita del proyecto en esta etapa.
- El MVP deberia centrarse en crear factura, guardar borrador, generar PDF y consultar historial local.
- El MVP debe contemplar clientes empresa y clientes particular.
- La propuesta actual separa los datos del cliente segun tipo, pero mantiene un unico flujo de facturacion.
- La siguiente iteracion tecnica con mas valor es persistencia local real para clientes o borradores de factura.
- La siguiente iteracion con mas valor ahora es persistir el primer borrador de factura o conectar conceptos reales al editor.
- Salvo confirmacion posterior, la politica inicial de datos es "todo se queda en el dispositivo" y cualquier backup externo debe ser accion explicita del usuario.

## Disparadores de actualizacion

Actualizar este documento cuando ocurra cualquiera de estos eventos:

- Se confirme el tipo de producto o problema que resuelve `Lifac`.
- Se elija stack, framework, runtime o estrategia de despliegue.
- Se creen carpetas base de aplicacion, paquetes o servicios.
- Cambien prioridades, restricciones o criterios de calidad.
- Se cierre o aparezca un riesgo que afecte la direccion del proyecto.
