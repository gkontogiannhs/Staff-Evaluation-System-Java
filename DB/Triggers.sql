/* ON UPDATE COMPANY*/
Drop TRIGGER IF EXISTS Prive;
DELIMITER $

CREATE TRIGGER Prive
BEFORE UPDATE ON company
FOR EACH ROW
BEGIN

SET NEW.afm=OLD.afm ;

SET NEW.doy=OLD.doy;

SET NEW.comp_name=OLD.comp_name;


IF NEW.phone='' THEN 
SET NEW.phone=OLD.phone;
END IF;

IF NEW.street='' THEN 
SET NEW.street=OLD.street;
END IF;

IF NEW.num='' THEN 
SET NEW.num=OLD.num; 
END IF;

IF NEW.city='' THEN 
SET NEW.city=OLD.city;
END IF;

IF NEW.country='' THEN 
SET NEW.country=OLD.country;
END IF;


END$

DELIMITER ;


/*DELETE COMPANY*/
Drop TRIGGER IF EXISTS Prive2;
DELIMITER $

CREATE TRIGGER Prive2
BEFORE DELETE ON company
FOR EACH ROW
BEGIN
SIGNAL SQLSTATE VALUE '45000'
SET MESSAGE_TEXT = '#NOT';
END$

DELIMITER ;




/*ChangeEval*/
Drop TRIGGER IF EXISTS ChangeEval;
DELIMITER $

CREATE TRIGGER ChangeEval
BEFORE UPDATE ON evaluator
FOR EACH ROW
BEGIN
IF (NEW.ev_username != OLD.ev_username OR NEW.eval_id != OLD.eval_id ) THEN
SIGNAL SQLSTATE VALUE '45000'
SET MESSAGE_TEXT = '#NOT';
END IF;
END$

DELIMITER ;



/*ChangeManager*/
Drop TRIGGER IF EXISTS ChangeManag;
DELIMITER $

CREATE TRIGGER ChangeManag
BEFORE UPDATE ON manager
FOR EACH ROW
BEGIN
IF (NEW.exp_years != OLD.exp_years OR NEW.man_username != OLD.man_username OR NEW.firm != OLD.firm) THEN
SIGNAL SQLSTATE VALUE '45000'
SET MESSAGE_TEXT = '#NOT';
END IF;
END$

DELIMITER ;




/*ChangeEmployee*/
Drop TRIGGER IF EXISTS ChangeEmpl;
DELIMITER $

CREATE TRIGGER ChangeEmpl
BEFORE UPDATE ON employee
FOR EACH ROW
BEGIN

SET NEW.username = OLD.username; 
SET NEW.comp_afm = OLD.comp_afm;

END$

DELIMITER ;




/*UpdateLogRequestEval*/
Drop TRIGGER IF EXISTS LogRE;
DELIMITER $

CREATE TRIGGER LogRE
BEFORE UPDATE ON request_evaluation
FOR EACH ROW
BEGIN

DECLARE LogUsername VARCHAR(12);
DECLARE Succ VARCHAR(12);

SET LogUsername = NEW.empl_username;

IF (NEW.interview != OLD.interview) THEN
SET Succ = 'Done';
ELSE
SET Succ = 'Not Done';
END IF;



INSERT INTO log(num,user,date_time,action,table_name,kind) VALUES
(NULL,LogUsername,CURRENT_TIMESTAMP,'Update','request_evaluation',Succ);

END$

DELIMITER ;



/*InsertLogRequestEval*/
Drop TRIGGER IF EXISTS LogRE1;
DELIMITER $

CREATE TRIGGER LogRE1
AFTER INSERT ON request_evaluation
FOR EACH ROW
BEGIN

INSERT INTO log(num,user,date_time,action,table_name,kind) VALUES
(NULL,NEW.empl_username,CURRENT_TIMESTAMP,'Insert','request_evaluation','Done');

END$

DELIMITER ;





/*DeleteLogRequestEval*/
Drop TRIGGER IF EXISTS LogRE2;
DELIMITER $

CREATE TRIGGER LogRE2
BEFORE DELETE ON request_evaluation
FOR EACH ROW
BEGIN

INSERT INTO log(num,user,date_time,action,table_name,kind) VALUES
(NULL,OLD.empl_username,CURRENT_TIMESTAMP,'Delete','request_evaluation','Done');

END$

DELIMITER ;



/*UpdateLogEmployee*/
Drop TRIGGER IF EXISTS LogEm;
DELIMITER $

CREATE TRIGGER LogEm
BEFORE UPDATE ON employee
FOR EACH ROW
BEGIN

DECLARE LogUsername VARCHAR(12);
DECLARE Succ VARCHAR(12);

SET LogUsername = NEW.username;

IF (NEW.bio != OLD.bio OR NEW.exp_years != OLD.exp_years OR NEW.certificates != OLD.certificates 
	OR NEW.awards != OLD.awards OR NEW.reference != OLD.reference ) THEN
SET Succ = 'Done';

ELSE
SET Succ = 'Not Done';

END IF;



INSERT INTO log(num,user,date_time,action,table_name,kind) VALUES
(NULL,LogUsername,CURRENT_TIMESTAMP,'Update','employee',Succ);

END$

DELIMITER ;



/*InsertLogEmployee*/
Drop TRIGGER IF EXISTS LogEm1;
DELIMITER $

CREATE TRIGGER LogEm1
BEFORE INSERT ON employee
FOR EACH ROW
BEGIN

DECLARE LogUsername VARCHAR(12);
DECLARE Succ VARCHAR(12);

SET LogUsername = NEW.username;

IF (NEW.username IS NOT NULL AND NEW.comp_afm IS NOT NULL 
AND NEW.bio IS NOT NULL AND NEW.exp_years IS NOT NULL AND NEW.certificates IS NOT NULL 
AND NEW.awards IS NOT NULL AND NEW.reference IS NOT NULL) THEN
SET Succ = 'Done';
ELSE
SET Succ = 'Not Done';
END IF;



INSERT INTO log(num,user,date_time,action,table_name,kind) VALUES
(NULL,LogUsername,CURRENT_TIMESTAMP,'Insert','employee',Succ);

END$

DELIMITER ;



/*DeleteLogEmployee*/
Drop TRIGGER IF EXISTS LogEm2;
DELIMITER $

CREATE TRIGGER LogEm2
BEFORE DELETE ON employee
FOR EACH ROW
BEGIN

DECLARE LogUsername VARCHAR(12);
DECLARE Succ VARCHAR(12);

SET LogUsername = OLD.username;
SET Succ = 'Done';


INSERT INTO log(num,user,date_time,action,table_name,kind) VALUES
(NULL,LogUsername,CURRENT_TIMESTAMP,'Delete','employee',Succ);

END$

DELIMITER ;



/*UpdateLogJob*/
Drop TRIGGER IF EXISTS LogJob;
DELIMITER $

CREATE TRIGGER LogJob
BEFORE UPDATE ON job
FOR EACH ROW
BEGIN

DECLARE Succ VARCHAR(12);

IF ( NEW.pos_title != OLD.pos_title OR NEW.salary != OLD.salary OR NEW.edra != OLD.edra OR NEW.evaluator != OLD.evaluator
OR NEW.announce_date != OLD.announce_date OR NEW.deadline != OLD.deadline) THEN

SET Succ = 'Done';
ELSE
SET Succ = 'Not Done';
END IF;

INSERT INTO log(num,user,date_time,action,table_name,kind) VALUES
(NULL,OLD.evaluator,CURRENT_TIMESTAMP,'Update','job',Succ);

END$

DELIMITER ;




/*InsertLogJob*/
Drop TRIGGER IF EXISTS LogJob1;
DELIMITER $

CREATE TRIGGER LogJob1
BEFORE INSERT ON job
FOR EACH ROW
BEGIN

DECLARE LogUsername VARCHAR(12);
DECLARE Succ VARCHAR(12);


IF (NEW.code IS NOT NULL AND NEW.start_date IS NOT NULL 
AND NEW.salary IS NOT NULL AND NEW.pos_title IS NOT NULL AND NEW.edra IS NOT NULL 
AND NEW.evaluator IS NOT NULL AND NEW.announce_date IS NOT NULL AND NEW.deadline IS NOT NULL )THEN
SET Succ = 'Done';
ELSE
SET Succ = 'Not Done';
END IF;



INSERT INTO log(num,user,date_time,action,table_name,kind) VALUES
(NULL,LogUsername,CURRENT_TIMESTAMP,'Insert','job',Succ);

END$

DELIMITER ;



/*DeleteLogJob*/
Drop TRIGGER IF EXISTS LogJob2;
DELIMITER $

CREATE TRIGGER LogJob2
BEFORE DELETE ON job
FOR EACH ROW
BEGIN

DECLARE LogUsername VARCHAR(12);
DECLARE Succ VARCHAR(12);

SET Succ = 'Done';

INSERT INTO log(num,user,date_time,action,table_name,kind) VALUES
(NULL,LogUsername,CURRENT_TIMESTAMP,'Delete','job',Succ);

END$

DELIMITER ;



/*if an update on user is "" it keeps the old values*/

DELIMITER $
DROP TRIGGER IF EXISTS UserIfNull $
CREATE TRIGGER UserIfNull
BEFORE UPDATE ON user 
FOR EACH ROW
BEGIN 

IF NEW.password = '' THEN 
SET NEW.password = OLD.password;
END IF;

IF NEW.name = '' THEN
SET NEW.name = OLD.name;
END IF;

IF NEW.surname = '' THEN 
SET NEW.surname = OLD.surname;
END IF;

IF NEW.register_date != OLD.register_date THEN 
SET NEW.register_date = OLD.register_date;
END IF;

IF NEW.email = '' THEN 
SET NEW.email=OLD.email;
END IF;

END$

DELIMITER ;

/*if an update on job is "" it keeps the old values*/

DELIMITER $
DROP TRIGGER IF EXISTS JobIfNull $
CREATE TRIGGER JobIfNull
BEFORE UPDATE ON job 
FOR EACH ROW
BEGIN 

IF NEW.start_date = '' THEN 
SET NEW.start_date = OLD.start_date;
END IF;

IF NEW.salary = '' THEN
SET NEW.salary = OLD.salary;
END IF;

IF NEW.pos_title = '' THEN 
SET NEW.pos_title = OLD.pos_title;
END IF;

IF NEW.edra = '' THEN 
SET NEW.edra = OLD.edra;
END IF;

IF NEW.deadline = '' THEN 
SET NEW.deadline = OLD.deadline;
END IF;

END$

DELIMITER ;


/*if an update on needs is "" it keeps the old values*/

DELIMITER $
DROP TRIGGER IF EXISTS needsIfNull $
CREATE TRIGGER needsIfNull
BEFORE UPDATE ON needs 
FOR EACH ROW
BEGIN 

IF NEW.job_code = '' THEN 
SET NEW.job_code = OLD.job_code;
END IF;

IF NEW.field_title = '' THEN
SET NEW.field_title = OLD.field_title;
END IF;

END$

DELIMITER ;

/*if an update on field is "" it keeps the old values*/

DELIMITER $
DROP TRIGGER IF EXISTS fieldIfNull $
CREATE TRIGGER fieldIfNull
BEFORE UPDATE ON field 
FOR EACH ROW
BEGIN 

IF NEW.title = '' THEN 
SET NEW.title = OLD.title;
END IF;

IF NEW.descr = '' THEN
SET NEW.descr = OLD.descr;
END IF;

IF NEW.belongs_to= '' THEN
SET NEW.belongs_to = OLD.belongs_to;
END IF;

END$

DELIMITER ;



/*holds the old values for certificates,awards ,references if null */

DELIMITER $
DROP TRIGGER IF EXISTS emplNull $
CREATE TRIGGER emplNull
BEFORE UPDATE ON employee 
FOR EACH ROW
BEGIN 

IF NEW.certificates = '' THEN 
SET NEW.certificates = OLD.certificates;
END IF;

IF NEW.awards = '' THEN
SET NEW.awards= OLD.awards;
END IF;

IF NEW.reference = '' THEN
SET NEW.reference = OLD.reference;
END IF;

END$

DELIMITER ;


