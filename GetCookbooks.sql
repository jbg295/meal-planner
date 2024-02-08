use mealplanning;

DROP PROCEDURE IF EXISTS GetCookbooks;
Delimiter $$
CREATE PROCEDURE GetCookbooks()
BEGIN
SELECT CookbookName
FROM Cookbook;
END$$
Delimiter ;