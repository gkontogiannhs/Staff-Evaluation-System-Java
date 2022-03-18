DROP DATABASE IF EXISTS db;

CREATE DATABASE db;

USE db;


CREATE TABLE company(
  afm CHAR(9) NOT NULL,
  doy VARCHAR(15),
  comp_name VARCHAR(35) NOT NULL DEFAULT 'NO-NAME registered',
  phone BIGINT(16),
  street VARCHAR(15) ,
  num TINYINT(4),
  city VARCHAR(15),
  country VARCHAR(15),
  PRIMARY KEY(afm)
  );





CREATE TABLE user(
	username VARCHAR(12) NOT NULL,
	password VARCHAR(10) NOT NULL,
	name VARCHAR(10) NOT NULL,
	surname VARCHAR(25) NOT NULL,
	register_date DATETIME,
	email VARCHAR(30),
	PRIMARY KEY(username)
);







CREATE TABLE employee(
	username VARCHAR(12) NOT NULL,
	comp_afm CHAR(9) NOT NULL,
	bio TEXT NOT NULL,
	exp_years tinyint,
	certificates VARCHAR(35),
	awards VARCHAR(35),
	reference VARCHAR(35),
	PRIMARY KEY(username),

	CONSTRAINT user_emp FOREIGN KEY(username)  REFERENCES user(username)
	ON DELETE CASCADE ON UPDATE RESTRICT,

	CONSTRAINT emp_comp FOREIGN KEY(comp_afm) REFERENCES company(afm)
	ON DELETE CASCADE ON UPDATE CASCADE
);
    

 


CREATE TABLE evaluator(
	eval_id INT(4) NOT NULL AUTO_INCREMENT,
	ev_username VARCHAR(12) NOT NULL,
	exp_years TINYINT,
	firm CHAR(9),

	PRIMARY KEY(eval_id,ev_username),

	CONSTRAINT ev_user FOREIGN KEY(ev_username) REFERENCES user(username)
    ON DELETE CASCADE ON UPDATE RESTRICT,

    CONSTRAINT ev_comp FOREIGN KEY(firm) REFERENCES company(afm)
    ON DELETE CASCADE ON UPDATE CASCADE
);





CREATE TABLE manager(
  man_username VARCHAR(12) NOT NULL,
  exp_years TINYINT(4),
  firm CHAR(9) NOT NULL,
  PRIMARY KEY(man_username),

  CONSTRAINT man_user FOREIGN KEY(man_username) REFERENCES user(username)
  ON DELETE CASCADE ON UPDATE RESTRICT,

  CONSTRAINT man_comp FOREIGN KEY(firm) REFERENCES company(afm)
  ON DELETE CASCADE ON UPDATE CASCADE
);





CREATE TABLE degree(
	title VARCHAR(50) NOT NULL,
	institute VARCHAR(40) NOT NULL,
	level ENUM('LYKEIO','IEK','TEI','BSc','MSc','PHD'),

	PRIMARY KEY(title,institute)
);




CREATE TABLE has_degree(
    degree_title VARCHAR(50),
    degree_inst VARCHAR(50),
    empl_username VARCHAR(12),
    grad_year YEAR(4),
    grade FLOAT(3,1),

    PRIMARY KEY(degree_title,degree_inst,empl_username),

    CONSTRAINT hd_degr FOREIGN KEY(degree_title,degree_inst) REFERENCES degree(title,institute)
    ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT hd_user FOREIGN KEY(empl_username) REFERENCES user(username)
    ON DELETE CASCADE ON UPDATE CASCADE
);





CREATE TABLE languages(
  us_employee VARCHAR(12) NOT NULL,
  lang SET('EN','FR','SP','GR'),

  PRIMARY KEY(us_employee,lang),

  CONSTRAINT lang_emp FOREIGN KEY(us_employee) REFERENCES employee(username)
  ON DELETE CASCADE ON UPDATE CASCADE  
);





CREATE TABLE project(
    num INT(4) NOT NULL AUTO_INCREMENT,
    empl_username VARCHAR(12) NOT NULL,
    descr TEXT,	
    url VARCHAR(60),

    PRIMARY KEY(num,empl_username),

    CONSTRAINT proj_emp FOREIGN KEY(empl_username) REFERENCES employee(username)
    ON DELETE CASCADE ON UPDATE CASCADE
);





CREATE TABLE job(
	code INT(4) NOT NULL AUTO_INCREMENT,
	start_date DATE NOT NULL,
	salary FLOAT(6,1),
	pos_title VARCHAR(40) NOT NULL,
	edra VARCHAR(40) NOT NULL,
	evaluator VARCHAR(12) NOT NULL,
	announce_date DATETIME DEFAULT NOW(),
	deadline DATE NOT NULL,

	PRIMARY KEY(code),

	CONSTRAINT job_eval FOREIGN KEY(evaluator) REFERENCES evaluator(ev_username)
	ON DELETE CASCADE ON UPDATE CASCADE
);





CREATE TABLE field(
	title VARCHAR(36) NOT NULL,
	descr TINYTEXT, 
	belongs_to VARCHAR(36),

	PRIMARY KEY(title),

	CONSTRAINT field FOREIGN KEY(belongs_to) REFERENCES field(title)
	ON DELETE CASCADE ON UPDATE CASCADE
);



CREATE TABLE needs(
	job_code INT(4) NOT NULL,
	field_title VARCHAR(36) NOT NULL,

	PRIMARY KEY(job_code,field_title),

	CONSTRAINT needs_job FOREIGN KEY(job_code) REFERENCES job(code)
	ON DELETE CASCADE ON UPDATE CASCADE,

	CONSTRAINT needs_field FOREIGN KEY(field_title) REFERENCES field(title)
    ON DELETE CASCADE ON UPDATE CASCADE	
);





CREATE TABLE request_evaluation(
	empl_username VARCHAR(12) NOT NULL,
	job_code INT(4) NOT NULL,
	interview TEXT,

	PRIMARY KEY(empl_username,job_code),

	CONSTRAINT re_empl FOREIGN KEY(empl_username) REFERENCES employee(username)
	ON DELETE CASCADE ON UPDATE CASCADE,

	CONSTRAINT re_job FOREIGN KEY(job_code) REFERENCES job(code)
	ON DELETE CASCADE ON UPDATE CASCADE 
); 





CREATE TABLE evaluation_info(
	evaluator VARCHAR(12) NOT NULL,
	e_username VARCHAR(12) NOT NULL,
	job_code INT(4) NOT NULL,
	manager_grade INT(4),
	evaluator_grade INT(4),
	qual_grade INT(4),
	comments VARCHAR(255),
	registered BOOLEAN DEFAULT 0,

	PRIMARY KEY(evaluator,e_username,job_code),

	CONSTRAINT ei_re FOREIGN KEY(e_username,job_code) REFERENCES request_evaluation(empl_username,job_code)
	ON DELETE CASCADE ON UPDATE CASCADE,

	CONSTRAINT ei_eval FOREIGN KEY(evaluator) REFERENCES evaluator(ev_username)
    ON DELETE CASCADE ON UPDATE CASCADE

);





CREATE TABLE evaluation_result(
	id INT(4) NOT NULL AUTO_INCREMENT,
	empl_username VARCHAR(12) NOT NULL,
	job_code INT(4),
	tot_grade INT(4) NOT NULL,
	comments VARCHAR(255) NOT NULL,

	PRIMARY KEY(id,empl_username,job_code),

	CONSTRAINT er_empl FOREIGN KEY(empl_username) REFERENCES employee(username)
	ON DELETE CASCADE ON UPDATE CASCADE,

	CONSTRAINT er_job FOREIGN KEY(job_code) REFERENCES job(code)
	ON DELETE CASCADE ON UPDATE CASCADE 
);



CREATE TABLE log(
	num INT(4) NOT NULL AUTO_INCREMENT,
	user VARCHAR(12) NOT NULL,
	date_time DATETIME,
	action VARCHAR(30), /*CAN ALSO BE BOOLEAN */
	table_name VARCHAR(30),
	kind VARCHAR(10),

	PRIMARY KEY(num,user),

	CONSTRAINT log_user FOREIGN KEY(user) REFERENCES user(username)
	ON DELETE CASCADE ON UPDATE CASCADE
);


    