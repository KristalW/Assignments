SELECT Chef
FROM Menu JOIN ChefSkill ON Menu.Dish=ChefSkill.Dish
GROUP BY Chef
HAVING Count(Chef)=(SELECT COUNT(*)
                   FROM Menu);