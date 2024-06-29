CREATE OR REPLACE FUNCTION проверить_инн() RETURNS trigger AS $$
DECLARE
    сумма INTEGER := 0;
    сумма1 INTEGER := 0;
    сумма2 INTEGER := 0;
BEGIN

    IF NOT NEW.ИНН ~ '^[0-9]+$' THEN
        RAISE EXCEPTION 'ИНН должен содержать только цифры';
    END IF;

    IF (CHAR_LENGTH(NEW.ИНН) NOT IN (10, 12)) THEN
        RAISE EXCEPTION 'ИНН должен состоять из 10 или 12 символов';
    END IF;

    IF CHAR_LENGTH(NEW.ИНН) = 10 THEN
        сумма := (SUBSTR(NEW.ИНН, 1, 1)::INTEGER * 2 + SUBSTR(NEW.ИНН, 2, 1)::INTEGER * 4 +
                SUBSTR(NEW.ИНН, 3, 1)::INTEGER * 10 + SUBSTR(NEW.ИНН, 4, 1)::INTEGER * 3 +
                SUBSTR(NEW.ИНН, 5, 1)::INTEGER * 5 + SUBSTR(NEW.ИНН, 6, 1)::INTEGER * 9 +
                SUBSTR(NEW.ИНН, 7, 1)::INTEGER * 4 + SUBSTR(NEW.ИНН, 8, 1)::INTEGER * 6 +
                SUBSTR(NEW.ИНН, 9, 1)::INTEGER * 8) % 11 % 10;
        IF сумма <> SUBSTR(NEW.ИНН, 10, 1)::INTEGER THEN
            RAISE EXCEPTION 'Неправильный ИНН';
        END IF;
    ELSE
        сумма1 := (SUBSTR(NEW.ИНН, 1, 1)::INTEGER * 7 + SUBSTR(NEW.ИНН, 2, 1)::INTEGER * 2 +
                 SUBSTR(NEW.ИНН, 3, 1)::INTEGER * 4 + SUBSTR(NEW.ИНН, 4, 1)::INTEGER * 10 +
                 SUBSTR(NEW.ИНН, 5, 1)::INTEGER * 3 + SUBSTR(NEW.ИНН, 6, 1)::INTEGER * 5 +
                 SUBSTR(NEW.ИНН, 7, 1)::INTEGER * 9 + SUBSTR(NEW.ИНН, 8, 1)::INTEGER * 4 +
                 SUBSTR(NEW.ИНН, 9, 1)::INTEGER * 6 + SUBSTR(NEW.ИНН, 10, 1)::INTEGER * 8) % 11 % 10;
        сумма2 := (SUBSTR(NEW.ИНН, 1, 1)::INTEGER * 3 + SUBSTR(NEW.ИНН, 2, 1)::INTEGER * 7 +
                 SUBSTR(NEW.ИНН, 3, 1)::INTEGER * 2 + SUBSTR(NEW.ИНН, 4, 1)::INTEGER * 4 +
                 SUBSTR(NEW.ИНН, 5, 1)::INTEGER * 10 + SUBSTR(NEW.ИНН, 6, 1)::INTEGER * 3 +
                 SUBSTR(NEW.ИНН, 7, 1)::INTEGER * 5 + SUBSTR(NEW.ИНН, 8, 1)::INTEGER * 9 +
                 SUBSTR(NEW.ИНН, 9, 1)::INTEGER * 4 + SUBSTR(NEW.ИНН, 10, 1)::INTEGER * 6 +
                 SUBSTR(NEW.ИНН, 11, 1)::INTEGER * 8) % 11 % 10;
        IF сумма1 <> SUBSTR(NEW.ИНН, 11, 1)::INTEGER OR сумма2 <> SUBSTR(NEW.ИНН, 12, 1)::INTEGER THEN
            RAISE EXCEPTION 'Неправильный ИНН';
        END IF;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER триггер_инн BEFORE INSERT OR UPDATE ON физлица
    FOR EACH ROW EXECUTE FUNCTION проверить_инн();
