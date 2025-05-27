-- assets_stt_jobs
create table assets_stt_jobs
(
    id                 bigint                        not null primary key,
    asset_id           bigint                        not null,
    status             varchar(20) default 'WAITING' not null,
    source_path        varchar(1000)                 not null,
    audio_path         varchar(1000),
    created_date       timestamp   default now()     not null,
    last_modified_date timestamp
);

-- assets_stt_speakers
create table assets_stt_speakers
(
    id                 bigint                  not null primary key,
    job_id             bigint                  not null,
    speaker            varchar(20)             not null,
    speaker_name       varchar(100),
    created_date       timestamp default now() not null,
    last_modified_date timestamp,

    foreign key (job_id) references assets_stt_jobs (id)
);

-- assets_stt_speaker_times
create table assets_stt_speakers_times
(
    id                 bigint                  not null primary key,
    speaker_id         bigint                  not null,
    start_time         real                    not null,
    end_Time           real                    not null,
    created_date       timestamp default now() not null,
    last_modified_date timestamp,

    foreign key (speaker_id) references assets_stt_speakers (id)
);


-- assets_stt_speaker_texts
create table assets_stt_speakers_texts
(
    id                 bigint                  not null primary key,
    job_id             bigint                  not null,
    speaker_id         bigint                  not null,
    start_time         real                    not null,
    end_time           real                    not null,
    created_date       timestamp default now() not null,
    last_modified_date timestamp,


    foreign key (job_id) references assets_stt_jobs (id),
    foreign key (speaker_id) references assets_stt_speakers (id)
);

