use wooddatabase;
drop table if exists woodstock;

create table woodstock(
id           VARCHAR(4),
title        VARCHAR(64),
price        FLOAT,
description  VARCHAR(128),
inventory    INT
);

INSERT INTO woodstock VALUES('101','Plank 1',56,'random small piece of wood 1',10);
INSERT INTO woodstock VALUES('102','Plankton',60,'WHO LIVE IN A PINEAPPLE UNDER THE SEE', 30);
INSERT INTO woodstock VALUES('103','Plank 2',80,'random big wood', 80);
INSERT INTO woodstock VALUES('105','Eddie plank',100,'a plank with a smile', 3000);
INSERT INTO woodstock VALUES('109','Derp Plank',892,'WORLD 1ST PLANK THAT DERPS', 3023);
INSERT INTO woodstock VALUES('120','WALK PLANK',999,'Walk plank walk', 678);
INSERT INTO woodstock VALUES('121','Plankton 3.0',647,'a plank that modifies itself', 780);
INSERT INTO woodstock VALUES('140','BURN Plank',10,'USED for bbq', 10000);
INSERT INTO woodstock VALUES('143','BURN Plank 10.0',100,'EXtra burn extra fast extra taste', 90000);
INSERT INTO woodstock VALUES('180','Ninja Plank',80,'IT just goes missing', 899);

select* from woodstock;