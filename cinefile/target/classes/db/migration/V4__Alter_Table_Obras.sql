ALTER TABLE obras
ADD COLUMN IF NOT EXISTS active BOOLEAN;
UPDATE obras SET active = true;

