CREATE OR REPLACE FUNCTION проверить_снилс() RETURNS trigger AS $$
DECLARE
    сумма INTEGER;
BEGIN
    
    IF NOT NEW.СНИЛС ~ '^[0-9]+$' THEN
        RAISE EXCEPTION 'СНИЛС должен содержать только цифры';
    END IF;

    IF NEW.СНИЛС ~ '([0-9])\1{2}' THEN
        RAISE EXCEPTION 'СНИЛС содержит 3+ одинаковых цифр';
    END IF;


    сумма := ( substring(NEW.СНИЛС, 1, 1)::INTEGER * 9 +
                    substring(NEW.СНИЛС, 2, 1)::INTEGER * 8 +
                    substring(NEW.СНИЛС, 3, 1)::INTEGER * 7 +
                    substring(NEW.СНИЛС, 4, 1)::INTEGER * 6 +
                    substring(NEW.СНИЛС, 5, 1)::INTEGER * 5 +
                    substring(NEW.СНИЛС, 6, 1)::INTEGER * 4 +
                    substring(NEW.СНИЛС, 7, 1)::INTEGER * 3 +
                    substring(NEW.СНИЛС, 8, 1)::INTEGER * 2 +
                    substring(NEW.СНИЛС, 9, 1)::INTEGER
            ) % 101;

    IF сумма < 100 THEN
        сумма := сумма;
    ELSIF сумма = 100 OR сумма = 101 THEN
        сумма := 0;
    ELSE
        сумма := сумма % 100;
    END IF;

    IF substring(NEW.СНИЛС, 10, 2)::INTEGER <> сумма THEN
        RAISE EXCEPTION 'Неправильный СНИЛС';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER триггер_снилс BEFORE INSERT OR UPDATE ON физлица
    FOR EACH ROW EXECUTE FUNCTION проверить_снилс();
