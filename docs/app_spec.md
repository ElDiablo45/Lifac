# Especificacion Inicial de la Aplicacion

## Resumen del proyecto

`Lifac` es una aplicacion Android nativa orientada a generar facturas de la forma mas sencilla posible. El producto esta pensado inicialmente para pequeno negocio, con un primer usuario de referencia en el sector de la construccion. La app debe funcionar en modo local, mantener los datos en el dispositivo y generar facturas completas en PDF sin depender de una cuenta ni de una nube propia.

## Objetivos funcionales actuales

- Mantener una base documental versionada que permita retomar el contexto del proyecto en futuras sesiones.
- Permitir crear facturas completas con datos fiscales, serie o numero, fecha, cliente, lineas e IVA.
- Mantener catalogo local de clientes y catalogo local de productos o conceptos.
- Generar un PDF final listo para compartir o archivar.
- Guardar y consultar las facturas en almacenamiento local del dispositivo.
- Establecer una base tecnica moderna sobre la que el producto real pueda crecer sin retrabajo innecesario.

## No objetivos iniciales

- No depender de backend ni cuenta de usuario.
- No sincronizar datos operativos a servidores propios.
- No cubrir de inicio casuisticas fiscales de muchos paises o sectores a la vez.
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

- Aplicacion Android con `compileSdk` 37.
- `targetSdk` 36.
- `minSdk` 36.
- AGP `9.1.1` con soporte Kotlin integrado.
- Kotlin como lenguaje principal.
- Soporte Kotlin integrado en AGP 9, sin aplicar `org.jetbrains.kotlin.android`.
- Compose como toolkit de UI recomendado por Google.
- Material 3 para componentes y tema.
- `ViewModel` y `StateFlow` para manejo de estado.
- Persistencia local en dispositivo.
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
- El producto debe ser usable completamente offline para la operativa principal.
- El backup externo, si existe, debe ser opcional y accionado por el usuario.

## Requisitos tecnicos pendientes de definir

- Estrategia exacta de persistencia local.
- Navegacion de la app y flujo principal.
- Modelo de numeracion de facturas y series.
- Casuistica fiscal exacta del MVP para construccion en Espana.
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

Estado actual de validacion:

- El usuario confirmo que la app ya compila y se ejecuta correctamente en un movil real.

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

- Definir el flujo exacto de facturacion para pequeno negocio.
- Identificar datos obligatorios del emisor, cliente y factura.
- Confirmar tratamiento fiscal inicial necesario para el primer usuario.
- Convertir incertidumbre en decisiones documentadas.

### Fase 3: Decisiones tecnicas de producto

- Definir persistencia local, generacion de PDF y estrategia de backup futuro.
- Definir estructura de carpetas, calidad y despliegue.
- Registrar riesgos y tradeoffs.

### Fase 4: Implementacion del primer entregable real

- Construir el MVP de facturacion local con PDF.
- Validar funcionalidad contra criterios documentados.
- Mantener roadmap y memoria sincronizados con el avance real.
