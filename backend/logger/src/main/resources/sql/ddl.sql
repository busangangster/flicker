create table movie_average_rating
(
    movie_seq          int    not null
        primary key,
    movie_count        int    not null,
    movie_total_rating double not null
);

create table movie_review_info
(
    id           bigint auto_increment
        primary key,
    user_seq     bigint                               not null,
    review_seq   bigint                               not null,
    movie_seq    bigint                               not null,
    rating       double                               not null,
    type         varchar(50)                          not null,
    action       varchar(50)                          not null,
    timestamp    timestamp  default CURRENT_TIMESTAMP null,
    is_deleted   tinyint(1) default 0                 null,
    is_processed tinyint(1) default 0                 null
);

create table sentiment_review_logs
(
    review_seq      int auto_increment
        primary key,
    content         varchar(255)                         not null,
    timestamp       timestamp  default CURRENT_TIMESTAMP null,
    sentiment_score double                               null,
    is_processed    tinyint(1) default 0                 null,
    is_delete       tinyint(1) default 0                 null,
    updated_at      timestamp                            null
);

create table user_action_logs
(
    user_seq  int                                 not null,
    keyword   varchar(255)                        null,
    action    varchar(50)                         null,
    timestamp timestamp default CURRENT_TIMESTAMP null
);

