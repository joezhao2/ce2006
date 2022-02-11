import os
import re
import pandas as pd
path = os.path.join(os.getcwd() + '\\html_pages')

course_cycle = 0
prereq_cycle = 0

columns = [ 'course_code',
			'course_name',
			'AU',
			'prerequisite',
			'mutually_exclusive_with',
			'not_avail_to_program',
			'not_avail_to_program_with',
			'description']
courses = pd.DataFrame(columns = columns)
courses_list = []

file_count = 1

# Opens the saved html pages and extract all the course codes and saving them in .xlsx format
new_row = dict()
for filename in os.listdir(path):
	with open(os.path.join(path, filename), 'r') as f:
		for line in f:

			# Searches for course code, course title, and AU
			matches = re.findall('<FONT\s*SIZE=2\s*COLOR=#0000FF>', line)
			if matches != []:
				if re.findall('<FONT\s*SIZE=2\s*COLOR=#0000FF>[A-Za-z][A-Za-z][0-9][0-9][0-9][0-9]', line):
					courses_list.append(new_row)
					new_row = dict()
					new_row['file_name'] = filename
					# print(line[line.find(matches[0]) + 27:-17])
					new_row['course_code'] = line[line.find(matches[0]) + 27:-17]
					course_cycle = 0
				elif course_cycle == 1:
					# print(line[line.find(matches[0]) + 27:-17])
					new_row['course_name'] = line[line.find(matches[0]) + 27:-17]
				elif course_cycle == 2:
					# print(line[line.find(matches[0]) + 31:-20])
					new_row['AU'] = line[line.find(matches[0]) + 31:-20]

				course_cycle += 1
				prereq_cycle = 0

			# -------------- WIP --------------
			# Searches for prereqs
			matches = re.findall('<FONT\s*SIZE=2\s*COLOR=#FF00FF>', line)
			if matches != []:
				if prereq_cycle == 1 :
					# print(line[line.find(matches[0]) + 28:-17])
					if line[line.find(matches[0]) + 35:-17] == 'OR':
						new_row['prerequisite'] = line[line.find(matches[0]) + 28:-20] + ', '
					else:
						new_row['prerequisite'] = line[line.find(matches[0]) + 28:-17]
				elif prereq_cycle > 1:
					# print(line[line.find(matches[0]) + 28:-17])
					if line[line.find(matches[0]) + 35:-17] == 'OR':
						new_row['prerequisite'] += line[line.find(matches[0]) + 28:-20] + ', '
					else:
						new_row['prerequisite'] += line[line.find(matches[0]) + 28:-17]
				prereq_cycle += 1

			# matches =  re.findall('<FONT\s*SIZE=2\s*COLOR=#FF00FF>', line)
			# if line.find('<B><FONT SIZE=2 COLOR=#0000FF>') != -1:
			# 	print(line[line.find('<B><FONT SIZE=2 COLOR=#0000FF>') + 30:])
		courses_list.append(new_row)
		print(file_count)
		file_count += 1

courses = pd.DataFrame(courses_list)
courses.to_excel('courses.xlsx')
