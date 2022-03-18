/*a second approach for upper stored procedure with no cursor
This is gonna be used for the "show me avaliable jobs button in employee's username" */


DELIMITER #

DROP PROCEDURE IF EXISTS ShowNewJobs#

CREATE  PROCEDURE ShowNewJobs(IN usr VARCHAR(12))
BEGIN

SELECT pos_title 
FROM job
INNER JOIN evaluator e
ON e.ev_username = job.evaluator 
INNER JOIN company c
ON e.firm = c.afm
INNER JOIN employee em
ON c.afm = em.comp_afm
WHERE em.username = usr; 

END #

DELIMITER ;





/*call procedure that applies  request for evaluation for a new avaliable job 
   super useful for apply for job button*/

DROP PROCEDURE IF EXISTS RequestEval;

DELIMITER #

CREATE PROCEDURE RequestEval(IN usr VARCHAR(12) , IN job INT(4), IN somewords TEXT)
BEGIN

INSERT INTO request_evaluation(empl_username,job_code,interview) VALUES
(usr,job,somewords);

END#

DELIMITER ;


/*3.1 stored procedure */ 
DELIMITER $
DROP PROCEDURE IF EXISTS SeeJobActivity$
CREATE PROCEDURE SeeJobActivity (IN em_name VARCHAR(25), IN em_surname VARCHAR(25))

BEGIN 

DECLARE usrnm VARCHAR(12);
DECLARE employee VARCHAR(12);
DECLARE eval VARCHAR(12);
DECLARE com VARCHAR(255);
DECLARE job INT(4);
DECLARE flag INT;

DECLARE nonFinalizedJobs_cursor CURSOR  FOR 
SELECT evaluator,e_username,job_code,comments 
FROM evaluation_info 
WHERE registered = 0 AND e_username = usrnm;

DECLARE CONTINUE HANDLER FOR NOT FOUND SET flag=1;
 OPEN nonFinalizedJobs_cursor;
 SET flag=0;


 REPEAT

 FETCH nonFinalizedJobs_cursor INTO eval, employee, job, com;
 IF (flag=0) THEN
  
    SELECT eval, employee, job, com;

 END IF;

 UNTIL (flag=1)
 END REPEAT;

 CLOSE nonFinalizedJobs_cursor;



SELECT username 
INTO usrnm 
FROM user 
WHERE name=em_name AND surname=em_surname;

SELECT * FROM request_evaluation WHERE empl_username=usrnm;

SELECT * FROM evaluation_result WHERE empl_username=usrnm;


SELECT name AS 'Evaluator Name',surname AS 'Evaluaton Surname' FROM user u
INNER JOIN evaluator e ON e.ev_username=u.username 
INNER JOIN evaluation_info ei ON ei.evaluator=e.ev_username
WHERE ei.e_username=usrnm;

END$

DELIMITER ;






/*3.1 stored procedure second approach without cursor*/ 
DELIMITER $
DROP PROCEDURE IF EXISTS SeeReqActivity$
CREATE PROCEDURE SeeReqActivity (IN em_name VARCHAR(25), IN em_surname VARCHAR(25))

BEGIN 

DECLARE usrnm VARCHAR(12);
DECLARE employee VARCHAR(12);
DECLARE eval VARCHAR(12);
DECLARE com VARCHAR(255);
DECLARE job INT(4);
DECLARE flag INT;


/*prints out all requests employee has done for evaluation and those who are finalized. */
SELECT username 
INTO usrnm 
FROM user 
WHERE name=em_name AND surname=em_surname;

SELECT * FROM request_evaluation WHERE empl_username=usrnm;

SELECT * FROM evaluation_result WHERE empl_username=usrnm;

/*prints out the evaluator for each evaluation*/
SELECT name AS 'Evaluator Name',surname AS 'Evaluaton Surname' FROM user u
INNER JOIN evaluator e ON e.ev_username=u.username 
INNER JOIN evaluation_info ei ON ei.evaluator=e.ev_username
WHERE ei.e_username=usrnm;

/*prints out those requests who are in the middle of finalization*/
SELECT evaluator,e_username,job_code,comments 
FROM evaluation_info 
WHERE registered = 0 AND e_username = usrnm;


END$

DELIMITER ;




/*stored procedure which selects all jobs from job table for a specific company or selects all jobs for a specific evaluator
   if choice = 0, its the first query , for choice = 1 the second */


DELIMITER #

DROP PROCEDURE IF EXISTS ReturnJobs#

CREATE PROCEDURE ReturnJobs(IN usr VARCHAR(12), IN choice BIT)
BEGIN

DECLARE afm CHAR(9);

 IF (choice = 0) THEN
 SELECT firm INTO afm FROM evaluator WHERE ev_username = usr;

 SELECT code, start_date AS 'Starts', salary, pos_title AS 'Title', edra AS 'Head Office', evaluator, announce_date AS 'Announced', deadline FROM job j
 INNER JOIN evaluator e ON e.ev_username = j.evaluator
 WHERE afm = e.firm
 ORDER BY code;
 END IF;

 IF (choice = 1) THEN
 SELECT code, start_date AS 'Starts', salary, pos_title AS 'Title', edra AS 'Head Office', announce_date AS 'Announced', deadline FROM job j
 WHERE j.evaluator = usr
 ORDER BY code;
 END IF;

END#

DELIMITER ; 





/*stored procedure which updates  job table */

DELIMITER #

DROP PROCEDURE IF EXISTS updateJob#

CREATE PROCEDURE updateJob(IN jobCode INT(4), IN start DATE, IN sal FLOAT(6,1), IN posTitle VARCHAR(40), IN headOffice VARCHAR(40),IN due DATE)
BEGIN

UPDATE job SET start_date = start, salary = sal , pos_title = posTitle, edra = headOffice, deadline = due WHERE code = jobCode;

END#

DELIMITER ;



/*stored procedure which inserts new register into job table */

DELIMITER #

DROP PROCEDURE IF EXISTS InsertIntoJob#

CREATE PROCEDURE InsertIntoJob(IN start DATE, IN sal FLOAT(6,1), IN posTitle VARCHAR(40), IN headOffice VARCHAR(40),IN eval VARCHAR(12), IN due DATE )
BEGIN


INSERT INTO job VALUES (NULL,start,sal,posTitle,headOffice,eval,CURRENT_TIMESTAMP,due);

END#

DELIMITER ;




/*stored procedure which inserts new register into needs table */

DELIMITER #

DROP PROCEDURE IF EXISTS InsertIntoNeeds#

CREATE PROCEDURE InsertIntoNeeds(IN code INT(4),IN jobField VARCHAR(36))
BEGIN

INSERT INTO needs(job_code,field_title) VALUES(code,jobField);

END#

DELIMITER ;




/* 3.2 stored procedure */

DROP PROCEDURE IF EXISTS FinalizeRequest;
DELIMITER #

CREATE PROCEDURE FinalizeRequest(IN job INT(4) , IN eval VARCHAR(12) )
BEGIN

DECLARE m_grade INT(4);
DECLARE e_grade INT(4);
DECLARE q_grade INT(4);
DECLARE employee VARCHAR(12);
DECLARE com VARCHAR(255);
DECLARE tot INT(4);
DECLARE reg BOOLEAN;
DECLARE flag int;


DECLARE jobs_result CURSOR FOR 
SELECT manager_grade ,evaluator_grade ,qual_grade ,e_username ,comments, registered
FROM evaluation_info
WHERE job_code = job AND evaluator = eval;

DECLARE CONTINUE HANDLER FOR NOT FOUND SET flag=1;
 OPEN jobs_result;
 SET flag=0;

 REPEAT

 FETCH jobs_result INTO m_grade, e_grade, q_grade, employee, com, reg;
 IF (flag=0) THEN
    
   SET tot = m_grade + e_grade + q_grade;

   IF ( ( (m_grade & e_grade & q_grade) IS NOT NULL) AND (reg = 0)) THEN
   INSERT INTO evaluation_result(id,empl_username,job_code,tot_grade,comments) VALUES (NULL,employee,job,tot,com);

   UPDATE evaluation_info set registered = 1
   WHERE e_username = employee AND job_code = job;

   END IF;

   IF(m_grade IS NULL) THEN
   SIGNAL SQLSTATE '45000'
   SET MESSAGE_TEXT = 'Pending Manager Grade.';
   END IF;

 END IF;

 UNTIL (flag=1)
 END REPEAT;

 CLOSE jobs_result;

END#

DELIMITER ;



/*stored procedure which returns the job,name,surname,usernames from table request_evaluation which have applied for evaluation to a specific evaluator
AND ARE NOT FINALIZED
IF CHOICE = 0 RETURNS ALL REQUESTS THAT HAVE BEEN DONE.
IF CHOICE = 1 RETURNS ONLY THE UNGRADED...nice word george*/


DELIMITER #

DROP PROCEDURE IF EXISTS returnRequests#

CREATE PROCEDURE returnRequests(IN eval VARCHAR(12),IN choice int )
BEGIN

IF(choice = 0) THEN
SELECT DISTINCT code,name,surname,username
FROM user u
JOIN request_evaluation re ON re.empl_username = u.username
JOIN job j ON re.job_code = j.code 
WHERE j.evaluator = eval; 

ELSEIF(choice = 1) THEN
SELECT DISTINCT code,name,surname,username
FROM user u
JOIN request_evaluation re ON re.empl_username = u.username
JOIN job j ON re.job_code = j.code 
LEFT JOIN evaluation_info ei ON ei.e_username = re.empl_username AND re.job_code = ei.job_code
WHERE j.evaluator = eval AND ei.evaluator_grade IS NULL;
END IF;

END#

DELIMITER ;




/*stored procedure which inserts the new evaluation from gradeField and comField*/
DELIMITER #

DROP PROCEDURE IF EXISTS gradeEmployee#

CREATE PROCEDURE gradeEmployee(IN eval VARCHAR(12), IN employee VARCHAR(12), IN job INT(4), IN evalGrade INT(4), IN com TEXT )
BEGIN

DECLARE skillsGrade INT(4);

/*calls and calculates through a specific algorithm qualification grade for an employee */
call calcQualGrade(employee,@x);
SET skillsGrade  = @x;

INSERT INTO evaluation_info(evaluator,e_username,job_code,manager_grade,evaluator_grade,qual_grade,comments,registered)
VALUES(eval,employee,job,NULL,evalGrade,skillsGrade,com,0);

END#

DELIMITER ;





/* if choice = 0, then return me all finalized requests , else if 1 ,return unfinalized*/
DROP PROCEDURE IF EXISTS ShowCandidates;

DELIMITER #

CREATE PROCEDURE ShowCandidates(IN eval VARCHAR(12),IN job INT(4),IN choice BIT)
BEGIN

IF(choice = 0) THEN
SELECT DISTINCT job_code AS 'Code',e_username AS 'Username',manager_grade AS 'Manager Grade', evaluator_grade AS 'My Grade',qual_grade AS 'Skill Grade'
FROM evaluation_info ei
WHERE ei.registered = 0 AND ei.job_code = job AND ei.evaluator = eval;

ELSEIF(choice = 1) THEN
SELECT DISTINCT er.id ,er.job_code AS 'Code',er.empl_username AS 'Username',er.tot_grade AS 'Final Grade',er.comments AS 'Comments'
FROM evaluation_result er
INNER JOIN evaluation_info ei ON er.job_code = ei.job_code
WHERE ei.registered = 1 AND ei.job_code = job AND ei.evaluator = eval
ORDER BY tot_grade DESC;
END IF;

END#

DELIMITER ;


/* stored procedure which updates company's info */

DROP PROCEDURE IF EXISTS updateCompany;

DELIMITER #

CREATE PROCEDURE updateCompany(IN man VARCHAR(12),IN phn BIGINT(16), IN st VARCHAR(15), IN nm TINYINT(4), IN C VARCHAR(15) , IN cntr VARCHAR(15))
BEGIN

DECLARE compCode CHAR(9);

SELECT firm INTO compCode FROM manager WHERE man_username = man;

UPDATE company SET phone = phn , street = st, num = nm, city = c , country = cntr WHERE afm = compCode;

END#

DELIMITER ;




/*returns finalized company's job requests*/

DELIMITER $ 

DROP PROCEDURE IF EXISTS ManShowEvRes $
CREATE PROCEDURE ManShowEvRes (IN manager VARCHAR(12))
BEGIN 

SELECT DISTINCT CONCAT(u.name,' ',u.surname) AS 'Full Name',er.job_code AS 'Job Code',er.empl_username AS 'Username',er.tot_grade AS 'Final Grade',er.comments AS 'Comments'
FROM evaluation_result er
JOIN employee e ON e.username=er.empl_username
JOIN user u ON u.username = e.username
JOIN company c ON c.afm=e.comp_afm
JOIN manager m ON m.firm=c.afm
WHERE m.man_username=manager
ORDER BY e.username;

END$

DELIMITER ;


/*returns unfinalized company's job requests*/

DELIMITER $ 

DROP PROCEDURE IF EXISTS ManShowNoGrade $
CREATE PROCEDURE ManShowNoGrade(IN manager VARCHAR(12))
BEGIN 

SELECT ei.job_code AS 'Job Code', j.pos_title AS 'Job Title', ei.e_username AS 'Username',ei.evaluator_grade AS 'Evaluation',ei.manager_grade AS 'My Grade'
FROM evaluation_info ei
JOIN job j ON j.code = ei.job_code
JOIN evaluator ev ON ev.ev_username = ei.evaluator
JOIN manager m ON m.firm = ev.firm
WHERE ei.registered = 0 AND m.man_username = manager;

END$

DELIMITER ;



/*stored procedure which inserts the new evaluation from gradeField and comField*/


DELIMITER #

DROP PROCEDURE IF EXISTS MangradeEmployee#

CREATE PROCEDURE MangradeEmployee(IN employee VARCHAR(12), IN job INT(4), IN man_grade INT(4) )
BEGIN

UPDATE evaluation_info SET manager_grade = man_grade WHERE job_code = job AND e_username = employee;

END#

DELIMITER ;




/*stored procedure which returns the average grade of evaluation for every evaluator*/
DELIMITER $

DROP PROCEDURE IF EXISTS avgEv $
CREATE PROCEDURE avgEv (IN manager VARCHAR(12))
BEGIN 

SELECT name AS 'Name',surname AS 'Surname', evaluator AS 'Username', AVG(evaluator_grade) AS 'Evaluation AVG '
FROM evaluation_info ei
JOIN evaluator ev ON ev.ev_username=ei.evaluator
JOIN company c ON c.afm=ev.firm
JOIN manager m ON m.firm=c.afm
JOIN user u ON u.username = ev.ev_username
WHERE m.man_username=manager AND registered=1
GROUP BY evaluator;

END $

DELIMITER ;




/*stored procedure which returns employee's folder for a specific company*/
DELIMITER $

DROP PROCEDURE IF EXISTS returnEmp $
CREATE PROCEDURE returnEmp (IN m_username VARCHAR(12),IN empusrnm VARCHAR(12) )
BEGIN

SELECT CONCAT(u.name,' ',u.surname) AS 'Full Name',certificates,awards,reference FROM employee e
JOIN company c ON c.afm=e.comp_afm
JOIN manager m ON m.firm=c.afm 
JOIN user u ON u.username = e.username
WHERE m.man_username=m_username AND e.username = empusrnm;

END$

DELIMITER ;



/*stored procedure which updates employee's folder*/

DELIMITER $

DROP PROCEDURE IF EXISTS updateEmp $
CREATE PROCEDURE updateEmp (IN m_username VARCHAR(12),IN empusrnm VARCHAR(12),IN certif VARCHAR (35),IN awar VARCHAR (35),IN refer VARCHAR(35) )
BEGIN

UPDATE employee
SET certificates=certif , awards=awar , reference=refer
WHERE employee.username=empusrnm;
END$

DELIMITER ;




/*stored procedure which returns employee usernames for a specific company in order to get filled into combo box*/
DELIMITER $

DROP PROCEDURE IF EXISTS returnEmpUsr $
CREATE PROCEDURE returnEmpUsr ( IN manager VARCHAR(12) )
BEGIN

DECLARE afm INT(4);

SELECT firm INTO afm FROM manager m WHERE m.man_username = manager; 

SELECT e.username FROM employee e WHERE e.comp_afm = afm;
END$

DELIMITER ;



/*stored procedure which returns employees full name to get staffed into combobox */ 
DELIMITER $

DROP PROCEDURE IF EXISTS getEmpFullNames $
CREATE PROCEDURE getEmpFullNames (IN manager VARCHAR(12) )
BEGIN

DECLARE afm INT(4);

SELECT firm INTO afm FROM manager m WHERE m.man_username = manager;

SELECT concat(name,' ',surname) AS 'Full Name' FROM user u 
JOIN employee e ON e.username = u.username 
WHERE comp_afm = afm;

END$

DELIMITER ;





/*loads employee's skills*/
DELIMITER $

DROP PROCEDURE IF EXISTS emplProjDegLan $
CREATE PROCEDURE emplProjDegLan(IN empl_usrnm VARCHAR(12))
BEGIN 

SELECT DISTINCT p.url AS 'Projects',l.lang AS 'Languages',hd.degree_title AS 'Degree Title',hd.degree_inst AS 'Institute',
hd.grad_year AS 'Grad.Year',hd.grade AS 'Grade',d.level AS 'Level'
FROM project p  
INNER JOIN employee e ON e.username=p.empl_username
LEFT JOIN languages l ON l.us_employee=e.username
LEFT JOIN has_degree hd ON hd.empl_username=e.username
LEFT JOIN degree d ON d.title=hd.degree_title 
AND d.institute=hd.degree_inst
WHERE e.username=empl_usrnm;


END$

DELIMITER ;


/*make a new user register when called by admin*/
DELIMITER $

DROP PROCEDURE IF EXISTS regNewUser $
CREATE PROCEDURE regNewUser (IN usrnm VARCHAR(12),IN pword VARCHAR(10),IN uname VARCHAR(25),IN usurrname VARCHAR(25),IN umail VARCHAR(30))
BEGIN

INSERT INTO user(username,password,name,surname,register_date,email)
VALUES (usrnm,pword,uname,usurrname,CURRENT_TIMESTAMP,umail);

END $
DELIMITER ;



/*make a new manager register when called by admin*/
DELIMITER $

DROP PROCEDURE IF EXISTS regNewManager $
CREATE PROCEDURE regNewManager (IN m_usrnm VARCHAR(12),IN m_exp_years TINYINT,IN m_firm CHAR(9))
BEGIN 

INSERT INTO manager(man_username,exp_years,firm)
VALUES (m_usrnm,m_exp_years,m_firm);

END $

DELIMITER ;



/*make a new evaluator register when called by admin*/
DELIMITER $

DROP PROCEDURE IF EXISTS regNewEvaluator $
CREATE PROCEDURE regNewEvaluator (IN ev_usrnm VARCHAR(12),IN ev_exp_years TINYINT,IN ev_firm CHAR(9))
BEGIN 

INSERT INTO evaluator (eval_id,ev_username,exp_years,firm)
VALUES (NULL,ev_usrnm,ev_exp_years,ev_firm);

END $

DELIMITER ;




/*make a new employee register when called by admin*/
DELIMITER $ 

DROP PROCEDURE IF EXISTS regNewEmployee $
CREATE PROCEDURE regNewEmployee (IN em_usrnm VARCHAR(12),IN emp_firm CHAR(9),IN emp_bio TEXT,IN emp_exp_years TINYINT,IN emp_cert VARCHAR(35),
IN emp_awards VARCHAR(35),IN emp_refer VARCHAR(35))

BEGIN 

INSERT INTO employee ( username,comp_afm,bio,exp_years,certificates,awards,reference)
VALUES (em_usrnm,emp_firm,emp_bio,emp_exp_years,emp_cert,emp_awards,emp_refer);


END $


DELIMITER ; 




/*make a new company register when called by admin*/
DELIMITER $

DROP PROCEDURE IF EXISTS regNewCompany $
CREATE PROCEDURE regNewCompany(IN c_afm CHAR(9),IN c_doy VARCHAR(15),IN c_name VARCHAR(35),IN c_phone BIGINT(16),IN c_street VARCHAR(15),IN c_num TINYINT(4),
IN c_city VARCHAR(15),IN c_country VARCHAR(15))
BEGIN 

INSERT INTO company (afm,doy,comp_name,phone,street,num,city,country)
VALUES (c_afm,c_doy,c_name,c_phone,c_street,c_num,c_city,c_country);

END $

DELIMITER ;



/*make a new job field/category register when called by admin*/
DELIMITER $

DROP PROCEDURE IF EXISTS regNewField $
CREATE PROCEDURE regNewField (IN f_title VARCHAR(36),IN f_descr TINYTEXT,IN f_belongs_to VARCHAR(36))

BEGIN 

INSERT INTO field (title,descr,belongs_to)
VALUES (f_belongs_to,'Root Element',NULL);

INSERT INTO field(title,descr,belongs_to)
VALUES (f_title,f_descr,f_belongs_to);

END $

DELIMITER ;



/* stored procedure which calcualtes the qual grade*/

DROP PROCEDURE IF EXISTS calcQualGrade;

DELIMITER #
CREATE PROCEDURE calcQualGrade(IN usr VARCHAR(12),OUT gr INT(4))
BEGIN

DECLARE cnt SMALLINT;
DECLARE grade INT(4);

set grade = 0;
set cnt = 0;

SELECT count(*) INTO cnt FROM project WHERE empl_username = usr;

IF ( cnt >=2 ) THEN
SET grade = 0.5;
ELSE SET grade = 0;
SET cnt = 0;
END IF;

SELECT count(title) INTO cnt
FROM degree d
JOIN has_degree hd ON hd.degree_title = d.title AND hd.degree_inst = d.institute
JOIN employee e ON hd.empl_username = e.username
WHERE e.username = usr;

IF(cnt >=3) THEN 
SET grade = grade + 1;
ELSEIF (cnt = 1) THEN
SET grade = grade + 0.5;
END IF;

SET cnt = 0;
 
SELECT count(us_employee) INTO cnt FROM languages WHERE us_employee = usr;

IF(cnt>=1) THEN 
SET grade = grade + 0.5;
END IF;

IF(grade > 2) THEN
SET grade = 2;
END IF;

SET gr = grade;

END#

DELIMITER ;



/*for employee */
DELIMITER $ 

DROP PROCEDURE IF EXISTS createNewEmplProject $
CREATE PROCEDURE createNewEmplProject (IN em_usrnm VARCHAR(12),IN des TEXT ,IN purl VARCHAR(60))
BEGIN 

INSERT INTO project (empl_username,num,descr,url)
VALUES (em_usrnm,NULL,des,purl);


END $ 

DELIMITER ;




DELIMITER $ 

DROP PROCEDURE IF EXISTS DeleteEmplProject $
CREATE PROCEDURE DeleteEmplProject(IN Em_Name VARCHAR(12),IN P_Code INT(4))
BEGIN 
DELETE FROM project WHERE num = P_Code AND empl_username = Em_Name;
END$
DELIMITER ;



DELIMITER $
DROP PROCEDURE IF EXISTS createNewEmplLan $
CREATE PROCEDURE createNewEmplLan(IN em_usrnm VARCHAR(12) , IN lan SET('EN','FR','SP','GR'))
BEGIN 
DECLARE usr VARCHAR(12);
SELECT us_employee INTO usr FROM languages WHERE us_employee = em_usrnm;
IF (usr IS NOT NULL) THEN
UPDATE languages SET lang = lan WHERE us_employee = usr;
ELSEIF (usr IS NULL) THEN
INSERT INTO languages (us_employee,lang) VALUES (em_usrnm,lan);
END IF;
END $
DELIMITER ;







DELIMITER $
DROP PROCEDURE IF EXISTS changeBios $
CREATE PROCEDURE changeBios (IN Em_Username VARCHAR(12),IN New_Bios TEXT)
BEGIN
UPDATE employee SET bio = New_Bios WHERE username = Em_username;
END$
DELIMITER ;




DROP PROCEDURE IF EXISTS RequestEval;
DELIMITER #
CREATE PROCEDURE RequestEval(IN usr VARCHAR(12) , IN job INT(4), IN somewords TEXT)
BEGIN
INSERT INTO request_evaluation(empl_username,job_code,interview) VALUES
(usr,job,somewords);
END#
DELIMITER ;




DROP PROCEDURE IF EXISTS ShowJobs;
DELIMITER #
CREATE PROCEDURE ShowJobs(IN Em_username VARCHAR(12))
BEGIN
SELECT j.code,j.start_date AS 'Starts',j.salary,j.pos_title AS 'Titlle',j.edra AS 'Head Office',j.evaluator,j.announce_date AS 'Announced',j.deadline
FROM job j
JOIN evaluator ev
ON ev.ev_username = j.evaluator
JOIN employee e
ON e.comp_afm = ev.firm
WHERE e.username = Em_username;
END#
DELIMITER ;




DROP PROCEDURE IF EXISTS ShowAppliedJobs;
DELIMITER #
CREATE PROCEDURE ShowAppliedJobs(IN Em_username VARCHAR(12))
BEGIN
SELECT j.code,j.pos_title AS 'Title',er.tot_grade AS 'Total',re.interview FROM request_evaluation re
JOIN job j 
ON re.job_code = j.code
LEFT JOIN evaluation_result er ON er.empl_username = re.empl_username AND re.job_code = er.job_code
WHERE re.empl_username = Em_username;
END#
DELIMITER ;





DROP PROCEDURE IF EXISTS DeleteAppliedJobs;
DELIMITER #
CREATE PROCEDURE DeleteAppliedJobs(IN usr VARCHAR(12),IN job INT(4))
BEGIN

DECLARE tempUsr VARCHAR(12);
SELECT ei.e_username INTO tempUsr FROM evaluation_info ei WHERE ei.e_username = usr AND ei.job_code = job;

IF(tempUsr IS NULL) THEN
DELETE FROM request_evaluation WHERE empl_username = usr AND job_code = job;
ELSE 
SIGNAL SQLSTATE VALUE '45000'
SET MESSAGE_TEXT = 'Cannot delete once its evaluated.';
END IF;

END#
DELIMITER ;




DROP PROCEDURE IF EXISTS UpdateApplication;
DELIMITER #
CREATE PROCEDURE UpdateApplication(IN usr VARCHAR(12),IN job INT(4),IN Interv TEXT)
BEGIN

DECLARE tempUsr VARCHAR(12);
SELECT ei.e_username INTO tempUsr FROM evaluation_info ei WHERE ei.e_username = usr AND ei.job_code = job;

IF(tempUsr IS NULL) THEN
UPDATE request_evaluation SET interview = Interv WHERE empl_username = usr AND job_code = job;
ELSE 
SIGNAL SQLSTATE VALUE '45000'
SET MESSAGE_TEXT = 'Cannot Update Once its evaluated.';
END IF;
END#
DELIMITER ;



DROP PROCEDURE IF EXISTS ShowProject;
DELIMITER #
CREATE PROCEDURE ShowProject(IN usr VARCHAR(12))
BEGIN
SELECT descr,url FROM project WHERE empl_username = usr ;
END #
DELIMITER ;


DROP PROCEDURE IF EXISTS ShowDegree;
DELIMITER #
CREATE PROCEDURE ShowDegree(IN usr VARCHAR(12))
BEGIN

SELECT DISTINCT d.title,d.institute,d.level,hd.grad_year,hd.grade FROM degree d
JOIN has_degree hd ON d.title = hd.degree_title AND d.institute = hd.degree_inst
WHERE hd.empl_username = usr;
END #
DELIMITER ;



DELIMITER $
DROP PROCEDURE IF EXISTS createNewEmplLan $
CREATE PROCEDURE createNewEmplLan(IN em_usrnm VARCHAR(12) , IN lan SET('EN','FR','SP','GR'))
BEGIN 
DECLARE usr VARCHAR(12);
SELECT us_employee INTO usr FROM languages WHERE us_employee = em_usrnm;
IF (usr IS NOT NULL) THEN
UPDATE languages SET lang = lan WHERE us_employee = usr;
ELSEIF (usr IS NULL) THEN
INSERT INTO languages (us_employee,lang) VALUES (em_usrnm,lan);
END IF;
END $
DELIMITER ;




DELIMITER $

DROP PROCEDURE IF EXISTS createNewEmplDegree $
CREATE PROCEDURE createNewEmplDegree (IN d_title VARCHAR(50),IN d_inst VARCHAR(40),IN em_usrnm VARCHAR(12),
IN gradye YEAR(4),IN d_grade FLOAT(3,1),IN d_level ENUM('LYKEIO','IEK','TEI','BSc','MSc','PHD'))

BEGIN 

IF NOT EXISTS (SELECT title,institute FROM degree where title = d_title AND institute = d_inst) THEN
INSERT INTO  degree (title,institute,level)
VALUES (d_title,d_inst,d_level);
END IF;

INSERT INTO has_degree (degree_title,degree_inst,empl_username,grad_year,grade)
VALUES (d_title,d_inst,em_usrnm,gradye,d_grade);

END $

DELIMITER ;



