CREATE OR REPLACE FUNCTION уникальный_id(имя_таблицы TEXT)
RETURNS BIGINT AS $$
DECLARE
    название_последовательности TEXT := 'послед_' || имя_таблицы;
    новый_id BIGINT;
BEGIN
    PERFORM sequence_name FROM information_schema.sequences WHERE sequence_name = название_последовательности;
    IF NOT FOUND THEN
        EXECUTE 'CREATE SEQUENCE ' || название_последовательности || ' START 1';
    END IF;

    новый_id := nextval(название_последовательности);

    RETURN новый_id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION генерация_id()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        NEW.id := уникальный_id(TG_TABLE_NAME);
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DO $$
DECLARE
    t record;
BEGIN
    FOR t IN SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'
    LOOP
        EXECUTE 'CREATE TRIGGER триггер_для_id BEFORE INSERT ON ' || t.table_name || ' FOR EACH ROW EXECUTE FUNCTION генерация_id()';
    END LOOP;
END $$;
