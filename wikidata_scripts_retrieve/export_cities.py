# Python program to read 
# json file 
  
  
import json 
  
# Opening JSON file 
f = open('cities.json',) 
  
# returns JSON object as  
# a dictionary 
data = json.load(f)

f = open("cities.sql", "w" , encoding='utf-8')
  
# Iterating through the json 
# list 
for result in data['results']: 
    insert_string = ''

    insert_string += "INSERT INTO city(name, lat, lon, province_code) VALUES ("
    insert_string += "'" + result['Place_Name'].replace("'", "''") + "',"
    insert_string += "" + str(result['Latitude']) + ","
    insert_string += "" + str(result['Longitude']) + ","
    insert_string += "'" + result['Admin_Code2'].replace("'", "''") + "') ON CONFLICT DO NOTHING;\n"

    f.write(insert_string)
  
# Closing file 
f.close() 