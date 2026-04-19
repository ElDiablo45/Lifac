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

## En curso

- [ ] Consolidar una definicion minima del producto `Lifac`.

## Pendiente

- [ ] Definir problema, usuarios y casos de uso principales.
- [ ] Diseñar la arquitectura funcional del producto sobre la base Android creada.
- [ ] Implementar el primer entregable funcional.
- [ ] Configurar estrategia de testing.
- [ ] Configurar flujo de integracion y despliegue.

## Riesgos abiertos

- [ ] El producto real sigue sin estar definido mas alla de ser una app Android.
- [ ] El `minSdk` actual de Android 16+ es una decision fuerte que debera confirmarse frente al alcance comercial real.
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

- [ ] Definir el producto real en una pagina y convertir esa definicion en el siguiente corte de arquitectura y roadmap.
