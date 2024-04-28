INSERT INTO account (id, email, hash_password, full_name)
VALUES ('476e2e64-048c-11ee-be56-0242ac120002',
        'ADMIN@ADMIN.COM',
        '$2a$10$6uYcNCsaUVIiifFes83yd.lxeaujBbF/QnE/8p4vlD3k2NeuHBYDe',
        'ADMIN ADMINOV');

INSERT INTO roles (account_id, role)
VALUES('476e2e64-048c-11ee-be56-0242ac120002', 'SUPER_ADMIN');