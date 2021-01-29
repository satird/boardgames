create sequence hibernate_sequence start 1 increment 1;

create table message
    (id int8 not null,
    tag varchar(255),
    text varchar(2048) not null,
    user_id int8,
    primary key (id));

create table user_role
    (user_id int8 not null,
    roles varchar(255));

create table usr
    (id int8 not null,
    activation_code varchar(255),
    active boolean not null,
    email varchar(255),
    password varchar(255) not null,
    username varchar(255) not null,
    locale varchar(255),
    userpic varchar(255),
    last_visit timestamp without time zone,
    google_id varchar(255),
    auth_provider varchar(255),
    created_time timestamp without time zone,
    primary key (id));

create table comment
    (id int8 not null,
    text varchar(2048) not null,
    user_id int8 not null,
    bgg_id int4 not null,
    creation_date timestamp without time zone,
    primary key (id));

create table bgg
    (bgg_id int8 not null,
    type_game text,
    id_game int4,
    thumbnail text,
    image text,
    primary_name text,
    alternate text,
    description text,
    yearpublished int4,
    minplayers int4,
    maxplayers int4,
    suggested_num_players text,
    suggested_playerage text,
    suggested_language_dependence text,
    playingtime int4,
    minplaytime int4,
    maxplaytime int4,
    minage int4,
    boardgamecategory text,
    boardgamemechanic text,
    boardgamefamily text,
    boardgameexpansion text,
    boardgameimplementation text,
    boardgamedesigner text,
    boardgameartist text,
    boardgamepublisher text,
    usersrated double precision,
    average double precision,
    baysaverage double precision,
    board_game_rank int4,
    strategy_game_runk int4,
    family_game_runk int4,
    stddev double precision,
    median int4,
    owned_num int4,
    trading int4,
    wanting int4,
    wishing int4,
    numcomments int4,
    numweights int4,
    averageweight double precision,
    boardgameintegration text,
    boardgamecompilation text,
    party_game_rank int4,
    abstract_game_runk int4,
    thematic_rank int4,
    war_game_rank int4,
    childrens_game_runk int4,
    rpg_item_runk int4,
    accessory_rank int4,
    amiga_rank int4,
    commodore_64_rank int4,
    arcade_rank int4,
    atary_st_rank int4,
    primary key (bgg_id));

alter table if exists message
    add constraint message_user_fk
    foreign key (user_id) references usr;

alter table if exists user_role
    add constraint user_role_user_fk
    foreign key (user_id) references usr;

alter table if exists comment
    add constraint comment_user_fk
    foreign key (user_id) references usr,
    add constraint comment_bgg_fk
    foreign key (bgg_id) references bgg;