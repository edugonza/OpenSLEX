CREATE TABLE IF NOT EXISTS "class" (
    "id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    "name" TEXT NOT NULL,
    "common" BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS "attribute" (
    "id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    "name" TEXT NOT NULL,
    "classID" INTEGER NOT NULL,
    FOREIGN KEY ("classID") REFERENCES "class"("id")
);

CREATE TABLE IF NOT EXISTS "attribute_value" (
    "eventID" INTEGER NOT NULL,
    "attributeID" INTEGER NOT NULL,
    "value" TEXT,
    FOREIGN KEY ("eventID") REFERENCES "event"("id"),
    FOREIGN KEY ("attributeID") REFERENCES "attribute"("id"),
    PRIMARY KEY ("eventID","attributeID")
);

CREATE TABLE IF NOT EXISTS "collection" (
    "id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    "name" TEXT
);

CREATE TABLE IF NOT EXISTS "event" (
    "id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    "collectionID" INTEGER NOT NULL,
    FOREIGN KEY("collectionID") REFERENCES "collection"("id")
);
