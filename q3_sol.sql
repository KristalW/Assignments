SELECT name,bonus
FROM Employee left JOIN Bonus ON Employee.empId=Bonus.empId
WHERE bonus is NULL or bonus < 1000
Order by name desc;
