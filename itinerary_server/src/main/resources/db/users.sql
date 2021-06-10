INSERT INTO "user" (email,password,first_name,last_name) VALUES ('a@a.it','$2a$10$SjeWmCbtx15FayazKZmW4eY/BdPlQZhchBsqdSH7J.xmhiimZDrum','Alex','Dametto');
INSERT INTO "user" (email,password,first_name,last_name) VALUES ('a2@a.it','$2a$10$SjeWmCbtx15FayazKZmW4eY/BdPlQZhchBsqdSH7J.xmhiimZDrum','Mario','Rossi');
INSERT INTO "user" (email,password,first_name,last_name) VALUES ('a3@a.it','$2a$10$SjeWmCbtx15FayazKZmW4eY/BdPlQZhchBsqdSH7J.xmhiimZDrum','Luigi','Ferrero');
INSERT INTO "user" (email,password,first_name,last_name) VALUES ('a4@a.it','$2a$10$SjeWmCbtx15FayazKZmW4eY/BdPlQZhchBsqdSH7J.xmhiimZDrum','Marco','Bianco');
INSERT INTO "user" (email,password,first_name,last_name) VALUES ('a5@a.it','$2a$10$SjeWmCbtx15FayazKZmW4eY/BdPlQZhchBsqdSH7J.xmhiimZDrum','Luca','Marino');


INSERT INTO "follow" (sender,receiver,type) VALUES (1,2,'FR');
INSERT INTO "follow" (sender,receiver,type) VALUES (1,3,'ACCEPTED');
INSERT INTO "follow" (sender,receiver,type) VALUES (1,5,'ACCEPTED');
INSERT INTO "follow" (sender,receiver,type) VALUES (2,1,'ACCEPTED');
INSERT INTO "follow" (sender,receiver,type) VALUES (2,4,'FR');
INSERT INTO "follow" (sender,receiver,type) VALUES (3,5,'ACCEPTED');
INSERT INTO "follow" (sender,receiver,type) VALUES (4,1,'FR');
INSERT INTO "follow" (sender,receiver,type) VALUES (5,1,'ACCEPTED');
INSERT INTO "follow" (sender,receiver,type) VALUES (5,2,'FR');