SELECT DISTINCT student_name AS "Name", SUM(units) AS "Units_complete"
FROM Student_registration JOIN Courses ON Student_registration.course = Courses.course
WHERE student_name in (SELECT DISTINCT student_name
						FROM Student_registration
						WHERE student_name NOT IN (SELECT DISTINCT student_name
													FROM Student_registration
													WHERE status = 'IP'))
GROUP BY student_name
HAVING SUM(units) > 10