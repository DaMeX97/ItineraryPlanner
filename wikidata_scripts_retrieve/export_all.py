import glob,os

program_list = ['export_churches.py', 'export_monuments.py', 'export_museums.py', 'export_squares.py', 'export_theaters.py', 'export_tourist_attractions.py', 'export_zoo.py']

os.chdir(".")  # locate ourselves in the directory
for script in sorted(program_list):
    with open(script) as f:
       contents = f.read()
    exec(contents)
    print("Finished:" + script)