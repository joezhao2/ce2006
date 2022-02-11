# For downloading html page for all the courses to the 'html_pages' folder

import requests

request_codes = dict()

# Extracting programme codes to a usable format
for line in open('codes.txt', 'r'):
	request_code = line[24:].split("\"")
	request_code[1] = request_code[1][1:-1]
	if request_code[0] == '':
		continue
	request_codes[request_code[0]] = request_code[1]


terms = ['1', '2', 'S', 'T']

# Request URL
url = 'https://wis.ntu.edu.sg/webexe/owa/AUS_SUBJ_CONT.main_display1'

# JSON for parseing request
request = {'acadsem': '2021_2', 'r_course_yr': 'ACC;GB;3;F', 'r_subj_code':'Enter Keywords or Course Code', 'boption': 'CLoad', 'acad': '2021', 'semester': '2'}

# Requesting data from the server and saving them into html files
for year in range(2017, 2022):
	for term in terms:
		request['acadsem'] = str(year) + '_' + term
		request['acad'] = str(year)
		request['semester'] = term
		for key in request_codes:
			request['r_course_yr'] = key

			response = requests.post(url, data = request)

			print(year, term, request_codes[key])

			with open('html_pages/' + request_codes[key] + " " + str(year) + '_' + term + '.html', 'w') as f:
				f.write(response.text)
			# exit(0)