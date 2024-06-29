CREATE TABLE физлица(
    id INTEGER PRIMARY KEY,
    фамилия VARCHAR(100),
    имя VARCHAR(100),
    отчество VARCHAR(100),
  	пол VARCHAR(7),
    дата_рождения DATE,
    место_рождения VARCHAR(100),
    гражданство VARCHAR(100),
    ИНН VARCHAR(12),
    СНИЛС VARCHAR(11),
    специальность VARCHAR(100),
    стаж VARCHAR(10),
    паспорт_номер VARCHAR(6),
    паспорт_дата_выдачи DATE,
    паспорт_выдан VARCHAR(300),
    адрес_по_паспорту VARCHAR(300),
    адрес_фактический VARCHAR(300),
    дата_регистрации_места_жительства DATE,
    телефон VARCHAR(12),
  	id_брака INTEGER,
    паспорт_серия VARCHAR(4)
);

CREATE TABLE языки (
    id INTEGER PRIMARY KEY,
    название VARCHAR(32)
);

CREATE TABLE наименования_наград (
	id INTEGER PRIMARY KEY,
    название VARCHAR(40)
);

CREATE TABLE информация_о_воинском_учете (
	id INTEGER PRIMARY KEY,
    id_физлица INTEGER,
    id_воинского_звания INTEGER,
    id_категории_запаса INTEGER,
    ВУС VARCHAR(20),
    состав VARCHAR(30),
    военный_комиссариат VARCHAR(100),
    воинский_учет VARCHAR(30),
    дата_снятия_с_воинского_учета DATE,
    id_подписи INTEGER,
    категория_годности VARCHAR(4)
);

CREATE TABLE языки_физлица (
    id INTEGER PRIMARY KEY,
    id_физлица INTEGER,
    id_языка INTEGER
);

CREATE TABLE информация_о_наградах (
	id INTEGER PRIMARY KEY,
    id_физлица INTEGER,
    id_наименования_награды INTEGER,
    id_дока_награды INTEGER,
    док_номер VARCHAR(10),
    док_дата DATE
);

CREATE TABLE документ_награды (
	id INTEGER PRIMARY KEY,
    название VARCHAR(30)
);

CREATE TABLE журнал_проф_переподготовки (
	id INTEGER PRIMARY KEY,
    состояние VARCHAR(32),
    id_сотрудника INTEGER,
    id_специальности INTEGER,
    основание_проф_переподготовки INTEGER,
    id_док_наименование INTEGER,
    дата_начала DATE,
    дата_окончания DATE, 
    док_номер VARCHAR(20),
    док_дата DATE
);

CREATE TABLE виды_образований (
	id INTEGER PRIMARY KEY,
    название VARCHAR(100)
);

CREATE TABLE документ_наименование (
	id INTEGER PRIMARY KEY,
    название VARCHAR(100)
);

CREATE TABLE квалификации (
	id INTEGER PRIMARY KEY,
    название VARCHAR(100)
);

CREATE TABLE информация_об_образовании (
	id INTEGER PRIMARY KEY,
    id_физлица INTEGER,
    id_док_наименование INTEGER,
    id_квалификации INTEGER,
    id_вида_образования INTEGER,
    id_специальности INTEGER,
    образовательное_учреждение VARCHAR(150),
    док_серия VARCHAR(10),
    док_номер VARCHAR(10),
    док_год_окончания DATE
);

CREATE TABLE категория_запаса (
	id INTEGER PRIMARY KEY,
    название VARCHAR(100)
);

CREATE TABLE воинские_звания (
	id INTEGER PRIMARY KEY,
    название VARCHAR(100)
);

CREATE TABLE состояние_в_браке(
	id INTEGER PRIMARY KEY,
	название VARCHAR(27)
);

CREATE TABLE подписи (
	id INTEGER PRIMARY KEY,
    должность VARCHAR(100),
    подпись VARCHAR(100),
    расшифровка_подписи VARCHAR(100),
    подпись_сотрудника VARCHAR(100),
    дата DATE
);

CREATE TABLE специальности (
	id INTEGER PRIMARY KEY,
    название VARCHAR(300)
);

CREATE TABLE сотрудники (
	id INTEGER PRIMARY KEY,
    id_физлица INTEGER,
    табельный_номер VARCHAR(6)
);

CREATE TABLE трудовой_договор(
	id INTEGER PRIMARY KEY,
    id_приказа_о_приеме INTEGER,
    id_сотрудника INTEGER,
    id_приказа_увольнения INTEGER,
    дата DATE,
    номер VARCHAR(20)
);

CREATE TABLE личные_карточки_сотрудников (
	id INTEGER PRIMARY KEY,
    id_сотрудника INTEGER,
    id_учреждения INTEGER,
    id_трудового_договора INTEGER,
    дата DATE
);

CREATE TABLE решение_комиссии(
    id INTEGER PRIMARY KEY,
    название VARCHAR(35)
);

CREATE TABLE журнал_аттестации(
    id INTEGER PRIMARY KEY,
    состояние VARCHAR(50),
    id_решение_комиссии INTEGER,
    id_сотрудника INTEGER,
    дата_аттестации DATE,
    основание_аттестации VARCHAR(80),
    док_номер VARCHAR(10),
    док_дата DATE
);

CREATE TABLE учреждение (
	id INTEGER PRIMARY KEY,
    название VARCHAR(200),
    ОКПО VARCHAR(11)
);

CREATE TABLE журнал_приема_перевода(
    id INTEGER PRIMARY KEY,
    состояние VARCHAR(90),
    id_сотрудника INTEGER,
    id_подразделения INTEGER,
    id_основания_приема_перевода INTEGER,
    id_должности INTEGER,
    оклад VARCHAR(12),
    дата DATE,
    подпись_сотрудника VARCHAR(255)
);

CREATE TABLE наименования_льгот(
    id INTEGER PRIMARY KEY,
    название VARCHAR(100)
);

CREATE TABLE основания_прием_перевода(
    id INTEGER PRIMARY KEY,
    название VARCHAR(100)
);

CREATE TABLE основания_льгот(
    id INTEGER PRIMARY KEY,
    название VARCHAR(100)
);

CREATE TABLE информация_льготы(
    id INTEGER PRIMARY KEY,
    id_сотрудника INTEGER,
    id_основания_льгот INTEGER,
    id_наименования_льгот INTEGER,
    док_дата DATE,
    док_номер VARCHAR(12)
);

CREATE TABLE журнал_повышения_квалификации(
    id INTEGER PRIMARY KEY,
    состояние VARCHAR(100),
    id_сотрудника INTEGER,
    основание VARCHAR(50),
    образовательное_учреждение VARCHAR(150),
    дата_начала_обучения DATE,
    дата_окончания_обучения DATE,
    док_серия_номер VARCHAR(10),
    док_наименование VARCHAR(40),
    вид_повышения_квалификации VARCHAR(40),
    док_дата DATE
);

CREATE TABLE приказ_отпуск(
    id INTEGER PRIMARY KEY,
    id_сотрудника INTEGER,
    id_журнала_приказов INTEGER,
    id_должности INTEGER,
    id_подразделения INTEGER,
    id_учреждения INTEGER,
    id_отпуска INTEGER
);

CREATE TABLE журнал_увольнения(
    id INTEGER PRIMARY KEY,
    состояние VARCHAR(75),
    id_сотрудника INTEGER,
    id_подписей INTEGER,
    дата DATE,
    приказ VARCHAR(100),
    основание VARCHAR(100)
);

CREATE TABLE отпуск(
    id INTEGER PRIMARY KEY,
    id_вида_отпуска INTEGER,
    основание VARCHAR(40),
    количество_дней INTEGER,
    за_период_работы_с DATE,
    за_период_работы_по DATE,
    дата_начала DATE,
    дата_окончания DATE
);

CREATE TABLE вид_отпуска(
    id INTEGER PRIMARY KEY,
    название VARCHAR(60)
);

CREATE TABLE приказы_увольнения(
    id INTEGER PRIMARY KEY,
    id_учреждения INTEGER,
    id_сотрудника INTEGER,
    id_журнала_приказов INTEGER,
    основание VARCHAR(300),
    дата_увольнения DATE,
    заявление_сотрудника VARCHAR(255),
    док_дата DATE
);

CREATE TABLE журнал_места_работы(
    id INTEGER PRIMARY KEY,
    состояние VARCHAR(30),
    id_журнала_приказов INTEGER,
    id_условия INTEGER,
    id_подразделения INTEGER,
    id_должности INTEGER,
    id_сотрудника INTEGER,
    дата_журнала DATE,
    ставка VARCHAR(5)
);

CREATE TABLE информация_состав_семьи(
    id INTEGER PRIMARY KEY,
    id_физлица INTEGER,
    фамилия VARCHAR(30),
    имя VARCHAR(30),
    отчество VARCHAR(30),
    степень_родства VARCHAR(25),
    дата_рождения DATE
);

CREATE TABLE журнал_приказов(
    id INTEGER PRIMARY KEY,
    статус VARCHAR(50),
    id_типа INTEGER,
    дата DATE,
    номер_дока VARCHAR(20)
);

CREATE TABLE надбавки(
    id INTEGER PRIMARY KEY,
    id_сотрудника INTEGER,
    id_журнала_приказов INTEGER,
    размер float,
    вид_надбавки VARCHAR(30)
);

CREATE TABLE приказы_перевода(
    id INTEGER PRIMARY KEY,
    id_сотрудника INTEGER,
    id_журнала_приказов INTEGER,
    id_учреждения INTEGER,
    id_подразделения INTEGER,
    id_должности INTEGER,
    дата_принятия_с DATE,
    дата_принятия_по DATE,
    вид_перевода VARCHAR(100),
    причина_перевода VARCHAR(240)
);

CREATE TABLE типы_приказов(
    id INTEGER PRIMARY KEY,
    название VARCHAR(45)
);

CREATE TABLE структурные_подразделения(
    id INTEGER PRIMARY KEY,
    название VARCHAR(100)
);

CREATE TABLE должности(
    id INTEGER PRIMARY KEY,
    название VARCHAR(100)
);

CREATE TABLE приказы_приема(
    id INTEGER PRIMARY KEY,
    id_физлица INTEGER,
    id_сотрудника INTEGER,
    id_журнала_приказов INTEGER,
    id_подразделения INTEGER,
    id_должности INTEGER,
    id_условия INTEGER,
    id_учреждения INTEGER,
    дата_принятия_с DATE,
    дата_принятия_по DATE,
    оклад VARCHAR(11)
);

CREATE TABLE условия_приема(
    id INTEGER PRIMARY KEY,
    название VARCHAR(100)
);

CREATE TABLE журнал_версий(
    id INTEGER PRIMARY KEY,
    поле VARCHAR(60),
    значение VARCHAR(50),
    дата DATE,
    id_физлица INTEGER
);

CREATE TABLE согласования(
    id INTEGER PRIMARY KEY,
    должность VARCHAR(40),
    фамилия VARCHAR(30)
);

CREATE TABLE журнал_согласования(
    id INTEGER PRIMARY KEY,
    id_журнала_приказов INTEGER,
    id_согласования INTEGER
);



ALTER TABLE журнал_аттестации
ADD CONSTRAINT fk_журнал_аттестации_id_решение_комиссии FOREIGN KEY (id_решение_комиссии)
REFERENCES решение_комиссии(id);

ALTER TABLE журнал_аттестации
ADD CONSTRAINT fk_журнал_аттестации_id_сотрудника FOREIGN KEY (id_сотрудника)
REFERENCES сотрудники(id);



ALTER TABLE журнал_приема_перевода
ADD CONSTRAINT fk_журнал_приема_перевода_id_основания_приема_перевода FOREIGN KEY (id_основания_приема_перевода)
REFERENCES основания_прием_перевода(id);

ALTER TABLE журнал_приема_перевода
ADD CONSTRAINT fk_журнал_приема_перевода_id_должности FOREIGN KEY (id_должности)
REFERENCES должности(id);

ALTER TABLE журнал_приема_перевода
ADD CONSTRAINT fk_журнал_приема_перевода_id_подразделения FOREIGN KEY (id_подразделения)
REFERENCES структурные_подразделения(id);

ALTER TABLE журнал_приема_перевода
ADD CONSTRAINT fk_журнал_приема_перевода_id_сотрудника FOREIGN KEY (id_сотрудника)
REFERENCES сотрудники(id);



ALTER TABLE информация_льготы
ADD CONSTRAINT fk_информация_льготы_id_наименования_льгот FOREIGN KEY (id_наименования_льгот)
REFERENCES наименования_льгот(id);

ALTER TABLE информация_льготы
ADD CONSTRAINT fk_информация_льготы_id_основания_льгот FOREIGN KEY (id_основания_льгот)
REFERENCES основания_льгот(id);

ALTER TABLE информация_льготы
ADD CONSTRAINT fk_информация_льготы_id_сотрудника FOREIGN KEY (id_сотрудника)
REFERENCES сотрудники(id);



ALTER TABLE журнал_повышения_квалификации
ADD CONSTRAINT fk_журнал_повышения_квалификации_id_сотрудника FOREIGN KEY (id_сотрудника)
REFERENCES сотрудники(id);



ALTER TABLE отпуск
ADD CONSTRAINT fk_отпуск_id_вида_отпуска FOREIGN KEY (id_вида_отпуска)
REFERENCES вид_отпуска(id);



ALTER TABLE приказ_отпуск
ADD CONSTRAINT fk_приказ_отпуск_id_отпуска FOREIGN KEY (id_отпуска)
REFERENCES отпуск(id);

ALTER TABLE приказ_отпуск
ADD CONSTRAINT fk_приказ_отпуск_id_подразделения FOREIGN KEY (id_подразделения)
REFERENCES структурные_подразделения(id);

ALTER TABLE приказ_отпуск
ADD CONSTRAINT fk_приказ_отпуск_id_должности FOREIGN KEY (id_должности)
REFERENCES должности(id);

ALTER TABLE приказ_отпуск
ADD CONSTRAINT fk_приказ_отпуск_id_журнала_приказов FOREIGN KEY (id_журнала_приказов)
REFERENCES журнал_приказов(id);

ALTER TABLE приказ_отпуск
ADD CONSTRAINT fk_приказ_отпуск_id_сотрудника FOREIGN KEY (id_сотрудника)
REFERENCES сотрудники(id);

ALTER TABLE приказ_отпуск
ADD CONSTRAINT fk_приказ_отпуск_id_учреждения FOREIGN KEY (id_учреждения)
REFERENCES учреждение(id);



ALTER TABLE надбавки
ADD CONSTRAINT fk_надбавки_id_журнала_приказов FOREIGN KEY (id_журнала_приказов)
REFERENCES журнал_приказов(id);

ALTER TABLE надбавки
ADD CONSTRAINT fk_надбавки_id_сотрудника FOREIGN KEY (id_сотрудника)
REFERENCES сотрудники(id);



ALTER TABLE журнал_приказов
ADD CONSTRAINT fk_журнал_приказов_id_типа FOREIGN KEY (id_типа)
REFERENCES типы_приказов(id);



ALTER TABLE приказы_перевода
ADD CONSTRAINT fk_приказы_перевода_id_журнала_приказов FOREIGN KEY (id_журнала_приказов)
REFERENCES журнал_приказов(id);

ALTER TABLE приказы_перевода
ADD CONSTRAINT fk_приказы_перевода_id_подразделения FOREIGN KEY (id_подразделения)
REFERENCES структурные_подразделения(id);

ALTER TABLE приказы_перевода
ADD CONSTRAINT fk_приказы_перевода_id_должности FOREIGN KEY (id_должности)
REFERENCES должности(id);

ALTER TABLE приказы_перевода
ADD CONSTRAINT fk_приказы_перевода_id_сотрудника FOREIGN KEY (id_сотрудника)
REFERENCES сотрудники(id);

ALTER TABLE приказы_перевода
ADD CONSTRAINT fk_приказы_перевода_id_учреждения FOREIGN KEY (id_учреждения)
REFERENCES учреждение(id);



ALTER TABLE приказы_приема
ADD CONSTRAINT fk_приказы_приема_id_журнала_приказов FOREIGN KEY (id_журнала_приказов)
REFERENCES журнал_приказов(id);

ALTER TABLE приказы_приема
ADD CONSTRAINT fk_приказы_приема_id_подразделения FOREIGN KEY (id_подразделения)
REFERENCES структурные_подразделения(id);

ALTER TABLE приказы_приема
ADD CONSTRAINT fk_приказы_приема_id_должности FOREIGN KEY (id_должности)
REFERENCES должности(id);

ALTER TABLE приказы_приема
ADD CONSTRAINT fk_приказы_приема_id_условия FOREIGN KEY (id_условия)
REFERENCES условия_приема(id);

ALTER TABLE приказы_приема
ADD CONSTRAINT fk_приказы_приема_id_сотрудника FOREIGN KEY (id_сотрудника)
REFERENCES сотрудники(id);

ALTER TABLE приказы_приема
ADD CONSTRAINT fk_приказы_приема_id_физлица FOREIGN KEY (id_физлица)
REFERENCES физлица(id);

ALTER TABLE приказы_приема
ADD CONSTRAINT fk_приказы_приема_id_учреждения FOREIGN KEY (id_учреждения)
REFERENCES учреждение(id);



ALTER TABLE журнал_места_работы
ADD CONSTRAINT fk_журнал_места_работы_id_журнала_приказов FOREIGN KEY (id_журнала_приказов)
REFERENCES журнал_приказов(id);

ALTER TABLE журнал_места_работы
ADD CONSTRAINT fk_журнал_места_работы_id_условия FOREIGN KEY (id_условия)
REFERENCES условия_приема(id);

ALTER TABLE журнал_места_работы
ADD CONSTRAINT fk_журнал_места_работы_id_должности FOREIGN KEY (id_должности)
REFERENCES должности(id);

ALTER TABLE журнал_места_работы
ADD CONSTRAINT fk_журнал_места_работы_id_подразделения FOREIGN KEY (id_подразделения)
REFERENCES структурные_подразделения(id);

ALTER TABLE журнал_места_работы
ADD CONSTRAINT fk_журнал_места_работы_id_сотрудника FOREIGN KEY (id_сотрудника)
REFERENCES сотрудники(id);



ALTER TABLE приказы_увольнения
ADD CONSTRAINT fk_приказы_увольнения_id_журнала_приказов FOREIGN KEY (id_журнала_приказов)
REFERENCES журнал_приказов(id);

ALTER TABLE приказы_увольнения
ADD CONSTRAINT fk_приказы_увольнения_id_сотрудника FOREIGN KEY (id_сотрудника)
REFERENCES сотрудники(id);

ALTER TABLE приказы_увольнения
ADD CONSTRAINT fk_приказы_увольнения_id_учреждения FOREIGN KEY (id_учреждения)
REFERENCES учреждение(id);



ALTER TABLE физлица
ADD CONSTRAINT fk_физлица_id_брака FOREIGN KEY (id_брака)
REFERENCES состояние_в_браке(id);



ALTER TABLE языки_физлица
ADD CONSTRAINT fk_языки_физлица_id_физлица FOREIGN KEY (id_физлица)
REFERENCES физлица(id);

ALTER TABLE языки_физлица
ADD CONSTRAINT fk_языки_физлица_id_языка FOREIGN KEY (id_языка)
REFERENCES языки(id);



ALTER TABLE информация_о_наградах
ADD CONSTRAINT fk_информация_о_наградах_id_физлица FOREIGN KEY (id_физлица)
REFERENCES физлица(id);

ALTER TABLE информация_о_наградах
ADD CONSTRAINT fk_информация_о_наградах_id_дока_награды FOREIGN KEY (id_дока_награды)
REFERENCES документ_награды(id);

ALTER TABLE информация_о_наградах
ADD CONSTRAINT fk_информация_о_наградах_id_наименования_награды FOREIGN KEY (id_наименования_награды)
REFERENCES наименования_наград(id);



ALTER TABLE информация_об_образовании
ADD CONSTRAINT fk_информация_об_образовании_id_вида_образования FOREIGN KEY (id_вида_образования)
REFERENCES виды_образований(id);

ALTER TABLE информация_об_образовании
ADD CONSTRAINT fk_информация_об_образовании_id_док_наименование FOREIGN KEY (id_док_наименование)
REFERENCES документ_наименование(id);

ALTER TABLE информация_об_образовании
ADD CONSTRAINT fk_информация_об_образовании_id_квалификации FOREIGN KEY (id_квалификации)
REFERENCES квалификации(id);

ALTER TABLE информация_об_образовании
ADD CONSTRAINT fk_информация_об_образовании_id_специальности FOREIGN KEY (id_специальности)
REFERENCES специальности(id);



ALTER TABLE информация_о_воинском_учете
ADD CONSTRAINT fk_информация_о_воинском_учете_id_физлица FOREIGN KEY (id_физлица)
REFERENCES физлица(id);

ALTER TABLE информация_о_воинском_учете
ADD CONSTRAINT fk_информация_о_воинском_учете_id_категории_запаса FOREIGN KEY (id_категории_запаса)
REFERENCES категория_запаса(id);

ALTER TABLE информация_о_воинском_учете
ADD CONSTRAINT fk_информация_о_воинском_учете_id_воинского_звания FOREIGN KEY (id_воинского_звания)
REFERENCES воинские_звания(id);

ALTER TABLE информация_о_воинском_учете
ADD CONSTRAINT fk_информация_о_воинском_учете_id_подписи FOREIGN KEY (id_подписи)
REFERENCES подписи(id);



ALTER TABLE сотрудники
ADD CONSTRAINT fk_сотрудники_id_физлица FOREIGN KEY (id_физлица)
REFERENCES физлица(id);



ALTER TABLE журнал_проф_переподготовки
ADD CONSTRAINT fk_журнал_проф_переподготовки_id_сотрудника FOREIGN KEY (id_сотрудника)
REFERENCES сотрудники(id);

ALTER TABLE журнал_проф_переподготовки
ADD CONSTRAINT fk_журнал_проф_переподготовки_id_специальности FOREIGN KEY (id_специальности)
REFERENCES специальности(id);

ALTER TABLE журнал_проф_переподготовки
ADD CONSTRAINT fk_журнал_проф_переподготовки_id_док_наименование FOREIGN KEY (id_док_наименование)
REFERENCES документ_наименование(id);



ALTER TABLE трудовой_договор
ADD CONSTRAINT fk_трудовой_договор_id_сотрудника FOREIGN KEY (id_сотрудника)
REFERENCES сотрудники(id);

ALTER TABLE трудовой_договор
ADD CONSTRAINT fk_трудовой_договор_id_приказа_о_приеме FOREIGN KEY (id_приказа_о_приеме)
REFERENCES приказы_приема(id);

ALTER TABLE трудовой_договор
ADD CONSTRAINT fk_трудовой_договор_id_приказа_увольнения FOREIGN KEY (id_приказа_увольнения)
REFERENCES приказы_увольнения(id);



ALTER TABLE личные_карточки_сотрудников
ADD CONSTRAINT fk_личные_карточки_сотрудников_id_учреждения FOREIGN KEY (id_учреждения)
REFERENCES учреждение(id);

ALTER TABLE личные_карточки_сотрудников
ADD CONSTRAINT fk_личные_карточки_сотрудников_id_сотрудника FOREIGN KEY (id_сотрудника)
REFERENCES сотрудники(id);

ALTER TABLE личные_карточки_сотрудников
ADD CONSTRAINT fk_личные_карточки_сотрудников_id_трудового_договора FOREIGN KEY (id_трудового_договора)
REFERENCES трудовой_договор(id);



ALTER TABLE журнал_увольнения
ADD CONSTRAINT fk_журнал_увольнения_id_сотрудника FOREIGN KEY (id_сотрудника)
REFERENCES сотрудники(id);

ALTER TABLE журнал_увольнения
ADD CONSTRAINT fk_журнал_увольнения_id_подписей FOREIGN KEY (id_подписей)
REFERENCES подписи(id);



ALTER TABLE информация_состав_семьи
ADD CONSTRAINT fk_информация_состав_семьи_id_физлица FOREIGN KEY (id_физлица)
REFERENCES физлица(id);



ALTER TABLE журнал_согласования
ADD CONSTRAINT fk_журнал_согласования_id_журнала_приказов FOREIGN KEY (id_журнала_приказов)
REFERENCES журнал_приказов(id);

ALTER TABLE журнал_согласования
ADD CONSTRAINT fk_журнал_согласования_id_согласования FOREIGN KEY (id_согласования)
REFERENCES журнал_приказов(id);



ALTER TABLE журнал_версий
ADD CONSTRAINT fk_журнал_версий_id_физлица FOREIGN KEY (id_физлица)
REFERENCES физлица(id);