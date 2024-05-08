INSERT INTO reservation_time (start_at)
VALUES ('10:00:00'), -- 삭제 가능
       ('11:00:00');

INSERT INTO theme (name, description, thumbnail)
VALUES ('이름1', '설명1', '썸네일1'), -- 삭제 가능
       ('이름2', '설명2', '썸네일2');

INSERT INTO reservation (name, date, time_id, theme_id, created_at)
VALUES ('칸쵸와 알파고', FORMATDATETIME(TIMESTAMPADD(DAY, -1, CURRENT_TIMESTAMP()), 'yyyy-MM-dd'), 2, 2, TIMESTAMPADD(DAY, -2, CURRENT_TIMESTAMP()));
