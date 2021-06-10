INSERT INTO "itinerary" (name, status, public_visibility, city_id, hours_per_day, start_day, user_id) VALUES ('Travel To Rome', 'DRAFT', true, 5774, 8, '2021-06-04 08:00:00', 2);

INSERT INTO "itinerary_day" (itinerary_id) VALUES (1);
INSERT INTO "itinerary_day" (itinerary_id) VALUES (1);

INSERT INTO "itinerary_attraction_day" (itinerary_day_id, tourist_attraction_id) VALUES (1, 2306);
INSERT INTO "itinerary_attraction_day" (itinerary_day_id, tourist_attraction_id) VALUES (1, 2304);
INSERT INTO "itinerary_attraction_day" (itinerary_day_id, tourist_attraction_id) VALUES (1, 74);
INSERT INTO "itinerary_attraction_day" (itinerary_day_id, tourist_attraction_id) VALUES (1, 1163);
INSERT INTO "itinerary_attraction_day" (itinerary_day_id, tourist_attraction_id) VALUES (1, 2295);

INSERT INTO "itinerary_attraction_day" (itinerary_day_id, tourist_attraction_id) VALUES (2, 676);
INSERT INTO "itinerary_attraction_day" (itinerary_day_id, tourist_attraction_id) VALUES (2, 77);
INSERT INTO "itinerary_attraction_day" (itinerary_day_id, tourist_attraction_id) VALUES (2, 66);
INSERT INTO "itinerary_attraction_day" (itinerary_day_id, tourist_attraction_id) VALUES (2, 2297);
INSERT INTO "itinerary_attraction_day" (itinerary_day_id, tourist_attraction_id) VALUES (2, 172);
INSERT INTO "itinerary_attraction_day" (itinerary_day_id, tourist_attraction_id) VALUES (2, 88);

INSERT INTO "post" (author_id, body, created_at) VALUES (2, 'I just planned a trip to Rome!', '2021-06-04 20:38:05');
INSERT INTO "comment" (author_id, post_id, body, created_at) VALUES (5, 1, 'Very nice! I will go to Rome too one day!', '2021-06-04 21:10:05');
INSERT INTO "comment" (author_id, post_id, body, created_at) VALUES (3, 1, 'Good job!', '2021-06-05 11:54:05');