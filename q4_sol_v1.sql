SELECT Chef
FROM ChefSkill
WHERE Dish IN(SELECT *
              FROM Menu)
GROUP BY Chef
HAVING COUNT(*) = (SELECT COUNT(*)
					 	FROM Menu);