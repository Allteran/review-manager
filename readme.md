## Documentation
### Getting started
You can build project with <code> ./gradlew build </code>. It will build <code>*.jar</code> file to run with java. <br> 
To store data uses **PostgreSQL**, so before run you should create database with granted privileges on database and pass 
some params to <code>*jar</code> file: <br>
* <code>SPRING_DATASOURCE_URL</code> - URL where your PosrgreSQL database installed;
* <code>SPRING_DATASOURCE_USERNAME</code> - username of database user that have privileges on database;
* <code>SPRING_DATASOURCE_PASSWORD</code> - password of early mentioned user;

Second way is just to clone project and run it in debug mode (it shure will be more usefull)

### Functionality for anonymous User
Simple users can pass reviews without login and registration. Service generates UUID for each anonymous user
using <code>Cookie</code>. For each simple user you can use next methods:
* <code>GET: api/review/active</code> - non passed reviews
* <code>GET: api/review/passed</code> - passed reviews
* <code>POST: api/review/start/{id}</code> - start to pass review
* <code>POST: api/review/end/{id}</code> - end to pass review

Every time when user starts or ends review - __Review Manager__ copy selected review to current user. Considering that every 
of our user is non-logged, you may ask "How does it know who is who?", that is the way: every time when user comes first 
time to __Review Manager__ it creates new anonymous user with autogenerated ID and stores ID to cookie. Then, everytime you try to 
call any of POST methods - __Review Manager__ will check cookies for saved ID and compare it to existing users.
### Functionality for Admin
Admin should exist in system, so first you will want to create user in PostreSQL via command line or put <code>@PostConstruct</code>
annotation to init method where you will create user with admin right, for example: <br>
<code>
@PostConstruct <br>
    public void initUser() { <br>
        User admin = new User(); <br>
        admin.setId(10); <br>
        admin.setUsername("admin");<br>
        admin.setPassword("admin");<br>
        admin.setRoles(Collections.singleton(Role.ADMIN));<br>
        createUser(admin);<br>
 }
</code>
<br>
<br>
Administrator have next methods to work with service:
* <code>GET: api/adm/review/list</code> - list all of reviews
* <code>GET: api/adm/review/{id}</code> - load review by ID
* <code>POST: api/adm/review/new</code> - create new review (see more detail instruction below). You should pass <code>Review</code> object as param to request.
* <code>PUT: api/adm/review/{id}</code> - update review by ID. Note, if field <code>startDate</code> isn't null - 
* method will return original review. As params it pulls original __Review__ by it's id and takes __Review__ with updated fields as second param.
* <code>DELETE: api/adm/review/{id}</code> - delete review by ID from database. Takes by entered ID current review.

### Create new Review
To create some review you should understand the design of entity **Review**. Let's get some fields to understand that: <br>
1. __Id__ [Long] - UUID. Generates with Hibernate for database.
2. __Name__ [String] - name of Review.
3. __Description__ [String] - description of Review.
4. __Start Date__ [LocalDateTime] - date and time of start Review.
5. __End Date__ [LocalDateTime] - date and time of ending Review.
6. __Questions__ [Set] - set of questions for review. Build with entity **Question**. Uses <code>OneToMany</code> type of relations.

**Question** entity has also its own structure:
1. __Id__ [Long] - UUID. Generates with Hibernate for database.
2. __Text__ [String] - text of question.
3. __Question Type__ [ENUM] - type of question, for further details see instructions below.
4. __Answers__ [Set] - set of answers for question. Build with entity __Answer__. Depends on <code>QuestionType</code> 
5. set will have one or several answers. Uses <code>OneToMany</code> type of relations.

**QuestionType** (ENUM) could be:
* SINGLE - for single choice questions.
* MULTIPLE - for multiple choice questions.
* TEXT - for answers where user has to type his own answer

**Answer** has next structure:
* __Id__ [Long] - UUID. Generates with Hibernate for database.
* __Text__ [String] - text of answer. Depends on <code>QuestionType</code> it could be filled with user or created with Review creator.
* __Checked__ [Boolean] - flag for end user that shows if user checked current answer. 
