# Especificacion Inicial de la Aplicacion

## Resumen del proyecto

`Lifac` es una aplicacion Android nativa orientada a generar facturas de la forma mas sencilla posible. El producto esta pensado inicialmente para pequeno negocio, con un primer usuario de referencia en el sector de la construccion. La app debe funcionar en modo local, mantener los datos en el dispositivo y generar facturas completas en PDF sin depender de una cuenta ni de una nube propia.

## Objetivos funcionales actuales

- Mantener una base documental versionada que permita retomar el contexto del proyecto en futuras sesiones.
- Permitir crear facturas completas con datos fiscales, serie o numero, fecha, cliente, lineas e IVA.
- Mantener catalogo local de clientes y catalogo local de productos o conceptos.
- Generar un PDF final listo para compartir o archivar.
- Guardar y consultar las facturas en almacenamiento local del dispositivo.
- Soportar destinatarios empresa y particular.
- Numerar facturas automaticamente por serie.
- Reutilizar los datos del emisor desde ajustes persistentes.
- Modelar cada linea como `concepto` en el MVP inicial.
- Establecer una base tecnica moderna sobre la que el producto real pueda crecer sin retrabajo innecesario.

## Propuesta de MVP v1

### Resultado esperado

El usuario debe poder crear una factura completa en pocos minutos, guardarla localmente, regenerarla despues y exportarla como PDF sin depender de internet.

### Pantallas del MVP

1. `Inicio`
2. `Facturas`
3. `Editor de factura`
4. `Clientes`
5. `Conceptos`
6. `Ajustes`

### Rol de cada pantalla

- `Inicio`: acceso rapido a `Nueva factura`, `Facturas`, `Clientes`, `Conceptos` y `Ajustes`.
- `Facturas`: listado local, busqueda, filtro basico por estado o serie y acciones de abrir, duplicar, exportar o eliminar.
- `Editor de factura`: flujo principal de captura, con secciones cortas y progreso visible.
- `Clientes`: catalogo local con alta y edicion de clientes empresa o particular.
- `Conceptos`: catalogo local reutilizable para conceptos frecuentes.
- `Ajustes`: datos persistentes del emisor, series y preferencias futuras de backup.

### Flujo principal de nueva factura

1. Seleccionar cliente existente o crear uno nuevo.
2. Completar datos de factura:
   - serie,
   - numero autogenerado,
   - fecha de emision,
   - fecha de operacion si difiere,
   - estado borrador o emitida.
3. Añadir conceptos:
   - descripcion,
   - cantidad,
   - precio unitario,
   - descuento opcional,
   - impuesto aplicable.
4. Revisar impuestos y totales.
5. Previsualizar.
6. Generar PDF y compartir o guardar.

### Flujo secundario de facturas

- Crear borrador y retomarlo mas tarde.
- Duplicar factura anterior para acelerar la emision de facturas similares.
- Exportar de nuevo un PDF desde el historial.
- Rectificar mas adelante como fase posterior, no obligatoria para el primer corte del MVP.

## Propuesta de datos del MVP

### Emisor

- nombre o razon social
- NIF
- direccion fiscal
- codigo postal
- municipio
- provincia
- telefono opcional
- email opcional
- nombre comercial opcional

### Cliente

Campos comunes:

- tipo: `empresa` o `particular`
- nombre fiscal
- NIF o identificador fiscal
- direccion
- codigo postal
- municipio
- provincia
- telefono opcional
- email opcional
- notas opcionales

Diferencia inicial propuesta:

- `empresa`: razon social y NIF obligatorios.
- `particular`: nombre completo obligatorio y NIF configurable como obligatorio segun el caso de uso final.

### Concepto reutilizable

- nombre corto
- descripcion por defecto
- precio por defecto opcional
- impuesto por defecto
- activo o archivado

### Serie de facturacion

- codigo de serie
- siguiente numero
- descripcion opcional
- activa o inactiva

### Factura

- id local
- serie
- numero
- numero completo renderizado
- estado: `borrador`, `emitida`
- fecha de emision
- fecha de operacion opcional
- clienteId
- observaciones opcionales
- subtotal
- totalImpuestos
- total
- ruta o referencia local al PDF exportado opcional
- timestamps de creacion y actualizacion

### Linea de factura

- facturaId
- orden
- descripcion
- cantidad
- precioUnitario
- descuentoImporte opcional
- tipoImpositivo o modo fiscal
- baseLinea
- cuotaLinea
- totalLinea

## Propuesta de reglas de negocio del MVP

### Numeracion

- Cada factura emitida pertenece a una serie.
- El numero se asigna automaticamente tomando el siguiente disponible de la serie elegida.
- Los borradores no deberian consumir numero definitivo hasta la emision final, salvo que posteriormente se decida lo contrario por simplicidad tecnica.

### Cliente

- Debe existir un cliente seleccionado para emitir factura.
- La app debe permitir crear cliente sin salir del flujo de nueva factura.

### Impuestos

La propuesta inicial para el selector fiscal del MVP es:

- `IVA 21%`
- `IVA 10%`
- `Inversion del sujeto pasivo`

Interpretacion operativa inicial:

- `IVA 21%`: caso general.
- `IVA 10%`: caso reducido cuando proceda.
- `Inversion del sujeto pasivo`: la factura no repercute cuota de IVA y debe quedar preparada para incluir la mencion correspondiente en el PDF.

### PDF

- Toda factura emitida debe poder exportarse a PDF.
- El PDF debe incluir datos del emisor, cliente, numeracion, fechas, conceptos, bases, impuestos, total y observaciones.

### Datos locales

- Clientes, conceptos, series, facturas y PDFs se guardan localmente.
- No se sube ningun dato operativo a infraestructura propia.
- Cualquier backup futuro debe ser accion voluntaria del usuario.

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
  - shell navegable del MVP con persistencia local ya aplicada a clientes, conceptos, borradores y facturas,
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
- Navegacion final de la app y detalles del flujo principal.
- Modelo definitivo de numeracion de facturas y series.
- Casuistica fiscal exacta del MVP para construccion en Espana.
- Datos exactos del emisor y del cliente que seran obligatorios u opcionales.
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
- Definir numeracion automatica por serie y comportamiento de borradores.
- Validar la propuesta de pantallas y datos del MVP.
- Convertir incertidumbre en decisiones documentadas.

### Fase 3: Decisiones tecnicas de producto

- Definir persistencia local, generacion de PDF y estrategia de backup futuro.
- Definir estructura de carpetas, calidad y despliegue.
- Registrar riesgos y tradeoffs.

### Fase 4: Implementacion del primer entregable real

- Construir el MVP de facturacion local con PDF.
- Validar funcionalidad contra criterios documentados.
- Mantener roadmap y memoria sincronizados con el avance real.

## Estado actual de implementacion

- El `Hello World` inicial ya fue sustituido por un primer shell de producto.
- La app ya muestra `Inicio`, `Facturas`, `Clientes`, `Conceptos`, `Ajustes` y `Editor de factura`.
- El flujo visual ya incluye placeholders para campos aun no cerrados.
- `Clientes` ya funciona con persistencia local real.
- `Nueva factura` ya puede seleccionar clientes reales guardados.
- El flujo de cliente ya permite saltar a `Clientes` y regresar al borrador con la seleccion aplicada.
- `Nueva factura` ya guarda y recarga un borrador activo local con campos principales.
- `Nueva factura` ya permite editar lineas reales del borrador y recalcula totales locales basicos.
- `Conceptos` ya permite alta local de conceptos reutilizables y aplicarlos al borrador activo.
- `Facturas` ya puede mostrar historial real persistido a partir de facturas guardadas desde el borrador.
- Tocar una factura del historial ya permite cargarla al editor como borrador editable.
- La persistencia local del resto de entidades y la generacion real de PDF siguen pendientes.
