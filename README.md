# CLEAR-CARD
**Citation Landscape Explorer for Analysis & Research In Cardiology Domains**

> Query, schedule, and visualize large-scale bibliometrics with a reproducible, job-driven pipeline

## Why this project exists
Modern journal-level indicators often reflect heterogeneous article types. To understand *article-level* influence—and how shifts in content mix affect journal metrics—researchers need a transparent, scalable way to:
- ingest and normalize article metadata and citation links,
- classify article types consistently (e.g., original research vs non-original research),
- compute short- and long-term impact measures (e.g., 2-year citations; model-based “fitness”),
- and explore trends interactively and reproducibly.

CLEAR-CARD provides that end-to-end workflow.

## What you can do with CLEAR-CARD
- **Interactive job builder**: assemble complex SQL-backed analyses without hand-writing SQL.
- **Asynchronous jobs**: submit analyses that run in the background via a Python worker.
- **Results**: view plots/tables; download results as **CSV** (stable) or **JSON** (beta).
- **Pin to dashboard**: save selected results to a personal dashboard.
- **Auth**: simple email/password login for per-user job lists and pins.
- **Reproducibility**: every job stores the query spec and classification settings used.

## System architecture
- React (Vite + Typescript) client,
- Java Spring Server,
- Python worker (communicating via message-handler),
- Python message handler (using gRPC)
