-- Inserimento utenti
INSERT INTO users (id, name, surname, email, state) VALUES (nextval('users_seq'),'Emanuele', 'Gottardelli', 'ema.gotta@ex.it', 'ACTIVE')
INSERT INTO users (id,name, surname, email, state) VALUES (nextval('users_seq'),'Franca', 'Verdi', 'franca.v1998@ex.com', 'BANNED');

-- Inserimento credenziali
INSERT INTO credentials (id,username, password, role, user_id) VALUES (nextval('credentials_seq'),'SuperAdmin', '$2b$12$sMe4cEfpceel5ijYC5gWh.m.tMs0rT2pOCtvj99B11GzNg7Fara/C', 'ADMIN_ROLE', 1);
INSERT INTO credentials (id,username, password, role, user_id) VALUES (nextval('credentials_seq'),'Franca', '$2b$12$sMe4cEfpceel5ijYC5gWh.m.tMs0rT2pOCtvj99B11GzNg7Fara/C', 'DEFAULT_ROLE', 51);

-- Inserimento ricette
INSERT INTO recipe (id, title, description, prep_time, difficulty, category, procedimento, author_id,entry_date) VALUES (nextval('recipe_seq'),'Muffin', 'Deliziosi dolci con gocce di cioccolato fondente', '35', '1','Dolci','Per preparare i muffin con gocce di cioccolato il burro va lasciato almeno 1 h fuori dal frigo, per farlo ammorbidire.  Unite al burro morbido lo zucchero, lavorate con le fruste elettriche fino ad ottenere un composto spumoso e cremoso. Dopodiché incidete una bacca di vaniglia e raschiate i semi utilizzando il dorso di un coltello. Versate questi ultimi nella ciotola con burro e zucchero. Azionate nuovamente le fruste e aggiungete le uova, anche queste a temperatura ambiente, una alla volta in questo modo gli ingredienti non si slegheranno. Ora setacciate la farina, il lievito per dolci e il bicarbonato direttamente nella ciotola con il composto.Aggiungete anche il pizzico di sale e azionate nuovamente le fruste per inglobate le polveri. Noterete che l’impasto diventerà più consistente quindi stemperatelo con il latte a temperatura ambiente versato a filo. A questo punto il composto sarà morbido e compatto. Unite 80 g di gocce di cioccolato e mescolate con una spatola per inglobarle dopodiché trasferite il composto in una sàc-a-poche usa e getta senza bocchetta in questo modo potrete effettuare un lavoro più pulito altrimenti utilizzate pure un cucchiaio. Sistemate i pirottini di carta in una pirofila da muffin e riempiteli per 2/3, quindi lasciando meno di un centimetro dalla superficie. Ogni muffin dovrà pesare all’incirca 70 g.Sulle tortine versate le gocce di cioccolato rimanenti e cuocete in forno preriscaldato a 180° per 18-20 minuti in modalità statica. A questo punto i vostri muffin con gocce di cioccolato sono pronti per essere gustati.', 51,'03-02-2026');

-- Inserimento ingredienti
INSERT INTO ingredient (id, name, quantity, unit_measurement, recipe_id) VALUES (nextval('ingredient_seq'), 'Burro', '125', 'g', 1);
INSERT INTO ingredient (id, name, quantity, unit_measurement, recipe_id) VALUES (nextval('ingredient_seq'), 'Zucchero', '135', 'g', 1);
INSERT INTO ingredient (id, name, quantity, unit_measurement, recipe_id) VALUES (nextval('ingredient_seq'), 'Uova', '2', 'medie', 1);
INSERT INTO ingredient (id, name, quantity, unit_measurement, recipe_id) VALUES (nextval('ingredient_seq'), 'Baccello di vaniglia', '1', ' ', 1);
INSERT INTO ingredient (id, name, quantity, unit_measurement, recipe_id) VALUES (nextval('ingredient_seq'), 'Sale fino', '1', 'pizzico', 1);
INSERT INTO ingredient (id, name, quantity, unit_measurement, recipe_id) VALUES (nextval('ingredient_seq'), 'Farina 00', '265', 'g', 1);
INSERT INTO ingredient (id, name, quantity, unit_measurement, recipe_id) VALUES (nextval('ingredient_seq'), 'Latte intero', '135', 'g', 1);
INSERT INTO ingredient (id, name, quantity, unit_measurement, recipe_id) VALUES (nextval('ingredient_seq'), 'Gocce di cioccolato fondente', '100', 'g', 1);
INSERT INTO ingredient (id, name, quantity, unit_measurement, recipe_id) VALUES (nextval('ingredient_seq'), 'Bicarbonato', '1', 'cucchiaino', 1);
INSERT INTO ingredient (id, name, quantity, unit_measurement, recipe_id) VALUES (nextval('ingredient_seq'), 'Lievito in polvere per dolci', '10', 'g', 1);
