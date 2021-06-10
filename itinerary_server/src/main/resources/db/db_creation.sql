/* Drop existing data */
DROP TABLE IF EXISTS "itinerary_attraction_day";

DROP TABLE IF EXISTS "itinerary_day";
DROP SEQUENCE IF EXISTS "itinerary_day_id_seq";

DROP TABLE IF EXISTS "itinerary";
DROP SEQUENCE IF EXISTS "itinerary_id_seq";

DROP TABLE IF EXISTS "follow";

DROP TABLE IF EXISTS "comment";
DROP SEQUENCE IF EXISTS "comment_id_seq";

DROP TABLE IF EXISTS "post";
DROP SEQUENCE IF EXISTS "post_id_seq";

DROP TABLE IF EXISTS "user";
DROP SEQUENCE IF EXISTS "user_id_seq";

DROP TABLE IF EXISTS "city";
DROP SEQUENCE IF EXISTS "city_id_seq";

DROP TABLE IF EXISTS "tourist_attraction";
DROP SEQUENCE IF EXISTS "tourist_attraction_id_seq";

/* Users */
CREATE SEQUENCE user_id_seq START 1;
CREATE TABLE "user" (
    id integer NOT NULL DEFAULT nextval('user_id_seq'),
    email varchar(64) UNIQUE NOT NULL,
    password varchar(250) NOT NULL,
    first_name varchar(64) NOT NULL,
    last_name varchar(64) NOT NULL,
    PRIMARY KEY(id)
);

ALTER SEQUENCE user_id_seq OWNED BY "user".id;

/* UserRelationship */
CREATE TABLE "follow" (
    sender integer NOT NULL,
    receiver integer NOT NULL,
    type varchar(65) NOT NULL,
    PRIMARY KEY(sender, receiver),
    CONSTRAINT fk_sender FOREIGN KEY (sender) REFERENCES "user"(id) ON DELETE CASCADE,
    CONSTRAINT fk_receiver FOREIGN KEY (receiver) REFERENCES "user"(id) ON DELETE CASCADE
);

/* Cities */
CREATE SEQUENCE city_id_seq START 1;
CREATE TABLE "city" (
    id integer NOT NULL DEFAULT nextval('city_id_seq'),
    name varchar(255) UNIQUE NOT NULL,
    lat DECIMAL NOT NULL,
    lon DECIMAL NOT NULL,
    province_code varchar(60) NOT NULL,
    PRIMARY KEY(id)
);

ALTER SEQUENCE city_id_seq OWNED BY "city".id;


/* Tourist Attractions */
CREATE SEQUENCE tourist_attraction_id_seq START 1;
CREATE TABLE "tourist_attraction" (
    id integer NOT NULL DEFAULT nextval('tourist_attraction_id_seq'),
    wikidata_url varchar(255) NOT NULL UNIQUE,
    name varchar(255) NOT NULL,
    description varchar(999) NOT NULL,
    image_url varchar(500),
    lat DECIMAL NOT NULL,
    lon DECIMAL NOT NULL,
    visits bigint NOT NULL DEFAULT 0,
    visit_duration_minutes INTEGER NOT NULL DEFAULT 60,
    PRIMARY KEY(id)
);

ALTER SEQUENCE tourist_attraction_id_seq OWNED BY "tourist_attraction".id;



/* Itinerary */
CREATE SEQUENCE itinerary_id_seq START 1;
CREATE TABLE "itinerary" (
    id integer NOT NULL DEFAULT nextval('itinerary_id_seq'),
    name varchar(64) NOT NULL,
    status varchar(64) NOT NULL DEFAULT 'DRAFT',
    public_visibility boolean NOT NULL DEFAULT false,
    city_id INTEGER NOT NULL,
    hours_per_day INTEGER NOT NULL DEFAULT 8,
    start_day TIMESTAMP,
    user_id integer NOT NULL,
    break_minutes integer NOT NULL DEFAULT 10,
    PRIMARY KEY(id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES "user"(id) ON DELETE CASCADE,
    CONSTRAINT fk_city FOREIGN KEY (city_id) REFERENCES "city"(id) ON DELETE CASCADE
);

ALTER SEQUENCE itinerary_id_seq OWNED BY "itinerary".id;

/* Itinerary days */
CREATE SEQUENCE itinerary_day_id_seq START 1;
CREATE TABLE "itinerary_day" (
    id integer NOT NULL DEFAULT nextval('itinerary_day_id_seq'),
    itinerary_id integer NOT NULL,
    PRIMARY KEY(id),
    CONSTRAINT fk_itinerary FOREIGN KEY (itinerary_id) REFERENCES itinerary(id) ON DELETE CASCADE
);

ALTER SEQUENCE itinerary_day_id_seq OWNED BY "itinerary_day".id;

/* Itinerary day attraction */
CREATE TABLE "itinerary_attraction_day" (
    itinerary_day_id integer NOT NULL,
    tourist_attraction_id integer NOT NULL,
    PRIMARY KEY(itinerary_day_id, tourist_attraction_id),
    CONSTRAINT fk_itinerary FOREIGN KEY (itinerary_day_id) REFERENCES itinerary_day(id),
    CONSTRAINT fk_tourist_attraction FOREIGN KEY (tourist_attraction_id) REFERENCES tourist_attraction(id)
);

/* Social posts */
CREATE SEQUENCE post_id_seq START 1;
CREATE TABLE "post" (
    id integer NOT NULL DEFAULT nextval('post_id_seq'),
    author_id integer NOT NULL,
    body text NOT NULL,
    created_at TIMESTAMP DEFAULT current_timestamp,
    PRIMARY KEY(id),
    CONSTRAINT fk_author FOREIGN KEY (author_id) REFERENCES "user"(id) ON DELETE CASCADE
);
ALTER SEQUENCE post_id_seq OWNED BY "post".id;

/* Social comments */
CREATE SEQUENCE comment_id_seq START 1;
CREATE TABLE "comment" (
    id integer NOT NULL DEFAULT nextval('comment_id_seq'),
    author_id integer NOT NULL,
    post_id integer NOT NULL,
    body text NOT NULL,
    created_at TIMESTAMP DEFAULT current_timestamp,
    PRIMARY KEY(id),
    CONSTRAINT fk_author FOREIGN KEY (author_id) REFERENCES "user"(id) ON DELETE CASCADE,
    CONSTRAINT fk_post FOREIGN KEY (post_id) REFERENCES post(id) ON DELETE CASCADE
);
ALTER SEQUENCE comment_id_seq OWNED BY "comment".id;

/* Distance calculation function */
DROP FUNCTION IF EXISTS calculate_distance(decimal, decimal, decimal, decimal, varchar);
CREATE OR REPLACE FUNCTION calculate_distance(lat1 DECIMAL, lon1 DECIMAL, lat2 DECIMAL, lon2 DECIMAL, units varchar)
RETURNS DECIMAL AS '
    DECLARE
        dist DECIMAL = 0;
        radlat1 DECIMAL;
        radlat2 DECIMAL;
        theta DECIMAL;
        radtheta DECIMAL;
    BEGIN
        IF lat1 = lat2 OR lon1 = lon2
            THEN RETURN dist;
        ELSE
            radlat1 = pi() * lat1 / 180;
            radlat2 = pi() * lat2 / 180;
            theta = lon1 - lon2;
            radtheta = pi() * theta / 180;
            dist = sin(radlat1) * sin(radlat2) + cos(radlat1) * cos(radlat2) * cos(radtheta);

            IF dist > 1 THEN dist = 1; END IF;

            dist = acos(dist);
            dist = dist * 180 / pi();
            dist = dist * 60 * 1.1515;

            IF units = ''K'' THEN dist = dist * 1.609344; END IF;
            IF units = ''N'' THEN dist = dist * 0.8684; END IF;

            RETURN dist;
        END IF;
    END;
' LANGUAGE plpgsql;