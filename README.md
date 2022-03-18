# Staff Evaluation System

![alt text](https://github.com/gkontogiannhs/StaffEvaluation/blob/main/ERDiagramm.png)

# The above Database concerns a staff evaluation system of a group of companies to which four categories of users are connected:
## Managers
The Managers of the company in which the staff is evaluated decide on the positions that will be announced for promotion. They also update employee records whenever there is a change (eg additional certification, additional letter of recommendation, etc.).
## Evaluators
Evaluators who work with the company for evaluation staff, announce the positions that employees can claim and make ratings.
## Employees
Employees of the company who apply for position evaluation in which they can be promoted or moved. All employees of each company are informed for positions that are announced for promotion in their company and can apply for the position or positions they are interested in.
## Administrators
The Administrators of the system that implements the staff evaluation. Also, admin holds an action table in which will record the actions performed by any user category. This table is updated each time an insert, update or delete proccess take place and stores the user's username, date and time of the event, if it was executed successfully or not, its type and the name of the table concerned.


## Information stored for each category (DB)
- The system supports more than one group of companies.
- For each company, the system maintains the following data: VAT, the Tax Office, phone number and loacation (street, number, city and country).
- For each user, the system maintains the following data: username, e-mail, password, registration date, the VAT of the company he's/she's currently working, years of experience in that company, CV, foreign languages, certifications and reports of discrimination received during his/her work at the company. In addition, employees can save their projects in the system that have implemented, inside or outside the company. 
- For each manager, the system maintains the following data: the company in which they work and total years of experience.
- The evaluators announces the jobs available in the company, directed to the promotion of the existing staff.


# Functioning per category (via GUI)
## The Manager can:
- Update the information of the company and his/her personal information registered in the system
- Modify the salary from a promotion position that has been announced in his company.
- Read the evaluation results for the positions of his/her company that have been finalized.
- Read the average evaluation grade for every evaluator in the company
- Read and update the employee's file
- Given an employee name, display a list of all the applications, the final evaluations completed and the evaluators info respected.

## The Manager can:
- View and edit his/her account information
- Have access to the information of the promotion positions of the company
- Add a new promotion position by entering all the necessary information
- View all the jobs he has added to the system being able to edit them
- View the number of applications submitted and enter evaluation data in the positions (only the ones submitted by his/herself and deadline is over)

## The Employee can:
- Update account's personal information
- Update Bio. All the other information is being updated by the manager
- Apply to one or more of the open positions for promotion
- Display job applications
- To withdraw a candidacy if the position is still open for application evaluation

## The Administrator can:
- Create new accounts in the system for every type of user
- To insert new companies of the group, new objects of interest and new activity sectors of a company
