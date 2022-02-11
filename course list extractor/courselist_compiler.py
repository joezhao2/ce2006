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

# string_list = ['']
# bracket_depth = 0
# prev1_bracket_depth = 0
# prev2_bracket_depth = 0
# for filename in os.listdir(path):
# 	with open(os.path.join(path, filename), 'r') as f:
# 		for line in f:
# 			for char in line:
# 				if char == '<':
# 					bracket_depth += 1
# 				elif char == '>':
# 					bracket_depth -= 1
# 				else:

# 					if not bracket_depth:
# 						if prev2_bracket_depth == 1:
# 							string_list.append('')
# 						string_list[len(string_list) - 1] = string_list[len(string_list) - 1] + char
# 				prev2_bracket_depth = prev1_bracket_depth
# 				prev1_bracket_depth = bracket_depth

# def filter_enter(char):
# 	return not char == '\n' and not char == '\n\n' and not char == ''

# for i, string in enumerate(string_list):
# 	if string == '\n' :
# 		string_list.pop(i)
# # string_list = list(filter(filter_enter, string_list))

# columns = [ 'course_code',
# 			'course_name',
# 			'AU',
# 			'prerequisite',
# 			'mutually_exclusive_with',
# 			'not_avail_to_program',
# 			'not_avail_to_program_with',
# 			'description']
# new_row = dict()

# state = 0
# title_state = 0
# for string in string_list:
# 	if bool(re.match("[A-Z][A-Z][0-9][0-9][0-9][0-9]", string)) and len(string) == 6:
# 		print(new_row)
# 		state = 0
# 		new_row['course_code'] = string
# 		title_state = 1
# 		continue
# 	if string == 'Prerequisite:':
# 		state = 1
# 		continue
# 	if string == 'Grade Type:':
# 		state = 2
# 		continue
# 	if string == 'Mutually exclusive with:':
# 		state = 3
# 		continue
# 	if string == 'Not available to Programme:':
# 		state = 4
# 		continue
# 	if string == 'Not available to all Programme with:':
# 		state = 5
# 		continue

# 	if state == 0:
# 		if title_state == 1:
# 			new_row['course_name'] = string
# 		elif title_state == 2:
# 			new_row['AU'] = string
# 		title_state += 1
# 	elif state == 1:
# 		try:
# 			new_row['prerequisite'] += string
# 		except:
# 			new_row['prerequisite'] = string

# print('\n\n\n\n\n\n')
# print(string_list)
courses = pd.DataFrame(courses_list)
courses.to_excel('courses.xlsx')
