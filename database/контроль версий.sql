CREATE OR REPLACE FUNCTION добавить_запись()
RETURNS TRIGGER AS $$
BEGIN
    IF OLD.фамилия <> NEW.фамилия THEN
        INSERT INTO журнал_версий(поле, значение, дата, id_физлица)
        VALUES ('фамилия', NEW.фамилия, CURRENT_DATE, NEW.id);
    END IF;
    
    IF OLD.имя <> NEW.имя THEN
        INSERT INTO журнал_версий(поле, значение, дата, id_физлица)
        VALUES ('имя', NEW.имя, CURRENT_DATE, NEW.id);
    END IF;
    
    IF OLD.отчество <> NEW.отчество THEN
        INSERT INTO журнал_версий(поле, значение, дата, id_физлица)
        VALUES ('отчество', NEW.отчество, CURRENT_DATE, NEW.id);
    END IF;

    IF OLD.отчество <> NEW.отчество THEN
        INSERT INTO журнал_версий(поле, значение, дата, id_физлица)
        VALUES ('отчество', NEW.отчество, CURRENT_DATE, NEW.id);
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER контроль_данных
AFTER UPDATE ON физлица
FOR EACH ROW
EXECUTE FUNCTION добавить_запись();


UPDATE физлица
SET фамилия = 'Филимонов', имя = 'Эдуард'
WHERE id = 1;