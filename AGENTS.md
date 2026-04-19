# AGENTS.md

## Lectura obligatoria antes de continuar desarrollo

Antes de proponer, implementar o refactorizar cualquier cambio en este repositorio, leer en este orden:

1. `docs/agent_memory.md`
2. `docs/app_spec.md`
3. `docs/roadmap.md`

## Rol de este archivo

Este archivo es el indice operativo del repositorio. Define el orden de lectura, las reglas de trabajo y el punto de partida para cualquier agente o colaborador que entre al proyecto.

## Estado actual del repositorio

- Repositorio Android inicializado con modulo `app`.
- Stack base confirmado: Kotlin, Jetpack Compose, Material 3, Gradle Kotlin DSL y version catalog.
- Wrapper de Gradle incluido en el repo.
- La app ya no esta en `Hello World`; existe un shell navegable del MVP.
- Estado base validado en Android Studio y ejecutado correctamente en un movil real.
- El estado actual incluye ajustes aplicados por Android Studio al tooling del proyecto.
- `Clientes` ya persiste datos reales en Room.
- `Nueva factura` ya reutiliza clientes reales y puede guardar un borrador activo local.
- `Conceptos` ya debe reflejar siempre su estado real en Room y cualquier acceso desde `Nueva factura` debe preservar el contexto del borrador.

## Reglas operativas del proyecto

- Explorar el repositorio real antes de asumir arquitectura, stack, flujos o convenciones.
- No inventar estado actual del proyecto. Si algo no existe en el repo, dejarlo explicitado en la documentacion.
- Documentar decisiones importantes antes de implementar cuando cambien arquitectura, stack, modelo de datos, flujos criticos o alcance.
- Mantener `docs/agent_memory.md`, `docs/app_spec.md` y `docs/roadmap.md` sincronizados con la realidad del repo.
- Registrar en `docs/agent_memory.md` las decisiones activas, restricciones, riesgos y supuestos temporales.
- Mantener commits pequenos, coherentes y faciles de revertir.
- Priorizar historial limpio y reversible sobre cambios grandes y opacos.
- Evitar cambios destructivos no solicitados. No reescribir historia sin instruccion explicita.
- Si el worktree tiene cambios ajenos, integrarlos con cuidado y no revertirlos por defecto.
- Reflejar cualquier stack real solo cuando este confirmado por archivos, configuracion o decisiones documentadas.
- Crear estructura minima nueva solo cuando sea necesaria y dejar trazabilidad documental de por que se crea.
- Validar con checks proporcionales al cambio antes de cerrar un hito.
- Hacer push despues de cada commit relevante para no perder continuidad entre sesiones.

## Coordinacion de agentes

- Cualquier agente nuevo debe empezar por la lectura obligatoria indicada arriba.
- Si una conversacion cambia prioridades o decisiones, actualizar primero la documentacion persistente y despues el codigo.
- Si hay ambiguedad sobre producto o arquitectura, anotar el supuesto en `docs/agent_memory.md` y tratarlo como temporal.
