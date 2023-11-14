INSERT INTO users (id, first_name, last_name, email, password, phone_number, role)
VALUES (1, 'Manas', 'Abdugani uulu', 'admin@gmail.com', '$2a$12$xn.XL/ZseJtGODxaKtbqf.05yQlma3hGcxHFH/ywUl7w3yrlEORrW', '+996221010101', 'ADMIN'), --Admin123
       (2, 'Aijamal', 'Asangazieva', 'aijamal@gmail.com', '$2a$12$28dRuthp7v2zllKPCjZsAOVQ9PztcbMUiifibAMa2J92hnSRnqx52', '+99622100101', 'INSTRUCTOR'),     --instructor123
       (3, 'Datka', 'Mamatzhanova', 'datka@gmail.com', '$2a$12$q4VHqHXBed9gw4ToiiGg9uQ3mWwrBnnGBy54E/UF4GzP2A3xAbEdy', '+99622101101', 'INSTRUCTOR'),         --instructor321
       (4, 'Baytik', 'Taalaybekov', 'baytik@gmail.com', '$2a$12$VIrzHAe0rAklGWakK.rsQOH0cwynRNbWWHB8PkJ8b5SqBdpVQsyou', '+99622101002', 'STUDENT'),--student321
       (5, 'Aizat', 'Duisheeva', 'malik@gmail.com', '$2a$12$CyLOcsHqEsVjzx7Htir70uoAvBIunZiROf32K9XC2Nxb72aMMcFWu', '0700123456', 'STUDENT'),      --student123
       (6, 'Aigerim', 'Bektenova', 'aizat@gmail.com', '$2a$12$jjqTsweI.rgnK1lJb63u..nwbOor4AXW8MgfG8.kT5WmykCYM..1a', '0700423456', 'STUDENT');    --student12345

insert into instructors(id, specialization, user_id)
VALUES (1, 'INSTRUCTOR', 2),
       (2, 'INSTRUCTOR', 3);

INSERT INTO groups (id, group_name, image, description, create_date, date_of_graduate)
VALUES (1, 'Java-9', 'groups java', 'Java', now(), '2023-12-01'),
       (2, 'JS-9', 'groups js', 'JavaScript', now(), '2024-11-06'),
       (3, 'Python-9', 'groups python', 'Python', now(), '2024-08-12');

insert into students(id, study_format, is_blocked, group_id, user_id)
VALUES (1, 'ONLINE', true, 1, 4),
       (2, 'OFFLINE', false, 2, 5),
       (3, 'ONLINE', true, 3, 6);

insert into courses(id, course_name, image, description, date, date_of_graduation, group_id)
VALUES (1, 'Java', 'course photo', 'Backend', now(),'2024-11-02', 1),
       (2, 'JavaScript-9', 'course photo', 'Frontend', now(),'2024-05-11', 2),
       (3, 'Python-9', 'course photo', 'Backend', now(),'2024-04-10', 3);

insert into courses_instructors(courses_id, instructors_id)
VALUES (1, 1),
       (2, 2);

insert into lessons(id, lesson_name, course_id)
VALUES (1, 'Array', 1),
       (2, 'Loop', 2),
       (3, 'OOP', 3);

insert into presentations(id, name, description, link_file_ppt, lesson_id)
VALUES (1, 'Array', 'Arrays explanation', 'String', 1),
       (2, 'Loop', 'Loop explanation', 'String', 2),
       (3, 'OOP', 'Loop ', 'String', 3);

insert into video_lessons(id, name, description, link, lesson_id)
VALUES (1, 'Array', 'link lesson', 'Lesson', 1),
       (2, 'Loop', 'link lesson', 'Lesson', 2),
       (3, 'Loop', 'OOP lesson', 'Lesson', 3);

insert into tasks(id,task_name,lesson_id,text,file_name,file_link,link_name,link,image,code,deadline)
VALUES (1, 'Task code', 1,'text','File-name','Description','link name','link','image','code','2023-07-30'),
       (2, 'Task code', 2,'text','File-name','Description','link name','link','image','code','2023-07-30'),
       (3, 'Task code', 3,'text','File-name','Description','link name','link','image','code','2023-07-30');

insert into links(id, lesson_id, link, text)
VALUES (1, 1, 'Link to information', 'Array'),
       (2, 2, 'Link to information', 'Loop'),
       (3, 3, 'Link ', 'OOP');

insert into tests(id, name, is_enable, lesson_id)
VALUES (1, 'testingOOP', true, 1),
       (2, 'testHibernate', false, 2),
       (3, 'testJDBC', true, 3);

insert into questions(id, question_name, question_type, test_id)
VALUES (1, 'OOP', 'SINGLE', 1),
       (2, 'JDBC', 'MULTIPLE', 2),
       (3, 'JDBCTEMPLATE', 'SINGLE', 3);

insert into test_answers(id, test_name, correct, in_correct, point, is_accepted, test_id, student_id)
VALUES (1, 'testingOP', 2, 3, 5, true, 1, 1),
       (2, 'testingHibernate', 3, 4, 4, false, 2, 2),
       (3, 'testingOP', 2, 3, 5, true, 1, 1);

insert into task_answers(id, file, text, comment, task_answer_status, student_id, task_id)
VALUES (1, 'file', 'text', 'comment', 'REVIEWED', 1, 1),
       (2, 'image', 'file', 'comment', 'REVIEWED', 2, 2),
       (3, 'link', 'image', 'comment', 'REVIEWED', 3, 3);

insert into option_tests(id, option, is_true, question_id)
VALUES (1, 'String', false, 1),
       (2, 'String', true, 2),
       (3, 'String', false, 3);

insert into question_answers(id, point, question_id, test_answer_id)
VALUES (1, 2, 2, 1),
       (2, 3, 2, 2),
       (3, 4, 3, 3);

insert into question_answers_option(option_id, question_answer_id)
Values (1, 1),
       (2, 2),
       (3, 3);
