CREATE SCHEMA IF NOT EXISTS edge_tree;

CREATE TABLE IF NOT EXISTS edge_tree.edge (
                                              from_id INT NOT NULL,
                                              to_id INT NOT NULL,
                                              PRIMARY KEY (from_id, to_id)
    );


INSERT INTO edge_tree.edge (from_id, to_id) VALUES
                                                (1, 2), (1, 3), (2, 4), (3, 5),(2,6),(3,7);