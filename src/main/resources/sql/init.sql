CREATE TABLE IF NOT EXISTS app_role (
	id SERIAL PRIMARY KEY,
	role_name TEXT UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS app_user (
	id SERIAL PRIMARY KEY,
	username TEXT UNIQUE NOT NULL,
	role_id INTEGER REFERENCES app_role (id)
);

CREATE TABLE IF NOT EXISTS item (
	id SERIAL PRIMARY KEY,
	name TEXT UNIQUE NOT NULL,
	display_name TEXT UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS chest_casino (
	id SERIAL PRIMARY KEY,
	user_id INTEGER REFERENCES app_user (id),
	last_opened_time TIMESTAMP,
	last_received_item INTEGER REFERENCES item (id)
);

CREATE TABLE IF NOT EXISTS trade_offer (
	id SERIAL PRIMARY KEY,
	user_id INTEGER REFERENCES app_user (id)
);

CREATE TABLE IF NOT EXISTS trade_offer_items_wanted (
	offer_id INTEGER REFERENCES trade_offer (id),
	item_id INTEGER REFERENCES item (id),
	count INTEGER NOT NULL,
	PRIMARY KEY (offer_id, item_id),
	CHECK (count > 0)
);

CREATE TABLE IF NOT EXISTS trade_offer_items_offered (
	offer_id INTEGER REFERENCES trade_offer (id),
	item_id INTEGER REFERENCES item (id),
	count INTEGER NOT NULL,
	PRIMARY KEY (offer_id, item_id),
	CHECK (count > 0)	
);

CREATE TABLE IF NOT EXISTS recipe (
	id SERIAL PRIMARY KEY,
	result_item_id INTEGER REFERENCES item (id),
	result_item_cnt INTEGER NOT NULL,
	LT_ID INTEGER REFERENCES item (id),
	LC_ID INTEGER REFERENCES item (id),
	LB_ID INTEGER REFERENCES item (id),
	CT_ID INTEGER REFERENCES item (id),
	CC_ID INTEGER REFERENCES item (id),
	CB_ID INTEGER REFERENCES item (id),
	RT_ID INTEGER REFERENCES item (id),
	RC_ID INTEGER REFERENCES item (id),
	RB_ID INTEGER REFERENCES item (id),
	CHECK (result_item_cnt > 0)
);

CREATE TABLE IF NOT EXISTS recipe_created_by (
	user_id INTEGER REFERENCES app_user (id),
	recipe_id INTEGER REFERENCES recipe (id),
	PRIMARY KEY (user_id, recipe_id)
);

CREATE TABLE IF NOT EXISTS user_items (
	user_id INTEGER REFERENCES app_user (id),
	item_id INTEGER REFERENCES item (id),
	count INTEGER NOT NULL,
	PRIMARY KEY (user_id, item_id),
	CHECK (count > 0)
);
