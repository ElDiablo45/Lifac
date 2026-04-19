# Memoria del Proyecto

Este documento conserva el contexto operativo que no debe perderse entre conversaciones.

## Estado observado del repositorio

- Repositorio nuevo en fase cero.
- Git inicializado con remoto `origin`, pero sin commits locales en la copia actual.
- No hay codigo fuente, configuracion de herramientas, estructura de carpetas de aplicacion ni stack verificable.
- Solo existe un archivo `.codex` vacio ademas del directorio `.git`.

## Decisiones activas

- La documentacion base se crea antes de cualquier implementacion de producto.
- Hasta que existan archivos reales, no se considera confirmado ningun stack tecnologico.
- El proyecto usara memoria persistente basada en documentos del repo: `AGENTS.md`, `docs/agent_memory.md`, `docs/app_spec.md` y `docs/roadmap.md`.
- Cualquier decision tecnica futura relevante debe quedar reflejada primero en esta memoria o en la spec antes de extender la base de codigo.

## Restricciones acordadas

- No asumir arquitectura ni funcionalidad que no este respaldada por archivos reales o decisiones explicitas.
- Adaptar la documentacion al estado real del repo, aunque ese estado sea casi vacio.
- Mantener continuidad entre conversaciones mediante documentos versionados dentro del repo.
- Priorizar commits pequenos, reversibles y con una unica intencion por hito.
- Hacer push tras cada commit relevante para conservar trazabilidad fuera de la maquina local.

## Riesgos abiertos

- El producto real aun no esta definido en el repositorio mas alla del nombre `Lifac`.
- No hay informacion suficiente para fijar dominio de negocio, usuarios objetivo ni alcance funcional.
- No existe stack confirmado, por lo que cualquier bootstrap tecnico prematuro podria generar retrabajo.
- El remoto `origin/main` aparece sin referencia util en la copia local actual; conviene validar mas adelante el estado real de la rama remota si afecta al flujo de trabajo.

## Contexto que no debe perderse

- Este repositorio fue preparado explicitamente para trabajar con memoria persistente y coordinacion de agentes desde el inicio.
- El objetivo inmediato no es implementar producto, sino dejar una base documental que permita futuras conversaciones con contexto consistente.
- La ausencia de codigo no es un error: forma parte del estado actual que debe preservarse en la documentacion.

## Supuestos validos por ahora

- `Lifac` es, como minimo, el nombre tentativo del proyecto porque coincide con el nombre del repositorio.
- El proyecto esta en etapa de definicion inicial y todavia no ha pasado por una fase formal de bootstrap tecnico.
- La siguiente conversacion util deberia aportar definicion de producto, stack o restricciones de negocio antes de generar codigo estructural.

## Disparadores de actualizacion

Actualizar este documento cuando ocurra cualquiera de estos eventos:

- Se confirme el tipo de producto o problema que resuelve `Lifac`.
- Se elija stack, framework, runtime o estrategia de despliegue.
- Se creen carpetas base de aplicacion, paquetes o servicios.
- Cambien prioridades, restricciones o criterios de calidad.
- Se cierre o aparezca un riesgo que afecte la direccion del proyecto.
