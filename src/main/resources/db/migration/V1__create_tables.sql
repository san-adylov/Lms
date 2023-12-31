-- drop table if exists links cascade;
--
-- drop table if exists presentations cascade;
--
-- drop table if exists courses_instructors cascade;
--
-- drop table if exists instructors cascade;
--
-- drop table if exists task_answers cascade;
--
-- drop table if exists tasks cascade;
--
-- drop table if exists question_answers_option cascade;
--
-- drop table if exists option_tests cascade;
--
-- drop table if exists question_answers cascade;
--
-- drop table if exists questions cascade;
--
-- drop table if exists test_answers cascade;
--
-- drop table if exists tests cascade;
--
-- drop table if exists students cascade;
--
-- drop table if exists users cascade;
--
-- drop table if exists video_lessons cascade;
--
-- drop table if exists lessons cascade;
--
-- drop table if exists courses cascade;
--
-- drop table if exists groups cascade;
--
-- drop table if exists flyway_schema_history cascade;
--
--
-- create sequence if not exists course_seq
--     start with 4;
--
-- create sequence if not exists group_seq
--     start with 4;
--
-- create sequence if not exists instructor_seq
--     start with 3;
--
-- create sequence if not exists lesson_seq
--     start with 4;
--
-- create sequence if not exists link_seq
--     start with 4;
--
-- create sequence if not exists option_test_seq
--     start with 4;
--
-- create sequence if not exists presentation_seq
--     start with 4;
--
-- create sequence if not exists question_seq
--     start with 4;
--
-- create sequence if not exists student_seq
--     start with 4;
--
-- create sequence if not exists task_seq
--     start with 4;
--
-- create sequence if not exists task_answer_seq
--     start with 4;
--
-- create sequence if not exists test_seq
--     start with 4;
--
-- create sequence if not exists test_answer_seq
--     start with 4;
--
-- create sequence if not exists user_seq
--     start with 7;
--
-- create sequence if not exists video_lesson_seq
--     start with 4;
--
-- create table if not exists groups
-- (
--     create_date      date,
--     date_of_graduate date,
--     id               bigint       not null
--         primary key,
--     description      varchar(255),
--     group_name       varchar(255) not null
--         unique,
--     image            varchar(255)
-- );
--
-- create table if not exists courses
-- (
--     date               date,
--     date_of_graduation date,
--     group_id           bigint
--         constraint fkqeed8jx354sfrloky6s2iu6wa
--             references groups,
--     id                 bigint not null
--         primary key,
--     course_name        varchar(255),
--     description        varchar(255),
--     image              varchar(255)
-- );
--
-- create table if not exists lessons
-- (
--     course_id   bigint
--         constraint fk17ucc7gjfjddsyi0gvstkqeat
--             references courses,
--     id          bigint not null
--         primary key,
--     lesson_name varchar(255)
-- );
--
-- create table if not exists links
-- (
--     id        bigint not null
--         primary key,
--     lesson_id bigint
--         constraint fkjirjvys02hemawim2y7vu6t4n
--             references lessons,
--     link      varchar(255),
--     text      varchar(255)
-- );
--
-- create table if not exists presentations
-- (
--     id            bigint not null
--         primary key,
--     lesson_id     bigint
--         constraint fkbegxylp5clxjks0g69uwa82dw
--             references lessons,
--     description   varchar(255),
--     link_file_ppt varchar(255),
--     name          varchar(255)
-- );
--
-- create table if not exists tasks
-- (
--     deadline  date,
--     id        bigint not null
--         primary key,
--     lesson_id bigint
--         constraint fkij2nuclq3hx0l5tanobfknbpw
--             references lessons,
--     file_name varchar(255),
--     task_name varchar(255),
--     text      varchar(255)
-- );
--
-- create table if not exists tests
-- (
--     is_enable boolean not null,
--     id        bigint  not null
--         primary key,
--     lesson_id bigint
--         constraint fka9ekvwmlio4eibo0hcwntdjxf
--             references lessons,
--     name      varchar(255)
-- );
--
-- create table if not exists questions
-- (
--     id            bigint not null
--         primary key,
--     test_id       bigint
--         constraint fkoc6xkgj16nhyyes4ath9dyxxw
--             references tests,
--     question_name varchar(255),
--     question_type varchar(255)
--         constraint questions_question_type_check
--             check ((question_type)::text = ANY
--                    ((ARRAY ['SINGLE'::character varying, 'MULTIPLE'::character varying])::text[]))
-- );
--
-- create table if not exists option_tests
-- (
--     is_true     boolean not null,
--     id          bigint  not null
--         primary key,
--     question_id bigint
--         constraint fkm86myxx5fy6tgm8o1x27fbvby
--             references questions,
--     option      varchar(255)
-- );
--
-- create table if not exists users
-- (
--     id           bigint not null
--         primary key,
--     email        varchar(255),
--     first_name   varchar(255),
--     last_name    varchar(255),
--     password     varchar(255),
--     phone_number varchar(255),
--     role         varchar(255)
--         constraint users_role_check
--             check ((role)::text = ANY
--                    ((ARRAY ['ADMIN'::character varying, 'INSTRUCTOR'::character varying, 'STUDENT'::character varying])::text[]))
-- );
--
-- create table if not exists instructors
-- (
--     id             bigint not null
--         primary key,
--     user_id        bigint
--         unique
--         constraint fkds2m3jgxj98sd5mr1qw23ecjp
--             references users,
--     specialization varchar(255)
-- );
--
-- create table if not exists courses_instructors
-- (
--     courses_id     bigint not null
--         constraint fkctiygp7sm3rfoc7r4vtt5xvqv
--             references courses,
--     instructors_id bigint not null
--         constraint fkhhxcr97jdw97s661pd6h03t63
--             references instructors
-- );
--
-- create table if not exists students
-- (
--     is_blocked   boolean not null,
--     group_id     bigint
--         constraint fkmsev1nou0j86spuk5jrv19mss
--             references groups,
--     id           bigint  not null
--         primary key,
--     user_id      bigint
--         unique
--         constraint fkdt1cjx5ve5bdabmuuf3ibrwaq
--             references users,
--     study_format varchar(255)
--         constraint students_study_format_check
--             check ((study_format)::text = ANY
--                    ((ARRAY ['ONLINE'::character varying, 'OFFLINE'::character varying])::text[]))
-- );
--
-- create table if not exists task_answers
-- (
--     task_answer_status smallint
--         constraint task_answers_task_answer_status_check
--             check ((task_answer_status >= 0) AND (task_answer_status <= 1)),
--     id                 bigint not null
--         primary key,
--     student_id         bigint
--         constraint fko9ulgsp51vtw8537vrs1kmb6c
--             references students,
--     task_id            bigint
--         constraint fkb90litdqxa7f0mgsw7fcklq6w
--             references tasks,
--     comment            varchar(255),
--     file               varchar(255),
--     text               varchar(255)
-- );
--
-- create table if not exists test_answers
-- (
--     correct     integer          not null,
--     in_correct  integer          not null,
--     is_accepted boolean          not null,
--     point       double precision not null,
--     id          bigint           not null
--         primary key,
--     student_id  bigint
--         constraint fktkxax7cqivjppd0ttiy79e8j7
--             references students,
--     test_id     bigint
--         constraint fkcnatg95e7uviyw3nadyh6q5ot
--             references tests,
--     test_name   varchar(255)
-- );
--
-- create table if not exists question_answers
-- (
--     point          double precision not null,
--     id             bigint           not null
--         primary key,
--     question_id    bigint
--         constraint fkrms3u35c10orgjqyw03ajd7x7
--             references questions,
--     test_answer_id bigint
--         constraint fk1d1l1olfrfq7c0h47w15ao80p
--             references test_answers
-- );
--
-- create table if not exists question_answers_option
-- (
--     option_id          bigint not null
--         unique
--         constraint fk6w8m9m7oljtqqfca8fmg0dkc3
--             references option_tests,
--     question_answer_id bigint not null
--         constraint fkeg0bsbu3qam9acg4i0tt19w8u
--             references question_answers
-- );
--
-- create table if not exists video_lessons
-- (
--     id          bigint not null
--         primary key,
--     lesson_id   bigint
--         constraint fk1iolcxyvtxxsyua3s2g3lqsqh
--             references lessons,
--     description varchar(255),
--     link        varchar(255),
--     name        varchar(255)
-- );
