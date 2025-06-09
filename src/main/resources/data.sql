DO $$
    DECLARE
        i BIGINT;
        j INT;
    BEGIN
        FOR i IN 1..1000000 LOOP
                INSERT INTO orders(id, customer, version)
                VALUES (i, 'Customer ' || i, 0);
                FOR j IN 1..5 LOOP
                        INSERT INTO order_item(product, quantity, order_id)
                        VALUES ('Product ' || j, (random() * 10 + 1)::INT, i);
                    END LOOP;

            END LOOP;
    END $$;