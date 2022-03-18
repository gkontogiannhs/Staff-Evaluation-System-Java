/*companies*/
  INSERT INTO company(afm,doy,comp_name,phone,street,num,city,country) VALUES
  (123456789,'Doy of Patras','IBM',2634025689,'Korinthou',65,'patra','Greece'),
  (987654321,'Doy of Athens','Microsoft',21058585832,'panepistimiou',76,'Athens','Greece');



/* employees of IBM*/
INSERT INTO user(username,password,name,surname,register_date,email) VALUES
('Current','voltage','Dario','Gil','2010-12-05 00:00:00','Gil@gmail.com'),
('ColdBeer','ice','Juan','Zufiria','2005-06-06 15:13:12','JuanZ@outlook.com'),
('ILoveGaming','Dota2','Rob','Thomas','2014-03-01 17:32:00','ThomasRob@yahoo.com'),
('PinkFloyd','Dogs','Christina','Montgomery','2017-11-08 21:07:12','Christina@hotmail.com'),

/* IBM manager*/
('strawberies','TheBest','Virginia','Rometty','2010-01-01 13:00:00','VirginiaRom@gmail.com'),

/*IBM Evaluators*/
('NoShave','Mustache','Arvind','Krishna','2010-02-05 18:08:00','ArvindKri@outlook.com'),
('Energy','BlueShirts','Jim','Whitehurst','2010-04-08 11:25:32','Whitehurst@hotmail.com'),

/*employess of Microsoft*/
('League', 'garenmid', 'Chris', 'Capossela', '2018-02-13 12:23:34', 'Capossela@gmail.com'),
('BaldMan', 'ImissHair', 'Kevin', 'Scott', '2017-05-11 14:08:23', 'KevinScott@yahoo.com'),
('Earings', 'bestdriver', 'Amy', 'Hood', '2018-10-07 20:09:10', 'Amy@gmail.com'),

/* Microsoft Manager*/
('Computers','Windows','Bill','Gates','2008-07-05 22:13:32','BillGates@outlook.com'),

/* Microsoft Evaluator*/
('circuit','HappyLife','Brad','Smith','2009-05-02 14:00:00','Brad@outlook.com');




 INSERT INTO employee(username,comp_afm,bio,exp_years,certificates,awards,reference) VALUES
    /*IBM*/
    ('Current',123456789,'Dr. Gil is a Trustee of the New York Hall of Science, and is a member of the National Science Board (NSB), the governing body 
     of the National Science Foundation (NSF). Dr. Gil received his Ph.D. in Electrical Engineering and Computer Science from MIT.).'
     ,10,'Gils_RecLetters.pdf','Gils_Awards.pdf','Gils_References.pdf'),

    ('ColdBeer',123456789,'Mr. Zufiria is married and has two children. He earned a Ph.D. in Aeronautical Engineering from the Polytechnic University of
     Madrid and a Ph.D. in Applied Mathematics from the California Institute of Technology, where he also served as faculty. In addition, he holds an MBA
     from the London School of Economics.',20,'Juans_RecLetters.pdf','Juans_Awards.pdf','Juans_References.pdf'),

    ('ILoveGaming',123456789,'Born in Florida, Rob studied economics at Vanderbilt University, earning his BA in Economics. During his MBA from the
       University of Florida, Rob worked in equity research, learning applied economics, finance, and financial analysis. Rob serves on the board of Domus
       (Stamford, CT), which assists underprivileged children in Fairfield county. He is an active volunteer at Filling in the Blanks, an organization 
       focused on fightingchildhood hunger in local communities. He lives in New Canaan, CT with his wife and three children.',14,'Robs_RecLetters.pdf',
       'Robs_Awards.pdf','Robs_references.pdf'),

    ('PinkFloyd',123456789,'Ms. Montgomery received a B.A. from Binghamton University and a J.D. from Harvard Law School.',8,'christinas_RecLetters.pdf',
    'Christinas_Awards.pdf','Christinas_References.pdf'),

     /*Microsoft*/
    ('League',987654321,'Capossela holds a Bsc degree in computer science and economics from Harvard University. Originally from Boston, his interest
      in technology began when, as a boy, he wrote a reservation system for his family’s small Italian restaurant using dBase for DOS on an early IBM PC.',
      11,'Chris_RecLetters.pdf','Chris_Awards.pds','Chris_References.pdf'),

    ('BaldMan',987654321,'Scott holds an M.S. in computer science from Wake Forest University, a B.S. in computer science from Lynchburg College, and has
     completed most of his Ph.D. in in computer science at the University of Virginia.',16,'Scotts_RecLetters.pdf','Scotts_Awards.pdf','Scotts_References.pdf'),

    ('Earings',987654321,'Amy earned a bachelor’s degree in economics from Duke University and a master’s degree in business administration from Harvard
      University. Hood lives in Seattle with her husband and daughters.',10,'Amys_RecLetters.pdf','Amys_Awards.pdf','Amys_References.pdf');




INSERT INTO evaluator(eval_id,ev_username,exp_years,firm) VALUES

(NULL,'NoShave',25,123456789),
(NULL,'Energy',17,123456789),
(NULL,'circuit',18,987654321);



INSERT INTO manager(man_username,exp_years,firm) VALUES
('strawberies',30,123456789),
('Computers',28,987654321);


INSERT INTO degree(title,institute,level) VALUES
('Electrical Engineer','massachusett institute of technology','PHD'),
('Aeronautical Engineer','Polytechnic University of Madrid','PHD'),
('Applied Mathematics','California Institute of Technology','PHD'),
('Economics','London School of Economics','MSc'),
('Economics','Vanderbilt University','MSc'),
('Computer Science','Harvard University','BSc'),
('Law','Harvard University','BSc'),

('Economics','Harvard University','BSc'),
('Computer Science','Wake Forest University','MSc'),
('Computer Science','University of Virginia','PHD'),
('Economics','Duke University','BSc'),
('Business Administration','Harvard University','MSc');



INSERT INTO has_degree(degree_title,degree_inst,empl_username,grad_year,grade) VALUES
('Electrical Engineer','massachusett institute of technology','Current',1995,7.8),
('Aeronautical Engineer','Polytechnic University of Madrid','ColdBeer',1980,8.7),
('Applied Mathematics','California Institute of Technology','ColdBeer',1984,8.0),
('Economics','London School of Economics','ColdBeer',1995,9.5),
('Economics','Vanderbilt University','ILoveGaming',1988,6.0),
('Computer Science','Harvard University','ILoveGaming',1991,8.6),
('Law','Harvard University','PinkFloyd',1990,9.7),

('Economics','Harvard University','League',1998,10.0),
('Computer Science','Harvard University','League',2002,9.5),
('Computer Science','Wake Forest University','BaldMan',1990,6.7),
('Computer Science','University of Virginia','BaldMan',2005,8.9),
('Economics','Duke University','Earings',2000,8.0),
('Business Administration','Harvard University','Earings',2005,9.1);


INSERT INTO project(num,empl_username,descr,url) VALUES
(NULL,'ColdBeer','Profit margin expansion, and significant new signings with clients across a spectrum of industries.','https://newsroom.ibm.com/Juan-Zufiria'),
(NULL,'ILoveGaming','The AI Ladder: Accelerate Your Journey to AI.','https://newsroom.ibm.com/Rob-Thomas'),
(NULL,'Current','COVID-19 High-Performance Computing Consortium','https://newsroom.ibm.com/dario-gil'),
(NULL,'PinkFloyd','Law Departments strategic and transformational initiatives','https://newsroom.ibm.com/Christina-Montgomery'),
(NULL,'League','Consumer Business organization','https://news.microsoft.com/exec/chris-capossela/'),
(NULL,'BaldMan','Reprogramming the American Dream','https://news.microsoft.com/exec/kevin-scott/'),
(NULL,'Earings','worldwide finance organization','https://news.microsoft.com/exec/amy-hood/');


INSERT INTO job(code,start_date,salary,pos_title,edra,evaluator,announce_date,deadline) VALUES
(NULL,'2021-06-23',NULL,'data analyst', 'Patra, Greece','NoShave','2020-12-22 19:00:00','2021-06-05'),
(NULL,'2021-04-04',NULL,'web programmer', 'Patra, Greece','Energy','2020-12-22 19:00:00','2021-06-05'),
(NULL,'2021-03-28',NULL,'mobile app developer', 'Patra, Greece','circuit','2020-12-22 19:00:00','2020-06-05');



INSERT INTO field (title,descr,belongs_to) VALUES
('Computer Science', 'Root element, no more general antikeim', NULL),
('Databases', 'Level one element, child of Computer Science', 'Computer Science'),
('AI', 'Level one element, child of Computer Science', 'Computer Science');


INSERT INTO needs(job_code,field_title) VALUES
(1, 'Databases'),
(2,'AI'),
(3, 'Databases');



INSERT INTO request_evaluation(empl_username,job_code,interview) VALUES
('ColdBeer',1,'HIRE ME YOU IDIOT'),
('ILoveGaming',1,'Last year i proved my value.');


INSERT INTO evaluation_info(evaluator,e_username,job_code,manager_grade,evaluator_grade,qual_grade,comments,registered) VALUES
('NoShave','ColdBeer',1,4,4,2,'This guy sucks.',1),
('NoShave','ILoveGaming',1,4,4,2,'Every little is noticable.',0);


INSERT INTO evaluation_result(id,empl_username,job_code,tot_grade,comments) VALUES
(NULL,'ColdBeer',1,10,'This guy sucks.');







