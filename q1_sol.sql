SELECT DISTINCT student_name, stu_id
FROM Student_registration
WHERE student_name NOT IN (SELECT DISTINCT student_name
							FROM Student_registration
							WHERE status = 'IP')
ORDER BY stu_id asc;