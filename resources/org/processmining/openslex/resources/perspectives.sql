CREATE TABLE IF NOT EXISTS "perspective" (
    "id" INTEGER PRIMARY KEY NOT NULL,
    "name" TEXT NOT NULL,
    "collectionID" INTEGER NOT NULL,
    "collectionFileName" TEXT
);

CREATE TABLE IF NOT EXISTS "trace" (
    "id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    "caseID" TEXT NOT NULL,
    "perspectiveID" INTEGER NOT NULL,
    FOREIGN KEY ("perspectiveID") REFERENCES "perspective"("id")
);

CREATE TABLE IF NOT EXISTS "trace_has_event" (
    "traceID" INTEGER NOT NULL,
    "eventID" INTEGER NOT NULL,
    "ordering" INTEGER NOT NULL,
    FOREIGN KEY ("traceID") REFERENCES "trace"("id"),
    PRIMARY KEY ("traceID","eventID")
);
