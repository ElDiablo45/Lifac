# Roadmap Vivo

## Completado

- [x] Inspeccion inicial del repositorio real antes de asumir arquitectura o stack.
- [x] Confirmacion de que el repo esta practicamente vacio y sin codigo de aplicacion.
- [x] Creacion del sistema base de memoria persistente y coordinacion documental.
- [x] Definicion del orden de lectura operativo en `AGENTS.md`.
- [x] Definicion de la plataforma inicial del proyecto como app Android nativa.
- [x] Definicion de objetivo tecnico inicial: base moderna para Android 16.
- [x] Creacion del bootstrap Android con Gradle Kotlin DSL, wrapper y modulo `app`.
- [x] Implementacion de una primera pantalla `Hello World` en Compose.
- [x] Validacion del proyecto en Android Studio.
- [x] Ejecucion correcta de la app en un movil real.
- [x] Consolidacion del bootstrap actual como estado base del repositorio.
- [x] Definicion del producto a alto nivel como app de facturacion local para pequeno negocio.
- [x] Sustitucion del `Hello World` por un shell navegable del MVP.
- [x] Primera capacidad real persistida: alta y listado local de clientes.
- [x] Conexion de `Nueva factura` con clientes reales guardados.
- [x] Flujo util entre `Nueva factura` y `Clientes` para seleccionar cliente sin salir del contexto.
- [x] Persistencia local del primer borrador activo de factura.
- [x] Persistencia y edicion local de lineas reales dentro del borrador activo.
- [x] Catalogo local de conceptos con reutilizacion desde el borrador.
- [x] Persistencia de facturas reales y reemplazo del historial mock.

## En curso

- [ ] Traducir la idea general del producto a un MVP concreto de pantallas y datos.
- [ ] Revisar y validar la propuesta de MVP documentada.
- [ ] Iterar el shell visual con placeholders hasta convertir cada seccion en funcionalidad real.

## Pendiente

- [ ] Definir flujo de creacion de factura de extremo a extremo.
- [ ] Definir modelo de datos local para emisor, productos, facturas y lineas.
- [ ] Diseñar la arquitectura funcional del producto sobre la base Android creada.
- [ ] Diseñar la salida PDF inicial.
- [ ] Confirmar casuistica fiscal minima del MVP para una pequena constructora en Espana.
- [ ] Definir numeracion automatica por serie.
- [ ] Definir ajustes persistentes del emisor.
- [ ] Definir diferencias de captura entre cliente empresa y cliente particular.
- [ ] Confirmar cuando un borrador debe consumir o no numero definitivo.
- [ ] Implementar el primer entregable funcional.
- [ ] Configurar estrategia de testing.
- [ ] Configurar flujo de integracion y despliegue.
- [ ] Diseñar backup local-exportable y futura integracion opcional con Google Drive.

## Riesgos abiertos

- [ ] La simplicidad percibida puede romperse si el flujo fiscal y de numeracion se vuelve demasiado configurable.
- [ ] El `minSdk` actual de Android 16+ es una decision fuerte que debera confirmarse frente al alcance comercial real.
- [ ] Para construccion no siempre basta con "solo IVA"; puede haber operaciones con inversion del sujeto pasivo.
- [ ] La shell actual no expone JDK ni Gradle, asi que la validacion inicial sigue dependiendo del IDE local del usuario.

## Criterio de avance inmediato

Se puede avanzar al siguiente hito cuando exista una descripcion breve y verificable de:

- que es `Lifac`,
- para quien se construye,
- cual es el primer problema a resolver,
- que restricciones tecnicas o de negocio condicionan el arranque.

Y, en el corto plazo tecnico, cuando:

- el proyecto sincronice en Android Studio,
- la app instale en emulador o dispositivo,
- la pantalla inicial se renderice correctamente.

## Siguiente hito recomendado

- [ ] Abrir facturas guardadas desde historial o generar PDF real a partir de ellas.
